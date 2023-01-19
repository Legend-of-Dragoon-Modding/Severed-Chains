package legend.game.unpacker.video;

import legend.core.MathHelper;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;

public class Frame {
    public static BufferedImage generate(final ByteBuffer data, int sector) {
        int chunkCount = data.get(sector * 0x930 + 30);
        ByteBuffer frameData = ByteBuffer
                .allocate(data.getInt(sector * 0x930 + 36))
                .order(ByteOrder.LITTLE_ENDIAN);
        int width = data.getShort(sector * 0x930 + 40);
        int height = data.getShort(sector * 0x930 + 42);
        int compressedCodesSize = data.getShort(sector * 0x930 + 64);

        for (int i = 0; i < chunkCount; i++) {
            frameData.put(data.slice(sector * 0x930 + 56,
                    Math.min(0x7E0 ,frameData.capacity() - i * 0x7E0)));
            sector++;
            sector += (sector & (sector >>> 1) & (sector >>> 2 )) & 1;
        }

        return Decode(frameData, width, height, compressedCodesSize);
    }

    private static BufferedImage Decode(ByteBuffer data, int width, int height, int compressedCodesSize) {
        data.position(10);
        final int lumaW = width + 15;
        final int lumaH = height + 15;
        final int blockW = lumaW / 16;
        final int blockH = lumaH / 16;
        final int chromaW = lumaW / 2;
        final int chromaH = lumaH / 2;

        final int macroblockCount = blockW * blockH;
        final int uncompressedSize = macroblockCount * 6 * 2;
        final ByteBuffer initialBlockCodes = ByteBuffer.allocate(uncompressedSize);

        outer:
        while(initialBlockCodes.position() < uncompressedSize) {
            final int flags = data.get();
            int mask = 1;

            for(int bit = 0; bit < 8; bit++) {
                if((flags & mask) == 0) {
                    initialBlockCodes.put(data.get());
                } else {
                    final int copySize = (data.get() & 0xff) + 3;
                    int copyOffset = data.get() & 0xff;

                    if((copyOffset & 0x80) != 0) {
                        copyOffset = (copyOffset & 0x7f) << 8 | data.get() & 0xff;
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

        final int[] chromaMacroBlockOffsetLookup = new int[macroblockCount];
        final int[] lumaBlockOffsetLookup = new int[blockW * blockH * 4];

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
        final ArrayBitReader bitReader = new ArrayBitReader(data, compressedCodesSize);
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
                    coefficients[0] = dc * QUANTIZATION_MATRIX[0];
                    nonZeroCount = 1;
                } else {
                    nonZeroCount = 0;
                }

                while(getNextVlc(vlc, bitReader)) {
                    vectorPos += vlc.zeroes + 1;

                    if(vlc.coefficient != 0) {
                        final int zigzagPos = REVERSE_ZIGZAG[vectorPos];
                        coefficients[zigzagPos] = vlc.coefficient * QUANTIZATION_MATRIX[zigzagPos] * blockQuant + 4 >> 3; // (int)Math.round(i / 8.0)
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
        int[] framePixels = new int[width * height];
        readDecodedRgb(chromaW, lumaW, cr, cb, luma, width, height, framePixels, 0, width);
        return getImageFromArray(framePixels, width, height);
    }

    public static BufferedImage getImageFromArray(int[] pixels, int width, int height) {
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        final int[] a = ( (DataBufferInt) image.getRaster().getDataBuffer() ).getData();
        System.arraycopy(pixels, 0, a, 0, pixels.length);
        BufferedImage convertedImg = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_3BYTE_BGR);
        convertedImg.getGraphics().drawImage(image, 0, 0, null);
        return convertedImg;
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

    private static void idct(final int[] idctMatrix, final int iOutputOffset, final int[] output) {
        final long[] _aTemp = new long[64];
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

                output[iOutputOffset + x + y * 8] = (int) MathHelper.shrRound(tempSum, 32);
            }
        }
    }

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



    private static final int[] REVERSE_ZIGZAG = {
            0,  1,  8, 16,  9,  2,  3, 10,
            17, 24, 32, 25, 18, 11,  4,  5,
            12, 19, 26, 33, 40, 48, 41, 34,
            27, 20, 13,  6,  7, 14, 21, 28,
            35, 42, 49, 56, 57, 50, 43, 36,
            29, 22, 15, 23, 30, 37, 44, 51,
            58, 59, 52, 45, 38, 31, 39, 46,
            53, 60, 61, 54, 47, 55, 62, 63,
    };

    private static final int[] QUANTIZATION_MATRIX = {
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

}
