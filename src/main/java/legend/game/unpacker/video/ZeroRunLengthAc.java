/*
 * jPSXdec: PlayStation 1 Media Decoder/Converter in Java
 * Copyright (C) 2019  Michael Sabin
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

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;

/**
 * Assigns value to a {@link legend.game.fmv.BitStreamCode}. The value can have a
 * zero-run-length and AC coefficient (i.e. {@link MdecCode})
 * and/or the escape code or end-of-block code.
 */
public class ZeroRunLengthAc {
    @Nonnull
    private final BitStreamCode _bitStreamCode;

    @CheckForNull
    private final MdecCode _mdecCode;

    private final boolean _blnIsEscapeCode;
    private final boolean _blnIsEndOfBlock;

    /* If this is a normal AC code with MDEC code equivalent. */
    public ZeroRunLengthAc(@Nonnull final BitStreamCode bitStreamCode, final int iZeroRunLength, final int iAcCoefficient) {
        this._bitStreamCode = bitStreamCode;
        this._mdecCode = new MdecCode(iZeroRunLength, iAcCoefficient);
        this._blnIsEscapeCode = false;
        this._blnIsEndOfBlock = false;
    }

    /* If this is a special AC code without an MDEC equialent. */
    public ZeroRunLengthAc(@Nonnull final BitStreamCode bitStreamCode, final boolean blnIsEscapeCode, final boolean blnIsEndOfBlock) {
        this(bitStreamCode, null, blnIsEscapeCode, blnIsEndOfBlock);
    }

    /* If this is a special AC code with an MDEC equialent. */
    public ZeroRunLengthAc(@Nonnull final legend.game.unpacker.video.BitStreamCode bitStreamCode, final int iZeroRunLength, final int iAcCoefficient, final boolean blnIsEscapeCode, final boolean blnIsEndOfBlock) {
        this(bitStreamCode, new MdecCode(iZeroRunLength, iAcCoefficient), blnIsEscapeCode, blnIsEndOfBlock);
    }

    /* If this is a special AC code with an MDEC equialent. */
    private ZeroRunLengthAc(@Nonnull final legend.game.unpacker.video.BitStreamCode bitStreamCode, @CheckForNull final MdecCode mdecCode, final boolean blnIsEscapeCode, final boolean blnIsEndOfBlock) {
        if(blnIsEscapeCode && blnIsEndOfBlock) {
            throw new IllegalArgumentException("Only one of [escape code] or [end of block] can be true");
        }
        this._bitStreamCode = bitStreamCode;
        this._mdecCode = mdecCode;
        this._blnIsEscapeCode = blnIsEscapeCode;
        this._blnIsEndOfBlock = blnIsEndOfBlock;
    }

    public @CheckForNull MdecCode getMdecCodeCopy() {
        if(this._mdecCode == null) {
            return null;
        } else {
            return this._mdecCode.copy();
        }
    }

    public @Nonnull BitStreamCode getBitStreamCode() {
        return this._bitStreamCode;
    }

    public int getBitLength() {
        return this._bitStreamCode.getLength();
    }

    public boolean isEscapeCode() {
        return this._blnIsEscapeCode;
    }

    public boolean isEndOfBlock() {
        return this._blnIsEndOfBlock;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append(this._bitStreamCode);
        if(this._mdecCode != null) {
            sb.append(' ').append(this._mdecCode);
        }
        if(this._blnIsEscapeCode) {
            sb.append(" ESCAPE_CODE");
        }
        if(this._blnIsEndOfBlock) {
            sb.append(" END_OF_BLOCK");
        }
        return sb.toString();
    }
}