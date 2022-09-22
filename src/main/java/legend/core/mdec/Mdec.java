package legend.core.mdec;

import legend.core.MathHelper;
import legend.core.dma.DmaInterface;
import legend.core.memory.IllegalAddressException;
import legend.core.memory.Memory;
import legend.core.memory.Segment;
import legend.core.memory.Value;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;
import java.nio.ByteBuffer;
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Queue;

import static legend.core.Hardware.DMA;
import static legend.core.Hardware.MEMORY;

public class Mdec {
  public static final Value MDEC_REG0 = MEMORY.ref(4, 0x1f801820L);
  public static final Value MDEC_REG1 = MEMORY.ref(4, 0x1f801824L);

  private static final Logger LOGGER = LogManager.getFormatterLogger(Mdec.class);

  private static final int NUM_BLOCKS = 6;
  //For some reason even tho it iterates all blocks it starts at 4
  //going 4 (Cr), 5 (Cb), 0, 1, 2, 3 (Y)
  private static final int INITIAL_BLOCK = 4;
  private static final int MACRO_BLOCK_DECODED_BYTES = 256 * 3;

  //Status Register
  //private bool isDataOutFifoEmpty;
  private boolean isDataInFifoFull;
  private boolean isCommandBusy;
  private boolean isDataInRequested;
  private boolean isDataOutRequested;
  private int dataOutputDepth;
  private boolean isSigned;
  private int bit15;
  private int currentBlock = INITIAL_BLOCK;
  private int remainingDataWords;

  private boolean isColored;

  private final byte[] luminanceQuantTable = new byte[64];
  private final byte[] colorQuantTable = new byte[64];
  private final short[] scaleTable = new short[64];

  @Nullable
  private Runnable command;

  private final short[][] block = {new short[64], new short[64], new short[64], new short[64], new short[64], new short[64]};

  private short[] dst = new short[64];

  private final Queue<Short> inBuffer = new ArrayDeque<>(1024);

  private final byte[] outBuffer = new byte[0x30000]; // Wild guess while resumable DMAs come...
  private int outBufferPos;

  public Mdec(final Memory memory) {
    DMA.mdecIn.setDmaInterface(new DmaInterface() {
      @Override
      public void blockCopy(final int size) {
        final byte[] data = MEMORY.getBytes(DMA.mdecIn.MADR.get(), size * 4);

        for(int i = 0; i < data.length; i += 4) {
          MDEC_REG0.setu(MathHelper.get(data, i, 4));
        }

        DMA.mdecIn.MADR.addu(DMA.mdecIn.channelControl.getAddressStep().step * size);
      }

      @Override
      public void linkedList() {
        assert false;
      }
    });

    DMA.mdecOut.setDmaInterface(new DmaInterface() {
      @Override
      public void blockCopy(final int size) {
        final byte[] data = Mdec.this.processDmaLoad(size);
        MEMORY.setBytes(DMA.mdecOut.MADR.get(), data);

        DMA.mdecOut.MADR.addu(DMA.mdecOut.channelControl.getAddressStep().step * size);
      }

      @Override
      public void linkedList() {
        assert false;
      }
    });

    memory.addSegment(new MdecSegment(0x1f80_1820L));
  }

  private void decodeCommand(final int value) {
    final int rawCommand = value >>> 29;
    this.dataOutputDepth = value >>> 27 & 0x3;
    this.isSigned = (value >>> 26 & 0x1) == 1;
    this.bit15 = value >>> 25 & 0x1;
    this.remainingDataWords = value & 0xffff;
    this.isColored = (value & 0x1) == 1; //only used on command2;

    switch(rawCommand) {
      case 1 -> this.command = this::decodeMacroBlocks;
      case 2 -> {
        this.command = this::setQuantTable;
        this.remainingDataWords = 16 + (this.isColored ? 16 : 0);
      }
      case 3 -> {
        this.command = this::setScaleTable;
        this.remainingDataWords = 32;
      }
      default -> throw new RuntimeException("[MDEC] Unhandled Command " + Long.toHexString(rawCommand));
    }
  }

  private void decodeMacroBlocks() {
    while(!this.inBuffer.isEmpty()) {
      for(int i = 0; i < NUM_BLOCKS; i++) {
        //Try to decode a macro block (6 blocks)
        //But actually iterate from the current block
        final byte[] qt = this.currentBlock >= 4 ? this.colorQuantTable : this.luminanceQuantTable;
        //todo check if was success and idct and currentBlock++ there so we can return here
        //and recover state when handling resumable macroblock decoding...
        this.rl_decode_block(this.block[this.currentBlock], qt);
        this.idct_core(this.block[this.currentBlock]);

        this.currentBlock++;
        this.currentBlock %= NUM_BLOCKS;
      }

      this.yuv_to_rgb(this.block[0], 0, 0);
      this.yuv_to_rgb(this.block[1], 8, 0);
      this.yuv_to_rgb(this.block[2], 0, 8);
      this.yuv_to_rgb(this.block[3], 8, 8);

      this.yuvToRgbBlockPos += MACRO_BLOCK_DECODED_BYTES;

      //for (int i = 0; i < output.Length; i++) {
      //    Console.WriteLine(i + " " + output[i].ToString("x8"));
      //}
      //Console.WriteLine("MacroBlock decoded " + ++block + " srcPointer " + srcPointer + " bufferPtr " + ptr);
    }
    //Console.WriteLine("Finalized decode" + srcPointer + " " + ptr);
  }

  int yuvToRgbBlockPos;

  private void yuv_to_rgb(final short[] Yblk, final int xx, final int yy) {
    for(int y = 0; y < 8; y++) {
      for(int x = 0; x < 8; x++) {
        int r = this.block[4][(x + xx) / 2 + (y + yy) / 2 * 8]; //CR Block
        int b = this.block[5][(x + xx) / 2 + (y + yy) / 2 * 8]; //CB Block
        int g = (int)(-0.3437 * b + -0.7143 * r);

        r = (int)(1.402 * r);
        b = (int)(1.772 * b);
        final int Y = Yblk[x + y * 8];

        r = Math.min(Math.max(Y + r, -128), 127);
        g = Math.min(Math.max(Y + g, -128), 127);
        b = Math.min(Math.max(Y + b, -128), 127);

        r ^= 0x80;
        g ^= 0x80;
        b ^= 0x80;

        final int position = (x + xx + (y + yy) * 16) * 3 + this.yuvToRgbBlockPos;
        this.outBuffer[position    ] = (byte)r;
        this.outBuffer[position + 1] = (byte)g;
        this.outBuffer[position + 2] = (byte)b;
      }
    }
  }

  private int blockPointer;
  private int q_scale;
  private int val;
  private short n;

  public void rl_decode_block(final short[] blk, final byte[] qt) {
    if(this.blockPointer >= 64) { //Start of new block
      Arrays.fill(blk, (short)0);

      if(this.inBuffer.isEmpty()) {
        return;
      }
      this.n = this.inBuffer.remove();
      while((this.n & 0xffff) == 0xfe00) {
        if(this.inBuffer.isEmpty()) {
          return;
        }
        this.n = this.inBuffer.remove();
      }

      this.q_scale = this.n >>> 10 & 0x3f;
      this.val = this.signed10bit(this.n & 0x3ff) * (qt[0] & 0xff);

      this.blockPointer = 0;
    }

    while(this.blockPointer < blk.length) {
      if(this.q_scale == 0) {
        this.val = this.signed10bit(this.n & 0x3ff) * 2;
      }

      this.val = Math.min(Math.max(this.val, -0x400), 0x3ff);

      if(this.q_scale > 0) {
        blk[zigzag[this.blockPointer]] = (short)this.val;
      }

      if(this.q_scale == 0) {
        blk[this.blockPointer] = (short)this.val;
      }

      if(this.inBuffer.isEmpty()) {
        return;
      }

      this.n = this.inBuffer.remove();
      this.blockPointer += (this.n >>> 10 & 0x3f) + 1;
      this.val = (this.signed10bit(this.n & 0x3ff) * qt[this.blockPointer & 0x3f] * this.q_scale + 4) / 8;
    }
  }

  private void idct_core(short[] src) {
    for(int i = 0; i < 2; i++) {
      for(int x = 0; x < 8; x++) {
        for(int y = 0; y < 8; y++) {
          int sum = 0;
          for(int z = 0; z < 8; z++) {
            sum += src[y + z * 8] * (this.scaleTable[x + z * 8] / 8);
          }

          this.dst[x + y * 8] = (short)((sum + 0xfff) / 0x2000);
        }
      }

      final short[] tmp = this.dst;
      this.dst = src;
      src = tmp;
    }
  }

  private int signed10bit(final int n) {
    return n << 22 >> 22;
  }

  private void setQuantTable() {//64 unsigned parameter bytes for the Luminance Quant Table (used for Y1..Y4)
    for(int i = 0; i < 32; i++) { //16 words for each table
      final short value = this.inBuffer.remove();
      this.luminanceQuantTable[i * 2    ] = (byte)value;
      this.luminanceQuantTable[i * 2 + 1] = (byte)(value >>> 8);
    }

    //Console.WriteLine("setQuantTable: Luminance");

    if(!this.isColored) {
      return; //and if Command.Bit0 was set, by another 64 unsigned parameter bytes for the Color Quant Table(used for Cb and Cr).
    }

    for(int i = 0; i < 32; i++) { //16 words continuation from buffer
      final short value = this.inBuffer.remove();
      this.colorQuantTable[i * 2    ] = (byte)value;
      this.colorQuantTable[i * 2 + 1] = (byte)(value >>> 8);
    }

    //Console.WriteLine("setQuantTable: color");
  }

  private void setScaleTable() {  //64 signed half-words with 14bit fractional part
    for(int i = 0; i < 64; i++) { //written as 32 words on buffer
      this.scaleTable[i] = this.inBuffer.remove();
    }
  }

  public byte[] processDmaLoad(final int size) {
    if(this.dataOutputDepth == 2) { // 24b
      // RGBR, GBRG, BRGB...
      final byte[] data = new byte[size * 4];
      System.arraycopy(this.outBuffer, this.outBufferPos++ * data.length, data, 0, data.length);
      return data;
    }

    if(this.dataOutputDepth == 3) { // 15b
      // RGB * 2 => b15|b15
      final byte[] byteSpan = new byte[size * 6];
      System.arraycopy(this.outBuffer, this.outBufferPos++ * byteSpan.length, byteSpan, 0, byteSpan.length);

      for(int b24 = 0, b15 = 0; b24 < byteSpan.length; b24 += 3, b15 += 2) {
        final var r = (byteSpan[b24    ] & 0xff) >>> 3;
        final var g = (byteSpan[b24 + 1] & 0xff) >>> 3;
        final var b = (byteSpan[b24 + 2] & 0xff) >>> 3;

        final var rgb15 = (short)((b & 0xff) << 10 | (g & 0xff) << 5 | r & 0xff);
        final var lo = (byte)rgb15;
        final var hi = (byte)(rgb15 >>> 8);

        byteSpan[b15    ] = lo;
        byteSpan[b15 + 1] = hi;
      }

      final byte[] data = new byte[size * 4];
      System.arraycopy(byteSpan, 0, data, 0, data.length);
      return data;
    }

    //unsupported mode allocate and return garbage so can be seen
    final byte[] garbage = new byte[size * 4];
    for(int i = 0; i < size * 4; i += 2) {
      garbage[i] = (byte)0xff;
    }
    return garbage; // ff00ff00ff00ff00...
  }

  public short convert24to15bpp(final byte sr, final byte sg, final byte sb) {
    final byte r = (byte)(sr >>> 3);
    final byte g = (byte)(sg >>> 3);
    final byte b = (byte)(sb >>> 3);

    return (short)((b & 0xff) << 10 | (g & 0xff) << 5 | r & 0xff);
  }

  private boolean isDataOutFifoEmpty() {
    return this.outBufferPos == 0; //TODO compare to inBufferPos when we handle full dma in
  }

  public void dump(final ByteBuffer stream) {
    //TODO
  }

  public void load(final ByteBuffer stream, final int version) {
    //TODO
  }

  private static final byte[] zigzag = {
     0,  1,  8, 16,  9,  2,  3, 10,
    17, 24, 32, 25, 18, 11,  4,  5,
    12, 19, 26, 33, 40, 48, 41, 34,
    27, 20, 13,  6,  7, 14, 21, 28,
    35, 42, 49, 56, 57, 50, 43, 36,
    29, 22, 15, 23, 30, 37, 44, 51,
    58, 59, 52, 45, 38, 31, 39, 46,
    53, 60, 61, 54, 47, 55, 62, 63,
  };

  private final class MdecSegment extends Segment {
    private MdecSegment(final long address) {
      super(address, 0x8);
    }

    @Override
    public byte get(final int offset) {
      throw new UnsupportedOperationException("MDEC registers may only be accessed with 32-bit reads and writes");
    }

    @Override
    public long get(final int offset, final int size) {
      if(size != 4) {
        throw new UnsupportedOperationException("MDEC registers may only be accessed with 32-bit reads and writes");
      }

      return switch(offset & 0b100) {
        case 0x0 -> {
          if(Mdec.this.dataOutputDepth == 2) { //2 24b
            final int val = (Mdec.this.outBuffer[Mdec.this.outBufferPos * 4 + 3] & 0xff) << 24 | (Mdec.this.outBuffer[Mdec.this.outBufferPos * 4 + 2] & 0xff) << 16 | (Mdec.this.outBuffer[Mdec.this.outBufferPos * 4 + 1] & 0xff) << 8 | Mdec.this.outBuffer[Mdec.this.outBufferPos * 4] & 0xff;
            Mdec.this.outBufferPos++;
            yield val;
          }

          if(Mdec.this.dataOutputDepth == 3) { //3 15b
            final short lo = (short)(Mdec.this.bit15 << 15 | Mdec.this.convert24to15bpp(Mdec.this.outBuffer[Mdec.this.outBufferPos * 6    ], Mdec.this.outBuffer[Mdec.this.outBufferPos * 6 + 1], Mdec.this.outBuffer[Mdec.this.outBufferPos * 6 + 2]) & 0xffff);
            final short hi = (short)(Mdec.this.bit15 << 15 | Mdec.this.convert24to15bpp(Mdec.this.outBuffer[Mdec.this.outBufferPos * 6 + 3], Mdec.this.outBuffer[Mdec.this.outBufferPos * 6 + 4], Mdec.this.outBuffer[Mdec.this.outBufferPos * 6 + 5]) & 0xffff);

            yield (hi & 0xffffL) << 16 | lo & 0xffffL;
          }

          yield 0x00ff_00ffL;
        }

        case 0x4 -> {
          long status = 0;

          status |= (Mdec.this.isDataOutFifoEmpty() ? 1L : 0) << 31;
          status |= (Mdec.this.isDataInFifoFull ? 1L : 0) << 30;
          status |= (Mdec.this.isCommandBusy ? 1L : 0) << 29;
          status |= (Mdec.this.isDataInRequested ? 1L : 0) << 28;
          status |= (Mdec.this.isDataOutRequested ? 1L : 0) << 27;
          status |= (long)Mdec.this.dataOutputDepth << 25;
          status |= (Mdec.this.isSigned ? 1L : 0) << 24;
          status |= (long)Mdec.this.bit15 << 23;
          status |= (long)Mdec.this.currentBlock << 16;
          status |= Mdec.this.remainingDataWords - 1L & 0xffffL;
          //Console.WriteLine("[MDEC] Load Status " + status.ToString("x8"));
          //Console.ReadLine();

          Mdec.this.isCommandBusy = false;

          yield status;
        }

        default -> throw new IllegalAddressException("There is no MDEC port at " + Long.toHexString(this.getAddress() + offset));
      };
    }

    @Override
    public void set(final int offset, final byte value) {
      throw new UnsupportedOperationException("MDEC registers may only be accessed with 32-bit reads and writes");
    }

    @Override
    public void set(final int offset, final int size, final long value) {
      if(size != 4) {
        throw new UnsupportedOperationException("MDEC registers may only be accessed with 32-bit reads and writes");
      }

      switch(offset & 0b100) {
        case 0x0 -> {
          LOGGER.debug("[MDEC] Write %08x", value);

          if(Mdec.this.remainingDataWords == 0) {
            LOGGER.debug("[MDEC] Decoding %08x", value);
            Mdec.this.decodeCommand((int)value);
          } else {
            Mdec.this.inBuffer.add((short)value);
            Mdec.this.inBuffer.add((short)(value >>> 16));

            Mdec.this.remainingDataWords--;
            LOGGER.debug("[MDEC] remaining %04x", Mdec.this.remainingDataWords);
          }

          if(Mdec.this.remainingDataWords == 0) {
            Mdec.this.isCommandBusy = true;
            Mdec.this.yuvToRgbBlockPos = 0;
            Mdec.this.outBufferPos = 0;
            Mdec.this.command.run();
          }
        }

        case 0x4 -> {
          final boolean abortCommand = (value >>> 31 & 0x1) == 1;
          if(abortCommand) { //Set status to 80040000h
            Mdec.this.outBufferPos = 0;
            Mdec.this.currentBlock = 4;
            Mdec.this.remainingDataWords = 0;
            Mdec.this.command = null;
          }

          Mdec.this.isDataInRequested = (value >>> 30 & 0x1) == 1; //todo enable dma
          Mdec.this.isDataOutRequested = (value >>> 29 & 0x1) == 1;

          //Console.WriteLine("[MDEC] dataInRequest " + isDataInRequested + " dataOutRequested " + isDataOutRequested);
        }
      }
    }
  }
}
