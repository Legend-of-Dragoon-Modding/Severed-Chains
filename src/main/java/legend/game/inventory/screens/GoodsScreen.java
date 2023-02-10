package legend.game.inventory.screens;

import legend.core.MathHelper;
import legend.game.types.MenuItemStruct04;
import legend.game.types.Renderable58;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

import static legend.game.SItem.FUN_80104b60;
import static legend.game.SItem.Goods_8011cf48;
import static legend.game.SItem._8011c008;
import static legend.game.SItem.allocateUiElement;
import static legend.game.SItem.goodsGlyphs_801141c4;
import static legend.game.SItem.renderGlyphs;
import static legend.game.SItem.renderString;
import static legend.game.SItem.renderText;
import static legend.game.Scus94491BpeSegment.scriptStartEffect;
import static legend.game.Scus94491BpeSegment_8002.deallocateRenderables;
import static legend.game.Scus94491BpeSegment_8002.playSound;
import static legend.game.Scus94491BpeSegment_8002.uploadRenderables;
import static legend.game.Scus94491BpeSegment_800b.gameState_800babc8;
import static legend.game.Scus94491BpeSegment_800b.saveListDownArrow_800bdb98;
import static legend.game.Scus94491BpeSegment_800b.saveListUpArrow_800bdb94;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_ESCAPE;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_UP;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_DOWN;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_LEFT;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_RIGHT;

public class GoodsScreen extends MenuScreen {
  private int loadingStage;
  private double scrollAccumulator;
  private final Runnable unload;

  private int slotScroll;
  private int selectedSlot;
  private Renderable58 highlight;
  private Renderable58 _800bdb9c;
  private Renderable58 _800bdba0;

  private final List<MenuItemStruct04> menuItems = new ArrayList<>();

  public GoodsScreen(final Runnable unload) {
    this.unload = unload;
  }

  @Override
  protected void render() {
    switch(this.loadingStage) {
      case 0 -> {
        scriptStartEffect(2, 10);
        deallocateRenderables(0xff);
        renderGlyphs(goodsGlyphs_801141c4, 0, 0);

        //LAB_800fec7c
        this.menuItems.clear();
        for(int i = 0; i < 64; i++) {
          if((gameState_800babc8.dragoonSpirits_19c.get(i >>> 5).get() & 0x1 << (i & 0x1f)) != 0) {
            final MenuItemStruct04 item = new MenuItemStruct04();
            item.itemId_00 = i;
            this.menuItems.add(item);
          }
        }

        this.slotScroll = 0;
        this.selectedSlot = 0;
        this.highlight = allocateUiElement(0x76, 0x76, this.getSlotX(0), this.getSlotY(this.selectedSlot) + 32);
        FUN_80104b60(this.highlight);
        this.renderGoods(this.selectedSlot, this.slotScroll, 0xff);
        this.loadingStage++;
      }

      case 1 -> {
        this.renderGoods(this.selectedSlot, this.slotScroll, 0);

        if(this.scrollAccumulator >= 1.0d) {
          this.scrollAccumulator -= 1.0d;

          if(this.slotScroll > 0) {
            this.scroll(this.slotScroll - 2);
          }
        }

        if(this.scrollAccumulator <= -1.0d) {
          this.scrollAccumulator += 1.0d;

          if(this.slotScroll < MathHelper.roundUp(this.menuItems.size() / 2 - 7, 2)) {
            this.scroll(this.slotScroll + 2);
          }
        }
      }

      // Fade out
      case 100 -> {
        this.renderGoods(this.selectedSlot, this.slotScroll, 0);

        saveListDownArrow_800bdb98 = null;
        saveListUpArrow_800bdb94 = null;

        this.unload.run();
      }
    }
  }

  private void renderGoods(final int selectedSlot, final int slotScroll, final long a3) {
    final boolean allocate = a3 == 0xff;

    if(allocate) {
      allocateUiElement(0x55, 0x55, 16, 16);
      allocateUiElement(0x55, 0x55, 194, 16);
      this._800bdb9c = allocateUiElement(0x3d, 0x44, 358, this.getSlotY(2));
      this._800bdba0 = allocateUiElement(0x35, 0x3c, 358, this.getSlotY(8));
    }

    renderText(Goods_8011cf48, 32, 22, 4);
    renderText(Goods_8011cf48, 210, 22, 4);
    this.FUN_8010965c(slotScroll, this._800bdb9c, this._800bdba0);
    renderString(1, 194, 178, this.menuItems.get(slotScroll + selectedSlot).itemId_00, allocate);
    uploadRenderables();
  }

  private void FUN_8010965c(final int slotScroll, @Nullable final Renderable58 a1, @Nullable final Renderable58 a2) {
    int i;
    for(i = 0; i < 14 && slotScroll + i < this.menuItems.size(); i += 2) {
      renderText(_8011c008.get(this.menuItems.get(slotScroll + i).itemId_00).deref(), 37, this.getSlotY(i / 2) + 34, 4);

      if(slotScroll + i + 1 < this.menuItems.size()) {
        renderText(_8011c008.get(this.menuItems.get(slotScroll + i + 1).itemId_00).deref(), 214, this.getSlotY(i / 2) + 34, 4);
      }
    }

    if(a1 != null) {
      if(slotScroll > 0) {
        a1.flags_00 &= 0xffff_ffbf;
      } else {
        a1.flags_00 |= 0x40;
      }
    }

    if(a2 != null) {
      if(slotScroll + i < this.menuItems.size()) {
        a2.flags_00 &= 0xffff_ffbf;
      } else {
        a2.flags_00 |= 0x40;
      }
    }
  }

  private int getSlotX(final int slot) {
    if(slot == 0) {
      return 43;
    }

    return 221;
  }

  private int getSlotY(final int slot) {
    return 9 + slot * 17;
  }

  @Override
  protected void mouseMove(final int x, final int y) {
    if(this.loadingStage != 1) {
      return;
    }

    for(int i = 0; i < Math.min(14, this.menuItems.size() - this.slotScroll); i++) {
      if(this.selectedSlot != i && MathHelper.inBox(x, y, this.getSlotX(i & 1) - 8, this.getSlotY(i / 2) + 32, 135, 16)) {
        playSound(1);
        this.selectedSlot = i;
        this.highlight.x_40 = this.getSlotX(i & 1);
        this.highlight.y_44 = this.getSlotY(i / 2) + 32;
      }
    }
  }

  private void scroll(final int scroll) {
    playSound(1);
    this.slotScroll = scroll;
  }

  @Override
  protected void keyPress(final int key, final int scancode, final int mods) {
    if(this.loadingStage != 1 || mods != 0) {
      return;
    }

    switch(key) {
      case GLFW_KEY_LEFT -> {
        if(this.selectedSlot % 2 == 0) {
          break;
        }

        playSound(1);
        this.selectedSlot--;
        this.highlight.x_40 = this.getSlotX(this.selectedSlot & 1);
        this.highlight.y_44 = this.getSlotY(this.selectedSlot / 2) + 32;
      }

      case GLFW_KEY_RIGHT -> {
        if(this.selectedSlot % 2 != 0 || this.selectedSlot + this.slotScroll * 2 == this.menuItems.size() - 1) {
          break;
        }

        playSound(1);
        this.selectedSlot++;
        this.highlight.x_40 = this.getSlotX(this.selectedSlot & 1);
        this.highlight.y_44 = this.getSlotY(this.selectedSlot / 2) + 32;
      }

      case GLFW_KEY_DOWN -> {
        if(this.selectedSlot >= 12) {
          if((this.selectedSlot + this.slotScroll * 2) < this.menuItems.size()) {
            this.scroll(this.slotScroll + 2);
          }

          break;
        }

        playSound(1);
        this.selectedSlot += 2;
        this.highlight.x_40 = this.getSlotX(this.selectedSlot & 1);
        this.highlight.y_44 = this.getSlotY(this.selectedSlot / 2) + 32;
      }

      case GLFW_KEY_UP -> {
        if(this.selectedSlot < 2) {
          if(this.slotScroll > 0) {
            this.scroll(this.slotScroll - 2);
          }

          break;
        }

        playSound(1);
        this.selectedSlot -= 2;
        this.highlight.x_40 = this.getSlotX(this.selectedSlot & 1);
        this.highlight.y_44 = this.getSlotY(this.selectedSlot / 2) + 32;
      }

      case GLFW_KEY_ESCAPE -> {
        playSound(3);
        this.loadingStage = 100;
      }
    }
  }

  @Override
  protected void mouseScroll(final double deltaX, final double deltaY) {
    if(this.loadingStage != 1) {
      return;
    }

    if(this.scrollAccumulator < 0 && deltaY > 0 || this.scrollAccumulator > 0 && deltaY < 0) {
      this.scrollAccumulator = 0;
    }

    this.scrollAccumulator += deltaY;
  }
}
