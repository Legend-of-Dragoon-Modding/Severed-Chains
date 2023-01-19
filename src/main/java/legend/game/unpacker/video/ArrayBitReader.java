/*
 * jPSXdec: PlayStation 1 Media Decoder/Converter in Java
 * Copyright (C) 2007-2019  Michael Sabin
 * All rights reserved.
 *
 * Redistribution and use of the jPSXdec code or any derivative works are
 * permitted provided that the following conditions are met:
 *
 *  * Redistributions may not be sold, nor may they be used in commercial
 *    or revenue-generating business activities.
 *
 *  * Redistributions that are modified from the original source must
 *    include the complete source code, including the source code for all
 *    components used by a binary built from the modified sources. However, as
 *    a special exception, the source code distributed need not include
 *    anything that is normally distributed (in either source or binary form)
 *    with the major components (compiler, kernel, and so on) of the operating
 *    system on which the executable runs, unless that component itself
 *    accompanies the executable.
 *
 *  * Redistributions must reproduce the above copyright notice, this list
 *    of conditions and the following disclaimer in the documentation and/or
 *    other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS
 * IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED
 * TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A
 * PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER
 * OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */


package legend.game.unpacker.video;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nonnull;
import java.nio.ByteBuffer;

/**
 * A (hopefully) very fast bit reader. It can be initialized to read the bits
 * in big-endian order, or in 16-bit little-endian order.
 */
public class ArrayBitReader {
    private static final Logger LOG = LogManager.getFormatterLogger(ArrayBitReader.class);

    /**
     * Data to be read as a binary stream.
     */
    @Nonnull
    private final ByteBuffer _abData;
    /**
     * Size of the data (ignores data array size).
     */
    protected final int _iDataSize;
    /**
     * If 16-bit words should be read in big or little endian order.
     */
    protected int _iByteOffset;
    /**
     * The current 16-bit word value from the source data.
     */
    protected short _siCurrentWord;
    /**
     * Bits remaining to be read from the current word.
     */
    protected int _iBitsLeft;

    /**
     * Quick lookup table to mask remaining bits.
     */
    private static final int[] BIT_MASK = {
            0x00000000,
            0x00000001, 0x00000003, 0x00000007, 0x0000000F,
            0x0000001F, 0x0000003F, 0x0000007F, 0x000000FF,
            0x000001FF, 0x000003FF, 0x000007FF, 0x00000FFF,
            0x00001FFF, 0x00003FFF, 0x00007FFF, 0x0000FFFF,
            0x0001FFFF, 0x0003FFFF, 0x0007FFFF, 0x000FFFFF,
            0x001FFFFF, 0x003FFFFF, 0x007FFFFF, 0x00FFFFFF,
            0x01FFFFFF, 0x03FFFFFF, 0x07FFFFFF, 0x0FFFFFFF,
            0x1FFFFFFF, 0x3FFFFFFF, 0x7FFFFFFF, 0xFFFFFFFF,
    };

    /**
     * Start reading from a requested point in the array with the requested
     * endian-ness.
     *
     * @param iReadStart Position in array to start reading. Must be an even number.
     */
    public ArrayBitReader(@Nonnull final ByteBuffer abData, final int iReadStart) {
        if(iReadStart < 0 || iReadStart > abData.capacity()) {
            throw new IllegalArgumentException("Read start out of array bounds.");
        }
        if((iReadStart & 1) != 0) {
            throw new IllegalArgumentException("Data start must be on word boundary.");
        }

        this._iDataSize = abData.capacity() & ~1; // trim off an extra byte if the size is not an even value

        this._iByteOffset = iReadStart + 10;
        this._abData = abData;
        this._iBitsLeft = 0;
    }

    /**
     * Reads 16-bits at the requested offset in the proper endian order.
     */
    protected short readWord(final int i) throws MdecException.EndOfStream {
        if(i + 1 >= this._iDataSize) {
            throw new MdecException.EndOfStream(MdecException.END_OF_BITSTREAM(i));
        }

        return (short)(((this._abData.get(i + 1) & 0xFF) << 8) + (this._abData.get(i) & 0xFF));
    }

    /**
     * Reads the requested number of bits.
     *
     * @param iCount expected to be from 1 to 31
     */
    public int readUnsignedBits(int iCount) throws MdecException.EndOfStream {
        if(iCount < 0 || iCount >= 32) {
            throw new IllegalArgumentException("Bits to read are out of range " + iCount);
        }
        if(iCount == 0) {
            return 0;
        }

        // want to read the next 16-bit word only when it is needed
        // so we don't try to buffer data beyond the array
        if(this._iBitsLeft == 0) {
            this._siCurrentWord = this.readWord(this._iByteOffset);
            this._iByteOffset += 2;
            this._iBitsLeft = 16;
        }

        int iRet;
        if(iCount <= this._iBitsLeft) { // iCount <= _iBitsLeft <= 16
            iRet = this._siCurrentWord >>> this._iBitsLeft - iCount & BIT_MASK[iCount];
            this._iBitsLeft -= iCount;
        } else {
            iRet = this._siCurrentWord & BIT_MASK[this._iBitsLeft];
            iCount -= this._iBitsLeft;
            this._iBitsLeft = 0;

            try {
                while(iCount >= 16) {
                    iRet = iRet << 16 | this.readWord(this._iByteOffset) & 0xFFFF;
                    this._iByteOffset += 2;
                    iCount -= 16;
                }

                if(iCount > 0) { // iCount < 16
                    this._siCurrentWord = this.readWord(this._iByteOffset);
                    this._iByteOffset += 2;
                    this._iBitsLeft = 16 - iCount;
                    iRet = iRet << iCount | (this._siCurrentWord & 0xFFFF) >>> this._iBitsLeft;
                }
            } catch(final MdecException.EndOfStream ex) {
                LOG.debug("Bitstream is about to end", ex);
                // _iBitsLeft will == 0
                return iRet << iCount;
            }
        }

        return iRet;
    }

    /**
     * Reads the requested number of bits then sets the sign
     * according to the highest bit.
     *
     * @param iCount expected to be from 0 to 31
     */
    public int readSignedBits(final int iCount) throws MdecException.EndOfStream {
        return this.readUnsignedBits(iCount) << 32 - iCount >> 32 - iCount; // extend sign bit
    }

    /**
     * @param iCount expected to be from 1 to 31
     */
    public int peekUnsignedBits(final int iCount) throws MdecException.EndOfStream {
        final int iSaveOffs = this._iByteOffset;
        final int iSaveBitsLeft = this._iBitsLeft;
        final short siSaveCurrentWord = this._siCurrentWord;
        try {
            return this.readUnsignedBits(iCount);
        } finally {
            this._iByteOffset = iSaveOffs;
            this._iBitsLeft = iSaveBitsLeft;
            this._siCurrentWord = siSaveCurrentWord;
        }
    }

    public void skipBits(final int iCount) throws MdecException.EndOfStream {
        this._iBitsLeft -= iCount;
        if(this._iBitsLeft < 0) {
            // same as _iByteOffset += -(_iBitsLeft / 16)*2;
            this._iByteOffset += -this._iBitsLeft >> 4 << 1;
            // same as _iBitsLeft = _iBitsLeft % 16;
            this._iBitsLeft = -(-this._iBitsLeft & 0xf);
            if(this._iByteOffset > this._iDataSize) { // clearly out of bounds
                this._iBitsLeft = 0;
                this._iByteOffset = this._iDataSize;
                throw new MdecException.EndOfStream(MdecException.END_OF_BITSTREAM(this._iByteOffset));
            } else if(this._iBitsLeft < 0) { // _iBitsLeft should be <= 0
                if(this._iByteOffset == this._iDataSize) { // also out of bounds
                    this._iBitsLeft = 0;
                    throw new MdecException.EndOfStream(MdecException.END_OF_BITSTREAM(this._iByteOffset));
                }
                this._iBitsLeft += 16;
                this._siCurrentWord = this.readWord(this._iByteOffset);
                this._iByteOffset += 2;
            }
        }
    }
}

