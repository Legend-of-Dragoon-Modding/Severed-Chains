package legend.game.combat;

import legend.core.MathHelper;
import legend.core.gte.DVECTOR;
import legend.core.memory.Memory;
import legend.core.memory.Method;
import legend.core.memory.Value;
import legend.game.combat.types.BattleObject27c;
import legend.game.combat.types.BattleMenuStruct58;
import legend.game.combat.types.BattleStruct3c;
import legend.game.combat.types.BttlStructa4;
import legend.game.types.ActiveStatsa0;
import legend.game.types.LodString;
import legend.game.types.RunningScript;
import legend.game.types.ScriptState;

import static legend.core.Hardware.MEMORY;
import static legend.core.MemoryHelper.getMethodAddress;
import static legend.game.Scus94491BpeSegment.FUN_80012b1c;
import static legend.game.Scus94491BpeSegment.FUN_80012bb4;
import static legend.game.Scus94491BpeSegment.centreScreenX_1f8003dc;
import static legend.game.Scus94491BpeSegment.centreScreenY_1f8003de;
import static legend.game.Scus94491BpeSegment.insertElementIntoLinkedList;
import static legend.game.Scus94491BpeSegment.linkedListAddress_1f8003d8;
import static legend.game.Scus94491BpeSegment.loadDrgnBinFile;
import static legend.game.Scus94491BpeSegment.memcpy;
import static legend.game.Scus94491BpeSegment.playSound;
import static legend.game.Scus94491BpeSegment.simpleRand;
import static legend.game.Scus94491BpeSegment.tags_1f8003d0;
import static legend.game.Scus94491BpeSegment_8002.FUN_800232dc;
import static legend.game.Scus94491BpeSegment_8002.FUN_800297a0;
import static legend.game.Scus94491BpeSegment_8002.renderText;
import static legend.game.Scus94491BpeSegment_8002.textWidth;
import static legend.game.Scus94491BpeSegment_8003.GetTPage;
import static legend.game.Scus94491BpeSegment_8003.gpuLinkedListSetCommandTransparency;
import static legend.game.Scus94491BpeSegment_8003.setGp0_2c;
import static legend.game.Scus94491BpeSegment_8003.setGp0_38;
import static legend.game.Scus94491BpeSegment_8003.setGp0_50;
import static legend.game.Scus94491BpeSegment_8004._8004f2ac;
import static legend.game.Scus94491BpeSegment_8005._80050ae8;
import static legend.game.Scus94491BpeSegment_8005.spells_80052734;
import static legend.game.Scus94491BpeSegment_8006._8006e398;
import static legend.game.Scus94491BpeSegment_8006._8006f1d8;
import static legend.game.Scus94491BpeSegment_8006._8006f284;
import static legend.game.Scus94491BpeSegment_8007.joypadInput_8007a39c;
import static legend.game.Scus94491BpeSegment_8007.joypadPress_8007a398;
import static legend.game.Scus94491BpeSegment_8007.vsyncMode_8007a3b8;
import static legend.game.Scus94491BpeSegment_800b._800bb0fc;
import static legend.game.Scus94491BpeSegment_800b._800bb110;
import static legend.game.Scus94491BpeSegment_800b._800bb112;
import static legend.game.Scus94491BpeSegment_800b._800bc950;
import static legend.game.Scus94491BpeSegment_800b._800be5d0;
import static legend.game.Scus94491BpeSegment_800b.gameState_800babc8;
import static legend.game.Scus94491BpeSegment_800b.scriptStatePtrArr_800bc1c0;
import static legend.game.Scus94491BpeSegment_800b.stats_800be5f8;
import static legend.game.combat.Bttl_800c.FUN_800c7488;
import static legend.game.combat.Bttl_800c._800c669c;
import static legend.game.combat.Bttl_800c._800c66b0;
import static legend.game.combat.Bttl_800c._800c6718;
import static legend.game.combat.Bttl_800c._800c6748;
import static legend.game.combat.Bttl_800c._800c6758;
import static legend.game.combat.Bttl_800c.charCount_800c677c;
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
import static legend.game.combat.Bttl_800c._800c6ba0;
import static legend.game.combat.Bttl_800c._800c6ba1;
import static legend.game.combat.Bttl_800c._800c6ba8;
import static legend.game.combat.Bttl_800c._800c6c2c;
import static legend.game.combat.Bttl_800c._800c6c30;
import static legend.game.combat.Bttl_800c.battleMenu_800c6c34;
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
import static legend.game.combat.Bttl_800c._800c7124;
import static legend.game.combat.Bttl_800c._800c7190;
import static legend.game.combat.Bttl_800c._800c7192;
import static legend.game.combat.Bttl_800c._800c7193;
import static legend.game.combat.Bttl_800c._800c71ec;
import static legend.game.combat.Bttl_800c._800c71f0;
import static legend.game.combat.Bttl_800c._800c71fc;
import static legend.game.combat.Bttl_800c._800c721c;
import static legend.game.combat.Bttl_800c._800c723c;
import static legend.game.combat.Bttl_800c._800c724c;
import static legend.game.combat.Bttl_800c._800c726c;
import static legend.game.combat.Bttl_800c._800c7284;
import static legend.game.combat.Bttl_800c._800c729c;
import static legend.game.combat.Bttl_800c._800c72b4;
import static legend.game.combat.Bttl_800c._800d66b0;
import static legend.game.combat.Bttl_800c._800d6c30;
import static legend.game.combat.Bttl_800c._800fa0b8;
import static legend.game.combat.Bttl_800c.allText_800fb3c0;
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
      setGp0_2c(s0);
      gpuLinkedListSetCommandTransparency(s0, false);
      MEMORY.ref(1, s0).offset(0x6L).setu(0x80L);
      MEMORY.ref(1, s0).offset(0x5L).setu(0x80L);
      MEMORY.ref(1, s0).offset(0x4L).setu(0x80L);
      if(i == 5 || i == 7) {
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
    setGp0_38(s0);
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
    setGp0_38(s0);
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
      //LAB_800f1a28
      for(int i = 0; i < 3; i++) {
        final BattleStruct3c v1 = _800c6c40.get(i);

        if(v1.charIndex_00.get() != -1) {
          v1._14.get(2).set(0);
          v1.flags_06.and(0xffff_fffe).and(0xffff_fffd);
        }

        //LAB_800f1a4c
      }

      return;
    }

    //LAB_800f1a64
    //LAB_800f1a70
    for(int i = 0; i < 3; i++) {
      final BattleStruct3c v1 = _800c6c40.get(i);
      if(v1.charIndex_00.get() != -1) {
        v1._14.get(2).set(0);
        v1.flags_06.or(0x3);
      }

      //LAB_800f1a90
    }
  }

  @Method(0x800f1aa8L)
  public static int FUN_800f1aa8(final int scriptIndex1, final int scriptIndex2, final long a2) {
    long s0;
    long s4 = 0;
    ScriptState<?> state = scriptStatePtrArr_800bc1c0.get(scriptIndex1).deref();
    final BattleObject27c s1 = state.innerStruct_00.derefAs(BattleObject27c.class);

    state = scriptStatePtrArr_800bc1c0.get(scriptIndex2).deref();
    final BattleObject27c s5 = state.innerStruct_00.derefAs(BattleObject27c.class);

    final long s7 = (byte)s1.all_04.get((int)_800c703c.offset(a2 * 0x4L).get()).get() + 100;
    final long s3 = (state.ui_60.get() >>> 2) & 0x1L;
    if(a2 == 0) {
      if(s3 != 0) {
        s0 = _800fa0b8.offset(s1._4e.get() * 0xcL).offset(0x5L).get();
      } else {
        //LAB_800f1bf4
        s0 = s1.attackHit_3c.get();
      }

      //LAB_800f1bf8
      FUN_800f7b68(scriptIndex1);
    } else if(a2 == 0x1L) {
      //LAB_800f1c08
      s0 = _800fa0b8.offset(s1._4e.get() * 0xcL).offset(0x5L).get();
      FUN_800f7b68(scriptIndex1);
    } else {
      //LAB_800f1c38
      FUN_800f7a74(scriptIndex1);
      s0 = 100;
    }

    //LAB_800f1c44
    s0 = s0 * s7 / 100;

    final long avoid;
    if(a2 == 0) {
      avoid = s5.attackAvoid_40.get();
    } else {
      //LAB_800f1c9c
      avoid = s5.magicAvoid_42.get();
    }

    //LAB_800f1ca8
    final long a0_0 = (avoid * ((byte)s1.all_04.get((int)_800c703c.offset(0x10L).offset(a2 * 0x4L).get()).get() + 100)) / 100;
    if(a0_0 < s0 && s0 - a0_0 >= (simpleRand() * 101 >> 16)) {
      s4 = 0x1L;
      if(s3 != 0) {
        FUN_800f7b68(scriptIndex1);
      }
    }

    //LAB_800f1d28
    if((s1.all_04.get((int)_800c703c.offset(0x20L).offset(a2 * 0x4L).get()).get() & _800c703c.offset(0x30L).offset(a2 * 0x4L).get()) != 0) {
      s4 = 0x1L;
    }

    //LAB_800f1d5c
    return (int)s4;
  }

  @Method(0x800f1d88L)
  public static long FUN_800f1d88(final int scriptIndex1, final int scriptIndex2) {
    long s1;
    long s2;
    long s6;
    long s7;

    final long[] sp0x10 = new long[10];
    for(int i = 0; i < sp0x10.length; i++) {
      sp0x10[i] = _800c706c.offset(i * 0x2L).get();
    }

    ScriptState<?> a1 = scriptStatePtrArr_800bc1c0.get(scriptIndex1).deref();
    final BattleObject27c s0 = a1.innerStruct_00.derefAs(BattleObject27c.class);
    if((a1.ui_60.get() & 0x4L) == 0) {
      s2 = FUN_800f2af4(scriptIndex1, scriptIndex2);
      s1 = s0._1c.get();
    } else {
      //LAB_800f1e5c
      s2 = FUN_800f2d48(scriptIndex1, scriptIndex2);
      s1 = _800fa0b8.offset(s0._4e.get() * 0xcL).offset(0x8L).get();
    }

    //LAB_800f1e88
    s7 = FUN_800f89cc(s0._b4.get());
    if((int)_800c6b64.get() == -0x1L) {
      s6 = 0;
    } else {
      s6 = s1;
    }

    a1 = scriptStatePtrArr_800bc1c0.get(scriptIndex2).deref();
    final BattleObject27c s3 = a1.innerStruct_00.derefAs(BattleObject27c.class);

    //LAB_800f1eb0
    final long s0_0;
    if((a1.ui_60.get() & 0x4L) == 0) {
      if(s3.charIndex_272.get() == 0 && (gameState_800babc8.dragoonSpirits_19c.get(0).get() & 0xff) >>> 7 != 0 && (scriptStatePtrArr_800bc1c0.get(scriptIndex2).deref().ui_60.get() & 0x2L) != 0) {
        s0_0 = sp0x10[9];
      } else {
        s0_0 = sp0x10[s3.charIndex_272.get()];
      }
    } else {
      //LAB_800f1f1c
      s0_0 = s3._72.get();
    }

    //LAB_800f1f20
    final long s4_0 = FUN_800f89cc(s3._b8.get());

    final long s3_0;
    if((int)_800c6b64.get() == -0x1L) {
      s3_0 = 0;
    } else {
      //LAB_800f1f4c
      s3_0 = sp0x10[(int)_800c6b64.get()];
    }

    //LAB_800f1f54
    s2 = s2 * FUN_800f2fe0((short)s1, (short)s0_0, 0) / 100;
    s2 = s2 * FUN_800f2fe0((short)s7, (short)s4_0, 0x3L) / 100;
    s2 = s2 * FUN_800f2fe0((short)s6, (short)s3_0, 0x5L) / 100;
    if((int)s2 <= 0) {
      s2 = 0;
    }

    //LAB_800f2020
    return s2;
  }

  @Method(0x800f204cL)
  public static long FUN_800f204c(final int scriptIndex1, final int scriptIndex2, final long a2) {
    long s1;
    long s2;
    long s4;
    long s6;
    long fp;
    final short[] sp0x18 = new short[10];
    for(int i = 0; i < sp0x18.length; i++) {
      sp0x18[i] = (short)_800c706c.offset(i * 0x2L).getSigned();
    }

    final ScriptState<?> state1 = scriptStatePtrArr_800bc1c0.get(scriptIndex1).deref();
    final BattleObject27c data1 = state1.innerStruct_00.derefAs(BattleObject27c.class);
    final ScriptState<?> state2 = scriptStatePtrArr_800bc1c0.get(scriptIndex2).deref();
    final BattleObject27c data2 = state2.innerStruct_00.derefAs(BattleObject27c.class);
    if(a2 == 0x1L || data1._ea.get() == 0) {
      //LAB_800f2140
      if((data1._96.get() & 0x4L) != 0) {
        s4 = data2.maxHp_10.get() * data1._9c.get() / 100;

        if((data1._94.get() & 0x8L) != 0) {
          if((state1.ui_60.get() & 0x4L) == 0) {
            s1 = charCount_800c677c.get();
            s2 = _8006f1d8.getAddress();
          } else {
            //LAB_800f21a8
            s1 = _800c6758.get();
            s2 = _8006f1d8.offset(0x7cL).getAddress();
          }

          //LAB_800f21b0
          //LAB_800f21d4
          for(int i = 0; i < s1; i++) {
            final BattleObject27c a1_0 = scriptStatePtrArr_800bc1c0.get((int)MEMORY.ref(4, s2).get()).deref().innerStruct_00.derefAs(BattleObject27c.class);
            a1_0._a8.set(data1._a8.get());
            FUN_800f9380(data1, a1_0);
            s2 = s2 + 0x4L;
          }
        } else {
          //LAB_800f2210
          data2._a8.set(data1._a8.get());
          FUN_800f9380(data1, data2);
        }

        //LAB_800f2224
        data1._0e.or(0x800);
      } else {
        //LAB_800f2238
        if((state1.ui_60.get() & 0x4L) == 0) {
          s4 = FUN_800f2e98(scriptIndex1, scriptIndex2, a2);
        } else {
          //LAB_800f2250
          s4 = FUN_800f8768(scriptIndex1, scriptIndex2, a2);
        }

        //LAB_800f225c
        if(a2 == 0x1L) {
          s2 = _800fa0b8.offset(data1._4e.get() * 0xcL).offset(0x8L).get();
        } else {
          //LAB_800f228c
          s2 = data1._d6.get();
        }

        //LAB_800f2290
        fp = FUN_800f89cc(data1._b6.get());

        if((int)_800c6b64.get() == -0x1L) {
          s6 = 0;
        } else {
          s6 = s2;
        }

        //LAB_800f22b8
        if((state2.ui_60.get() & 0x4L) == 0) {
          if(data2.charIndex_272.get() == 0 && (gameState_800babc8.dragoonSpirits_19c.get(0).get() >>> 7) != 0 && (scriptStatePtrArr_800bc1c0.get(scriptIndex2).deref().ui_60.get() & 0x2L) != 0) {
            s1 = sp0x18[1];
          } else {
            s1 = sp0x18[data2.charIndex_272.get()];
          }
        } else {
          //LAB_800f2324
          s1 = data2._72.get();
        }

        //LAB_800f2328
        final long s0 = FUN_800f89cc(data2._ba.get());

        final long s3;
        if((int)_800c6b64.get() == -0x1L) {
          s3 = 0;
        } else {
          //LAB_800f2354
          s3 = sp0x18[(int)_800c6b64.get()];
        }

        //LAB_800f235c
        s4 = s4 * FUN_800f2fe0(s2, s1, 0) / 100 * FUN_800f2fe0(fp, s0, 0x4L) * FUN_800f2fe0(s6, s3, 0x5L) / 6250;
      }
    } else {
      //LAB_800f2404
      //LAB_800f2410
      for(s1 = 0; s1 < 8; s1++) {
        if((data1._ea.get() & 0x80 >> s1) != 0) {
          break;
        }
      }

      //LAB_800f2430
      switch((int)s1) {
        case 0 -> {
          //LAB_800f2454
          data1._0e.or(0x800);
          s4 = data2.maxHp_10.get();
        }

        case 1 -> {
          //LAB_800f2464
          data1._0e.or(0x800);
          s4 = data2.maxMp_12.get();
        }

        //LAB_800f2478
        case 6 -> s4 = data2.maxHp_10.get();

        //LAB_800f2484
        case 7 -> s4 = data2.maxMp_12.get();

        //LAB_800f2490
        default -> s4 = 0;
      }

      //LAB_800f2494
      //LAB_800f24bc
      s4 = s4 * data1._e6.get() / 100;
    }

    //LAB_800f24c0
    if((int)s4 <= 0) {
      s4 = 0;
    }

    //LAB_800f24d0
    return s4;
  }

  @Method(0x800f2500L)
  public static long FUN_800f2500(final RunningScript s3) {
    long t0;
    final ScriptState<?> v0 = scriptStatePtrArr_800bc1c0.get(s3.params_20.get(0).deref().get()).deref();
    final BattleObject27c s2 = v0.innerStruct_00.derefAs(BattleObject27c.class);
    long s1 = FUN_800f1d88(s3.params_20.get(0).deref().get(), s3.params_20.get(1).deref().get());
    if((v0.ui_60.get() & 0x4L) != 0) {
      s1 = FUN_800f946c(s2, s1, 0);
      FUN_800f9380(s2, s2);
    }

    //LAB_800f257c
    if((int)s1 <= 0) {
      s1 = 0x1L;
    }

    //LAB_800f2588
    long a2 = FUN_800f29d4(s3.params_20.get(1).deref().get(), s1, 0);
    ScriptState<?> a0 = scriptStatePtrArr_800bc1c0.get(s3.params_20.get(0).deref().get()).deref();
    final BattleObject27c a1 = a0.innerStruct_00.derefAs(BattleObject27c.class);

    final long a0_0;
    if((a0.ui_60.get() & 0x4L) == 0) {
      a0_0 = a1._1c.get();
    } else {
      //LAB_800f25f4
      a0_0 = _800fa0b8.offset(1, a1._4e.get() * 0xcL).offset(0x8L).get();
    }

    a0 = scriptStatePtrArr_800bc1c0.get(s3.params_20.get(1).deref().get()).deref();
    final BattleObject27c a3 = a0.innerStruct_00.derefAs(BattleObject27c.class);

    //LAB_800f2614
    if((a0.ui_60.get() & 0x4L) == 0) {
      t0 = a3._20.get();
    } else {
      t0 = 0;
    }

    //LAB_800f2620
    if((a0_0 & t0) != 0) {
      a2 = (int)a2 >> 1;
    }

    //LAB_800f2634
    if((a0_0 & a3._22.get()) != 0) {
      a2 = 0;
    }

    //LAB_800f2640
    s3.params_20.get(2).deref().set((int)a2);
    s3.params_20.get(3).deref().set(FUN_800f7c5c(s3.params_20.get(0).deref().get(), s3.params_20.get(1).deref().get(), 0));
    return 0;
  }

  @Method(0x800f2838L)
  public static long FUN_800f2838(final RunningScript s1) {
    long a0;
    long a1;
    long a3;
    final BattleObject27c s0 = scriptStatePtrArr_800bc1c0.get(s1.params_20.get(0).deref().get()).deref().innerStruct_00.derefAs(BattleObject27c.class);
    FUN_800f9e10(s0);
    FUN_800f7a74(s1.params_20.get(0).deref().get());
    a1 = Math.max(1, FUN_800f946c(s0, FUN_800f204c(s1.params_20.get(0).deref().get(), s1.params_20.get(1).deref().get(), 0), 0x1L));

    //LAB_800f28c8
    if((s0._0e.get() & 0x800L) != 0) {
      s0._0e.and(0xf7ff);
    } else {
      //LAB_800f28e4
      a1 = FUN_800f29d4(s1.params_20.get(1).deref().get(), a1, 0x1L);
      a3 = scriptStatePtrArr_800bc1c0.get(s1.params_20.get(0).deref().get()).deref()._d4.get() >>> 16;
      final ScriptState<?> state = scriptStatePtrArr_800bc1c0.get(s1.params_20.get(1).deref().get()).deref();
      a0 = state.innerStruct_00.getPointer(); //TODO

      //LAB_800f294c
      if((state.ui_60.get() & 0x4L) == 0 && (a3 & MEMORY.ref(2, a0).offset(0x20L).get()) != 0) {
        a1 = (int)a1 >> 1;
      }

      //LAB_800f2960
      if((a3 & MEMORY.ref(2, a0).offset(0x22L).get()) != 0) {
        a1 = 0;
      }
    }

    //LAB_800f2970
    s1.params_20.get(3).deref().set((int)a1);
    s1.params_20.get(4).deref().set(FUN_800f7c5c(s1.params_20.get(0).deref().get(), s1.params_20.get(1).deref().get(), 0x2L));
    FUN_800f8854(s1.params_20.get(0).deref().get(), s1.params_20.get(1).deref().get(), 0x1L);
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
  public static long FUN_800f2af4(final int scriptIndex1, final int scriptIndex2) {
    final ScriptState<BattleObject27c> state1 = scriptStatePtrArr_800bc1c0.get(scriptIndex1).derefAs(ScriptState.classFor(BattleObject27c.class));
    final ScriptState<BattleObject27c> state2 = scriptStatePtrArr_800bc1c0.get(scriptIndex2).derefAs(ScriptState.classFor(BattleObject27c.class));

    final BattleObject27c combatant1 = state1.innerStruct_00.deref();
    final BattleObject27c combatant2 = state2.innerStruct_00.deref();
    long s2 = combatant1.attack_34.get();

    long s0;
    if(combatant1.selectedAddition_58.get() == -1) { // No addition (Shana/???)
      //LAB_800f2c24
      if((state1.ui_60.get() & 0x2L) != 0) { // Is dragoon
        //LAB_800f2c4c
        s2 = s2 * combatant1.dragoonAttack_ac.get() / 100;
      }
    } else if(combatant1._56.get() > 0) {
      //LAB_800f2b94
      long s1 = 0;
      for(s0 = 0; s0 < ((combatant1._56.get() - 1) & 7) + 1; s0++) {
        s1 = s1 + FUN_800c7488(combatant1.charSlot_276.get(), s0, 0x4L);
      }

      //LAB_800f2bb4
      final long v0;
      if((state1.ui_60.get() & 0x2L) != 0) { // Is dragoon
        v0 = combatant1.dragoonAttack_ac.get();
      } else {
        //LAB_800f2bec
        v0 = combatant1._11c.get() + 100;
      }

      //LAB_800f2bfc
      s1 = s1 * v0 / 100;
      s2 = s2 * s1 / 100;
    }

    //LAB_800f2c6c
    //LAB_800f2c70
    long a0_0 = combatant2.defence_38.get();
    if((state2.ui_60.get() & 0x2L) != 0) {
      a0_0 = a0_0 * combatant2.dragoonDefence_b0.get() / 100;
    }

    //LAB_800f2ccc
    final long v1 = (combatant1.level_04.get() + 5) * s2;
    s0 = v1 * 5 % a0_0;
    s2 = v1 * 5 / a0_0;
    return s2 + ((int)s0 < a0_0 / 2 ? 0 : 1);
  }

  @Method(0x800f2d48L)
  public static long FUN_800f2d48(final int scriptIndex1, final int scriptIndex2) {
    final ScriptState<BattleObject27c> state1 = scriptStatePtrArr_800bc1c0.get(scriptIndex1).derefAs(ScriptState.classFor(BattleObject27c.class));
    final ScriptState<BattleObject27c> state2 = scriptStatePtrArr_800bc1c0.get(scriptIndex2).derefAs(ScriptState.classFor(BattleObject27c.class));
    final BattleObject27c combatant1 = state1.innerStruct_00.deref();
    final BattleObject27c combatant2 = state2.innerStruct_00.deref();

    final int atk = combatant1.attack_34.get() + (int)_800fa0b8.offset(combatant1._4e.get() * 0xcL).offset(0x4L).get();

    //TODO impossible condition with code that does nothing?
    if((state2.ui_60.get() & 0x4L) == 0x1L) {
      //LAB_800f2ddc
      //LAB_800f2dec
      for(int i = 0; i < Math.min(3, charCount_800c677c.get()); i++) {
        if(scriptStatePtrArr_800bc1c0.get((int)_8006e398.offset(0xe40L).offset(i * 0x4L).get()).deref().innerStruct_00.derefAs(BattleObject27c.class).charIndex_272.get() == 0) {
          break;
        }
      }
    }

    //LAB_800f2e28
    int def = combatant2.defence_38.get();
    if((state2.ui_60.get() & 0x2L) != 0) { // Is dragoon
      def = def * combatant2.dragoonDefence_b0.get() / 100;
    }

    //LAB_800f2e88
    return atk * atk * 5 / def;
  }

  @Method(0x800f2e98L)
  public static long FUN_800f2e98(final int scriptIndex1, final int scriptIndex2, final long a2) {
    final BattleObject27c t0 = scriptStatePtrArr_800bc1c0.get(scriptIndex1).deref().innerStruct_00.derefAs(BattleObject27c.class);
    long matk = t0.magicAttack_36.get();
    if(a2 == 0x1L) {
      matk = matk + _800fa0b8.offset(t0._4e.get() * 0xcL).offset(0x4L).get();
    } else {
      //LAB_800f2ef8
      matk = matk + t0._de.get();
    }

    //LAB_800f2f04
    if((scriptStatePtrArr_800bc1c0.get(scriptIndex1).deref().ui_60.get() & 0x2L) != 0) {
      matk = matk * t0.dragoonMagic_ae.get() / 100;
    }

    //LAB_800f2f5c
    final long a1 = scriptStatePtrArr_800bc1c0.get(scriptIndex2).deref().ui_60.get();
    final BattleObject27c t1 = scriptStatePtrArr_800bc1c0.get(scriptIndex2).deref().innerStruct_00.derefAs(BattleObject27c.class);
    long mdef = t1.magicDefence_3a.get();

    if((a1 & 0x4L) != 0x1L && (a1 & 0x2L) != 0) {
      mdef = mdef * t1.dragoonMagicDefence_b2.get() / 100;
    }

    //LAB_800f2fb4
    return (t0.level_04.get() + 0x5L) * matk * 5 / mdef;
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

  @Method(0x800f3204L)
  public static void FUN_800f3204(long a0) {
    final ScriptState<BattleObject27c> state = scriptStatePtrArr_800bc1c0.get((int)a0).derefAs(ScriptState.classFor(BattleObject27c.class));
    final BattleObject27c a1 = state.innerStruct_00.deref();

    final int a2;
    final int a3;
    final int t0;
    final int t1;
    final int speed;
    if((state.ui_60.get() & 0x4L) != 0) {
      a2 = 0;
      a3 = 0;
      t0 = 0;
      t1 = 0;
      speed = a1._64.get();
    } else {
      //LAB_800f3244
      final ActiveStatsa0 stats = stats_800be5f8.get(a1.charIndex_272.get());
      a2 = stats._4e.get();
      a3 = stats._50.get();
      t0 = stats._52.get();
      t1 = stats._54.get();
      speed = stats.gearSpeed_86.get() + stats.bodySpeed_69.get();
    }

    //LAB_800f327c
    if(a1._c8.get() != 0) {
      a1.speed_32.set(speed * 2);
    } else {
      a1.speed_32.set(speed);
    }

    //LAB_800f3294
    if(a1._ca.get() != 0) {
      a1.speed_32.set(speed >>> 1);
    }

    //LAB_800f32ac
    if(a1._cc.get() != 0) {
      a1._12a.set(a2 + (byte)a1._cc.get());
    } else {
      a1._12a.set(a2);
    }

    //LAB_800f32d4
    if(a1._ce.get() != 0) {
      a1._12c.set(a3 + (byte)a1._ce.get());
    } else {
      a1._12c.set(a3);
    }

    //LAB_800f32fc
    if(a1._d0.get() != 0) {
      a1._12e.set(t0 + (byte)a1._d0.get());
    } else {
      a1._12e.set(t0);
    }

    //LAB_800f3324
    if(a1._d2.get() != 0) {
      a1._130.set(t1 + (byte)a1._d2.get());
    } else {
      a1._130.set(t1);
    }

    //LAB_800f334c
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
    long s0 = 0;

    //LAB_800f3978
    for(int i = 0; i < 12; i++) {
      long v1 = _800c6b5c.get() + s0;
      if((MEMORY.ref(2, v1).offset(0x2L).getSigned() & 0x8000L) != 0) {
        if(MEMORY.ref(2, v1).offset(0x0L).getSigned() != 0) {
          int v0 = (int)MEMORY.ref(4, v1).offset(0x4L).getSigned();

          if(v0 != -1) {
            final ScriptState<BattleObject27c> state = scriptStatePtrArr_800bc1c0.get(v0).derefAs(ScriptState.classFor(BattleObject27c.class));
            final BattleObject27c data = state.innerStruct_00.deref();

            final short x;
            final short y;
            final short z;
            if((state.ui_60.get() & 0x4L) != 0) {
              x = (short)(-data._78.getZ() * 100);
              y = (short)(-data._78.getY() * 100);
              z = (short)(-data._78.getX() * 100);
            } else {
              //LAB_800f3a3c
              x = 0;
              y = -640;
              z = 0;
            }

            //LAB_800f3a44
            final DVECTOR v0_0 = FUN_800ec7e4(data._148, x, y, z);
            _800c6b5c.deref(4).offset(s0).offset(0x1cL).setu(clampX(v0_0.getX() + centreScreenX_1f8003dc.getSigned()));
            _800c6b5c.deref(4).offset(s0).offset(0x20L).setu(clampY(v0_0.getY() + centreScreenY_1f8003de.getSigned()));
          }

          //LAB_800f3ac8
          long a1 = _800c6b5c.get() + s0;
          long a0 = MEMORY.ref(2, a1).offset(0x0L).getSigned();

          if(a0 == 0x1L) {
            //LAB_800f3b24
            if(MEMORY.ref(4, a1).offset(0x10L).get() == 0x2L) {
              MEMORY.ref(2, a1).offset(0x0L).setu(0x2L);
            } else {
              //LAB_800f3b44
              _800c6b5c.deref(2).offset(s0).setu(0x62L);
            }
          } else if(a0 == 0x2L) {
            //LAB_800f3b50
            for(int n = 0; n < 5; n++) {
              if(MEMORY.ref(2, a1).offset(0x30L).getSigned() == -0x1L) {
                break;
              }

              a0 = MEMORY.ref(4, a1).offset(0x24L).get();

              if((a0 & 0x1L) != 0) {
                if((a0 & 0x2L) != 0) {
                  if((int)MEMORY.ref(4, a1).offset(0x2cL).get() < 0x5L) {
                    MEMORY.ref(2, a1).offset(0x34L).addu(MEMORY.ref(2, a1).offset(0x2cL).get());
                    MEMORY.ref(4, a1).offset(0x2cL).addu(0x1L);
                  }
                } else {
                  //LAB_800f3bb0
                  MEMORY.ref(4, a1).offset(0x24L).oru(0x8002L);
                  MEMORY.ref(4, a1).offset(0x2cL).setu(-0x4L);
                  MEMORY.ref(4, a1).offset(0x28L).setu(MEMORY.ref(2, a1).offset(0x34L).getSigned());
                }
              } else {
                //LAB_800f3bc8
                if(MEMORY.ref(4, a1).offset(0x2cL).get() == MEMORY.ref(4, a1).offset(0x28L).get()) {
                  MEMORY.ref(4, a1).offset(0x24L).oru(0x1L);
                }

                //LAB_800f3be0
                MEMORY.ref(4, a1).offset(0x2cL).addu(0x1L);
              }

              //LAB_800f3bf0
              a1 = a1 + 0x20L;
            }

            //LAB_800f3c00
            a0 = _800c6b5c.get() + s0;

            MEMORY.ref(4, a0).offset(0x14L).subu(0x1L);
            if(MEMORY.ref(4, a0).offset(0x14L).getSigned() <= 0) {
              MEMORY.ref(2, a0).offset(0x0L).setu(0x62L);
              MEMORY.ref(4, a0).offset(0x14L).setu(MEMORY.ref(4, a0).offset(0x18L).get());
            }
          } else if(a0 == 0x61L) {
            //LAB_800f3c34
            if(MEMORY.ref(4, a1).offset(0x14L).getSigned() <= 0) {
              MEMORY.ref(2, a1).offset(0x0L).setu(100);
            } else {
              //LAB_800f3c50
              MEMORY.ref(4, a1).offset(0x14L).subu(0x1L);
              v1 = MEMORY.ref(4, a1).offset(0xcL).get();
              a0 = (v1 - MEMORY.ref(1, a1).offset(0x18L).get()) & 0xffL;
              MEMORY.ref(4, a1).offset(0xcL).setu(v1 & 0xff00_0000L | a0 << 16 | a0 << 8 | a0);
            }
          } else if(a0 == 0x64L) {
            //LAB_800f3d38
            v1 = a1;
            MEMORY.ref(4, v1).offset(0x18L).setu(-0x1L);
            MEMORY.ref(4, v1).offset(0x14L).setu(-0x1L);
            MEMORY.ref(2, v1).offset(0x0L).setu(0);
            MEMORY.ref(2, v1).offset(0x2L).setu(0);
            MEMORY.ref(4, v1).offset(0x4L).setu(-0x1L);
            MEMORY.ref(4, v1).offset(0x8L).setu(0);
            MEMORY.ref(4, v1).offset(0xcL).setu(0x80_8080L);

            //LAB_800f3d60
            for(int n = 0; n < 5; n++) {
              MEMORY.ref(2, v1).offset(0x30L).setu(-0x1L);
              MEMORY.ref(4, v1).offset(0x24L).setu(0);
              MEMORY.ref(4, v1).offset(0x28L).setu(0);
              MEMORY.ref(4, v1).offset(0x2cL).setu(0);
              MEMORY.ref(2, v1).offset(0x40L).setu(0);
              v1 = v1 + 0x20L;
            }
            //LAB_800f3b04
          } else if(a0 < 0x63L) {
            //LAB_800f3c88
            if((MEMORY.ref(2, a1).offset(0x2L).get() & 0x1L) != 0) {
              MEMORY.ref(2, a1).offset(0x0L).setu(0x63L);
            } else {
              //LAB_800f3ca4
              MEMORY.ref(4, a1).offset(0x14L).subu(0x1L);

              if(MEMORY.ref(4, a1).offset(0x14L).getSigned() <= 0) {
                v1 = MEMORY.ref(4, a1).offset(0x10L).get();

                if(v1 > 0 && v1 < 0x3L) {
                  MEMORY.ref(2, a1).offset(0x0L).setu(0x61L);
                  MEMORY.ref(4, a1).offset(0xcL).setu(0x60_6060L);
                  MEMORY.ref(4, a1).offset(0x8L).setu(0x1L);

                  final long a2 = 60 / vsyncMode_8007a3b8.get() / 2;
                  MEMORY.ref(4, a1).offset(0x14L).setu(a2);
                  MEMORY.ref(4, a1).offset(0x18L).setu(96 / a2);
                } else {
                  //LAB_800f3d24
                  //LAB_800f3d2c
                  _800c6b5c.deref(2).offset(s0).setu(0x64L);
                }
              }
            }
          }
        }
      }

      //LAB_800f3d84
      s0 = s0 + 0xc4L;

      //LAB_800f3d88
    }
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
                setGp0_2c(s0);
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
    //LAB_800f41ac
    for(int i = 0; i < charCount_800c677c.get(); i++) {
      final BattleStruct3c s1 = _800c6c40.get(i);

      if(s1.charIndex_00.get() == -1 && _800be5d0.get() == 1) {
        FUN_800ef8d8(i);
      }

      //LAB_800f41dc
    }

    //LAB_800f41f4
    //LAB_800f41f8
    long a1 = _800c6c2c.get();
    long a0 = 0x3fL;

    //LAB_800f4220
    for(int i = 0; i < 3; i++) {
      final BattleStruct3c v1 = _800c6c40.get(i);

      if(v1.charIndex_00.get() != -1) {
        v1._08.set((short)a0);
        MEMORY.ref(2, a1).offset(0x0L).setu(a0);
      }

      //LAB_800f4238
      a1 = a1 + 0x144L;
      a0 = a0 + 0x5eL;
    }
  }

  @Method(0x800f4268L)
  public static void FUN_800f4268(final long s2, final long s3, final long s4) {
    final ScriptState<?> v1 = scriptStatePtrArr_800bc1c0.get((int)s2).deref();
    final BattleObject27c a0 = v1.innerStruct_00.derefAs(BattleObject27c.class);

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
    FUN_800f89f4(s2, 0, 0x2L, s3, clampX(vec.getX() + centreScreenX_1f8003dc.getSigned()), clampY(vec.getY() + centreScreenY_1f8003de.getSigned()), 60 / vsyncMode_8007a3b8.get() / 4, s4);
  }

  @Method(0x800f43dcL)
  public static long FUN_800f43dc(final RunningScript a3) {
    final long a1 = Math.min(charCount_800c677c.get(), 3);

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
    final BattleObject27c struct = scriptStatePtrArr_800bc1c0.get(a3.params_20.get(0).deref().get()).deref().innerStruct_00.derefAs(BattleObject27c.class);
    struct.sp_0a.add((short)a3.params_20.get(1).deref().get());
    _800bc950.offset(a2 * 0x4L).addu(a3.params_20.get(1).deref().get());

    if(struct.sp_0a.get() < struct.all_04.get(1).get() * 100) {
      //LAB_800f44d4
      if(struct.sp_0a.get() < 500) {
        struct.sp_0a.set((short)500);
      }
    } else {
      struct.sp_0a.set((short)(struct.all_04.get(1).get() * 100));
      if(struct.all_04.get(1).get() * 100 >= 500) {
        struct.sp_0a.set((short)500);
      }
    }

    //LAB_800f44ec
    if(struct.sp_0a.get() < 0) {
      struct.sp_0a.set((short)0);
    }

    //LAB_800f4500
    a3.params_20.get(2).deref().set(struct.sp_0a.get());
    return 0;
  }

  @Method(0x800f4518L)
  public static long FUN_800f4518(final RunningScript a0) {
    final long a3 = Math.min(charCount_800c677c.get(), 3);

    //LAB_800f4534
    //LAB_800f454c
    int i;
    for(i = 0; i < a3; i++) {
      if(_8006e398.offset(0xe40L).offset(i * 0x2L).get() == a0.params_20.get(0).deref().get()) {
        break;
      }
    }

    //LAB_800f456c
    final BattleStruct3c a2 = _800c6c40.get(i);
    a2._0c.set((short)0);
    a2._0e.set((short)a0.params_20.get(1).deref().get());

    final long a1 = scriptStatePtrArr_800bc1c0.get(a0.params_20.get(0).deref().get()).deref().innerStruct_00.getPointer(); //TODO
    MEMORY.ref(2, a1).offset(0xaL).subu(a0.params_20.get(2).deref().get());

    if(MEMORY.ref(2, a1).offset(0xaL).getSigned() <= 0) {
      MEMORY.ref(2, a1).offset(0xaL).setu(0);
      a2.flags_06.and(0xfff3);
    }

    //LAB_800f45f8
    return 0;
  }

  @Method(0x800f4600L)
  public static long FUN_800f4600(final RunningScript a0) {
    final short[] sp0x00 = new short[17];
    for(int i = 0; i < sp0x00.length; i++) {
      sp0x00[i] = (short)_800c7124.offset(i * 0x2L).getSigned();
    }

    final BttlStructa4 structa4 = _800c6b60.deref();
    int a2 = structa4._1c.get();
    if(structa4.charIndex_08.get() == 8 && structa4._0a.get() == 0x1L) {
      if(a2 == 0xa) {
        a2 = 0x41;
      }

      //LAB_800f46ec
      if(a2 == 0xb) {
        a2 = 0x42;
      }

      //LAB_800f46f8
      if(a2 == 0xc) {
        a2 = 0x43;
      }
    }

    //LAB_800f4704
    //LAB_800f4708
    a0.params_20.get(0).deref().set(structa4._a0.get());
    a0.params_20.get(1).deref().set(battleMenu_800c6c34.deref()._48.get());
    a0.params_20.get(2).deref().set(a2);

    //LAB_800f4770
    BattleObject27c t0 = null;
    for(int i = 0; i < charCount_800c677c.get(); i++) {
      t0 = scriptStatePtrArr_800bc1c0.get((int)_8006e398.offset(0xe40L).offset(i * 0x4L).get()).deref().innerStruct_00.derefAs(BattleObject27c.class);

      if(t0.charIndex_272.get() == _800c6b60.deref().charIndex_08.get()) {
        break;
      }
    }

    //LAB_800f47ac
    t0._4e.set((short)a2);

    if(structa4._a0.get() == 0x1L && structa4._0a.get() == 0) {
      //LAB_800f47e4
      for(int i = 0; i < 17; i++) {
        if(sp0x00[i] == a2 + 0xc0L) {
          //LAB_800f4674
          a0.params_20.get(1).deref().set(-1);
          break;
        }
      }
    }

    //LAB_800f4800
    //LAB_800f4804
    return 0;
  }

  @Method(0x800f480cL)
  public static long FUN_800f480c(final RunningScript s0) {
    long v0;
    long v1;
    long a0;
    BattleObject27c a1 = null;
    long t0;
    long t5;
    v0 = 0x800c_0000L;
    t5 = v0 + 0x7148L;
    final long[] sp0x10 = new long[8];
    for(int i = 0; i < sp0x10.length; i++) {
      sp0x10[i] = MEMORY.ref(4, t5).offset(i * 0x4L).get();
    }

    t0 = s0.params_20.get(0).deref().get();

    final BattleMenuStruct58 struct58 = battleMenu_800c6c34.deref();

    //LAB_800f489c
    for(a0 = 0; a0 < charCount_800c677c.get(); a0++) {
      a1 = scriptStatePtrArr_800bc1c0.get((int)_8006e398.offset(4, 0xe40L).offset(a0 * 0x4L).get()).deref().innerStruct_00.derefAs(BattleObject27c.class);

      if(struct58.charIndex_04.get() == a1.charIndex_272.get()) {
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
      v1 = struct58._48.get();
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
    final BttlStructa4 v0 = _800c6b60.deref();
    v0._00.set((short)0);
    v0._02.set(0);
    v0._04.set(0);
    v0._06.set(0);
    v0.charIndex_08.set((short)0);
    v0._0a.set((short)0);
    v0._0c.set(0);
    v0._0e.set(0);
    v0._10.set(0);
    v0._12.set(0);
    v0._14.set(0);
    v0._16.set(0x1000);
    v0._18.set((short)0);
    v0._1a.set((short)0);
    v0._1c.set((short)-1);
    v0._22.set((short)0);
    v0._24.set((short)0);
  }

  @Method(0x800f49bcL)
  public static void FUN_800f49bc(final int charIndex, final long a1) {
    final BttlStructa4 a2 = _800c6b60.deref();
    a2._00.set((short)1);
    a2._04.set(0xa0);
    a2._06.set(0x90);
    a2.charIndex_08.set((short)charIndex);
    a2._0a.set((short)(a1 & 1));
    a2._0c.set(0x20);
    a2._0e.set(0x2b);
    a2._10.set(0);
    a2._12.set(0);
    a2._14.set(0x1);
    a2._16.set(0x1000);
    a2._18.set((short)0);
    a2._1a.set((short)0);
    a2._1c.set((short)-1);
    a2._1e.set((short)0);
    a2._20.set((short)0);

    final short v1 = a2._0a.get();

    //LAB_800f4a58
    if(v1 == 0) {
      //LAB_800f4a6c
      //LAB_800f4a7c
      for(int i = 0; i < 32; i++) {
        if(gameState_800babc8.items_2e9.get(i).get() == 0xff) {
          break;
        }
      }

      //LAB_800f4a9c
      FUN_800f83c8();
      a2._22.set((short)_800c6b70.getSigned());
    } else if(v1 == 0x1L) {
      //LAB_800f4abc
      //LAB_800f4ae0
      int v1_0;
      for(v1_0 = 0; v1_0 < charCount_800c677c.get(); v1_0++) {
        if(_800c6960.offset(v1_0 * 0x9L).get() == (a2.charIndex_08.get() & 0xff)) {
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
      a2._22.set((short)i);
    } else if(v1 == 0x2L) {
      //LAB_800f4b4c
      a2._22.set((short)0);
    }

    //LAB_800f4b50
    //LAB_800f4b54
    //LAB_800f4b60
    for(int i = 0; i < 10; i++) {
      MEMORY.ref(4, a2._7c.getAddress()).offset(i * 0x4L).setu(0);
    }
  }

  @Method(0x800f4b80L)
  public static void FUN_800f4b80() {
    long v0;
    long a0;
    long a1;
    int s0;
    BattleObject27c data;
    long s2;
    final BttlStructa4 structa4 = _800c6b60.deref();
    if(structa4._00.get() == 0) {
      return;
    }

    if(structa4._0a.get() != 0) {
      s0 = 0x80;
    } else {
      s0 = 0xba;
    }

    //LAB_800f4bc0
    switch(structa4._00.get() - 1) {
      case 0 -> {
        structa4._90.set(0);
        structa4._a0.set(0);
        structa4._12.set(0);
        structa4._10.set(0);

        if(structa4._0a.get() == 0) {
          structa4._24.set(structa4._26.get());
          structa4._02.or(0x20);
          structa4._1e.set(structa4._28.get());
          structa4._20.set(structa4._2a.get());
          structa4._94.set(structa4._2c.get());

          if(structa4._22.get() - 1 < structa4._24.get() + structa4._1e.get()) {
            structa4._24.decr();

            if(structa4._24.get() < 0) {
              structa4._24.set((short)0);
              structa4._1e.set((short)0);
              structa4._20.set(structa4._1a.get());
              structa4._94.set(0); // This was a3.1a - a3.1a
            }
          }
        } else {
          //LAB_800f4ca0
          structa4._1e.set((short)0);
          structa4._20.set((short)0);
          structa4._94.set(0);
          structa4._24.set(structa4._30.get());
        }

        //LAB_800f4cb4
        structa4._1c.set((short)FUN_800f56c4());
        structa4._00.set((short)7);
        structa4._02.or(0x40);
      }

      case 1 -> {
        structa4._02.and(0xfcff);
        structa4._1c.set((short)FUN_800f56c4());
        if((joypadPress_8007a398.get() & 0x4L) != 0) {
          if(structa4._24.get() != 0) {
            structa4._88.set(2);
            structa4._24.set((short)0);
            structa4._00.set((short)5);
            playSound(0, 1, 0, 0, (short)0, (short)0);
          }

          break;
        }

        //LAB_800f4d54
        if((joypadPress_8007a398.get() & 0x1L) != 0) {
          s0 = structa4._24.get();

          if(structa4._22.get() - 1 >= structa4._1e.get() + 6) {
            structa4._24.set((short)6);
          } else {
            //LAB_800f4d8c
            structa4._24.set((short)(structa4._22.get() - (structa4._1e.get() + 1)));
          }

          //LAB_800f4d90
          structa4._88.set(2);
          structa4._00.set((short)5);

          if(s0 != structa4._24.get()) {
            playSound(0, 1, 0, 0, (short)0, (short)0);
          }

          break;
        }

        //LAB_800f4dc4
        if((joypadPress_8007a398.get() & 0x8L) != 0) {
          if(structa4._1e.get() == 0) {
            break;
          }

          if(structa4._1e.get() < 7) {
            structa4._24.set((short)0);
            structa4._1e.set((short)0);
            structa4._20.set(structa4._1a.get());
          } else {
            //LAB_800f4df4
            structa4._1e.sub((short)7);
            structa4._20.add((short)98);
          }

          //LAB_800f4e00
          structa4._88.set(2);
          structa4._00.set((short)5);
          structa4._94.set(structa4._1a.get() - structa4._20.get());
          playSound(0, 1, 0, 0, (short)0, (short)0);
          break;
        }

        //LAB_800f4e40
        if((joypadPress_8007a398.get() & 0x2L) != 0) {
          if(structa4._1e.get() + 6 >= structa4._22.get() - 1) {
            break;
          }

          structa4._1e.add((short)7);
          structa4._20.sub((short)98);

          if(structa4._1e.get() + 6 >= structa4._22.get() - 1) {
            structa4._24.set((short)0);
          }

          //LAB_800f4e98
          structa4._88.set(2);
          structa4._00.set((short)5);
          structa4._94.set(structa4._1a.get() - structa4._20.get());
          playSound(0, 1, 0, 0, (short)0, (short)0);
          break;
        }

        //LAB_800f4ecc
        if((joypadInput_8007a39c.get() & 0x1000L) != 0) {
          if(structa4._24.get() != 0) {
            structa4._00.set((short)5);
            structa4._24.decr();
            structa4._88.set(2);
          } else {
            //LAB_800f4f18
            if(structa4._1e.get() == 0) {
              break;
            }

            structa4._00.set((short)3);
            structa4._02.or(0x200);
            structa4._80.set(5);
            structa4._7c.set(structa4._20.get());
            structa4._20.add((short)5);
            structa4._1e.decr();
          }

          playSound(0, 1, 0, 0, (short)0, (short)0);
          break;
        }

        //LAB_800f4f74
        if((joypadInput_8007a39c.get() & 0x4000L) != 0) {
          if(structa4._24.get() != structa4._22.get() - 1) {
            if(structa4._1e.get() + structa4._24.get() + 1 < structa4._22.get()) {
              playSound(0, 1, 0, 0, (short)0, (short)0);

              if(structa4._24.get() != 6) {
                structa4._24.incr();
                structa4._88.set(2);
                structa4._00.set((short)5);
              } else {
                //LAB_800f4ff8
                structa4._80.set(-5);
                structa4._00.set((short)4);
                structa4._7c.set(structa4._20.get());
                structa4._20.sub((short)5);
                structa4._1e.incr();
                structa4._02.or(0x100);
              }
            }
          }

          break;
        }

        //LAB_800f5044
        structa4._90.set(0);

        if((joypadPress_8007a398.get() & 0x20L) != 0) {
          //LAB_800f5078
          int charSlot = 0;
          do {
            s2 = _8006e398.offset(0xe40L).offset(charSlot * 0x4L).get();
            data = scriptStatePtrArr_800bc1c0.get((int)s2).deref().innerStruct_00.derefAs(BattleObject27c.class);

            if(structa4.charIndex_08.get() == data.charIndex_272.get()) {
              //LAB_800f503c
              _800c6980.setu(charSlot);
              break;
            }

            charSlot++;
          } while(charSlot < charCount_800c677c.get());

          //LAB_800f50b8
          if(structa4._0a.get() == 0) {
            data._52.set(structa4._1c.get());
            FUN_800f7a74(s2);

            if((data._d4.get() & 0x4L) != 0) {
              _800c6b68.setu(0x1L);
            } else {
              //LAB_800f5100
              _800c6b68.setu(0);
            }

            //LAB_800f5108
            if((data._d4.get() & 0x2L) != 0) {
              _800c69c8.setu(0x1L);
            } else {
              //LAB_800f5128
              _800c69c8.setu(0);
            }
          } else {
            //LAB_800f5134
            final long s1 = FUN_800f9e50(structa4._1c.get());

            if(MEMORY.ref(2, s1).offset(0xcL).getSigned() < MEMORY.ref(2, s1).offset(0xa0L).getSigned()) {
              //LAB_800f5160
              //LAB_800f5168
              playSound(0, 3, 0, 0, (short)0, (short)0);
              break;
            }

            //LAB_800f517c
            v0 = _800c6b5c.get();
            MEMORY.ref(2, v0).offset(0x0L).setu(0);
            MEMORY.ref(2, v0).offset(0x2L).setu(0);
          }

          //LAB_800f5190
          playSound(0, 2, 0, 0, (short)0, (short)0);
          structa4._8c.set(0);
          structa4._02.or(0x4);
          if(structa4._0a.get() == 0) {
            structa4._94.set(structa4._1a.get() - structa4._20.get());
          }

          //LAB_800f51e8
          structa4._00.set((short)6);
          structa4._02.and(0xfffd);
          break;
        }

        //LAB_800f5208
        if((joypadPress_8007a398.get() & 0x40L) != 0) {
          playSound(0, 3, 0, 0, (short)0, (short)0);
          structa4._00.set((short)8);
          structa4._02.and(0xfff7);
        }
      }

      case 2 -> {
        s0 = structa4._80.get();
        structa4._90.incr();
        if(structa4._90.get() >= 0x3L) {
          s0 = s0 * 2;
        }

        //LAB_800f5278
        a1 = structa4._7c.get() + 14;
        structa4._20.add((short)s0);
        if(structa4._20.get() >= a1) {
          structa4._20.set((short)a1);
          structa4._00.set((short)2);
        }
      }

      case 3 -> {
        s0 = structa4._80.get();
        structa4._90.incr();
        if(structa4._90.get() >= 3) {
          s0 = s0 * 2;
        }

        //LAB_800f52d4
        a1 = structa4._7c.get() - 14;
        structa4._20.add((short)s0);
        if(structa4._20.get() <= a1) {
          //LAB_800f5300
          structa4._20.set((short)a1);
          structa4._00.set((short)2);
        }
      }

      case 4 -> {
        s0 = structa4._88.get();
        structa4._90.incr();
        if(structa4._90.get() >= 3) {
          s0 = s0 / 2;
        }

        //LAB_800f5338
        if(s0 <= 1) {
          structa4._00.set((short)2);
        }
      }

      case 5 -> {
        structa4._a0.set(0);
        structa4._1c.set((short)FUN_800f56c4());

        //LAB_800f538c
        int charSlot = 0;
        do {
          data = scriptStatePtrArr_800bc1c0.get((int)_8006e398.offset(0xe40L).offset(charSlot * 0x4L).get()).deref().innerStruct_00.derefAs(BattleObject27c.class);

          if(structa4.charIndex_08.get() == data.charIndex_272.get()) {
            break;
          }

          charSlot++;
        } while(charSlot < charCount_800c677c.get());

        //LAB_800f53c8
        if(structa4._0a.get() == 0) {
          a0 = _800c6b68.get();
          a1 = _800c69c8.get();
        } else {
          //LAB_800f53f8
          a1 = data._94.get();
          a0 = (a1 & 0x40L) > 0 ? 1 : 0;
          a1 = (a1 & 0x08L) > 0 ? 1 : 0;
        }

        //LAB_800f5410
        s0 = (int)FUN_800f7768(a0, a1);
        if(s0 == 0x1L) {
          if(structa4._0a.get() == 0) {
            FUN_800232dc(((structa4._1c.get() & 0xff) - 0x40L) & 0xffL);
          }

          //LAB_800f545c
          if(structa4._0a.get() == 0x1L) {
            data.mp_0c.sub(data._a0.get());
          }

          //LAB_800f5488
          playSound(0, 2, 0, 0, (short)0, (short)0);
          structa4._a0.set(1);
          structa4._00.set((short)9);
        } else if(s0 != 0) {
          //LAB_800f54b4
          playSound(0, 0, 3, 0, (short)0, (short)0);
          structa4._00.set((short)7);
          structa4._02.and(0xfffb).or(0x20);
        }
      }

      case 6 -> {
        structa4._00.set((short)2);
        playSound(0, 4, 0, 0, (short)0, (short)0);
        structa4._12.set(0x52);
        structa4._10.set(s0);
        structa4._18.set((short)(structa4._04.get() - s0 / 2 + 9));
        v0 = (structa4._06.get() - structa4._12.get()) - 16;
        structa4._1a.set((short)v0);
        structa4._20.set((short)v0);
        structa4._02.or(0xb);
        if((structa4._02.get() & 0x20L) != 0) {
          v0 = v0 - structa4._94.get();
          structa4._20.set((short)v0);
        }

        //LAB_800f5588
        if(structa4._0a.get() != 0) {
          structa4._1c.set((short)FUN_800f56c4());
          v0 = FUN_800f9e50(structa4._1c.get());
          FUN_800f3354(0, 0x1L, 0, MEMORY.ref(2, v0).offset(0xa0L).getSigned(), 0x118L, 0x87L, 0, 0x1L);
        }
      }

      case 7 -> {
        _800c69c8.setu(0);
        a0 = _800c6b5c.get();
        _800c6b68.setu(0);
        structa4._a0.set(-1);
        structa4._00.set((short)9);
        structa4._12.set(0);
        structa4._10.set(0);
        structa4._02.and(0xfffc);
        MEMORY.ref(2, a0).offset(0x0L).setu(0);
        MEMORY.ref(2, a0).offset(0x2L).setu(0);
      }

      case 8 -> {
        if(structa4._0a.get() == 0) {
          v0 = structa4._1a.get() - structa4._20.get();
          structa4._26.set(structa4._24.get());
          structa4._28.set(structa4._1e.get());
          structa4._2a.set(structa4._20.get());
          structa4._94.set(v0);
          structa4._2c.set(v0);
        }

        //LAB_800f568c
        FUN_800f4964();
      }
    }

    //LAB_800f5694
    //LAB_800f5698
    structa4._84.set(_800bb0fc.get() & 0x7L);

    //LAB_800f56ac
  }

  @Method(0x800f56c4L)
  public static long FUN_800f56c4() {
    final BttlStructa4 a1 = _800c6b60.deref();
    final long v1 = a1._0a.get();

    long a0;
    if(v1 == 0) {
      //LAB_800f56f0
      a0 = _800c6988.offset((a1._24.get() + a1._1e.get()) * 0x2L).get() - 0xc0L;
    } else if(v1 == 0x1L) {
      //LAB_800f5718
      //LAB_800f5740
      int a3;
      for(a3 = 0; a3 < charCount_800c677c.get(); a3++) {
        if(_800c6960.offset(a3 * 9).get() == (a1.charIndex_08.get() & 0xff)) {
          break;
        }
      }

      //LAB_800f5778
      final BttlStructa4 a2 = _800c6b60.deref();
      a0 = _800c6960.offset(a2._24.get() + a2._1e.get() + (short)a3 * 9 + 1).get();
      if(a2.charIndex_08.get() == 8) {
        if(a0 == 0x41L) {
          a0 = 0xaL;
        }

        //LAB_800f57d4
        if(a0 == 0x42L) {
          a0 = 0xbL;
        }

        //LAB_800f57e0
        if(a0 == 0x43L) {
          a0 = 0xcL;
        }
      }
    } else {
      throw new RuntimeException("Undefined a0");
    }

    //LAB_800f57ec
    //LAB_800f57f0
    return a0;
  }

  @Method(0x800f57f8L)
  public static void FUN_800f57f8(long a0) {
    long v0;
    long a1;
    long s6;
    long sp70;

    final BttlStructa4 structa4 = _800c6b60.deref();

    long spa8 = a0;
    long s5 = structa4._20.get();
    long sp60 = structa4._1a.get();
    long sp68 = structa4._06.get();

    //LAB_800f5860
    for(sp70 = 0; sp70 < charCount_800c677c.get(); sp70++) {
      if(_800c6960.offset(sp70 * 9).get() == (structa4.charIndex_08.get() & 0xff)) {
        break;
      }
    }

    //LAB_800f58a4
    long s7 = 0x1L;
    long sp7c = 0;

    final LodString sp0x18 = new LodString(18);
    final LodString sp0x40 = new LodString(5);
    final LodString sp0x48 = new LodString(12);

    //LAB_800f58e0
    for(s6 = 0; s6 < structa4._22.get(); s6++) {
      if(s5 >= sp68) {
        break;
      }

      long s2 = 0;
      LodString s3;
      if(spa8 == 0) {
        //LAB_800f5918
        s3 = _80050ae8.get((int)(_800c6988.offset(sp7c).get() - 0xc0)).deref();
        FUN_800297a0(_800c6988.offset(s7).get(), sp0x48);

        //LAB_800f5968
        int i;
        for(i = 0; ; i++) {
          sp0x18.charAt(i, s3.charAt(i));
          if(s3.charAt(i) == 0xa0ff) {
            break;
          }
        }

        //LAB_800f5990
        //LAB_800f59a4
        for(; i < 16; i++) {
          sp0x18.charAt(i, 0);
        }

        //LAB_800f59bc
        if(_800c6988.offset(s7).get() < 10) {
          sp0x18.charAt(i, 0);
          i++;
        }

        //LAB_800f59e8
        sp0x18.charAt(i, 0xa0ff);
        sp0x40.charAt(0, 0xe);

        //LAB_800f5a10
        for(a1 = 0; a1 < 2; a1++) {
          v0 = sp0x48.charAt((int)a1);
          if(v0 == 0xa0ffL) {
            break;
          }

          sp0x40.charAt((int)(a1 + 1), v0);
        }

        //LAB_800f5a38
        sp0x40.charAt((int)(a1 + 1), 0xa0ff);
      } else if(spa8 == 0x1L) {
        //LAB_800f5a4c
        a0 = _800c6960.offset(s6 + sp70 * 9 + 1).get();
        s3 = spells_80052734.get((int)a0).deref();

        if(structa4.charIndex_08.get() == 8) {
          if(a0 == 0x41L) {
            a0 = 0xaL;
          }

          //LAB_800f5ab4
          if(a0 == 0x42L) {
            a0 = 0xbL;
          }

          //LAB_800f5ac0
          if(a0 == 0x43L) {
            a0 = 0xcL;
          }
        }

        //LAB_800f5acc
        v0 = FUN_800f9e50(a0);

        if(MEMORY.ref(2, v0).offset(0xcL).getSigned() < MEMORY.ref(2, v0).offset(0xa0L).getSigned()) {
          s2 = 0xaL;
        }
      } else {
        throw new RuntimeException("Undefined s3");
      }

      //LAB_800f5af0
      if(s5 >= sp60) {
        //LAB_800f5b90
        if(sp68 >= s5 + 0xcL) {
          a1 = 0;
        } else {
          a1 = s5 - (sp68 - 0xcL);
        }

        //LAB_800f5bb4
        if((structa4._02.get() & 0x4L) != 0) {
          a1 = (short)structa4._8c.get();
        }

        //LAB_800f5bd8
        renderText(s3, structa4._18.get(), s5, s2, a1);

        if(spa8 == 0) {
          s3 = sp0x40;
          renderText(s3, structa4._18.get() + 0x80, s5, s2, a1);
        }
      } else if(sp60 < s5 + 0xcL) {
        if((structa4._02.get() & 0x4L) != 0) {
          a1 = structa4._8c.get();
        } else {
          a1 = s5 - sp60;
        }

        //LAB_800f5b40
        renderText(s3, structa4._18.get(), sp60, s2, a1);

        if(spa8 == 0) {
          s3 = sp0x40;
          renderText(s3, structa4._18.get() + 0x80, sp60, s2, a1);
        }
      }

      //LAB_800f5c38
      s5 = s5 + 0xeL;
      sp7c = sp7c + 0x2L;
      s7 = s7 + 0x2L;
    }

    //LAB_800f5c64
  }

  /** TODO use item menu */
  @Method(0x800f5c94L)
  public static void FUN_800f5c94() {
    final BttlStructa4 structa4 = _800c6b60.deref();

    if(structa4._00.get() != 0 && (structa4._02.get() & 0x1L) != 0) {
      if((structa4._02.get() & 0x2L) != 0) {
        FUN_800f57f8(structa4._0a.get());

        if((structa4._02.get() & 0x8L) != 0) {
          //LAB_800f5d78
          //LAB_800f5d90
          FUN_800f8cd8(structa4._18.get() - centreScreenX_1f8003dc.get() - 0x10L, structa4._1a.get() - centreScreenY_1f8003de.get() + structa4._24.get() * 0xeL + 0x2L, structa4._84.get() % 4 * 0x10L + 0xc0L & 0xf0L, structa4._84.get() / 4 * 0x8L + 0x20L & 0xf8L, 0xfL, 0x8L, 0xdL, 0x1L);

          long s0;
          if(structa4._0a.get() != 0) {
            s0 = 0;
          } else {
            s0 = 0x1aL;
          }

          //LAB_800f5e00
          long s1;
          if((structa4._02.get() & 0x100L) != 0) {
            s1 = 0x2L;
          } else {
            s1 = 0;
          }

          //LAB_800f5e18
          long t0;
          if((structa4._02.get() & 0x200L) != 0) {
            t0 = -0x2L;
          } else {
            t0 = 0;
          }

          //LAB_800f5e24
          if(structa4._1e.get() > 0) {
            FUN_800f74f4(_800c7190.getAddress(), structa4._04.get() + s0 + 0x38L, structa4._06.get() + t0 - 0x64L, _800c7192.get(), _800c7193.get(), 0xdL, -0x1L, 0);
          }

          //LAB_800f5e7c
          if(structa4._1e.get() + 6 < structa4._22.get() - 1) {
            FUN_800f74f4(_800c7190.getAddress(), structa4._04.get() + s0 + 0x38L, structa4._06.get() + s1 - 0x7L, _800c7192.get(), _800c7193.get(), 0xdL, -0x1L, 0x1L);
          }
        }

        //LAB_800f5ee8
        long a2 = structa4._10.get() + 6;
        long a3 = structa4._12.get() + 0x11L;
        FUN_800f1268(structa4._04.get() - a2 / 2, structa4._06.get() - a3, a2, a3, 0x8L);
      }

      //LAB_800f5f50
      if((structa4._02.get() & 0x40L) != 0) {
        long s1;
        if(structa4._0a.get() == 0) {
          //LAB_800f5f8c
          s1 = 0x4L;
        } else if(structa4._0a.get() == 0x1L) {
          //LAB_800f5f94
          s1 = 0x5L;
          if((structa4._02.get() & 0x2L) != 0) {
            long v0 = FUN_800f9e50(structa4._1c.get());
            FUN_800f3354(0, 0x1L, 0, MEMORY.ref(2, v0).offset(0xa0L).getSigned(), 0x118L, 0x87L, 0, structa4._0a.get());
            FUN_800f8cd8(236 - centreScreenX_1f8003dc.get(), 130 - centreScreenY_1f8003de.get(), 0x10L, 0x80L, 0x18L, 0x10L, 0x2cL, -0x1L);
            FUN_800f1268(0xecL, 0x82L, 0x40L, 0xeL, 0x8L);
          }
        } else {
          throw new RuntimeException("Undefined s1");
        }

        //LAB_800f604c
        //LAB_800f6050
        FUN_800f1268(0x2cL, 0x9cL, 0xe8L, 0xeL, 0x8L);
        FUN_800f8ac4((short)s1, structa4._1c.get(), 160, 163);
      }
    }

    //LAB_800f6088
  }

  @Method(0x800f60acL)
  public static void FUN_800f60ac() {
    final BattleMenuStruct58 v0 = battleMenu_800c6c34.deref();
    v0._00.set((short)0);
    v0._02.set(0);
    v0.charIndex_04.set((short)0xff);
    v0.x_06.set((short)0);
    v0.y_08.set((short)0);
    v0._0a.set((short)0);
    v0._0c.set((short)0);
    v0.iconCount_0e.set((short)0);
    v0.selectedIcon_22.set((short)0);
    v0._24.set((short)0);
    v0._26.set((short)0);
    v0._28.set((short)0);
    v0._2a.set((short)0);
    v0.colour_2c.set((short)0);

    //LAB_800f60fc
    for(int i = 0; i < 9; i++) {
      v0.iconFlags_10.get(i).set((short)-1);
    }

    //LAB_800f611c
    for(int i = 0; i < 10; i++) {
      v0.all_30.get(i).set(0);
    }
  }

  @Method(0x800f6134L)
  public static void FUN_800f6134(final long a0, final long a1, final long a2) {
    long v1;
    long t0;
    long t5;
    v1 = 0x800c_0000L;
    t5 = v1 + 0x7194L;

    final long[] sp0x10 = new long[8];
    for(int i = 0; i < 8; i++) {
      sp0x10[i] = MEMORY.ref(2, t5).offset(i * 0x2L).get();
    }

    final BattleMenuStruct58 v0 = battleMenu_800c6c34.deref();
    v0._00.set((short)1);
    v0._02.set(2);
    v0.x_06.set((short)160);
    v0.y_08.set((short)172);
    v0.selectedIcon_22.set((short)0);
    v0._24.set((short)0);
    v0._26.set((short)0);
    v0._28.set((short)0);
    v0._2a.set((short)0);
    v0.colour_2c.set((short)128);

    //LAB_800f61d8
    for(int i = 0; i < 9; i++) {
      v0.iconFlags_10.get(i).set((short)-1);
    }

    //LAB_800f61f8
    for(int i = 0; i < 10; i++) {
      v0.all_30.get(i).set(0);
    }

    t0 = charCount_800c677c.get();

    if((int)t0 >= 0x4L) {
      t0 = 0x3L;
    }

    //LAB_800f6224
    //LAB_800f6234
    int a3;
    for(a3 = 0; a3 < t0; a3++) {
      if(_8006e398.offset(0xe40L).offset(a3 * 0x4L).get() == a0) {
        break;
      }
    }

    //LAB_800f6254
    v0.iconCount_0e.set((short)0);
    v0.charIndex_04.set(scriptStatePtrArr_800bc1c0.get((int)_8006e398.offset(a3 * 0x4L).offset(4, 0xe40L).get()).deref().innerStruct_00.derefAs(BattleObject27c.class).charIndex_272.get());

    //LAB_800f62a4
    int a0_0 = 0;
    for(int i = 0; i < 8; i++) {
      if((a1 & 1 << i) != 0) {
        v0.iconFlags_10.get(a0_0).set((short)sp0x10[i]);
        v0.iconCount_0e.incr();
        a0_0++;
      }

      //LAB_800f62d0
    }

    v0._0c.set((short)0);
    v0._0a.set((short)((v0.iconCount_0e.get() * 19 - 3) / 2));
    FUN_800f8b74(a2);
  }

  @Method(0x800f6330L)
  public static int FUN_800f6330() {
    long v1;
    long a0;
    long a1;
    long a2;
    long s1;
    final BattleMenuStruct58 struct58 = battleMenu_800c6c34.deref();

    if(struct58._00.get() == 0) {
      return 0;
    }

    s1 = 0;

    switch(struct58._00.get() - 1) {
      case 0 -> {
        struct58._00.set((short)2);
        struct58._28.set((short)(struct58.x_06.get() - struct58._0a.get() + struct58.selectedIcon_22.get() * 19 - 4));
        struct58._2a.set((short)(struct58.y_08.get() - 22));

        //LAB_800f63e8
        for(int i = 0; i < 10; i++) {
          struct58.all_30.get(i).set(0);
        }

        _800c697c.setu(0);
        _800c6ba1.setu(0);
        _800c6ba0.setu(0);

        //LAB_800f6424
        final long[] sp0x18 = new long[4];
        for(int i = 0; i < 4; i++) {
          sp0x18[i] = 0xffL;
          _800c6c30.offset(i).setu(0);
        }

        //LAB_800f6458
        for(a2 = 0; a2 < 4; a2++) {
          a0 = 0;
          a1 = _800c6718.offset(1, 0x18L).offset(a2 * 0x4L).get();

          //LAB_800f646c
          for(int i = 0; i < 4; i++) {
            if(sp0x18[i] == a1) {
              a0 = 0x1L;
              break;
            }

            //LAB_800f6480
          }

          if(a0 == 0) {
            sp0x18[(int)_800c6ba0.get()] = _800c6718.offset(1, 0x18L).offset(a2 * 0x4L).get();
            _800d6c30.offset(_800c6ba0.get()).setu(a2);

            if(_800d66b0.get() == a2) {
              _800c6ba1.setu(_800c6ba0.get());
            }

            //LAB_800f64dc
            _800c6ba0.addu(0x1L);
          }

          //LAB_800f64ec
        }
      }

      case 1 -> {
        a0 = _800c6ba0.get();
        struct58._40.set(0);
        struct58._44.set(0);
        if(a0 >= 0x2L && (joypadInput_8007a39c.get() & 0x2L) != 0) {
          _800c6ba1.addu(0x1L);
          if(_800c6ba1.get() >= a0) {
            _800c6ba1.setu(0);
          }

          //LAB_800f6560
          _800c6748.setu(0x21L);
          struct58._00.set((short)5);
          _800c66b0.setu(_800c6c30.offset(_800c6ba1.get()).get());
          struct58._44.set(60 / vsyncMode_8007a3b8.get() + 2);
          FUN_800f8c38(0);
          break;
        }

        //LAB_800f65b8
        if((joypadInput_8007a39c.get() & 0x2000L) != 0) {
          playSound(0, 1, 0, 0, (short)0, (short)0);

          if(struct58.selectedIcon_22.get() < struct58.iconCount_0e.get() - 1) {
            //LAB_800f6640
            struct58.selectedIcon_22.incr();
            struct58._00.set((short)3);

            //LAB_800f664c
            struct58._30.set(3);
            struct58._34.set(19);
            struct58._38.set(0);
            struct58._26.set((short)0);
            break;
          }

          struct58._00.set((short)4);
          struct58._02.or(1);
          struct58.selectedIcon_22.set((short)0);
          struct58._26.set((short)0);
          struct58._30.set(3);
          struct58._34.set(19);
          struct58._38.set(0);
          struct58._3c.set(struct58.x_06.get() - struct58._0a.get() - 23);
          break;
        }

        //LAB_800f6664
        if((joypadInput_8007a39c.get() & 0x8000L) != 0) {
          playSound(0, 1, 0, 0, (short)0, (short)0);

          if(struct58.selectedIcon_22.get() != 0) {
            //LAB_800f66f0
            struct58.selectedIcon_22.decr();
            struct58._00.set((short)3);

            //LAB_800f66fc
            struct58._30.set(3);
            struct58._34.set(-19);

            //LAB_800f6710
            struct58._38.set(0);
            struct58._26.set((short)0);
            break;
          }

          struct58._00.set((short)4);
          struct58._02.or(1);
          struct58.selectedIcon_22.set((short)(struct58.iconCount_0e.get() - 1));
          struct58._3c.set(struct58.x_06.get() - struct58._0a.get() + struct58.iconCount_0e.get() * 19 - 4);
          struct58._30.set(3);
          struct58._34.set(-19);
          struct58._38.set(0);
          struct58._26.set((short)0);
          break;
        }

        //LAB_800f671c
        if((joypadPress_8007a398.get() & 0x20L) != 0) {
          v1 = struct58.iconFlags_10.get(struct58.selectedIcon_22.get()).get();
          if((v1 & 0x80L) != 0) {
            playSound(0, 3, 0, 0, (short)0, (short)0);
          } else {
            v1 = v1 & 0xfL;
            if(v1 != 0x5L) {
              //LAB_800f6790
              if(v1 != 0x3L) {
                //LAB_800f6858
                //LAB_800f6860
                playSound(0, 2, 0, 0, (short)0, (short)0);
                s1 = struct58.iconFlags_10.get(struct58.selectedIcon_22.get()).get() & 0xfL;
              } else {
                //LAB_800f67b8
                for(a2 = 0; a2 < charCount_800c677c.get(); a2++) {
                  if(_800c6960.offset(a2 * 0x9L).get() == (struct58.charIndex_04.get() & 0xff)) {
                    break;
                  }
                }

                //LAB_800f67d8
                //LAB_800f67f4
                for(a0 = 1; a0 < 9; a0++) {
                  if(_800c6960.offset(a2 * 9).offset(a0).get() != 0xffL) {
                    break;
                  }
                }

                //LAB_800f681c
                if(a0 == 0x9L) {
                  playSound(0, 3, 0, 0, (short)0, (short)0);
                } else {
                  playSound(0, 2, 0, 0, (short)0, (short)0);
                  s1 = struct58.iconFlags_10.get(struct58.selectedIcon_22.get()).get() & 0xfL;
                }
              }
            } else {
              FUN_800f83c8();

              if(_800c6b70.getSigned() == 0) {
                playSound(0, 3, 0, 0, (short)0, (short)0);
              } else {
                playSound(0, 2, 0, 0, (short)0, (short)0);
                s1 = struct58.iconFlags_10.get(struct58.selectedIcon_22.get()).get() & 0xfL;
              }
            }
          }
          //LAB_800f6898
        } else if((joypadPress_8007a398.get() & 0x40L) != 0) {
          //LAB_800f68a4
          //LAB_800f68bc
          playSound(0, 3, 0, 0, (short)0, (short)0);
        }

        //LAB_800f68c4
        //LAB_800f68c8
        struct58._40.set(0x1L);
      }

      case 2 -> {
        struct58._38.incr();
        struct58._28.add((short)(struct58._34.get() / struct58._30.get()));

        if(struct58._38.get() >= struct58._30.get()) {
          struct58._00.set((short)2);
          struct58._30.set(0);
          struct58._34.set(0);
          struct58._38.set(0);
          struct58._28.set((short)(struct58.x_06.get() - struct58._0a.get() + struct58.selectedIcon_22.get() * 19 - 4));
          struct58._2a.set((short)(struct58.y_08.get() - 22));
        }
      }

      case 3 -> {
        struct58._38.incr();
        struct58._28.add((short)(struct58._34.get() / struct58._30.get()));
        struct58._3c.add(struct58._34.get() / struct58._30.get());
        struct58.colour_2c.add((short)(0x80 / struct58._30.get()));

        if(struct58._38.get() >= struct58._30.get()) {
          struct58._00.set((short)2);
          struct58.colour_2c.set((short)0x80);
          struct58._38.set(0);
          struct58._34.set(0);
          struct58._30.set(0);
          struct58._28.set((short)(struct58.x_06.get() - struct58._0a.get() + struct58.selectedIcon_22.get() * 19 - 4));
          struct58._2a.set((short)(struct58.y_08.get() - 22));
          struct58._02.and(0xfffe);
        }
      }

      case 4 -> {
        struct58._44.decr();
        if(struct58._44.get() == 1) {
          FUN_800f8c38(0x1L);
          struct58._00.set((short)2);
        }
      }
    }

    //LAB_800f6a88
    //LAB_800f6a8c
    struct58._24.incr();
    if(struct58._24.get() >= 4) {
      struct58._24.set((short)0);
      struct58._26.incr();
      if(struct58._26.get() >= 4) {
        struct58._26.set((short)0);
      }
    }

    //LAB_800f6ae0
    FUN_800f6b04();

    //LAB_800f6aec
    return (int)s1;
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
    long s6;
    long s7;
    long fp;
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

    final BattleMenuStruct58 menu = battleMenu_800c6c34.deref();
    if(menu._00.get() != 0 && (menu._02.get() & 0x2L) != 0) {
      //LAB_800f6c48
      for(int iconIndex = 0; iconIndex < menu.iconCount_0e.get(); iconIndex++) {
        fp = (menu.iconFlags_10.get(iconIndex).get() & 0xfL) - 0x1L;
        if(menu.selectedIcon_22.get() == iconIndex) {
          s6 = sp0x60[menu._26.get()];
        } else {
          //LAB_800f6c88
          s6 = 0;
        }

        //LAB_800f6c90
        s3 = menu.x_06.get() - menu._0a.get() + iconIndex * 19 - centreScreenX_1f8003dc.get();
        s4 = menu.y_08.get() - _800fb6bc.offset(2, fp * 6 + s6 * 2).get() - centreScreenY_1f8003de.get();
        if((menu.iconFlags_10.get(iconIndex).get() & 0x80L) != 0) {
          // "X" icon over attack icon if attack is disabled
          FUN_800f8cd8(s3, menu.y_08.get() - (centreScreenY_1f8003de.get() + 0x10L), 0x60L, 0x70L, 0x10L, 0x10L, 0x19L, -0x1L);
        }

        //LAB_800f6d70
        if((menu.iconFlags_10.get(iconIndex).get() & 0xfL) != 0x2L) {
          //LAB_800f6e24
          s0 = _800fb674.offset(fp * 0x8L).offset(2, 0x4L).get();
        } else if(menu.charIndex_04.get() == 0 && (gameState_800babc8.dragoonSpirits_19c.get(0).get() & 0xffL) >>> 7 != 0) {
          s0 = sp0x48[9];
          if(s6 != 0) {
            //LAB_800f6de0
            FUN_800f8cd8(s3 + 0x4L, s4, s6 != 0x1L ? 0x58L : 0x50L, 0x70L, 0x8L, 0x10L, 0x98L, 0x1L);
          }
        } else {
          s0 = sp0x48[menu.charIndex_04.get()];
        }

        //LAB_800f6e34
        //LAB_800f6e38
        //LAB_800f6e3c
        t1 = _800fb674.offset(fp * 8).getAddress();
        v1 = s6 * 0x2L + fp * 0x6L;
        t0 = _800fb6f4.offset(v1).getAddress();
        v1 = _800fb6bc.offset(v1).getAddress();
        // Combat menu icons
        FUN_800f8cd8(s3, s4, MEMORY.ref(1, t1).offset(0x0L).get(), MEMORY.ref(1, t1).offset(0x2L).get() + MEMORY.ref(1, t0).offset(0x0L).get() & 0xffL, 0x10L, MEMORY.ref(2, v1).getSigned(), s0, MEMORY.ref(2, t1).offset(0x6L).getSigned());

        if(menu.selectedIcon_22.get() == iconIndex && menu._40.get() == 0x1L) {
          t1 = _800fb72c.offset(fp * 8).getAddress();
          a0 = menu.x_06.get() - menu._0a.get() + iconIndex * 19 - centreScreenX_1f8003dc.get() - MEMORY.ref(2, t1).offset(0x4L).get() / 2 + 0x8L;
          a1 = menu.y_08.get() - centreScreenY_1f8003de.get() - 0x18L;
          // Selected combat menu icon text
          FUN_800f8cd8(a0, a1, MEMORY.ref(1, t1).offset(0x0L).get(), MEMORY.ref(1, t1).offset(0x2L).get(), MEMORY.ref(2, t1).offset(0x4L).get(), 0x8L, MEMORY.ref(2, t1).offset(0x6L).getSigned(), -0x1L);
        }

        //LAB_800f6fa4
      }

      //LAB_800f6fc8
      FUN_800f7210(menu._28.get(), menu._2a.get(), sp0x30, 0x1fL, 0xcL, 0x1L, menu.colour_2c.get());

      if((menu._02.get() & 0x1L) != 0) {
        FUN_800f7210(menu._3c.get(), menu._2a.get(), sp0x30, 0x1fL, 0xcL, 0x1L, 0x80 - menu.colour_2c.get());
      }

      //LAB_800f704c
      s0 = menu.iconCount_0e.get() * 19 + 1;
      s1 = menu.x_06.get() - s0 / 2;
      s2 = menu.y_08.get() - 0xaL;
      FUN_800f74f4(_800fb5dc.getAddress(), s1, s2, s0, 0x2L, 0x2bL, 0x1L, _800fb5dc.offset(1, 0x4L).get());

      final long[] sp0x20 = new long[4];
      final long[] sp0x28 = new long[4];

      sp0x20[0] = s1;
      sp0x20[2] = s1;
      s1 = s1 + s0;
      s3 = menu.y_08.get() - 0x8L;
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
      for(int i = 0; i < 8; i++) {
        t0 = s6 + i * 0xcL;
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
        FUN_800f74f4(fp + i * 0x6L, (short)a1, (short)t2, (short)a3, (short)v1, 0x2bL, 0x1L, MEMORY.ref(1, s7).offset((i + 0x1L) * 0x6L).offset(0x4L).get());
      }
    }

    //LAB_800f71e0
  }

  @Method(0x800f7210L)
  public static void FUN_800f7210(final long x, final long y, final long[] a2, final long a3, final long a4, final long transparencyMode, final long colour) {
    long v0;
    long v1;
    final long s0 = linkedListAddress_1f8003d8.get();
    linkedListAddress_1f8003d8.addu(0x28L);
    setGp0_2c(s0);
    final long s5;
    if((int)transparencyMode == -1) {
      gpuLinkedListSetCommandTransparency(s0, false);
      s5 = 0;
    } else {
      //LAB_800f728c
      gpuLinkedListSetCommandTransparency(s0, true);
      s5 = transparencyMode;
    }

    //LAB_800f7294
    MEMORY.ref(1, s0).offset(0x6L).setu(colour);
    MEMORY.ref(1, s0).offset(0x5L).setu(colour);
    MEMORY.ref(1, s0).offset(0x4L).setu(colour);
    v1 = a2[0] + x - centreScreenX_1f8003dc.get();
    MEMORY.ref(2, s0).offset(0x18L).setu(v1);
    MEMORY.ref(2, s0).offset(0x8L).setu(v1);
    v1 = a2[2] + a2[0] + x - centreScreenX_1f8003dc.get();
    MEMORY.ref(2, s0).offset(0x20L).setu(v1);
    MEMORY.ref(2, s0).offset(0x10L).setu(v1);
    v1 = a2[1] + y - centreScreenY_1f8003de.get();
    MEMORY.ref(2, s0).offset(0x12L).setu(v1);
    MEMORY.ref(2, s0).offset(0xaL).setu(v1);
    v1 = a2[3] + a2[1] + y - centreScreenY_1f8003de.get();
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
    MEMORY.ref(2, s0).offset(0x16L).setu(GetTPage(0, s5, 704, 256));
    insertElementIntoLinkedList(tags_1f8003d0.getPointer() + a3 * 0x4L, s0);
  }

  /** Background of battle menu icons */
  @Method(0x800f74f4L)
  public static void FUN_800f74f4(long a0, long x, long y, long w, long h, long a5, long a6, long a7) {
    final long s0 = linkedListAddress_1f8003d8.get();
    linkedListAddress_1f8003d8.addu(0x28L);
    setGp0_2c(s0);

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
    setGpuPacketParams(s0, x - centreScreenX_1f8003dc.get(), y - centreScreenY_1f8003de.get(), 0, 0, w, h, false);

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

    final BattleMenuStruct58 struct58 = battleMenu_800c6c34.deref();

    if(a0 == 0x1L) {
      struct58._4c.set(0x1L);
      //LAB_800f77d4
      t1 = _800c6758.get();

      //LAB_800f77e8
      _800c697c.setu(_800c697e.get());
    } else {
      struct58._4c.set(0x1L);
      if((int)a0 < 0x2L && a0 == 0) {
        _800c697c.setu(_800c6980.get());
        t1 = charCount_800c677c.get();
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
    struct58._50.set(a0);
    if(a1 == 0) {
      struct58._54.set((int)_800c697c.getSigned());
    } else {
      //LAB_800f79b4
      struct58._54.set(-1);
    }

    //LAB_800f79bc
    struct58._48.set((int)t4);

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
      _800c697c.setu(0);
      struct58._4c.set(0);
    }

    //LAB_800f7a38
    if((joypadPress_8007a398.get() & 0x40L) != 0) {
      t6 = -0x1L;
      _800c697c.setu(0);
      struct58._48.set(-1);
      struct58._4c.set(0);
    }

    //LAB_800f7a68
    return t6;
  }

  @Method(0x800f7a74L)
  public static void FUN_800f7a74(final long a0) {
    final ScriptState<?> v0 = scriptStatePtrArr_800bc1c0.get((int)a0).deref();
    final long a1 = v0.innerStruct_00.getPointer(); //TODO

    //LAB_800f7a98
    for(int i = 0x68; i < 0x74; i++) {
      MEMORY.ref(2, a1).offset(0xd4L).offset(i * 0x2L).setu(0);
    }

    final long v1 = _8004f2ac.offset(MEMORY.ref(2, a1).offset(0x52L).getSigned() * 0xcL).getAddress();
    MEMORY.ref(2, a1).offset(0xd4L).setu(MEMORY.ref(1, v1).offset(0x0L).get());
    MEMORY.ref(2, a1).offset(0xd6L).setu(MEMORY.ref(1, v1).offset(0x1L).get());
    MEMORY.ref(2, a1).offset(0xd8L).setu(MEMORY.ref(1, v1).offset(0x2L).get());
    MEMORY.ref(2, a1).offset(0xdaL).setu(MEMORY.ref(1, v1).offset(0x3L).get());
    MEMORY.ref(2, a1).offset(0xdcL).setu(MEMORY.ref(1, v1).offset(0x4L).get());
    MEMORY.ref(2, a1).offset(0xdeL).setu(MEMORY.ref(1, v1).offset(0x5L).get());
    MEMORY.ref(2, a1).offset(0xe0L).setu(MEMORY.ref(1, v1).offset(0x6L).getSigned());
    MEMORY.ref(2, a1).offset(0xe2L).setu(MEMORY.ref(1, v1).offset(0x7L).getSigned());
    MEMORY.ref(2, a1).offset(0xe4L).setu(MEMORY.ref(1, v1).offset(0x8L).get());
    MEMORY.ref(2, a1).offset(0xe6L).setu(MEMORY.ref(1, v1).offset(0x9L).get());
    MEMORY.ref(2, a1).offset(0xe8L).setu(MEMORY.ref(1, v1).offset(0xaL).get());
    MEMORY.ref(2, a1).offset(0xeaL).setu(MEMORY.ref(1, v1).offset(0xbL).get());
  }

  @Method(0x800f7b68L)
  public static void FUN_800f7b68(final int scriptIndex) {
    long a1 = scriptStatePtrArr_800bc1c0.get(scriptIndex).deref().innerStruct_00.getPointer(); //TODO
    long v1 = a1 + 0x90L;

    //LAB_800f7b8c
    for(int i = 0x48; i < 0x54; i++) { // Not really sure why the loop uses these values
      MEMORY.ref(2, v1).offset(0x4L).setu(0);
      v1 = v1 + 0x2L;
    }

    if(MEMORY.ref(2, a1).offset(0x4eL).getSigned() != -1) {
      final long v0 = _800fa0b8.offset(MEMORY.ref(2, a1).offset(0x4eL).getSigned() * 0xcL).getAddress();
      MEMORY.ref(2, a1).offset(0x94L).setu(MEMORY.ref(1, v0).offset(0x0L).get());
      MEMORY.ref(2, a1).offset(0x96L).setu(MEMORY.ref(1, v0).offset(0x1L).get());
      MEMORY.ref(2, a1).offset(0x98L).setu(MEMORY.ref(1, v0).offset(0x2L).get());
      MEMORY.ref(2, a1).offset(0x9aL).setu(MEMORY.ref(1, v0).offset(0x3L).get());
      MEMORY.ref(2, a1).offset(0x9cL).setu(MEMORY.ref(1, v0).offset(0x4L).get());
      MEMORY.ref(2, a1).offset(0x9eL).setu(MEMORY.ref(1, v0).offset(0x5L).get());
      MEMORY.ref(2, a1).offset(0xa0L).setu(MEMORY.ref(1, v0).offset(0x6L).get());
      MEMORY.ref(2, a1).offset(0xa4L).setu(MEMORY.ref(1, v0).offset(0x8L).get());
      MEMORY.ref(2, a1).offset(0xa6L).setu(MEMORY.ref(1, v0).offset(0x9L).get());
      MEMORY.ref(2, a1).offset(0xa8L).setu(MEMORY.ref(1, v0).offset(0xaL).get());
      MEMORY.ref(2, a1).offset(0xa2L).setu(MEMORY.ref(1, v0).offset(0x7L).get());
      MEMORY.ref(2, a1).offset(0xaaL).setu(MEMORY.ref(1, v0).offset(0xbL).get());
    }

    //LAB_800f7c54
  }

  @Method(0x800f7c5cL)
  public static int FUN_800f7c5c(final long a0, final long a1, final long a2) {
    final ScriptState<BattleObject27c> a2_0 = scriptStatePtrArr_800bc1c0.get((int)a0).derefAs(ScriptState.classFor(BattleObject27c.class));
    final BattleObject27c s1 = a2_0.innerStruct_00.deref();
    long fp = a2_0.ui_60.get() & 0x4L;
    long v1 = (fp != 0 ? 0xcL : 0) + a2 * 0x4L;
    long s2 = s1.all_04.get((int)_800c7284.offset(v1).get()).get();
    final ScriptState<BattleObject27c> a2_1 = scriptStatePtrArr_800bc1c0.get((int)a1).derefAs(ScriptState.classFor(BattleObject27c.class));
    long spa8 = a2_1.ui_60.get() & 0x4L;
    final BattleObject27c s4 = a2_1.innerStruct_00.deref();
    long s3 = _800c726c.offset(v1).get();
    long s6 = _800c729c.offset(v1).get();
    long s7 = _800c72b4.offset(v1).get();
    int s0 = -1;
    if(a2 == 0x2L) {
      s2 = 0x65L;
    }

    //LAB_800f7e98
    if(simpleRand() * 0x65 / 0x100 < (int)s2) {
      final long a1_0 = s1.all_04.get((int)s3).get();

      if((a1_0 & 0xffL) != 0) {
        //LAB_800f7eec
        long v1_0;
        for(v1_0 = 0; v1_0 < 8; v1_0++) {
          if((a1_0 & (0x80L >> v1_0)) != 0) {
            break;
          }
        }

        //LAB_800f7f0c
        s0 = (int)_800c724c.offset(v1_0 * 0x4L).get();
      }

      //LAB_800f7f14
      long v1_0 = s1.all_04.get((int)s6).get() & s7;
      if(v1_0 != 0) {
        if(fp != 0 || a2 != 0) {
          //LAB_800f7f40
          if(a2 != 0x2L) {
            //LAB_800f7f68
            if(v1_0 == 0x10L) {
              //LAB_800f7f7c
              long v0;
              if(spa8 == 0) {
                v0 = s1._a4.get() & _800c706c.offset(s4.charIndex_272.get() * 0x2L).get();
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
          s0 = -1;
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
    for(int a3 = 0; a3 < gameState_800babc8.itemCount_1e6.get(); a3++) {
      //LAB_800f843c
      for(int a2 = 0; a2 < gameState_800babc8.itemCount_1e6.get(); a2++) {
        final long a0 = gameState_800babc8.items_2e9.get(a3).get();
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
  public static void loadBattleHudTextures() {
    loadDrgnBinFile(0, 4113, 0, getMethodAddress(Bttl_800e.class, "battleHudTexturesLoadedCallback", long.class, long.class, long.class), 0, 0x4L);
  }

  @Method(0x800f8568L)
  public static LodString FUN_800f8568(final BattleObject27c a0, final LodString a1) {
    if(a0.charIndex_272.get() != 0x185L) {
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

  @Method(0x800f8768L)
  public static long FUN_800f8768(final int scriptIndex1, final int scriptIndex2, final long a2) {
    final BattleObject27c data1 = scriptStatePtrArr_800bc1c0.get(scriptIndex1).deref().innerStruct_00.derefAs(BattleObject27c.class);
    long matk = data1.magicAttack_36.get();
    if(a2 == 0x1L) {
      matk = matk + _800fa0b8.offset(data1._4e.get() * 0xcL).offset(0x4L).get();
    } else {
      //LAB_800f87c4
      matk = matk + data1._de.get();
    }

    //LAB_800f87d0
    final BattleObject27c data2 = scriptStatePtrArr_800bc1c0.get(scriptIndex2).deref().innerStruct_00.derefAs(BattleObject27c.class);
    final long a1 = scriptStatePtrArr_800bc1c0.get(scriptIndex2).deref().ui_60.get();
    long mdef = data2.magicDefence_3a.get();
    if((a1 & 0x4L) != 0x1L && (a1 & 0x2L) != 0) {
      mdef = mdef * data2.dragoonMagicDefence_b2.get() / 100;
    }

    //LAB_800f8844
    return matk * matk * 5 / mdef;
  }

  @Method(0x800f8854L)
  public static void FUN_800f8854(final int scriptIndex1, final int scriptIndex2, final long a2) {
    final BattleObject27c s2 = scriptStatePtrArr_800bc1c0.get(scriptIndex1).deref().innerStruct_00.derefAs(BattleObject27c.class);

    if(a2 != 0x1L) {
      FUN_800f7a74(scriptIndex1);
    }

    //LAB_800f88a0
    //LAB_800f88a8
    for(int i = 0; i < 8; i++) {
      if((s2._da.get() & (0x80 >> i)) != 0) {
        FUN_800f923c(scriptIndex1, scriptIndex2, i, 0x6bL);
      }

      //LAB_800f88cc
    }

    //LAB_800f88e4
    for(int i = 0; i < 8; i++) {
      if((s2._dc.get() & (0x80 >> i)) != 0) {
        FUN_800f923c(scriptIndex1, scriptIndex2, i, 0x6cL);
      }

      //LAB_800f8908
    }

    FUN_800f3204(scriptIndex2);
  }

  @Method(0x800f89ccL)
  public static long FUN_800f89cc(long a0) {
    a0 = (byte)a0;

    if(a0 == 0) {
      return 0x2L;
    }

    if(a0 <= 0) {
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
  public static void FUN_800f8ac4(final long a0, final long a1, final long x, final long y) {
    final LodString str = allText_800fb3c0.get((int)a0).deref().get((int)a1).deref();
    renderText(str, x - textWidth(str) / 2, y - 6, 0, 0);
  }

  @Method(0x800f8b74L)
  public static void FUN_800f8b74(final long a0) {
    long v0 = 0x800c_0000L;
    long t8 = v0 + 0x7194L;
    final short[] sp0x00 = new short[8];
    for(int i = 0; i < 8; i++) {
      sp0x00[i] = (short)MEMORY.ref(2, t8).offset(i * 0x2L).get();
    }

    //LAB_800f8bd8
    for(int t1 = 0; t1 < 8; t1++) {
      if((a0 & 0x1L << t1) != 0) {
        final BattleMenuStruct58 struct58 = battleMenu_800c6c34.deref();

        //LAB_800f8bf4
        for(int a3 = 0; a3 < 8; a3++) {
          if((struct58.iconFlags_10.get(a3).get() & 0xfL) == sp0x00[t1]) {
            struct58.iconFlags_10.get(a3).or(0x80);
            break;
          }

          //LAB_800f8c10
        }
      }

      //LAB_800f8c20
    }
  }

  @Method(0x800f8c38L)
  public static void FUN_800f8c38(final long a0) {
    final BattleMenuStruct58 struct58 = battleMenu_800c6c34.deref();

    if(struct58._00.get() != 0) {
      //LAB_800f8c78
      if(a0 != 0x1L || struct58._44.get() != 0) {
        //LAB_800f8c64
        struct58._02.and(0xfffd);
      } else {
        struct58._02.or(0x2);
      }
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
  public static void FUN_800f8cd8(final long x, final long y, final long u, final long v, final long w, final long h, final long a6, final long a7) {
    final long v0 = linkedListAddress_1f8003d8.get();
    linkedListAddress_1f8003d8.addu(0x28L);
    setGp0_2c(v0);

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
    setGpuPacketParams(v0, x, y, u, v, w, h, true);
    FUN_800f9024(v0, a6, s1);
  }

  @Method(0x800f8dfcL)
  public static void FUN_800f8dfc(final long x, final long y, final long u, final long v, final long w, final long h, final long a6, final long a7, final long a8) {
    final long t3 = _800c71ec.getAddress();

    final byte[] sp0x20 = new byte[] {
      (byte)MEMORY.ref(1, t3).offset(0x0L).getSigned(),
      (byte)MEMORY.ref(1, t3).offset(0x1L).getSigned(),
      (byte)MEMORY.ref(1, t3).offset(0x2L).getSigned(),
    };

    final long s0 = linkedListAddress_1f8003d8.get();
    linkedListAddress_1f8003d8.addu(0x28L);
    setGp0_2c(s0);
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

    setGpuPacketParams(s0, x, y, u, v, w, h, true);
    FUN_800f9024(s0, a6, 0);
  }

  @Method(0x800f8facL)
  public static void setGpuPacketParams(final long addr, final long x, final long y, final long u, final long v, final long w, final long h, final boolean textured) {
    MEMORY.ref(2, addr).offset(0x08L).setu(x);
    MEMORY.ref(2, addr).offset(0x0aL).setu(y);
    MEMORY.ref(2, addr).offset(0x10L).setu(x + w);
    MEMORY.ref(2, addr).offset(0x12L).setu(y);
    MEMORY.ref(2, addr).offset(0x18L).setu(x);
    MEMORY.ref(2, addr).offset(0x1aL).setu(y + h);
    MEMORY.ref(2, addr).offset(0x22L).setu(y + h);
    MEMORY.ref(2, addr).offset(0x20L).setu(x + w);

    if(textured) {
      MEMORY.ref(1, addr).offset(0x0cL).setu(u);
      MEMORY.ref(1, addr).offset(0x0dL).setu(v);
      MEMORY.ref(1, addr).offset(0x14L).setu(u + w);
      MEMORY.ref(1, addr).offset(0x15L).setu(v);
      MEMORY.ref(1, addr).offset(0x1cL).setu(u);
      MEMORY.ref(1, addr).offset(0x1dL).setu(v + h);
      MEMORY.ref(1, addr).offset(0x24L).setu(u + w);
      MEMORY.ref(1, addr).offset(0x25L).setu(v + h);
    }

    //LAB_800f901c
  }

  @Method(0x800f9024L)
  public static void FUN_800f9024(final long a0, final long a1, final long transparencyMode) {
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
    MEMORY.ref(2, a0).offset(0x16L).setu(GetTPage(0, transparencyMode, 704, 496));
    insertElementIntoLinkedList(tags_1f8003d0.getPointer() + 0x7cL, a0);
  }

  @Method(0x800f923cL)
  public static void FUN_800f923c(final int scriptIndex1, final int scriptIndex2, final long a2, final long a3) {
    //LAB_800f9304
    final long a3_0 = a3 == 0x6bL ? _800c71fc.getAddress() : _800c721c.getAddress();

    //LAB_800f930c
    final BattleObject27c t0 = scriptStatePtrArr_800bc1c0.get(scriptIndex1).deref().innerStruct_00.derefAs(BattleObject27c.class);
    final BattleObject27c a1 = scriptStatePtrArr_800bc1c0.get(scriptIndex2).deref().innerStruct_00.derefAs(BattleObject27c.class);

    final long a0 = t0.charIndex_272.get() != a1.charIndex_272.get() ? 0x3L : 0x4L;

    //LAB_800f935c
    a1.all_04.get((int)MEMORY.ref(4, a3_0).offset(a2 * 0x4L).get()).set((short)(a0 << 8 | t0._e0.get()));
  }

  @Method(0x800f9380L)
  public static void FUN_800f9380(final BattleObject27c a0, final BattleObject27c a1) {
    if((a1._a8.get() & 0xff) != 0) {
      //LAB_800f93c8
      for(int i = 0; i < 8; i++) {
        if((a1._a8.get() & (0x80 >> i)) != 0) {
          //LAB_800f93e8
          final long a3;
          if(a0.charIndex_272.get() != a1.charIndex_272.get()) {
            a3 = 0x3L;
          } else {
            a3 = 0x4L;
          }

          //LAB_800f9428
          final long a0_0;
          if(i < 0x4L) {
            a0_0 = 50;
          } else {
            a0_0 = -50;
          }

          //LAB_800f9438
          a1.all_04.get((int)_800c723c.offset(i % 4 * 4).get()).set((short)(a3 << 8 | a0_0));
        }

        //LAB_800f9454
      }
    }

    //LAB_800f9464
  }

  @Method(0x800f946cL)
  public static long FUN_800f946c(final BattleObject27c a0, final long a1, final long a2) {
    final long a0_0;
    if(a2 == 0) {
      a0_0 = _800fa0b8.offset(a0._4e.get() * 0xcL).offset(0x3L).get();
    } else {
      //LAB_800f949c
      a0_0 = a0._d8.get();
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

  @Method(0x800f9660L)
  public static long FUN_800f9660(final RunningScript a0) {
    a0.params_20.get(2).deref().set(FUN_800f1aa8(a0.params_20.get(0).deref().get(), a0.params_20.get(1).deref().get(), 0x2L));
    return 0;
  }

  @Method(0x800f96a8L)
  public static long FUN_800f96a8(final RunningScript a0) {
    FUN_800f7b68(a0.params_20.get(0).deref().get());
    return 0;
  }

  @Method(0x800f96d4L)
  public static long FUN_800f96d4(final RunningScript a0) {
    final BattleObject27c v1 = scriptStatePtrArr_800bc1c0.get(a0.params_20.get(0).deref().get()).deref().innerStruct_00.derefAs(BattleObject27c.class);
    a0.params_20.get(1).deref().set(v1._148.coord2_14.coord.transfer.getX());
    a0.params_20.get(2).deref().set(v1._148.coord2_14.coord.transfer.getY());
    a0.params_20.get(3).deref().set(v1._148.coord2_14.coord.transfer.getZ());
    return 0;
  }

  @Method(0x800f97d8L)
  public static long FUN_800f97d8(final RunningScript a0) {
    FUN_800f4964();
    FUN_800f49bc(scriptStatePtrArr_800bc1c0.get(a0.params_20.get(0).deref().get()).deref().innerStruct_00.derefAs(BattleObject27c.class).charIndex_272.get(), (short)a0.params_20.get(1).deref().get());
    return 0;
  }

  @Method(0x800f984cL)
  public static long FUN_800f984c(final RunningScript a0) {
    FUN_800f4268(a0.params_20.get(0).deref().get(), a0.params_20.get(1).deref().get(), a0.params_20.get(2).deref().get());
    return 0;
  }

  @Method(0x800f9884L)
  public static long FUN_800f9884(final RunningScript a0) {
    FUN_800f7a74(a0.params_20.get(0).deref().get());
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

  @Method(0x800f9bd4L)
  public static long FUN_800f9bd4(final RunningScript a0) {
    final long v1 = _800c6b6c.get();
    MEMORY.ref(2, v1).offset(0x0L).setu(0x4L);
    MEMORY.ref(2, v1).offset(0x8L).setu(a0.params_20.get(0).deref().get());
    return 0;
  }

  @Method(0x800f9c00L)
  public static long FUN_800f9c00(final RunningScript a0) {
    FUN_800fa018(a0.params_20.get(0).deref().get());
    return 0;
  }

  @Method(0x800f9d7cL)
  public static long FUN_800f9d7c(final RunningScript a0) {
    long v0 = scriptStatePtrArr_800bc1c0.get(a0.params_20.get(0).deref().get()).deref().innerStruct_00.getPointer(); //TODO
    long a1 = v0 + 0xb0L;

    //LAB_800f9db0
    for(int a3 = 0x58; a3 < 0x68; a3++) {
      if(MEMORY.ref(2, a1).offset(0x4L).get() != 0) {
        if((MEMORY.ref(2, a1).offset(0x4L).get() & 0xff00L) < 0x200L) {
          MEMORY.ref(2, a1).offset(0x4L).setu(0);
        } else {
          //LAB_800f9ddc
          MEMORY.ref(2, a1).offset(0x4L).subu(0x100L);
        }
      }

      //LAB_800f9de8
      a1 = a1 + 0x2L;
    }

    FUN_800f3204(a0.params_20.get(0).deref().get());
    return 0;
  }

  /** Clears out some scripted player data */
  @Method(0x800f9e10L)
  public static void FUN_800f9e10(final BattleObject27c a0) {
    //LAB_800f9e18
    for(int i = 0x68; i < 0x74; i++) {
      a0.all_04.get(i).set((short)0);
    }

    //LAB_800f9e34
    for(int i = 0x48; i < 0x54; i++) {
      a0.all_04.get(i).set((short)0);
    }
  }

  @Method(0x800f9e50L)
  public static long FUN_800f9e50(long a0) {
    assert false;
    return 0;
  }

  @Method(0x800f9ee8L)
  public static void drawLine(final long x1, final long y1, final long x2, final long y2, final long r, final long g, final long b, final boolean transparent) {
    final long s0 = linkedListAddress_1f8003d8.get();
    setGp0_50(s0);
    gpuLinkedListSetCommandTransparency(s0, transparent);
    MEMORY.ref(1, s0).offset(0x4L).setu(r);
    MEMORY.ref(1, s0).offset(0x5L).setu(g);
    MEMORY.ref(1, s0).offset(0x6L).setu(b);
    MEMORY.ref(2, s0).offset(0x8L).setu(x1);
    MEMORY.ref(2, s0).offset(0xaL).setu(y1);
    MEMORY.ref(1, s0).offset(0xcL).setu(r);
    MEMORY.ref(1, s0).offset(0xdL).setu(g);
    MEMORY.ref(1, s0).offset(0xeL).setu(b);
    MEMORY.ref(2, s0).offset(0x10L).setu(x2);
    MEMORY.ref(2, s0).offset(0x12L).setu(y2);
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
  public static long clampX(final long a0) {
    return MathHelper.clamp(a0, 20, 300);
  }

  @Method(0x800fa090L)
  public static long clampY(final long a0) {
    return MathHelper.clamp(a0, 20, 220);
  }

  @Method(0x800fc3a0L)
  public static void FUN_800fc3a0(final long a0) {
    FUN_80012bb4();
  }
}
