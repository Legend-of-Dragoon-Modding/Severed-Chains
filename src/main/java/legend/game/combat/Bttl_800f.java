package legend.game.combat;

import legend.core.MathHelper;
import legend.core.gte.DVECTOR;
import legend.core.memory.Memory;
import legend.core.memory.Method;
import legend.core.memory.Value;
import legend.game.combat.types.BtldScriptData27c;
import legend.game.types.LodString;
import legend.game.types.RunningScript;
import legend.game.types.ScriptState;

import static legend.core.Hardware.MEMORY;
import static legend.core.MemoryHelper.getMethodAddress;
import static legend.game.Scus94491BpeSegment.FUN_80012b1c;
import static legend.game.Scus94491BpeSegment.FUN_800133ac;
import static legend.game.Scus94491BpeSegment.centreScreenX_1f8003dc;
import static legend.game.Scus94491BpeSegment.centreScreenY_1f8003de;
import static legend.game.Scus94491BpeSegment.insertElementIntoLinkedList;
import static legend.game.Scus94491BpeSegment.linkedListAddress_1f8003d8;
import static legend.game.Scus94491BpeSegment.loadDrgnBinFile;
import static legend.game.Scus94491BpeSegment.memcpy;
import static legend.game.Scus94491BpeSegment.playSound;
import static legend.game.Scus94491BpeSegment.tags_1f8003d0;
import static legend.game.Scus94491BpeSegment_8002.FUN_800232dc;
import static legend.game.Scus94491BpeSegment_8003.FUN_8003b590;
import static legend.game.Scus94491BpeSegment_8003.FUN_8003b5b0;
import static legend.game.Scus94491BpeSegment_8003.FUN_8003b690;
import static legend.game.Scus94491BpeSegment_8003.GetTPage;
import static legend.game.Scus94491BpeSegment_8003.gpuLinkedListSetCommandTransparency;
import static legend.game.Scus94491BpeSegment_8006._8006e398;
import static legend.game.Scus94491BpeSegment_8006._8006f284;
import static legend.game.Scus94491BpeSegment_8007.joypadInput_8007a39c;
import static legend.game.Scus94491BpeSegment_8007.joypadPress_8007a398;
import static legend.game.Scus94491BpeSegment_8007.vsyncMode_8007a3b8;
import static legend.game.Scus94491BpeSegment_800b._800bb0fc;
import static legend.game.Scus94491BpeSegment_800b._800bb110;
import static legend.game.Scus94491BpeSegment_800b._800bb112;
import static legend.game.Scus94491BpeSegment_800b._800bc950;
import static legend.game.Scus94491BpeSegment_800b.gameState_800babc8;
import static legend.game.Scus94491BpeSegment_800b.scriptStatePtrArr_800bc1c0;
import static legend.game.combat.Bttl_800c.FUN_800c7488;
import static legend.game.combat.Bttl_800c._800c669c;
import static legend.game.combat.Bttl_800c._800c6758;
import static legend.game.combat.Bttl_800c._800c677c;
import static legend.game.combat.Bttl_800c._800c6960;
import static legend.game.combat.Bttl_800c._800c697c;
import static legend.game.combat.Bttl_800c._800c697e;
import static legend.game.combat.Bttl_800c._800c6980;
import static legend.game.combat.Bttl_800c._800c6988;
import static legend.game.combat.Bttl_800c._800c69c8;
import static legend.game.combat.Bttl_800c._800c6b5c;
import static legend.game.combat.Bttl_800c._800c6b60;
import static legend.game.combat.Bttl_800c._800c6b64;
import static legend.game.combat.Bttl_800c._800c6b68;
import static legend.game.combat.Bttl_800c._800c6b6c;
import static legend.game.combat.Bttl_800c._800c6b70;
import static legend.game.combat.Bttl_800c._800c6ba8;
import static legend.game.combat.Bttl_800c._800c6c2c;
import static legend.game.combat.Bttl_800c._800c6c34;
import static legend.game.combat.Bttl_800c._800c6c40;
import static legend.game.combat.Bttl_800c._800c6f30;
import static legend.game.combat.Bttl_800c._800c6f4c;
import static legend.game.combat.Bttl_800c._800c6fec;
import static legend.game.combat.Bttl_800c._800c7028;
import static legend.game.combat.Bttl_800c._800c703c;
import static legend.game.combat.Bttl_800c._800c706c;
import static legend.game.combat.Bttl_800c._800c70a4;
import static legend.game.combat.Bttl_800c._800c70e0;
import static legend.game.combat.Bttl_800c._800c70f4;
import static legend.game.combat.Bttl_800c._800c7114;
import static legend.game.combat.Bttl_800c._800c7190;
import static legend.game.combat.Bttl_800c._800c7192;
import static legend.game.combat.Bttl_800c._800c7193;
import static legend.game.combat.Bttl_800c._800c71ec;
import static legend.game.combat.Bttl_800c._800c71f0;
import static legend.game.combat.Bttl_800c._800c723c;
import static legend.game.combat.Bttl_800c._800c724c;
import static legend.game.combat.Bttl_800c._800c726c;
import static legend.game.combat.Bttl_800c._800c7284;
import static legend.game.combat.Bttl_800c._800c729c;
import static legend.game.combat.Bttl_800c._800c72b4;
import static legend.game.combat.Bttl_800c._800fa0b8;
import static legend.game.combat.Bttl_800c._800fb4b4;
import static legend.game.combat.Bttl_800c._800fb534;
import static legend.game.combat.Bttl_800c._800fb548;
import static legend.game.combat.Bttl_800c._800fb55c;
import static legend.game.combat.Bttl_800c._800fb5dc;
import static legend.game.combat.Bttl_800c._800fb614;
import static legend.game.combat.Bttl_800c._800fb674;
import static legend.game.combat.Bttl_800c._800fb6bc;
import static legend.game.combat.Bttl_800c._800fb6f4;
import static legend.game.combat.Bttl_800c._800fb72c;
import static legend.game.combat.Bttl_800e.FUN_800ec7e4;
import static legend.game.combat.Bttl_800e.FUN_800ef8d8;

public final class Bttl_800f {
  private Bttl_800f() { }

  @Method(0x800f0f5cL)
  public static void FUN_800f0f5c(long a0) {
    long v0;
    long v1;

    //LAB_800f0fe4
    //LAB_800f0fe8
    final int[] sp0x20 = new int[80];
    for(int i = 0; i < sp0x20.length; i++) {
      sp0x20[i] = (int)_800c6f4c.offset(i * 0x2L).get();
    }

    //LAB_800f1014
    final long[] sp0x10 = new long[4];
    final long[] sp0x18 = new long[4];
    v0 = MEMORY.ref(2, a0).offset(0x8L).get() + 0x1L;
    sp0x10[0] = v0;
    sp0x10[2] = v0;
    v0 = MEMORY.ref(2, a0).offset(0x10L).get() - 0x1L;
    sp0x10[1] = v0;
    sp0x10[3] = v0;
    v0 = MEMORY.ref(2, a0).offset(0xaL).get();
    sp0x18[0] = v0;
    sp0x18[1] = v0;
    v0 = MEMORY.ref(2, a0).offset(0x1aL).get();
    sp0x18[2] = v0;
    sp0x18[3] = v0;

    //LAB_800f1060
    for(int i = 0; i < 8; i++) {
      long s0 = linkedListAddress_1f8003d8.get();
      linkedListAddress_1f8003d8.addu(0x28L);
      FUN_8003b590(s0);
      gpuLinkedListSetCommandTransparency(s0, false);
      MEMORY.ref(1, s0).offset(0x6L).setu(0x80L);
      MEMORY.ref(1, s0).offset(0x5L).setu(0x80L);
      MEMORY.ref(1, s0).offset(0x4L).setu(0x80L);
      if(i == 0x5L || i == 0x7L) {
        //LAB_800f10ac
        v1 = sp0x10[sp0x20[i * 10]] - sp0x20[i * 10 + 4];
        MEMORY.ref(2, s0).offset(0x20L).setu(v1);
        MEMORY.ref(2, s0).offset(0x10L).setu(v1);
        v1 = sp0x10[sp0x20[i * 10 + 1]] + sp0x20[i * 10 + 4];
        MEMORY.ref(2, s0).offset(0x18L).setu(v1);
        MEMORY.ref(2, s0).offset(0x8L).setu(v1);
        v0 = sp0x20[i * 10 + 2];
        MEMORY.ref(1, s0).offset(0x24L).setu(v0);
        MEMORY.ref(1, s0).offset(0x14L).setu(v0);
        v1 = sp0x20[i * 10 + 2] + sp0x20[i * 10 + 6] - 0x1L;
        MEMORY.ref(1, s0).offset(0x1cL).setu(v1);
        MEMORY.ref(1, s0).offset(0xcL).setu(v1);
      } else {
        //LAB_800f1128
        v1 = sp0x10[sp0x20[i * 10]] - sp0x20[i * 10 + 4];
        MEMORY.ref(2, s0).offset(0x18L).setu(v1);
        MEMORY.ref(2, s0).offset(0x8L).setu(v1);
        v1 = sp0x10[sp0x20[i * 10 + 1]] + sp0x20[i * 10 + 4];
        MEMORY.ref(2, s0).offset(0x20L).setu(v1);
        MEMORY.ref(2, s0).offset(0x10L).setu(v1);
        v0 = sp0x20[i * 10 + 2];
        MEMORY.ref(1, s0).offset(0x1cL).setu(v0);
        MEMORY.ref(1, s0).offset(0xcL).setu(v0);
        v1 = sp0x20[i * 10 + 2] + sp0x20[i * 10 + 6];
        MEMORY.ref(1, s0).offset(0x24L).setu(v1);
        MEMORY.ref(1, s0).offset(0x14L).setu(v1);
      }

      //LAB_800f11a0
      v1 = sp0x18[sp0x20[i * 10]] - sp0x20[i * 10 + 5];
      MEMORY.ref(2, s0).offset(0x12L).setu(v1);
      MEMORY.ref(2, s0).offset(0xaL).setu(v1);
      v0 = sp0x18[sp0x20[i * 10 + 1]] + sp0x20[i * 10 + 5];
      MEMORY.ref(2, s0).offset(0x22L).setu(v0);
      MEMORY.ref(2, s0).offset(0x1aL).setu(v0);
      v0 = sp0x20[i * 10 + 3];
      MEMORY.ref(1, s0).offset(0x15L).setu(v0);
      MEMORY.ref(1, s0).offset(0xdL).setu(v0);
      MEMORY.ref(2, s0).offset(0xeL).setu(0x7c6dL);
      v1 = sp0x20[i * 10 + 3] + sp0x20[i * 10 + 7];
      MEMORY.ref(1, s0).offset(0x25L).setu(v1);
      MEMORY.ref(1, s0).offset(0x1dL).setu(v1);
      MEMORY.ref(2, s0).offset(0x16L).setu(GetTPage(0, 0, 0x2c0L, 0x100L));
      insertElementIntoLinkedList(tags_1f8003d0.getPointer() + 0x7cL, s0);
    }
  }

  @Method(0x800f1268L)
  public static void FUN_800f1268(final long a0, final long a1, final long a2, final long a3, final long a4) {
    final byte[] sp0x10 = new byte[0x1b];
    for(int i = 0; i < sp0x10.length; i++) {
      sp0x10[i] = (byte)_800c6fec.offset(i).get();
    }

    final int s5;
    if((a4 & 0xfL) < 9) {
      s5 = (int)(a4 & 0xf);
    } else {
      s5 = 8;
    }

    //LAB_800f1340
    long s0 = linkedListAddress_1f8003d8.get();
    linkedListAddress_1f8003d8.addu(0x24L);
    FUN_8003b5b0(s0);
    gpuLinkedListSetCommandTransparency(s0, true);

    long s1 = a0 + a2;
    long v0 = a0 - centreScreenX_1f8003dc.get();
    MEMORY.ref(2, s0).offset(0x18L).setu(v0);
    MEMORY.ref(2, s0).offset(0x8L).setu(v0);

    v0 = s1 - centreScreenX_1f8003dc.get();
    MEMORY.ref(2, s0).offset(0x20L).setu(v0);
    MEMORY.ref(2, s0).offset(0x10L).setu(v0);

    v0 = a1 - centreScreenY_1f8003de.get();
    MEMORY.ref(2, s0).offset(0x12L).setu(v0);
    MEMORY.ref(2, s0).offset(0xaL).setu(v0);

    long s4 = a1 + a3;
    v0 = s4 - centreScreenY_1f8003de.get();
    MEMORY.ref(2, s0).offset(0x22L).setu(v0);
    MEMORY.ref(2, s0).offset(0x1aL).setu(v0);

    long v1 = sp0x10[s5 * 3];
    MEMORY.ref(1, s0).offset(0x14L).setu(v1);
    MEMORY.ref(1, s0).offset(0xcL).setu(v1);

    v1 = sp0x10[s5 * 3 + 1];
    MEMORY.ref(1, s0).offset(0x15L).setu(v1);
    MEMORY.ref(1, s0).offset(0xdL).setu(v1);

    v0 = sp0x10[s5 * 3 + 2];
    MEMORY.ref(1, s0).offset(0x1cL).setu(0);
    MEMORY.ref(1, s0).offset(0x4L).setu(0);
    MEMORY.ref(1, s0).offset(0x1dL).setu(0);
    MEMORY.ref(1, s0).offset(0x5L).setu(0);
    MEMORY.ref(1, s0).offset(0x1eL).setu(0);
    MEMORY.ref(1, s0).offset(0x6L).setu(0);
    MEMORY.ref(1, s0).offset(0x16L).setu(v0);
    MEMORY.ref(1, s0).offset(0xeL).setu(v0);
    FUN_800f0f5c(s0);

    insertElementIntoLinkedList(tags_1f8003d0.getPointer() + 0x7cL, s0);
    s0 = linkedListAddress_1f8003d8.get();
    linkedListAddress_1f8003d8.addu(0x24L);
    FUN_8003b5b0(s0);
    gpuLinkedListSetCommandTransparency(s0, true);

    v0 = a0 - centreScreenX_1f8003dc.get();
    MEMORY.ref(2, s0).offset(0x18L).setu(v0);
    MEMORY.ref(2, s0).offset(0x8L).setu(v0);

    s1 = s1 - centreScreenX_1f8003dc.get();
    MEMORY.ref(2, s0).offset(0x20L).setu(s1);
    MEMORY.ref(2, s0).offset(0x10L).setu(s1);

    v0 = a1 - centreScreenY_1f8003de.get();
    MEMORY.ref(2, s0).offset(0x12L).setu(v0);
    MEMORY.ref(2, s0).offset(0xaL).setu(v0);

    MEMORY.ref(1, s0).offset(0x14L).setu(0);
    MEMORY.ref(1, s0).offset(0xcL).setu(0);
    MEMORY.ref(1, s0).offset(0x15L).setu(0);
    MEMORY.ref(1, s0).offset(0xdL).setu(0);
    MEMORY.ref(1, s0).offset(0x16L).setu(0);
    MEMORY.ref(1, s0).offset(0xeL).setu(0);
    MEMORY.ref(1, s0).offset(0x1cL).setu(0);
    MEMORY.ref(1, s0).offset(0x4L).setu(0);
    MEMORY.ref(1, s0).offset(0x1dL).setu(0);
    MEMORY.ref(1, s0).offset(0x5L).setu(0);
    MEMORY.ref(1, s0).offset(0x1eL).setu(0);
    MEMORY.ref(1, s0).offset(0x6L).setu(0);

    s4 = s4 - centreScreenY_1f8003de.get();
    MEMORY.ref(2, s0).offset(0x22L).setu(s4);
    MEMORY.ref(2, s0).offset(0x1aL).setu(s4);

    insertElementIntoLinkedList(tags_1f8003d0.getPointer() + 0x7cL, s0);

    final long a1_0 = linkedListAddress_1f8003d8.get();
    MEMORY.ref(1, a1_0).offset(0x3L).setu(0x1L);
    MEMORY.ref(4, a1_0).offset(0x4L).setu(0xe100_0200L | (_800bb112.get() | 0xbL) & 0x9ffL);
    insertElementIntoLinkedList(tags_1f8003d0.getPointer() + 0x7cL, a1_0);
    linkedListAddress_1f8003d8.addu(0x8L);
  }

  @Method(0x800f1550L)
  public static void FUN_800f1550(long a0, long a1, long a2, long a3) {
    long v0;
    long v1;
    long t1;
    long t2;
    long s5;
    t2 = a2;
    v0 = 0x800c_0000L;
    s5 = v0 + 0x7008L;
    final short[] sp0x08 = new short[5];
    for(int i = 0; i < sp0x08.length; i++) {
      sp0x08[i] = (short)MEMORY.ref(2, s5).offset(i * 0x2L).get();
    }

    v0 = 0x800c_0000L;
    s5 = v0 + 0x7014L;
    final long[] sp0x18 = new long[10];
    for(int i = 0; i < sp0x18.length; i++) {
      sp0x18[i] = MEMORY.ref(2, s5).offset(i * 0x2L).get();
    }

    v0 = 0x800c_0000L;
    s5 = v0 + 0x7028L;
    final long[] sp0x30 = new long[10];
    for(int i = 0; i < sp0x30.length; i++) {
      sp0x30[i] = MEMORY.ref(2, s5).offset(i * 0x2L).get();
    }

    v1 = sp0x08[(int)a1];
    if(v1 == 0x2L) {
      //LAB_800f16a0
      if((short)a2 >= 0x64L) {
        t2 = 0x63L;
      }
    } else if((int)v1 >= 0x3L) {
      //LAB_800f16bc
      //LAB_800f16c0
      if((short)t2 >= 0x2710L) {
        t2 = 0x270fL;
      }
    } else if(v1 != 0x1L) {
      if((short)t2 >= 0x2710L) {
        t2 = 0x270fL;
      }
    } else {
      if((short)a2 >= 0xaL) {
        t2 = 0x9L;
      }
    }

    //LAB_800f16d4
    //LAB_800f16d8
    if((short)t2 <= 0) {
      t2 = 0;
    }

    //LAB_800f16e4
    v1 = _800c6c2c.get() + a0 * 0x144L;

    final short[] sp0x00 = new short[4];

    //LAB_800f171c
    for(int i = 0; i < 4; i++) {
      v0 = v1 + a1 * 0x40L + i * 0x10L;
      MEMORY.ref(2, v0).offset(0x4L).setu(-0x1L);
      sp0x00[i] = -1;
    }

    a2 = 0x1L;

    //LAB_800f1768
    for(t1 = 0; t1 < sp0x08[(int)a1] - 0x1L; t1++) {
      a2 = a2 * 10;
    }

    //LAB_800f1780
    //LAB_800f17b0
    for(int i = 0; i < sp0x08[(int)a1]; i++) {
      v1 = (int)t2 / (int)a2;
      t2 = (int)t2 % (int)a2;
      a2 = a2 / 10;
      sp0x00[i] = (short)v1;
    }

    //LAB_800f1800
    //LAB_800f1828
    for(a2 = 0; a2 < sp0x08[(int)a1] - 0x1L; a2++) {
      if(sp0x00[(int)a2] != 0) {
        break;
      }
    }

    //LAB_800f1848
    //LAB_800f184c
    long a3_0 = _800c6c2c.get() + a0 * 0x144L;

    //LAB_800f18cc
    for(t1 = 0; t1 < sp0x08[(int)a1] && a2 < sp0x08[(int)a1]; t1++, a2++) {
      if(a1 == 0x1L || (int)a1 > 0 && (int)a1 < 0x5L && (int)a1 >= 0x3L) {
        //LAB_800f18f0
        MEMORY.ref(2, a3_0).offset(a1 * 0x40L).offset(t1 * 0x10L).offset(0x6L).setu(sp0x18[(int)(a1 * 2)] + t1 * 5);
      } else {
        MEMORY.ref(2, a3_0).offset(a1 * 0x40L).offset(t1 * 0x10L).offset(0x6L).setu(sp0x18[(int)(a1 * 2)] + a2 * 5);
      }

      //LAB_800f1920
      final long a0_0 = a3_0 + a1 * 0x40L + t1 * 0x10L;
      MEMORY.ref(2, a0_0).offset(0x8L).setu(sp0x18[(int)(a1 * 2 + 1)]);
      MEMORY.ref(2, a0_0).offset(0xcL).setu(0x20L);
      MEMORY.ref(2, a0_0).offset(0xeL).setu(0x8L);
      MEMORY.ref(2, a0_0).offset(0x10L).setu(0x8L);
      MEMORY.ref(2, a0_0).offset(0xaL).setu(sp0x30[sp0x00[(int)a2]]);
      if(a3 == 0x1L) {
        //LAB_800f1984
        v0 = 0x80L;
      } else if((int)a3 < 0x2L) {
        v0 = 0x2dL;
      } else if(a3 == 0x2L) {
        //LAB_800f198c
        v0 = 0x82L;
      } else if(a3 != 0x3L) {
        v0 = 0x2dL;
      } else {
        //LAB_800f1994
        v0 = 0x83L;
      }

      //LAB_800f1998
      MEMORY.ref(2, a0_0).offset(0x12L).setu(v0);

      //LAB_800f199c
      MEMORY.ref(2, a3_0).offset(0x4L).offset(a1 * 0x40L).offset(t1 * 0x10L).setu(sp0x00[(int)a2]);
    }

    //LAB_800f19e0
  }

  @Method(0x800f1a00L)
  public static void FUN_800f1a00(final long a0) {
    if(a0 != 1) {
      //LAB_800f1a10
      long v1 = _800c6c40.getAddress();

      //LAB_800f1a28
      for(int i = 0; i < 3; i++) {
        if(MEMORY.ref(2, v1).offset(0x0L).getSigned() != -0x1L) {
          MEMORY.ref(4, v1).offset(0x1cL).setu(0);
          MEMORY.ref(2, v1).offset(0x6L).and(0xffff_fffeL).and(0xffff_fffdL);
        }

        //LAB_800f1a4c
        v1 = v1 + 0x3cL;
      }

      return;
    }

    //LAB_800f1a64
    long v1 = _800c6c40.getAddress();

    //LAB_800f1a70
    for(int i = 0; i < 3; i++) {
      if(MEMORY.ref(2, v1).offset(0x0L).getSigned() != -0x1L) {
        MEMORY.ref(4, v1).offset(0x1cL).setu(0);
        MEMORY.ref(2, v1).offset(0x6L).oru(0x3L);
      }

      //LAB_800f1a90
      v1 = v1 + 0x3cL;
    }
  }

  @Method(0x800f1aa8L)
  public static int FUN_800f1aa8(long a0, long a1, long a2) {
    long v0;
    long v1;
    long t3;
    long s0;
    long s1;
    long s2;
    long s3;
    long s4;
    long s5;
    long s6;
    long s7;
    long hi;
    long lo;
    s6 = a0;
    s4 = 0;
    v1 = scriptStatePtrArr_800bc1c0.getAddress();
    v0 = v1 + s6 * 0x4L;
    a1 = a1 << 2;
    s2 = a2;
    a0 = MEMORY.ref(4, v0).offset(0x0L).get();
    v0 = _800c703c.offset(s2 * 0x4L).get();
    a1 = a1 + v1;
    v0 = v0 << 1;
    v1 = MEMORY.ref(4, a0).offset(0x60L).get();
    s1 = MEMORY.ref(4, a0).offset(0x0L).get();
    a0 = MEMORY.ref(4, a1).offset(0x0L).get();
    s3 = v1 >>> 2;
    v0 = s1 + v0;
    v0 = MEMORY.ref(1, v0).offset(0x4L).getSigned();
    s5 = MEMORY.ref(4, a0).offset(0x0L).get();
    s7 = v0 + 0x64L;
    v0 = 0x1L;
    if(s2 == v0) {
      s3 = s3 & 0x1L;
      //LAB_800f1c08
      v0 = 0x8010_0000L;
      a1 = MEMORY.ref(2, s1).offset(0x4eL).getSigned();
      v0 = v0 - 0x5f48L;
      v1 = a1 << 1;
      v1 = v1 + a1;
      v1 = v1 << 2;
      v1 = v1 + v0;
      s0 = MEMORY.ref(1, v1).offset(0x5L).get();
      a0 = s6;
      FUN_800f7b68(a0);
      v1 = s0 << 16;
    } else {
      s3 = s3 & 0x1L;
      s0 = 0x64L;
      if((int)s2 < 0x2L && s2 == 0) {
        v0 = 0x8010_0000L;
        if(s3 != 0) {
          a0 = MEMORY.ref(2, s1).offset(0x4eL).getSigned();
          v0 = v0 - 0x5f48L;
          v1 = a0 << 1;
          v1 = v1 + a0;
          v1 = v1 << 2;
          v1 = v1 + v0;
          s0 = MEMORY.ref(1, v1).offset(0x5L).get();
        } else {
          //LAB_800f1bf4
          s0 = MEMORY.ref(2, s1).offset(0x3cL).get();
        }

        //LAB_800f1bf8
        a0 = s6;
        FUN_800f7b68(a0);
        v1 = s0 << 16;
      } else {
        //LAB_800f1c38
        a0 = s6;
        FUN_800f7a74(a0);
        v1 = s0 << 16;
      }
    }

    //LAB_800f1c44
    v1 = (int)v1 >> 16;
    lo = (long)(int)v1 * (int)s7 & 0xffff_ffffL;
    v1 = lo;
    a1 = 0x51eb_0000L;
    a1 = a1 | 0x851fL;
    hi = (long)(int)v1 * (int)a1 >>> 32;
    v0 = s2 << 2;
    v0 = _800c703c.offset(0x10L).offset(v0).get();

    v0 = v0 << 1;
    v0 = s1 + v0;
    v0 = MEMORY.ref(1, v0).offset(0x4L).getSigned();
    v1 = (int)v1 >> 31;
    t3 = hi;
    a0 = (int)t3 >> 5;
    s0 = a0 - v1;
    v1 = v0 + 0x64L;
    if(s2 == 0) {
      v0 = MEMORY.ref(2, s5).offset(0x40L).getSigned();
      lo = (long)(int)v0 * (int)v1 & 0xffff_ffffL;
    } else {
      //LAB_800f1c9c
      v0 = MEMORY.ref(2, s5).offset(0x42L).getSigned();
      lo = (long)(int)v0 * (int)v1 & 0xffff_ffffL;
    }

    //LAB_800f1ca8
    v0 = lo;


    hi = (long)(int)v0 * (int)a1 >>> 32;
    v0 = (int)v0 >> 31;
    v1 = hi;
    v1 = (int)v1 >> 5;
    a0 = v1 - v0;
    v1 = s0 << 16;
    v0 = a0 << 16;
    if((int)v0 < (int)v1) {
      s0 = s0 - a0;
      v0 = FUN_800133ac();
      v1 = v0 << 1;
      v1 = v1 + v0;
      v1 = v1 << 3;
      v1 = v1 + v0;
      v1 = v1 << 2;
      v1 = v1 + v0;
      v1 = (int)v1 >> 16;
      s0 = s0 << 16;
      s0 = (int)s0 >> 16;
      if(s0 >= v1) {
        s4 = 0x1L;
        if(s3 != 0) {
          a0 = s6;
          FUN_800f7b68(a0);
        }
      }
    }
    v1 = s2 << 2;

    //LAB_800f1d28
    v0 = _800c703c.offset(0x20L).offset(v1).get();
    v1 = _800c703c.offset(0x30L).offset(v1).get();
    v0 = v0 << 1;
    v0 = s1 + v0;
    v0 = MEMORY.ref(2, v0).offset(0x4L).getSigned();

    v0 = v0 & v1;
    if(v0 != 0) {
      s4 = 0x1L;
    }
    v0 = s4;

    //LAB_800f1d5c
    return (int)v0;
  }

  @Method(0x800f1d88L)
  public static long FUN_800f1d88(long a0, long s4) {
    long s0;
    long s1;
    long s2;
    long s6;
    long s7;

    final long[] sp0x10 = new long[10];
    for(int i = 0; i < sp0x10.length; i++) {
      sp0x10[i] = _800c706c.offset(i * 0x2L).get();
    }

    ScriptState<?> a1 = scriptStatePtrArr_800bc1c0.get((int)a0).deref();
    s0 = a1.innerStruct_00.getPointer(); //TODO
    if((a1.ui_60.get() & 0x4L) == 0) {
      s2 = FUN_800f2af4(a0, s4);
      s1 = MEMORY.ref(2, s0).offset(0x1cL).get();
    } else {
      //LAB_800f1e5c
      s2 = FUN_800f2d48(a0, s4);
      s1 = _800fa0b8.offset(MEMORY.ref(2, s0).offset(0x4eL).getSigned() * 0xcL).offset(0x8L).get();
    }

    //LAB_800f1e88
    s7 = FUN_800f89cc(MEMORY.ref(2, s0).offset(0xb4L).getSigned());
    if((int)_800c6b64.get() == -0x1L) {
      s6 = 0;
    } else {
      s6 = s1;
    }

    a1 = scriptStatePtrArr_800bc1c0.get((int)s4).deref();
    final BtldScriptData27c s3 = a1.innerStruct_00.derefAs(BtldScriptData27c.class);

    //LAB_800f1eb0
    if((a1.ui_60.get() & 0x4L) == 0) {
      if(s3._272.get() == 0 && (gameState_800babc8.dragoonSpirits_19c.get(0).get() & 0xff) >>> 7 != 0 && (scriptStatePtrArr_800bc1c0.get((int)s4).deref().ui_60.get() & 0x2L) != 0) {
        s0 = sp0x10[9];
      } else {
        s0 = sp0x10[s3._272.get()];
      }
    } else {
      //LAB_800f1f1c
      s0 = s3._72.get();
    }

    //LAB_800f1f20
    s4 = FUN_800f89cc(s3._b8.get());
    final long s3_0;
    if((int)_800c6b64.get() == -0x1L) {
      s3_0 = 0;
    } else {
      //LAB_800f1f4c
      s3_0 = sp0x10[(int)_800c6b64.get()];
    }

    //LAB_800f1f54
    s2 = s2 * FUN_800f2fe0((short)s1, (short)s0, 0) / 100;
    s2 = s2 * FUN_800f2fe0((short)s7, (short)s4, 0x3L) / 100;
    s2 = s2 * FUN_800f2fe0((short)s6, (short)s3_0, 0x5L) / 100;
    if((int)s2 <= 0) {
      s2 = 0;
    }

    //LAB_800f2020
    return s2;
  }

  @Method(0x800f2500L)
  public static long FUN_800f2500(final RunningScript s3) {
    long a1;
    long a2;
    long a3;
    long t0;
    long s1;
    long s2;
    final ScriptState<?> v0 = scriptStatePtrArr_800bc1c0.get(s3.params_20.get(0).deref().get()).deref();
    s2 = v0.innerStruct_00.getPointer(); //TODO
    s1 = FUN_800f1d88(s3.params_20.get(0).deref().get(), s3.params_20.get(1).deref().get());
    if((v0.ui_60.get() & 0x4L) != 0) {
      s1 = FUN_800f946c(s2, s1, 0);
      FUN_800f9380(s2, s2);
    }

    //LAB_800f257c
    if((int)s1 <= 0) {
      s1 = 0x1L;
    }

    //LAB_800f2588
    a2 = FUN_800f29d4(s3.params_20.get(1).deref().get(), s1, 0);
    ScriptState<?> a0 = scriptStatePtrArr_800bc1c0.get(s3.params_20.get(0).deref().get()).deref();
    a1 = a0.innerStruct_00.getPointer(); //TODO

    final long a0_0;
    if((a0.ui_60.get() & 0x4L) == 0) {
      a0_0 = MEMORY.ref(2, a1).offset(0x1cL).get();
    } else {
      //LAB_800f25f4
      a0_0 = _800fa0b8.offset(1, MEMORY.ref(2, a1).offset(0x4eL).getSigned() * 0xcL).offset(0x8L).get();
    }

    a0 = scriptStatePtrArr_800bc1c0.get(s3.params_20.get(1).deref().get()).deref();
    a3 = a0.innerStruct_00.getPointer(); //TODO

    //LAB_800f2614
    if((a0.ui_60.get() & 0x4L) == 0) {
      t0 = MEMORY.ref(2, a3).offset(0x20L).get();
    } else {
      t0 = 0;
    }

    //LAB_800f2620
    if((a0_0 & t0) != 0) {
      a2 = (int)a2 >> 1;
    }

    //LAB_800f2634
    if((a0_0 & MEMORY.ref(2, a3).offset(0x22L).get()) != 0) {
      a2 = 0;
    }

    //LAB_800f2640
    s3.params_20.get(2).deref().set((int)a2);
    s3.params_20.get(3).deref().set((int)FUN_800f7c5c(s3.params_20.get(0).deref().get(), s3.params_20.get(1).deref().get(), 0));
    return 0;
  }

  /** TODO applying damage? */
  @Method(0x800f29d4L)
  public static long FUN_800f29d4(final int scriptIndex, long a1, long a2) {
    final long[] sp0x00 = {0x02L, 0x01L};
    final long[] sp0x08 = {0x88L, 0x89L};
    final long[] sp0x10 = {0x86L, 0x87L};
    final long[] sp0x18 = {0x60L, 0x61L};
    final ScriptState<?> v1 = scriptStatePtrArr_800bc1c0.get(scriptIndex).deref();
    final long a0_0 = v1.innerStruct_00.getPointer();
    if((v1.ui_60.get() & 0x4L) != 0 && (MEMORY.ref(2, a0_0).offset(0x6eL).getSigned() & sp0x00[(int)a2]) != 0) {
      a1 = 0x1L;
    }

    //LAB_800f2a64
    //LAB_800f2a68
    if(a1 >= 9999) {
      a1 = 9999;
    }

    //LAB_800f2a74
    if(MEMORY.ref(2, a0_0).offset(0x4L).offset(sp0x08[(int)a2] * 0x2L).getSigned() != 0) {
      a1 = a1 / 2;
    }

    //LAB_800f2aa0
    if(MEMORY.ref(2, a0_0).offset(0x4L).offset(sp0x10[(int)a2] * 0x2L).getSigned() != 0) {
      a1 = 0;
    }

    //LAB_800f2ac4
    if(MEMORY.ref(2, a0_0).offset(0x4L).offset(sp0x18[(int)a2] * 0x2L).getSigned() != 0) {
      a1 = 0;
    }

    //LAB_800f2aec
    return a1;
  }

  @Method(0x800f2af4L)
  public static long FUN_800f2af4(long a0, long a1) {
    long v0;
    long v1;
    long a2;
    long s0;
    long s1;
    long s2;
    long s3;
    long s4;
    long s5;
    long s6;
    long s7;
    long fp;
    long hi;
    long lo;
    s6 = a0;
    fp = a1;
    v1 = 0x800c_0000L;
    v1 = v1 - 0x3e40L;
    v0 = s6 << 2;
    a1 = v0 + v1;
    v0 = fp << 2;
    v0 = v0 + v1;
    a0 = MEMORY.ref(4, a1).offset(0x0L).get();
    v1 = -0x1L;
    s4 = MEMORY.ref(4, a0).offset(0x0L).get();
    a0 = MEMORY.ref(4, v0).offset(0x0L).get();
    s2 = MEMORY.ref(2, s4).offset(0x34L).getSigned();
    v0 = MEMORY.ref(2, s4).offset(0x58L).getSigned();
    s7 = MEMORY.ref(4, a0).offset(0x0L).get();
    if(v0 == v1) {
      //LAB_800f2c24
      a0 = MEMORY.ref(4, a1).offset(0x0L).get();
      v0 = MEMORY.ref(4, a0).offset(0x60L).get();
      v0 = v0 & 0x2L;
      if(v0 != 0) {
        v0 = MEMORY.ref(2, s4).offset(0xacL).getSigned();
        lo = ((long)(int)s2 * (int)v0) & 0xffff_ffffL;

        //LAB_800f2c4c
        v0 = lo;
        v1 = 0x51eb_0000L;
        v1 = v1 | 0x851fL;
        hi = ((long)(int)v0 * (int)v1) >>> 32;
        v0 = (int)v0 >> 31;
        v1 = hi;
        v1 = (int)v1 >> 5;
        s2 = v1 - v0;
      }
    } else {
      v0 = MEMORY.ref(2, s4).offset(0x56L).getSigned();
      v0 = v0 + v1;
      if((int)v0 >= 0) {
        s1 = 0;
        v0 = v0 & 0x7L;
        s5 = MEMORY.ref(2, s4).offset(0x276L).getSigned();
        v0 = v0 + 0x1L;
        if(v0 != 0) {
          s0 = s1;
          s3 = v0;

          //LAB_800f2b94
          do {
            a0 = s5;
            a1 = s0;
            a2 = 0x4L;
            v0 = FUN_800c7488(a0, a1, a2);
            s1 = s1 + v0;
            s0 = s0 + 0x1L;
          } while((int)s0 < (int)s3);
        }

        //LAB_800f2bb4
        v0 = 0x800c_0000L;
        v0 = v0 - 0x3e40L;
        v1 = s6 << 2;
        v1 = v1 + v0;
        a0 = MEMORY.ref(4, v1).offset(0x0L).get();
        v0 = MEMORY.ref(4, a0).offset(0x60L).get();
        v0 = v0 & 0x2L;
        if(v0 != 0) {
          v0 = MEMORY.ref(2, s4).offset(0xacL).getSigned();
          lo = ((long)(int)s1 * (int)v0) & 0xffff_ffffL;
        } else {
          //LAB_800f2bec
          v0 = MEMORY.ref(2, s4).offset(0x11cL).getSigned();
          v0 = v0 + 0x64L;
          lo = ((long)(int)s1 * (int)v0) & 0xffff_ffffL;
        }

        //LAB_800f2bfc
        v0 = lo;
        v1 = 0x51eb_0000L;
        v1 = v1 | 0x851fL;
        hi = ((long)(int)v0 * (int)v1) >>> 32;
        v0 = (int)v0 >> 31;
        v1 = hi;
        v1 = (int)v1 >> 5;
        s1 = v1 - v0;
        lo = ((long)(int)s2 * (int)s1) & 0xffff_ffffL;
        v0 = lo;
        v1 = 0x51eb_0000L;
        v1 = v1 | 0x851fL;
        hi = ((long)(int)v0 * (int)v1) >>> 32;
        v0 = (int)v0 >> 31;
        v1 = hi;
        v1 = (int)v1 >> 5;
        s2 = v1 - v0;
      }
    }

    //LAB_800f2c6c
    v1 = 0x800c_0000L;

    //LAB_800f2c70
    v1 = v1 - 0x3e40L;
    v0 = fp << 2;
    v0 = v0 + v1;
    a0 = MEMORY.ref(4, v0).offset(0x0L).get();
    v0 = 0x1L;
    a1 = MEMORY.ref(4, a0).offset(0x60L).get();
    a0 = MEMORY.ref(2, s7).offset(0x38L).getSigned();
    v1 = a1 & 0x4L;
    if(v1 != v0) {
      v0 = a1 & 0x2L;
      if(v0 != 0) {
        v1 = 0x51eb_0000L;
        v0 = MEMORY.ref(2, s7).offset(0xb0L).getSigned();
        lo = ((long)(int)a0 * (int)v0) & 0xffff_ffffL;
        v0 = lo;
        v1 = v1 | 0x851fL;
        hi = ((long)(int)v0 * (int)v1) >>> 32;
        v0 = (int)v0 >> 31;
        v1 = hi;
        v1 = (int)v1 >> 5;
        a0 = v1 - v0;
      }
    }

    //LAB_800f2ccc
    v1 = MEMORY.ref(2, s4).offset(0x4L).getSigned();
    v1 = v1 + 0x5L;
    lo = ((long)(int)v1 * (int)s2) & 0xffff_ffffL;
    v1 = lo;
    v0 = v1 << 2;
    v0 = v0 + v1;
    hi = (v0 & 0xffff_ffffL) % (a0 & 0xffff_ffffL);
    s0 = hi;
    lo = (int)v0 / (int)a0;
    s2 = lo;
    v0 = a0 >>> 31;
    v0 = a0 + v0;
    v0 = (int)v0 >> 1;
    v0 = (int)s0 < (int)v0 ? 1 : 0;
    v0 = v0 ^ 0x1L;
    v0 = s2 + v0;
    return v0;
  }

  @Method(0x800f2d48L)
  public static long FUN_800f2d48(long a0, long a1) {
    long v0;
    long v1;
    long a2;
    long a3;
    long t0;
    long t1;
    long t2;
    long hi;
    long lo;
    v0 = 0x800c_0000L;
    t1 = v0 - 0x3e40L;
    a0 = a0 << 2;
    a0 = a0 + t1;
    a2 = MEMORY.ref(4, a0).offset(0x0L).get();
    v1 = 0x8010_0000L;
    v0 = MEMORY.ref(4, a2).offset(0x0L).get();
    v1 = v1 - 0x5f48L;
    a0 = MEMORY.ref(2, v0).offset(0x4eL).getSigned();
    t0 = MEMORY.ref(2, v0).offset(0x34L).getSigned();
    v0 = a0 << 1;
    v0 = v0 + a0;
    v0 = v0 << 2;
    v0 = v0 + v1;
    v0 = MEMORY.ref(1, v0).offset(0x4L).get();
    t0 = t0 + v0;
    lo = ((long)(int)t0 * (int)t0) & 0xffff_ffffL;
    v0 = a1 << 2;
    v0 = v0 + t1;
    a2 = MEMORY.ref(4, v0).offset(0x0L).get();
    v0 = MEMORY.ref(4, a2).offset(0x60L).get();
    t2 = MEMORY.ref(4, a2).offset(0x0L).get();
    v0 = v0 & 0x4L;
    a0 = lo;
    v1 = a0 << 2;
    t0 = v1 + a0;
    v1 = 0x1L;
    if(v0 == v1) {
      v0 = 0x800c_0000L;
      a3 = MEMORY.ref(4, v0).offset(0x677cL).get();

      if((int)a3 >= 0x4L) {
        a3 = 0x3L;
      }

      //LAB_800f2ddc
      if((int)a3 > 0) {
        v1 = 0;
        v0 = 0x8007_0000L;
        a0 = v0 - 0x1c68L;

        //LAB_800f2dec
        do {
          v0 = MEMORY.ref(4, a0).offset(0xe40L).get();
          v0 = v0 << 2;
          v0 = v0 + t1;
          a2 = MEMORY.ref(4, v0).offset(0x0L).get();
          v0 = MEMORY.ref(4, a2).offset(0x0L).get();
          v0 = MEMORY.ref(2, v0).offset(0x272L).getSigned();

          if(v0 == 0) {
            break;
          }
          v1 = v1 + 0x1L;
          a0 = a0 + 0x4L;
        } while((int)v1 < (int)a3);
      }
    }

    //LAB_800f2e28
    v1 = 0x800c_0000L;
    v1 = v1 - 0x3e40L;
    v0 = a1 << 2;
    v0 = v0 + v1;
    a2 = MEMORY.ref(4, v0).offset(0x0L).get();
    v0 = 0x1L;
    a0 = MEMORY.ref(4, a2).offset(0x60L).get();
    a1 = MEMORY.ref(2, t2).offset(0x38L).getSigned();
    v1 = a0 & 0x4L;
    if(v1 != v0) {
      v0 = a0 & 0x2L;
      if(v0 != 0) {
        v1 = 0x51eb_0000L;
        v0 = MEMORY.ref(2, t2).offset(0xb0L).getSigned();
        lo = ((long)(int)a1 * (int)v0) & 0xffff_ffffL;
        v0 = lo;
        v1 = v1 | 0x851fL;
        hi = ((long)(int)v0 * (int)v1) >>> 32;
        v0 = (int)v0 >> 31;
        v1 = hi;
        v1 = (int)v1 >> 5;
        a1 = v1 - v0;
      }
    }

    //LAB_800f2e88
    lo = (int)t0 / (int)a1;
    v0 = lo;
    return v0;
  }

  @Method(0x800f2fe0L)
  public static long FUN_800f2fe0(long a0, long a1, long a2) {
    long v1;
    long s0;
    long s2;
    long s7;
    long s6 = 0;
    long s4 = 0x1L;
    long s5 = 0x1L;
    long s3 = _800c70a4.offset(a2 * 0x4L).get();
    long s1 = 0;

    //LAB_800f3094
    for(s7 = 0; s7 < s3; s7++) {
      if((a0 & s5) != 0) {
        //LAB_800f30b0
        for(s2 = 0; s2 < s3; s2++) {
          if((a1 & s4) != 0) {
            s0 = s3 - (FUN_800f8ca0(a0 & s5) + 0x1L);
            v1 = s3 - (FUN_800f8ca0(a1 & s4) + 0x1L);

            // 0 800f3108
            // 1 800f3188
            // 2 800f3188
            // 3 800f3124
            // 4 800f3144
            // 5 800f3164
            // 6 800f3188
            switch((int)a2) {
              case 0 -> {
                //LAB_800f3108
                s1 = s1 + _800fb4b4.offset(v1 * 0x10L).offset(2, s0 * 0x2L).getSigned();
              }

              case 3 -> {
                //LAB_800f3124
                s1 = s1 + _800fb534.offset(v1 * 0x6L).offset(2, s0 * 0x2L).getSigned();
              }

              case 4 -> {
                //LAB_800f3144
                s1 = s1 + _800fb548.offset(v1 * 0x6L).offset(2, s0 * 0x2L).getSigned();
              }

              case 5 -> {
                //LAB_800f3164
                //LAB_800f317c
                s1 = s1 + _800fb55c.offset(v1 * 0x10L).offset(2, s0 * 0x2L).getSigned();
              }

              default ->
                //LAB_800f3188
                s1 = s1 + 0x64L;
            }

            //LAB_800f318c
            s6 = s6 + 0x1L;
          }

          //LAB_800f3190
          s4 = s4 << 1;
        }
      }

      //LAB_800f31a0
      s4 = 0x1L;
      s5 = s5 << 1;
    }

    //LAB_800f31b4
    if(s6 == 0) {
      s1 = 0x64L;
    } else {
      //LAB_800f31c4
      s1 = (int)s1 / (int)s6;
    }

    //LAB_800f31cc
    return s1;
  }

  @Method(0x800f3354L)
  public static void FUN_800f3354(final long t9, long a1, long a2, final long a3, final long a4, final long a5, final long a6, final long a7) {
    long a0;
    long v0;
    long v1;
    long t0;
    long t2;
    long t4;
    long t5;
    long s1;

    final Memory.TemporaryReservation sp0x00tmp = MEMORY.temp(0xa);
    final Value sp0x00 = sp0x00tmp.get();

    final Memory.TemporaryReservation sp0x10tmp = MEMORY.temp(0x14);
    final Value sp0x10 = sp0x10tmp.get();
    memcpy(sp0x10.getAddress(), _800c70e0.getAddress(), 0x14);

    final Memory.TemporaryReservation sp0x28tmp = MEMORY.temp(0x14);
    final Value sp0x28 = sp0x28tmp.get();
    memcpy(sp0x28.getAddress(), _800c7028.getAddress(), 0x14);

    final Memory.TemporaryReservation sp0x40tmp = MEMORY.temp(0x1e);
    final Value sp0x40 = sp0x40tmp.get();
    memcpy(sp0x40.getAddress(), _800c70f4.getAddress(), 0x1e);

    long s2 = a6;
    long t7;
    long t3;
    if((int)a3 != -0x1L) {
      t7 = a1;
      s1 = a2;
      t3 = a7 & 0xfL;
    } else {
      t7 = 0x2L;
      s1 = 0x2L;
      t3 = 0xeL;
    }

    //LAB_800f34d4
    v1 = _800c6b5c.get() + t9 * 0xc4L;
    MEMORY.ref(4, v1).offset(0x18L).setu(-0x1L);
    MEMORY.ref(4, v1).offset(0x14L).setu(-0x1L);
    MEMORY.ref(2, v1).offset(0x0L).setu(0);
    MEMORY.ref(2, v1).offset(0x2L).setu(0);
    MEMORY.ref(4, v1).offset(0x4L).setu(-0x1L);
    MEMORY.ref(4, v1).offset(0x8L).setu(0);
    MEMORY.ref(4, v1).offset(0xcL).setu(0x80_8080L);

    //LAB_800f3528
    for(t0 = 0; t0 < 5; t0++) {
      MEMORY.ref(2, v1).offset(0x30L).setu(-0x1L);
      MEMORY.ref(4, v1).offset(0x24L).setu(0);
      MEMORY.ref(4, v1).offset(0x28L).setu(0);
      MEMORY.ref(4, v1).offset(0x2cL).setu(0);
      MEMORY.ref(2, v1).offset(0x40L).setu(0);
      v1 = v1 + 0x20L;
    }

    v1 = _800c6b5c.get() + t9 * 0xc4L;
    MEMORY.ref(2, v1).offset(0x0L).setu(0x1L);
    if(s2 == 0) {
      MEMORY.ref(2, v1).offset(0x2L).oru(0x1L);
    }

    //LAB_800f3588
    v0 = _800c6b5c.get() + t9 * 0xc4L;
    MEMORY.ref(4, v0).offset(0x8L).setu(0);
    MEMORY.ref(4, v0).offset(0xcL).setu(0x80_8080L);
    MEMORY.ref(4, v0).offset(0x10L).setu(s1);
    MEMORY.ref(2, v0).offset(0x2L).oru(0x8000L);
    if(s1 == 0x2L && s2 == 0) {
      s2 = 60 / vsyncMode_8007a3b8.get() * 2;
    }

    //LAB_800f35dc
    a0 = 0x1L;

    //LAB_800f35e4
    for(t0 = 4; t0 >= 0; t0--) {
      a0 = a0 * 10;
    }

    if((int)a3 >= (int)a0) {
      a1 = a0 - 0x1L;
    } else {
      a1 = a3;
    }

    //LAB_800f3608
    if((int)a1 <= 0) {
      a1 = 0;
    }

    //LAB_800f3614
    v1 = _800c6b5c.get() + t9 * 0xc4L;
    MEMORY.ref(4, v1).offset(0x1cL).setu(a4);
    MEMORY.ref(4, v1).offset(0x20L).setu(a5);

    //LAB_800f3654
    for(t0 = 0; t0 < 5; t0++) {
      MEMORY.ref(2, v1).offset(0x30L).setu(-0x1L);
      sp0x00.offset(2, t0 * 0x2L).setu(-0x1L);
      v1 = v1 + 0x20L;
    }

    t0 = a0 / 10;

    //LAB_800f36a0
    for(t2 = 0; t2 < 5; t2++) {
      a0 = (int)a1 / (int)t0;
      a1 = (int)a1 % (int)t0;
      sp0x00.offset(2, t2 * 0x2L).setu(a0);
      t0 = t0 / 10;
    }

    //LAB_800f36dc
    //LAB_800f36ec
    for(t0 = 0; t0 < 4; t0++) {
      if(sp0x00.offset(2, t0 * 0x2L).getSigned() != 0) {
        break;
      }
    }

    //LAB_800f370c
    //LAB_800f3710
    if(t7 == 0x1L) {
      //LAB_800f3738
      a0 = -(5 - t0) * 5 / 2;
    } else if(t7 == 0x2) {
      //LAB_800f3758
      a0 = -0x12L;
    } else {
      //LAB_800f372c
      a0 = -(5 - t0) * 4;
    }

    //LAB_800f375c
    a2 = 0x10L;
    a1 = 0;
    final long t3_0 = t3;
    t4 = a0;
    t3 = a0;
    t5 = a0;

    //LAB_800f37ac
    for(t2 = 0; t2 < 5; t2++) {
      v1 = _800c6b5c.get() + t9 * 0xc4L + a1;
      MEMORY.ref(2, v1).offset(0x34L).setu(0);
      MEMORY.ref(4, v1).offset(0x24L).setu(0x8000L);

      if(s1 == 0x2L) {
        MEMORY.ref(4, v1).offset(0x24L).setu(0);
        MEMORY.ref(4, v1).offset(0x28L).setu(t2);
        MEMORY.ref(4, v1).offset(0x2cL).setu(0);
      }

      //LAB_800f37d8
      if(t7 == 0x1L) {
        //LAB_800f382c
        v1 = _800c6b5c.get() + t9 * 0xc4L + a1;
        MEMORY.ref(2, v1).offset(0x32L).setu(t3);
        MEMORY.ref(2, v1).offset(0x3aL).setu(0x8L);
        MEMORY.ref(2, v1).offset(0x3cL).setu(0x8L);
        MEMORY.ref(2, v1).offset(0x38L).setu(0x20L);
        MEMORY.ref(2, v1).offset(0x36L).setu(sp0x28.offset(2, sp0x00.offset(2, (t0 + t2) * 0x2L).getSigned() * 0x2L).get());
      } else if(t7 == 0x2L) {
        //LAB_800f386c
        v0 = _800c6b5c.get() + t9 * 0xc4L + a1;
        MEMORY.ref(2, v0).offset(0x3aL).setu(0x24L);
        MEMORY.ref(2, v0).offset(0x36L).setu(0x48L);
        MEMORY.ref(2, v0).offset(0x32L).setu(t4);
        MEMORY.ref(2, v0).offset(0x3cL).setu(a2);
        MEMORY.ref(2, v0).offset(0x38L).setu(0x80L);
      } else {
        //LAB_800f37f4
        v1 = _800c6b5c.get() + t9 * 0xc4L + a1;
        MEMORY.ref(2, v1).offset(0x32L).setu(t5);
        MEMORY.ref(2, v1).offset(0x3aL).setu(0x8L);
        MEMORY.ref(2, v1).offset(0x3cL).setu(a2);
        MEMORY.ref(2, v1).offset(0x38L).setu(0x28L);
        MEMORY.ref(2, v1).offset(0x36L).setu(sp0x10.offset(2, sp0x00.offset(2, (t0 + t2) * 0x2L).getSigned() * 0x2L).get());
      }

      //LAB_800f3898
      v1 = _800c6b5c.get() + t9 * 0xc4L + a1;
      MEMORY.ref(2, v1).offset(0x3eL).setu(sp0x40.offset(2, t3_0 * 0x2L).get());
      MEMORY.ref(2, v1).offset(0x40L).setu(0x1000L);
      MEMORY.ref(2, v1).offset(0x30L).setu(sp0x00.offset(2, (t0 + t2) * 0x2L).get());

      t4 = t4 + 0x24L;
      a1 = a1 + 0x20L;
      t5 = t5 + 0x8L;
      t3 = t3 + 0x5L;
      t0 = t0 + 0x1L;
      if((int)t0 >= 5) {
        break;
      }
    }

    //LAB_800f38e8
    v0 = _800c6b5c.get() + t9 * 0xc4L;
    MEMORY.ref(4, v0).offset(0x18L).setu(s2 + 0x4L);
    MEMORY.ref(4, v0).offset(0x14L).setu(t2 + 0xcL);

    sp0x00tmp.release();
    sp0x10tmp.release();
    sp0x28tmp.release();
    sp0x40tmp.release();
  }

  @Method(0x800f3940L)
  public static void FUN_800f3940() {
    long v0;
    long v1;
    long a0;
    long a1;
    long a2;
    long a3;
    long s0;
    long s1;
    long s2;
    long s3;
    long s4;
    long s5;
    long lo;
    s3 = 0;
    s1 = 0x800c_0000L;
    s2 = -0x1L;
    s5 = 0x61L;
    s4 = 0x63L;
    s0 = s3;

    //LAB_800f3978
    do {
      v0 = MEMORY.ref(4, s1).offset(0x6b5cL).get();

      v1 = s0 + v0;
      v0 = MEMORY.ref(2, v1).offset(0x2L).getSigned();

      v0 = v0 & 0x8000L;
      if(v0 != 0) {
        v0 = MEMORY.ref(2, v1).offset(0x0L).getSigned();

        if(v0 != 0) {
          v0 = MEMORY.ref(4, v1).offset(0x4L).get();

          if(v0 != s2) {
            final ScriptState<BtldScriptData27c> state = scriptStatePtrArr_800bc1c0.get((int)v0).derefAs(ScriptState.classFor(BtldScriptData27c.class));
            final BtldScriptData27c data = state.innerStruct_00.deref();

            a2 = -0x280L;
            if((state.ui_60.get() & 0x4L) != 0) {
              v1 = data._78.getX();
              v1 = -v1;
              v0 = v1 << 1;
              v0 = v0 + v1;
              v0 = v0 << 3;
              v0 = v0 + v1;
              v1 = data._78.getY();
              a3 = v0 << 2;
              v1 = -v1;
              v0 = v1 << 1;
              v0 = v0 + v1;
              v0 = v0 << 3;
              v0 = v0 + v1;
              v1 = data._78.getZ();
              a2 = v0 << 2;
              v1 = -v1;
              v0 = v1 << 1;
              v0 = v0 + v1;
              v0 = v0 << 3;
              v0 = v0 + v1;
              v0 = v0 << 2;
            } else {
              //LAB_800f3a3c
              v0 = 0;
              a3 = v0;
            }

            //LAB_800f3a44
            final DVECTOR v0_0 = FUN_800ec7e4(data._148, (short)v0, (short)a2, (short)a3);
            a1 = 0x1f80_0000L;
            v1 = v0_0.getX();
            a2 = MEMORY.ref(4, s1).offset(0x6b5cL).get();
            a0 = MEMORY.ref(2, a1).offset(0x3dcL).getSigned();
            a1 = a1 + 0x3dcL;
            a2 = s0 + a2;
            v1 = v1 + a0;
            MEMORY.ref(4, a2).offset(0x1cL).setu(v1);
            a0 = v1;
            v1 = MEMORY.ref(2, a1).offset(0x2L).getSigned();
            v0 = v0_0.getY();
            v0 = v0 + v1;
            MEMORY.ref(4, a2).offset(0x20L).setu(v0);
            v0 = FUN_800fa068(a0);
            v1 = MEMORY.ref(4, s1).offset(0x6b5cL).get();

            v1 = s0 + v1;
            a0 = MEMORY.ref(4, v1).offset(0x20L).get();
            MEMORY.ref(4, v1).offset(0x1cL).setu(v0);
            v0 = FUN_800fa090(a0);
            v1 = MEMORY.ref(4, s1).offset(0x6b5cL).get();

            v1 = s0 + v1;
            MEMORY.ref(4, v1).offset(0x20L).setu(v0);
          }

          //LAB_800f3ac8
          v0 = MEMORY.ref(4, s1).offset(0x6b5cL).get();

          a1 = s0 + v0;
          a0 = MEMORY.ref(2, a1).offset(0x0L).getSigned();

          if(a0 == s5) {
            //LAB_800f3c34
            v0 = MEMORY.ref(4, a1).offset(0x14L).get();

            if((int)v0 <= 0) {
              v0 = 0x64L;
              MEMORY.ref(2, a1).offset(0x0L).setu(v0);
            } else {
              v0 = v0 - 0x1L;

              //LAB_800f3c50
              v1 = MEMORY.ref(4, a1).offset(0xcL).get();
              a0 = MEMORY.ref(1, a1).offset(0x18L).get();
              MEMORY.ref(4, a1).offset(0x14L).setu(v0);
              v0 = 0xff00_0000L;
              a0 = v1 - a0;
              v0 = v1 & v0;
              a0 = a0 & 0xffL;
              v1 = a0 << 16;
              v0 = v0 | v1;
              v1 = a0 << 8;
              v0 = v0 | v1;
              v1 = v0 | a0;
              MEMORY.ref(4, a1).offset(0xcL).setu(v1);
            }
          } else {
            if((int)a0 > 0x63L) {
              //LAB_800f3b04
              if(a0 != s4) {
                if((int)a0 < 0x63L) {
                  //LAB_800f3c88
                  v0 = MEMORY.ref(2, a1).offset(0x2L).get();

                  v0 = v0 & 0x1L;
                  if(v0 != 0) {
                    MEMORY.ref(2, a1).offset(0x0L).setu(s4);
                  } else {
                    //LAB_800f3ca4
                    v0 = MEMORY.ref(4, a1).offset(0x14L).get();
                    v0 = v0 - 0x1L;
                    MEMORY.ref(4, a1).offset(0x14L).setu(v0);
                    if((int)v0 <= 0) {
                      v1 = MEMORY.ref(4, a1).offset(0x10L).get();

                      if((int)v1 > 0 && (int)v1 < 0x3L) {
                        v0 = 0x8008_0000L;
                        v1 = MEMORY.ref(4, v0).offset(-0x5c48L).get();
                        v0 = 0x3cL;
                        lo = (int)v0 / (int)v1;
                        v0 = lo;

                        v1 = v0 >>> 31;
                        v0 = v0 + v1;
                        a2 = (int)v0 >> 1;
                        v1 = 0x60L;
                        lo = (int)v1 / (int)a2;
                        v1 = lo;
                        MEMORY.ref(2, a1).offset(0x0L).setu(s5);
                        v0 = 0x60_0000L;
                        v0 = v0 | 0x6060L;
                        MEMORY.ref(4, a1).offset(0xcL).setu(v0);
                        v0 = 0x1L;
                        MEMORY.ref(4, a1).offset(0x8L).setu(v0);
                        MEMORY.ref(4, a1).offset(0x14L).setu(a2);
                        MEMORY.ref(4, a1).offset(0x18L).setu(v1);
                      } else {
                        //LAB_800f3d24
                        v0 = MEMORY.ref(4, s1).offset(0x6b5cL).get();
                        v1 = 0x64L;

                        //LAB_800f3d2c
                        v0 = s0 + v0;
                        MEMORY.ref(2, v0).offset(0x0L).setu(v1);
                      }
                    }
                  }
                } else {
                  v0 = 0x64L;
                  if(a0 == v0) {
                    v0 = 0x80_0000L;

                    //LAB_800f3d38
                    v0 = v0 | 0x8080L;
                    a2 = 0;
                    v1 = a1;
                    MEMORY.ref(4, v1).offset(0x18L).setu(s2);
                    MEMORY.ref(4, v1).offset(0x14L).setu(s2);
                    MEMORY.ref(2, v1).offset(0x0L).setu(0);
                    MEMORY.ref(2, v1).offset(0x2L).setu(0);
                    MEMORY.ref(4, v1).offset(0x4L).setu(s2);
                    MEMORY.ref(4, v1).offset(0x8L).setu(0);
                    MEMORY.ref(4, v1).offset(0xcL).setu(v0);

                    //LAB_800f3d60
                    do {
                      MEMORY.ref(2, v1).offset(0x30L).setu(s2);
                      MEMORY.ref(4, v1).offset(0x24L).setu(0);
                      MEMORY.ref(4, v1).offset(0x28L).setu(0);
                      MEMORY.ref(4, v1).offset(0x2cL).setu(0);
                      MEMORY.ref(2, v1).offset(0x40L).setu(0);
                      a2 = a2 + 0x1L;
                      v1 = v1 + 0x20L;
                    } while((int)a2 < 0x5L);
                  }
                }
              }
            } else {
              v0 = 0x1L;
              if(a0 == v0) {
                v0 = 0x2L;
                //LAB_800f3b24
                v1 = MEMORY.ref(4, a1).offset(0x10L).get();

                if(v1 != a0 && v1 == v0) {
                  MEMORY.ref(2, a1).offset(0x0L).setu(v1);
                } else {
                  //LAB_800f3b44
                  v0 = MEMORY.ref(4, s1).offset(0x6b5cL).get();
                  v1 = 0x62L;
                  v0 = s0 + v0;
                  MEMORY.ref(2, v0).offset(0x0L).setu(v1);
                }
              } else {
                v0 = 0x2L;
                if(a0 == v0) {
                  a2 = 0;
                  //LAB_800f3b50
                  do {
                    v0 = MEMORY.ref(2, a1).offset(0x30L).getSigned();

                    if(v0 == s2) {
                      break;
                    }

                    a0 = MEMORY.ref(4, a1).offset(0x24L).get();

                    v0 = a0 & 0x1L;
                    if(v0 != 0) {
                      v0 = a0 & 0x2L;
                      if(v0 != 0) {
                        v0 = MEMORY.ref(4, a1).offset(0x2cL).get();

                        if((int)v0 < 0x5L) {
                          v0 = MEMORY.ref(2, a1).offset(0x34L).get();
                          a0 = MEMORY.ref(2, a1).offset(0x2cL).get();
                          v1 = MEMORY.ref(4, a1).offset(0x2cL).get();
                          v0 = v0 + a0;
                          v1 = v1 + 0x1L;
                          MEMORY.ref(2, a1).offset(0x34L).setu(v0);
                          MEMORY.ref(4, a1).offset(0x2cL).setu(v1);
                        }
                      } else {
                        v0 = a0 | 0x8002L;

                        //LAB_800f3bb0
                        v1 = MEMORY.ref(2, a1).offset(0x34L).getSigned();
                        MEMORY.ref(4, a1).offset(0x24L).setu(v0);
                        v0 = -0x4L;
                        MEMORY.ref(4, a1).offset(0x2cL).setu(v0);
                        MEMORY.ref(4, a1).offset(0x28L).setu(v1);
                      }
                    } else {
                      //LAB_800f3bc8
                      v1 = MEMORY.ref(4, a1).offset(0x2cL).get();
                      v0 = MEMORY.ref(4, a1).offset(0x28L).get();

                      if(v1 == v0) {
                        v0 = a0 | 0x1L;
                        MEMORY.ref(4, a1).offset(0x24L).setu(v0);
                      }

                      //LAB_800f3be0
                      v0 = MEMORY.ref(4, a1).offset(0x2cL).get();

                      v0 = v0 + 0x1L;
                      MEMORY.ref(4, a1).offset(0x2cL).setu(v0);
                    }

                    //LAB_800f3bf0
                    a2 = a2 + 0x1L;
                    a1 = a1 + 0x20L;
                  } while((int)a2 < 0x5L);

                  //LAB_800f3c00
                  v0 = MEMORY.ref(4, s1).offset(0x6b5cL).get();

                  a0 = s0 + v0;
                  v0 = MEMORY.ref(4, a0).offset(0x14L).get();

                  v0 = v0 - 0x1L;
                  MEMORY.ref(4, a0).offset(0x14L).setu(v0);
                  if((int)v0 <= 0) {
                    v1 = MEMORY.ref(4, a0).offset(0x18L).get();
                    v0 = 0x62L;
                    MEMORY.ref(2, a0).offset(0x0L).setu(v0);
                    MEMORY.ref(4, a0).offset(0x14L).setu(v1);
                  }
                }
              }
            }
          }
        }
      }

      //LAB_800f3d84
      s0 = s0 + 0xc4L;

      //LAB_800f3d88
      s3 = s3 + 0x1L;
    } while((int)s3 < 0xcL);
  }

  @Method(0x800f3dbcL)
  public static void FUN_800f3dbc() {
    //LAB_800f3e20
    long fp = 0;
    for(long sp20 = 0; sp20 < 12; sp20++) {
      long a1 = _800c6b5c.get() + fp;

      if((MEMORY.ref(2, a1).offset(0x2L).getSigned() & 0x8000L) != 0) {
        if(MEMORY.ref(2, a1).offset(0x0L).getSigned() != 0) {
          long sp28 = MEMORY.ref(2, a1).offset(0x8L).get();
          long sp30 = MEMORY.ref(1, a1).offset(0xeL).get();
          long sp31 = MEMORY.ref(1, a1).offset(0xdL).get();
          long sp32 = MEMORY.ref(1, a1).offset(0xcL).get();

          //LAB_800f3e80
          long s6 = 0;
          for(long s7 = 0; s7 < 5; s7++) {
            long a0 = _800c6b5c.get() + fp + s6;

            if(MEMORY.ref(2, a0).offset(0x30L).getSigned() == -0x1L) {
              break;
            }

            if((MEMORY.ref(4, a0).offset(0x24L).get() & 0x8000L) != 0) {
              long s3 = 0x1L;

              //LAB_800f3ec0
              while(true) {
                long s0 = linkedListAddress_1f8003d8.get();
                FUN_8003b590(s0);
                gpuLinkedListSetCommandTransparency(s0, sp28 != 0);
                linkedListAddress_1f8003d8.addu(0x28L);

                MEMORY.ref(1, s0).offset(0x4L).setu(sp30);
                MEMORY.ref(1, s0).offset(0x5L).setu(sp31);
                MEMORY.ref(1, s0).offset(0x6L).setu(sp32);
                long v1 = _800c6b5c.get() + fp;
                a1 = MEMORY.ref(4, v1).offset(0x1cL).get() - centreScreenX_1f8003dc.getSigned();
                a0 = v1 + s6;
                long a2 = MEMORY.ref(4, v1).offset(0x20L).get();
                long v0 = MEMORY.ref(2, a0).offset(0x32L).get() + a1;
                MEMORY.ref(2, s0).offset(0x18L).setu(v0);
                MEMORY.ref(2, s0).offset(0x8L).setu(v0);
                v1 = MEMORY.ref(2, a0).offset(0x32L).get() + MEMORY.ref(2, a0).offset(0x3aL).get() + a1;
                MEMORY.ref(2, s0).offset(0x20L).setu(v1);
                MEMORY.ref(2, s0).offset(0x10L).setu(v1);
                a2 = a2 - centreScreenY_1f8003de.getSigned();
                v0 = MEMORY.ref(2, a0).offset(0x34L).get() + a2;
                MEMORY.ref(2, s0).offset(0x12L).setu(v0);
                MEMORY.ref(2, s0).offset(0xaL).setu(v0);
                v1 = MEMORY.ref(2, a0).offset(0x34L).get() + MEMORY.ref(2, a0).offset(0x3cL).get() + a2;
                MEMORY.ref(2, s0).offset(0x22L).setu(v1);
                MEMORY.ref(2, s0).offset(0x1aL).setu(v1);
                v0 = MEMORY.ref(1, a0).offset(0x36L).get();
                MEMORY.ref(1, s0).offset(0x1cL).setu(v0);
                MEMORY.ref(1, s0).offset(0xcL).setu(v0);
                v0 = _800c6b5c.get() + fp + s6;
                v1 = MEMORY.ref(1, v0).offset(0x36L).get() + MEMORY.ref(1, v0).offset(0x3aL).get();
                MEMORY.ref(1, s0).offset(0x24L).setu(v1);
                MEMORY.ref(1, s0).offset(0x14L).setu(v1);
                v0 = _800c6b5c.get() + fp + s6;
                v0 = MEMORY.ref(1, v0).offset(0x38L).get();
                MEMORY.ref(1, s0).offset(0x15L).setu(v0);
                MEMORY.ref(1, s0).offset(0xdL).setu(v0);
                v0 = _800c6b5c.get() + fp + s6;
                v1 = MEMORY.ref(1, v0).offset(0x38L).get() + MEMORY.ref(1, v0).offset(0x3cL).get();
                MEMORY.ref(1, s0).offset(0x25L).setu(v1);
                MEMORY.ref(1, s0).offset(0x1dL).setu(v1);
                v0 = _800c6b5c.get() + fp;
                v0 = v0 + s6;
                v1 = MEMORY.ref(2, v0).offset(0x3eL).getSigned();

                long t1;
                long t0;
                if((int)v1 >= 0x80L) {
                  t1 = 0x1L;
                  t0 = v1 - 0x80L;
                } else {
                  t1 = 0;

                  //LAB_800f4044
                  t0 = v1;
                }

                //LAB_800f4048
                long a3 = t0;

                //LAB_800f4058
                t0 = t0 / 0x10L;

                //LAB_800f4068
                a2 = (_800c7114.offset(2, (t1 * 0x2L + 0x1L) * 0x4L).get() + a3 % 0x10L) * 0x40L;
                v0 = _800c7114.offset(4, t1 * 0x8L).get() + t0 * 0x10L;
                v0 = v0 & 0x3f0L;
                v0 = (int)v0 >> 4;
                a2 = a2 | v0;
                MEMORY.ref(2, s0).offset(0xeL).setu(a2);
                v0 = GetTPage(0, s3, 704, 496);
                MEMORY.ref(2, s0).offset(0x16L).setu(v0);
                insertElementIntoLinkedList(tags_1f8003d0.getPointer() + 0x1cL, s0);

                v0 = _800c6b5c.get() + fp;

                if((MEMORY.ref(2, v0).offset(0x0L).get() & 0x61L) == 0) {
                  //LAB_800f4118
                  break;
                }

                if(s3 == 0x2L) {
                  break;
                }

                //LAB_800f4110
                s3 = s3 + 0x1L;

                //LAB_800f411c
              }
            }

            //LAB_800f4124
            s6 = s6 + 0x20L;
          }
        }
      }

      //LAB_800f4134
      fp = fp + 0xc4L;
    }
  }

  @Method(0x800f417cL)
  public static void FUN_800f417c() {
    long v0;
    long v1;
    long a0;
    long a1;
    long a2;
    long s0;
    long s1;
    long s2;
    v0 = 0x800c_0000L;
    v0 = MEMORY.ref(4, v0).offset(0x677cL).get();
    s0 = 0;
    if((int)v0 > 0) {
      s2 = -0x1L;
      v0 = 0x800c_0000L;
      s1 = v0 + 0x6c40L;

      //LAB_800f41ac
      do {
        v0 = MEMORY.ref(2, s1).offset(0x0L).getSigned();

        if(v0 == s2) {
          v0 = 0x800c_0000L;
          v1 = MEMORY.ref(4, v0).offset(-0x1a30L).get();
          v0 = 0x1L;
          if(v1 == v0) {
            a0 = s0;
            FUN_800ef8d8(a0);
          }
        }
        v0 = 0x800c_0000L;

        //LAB_800f41dc
        v0 = MEMORY.ref(4, v0).offset(0x677cL).get();
        s0 = s0 + 0x1L;
        s1 = s1 + 0x3cL;
      } while((int)s0 < (int)v0);

      s0 = 0;
    }

    //LAB_800f41f4
    //LAB_800f41f8
    do {
      s0 = s0 + 0x1L;
    } while((int)s0 < 0x3L);

    s0 = 0;
    a2 = -0x1L;
    v0 = 0x800c_0000L;
    a1 = MEMORY.ref(4, v0).offset(0x6c2cL).get();
    a0 = 0x3fL;
    v0 = 0x800c_0000L;
    v1 = v0 + 0x6c40L;

    //LAB_800f4220
    do {
      v0 = MEMORY.ref(2, v1).offset(0x0L).getSigned();

      if(v0 != a2) {
        MEMORY.ref(2, v1).offset(0x8L).setu(a0);
        MEMORY.ref(2, a1).offset(0x0L).setu(a0);
      }

      //LAB_800f4238
      a1 = a1 + 0x144L;
      a0 = a0 + 0x5eL;
      s0 = s0 + 0x1L;
      v1 = v1 + 0x3cL;
    } while((int)s0 < 0x3L);
  }

  @Method(0x800f4268L)
  public static void FUN_800f4268(final long s2, final long s3, final long s4) {
    final ScriptState<?> v1 = scriptStatePtrArr_800bc1c0.get((int)s2).deref();
    final BtldScriptData27c a0 = v1.innerStruct_00.derefAs(BtldScriptData27c.class);

    final short x;
    final short y;
    final short z;
    if((v1.ui_60.get() & 0x4L) != 0) {
      x = (short)(-a0._78.getZ() * 100);
      y = (short)(-a0._78.getY() * 100);
      z = (short)(-a0._78.getX() * 100);
    } else {
      //LAB_800f4314
      x = 0;
      y = -640;
      z = 0;
    }

    //LAB_800f4320
    final DVECTOR vec = FUN_800ec7e4(a0._148, x, y, z);

    //LAB_800f4394
    FUN_800f89f4(s2, 0, 0x2L, s3, FUN_800fa068(vec.getX() + centreScreenX_1f8003dc.getSigned()), FUN_800fa090(vec.getY() + centreScreenY_1f8003de.getSigned()), 60 / vsyncMode_8007a3b8.get() / 4, s4);
  }

  @Method(0x800f43dcL)
  public static long FUN_800f43dc(final RunningScript a3) {
    final long a1 = Math.min(_800c677c.get(), 3);

    //LAB_800f43f8
    long a0 = _8006e398.getAddress();

    //LAB_800f4410
    long a2;
    for(a2 = 0; a2 < a1; a2++) {
      if(MEMORY.ref(4, a0).offset(0xe40L).get() == a3.params_20.get(0).deref().get()) {
        break;
      }

      a0 = a0 + 0x4L;
    }

    //LAB_800f4430
    final BtldScriptData27c struct = scriptStatePtrArr_800bc1c0.get(a3.params_20.get(0).deref().get()).deref().innerStruct_00.derefAs(BtldScriptData27c.class);
    struct._0a.add((short)a3.params_20.get(1).deref().get());
    _800bc950.offset(a2 * 0x4L).addu(a3.params_20.get(1).deref().get());

    if(struct._0a.get() < struct._04.get(1).get() * 100) {
      //LAB_800f44d4
      if(struct._0a.get() < 500) {
        struct._0a.set((short)500);
      }
    } else {
      struct._0a.set((short)(struct._04.get(1).get() * 100));
      if(struct._04.get(1).get() * 100 >= 500) {
        struct._0a.set((short)500);
      }
    }

    //LAB_800f44ec
    if(struct._0a.get() < 0) {
      struct._0a.set((short)0);
    }

    //LAB_800f4500
    a3.params_20.get(2).deref().set(struct._0a.get());
    return 0;
  }

  @Method(0x800f4518L)
  public static long FUN_800f4518(final RunningScript a0) {
    final long a3 = Math.min(_800c677c.get(), 3);

    //LAB_800f4534
    //LAB_800f454c
    int i;
    for(i = 0; i < a3; i++) {
      if(_8006e398.offset(0xe40L).offset(i * 0x2L).get() == a0.params_20.get(0).deref().get()) {
        break;
      }
    }

    //LAB_800f456c
    final long a2 = _800c6c40.offset(i * 0x3cL).getAddress();
    MEMORY.ref(2, a2).offset(0xeL).setu(0);
    MEMORY.ref(2, a2).offset(0xcL).setu(0);
    MEMORY.ref(2, a2).offset(0xeL).setu(a0.params_20.get(1).deref().get());

    final long a1 = scriptStatePtrArr_800bc1c0.get(a0.params_20.get(0).deref().get()).deref().innerStruct_00.getPointer(); //TODO
    MEMORY.ref(2, a1).offset(0xaL).subu(a0.params_20.get(2).deref().get());

    if(MEMORY.ref(2, a1).offset(0xaL).getSigned() <= 0) {
      MEMORY.ref(2, a1).offset(0xaL).setu(0);
      MEMORY.ref(2, a2).offset(0x6L).and(0xfff3L);
    }

    //LAB_800f45f8
    return 0;
  }

  @Method(0x800f480cL)
  public static long FUN_800f480c(final RunningScript s0) {
    long v0;
    long v1;
    long a0;
    BtldScriptData27c a1 = null;
    long t0;
    long t5;
    v0 = 0x800c_0000L;
    t5 = v0 + 0x7148L;
    final long[] sp0x10 = new long[8];
    for(int i = 0; i < sp0x10.length; i++) {
      sp0x10[i] = MEMORY.ref(4, t5).offset(i * 0x4L).get();
    }

    t0 = s0.params_20.get(0).deref().get();

    //LAB_800f489c
    for(a0 = 0; a0 < _800c677c.get(); a0++) {
      a1 = scriptStatePtrArr_800bc1c0.get((int)_8006e398.offset(4, 0xe40L).offset(a0 * 0x4L).get()).deref().innerStruct_00.derefAs(BtldScriptData27c.class);

      if(_800c6c34.deref(2).offset(0x4L).getSigned() == a1._272.get()) {
        break;
      }
    }

    //LAB_800f48d8
    if((a1._14.get() & 0x8L) != 0) {
      t0 = 0x3L;
    }

    //LAB_800f48f4
    v1 = FUN_800f7768(sp0x10[(int)t0 * 2], sp0x10[(int)t0 * 2 + 1]);
    if(v1 == 0) {
      return 0x2L;
    }

    if(v1 == 0x1L) {
      //LAB_800f4930
      v1 = _800c6c34.deref(4).offset(0x48L).get();
    } else {
      //LAB_800f4944
      //LAB_800f4948
      v1 = -0x1L;
    }

    //LAB_800f4950
    s0.params_20.get(1).deref().set((int)v1);

    //LAB_800f4954
    return 0;
  }

  @Method(0x800f4964L)
  public static void FUN_800f4964() {
    final long v0 = _800c6b60.get();
    MEMORY.ref(2, v0).offset(0x00L).setu(0);
    MEMORY.ref(2, v0).offset(0x02L).setu(0);
    MEMORY.ref(2, v0).offset(0x04L).setu(0);
    MEMORY.ref(2, v0).offset(0x06L).setu(0);
    MEMORY.ref(2, v0).offset(0x08L).setu(0);
    MEMORY.ref(2, v0).offset(0x0aL).setu(0);
    MEMORY.ref(2, v0).offset(0x0cL).setu(0);
    MEMORY.ref(2, v0).offset(0x0eL).setu(0);
    MEMORY.ref(2, v0).offset(0x10L).setu(0);
    MEMORY.ref(2, v0).offset(0x12L).setu(0);
    MEMORY.ref(2, v0).offset(0x14L).setu(0);
    MEMORY.ref(2, v0).offset(0x16L).setu(0x1000L);
    MEMORY.ref(2, v0).offset(0x18L).setu(0);
    MEMORY.ref(2, v0).offset(0x1aL).setu(0);
    MEMORY.ref(2, v0).offset(0x1cL).setu(-0x1L);
    MEMORY.ref(2, v0).offset(0x22L).setu(0);
    MEMORY.ref(2, v0).offset(0x24L).setu(0);
  }

  @Method(0x800f49bcL)
  public static void FUN_800f49bc(final long a0, final long a1) {
    long a2 = _800c6b60.get();
    MEMORY.ref(2, a2).offset(0x0L).setu(0x1L);
    MEMORY.ref(2, a2).offset(0x14L).setu(0x1L);
    MEMORY.ref(2, a2).offset(0xaL).setu(a1 & 0x1L);
    MEMORY.ref(2, a2).offset(0x4L).setu(0xa0L);
    MEMORY.ref(2, a2).offset(0x6L).setu(0x90L);
    MEMORY.ref(2, a2).offset(0xeL).setu(0x2bL);
    MEMORY.ref(2, a2).offset(0x16L).setu(0x1000L);
    MEMORY.ref(2, a2).offset(0x1cL).setu(-0x1L);
    MEMORY.ref(2, a2).offset(0xcL).setu(0x20L);
    MEMORY.ref(2, a2).offset(0x12L).setu(0);
    MEMORY.ref(2, a2).offset(0x10L).setu(0);
    MEMORY.ref(2, a2).offset(0x18L).setu(0);
    MEMORY.ref(2, a2).offset(0x1aL).setu(0);
    MEMORY.ref(2, a2).offset(0x1eL).setu(0);
    MEMORY.ref(2, a2).offset(0x20L).setu(0);
    MEMORY.ref(2, a2).offset(0x8L).setu(a0);

    long v1 = MEMORY.ref(2, a2).offset(0xaL).getSigned();

    //LAB_800f4a58
    if(v1 == 0) {
      //LAB_800f4a6c
      //LAB_800f4a7c
      for(int i = 0; i < 32; i++) {
        if(gameState_800babc8._2e9.get(i).get() == 0xff) {
          break;
        }
      }

      //LAB_800f4a9c
      FUN_800f83c8();
      _800c6b60.deref(2).offset(0x22L).setu(_800c6b70.get());
    } else if(v1 == 0x1L) {
      //LAB_800f4abc
      //LAB_800f4ae0
      int v1_0;
      for(v1_0 = 0; v1_0 < _800c677c.get(); v1_0++) {
        if(_800c6960.offset(v1_0 * 0x9L).get() == MEMORY.ref(1, a2).offset(0x8L).get()) {
          break;
        }
      }

      //LAB_800f4b00
      v1_0 = v1_0 * 9;

      //LAB_800f4b18
      int i;
      for(i = 1; i < 9; i++) {
        if(_800c6960.offset(v1_0).offset(i).get() == 0xffL) {
          break;
        }
      }

      //LAB_800f4b3c
      _800c6b60.deref(2).offset(0x22L).setu(i);
    } else if(v1 == 0x2L) {
      //LAB_800f4b4c
      MEMORY.ref(2, a2).offset(0x22L).setu(0);
    }

    //LAB_800f4b50
    //LAB_800f4b54
    long v0 = _800c6b60.get() + 0x24L;
    v1 = 0x9L;

    //LAB_800f4b60
    do {
      MEMORY.ref(4, v0).offset(0x7cL).setu(0);
      v1 = v1 - 0x1L;
      v0 = v0 - 0x4L;
    } while((int)v1 >= 0);
  }

  @Method(0x800f4b80L)
  public static void FUN_800f4b80() {
    long v0;
    long v1;
    long a0;
    long a1;
    long a2;
    long a3;
    long t0;
    long t1;
    long t2;
    long s0;
    long s1;
    long s2;
    long sp18;
    long sp14;
    long sp10;
    long sp1c;
    v0 = 0x800c_0000L;
    v1 = MEMORY.ref(4, v0).offset(0x6b60L).get();
    v0 = MEMORY.ref(2, v1).offset(0x0L).getSigned();
    a0 = MEMORY.ref(2, v1).offset(0x0L).get();
    if(v0 == 0) {
      return;
    }

    v0 = MEMORY.ref(2, v1).offset(0xaL).getSigned();

    if(v0 != 0) {
      s0 = 0x80L;
    } else {
      s0 = 0xbaL;
    }

    //LAB_800f4bc0
    v0 = a0 - 0x1L;
    switch((short)v0) {
      case 0 -> {
        v0 = 0x800c_0000L;
        a3 = MEMORY.ref(4, v0).offset(0x6b60L).get();
        v0 = MEMORY.ref(2, a3).offset(0xaL).getSigned();
        MEMORY.ref(4, a3).offset(0x90L).setu(0);
        MEMORY.ref(4, a3).offset(0xa0L).setu(0);
        MEMORY.ref(2, a3).offset(0x12L).setu(0);
        MEMORY.ref(2, a3).offset(0x10L).setu(0);
        if(v0 == 0) {
          v1 = MEMORY.ref(2, a3).offset(0x26L).get();
          a0 = MEMORY.ref(2, a3).offset(0x28L).get();
          a1 = MEMORY.ref(2, a3).offset(0x2aL).get();
          a2 = MEMORY.ref(4, a3).offset(0x2cL).get();
          v0 = MEMORY.ref(2, a3).offset(0x2L).get();
          MEMORY.ref(2, a3).offset(0x24L).setu(v1);
          v1 = MEMORY.ref(2, a3).offset(0x22L).getSigned();
          v0 = v0 | 0x20L;
          MEMORY.ref(2, a3).offset(0x2L).setu(v0);
          v0 = MEMORY.ref(2, a3).offset(0x24L).getSigned();
          MEMORY.ref(2, a3).offset(0x1eL).setu(a0);
          a0 = MEMORY.ref(2, a3).offset(0x1eL).getSigned();
          MEMORY.ref(2, a3).offset(0x20L).setu(a1);
          MEMORY.ref(4, a3).offset(0x94L).setu(a2);
          v1 = v1 - 0x1L;
          v0 = v0 + a0;
          if((int)v1 < (int)v0) {
            v0 = MEMORY.ref(2, a3).offset(0x24L).get() - 0x1L;
            MEMORY.ref(2, a3).offset(0x24L).setu(v0);
            v0 = v0 << 16;
            if((int)v0 < 0) {
              v1 = MEMORY.ref(2, a3).offset(0x1aL).get();
              v0 = MEMORY.ref(2, a3).offset(0x1aL).getSigned();
              MEMORY.ref(2, a3).offset(0x24L).setu(0);
              MEMORY.ref(2, a3).offset(0x1eL).setu(0);
              MEMORY.ref(2, a3).offset(0x20L).setu(v1);
              v1 = v1 << 16;
              v1 = (int)v1 >> 16;
              v0 = v0 - v1;
              MEMORY.ref(4, a3).offset(0x94L).setu(v0);
            }
          }
        } else {
          //LAB_800f4ca0
          v0 = MEMORY.ref(2, a3).offset(0x30L).get();
          MEMORY.ref(2, a3).offset(0x1eL).setu(0);
          MEMORY.ref(2, a3).offset(0x20L).setu(0);
          MEMORY.ref(4, a3).offset(0x94L).setu(0);
          MEMORY.ref(2, a3).offset(0x24L).setu(v0);
        }

        //LAB_800f4cb4
        v0 = FUN_800f56c4();
        v1 = 0x800c_0000L;
        a0 = MEMORY.ref(4, v1).offset(0x6b60L).get();
        MEMORY.ref(2, a0).offset(0x1cL).setu(v0);
        v0 = MEMORY.ref(2, a0).offset(0x2L).get();
        v1 = 0x7L;
        MEMORY.ref(2, a0).offset(0x0L).setu(v1);
        v0 = v0 | 0x40L;
        MEMORY.ref(2, a0).offset(0x2L).setu(v0);
      }

      case 1 -> {
        s0 = 0x800c_0000L;
        v1 = MEMORY.ref(4, s0).offset(0x6b60L).get();
        v0 = MEMORY.ref(2, v1).offset(0x2L).get();
        v0 = v0 & 0xfcffL;
        MEMORY.ref(2, v1).offset(0x2L).setu(v0);
        v0 = FUN_800f56c4();
        v1 = 0x8008_0000L;
        t2 = MEMORY.ref(4, s0).offset(0x6b60L).get();
        a0 = MEMORY.ref(4, v1).offset(-0x5c68L).get();
        MEMORY.ref(2, t2).offset(0x1cL).setu(v0);
        if((a0 & 0x4L) != 0) {
          if(MEMORY.ref(2, t2).offset(0x24L).getSigned() != 0) {
            MEMORY.ref(4, t2).offset(0x88L).setu(0x2L);
            MEMORY.ref(2, t2).offset(0x24L).setu(0);
            MEMORY.ref(2, t2).offset(0x0L).setu(0x5L);
            playSound(0, 1, 0, 0, (short)0, (short)0);
          }

          break;
        }

        //LAB_800f4d54
        if((a0 & 0x1L) != 0) {
          s0 = MEMORY.ref(2, t2).offset(0x24L).getSigned();
          v0 = MEMORY.ref(2, t2).offset(0x1eL).getSigned();
          v1 = MEMORY.ref(2, t2).offset(0x22L).getSigned();
          a0 = MEMORY.ref(2, t2).offset(0x22L).get();
          v0 = v0 + 0x6L;
          v1 = v1 - 0x1L;
          if((int)v1 >= (int)v0) {
            v0 = 0x6L;
          } else {
            v0 = MEMORY.ref(2, t2).offset(0x1eL).get();
            v0 = v0 + 0x1L;

            //LAB_800f4d8c
            v0 = a0 - v0;
          }

          //LAB_800f4d90
          MEMORY.ref(2, t2).offset(0x24L).setu(v0);
          v0 = 0x800c_0000L;
          v1 = MEMORY.ref(4, v0).offset(0x6b60L).get();

          a0 = MEMORY.ref(2, v1).offset(0x24L).getSigned();
          v0 = 0x2L;
          MEMORY.ref(4, v1).offset(0x88L).setu(v0);
          v0 = 0x5L;
          MEMORY.ref(2, v1).offset(0x0L).setu(v0);
          if(s0 != a0) {
            playSound(0, 1, 0, 0, (short)0, (short)0);
          }

          break;
        }
        v0 = a0 & 0x8L;

        //LAB_800f4dc4
        if(v0 != 0) {
          v0 = MEMORY.ref(2, t2).offset(0x1eL).getSigned();
          v1 = MEMORY.ref(2, t2).offset(0x1eL).get();
          if(v0 == 0) {
            break;
          }
          v1 = v1 - 0x7L;
          if((int)v0 < 0x7L) {
            v0 = MEMORY.ref(2, t2).offset(0x1aL).get();
            MEMORY.ref(2, t2).offset(0x24L).setu(0);
            MEMORY.ref(2, t2).offset(0x1eL).setu(0);
          } else {
            //LAB_800f4df4
            v0 = MEMORY.ref(2, t2).offset(0x20L).get();
            MEMORY.ref(2, t2).offset(0x1eL).setu(v1);
            v0 = v0 + 0x62L;
          }

          //LAB_800f4e00
          MEMORY.ref(2, t2).offset(0x20L).setu(v0);
          v0 = 0x800c_0000L;
          t0 = MEMORY.ref(4, v0).offset(0x6b60L).get();
          v1 = MEMORY.ref(2, t0).offset(0x1aL).getSigned();
          t1 = MEMORY.ref(2, t0).offset(0x20L).getSigned();
          v0 = 0x2L;
          MEMORY.ref(4, t0).offset(0x88L).setu(v0);
          v0 = 0x5L;
          MEMORY.ref(2, t0).offset(0x0L).setu(v0);
          v1 = v1 - t1;
          MEMORY.ref(4, t0).offset(0x94L).setu(v1);
          playSound(0, 1, 0, 0, (short)0, (short)0);
          break;
        }
        v0 = a0 & 0x2L;

        //LAB_800f4e40
        if(v0 != 0) {
          v1 = MEMORY.ref(2, t2).offset(0x1eL).getSigned();
          v0 = MEMORY.ref(2, t2).offset(0x22L).getSigned();
          a0 = MEMORY.ref(2, t2).offset(0x1eL).get();
          v1 = v1 + 0x6L;
          v0 = v0 - 0x1L;
          if((int)v1 >= (int)v0) {
            break;
          }
          v1 = a0 + 0x7L;
          v0 = MEMORY.ref(2, t2).offset(0x20L).get();
          MEMORY.ref(2, t2).offset(0x1eL).setu(v1);
          v1 = MEMORY.ref(2, t2).offset(0x1eL).getSigned();
          v0 = v0 - 0x62L;
          MEMORY.ref(2, t2).offset(0x20L).setu(v0);
          v0 = MEMORY.ref(2, t2).offset(0x22L).getSigned();
          v1 = v1 + 0x6L;
          v0 = v0 - 0x1L;
          if((int)v1 >= (int)v0) {
            MEMORY.ref(2, t2).offset(0x24L).setu(0);
          }

          //LAB_800f4e98
          t0 = MEMORY.ref(4, s0).offset(0x6b60L).get();
          v1 = MEMORY.ref(2, t0).offset(0x1aL).getSigned();
          t1 = MEMORY.ref(2, t0).offset(0x20L).getSigned();
          v0 = 0x2L;
          MEMORY.ref(4, t0).offset(0x88L).setu(v0);
          v0 = 0x5L;
          MEMORY.ref(2, t0).offset(0x0L).setu(v0);
          v1 = v1 - t1;
          MEMORY.ref(4, t0).offset(0x94L).setu(v1);
          playSound(0, 1, 0, 0, (short)0, (short)0);
          break;
        }
        v0 = 0x8008_0000L;

        //LAB_800f4ecc
        v1 = MEMORY.ref(4, v0).offset(-0x5c64L).get();
        v0 = v1 & 0x1000L;
        if(v0 != 0) {
          v0 = MEMORY.ref(2, t2).offset(0x24L).getSigned();
          v1 = MEMORY.ref(2, t2).offset(0x24L).get();
          if(v0 != 0) {
            v0 = v1 - 0x1L;
            MEMORY.ref(2, t2).offset(0x24L).setu(v0);
            v0 = 0x2L;
            MEMORY.ref(4, t2).offset(0x88L).setu(v0);
            v0 = 0x5L;
            MEMORY.ref(2, t2).offset(0x0L).setu(v0);
          } else {
            //LAB_800f4f18
            v0 = MEMORY.ref(2, t2).offset(0x1eL).getSigned();

            if(v0 == 0) {
              break;
            }
            t1 = MEMORY.ref(2, t2).offset(0x20L).getSigned();
            v1 = MEMORY.ref(2, t2).offset(0x20L).get();
            v0 = 0x5L;
            MEMORY.ref(4, t2).offset(0x80L).setu(v0);
            t0 = MEMORY.ref(2, t2).offset(0x80L).get();
            v0 = 0x3L;
            MEMORY.ref(2, t2).offset(0x0L).setu(v0);
            v0 = MEMORY.ref(2, t2).offset(0x1eL).get();
            v1 = v1 + t0;
            MEMORY.ref(2, t2).offset(0x20L).setu(v1);
            v1 = MEMORY.ref(2, t2).offset(0x2L).get();
            v0 = v0 - 0x1L;
            MEMORY.ref(4, t2).offset(0x7cL).setu(t1);
            MEMORY.ref(2, t2).offset(0x1eL).setu(v0);
            v1 = v1 | 0x200L;
            MEMORY.ref(2, t2).offset(0x2L).setu(v1);
          }
          playSound(0, 1, 0, 0, (short)0, (short)0);
          break;
        }
        v0 = v1 & 0x4000L;

        //LAB_800f4f74
        if(v0 != 0) {
          a0 = MEMORY.ref(2, t2).offset(0x22L).getSigned();
          v1 = MEMORY.ref(2, t2).offset(0x24L).getSigned();
          v0 = a0 - 0x1L;
          if(v1 != v0) {
            v0 = MEMORY.ref(2, t2).offset(0x1eL).getSigned();

            v0 = v0 + 0x1L;
            v0 = v0 + v1;
            if((int)v0 < (int)a0) {
              playSound(0, 1, 0, 0, (short)0, (short)0);
              v0 = 0x7L;
              a2 = MEMORY.ref(4, s0).offset(0x6b60L).get();
              v0 = v0 - 0x1L;
              v1 = MEMORY.ref(2, a2).offset(0x24L).getSigned();
              a0 = MEMORY.ref(2, a2).offset(0x24L).get();
              if(v1 != v0) {
                v0 = a0 + 0x1L;
                MEMORY.ref(2, a2).offset(0x24L).setu(v0);
                v0 = 0x2L;
                MEMORY.ref(4, a2).offset(0x88L).setu(v0);
                v0 = 0x5L;
                MEMORY.ref(2, a2).offset(0x0L).setu(v0);
              } else {
                //LAB_800f4ff8
                a1 = MEMORY.ref(2, a2).offset(0x20L).getSigned();
                v1 = MEMORY.ref(2, a2).offset(0x20L).get();
                v0 = -0x5L;
                MEMORY.ref(4, a2).offset(0x80L).setu(v0);
                a0 = MEMORY.ref(2, a2).offset(0x80L).get();
                v0 = 0x4L;
                MEMORY.ref(2, a2).offset(0x0L).setu(v0);
                v0 = MEMORY.ref(2, a2).offset(0x1eL).get();
                v1 = v1 + a0;
                MEMORY.ref(2, a2).offset(0x20L).setu(v1);
                v1 = MEMORY.ref(2, a2).offset(0x2L).get();
                v0 = v0 + 0x1L;
                MEMORY.ref(4, a2).offset(0x7cL).setu(a1);
                MEMORY.ref(2, a2).offset(0x1eL).setu(v0);
                v1 = v1 | 0x100L;
                MEMORY.ref(2, a2).offset(0x2L).setu(v1);
              }
            }
          }

          break;
        }
        v0 = a0 & 0x20L;

        //LAB_800f5044
        MEMORY.ref(4, t2).offset(0x90L).setu(0);
        if(v0 != 0) {
          v1 = _800c677c.get();

          s0 = 0;
          v0 = 0x800c_0000L;
          a2 = v0 - 0x3e40L;
          a1 = MEMORY.ref(2, t2).offset(0x8L).getSigned();
          a0 = v1;
          v0 = 0x8007_0000L;
          v1 = v0 - 0x1c68L;

          //LAB_800f5078
          do {
            s2 = MEMORY.ref(4, v1).offset(0xe40L).get();
            s1 = MEMORY.ref(4, a2).offset(s2 * 0x4L).deref(4).get();

            if(a1 == MEMORY.ref(2, s1).offset(0x272L).getSigned()) {
              //LAB_800f503c
              _800c6980.setu(s0);
              break;
            }
            s0 = s0 + 0x1L;
            v1 = v1 + 0x4L;
          } while((int)s0 < (int)a0);

          //LAB_800f50b8
          v0 = 0x800c_0000L;
          v1 = MEMORY.ref(4, v0).offset(0x6b60L).get();

          v0 = MEMORY.ref(2, v1).offset(0xaL).getSigned();

          if(v0 == 0) {
            v0 = MEMORY.ref(2, v1).offset(0x1cL).get();
            MEMORY.ref(2, s1).offset(0x52L).setu(v0);
            FUN_800f7a74(s2);
            v0 = MEMORY.ref(2, s1).offset(0xd4L).get();

            v0 = v0 & 0x4L;
            v1 = 0x800c_0000L;
            if(v0 != 0) {
              v0 = 0x1L;
              MEMORY.ref(4, v1).offset(0x6b68L).setu(v0);
            } else {
              //LAB_800f5100
              v0 = 0x800c_0000L;
              MEMORY.ref(4, v0).offset(0x6b68L).setu(0);
            }

            //LAB_800f5108
            v0 = MEMORY.ref(2, s1).offset(0xd4L).get();
            v0 = v0 & 0x2L;
            v1 = 0x800c_0000L;
            if(v0 != 0) {
              v0 = 0x1L;
              MEMORY.ref(4, v1).offset(0x69c8L).setu(v0);
            } else {
              //LAB_800f5128
              v0 = 0x800c_0000L;
              MEMORY.ref(4, v0).offset(0x69c8L).setu(0);
            }
          } else {
            //LAB_800f5134
            a0 = MEMORY.ref(2, v1).offset(0x1cL).getSigned();

            v0 = FUN_800f9e50(a0);
            s1 = v0;
            v0 = MEMORY.ref(2, s1).offset(0xcL).getSigned();
            v1 = MEMORY.ref(2, s1).offset(0xa0L).getSigned();

            if((int)v0 < (int)v1) {
              //LAB_800f5160
              //LAB_800f5168
              playSound(0, 3, 0, 0, (short)0, (short)0);
              break;
            }

            //LAB_800f517c
            v0 = 0x800c_0000L;
            v0 = MEMORY.ref(4, v0).offset(0x6b5cL).get();

            MEMORY.ref(2, v0).offset(0x0L).setu(0);
            MEMORY.ref(2, v0).offset(0x2L).setu(0);
          }

          //LAB_800f5190
          playSound(0, 2, 0, 0, (short)0, (short)0);
          a1 = 0x800c_0000L;
          a0 = MEMORY.ref(4, a1).offset(0x6b60L).get();
          v0 = MEMORY.ref(2, a0).offset(0x2L).get();
          v1 = MEMORY.ref(2, a0).offset(0xaL).getSigned();
          MEMORY.ref(4, a0).offset(0x8cL).setu(0);
          v0 = v0 | 0x4L;
          v0 = v0 & 0xfff7L;
          MEMORY.ref(2, a0).offset(0x2L).setu(v0);
          if(v1 == 0) {
            v0 = MEMORY.ref(2, a0).offset(0x1aL).getSigned();
            v1 = MEMORY.ref(2, a0).offset(0x20L).getSigned();
            v0 = v0 - v1;
            MEMORY.ref(4, a0).offset(0x94L).setu(v0);
          }

          //LAB_800f51e8
          v0 = MEMORY.ref(4, a1).offset(0x6b60L).get();
          v1 = MEMORY.ref(2, v0).offset(0x2L).get();
          a0 = 0x6L;
          MEMORY.ref(2, v0).offset(0x0L).setu(a0);
          v1 = v1 & 0xfffdL;
          MEMORY.ref(2, v0).offset(0x2L).setu(v1);
          break;
        }

        //LAB_800f5208
        v0 = a0 & 0x40L;
        if(v0 != 0) {
          playSound(0, 3, 0, 0, (short)0, (short)0);
          v0 = MEMORY.ref(4, s0).offset(0x6b60L).get();
          v1 = MEMORY.ref(2, v0).offset(0x2L).get();
          a0 = 0x8L;
          MEMORY.ref(2, v0).offset(0x0L).setu(a0);
          v1 = v1 & 0xfff7L;
          MEMORY.ref(2, v0).offset(0x2L).setu(v1);
        }
      }

      case 2 -> {
        v0 = 0x800c_0000L;
        a0 = MEMORY.ref(4, v0).offset(0x6b60L).get();
        v0 = MEMORY.ref(4, a0).offset(0x90L).get();
        s0 = MEMORY.ref(4, a0).offset(0x80L).get();
        v0 = v0 + 0x1L;
        MEMORY.ref(4, a0).offset(0x90L).setu(v0);
        if((int)v0 >= 0x3L) {
          s0 = s0 << 1;
        }

        //LAB_800f5278
        v0 = MEMORY.ref(2, a0).offset(0x20L).get();
        v1 = MEMORY.ref(2, a0).offset(0x7cL).get();
        v0 = v0 + s0;
        a1 = v1 + 0xeL;
        MEMORY.ref(2, a0).offset(0x20L).setu(v0);
        v0 = v0 << 16;
        v1 = a1 << 16;
        if((int)v0 >= (int)v1) {
          v0 = 0x2L;
          MEMORY.ref(2, a0).offset(0x20L).setu(a1);
          MEMORY.ref(2, a0).offset(0x0L).setu(v0);
        }
      }

      case 3 -> {
        v0 = 0x800c_0000L;
        a0 = MEMORY.ref(4, v0).offset(0x6b60L).get();
        v0 = MEMORY.ref(4, a0).offset(0x90L).get();
        s0 = MEMORY.ref(4, a0).offset(0x80L).get();
        v0 = v0 + 0x1L;
        MEMORY.ref(4, a0).offset(0x90L).setu(v0);
        if((int)v0 >= 0x3L) {
          s0 = s0 << 1;
        }

        //LAB_800f52d4
        v0 = MEMORY.ref(2, a0).offset(0x20L).get();
        v1 = MEMORY.ref(2, a0).offset(0x7cL).get();
        v0 = v0 + s0;
        a1 = v1 - 0xeL;
        MEMORY.ref(2, a0).offset(0x20L).setu(v0);
        v0 = v0 << 16;
        v1 = a1 << 16;
        if((int)v1 >= (int)v0) {
          v0 = 0x2L;

          //LAB_800f5300
          MEMORY.ref(2, a0).offset(0x20L).setu(a1);
          MEMORY.ref(2, a0).offset(0x0L).setu(v0);
        }
      }

      case 4 -> {
        v0 = 0x800c_0000L;
        v1 = MEMORY.ref(4, v0).offset(0x6b60L).get();
        v0 = MEMORY.ref(4, v1).offset(0x90L).get();
        s0 = MEMORY.ref(4, v1).offset(0x88L).get();
        v0 = v0 + 0x1L;
        MEMORY.ref(4, v1).offset(0x90L).setu(v0);
        if((int)v0 >= 0x3L) {
          s0 = (int)s0 >> 1;
        }

        //LAB_800f5338
        s0 = s0 - 0x1L;
        if((int)s0 <= 0) {
          v0 = 0x2L;
          MEMORY.ref(2, v1).offset(0x0L).setu(v0);
        }
      }

      case 5 -> {
        s0 = 0x800c_0000L;
        v0 = MEMORY.ref(4, s0).offset(0x6b60L).get();
        MEMORY.ref(4, v0).offset(0xa0L).setu(0);
        v0 = FUN_800f56c4();
        v1 = 0x800c_0000L;
        a0 = MEMORY.ref(4, s0).offset(0x6b60L).get();
        v1 = MEMORY.ref(4, v1).offset(0x677cL).get();
        s0 = 0;
        MEMORY.ref(2, a0).offset(0x1cL).setu(v0);
        v0 = 0x800c_0000L;
        a2 = v0 - 0x3e40L;
        a1 = MEMORY.ref(2, a0).offset(0x8L).getSigned();
        a0 = v1;
        v0 = 0x8007_0000L;
        v1 = v0 - 0x1c68L;

        //LAB_800f538c
        do {
          s2 = MEMORY.ref(4, v1).offset(0xe40L).get();

          v0 = s2 << 2;
          v0 = v0 + a2;
          v0 = MEMORY.ref(4, v0).offset(0x0L).get();

          s1 = MEMORY.ref(4, v0).offset(0x0L).get();

          v0 = MEMORY.ref(2, s1).offset(0x272L).getSigned();

          if(a1 == v0) {
            break;
          }
          s0 = s0 + 0x1L;
          v1 = v1 + 0x4L;
        } while((int)s0 < (int)a0);

        //LAB_800f53c8
        v0 = 0x800c_0000L;
        v0 = MEMORY.ref(4, v0).offset(0x6b60L).get();
        v0 = MEMORY.ref(2, v0).offset(0xaL).getSigned();
        if(v0 == 0) {
          v0 = 0x800c_0000L;
          a0 = MEMORY.ref(4, v0).offset(0x6b68L).get();
          v0 = 0x800c_0000L;
          a1 = MEMORY.ref(4, v0).offset(0x69c8L).get();
        } else {
          //LAB_800f53f8
          a1 = MEMORY.ref(2, s1).offset(0x94L).get();

          a0 = a1 & 0x40L;
          a0 = 0 < a0 ? 1 : 0;
          a1 = a1 & 0x8L;
          a1 = 0 < a1 ? 1 : 0;
        }

        //LAB_800f5410
        v0 = FUN_800f7768(a0, a1);
        s0 = v0;
        if(s0 != 0) {
          v0 = 0x1L;
          if(s0 == v0) {
            s2 = 0x800c_0000L;
            v1 = MEMORY.ref(4, s2).offset(0x6b60L).get();
            v0 = MEMORY.ref(2, v1).offset(0xaL).getSigned();

            if(v0 == 0) {
              a0 = MEMORY.ref(1, v1).offset(0x1cL).get();
              a0 = a0 - 0x40L;
              a0 = a0 & 0xffL;
              FUN_800232dc(a0);
            }

            //LAB_800f545c
            v0 = MEMORY.ref(4, s2).offset(0x6b60L).get();
            v0 = MEMORY.ref(2, v0).offset(0xaL).getSigned();

            if(v0 == s0) {
              v0 = MEMORY.ref(2, s1).offset(0xcL).get();
              v1 = MEMORY.ref(2, s1).offset(0xa0L).get();

              v0 = v0 - v1;
              MEMORY.ref(2, s1).offset(0xcL).setu(v0);
            }

            //LAB_800f5488
            playSound(0, 2, 0, 0, (short)0, (short)0);
            v1 = MEMORY.ref(4, s2).offset(0x6b60L).get();
            v0 = 0x9L;
            MEMORY.ref(4, v1).offset(0xa0L).setu(s0);
            MEMORY.ref(2, v1).offset(0x0L).setu(v0);
          } else {
            //LAB_800f54b4
            playSound(0, 0, 3, 0, (short)0, (short)0);
            v0 = 0x800c_0000L;
            a0 = MEMORY.ref(4, v0).offset(0x6b60L).get();
            v0 = MEMORY.ref(2, a0).offset(0x2L).get();
            v1 = 0x7L;
            MEMORY.ref(2, a0).offset(0x0L).setu(v1);
            v0 = v0 & 0xfffbL;
            v0 = v0 | 0x20L;
            MEMORY.ref(2, a0).offset(0x2L).setu(v0);
          }
        }
      }

      case 6 -> {
        s1 = 0x800c_0000L;
        v1 = MEMORY.ref(4, s1).offset(0x6b60L).get();
        v0 = 0x2L;
        MEMORY.ref(2, v1).offset(0x0L).setu(v0);
        playSound(0, 4, 0, 0, (short)0, (short)0);
        a1 = MEMORY.ref(4, s1).offset(0x6b60L).get();
        v0 = 0x52L;
        MEMORY.ref(2, a1).offset(0x12L).setu(v0);
        v0 = MEMORY.ref(2, a1).offset(0x4L).get();
        a0 = MEMORY.ref(2, a1).offset(0x6L).get();
        v1 = s0 >>> 1;
        MEMORY.ref(2, a1).offset(0x10L).setu(s0);
        v0 = v0 - v1;
        v1 = MEMORY.ref(2, a1).offset(0x12L).get();
        v0 = v0 + 0x9L;
        MEMORY.ref(2, a1).offset(0x18L).setu(v0);
        v0 = -0x10L;
        a0 = a0 - v1;
        v1 = MEMORY.ref(2, a1).offset(0x2L).get();
        v0 = v0 + a0;
        MEMORY.ref(2, a1).offset(0x1aL).setu(v0);
        MEMORY.ref(2, a1).offset(0x20L).setu(v0);
        v1 = v1 | 0xbL;
        MEMORY.ref(2, a1).offset(0x2L).setu(v1);
        v1 = v1 & 0x20L;
        if(v1 != 0) {
          v1 = MEMORY.ref(2, a1).offset(0x94L).get();
          v0 = v0 - v1;
          MEMORY.ref(2, a1).offset(0x20L).setu(v0);
        }

        //LAB_800f5588
        v0 = MEMORY.ref(4, s1).offset(0x6b60L).get();
        v0 = MEMORY.ref(2, v0).offset(0xaL).getSigned();
        if(v0 != 0) {
          v0 = FUN_800f56c4();
          a0 = v0 << 16;
          v1 = MEMORY.ref(4, s1).offset(0x6b60L).get();
          a0 = (int)a0 >> 16;
          MEMORY.ref(2, v1).offset(0x1cL).setu(v0);
          v0 = FUN_800f9e50(a0);
          a0 = 0;
          a1 = 0x1L;
          a2 = a0;
          a3 = MEMORY.ref(2, v0).offset(0xa0L).getSigned();
          v0 = 0x118L;
          sp10 = v0;
          v0 = 0x87L;
          sp14 = v0;
          v0 = a1;
          sp18 = 0;
          sp1c = v0;
          FUN_800f3354(a0, a1, a2, a3, sp10, sp14, sp18, sp1c);
        }
      }

      case 7 -> {
        _800c69c8.setu(0);
        v1 = _800c6b60.get();
        a0 = _800c6b5c.get();
        _800c6b68.setu(0);
        MEMORY.ref(4, v1).offset(0xa0L).setu(-0x1L);
        MEMORY.ref(2, v1).offset(0x0L).setu(0x9L);
        MEMORY.ref(2, v1).offset(0x12L).setu(0);
        MEMORY.ref(2, v1).offset(0x10L).setu(0);
        MEMORY.ref(2, v1).offset(0x2L).and(0xfffcL);
        MEMORY.ref(2, a0).offset(0x0L).setu(0);
        MEMORY.ref(2, a0).offset(0x2L).setu(0);
      }

      case 8 -> {
        a3 = _800c6b60.get();
        if(MEMORY.ref(2, a3).offset(0xaL).getSigned() == 0) {
          v0 = MEMORY.ref(2, a3).offset(0x1aL).getSigned() - MEMORY.ref(2, a3).offset(0x20L).getSigned();
          MEMORY.ref(2, a3).offset(0x26L).setu(MEMORY.ref(2, a3).offset(0x24L).get());
          MEMORY.ref(2, a3).offset(0x28L).setu(MEMORY.ref(2, a3).offset(0x1eL).get());
          MEMORY.ref(2, a3).offset(0x2aL).setu(MEMORY.ref(2, a3).offset(0x20L).get());
          MEMORY.ref(4, a3).offset(0x94L).setu(v0);
          MEMORY.ref(4, a3).offset(0x2cL).setu(v0);
        }

        //LAB_800f568c
        FUN_800f4964();
      }
    }

    //LAB_800f5694
    //LAB_800f5698
    _800c6b60.deref(4).offset(0x84L).setu(_800bb0fc.get() & 0x7L);

    //LAB_800f56ac
  }

  @Method(0x800f56c4L)
  public static long FUN_800f56c4() {
    assert false;
    return 0;
  }

  @Method(0x800f57f8L)
  public static void FUN_800f57f8(long a0) {
    assert false;
  }

  @Method(0x800f5c94L)
  public static void FUN_800f5c94() {
    long v0;
    long v1;
    long a0;
    long a1;
    long a2;
    long a3;
    long t0;
    long s0;
    long s1;
    a0 = _800c6b60.get();

    if(MEMORY.ref(2, a0).offset(0x0L).getSigned() != 0) {
      v1 = MEMORY.ref(2, a0).offset(0x2L).get();

      if((v1 & 0x1L) != 0) {
        if((v1 & 0x2L) != 0) {
          FUN_800f57f8(MEMORY.ref(2, a0).offset(0xaL).getSigned());

          a1 = _800c6b60.get();
          if((MEMORY.ref(2, a1).offset(0x2L).get() & 0x8L) != 0) {
            //LAB_800f5d78
            //LAB_800f5d90
            t0 = MEMORY.ref(4, a1).offset(0x84L).get();
            s1 = t0 % 4;
            t0 = t0 / 0x4L;
            FUN_800f8cd8(MEMORY.ref(2, a1).offset(0x18L).get() - centreScreenX_1f8003dc.get() - 0x10L, MEMORY.ref(2, a1).offset(0x1aL).get() - centreScreenY_1f8003de.get() + MEMORY.ref(2, a1).offset(0x24L).getSigned() * 0xeL + 0x2L, s1 * 0x10L + 0xc0L & 0xf0L, t0 * 0x8L + 0x20L & 0xf8L, 0xfL, 0x8L, 0xdL, 0x1L);

            v1 = _800c6b60.get();
            if(MEMORY.ref(2, v1).offset(0xaL).getSigned() != 0) {
              s0 = 0;
            } else {
              s0 = 0x1aL;
            }

            //LAB_800f5e00
            a0 = MEMORY.ref(2, v1).offset(0x2L).get();

            if((a0 & 0x100L) != 0) {
              s1 = 0x2L;
            } else {
              s1 = 0;
            }

            //LAB_800f5e18
            if((a0 & 0x200L) != 0) {
              t0 = -0x2L;
            } else {
              t0 = 0;
            }

            //LAB_800f5e24
            if(MEMORY.ref(2, v1).offset(0x1eL).getSigned() > 0) {
              FUN_800f74f4(_800c7190.getAddress(), MEMORY.ref(2, v1).offset(0x4L).get() + s0 + 0x38L, MEMORY.ref(2, v1).offset(0x6L).get() + t0 - 0x64L, _800c7192.get(), _800c7193.get(), 0xdL, -0x1L, 0);
            }

            //LAB_800f5e7c
            a0 = _800c6b60.get();
            if(MEMORY.ref(2, a0).offset(0x1eL).getSigned() + 0x6L < MEMORY.ref(2, a0).offset(0x22L).getSigned() - 0x1L) {
              FUN_800f74f4(_800c7190.getAddress(), MEMORY.ref(2, a0).offset(0x4L).get() + s0 + 0x38L, MEMORY.ref(2, a0).offset(0x6L).get() + s1 - 0x7L, _800c7192.get(), _800c7193.get(), 0xdL, -0x1L, 0x1L);
            }
          }

          //LAB_800f5ee8
          v1 = _800c6b60.get();
          a2 = MEMORY.ref(2, v1).offset(0x10L).get() + 0x6L;
          v0 = (int)a2 >> 1;
          a3 = MEMORY.ref(2, v1).offset(0x12L).get() + 0x11L;
          FUN_800f1268(MEMORY.ref(2, v1).offset(0x4L).get() - v0, MEMORY.ref(2, v1).offset(0x6L).get() - a3, a2, a3, 0x8L);
        }

        //LAB_800f5f50
        v1 = _800c6b60.get();
        a0 = MEMORY.ref(2, v1).offset(0x2L).get();

        if((a0 & 0x40L) != 0) {
          s0 = MEMORY.ref(2, v1).offset(0xaL).getSigned();

          if(s0 == 0) {
            //LAB_800f5f8c
            s1 = 0x4L;
          } else if(s0 == 0x1L) {
            //LAB_800f5f94
            s1 = 0x5L;
            if((a0 & 0x2L) != 0) {
              v0 = FUN_800f9e50(MEMORY.ref(2, v1).offset(0x1cL).getSigned());
              FUN_800f3354(0, 0x1L, 0, MEMORY.ref(2, v0).offset(0xa0L).getSigned(), 0x118L, 0x87L, 0, s0);
              FUN_800f8cd8(236 - centreScreenX_1f8003dc.get(), 130 - centreScreenY_1f8003de.get(), 0x10L, 0x80L, 0x18L, 0x10L, 0x2cL, -0x1L);
              FUN_800f1268(0xecL, 0x82L, 0x40L, 0xeL, 0x8L);
            }
          } else {
            throw new RuntimeException("Undefined s1");
          }

          //LAB_800f604c
          //LAB_800f6050
          FUN_800f1268(0x2cL, 0x9cL, 0xe8L, 0xeL, 0x8L);
          FUN_800f8ac4((short)s1, _800c6b60.deref(2).offset(0x1cL).getSigned(), 0xa0L, 0xa3L);
        }
      }
    }

    //LAB_800f6088
  }

  @Method(0x800f60acL)
  public static void FUN_800f60ac() {
    final long v0 = _800c6c34.get();
    MEMORY.ref(2, v0).offset(0x0L).setu(0);
    MEMORY.ref(2, v0).offset(0x2L).setu(0);
    MEMORY.ref(2, v0).offset(0x4L).setu(0xffL);
    MEMORY.ref(2, v0).offset(0x8L).setu(0);
    MEMORY.ref(2, v0).offset(0x6L).setu(0);
    MEMORY.ref(2, v0).offset(0xcL).setu(0);
    MEMORY.ref(2, v0).offset(0xaL).setu(0);
    MEMORY.ref(2, v0).offset(0xeL).setu(0);
    MEMORY.ref(2, v0).offset(0x22L).setu(0);
    MEMORY.ref(2, v0).offset(0x24L).setu(0);
    MEMORY.ref(2, v0).offset(0x26L).setu(0);
    MEMORY.ref(2, v0).offset(0x28L).setu(0);
    MEMORY.ref(2, v0).offset(0x2aL).setu(0);
    MEMORY.ref(2, v0).offset(0x2cL).setu(0);

    //LAB_800f60fc
    for(int i = 0; i < 9; i++) {
      MEMORY.ref(2, v0).offset(0x10L).offset(i * 0x2L).setu(-0x1L);
    }

    //LAB_800f611c
    for(int i = 0; i < 10; i++) {
      MEMORY.ref(4, v0).offset(0x30L).offset(i * 0x4L).setu(0);
    }
  }

  @Method(0x800f6134L)
  public static void FUN_800f6134(long a0, long a1, long a2) {
    long v0;
    long v1;
    long a3;
    long t0;
    long t5;
    a3 = 0x8L;
    v1 = 0x800c_0000L;
    t5 = v1 + 0x7194L;

    final long[] sp0x10 = new long[8];
    for(int i = 0; i < 8; i++) {
      sp0x10[i] = MEMORY.ref(2, t5).offset(i * 0x2L).get();
    }

    v0 = _800c6c34.get();
    t0 = v0 + 0x10L;
    MEMORY.ref(2, v0).offset(0x0L).setu(0x1L);
    MEMORY.ref(2, v0).offset(0x2L).setu(0x2L);
    MEMORY.ref(2, v0).offset(0x6L).setu(0xa0L);
    MEMORY.ref(2, v0).offset(0x8L).setu(0xacL);
    MEMORY.ref(2, v0).offset(0x22L).setu(0);
    MEMORY.ref(2, v0).offset(0x24L).setu(0);
    MEMORY.ref(2, v0).offset(0x26L).setu(0);
    MEMORY.ref(2, v0).offset(0x28L).setu(0);
    MEMORY.ref(2, v0).offset(0x2aL).setu(0);
    MEMORY.ref(2, v0).offset(0x2cL).setu(0x80L);

    //LAB_800f61d8
    do {
      MEMORY.ref(2, t0).offset(0x10L).setu(-0x1L);
      a3 = a3 - 0x1L;
      t0 = t0 - 0x2L;
    } while((int)a3 >= 0);

    v0 = _800c6c34.get();
    a3 = 0x9L;
    v0 = v0 + 0x24L;

    //LAB_800f61f8
    do {
      MEMORY.ref(4, v0).offset(0x30L).setu(0);
      a3 = a3 - 0x1L;
      v0 = v0 - 0x4L;
    } while((int)a3 >= 0);

    t0 = _800c677c.get();

    if((int)t0 >= 0x4L) {
      t0 = 0x3L;
    }

    //LAB_800f6224
    v1 = _8006e398.getAddress();

    //LAB_800f6234
    for(a3 = 0; a3 < t0; a3++) {
      if(MEMORY.ref(4, v1).offset(0xe40L).get() == a0) {
        break;
      }

      v1 = v1 + 0x4L;
    }

    //LAB_800f6254
    v1 = _800c6c34.get();
    MEMORY.ref(2, v1).offset(0xeL).setu(0);
    MEMORY.ref(2, v1).offset(0x4L).setu(scriptStatePtrArr_800bc1c0.get((int)_8006e398.offset(a3 * 0x4L).offset(4, 0xe40L).get()).deref().innerStruct_00.derefAs(BtldScriptData27c.class)._272.get());

    //LAB_800f62a4
    a0 = 0;
    for(int i = 0; i < 8; i++) {
      if((a1 & 1 << i) != 0) {
        MEMORY.ref(2, v1).offset(0x10L).offset(a0).setu(sp0x10[i]);
        MEMORY.ref(2, v1).offset(0xeL).addu(0x1L);
        a0 = a0 + 0x2L;
      }

      //LAB_800f62d0
    }

    a1 = _800c6c34.get();
    MEMORY.ref(2, a1).offset(0xcL).setu(0);
    MEMORY.ref(2, a1).offset(0xaL).setu((MEMORY.ref(2, a1).offset(0xeL).getSigned() * 19 - 3) / 2);
    FUN_800f8b74(a2);
  }

  @Method(0x800f6330L)
  public static int FUN_800f6330() {
    long v0;
    long v1;
    long a0;
    long a1;
    long a2;
    long a3;
    long t0;
    long t1;
    long t2;
    long s0;
    long s1;
    long s2;
    long lo;
    v0 = 0x800c_0000L;
    v0 = MEMORY.ref(4, v0).offset(0x6c34L).get();
    v1 = MEMORY.ref(2, v0).offset(0x0L).getSigned();
    v0 = MEMORY.ref(2, v0).offset(0x0L).get();
    if(v1 != 0) {
      s1 = 0;
      s2 = 0x13L;
      v0 = v0 - 0x1L;
      v0 = v0 << 16;
      v1 = (int)v0 >> 16;
      s0 = 0x3L;

      switch((int)v1) {
        case 0 -> {
          v0 = 0x800c_0000L;
          a2 = 0x9L;
          a1 = MEMORY.ref(4, v0).offset(0x6c34L).get();
          v0 = 0x2L;
          v1 = MEMORY.ref(2, a1).offset(0x6L).get();
          a3 = a1 + 0x24L;
          MEMORY.ref(2, a1).offset(0x0L).setu(v0);
          v0 = MEMORY.ref(2, a1).offset(0xaL).get();
          a0 = MEMORY.ref(2, a1).offset(0x22L).getSigned();
          v1 = v1 - v0;
          v0 = a0 << 2;
          v0 = v0 + a0;
          v0 = v0 << 2;
          v0 = v0 - a0;
          v1 = v1 + v0;
          v0 = MEMORY.ref(2, a1).offset(0x8L).get();
          v1 = v1 - 0x4L;
          MEMORY.ref(2, a1).offset(0x28L).setu(v1);
          v0 = v0 - 0x16L;
          MEMORY.ref(2, a1).offset(0x2aL).setu(v0);

          //LAB_800f63e8
          do {
            MEMORY.ref(4, a3).offset(0x30L).setu(0);
            a2 = a2 - 0x1L;
            a3 = a3 - 0x4L;
          } while((int)a2 >= 0);
          v0 = 0x800c_0000L;
          a1 = v0 + 0x6c30L;
          MEMORY.ref(2, v0).offset(0x697cL).setu(0);
          MEMORY.ref(1, v0).offset(0x6ba1L).setu(0);
          MEMORY.ref(1, v0).offset(0x6ba0L).setu(0);

          //LAB_800f6424
          final long[] sp0x18 = new long[4];
          for(int i = 0; i < 4; i++) {
            sp0x18[i] = 0xffL;
            MEMORY.ref(1, a1).offset(i).setu(0);
          }
          v0 = 0x800c_0000L;
          t2 = v0 + 0x6718L;
          t0 = 0x800c_0000L;
          a3 = 0x18L;

          //LAB_800f6458
          for(a2 = 0; a2 < 4; a2++) {
            a0 = 0;
            v0 = a3 + t2;
            a1 = MEMORY.ref(1, v0).offset(0x0L).get();

            //LAB_800f646c
            for(int i = 0; i < 4; i++) {
              if(sp0x18[i] == a1) {
                a0 = 0x1L;
                break;
              }

              //LAB_800f6480
            }

            if(a0 == 0) {
              v1 = a3 + t2;
              v0 = MEMORY.ref(1, t0).offset(0x6ba0L).get();
              v1 = MEMORY.ref(1, v1).offset(0x0L).get();
              sp0x18[(int)v0] = v1;
              v0 = 0x800c_0000L;
              v1 = MEMORY.ref(1, t0).offset(0x6ba0L).get();
              v0 = v0 + 0x6c30L;
              v1 = v1 + v0;
              v0 = 0x800c_0000L;
              MEMORY.ref(1, v1).offset(0x0L).setu(a2);
              v1 = MEMORY.ref(4, v0).offset(0x66b0L).get();
              v0 = a2 & 0xffL;
              if(v1 == v0) {
                v0 = 0x800c_0000L;
                v1 = MEMORY.ref(1, t0).offset(0x6ba0L).get();
                MEMORY.ref(1, v0).offset(0x6ba1L).setu(v1);
              }

              //LAB_800f64dc
              v0 = MEMORY.ref(1, t0).offset(0x6ba0L).get();

              v0 = v0 + 0x1L;
              MEMORY.ref(1, t0).offset(0x6ba0L).setu(v0);
            }

            //LAB_800f64ec
            a3 = a3 + 0x4L;
          }
        }
        case 1 -> {
          v0 = 0x800c_0000L;
          v1 = 0x800c_0000L;
          a0 = MEMORY.ref(1, v1).offset(0x6ba0L).get();
          a3 = MEMORY.ref(4, v0).offset(0x6c34L).get();
          MEMORY.ref(4, a3).offset(0x40L).setu(0);
          MEMORY.ref(4, a3).offset(0x44L).setu(0);
          if(a0 >= 0x2L && (joypadInput_8007a39c.get() & 0x2L) != 0) {
            v1 = 0x800c_0000L;
            v0 = MEMORY.ref(1, v1).offset(0x6ba1L).get();

            v0 = v0 + 0x1L;
            MEMORY.ref(1, v1).offset(0x6ba1L).setu(v0);
            v0 = v0 & 0xffL;
            if(v0 < a0) {
              v0 = 0x8008_0000L;
            } else {
              v0 = 0x8008_0000L;
              MEMORY.ref(1, v1).offset(0x6ba1L).setu(0);
            }

            //LAB_800f6560
            v0 = MEMORY.ref(4, v0).offset(-0x5c48L).get();
            a1 = 0x3cL;
            lo = (int)a1 / (int)v0;
            a1 = lo;
            a0 = 0;
            a2 = 0x800c_0000L;
            v1 = MEMORY.ref(1, v1).offset(0x6ba1L).get();
            v0 = 0x800c_0000L;
            v0 = v0 + 0x6c30L;
            v1 = v1 + v0;
            v1 = MEMORY.ref(1, v1).offset(0x0L).get();
            v0 = 0x21L;
            MEMORY.ref(4, a2).offset(0x6748L).setu(v0);
            v0 = 0x5L;
            MEMORY.ref(2, a3).offset(0x0L).setu(v0);
            v0 = 0x800c_0000L;
            MEMORY.ref(4, v0).offset(0x66b0L).setu(v1);
            a1 = a1 + 0x2L;
            MEMORY.ref(4, a3).offset(0x44L).setu(a1);
            FUN_800f8c38(a0);
            break;
          }

          //LAB_800f65b8
          v0 = 0x8008_0000L;
          v1 = MEMORY.ref(4, v0).offset(-0x5c64L).get();
          v0 = v1 & 0x2000L;
          if(v0 != 0) {
            playSound(0, 1, 0, 0, (short)0, (short)0);
            v0 = 0x800c_0000L;
            a1 = MEMORY.ref(4, v0).offset(0x6c34L).get();

            v0 = MEMORY.ref(2, a1).offset(0xeL).getSigned();
            v1 = MEMORY.ref(2, a1).offset(0x22L).getSigned();
            v0 = v0 - 0x1L;
            if((int)v1 < (int)v0) {
              v0 = MEMORY.ref(2, a1).offset(0x22L).get();
              v0 = v0 + 0x1L;

              //LAB_800f6640
              MEMORY.ref(2, a1).offset(0x22L).setu(v0);
              v0 = 0x3L;
              MEMORY.ref(2, a1).offset(0x0L).setu(v0);

              //LAB_800f664c
              v0 = 0x800c_0000L;
              v0 = MEMORY.ref(4, v0).offset(0x6c34L).get();

              MEMORY.ref(4, v0).offset(0x30L).setu(s0);
              MEMORY.ref(4, v0).offset(0x34L).setu(s2);
              MEMORY.ref(4, v0).offset(0x38L).setu(0);
              MEMORY.ref(2, v0).offset(0x26L).setu(0);
              break;
            }
            v0 = 0x4L;
            MEMORY.ref(2, a1).offset(0x0L).setu(v0);
            v0 = MEMORY.ref(2, a1).offset(0x2L).get();
            v1 = MEMORY.ref(2, a1).offset(0x6L).getSigned();
            a0 = MEMORY.ref(2, a1).offset(0xaL).getSigned();
            MEMORY.ref(2, a1).offset(0x22L).setu(0);
            v0 = v0 | 0x1L;
            v1 = v1 - a0;
            v1 = v1 - 0x17L;
            MEMORY.ref(2, a1).offset(0x2L).setu(v0);
            MEMORY.ref(4, a1).offset(0x3cL).setu(v1);
            v0 = 0x800c_0000L;
            v0 = MEMORY.ref(4, v0).offset(0x6c34L).get();

            MEMORY.ref(4, v0).offset(0x30L).setu(s0);
            MEMORY.ref(4, v0).offset(0x34L).setu(s2);
            MEMORY.ref(4, v0).offset(0x38L).setu(0);
            MEMORY.ref(2, v0).offset(0x26L).setu(0);
            break;
          }

          //LAB_800f6664
          v0 = v1 & 0x8000L;
          if(v0 != 0) {
            playSound(0, 1, 0, 0, (short)0, (short)0);
            v0 = 0x800c_0000L;
            a1 = MEMORY.ref(4, v0).offset(0x6c34L).get();

            v0 = MEMORY.ref(2, a1).offset(0x22L).getSigned();
            v1 = MEMORY.ref(2, a1).offset(0x22L).get();
            if(v0 != 0) {
              v0 = v1 - 0x1L;
              //LAB_800f66f0
              MEMORY.ref(2, a1).offset(0x22L).setu(v0);
              v0 = 0x3L;
              MEMORY.ref(2, a1).offset(0x0L).setu(v0);

              //LAB_800f66fc
              v0 = 0x800c_0000L;
              v0 = MEMORY.ref(4, v0).offset(0x6c34L).get();
              v1 = -s2;
              MEMORY.ref(4, v0).offset(0x30L).setu(s0);
              MEMORY.ref(4, v0).offset(0x34L).setu(v1);

              //LAB_800f6710
              MEMORY.ref(4, v0).offset(0x38L).setu(0);
              MEMORY.ref(2, v0).offset(0x26L).setu(0);
              break;
            }
            v0 = v1 - 0x1L;
            v1 = MEMORY.ref(2, a1).offset(0x2L).get();
            v0 = 0x4L;
            MEMORY.ref(2, a1).offset(0x0L).setu(v0);
            v0 = MEMORY.ref(2, a1).offset(0xeL).get();
            v1 = v1 | 0x1L;
            MEMORY.ref(2, a1).offset(0x2L).setu(v1);
            v1 = MEMORY.ref(2, a1).offset(0x6L).getSigned();
            v0 = v0 - 0x1L;
            MEMORY.ref(2, a1).offset(0x22L).setu(v0);
            v0 = MEMORY.ref(2, a1).offset(0xaL).getSigned();
            a0 = MEMORY.ref(2, a1).offset(0xeL).getSigned();
            v1 = v1 - v0;
            v0 = a0 << 2;
            v0 = v0 + a0;
            v0 = v0 << 2;
            v0 = v0 - a0;
            v1 = v1 + v0;
            v1 = v1 - 0x4L;
            MEMORY.ref(4, a1).offset(0x3cL).setu(v1);
            v0 = 0x800c_0000L;
            v0 = MEMORY.ref(4, v0).offset(0x6c34L).get();
            v1 = -s2;
            MEMORY.ref(4, v0).offset(0x30L).setu(s0);
            MEMORY.ref(4, v0).offset(0x34L).setu(v1);
            MEMORY.ref(4, v0).offset(0x38L).setu(0);
            MEMORY.ref(2, v0).offset(0x26L).setu(0);
            break;
          }

          //LAB_800f671c
          v0 = 0x8008_0000L;
          v1 = MEMORY.ref(4, v0).offset(-0x5c68L).get();
          v0 = v1 & 0x20L;
          if(v0 == 0) {
            //LAB_800f6898
            v0 = v1 & 0x40L;
            if(v0 != 0) {
              //LAB_800f68a4
              //LAB_800f68bc
              playSound(0, 3, 0, 0, (short)0, (short)0);
            }
          } else {
            s0 = 0x800c_0000L;
            a0 = MEMORY.ref(4, s0).offset(0x6c34L).get();

            v0 = MEMORY.ref(2, a0).offset(0x22L).getSigned();

            v0 = v0 << 1;
            v0 = a0 + v0;
            v1 = MEMORY.ref(2, v0).offset(0x10L).get();

            v0 = v1 & 0x80L;
            if(v0 != 0) {
              v1 = v1 & 0xfL;
              playSound(0, 3, 0, 0, (short)0, (short)0);
            } else {
              v1 = v1 & 0xfL;
              v0 = 0x5L;
              if(v1 != v0) {
                v0 = 0x3L;

                //LAB_800f6790
                if(v1 != v0) {
                  //LAB_800f6858
                  a1 = 0x2L;

                  //LAB_800f6860
                  playSound(0, (int)a1, 0, 0, (short)0, (short)0);
                  v0 = MEMORY.ref(4, s0).offset(0x6c34L).get();
                  v0 = v0 + MEMORY.ref(2, v0).offset(0x22L).getSigned() * 0x2L;
                  s1 = MEMORY.ref(2, v0).offset(0x10L).get() & 0xfL;
                } else {
                  v0 = 0x800c_0000L;
                  v0 = MEMORY.ref(4, v0).offset(0x677cL).get();

                  if((int)v0 <= 0) {
                    a2 = 0;
                  } else {
                    a2 = 0;
                    a1 = MEMORY.ref(1, a0).offset(0x4L).get();
                    a0 = v0;
                    v0 = 0x800c_0000L;
                    v1 = v0 + 0x6960L;

                    //LAB_800f67b8
                    do {
                      v0 = MEMORY.ref(1, v1).offset(0x0L).get();

                      if(v0 == a1) {
                        break;
                      }

                      a2 = a2 + 0x1L;
                      v1 = v1 + 0x9L;
                    } while((int)a2 < (int)a0);
                  }

                  //LAB_800f67d8
                  a0 = 0x1L;
                  v0 = 0x800c_0000L;
                  a3 = v0 + 0x6960L;
                  v0 = a2 << 3;
                  v1 = v0 + a2;
                  a1 = 0xffL;

                  //LAB_800f67f4
                  do {
                    v0 = a0 + v1;
                    v0 = v0 + a3;
                    v0 = MEMORY.ref(1, v0).offset(0x0L).get();

                    if(v0 != a1) {
                      break;
                    }
                    a0 = a0 + 0x1L;
                  } while((int)a0 < 0x9L);

                  v0 = 0x9L;

                  //LAB_800f681c
                  if(a0 == v0) {
                    playSound(0, 3, 0, 0, (short)0, (short)0);
                  } else {
                    v0 = 0x800c_0000L;
                    v1 = MEMORY.ref(4, v0).offset(0x6c34L).get();
                    v0 = MEMORY.ref(2, v1).offset(0x22L).getSigned();
                    v0 = v0 << 1;
                    v1 = v1 + v0;
                    v0 = MEMORY.ref(2, v1).offset(0x10L).get();
                    s1 = v0 & 0xfL;
                    playSound(0, 2, 0, 0, (short)0, (short)0);
                  }
                }
              } else {
                FUN_800f83c8();
                v0 = 0x800c_0000L;
                v0 = MEMORY.ref(2, v0).offset(0x6b70L).getSigned();

                if(v0 == 0) {
                  playSound(0, 3, 0, 0, (short)0, (short)0);
                } else {
                  playSound(0, 2, 0, 0, (short)0, (short)0);
                  v0 = MEMORY.ref(4, s0).offset(0x6c34L).get();
                  v0 = v0 + MEMORY.ref(2, v0).offset(0x22L).getSigned() * 0x2L;
                  s1 = MEMORY.ref(2, v0).offset(0x10L).get() & 0xfL;
                }
              }
            }
          }

          //LAB_800f68c4
          v0 = 0x800c_0000L;

          //LAB_800f68c8
          v1 = MEMORY.ref(4, v0).offset(0x6c34L).get();
          v0 = 0x1L;
          MEMORY.ref(4, v1).offset(0x40L).setu(v0);
        }
        case 2 -> {
          v0 = 0x800c_0000L;
          a2 = MEMORY.ref(4, v0).offset(0x6c34L).get();
          a0 = MEMORY.ref(4, a2).offset(0x34L).get();
          v0 = MEMORY.ref(4, a2).offset(0x30L).get();
          lo = (int)a0 / (int)v0;
          a0 = lo;
          v1 = MEMORY.ref(4, a2).offset(0x38L).get();
          a1 = MEMORY.ref(4, a2).offset(0x30L).get();
          v0 = MEMORY.ref(2, a2).offset(0x28L).get();
          v1 = v1 + 0x1L;
          MEMORY.ref(4, a2).offset(0x38L).setu(v1);
          v0 = v0 + a0;
          MEMORY.ref(2, a2).offset(0x28L).setu(v0);
          if((int)v1 >= (int)a1) {
            v1 = MEMORY.ref(2, a2).offset(0x6L).get();
            v0 = 0x2L;
            MEMORY.ref(2, a2).offset(0x0L).setu(v0);
            v0 = MEMORY.ref(2, a2).offset(0xaL).get();
            a0 = MEMORY.ref(2, a2).offset(0x22L).getSigned();
            MEMORY.ref(4, a2).offset(0x38L).setu(0);
            MEMORY.ref(4, a2).offset(0x34L).setu(0);
            MEMORY.ref(4, a2).offset(0x30L).setu(0);
            v1 = v1 - v0;
            v0 = a0 << 2;
            v0 = v0 + a0;
            v0 = v0 << 2;
            v0 = v0 - a0;
            v1 = v1 + v0;
            v0 = MEMORY.ref(2, a2).offset(0x8L).get();
            v1 = v1 - 0x4L;
            MEMORY.ref(2, a2).offset(0x28L).setu(v1);
            v0 = v0 - 0x16L;
            MEMORY.ref(2, a2).offset(0x2aL).setu(v0);
          }
        }
        case 3 -> {
          v0 = 0x800c_0000L;
          t0 = MEMORY.ref(4, v0).offset(0x6c34L).get();
          a2 = MEMORY.ref(4, t0).offset(0x34L).get();
          v0 = MEMORY.ref(4, t0).offset(0x30L).get();
          lo = (int)a2 / (int)v0;
          a2 = lo;
          a1 = MEMORY.ref(4, t0).offset(0x34L).get();
          v0 = MEMORY.ref(4, t0).offset(0x30L).get();
          lo = (int)a1 / (int)v0;
          a1 = lo;
          a3 = MEMORY.ref(4, t0).offset(0x30L).get();
          t1 = 0x80L;
          lo = (int)t1 / (int)a3;
          a3 = lo;
          a0 = MEMORY.ref(4, t0).offset(0x38L).get();
          v1 = MEMORY.ref(4, t0).offset(0x3cL).get();
          v0 = MEMORY.ref(2, t0).offset(0x28L).get();
          a0 = a0 + 0x1L;
          MEMORY.ref(4, t0).offset(0x38L).setu(a0);
          v0 = v0 + a2;
          MEMORY.ref(2, t0).offset(0x28L).setu(v0);
          v0 = MEMORY.ref(2, t0).offset(0x2cL).get();
          v1 = v1 + a1;
          MEMORY.ref(4, t0).offset(0x3cL).setu(v1);
          v1 = MEMORY.ref(4, t0).offset(0x30L).get();
          v0 = v0 - a3;
          MEMORY.ref(2, t0).offset(0x2cL).setu(v0);
          if((int)a0 >= (int)v1) {
            v1 = MEMORY.ref(2, t0).offset(0x6L).get();
            v0 = 0x2L;
            MEMORY.ref(2, t0).offset(0x0L).setu(v0);
            v0 = MEMORY.ref(2, t0).offset(0xaL).get();
            a0 = MEMORY.ref(2, t0).offset(0x22L).getSigned();
            MEMORY.ref(2, t0).offset(0x2cL).setu(t1);
            MEMORY.ref(4, t0).offset(0x38L).setu(0);
            MEMORY.ref(4, t0).offset(0x34L).setu(0);
            MEMORY.ref(4, t0).offset(0x30L).setu(0);
            v1 = v1 - v0;
            v0 = a0 << 2;
            v0 = v0 + a0;
            v0 = v0 << 2;
            v0 = v0 - a0;
            v1 = v1 + v0;
            v0 = MEMORY.ref(2, t0).offset(0x8L).get();
            v1 = v1 - 0x4L;
            MEMORY.ref(2, t0).offset(0x28L).setu(v1);
            v1 = MEMORY.ref(2, t0).offset(0x2L).get();
            v0 = v0 - 0x16L;
            v1 = v1 & 0xfffeL;
            MEMORY.ref(2, t0).offset(0x2aL).setu(v0);
            MEMORY.ref(2, t0).offset(0x2L).setu(v1);
          }
        }
        case 4 -> {
          v0 = _800c6c34.get();
          MEMORY.ref(4, v0).offset(0x44L).subu(0x1L);
          if(MEMORY.ref(4, v0).offset(0x44L).get() == 0x1L) {
            FUN_800f8c38(0x1L);
            v1 = _800c6c34.get();
            MEMORY.ref(2, v1).offset(0x0L).setu(0x2L);
          }
        }
      }

      //LAB_800f6a88
      //LAB_800f6a8c
      v1 = _800c6c34.get();
      MEMORY.ref(2, v1).offset(0x24L).addu(0x1L);
      if(MEMORY.ref(2, v1).offset(0x24L).getSigned() >= 0x4L) {
        MEMORY.ref(2, v1).offset(0x24L).setu(0);
        MEMORY.ref(2, v1).offset(0x26L).addu(0x1L);
        if(MEMORY.ref(2, v1).offset(0x26L).getSigned() >= 0x4L) {
          MEMORY.ref(2, v1).offset(0x26L).setu(0);
        }
      }

      //LAB_800f6ae0
      FUN_800f6b04();
      v0 = s1;
    }

    //LAB_800f6aec
    return (int)v0;
  }

  @Method(0x800f6b04L)
  public static void FUN_800f6b04() {
    long v0;
    long v1;
    long a0;
    long a1;
    long a2;
    long a3;
    long t0;
    long t1;
    long t2;
    long t7;
    long s0;
    long s1;
    long s2;
    long s3;
    long s4;
    long s5;
    long s6;
    long s7;
    long fp;
    a1 = 0x800c_0000L;
    v1 = MEMORY.ref(4, a1).offset(0x6c34L).get();
    v0 = 0x800c_0000L;
    t7 = v0 + 0x71bcL;
    final long[] sp0x30 = new long[10];
    for(int i = 0; i < 10; i++) {
      sp0x30[i] = MEMORY.ref(2, t7).offset(i * 0x2L).get();
    }

    t7 = v0 + 0x71d0L;
    final long[] sp0x48 = new long[10];
    for(int i = 0; i < 10; i++) {
      sp0x48[i] = MEMORY.ref(2, t7).offset(i * 0x2L).get();
    }

    t7 = v0 + 0x71e4L;
    final long[] sp0x60 = new long[4];
    for(int i = 0; i < 4; i++) {
      sp0x60[i] = MEMORY.ref(2, t7).offset(i * 0x2L).get();
    }

    if(MEMORY.ref(2, v1).offset(0x0L).getSigned() != 0) {
      if((MEMORY.ref(2, v1).offset(0x2L).get() & 0x2L) != 0) {
        //LAB_800f6c48
        for(s5 = 0; s5 < _800c6c34.deref(2).offset(0xeL).getSigned(); s5++) {
          a1 = _800c6c34.get();
          fp = (MEMORY.ref(2, a1).offset(0x10L).offset(s5 * 0x2L).get() & 0xfL) - 0x1L;
          if(MEMORY.ref(2, a1).offset(0x22L).getSigned() == s5) {
            s6 = sp0x60[(int)MEMORY.ref(2, a1).offset(0x26L).getSigned()];
          } else {
            //LAB_800f6c88
            s6 = 0;
          }

          //LAB_800f6c90
          a2 = _800c6c34.get();
          a0 = MEMORY.ref(2, a2).offset(0x6L).get() - MEMORY.ref(2, a2).offset(0xaL).get() + s5 * 19;
          s3 = a0 - centreScreenX_1f8003dc.get();
          s2 = s6;
          s0 = fp;
          v0 = _800fb6bc.offset(s0 * 6 + s2 * 2).getAddress();
          a2 = a2 + s5 * 0x2L;
          s4 = MEMORY.ref(2, a2).offset(0x8L).get() - MEMORY.ref(2, v0).offset(0x0L).get() - centreScreenY_1f8003de.get();
          if((MEMORY.ref(2, a2).offset(0x10L).get() & 0x80L) != 0) {
            FUN_800f8cd8(s3, MEMORY.ref(2, a2).offset(0x8L).get() - (centreScreenY_1f8003de.get() + 0x10L), 0x60L, 0x70L, 0x10L, 0x10L, 0x19L, -0x1L);
          }

          //LAB_800f6d70
          a0 = _800c6c34.get();
          if((MEMORY.ref(2, a0).offset(s5 * 0x2L).offset(0x10L).get() & 0xfL) != 0x2L) {
            //LAB_800f6e24
            s0 = _800fb674.offset(s0 * 0x8L).offset(2, 0x4L).get();
          } else if(MEMORY.ref(2, a0).offset(0x4L).getSigned() == 0 && (gameState_800babc8.dragoonSpirits_19c.get(0).get() & 0xffL) >>> 7 != 0) {
            s0 = sp0x48[9];
            if(s2 != 0) {
              //LAB_800f6de0
              FUN_800f8cd8(s3 + 0x4L, s4, s2 != 0x1L ? 0x58L : 0x50L, 0x70L, 0x8L, 0x10L, 0x98L, 0x1L);
            }
          } else {
            s0 = sp0x48[(int)MEMORY.ref(2, a0).offset(0x4L).getSigned()];
          }

          //LAB_800f6e34
          //LAB_800f6e38
          //LAB_800f6e3c
          t1 = _800fb674.offset(fp * 8).getAddress();
          v1 = s6 * 0x2L + fp * 0x6L;
          t0 = _800fb6f4.offset(v1).getAddress();
          v1 = _800fb6bc.offset(v1).getAddress();
          FUN_800f8cd8(s3, s4, MEMORY.ref(1, t1).offset(0x0L).get(), MEMORY.ref(1, t1).offset(0x2L).get() + MEMORY.ref(1, t0).offset(0x0L).get() & 0xffL, 0x10L, MEMORY.ref(2, v1).getSigned(), s0, MEMORY.ref(2, t1).offset(0x6L).getSigned());

          a1 = _800c6c34.get();
          if(MEMORY.ref(2, a1).offset(0x22L).getSigned() == s5 && MEMORY.ref(4, a1).offset(0x40L).get() == 0x1L) {
            t1 = _800fb72c.offset(fp * 8).getAddress();
            a0 = MEMORY.ref(2, a1).offset(0x6L).get() - MEMORY.ref(2, a1).offset(0xaL).get() + s5 * 11 - centreScreenX_1f8003dc.get() - MEMORY.ref(2, t1).offset(0x4L).get() / 2 + 0x8L;
            a1 = MEMORY.ref(2, a1).offset(0x8L).get() - centreScreenY_1f8003de.get() - 0x18L;
            FUN_800f8cd8(a0, a1, MEMORY.ref(1, t1).offset(0x0L).get(), MEMORY.ref(1, t1).offset(0x2L).get(), MEMORY.ref(2, t1).offset(0x4L).get(), 0x8L, MEMORY.ref(2, t1).offset(0x6L).getSigned(), -0x1L);
          }

          //LAB_800f6fa4
        }

        //LAB_800f6fc8
        v0 = _800c6c34.get();
        FUN_800f7210(MEMORY.ref(2, v0).offset(0x28L).getSigned(), MEMORY.ref(2, v0).offset(0x2aL).getSigned(), sp0x30, 0x1fL, 0xcL, 0x1L, MEMORY.ref(2, v0).offset(0x2cL).getSigned());

        if((MEMORY.ref(2, v0).offset(0x2L).get() & 0x1L) != 0) {
          FUN_800f7210(MEMORY.ref(2, v0).offset(0x3cL).getSigned(), MEMORY.ref(2, v0).offset(0x2aL).getSigned(), sp0x30, 0x1fL, 0xcL, 0x1L, 0x80L - MEMORY.ref(2, v0).offset(0x2cL).get());
        }

        //LAB_800f704c
        s0 = MEMORY.ref(2, v0).offset(0xeL).getSigned() * 0x13 + 1;
        s1 = MEMORY.ref(2, v0).offset(0x6L).get() - s0 / 2;
        s2 = MEMORY.ref(2, v0).offset(0x8L).get() - 0xaL;
        FUN_800f74f4(_800fb5dc.getAddress(), s1, s2, s0, 0x2L, 0x2bL, 0x1L, _800fb5dc.offset(1, 0x4L).get());

        final long[] sp0x20 = new long[4];
        final long[] sp0x28 = new long[4];

        sp0x20[0] = s1;
        sp0x20[2] = s1;
        s1 = s1 + s0;
        s3 = MEMORY.ref(2, v0).offset(0x8L).get() - 0x8L;
        sp0x20[1] = s1;
        sp0x20[3] = s1;
        sp0x28[0] = s2;
        sp0x28[1] = s2;
        sp0x28[2] = s3;
        sp0x28[3] = s3;

        //LAB_800f710c
        s7 = _800fb5dc.getAddress();
        fp = _800fb5dc.getAddress() + 0x6L;
        s6 = _800fb614.getAddress();
        for(s5 = 0; s5 < 8; s5++) {
          t0 = s6 + s5 * 0xcL;
          a2 = MEMORY.ref(2, t0).offset(0x6L).getSigned();
          a1 = sp0x20[(int)MEMORY.ref(2, t0).getSigned()] + MEMORY.ref(2, t0).offset(0x2L).get();
          t2 = sp0x28[(int)MEMORY.ref(2, t0).getSigned()] + MEMORY.ref(2, t0).offset(0x4L).get();
          if(a2 != 0) {
            a3 = a2;
          } else {
            a3 = s0;
          }

          //LAB_800f7158
          if(MEMORY.ref(2, t0).offset(0x8L).getSigned() == 0) {
            v1 = 0x2L;
          } else {
            v1 = MEMORY.ref(2, t0).offset(0x8L).get();
          }

          //LAB_800f716c
          FUN_800f74f4(fp + s5 * 0x6L, (short)a1, (short)t2, (short)a3, (short)v1, 0x2bL, 0x1L, MEMORY.ref(1, s7).offset((s5 + 0x1L) * 0x6L).offset(0x4L).get());
        }
      }
    }

    //LAB_800f71e0
  }

  @Method(0x800f7210L)
  public static void FUN_800f7210(long a0, long a1, long[] a2, long a3, long a4, long a5, long a6) {
    long v0;
    long v1;
    final long s0 = linkedListAddress_1f8003d8.get();
    linkedListAddress_1f8003d8.addu(0x28L);
    FUN_8003b590(s0);
    final long s5;
    if((int)a5 == -0x1L) {
      gpuLinkedListSetCommandTransparency(s0, false);
      s5 = 0;
    } else {
      //LAB_800f728c
      gpuLinkedListSetCommandTransparency(s0, true);
      s5 = a5;
    }

    //LAB_800f7294
    MEMORY.ref(1, s0).offset(0x6L).setu(a6);
    MEMORY.ref(1, s0).offset(0x5L).setu(a6);
    MEMORY.ref(1, s0).offset(0x4L).setu(a6);
    v1 = a2[0] + a0 - centreScreenX_1f8003dc.get();
    MEMORY.ref(2, s0).offset(0x18L).setu(v1);
    MEMORY.ref(2, s0).offset(0x8L).setu(v1);
    v1 = a2[2] + a2[0] + a0 - centreScreenX_1f8003dc.get();
    MEMORY.ref(2, s0).offset(0x20L).setu(v1);
    MEMORY.ref(2, s0).offset(0x10L).setu(v1);
    v1 = a2[1] + a1 - centreScreenY_1f8003de.get();
    MEMORY.ref(2, s0).offset(0x12L).setu(v1);
    MEMORY.ref(2, s0).offset(0xaL).setu(v1);
    v1 = a2[3] + a2[1] + a1 - centreScreenY_1f8003de.get();
    MEMORY.ref(2, s0).offset(0x22L).setu(v1);
    MEMORY.ref(2, s0).offset(0x1aL).setu(v1);
    v1 = (short)a2[8];
    if(v1 == 0) {
      //LAB_800f7360
      v0 = a2[4];
      MEMORY.ref(1, s0).offset(0x1cL).setu(v0);
      MEMORY.ref(1, s0).offset(0xcL).setu(v0);
      v0 = a2[4] + a2[6];
      MEMORY.ref(1, s0).offset(0x24L).setu(v0);
      MEMORY.ref(1, s0).offset(0x14L).setu(v0);

      //LAB_800f73e0
      v0 = a2[5];
      MEMORY.ref(1, s0).offset(0x15L).setu(v0);
      MEMORY.ref(1, s0).offset(0xdL).setu(v0);
      v0 = a2[5] + a2[7];
      MEMORY.ref(1, s0).offset(0x25L).setu(v0);
      MEMORY.ref(1, s0).offset(0x1dL).setu(v0);
    } else if(v1 == 0x1L) {
      //LAB_800f738c
      v0 = a2[4];
      MEMORY.ref(1, s0).offset(0x1cL).setu(v0);
      MEMORY.ref(1, s0).offset(0xcL).setu(v0);
      v0 = a2[4] + a2[6];
      MEMORY.ref(1, s0).offset(0x24L).setu(v0);
      MEMORY.ref(1, s0).offset(0x14L).setu(v0);
      v0 = a2[5] - 0x1L;
      MEMORY.ref(1, s0).offset(0x25L).setu(v0);
      MEMORY.ref(1, s0).offset(0x1dL).setu(v0);
      v0 = v0 + a2[7];
      MEMORY.ref(1, s0).offset(0x15L).setu(v0);
      MEMORY.ref(1, s0).offset(0xdL).setu(v0);
      //LAB_800f7344
    } else if(v1 == 0x2L) {
      //LAB_800f73b8
      v0 = a2[4] - 0x1L;
      MEMORY.ref(1, s0).offset(0x24L).setu(v0);
      MEMORY.ref(1, s0).offset(0x14L).setu(v0);
      v0 = v0 + a2[6];
      MEMORY.ref(1, s0).offset(0x1cL).setu(v0);
      MEMORY.ref(1, s0).offset(0xcL).setu(v0);

      //LAB_800f73e0
      v0 = a2[5];
      MEMORY.ref(1, s0).offset(0x15L).setu(v0);
      MEMORY.ref(1, s0).offset(0xdL).setu(v0);
      v0 = a2[5] + a2[7];
      MEMORY.ref(1, s0).offset(0x25L).setu(v0);
      MEMORY.ref(1, s0).offset(0x1dL).setu(v0);
    } else if(v1 == 0x3L) {
      //LAB_800f740c
      v0 = a2[4] - 0x1L;
      MEMORY.ref(1, s0).offset(0x24L).setu(v0);
      MEMORY.ref(1, s0).offset(0x14L).setu(v0);
      v0 = v0 + a2[6];
      MEMORY.ref(1, s0).offset(0x1cL).setu(v0);
      MEMORY.ref(1, s0).offset(0xcL).setu(v0);

      //LAB_800f7434
      v0 = a2[5] - 0x1L;
      MEMORY.ref(1, s0).offset(0x25L).setu(v0);
      MEMORY.ref(1, s0).offset(0x1dL).setu(v0);
      v0 = v0 + a2[7];
      MEMORY.ref(1, s0).offset(0x15L).setu(v0);
      MEMORY.ref(1, s0).offset(0xdL).setu(v0);
    }

    //LAB_800f745c
    //LAB_800f7460
    if((int)a4 >= 0) {
      v0 = a4;
    } else {
      v0 = a4 + 0xfL;
    }

    //LAB_800f746c
    v0 = (int)v0 >> 4;
    v0 = v0 << 4;
    v1 = a4 - v0 + 0x1f0L;
    v1 = v1 << 6;
    v0 = v0 + 0x2c0L;
    v0 = v0 & 0x3f0L;
    v0 = (int)v0 >> 4;
    v1 = v1 | v0;
    MEMORY.ref(2, s0).offset(0xeL).setu(v1);
    MEMORY.ref(2, s0).offset(0x16L).setu(GetTPage(0, s5, 0x2c0L, 0x100L));
    insertElementIntoLinkedList(tags_1f8003d0.getPointer() + a3 * 0x4L, s0);
  }

  @Method(0x800f74f4L)
  public static void FUN_800f74f4(long a0, long a1, long a2, long a3, long a4, long a5, long a6, long a7) {
    final long s0 = linkedListAddress_1f8003d8.get();
    linkedListAddress_1f8003d8.addu(0x28L);
    FUN_8003b590(s0);

    final long s5;
    if((int)a6 == -0x1L) {
      gpuLinkedListSetCommandTransparency(s0, false);
      s5 = 0;
    } else {
      //LAB_800f7578
      gpuLinkedListSetCommandTransparency(s0, true);
      s5 = a6;
    }

    //LAB_800f7580
    MEMORY.ref(1, s0).offset(0x4L).setu(0x80L);
    MEMORY.ref(1, s0).offset(0x5L).setu(0x80L);
    MEMORY.ref(1, s0).offset(0x6L).setu(0x80L);
    FUN_800f8fac(s0, a1 - centreScreenX_1f8003dc.get(), a2 - centreScreenY_1f8003de.get(), 0, 0, a3, a4, 0);

    final long v1 = (short)a7;
    long v0;
    if(v1 == 0) {
      //LAB_800f7628
      v0 = MEMORY.ref(1, a0).offset(0x0L).get();
      MEMORY.ref(1, s0).offset(0x1cL).setu(v0);
      MEMORY.ref(1, s0).offset(0xcL).setu(v0);
      v0 = MEMORY.ref(1, a0).offset(0x0L).get() + MEMORY.ref(1, a0).offset(0x2L).get();
      MEMORY.ref(1, s0).offset(0x24L).setu(v0);
      MEMORY.ref(1, s0).offset(0x14L).setu(v0);
      v0 = MEMORY.ref(1, a0).offset(0x1L).get();
      MEMORY.ref(1, s0).offset(0x15L).setu(v0);
      MEMORY.ref(1, s0).offset(0xdL).setu(v0);
      v0 = MEMORY.ref(1, a0).offset(0x1L).get() + MEMORY.ref(1, a0).offset(0x3L).get();
      MEMORY.ref(1, s0).offset(0x25L).setu(v0);
      MEMORY.ref(1, s0).offset(0x1dL).setu(v0);
    } else if(v1 == 0x1L) {
      //LAB_800f7654
      v0 = MEMORY.ref(1, a0).offset(0x0L).get();
      MEMORY.ref(1, s0).offset(0x1cL).setu(v0);
      MEMORY.ref(1, s0).offset(0xcL).setu(v0);
      v0 = MEMORY.ref(1, a0).offset(0x0L).get() + MEMORY.ref(1, a0).offset(0x2L).get();
      MEMORY.ref(1, s0).offset(0x24L).setu(v0);
      MEMORY.ref(1, s0).offset(0x14L).setu(v0);
      v0 = MEMORY.ref(1, a0).offset(0x1L).get() - 0x1L;
      MEMORY.ref(1, s0).offset(0x25L).setu(v0);
      MEMORY.ref(1, s0).offset(0x1dL).setu(v0);
      v0 = v0 + MEMORY.ref(1, a0).offset(0x3L).get();
      MEMORY.ref(1, s0).offset(0x15L).setu(v0);
      MEMORY.ref(1, s0).offset(0xdL).setu(v0);
      //LAB_800f7610
    } else if(v1 == 0x2L) {
      //LAB_800f7680
      v0 = MEMORY.ref(1, a0).offset(0x0L).get() - 0x1L;
      MEMORY.ref(1, s0).offset(0x24L).setu(v0);
      MEMORY.ref(1, s0).offset(0x14L).setu(v0);
      v0 = v0 + MEMORY.ref(1, a0).offset(0x2L).get();
      MEMORY.ref(1, s0).offset(0x1cL).setu(v0);
      MEMORY.ref(1, s0).offset(0xcL).setu(v0);

      //LAB_800f76a8
      v0 = MEMORY.ref(1, a0).offset(0x1L).get();
      MEMORY.ref(1, s0).offset(0x15L).setu(v0);
      MEMORY.ref(1, s0).offset(0xdL).setu(v0);
      v0 = MEMORY.ref(1, a0).offset(0x1L).get() + MEMORY.ref(1, a0).offset(0x3L).get();
      MEMORY.ref(1, s0).offset(0x25L).setu(v0);
      MEMORY.ref(1, s0).offset(0x1dL).setu(v0);
    } else if(v1 == 0x3L) {
      //LAB_800f76d4
      v0 = MEMORY.ref(1, a0).offset(0x0L).get() - 0x1L;
      MEMORY.ref(1, s0).offset(0x24L).setu(v0);
      MEMORY.ref(1, s0).offset(0x14L).setu(v0);
      v0 = v0 + MEMORY.ref(1, a0).offset(0x2L).get();
      MEMORY.ref(1, s0).offset(0x1cL).setu(v0);
      MEMORY.ref(1, s0).offset(0xcL).setu(v0);

      //LAB_800f76fc
      v0 = MEMORY.ref(1, a0).offset(0x1L).get() - 0x1L;
      MEMORY.ref(1, s0).offset(0x25L).setu(v0);
      MEMORY.ref(1, s0).offset(0x1dL).setu(v0);
      v0 = v0 + MEMORY.ref(1, a0).offset(0x3L).get();
      MEMORY.ref(1, s0).offset(0x15L).setu(v0);
      MEMORY.ref(1, s0).offset(0xdL).setu(v0);
    }

    //LAB_800f7724
    //LAB_800f772c
    FUN_800f9024(s0, (short)a5, s5);
  }

  @Method(0x800f7768L)
  public static long FUN_800f7768(final long a0, final long a1) {
    long v1;
    long t1;
    long t4 = 0;
    long t6 = 0;
    long t3 = 0x1L;

    if(a0 == 0x1L) {
      _800c6c34.deref(4).offset(0x4cL).setu(0x1L);
      //LAB_800f77d4
      t1 = _800c6758.get();

      //LAB_800f77e8
      _800c697c.setu(_800c697e.get());
    } else {
      _800c6c34.deref(4).offset(0x4cL).setu(0x1L);
      if((int)a0 < 0x2L && a0 == 0) {
        _800c697c.setu(_800c6980.get());
        t1 = _800c677c.get();
      } else {
        //LAB_800f77f0
        t1 = _800c669c.get();
      }
    }

    //LAB_800f77f4
    if((joypadPress_8007a398.get() & 0x3000L) != 0) {
      _800c697c.addu(0x1L);
      if(_800c697c.getSigned() >= t1) {
        _800c697c.setu(0);
      }
      t3 = 0x1L;
    }

    //LAB_800f7830
    if((joypadPress_8007a398.get() & 0xc000L) != 0) {
      _800c697c.subu(0x1L);
      if(_800c697c.getSigned() < 0) {
        _800c697c.setu(t1 - 0x1L);
      }
      t3 = -0x1L;
    }

    //LAB_800f786c
    //LAB_800f7880
    if(_800c697c.getSigned() < 0 || _800c697c.getSigned() >= (short)t1) {
      //LAB_800f78a0
      _800c697c.setu(0);
      t3 = 0x1L;
    }

    //LAB_800f78ac
    //LAB_800f78d4
    for(v1 = 0; v1 < t1; v1++) {
      t4 = _800c71f0.offset(a0 * 0x4L).deref(4).offset(_800c697c.getSigned() * 0x4L).get();

      if((scriptStatePtrArr_800bc1c0.get((int)t4).deref().ui_60.get() & 0x4000L) == 0) {
        break;
      }

      _800c697c.addu(t3);

      if(_800c697c.get() >= t1) {
        _800c697c.setu(0);
      }

      //LAB_800f792c
      if(_800c697c.getSigned() < 0) {
        _800c697c.setu(t1 - 0x1L);
      }

      //LAB_800f7948
    }

    //LAB_800f7960
    if(v1 == t1) {
      t4 = _800c71f0.offset(a0 * 0x4L).deref(4).offset(_800c697c.getSigned() * 0x4L).get();
      _800c697c.setu(0);
    }

    //LAB_800f7998
    v1 = _800c6c34.get();
    MEMORY.ref(4, v1).offset(0x50L).setu(a0);
    if(a1 == 0) {
      MEMORY.ref(4, v1).offset(0x54L).setu(_800c697c.getSigned());
    } else {
      //LAB_800f79b4
      MEMORY.ref(4, v1).offset(0x54L).setu(-0x1L);
    }

    //LAB_800f79bc
    _800c6c34.deref(4).offset(0x48L).setu(t4);

    if(a0 == 0x1L) {
      //LAB_800f79fc
      _800c697e.setu(_800c697c.get());
    } else if(a0 == 0) {
      _800c6980.setu(_800c697c.get());
    }

    //LAB_800f7a0c
    //LAB_800f7a10
    if((joypadPress_8007a398.get() & 0x20L) != 0) {
      t6 = 0x1L;
      v1 = _800c6c34.get();
      _800c697c.setu(0);
      MEMORY.ref(4, v1).offset(0x4cL).setu(0);
    }

    //LAB_800f7a38
    if((joypadPress_8007a398.get() & 0x40L) != 0) {
      t6 = -0x1L;
      _800c697c.setu(0);
      v1 = _800c6c34.get();
      MEMORY.ref(4, v1).offset(0x48L).setu(t6);
      MEMORY.ref(4, v1).offset(0x4cL).setu(0);
    }

    //LAB_800f7a68
    return t6;
  }

  @Method(0x800f7a74L)
  public static void FUN_800f7a74(long a0) {
    assert false;
  }

  @Method(0x800f7b68L)
  public static void FUN_800f7b68(long a0) {
    long a1 = scriptStatePtrArr_800bc1c0.get((int)a0).deref().innerStruct_00.getPointer(); //TODO
    long v1 = a1 + 0x90L;
    a0 = 0x48L;

    //LAB_800f7b8c
    do {
      MEMORY.ref(2, v1).offset(0x4L).setu(0);
      a0 = a0 + 0x1L;
      v1 = v1 + 0x2L;
    } while((int)a0 < 0x54L);

    a0 = MEMORY.ref(2, a1).offset(0x4eL).getSigned();
    if(a0 != -0x1L) {
      v1 = 0x8010_0000L;
      v1 = v1 - 0x5f48L;
      long v0 = a0 << 1;
      v0 = v0 + a0;
      v0 = v0 << 2;
      v0 = v0 + v1;
      v1 = MEMORY.ref(1, v0).offset(0x0L).get();
      MEMORY.ref(2, a1).offset(0x94L).setu(v1);
      v1 = MEMORY.ref(1, v0).offset(0x1L).get();
      MEMORY.ref(2, a1).offset(0x96L).setu(v1);
      v1 = MEMORY.ref(1, v0).offset(0x2L).get();
      MEMORY.ref(2, a1).offset(0x98L).setu(v1);
      v1 = MEMORY.ref(1, v0).offset(0x3L).get();
      MEMORY.ref(2, a1).offset(0x9aL).setu(v1);
      v1 = MEMORY.ref(1, v0).offset(0x4L).get();
      MEMORY.ref(2, a1).offset(0x9cL).setu(v1);
      v1 = MEMORY.ref(1, v0).offset(0x5L).get();
      MEMORY.ref(2, a1).offset(0x9eL).setu(v1);
      v1 = MEMORY.ref(1, v0).offset(0x6L).get();
      MEMORY.ref(2, a1).offset(0xa0L).setu(v1);
      v1 = MEMORY.ref(1, v0).offset(0x8L).get();
      MEMORY.ref(2, a1).offset(0xa4L).setu(v1);
      v1 = MEMORY.ref(1, v0).offset(0x9L).get();
      MEMORY.ref(2, a1).offset(0xa6L).setu(v1);
      v1 = MEMORY.ref(1, v0).offset(0xaL).get();
      MEMORY.ref(2, a1).offset(0xa8L).setu(v1);
      v1 = MEMORY.ref(1, v0).offset(0x7L).get();
      MEMORY.ref(2, a1).offset(0xa2L).setu(v1);
      v0 = MEMORY.ref(1, v0).offset(0xbL).get();
      MEMORY.ref(2, a1).offset(0xaaL).setu(v0);
    }

    //LAB_800f7c54
  }

  @Method(0x800f7c5cL)
  public static long FUN_800f7c5c(final long a0, final long a1, final long a2) {
    final ScriptState<BtldScriptData27c> a2_0 = scriptStatePtrArr_800bc1c0.get((int)a0).derefAs(ScriptState.classFor(BtldScriptData27c.class));
    final BtldScriptData27c s1 = a2_0.innerStruct_00.deref();
    long fp = a2_0.ui_60.get() & 0x4L;
    long v1 = (fp != 0 ? 0xcL : 0) + a2 * 0x4L;
    long s2 = s1._04.get((int)_800c7284.offset(v1).get()).get();
    final ScriptState<BtldScriptData27c> a2_1 = scriptStatePtrArr_800bc1c0.get((int)a1).derefAs(ScriptState.classFor(BtldScriptData27c.class));
    long spa8 = a2_1.ui_60.get() & 0x4L;
    final BtldScriptData27c s4 = a2_1.innerStruct_00.deref();
    long s3 = _800c726c.offset(v1).get();
    long s6 = _800c729c.offset(v1).get();
    long s7 = _800c72b4.offset(v1).get();
    long s0 = -0x1L;
    if(a2 == 0x2L) {
      s2 = 0x65L;
    }

    //LAB_800f7e98
    if(FUN_800133ac() * 0x65 / 0x100 < (int)s2) {
      final long a1_0 = s1._04.get((int)s3).get();

      if((a1_0 & 0xffL) != 0) {
        //LAB_800f7eec
        long v1_0;
        for(v1_0 = 0; v1_0 < 8; v1_0++) {
          if((a1_0 & (0x80L >> v1_0)) != 0) {
            break;
          }
        }

        //LAB_800f7f0c
        s0 = _800c724c.offset(v1_0 * 0x4L).get();
      }

      //LAB_800f7f14
      long v1_0 = s1._04.get((int)s6).get() & s7;
      if(v1_0 != 0) {
        if(fp != 0 || a2 != 0) {
          //LAB_800f7f40
          if(a2 != 0x2L) {
            //LAB_800f7f68
            if(v1_0 == 0x10L) {
              //LAB_800f7f7c
              long v0;
              if(spa8 == 0) {
                v0 = s1._a4.get() & _800c706c.offset(s4._272.get() * 0x2L).get();
              } else {
                //LAB_800f7fac
                v0 = s1._a4.get() & s4._1c.get();
              }

              //LAB_800f7fbc
              if(v0 != 0) {
                //LAB_800f7fc4
                s0 = 0;
              }
            } else if(v1_0 == 0x80L) {
              s0 = 0;
            }
          } else {
            s0 = 0;
          }
        } else {
          s0 = 0;
        }

        //LAB_800f7fc8
        if((s4._14.get() & 0x80L) != 0) {
          s0 = -0x1L;
        }
      }
    }

    //LAB_800f7fe0
    //LAB_800f7fe4
    return s0;
  }

  @Method(0x800f83c8L)
  public static void FUN_800f83c8() {
    //LAB_800f83dc
    for(int i = 0; i < 0x40; i++) {
      _800c6988.offset(i).setu(0xffL);
    }

    _800c6b70.setu(0);

    //LAB_800f8420
    for(int a3 = 0; a3 < gameState_800babc8._1e6.get(); a3++) {
      //LAB_800f843c
      for(int a2 = 0; a2 < gameState_800babc8._1e6.get(); a2++) {
        final long a0 = gameState_800babc8._2e9.get(a3).get();
        final long v0 = _800c6988.offset(a2 * 0x2L).get();

        if(v0 == a0) {
          _800c6988.offset(a2 * 0x2L).offset(0x1L).addu(0x1L);
          break;
        }

        //LAB_800f8468
        if(v0 == 0xffL) {
          _800c6988.offset(a2 * 0x2L).setu(a0);
          _800c6988.offset(a2 * 0x2L).offset(0x1L).setu(0x1L);
          _800c6b70.addu(0x1L);
          break;
        }

        //LAB_800f848c
      }

      //LAB_800f84a4
      //LAB_800f84a8
    }

    //LAB_800f84b8
  }

  @Method(0x800f84c0L)
  public static void FUN_800f84c0() {
    // empty
  }

  @Method(0x800f84c8L)
  public static void FUN_800f84c8() {
    loadDrgnBinFile(0, 4113, 0, getMethodAddress(Bttl_800e.class, "FUN_800ee8c4", long.class, long.class, long.class), 0, 0x4L);
  }

  @Method(0x800f8568L)
  public static LodString FUN_800f8568(final BtldScriptData27c a0, final LodString a1) {
    if(a0._272.get() != 0x185L) {
      return a1;
    }

    final long v1 = _8006f284.get();
    if(v1 != 0x4L) {
      if((int)v1 < 0x5L) {
        if(v1 != 0) {
          return a1;
        }
        //LAB_800f85ec
      } else if(v1 != 0x6L) {
        return a1;
      }
    }

    //LAB_800f85f4
    //LAB_800f8634
    return _800c6ba8.get((int)_800c6f30.offset(_8006f284.get() * 0x4L).get());
  }

  @Method(0x800f863cL)
  public static void FUN_800f863c() {
    FUN_80012b1c(0x2L, getMethodAddress(Bttl_800e.class, "FUN_800ef28c", long.class), 0x1L);
  }

  @Method(0x800f8670L)
  public static void FUN_800f8670(final long a0) {
    FUN_80012b1c(0x1L, getMethodAddress(Bttl_800e.class, "FUN_800eee80", long.class), a0);
  }

  @Method(0x800f89ccL)
  public static long FUN_800f89cc(long a0) {
    a0 = (byte)a0;

    if(a0 == 0) {
      return 0x2L;
    }

    if((int)a0 <= 0) {
      return 0x4L;
    }

    //LAB_800f89ec
    return 0x1L;
  }

  @Method(0x800f89f4L)
  public static long FUN_800f89f4(final long a0, final long a1, final long a2, final long a3, final long a4, final long a5, final long a6, final long a7) {
    //LAB_800f8a30
    for(int i = 0; i < 12; i++) {
      if(_800c6b5c.deref(2).offset(i * 0xc4L).getSigned() == 0) {
        FUN_800f3354(i, a1, a2, a3, a4, a5, a6, a7);
        _800c6b5c.deref(4).offset(i * 0xc4L).offset(0x4L).setu(a0);
        return 0x1L;
      }

      //LAB_800f8a74
    }

    //LAB_800f8a84
    return 0;
  }

  @Method(0x800f8aa4L)
  public static void FUN_800f8aa4(final long a0, final long a1) {
    FUN_800f4268(a0, a1, 0x8L);
  }

  @Method(0x800f8ac4L)
  public static void FUN_800f8ac4(long a0, long a1, long a2, long a3) {
    assert false;
  }

  @Method(0x800f8b74L)
  public static void FUN_800f8b74(long a0) {
    long v0 = 0x800c_0000L;
    long t8 = v0 + 0x7194L;
    final short[] sp0x00 = new short[8];
    for(int i = 0; i < 8; i++) {
      sp0x00[i] = (short)MEMORY.ref(2, t8).offset(i * 0x2L).get();
    }

    //LAB_800f8bd8
    for(int t1 = 0; t1 < 8; t1++) {
      if((a0 & 0x1L << t1) != 0) {
        long a2 = _800c6c34.get();

        //LAB_800f8bf4
        for(int a3 = 0; a3 < 8; a3++) {
          if((MEMORY.ref(2, a2).offset(0x10L).get() & 0xfL) == sp0x00[a3]) {
            MEMORY.ref(2, a2).offset(0x10L).oru(0x80L);
            break;
          }

          //LAB_800f8c10
          a2 = a2 + 0x2L;
        }
      }

      //LAB_800f8c20
    }
  }

  @Method(0x800f8c38L)
  public static void FUN_800f8c38(final long a0) {
    final long v1 = _800c6c34.get();

    if(MEMORY.ref(2, v1).getSigned() != 0) {
      //LAB_800f8c78
      if(a0 != 0x1L || MEMORY.ref(4, v1).offset(0x44L).get() != 0) {
        //LAB_800f8c64
        MEMORY.ref(2, v1).offset(0x2L).and(0xfffdL);
        return;
      }

      MEMORY.ref(2, v1).offset(0x2L).oru(0x2L);
    }

    //LAB_800f8c98
  }

  @Method(0x800f8ca0L)
  public static long FUN_800f8ca0(final long a0) {
    //LAB_800f8cac
    long a2 = -0x1L;
    for(int i = 0; i < 32; i++) {
      if((a0 & 1 << i) != 0) {
        a2 = i;
        break;
      }

      //LAB_800f8cc0
    }

    //LAB_800f8cd0
    return a2;
  }

  @Method(0x800f8cd8L)
  public static void FUN_800f8cd8(final long a0, final long a1, final long a2, final long a3, final long a4, final long a5, final long a6, final long a7) {
    final long v0 = linkedListAddress_1f8003d8.get();
    linkedListAddress_1f8003d8.addu(0x28L);
    FUN_8003b590(v0);

    final long s1;
    if(a7 == -0x1L) {
      gpuLinkedListSetCommandTransparency(v0, false);
      s1 = 0;
    } else {
      //LAB_800f8d5c
      gpuLinkedListSetCommandTransparency(v0, true);
      s1 = a7;
    }

    //LAB_800f8d64
    MEMORY.ref(1, v0).offset(0x4L).setu(0x80L);
    MEMORY.ref(1, v0).offset(0x5L).setu(0x80L);
    MEMORY.ref(1, v0).offset(0x6L).setu(0x80L);
    FUN_800f8fac(v0, a0, a1, a2 & 0xffL, a3 & 0xffL, a4, a5, 0x1L);
    FUN_800f9024(v0, a6, s1);
  }

  @Method(0x800f8dfcL)
  public static void FUN_800f8dfc(final long a0, final long a1, final long a2, final long a3, final long a4, final long a5, final long a6, final long a7, final long a8) {
    final long t3 = _800c71ec.getAddress();

    final byte[] sp0x20 = new byte[] {
      (byte)MEMORY.ref(1, t3).offset(0x0L).getSigned(),
      (byte)MEMORY.ref(1, t3).offset(0x1L).getSigned(),
      (byte)MEMORY.ref(1, t3).offset(0x2L).getSigned(),
    };

    final long s0 = linkedListAddress_1f8003d8.get();
    linkedListAddress_1f8003d8.addu(0x28L);
    FUN_8003b590(s0);
    gpuLinkedListSetCommandTransparency(s0, false);

    if((int)a8 < 0x6L) {
      final long v0 = (sp0x20[(int)a7] + 0x80) / 6 * a8 - 0x80L;
      MEMORY.ref(1, s0).offset(0x4L).setu(v0);
      MEMORY.ref(1, s0).offset(0x5L).setu(v0);
      MEMORY.ref(1, s0).offset(0x6L).setu(v0);
    } else {
      //LAB_800f8ef4
      MEMORY.ref(1, s0).offset(0x4L).setu(sp0x20[(int)a7]);
      MEMORY.ref(1, s0).offset(0x5L).setu(sp0x20[(int)a7]);
      MEMORY.ref(1, s0).offset(0x6L).setu(sp0x20[(int)a7]);
    }

    FUN_800f8fac(s0, a0, a1, a2 & 0xffL, a3 & 0xffL, a4, a5, 0x1L);
    FUN_800f9024(s0, a6, 0);
  }

  @Method(0x800f8facL)
  public static void FUN_800f8fac(final long a0, final long a1, final long a2, final long a3, final long a4, final long a5, final long a6, final long a7) {
    MEMORY.ref(2, a0).offset(0x18L).setu(a1);
    MEMORY.ref(2, a0).offset(0x8L).setu(a1);
    MEMORY.ref(2, a0).offset(0x12L).setu(a2);
    MEMORY.ref(2, a0).offset(0xaL).setu(a2);
    MEMORY.ref(2, a0).offset(0x22L).setu(a2 + a6);
    MEMORY.ref(2, a0).offset(0x1aL).setu(a2 + a6);
    MEMORY.ref(2, a0).offset(0x20L).setu(a1 + a5);
    MEMORY.ref(2, a0).offset(0x10L).setu(a1 + a5);

    if(a7 == 0x1L) {
      MEMORY.ref(1, a0).offset(0x24L).setu(a3 + a5);
      MEMORY.ref(1, a0).offset(0x14L).setu(a3 + a5);
      MEMORY.ref(1, a0).offset(0x1cL).setu(a3);
      MEMORY.ref(1, a0).offset(0xcL).setu(a3);
      MEMORY.ref(1, a0).offset(0x15L).setu(a4);
      MEMORY.ref(1, a0).offset(0xdL).setu(a4);
      MEMORY.ref(1, a0).offset(0x25L).setu(a4 + a6);
      MEMORY.ref(1, a0).offset(0x1dL).setu(a4 + a6);
    }

    //LAB_800f901c
  }

  @Method(0x800f9024L)
  public static void FUN_800f9024(final long a0, final long a1, final long a2) {
    final long t0;
    final long t1;
    if((int)a1 >= 0x80L) {
      t1 = 0x1L;
      t0 = a1 - 0x80L;
    } else {
      //LAB_800f9080
      t1 = 0;
      t0 = a1;
    }

    //LAB_800f9088
    //LAB_800f9098
    //LAB_800f90a8
    long v1 = (_800c7114.offset(2, t1 * 0x8L + 0x4L).get() + t0 % 0x10L) * 0x40L;
    long v0 = _800c7114.offset(4, t1 * 0x8L).get() + t0 / 0x10L * 0x10L;
    v0 = v0 & 0x3f0L;
    v0 = (int)v0 >> 4;
    v1 = v1 | v0;
    MEMORY.ref(2, a0).offset(0xeL).setu(v1);
    MEMORY.ref(2, a0).offset(0x16L).setu(GetTPage(0, a2, 704, 496));
    insertElementIntoLinkedList(tags_1f8003d0.getPointer() + 0x7cL, a0);
  }

  @Method(0x800f9380L)
  public static void FUN_800f9380(final long a0, final long a1) {
    if(MEMORY.ref(1, a1).offset(0xa8L).get() != 0) {
      //LAB_800f93c8
      for(int i = 0; i < 8; i++) {
        if((MEMORY.ref(2, a1).offset(0xa8L).getSigned() & (0x80L >> i)) != 0) {
          //LAB_800f93e8
          final long a3;
          if(MEMORY.ref(2, a0).offset(0x272L).getSigned() != MEMORY.ref(2, a1).offset(0x272L).getSigned()) {
            a3 = 0x3L;
          } else {
            a3 = 0x4L;
          }

          //LAB_800f9428
          final long a0_0;
          if(i < 0x4L) {
            a0_0 = 0x32L;
          } else {
            a0_0 = -0x32L;
          }

          //LAB_800f9438
          MEMORY.ref(2, a1).offset(0x4L).offset(_800c723c.offset(i % 4 * 4).get() * 0x2L).setu(a3 << 8 | a0_0);
        }

        //LAB_800f9454
      }
    }

    //LAB_800f9464
  }

  @Method(0x800f946cL)
  public static long FUN_800f946c(final long a0, final long a1, final long a2) {
    final long a0_0;
    if(a2 == 0) {
      a0_0 = _800fa0b8.offset(MEMORY.ref(2, a0).offset(0x4eL).getSigned() * 0xcL).offset(0x3L).get();
    } else {
      //LAB_800f949c
      a0_0 = MEMORY.ref(2, a0).offset(0xd8L).getSigned();
    }

    if(a0_0 == 0x1L) {
      //LAB_800f9570
      return a1 * 8;
    }

    if(a0_0 == 0x2L) {
      //LAB_800f9564
      return a1 * 6;
    }

    //LAB_800f94d8
    if(a0_0 == 0x4L) {
      //LAB_800f955c
      return a1 * 5;
    }

    //LAB_800f94a0
    if(a0_0 == 0x8L) {
      //LAB_800f9554
      return a1 * 4;
    }

    if(a0_0 == 0x10L) {
      //LAB_800f954c
      return a1 * 3;
    }

    //LAB_800f94ec
    if(a0_0 == 0x20L) {
      //LAB_800f9544
      return a1 * 2;
    }

    //LAB_800f9510
    if(a0_0 == 0x40L) {
      //LAB_800f9534
      return a1 + a1 / 2;
    }

    if(a0_0 == 0x80L) {
      return a1 / 2;
    }

    //LAB_800f9578
    //LAB_800f957c
    return a1;
  }

  @Method(0x800f9584L)
  public static void FUN_800f9584() {
    final long v0 = _800c6b6c.get();
    MEMORY.ref(2, v0).offset(0x00L).setu(0);
    MEMORY.ref(2, v0).offset(0x02L).setu(0);
    MEMORY.ref(2, v0).offset(0x04L).setu(0);
    MEMORY.ref(2, v0).offset(0x06L).setu(0);
    MEMORY.ref(2, v0).offset(0x08L).setu(0);
    MEMORY.ref(2, v0).offset(0x0aL).setu(0);
    MEMORY.ref(2, v0).offset(0x0cL).setu(0);
    MEMORY.ref(2, v0).offset(0x0eL).setu(0);
    MEMORY.ref(2, v0).offset(0x10L).setu(0);

    //LAB_800f95b8
    for(int i = 0; i < 10; i++) {
      MEMORY.ref(4, v0).offset(0x14L).offset(i * 0x4L).setu(0);
    }
  }

  @Method(0x800f95d0L)
  public static long FUN_800f95d0(final RunningScript a0) {
    a0.params_20.get(2).deref().set(FUN_800f1aa8(a0.params_20.get(0).deref().get(), a0.params_20.get(1).deref().get(), 0));
    return 0;
  }

  @Method(0x800f96a8L)
  public static long FUN_800f96a8(final RunningScript a0) {
    FUN_800f7b68(a0.params_20.get(0).deref().get());
    return 0;
  }

  @Method(0x800f96d4L)
  public static long FUN_800f96d4(final RunningScript a0) {
    final BtldScriptData27c v1 = scriptStatePtrArr_800bc1c0.get(a0.params_20.get(0).deref().get()).deref().innerStruct_00.derefAs(BtldScriptData27c.class);
    a0.params_20.get(1).deref().set(v1._148.coord2_14.coord.transfer.getX());
    a0.params_20.get(2).deref().set(v1._148.coord2_14.coord.transfer.getY());
    a0.params_20.get(3).deref().set(v1._148.coord2_14.coord.transfer.getZ());
    return 0;
  }

  @Method(0x800f97d8L)
  public static long FUN_800f97d8(final RunningScript a0) {
    FUN_800f4964();
    FUN_800f49bc(scriptStatePtrArr_800bc1c0.get(a0.params_20.get(0).deref().get()).deref().innerStruct_00.derefAs(BtldScriptData27c.class)._272.get(), (short)a0.params_20.get(1).deref().get());
    return 0;
  }

  @Method(0x800f9b2cL)
  public static long FUN_800f9b2c(final RunningScript a0) {
    //LAB_800f9b3c
    final long v1 = _800c6b5c.get();
    long a2 = 0;
    for(int i = 0; i < 12; i++) {
      if(MEMORY.ref(2, v1).offset(i * 0xc4L).getSigned() != 0) {
        a2 = 0x1L;
        break;
      }

      //LAB_800f9b58
    }

    //LAB_800f9b64
    a0.params_20.get(0).deref().set((int)a2);
    return 0;
  }

  @Method(0x800f9b78L)
  public static long FUN_800f9b78(final RunningScript a0) {
    _800c6b64.setu(a0.params_20.get(0).deref().get());
    return 0;
  }

  @Method(0x800f9b94L)
  public static long FUN_800f9b94(final RunningScript a0) {
    final long v1 = _800c6b6c.get();
    MEMORY.ref(2, v1).offset(0x0L).setu(0x1L);
    MEMORY.ref(2, v1).offset(0x6L).setu(a0.params_20.get(0).deref().get());
    MEMORY.ref(2, v1).offset(0x8L).setu(a0.params_20.get(1).deref().get());
    return 0;
  }

  @Method(0x800f9c00L)
  public static long FUN_800f9c00(final RunningScript a0) {
    FUN_800fa018(a0.params_20.get(0).deref().get());
    return 0;
  }

  @Method(0x800f9e50L)
  public static long FUN_800f9e50(long a0) {
    assert false;
    return 0;
  }

  @Method(0x800f9ee8L)
  public static void FUN_800f9ee8(final long a0, final long a1, final long a2, final long a3, final long r, final long g, final long b, final boolean transparent) {
    final long s0 = linkedListAddress_1f8003d8.get();
    FUN_8003b690(s0);
    gpuLinkedListSetCommandTransparency(s0, transparent);
    MEMORY.ref(1, s0).offset(0x4L).setu(r);
    MEMORY.ref(1, s0).offset(0x5L).setu(g);
    MEMORY.ref(1, s0).offset(0x6L).setu(b);
    MEMORY.ref(2, s0).offset(0x8L).setu(a0);
    MEMORY.ref(2, s0).offset(0xaL).setu(a1);
    MEMORY.ref(1, s0).offset(0xcL).setu(r);
    MEMORY.ref(1, s0).offset(0xdL).setu(g);
    MEMORY.ref(1, s0).offset(0xeL).setu(b);
    MEMORY.ref(2, s0).offset(0x10L).setu(a2);
    MEMORY.ref(2, s0).offset(0x12L).setu(a3);
    insertElementIntoLinkedList(tags_1f8003d0.getPointer() + 0x7cL, s0);
    linkedListAddress_1f8003d8.addu(0x14L);

    final long a1_0 = linkedListAddress_1f8003d8.get();
    MEMORY.ref(1, a1_0).offset(0x3L).setu(0x1L);
    MEMORY.ref(4, a1_0).offset(0x4L).setu(0xe100_0200L | (_800bb110.offset((transparent ? 1 : 0) * 0x4L).offset(0x2L).get() | 0xbL) & 0x9ffL);
    insertElementIntoLinkedList(tags_1f8003d0.getPointer() + 0x7cL, a1_0);
    linkedListAddress_1f8003d8.addu(0x8L);
  }

  @Method(0x800fa018L)
  public static void FUN_800fa018(final long a0) {
    final long v1 = _800c6b6c.get();

    if(MEMORY.ref(2, v1).offset(0x0L).getSigned() != 0) {
      if(a0 == 0x1L) {
        MEMORY.ref(2, v1).offset(0x2L).oru(0x1L);
      } else {
        //LAB_800fa050
        MEMORY.ref(2, v1).offset(0x2L).and(0xfffeL);
      }
    }

    //LAB_800fa060
  }

  @Method(0x800fa068L)
  public static long FUN_800fa068(final long a0) {
    return MathHelper.clamp(a0, 20, 300);
  }

  @Method(0x800fa090L)
  public static long FUN_800fa090(final long a0) {
    return MathHelper.clamp(a0, 20, 220);
  }
}
