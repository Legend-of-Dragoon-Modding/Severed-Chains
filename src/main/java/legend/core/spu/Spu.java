package legend.core.spu;

import legend.core.DebugHelper;
import legend.core.IoHelper;
import legend.core.MathHelper;
import legend.core.memory.types.ShortRef;
import legend.game.Scus94491BpeSegment_8004;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;

public class Spu implements Runnable {
  private static final Logger LOGGER = LogManager.getFormatterLogger(Spu.class);
  private static final Marker SPU_MARKER = MarkerManager.getMarker("SPU");

  private static final int NANOS_PER_TICK = 1_000_000_000 / 50;
  private static final int SAMPLES_PER_TICK = 44_100 / 50;

  private SourceDataLine sound;

  private final byte[] spuOutput = new byte[SAMPLES_PER_TICK * 4];
  private final byte[] ram = new byte[512 * 1024]; // 0x8_0000
  public final Voice[] voices = new Voice[24];

  private int mainVolumeL;
  private int mainVolumeR;
  private int reverbOutputVolumeL;
  private int reverbOutputVolumeR;
  private long keyOn;
  private long keyOff;
  private long channelFmMode;
  private long channelNoiseMode;
  private long channelReverbMode;
  private int reverbWorkAreaAddress;
  private int reverbCurrentAddress;
  private final Control control = new Control();
  private boolean reverbEnabled;
  private boolean muted = true;
  private int noiseFrequencyShift;
  private int noiseFrequencyStep;
  private ReverbConfig reverb;

  private int reverbCounter;

  private boolean running;

  public Spu() {
    try {
      this.sound = AudioSystem.getSourceDataLine(new AudioFormat(44100, 16, 2, true, false));
      this.sound.open();
      this.sound.start();
    } catch(final LineUnavailableException|IllegalArgumentException e) {
      LOGGER.error("Failed to start audio", e);
      this.sound = null;
    }

    for(int i = 0; i < this.voices.length; i++) {
      this.voices[i] = new Voice(i);
    }

    // Initialize silent loop, voices start pointing to this
    for(int i = 0x1010; i < 0x1020; i++) {
      this.ram[i] = 0x7;
    }

    for(int i = 0; i < interpolationWeights.length; i++) {
      final double pow1 = i / (double)interpolationWeights.length;
      final double pow2 = pow1 * pow1;
      final double pow3 = pow2 * pow1;

      interpolationWeights[i] = new double[] {
        0.45d * (-pow3 + 2 * pow2 - pow1),
        0.45d * (3 * pow3 - 5 * pow2 + 2),
        0.45d * (-3 * pow3 + 4 * pow2 + pow1),
        0.45d * (pow3 - pow2)
      };
    }

    for(int i = 0; i < sampleRates.length; i++) {
      sampleRates[i] = (int)Math.round(0x1000 * Math.pow(2, i / (double)sampleRates.length));
    }
  }

  @Override
  public void run() {
    this.running = true;

    long time = System.nanoTime();

    while(this.running) {
      this.tick();

      long interval = System.nanoTime() - time;

      // Failsafe if we run too far behind (also applies to pausing in IDE)
      if(interval >= NANOS_PER_TICK * 3) {
        LOGGER.warn("SPU running behind, skipping ticks to catch up");
        interval = NANOS_PER_TICK;
        time = System.nanoTime() - interval;
      }

      final int toSleep = (int)Math.max(0, NANOS_PER_TICK - interval) / 1_000_000;
      DebugHelper.sleep(toSleep);
      time += NANOS_PER_TICK;
    }
  }

  public void stop() {
    this.running = false;
  }

  private final ShortRef reverbL = new ShortRef();
  private final ShortRef reverbR = new ShortRef();

  private void tick() {
    synchronized(Spu.class) {
      int dataIndex = 0;
      for(int i = 0; i < SAMPLES_PER_TICK; i++) {
        int sumLeft = 0;
        int sumRight = 0;

        int sumLeftReverb = 0;
        int sumRightReverb = 0;

        final long edgeKeyOn = this.keyOn;
        final long edgeKeyOff = this.keyOff;
        this.keyOn = 0;
        this.keyOff = 0;

        if(edgeKeyOn != 0) {
          LOGGER.debug(SPU_MARKER, "Keying on %x", edgeKeyOn);
        }

        if(edgeKeyOff != 0) {
          LOGGER.debug(SPU_MARKER, "Keying off %x", edgeKeyOff);
        }

        this.tickNoiseGenerator();

        for(int voiceIndex = 0; voiceIndex < this.voices.length; voiceIndex++) {
          final Voice v = this.voices[voiceIndex];

          //keyOn and KeyOff are edge triggered on 0 to 1
          if((edgeKeyOn & 0x1L << voiceIndex) != 0) {
            LOGGER.debug(SPU_MARKER, "Keying on voice %d", voiceIndex);
            v.keyOn();
          }

          if((edgeKeyOff & 0x1L << voiceIndex) != 0) {
            LOGGER.debug(SPU_MARKER, "Keying off voice %d", voiceIndex);
            v.keyOff();
          }

          if(v.adsrPhase == Phase.Off) {
            v.latest = 0;
            continue;
          }

          short sample;
          if((this.channelNoiseMode & 0x1L << voiceIndex) == 0) {
            sample = this.sampleVoice(voiceIndex);
          } else {
            //Generated by tickNoiseGenerator
            sample = (short)this.noiseLevel;
          }

          //Handle ADSR Envelope
          sample = (short)(sample * v.adsrVolume >> 15);
          v.tickAdsr();

          //Save sample for possible pitch modulation
          v.latest = sample;

          //Sum each voice sample
          if(!this.muted) {
            sumLeft += sample * v.processVolume(v.volumeLeft) >> 15;
            sumRight += sample * v.processVolume(v.volumeRight) >> 15;
          }

          if((this.channelReverbMode & 0x1L << i) != 0) {
            sumLeftReverb += sample * v.processVolume(v.volumeLeft) >> 15;
            sumRightReverb += sample * v.processVolume(v.volumeRight) >> 15;
          }
        }

        if(this.reverbCounter == 0) {
          this.processReverb(this.reverbL, this.reverbR, sumLeftReverb, sumRightReverb);

          sumLeft += this.reverbL.get();
          sumRight += this.reverbR.get();
        }

        // Retail alternates processing L/R with each sample, we're just processing both every other sample
        this.reverbCounter = this.reverbCounter + 1 & 0x1;

        //Clamp sum
        sumLeft = MathHelper.clamp(sumLeft, -0x8000, 0x7fff) * (short)this.mainVolumeL >> 15;
        sumRight = MathHelper.clamp(sumRight, -0x8000, 0x7fff) * (short)this.mainVolumeR >> 15;

        //Add to samples bytes to output list
        this.spuOutput[dataIndex++] = (byte)sumLeft;
        this.spuOutput[dataIndex++] = (byte)(sumLeft >> 8);
        this.spuOutput[dataIndex++] = (byte)sumRight;
        this.spuOutput[dataIndex++] = (byte)(sumRight >> 8);
      }

      if(this.sound != null) {
        this.sound.write(this.spuOutput, 0, this.spuOutput.length);
      }
    }
  }

  public void processReverb(final ShortRef reverbL, final ShortRef reverbR, final int lInput, final int rInput) {
    final int dAPF1 = this.reverb.dApf1.get() << 3;
    final int dAPF2 = this.reverb.dApf2.get() << 3;
    final short vIIR = this.reverb.vIir.get();
    final short vCOMB1 = this.reverb.vComb1.get();
    final short vCOMB2 = this.reverb.vComb2.get();
    final short vCOMB3 = this.reverb.vComb3.get();
    final short vCOMB4 = this.reverb.vComb4.get();
    final short vWALL = this.reverb.vWall.get();
    final short vAPF1 = this.reverb.vApf1.get();
    final short vAPF2 = this.reverb.vApf2.get();
    final int mLSAME = this.reverb.mLSame.get() << 3;
    final int mRSAME = this.reverb.mRSame.get() << 3;
    final int mLCOMB1 = this.reverb.mLComb1.get() << 3;
    final int mRCOMB1 = this.reverb.mRComb1.get() << 3;
    final int mLCOMB2 = this.reverb.mLComb2.get() << 3;
    final int mRCOMB2 = this.reverb.mRComb2.get() << 3;
    final int dLSAME = this.reverb.dLSame.get() << 3;
    final int dRSAME = this.reverb.dRSame.get() << 3;
    final int mLDIFF = this.reverb.mLDiff.get() << 3;
    final int mRDIFF = this.reverb.mRDiff.get() << 3;
    final int mLCOMB3 = this.reverb.mLComb3.get() << 3;
    final int mRCOMB3 = this.reverb.mRComb3.get() << 3;
    final int mLCOMB4 = this.reverb.mLComb4.get() << 3;
    final int mRCOMB4 = this.reverb.mRComb4.get() << 3;
    final int dLDIFF = this.reverb.dLDiff.get() << 3;
    final int dRDIFF = this.reverb.dRDiff.get() << 3;
    final int mLAPF1 = this.reverb.mLApf1.get() << 3;
    final int mRAPF1 = this.reverb.mRApf1.get() << 3;
    final int mLAPF2 = this.reverb.mLApf2.get() << 3;
    final int mRAPF2 = this.reverb.mRApf2.get() << 3;
    final short vLIN = this.reverb.vLIn.get();
    final short vRIN = this.reverb.vRIn.get();

    // Input from mixer
    final int Lin = vLIN * lInput >> 15;
    final int Rin = vRIN * rInput >> 15;

    // Same side reflection L->L and R->R
    final short mlSame = this.saturateSample(((Lin + (this.loadReverb(dLSAME) * vWALL >> 15) - this.loadReverb(mLSAME - 2)) * vIIR >> 15) + this.loadReverb(mLSAME - 2));
    final short mrSame = this.saturateSample(((Rin + (this.loadReverb(dRSAME) * vWALL >> 15) - this.loadReverb(mRSAME - 2)) * vIIR >> 15) + this.loadReverb(mRSAME - 2));

    this.writeReverb(mLSAME, mlSame);
    this.writeReverb(mRSAME, mrSame);

    // Different side reflection L->R and R->L
    final short mlDiff = this.saturateSample(((Lin + (this.loadReverb(dRDIFF) * vWALL >> 15) - this.loadReverb(mLDIFF - 2)) * vIIR >> 15) + this.loadReverb(mLDIFF - 2));
    final short mrDiff = this.saturateSample(((Rin + (this.loadReverb(dLDIFF) * vWALL >> 15) - this.loadReverb(mRDIFF - 2)) * vIIR >> 15) + this.loadReverb(mRDIFF - 2));

    this.writeReverb(mLDIFF, mlDiff);
    this.writeReverb(mRDIFF, mrDiff);

    // Early echo (comb filter with input from buffer)
    short l = this.saturateSample((vCOMB1 * this.loadReverb(mLCOMB1) >> 15) + (vCOMB2 * this.loadReverb(mLCOMB2) >> 15) + (vCOMB3 * this.loadReverb(mLCOMB3) >> 15) + (vCOMB4 * this.loadReverb(mLCOMB4) >> 15));
    short r = this.saturateSample((vCOMB1 * this.loadReverb(mRCOMB1) >> 15) + (vCOMB2 * this.loadReverb(mRCOMB2) >> 15) + (vCOMB3 * this.loadReverb(mRCOMB3) >> 15) + (vCOMB4 * this.loadReverb(mRCOMB4) >> 15));

    // Late reverb APF1 (All pass filter 1 with input from COMB)
    l = this.saturateSample(l - this.saturateSample(vAPF1 * this.loadReverb(mLAPF1 - dAPF1) >> 15));
    r = this.saturateSample(r - this.saturateSample(vAPF1 * this.loadReverb(mRAPF1 - dAPF1) >> 15));

    this.writeReverb(mLAPF1, l);
    this.writeReverb(mRAPF1, r);

    l = this.saturateSample((l * vAPF1 >> 15) + this.loadReverb(mLAPF1 - dAPF1));
    r = this.saturateSample((r * vAPF1 >> 15) + this.loadReverb(mRAPF1 - dAPF1));

    // Late reverb APF2 (All pass filter 2 with input from APF1)
    l = this.saturateSample(l - this.saturateSample(vAPF2 * this.loadReverb(mLAPF2 - dAPF2) >> 15));
    r = this.saturateSample(r - this.saturateSample(vAPF2 * this.loadReverb(mRAPF2 - dAPF2) >> 15));

    this.writeReverb(mLAPF2, l);
    this.writeReverb(mRAPF2, r);

    l = this.saturateSample((l * vAPF2 >> 15) + this.loadReverb(mLAPF2 - dAPF2));
    r = this.saturateSample((r * vAPF2 >> 15) + this.loadReverb(mRAPF2 - dAPF2));

    // Output to mixer (output volume multiplied with input from APF2)
    l = this.saturateSample(l * this.reverbOutputVolumeL >> 15);
    r = this.saturateSample(r * this.reverbOutputVolumeR >> 15);

    // Saturate address
    this.reverbCurrentAddress = Math.max(this.reverbWorkAreaAddress, this.reverbCurrentAddress + 2 & 0x7_fffe);

    reverbL.set(l);
    reverbR.set(r);
  }

  public short saturateSample(final int sample) {
    if(sample < -0x8000) {
      return -0x8000;
    }

    if(sample > 0x7fff) {
      return 0x7fff;
    }

    return (short)sample;
  }

  private void writeReverb(final int addr, final short value) {
    if(!this.reverbEnabled) {
      return;
    }

    final int relative = (addr + this.reverbCurrentAddress - this.reverbWorkAreaAddress) % (0x8_0000 - this.reverbWorkAreaAddress);
    final int address = this.reverbWorkAreaAddress + relative & 0x7_fffe;

    MathHelper.set(this.ram, address, 2, value);
  }

  private short loadReverb(final int addr) {
    final int relative = (addr + this.reverbCurrentAddress - this.reverbWorkAreaAddress) % (0x8_0000 - this.reverbWorkAreaAddress);
    final int address = this.reverbWorkAreaAddress + relative & 0x7_fffe;

    return IoHelper.readShort(this.ram, address);
  }

  //Wait(1 cycle); at 44.1kHz clock
  //Timer=Timer-NoiseStep  ;subtract Step(4..7)
  //ParityBit = NoiseLevel.Bit15 xor Bit12 xor Bit11 xor Bit10 xor 1
  //IF Timer<0 then NoiseLevel = NoiseLevel * 2 + ParityBit
  //IF Timer<0 then Timer = Timer + (20000h SHR NoiseShift); reload timer once
  //IF Timer<0 then Timer = Timer + (20000h SHR NoiseShift); reload again if needed
  int noiseTimer;
  int noiseLevel;

  private void tickNoiseGenerator() {
    final int noiseStep = this.control.noiseFrequencyStep() + 4;
    final int noiseShift = this.control.noiseFrequencyShift();

    this.noiseTimer -= noiseStep;
    final int parityBit = this.noiseLevel >> 15 & 0x1 ^ this.noiseLevel >> 12 & 0x1 ^ this.noiseLevel >> 11 & 0x1 ^ this.noiseLevel >> 10 & 0x1 ^ 1;
    if(this.noiseTimer < 0) {
      this.noiseLevel = this.noiseLevel * 2 + parityBit;
    }
    if(this.noiseTimer < 0) {
      this.noiseTimer += 0x20000 >> noiseShift;
    }
    if(this.noiseTimer < 0) {
      this.noiseTimer += 0x20000 >> noiseShift;
    }
  }

  private short sampleVoice(final int v) {
    final Voice voice = this.voices[v];

    //Decode samples if its empty / next block
    if(!voice.hasSamples) {
      voice.decodeSamples(this.ram);
      voice.hasSamples = true;

      final byte flags = this.voices[v].spuAdpcm[1];
      final boolean loopStart = (flags & 0x4) != 0;

      if(loopStart) {
        assert voice.currentAddress >= 0 : "Negative address";
        voice.adpcmRepeatAddress = voice.currentAddress;
      }
    }

    //Get indices for gauss interpolation
    final int interpolationIndex = voice.counter.interpolationIndex();
    final int sampleIndex = voice.counter.currentSampleIndex();

    //Interpolate latest samples
    //this is why the latest 3 samples from the last block are saved because if index is 0
    //any subtraction is gonna be oob of the current voice adpcm array

    final double[] weights = interpolationWeights[interpolationIndex];

    double interpolated;
    interpolated = weights[0] * voice.getSample(sampleIndex - 3);
    interpolated += weights[1] * voice.getSample(sampleIndex - 2);
    interpolated += weights[2] * voice.getSample(sampleIndex - 1);
    interpolated += weights[3] * voice.getSample(sampleIndex);

    //Pitch modulation: Starts at voice 1 as it needs the last voice
    int step = voice.pitch;
    if(v > 0 && (this.channelFmMode & 0x1L << v) != 0) {
      final int factor = this.voices[v - 1].latest + 0x8000; //From previous voice
      step = step * factor >> 15;
      step &= 0xffff;
    }
    if(step > 0x3fff) {
      step = 0x4000;
    }

    voice.counter.register += step;

    if(voice.counter.currentSampleIndex() >= 28) {
      //Beyond the current adpcm sample block prepare to decode next
      voice.counter.currentSampleIndex(voice.counter.currentSampleIndex() - 28);
      voice.currentAddress += 2;
      voice.hasSamples = false;

      //LoopEnd and LoopRepeat flags are set after the "current block" set them as it's finished
      final byte flags = this.voices[v].spuAdpcm[1];
      final boolean loopEnd = (flags & 0x1) != 0;
      final boolean loopRepeat = (flags & 0x2) != 0;

      if(loopEnd) {
        if(loopRepeat) {
          assert voice.adpcmRepeatAddress >= 0 : "Negative address";
          assert voice.adpcmRepeatAddress < this.ram.length : "Address overflow";
          voice.currentAddress = voice.adpcmRepeatAddress;
        } else {
          voice.adsrPhase = Phase.Off;
          voice.adsrVolume = 0;
        }
      }
    }

    return (short)interpolated;
  }

  public void directWrite(final int spuRamOffset, final byte[] dma) {
    LOGGER.info("Performing direct write from stack to SPU @ %04x (%d bytes)", spuRamOffset, dma.length);

    synchronized(Spu.class) {
      System.arraycopy(dma, 0, this.ram, spuRamOffset, dma.length);
      Scus94491BpeSegment_8004.spuDmaCallback();
    }
  }

  public void setMainVolume(final int left, final int right) {
    LOGGER.info(SPU_MARKER, "Setting SPU main volume to %04x, %04x", left, right);

    synchronized(Spu.class) {
      this.mainVolumeL = left;
      this.mainVolumeR = right;
    }
  }

  public int getMainVolumeLeft() {
    synchronized(Spu.class) {
      return this.mainVolumeL;
    }
  }

  public int getMainVolumeRight() {
    synchronized(Spu.class) {
      return this.mainVolumeR;
    }
  }

  public void setReverbVolume(final int left, final int right) {
    LOGGER.info(SPU_MARKER, "Setting SPU reverb volume to %04x, %04x", left, right);

    synchronized(Spu.class) {
      this.reverbOutputVolumeL = left;
      this.reverbOutputVolumeR = right;
    }
  }

  public void keyOff(final long voices) {
    LOGGER.debug(SPU_MARKER, "Setting SPU key off to %08x", voices);

    synchronized(Spu.class) {
      this.keyOff |= voices;
    }
  }

  public void keyOn(final long voices) {
    LOGGER.debug(SPU_MARKER, "Setting SPU key on to %08x", voices);

    synchronized(Spu.class) {
      this.keyOn |= voices;
    }
  }

  public void clearKeyOn() {
    LOGGER.info(SPU_MARKER, "Clearing SPU key on");

    synchronized(Spu.class) {
      this.keyOn = 0;
    }
  }

  public void setNoiseMode(final long noiseMode) {
//    LOGGER.debug(SPU_MARKER, "Setting SPU noise mode to %x", noiseMode);

    synchronized(Spu.class) {
      this.channelNoiseMode = noiseMode;
    }
  }

  public void setReverbMode(final long reverbMode) {
//    LOGGER.debug(SPU_MARKER, "Setting SPU reverb mode to %x", reverbMode);

    synchronized(Spu.class) {
      this.channelReverbMode = reverbMode;
    }
  }

  public void setReverbWorkAreaAddress(final int workArea) {
    LOGGER.info(SPU_MARKER, "Setting SPU work area address to %x", workArea);

    synchronized(Spu.class) {
      this.reverbWorkAreaAddress = workArea * 8;
      this.reverbCurrentAddress = this.reverbWorkAreaAddress;
    }
  }

  public void setReverb(final ReverbConfig reverb) {
    synchronized(Spu.class) {
      this.reverb = reverb;
    }
  }

  public void mute() {
    LOGGER.info(SPU_MARKER, "Muting SPU");

    synchronized(Spu.class) {
      this.muted = true;
    }
  }

  public void unmute() {
    LOGGER.info(SPU_MARKER, "Unmuting SPU");

    synchronized(Spu.class) {
      this.muted = false;
    }
  }

  public void enableReverb() {
    LOGGER.info(SPU_MARKER, "Enabling SPU reverb");

    synchronized(Spu.class) {
      this.reverbEnabled = true;
    }
  }

  public void disableReverb() {
    LOGGER.info(SPU_MARKER, "Disabling SPU reverb");

    synchronized(Spu.class) {
      this.reverbEnabled = false;
    }
  }

  public void setNoiseFrequency(final int packed) {
    LOGGER.info(SPU_MARKER, "Setting SPU noise frequency %x", packed);

    synchronized(Spu.class) {
      this.noiseFrequencyShift = packed >> 2 & 0xf;
      this.noiseFrequencyStep = packed & 0x3;
    }
  }

  private static final double[][] interpolationWeights = new double[512][];
  public static final int[] sampleRates = new int[768];
}
