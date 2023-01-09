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

import javax.annotation.Nonnull;
import java.util.Iterator;
import java.util.TreeSet;

import static legend.game.unpacker.video.BitStreamCode.LONGEST_BITSTREAM_CODE_17BITS;
import static legend.game.unpacker.video.BitStreamCode._00000000000100000;
import static legend.game.unpacker.video.BitStreamCode._00000000000100001;
import static legend.game.unpacker.video.BitStreamCode._00000000000100010;
import static legend.game.unpacker.video.BitStreamCode._00000000000100011;
import static legend.game.unpacker.video.BitStreamCode._00000000000100100;
import static legend.game.unpacker.video.BitStreamCode._00000000000100101;
import static legend.game.unpacker.video.BitStreamCode._00000000000100110;
import static legend.game.unpacker.video.BitStreamCode._00000000000100111;
import static legend.game.unpacker.video.BitStreamCode._00000000000101000;
import static legend.game.unpacker.video.BitStreamCode._00000000000101001;
import static legend.game.unpacker.video.BitStreamCode._00000000000101010;
import static legend.game.unpacker.video.BitStreamCode._00000000000101011;
import static legend.game.unpacker.video.BitStreamCode._00000000000101100;
import static legend.game.unpacker.video.BitStreamCode._00000000000101101;
import static legend.game.unpacker.video.BitStreamCode._00000000000101110;
import static legend.game.unpacker.video.BitStreamCode._00000000000101111;
import static legend.game.unpacker.video.BitStreamCode._00000000000110000;
import static legend.game.unpacker.video.BitStreamCode._00000000000110001;
import static legend.game.unpacker.video.BitStreamCode._00000000000110010;
import static legend.game.unpacker.video.BitStreamCode._00000000000110011;
import static legend.game.unpacker.video.BitStreamCode._00000000000110100;
import static legend.game.unpacker.video.BitStreamCode._00000000000110101;
import static legend.game.unpacker.video.BitStreamCode._00000000000110110;
import static legend.game.unpacker.video.BitStreamCode._00000000000110111;
import static legend.game.unpacker.video.BitStreamCode._00000000000111000;
import static legend.game.unpacker.video.BitStreamCode._00000000000111001;
import static legend.game.unpacker.video.BitStreamCode._00000000000111010;
import static legend.game.unpacker.video.BitStreamCode._00000000000111011;
import static legend.game.unpacker.video.BitStreamCode._00000000000111100;
import static legend.game.unpacker.video.BitStreamCode._00000000000111101;
import static legend.game.unpacker.video.BitStreamCode._00000000000111110;
import static legend.game.unpacker.video.BitStreamCode._00000000000111111;
import static legend.game.unpacker.video.BitStreamCode._0000000000100000_;
import static legend.game.unpacker.video.BitStreamCode._0000000000100001_;
import static legend.game.unpacker.video.BitStreamCode._0000000000100010_;
import static legend.game.unpacker.video.BitStreamCode._0000000000100011_;
import static legend.game.unpacker.video.BitStreamCode._0000000000100100_;
import static legend.game.unpacker.video.BitStreamCode._0000000000100101_;
import static legend.game.unpacker.video.BitStreamCode._0000000000100110_;
import static legend.game.unpacker.video.BitStreamCode._0000000000100111_;
import static legend.game.unpacker.video.BitStreamCode._0000000000101000_;
import static legend.game.unpacker.video.BitStreamCode._0000000000101001_;
import static legend.game.unpacker.video.BitStreamCode._0000000000101010_;
import static legend.game.unpacker.video.BitStreamCode._0000000000101011_;
import static legend.game.unpacker.video.BitStreamCode._0000000000101100_;
import static legend.game.unpacker.video.BitStreamCode._0000000000101101_;
import static legend.game.unpacker.video.BitStreamCode._0000000000101110_;
import static legend.game.unpacker.video.BitStreamCode._0000000000101111_;
import static legend.game.unpacker.video.BitStreamCode._0000000000110000_;
import static legend.game.unpacker.video.BitStreamCode._0000000000110001_;
import static legend.game.unpacker.video.BitStreamCode._0000000000110010_;
import static legend.game.unpacker.video.BitStreamCode._0000000000110011_;
import static legend.game.unpacker.video.BitStreamCode._0000000000110100_;
import static legend.game.unpacker.video.BitStreamCode._0000000000110101_;
import static legend.game.unpacker.video.BitStreamCode._0000000000110110_;
import static legend.game.unpacker.video.BitStreamCode._0000000000110111_;
import static legend.game.unpacker.video.BitStreamCode._0000000000111000_;
import static legend.game.unpacker.video.BitStreamCode._0000000000111001_;
import static legend.game.unpacker.video.BitStreamCode._0000000000111010_;
import static legend.game.unpacker.video.BitStreamCode._0000000000111011_;
import static legend.game.unpacker.video.BitStreamCode._0000000000111100_;
import static legend.game.unpacker.video.BitStreamCode._0000000000111101_;
import static legend.game.unpacker.video.BitStreamCode._0000000000111110_;
import static legend.game.unpacker.video.BitStreamCode._0000000000111111_;
import static legend.game.unpacker.video.BitStreamCode._000000000100000__;
import static legend.game.unpacker.video.BitStreamCode._000000000100001__;
import static legend.game.unpacker.video.BitStreamCode._000000000100010__;
import static legend.game.unpacker.video.BitStreamCode._000000000100011__;
import static legend.game.unpacker.video.BitStreamCode._000000000100100__;
import static legend.game.unpacker.video.BitStreamCode._000000000100101__;
import static legend.game.unpacker.video.BitStreamCode._000000000100110__;
import static legend.game.unpacker.video.BitStreamCode._000000000100111__;
import static legend.game.unpacker.video.BitStreamCode._000000000101000__;
import static legend.game.unpacker.video.BitStreamCode._000000000101001__;
import static legend.game.unpacker.video.BitStreamCode._000000000101010__;
import static legend.game.unpacker.video.BitStreamCode._000000000101011__;
import static legend.game.unpacker.video.BitStreamCode._000000000101100__;
import static legend.game.unpacker.video.BitStreamCode._000000000101101__;
import static legend.game.unpacker.video.BitStreamCode._000000000101110__;
import static legend.game.unpacker.video.BitStreamCode._000000000101111__;
import static legend.game.unpacker.video.BitStreamCode._000000000110000__;
import static legend.game.unpacker.video.BitStreamCode._000000000110001__;
import static legend.game.unpacker.video.BitStreamCode._000000000110010__;
import static legend.game.unpacker.video.BitStreamCode._000000000110011__;
import static legend.game.unpacker.video.BitStreamCode._000000000110100__;
import static legend.game.unpacker.video.BitStreamCode._000000000110101__;
import static legend.game.unpacker.video.BitStreamCode._000000000110110__;
import static legend.game.unpacker.video.BitStreamCode._000000000110111__;
import static legend.game.unpacker.video.BitStreamCode._000000000111000__;
import static legend.game.unpacker.video.BitStreamCode._000000000111001__;
import static legend.game.unpacker.video.BitStreamCode._000000000111010__;
import static legend.game.unpacker.video.BitStreamCode._000000000111011__;
import static legend.game.unpacker.video.BitStreamCode._000000000111100__;
import static legend.game.unpacker.video.BitStreamCode._000000000111101__;
import static legend.game.unpacker.video.BitStreamCode._000000000111110__;
import static legend.game.unpacker.video.BitStreamCode._000000000111111__;
import static legend.game.unpacker.video.BitStreamCode._00000000100000___;
import static legend.game.unpacker.video.BitStreamCode._00000000100001___;
import static legend.game.unpacker.video.BitStreamCode._00000000100010___;
import static legend.game.unpacker.video.BitStreamCode._00000000100011___;
import static legend.game.unpacker.video.BitStreamCode._00000000100100___;
import static legend.game.unpacker.video.BitStreamCode._00000000100101___;
import static legend.game.unpacker.video.BitStreamCode._00000000100110___;
import static legend.game.unpacker.video.BitStreamCode._00000000100111___;
import static legend.game.unpacker.video.BitStreamCode._00000000101000___;
import static legend.game.unpacker.video.BitStreamCode._00000000101001___;
import static legend.game.unpacker.video.BitStreamCode._00000000101010___;
import static legend.game.unpacker.video.BitStreamCode._00000000101011___;
import static legend.game.unpacker.video.BitStreamCode._00000000101100___;
import static legend.game.unpacker.video.BitStreamCode._00000000101101___;
import static legend.game.unpacker.video.BitStreamCode._00000000101110___;
import static legend.game.unpacker.video.BitStreamCode._00000000101111___;
import static legend.game.unpacker.video.BitStreamCode._00000000110000___;
import static legend.game.unpacker.video.BitStreamCode._00000000110001___;
import static legend.game.unpacker.video.BitStreamCode._00000000110010___;
import static legend.game.unpacker.video.BitStreamCode._00000000110011___;
import static legend.game.unpacker.video.BitStreamCode._00000000110100___;
import static legend.game.unpacker.video.BitStreamCode._00000000110101___;
import static legend.game.unpacker.video.BitStreamCode._00000000110110___;
import static legend.game.unpacker.video.BitStreamCode._00000000110111___;
import static legend.game.unpacker.video.BitStreamCode._00000000111000___;
import static legend.game.unpacker.video.BitStreamCode._00000000111001___;
import static legend.game.unpacker.video.BitStreamCode._00000000111010___;
import static legend.game.unpacker.video.BitStreamCode._00000000111011___;
import static legend.game.unpacker.video.BitStreamCode._00000000111100___;
import static legend.game.unpacker.video.BitStreamCode._00000000111101___;
import static legend.game.unpacker.video.BitStreamCode._00000000111110___;
import static legend.game.unpacker.video.BitStreamCode._00000000111111___;
import static legend.game.unpacker.video.BitStreamCode._0000000100000____;
import static legend.game.unpacker.video.BitStreamCode._0000000100001____;
import static legend.game.unpacker.video.BitStreamCode._0000000100010____;
import static legend.game.unpacker.video.BitStreamCode._0000000100011____;
import static legend.game.unpacker.video.BitStreamCode._0000000100100____;
import static legend.game.unpacker.video.BitStreamCode._0000000100101____;
import static legend.game.unpacker.video.BitStreamCode._0000000100110____;
import static legend.game.unpacker.video.BitStreamCode._0000000100111____;
import static legend.game.unpacker.video.BitStreamCode._0000000101000____;
import static legend.game.unpacker.video.BitStreamCode._0000000101001____;
import static legend.game.unpacker.video.BitStreamCode._0000000101010____;
import static legend.game.unpacker.video.BitStreamCode._0000000101011____;
import static legend.game.unpacker.video.BitStreamCode._0000000101100____;
import static legend.game.unpacker.video.BitStreamCode._0000000101101____;
import static legend.game.unpacker.video.BitStreamCode._0000000101110____;
import static legend.game.unpacker.video.BitStreamCode._0000000101111____;
import static legend.game.unpacker.video.BitStreamCode._0000000110000____;
import static legend.game.unpacker.video.BitStreamCode._0000000110001____;
import static legend.game.unpacker.video.BitStreamCode._0000000110010____;
import static legend.game.unpacker.video.BitStreamCode._0000000110011____;
import static legend.game.unpacker.video.BitStreamCode._0000000110100____;
import static legend.game.unpacker.video.BitStreamCode._0000000110101____;
import static legend.game.unpacker.video.BitStreamCode._0000000110110____;
import static legend.game.unpacker.video.BitStreamCode._0000000110111____;
import static legend.game.unpacker.video.BitStreamCode._0000000111000____;
import static legend.game.unpacker.video.BitStreamCode._0000000111001____;
import static legend.game.unpacker.video.BitStreamCode._0000000111010____;
import static legend.game.unpacker.video.BitStreamCode._0000000111011____;
import static legend.game.unpacker.video.BitStreamCode._0000000111100____;
import static legend.game.unpacker.video.BitStreamCode._0000000111101____;
import static legend.game.unpacker.video.BitStreamCode._0000000111110____;
import static legend.game.unpacker.video.BitStreamCode._0000000111111____;
import static legend.game.unpacker.video.BitStreamCode._00000010000______;
import static legend.game.unpacker.video.BitStreamCode._00000010001______;
import static legend.game.unpacker.video.BitStreamCode._00000010010______;
import static legend.game.unpacker.video.BitStreamCode._00000010011______;
import static legend.game.unpacker.video.BitStreamCode._00000010100______;
import static legend.game.unpacker.video.BitStreamCode._00000010101______;
import static legend.game.unpacker.video.BitStreamCode._00000010110______;
import static legend.game.unpacker.video.BitStreamCode._00000010111______;
import static legend.game.unpacker.video.BitStreamCode._00000011000______;
import static legend.game.unpacker.video.BitStreamCode._00000011001______;
import static legend.game.unpacker.video.BitStreamCode._00000011010______;
import static legend.game.unpacker.video.BitStreamCode._00000011011______;
import static legend.game.unpacker.video.BitStreamCode._00000011100______;
import static legend.game.unpacker.video.BitStreamCode._00000011101______;
import static legend.game.unpacker.video.BitStreamCode._00000011110______;
import static legend.game.unpacker.video.BitStreamCode._00000011111______;
import static legend.game.unpacker.video.BitStreamCode._00001000_________;
import static legend.game.unpacker.video.BitStreamCode._00001001_________;
import static legend.game.unpacker.video.BitStreamCode._00001010_________;
import static legend.game.unpacker.video.BitStreamCode._00001011_________;
import static legend.game.unpacker.video.BitStreamCode._00001100_________;
import static legend.game.unpacker.video.BitStreamCode._00001101_________;
import static legend.game.unpacker.video.BitStreamCode._00001110_________;
import static legend.game.unpacker.video.BitStreamCode._00001111_________;
import static legend.game.unpacker.video.BitStreamCode._0001000__________;
import static legend.game.unpacker.video.BitStreamCode._0001001__________;
import static legend.game.unpacker.video.BitStreamCode._0001010__________;
import static legend.game.unpacker.video.BitStreamCode._0001011__________;
import static legend.game.unpacker.video.BitStreamCode._0001100__________;
import static legend.game.unpacker.video.BitStreamCode._0001101__________;
import static legend.game.unpacker.video.BitStreamCode._0001110__________;
import static legend.game.unpacker.video.BitStreamCode._0001111__________;
import static legend.game.unpacker.video.BitStreamCode._001000000________;
import static legend.game.unpacker.video.BitStreamCode._001000001________;
import static legend.game.unpacker.video.BitStreamCode._001000010________;
import static legend.game.unpacker.video.BitStreamCode._001000011________;
import static legend.game.unpacker.video.BitStreamCode._001000100________;
import static legend.game.unpacker.video.BitStreamCode._001000101________;
import static legend.game.unpacker.video.BitStreamCode._001000110________;
import static legend.game.unpacker.video.BitStreamCode._001000111________;
import static legend.game.unpacker.video.BitStreamCode._001001000________;
import static legend.game.unpacker.video.BitStreamCode._001001001________;
import static legend.game.unpacker.video.BitStreamCode._001001010________;
import static legend.game.unpacker.video.BitStreamCode._001001011________;
import static legend.game.unpacker.video.BitStreamCode._001001100________;
import static legend.game.unpacker.video.BitStreamCode._001001101________;
import static legend.game.unpacker.video.BitStreamCode._001001110________;
import static legend.game.unpacker.video.BitStreamCode._001001111________;
import static legend.game.unpacker.video.BitStreamCode._001010___________;
import static legend.game.unpacker.video.BitStreamCode._001011___________;
import static legend.game.unpacker.video.BitStreamCode._001100___________;
import static legend.game.unpacker.video.BitStreamCode._001101___________;
import static legend.game.unpacker.video.BitStreamCode._001110___________;
import static legend.game.unpacker.video.BitStreamCode._001111___________;
import static legend.game.unpacker.video.BitStreamCode._01000____________;
import static legend.game.unpacker.video.BitStreamCode._01001____________;
import static legend.game.unpacker.video.BitStreamCode._01010____________;
import static legend.game.unpacker.video.BitStreamCode._01011____________;
import static legend.game.unpacker.video.BitStreamCode._0110_____________;
import static legend.game.unpacker.video.BitStreamCode._0111_____________;
import static legend.game.unpacker.video.BitStreamCode._110______________;
import static legend.game.unpacker.video.BitStreamCode._111______________;

/**
 * Creates ultra fast bit-stream lookup tables.
 */
public class ZeroRunLengthAcLookup implements Iterable<ZeroRunLengthAc> {
    public static class Builder {
        private final ZeroRunLengthAc[] _aoList = new ZeroRunLengthAc[BitStreamCode.getTotalCount()];

        private final TreeSet<MdecCode> _duplicateCodeChecker = new TreeSet<>();

        /**
         * To set the zero run-length and AC coefficient for a single bit stream code.
         */
        public @Nonnull Builder set(@Nonnull final BitStreamCode bitStreamCode, final int iZeroRunLength, final int iAcCoefficient) {
            return this.add(new ZeroRunLengthAc(bitStreamCode, iZeroRunLength, iAcCoefficient));
        }

        /**
         * To set the zero run-length and AC coefficient for a single bit stream code.
         */
        public @Nonnull Builder add(@Nonnull final ZeroRunLengthAc zrlac) {
            final MdecCode mdecCode = zrlac.getMdecCodeCopy();
            if(mdecCode != null) {
                if(!this._duplicateCodeChecker.add(mdecCode)) {
                    throw new RuntimeException("Already got MDEC code " + mdecCode);
                }
            }
            final int iIndex = zrlac.getBitStreamCode().ordinal();
            if(this._aoList[iIndex] != null) {
                throw new RuntimeException("Trying to replace " + this._aoList[iIndex] + " with " + zrlac + " at " + iIndex);
            }
            this._aoList[iIndex] = zrlac;
            return this;
        }

        // To set the zero run-length and AC coefficient for the 2 codes covered with a sign bit.
        //
        public ZeroRunLengthAcLookup.Builder _11s(final int zr, final int aac) {
            return this.set(_110______________, _111______________, zr, aac);
        }

        // ------------------------------------------------------------------------------
        public ZeroRunLengthAcLookup.Builder _011s(final int zr, final int aac) {
            return this.set(_0110_____________, _0111_____________, zr, aac);
        }

        public ZeroRunLengthAcLookup.Builder _0100s(final int zr, final int aac) {
            return this.set(_01000____________, _01001____________, zr, aac);
        }

        public ZeroRunLengthAcLookup.Builder _0101s(final int zr, final int aac) {
            return this.set(_01010____________, _01011____________, zr, aac);
        }

        public ZeroRunLengthAcLookup.Builder _00101s(final int zr, final int aac) {
            return this.set(_001010___________, _001011___________, zr, aac);
        }

        public ZeroRunLengthAcLookup.Builder _00110s(final int zr, final int aac) {
            return this.set(_001100___________, _001101___________, zr, aac);
        }

        public ZeroRunLengthAcLookup.Builder _00111s(final int zr, final int aac) {
            return this.set(_001110___________, _001111___________, zr, aac);
        }

        public ZeroRunLengthAcLookup.Builder _000100s(final int zr, final int aac) {
            return this.set(_0001000__________, _0001001__________, zr, aac);
        }

        public ZeroRunLengthAcLookup.Builder _000101s(final int zr, final int aac) {
            return this.set(_0001010__________, _0001011__________, zr, aac);
        }

        public ZeroRunLengthAcLookup.Builder _000110s(final int zr, final int aac) {
            return this.set(_0001100__________, _0001101__________, zr, aac);
        }

        public ZeroRunLengthAcLookup.Builder _000111s(final int zr, final int aac) {
            return this.set(_0001110__________, _0001111__________, zr, aac);
        }

        public ZeroRunLengthAcLookup.Builder _0000100s(final int zr, final int aac) {
            return this.set(_00001000_________, _00001001_________, zr, aac);
        }

        public ZeroRunLengthAcLookup.Builder _0000101s(final int zr, final int aac) {
            return this.set(_00001010_________, _00001011_________, zr, aac);
        }

        public ZeroRunLengthAcLookup.Builder _0000110s(final int zr, final int aac) {
            return this.set(_00001100_________, _00001101_________, zr, aac);
        }

        public ZeroRunLengthAcLookup.Builder _0000111s(final int zr, final int aac) {
            return this.set(_00001110_________, _00001111_________, zr, aac);
        }

        public ZeroRunLengthAcLookup.Builder _00100000s(final int zr, final int aac) {
            return this.set(_001000000________, _001000001________, zr, aac);
        }

        public ZeroRunLengthAcLookup.Builder _00100001s(final int zr, final int aac) {
            return this.set(_001000010________, _001000011________, zr, aac);
        }

        public ZeroRunLengthAcLookup.Builder _00100010s(final int zr, final int aac) {
            return this.set(_001000100________, _001000101________, zr, aac);
        }

        public ZeroRunLengthAcLookup.Builder _00100011s(final int zr, final int aac) {
            return this.set(_001000110________, _001000111________, zr, aac);
        }

        public ZeroRunLengthAcLookup.Builder _00100100s(final int zr, final int aac) {
            return this.set(_001001000________, _001001001________, zr, aac);
        }

        public ZeroRunLengthAcLookup.Builder _00100101s(final int zr, final int aac) {
            return this.set(_001001010________, _001001011________, zr, aac);
        }

        public ZeroRunLengthAcLookup.Builder _00100110s(final int zr, final int aac) {
            return this.set(_001001100________, _001001101________, zr, aac);
        }

        public ZeroRunLengthAcLookup.Builder _00100111s(final int zr, final int aac) {
            return this.set(_001001110________, _001001111________, zr, aac);
        }

        // ------------------------------------------------------------------------------
        public ZeroRunLengthAcLookup.Builder _0000001000s(final int zr, final int aac) {
            return this.set(_00000010000______, _00000010001______, zr, aac);
        }

        public ZeroRunLengthAcLookup.Builder _0000001001s(final int zr, final int aac) {
            return this.set(_00000010010______, _00000010011______, zr, aac);
        }

        public ZeroRunLengthAcLookup.Builder _0000001010s(final int zr, final int aac) {
            return this.set(_00000010100______, _00000010101______, zr, aac);
        }

        public ZeroRunLengthAcLookup.Builder _0000001011s(final int zr, final int aac) {
            return this.set(_00000010110______, _00000010111______, zr, aac);
        }

        public ZeroRunLengthAcLookup.Builder _0000001100s(final int zr, final int aac) {
            return this.set(_00000011000______, _00000011001______, zr, aac);
        }

        public ZeroRunLengthAcLookup.Builder _0000001101s(final int zr, final int aac) {
            return this.set(_00000011010______, _00000011011______, zr, aac);
        }

        public ZeroRunLengthAcLookup.Builder _0000001110s(final int zr, final int aac) {
            return this.set(_00000011100______, _00000011101______, zr, aac);
        }

        public ZeroRunLengthAcLookup.Builder _0000001111s(final int zr, final int aac) {
            return this.set(_00000011110______, _00000011111______, zr, aac);
        }

        public ZeroRunLengthAcLookup.Builder _000000010000s(final int zr, final int aac) {
            return this.set(_0000000100000____, _0000000100001____, zr, aac);
        }

        public ZeroRunLengthAcLookup.Builder _000000010001s(final int zr, final int aac) {
            return this.set(_0000000100010____, _0000000100011____, zr, aac);
        }

        public ZeroRunLengthAcLookup.Builder _000000010010s(final int zr, final int aac) {
            return this.set(_0000000100100____, _0000000100101____, zr, aac);
        }

        public ZeroRunLengthAcLookup.Builder _000000010011s(final int zr, final int aac) {
            return this.set(_0000000100110____, _0000000100111____, zr, aac);
        }

        public ZeroRunLengthAcLookup.Builder _000000010100s(final int zr, final int aac) {
            return this.set(_0000000101000____, _0000000101001____, zr, aac);
        }

        public ZeroRunLengthAcLookup.Builder _000000010101s(final int zr, final int aac) {
            return this.set(_0000000101010____, _0000000101011____, zr, aac);
        }

        public ZeroRunLengthAcLookup.Builder _000000010110s(final int zr, final int aac) {
            return this.set(_0000000101100____, _0000000101101____, zr, aac);
        }

        public ZeroRunLengthAcLookup.Builder _000000010111s(final int zr, final int aac) {
            return this.set(_0000000101110____, _0000000101111____, zr, aac);
        }

        public ZeroRunLengthAcLookup.Builder _000000011000s(final int zr, final int aac) {
            return this.set(_0000000110000____, _0000000110001____, zr, aac);
        }

        public ZeroRunLengthAcLookup.Builder _000000011001s(final int zr, final int aac) {
            return this.set(_0000000110010____, _0000000110011____, zr, aac);
        }

        public ZeroRunLengthAcLookup.Builder _000000011010s(final int zr, final int aac) {
            return this.set(_0000000110100____, _0000000110101____, zr, aac);
        }

        public ZeroRunLengthAcLookup.Builder _000000011011s(final int zr, final int aac) {
            return this.set(_0000000110110____, _0000000110111____, zr, aac);
        }

        public ZeroRunLengthAcLookup.Builder _000000011100s(final int zr, final int aac) {
            return this.set(_0000000111000____, _0000000111001____, zr, aac);
        }

        public ZeroRunLengthAcLookup.Builder _000000011101s(final int zr, final int aac) {
            return this.set(_0000000111010____, _0000000111011____, zr, aac);
        }

        public ZeroRunLengthAcLookup.Builder _000000011110s(final int zr, final int aac) {
            return this.set(_0000000111100____, _0000000111101____, zr, aac);
        }

        public ZeroRunLengthAcLookup.Builder _000000011111s(final int zr, final int aac) {
            return this.set(_0000000111110____, _0000000111111____, zr, aac);
        }

        public ZeroRunLengthAcLookup.Builder _0000000010000s(final int zr, final int aac) {
            return this.set(_00000000100000___, _00000000100001___, zr, aac);
        }

        public ZeroRunLengthAcLookup.Builder _0000000010001s(final int zr, final int aac) {
            return this.set(_00000000100010___, _00000000100011___, zr, aac);
        }

        public ZeroRunLengthAcLookup.Builder _0000000010010s(final int zr, final int aac) {
            return this.set(_00000000100100___, _00000000100101___, zr, aac);
        }

        public ZeroRunLengthAcLookup.Builder _0000000010011s(final int zr, final int aac) {
            return this.set(_00000000100110___, _00000000100111___, zr, aac);
        }

        public ZeroRunLengthAcLookup.Builder _0000000010100s(final int zr, final int aac) {
            return this.set(_00000000101000___, _00000000101001___, zr, aac);
        }

        public ZeroRunLengthAcLookup.Builder _0000000010101s(final int zr, final int aac) {
            return this.set(_00000000101010___, _00000000101011___, zr, aac);
        }

        public ZeroRunLengthAcLookup.Builder _0000000010110s(final int zr, final int aac) {
            return this.set(_00000000101100___, _00000000101101___, zr, aac);
        }

        public ZeroRunLengthAcLookup.Builder _0000000010111s(final int zr, final int aac) {
            return this.set(_00000000101110___, _00000000101111___, zr, aac);
        }

        public ZeroRunLengthAcLookup.Builder _0000000011000s(final int zr, final int aac) {
            return this.set(_00000000110000___, _00000000110001___, zr, aac);
        }

        public ZeroRunLengthAcLookup.Builder _0000000011001s(final int zr, final int aac) {
            return this.set(_00000000110010___, _00000000110011___, zr, aac);
        }

        public ZeroRunLengthAcLookup.Builder _0000000011010s(final int zr, final int aac) {
            return this.set(_00000000110100___, _00000000110101___, zr, aac);
        }

        public ZeroRunLengthAcLookup.Builder _0000000011011s(final int zr, final int aac) {
            return this.set(_00000000110110___, _00000000110111___, zr, aac);
        }

        public ZeroRunLengthAcLookup.Builder _0000000011100s(final int zr, final int aac) {
            return this.set(_00000000111000___, _00000000111001___, zr, aac);
        }

        public ZeroRunLengthAcLookup.Builder _0000000011101s(final int zr, final int aac) {
            return this.set(_00000000111010___, _00000000111011___, zr, aac);
        }

        public ZeroRunLengthAcLookup.Builder _0000000011110s(final int zr, final int aac) {
            return this.set(_00000000111100___, _00000000111101___, zr, aac);
        }

        public ZeroRunLengthAcLookup.Builder _0000000011111s(final int zr, final int aac) {
            return this.set(_00000000111110___, _00000000111111___, zr, aac);
        }

        // ------------------------------------------------------------------------------
        public ZeroRunLengthAcLookup.Builder _00000000010000s(final int zr, final int aac) {
            return this.set(_000000000100000__, _000000000100001__, zr, aac);
        }

        public ZeroRunLengthAcLookup.Builder _00000000010001s(final int zr, final int aac) {
            return this.set(_000000000100010__, _000000000100011__, zr, aac);
        }

        public ZeroRunLengthAcLookup.Builder _00000000010010s(final int zr, final int aac) {
            return this.set(_000000000100100__, _000000000100101__, zr, aac);
        }

        public ZeroRunLengthAcLookup.Builder _00000000010011s(final int zr, final int aac) {
            return this.set(_000000000100110__, _000000000100111__, zr, aac);
        }

        public ZeroRunLengthAcLookup.Builder _00000000010100s(final int zr, final int aac) {
            return this.set(_000000000101000__, _000000000101001__, zr, aac);
        }

        public ZeroRunLengthAcLookup.Builder _00000000010101s(final int zr, final int aac) {
            return this.set(_000000000101010__, _000000000101011__, zr, aac);
        }

        public ZeroRunLengthAcLookup.Builder _00000000010110s(final int zr, final int aac) {
            return this.set(_000000000101100__, _000000000101101__, zr, aac);
        }

        public ZeroRunLengthAcLookup.Builder _00000000010111s(final int zr, final int aac) {
            return this.set(_000000000101110__, _000000000101111__, zr, aac);
        }

        public ZeroRunLengthAcLookup.Builder _00000000011000s(final int zr, final int aac) {
            return this.set(_000000000110000__, _000000000110001__, zr, aac);
        }

        public ZeroRunLengthAcLookup.Builder _00000000011001s(final int zr, final int aac) {
            return this.set(_000000000110010__, _000000000110011__, zr, aac);
        }

        public ZeroRunLengthAcLookup.Builder _00000000011010s(final int zr, final int aac) {
            return this.set(_000000000110100__, _000000000110101__, zr, aac);
        }

        public ZeroRunLengthAcLookup.Builder _00000000011011s(final int zr, final int aac) {
            return this.set(_000000000110110__, _000000000110111__, zr, aac);
        }

        public ZeroRunLengthAcLookup.Builder _00000000011100s(final int zr, final int aac) {
            return this.set(_000000000111000__, _000000000111001__, zr, aac);
        }

        public ZeroRunLengthAcLookup.Builder _00000000011101s(final int zr, final int aac) {
            return this.set(_000000000111010__, _000000000111011__, zr, aac);
        }

        public ZeroRunLengthAcLookup.Builder _00000000011110s(final int zr, final int aac) {
            return this.set(_000000000111100__, _000000000111101__, zr, aac);
        }

        public ZeroRunLengthAcLookup.Builder _00000000011111s(final int zr, final int aac) {
            return this.set(_000000000111110__, _000000000111111__, zr, aac);
        }

        public ZeroRunLengthAcLookup.Builder _000000000010000s(final int zr, final int aac) {
            return this.set(_0000000000100000_, _0000000000100001_, zr, aac);
        }

        public ZeroRunLengthAcLookup.Builder _000000000010001s(final int zr, final int aac) {
            return this.set(_0000000000100010_, _0000000000100011_, zr, aac);
        }

        public ZeroRunLengthAcLookup.Builder _000000000010010s(final int zr, final int aac) {
            return this.set(_0000000000100100_, _0000000000100101_, zr, aac);
        }

        public ZeroRunLengthAcLookup.Builder _000000000010011s(final int zr, final int aac) {
            return this.set(_0000000000100110_, _0000000000100111_, zr, aac);
        }

        public ZeroRunLengthAcLookup.Builder _000000000010100s(final int zr, final int aac) {
            return this.set(_0000000000101000_, _0000000000101001_, zr, aac);
        }

        public ZeroRunLengthAcLookup.Builder _000000000010101s(final int zr, final int aac) {
            return this.set(_0000000000101010_, _0000000000101011_, zr, aac);
        }

        public ZeroRunLengthAcLookup.Builder _000000000010110s(final int zr, final int aac) {
            return this.set(_0000000000101100_, _0000000000101101_, zr, aac);
        }

        public ZeroRunLengthAcLookup.Builder _000000000010111s(final int zr, final int aac) {
            return this.set(_0000000000101110_, _0000000000101111_, zr, aac);
        }

        public ZeroRunLengthAcLookup.Builder _000000000011000s(final int zr, final int aac) {
            return this.set(_0000000000110000_, _0000000000110001_, zr, aac);
        }

        public ZeroRunLengthAcLookup.Builder _000000000011001s(final int zr, final int aac) {
            return this.set(_0000000000110010_, _0000000000110011_, zr, aac);
        }

        public ZeroRunLengthAcLookup.Builder _000000000011010s(final int zr, final int aac) {
            return this.set(_0000000000110100_, _0000000000110101_, zr, aac);
        }

        public ZeroRunLengthAcLookup.Builder _000000000011011s(final int zr, final int aac) {
            return this.set(_0000000000110110_, _0000000000110111_, zr, aac);
        }

        public ZeroRunLengthAcLookup.Builder _000000000011100s(final int zr, final int aac) {
            return this.set(_0000000000111000_, _0000000000111001_, zr, aac);
        }

        public ZeroRunLengthAcLookup.Builder _000000000011101s(final int zr, final int aac) {
            return this.set(_0000000000111010_, _0000000000111011_, zr, aac);
        }

        public ZeroRunLengthAcLookup.Builder _000000000011110s(final int zr, final int aac) {
            return this.set(_0000000000111100_, _0000000000111101_, zr, aac);
        }

        public ZeroRunLengthAcLookup.Builder _000000000011111s(final int zr, final int aac) {
            return this.set(_0000000000111110_, _0000000000111111_, zr, aac);
        }

        public ZeroRunLengthAcLookup.Builder _0000000000010000s(final int zr, final int aac) {
            return this.set(_00000000000100000, _00000000000100001, zr, aac);
        }

        public ZeroRunLengthAcLookup.Builder _0000000000010001s(final int zr, final int aac) {
            return this.set(_00000000000100010, _00000000000100011, zr, aac);
        }

        public ZeroRunLengthAcLookup.Builder _0000000000010010s(final int zr, final int aac) {
            return this.set(_00000000000100100, _00000000000100101, zr, aac);
        }

        public ZeroRunLengthAcLookup.Builder _0000000000010011s(final int zr, final int aac) {
            return this.set(_00000000000100110, _00000000000100111, zr, aac);
        }

        public ZeroRunLengthAcLookup.Builder _0000000000010100s(final int zr, final int aac) {
            return this.set(_00000000000101000, _00000000000101001, zr, aac);
        }

        public ZeroRunLengthAcLookup.Builder _0000000000010101s(final int zr, final int aac) {
            return this.set(_00000000000101010, _00000000000101011, zr, aac);
        }

        public ZeroRunLengthAcLookup.Builder _0000000000010110s(final int zr, final int aac) {
            return this.set(_00000000000101100, _00000000000101101, zr, aac);
        }

        public ZeroRunLengthAcLookup.Builder _0000000000010111s(final int zr, final int aac) {
            return this.set(_00000000000101110, _00000000000101111, zr, aac);
        }

        public ZeroRunLengthAcLookup.Builder _0000000000011000s(final int zr, final int aac) {
            return this.set(_00000000000110000, _00000000000110001, zr, aac);
        }

        public ZeroRunLengthAcLookup.Builder _0000000000011001s(final int zr, final int aac) {
            return this.set(_00000000000110010, _00000000000110011, zr, aac);
        }

        public ZeroRunLengthAcLookup.Builder _0000000000011010s(final int zr, final int aac) {
            return this.set(_00000000000110100, _00000000000110101, zr, aac);
        }

        public ZeroRunLengthAcLookup.Builder _0000000000011011s(final int zr, final int aac) {
            return this.set(_00000000000110110, _00000000000110111, zr, aac);
        }

        public ZeroRunLengthAcLookup.Builder _0000000000011100s(final int zr, final int aac) {
            return this.set(_00000000000111000, _00000000000111001, zr, aac);
        }

        public ZeroRunLengthAcLookup.Builder _0000000000011101s(final int zr, final int aac) {
            return this.set(_00000000000111010, _00000000000111011, zr, aac);
        }

        public ZeroRunLengthAcLookup.Builder _0000000000011110s(final int zr, final int aac) {
            return this.set(_00000000000111100, _00000000000111101, zr, aac);
        }

        public ZeroRunLengthAcLookup.Builder _0000000000011111s(final int zr, final int aac) {
            return this.set(_00000000000111110, _00000000000111111, zr, aac);
        }

        private @Nonnull ZeroRunLengthAcLookup.Builder set(@Nonnull final BitStreamCode positiveBitStreamCodeEndsWith0, @Nonnull final BitStreamCode negitiveBitStreamCodeEndsWith1, final int iZeroRunLength, final int iAbsoluteAcCoefficient) {
            this.set(positiveBitStreamCodeEndsWith0, _0001101__________, iZeroRunLength, iAbsoluteAcCoefficient);
            this.set(negitiveBitStreamCodeEndsWith1, _0001101__________, iZeroRunLength, -iAbsoluteAcCoefficient);
            return this;
        }

        public @Nonnull ZeroRunLengthAcLookup build() {
            return new ZeroRunLengthAcLookup(this._aoList);
        }
    }

    @Nonnull
    private final ZeroRunLengthAc[] _aoList;

    /**
     * Table to look up '10' and '11s' codes using all but the first (1) bit.
     */
    private final ZeroRunLengthAc[] _aoTable_1xx = new ZeroRunLengthAc[4];
    /**
     * Table to look up codes '011s' to '00100111s' using all but the first (0) bit.
     * Given a bit code in that range, strip the leading zero bit, then pad
     * any extra trailing bits to make an 8 bit value. Use that value as the
     * index in this table to get the corresponding code.
     */
    private final ZeroRunLengthAc[] _aoTable_0xxxxxxx = new ZeroRunLengthAc[256];
    /**
     * Table to look up codes '0000001000s' to '0000000011111s' using all but
     * the first 6 zero bits.
     * Given a bit code in that range, strip the leading 6 zero bits, then pad
     * any extra trailing bits to make an 8 bit value. Use that value as the
     * index in this table to get the corresponding code.
     */
    private final ZeroRunLengthAc[] _aoTable_000000xxxxxxxx = new ZeroRunLengthAc[256];
    /**
     * Table to look up codes '00000000010000s' to '0000000000011111s' using
     * all but the first 9 zero bits.
     * Given a bit code in that range, strip the leading 9 zero bits, then pad
     * any extra trailing bits to make an 8 bit value. Use that value as the
     * index in this table to get the corresponding code.
     */
    private final ZeroRunLengthAc[] _aoTable_000000000xxxxxxxx = new ZeroRunLengthAc[256];

    private ZeroRunLengthAcLookup(@Nonnull final ZeroRunLengthAc[] aoList) {
        this._aoList = aoList;
        for(int i = 0; i < aoList.length; i++) {
            final ZeroRunLengthAc zrlac = aoList[i];
            final BitStreamCode bitStreamCode = BitStreamCode.get(i);
            if(zrlac == null) {
                throw new IllegalStateException("Table incomplete: missing " + bitStreamCode);
            }
            this.setBits(bitStreamCode, zrlac);
        }
    }

    /**
     * Identifies the lookup table in which to place the bit code.
     */
    private void setBits(@Nonnull final BitStreamCode bsc, @Nonnull final ZeroRunLengthAc zrlac) {
        final int iBitsRemain;
        final ZeroRunLengthAc[] aoTable;
        final int iTableStart;

        // This needs some explaination
        if(bsc.getString().startsWith("000000000")) {
            aoTable = this._aoTable_000000000xxxxxxxx;
            iBitsRemain = 8 - (bsc.getLength() - 9);
            iTableStart = Integer.parseInt(bsc.getString(), 2) << iBitsRemain;
        } else if(bsc.getString().startsWith("000000")) {
            aoTable = this._aoTable_000000xxxxxxxx;
            iBitsRemain = 8 - (bsc.getLength() - 6);
            iTableStart = Integer.parseInt(bsc.getString(), 2) << iBitsRemain;
        } else if(bsc.getString().startsWith("0")) {
            aoTable = this._aoTable_0xxxxxxx;
            iBitsRemain = 8 - (bsc.getLength() - 1);
            iTableStart = Integer.parseInt(bsc.getString(), 2) << iBitsRemain;
        } else { //                startsWith("1")
            aoTable = this._aoTable_1xx;
            iBitsRemain = 2 - (bsc.getLength() - 1);
            iTableStart = Integer.parseInt(bsc.getString().substring(1), 2) << iBitsRemain;
        }

        final int iTableEntriesToAssociate = (1 << iBitsRemain);
        for(int i = 0; i < iTableEntriesToAssociate; i++) {
            if(aoTable[iTableStart + i] != null) {
                throw new RuntimeException("Trying to replace " + aoTable[iTableStart + i] + " with " + zrlac);
            }
            aoTable[iTableStart + i] = zrlac;
        }
    }

    // #########################################################################

    public @Nonnull Iterator<ZeroRunLengthAc> iterator() {
        return new Iterator<>() {
            private int _i = 0;

            public boolean hasNext() {
                return this._i < ZeroRunLengthAcLookup.this._aoList.length;
            }

            public @Nonnull ZeroRunLengthAc next() {
                return ZeroRunLengthAcLookup.this._aoList[this._i++];
            }

            public void remove() {
                throw new UnsupportedOperationException("List is immutable");
            }
        };
    }

    private static final int b10000000000000000 = 0x10000;
    private static final int b01111100000000000 = 0x0F800;
    private static final int b00000011100000000 = 0x00700;
    private static final int b00000000011100000 = 0x000E0;

    /**
     * Converts bits to the equivalent {@link BitstreamToMdecLookup}.
     * 17 bits need to be supplied to decode (the longest bit code).
     * Bits should start at the least-significant bit.
     * Bits beyond the 17 least-significant bits are ignored.
     * If a full 17 bits are unavailable, fill the remaining with zeros
     * to ensure failure if bit code is invalid.
     *
     * @param i17bits Integer containing 17 bits to decode.
     */
    public @Nonnull ZeroRunLengthAc lookup(final int i17bits) throws MdecException.ReadCorruption {
        if((i17bits & b10000000000000000) != 0) {
            return this._aoTable_1xx[(i17bits >> 14) & 3];
        } else if((i17bits & b01111100000000000) != 0) {
            return this._aoTable_0xxxxxxx[(i17bits >> 8) & 0xff];
        } else if((i17bits & b00000011100000000) != 0) {
            return this._aoTable_000000xxxxxxxx[(i17bits >> 3) & 0xff];
        } else if((i17bits & b00000000011100000) != 0) {
            return this._aoTable_000000000xxxxxxxx[i17bits & 0xff];
        } else {
            throw new MdecException.ReadCorruption(UNMATCHED_AC_VLC(i17bits));
        }
    }

    private static @Nonnull String UNMATCHED_AC_VLC(final int i17bits) {
        return "Unmatched AC variable length code: " + Misc.bitsToString(i17bits, LONGEST_BITSTREAM_CODE_17BITS);
    }
}
