package legend.core.spu;

import it.unimi.dsi.fastutil.bytes.ByteArrayList;
import it.unimi.dsi.fastutil.bytes.ByteList;
import legend.core.DebugHelper;
import legend.core.Hardware;
import legend.core.InterruptType;
import legend.core.IoHelper;
import legend.core.MathHelper;
import legend.core.dma.DmaChannel;
import legend.core.dma.DmaInterface;
import legend.core.memory.IllegalAddressException;
import legend.core.memory.Memory;
import legend.core.memory.MisalignedAccessException;
import legend.core.memory.Segment;
import legend.core.memory.Value;
import legend.core.memory.segments.RamSegment;
import legend.core.memory.types.MemoryRef;
import legend.core.memory.types.UnsignedIntRef;
import legend.core.memory.types.UnsignedShortRef;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import java.nio.ByteBuffer;
import java.util.ArrayDeque;
import java.util.Queue;

import static legend.core.Hardware.DMA;
import static legend.core.Hardware.INTERRUPTS;
import static legend.core.Hardware.MEMORY;

public class Spu implements Runnable, MemoryRef {
  private static final Logger LOGGER = LogManager.getFormatterLogger(Spu.class);

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
  public final UnsignedShortRef VOICE_KEY_OFF_LO = MEMORY.ref(2, 0x1f801d8cL, UnsignedShortRef::new);
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
  public final UnsignedShortRef CURR_MAIN_VOL_L = MEMORY.ref(2, 0x1f801db8L, UnsignedShortRef::new);
  public final UnsignedShortRef CURR_MAIN_VOL_R = MEMORY.ref(2, 0x1f801dbaL, UnsignedShortRef::new);

  private final SourceDataLine sound;

  private final ByteList spuOutput = new ByteArrayList();
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
  private final Status status = new Status();
  private long cdVolumeL;
  private long cdVolumeR;
  private long externalVolumeL;
  private long externalVolumeR;
  private long currentMainVolumeL;
  private long currentMainVolumeR;

  private static final int CYCLES_PER_SAMPLE = 0x300; //33868800 / 44100hz
  private int counter;
  private boolean running;

  public Spu(final Memory memory) {
    try {
      this.sound = AudioSystem.getSourceDataLine(new AudioFormat(44100, 16, 2, true, false));
      this.sound.open();
      this.sound.start();
    } catch(final LineUnavailableException e) {
      throw new RuntimeException("Failed to start audio", e);
    }

    for(int i = 0; i < this.voices.length; i++) {
      this.voices[i] = new Voice(memory, i);
    }

    DMA.spu.setDmaInterface(new DmaInterface() {
      @Override
      public void blockCopy(final int size) {
        final byte[] data;
        if(DMA.spu.channelControl.getTransferDirection() == DmaChannel.ChannelControl.TRANSFER_DIRECTION.TO_MAIN_RAM) {
          data = Spu.this.processDmaLoad(size);
          MEMORY.setBytes(DMA.spu.MADR.get(), data);
        } else {
          data = MEMORY.getBytes(DMA.spu.MADR.get(), size);
          Spu.this.processDmaWrite(data);
        }

        DMA.spu.MADR.addu(DMA.spu.channelControl.getAddressStep().step * size);
      }

      @Override
      public void linkedList() {
        assert false;
      }
    });

    memory.addSegment(new SpuSegment(0x1f80_1d80L));
    memory.addSegment(new RamSegment(0x1f80_1dc0L, 0x40)); //TODO GH#1
  }

  @Override
  public void run() {
    this.running = true;

    while(this.running) {
      if(this.tick(100)) {
        INTERRUPTS.set(InterruptType.SPU);
      }

      while(Hardware.dumping) {
        Hardware.spuWaiting = true;
        DebugHelper.sleep(1);
      }

      Hardware.spuWaiting = false;

      DebugHelper.sleep(0);
    }
  }

  public void stop() {
    this.running = false;
  }

  private boolean tick(final int cycles) {
    this.counter += cycles;

    if(this.counter < CYCLES_PER_SAMPLE) {
      return false;
    }

    this.counter -= CYCLES_PER_SAMPLE;

    boolean edgeTrigger = false;

    int sumLeft = 0;
    int sumRight = 0;

    final int edgeKeyOn = (int)this.keyOn;
    final int edgeKeyOff = (int)this.keyOff;
    this.keyOn = 0;
    this.keyOff = 0;

    this.tickNoiseGenerator();

    for(int i = 0; i < this.voices.length; i++) {
      final Voice v = this.voices[i];

      //keyOn and KeyOff are edge triggered on 0 to 1
      if((edgeKeyOff & 0x1 << i) != 0) {
        v.keyOff();
      }

      if((edgeKeyOn & 0x1 << i) != 0) {
        this.channelOnOffStatus &= ~(1L << i);
        v.keyOn();
      }

      if(v.adsrPhase == Phase.Off) {
        v.latest = 0;
        continue;
      }

      short sample;
      if((this.channelNoiseMode & 1L << i) == 0) {
        sample = this.sampleVoice(i);
        //Read irqAddress Irq
        edgeTrigger |= this.control.irq9Enabled() && v.readRamIrq;
        v.readRamIrq = false;
      } else {
        //Generated by tickNoiseGenerator
        sample = (short)this.noiseLevel;
      }

      //Handle ADSR Envelope
      sample = (short)(sample * v.adsrVolume >> 15);
      v.tickAdsr(i);

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
    edgeTrigger |= this.handleCaptureBuffer(0 * 1024 + this.captureBufferPos, cdL);
    edgeTrigger |= this.handleCaptureBuffer(1 * 1024 + this.captureBufferPos, cdR);
    edgeTrigger |= this.handleCaptureBuffer(2 * 1024 + this.captureBufferPos, this.voices[1].latest);
    edgeTrigger |= this.handleCaptureBuffer(3 * 1024 + this.captureBufferPos, this.voices[3].latest);
    this.captureBufferPos = this.captureBufferPos + 2 & 0x3FF;

    //Clamp sum
    sumLeft  = MathHelper.clamp(sumLeft,  -0x8000, 0x7FFF) * (short)this.mainVolumeL >> 15;
    sumRight = MathHelper.clamp(sumRight, -0x8000, 0x7FFF) * (short)this.mainVolumeR >> 15;

    //Add to samples bytes to output list
    this.spuOutput.add((byte)sumLeft);
    this.spuOutput.add((byte)(sumLeft >> 8));
    this.spuOutput.add((byte)sumRight);
    this.spuOutput.add((byte)(sumRight >> 8));

    if(this.spuOutput.size() > 2048) {
      final byte[] samples = this.spuOutput.toByteArray();
      this.sound.write(samples, 0, samples.length);
      this.spuOutput.clear();
    }

    if(this.control.spuEnabled() && this.control.irq9Enabled() && edgeTrigger) {
      this.status.irq9Flag(true);
    }

    return this.control.spuEnabled() && this.control.irq9Enabled() && edgeTrigger; //todo move spuEnabled outside
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
    int interpolated;
    interpolated = gaussTable[0x0FF - interpolationIndex] * voice.getSample(sampleIndex - 3);
    interpolated += gaussTable[0x1FF - interpolationIndex] * voice.getSample(sampleIndex - 2);
    interpolated += gaussTable[0x100 + interpolationIndex] * voice.getSample(sampleIndex - 1);
    interpolated += gaussTable[0x000 + interpolationIndex] * voice.getSample(sampleIndex - 0);
    interpolated >>= 15;

    //Todo adsr
    //interpolated = (interpolated * voice.adsrVolume) >> 15;

    //Pitch modulation: Starts at voice 1 as it needs the last voice
    int step = voice.pitch;
    if((this.channelFmMode & 0b1) << v != 0 && v > 0) {
      final int factor = this.voices[v - 1].latest + 0x8000; //From previous voice
      step = step * factor >> 15;
      step &= 0xFFFF;
    }
    if(step > 0x3FFF) {
      step = 0x4000;
    }

    //Console.WriteLine("step u " + ((uint)step).ToString("x8") + "step i" + ((int)step).ToString("x8") + " " + voice.counter.register.ToString("x8"));
    voice.counter.register += (short)step;

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
      }
      if(loopEnd && !loopRepeat) {
        voice.adsrPhase = Phase.Off;
        voice.adsrVolume = 0;
      }
      if(loopEnd && loopRepeat) {
        assert voice.adpcmRepeatAddress >= 0 : "Negative address";
        assert voice.adpcmRepeatAddress < this.ram.length : "Address overflow";
        voice.currentAddress = voice.adpcmRepeatAddress;
      }
    }

    return (short)interpolated;
  }

  public void directWrite(final int spuRamOffset, final long ramOffset, final int size) {
    LOGGER.info("Performing direct write from RAM @ %08x to SPU @ %04x (%d bytes)", ramOffset, spuRamOffset, size);
    final byte[] data = MEMORY.getBytes(ramOffset, size);
    this.processDmaWrite(spuRamOffset, data);
    DMA.spu.transferComplete();
    INTERRUPTS.set(InterruptType.SPU);
  }

  public void directWrite(final int spuRamOffset, final byte[] data) {
    LOGGER.info("Performing direct write from byte array to SPU @ %04x (%d bytes)", spuRamOffset, data.length);
    this.processDmaWrite(spuRamOffset, data);
    DMA.spu.transferComplete();
    INTERRUPTS.set(InterruptType.SPU);
  }

  private byte[] processDmaLoad(final int size) {
    final byte[] dma = new byte[size];
    System.arraycopy(this.ram, this.ramDataTransferAddressInternal, dma, 0, size);

    //ramDataTransferAddressInternal and ramIrqAddress already are >> 3
    //so check if it's in the size range and trigger int
    if(this.irqAddress > this.ramDataTransferAddressInternal && this.irqAddress < this.ramDataTransferAddressInternal + size) {
      this.ramDataTransferAddressInternal += size * 4;
      INTERRUPTS.set(InterruptType.SPU);
    } else {
      this.ramDataTransferAddressInternal += size * 4;
    }

    return dma;
  }

  private void processDmaWrite(final byte[] dma) {
    this.processDmaWrite(this.ramDataTransferAddressInternal, dma);
    this.ramDataTransferAddressInternal += dma.length;
  }

  private void processDmaWrite(final int spuRamOffset, final byte[] dma) {
    //Tekken 3 and FF8 overflows SPU Ram
    final int destAddress = spuRamOffset + dma.length - 1;

    if(destAddress <= 0x7FFFF) {
      System.arraycopy(dma, 0, this.ram, spuRamOffset, dma.length);
    } else {
      final int overflow = destAddress - 0x7FFFF;

      final byte[] firstSlice = new byte[dma.length - overflow];
      final byte[] overflowSpan = new byte[overflow];
      System.arraycopy(dma, 0, firstSlice, 0, firstSlice.length);
      System.arraycopy(dma, firstSlice.length, overflowSpan, 0, overflowSpan.length);

      System.arraycopy(firstSlice, 0, this.ram, spuRamOffset, firstSlice.length);
      System.arraycopy(overflowSpan, 0, this.ram, 0, overflowSpan.length);
    }

    if(this.irqAddress > spuRamOffset && this.irqAddress < spuRamOffset + dma.length) {
      INTERRUPTS.set(InterruptType.SPU);
    }
  }

  public void pushCdBufferSamples(final byte[] decodedXaAdpcm) {
    synchronized(this.cdBuffer) {
//      this.cdBuffer.clear(); TODO is not clearing the buffer going to be a problem?

      for(final byte b : decodedXaAdpcm) {
        this.cdBuffer.add(b);
      }
    }
  }

  private static final short[] gaussTable = {
    -0x001, -0x001, -0x001, -0x001, -0x001, -0x001, -0x001, -0x001,
    -0x001, -0x001, -0x001, -0x001, -0x001, -0x001, -0x001, -0x001,
    0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0001,
    0x0001, 0x0001, 0x0001, 0x0002, 0x0002, 0x0002, 0x0003, 0x0003,
    0x0003, 0x0004, 0x0004, 0x0005, 0x0005, 0x0006, 0x0007, 0x0007,
    0x0008, 0x0009, 0x0009, 0x000A, 0x000B, 0x000C, 0x000D, 0x000E,
    0x000F, 0x0010, 0x0011, 0x0012, 0x0013, 0x0015, 0x0016, 0x0018,
    0x0019, 0x001B, 0x001C, 0x001E, 0x0020, 0x0021, 0x0023, 0x0025,
    0x0027, 0x0029, 0x002C, 0x002E, 0x0030, 0x0033, 0x0035, 0x0038,
    0x003A, 0x003D, 0x0040, 0x0043, 0x0046, 0x0049, 0x004D, 0x0050,
    0x0054, 0x0057, 0x005B, 0x005F, 0x0063, 0x0067, 0x006B, 0x006F,
    0x0074, 0x0078, 0x007D, 0x0082, 0x0087, 0x008C, 0x0091, 0x0096,
    0x009C, 0x00A1, 0x00A7, 0x00AD, 0x00B3, 0x00BA, 0x00C0, 0x00C7,
    0x00CD, 0x00D4, 0x00DB, 0x00E3, 0x00EA, 0x00F2, 0x00FA, 0x0101,
    0x010A, 0x0112, 0x011B, 0x0123, 0x012C, 0x0135, 0x013F, 0x0148,
    0x0152, 0x015C, 0x0166, 0x0171, 0x017B, 0x0186, 0x0191, 0x019C,
    0x01A8, 0x01B4, 0x01C0, 0x01CC, 0x01D9, 0x01E5, 0x01F2, 0x0200,
    0x020D, 0x021B, 0x0229, 0x0237, 0x0246, 0x0255, 0x0264, 0x0273,
    0x0283, 0x0293, 0x02A3, 0x02B4, 0x02C4, 0x02D6, 0x02E7, 0x02F9,
    0x030B, 0x031D, 0x0330, 0x0343, 0x0356, 0x036A, 0x037E, 0x0392,
    0x03A7, 0x03BC, 0x03D1, 0x03E7, 0x03FC, 0x0413, 0x042A, 0x0441,
    0x0458, 0x0470, 0x0488, 0x04A0, 0x04B9, 0x04D2, 0x04EC, 0x0506,
    0x0520, 0x053B, 0x0556, 0x0572, 0x058E, 0x05AA, 0x05C7, 0x05E4,
    0x0601, 0x061F, 0x063E, 0x065C, 0x067C, 0x069B, 0x06BB, 0x06DC,
    0x06FD, 0x071E, 0x0740, 0x0762, 0x0784, 0x07A7, 0x07CB, 0x07EF,
    0x0813, 0x0838, 0x085D, 0x0883, 0x08A9, 0x08D0, 0x08F7, 0x091E,
    0x0946, 0x096F, 0x0998, 0x09C1, 0x09EB, 0x0A16, 0x0A40, 0x0A6C,
    0x0A98, 0x0AC4, 0x0AF1, 0x0B1E, 0x0B4C, 0x0B7A, 0x0BA9, 0x0BD8,
    0x0C07, 0x0C38, 0x0C68, 0x0C99, 0x0CCB, 0x0CFD, 0x0D30, 0x0D63,
    0x0D97, 0x0DCB, 0x0E00, 0x0E35, 0x0E6B, 0x0EA1, 0x0ED7, 0x0F0F,
    0x0F46, 0x0F7F, 0x0FB7, 0x0FF1, 0x102A, 0x1065, 0x109F, 0x10DB,
    0x1116, 0x1153, 0x118F, 0x11CD, 0x120B, 0x1249, 0x1288, 0x12C7,
    0x1307, 0x1347, 0x1388, 0x13C9, 0x140B, 0x144D, 0x1490, 0x14D4,
    0x1517, 0x155C, 0x15A0, 0x15E6, 0x162C, 0x1672, 0x16B9, 0x1700,
    0x1747, 0x1790, 0x17D8, 0x1821, 0x186B, 0x18B5, 0x1900, 0x194B,
    0x1996, 0x19E2, 0x1A2E, 0x1A7B, 0x1AC8, 0x1B16, 0x1B64, 0x1BB3,
    0x1C02, 0x1C51, 0x1CA1, 0x1CF1, 0x1D42, 0x1D93, 0x1DE5, 0x1E37,
    0x1E89, 0x1EDC, 0x1F2F, 0x1F82, 0x1FD6, 0x202A, 0x207F, 0x20D4,
    0x2129, 0x217F, 0x21D5, 0x222C, 0x2282, 0x22DA, 0x2331, 0x2389,
    0x23E1, 0x2439, 0x2492, 0x24EB, 0x2545, 0x259E, 0x25F8, 0x2653,
    0x26AD, 0x2708, 0x2763, 0x27BE, 0x281A, 0x2876, 0x28D2, 0x292E,
    0x298B, 0x29E7, 0x2A44, 0x2AA1, 0x2AFF, 0x2B5C, 0x2BBA, 0x2C18,
    0x2C76, 0x2CD4, 0x2D33, 0x2D91, 0x2DF0, 0x2E4F, 0x2EAE, 0x2F0D,
    0x2F6C, 0x2FCC, 0x302B, 0x308B, 0x30EA, 0x314A, 0x31AA, 0x3209,
    0x3269, 0x32C9, 0x3329, 0x3389, 0x33E9, 0x3449, 0x34A9, 0x3509,
    0x3569, 0x35C9, 0x3629, 0x3689, 0x36E8, 0x3748, 0x37A8, 0x3807,
    0x3867, 0x38C6, 0x3926, 0x3985, 0x39E4, 0x3A43, 0x3AA2, 0x3B00,
    0x3B5F, 0x3BBD, 0x3C1B, 0x3C79, 0x3CD7, 0x3D35, 0x3D92, 0x3DEF,
    0x3E4C, 0x3EA9, 0x3F05, 0x3F62, 0x3FBD, 0x4019, 0x4074, 0x40D0,
    0x412A, 0x4185, 0x41DF, 0x4239, 0x4292, 0x42EB, 0x4344, 0x439C,
    0x43F4, 0x444C, 0x44A3, 0x44FA, 0x4550, 0x45A6, 0x45FC, 0x4651,
    0x46A6, 0x46FA, 0x474E, 0x47A1, 0x47F4, 0x4846, 0x4898, 0x48E9,
    0x493A, 0x498A, 0x49D9, 0x4A29, 0x4A77, 0x4AC5, 0x4B13, 0x4B5F,
    0x4BAC, 0x4BF7, 0x4C42, 0x4C8D, 0x4CD7, 0x4D20, 0x4D68, 0x4DB0,
    0x4DF7, 0x4E3E, 0x4E84, 0x4EC9, 0x4F0E, 0x4F52, 0x4F95, 0x4FD7,
    0x5019, 0x505A, 0x509A, 0x50DA, 0x5118, 0x5156, 0x5194, 0x51D0,
    0x520C, 0x5247, 0x5281, 0x52BA, 0x52F3, 0x532A, 0x5361, 0x5397,
    0x53CC, 0x5401, 0x5434, 0x5467, 0x5499, 0x54CA, 0x54FA, 0x5529,
    0x5558, 0x5585, 0x55B2, 0x55DE, 0x5609, 0x5632, 0x565B, 0x5684,
    0x56AB, 0x56D1, 0x56F6, 0x571B, 0x573E, 0x5761, 0x5782, 0x57A3,
    0x57C3, 0x57E2, 0x57FF, 0x581C, 0x5838, 0x5853, 0x586D, 0x5886,
    0x589E, 0x58B5, 0x58CB, 0x58E0, 0x58F4, 0x5907, 0x5919, 0x592A,
    0x593A, 0x5949, 0x5958, 0x5965, 0x5971, 0x597C, 0x5986, 0x598F,
    0x5997, 0x599E, 0x59A4, 0x59A9, 0x59AD, 0x59B0, 0x59B2, 0x59B3,
  };

  @Override
  public long getAddress() {
    return 0x1f80_1c00L;
  }

  public void dump(final ByteBuffer stream) {
    IoHelper.write(stream, this.spuOutput.size());

    for(final byte b : this.spuOutput) {
      IoHelper.write(stream, b);
    }

    IoHelper.write(stream, this.cdBuffer.size());

    for(final byte b : this.cdBuffer) {
      IoHelper.write(stream, b);
    }

    stream.get(this.ram);

    for(final Voice voice : this.voices) {
      voice.dump(stream);
    }

    IoHelper.write(stream, this.ramDataTransferAddressInternal);

    IoHelper.write(stream, this.captureBufferPos);

    IoHelper.write(stream, this.mainVolumeL);
    IoHelper.write(stream, this.mainVolumeR);
    IoHelper.write(stream, this.reverbOutputVolumeL);
    IoHelper.write(stream, this.reverbOutputVolumeR);
    IoHelper.write(stream, this.keyOn);
    IoHelper.write(stream, this.keyOff);
    IoHelper.write(stream, this.channelFmMode);
    IoHelper.write(stream, this.channelNoiseMode);
    IoHelper.write(stream, this.channelReverbMode);
    IoHelper.write(stream, this.channelOnOffStatus);
    IoHelper.write(stream, this.reverbWorkAreaAddress);
    IoHelper.write(stream, this.irqAddress);
    IoHelper.write(stream, this.dataTransferAddress);
    IoHelper.write(stream, this.dataTransferFifo);
    this.control.dump(stream);
    IoHelper.write(stream, this.dataTransferControl);
    this.status.dump(stream);
    IoHelper.write(stream, this.cdVolumeL);
    IoHelper.write(stream, this.cdVolumeR);
    IoHelper.write(stream, this.externalVolumeL);
    IoHelper.write(stream, this.externalVolumeR);
    IoHelper.write(stream, this.currentMainVolumeL);
    IoHelper.write(stream, this.currentMainVolumeR);

    IoHelper.write(stream, this.counter);
    IoHelper.write(stream, this.running);

    legend.core.spu.XaAdpcm.dump(stream);
  }

  public void load(final ByteBuffer stream, final int version) {
    this.spuOutput.clear();
    final int spuOutputSize = IoHelper.readInt(stream);
    for(int i = 0; i < spuOutputSize; i++) {
      this.spuOutput.add(IoHelper.readByte(stream));
    }

    this.cdBuffer.clear();
    final int cdBufferSize = IoHelper.readInt(stream);
    for(int i = 0; i < cdBufferSize; i++) {
      this.cdBuffer.add(IoHelper.readByte(stream));
    }

    stream.get(this.ram);

    for(final Voice voice : this.voices) {
      voice.load(stream);
    }

    this.ramDataTransferAddressInternal = IoHelper.readInt(stream);

    this.captureBufferPos = IoHelper.readInt(stream);

    this.mainVolumeL = IoHelper.readLong(stream);
    this.mainVolumeR = IoHelper.readLong(stream);
    this.reverbOutputVolumeL = IoHelper.readLong(stream);
    this.reverbOutputVolumeR = IoHelper.readLong(stream);
    this.keyOn = IoHelper.readLong(stream);
    this.keyOff = IoHelper.readLong(stream);
    this.channelFmMode = IoHelper.readLong(stream);
    this.channelNoiseMode = IoHelper.readLong(stream);
    this.channelReverbMode = IoHelper.readLong(stream);
    this.channelOnOffStatus = IoHelper.readLong(stream);
    this.reverbWorkAreaAddress = IoHelper.readLong(stream);
    this.irqAddress = IoHelper.readLong(stream);
    this.dataTransferAddress = IoHelper.readLong(stream);
    this.dataTransferFifo = IoHelper.readLong(stream);
    this.control.load(stream);
    this.dataTransferControl = IoHelper.readLong(stream);
    this.status.load(stream);
    this.cdVolumeL = IoHelper.readLong(stream);
    this.cdVolumeR = IoHelper.readLong(stream);
    this.externalVolumeL = IoHelper.readLong(stream);
    this.externalVolumeR = IoHelper.readLong(stream);
    this.currentMainVolumeL = IoHelper.readLong(stream);
    this.currentMainVolumeR = IoHelper.readLong(stream);

    this.counter = IoHelper.readInt(stream);
    this.running = IoHelper.readBool(stream);

    XaAdpcm.load(stream);
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
          case 0x2e -> Spu.this.status.register & 0xffffL;
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

    @Override
    public void set(final int offset, final byte value) {
      throw new MisalignedAccessException("SPU registers may not be accessed via 8-bit reads or writes");
    }

    @Override
    public void set(final int offset, final int size, final long value) {
      if(size == 1) {
        this.set(offset, (byte)value);
        return;
      }

      if(size == 2) {
        switch(offset & 0x3e) {
          case 0x00 -> {
            LOGGER.debug("Setting SPU main volume left to %04x", value);
            Spu.this.mainVolumeL = value;
          }

          case 0x02 -> {
            LOGGER.debug("Setting SPU main volume right to %04x", value);
            Spu.this.mainVolumeR = value;
          }

          case 0x04 -> {
            LOGGER.debug("Setting SPU reverb output volume left to %04x", value);
            Spu.this.reverbOutputVolumeL = value;
          }

          case 0x06 -> {
            LOGGER.debug("Setting SPU reverb output volume right to %04x", value);
            Spu.this.reverbOutputVolumeR = value;
          }

          case 0x22 -> {
            LOGGER.debug("Setting SPU reverb work area address to %04x", value);
            Spu.this.reverbWorkAreaAddress = value;
          }

          case 0x24 -> {
            LOGGER.info("Setting SPU IRQ address to %04x", value);
            Spu.this.irqAddress = value;
          }

          case 0x26 -> {
            LOGGER.debug("Setting SPU data transfer address to %04x", value);
            Spu.this.dataTransferAddress = value;
            Spu.this.ramDataTransferAddressInternal = (int)(value * 8);
          }

          case 0x28 -> {
            LOGGER.debug("Setting SPU data transfer FIFO to %04x", value);
            Spu.this.dataTransferFifo = value;
            Spu.this.ram[Spu.this.ramDataTransferAddressInternal++ & 0xffff] = (byte)(value & 0xff);
            Spu.this.ram[Spu.this.ramDataTransferAddressInternal++ & 0xffff] = (byte)(value >>> 8 & 0xff);
          }

          case 0x2a -> {
            LOGGER.debug("Setting SPU control to %04x", value);
            Spu.this.control.register = (short)value;

            // IRQ flag is reset on ack
            if(!Spu.this.control.irq9Enabled()) {
              Spu.this.status.irq9Flag(false);
            }

            //Status lower 5 bits are the same as control
            Spu.this.status.register &= 0xFFE0;
            Spu.this.status.register |= (short)(value & 0x1F);
          }

          case 0x2c -> {
            LOGGER.debug("Setting SPU data transfer control to %04x", value);
            Spu.this.dataTransferControl = value;
          }

          case 0x2e -> throw new IllegalAddressException("SPU status register is read-only");

          case 0x30 -> {
            LOGGER.debug("Setting SPU CD volume left to %04x", value);
            Spu.this.cdVolumeL = value;
          }

          case 0x32 -> {
            LOGGER.debug("Setting SPU CD volume right to %04x", value);
            Spu.this.cdVolumeR = value;
          }

          case 0x34 -> {
            LOGGER.debug("Setting SPU external volume left to %04x", value);
            Spu.this.externalVolumeL = value;
          }

          case 0x36 -> {
            LOGGER.debug("Setting SPU external volume right to %04x", value);
            Spu.this.externalVolumeR = value;
          }

          case 0x38 -> {
            LOGGER.debug("Setting SPU current volume left to %04x", value);
            Spu.this.currentMainVolumeL = value;
          }

          case 0x3a -> {
            LOGGER.debug("Setting SPU current volume right to %04x", value);
            Spu.this.currentMainVolumeR = value;
          }
        }

        return;
      }

      if(size == 4) {
        switch(offset & 0x3c) {
          case 0x00 -> {
            LOGGER.debug("Setting SPU main volume to %08x", value);
            Spu.this.mainVolumeL = value & 0xffffL;
            Spu.this.mainVolumeR = value >>> 16;
          }

          case 0x04 -> {
            LOGGER.debug("Setting SPU reverb output volume to %08x", value);
            Spu.this.reverbOutputVolumeL = value & 0xffffL;
            Spu.this.reverbOutputVolumeR = value >>> 16;
          }

          case 0x08 -> {
            LOGGER.debug("Setting SPU key on to 08x", value);
            Spu.this.keyOn = value;
          }

          case 0x0c -> {
            LOGGER.debug("Setting SPU key off to 08x", value);
            Spu.this.keyOff = value;
          }

          case 0x10 -> {
            LOGGER.debug("Setting SPU channel FM mode to 08x", value);
            Spu.this.channelFmMode = value;
          }

          case 0x14 -> {
            LOGGER.debug("Setting SPU channel noise mode to 08x", value);
            Spu.this.channelNoiseMode = value;
          }

          case 0x18 -> {
            LOGGER.debug("Setting SPU channel reverb mode to 08x", value);
            Spu.this.channelReverbMode = value;
          }

          case 0x1c -> throw new IllegalAddressException("SPU channel on/off status is read-only");

          case 0x30 -> {
            LOGGER.debug("Setting SPU CD volume to %08x", value);
            Spu.this.cdVolumeL = value & 0xffffL;
            Spu.this.cdVolumeR = value >>> 16;
          }

          case 0x34 -> {
            LOGGER.debug("Setting SPU external volume to %08x", value);
            Spu.this.externalVolumeL = value & 0xffffL;
            Spu.this.externalVolumeR = value >>> 16;
          }

          case 0x38 -> {
            LOGGER.debug("Setting SPU current volume to %08x", value);
            Spu.this.currentMainVolumeL = value & 0xffffL;
            Spu.this.currentMainVolumeR = value >>> 16;
          }
        }
      }
    }
  }
}
