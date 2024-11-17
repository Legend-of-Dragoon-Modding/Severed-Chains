package legend.game.combat.ui;

import legend.core.Config;
import legend.core.QueuedModelStandard;
import legend.core.gte.MV;
import legend.core.memory.Method;
import legend.core.opengl.Obj;
import legend.core.opengl.QuadBuilder;
import legend.game.combat.bent.PlayerBattleEntity;
import legend.game.combat.environment.BattleMenuBackgroundUvMetrics04;
import legend.game.input.Input;
import legend.game.input.InputAction;
import legend.game.scripting.RunningScript;
import legend.game.types.Translucency;

import static legend.core.GameEngine.RENDERER;
import static legend.game.Scus94491BpeSegment_8002.playMenuSound;
import static legend.game.Scus94491BpeSegment_800b.tickCount_800bb0fc;

public abstract class ListMenu {
  private static final BattleMenuBackgroundUvMetrics04 battleItemMenuScrollArrowUvMetrics_800c7190 = new BattleMenuBackgroundUvMetrics04(224, 8, 16, 8);

  protected int menuState_00;
  /** ushort */
  protected int flags_02;
  /** ushort */
  protected int x_04;
  /** ushort */
  protected int y_06;
  protected final PlayerBattleEntity player_08;

  /** ushort */
  protected int width_10;
  /** ushort */
  protected int height_12;

  protected int textX_18;
  protected int listStartY_1a;

  protected int listScroll_1e;
  /** Decreases as you scroll down the list */
  protected int listOffsetY_20;

  /** Selected item relative to listScroll */
  protected int listIndex_24;

  protected int lastListOffset_7c;
  /** +5 for up, -5 for down*/
  protected int scrollAmount_80;
  protected int selectionArrowFrame_84;

  /** Used to speed up scrolling after holding up or down for 3 items */
  protected int scrollCounter_90;

  /**
   * <ul>
   *   <li>0 - nothing selected</li>
   *   <li>1 - item or spell selected</li>
   *   <li>2 - menu unloading</li>
   * </ul>
   */
  public int selectionState_a0;

  private UiBox battleUiList;
  protected final BattleHud hud;

  protected final MV transforms = new MV();
  protected Obj menuObj;
  protected int arrowObjOffset;
  protected int mpObjOffset;
  protected int upObjOffset;
  protected int downObjOffset;

  private final ListPosition lastPosition;
  private final Runnable onClose;

  public ListMenu(final BattleHud hud, final PlayerBattleEntity activePlayer, final int width, final ListPosition lastPosition, final Runnable onClose) {
    this.hud = hud;
    this.lastPosition = lastPosition;
    this.onClose = onClose;

    this.menuState_00 = 1;
    this.flags_02 |= 0x1 | 0x2 | 0x8 | 0x40;
    this.x_04 = 160;
    this.y_06 = 144;
    this.player_08 = activePlayer;
    this.width_10 = width;
    this.height_12 = 82;
    this.textX_18 = this.x_04 - width / 2 + 9;

    final int y = (this.y_06 - this.height_12) - 16;
    this.listStartY_1a = y;
    this.listOffsetY_20 = y;
  }

  protected abstract int getListCount();
  protected abstract void drawListEntry(final int index, final int x, final int y, final int trim);
  protected abstract void onSelection(final int index);
  protected abstract void onUse(final int index);
  protected abstract void onClose();
  protected abstract int handleTargeting();
  public abstract void getTargetingInfo(final RunningScript<?> script);

  private void initObjs() {
    if(this.menuObj == null) {
      final QuadBuilder builder = new QuadBuilder("Spell/item menu");

      this.arrowObjOffset = builder.currentQuadIndex() * 4;
      for(int i = 0; i < 8; i++) {
        this.hud.buildBattleMenuElement(builder, 0, 0, i % 4 * 16 + 192 & 0xf0, i / 4 * 8 + 32 & 0xf8, 15, 8, 0xd);
      }

      this.mpObjOffset = builder.currentQuadIndex() * 4;
      this.hud.buildBattleMenuElement(builder, 0, 0, 16, 128, 24, 16, 0x2c);

      this.upObjOffset = builder.currentQuadIndex() * 4;
      this.hud.buildBattleMenuBackground(builder, battleItemMenuScrollArrowUvMetrics_800c7190, 0, 0, battleItemMenuScrollArrowUvMetrics_800c7190.w_02, battleItemMenuScrollArrowUvMetrics_800c7190.h_03, 0xd, (short)0);
      this.downObjOffset = builder.currentQuadIndex() * 4;
      this.hud.buildBattleMenuBackground(builder, battleItemMenuScrollArrowUvMetrics_800c7190, 0, 0, battleItemMenuScrollArrowUvMetrics_800c7190.w_02, battleItemMenuScrollArrowUvMetrics_800c7190.h_03, 0xd, (short)1);

      this.menuObj = builder.build();
    }
  }

  @Method(0x800f4b80L)
  public void tick() {
    //LAB_800f4bc0
    switch(this.menuState_00) {
      // Restore position
      case 1 -> {
        this.listIndex_24 = this.lastPosition.lastListIndex_26;
        this.listScroll_1e = this.lastPosition.lastListScroll_28;

        if(this.listIndex_24 + this.listScroll_1e > this.getListCount() - 1) {
          final int delta = this.listIndex_24 + this.listScroll_1e - (this.getListCount() - 1);
          this.listScroll_1e -= delta;

          if(this.listScroll_1e < 0) {
            this.listIndex_24 += this.listScroll_1e;
            this.listScroll_1e = 0;
          }
        }

        this.listOffsetY_20 = (short)(this.y_06 - this.height_12 - 16 - this.listScroll_1e * 14);
        this.menuState_00 = 2;
      }

      case 2 -> {
        this.flags_02 &= ~(0x100 | 0x200);

        if(Input.pressedThisFrame(InputAction.BUTTON_SHOULDER_LEFT_1)) {
          if(this.listIndex_24 != 0) {
            this.listIndex_24 = 0;
            this.menuState_00 = 5;
            playMenuSound(1);
          }

          break;
        }

        //LAB_800f4d54
        if(Input.pressedThisFrame(InputAction.BUTTON_SHOULDER_LEFT_2)) {
          final int oldScroll = this.listIndex_24;

          if(this.getListCount() - 1 >= this.listScroll_1e + 6) {
            this.listIndex_24 = 6;
          } else {
            //LAB_800f4d8c
            this.listIndex_24 = this.getListCount() - (this.listScroll_1e + 1);
          }

          //LAB_800f4d90
          this.menuState_00 = 5;

          if(oldScroll != this.listIndex_24) {
            playMenuSound(1);
          }

          break;
        }

        //LAB_800f4dc4
        if(Input.pressedThisFrame(InputAction.BUTTON_SHOULDER_RIGHT_1)) {
          if(this.listScroll_1e == 0) {
            break;
          }

          if(this.listScroll_1e < 7) {
            this.listIndex_24 = 0;
            this.listScroll_1e = 0;
            this.listOffsetY_20 = this.listStartY_1a;
          } else {
            //LAB_800f4df4
            this.listScroll_1e -= 7;
            this.listOffsetY_20 += 98;
          }

          //LAB_800f4e00
          this.menuState_00 = 5;
          playMenuSound(1);
          break;
        }

        //LAB_800f4e40
        if(Input.pressedThisFrame(InputAction.BUTTON_SHOULDER_RIGHT_2)) {
          if(this.listScroll_1e + 6 >= this.getListCount() - 1) {
            break;
          }

          this.listScroll_1e += 7;
          this.listOffsetY_20 -= 98;

          if(this.listScroll_1e + 6 >= this.getListCount() - 1) {
            this.listIndex_24 = 0;
          }

          //LAB_800f4e98
          this.menuState_00 = 5;
          playMenuSound(1);
          break;
        }

        //LAB_800f4ecc
        if(Input.getButtonState(InputAction.DPAD_UP) || Input.getButtonState(InputAction.JOYSTICK_LEFT_BUTTON_UP)) {
          if(this.listIndex_24 != 0) {
            this.menuState_00 = 5;
            this.listIndex_24--;
          } else {
            //LAB_800f4f18
            if(this.listScroll_1e == 0) {
              break;
            }

            this.menuState_00 = 3;
            this.flags_02 |= 0x200;
            this.scrollAmount_80 = 5;
            this.lastListOffset_7c = this.listOffsetY_20;
            this.listOffsetY_20 += 5;
            this.listScroll_1e--;
          }

          playMenuSound(1);
          break;
        }

        //LAB_800f4f74
        if(Input.getButtonState(InputAction.DPAD_DOWN) || Input.getButtonState(InputAction.JOYSTICK_LEFT_BUTTON_DOWN)) {
          if(this.listIndex_24 != this.getListCount() - 1) {
            if(this.listScroll_1e + this.listIndex_24 + 1 < this.getListCount()) {
              playMenuSound(1);

              if(this.listIndex_24 != 6) {
                this.listIndex_24++;
                this.menuState_00 = 5;
              } else {
                //LAB_800f4ff8
                this.scrollAmount_80 = -5;
                this.menuState_00 = 4;
                this.lastListOffset_7c = this.listOffsetY_20;
                this.listOffsetY_20 -= 5;
                this.listScroll_1e++;
                this.flags_02 |= 0x100;
              }
            }
          }

          break;
        }

        //LAB_800f5044
        this.scrollCounter_90 = 0;

        if(Input.pressedThisFrame(InputAction.BUTTON_SOUTH)) {
          //LAB_800f5078
          this.hud.battleMenu_800c6c34.targetedPlayerSlot_800c6980 = this.player_08.charSlot_276;
          this.onSelection(this.listScroll_1e + this.listIndex_24);

          //LAB_800f5190
          playMenuSound(2);

          //LAB_800f51e8
          this.menuState_00 = 6;
          this.flags_02 &= ~0x2;
          this.flags_02 |= 0x4;
          break;
        }

        //LAB_800f5208
        if(Input.pressedThisFrame(InputAction.BUTTON_EAST)) {
          playMenuSound(3);
          this.menuState_00 = 8;
          this.flags_02 &= ~0x8;
        }
      }

      // Scroll past top of list
      case 3 -> {
        int scrollAmount = this.scrollAmount_80;
        this.scrollCounter_90++;
        if(this.scrollCounter_90 >= 3) {
          scrollAmount *= 2;
        }

        //LAB_800f5278
        final int scrollDest = this.lastListOffset_7c + 14;
        this.listOffsetY_20 += scrollAmount;
        if(this.listOffsetY_20 >= scrollDest) {
          this.listOffsetY_20 = scrollDest;
          this.menuState_00 = 2;
        }
      }

      // Scroll past bottom of list
      case 4 -> {
        int scrollAmount = this.scrollAmount_80;
        this.scrollCounter_90++;
        if(this.scrollCounter_90 >= 3) {
          scrollAmount *= 2;
        }

        //LAB_800f52d4
        final int scrollDest = this.lastListOffset_7c - 14;
        this.listOffsetY_20 += scrollAmount;
        if(this.listOffsetY_20 <= scrollDest) {
          //LAB_800f5300
          this.listOffsetY_20 = scrollDest;
          this.menuState_00 = 2;
        }
      }

      // Scroll down
      case 5 -> {
        this.scrollCounter_90++;

        //LAB_800f5338
        if(this.scrollCounter_90 >= 3) {
          this.menuState_00 = 2;
        }
      }

      case 6 -> {
        this.selectionState_a0 = 0;

        //LAB_800f53c8
        //LAB_800f5410
        final int ret = this.handleTargeting();
        if(ret == 1) { // Pressed X
          this.onUse(this.listScroll_1e + this.listIndex_24);

          //LAB_800f5488
          playMenuSound(2);
          this.selectionState_a0 = 1;
          this.menuState_00 = 9;
        } else if(ret == 2) { // Instant confirm, no sound
          this.onUse(this.listScroll_1e + this.listIndex_24);
          this.selectionState_a0 = 1;
          this.menuState_00 = 9;
        } else if(ret == -1) { // Pressed O
          //LAB_800f54b4
          playMenuSound(0);
          playMenuSound(4);
          this.menuState_00 = 2;
          this.flags_02 &= ~0x4;
          this.flags_02 |= 0x1 | 0x2 | 0x8 | 0x20;
        }
      }

      // Close list
      case 8 -> {
        this.selectionState_a0 = -1;
        this.menuState_00 = 9;
        this.flags_02 &= ~(0x1 | 0x2);
      }

      // Deallocate list
      case 9 -> {
        this.lastPosition.lastListIndex_26 = this.listIndex_24;
        this.lastPosition.lastListScroll_28 = this.listScroll_1e;
        this.onClose();
        this.delete();
        this.onClose.run();
      }

      default -> throw new RuntimeException("Invalid menu state " + this.menuState_00);
    }

    //LAB_800f5694
    //LAB_800f5698
    this.selectionArrowFrame_84 = tickCount_800bb0fc & 0x7;

    //LAB_800f56ac
  }

  public void draw() {
    if(this.menuState_00 != 0 && (this.flags_02 & 0x1) != 0) {
      this.initObjs();

      if((this.flags_02 & 0x2) != 0) {
        //LAB_800f5ee8
        //Item menu
        final int w = this.width_10 + 6;
        final int h = this.height_12 + 17;

        if(this.battleUiList == null) {
          this.battleUiList = new UiBox("Battle UI List", this.x_04 - w / 2, this.y_06 - h, w, h);
        }

        this.battleUiList.render(Config.changeBattleRgb() ? Config.getBattleRgb() : Config.defaultUiColour);

        this.drawList();

        if((this.flags_02 & 0x8) != 0) {
          //LAB_800f5d78
          //LAB_800f5d90
          this.transforms.identity();
          this.transforms.transfer.set(this.textX_18 - 16, this.listStartY_1a + this.listIndex_24 * 14 + 2, 124.0f);
          RENDERER.queueOrthoModel(this.menuObj, this.transforms, QueuedModelStandard.class)
            .vertices(this.arrowObjOffset + this.selectionArrowFrame_84 * 4, 4)
            .translucency(Translucency.B_PLUS_F);

          //LAB_800f5e00
          final int s1;
          if((this.flags_02 & 0x100) != 0) {
            s1 = 2;
          } else {
            s1 = 0;
          }

          //LAB_800f5e18
          final int t0;
          if((this.flags_02 & 0x200) != 0) {
            t0 = -2;
          } else {
            t0 = 0;
          }

          //LAB_800f5e24
          if(this.listScroll_1e > 0) {
            this.transforms.identity();
            this.transforms.transfer.set(this.x_04 + 82, this.y_06 + t0 - 100, 124.0f);
            RENDERER.queueOrthoModel(this.menuObj, this.transforms, QueuedModelStandard.class)
              .vertices(this.upObjOffset, 4);
          }

          //LAB_800f5e7c
          if(this.listScroll_1e + 6 < this.getListCount() - 1) {
            this.transforms.identity();
            this.transforms.transfer.set(this.x_04 + 82, this.y_06 + s1 - 7, 124.0f);
            RENDERER.queueOrthoModel(this.menuObj, this.transforms, QueuedModelStandard.class)
              .vertices(this.downObjOffset, 4);
          }
        }
      }
    }
  }

  @Method(0x800f57f8L)
  private void drawList() {
    int trim;

    int y1 = this.listOffsetY_20;
    final int y2 = this.listStartY_1a;
    final int sp68 = this.y_06;

    //LAB_800f5860
    //LAB_800f58a4
    //LAB_800f58e0
    for(int index = 0; index < this.getListCount(); index++) {
      if(y1 >= sp68) {
        break;
      }

      //LAB_800f5af0
      if(y1 >= y2) {
        //LAB_800f5b90
        if(sp68 >= y1 + 12 || (this.flags_02 & 0x4) != 0) {
          trim = 0;
        } else {
          trim = y1 - (sp68 - 12);
        }

        //LAB_800f5bb4
        //LAB_800f5bd8
        this.drawListEntry(index, this.textX_18, y1, trim);
      } else if(y2 < y1 + 12) {
        if((this.flags_02 & 0x4) != 0) {
          trim = 0;
        } else {
          trim = y1 - y2;
        }

        //LAB_800f5b40
        this.drawListEntry(index, this.textX_18, y2, trim);
      }

      //LAB_800f5c38
      y1 += 14;
    }

    //LAB_800f5c64
  }

  public void delete() {
    if(this.menuObj != null) {
      this.menuObj.delete();
      this.menuObj = null;
    }

    if(this.battleUiList != null) {
      this.battleUiList.delete();
      this.battleUiList = null;
    }
  }
}
