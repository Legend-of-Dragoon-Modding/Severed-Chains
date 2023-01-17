package legend.game.fmv;

import legend.core.DebugHelper;
import legend.core.MathHelper;
import legend.core.opengl.Window;
import legend.core.spu.XaAdpcm;
import legend.game.types.FileEntry08;
import legend.game.unpacker.Unpacker;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import java.nio.ByteBuffer;
import java.util.Arrays;

import static legend.core.GameEngine.GPU;
import static legend.game.Scus94491BpeSegment.setWidthAndFlags;
import static legend.game.Scus94491BpeSegment_8004.mainCallbackIndexOnceLoaded_8004dd24;
import static legend.game.Scus94491BpeSegment_8005._80052d6c;
import static legend.game.Scus94491BpeSegment_8005.diskFmvs_80052d7c;
import static legend.game.Scus94491BpeSegment_800b.afterFmvLoadingStage_800bf0ec;
import static legend.game.Scus94491BpeSegment_800b.drgnBinIndex_800bc058;
import static legend.game.Scus94491BpeSegment_800b.fmvIndex_800bf0dc;
import static legend.game.Scus94491BpeSegment_800b.fmvStage_800bf0d8;
import static legend.game.Scus94491BpeSegment_800b.submapIndex_800bd808;

public class Fmv {
  private static final Logger LOGGER = LogManager.getFormatterLogger(Fmv.class);

  private static final ZeroRunLengthAc ESCAPE_CODE = new ZeroRunLengthAc(BitStreamCode._000001___________, true, false);
  private static final ZeroRunLengthAc END_OF_BLOCK = new ZeroRunLengthAc(BitStreamCode._10_______________, MdecCode.MDEC_END_OF_DATA_TOP6, MdecCode.MDEC_END_OF_DATA_BOTTOM10, false, true);

  private static final ZeroRunLengthAcLookup AC_VARIABLE_LENGTH_CODES_MPEG1 = new ZeroRunLengthAcLookup.Builder()
    //  Code        "Run" "Level"
    ._11s              (0 ,  1)
    ._011s             (1 ,  1)
    ._0100s            (0 ,  2)
    ._0101s            (2 ,  1)
    ._00101s           (0 ,  3)
    ._00110s           (4 ,  1)
    ._00111s           (3 ,  1)
    ._000100s          (7 ,  1)
    ._000101s          (6 ,  1)
    ._000110s          (1 ,  2)
    ._000111s          (5 ,  1)
    ._0000100s         (2 ,  2)
    ._0000101s         (9 ,  1)
    ._0000110s         (0 ,  4)
    ._0000111s         (8 ,  1)
    ._00100000s        (13,  1)
    ._00100001s        (0 ,  6)
    ._00100010s        (12,  1)
    ._00100011s        (11,  1)
    ._00100100s        (3 ,  2)
    ._00100101s        (1 ,  3)
    ._00100110s        (0 ,  5)
    ._00100111s        (10,  1)
    ._0000001000s      (16,  1)
    ._0000001001s      (5 ,  2)
    ._0000001010s      (0 ,  7)
    ._0000001011s      (2 ,  3)
    ._0000001100s      (1 ,  4)
    ._0000001101s      (15,  1)
    ._0000001110s      (14,  1)
    ._0000001111s      (4 ,  2)
    ._000000010000s    (0 , 11)
    ._000000010001s    (8 ,  2)
    ._000000010010s    (4 ,  3)
    ._000000010011s    (0 , 10)
    ._000000010100s    (2 ,  4)
    ._000000010101s    (7 ,  2)
    ._000000010110s    (21,  1)
    ._000000010111s    (20,  1)
    ._000000011000s    (0 ,  9)
    ._000000011001s    (19,  1)
    ._000000011010s    (18,  1)
    ._000000011011s    (1 ,  5)
    ._000000011100s    (3 ,  3)
    ._000000011101s    (0 ,  8)
    ._000000011110s    (6 ,  2)
    ._000000011111s    (17,  1)
    ._0000000010000s   (10,  2)
    ._0000000010001s   (9 ,  2)
    ._0000000010010s   (5 ,  3)
    ._0000000010011s   (3 ,  4)
    ._0000000010100s   (2 ,  5)
    ._0000000010101s   (1 ,  7)
    ._0000000010110s   (1 ,  6)
    ._0000000010111s   (0 , 15)
    ._0000000011000s   (0 , 14)
    ._0000000011001s   (0 , 13)
    ._0000000011010s   (0 , 12)
    ._0000000011011s   (26,  1)
    ._0000000011100s   (25,  1)
    ._0000000011101s   (24,  1)
    ._0000000011110s   (23,  1)
    ._0000000011111s   (22,  1)
    ._00000000010000s  (0 , 31)
    ._00000000010001s  (0 , 30)
    ._00000000010010s  (0 , 29)
    ._00000000010011s  (0 , 28)
    ._00000000010100s  (0 , 27)
    ._00000000010101s  (0 , 26)
    ._00000000010110s  (0 , 25)
    ._00000000010111s  (0 , 24)
    ._00000000011000s  (0 , 23)
    ._00000000011001s  (0 , 22)
    ._00000000011010s  (0 , 21)
    ._00000000011011s  (0 , 20)
    ._00000000011100s  (0 , 19)
    ._00000000011101s  (0 , 18)
    ._00000000011110s  (0 , 17)
    ._00000000011111s  (0 , 16)
    ._000000000010000s (0 , 40)
    ._000000000010001s (0 , 39)
    ._000000000010010s (0 , 38)
    ._000000000010011s (0 , 37)
    ._000000000010100s (0 , 36)
    ._000000000010101s (0 , 35)
    ._000000000010110s (0 , 34)
    ._000000000010111s (0 , 33)
    ._000000000011000s (0 , 32)
    ._000000000011001s (1 , 14)
    ._000000000011010s (1 , 13)
    ._000000000011011s (1 , 12)
    ._000000000011100s (1 , 11)
    ._000000000011101s (1 , 10)
    ._000000000011110s (1 ,  9)
    ._000000000011111s (1 ,  8)
    ._0000000000010000s(1 , 18)
    ._0000000000010001s(1 , 17)
    ._0000000000010010s(1 , 16)
    ._0000000000010011s(1 , 15)
    ._0000000000010100s(6 ,  3)
    ._0000000000010101s(16,  2)
    ._0000000000010110s(15,  2)
    ._0000000000010111s(14,  2)
    ._0000000000011000s(13,  2)
    ._0000000000011001s(12,  2)
    ._0000000000011010s(11,  2)
    ._0000000000011011s(31,  1)
    ._0000000000011100s(30,  1)
    ._0000000000011101s(29,  1)
    ._0000000000011110s(28,  1)
    ._0000000000011111s(27,  1)
    .add(ESCAPE_CODE)
    .add(END_OF_BLOCK)
    .build();

  private static final int[] reverseZigzag = {
     0,  1,  8, 16,  9,  2,  3, 10,
    17, 24, 32, 25, 18, 11,  4,  5,
    12, 19, 26, 33, 40, 48, 41, 34,
    27, 20, 13,  6,  7, 14, 21, 28,
    35, 42, 49, 56, 57, 50, 43, 36,
    29, 22, 15, 23, 30, 37, 44, 51,
    58, 59, 52, 45, 38, 31, 39, 46,
    53, 60, 61, 54, 47, 55, 62, 63,
  };

  private static final int[] quantizationMatrix = {
     2, 16, 19, 22, 26, 27, 29, 34,
    16, 16, 22, 24, 27, 29, 34, 37,
    19, 22, 26, 27, 29, 34, 34, 38,
    22, 22, 26, 27, 29, 34, 37, 40,
    22, 26, 27, 29, 32, 35, 40, 48,
    26, 27, 29, 32, 35, 40, 48, 58,
    26, 27, 29, 34, 38, 46, 56, 69,
    27, 29, 35, 38, 46, 56, 69, 83,
  };

  private static final int[] PSX_DEFAULT_COSINE_MATRIX = {
    23170,  23170,  23170,  23170,  23170,  23170,  23170,  23170,
    32138,  27245,  18204,   6392,  -6393, -18205, -27246, -32139,
    30273,  12539, -12540, -30274, -30274, -12540,  12539,  30273,
    27245,  -6393, -32139, -18205,  18204,  32138,   6392, -27246,
    23170, -23171, -23171,  23170,  23170, -23171, -23171,  23170,
    18204, -32139,   6392,  27245, -27246,  -6393,  32138, -18205,
    12539, -30274,  30273, -12540, -12540,  30273, -30274,  12539,
     6392, -18205,  27245, -32139,  32138, -27246,  18204,  -6393,
  };

  private static Runnable oldRenderer;
  private static int oldFps;
  private static int oldWidth;
  private static int oldHeight;
  private static int sector;

  private static SourceDataLine sound;

  private static Window.Events.Char charPress;
  private static Window.Events.Click click;
  private static boolean shouldStop;

  public static void playCurrentFmv() {
//TODO this might be necessary for the post-game cutscene or something?
//      creditsLoaded_800d1cb8.setu(0);
//      loadDrgnBinFile(0, 5721, 0, SMap::loadCreditsMrg, (int)fmvIndex_800bf0dc.get(), 0x4L);

    final int width = switch((int)fmvIndex_800bf0dc.get()) {
      case 0, 2, 3, 4, 6, 7, 8, 9, 14, 15, 16, 17 -> 320;
      case 1, 5, 10, 11, 12, 13 -> 640;
      default -> throw new RuntimeException("Bad FMV index");
    };

    setWidthAndFlags(width);

    submapIndex_800bd808.set(-1);

    final FileEntry08 file = diskFmvs_80052d7c.get(drgnBinIndex_800bc058.get()).deref().get((int)(fmvIndex_800bf0dc.get() - _80052d6c.get(drgnBinIndex_800bc058.get() - 1).get()));
    Fmv.play(file.name_04.deref().get(), true);
    fmvStage_800bf0d8.setu(0);
    mainCallbackIndexOnceLoaded_8004dd24.set(afterFmvLoadingStage_800bf0ec.get());
  }

  public static void play(final String file, final boolean doubleSpeed) {
    shouldStop = false;

    final byte[] data = new byte[2352];
    final SectorHeader header = new SectorHeader(data);
    final VideoSector video = new VideoSector(data);

    final int sectorCount = doubleSpeed ? 10 : 5; // (150|75) sectors per second / 15 frames per second
    final byte[] demuxedRaw = new byte[2016 * sectorCount];
    final ByteBuffer demuxed = ByteBuffer.wrap(demuxedRaw);
    final FrameHeader frameHeader = new FrameHeader(demuxedRaw);

    final byte[] fileData = Unpacker.loadFile(file);
    sector = 0;

    while(!GPU.isReady()) {
      DebugHelper.sleep(1);
    }

    oldRenderer = GPU.mainRenderer;
    oldFps = GPU.window().getFpsLimit();
    oldWidth = GPU.window().getWidth();
    oldHeight = GPU.window().getHeight();
    GPU.window().setFpsLimit(15);

    try {
      sound = AudioSystem.getSourceDataLine(new AudioFormat(44100, 16, 2, true, false));
      sound.open();
      sound.start();
    } catch(final LineUnavailableException|IllegalArgumentException e) {
      LOGGER.error("Failed to start audio for FMV");
    }

    charPress = GPU.window().events.onCharPress((window, codepoint) -> shouldStop = true);
    click = GPU.window().events.onMouseRelease((window, x, y, button, mods) -> shouldStop = true);

    GPU.mainRenderer = () -> {
      if(shouldStop) {
        stop();
      }

      int demuxedSize = 0;

      // Demultiplex the sectors
      Arrays.fill(demuxedRaw, (byte)0);
      for(int sectorIndex = 0, videoSectorIndex = 0; sectorIndex < sectorCount; sectorIndex++, sector++) {
        System.arraycopy(fileData, sector * data.length, data, 0, data.length);

        if(header.submode.isEof()) {
          stop();
          return;
        }

        if(header.submode.getType() == SectorHeader.TYPE.DATA) {
          if(sectorIndex == 0) {
            demuxedSize = video.getDemuxedSize();
          }

          video.readSector(demuxedRaw, videoSectorIndex++);
        }

        if(header.submode.getType() == SectorHeader.TYPE.AUDIO) {
          final byte[] decodedXaAdpcm = XaAdpcm.decode(data, data[19]);

          // Halve the volume
          for(int i = 0; i < decodedXaAdpcm.length; i++) {
            decodedXaAdpcm[i] >>= 1;
          }

          if(sound != null) {
            sound.write(decodedXaAdpcm, 0, decodedXaAdpcm.length);
          }
        }
      }

      demuxed.position(10);
      final int blockW = (frameHeader.getWidth() + 15) / 16;
      final int blockH = (frameHeader.getHeight() + 15) / 16;
      final int chromaW = blockW * 8;
      final int chromaH = blockH * 8;
      final int lumaW = blockW * 16;
      final int lumaH = blockH * 16;

      final int macroblockCount = blockW * blockH;
      final int uncompressedSize = macroblockCount * 6 * 2;
      final ByteBuffer initialBlockCodes = ByteBuffer.allocate(uncompressedSize);

      // Decompress initial block codes
      outer:
      while(initialBlockCodes.position() < uncompressedSize) {
        final int flags = demuxed.get();
        int mask = 1;

        for(int bit = 0; bit < 8; bit++) {
          if((flags & mask) == 0) {
            initialBlockCodes.put(demuxed.get());
          } else {
            final int copySize = (demuxed.get() & 0xff) + 3;
            int copyOffset = demuxed.get() & 0xff;

            if((copyOffset & 0x80) != 0) {
              copyOffset = (copyOffset & 0x7f) << 8 | demuxed.get() & 0xff;
            }

            copyOffset++;

            for(int i = 0; i < copySize; i++) {
              initialBlockCodes.put(initialBlockCodes.get(initialBlockCodes.position() - copyOffset));
            }
          }

          if(initialBlockCodes.position() >= uncompressedSize) {
            break outer;
          }

          mask <<= 1;
        }
      }

      final int[] chromaMacroBlockOffsetLookup = new int[blockW * blockH];
      final int[] lumaBlockOffsetLookup = new int[blockW * blockH * 4];

      // build a table that holds the starting index of every (macro)block
      // in the output buffer so we don't have to do this calculation during decoding
      {
        int macroblockIndex = 0;
        for(int macroblockX = 0; macroblockX < blockW; macroblockX++) {
          for(int macroblockY = 0; macroblockY < blockH; macroblockY++) {
            chromaMacroBlockOffsetLookup[macroblockIndex] = macroblockX * 8 + macroblockY * 8 * chromaW;
            int blockIndex = 0;
            for(int blockX = 0; blockX < 2; blockX++) {
              for(int blockY = 0; blockY < 2; blockY++) {
                lumaBlockOffsetLookup[macroblockIndex * 4 + blockIndex] = macroblockX * 16 + blockY * 8 + (macroblockY * 16 + blockX * 8) * lumaW;
                blockIndex++;
              }
            }
            macroblockIndex++;
          }
        }
      }

      // Dequantize and apply inverse discrete cosine transform
      final VariableLengthCode vlc = new VariableLengthCode();
      final ArrayBitReader bitReader = new ArrayBitReader(demuxedRaw, demuxedSize, true, 10 + frameHeader.getCompressedCodesSize());
      final int[] cr = new int[chromaW * chromaH];
      final int[] cb = new int[chromaW * chromaH];
      final int[] luma = new int[lumaW * lumaH];

      for(int macroblockIndex = 0; macroblockIndex < macroblockCount; macroblockIndex++) {
        for(int blockIndex = 0; blockIndex < 6; blockIndex++) { // for Cr, Cb, Y1, Y2, Y3, Y4
          final int[] coefficients = new int[64];
          int vectorPos = 0;

          final int initialCode = (initialBlockCodes.get(macroblockIndex * 6 + blockIndex) & 0xff) << 8 | initialBlockCodes.get(macroblockIndex * 6 + blockIndex + uncompressedSize / 2) & 0xff;
          final int dc = initialCode << 22 >> 22; // 10-bit signed
          final int blockQuant = initialCode >>> 10; // 6-bit unsigned

          int nonZeroCount;
          if(dc != 0) {
            coefficients[0] = dc * quantizationMatrix[0];
            nonZeroCount = 1;
          } else {
            nonZeroCount = 0;
          }

          while(getNextVlc(vlc, bitReader)) {
            vectorPos += vlc.zeroes + 1;

            if(vlc.coefficient != 0) {
              final int zigzagPos = reverseZigzag[vectorPos];
              coefficients[zigzagPos] = vlc.coefficient * quantizationMatrix[zigzagPos] * blockQuant + 4 >> 3; // (int)Math.round(i / 8.0)
              nonZeroCount++;
            }
          }

          if(vectorPos > 63) {
            throw new RuntimeException("Too many AC coefficients codes (" + vectorPos + ')');
          }

          final int[] outputBuffer;
          int iOutOffset;
          final int iOutWidth;
          switch(blockIndex) {
            case 0 -> {
              outputBuffer = cr;
              iOutOffset = chromaMacroBlockOffsetLookup[macroblockIndex];
              iOutWidth = chromaW;
            }
            case 1 -> {
              outputBuffer = cb;
              iOutOffset = chromaMacroBlockOffsetLookup[macroblockIndex];
              iOutWidth = chromaW;
            }
            default -> {
              outputBuffer = luma;
              iOutOffset = lumaBlockOffsetLookup[macroblockIndex * 4 + blockIndex - 2];
              iOutWidth = lumaW;
            }
          }

          if(nonZeroCount == 0) {
            for(int i = 0; i < 8; i++, iOutOffset += iOutWidth) {
              Arrays.fill(outputBuffer, iOutOffset, iOutOffset + 8, 0);
            }
          } else {
            idct(coefficients, 0, coefficients);

            // TODO: have IDCT write to the destination location directly
            for(int i = 0, iSrcOfs = 0; i < 8; i++, iSrcOfs += 8, iOutOffset += iOutWidth) {
              System.arraycopy(coefficients, iSrcOfs, outputBuffer, iOutOffset, 8);
            }
          }
        }
      }

      // Build YCbCr pixel array
      final int[] framePixels = new int[frameHeader.getWidth() * frameHeader.getHeight()];
      readDecodedRgb(chromaW, lumaW, cr, cb, luma, frameHeader.getWidth(), frameHeader.getHeight(), framePixels, 0, frameHeader.getWidth());

      GPU.displaySize(frameHeader.getWidth(), frameHeader.getHeight());
      GPU.displayTexture(framePixels);
      GPU.drawMesh();
    };
  }

  public static void stop() {
    GPU.mainRenderer = () -> {
      if(charPress != null) {
        GPU.window().events.removeCharPress(charPress);
        charPress = null;
      }

      if(click != null) {
        GPU.window().events.removeMouseRelease(click);
        click = null;
      }

      GPU.mainRenderer = oldRenderer;
      GPU.window().setFpsLimit(oldFps);
      GPU.displaySize(oldWidth, oldHeight);
      oldRenderer = null;

      if(sound != null) {
        sound.close();
        sound = null;
      }
    };
  }

  private static boolean getNextVlc(final VariableLengthCode vlc, final ArrayBitReader bitReader) {
    final int value = bitReader.peekUnsignedBits(BitStreamCode.LONGEST_BITSTREAM_CODE_17BITS);

    final ZeroRunLengthAc ac = AC_VARIABLE_LENGTH_CODES_MPEG1.lookup(value);
    bitReader.skipBits(ac.getBitLength());

    if(ac.isEndOfBlock()) {
      return false;
    }

    if(ac.isEscapeCode()) {
      vlc.zeroes = bitReader.readUnsignedBits(6);
      vlc.coefficient = bitReader.readSignedBits(10);
      return true;
    }

    vlc.zeroes = ac.getMdecCodeCopy().getTop6Bits();
    vlc.coefficient = ac.getMdecCodeCopy().getBottom10Bits();
    return true;
  }

  private static final long[] _aTemp = new long[64];
  private static void idct(final int[] idctMatrix, final int iOutputOffset, final int[] output) {
    long tempSum;
    int x;
    int y;
    int i;

    for(x = 0; x < 8; x++) {
      for(y = 0; y < 8; y++) {
        tempSum = 0;

        for(i = 0; i < 8; i++) {
          tempSum += (PSX_DEFAULT_COSINE_MATRIX[i * 8 + y] * idctMatrix[x + i * 8]);
        }

        _aTemp[x + y * 8] = tempSum;
      }
    }

    for(x = 0; x < 8; x++) {
      for(y = 0; y < 8; y++) {
        tempSum = 0;

        for(i = 0; i < 8; i++) {
          tempSum += _aTemp[i + y * 8] * PSX_DEFAULT_COSINE_MATRIX[x + i * 8];
        }

        output[iOutputOffset + x + y * 8] = (int)MathHelper.shrRound(tempSum, 32);
      }
    }
  }

  public static void readDecodedRgb(final int chromaW, final int lumaW, final int[] cr, final int[] cb, final int[] luma, final int destW, final int destH, final int[] dest, final int outStart, final int outStride) {
    final PsxYCbCr_int psxycc = new PsxYCbCr_int();
    final RGB rgb1 = new RGB(), rgb2 = new RGB(), rgb3 = new RGB(), rgb4 = new RGB();

    final int W_x2 = lumaW * 2, iOutStride_x2 = outStride * 2;

    final int iDestWidthSub1 = destW - 1;
    final int iDestHeightSub1 = destH - 1;

    int iLumaLineOfsStart = 0, iChromaLineOfsStart = 0, iDestLineOfsStart = outStart;
    int iY = 0;
    for(; iY < iDestHeightSub1; iY += 2, iLumaLineOfsStart += W_x2, iChromaLineOfsStart += chromaW, iDestLineOfsStart += iOutStride_x2) {
      // writes 2 lines at a time
      int iSrcLumaOfs1 = iLumaLineOfsStart, iSrcLumaOfs2 = iLumaLineOfsStart + lumaW, iSrcChromaOfs = iChromaLineOfsStart, iDestOfs1 = iDestLineOfsStart, iDestOfs2 = iDestLineOfsStart + outStride;

      int iX = 0;
      for(; iX < iDestWidthSub1; iX += 2, iSrcChromaOfs++) {
        psxycc.cr = cr[iSrcChromaOfs];
        psxycc.cb = cb[iSrcChromaOfs];

        psxycc.y1 = luma[iSrcLumaOfs1++];
        psxycc.y2 = luma[iSrcLumaOfs1++];
        psxycc.y3 = luma[iSrcLumaOfs2++];
        psxycc.y4 = luma[iSrcLumaOfs2++];

        psxycc.toRgb(rgb1, rgb2, rgb3, rgb4);

        dest[iDestOfs1++] = rgb1.toRgba();
        dest[iDestOfs1++] = rgb2.toRgba();
        dest[iDestOfs2++] = rgb3.toRgba();
        dest[iDestOfs2++] = rgb4.toRgba();
      }

      if(iX < destW) {
        // if the width is odd, add 2 pixels
        psxycc.cr = cr[iSrcChromaOfs];
        psxycc.cb = cb[iSrcChromaOfs];

        psxycc.y1 = luma[iSrcLumaOfs1];
        psxycc.y2 = luma[iSrcLumaOfs1];
        psxycc.y3 = luma[iSrcLumaOfs2];
        psxycc.y4 = luma[iSrcLumaOfs2];

        psxycc.toRgb(rgb1, rgb2, rgb3, rgb4); // rgb2,4 ignored

        dest[iDestOfs1] = rgb1.toRgba();
        dest[iDestOfs2] = rgb3.toRgba();
      }
    }

    if(iY < destH) {
      // if the height is odd, write 1 line
      int iSrcLumaOfs1 = iLumaLineOfsStart, iSrcLumaOfs2 = iLumaLineOfsStart + lumaW, iSrcChromaOfs = iChromaLineOfsStart, iDestOfs1 = iDestLineOfsStart;

      int iX = 0;
      for(; iX < iDestWidthSub1; iX += 2, iSrcChromaOfs++) {
        psxycc.cr = cr[iSrcChromaOfs];
        psxycc.cb = cb[iSrcChromaOfs];

        psxycc.y1 = luma[iSrcLumaOfs1++];
        psxycc.y2 = luma[iSrcLumaOfs1++];
        psxycc.y3 = luma[iSrcLumaOfs2++];
        psxycc.y4 = luma[iSrcLumaOfs2++];

        psxycc.toRgb(rgb1, rgb2, rgb3, rgb4); // rgb3,4 ignored

        dest[iDestOfs1++] = rgb1.toRgba();
        dest[iDestOfs1++] = rgb2.toRgba();
      }

      if(iX < destW) {
        // if the width is odd, add 1 pixel
        psxycc.cr = cr[iSrcChromaOfs];
        psxycc.cb = cb[iSrcChromaOfs];

        psxycc.y1 = luma[iSrcLumaOfs1];
        psxycc.y2 = luma[iSrcLumaOfs1];
        psxycc.y3 = luma[iSrcLumaOfs2];
        psxycc.y4 = luma[iSrcLumaOfs2];

        psxycc.toRgb(rgb1, rgb2, rgb3, rgb4); // rgb2,3,4 ignored

        dest[iDestOfs1] = rgb1.toRgba();
      }
    }
  }
}
