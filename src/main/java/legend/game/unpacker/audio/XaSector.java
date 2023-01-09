package legend.game.unpacker.audio;

import it.unimi.dsi.fastutil.shorts.ShortArrayList;
import it.unimi.dsi.fastutil.shorts.ShortList;

import java.nio.ByteBuffer;

public class XaSector {
    private static final int[] positiveXaAdpcmTable = { 0, 60, 115, 98, 122 };
    private static final int[] negativeXaAdpcmTable = { 0, 0, -52, -55, -60 };

    public static int[] generate(final ByteBuffer sector, final short[] old, final short[] older) {
        final ShortList l = new ShortArrayList();
        final ShortList r = new ShortArrayList();

        byte codingInfo = sector.get(19);
        boolean isStereo = (codingInfo & 0x1) != 0;

        int position = 24;

        for(int i = 0; i < 18; i++) { //Each sector consists of 12h 128-byte portions (=900h bytes) (the remaining 14h bytes of the sectors 914h-byte data region are 00h filled).
            for(int blk = 0; blk < 4; blk++) {

                l.addAll(decodeNibbles(sector, position, blk, 0, 0, old, older));

                if(isStereo) {
                    r.addAll(decodeNibbles(sector, position, blk, 1, 1, old, older));
                } else {
                    l.addAll(decodeNibbles(sector, position, blk, 1, 0, old, older));
                }
            }

            position += 128;
        }

        int[] data = new int[l.size()];

        if(isStereo) {
            for(int sample = 0; sample < l.size(); sample++) {
                data[sample] = ((l.getShort(sample) & 0xffff) << 16) | (r.getShort(sample) & 0xffff);
            }
        } else {
            for(int sample = 0; sample < l.size(); sample++) {
                data[sample] = ((l.getShort(sample) & 0xffff) << 16) | (l.getShort(sample) & 0xffff);
            }
        }

        return data;
    }

    private static ShortList decodeNibbles(ByteBuffer xaapdcm, final int position, final int blk, final int nibble, final int lr, final short[] old, final short[] older) {
        final ShortList list = new ShortArrayList();

        final int shift = 12 - (xaapdcm.get(position + 4 + blk * 2 + nibble) & 0x0F);
        final int filter = (xaapdcm.get(position + 4 + blk * 2 + nibble) & 0x30) >> 4;

        final int f0 = positiveXaAdpcmTable[filter];
        final int f1 = negativeXaAdpcmTable[filter];

        for(int i = 0; i < 28; i++) {
            final int t = signed4bit((byte)(xaapdcm.get(position + 16 + blk + i * 4) >> nibble * 4 & 0x0F));
            final int s = (t << shift) + (old[lr] * f0 + older[lr] * f1 + 32) / 64;
            final short sample = (short)s;

            list.add(sample);
            older[lr] = old[lr];
            old[lr] = sample;
        }

        return list;
    }

    public static int signed4bit(final byte value) {
        return value << 28 >> 28;
    }
}
