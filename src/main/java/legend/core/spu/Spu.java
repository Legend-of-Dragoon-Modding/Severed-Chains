package legend.core.spu;

import legend.core.DebugHelper;
import legend.core.MathHelper;
import legend.core.memory.IllegalAddressException;
import legend.core.memory.Memory;
import legend.core.memory.MisalignedAccessException;
import legend.core.memory.Segment;
import legend.core.memory.Value;
import legend.core.memory.segments.RamSegment;
import legend.core.memory.types.MemoryRef;
import legend.core.memory.types.UnsignedIntRef;
import legend.core.memory.types.UnsignedShortRef;
import legend.game.Scus94491BpeSegment_8004;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import java.util.ArrayDeque;
import java.util.Queue;

import static legend.core.GameEngine.MEMORY;

public class Spu implements Runnable, MemoryRef {
  private static final Logger LOGGER = LogManager.getFormatterLogger(Spu.class);
  private static final Marker SPU_MARKER = MarkerManager.getMarker("SPU");

  private static final int NANOS_PER_TICK = 1_000_000_000 / 50;
  private static final int SAMPLES_PER_TICK = 44_100 / 50;

  public final UnsignedIntRef MAIN_VOL = MEMORY.ref(4, 0x1f801d80L, UnsignedIntRef::new);
  public final UnsignedShortRef MAIN_VOL_L = MEMORY.ref(2, 0x1f801d80L, UnsignedShortRef::new);
  public final UnsignedShortRef MAIN_VOL_R = MEMORY.ref(2, 0x1f801d82L, UnsignedShortRef::new);
  public final Value REVERB_OUT = MEMORY.ref(4, 0x1f801d84L);
  public final UnsignedShortRef REVERB_OUT_L = MEMORY.ref(2, 0x1f801d84L, UnsignedShortRef::new);
  public final UnsignedShortRef REVERB_OUT_R = MEMORY.ref(2, 0x1f801d86L, UnsignedShortRef::new);
  public final UnsignedIntRef VOICE_KEY_ON = MEMORY.ref(4, 0x1f801d88L, UnsignedIntRef::new);
  public final UnsignedShortRef VOICE_KEY_ON_LO = MEMORY.ref(2, 0x1f801d88L, UnsignedShortRef::new);
  public final UnsignedShortRef VOICE_KEY_ON_HI = MEMORY.ref(2, 0x1f801d8aL, UnsignedShortRef::new);
  public final UnsignedIntRef VOICE_KEY_OFF = MEMORY.ref(4, 0x1f801d8cL, UnsignedIntRef::new);
  /** 0x18c */
  public final UnsignedShortRef VOICE_KEY_OFF_LO = MEMORY.ref(2, 0x1f801d8cL, UnsignedShortRef::new);
  /** 0x18e */
  public final UnsignedShortRef VOICE_KEY_OFF_HI = MEMORY.ref(2, 0x1f801d8eL, UnsignedShortRef::new);
  public final UnsignedIntRef VOICE_CHN_FM_MODE = MEMORY.ref(4, 0x1f801d90L, UnsignedIntRef::new);
  public final UnsignedIntRef VOICE_CHN_NOISE_MODE = MEMORY.ref(4, 0x1f801d94L, UnsignedIntRef::new);
  public final UnsignedIntRef VOICE_CHN_REVERB_MODE = MEMORY.ref(4, 0x1f801d98L, UnsignedIntRef::new);
  public final UnsignedIntRef VOICE_CHN_ON_OFF_STATUS = MEMORY.ref(4, 0x1f801d9cL, UnsignedIntRef::new);
  public final UnsignedShortRef SOUND_RAM_REVERB_WORK_ADDR = MEMORY.ref(2, 0x1f801da2L, UnsignedShortRef::new);
  public final UnsignedShortRef SOUND_RAM_IRQ_ADDR = MEMORY.ref(2, 0x1f801da4L, UnsignedShortRef::new);
  public final UnsignedShortRef SOUND_RAM_DATA_TRANSFER_ADDR = MEMORY.ref(2, 0x1f801da6L, UnsignedShortRef::new);
  public final UnsignedShortRef SOUND_RAM_DATA_TRANSFER_FIFO = MEMORY.ref(2, 0x1f801da8L, UnsignedShortRef::new);
  public final UnsignedShortRef SPUCNT = MEMORY.ref(2, 0x1f801daaL, UnsignedShortRef::new);
  public final UnsignedShortRef SOUND_RAM_DATA_TRANSFER_CTRL = MEMORY.ref(2, 0x1f801dacL, UnsignedShortRef::new);
  public final UnsignedShortRef SPUSTAT = MEMORY.ref(2, 0x1f801daeL, UnsignedShortRef::new);
  public final UnsignedIntRef CD_VOL = MEMORY.ref(4, 0x1f801db0L, UnsignedIntRef::new);
  public final UnsignedShortRef CD_VOL_L = MEMORY.ref(2, 0x1f801db0L, UnsignedShortRef::new);
  public final UnsignedShortRef CD_VOL_R = MEMORY.ref(2, 0x1f801db2L, UnsignedShortRef::new);
  public final UnsignedIntRef EXT_VOL = MEMORY.ref(4, 0x1f801db4L, UnsignedIntRef::new);
  public final UnsignedShortRef EXT_VOL_L = MEMORY.ref(2, 0x1f801db4L, UnsignedShortRef::new);
  public final UnsignedShortRef EXT_VOL_R = MEMORY.ref(2, 0x1f801db6L, UnsignedShortRef::new);
  public final UnsignedIntRef CURR_MAIN_VOL = MEMORY.ref(4, 0x1f801db8L, UnsignedIntRef::new);
  /** 1b8 */
  public final UnsignedShortRef CURR_MAIN_VOL_L = MEMORY.ref(2, 0x1f801db8L, UnsignedShortRef::new);
  /** 1ba */
  public final UnsignedShortRef CURR_MAIN_VOL_R = MEMORY.ref(2, 0x1f801dbaL, UnsignedShortRef::new);

  private SourceDataLine sound;

  private final byte[] spuOutput = new byte[SAMPLES_PER_TICK * 4];
  private final Queue<Byte> cdBuffer = new ArrayDeque<>();

  private final byte[] ram = new byte[512 * 1024];
  public final Voice[] voices = new Voice[24];

  private int ramDataTransferAddressInternal;

  private int captureBufferPos;

  private long mainVolumeL;
  private long mainVolumeR;
  private long reverbOutputVolumeL;
  private long reverbOutputVolumeR;
  private long keyOn;
  private long keyOff;
  private long channelFmMode;
  private long channelNoiseMode;
  private long channelReverbMode;
  private long channelOnOffStatus;
  private long reverbWorkAreaAddress;
  private long irqAddress;
  private long dataTransferAddress;
  private long dataTransferFifo;
  private final Control control = new Control();
  private long dataTransferControl;
  private short status;
  private long cdVolumeL;
  private long cdVolumeR;
  private long externalVolumeL;
  private long externalVolumeR;
  private long currentMainVolumeL;
  private long currentMainVolumeR;

  private boolean running;

  public Spu(final Memory memory) {
    try {
      this.sound = AudioSystem.getSourceDataLine(new AudioFormat(44100, 16, 2, true, false));
      this.sound.open();
      this.sound.start();
    } catch(final LineUnavailableException|IllegalArgumentException e) {
      LOGGER.error("Failed to start audio", e);
      this.sound = null;
    }

    for(int i = 0; i < this.voices.length; i++) {
      this.voices[i] = new Voice(memory, i);
    }

    memory.addSegment(new SpuSegment(0x1f80_1d80L));
    memory.addSegment(new RamSegment(0x1f80_1dc0L, 0x40)); //TODO GH#1

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

  private void tick() {
    synchronized(Spu.class) {
      int dataIndex = 0;
      for(int i = 0; i < SAMPLES_PER_TICK; i++) {
        int sumLeft = 0;
        int sumRight = 0;

        final int edgeKeyOn = (int)this.keyOn;
        final int edgeKeyOff = (int)this.keyOff;
        this.keyOn = 0;
        this.keyOff = 0;

        if(edgeKeyOn != 0) {
          LOGGER.info(SPU_MARKER, "Keying on %x", edgeKeyOn);
        }

        if(edgeKeyOff != 0) {
          LOGGER.info(SPU_MARKER, "Keying off %x", edgeKeyOff);
        }

        this.tickNoiseGenerator();

        for(int voiceIndex = 0; voiceIndex < this.voices.length; voiceIndex++) {
          final Voice v = this.voices[voiceIndex];

          //keyOn and KeyOff are edge triggered on 0 to 1
          if((edgeKeyOn & 0x1 << voiceIndex) != 0) {
            LOGGER.info(SPU_MARKER, "Keying on voice %d", voiceIndex);
            this.channelOnOffStatus &= ~(1L << voiceIndex);
            v.keyOn();
          }

          if((edgeKeyOff & 0x1 << voiceIndex) != 0) {
            LOGGER.info(SPU_MARKER, "Keying off voice %d", voiceIndex);
            v.keyOff();
          }

          if(v.adsrPhase == Phase.Off) {
            v.latest = 0;
            continue;
          }

          short sample;
          if((this.channelNoiseMode & 1L << voiceIndex) == 0) {
            sample = this.sampleVoice(voiceIndex);
            //Read irqAddress Irq
            v.readRamIrq = false;
          } else {
            //Generated by tickNoiseGenerator
            sample = (short)this.noiseLevel;
          }

          //Handle ADSR Envelope
          sample = (short)(sample * v.adsrVolume >> 15);
          v.tickAdsr(voiceIndex);

          //Save sample for possible pitch modulation
          v.latest = sample;

          //Sum each voice sample
          sumLeft += sample * v.processVolume(v.volumeLeft) >> 15;
          sumRight += sample * v.processVolume(v.volumeRight) >> 15;
        }

        if(!this.control.spuUnmuted()) { //todo merge this on the for voice loop
          //On mute the spu still ticks but output is 0 for voices (not for cdInput)
          sumLeft = 0;
          sumRight = 0;
        }

        //Merge in CD audio (CDDA or XA)
        short cdL = 0;
        short cdR = 0;
        synchronized(this.cdBuffer) {
          if(this.control.cdAudioEnabled() && this.cdBuffer.size() > 3) { //Be sure theres something on the queue...
            final byte cdLLo = this.cdBuffer.remove();
            final byte cdLHi = this.cdBuffer.remove();
            final byte cdRLo = this.cdBuffer.remove();
            final byte cdRHi = this.cdBuffer.remove();

            cdL = (short)((cdLHi & 0xff) << 8 | cdLLo & 0xff);
            cdR = (short)((cdRHi & 0xff) << 8 | cdRLo & 0xff);

            //Apply Spu Cd In (CDDA/XA) Volume
            cdL = (short)(cdL * this.cdVolumeL >> 15);
            cdR = (short)(cdR * this.cdVolumeR >> 15);

            sumLeft += cdL;
            sumRight += cdR;
          }
        }

        //Write to capture buffers and check ram irq
        this.handleCaptureBuffer(0 * 1024 + this.captureBufferPos, cdL);
        this.handleCaptureBuffer(1 * 1024 + this.captureBufferPos, cdR);
        this.handleCaptureBuffer(2 * 1024 + this.captureBufferPos, this.voices[1].latest);
        this.handleCaptureBuffer(3 * 1024 + this.captureBufferPos, this.voices[3].latest);
        this.captureBufferPos = this.captureBufferPos + 2 & 0x3FF;

        //Clamp sum
        sumLeft = MathHelper.clamp(sumLeft, -0x8000, 0x7FFF) * (short)this.mainVolumeL >> 15;
        sumRight = MathHelper.clamp(sumRight, -0x8000, 0x7FFF) * (short)this.mainVolumeR >> 15;

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

  private boolean handleCaptureBuffer(final int address, final short sample) {
    this.ram[address] = (byte)(sample & 0xFF);
    this.ram[address + 1] = (byte)(sample >> 8 & 0xFF);

    return address >> 3 == this.irqAddress;
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

  public short sampleVoice(final int v) {
    final Voice voice = this.voices[v];

    //Decode samples if its empty / next block
    if(!voice.hasSamples) {
      voice.decodeSamples(this.ram, (int)this.irqAddress);
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
    if(v > 0 && (this.channelFmMode & 0b1 << v) != 0) {
      final int factor = this.voices[v - 1].latest + 0x8000; //From previous voice
      step = step * factor >> 15;
      step &= 0xffff;
    }
    if(step > 0x3fff) {
      step = 0x4000;
    }

    //Console.WriteLine("step u " + ((uint)step).ToString("x8") + "step i" + ((int)step).ToString("x8") + " " + voice.counter.register.ToString("x8"));
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
        this.channelOnOffStatus |= 1L << v;

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

  public void directWrite(final int spuRamOffset, final long ramOffset, final int size) {
    LOGGER.info("Performing direct write from RAM @ %08x to SPU @ %04x (%d bytes)", ramOffset, spuRamOffset, size);
    final byte[] data = MEMORY.getBytes(ramOffset, size);
    this.directWrite(spuRamOffset, data);
  }

  public void directWrite(final int spuRamOffset, final byte[] dma) {
    System.arraycopy(dma, 0, this.ram, spuRamOffset, dma.length);
    Scus94491BpeSegment_8004.spuDmaCallback();
  }

  public void pushCdBufferSamples(final byte[] decodedXaAdpcm) {
    synchronized(this.cdBuffer) {
//      this.cdBuffer.clear(); // TODO is not clearing the buffer going to be a problem?

      for(final byte b : decodedXaAdpcm) {
        this.cdBuffer.add(b);
      }
    }
  }

  private static final double[][] interpolationWeights = new double[512][];
  public static final int[] sampleRates = new int[768];

  @Override
  public long getAddress() {
    return 0x1f80_1c00L;
  }

  public class SpuSegment extends Segment {
    public SpuSegment(final long address) {
      super(address, 0x40);
    }

    @Override
    public byte get(final int offset) {
      throw new MisalignedAccessException("SPU registers may not be accessed via 8-bit reads or writes");
    }

    @Override
    public long get(final int offset, final int size) {
      synchronized(Spu.class) {
        if(size == 1) {
          return this.get(offset);
        }

        if(size == 2) {
          return switch(offset & 0x3e) {
            case 0x00 -> Spu.this.mainVolumeL;
            case 0x02 -> Spu.this.mainVolumeR;
            case 0x04 -> Spu.this.reverbOutputVolumeL;
            case 0x06 -> Spu.this.reverbOutputVolumeR;
            case 0x22 -> Spu.this.reverbWorkAreaAddress;
            case 0x24 -> Spu.this.irqAddress;
            case 0x26 -> Spu.this.dataTransferAddress;
            case 0x28 -> Spu.this.dataTransferFifo;
            case 0x2a -> Spu.this.control.register & 0xffffL;
            case 0x2c -> Spu.this.dataTransferControl;
            case 0x2e -> Spu.this.status & 0xffffL;
            case 0x30 -> Spu.this.cdVolumeL;
            case 0x32 -> Spu.this.cdVolumeR;
            case 0x34 -> Spu.this.externalVolumeL;
            case 0x36 -> Spu.this.externalVolumeR;
            case 0x38 -> Spu.this.currentMainVolumeL;
            case 0x3a -> Spu.this.currentMainVolumeR;
            default -> throw new MisalignedAccessException("SPU port " + Long.toHexString(offset) + " may not be accessed with 16-bit reads or writes");
          };
        }

        return switch(offset & 0x3c) {
          case 0x00 -> Spu.this.mainVolumeR << 16 | Spu.this.mainVolumeL;
          case 0x04 -> Spu.this.reverbOutputVolumeR << 16 | Spu.this.reverbOutputVolumeL;
          case 0x08 -> Spu.this.keyOn;
          case 0x0c -> Spu.this.keyOff;
          case 0x10 -> Spu.this.channelFmMode;
          case 0x14 -> Spu.this.channelNoiseMode;
          case 0x18 -> Spu.this.channelReverbMode;
          case 0x1c -> Spu.this.channelOnOffStatus;
          case 0x30 -> Spu.this.cdVolumeR << 16 | Spu.this.cdVolumeL;
          case 0x34 -> Spu.this.externalVolumeR << 16 | Spu.this.externalVolumeL;
          case 0x38 -> Spu.this.currentMainVolumeR << 16 | Spu.this.currentMainVolumeL;
          default -> throw new MisalignedAccessException("SPU port " + Long.toHexString(offset) + " may not be accessed with 32-bit reads or writes");
        };
      }
    }

    @Override
    public void set(final int offset, final byte value) {
      throw new MisalignedAccessException("SPU registers may not be accessed via 8-bit reads or writes");
    }

    @Override
    public void set(final int offset, final int size, final long value) {
      synchronized(Spu.class) {
        if(size == 1) {
          this.set(offset, (byte)value);
          return;
        }

        if(size == 2) {
          switch(offset & 0x3e) {
            case 0x00 -> {
              LOGGER.info(SPU_MARKER, "Setting SPU main volume left to %04x", value);
              Spu.this.mainVolumeL = value;
            }

            case 0x02 -> {
              LOGGER.info(SPU_MARKER, "Setting SPU main volume right to %04x", value);
              Spu.this.mainVolumeR = value;
            }

            case 0x04 -> {
              LOGGER.info(SPU_MARKER, "Setting SPU reverb output volume left to %04x", value);
              Spu.this.reverbOutputVolumeL = value;
            }

            case 0x06 -> {
              LOGGER.info(SPU_MARKER, "Setting SPU reverb output volume right to %04x", value);
              Spu.this.reverbOutputVolumeR = value;
            }

            case 0x22 -> {
              LOGGER.info(SPU_MARKER, "Setting SPU reverb work area address to %04x", value);
              Spu.this.reverbWorkAreaAddress = value;
            }

            case 0x24 -> {
              LOGGER.info("Setting SPU IRQ address to %04x", value);
              Spu.this.irqAddress = value;
            }

            case 0x26 -> {
              LOGGER.info(SPU_MARKER, "Setting SPU data transfer address to %04x", value);
              Spu.this.dataTransferAddress = value;
              Spu.this.ramDataTransferAddressInternal = (int)(value * 8);
            }

            case 0x28 -> {
              LOGGER.info(SPU_MARKER, "Setting SPU data transfer FIFO to %04x", value);
              Spu.this.dataTransferFifo = value;
              Spu.this.ram[Spu.this.ramDataTransferAddressInternal++ & 0xffff] = (byte)(value & 0xff);
              Spu.this.ram[Spu.this.ramDataTransferAddressInternal++ & 0xffff] = (byte)(value >>> 8 & 0xff);
            }

            case 0x2a -> {
              LOGGER.info(SPU_MARKER, "Setting SPU control to %04x", value);
              Spu.this.control.register = (short)value;

              //Status lower 5 bits are the same as control
              Spu.this.status &= 0xFFE0;
              Spu.this.status |= (short)(value & 0x1F);
            }

            case 0x2c -> {
              LOGGER.info(SPU_MARKER, "Setting SPU data transfer control to %04x", value);
              Spu.this.dataTransferControl = value;
            }

            case 0x2e -> throw new IllegalAddressException("SPU status register is read-only");

            case 0x30 -> {
              LOGGER.info(SPU_MARKER, "Setting SPU CD volume left to %04x", value);
              Spu.this.cdVolumeL = value;
            }

            case 0x32 -> {
              LOGGER.info(SPU_MARKER, "Setting SPU CD volume right to %04x", value);
              Spu.this.cdVolumeR = value;
            }

            case 0x34 -> {
              LOGGER.info(SPU_MARKER, "Setting SPU external volume left to %04x", value);
              Spu.this.externalVolumeL = value;
            }

            case 0x36 -> {
              LOGGER.info(SPU_MARKER, "Setting SPU external volume right to %04x", value);
              Spu.this.externalVolumeR = value;
            }

            case 0x38 -> {
              LOGGER.info(SPU_MARKER, "Setting SPU current volume left to %04x", value);
              Spu.this.currentMainVolumeL = value;
            }

            case 0x3a -> {
              LOGGER.info(SPU_MARKER, "Setting SPU current volume right to %04x", value);
              Spu.this.currentMainVolumeR = value;
            }
          }

          return;
        }

        if(size == 4) {
          switch(offset & 0x3c) {
            case 0x00 -> {
              LOGGER.info(SPU_MARKER, "Setting SPU main volume to %08x", value);
              Spu.this.mainVolumeL = value & 0xffffL;
              Spu.this.mainVolumeR = value >>> 16;
            }

            case 0x04 -> {
              LOGGER.info(SPU_MARKER, "Setting SPU reverb output volume to %08x", value);
              Spu.this.reverbOutputVolumeL = value & 0xffffL;
              Spu.this.reverbOutputVolumeR = value >>> 16;
            }

            case 0x08 -> {
              LOGGER.info(SPU_MARKER, "Setting SPU key on to %08x", value);
              Spu.this.keyOn |= value;
            }

            case 0x0c -> {
              LOGGER.info(SPU_MARKER, "Setting SPU key off to %08x", value);
              Spu.this.keyOff |= value;
            }

            case 0x10 -> {
              LOGGER.info(SPU_MARKER, "Setting SPU channel FM mode to %08x", value);
              Spu.this.channelFmMode = value;
            }

            case 0x14 -> {
//              LOGGER.info(SPU_MARKER, "Setting SPU channel noise mode to %08x", value);
              Spu.this.channelNoiseMode = value;
            }

            case 0x18 -> {
//              LOGGER.info(SPU_MARKER, "Setting SPU channel reverb mode to %08x", value);
              Spu.this.channelReverbMode = value;
            }

            case 0x1c -> throw new IllegalAddressException("SPU channel on/off status is read-only");

            case 0x30 -> {
              LOGGER.info(SPU_MARKER, "Setting SPU CD volume to %08x", value);
              Spu.this.cdVolumeL = value & 0xffffL;
              Spu.this.cdVolumeR = value >>> 16;
            }

            case 0x34 -> {
              LOGGER.info(SPU_MARKER, "Setting SPU external volume to %08x", value);
              Spu.this.externalVolumeL = value & 0xffffL;
              Spu.this.externalVolumeR = value >>> 16;
            }

            case 0x38 -> {
              LOGGER.info(SPU_MARKER, "Setting SPU current volume to %08x", value);
              Spu.this.currentMainVolumeL = value & 0xffffL;
              Spu.this.currentMainVolumeR = value >>> 16;
            }
          }
        }
      }
    }
  }
}
