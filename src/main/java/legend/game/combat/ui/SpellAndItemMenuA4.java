package legend.game.combat.ui;

import legend.core.gte.MV;
import legend.core.memory.Method;
import legend.core.opengl.Obj;
import legend.game.characters.VitalsStat;
import legend.game.combat.bent.PlayerBattleEntity;
import legend.game.combat.environment.BattleMenuBackgroundUvMetrics04;
import legend.game.inventory.Item;
import legend.game.modding.coremod.CoreMod;
import legend.game.modding.events.inventory.RepeatItemReturnEvent;
import legend.game.types.Translucency;
import legend.lodmod.LodMod;

import static legend.core.GameEngine.EVENTS;
import static legend.core.GameEngine.REGISTRIES;
import static legend.game.Scus94491BpeSegment.playSound;
import static legend.game.Scus94491BpeSegment_8002.takeItemId;
import static legend.game.Scus94491BpeSegment_8006.battleState_8006e398;
import static legend.game.Scus94491BpeSegment_800b.input_800bee90;
import static legend.game.Scus94491BpeSegment_800b.press_800bee94;
import static legend.game.Scus94491BpeSegment_800b.tickCount_800bb0fc;
import static legend.game.combat.Bttl.battleMenu_800c6c34;
import static legend.game.combat.Bttl.buildBattleMenuBackground;
import static legend.game.combat.Bttl.buildBattleMenuElement;
import static legend.game.combat.Bttl.charCount_800c677c;
import static legend.game.combat.Bttl.combatItems_800c6988;
import static legend.game.combat.Bttl.dragoonSpells_800c6960;
import static legend.game.combat.Bttl.itemTargetAll_800c69c8;
import static legend.game.combat.Bttl.itemTargetType_800c6b68;
import static legend.game.combat.Bttl.prepareItemList;
import static legend.game.combat.Bttl.setActiveCharacterSpell;
import static legend.game.combat.Bttl.setTempItemMagicStats;
import static legend.game.combat.Bttl.usedRepeatItems_800c6c3c;

public class SpellAndItemMenuA4 {
  private static final int[] repeatItemIds_800c6e34 = {224, 227, 228, 230, 232, 235, 236, 237, 238};

  private static final BattleMenuBackgroundUvMetrics04 battleItemMenuScrollArrowUvMetrics_800c7190 = new BattleMenuBackgroundUvMetrics04(224, 8, 16, 8);

  public short menuState_00;
  /** ushort */
  public int _02;
  /** ushort */
  public int x_04;
  /** ushort */
  public int y_06;
  public short charIndex_08;
  /**
   * <ol start="0">
   *   <li>Items</li>
   *   <li>Spells</li>
   * </ol>
   */
  public short menuType_0a;
  /** ushort */
  public int _0c;
  /** ushort */
  public int _0e;
  /** ushort */
  public int _10;
  /** ushort */
  public int _12;
  /** ushort */
  public int _14;
  /** ushort */
  public int _16;
  public short textX_18;
  public short _1a;
  public short itemOrSpellId_1c;
  public short listIndex_1e;
  public short _20;
  public short count_22;
  public short listScroll_24;
  public short _26;
  public short _28;
  public short _2a;
  public int _2c;
  public short _30;

  public int _7c;
  public int _80;
  public int _84;
  public int _88;
  public int _8c;
  public int _90;
  public int _94;
  public int _98;
  public int _9c;
  public int _a0;

  public final MV transforms = new MV();
  public final Obj[] unknownObj1 = new Obj[8];
  public Obj unknownObj2;
  public Obj upArrow;
  public Obj downArrow;

  private final BattleHud hud;

  public SpellAndItemMenuA4(final BattleHud hud) {
    this.hud = hud;
  }

  public void init() {
    if(this.unknownObj1[0] == null) {
      for(int i = 0; i < 8; i++) {
        this.unknownObj1[i] = buildBattleMenuElement("Unknown Spell/Item UI Obj 1 " + i, 0, 0, i % 4 * 16 + 192 & 0xf0, i / 4 * 8 + 32 & 0xf8, 15, 8, 0xd, Translucency.B_PLUS_F);
      }

      this.unknownObj2 = buildBattleMenuElement("Unknown Spell/Item UI Obj 2", 0, 0, 16, 128, 24, 16, 0x2c, null);

      this.upArrow = buildBattleMenuBackground("Spell/Item Up Arrow", battleItemMenuScrollArrowUvMetrics_800c7190, 0, 0, battleItemMenuScrollArrowUvMetrics_800c7190.w_02, battleItemMenuScrollArrowUvMetrics_800c7190.h_03, 0xd, null, (short)0);
      this.downArrow = buildBattleMenuBackground("Spell/Item Down Arrow", battleItemMenuScrollArrowUvMetrics_800c7190, 0, 0, battleItemMenuScrollArrowUvMetrics_800c7190.w_02, battleItemMenuScrollArrowUvMetrics_800c7190.h_03, 0xd, null, (short)1);
    }
  }

  public void delete() {
    if(this.unknownObj1[0] != null) {
      for(int i = 0; i < 8; i++) {
        if(this.unknownObj1[i] != null) {
          this.unknownObj1[i].delete();
          this.unknownObj1[i] = null;
        }
      }

      if(this.unknownObj2 != null) {
        this.unknownObj2.delete();
        this.unknownObj2 = null;
      }

      if(this.upArrow != null) {
        this.upArrow.delete();
        this.upArrow = null;
      }

      if(this.downArrow != null) {
        this.downArrow.delete();
        this.downArrow = null;
      }
    }
  }

  @Method(0x800f4964L)
  public void clearSpellAndItemMenu() {
    this.menuState_00 = 0;
    this._02 = 0;
    this.x_04 = 0;
    this.y_06 = 0;
    this.charIndex_08 = 0;
    this.menuType_0a = 0;
    this._0c = 0;
    this._0e = 0;
    this._10 = 0;
    this._12 = 0;
    this._14 = 0;
    this._16 = 0x1000;
    this.textX_18 = 0;
    this._1a = 0;
    this.itemOrSpellId_1c = -1;
    this.count_22 = 0;
    this.listScroll_24 = 0;
  }

  @Method(0x800f49bcL)
  public void initSpellAndItemMenu(final int charIndex, final int menuType) {
    this.menuState_00 = 1;
    this.x_04 = 160;
    this.y_06 = 144;
    this.charIndex_08 = (short)charIndex;
    this.menuType_0a = (short)(menuType & 0x1);
    this._0c = 0x20;
    this._0e = 0x2b;
    this._10 = 0;
    this._12 = 0;
    this._14 = 0x1;
    this._16 = 0x1000;
    this.textX_18 = 0;
    this._1a = 0;
    this.itemOrSpellId_1c = -1;
    this.listIndex_1e = 0;
    this._20 = 0;

    //LAB_800f4a58
    if(menuType == 0) {
      //LAB_800f4a9c
      prepareItemList();
      this.count_22 = (short)combatItems_800c6988.size();
    } else if(menuType == 1) {
      //LAB_800f4abc
      //LAB_800f4ae0
      int charSlot;
      for(charSlot = 0; charSlot < charCount_800c677c.get(); charSlot++) {
        if(dragoonSpells_800c6960.get(charSlot).charIndex_00.get() == this.charIndex_08) {
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
      this.count_22 = spellIndex;
    } else if(menuType == 2) {
      //LAB_800f4b4c
      this.count_22 = 0;
    }

    //LAB_800f4b50
    //LAB_800f4b54
    //LAB_800f4b60
    this._7c = 0;
    this._80 = 0;
    this._84 = 0;
    this._88 = 0;
    this._8c = 0;
    this._90 = 0;
    this._94 = 0;
    this._98 = 0;
    this._9c = 0;
    this._a0 = 0;
  }

  @Method(0x800f4b80L)
  public void handleSpellAndItemMenu() {
    if(this.menuState_00 == 0) {
      return;
    }

    int v0;
    final int a1;
    int s0;

    //LAB_800f4bc0
    switch(this.menuState_00) {
      case 1 -> {
        this._90 = 0;
        this._a0 = 0;
        this._12 = 0;
        this._10 = 0;

        if(this.menuType_0a == 0) {
          this.listScroll_24 = this._26;
          this._02 |= 0x20;
          this.listIndex_1e = this._28;
          this._20 = this._2a;
          this._94 = this._2c;

          if(this.count_22 - 1 < this.listScroll_24 + this.listIndex_1e) {
            this.listScroll_24--;

            if(this.listScroll_24 < 0) {
              this.listScroll_24 = 0;
              this.listIndex_1e = 0;
              this._20 = this._1a;
              this._94 = 0; // This was a3.1a - a3.1a
            }
          }
        } else {
          //LAB_800f4ca0
          this.listIndex_1e = 0;
          this._20 = 0;
          this._94 = 0;
          this.listScroll_24 = this._30;
        }

        //LAB_800f4cb4
        this.itemOrSpellId_1c = (short)this.getItemOrSpellId();
        this.menuState_00 = 7;
        this._02 |= 0x40;
      }

      case 2 -> {
        this._02 &= 0xfcff;
        this.itemOrSpellId_1c = (short)this.getItemOrSpellId();

        if((press_800bee94 & 0x4) != 0) { // L1
          if(this.listScroll_24 != 0) {
            this._88 = 2;
            this.listScroll_24 = 0;
            this.menuState_00 = 5;
            playSound(0, 1, 0, 0, (short)0, (short)0);
          }

          break;
        }

        //LAB_800f4d54
        if((press_800bee94 & 0x1) != 0) { // L2
          final int oldScroll = this.listScroll_24;

          if(this.count_22 - 1 >= this.listIndex_1e + 6) {
            this.listScroll_24 = 6;
          } else {
            //LAB_800f4d8c
            this.listScroll_24 = (short)(this.count_22 - (this.listIndex_1e + 1));
          }

          //LAB_800f4d90
          this._88 = 2;
          this.menuState_00 = 5;

          if(oldScroll != this.listScroll_24) {
            playSound(0, 1, 0, 0, (short)0, (short)0);
          }

          break;
        }

        //LAB_800f4dc4
        if((press_800bee94 & 0x8) != 0) { // R1
          if(this.listIndex_1e == 0) {
            break;
          }

          if(this.listIndex_1e < 7) {
            this.listScroll_24 = 0;
            this.listIndex_1e = 0;
            this._20 = this._1a;
          } else {
            //LAB_800f4df4
            this.listIndex_1e -= 7;
            this._20 += 98;
          }

          //LAB_800f4e00
          this._88 = 2;
          this.menuState_00 = 5;
          this._94 = this._1a - this._20;
          playSound(0, 1, 0, 0, (short)0, (short)0);
          break;
        }

        //LAB_800f4e40
        if((press_800bee94 & 0x2) != 0) { // R2
          if(this.listIndex_1e + 6 >= this.count_22 - 1) {
            break;
          }

          this.listIndex_1e += 7;
          this._20 -= 98;

          if(this.listIndex_1e + 6 >= this.count_22 - 1) {
            this.listScroll_24 = 0;
          }

          //LAB_800f4e98
          this._88 = 2;
          this.menuState_00 = 5;
          this._94 = this._1a - this._20;
          playSound(0, 1, 0, 0, (short)0, (short)0);
          break;
        }

        //LAB_800f4ecc
        if((input_800bee90 & 0x1000) != 0) { // Up
          if(this.listScroll_24 != 0) {
            this.menuState_00 = 5;
            this.listScroll_24--;
            this._88 = 2;
          } else {
            //LAB_800f4f18
            if(this.listIndex_1e == 0) {
              break;
            }

            this.menuState_00 = 3;
            this._02 |= 0x200;
            this._80 = 5;
            this._7c = this._20;
            this._20 += 5;
            this.listIndex_1e--;
          }

          playSound(0, 1, 0, 0, (short)0, (short)0);
          break;
        }

        //LAB_800f4f74
        if((input_800bee90 & 0x4000) != 0) { // Down
          if(this.listScroll_24 != this.count_22 - 1) {
            if(this.listIndex_1e + this.listScroll_24 + 1 < this.count_22) {
              playSound(0, 1, 0, 0, (short)0, (short)0);

              if(this.listScroll_24 != 6) {
                this.listScroll_24++;
                this._88 = 2;
                this.menuState_00 = 5;
              } else {
                //LAB_800f4ff8
                this._80 = -5;
                this.menuState_00 = 4;
                this._7c = this._20;
                this._20 -= 5;
                this.listIndex_1e++;
                this._02 |= 0x100;
              }
            }
          }

          break;
        }

        //LAB_800f5044
        this._90 = 0;

        if((press_800bee94 & 0x20) != 0) { // X
          //LAB_800f5078
          PlayerBattleEntity player = null;

          for(int charSlot = 0; charSlot < charCount_800c677c.get(); charSlot++) {
            player = battleState_8006e398.charBents_e40[charSlot].innerStruct_00;

            if(this.charIndex_08 == player.charId_272) {
              //LAB_800f503c
              battleMenu_800c6c34._800c6980 = charSlot;
              break;
            }
          }

          //LAB_800f50b8
          if(this.menuType_0a == 0) {
            player.itemId_52 = this.itemOrSpellId_1c;
            setTempItemMagicStats(player);

            if((player.item_d4.target_00 & 0x4) != 0) {
              itemTargetType_800c6b68.set(1);
            } else {
              //LAB_800f5100
              itemTargetType_800c6b68.set(0);
            }

            //LAB_800f5108
            //LAB_800f5128
            itemTargetAll_800c69c8.set((player.item_d4.target_00 & 0x2) != 0);
          } else {
            //LAB_800f5134
            final PlayerBattleEntity caster = setActiveCharacterSpell(this.itemOrSpellId_1c);

            if(caster.stats.getStat(CoreMod.MP_STAT.get()).getCurrent() < caster.spell_94.mp_06) {
              //LAB_800f5160
              //LAB_800f5168
              playSound(0, 3, 0, 0, (short)0, (short)0);
              break;
            }

            //LAB_800f517c
            this.hud.clearFloatingNumber(0);
          }

          //LAB_800f5190
          playSound(0, 2, 0, 0, (short)0, (short)0);
          this._8c = 0;
          this._02 |= 0x4;
          if(this.menuType_0a == 0) {
            this._94 = this._1a - this._20;
          }

          //LAB_800f51e8
          this.menuState_00 = 6;
          this._02 &= 0xfffd;
          break;
        }

        //LAB_800f5208
        if((press_800bee94 & 0x40) != 0) { // O
          playSound(0, 3, 0, 0, (short)0, (short)0);
          this.menuState_00 = 8;
          this._02 &= 0xfff7;
        }
      }

      case 3 -> {
        s0 = this._80;
        this._90++;
        if(this._90 >= 3) {
          s0 *= 2;
        }

        //LAB_800f5278
        a1 = this._7c + 14;
        this._20 += s0;
        if(this._20 >= a1) {
          this._20 = (short)a1;
          this.menuState_00 = 2;
        }
      }

      case 4 -> {
        s0 = this._80;
        this._90++;
        if(this._90 >= 3) {
          s0 = s0 * 2;
        }

        //LAB_800f52d4
        a1 = this._7c - 14;
        this._20 += s0;
        if(this._20 <= a1) {
          //LAB_800f5300
          this._20 = (short)a1;
          this.menuState_00 = 2;
        }
      }

      case 5 -> {
        s0 = this._88;
        this._90++;
        if(this._90 >= 3) {
          s0 = s0 / 2;
        }

        //LAB_800f5338
        if(s0 <= 1) {
          this.menuState_00 = 2;
        }
      }

      case 6 -> {
        this._a0 = 0;
        this.itemOrSpellId_1c = (short)this.getItemOrSpellId();
        PlayerBattleEntity player;

        //LAB_800f538c
        int charSlot = 0;
        do {
          player = battleState_8006e398.charBents_e40[charSlot].innerStruct_00;

          if(this.charIndex_08 == player.charId_272) {
            break;
          }

          charSlot++;
        } while(charSlot < charCount_800c677c.get());

        //LAB_800f53c8
        final int targetType;
        final boolean targetAll;
        if(this.menuType_0a == 0) { // Items
          targetType = itemTargetType_800c6b68.get();
          targetAll = itemTargetAll_800c69c8.get();
        } else { // Spells
          //LAB_800f53f8
          final int itemTargetType = player.spell_94.targetType_00;
          targetType = (itemTargetType & 0x40) > 0 ? 1 : 0;
          targetAll = (itemTargetType & 0x8) != 0;
        }

        //LAB_800f5410
        final int ret = battleMenu_800c6c34.handleTargeting(targetType, targetAll);
        if(ret == 1) { // Pressed X
          if(this.menuType_0a == 0) {
            final int itemId = this.itemOrSpellId_1c + 192;
            final Item item = REGISTRIES.items.getEntry(LodMod.itemIdMap.get(this.itemOrSpellId_1c)).get(); //TODO
            takeItemId(item);

            boolean returnItem = false;
            for(int repeatItemIndex = 0; repeatItemIndex < 9; repeatItemIndex++) {
              if(itemId == repeatItemIds_800c6e34[repeatItemIndex]) {
                returnItem = true;
                break;
              }
            }

            if(itemId == 0xfa) { // Psych Bomb X
              returnItem = true;
            }

            final RepeatItemReturnEvent repeatItemReturnEvent = EVENTS.postEvent(new RepeatItemReturnEvent(itemId, returnItem));

            if(repeatItemReturnEvent.returnItem) {
              usedRepeatItems_800c6c3c.add(item);
            }
          }

          //LAB_800f545c
          if(this.menuType_0a == 1) {
            final VitalsStat mp = player.stats.getStat(CoreMod.MP_STAT.get());
            mp.setCurrent(mp.getCurrent() - player.spell_94.mp_06);
          }

          //LAB_800f5488
          playSound(0, 2, 0, 0, (short)0, (short)0);
          this._a0 = 1;
          this.menuState_00 = 9;
        } else if(ret == -1) { // Pressed O
          //LAB_800f54b4
          playSound(0, 0, 3, 0, (short)0, (short)0);
          this.menuState_00 = 7;
          this._02 &= 0xfffb;
          this._02 |= 0x20;
        }
      }

      case 7 -> {
        if(this.menuType_0a != 0) {
          s0 = 0x80;
        } else {
          s0 = 0xba;
        }

        this.menuState_00 = 2;
        playSound(0, 4, 0, 0, (short)0, (short)0);
        this._12 = 0x52;
        this._10 = s0;
        this.textX_18 = (short)(this.x_04 - s0 / 2 + 9);
        v0 = (this.y_06 - this._12) - 16;
        this._1a = (short)v0;
        this._20 = (short)v0;
        this._02 |= 0xb;
        if((this._02 & 0x20) != 0) {
          v0 = v0 - this._94;
          this._20 = (short)v0;
        }

        //LAB_800f5588
        if(this.menuType_0a != 0) {
          this.itemOrSpellId_1c = (short)this.getItemOrSpellId();
          this.hud.addFloatingNumber(0, 1, 0, setActiveCharacterSpell(this.itemOrSpellId_1c).spell_94.mp_06, 280, 135, 0, 1);
        }
      }

      case 8 -> {
        itemTargetAll_800c69c8.set(false);
        itemTargetType_800c6b68.set(0);
        this._a0 = -1;
        this.menuState_00 = 9;
        this._12 = 0;
        this._10 = 0;
        this._02 &= 0xfffc;
        this.hud.clearFloatingNumber(0);
      }

      case 9 -> {
        if(this.menuType_0a == 0) {
          v0 = this._1a - this._20;
          this._26 = this.listScroll_24;
          this._28 = this.listIndex_1e;
          this._2a = this._20;
          this._94 = v0;
          this._2c = v0;
        }

        //LAB_800f568c
        this.clearSpellAndItemMenu();
      }
    }

    //LAB_800f5694
    //LAB_800f5698
    this._84 = tickCount_800bb0fc & 0x7;

    //LAB_800f56ac
  }

  @Method(0x800f56c4L)
  private int getItemOrSpellId() {
    if(this.menuType_0a == 0) {
      //LAB_800f56f0
      return LodMod.idItemMap.getInt(combatItems_800c6988.get(this.listScroll_24 + this.listIndex_1e).item.getRegistryId());
    }

    if(this.menuType_0a == 1) {
      //LAB_800f5718
      //LAB_800f5740
      int charSlot;
      for(charSlot = 0; charSlot < charCount_800c677c.get(); charSlot++) {
        if(dragoonSpells_800c6960.get(charSlot).charIndex_00.get() == this.charIndex_08) {
          break;
        }
      }

      //LAB_800f5778
      int spellIndex = dragoonSpells_800c6960.get(charSlot).spellIndex_01.get(this.listScroll_24 + this.listIndex_1e).get();
      if(this.charIndex_08 == 8) {
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
}
