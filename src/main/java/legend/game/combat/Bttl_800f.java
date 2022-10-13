package legend.game.combat;

import legend.core.MathHelper;
import legend.core.gte.DVECTOR;
import legend.core.memory.Method;
import legend.game.Scus94491BpeSegment_8002;
import legend.game.combat.types.BattleDisplayStats144;
import legend.game.combat.types.BattleDisplayStats144Sub10;
import legend.game.combat.types.BattleMenuStruct58;
import legend.game.combat.types.BattleObject27c;
import legend.game.combat.types.BattleStruct3c;
import legend.game.combat.types.BttlStructa4;
import legend.game.combat.types.FloatingNumberC4;
import legend.game.combat.types.FloatingNumberC4Sub20;
import legend.game.types.ActiveStatsa0;
import legend.game.types.ItemStats0c;
import legend.game.types.LodString;
import legend.game.types.RunningScript;
import legend.game.types.ScriptState;
import legend.game.types.SpellStats0c;
import legend.game.types.TexPageBpp;
import legend.game.types.TexPageTrans;
import legend.game.types.TexPageY;

import javax.annotation.Nullable;

import static java.lang.Math.round;
import static legend.core.Hardware.MEMORY;
import static legend.core.MemoryHelper.getConsumerAddress;
import static legend.core.MemoryHelper.getMethodAddress;
import static legend.game.Scus94491BpeSegment.centreScreenX_1f8003dc;
import static legend.game.Scus94491BpeSegment.centreScreenY_1f8003de;
import static legend.game.Scus94491BpeSegment.queueGpuPacket;
import static legend.game.Scus94491BpeSegment.gpuPacketAddr_1f8003d8;
import static legend.game.Scus94491BpeSegment.loadSupportOverlay;
import static legend.game.Scus94491BpeSegment.loadDrgnBinFile;
import static legend.game.Scus94491BpeSegment.playSound;
import static legend.game.Scus94491BpeSegment.simpleRand;
import static legend.game.Scus94491BpeSegment.tags_1f8003d0;
import static legend.game.Scus94491BpeSegment_8002.giveItem;
import static legend.game.Scus94491BpeSegment_8002.intToStr;
import static legend.game.Scus94491BpeSegment_8002.takeItem;
import static legend.game.Scus94491BpeSegment_8002.textWidth;
import static legend.game.Scus94491BpeSegment_8003.GetTPage;
import static legend.game.Scus94491BpeSegment_8003.bzero;
import static legend.game.Scus94491BpeSegment_8003.gpuLinkedListSetCommandTransparency;
import static legend.game.Scus94491BpeSegment_8003.setGp0_2c;
import static legend.game.Scus94491BpeSegment_8003.setGp0_38;
import static legend.game.Scus94491BpeSegment_8003.setGp0_50;
import static legend.game.Scus94491BpeSegment_8004.itemStats_8004f2ac;
import static legend.game.Scus94491BpeSegment_8005._80050ae8;
import static legend.game.Scus94491BpeSegment_8005.spells_80052734;
import static legend.game.Scus94491BpeSegment_8006._8006e398;
import static legend.game.Scus94491BpeSegment_8006._8006f1d8;
import static legend.game.Scus94491BpeSegment_8006._8006f210;
import static legend.game.Scus94491BpeSegment_8006._8006f254;
import static legend.game.Scus94491BpeSegment_8006._8006f284;
import static legend.game.Scus94491BpeSegment_8007.joypadInput_8007a39c;
import static legend.game.Scus94491BpeSegment_8007.joypadPress_8007a398;
import static legend.game.Scus94491BpeSegment_8007.vsyncMode_8007a3b8;
import static legend.game.Scus94491BpeSegment_800b._800bb0fc;
import static legend.game.Scus94491BpeSegment_800b._800be5d0;
import static legend.game.Scus94491BpeSegment_800b.gameState_800babc8;
import static legend.game.Scus94491BpeSegment_800b.scriptStatePtrArr_800bc1c0;
import static legend.game.Scus94491BpeSegment_800b.spGained_800bc950;
import static legend.game.Scus94491BpeSegment_800b.stats_800be5f8;
import static legend.game.Scus94491BpeSegment_800b.texPages_800bb110;
import static legend.game.combat.Bttl_800c._800c669c;
import static legend.game.combat.Bttl_800c._800c66b0;
import static legend.game.combat.Bttl_800c._800c6718;
import static legend.game.combat.Bttl_800c._800c6748;
import static legend.game.combat.Bttl_800c._800c6758;
import static legend.game.combat.Bttl_800c._800c697c;
import static legend.game.combat.Bttl_800c._800c697e;
import static legend.game.combat.Bttl_800c._800c6980;
import static legend.game.combat.Bttl_800c._800c6988;
import static legend.game.combat.Bttl_800c._800c69c8;
import static legend.game.combat.Bttl_800c._800c6b60;
import static legend.game.combat.Bttl_800c._800c6b64;
import static legend.game.combat.Bttl_800c._800c6b68;
import static legend.game.combat.Bttl_800c._800c6b6c;
import static legend.game.combat.Bttl_800c._800c6b70;
import static legend.game.combat.Bttl_800c._800c6ba0;
import static legend.game.combat.Bttl_800c._800c6ba1;
import static legend.game.combat.Bttl_800c._800c6ba8;
import static legend.game.combat.Bttl_800c._800c6c30;
import static legend.game.combat.Bttl_800c._800c6c40;
import static legend.game.combat.Bttl_800c._800c6f30;
import static legend.game.combat.Bttl_800c._800c6f4c;
import static legend.game.combat.Bttl_800c._800c6fec;
import static legend.game.combat.Bttl_800c._800c7028;
import static legend.game.combat.Bttl_800c._800c703c;
import static legend.game.combat.Bttl_800c._800c70a4;
import static legend.game.combat.Bttl_800c._800c70e0;
import static legend.game.combat.Bttl_800c._800c70f4;
import static legend.game.combat.Bttl_800c._800c7114;
import static legend.game.combat.Bttl_800c._800c7124;
import static legend.game.combat.Bttl_800c._800c7190;
import static legend.game.combat.Bttl_800c._800c7192;
import static legend.game.combat.Bttl_800c._800c7193;
import static legend.game.combat.Bttl_800c._800c7194;
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
import static legend.game.combat.Bttl_800c._800c72cc;
import static legend.game.combat.Bttl_800c._800d66b0;
import static legend.game.combat.Bttl_800c._800d6c30;
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
import static legend.game.combat.Bttl_800c.allText_800fb3c0;
import static legend.game.combat.Bttl_800c.battleMenu_800c6c34;
import static legend.game.combat.Bttl_800c.charCount_800c677c;
import static legend.game.combat.Bttl_800c.characterElements_800c706c;
import static legend.game.combat.Bttl_800c.displayStats_800c6c2c;
import static legend.game.combat.Bttl_800c.dragoonSpells_800c6960;
import static legend.game.combat.Bttl_800c.floatingNumbers_800c6b5c;
import static legend.game.combat.Bttl_800c.getHitMultiplier;
import static legend.game.combat.Bttl_800c.spellStats_800fa0b8;
import static legend.game.combat.Bttl_800e.FUN_800ef8d8;
import static legend.game.combat.Bttl_800e.perspectiveTransformXyz;

public final class Bttl_800f {
  private Bttl_800f() { }

  @Method(0x800f0f5cL)
  public static void FUN_800f0f5c(final long a0) {
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
      final long s0 = gpuPacketAddr_1f8003d8.get();
      gpuPacketAddr_1f8003d8.addu(0x28L);
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
      MEMORY.ref(2, s0).offset(0x16L).setu(GetTPage(TexPageBpp.BITS_4, TexPageTrans.HALF_B_PLUS_HALF_F, 704, 256));
      queueGpuPacket(tags_1f8003d0.deref().get(31).getAddress(), s0);
    }
  }

  @Method(0x800f1268L)
  public static void renderTextBoxBackground(final long a0, final long a1, final long a2, final long a3, final long a4) {
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
    long s0 = gpuPacketAddr_1f8003d8.get();
    gpuPacketAddr_1f8003d8.addu(0x24L);
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

    queueGpuPacket(tags_1f8003d0.getPointer() + 0x7cL, s0);
    s0 = gpuPacketAddr_1f8003d8.get();
    gpuPacketAddr_1f8003d8.addu(0x24L);
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

    queueGpuPacket(tags_1f8003d0.getPointer() + 0x7cL, s0);

    final long a1_0 = gpuPacketAddr_1f8003d8.get();
    MEMORY.ref(1, a1_0).offset(0x3L).setu(0x1L);
    MEMORY.ref(4, a1_0).offset(0x4L).setu(0xe100_0200L | (texPages_800bb110.get(TexPageBpp.BITS_4).get(TexPageTrans.HALF_B_PLUS_HALF_F).get(TexPageY.Y_256).get() | 0xbL) & 0x9ffL);
    queueGpuPacket(tags_1f8003d0.getPointer() + 0x7cL, a1_0);
    gpuPacketAddr_1f8003d8.addu(0x8L);
  }

  @Method(0x800f1550L)
  public static void FUN_800f1550(final int charSlot, final int a1, long a2, final long a3) {
    long v0;
    long v1;
    int t1;
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

    v1 = sp0x08[a1];
    if(v1 == 1) {
      if((short)a2 > 9) {
        t2 = 9;
      }
    } else if(v1 == 2) {
      //LAB_800f16a0
      if((short)a2 > 99) {
        t2 = 99;
      }
    } else {
      if((short)t2 > 9999) {
        t2 = 9999;
      }
    }

    //LAB_800f16d4
    //LAB_800f16d8
    if((short)t2 < 0) {
      t2 = 0;
    }

    //LAB_800f16e4
    final BattleDisplayStats144 displayStats = displayStats_800c6c2c.deref().get(charSlot);

    final short[] sp0x00 = new short[4];

    //LAB_800f171c
    for(int i = 0; i < 4; i++) {
      displayStats._04.get(a1).get(i)._00.set((short)-1);
      sp0x00[i] = -1;
    }

    a2 = 0x1L;

    //LAB_800f1768
    for(t1 = 0; t1 < sp0x08[a1] - 0x1L; t1++) {
      a2 = a2 * 10;
    }

    //LAB_800f1780
    //LAB_800f17b0
    for(int i = 0; i < sp0x08[a1]; i++) {
      v1 = (int)t2 / (int)a2;
      t2 = (int)t2 % (int)a2;
      a2 = a2 / 10;
      sp0x00[i] = (short)v1;
    }

    //LAB_800f1800
    //LAB_800f1828
    for(a2 = 0; a2 < sp0x08[a1] - 0x1L; a2++) {
      if(sp0x00[(int)a2] != 0) {
        break;
      }
    }

    //LAB_800f1848
    //LAB_800f184c
    //LAB_800f18cc
    for(t1 = 0; t1 < sp0x08[a1] && a2 < sp0x08[a1]; t1++, a2++) {
      final BattleDisplayStats144Sub10 struct = displayStats._04.get(a1).get(t1);

      if(a1 == 0x1L || a1 > 0 && a1 < 0x5L && a1 >= 0x3L) {
        //LAB_800f18f0
        struct.x_02.set((short)(sp0x18[a1 * 2] + t1 * 5));
      } else {
        struct.x_02.set((short)(sp0x18[a1 * 2] + a2 * 5));
      }

      //LAB_800f1920
      struct.y_04.set((short)sp0x18[a1 * 2 + 1]);
      struct.u_06.set((short)sp0x30[sp0x00[(int)a2]]);
      struct.v_08.set((short)0x20);
      struct.w_0a.set((short)0x8);
      struct.h_0c.set((short)0x8);

      if(a3 == 0x1L) {
        //LAB_800f1984
        v0 = 0x80L;
      } else if(a3 == 0x2L) {
        //LAB_800f198c
        v0 = 0x82L;
      } else if(a3 == 0x3L) {
        //LAB_800f1994
        v0 = 0x83L;
      } else {
        v0 = 0x2dL;
      }

      //LAB_800f1998
      struct._0e.set((short)v0);

      //LAB_800f199c
      struct._00.set(sp0x00[(int)a2]);
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

  /**
   * @param attackType 0 is physical attack, 1/2 are both magical, don't know the difference
   */
  @Method(0x800f1aa8L)
  public static int FUN_800f1aa8(final int bobjIndex1, final int bobjIndex2, final int attackType) {
    long s4 = 0;
    ScriptState<?> state = scriptStatePtrArr_800bc1c0.get(bobjIndex1).deref();
    final BattleObject27c bobj1 = state.innerStruct_00.derefAs(BattleObject27c.class);
    final boolean isEnemy = (state.ui_60.get() & 0x4L) != 0;

    state = scriptStatePtrArr_800bc1c0.get(bobjIndex2).deref();
    final BattleObject27c bobj2 = state.innerStruct_00.derefAs(BattleObject27c.class);

    final int hitStat = (byte)bobj1.all_04.get((int)_800c703c.offset(attackType * 0x4L).get()).get();
    int attackHit;
    if(attackType == 0) {
      if(isEnemy) {
        attackHit = spellStats_800fa0b8.get(bobj1.spellId_4e.get()).accuracy_05.get();
      } else {
        //LAB_800f1bf4
        attackHit = bobj1.attackHit_3c.get();
      }

      //LAB_800f1bf8
      setTempSpellStats(bobjIndex1);
    } else if(attackType == 1) {
      //LAB_800f1c08
      attackHit = spellStats_800fa0b8.get(bobj1.spellId_4e.get()).accuracy_05.get();
      setTempSpellStats(bobjIndex1);
    } else {
      //LAB_800f1c38
      setTempWeaponStats(bobjIndex1);
      attackHit = 100;
    }

    //LAB_800f1c44
    attackHit = attackHit * (hitStat + 100) / 100;

    final long avoid;
    if(attackType == 0) {
      avoid = bobj2.attackAvoid_40.get();
    } else {
      //LAB_800f1c9c
      avoid = bobj2.magicAvoid_42.get();
    }

    //LAB_800f1ca8
    final long a0_0 = (avoid * ((byte)bobj1.all_04.get((int)_800c703c.offset(0x10L).offset(attackType * 0x4L).get()).get() + 100)) / 100;
    if(a0_0 < attackHit && attackHit - a0_0 >= (simpleRand() * 101 >> 16)) {
      s4 = 0x1L;
      if(isEnemy) {
        setTempSpellStats(bobjIndex1);
      }
    }

    //LAB_800f1d28
    if((bobj1.all_04.get((int)_800c703c.offset(0x20L).offset(attackType * 0x4L).get()).get() & _800c703c.offset(0x30L).offset(attackType * 0x4L).get()) != 0) {
      s4 = 0x1L;
    }

    //LAB_800f1d5c
    return (int)s4;
  }

  @Method(0x800f1d88L)
  public static int FUN_800f1d88(final int scriptIndex1, final int scriptIndex2) {
    int s2;
    final long s6;
    final long s7;

    ScriptState<?> a1 = scriptStatePtrArr_800bc1c0.get(scriptIndex1).deref();
    final BattleObject27c s0 = a1.innerStruct_00.derefAs(BattleObject27c.class);
    final int element;
    if((a1.ui_60.get() & 0x4L) == 0) {
      s2 = calculateAdditionDamage(scriptIndex1, scriptIndex2);
      element = s0.elementFlag_1c.get();
    } else {
      //LAB_800f1e5c
      s2 = FUN_800f2d48(scriptIndex1, scriptIndex2);
      element = spellStats_800fa0b8.get(s0.spellId_4e.get()).element_08.get();
    }

    //LAB_800f1e88
    s7 = FUN_800f89cc(s0.powerAttack_b4.get());
    if((int)_800c6b64.get() == -0x1L) {
      s6 = 0;
    } else {
      s6 = element;
    }

    a1 = scriptStatePtrArr_800bc1c0.get(scriptIndex2).deref();
    final BattleObject27c s3 = a1.innerStruct_00.derefAs(BattleObject27c.class);

    //LAB_800f1eb0
    final long s0_0;
    if((a1.ui_60.get() & 0x4L) == 0) {
      if(s3.charIndex_272.get() == 0 && (gameState_800babc8.dragoonSpirits_19c.get(0).get() & 0xff) >>> 7 != 0 && (scriptStatePtrArr_800bc1c0.get(scriptIndex2).deref().ui_60.get() & 0x2L) != 0) {
        s0_0 = characterElements_800c706c.get(9).get();
      } else {
        s0_0 = characterElements_800c706c.get(s3.charIndex_272.get()).get();
      }
    } else {
      //LAB_800f1f1c
      s0_0 = s3.monsterElementFlag_72.get();
    }

    //LAB_800f1f20
    final long s4_0 = FUN_800f89cc(s3.powerDefence_b8.get());

    final long s3_0;
    if((int)_800c6b64.get() == -0x1L) {
      s3_0 = 0;
    } else {
      //LAB_800f1f4c
      s3_0 = characterElements_800c706c.get((int)_800c6b64.get()).get();
    }

    //LAB_800f1f54
    s2 = s2 * FUN_800f2fe0((short)element, (short)s0_0, 0) / 100;
    s2 = s2 * FUN_800f2fe0((short)s7, (short)s4_0, 0x3L) / 100;
    s2 = s2 * FUN_800f2fe0((short)s6, (short)s3_0, 0x5L) / 100;
    if(s2 <= 0) {
      s2 = 0;
    }

    //LAB_800f2020
    return s2;
  }

  @Method(0x800f204cL)
  public static int FUN_800f204c(final int attackerBobjIndex, final int defenderBobjIndex, final int attackType) {
    long s1;
    long s2;
    final long s6;
    final long fp;

    final ScriptState<?> attackerState = scriptStatePtrArr_800bc1c0.get(attackerBobjIndex).deref();
    final BattleObject27c attacker = attackerState.innerStruct_00.derefAs(BattleObject27c.class);
    final ScriptState<?> defenderState = scriptStatePtrArr_800bc1c0.get(defenderBobjIndex).deref();
    final BattleObject27c defender = defenderState.innerStruct_00.derefAs(BattleObject27c.class);

    int damage;
    if(attackType == 1 || attacker.itemType_ea.get() == 0) {
      //LAB_800f2140
      if((attacker._96.get() & 0x4L) != 0) {
        damage = defender.maxHp_10.get() * attacker.spellMulti_9c.get() / 100;

        if((attacker._94.get() & 0x8L) != 0) {
          // Attack all

          if((attackerState.ui_60.get() & 0x4L) == 0) {
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
            a1_0._a8.set(attacker._a8.get());
            FUN_800f9380(attacker, a1_0);
            s2 = s2 + 0x4L;
          }
        } else {
          // Attack single

          //LAB_800f2210
          defender._a8.set(attacker._a8.get());
          FUN_800f9380(attacker, defender);
        }

        //LAB_800f2224
        attacker.dragoonFlag_0e.or(0x800);
      } else {
        //LAB_800f2238
        if((attackerState.ui_60.get() & 0x4L) == 0) {
          damage = playerMagicAttack(attackerBobjIndex, defenderBobjIndex, attackType);
        } else {
          //LAB_800f2250
          damage = monsterMagicAttack(attackerBobjIndex, defenderBobjIndex, attackType);
        }

        //LAB_800f225c
        final int attackElement;
        if(attackType == 0x1L) {
          attackElement = spellStats_800fa0b8.get(attacker.spellId_4e.get()).element_08.get();
        } else {
          //LAB_800f228c
          attackElement = attacker.itemElement_d6.get();
        }

        //LAB_800f2290
        fp = FUN_800f89cc(attacker.powerMagicAttack_b6.get());

        if((int)_800c6b64.get() == -1) {
          s6 = 0;
        } else {
          s6 = attackElement;
        }

        //LAB_800f22b8
        final int defenderElement;
        if((defenderState.ui_60.get() & 0x4L) == 0) {
          if(defender.charIndex_272.get() == 0 && (gameState_800babc8.dragoonSpirits_19c.get(0).get() & 0xff) >>> 7 != 0 && (defenderState.ui_60.get() & 0x2L) != 0) {
            defenderElement = characterElements_800c706c.get(1).get();
          } else {
            defenderElement = characterElements_800c706c.get(defender.charIndex_272.get()).get();
          }
        } else {
          //LAB_800f2324
          defenderElement = defender.monsterElementFlag_72.get();
        }

        //LAB_800f2328
        final long s0 = FUN_800f89cc(defender.powerMagicDefence_ba.get());

        final int s3;
        if((int)_800c6b64.get() == -1) {
          s3 = 0;
        } else {
          //LAB_800f2354
          s3 = characterElements_800c706c.get((int)_800c6b64.get()).get();
        }

        //LAB_800f235c
        damage = damage * FUN_800f2fe0(attackElement, defenderElement, 0) / 100 * FUN_800f2fe0(fp, s0, 4L) / 100 * FUN_800f2fe0(s6, s3, 5) / 100;
      }
    } else {
      //LAB_800f2404
      //LAB_800f2410
      for(s1 = 0; s1 < 8; s1++) {
        if((attacker.itemType_ea.get() & 0x80 >> s1) != 0) {
          break;
        }
      }

      //LAB_800f2430
      damage = switch((int)s1) {
        case 0 -> {
          //LAB_800f2454
          attacker.dragoonFlag_0e.or(0x800);
          yield defender.maxHp_10.get();
        }

        case 1 -> {
          //LAB_800f2464
          attacker.dragoonFlag_0e.or(0x800);
          yield defender.maxMp_12.get();
        }

        //LAB_800f2478
        case 6 -> defender.maxHp_10.get();

        //LAB_800f2484
        case 7 -> defender.maxMp_12.get();

        //LAB_800f2490
        default -> 0;
      };

      //LAB_800f2494
      //LAB_800f24bc
      damage = damage * attacker.itemPercentage_e6.get() / 100;
    }

    //LAB_800f24c0
    if(damage < 0) {
      damage = 0;
    }

    //LAB_800f24d0
    return damage;
  }

  @Method(0x800f2500L)
  public static long FUN_800f2500(final RunningScript s3) {
    final long t0;
    final ScriptState<?> v0 = scriptStatePtrArr_800bc1c0.get(s3.params_20.get(0).deref().get()).deref();
    final BattleObject27c s2 = v0.innerStruct_00.derefAs(BattleObject27c.class);
    int damage = FUN_800f1d88(s3.params_20.get(0).deref().get(), s3.params_20.get(1).deref().get());
    if((v0.ui_60.get() & 0x4L) != 0) {
      damage = FUN_800f946c(s2, damage, 0);
      FUN_800f9380(s2, s2);
    }

    //LAB_800f257c
    if(damage <= 0) {
      damage = 1;
    }

    //LAB_800f2588
    damage = applyResistancesAndImmunities(s3.params_20.get(1).deref().get(), damage, 0);
    ScriptState<?> a0 = scriptStatePtrArr_800bc1c0.get(s3.params_20.get(0).deref().get()).deref();
    final BattleObject27c a1 = a0.innerStruct_00.derefAs(BattleObject27c.class);

    final long a0_0;
    if((a0.ui_60.get() & 0x4L) == 0) {
      a0_0 = a1.elementFlag_1c.get();
    } else {
      //LAB_800f25f4
      a0_0 = spellStats_800fa0b8.get(a1.spellId_4e.get()).element_08.get();
    }

    a0 = scriptStatePtrArr_800bc1c0.get(s3.params_20.get(1).deref().get()).deref();
    final BattleObject27c a3 = a0.innerStruct_00.derefAs(BattleObject27c.class);

    //LAB_800f2614
    if((a0.ui_60.get() & 0x4L) == 0) {
      t0 = a3.elementalResistanceFlag_20.get();
    } else {
      t0 = 0;
    }

    //LAB_800f2620
    if((a0_0 & t0) != 0) {
      damage = damage / 2;
    }

    //LAB_800f2634
    if((a0_0 & a3.elementalImmunityFlag_22.get()) != 0) {
      damage = 0;
    }

    //LAB_800f2640
    s3.params_20.get(2).deref().set(damage);
    s3.params_20.get(3).deref().set(FUN_800f7c5c(s3.params_20.get(0).deref().get(), s3.params_20.get(1).deref().get(), 0));
    return 0;
  }

  @Method(0x800f2694L)
  public static long FUN_800f2694(final RunningScript s1) {
    final BattleObject27c bobj = scriptStatePtrArr_800bc1c0.get(s1.params_20.get(0).deref().get()).deref().innerStruct_00.derefAs(BattleObject27c.class);
    bobj.spellId_4e.set((short)s1.params_20.get(2).deref().get());
    clearTempWeaponAndSpellStats(bobj);
    setTempSpellStats(s1.params_20.get(0).deref().get());

    int damage = Math.max(1, FUN_800f946c(bobj, FUN_800f204c(s1.params_20.get(0).deref().get(), s1.params_20.get(1).deref().get(), 1), 0));

    //LAB_800f272c
    if((bobj.dragoonFlag_0e.get() & 0x800) != 0) {
      bobj.dragoonFlag_0e.and(0xf7ff);
    } else {
      //LAB_800f2748
      damage = applyResistancesAndImmunities(s1.params_20.get(1).deref().get(), damage, 1);
      final ScriptState<?> a3 = scriptStatePtrArr_800bc1c0.get(s1.params_20.get(1).deref().get()).deref();
      final BattleObject27c v1 = a3.innerStruct_00.derefAs(BattleObject27c.class);
      final int element = spellStats_800fa0b8.get(scriptStatePtrArr_800bc1c0.get(s1.params_20.get(0).deref().get()).deref().innerStruct_00.derefAs(BattleObject27c.class).spellId_4e.get()).element_08.get();

      //LAB_800f27c8
      if((a3.ui_60.get() & 0x4L) == 0 && (v1.elementalResistanceFlag_20.get() & element) != 0) {
        damage = damage >> 1;
      }

      //LAB_800f27dc
      if((v1.elementalImmunityFlag_22.get() & element) != 0) {
        damage = 0;
      }

      //LAB_800f27e8
    }

    //LAB_800f27ec
    s1.params_20.get(3).deref().set(damage);
    s1.params_20.get(4).deref().set(FUN_800f7c5c(s1.params_20.get(0).deref().get(), s1.params_20.get(1).deref().get(), 1));
    return 0;
  }

  @Method(0x800f2838L)
  public static long FUN_800f2838(final RunningScript s1) {
    final BattleObject27c s0 = scriptStatePtrArr_800bc1c0.get(s1.params_20.get(0).deref().get()).deref().innerStruct_00.derefAs(BattleObject27c.class);
    clearTempWeaponAndSpellStats(s0);
    setTempWeaponStats(s1.params_20.get(0).deref().get());
    int a1 = Math.max(1, FUN_800f946c(s0, FUN_800f204c(s1.params_20.get(0).deref().get(), s1.params_20.get(1).deref().get(), 0), 1));

    //LAB_800f28c8
    if((s0.dragoonFlag_0e.get() & 0x800L) != 0) {
      s0.dragoonFlag_0e.and(0xf7ff);
    } else {
      //LAB_800f28e4
      a1 = applyResistancesAndImmunities(s1.params_20.get(1).deref().get(), a1, 1);
      final long a3 = scriptStatePtrArr_800bc1c0.get(s1.params_20.get(0).deref().get()).deref()._d4.get() >>> 16;
      final ScriptState<?> defenderState = scriptStatePtrArr_800bc1c0.get(s1.params_20.get(1).deref().get()).deref();
      final BattleObject27c defender = defenderState.innerStruct_00.derefAs(BattleObject27c.class);

      //LAB_800f294c
      if((defenderState.ui_60.get() & 0x4L) == 0 && (a3 & defender.elementalResistanceFlag_20.get()) != 0) {
        a1 = a1 >> 1;
      }

      //LAB_800f2960
      if((a3 & defender.elementalImmunityFlag_22.get()) != 0) {
        a1 = 0;
      }
    }

    //LAB_800f2970
    s1.params_20.get(3).deref().set(a1);
    s1.params_20.get(4).deref().set(FUN_800f7c5c(s1.params_20.get(0).deref().get(), s1.params_20.get(1).deref().get(), 0x2L));
    FUN_800f8854(s1.params_20.get(0).deref().get(), s1.params_20.get(1).deref().get(), 0x1L);
    return 0;
  }

  @Method(0x800f29d4L)
  public static int applyResistancesAndImmunities(final int defenderBobjIndex, int damage, final int attackType) {
    final int[] specialEnemyFlags = {0x02, 0x01}; // Physical - 1 damage, magical - 1 damage
    final int[] resistanceStats = {0x88, 0x89}; // Physical resistance, magical resistance
    final int[] immunityStats = {0x86, 0x87}; // Physical immunity, magical immunity
    final int[] tempImmunityStats = {0x60, 0x61}; // Temp physical immunity, temp magical immunity

    final ScriptState<?> defenderState = scriptStatePtrArr_800bc1c0.get(defenderBobjIndex).deref();
    final BattleObject27c defender = defenderState.innerStruct_00.derefAs(BattleObject27c.class);

    if((defenderState.ui_60.get() & 0x4L) != 0 && (defender.damageReductionFlags_6e.get() & specialEnemyFlags[attackType]) != 0) {
      damage = 1;
    }

    //LAB_800f2a64
    //LAB_800f2a68
    if(damage >= 9999) {
      damage = 9999;
    }

    //LAB_800f2a74
    if(defender.all_04.get(resistanceStats[attackType]).get() != 0) {
      damage /= 2;
    }

    //LAB_800f2aa0
    if(defender.all_04.get(immunityStats[attackType]).get() != 0) {
      damage = 0;
    }

    //LAB_800f2ac4
    if(defender.all_04.get(tempImmunityStats[attackType]).get() != 0) {
      damage = 0;
    }

    //LAB_800f2aec
    return damage;
  }

  @Method(0x800f2af4L)
  public static int calculateAdditionDamage(final int attackerBobjIndex, final int defenderBobjIndex) {
    final ScriptState<BattleObject27c> attackerState = scriptStatePtrArr_800bc1c0.get(attackerBobjIndex).derefAs(ScriptState.classFor(BattleObject27c.class));
    final ScriptState<BattleObject27c> defenderState = scriptStatePtrArr_800bc1c0.get(defenderBobjIndex).derefAs(ScriptState.classFor(BattleObject27c.class));

    final BattleObject27c attacker = attackerState.innerStruct_00.deref();
    final BattleObject27c defender = defenderState.innerStruct_00.deref();
    int attack = attacker.attack_34.get();

    if(attacker.selectedAddition_58.get() == -1) { // No addition (Shana/???)
      //LAB_800f2c24
      if((attackerState.ui_60.get() & 0x2L) != 0) { // Is dragoon
        //LAB_800f2c4c
        attack = attack * attacker.dragoonAttack_ac.get() / 100;
      }
    } else if(attacker.additionHits_56.get() > 0) {
      //LAB_800f2b94
      int additionMultiplier = 0;
      for(int i = 0; i < attacker.additionHits_56.get(); i++) {
        additionMultiplier += getHitMultiplier(attacker.charSlot_276.get(), i, 0x4L);
      }

      //LAB_800f2bb4
      final int damageMultiplier;
      if((attackerState.ui_60.get() & 0x2L) != 0) { // Is dragoon
        damageMultiplier = attacker.dragoonAttack_ac.get();
      } else {
        //LAB_800f2bec
        damageMultiplier = attacker.additionDamageMultiplier_11c.get() + 100;
      }

      //LAB_800f2bfc
      additionMultiplier = additionMultiplier * damageMultiplier / 100;
      attack = attack * additionMultiplier / 100;
    }

    //LAB_800f2c6c
    //LAB_800f2c70
    int defence = defender.defence_38.get();
    if((defenderState.ui_60.get() & 0x2L) != 0) {
      defence = defence * defender.dragoonDefence_b0.get() / 100;
    }

    //LAB_800f2ccc
    return round((attacker.level_04.get() + 5) * attack * 5 / (float)defence);
  }

  @Method(0x800f2d48L)
  public static int FUN_800f2d48(final int scriptIndex1, final int scriptIndex2) {
    final ScriptState<BattleObject27c> state1 = scriptStatePtrArr_800bc1c0.get(scriptIndex1).derefAs(ScriptState.classFor(BattleObject27c.class));
    final ScriptState<BattleObject27c> state2 = scriptStatePtrArr_800bc1c0.get(scriptIndex2).derefAs(ScriptState.classFor(BattleObject27c.class));
    final BattleObject27c combatant1 = state1.innerStruct_00.deref();
    final BattleObject27c combatant2 = state2.innerStruct_00.deref();

    final int atk = combatant1.attack_34.get() + spellStats_800fa0b8.get(combatant1.spellId_4e.get()).multi_04.get();

    //TODO impossible condition with code that does nothing?
    if((state2.ui_60.get() & 0x4L) == 0x1L) {
      //LAB_800f2ddc
      //LAB_800f2dec
      for(int i = 0; i < Math.min(3, charCount_800c677c.get()); i++) {
        if(scriptStatePtrArr_800bc1c0.get(_8006e398.charBobjIndices_e40.get(i).get()).deref().innerStruct_00.derefAs(BattleObject27c.class).charIndex_272.get() == 0) {
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
  public static int playerMagicAttack(final int attackerBobjIndex, final int defenderBobjIndex, final int attackType) {
    final BattleObject27c attacker = scriptStatePtrArr_800bc1c0.get(attackerBobjIndex).deref().innerStruct_00.derefAs(BattleObject27c.class);
    int matk = attacker.magicAttack_36.get();
    if(attackType == 1) {
      matk += spellStats_800fa0b8.get(attacker.spellId_4e.get()).multi_04.get();
    } else {
      //LAB_800f2ef8
      matk += attacker.itemUu1_de.get();
    }

    //LAB_800f2f04
    if((scriptStatePtrArr_800bc1c0.get(attackerBobjIndex).deref().ui_60.get() & 0x2L) != 0) {
      matk = matk * attacker.dragoonMagic_ae.get() / 100;
    }

    //LAB_800f2f5c
    final long a1 = scriptStatePtrArr_800bc1c0.get(defenderBobjIndex).deref().ui_60.get();
    final BattleObject27c defender = scriptStatePtrArr_800bc1c0.get(defenderBobjIndex).deref().innerStruct_00.derefAs(BattleObject27c.class);
    int mdef = defender.magicDefence_3a.get();

    if((a1 & 0x4L) != 0x1L && (a1 & 0x2L) != 0) {
      mdef = mdef * defender.dragoonMagicDefence_b2.get() / 100;
    }

    //LAB_800f2fb4
    return (attacker.level_04.get() + 5) * matk * 5 / mdef;
  }

  @Method(0x800f2fe0L)
  public static int FUN_800f2fe0(final long a0, final long a1, final long a2) {
    long v1;
    long s0;
    long s2;
    long s7;
    int s6 = 0;
    long s4 = 0x1L;
    long s5 = 0x1L;
    final long s3 = _800c70a4.offset(a2 * 0x4L).get();
    int s1 = 0;

    //LAB_800f3094
    for(s7 = 0; s7 < s3; s7++) {
      if((a0 & s5) != 0) {
        //LAB_800f30b0
        for(s2 = 0; s2 < s3; s2++) {
          if((a1 & s4) != 0) {
            s0 = s3 - (getTargetEnemyElement(a0 & s5) + 0x1L);
            v1 = s3 - (getTargetEnemyElement(a1 & s4) + 0x1L);

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
                s1 += _800fb4b4.offset(v1 * 0x10L).offset(2, s0 * 0x2L).getSigned();
              }

              case 3 -> {
                //LAB_800f3124
                s1 += _800fb534.offset(v1 * 0x6L).offset(2, s0 * 0x2L).getSigned();
              }

              case 4 -> {
                //LAB_800f3144
                s1 += _800fb548.offset(v1 * 0x6L).offset(2, s0 * 0x2L).getSigned();
              }

              case 5 -> {
                //LAB_800f3164
                //LAB_800f317c
                s1 += _800fb55c.offset(v1 * 0x10L).offset(2, s0 * 0x2L).getSigned();
              }

              default ->
                //LAB_800f3188
                s1 += 100;
            }

            //LAB_800f318c
            s6++;
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
      s1 = 100;
    } else {
      //LAB_800f31c4
      s1 = s1 / s6;
    }

    //LAB_800f31cc
    return s1;
  }

  @Method(0x800f3204L)
  public static void FUN_800f3204(final long a0) {
    final ScriptState<BattleObject27c> state = scriptStatePtrArr_800bc1c0.get((int)a0).derefAs(ScriptState.classFor(BattleObject27c.class));
    final BattleObject27c a1 = state.innerStruct_00.deref();

    final short spPerPhysicalHit;
    final short mpPerPhysicalHit;
    final short spPerMagicalHit;
    final short mpPerMagicalHit;
    final int speed;
    if((state.ui_60.get() & 0x4L) != 0) {
      spPerPhysicalHit = 0;
      mpPerPhysicalHit = 0;
      spPerMagicalHit = 0;
      mpPerMagicalHit = 0;
      speed = a1.originalSpeed_64.get();
    } else {
      //LAB_800f3244
      final ActiveStatsa0 stats = stats_800be5f8.get(a1.charIndex_272.get());
      spPerPhysicalHit = stats.spPerPhysicalHit_4e.get();
      mpPerPhysicalHit = stats.mpPerPhysicalHit_50.get();
      spPerMagicalHit = stats.spPerMagicalHit_52.get();
      mpPerMagicalHit = stats.mpPerMagicalHit_54.get();
      speed = stats.gearSpeed_86.get() + stats.bodySpeed_69.get();
    }

    //LAB_800f327c
    if(a1.speedUpTurns_c8.get() != 0) {
      a1.speed_32.set(speed * 2);
    } else {
      a1.speed_32.set(speed);
    }

    //LAB_800f3294
    if(a1.speedDownTurns_ca.get() != 0) {
      a1.speed_32.set(speed >>> 1);
    }

    //LAB_800f32ac
    if(a1.tempSpPerPhysicalHitTurns_cd.get() != 0) {
      a1.spPerPhysicalHit_12a.set((short)(spPerPhysicalHit + a1.tempSpPerPhysicalHit_cc.get()));
    } else {
      a1.spPerPhysicalHit_12a.set(spPerPhysicalHit);
    }

    //LAB_800f32d4
    if(a1.tempMpPerPhysicalHitTurns_cf.get() != 0) {
      a1.mpPerPhysicalHit_12c.set((short)(mpPerPhysicalHit + a1.tempMpPerPhysicalHit_ce.get()));
    } else {
      a1.mpPerPhysicalHit_12c.set(mpPerPhysicalHit);
    }

    //LAB_800f32fc
    if(a1.tempSpPerMagicalHitTurns_d1.get() != 0) {
      a1.itemSpPerMagicalHit_12e.set((short)(spPerMagicalHit + a1.tempSpPerMagicalHit_d0.get()));
    } else {
      a1.itemSpPerMagicalHit_12e.set(spPerMagicalHit);
    }

    //LAB_800f3324
    if(a1.tempMpPerMagicalHitTurns_d3.get() != 0) {
      a1.mpPerMagicalHit_130.set((short)(mpPerMagicalHit + a1.tempMpPerMagicalHit_d2.get()));
    } else {
      a1.mpPerMagicalHit_130.set(mpPerMagicalHit);
    }

    //LAB_800f334c
  }

  @Method(0x800f3354L)
  public static void calculateFloatingNumberRender(final int numIndex, final long onHitTextType, final long onHitClutCol, final long rawDamage, final int x, final int y, long a6, final long onHitClutRow) {
    final short[] damageDigits = new short[5];

    final byte floatingTextType;  // 0=floating numbers, 1=MP cost, 2=miss
    final byte clutCol;  //TODO: confirm this, it may not be this exactly
    final byte clutRow; //TODO: confirm this, it may not be this exactly
    if((int)rawDamage != -1) {
      floatingTextType = (byte)onHitTextType;
      clutCol = (byte)onHitClutCol;
      clutRow = (byte)onHitClutRow;
    } else {
      floatingTextType = 2;
      clutCol = 2;
      clutRow = 14;
    }

    //LAB_800f34d4
    final FloatingNumberC4 num = floatingNumbers_800c6b5c.deref().get(numIndex);
    num.state_00.set(0);
    num.flags_02.set(0);
    num.bobjIndex_04.set(-1);
    num.translucent_08.set(false);
    num.b_0c.set(0x80);
    num.g_0d.set(0x80);
    num.r_0e.set(0x80);
    num._18.set(-1);
    num._14.set(-1);

    //LAB_800f3528
    for(int i = 0; i < 5; i++) {
      num.digits_24.get(i)._00.set(0);
      num.digits_24.get(i)._04.set(0);
      num.digits_24.get(i)._08.set(0);
      num.digits_24.get(i).digit_0c.set((short)-1);
      num.digits_24.get(i)._1c.set(0);
    }

    num.state_00.set(1);
    if(a6 == 0) {
      num.flags_02.or(0x1);
    }

    //LAB_800f3588
    num.flags_02.or(0x8000);
    num.translucent_08.set(false);
    num.b_0c.set(0x80);
    num.g_0d.set(0x80);
    num.r_0e.set(0x80);
    num._10.set(clutCol);

    if(clutCol == 2 && a6 == 0) {
      a6 = 60 / vsyncMode_8007a3b8.get() * 2;
    }

    //LAB_800f35dc
    //LAB_800f35e4
    //LAB_800f3608
    //Clamp damage to 0-max
    int damage = Math.max(0, Math.min((int)rawDamage, 99999));

    //LAB_800f3614
    num.x_1c.set(x);
    num.y_20.set(y);

    //LAB_800f3654
    for(int i = 0; i < 5; i++) {
      num.digits_24.get(i).digit_0c.set((short)-1);
      damageDigits[i] = -1;
    }

    //LAB_800f36a0
    //Sets what places to render
    int currDigitPlace = 10000;
    int currDigit;
    for(int i = 0; i < 5; i++) {
      currDigit = damage / currDigitPlace;
      damage = damage % currDigitPlace;
      damageDigits[i] = (short)currDigit;
      currDigitPlace = currDigitPlace / 10;
    }

    //LAB_800f36dc
    //LAB_800f36ec
    int digitIdx;
    for(digitIdx = 0; digitIdx < 4; digitIdx++) {
      if(damageDigits[digitIdx] != 0) {
        break;
      }
    }

    //LAB_800f370c
    //LAB_800f3710
    int displayPosX;
    if(floatingTextType == 1) {
      //LAB_800f3738
      displayPosX = -(5 - digitIdx) * 5 / 2;
    } else if(floatingTextType == 2) {
      //LAB_800f3758
      displayPosX = -18;
    } else {
      //LAB_800f372c
      displayPosX = -(5 - digitIdx) * 4;
    }

    //LAB_800f375c
    //LAB_800f37ac
    int digitStructIdx;
    for(digitStructIdx = 0; digitStructIdx < 5; digitStructIdx++) {
      final FloatingNumberC4Sub20 digitStruct = num.digits_24.get(digitStructIdx);
      digitStruct._00.set(0x8000L);
      digitStruct.y_10.set((short)0);

      if(clutCol == 2) {
        digitStruct._00.set(0);
        digitStruct._04.set(digitStructIdx);
        digitStruct._08.set(0);
      }

      //LAB_800f37d8
      if(floatingTextType == 1) {
        //LAB_800f382c
        digitStruct.x_0e.set((short)displayPosX);
        digitStruct.u_12.set((int)_800c7028.offset(damageDigits[digitIdx] * 2).get());
        digitStruct.v_14.set(0x20);
        digitStruct.texW_16.set(0x8);
        digitStruct.texH_18.set(0x8);
        displayPosX += 5;
      } else if(floatingTextType == 2) {
        //LAB_800f386c
        digitStruct.x_0e.set((short)displayPosX);
        digitStruct.u_12.set(0x48);
        digitStruct.v_14.set(0x80);
        digitStruct.texW_16.set(0x24);
        digitStruct.texH_18.set(0x10);
        displayPosX += 36;
      } else {
        //LAB_800f37f4
        digitStruct.x_0e.set((short)displayPosX);
        digitStruct.u_12.set((int)_800c70e0.offset(damageDigits[digitIdx] * 2).get());
        digitStruct.v_14.set(0x28);
        digitStruct.texW_16.set(0x8);
        digitStruct.texH_18.set(0x10);
        displayPosX += 8;
      }

      //LAB_800f3898
      digitStruct.digit_0c.set(damageDigits[digitIdx]);
      digitStruct._1a.set((short)_800c70f4.offset(clutRow * 2).get());
      digitStruct._1c.set(0x1000);

      digitIdx++;
      if(digitIdx >= 5) {
        break;
      }
    }

    //LAB_800f38e8
    num._14.set(digitStructIdx + 12); //TODO: ID duration meaning
    num._18.set((int)(a6 + 4)); //TODO: ID duration meaning
  }

  @Method(0x800f3940L)
  public static void FUN_800f3940() {
    //LAB_800f3978
    for(int i = 0; i < 12; i++) {
      final FloatingNumberC4 num = floatingNumbers_800c6b5c.deref().get(i);
      if((num.flags_02.get() & 0x8000) != 0) {
        if(num.state_00.get() != 0) {
          final int bobjIndex = num.bobjIndex_04.get();

          if(bobjIndex != -1) {
            final ScriptState<BattleObject27c> state = scriptStatePtrArr_800bc1c0.get(bobjIndex).derefAs(ScriptState.classFor(BattleObject27c.class));
            final BattleObject27c bobj = state.innerStruct_00.deref();

            final short x;
            final short y;
            final short z;
            if((state.ui_60.get() & 0x4L) != 0) {
              x = (short)(-bobj._78.getZ() * 100);
              y = (short)(-bobj._78.getY() * 100);
              z = (short)(-bobj._78.getX() * 100);
            } else {
              //LAB_800f3a3c
              x = 0;
              y = -640;
              z = 0;
            }

            //LAB_800f3a44
            final DVECTOR screenCoords = perspectiveTransformXyz(bobj.model_148, x, y, z);
            num.x_1c.set(clampX(screenCoords.getX() + centreScreenX_1f8003dc.get()));
            num.y_20.set(clampY(screenCoords.getY() + centreScreenY_1f8003de.get()));
          }

          //LAB_800f3ac8
          final int state = num.state_00.get();

          if(state == 1) {
            //LAB_800f3b24
            if(num._10.get() == 0x2L) {
              num.state_00.set(2);
            } else {
              //LAB_800f3b44
              num.state_00.set(98);
            }
          } else if(state == 2) {
            //LAB_800f3b50
            for(int n = 0; n < 5; n++) {
              final FloatingNumberC4Sub20 a1 = num.digits_24.get(n);

              if(a1.digit_0c.get() == -1) {
                break;
              }

              final long a0 = a1._00.get();

              if((a0 & 0x1) != 0) {
                if((a0 & 0x2) != 0) {
                  if(a1._08.get() < 5) {
                    a1.y_10.add((short)a1._08.get());
                    a1._08.incr();
                  }
                } else {
                  //LAB_800f3bb0
                  a1._00.or(0x8002L);
                  a1._04.set(a1.y_10.get());
                  a1._08.set(-4);
                }
              } else {
                //LAB_800f3bc8
                if(a1._08.get() == a1._04.get()) {
                  a1._00.or(0x1L);
                }

                //LAB_800f3be0
                a1._08.incr();
              }
            }

            //LAB_800f3c00
            num._14.decr();
            if(num._14.get() <= 0) {
              num.state_00.set(98);
              num._14.set(num._18.get());
            }
          } else if(state == 97) {
            //LAB_800f3c34
            if(num._14.get() <= 0) {
              num.state_00.set(100);
            } else {
              //LAB_800f3c50
              num._14.decr();
              // Monochromify
              final int b = num.b_0c.get();
              final int colour = (b - (num._18.get() & 0xff)) & 0xff;
              num.b_0c.set(colour);
              num.g_0d.set(colour);
              num.r_0e.set(colour);
            }
          } else if(state == 100) {
            //LAB_800f3d38
            num.state_00.set(0);
            num.flags_02.set(0);
            num.bobjIndex_04.set(-1);
            num.translucent_08.set(false);
            num.b_0c.set(0x80);
            num.g_0d.set(0x80);
            num.r_0e.set(0x80);
            num._14.set(-1);
            num._18.set(-1);

            //LAB_800f3d60
            for(int n = 0; n < 5; n++) {
              final FloatingNumberC4Sub20 v1 = num.digits_24.get(n);
              v1._00.set(0);
              v1._04.set(0);
              v1._08.set(0);
              v1.digit_0c.set((short)-1);
              v1._1c.set(0);
            }
            //LAB_800f3b04
          } else if(state < 99) {
            //LAB_800f3c88
            if((num.flags_02.get() & 0x1) != 0) {
              num.state_00.set(99);
            } else {
              //LAB_800f3ca4
              num._14.decr();

              if(num._14.get() <= 0) {
                final long v1 = num._10.get();

                if(v1 > 0 && v1 < 3) {
                  num.state_00.set(97);
                  num.translucent_08.set(true);
                  num.b_0c.set(0x60);
                  num.g_0d.set(0x60);
                  num.r_0e.set(0x60);

                  final int a2 = 60 / (int)vsyncMode_8007a3b8.get() / 2;
                  num._14.set(a2);
                  num._18.set(96 / a2);
                } else {
                  //LAB_800f3d24
                  //LAB_800f3d2c
                  num.state_00.set(100);
                }
              }
            }
          }
        }
      }
    }
  }

  @Method(0x800f3dbcL)
  public static void drawFloatingNumbers() {
    //LAB_800f3e20
    for(int i = 0; i < 12; i++) {
      final FloatingNumberC4 num = floatingNumbers_800c6b5c.deref().get(i);

      if((num.flags_02.get() & 0x8000) != 0) {
        if(num.state_00.get() != 0) {
          final boolean translucent = num.translucent_08.get();
          final int r = num.r_0e.get();
          final int g = num.g_0d.get();
          final int b = num.b_0c.get();

          //LAB_800f3e80
          for(int s7 = 0; s7 < 5; s7++) {
            final FloatingNumberC4Sub20 digit = num.digits_24.get(s7);

            if(digit.digit_0c.get() == -1) {
              break;
            }

            if((digit._00.get() & 0x8000) != 0) {
              //LAB_800f3ec0
              for(int s3 = 1; s3 < 3; s3++) {
                final long packet = gpuPacketAddr_1f8003d8.get();
                setGp0_2c(packet); // Textured quad, opaque, texture blending
                gpuLinkedListSetCommandTransparency(packet, translucent); // Enable translucency?
                gpuPacketAddr_1f8003d8.addu(0x28L);

                MEMORY.ref(1, packet).offset(0x4L).setu(r); // R
                MEMORY.ref(1, packet).offset(0x5L).setu(g); // G
                MEMORY.ref(1, packet).offset(0x6L).setu(b); // B
                final long a1 = num.x_1c.get() - centreScreenX_1f8003dc.get();
                long v0 = digit.x_0e.get() + a1;
                MEMORY.ref(2, packet).offset(0x18L).setu(v0); // X2
                MEMORY.ref(2, packet).offset(0x8L).setu(v0); // X0
                long v1 = digit.x_0e.get() + digit.texW_16.get() + a1;
                MEMORY.ref(2, packet).offset(0x20L).setu(v1); // X3
                MEMORY.ref(2, packet).offset(0x10L).setu(v1); // X1
                long a2 = num.y_20.get() - centreScreenY_1f8003de.get();
                v0 = digit.y_10.get() + a2;
                MEMORY.ref(2, packet).offset(0x12L).setu(v0); // Y1
                MEMORY.ref(2, packet).offset(0xaL).setu(v0); // Y0
                v1 = digit.y_10.get() + digit.texH_18.get() + a2;
                MEMORY.ref(2, packet).offset(0x22L).setu(v1); // Y3
                MEMORY.ref(2, packet).offset(0x1aL).setu(v1); // Y2
                v0 = digit.u_12.get();
                MEMORY.ref(1, packet).offset(0x1cL).setu(v0); // U2
                MEMORY.ref(1, packet).offset(0xcL).setu(v0); // U0
                v1 = digit.u_12.get() + digit.texW_16.get();
                MEMORY.ref(1, packet).offset(0x24L).setu(v1); // U3
                MEMORY.ref(1, packet).offset(0x14L).setu(v1); // U1
                v0 = digit.v_14.get();
                MEMORY.ref(1, packet).offset(0x15L).setu(v0); // V1
                MEMORY.ref(1, packet).offset(0xdL).setu(v0); // V0
                v1 = digit.v_14.get() + digit.texH_18.get();
                MEMORY.ref(1, packet).offset(0x25L).setu(v1); // V3
                MEMORY.ref(1, packet).offset(0x1dL).setu(v1); // V2
                v1 = digit._1a.get();

                final long t1;
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
                final long a3 = t0;

                //LAB_800f4058
                t0 = t0 / 0x10L;

                //LAB_800f4068
                a2 = (_800c7114.offset(2, (t1 * 0x2L + 0x1L) * 0x4L).get() + a3 % 0x10L) * 0x40L; // TODO wtf is this
                v0 = _800c7114.offset(4, t1 * 0x8L).get() + t0 * 0x10L;
                v0 = v0 & 0x3f0L;
                v0 = (int)v0 >> 4;
                a2 = a2 | v0;
                MEMORY.ref(2, packet).offset(0xeL).setu(a2); // CLUT
                final int tpage = GetTPage(TexPageBpp.BITS_4, TexPageTrans.of(s3), 704, 496);
                MEMORY.ref(2, packet).offset(0x16L).setu(tpage); // TPAGE
                queueGpuPacket(tags_1f8003d0.getPointer() + 0x1cL, packet);

                if((num.state_00.get() & 97) == 0) {
                  //LAB_800f4118
                  break;
                }

                //LAB_800f4110
              }
            }
          }
        }
      }
      //LAB_800f4134
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
    short x = 63;

    //LAB_800f4220
    for(int charSlot = 0; charSlot < 3; charSlot++) {
      final BattleDisplayStats144 displayStats = displayStats_800c6c2c.deref().get(charSlot);
      final BattleStruct3c v1 = _800c6c40.get(charSlot);

      if(v1.charIndex_00.get() != -1) {
        v1._08.set(x);
        displayStats.x_00.set(x);
      }

      //LAB_800f4238
      x += 94;
    }
  }

  @Method(0x800f4268L)
  public static void setFloatingNumCoordsAndRender(final long s2, final long s3, final long s4) {
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
    final DVECTOR screenCoords = perspectiveTransformXyz(a0.model_148, x, y, z);

    //LAB_800f4394
    FUN_800f89f4(s2, 0, 0x2L, s3, clampX(screenCoords.getX() + centreScreenX_1f8003dc.get()), clampY(screenCoords.getY() + centreScreenY_1f8003de.get()), 60 / vsyncMode_8007a3b8.get() / 4, s4);
  }

  @Method(0x800f43dcL)
  public static long FUN_800f43dc(final RunningScript script) {
    //LAB_800f43f8
    //LAB_800f4410
    int charSlot;
    for(charSlot = 0; charSlot < charCount_800c677c.get(); charSlot++) {
      if(_8006e398.charBobjIndices_e40.get(charSlot).get() == script.params_20.get(0).deref().get()) {
        break;
      }
    }

    //LAB_800f4430
    final BattleObject27c struct = scriptStatePtrArr_800bc1c0.get(script.params_20.get(0).deref().get()).deref().innerStruct_00.derefAs(BattleObject27c.class);
    struct.sp_0a.add((short)script.params_20.get(1).deref().get());
    spGained_800bc950.get(charSlot).add(script.params_20.get(1).deref().get());

    if(struct.sp_0a.get() >= struct.dlevel_06.get() * 100) {
      struct.sp_0a.set((short)(struct.dlevel_06.get() * 100));
    }

    //LAB_800f44d4
    if(struct.sp_0a.get() > 500) {
      struct.sp_0a.set((short)500);
    }

    //LAB_800f44ec
    if(struct.sp_0a.get() < 0) {
      struct.sp_0a.set((short)0);
    }

    //LAB_800f4500
    script.params_20.get(2).deref().set(struct.sp_0a.get());
    return 0;
  }

  @Method(0x800f4518L)
  public static long FUN_800f4518(final RunningScript a0) {
    //LAB_800f4534
    //LAB_800f454c
    int i;
    for(i = 0; i < charCount_800c677c.get(); i++) {
      if(_8006e398.charBobjIndices_e40.get(i).get() == a0.params_20.get(0).deref().get()) {
        break;
      }
    }

    //LAB_800f456c
    final BattleStruct3c a2 = _800c6c40.get(i);
    a2._0c.set((short)0);
    a2._0e.set((short)a0.params_20.get(1).deref().get());

    final BattleObject27c bobj = scriptStatePtrArr_800bc1c0.get(a0.params_20.get(0).deref().get()).deref().innerStruct_00.derefAs(BattleObject27c.class);
    bobj.sp_0a.sub((short)a0.params_20.get(2).deref().get());

    if(bobj.sp_0a.get() <= 0) {
      bobj.sp_0a.set((short)0);
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
      t0 = scriptStatePtrArr_800bc1c0.get(_8006e398.charBobjIndices_e40.get(i).get()).deref().innerStruct_00.derefAs(BattleObject27c.class);

      if(t0.charIndex_272.get() == _800c6b60.deref().charIndex_08.get()) {
        break;
      }
    }

    //LAB_800f47ac
    t0.spellId_4e.set((short)a2);

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
    final long v0;
    long v1;
    BattleObject27c a1 = null;
    long t0;
    final long t5;
    v0 = 0x800c_0000L;
    t5 = v0 + 0x7148L;
    final long[] sp0x10 = new long[8];
    for(int i = 0; i < sp0x10.length; i++) {
      sp0x10[i] = MEMORY.ref(4, t5).offset(i * 0x4L).get();
    }

    t0 = s0.params_20.get(0).deref().get();

    final BattleMenuStruct58 struct58 = battleMenu_800c6c34.deref();

    //LAB_800f489c
    for(int a0 = 0; a0 < charCount_800c677c.get(); a0++) {
      a1 = scriptStatePtrArr_800bc1c0.get(_8006e398.charBobjIndices_e40.get(a0).get()).deref().innerStruct_00.derefAs(BattleObject27c.class);

      if(struct58.charIndex_04.get() == a1.charIndex_272.get()) {
        break;
      }
    }

    //LAB_800f48d8
    if((a1.specialEffectFlag_14.get() & 0x8L) != 0) { // "Attack all"
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
    v0.count_22.set((short)0);
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
      a2.count_22.set((short)_800c6b70.getSigned());
    } else if(v1 == 0x1L) {
      //LAB_800f4abc
      //LAB_800f4ae0
      int charSlot;
      for(charSlot = 0; charSlot < charCount_800c677c.get(); charSlot++) {
        if(dragoonSpells_800c6960.get(charSlot).charIndex_00.get() == a2.charIndex_08.get()) {
          break;
        }
      }

      //LAB_800f4b00
      //LAB_800f4b18
      short spellIndex;
      for(spellIndex = 0; spellIndex < 8; spellIndex++) {
        if(dragoonSpells_800c6960.get(charSlot).spellIndex_01.get(spellIndex).get() == -1) {
          break;
        }
      }

      //LAB_800f4b3c
      a2.count_22.set(spellIndex);
    } else if(v1 == 0x2L) {
      //LAB_800f4b4c
      a2.count_22.set((short)0);
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
    final long a0;
    long a1;
    int s0;
    BattleObject27c data;
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

          if(structa4.count_22.get() - 1 < structa4._24.get() + structa4._1e.get()) {
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

          if(structa4.count_22.get() - 1 >= structa4._1e.get() + 6) {
            structa4._24.set((short)6);
          } else {
            //LAB_800f4d8c
            structa4._24.set((short)(structa4.count_22.get() - (structa4._1e.get() + 1)));
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
          if(structa4._1e.get() + 6 >= structa4.count_22.get() - 1) {
            break;
          }

          structa4._1e.add((short)7);
          structa4._20.sub((short)98);

          if(structa4._1e.get() + 6 >= structa4.count_22.get() - 1) {
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
          if(structa4._24.get() != structa4.count_22.get() - 1) {
            if(structa4._1e.get() + structa4._24.get() + 1 < structa4.count_22.get()) {
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
          int s2;
          do {
            s2 = _8006e398.charBobjIndices_e40.get(charSlot).get();
            data = scriptStatePtrArr_800bc1c0.get(s2).deref().innerStruct_00.derefAs(BattleObject27c.class);

            if(structa4.charIndex_08.get() == data.charIndex_272.get()) {
              //LAB_800f503c
              _800c6980.setu(charSlot);
              break;
            }

            charSlot++;
          } while(charSlot < charCount_800c677c.get());

          //LAB_800f50b8
          if(structa4._0a.get() == 0) {
            data.weaponId_52.set(structa4._1c.get());
            setTempWeaponStats(s2);

            if((data.itemTarget_d4.get() & 0x4L) != 0) {
              _800c6b68.setu(0x1L);
            } else {
              //LAB_800f5100
              _800c6b68.setu(0);
            }

            //LAB_800f5108
            if((data.itemTarget_d4.get() & 0x2L) != 0) {
              _800c69c8.setu(0x1L);
            } else {
              //LAB_800f5128
              _800c69c8.setu(0);
            }
          } else {
            //LAB_800f5134
            final BattleObject27c s1 = FUN_800f9e50(structa4._1c.get());

            if(s1.mp_0c.get() < s1.spellMp_a0.get()) {
              //LAB_800f5160
              //LAB_800f5168
              playSound(0, 3, 0, 0, (short)0, (short)0);
              break;
            }

            //LAB_800f517c
            final FloatingNumberC4 num = floatingNumbers_800c6b5c.deref().get(0);
            num.state_00.set(0);
            num.flags_02.set(0);
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
          data = scriptStatePtrArr_800bc1c0.get(_8006e398.charBobjIndices_e40.get(charSlot).get()).deref().innerStruct_00.derefAs(BattleObject27c.class);

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
            takeItem(((structa4._1c.get() & 0xff) - 0x40) & 0xff);
          }

          //LAB_800f545c
          if(structa4._0a.get() == 0x1L) {
            data.mp_0c.sub(data.spellMp_a0.get());
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
          calculateFloatingNumberRender(0, 0x1L, 0, FUN_800f9e50(structa4._1c.get()).spellMp_a0.get(), 280, 135, 0, 0x1L);
        }
      }

      case 7 -> {
        _800c69c8.setu(0);
        _800c6b68.setu(0);
        structa4._a0.set(-1);
        structa4._00.set((short)9);
        structa4._12.set(0);
        structa4._10.set(0);
        structa4._02.and(0xfffc);
        final FloatingNumberC4 num = floatingNumbers_800c6b5c.deref().get(0);
        num.state_00.set(0);
        num.flags_02.set(0);
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
  public static int FUN_800f56c4() {
    final BttlStructa4 a1 = _800c6b60.deref();
    final short v1 = a1._0a.get();

    if(v1 == 0) {
      //LAB_800f56f0
      return (int)(_800c6988.offset((a1._24.get() + a1._1e.get()) * 0x2L).get() - 0xc0);
    }

    if(v1 == 1) {
      //LAB_800f5718
      //LAB_800f5740
      int charSlot;
      for(charSlot = 0; charSlot < charCount_800c677c.get(); charSlot++) {
        if(dragoonSpells_800c6960.get(charSlot).charIndex_00.get() == a1.charIndex_08.get()) {
          break;
        }
      }

      //LAB_800f5778
      final BttlStructa4 a2 = _800c6b60.deref();
      int spellIndex = dragoonSpells_800c6960.get(charSlot).spellIndex_01.get(a2._24.get() + a2._1e.get()).get();
      if(a2.charIndex_08.get() == 8) {
        if(spellIndex == 65) {
          spellIndex = 10;
        }

        //LAB_800f57d4
        if(spellIndex == 66) {
          spellIndex = 11;
        }

        //LAB_800f57e0
        if(spellIndex == 67) {
          spellIndex = 12;
        }
      }

      return spellIndex;
    }

    throw new RuntimeException("Undefined a0");
  }

  @Method(0x800f57f8L)
  public static void FUN_800f57f8(final int a0) {
    long v0;
    long a1;

    final BttlStructa4 structa4 = _800c6b60.deref();

    int y1 = structa4._20.get();
    final int y2 = structa4._1a.get();
    final long sp68 = structa4._06.get();

    //LAB_800f5860
    int charSlot;
    for(charSlot = 0; charSlot < charCount_800c677c.get(); charSlot++) {
      if(dragoonSpells_800c6960.get(charSlot).charIndex_00.get() == structa4.charIndex_08.get()) {
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
    for(int spellSlot = 0; spellSlot < structa4.count_22.get(); spellSlot++) {
      if(y1 >= sp68) {
        break;
      }

      long s2 = 0;
      final LodString s3;
      if(a0 == 0) {
        //LAB_800f5918
        s3 = _80050ae8.get((int)(_800c6988.offset(sp7c).get() - 0xc0)).deref();
        intToStr((int)_800c6988.offset(s7).get(), sp0x48);

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
      } else if(a0 == 1) {
        //LAB_800f5a4c
        int spellIndex = dragoonSpells_800c6960.get(charSlot).spellIndex_01.get(spellSlot).get();
        s3 = spells_80052734.get(spellIndex).deref();

        if(structa4.charIndex_08.get() == 8) {
          if(spellIndex == 65) {
            spellIndex = 10;
          }

          //LAB_800f5ab4
          if(spellIndex == 66) {
            spellIndex = 11;
          }

          //LAB_800f5ac0
          if(spellIndex == 67) {
            spellIndex = 12;
          }
        }

        //LAB_800f5acc
        final BattleObject27c bobj = FUN_800f9e50((short)spellIndex);

        if(bobj.mp_0c.get() < bobj.spellMp_a0.get()) {
          s2 = 0xaL;
        }
      } else {
        throw new RuntimeException("Undefined s3");
      }

      //LAB_800f5af0
      if(y1 >= y2) {
        //LAB_800f5b90
        if(sp68 >= y1 + 0xcL) {
          a1 = 0;
        } else {
          a1 = y1 - (sp68 - 0xcL);
        }

        //LAB_800f5bb4
        if((structa4._02.get() & 0x4L) != 0) {
          a1 = (short)structa4._8c.get();
        }

        //LAB_800f5bd8
        Scus94491BpeSegment_8002.renderText(s3, structa4._18.get(), y1, s2, a1);

        if(a0 == 0) {
          Scus94491BpeSegment_8002.renderText(sp0x40, structa4._18.get() + 0x80, y1, s2, a1);
        }
      } else if(y2 < y1 + 12) {
        if((structa4._02.get() & 0x4L) != 0) {
          a1 = structa4._8c.get();
        } else {
          a1 = y1 - y2;
        }

        //LAB_800f5b40
        Scus94491BpeSegment_8002.renderText(s3, structa4._18.get(), y2, s2, a1);

        if(a0 == 0) {
          Scus94491BpeSegment_8002.renderText(sp0x40, structa4._18.get() + 0x80, y2, s2, a1);
        }
      }

      //LAB_800f5c38
      y1 += 14;
      sp7c = sp7c + 0x2L;
      s7 = s7 + 0x2L;
    }

    //LAB_800f5c64
  }

  /** Draws most elements associated with item and dragoon magic menus.
   * This includes:
   *   - Item and Dragoon magic backgrounds, scroll arrows, and text
   *   - Item and Dragoon magic description background and text
   *   - Dragoon magic MP cost background and normal text (excluding the number value) */
  @Method(0x800f5c94L)
  public static void drawItemMenuElements() {
    final BttlStructa4 structa4 = _800c6b60.deref();

    if(structa4._00.get() != 0 && (structa4._02.get() & 0x1L) != 0) {
      if((structa4._02.get() & 0x2L) != 0) {
        FUN_800f57f8(structa4._0a.get());

        if((structa4._02.get() & 0x8L) != 0) {
          //LAB_800f5d78
          //LAB_800f5d90
          FUN_800f8cd8(structa4._18.get() - centreScreenX_1f8003dc.get() - 0x10L, structa4._1a.get() - centreScreenY_1f8003de.get() + structa4._24.get() * 0xeL + 0x2L, structa4._84.get() % 4 * 0x10L + 0xc0L & 0xf0L, structa4._84.get() / 4 * 0x8L + 0x20L & 0xf8L, 0xfL, 0x8L, 0xdL, TexPageTrans.B_PLUS_F);

          final long s0;
          if(structa4._0a.get() != 0) {
            s0 = 0;
          } else {
            s0 = 0x1aL;
          }

          //LAB_800f5e00
          final long s1;
          if((structa4._02.get() & 0x100L) != 0) {
            s1 = 0x2L;
          } else {
            s1 = 0;
          }

          //LAB_800f5e18
          final long t0;
          if((structa4._02.get() & 0x200L) != 0) {
            t0 = -0x2L;
          } else {
            t0 = 0;
          }

          //LAB_800f5e24
          if(structa4._1e.get() > 0) {
            FUN_800f74f4(_800c7190.getAddress(), structa4._04.get() + s0 + 0x38L, structa4._06.get() + t0 - 0x64L, _800c7192.get(), _800c7193.get(), 0xdL, null, 0);
          }

          //LAB_800f5e7c
          if(structa4._1e.get() + 6 < structa4.count_22.get() - 1) {
            FUN_800f74f4(_800c7190.getAddress(), structa4._04.get() + s0 + 0x38L, structa4._06.get() + s1 - 0x7L, _800c7192.get(), _800c7193.get(), 0xdL, null, 0x1L);
          }
        }

        //LAB_800f5ee8
        //Item menu
        final long a2 = structa4._10.get() + 6;
        final long a3 = structa4._12.get() + 0x11L;
        renderTextBoxBackground(structa4._04.get() - a2 / 2, structa4._06.get() - a3, a2, a3, 0x8L);
      }

      //LAB_800f5f50
      if((structa4._02.get() & 0x40L) != 0) {
        final long s1;
        if(structa4._0a.get() == 0) {
          //LAB_800f5f8c
          s1 = 0x4L;
        } else if(structa4._0a.get() == 0x1L) {
          //LAB_800f5f94
          s1 = 0x5L;
          if((structa4._02.get() & 0x2L) != 0) {
            final BattleObject27c bobj = FUN_800f9e50(structa4._1c.get());
            calculateFloatingNumberRender(0, 0x1L, 0, bobj.spellMp_a0.get(), 280, 135, 0, structa4._0a.get());
            FUN_800f8cd8(236 - centreScreenX_1f8003dc.get(), 130 - centreScreenY_1f8003de.get(), 0x10L, 0x80L, 0x18L, 0x10L, 0x2cL, null);
            renderTextBoxBackground(0xecL, 0x82L, 0x40L, 0xeL, 0x8L);
          }
        } else {
          throw new RuntimeException("Undefined s1");
        }

        //LAB_800f604c
        //LAB_800f6050
        //Selected item description
        renderTextBoxBackground(0x2cL, 0x9cL, 0xe8L, 0xeL, 0x8L);
        renderText((short)s1, structa4._1c.get(), 160, 163);
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

    //LAB_800f6224
    //LAB_800f6234
    int a3;
    for(a3 = 0; a3 < charCount_800c677c.get(); a3++) {
      if(_8006e398.charBobjIndices_e40.get(a3).get() == a0) {
        break;
      }
    }

    //LAB_800f6254
    v0.iconCount_0e.set((short)0);
    v0.charIndex_04.set(scriptStatePtrArr_800bc1c0.get(_8006e398.charBobjIndices_e40.get(a3).get()).deref().innerStruct_00.derefAs(BattleObject27c.class).charIndex_272.get());

    //LAB_800f62a4
    for(int i = 0, used = 0; i < 8; i++) {
      if((a1 & 1 << i) != 0) {
        v0.iconFlags_10.get(used++).set(_800c7194.get(i).get());
        v0.iconCount_0e.incr();
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
            if(v1 == 0x5L) {
              FUN_800f83c8();

              if(_800c6b70.getSigned() == 0) {
                playSound(0, 3, 0, 0, (short)0, (short)0);
              } else {
                playSound(0, 2, 0, 0, (short)0, (short)0);
                s1 = struct58.iconFlags_10.get(struct58.selectedIcon_22.get()).get() & 0xfL;
              }
              //LAB_800f6790
            } else if(v1 == 0x3L) {
              //LAB_800f67b8
              int charSlot;
              for(charSlot = 0; charSlot < charCount_800c677c.get(); charSlot++) {
                if(dragoonSpells_800c6960.get(charSlot).charIndex_00.get() == struct58.charIndex_04.get()) {
                  break;
                }
              }

              //LAB_800f67d8
              //LAB_800f67f4
              int spellIndex;
              for(spellIndex = 0; spellIndex < 8; spellIndex++) {
                if(dragoonSpells_800c6960.get(charSlot).spellIndex_01.get(spellIndex).get() != -1) {
                  break;
                }
              }

              //LAB_800f681c
              if(spellIndex == 8) {
                playSound(0, 3, 0, 0, (short)0, (short)0);
              } else {
                playSound(0, 2, 0, 0, (short)0, (short)0);
                s1 = struct58.iconFlags_10.get(struct58.selectedIcon_22.get()).get() & 0xfL;
              }
            } else {
              //LAB_800f6858
              //LAB_800f6860
              playSound(0, 2, 0, 0, (short)0, (short)0);
              s1 = struct58.iconFlags_10.get(struct58.selectedIcon_22.get()).get() & 0xfL;
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
    final long v0;
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
    final long s2;
    long s3;
    long s4;
    long s6;
    final long s7;
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
          FUN_800f8cd8(s3, menu.y_08.get() - (centreScreenY_1f8003de.get() + 0x10L), 0x60L, 0x70L, 0x10L, 0x10L, 0x19L, null);
        }

        //LAB_800f6d70
        if((menu.iconFlags_10.get(iconIndex).get() & 0xfL) != 0x2L) {
          //LAB_800f6e24
          s0 = _800fb674.offset(fp * 0x8L).offset(2, 0x4L).get();
        } else if(menu.charIndex_04.get() == 0 && (gameState_800babc8.dragoonSpirits_19c.get(0).get() & 0xffL) >>> 7 != 0) {
          s0 = sp0x48[9];
          if(s6 != 0) {
            //LAB_800f6de0
            FUN_800f8cd8(s3 + 0x4L, s4, s6 != 0x1L ? 0x58L : 0x50L, 0x70L, 0x8L, 0x10L, 0x98L, TexPageTrans.B_PLUS_F);
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
        FUN_800f8cd8(s3, s4, MEMORY.ref(1, t1).offset(0x0L).get(), MEMORY.ref(1, t1).offset(0x2L).get() + MEMORY.ref(1, t0).offset(0x0L).get() & 0xffL, 0x10L, MEMORY.ref(2, v1).getSigned(), s0, TexPageTrans.of((int)MEMORY.ref(2, t1).offset(0x6L).getSigned()));

        if(menu.selectedIcon_22.get() == iconIndex && menu._40.get() == 0x1L) {
          t1 = _800fb72c.offset(fp * 8).getAddress();
          a0 = menu.x_06.get() - menu._0a.get() + iconIndex * 19 - centreScreenX_1f8003dc.get() - MEMORY.ref(2, t1).offset(0x4L).get() / 2 + 0x8L;
          a1 = menu.y_08.get() - centreScreenY_1f8003de.get() - 0x18L;
          // Selected combat menu icon text
          FUN_800f8cd8(a0, a1, MEMORY.ref(1, t1).offset(0x0L).get(), MEMORY.ref(1, t1).offset(0x2L).get(), MEMORY.ref(2, t1).offset(0x4L).get(), 0x8L, MEMORY.ref(2, t1).offset(0x6L).getSigned(), null);
        }

        //LAB_800f6fa4
      }

      //LAB_800f6fc8
      FUN_800f7210(menu._28.get(), menu._2a.get(), sp0x30, 0x1fL, 0xcL, TexPageTrans.B_PLUS_F, menu.colour_2c.get());

      if((menu._02.get() & 0x1L) != 0) {
        FUN_800f7210(menu._3c.get(), menu._2a.get(), sp0x30, 0x1fL, 0xcL, TexPageTrans.B_PLUS_F, 0x80 - menu.colour_2c.get());
      }

      //LAB_800f704c
      s0 = menu.iconCount_0e.get() * 19 + 1;
      s1 = menu.x_06.get() - s0 / 2;
      s2 = menu.y_08.get() - 0xaL;
      FUN_800f74f4(_800fb5dc.getAddress(), s1, s2, s0, 0x2L, 0x2bL, TexPageTrans.B_PLUS_F, _800fb5dc.offset(1, 0x4L).get());

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
        FUN_800f74f4(fp + i * 0x6L, (short)a1, (short)t2, (short)a3, (short)v1, 0x2bL, TexPageTrans.B_PLUS_F, MEMORY.ref(1, s7).offset((i + 0x1L) * 0x6L).offset(0x4L).get());
      }
    }

    //LAB_800f71e0
  }

  @Method(0x800f7210L)
  public static void FUN_800f7210(final long x, final long y, final long[] a2, final long a3, final long a4, @Nullable TexPageTrans transparencyMode, final long colour) {
    long v0;
    long v1;
    final long s0 = gpuPacketAddr_1f8003d8.get();
    gpuPacketAddr_1f8003d8.addu(0x28L);
    setGp0_2c(s0);
    if(transparencyMode == null) {
      gpuLinkedListSetCommandTransparency(s0, false);
      transparencyMode = TexPageTrans.HALF_B_PLUS_HALF_F;
    } else {
      //LAB_800f728c
      gpuLinkedListSetCommandTransparency(s0, true);
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
    MEMORY.ref(2, s0).offset(0x16L).setu(GetTPage(TexPageBpp.BITS_4, transparencyMode, 704, 256));
    queueGpuPacket(tags_1f8003d0.getPointer() + a3 * 0x4L, s0);
  }

  /** Background of battle menu icons */
  @Method(0x800f74f4L)
  public static void FUN_800f74f4(final long a0, final long x, final long y, final long w, final long h, final long a5, @Nullable TexPageTrans transMode, final long a7) {
    final long s0 = gpuPacketAddr_1f8003d8.get();
    gpuPacketAddr_1f8003d8.addu(0x28L);
    setGp0_2c(s0);

    if(transMode == null) {
      gpuLinkedListSetCommandTransparency(s0, false);
      transMode = TexPageTrans.HALF_B_PLUS_HALF_F;
    } else {
      //LAB_800f7578
      gpuLinkedListSetCommandTransparency(s0, true);
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
    FUN_800f9024(s0, (short)a5, transMode);
  }

  @Method(0x800f7768L)
  public static long FUN_800f7768(final long a0, final long a1) {
    long v1;
    final long t1;
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
      struct58.combatantIndex.set((int)_800c697c.getSigned());
    } else {
      //LAB_800f79b4
      struct58.combatantIndex.set(-1);
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
  public static void setTempWeaponStats(final int bobjIndex) {
    final ScriptState<?> state = scriptStatePtrArr_800bc1c0.get(bobjIndex).deref();
    final BattleObject27c bobj = state.innerStruct_00.derefAs(BattleObject27c.class);

    //LAB_800f7a98
    bzero(bobj.itemTarget_d4.getAddress(), 0x20);

    final ItemStats0c itemStats = itemStats_8004f2ac.get(bobj.weaponId_52.get());
    bobj.itemTarget_d4.set((short)itemStats.target_00.get());
    bobj.itemElement_d6.set((short)itemStats.element_01.get());
    bobj.itemDamage_d8.set((short)itemStats.damage_02.get());
    bobj.itemSpecial1_da.set((short)itemStats.special1_03.get());
    bobj.itemSpecial2_dc.set((short)itemStats.special2_04.get());
    bobj.itemUu1_de.set((short)itemStats.uu1_05.get());
    bobj.itemSpecialAmount_e0.set(itemStats.specialAmount_06.get());
    bobj._e2.set(itemStats.icon_07.get());
    bobj.itemStatus_e4.set((short)itemStats.status_08.get());
    bobj.itemPercentage_e6.set((short)itemStats.percentage_09.get());
    bobj.itemUu2_e8.set((short)itemStats.uu2_0a.get());
    bobj.itemType_ea.set((short)itemStats.type_0b.get());
  }

  @Method(0x800f7b68L)
  public static void setTempSpellStats(final int bobjIndex) {
    final BattleObject27c bobj = scriptStatePtrArr_800bc1c0.get(bobjIndex).deref().innerStruct_00.derefAs(BattleObject27c.class);

    //LAB_800f7b8c
    bzero(bobj._94.getAddress(), 0x18);

    if(bobj.spellId_4e.get() != -1) {
      final SpellStats0c spellStats = spellStats_800fa0b8.get(bobj.spellId_4e.get());
      bobj._94.set((short)spellStats._00.get());
      bobj._96.set((short)spellStats._01.get());
      bobj._98.set((short)spellStats._02.get());
      bobj.spellDamage_9a.set((short)spellStats.damage_03.get());
      bobj.spellMulti_9c.set((short)spellStats.multi_04.get());
      bobj.spellAccuracy_9e.set((short)spellStats.accuracy_05.get());
      bobj.spellMp_a0.set((short)spellStats.mp_06.get());
      bobj._a2.set((short)spellStats._07.get());
      bobj.spellElement_a4.set((short)spellStats.element_08.get());
      bobj._a6.set((short)spellStats._09.get());
      bobj._a8.set((short)spellStats._0a.get());
      bobj._aa.set((short)spellStats._0b.get());
    }

    //LAB_800f7c54
  }

  @Method(0x800f7c5cL)
  public static int FUN_800f7c5c(final long a0, final long a1, final long a2) {
    final ScriptState<BattleObject27c> a2_0 = scriptStatePtrArr_800bc1c0.get((int)a0).derefAs(ScriptState.classFor(BattleObject27c.class));
    final BattleObject27c s1 = a2_0.innerStruct_00.deref();
    final long fp = a2_0.ui_60.get() & 0x4L;
    final long v1 = (fp != 0 ? 0xcL : 0) + a2 * 0x4L;
    long s2 = s1.all_04.get((int)_800c7284.offset(v1).get()).get();
    final ScriptState<BattleObject27c> a2_1 = scriptStatePtrArr_800bc1c0.get((int)a1).derefAs(ScriptState.classFor(BattleObject27c.class));
    final long spa8 = a2_1.ui_60.get() & 0x4L;
    final BattleObject27c s4 = a2_1.innerStruct_00.deref();
    final long s3 = _800c726c.offset(v1).get();
    final long s6 = _800c729c.offset(v1).get();
    final long s7 = _800c72b4.offset(v1).get();
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
      final long v1_0 = s1.all_04.get((int)s6).get() & s7;
      if(v1_0 != 0) {
        if(fp != 0 || a2 != 0) {
          //LAB_800f7f40
          if(a2 != 0x2L) {
            //LAB_800f7f68
            if(v1_0 == 0x10L) {
              //LAB_800f7f7c
              final long v0;
              if(spa8 == 0) {
                v0 = s1.spellElement_a4.get() & characterElements_800c706c.get(s4.charIndex_272.get()).get();
              } else {
                //LAB_800f7fac
                v0 = s1.spellElement_a4.get() & s4.elementFlag_1c.get();
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
        if((s4.specialEffectFlag_14.get() & 0x80L) != 0) { // Resistance
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
    for(int itemSlot1 = 0; itemSlot1 < gameState_800babc8.itemCount_1e6.get(); itemSlot1++) {
      //LAB_800f843c
      for(int itemSlot2 = 0; itemSlot2 < gameState_800babc8.itemCount_1e6.get(); itemSlot2++) {
        final int itemId1 = gameState_800babc8.items_2e9.get(itemSlot1).get();
        final int itemId2 = (int)_800c6988.offset(itemSlot2 * 0x2L).get();

        if(itemId2 == itemId1) {
          _800c6988.offset(itemSlot2 * 0x2L).offset(0x1L).addu(0x1L);
          break;
        }

        //LAB_800f8468
        if(itemId2 == 0xff) {
          _800c6988.offset(itemSlot2 * 0x2L).setu(itemId1);
          _800c6988.offset(itemSlot2 * 0x2L).offset(0x1L).setu(0x1L);
          _800c6b70.addu(0x1L);
          break;
        }
      }
    }
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
  public static LodString getTargetEnemyName(final BattleObject27c target, final LodString targetName) {
    if(target.charIndex_272.get() != 0x185L) {
      return targetName;
    }

    final long v1 = _8006f284.get();
    if(v1 != 0x4L) {
      if((int)v1 < 0x5L) {
        if(v1 != 0) {
          return targetName;
        }
        //LAB_800f85ec
      } else if(v1 != 0x6L) {
        return targetName;
      }
    }

    //LAB_800f85f4
    //LAB_800f8634
    return _800c6ba8.get((int)_800c6f30.offset(_8006f284.get() * 0x4L).get());
  }

  @Method(0x800f863cL)
  public static void FUN_800f863c() {
    loadSupportOverlay(2, getConsumerAddress(Bttl_800e.class, "FUN_800ef28c", int.class), 1);
  }

  @Method(0x800f8670L)
  public static void loadMonster(final int bobjIndex) {
    loadSupportOverlay(1, getConsumerAddress(Bttl_800e.class, "loadMonster", int.class), bobjIndex);
  }

  @Method(0x800f8768L)
  public static int monsterMagicAttack(final int attackerBobjIndex, final int defenderBobjIndex, final int attackType) {
    final BattleObject27c attacker = scriptStatePtrArr_800bc1c0.get(attackerBobjIndex).deref().innerStruct_00.derefAs(BattleObject27c.class);
    int matk = attacker.magicAttack_36.get();
    if(attackType == 1) {
      matk += spellStats_800fa0b8.get(attacker.spellId_4e.get()).multi_04.get();
    } else {
      //LAB_800f87c4
      matk += attacker.itemUu1_de.get();
    }

    //LAB_800f87d0
    final BattleObject27c defender = scriptStatePtrArr_800bc1c0.get(defenderBobjIndex).deref().innerStruct_00.derefAs(BattleObject27c.class);
    final long a1 = scriptStatePtrArr_800bc1c0.get(defenderBobjIndex).deref().ui_60.get();
    int mdef = defender.magicDefence_3a.get();
    if((a1 & 0x4L) != 0x1L && (a1 & 0x2L) != 0) {
      mdef = mdef * defender.dragoonMagicDefence_b2.get() / 100;
    }

    //LAB_800f8844
    return matk * matk * 5 / mdef;
  }

  @Method(0x800f8854L)
  public static void FUN_800f8854(final int scriptIndex1, final int scriptIndex2, final long a2) {
    final BattleObject27c s2 = scriptStatePtrArr_800bc1c0.get(scriptIndex1).deref().innerStruct_00.derefAs(BattleObject27c.class);

    if(a2 != 0x1L) {
      setTempWeaponStats(scriptIndex1);
    }

    //LAB_800f88a0
    //LAB_800f88a8
    for(int i = 0; i < 8; i++) {
      if((s2.itemSpecial1_da.get() & (0x80 >> i)) != 0) {
        FUN_800f923c(scriptIndex1, scriptIndex2, i, 0x6bL);
      }

      //LAB_800f88cc
    }

    //LAB_800f88e4
    for(int i = 0; i < 8; i++) {
      if((s2.itemSpecial2_dc.get() & (0x80 >> i)) != 0) {
        FUN_800f923c(scriptIndex1, scriptIndex2, i, 0x6cL);
      }

      //LAB_800f8908
    }

    FUN_800f3204(scriptIndex2);
  }

  @Method(0x800f89ccL)
  public static int FUN_800f89cc(final byte a0) {
    if(a0 == 0) {
      return 2;
    }

    if(a0 <= 0) {
      return 4;
    }

    //LAB_800f89ec
    return 1;
  }

  @Method(0x800f89f4L)
  public static long FUN_800f89f4(final long a0, final long a1, final long a2, final long a3, final int x, final int y, final long a6, final long a7) {
    //LAB_800f8a30
    for(int i = 0; i < 12; i++) {
      final FloatingNumberC4 num = floatingNumbers_800c6b5c.deref().get(i);

      if(num.state_00.get() == 0) {
        calculateFloatingNumberRender(i, a1, a2, a3, x, y, a6, a7);
        num.bobjIndex_04.set((int)a0);
        return 0x1L;
      }

      //LAB_800f8a74
    }

    //LAB_800f8a84
    return 0;
  }

  @Method(0x800f8aa4L)
  public static void renderDamage(final long a0, final long a1) {
    setFloatingNumCoordsAndRender(a0, a1, 0x8L);
  }

  @Method(0x800f8ac4L)
  public static void renderText(final int textType, final int textIndex, final int x, final int y) {
    final LodString str = allText_800fb3c0.get(textType).deref().get(textIndex).deref();
    Scus94491BpeSegment_8002.renderText(str, x - textWidth(str) / 2, y - 6, 0, 0);
  }

  @Method(0x800f8b74L)
  public static void FUN_800f8b74(final long a0) {
    final BattleMenuStruct58 struct58 = battleMenu_800c6c34.deref();

    //LAB_800f8bd8
    for(int t1 = 0; t1 < 8; t1++) {
      if((a0 & 0x1L << t1) != 0) {
        //LAB_800f8bf4
        for(int a3 = 0; a3 < 8; a3++) {
          if((struct58.iconFlags_10.get(a3).get() & 0xfL) == _800c7194.get(t1).get()) {
            struct58.iconFlags_10.get(a3).or(0x80);
            break;
          }
        }
      }
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
  public static int getTargetEnemyElement(final long elemFlag) {
    //LAB_800f8cac
    int elemIdx = -1;
    for(int i = 0; i < 32; i++) {
      if((elemFlag & 1L << i) != 0) {
        elemIdx = i;
        break;
      }

      //LAB_800f8cc0
    }

    //LAB_800f8cd0
    return elemIdx;
  }

  @Method(0x800f8cd8L)
  public static void FUN_800f8cd8(final long x, final long y, final long u, final long v, final long w, final long h, final long a6, @Nullable TexPageTrans transMode) {
    final long v0 = gpuPacketAddr_1f8003d8.get();
    gpuPacketAddr_1f8003d8.addu(0x28L);
    setGp0_2c(v0);

    if(transMode == null) {
      gpuLinkedListSetCommandTransparency(v0, false);
      transMode = TexPageTrans.HALF_B_PLUS_HALF_F;
    } else {
      //LAB_800f8d5c
      gpuLinkedListSetCommandTransparency(v0, true);
    }

    //LAB_800f8d64
    MEMORY.ref(1, v0).offset(0x4L).setu(0x80L);
    MEMORY.ref(1, v0).offset(0x5L).setu(0x80L);
    MEMORY.ref(1, v0).offset(0x6L).setu(0x80L);
    setGpuPacketParams(v0, x, y, u, v, w, h, true);
    FUN_800f9024(v0, a6, transMode);
  }

  @Method(0x800f8dfcL)
  public static void drawUiTextureElement(final long x, final long y, final long u, final long v, final long w, final long h, final long a6, final long a7, final long a8) {
    final long t3 = _800c71ec.getAddress();

    final byte[] sp0x20 = {
      (byte)MEMORY.ref(1, t3).offset(0x0L).getSigned(),
      (byte)MEMORY.ref(1, t3).offset(0x1L).getSigned(),
      (byte)MEMORY.ref(1, t3).offset(0x2L).getSigned(),
    };

    final long s0 = gpuPacketAddr_1f8003d8.get();
    gpuPacketAddr_1f8003d8.addu(0x28L);
    setGp0_2c(s0);
    gpuLinkedListSetCommandTransparency(s0, false);

    if((int)a8 < 6) {
      final long v0 = (byte)(sp0x20[(int)a7] + 0x80) / 6 * a8 - 0x80;
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
    FUN_800f9024(s0, a6, TexPageTrans.HALF_B_PLUS_HALF_F);
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
  public static void FUN_800f9024(final long a0, final long a1, final TexPageTrans transparencyMode) {
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
    MEMORY.ref(2, a0).offset(0x16L).setu(GetTPage(TexPageBpp.BITS_4, transparencyMode, 704, 496));
    queueGpuPacket(tags_1f8003d0.getPointer() + 0x7cL, a0);
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
    a1.all_04.get((int)MEMORY.ref(4, a3_0).offset(a2 * 0x4L).get()).set((short)(a0 << 8 | t0.itemSpecialAmount_e0.get()));
  }

  @Method(0x800f9380L)
  public static void FUN_800f9380(final BattleObject27c attacker, final BattleObject27c defender) {
    if((defender._a8.get() & 0xff) != 0) {
      //LAB_800f93c8
      for(int i = 0; i < 8; i++) {
        if((defender._a8.get() & (0x80 >> i)) != 0) {
          //LAB_800f93e8
          final long a3;
          if(attacker.charIndex_272.get() != defender.charIndex_272.get()) {
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
          defender.all_04.get((int)_800c723c.offset(i % 4 * 4).get()).set((short)(a3 << 8 | a0_0));
        }

        //LAB_800f9454
      }
    }

    //LAB_800f9464
  }

  @Method(0x800f946cL)
  public static int FUN_800f946c(final BattleObject27c a0, final int a1, final int a2) {
    final long a0_0;
    if(a2 == 0) {
      a0_0 = spellStats_800fa0b8.get(a0.spellId_4e.get()).damage_03.get();
    } else {
      //LAB_800f949c
      a0_0 = a0.itemDamage_d8.get();
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

  @Method(0x800f9618L)
  public static long FUN_800f9618(final RunningScript a0) {
    a0.params_20.get(2).deref().set(FUN_800f1aa8(a0.params_20.get(0).deref().get(), a0.params_20.get(1).deref().get(), 1));
    return 0;
  }

  @Method(0x800f9660L)
  public static long FUN_800f9660(final RunningScript a0) {
    a0.params_20.get(2).deref().set(FUN_800f1aa8(a0.params_20.get(0).deref().get(), a0.params_20.get(1).deref().get(), 2));
    return 0;
  }

  @Method(0x800f96a8L)
  public static long FUN_800f96a8(final RunningScript a0) {
    setTempSpellStats(a0.params_20.get(0).deref().get());
    return 0;
  }

  @Method(0x800f96d4L)
  public static long FUN_800f96d4(final RunningScript a0) {
    final BattleObject27c v1 = scriptStatePtrArr_800bc1c0.get(a0.params_20.get(0).deref().get()).deref().innerStruct_00.derefAs(BattleObject27c.class);
    a0.params_20.get(1).deref().set(v1.model_148.coord2_14.coord.transfer.getX());
    a0.params_20.get(2).deref().set(v1.model_148.coord2_14.coord.transfer.getY());
    a0.params_20.get(3).deref().set(v1.model_148.coord2_14.coord.transfer.getZ());
    return 0;
  }

  @Method(0x800f9730L)
  public static long FUN_800f9730(final RunningScript a0) {
    //LAB_800f9758
    for(int i = 0; i < 12; i++) {
      final FloatingNumberC4 num = floatingNumbers_800c6b5c.deref().get(i);

      if(num.state_00.get() == 0) {
        calculateFloatingNumberRender(i, 0, 0, a0.params_20.get(0).deref().get(), a0.params_20.get(1).deref().get(), a0.params_20.get(2).deref().get(), 60 / vsyncMode_8007a3b8.get() * 5, 0);
        break;
      }

      //LAB_800f97b8
    }

    //LAB_800f97c8
    return 0;
  }

  @Method(0x800f97d8L)
  public static long FUN_800f97d8(final RunningScript a0) {
    FUN_800f4964();
    FUN_800f49bc(scriptStatePtrArr_800bc1c0.get(a0.params_20.get(0).deref().get()).deref().innerStruct_00.derefAs(BattleObject27c.class).charIndex_272.get(), (short)a0.params_20.get(1).deref().get());
    return 0;
  }

  @Method(0x800f984cL)
  public static long scriptRenderRecover(final RunningScript a0) {
    setFloatingNumCoordsAndRender(a0.params_20.get(0).deref().get(), a0.params_20.get(1).deref().get(), a0.params_20.get(2).deref().get());
    return 0;
  }

  @Method(0x800f9884L)
  public static long FUN_800f9884(final RunningScript a0) {
    setTempWeaponStats(a0.params_20.get(0).deref().get());
    return 0;
  }

  @Method(0x800f98b0L)
  public static long FUN_800f98b0(final RunningScript s3) {
    int itemId = s3.params_20.get(0).deref().get();

    if(gameState_800babc8.itemCount_1e6.get() == 0) {
      s3.params_20.get(1).deref().set(-1);
      return 0;
    }

    if(itemId == -1) {
      itemId = gameState_800babc8.items_2e9.get((simpleRand() * gameState_800babc8.itemCount_1e6.get()) >> 16).get();

      //LAB_800f996c
      for(int i = 0; i < 10; i++) {
        //TODO maybe protected item IDs that can't be dropped?
        if(itemId == _800c72cc.get(i).get()) {
          //LAB_800f999c
          itemId = -1;
          break;
        }
      }
    }

    //LAB_800f9988
    if(itemId != -1) {
      //LAB_800f99a4
      if(takeItem(itemId) != 0) {
        itemId = -1;
      }
    }

    //LAB_800f99c0
    s3.params_20.get(1).deref().set(itemId);
    return 0;
  }

  @Method(0x800f99ecL)
  public static long FUN_800f99ec(final RunningScript a0) {
    final int s1;
    if(giveItem(a0.params_20.get(0).deref().get()) == 0) {
      s1 = a0.params_20.get(0).deref().get();
    } else {
      s1 = -1;
    }

    //LAB_800f9a2c
    a0.params_20.get(1).deref().set(s1);
    return 0;
  }

  @Method(0x800f9a50L)
  public static long FUN_800f9a50(final RunningScript a0) {
    final long a2 = a0.params_20.get(0).deref().get();
    final long v1 = a0.params_20.get(1).deref().get();

    final long a0_0;
    final long v0;
    if(a2 == 1) {
      //LAB_800f9a94
      a0_0 = _8006f254.getAddress();
      v0 = _800c6758.get();
    } else if(a2 != 0) {
      //LAB_800f9aac
      a0_0 = _8006f210.getAddress();

      //LAB_800f9ab0
      v0 = _800c669c.get();
    } else {
      a0_0 = _8006f1d8.getAddress();
      v0 = charCount_800c677c.get();
    }

    //LAB_800f9abc
    //LAB_800f9adc
    for(int i = 0; i < (short)v0; i++) {
      if(v1 == MEMORY.ref(4, a0_0).offset(i * 0x4L).get()) {
        if(a2 == 1) {
          //LAB_800f9b0c
          _800c697e.setu(i);
        } else if(a2 < 2 && a2 != 0) {
          _800c6980.setu(i);
        }

        break;
      }

      //LAB_800f9b14
    }

    //LAB_800f9b24
    return 0;
  }

  @Method(0x800f9b2cL)
  public static long scriptIsFloatingNumberOnScreen(final RunningScript a0) {
    //LAB_800f9b3c
    int found = 0;
    for(int i = 0; i < 12; i++) {
      final FloatingNumberC4 num = floatingNumbers_800c6b5c.deref().get(i);

      if(num.state_00.get() != 0) {
        found = 1;
        break;
      }

      //LAB_800f9b58
    }

    //LAB_800f9b64
    a0.params_20.get(0).deref().set(found);
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

  @Method(0x800f9c2cL)
  public static long FUN_800f9c2c(final RunningScript script) {
    renderTextBoxBackground(
      (short)script.params_20.get(0).deref().get() - script.params_20.get(2).deref().get() / 2,
      (short)script.params_20.get(1).deref().get() - script.params_20.get(3).deref().get() / 2,
      (short)script.params_20.get(2).deref().get(),
      (short)script.params_20.get(3).deref().get(),
      (short)script.params_20.get(4).deref().get()
    );

    return 0;
  }

  @Method(0x800f9cacL)
  public static long FUN_800f9cac(final RunningScript script) {
    final BattleMenuStruct58 menu = battleMenu_800c6c34.deref();
    final int t2 = script.params_20.get(0).deref().get();

    //LAB_800f9d18
    for(int t0 = 0; t0 < 8; t0++) {
      if((t2 & 1 << t0) != 0) {
        //LAB_800f9d34
        for(int icon = 0; icon < 8; icon++) {
          if((menu.iconFlags_10.get(icon).get() & 0xf) == _800c7194.get(t0).get()) {
            menu.iconFlags_10.get(icon).or(0x80);
            break;
          }
        }
      }
    }

    return 0;
  }

  @Method(0x800f9d7cL)
  public static long FUN_800f9d7c(final RunningScript a0) {
    final long v0 = scriptStatePtrArr_800bc1c0.get(a0.params_20.get(0).deref().get()).deref().innerStruct_00.getPointer(); //TODO
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

  @Method(0x800f9e10L)
  public static void clearTempWeaponAndSpellStats(final BattleObject27c a0) {
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
  public static BattleObject27c FUN_800f9e50(final short a0) {
    final int charIndex = _800c6b60.deref().charIndex_08.get();

    //LAB_800f9e8c
    for(int charSlot = 0; charSlot < charCount_800c677c.get(); charSlot++) {
      final int bobjIndex = _8006e398.charBobjIndices_e40.get(charSlot).get();
      final BattleObject27c bobj = scriptStatePtrArr_800bc1c0.get(bobjIndex).deref().innerStruct_00.derefAs(BattleObject27c.class);

      if(charIndex == bobj.charIndex_272.get()) {
        //LAB_800f9ec8
        bobj.spellId_4e.set(a0);
        setTempSpellStats(bobjIndex);
        return bobj;
      }
    }

    throw new IllegalStateException();
  }

  @Method(0x800f9ee8L)
  public static void drawLine(final long x1, final long y1, final long x2, final long y2, final long r, final long g, final long b, final boolean transparent) {
    final long s0 = gpuPacketAddr_1f8003d8.get();
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
    queueGpuPacket(tags_1f8003d0.getPointer() + 0x7cL, s0);
    gpuPacketAddr_1f8003d8.addu(0x14L);

    final long a1_0 = gpuPacketAddr_1f8003d8.get();
    MEMORY.ref(1, a1_0).offset(0x3L).setu(0x1L);
    MEMORY.ref(4, a1_0).offset(0x4L).setu(0xe100_0200L | (texPages_800bb110.get(TexPageBpp.BITS_4).get((transparent ? TexPageTrans.B_PLUS_F : TexPageTrans.HALF_B_PLUS_HALF_F)).get(TexPageY.Y_256).get() | 0xbL) & 0x9ffL);
    queueGpuPacket(tags_1f8003d0.getPointer() + 0x7cL, a1_0);
    gpuPacketAddr_1f8003d8.addu(0x8L);
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
  public static int clampX(final int x) {
    return MathHelper.clamp(x, 20, 300);
  }

  @Method(0x800fa090L)
  public static int clampY(final int y) {
    return MathHelper.clamp(y, 20, 220);
  }
}
