package legend.game.inventory.screens;

import legend.core.MathHelper;
import legend.game.types.LodString;
import legend.game.types.MessageBox20;
import legend.game.types.MessageBoxResult;

import java.util.function.Consumer;

import static legend.game.SItem.No_8011c214;
import static legend.game.SItem.Yes_8011c20c;
import static legend.game.SItem.menuStack;
import static legend.game.SItem.messageBox;
import static legend.game.SItem.setMessageBoxOptions;
import static legend.game.SItem.setMessageBoxText;
import static legend.game.Scus94491BpeSegment_8002.playSound;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_ENTER;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_UP;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_DOWN;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_S;

public class MessageBoxScreen extends MenuScreen {
  private final MessageBox20 messageBox = new MessageBox20();
  private final Consumer<MessageBoxResult> onResult;
  private MessageBoxResult result;

  public MessageBoxScreen(final LodString text, final int type, final Consumer<MessageBoxResult> onResult) {
    this(text, Yes_8011c20c, No_8011c214, type, onResult);
  }

  public MessageBoxScreen(final LodString text, final LodString yes, final LodString no, final int type, final Consumer<MessageBoxResult> onResult) {
    setMessageBoxText(this.messageBox, text, type);
    setMessageBoxOptions(this.messageBox, yes, no);
    this.onResult = onResult;
  }

  @Override
  protected void render() {
    messageBox(this.messageBox);

    if(this.messageBox.state_0c == 0) {
      menuStack.popScreen();
      this.onResult.accept(this.result);
    }
  }

  @Override
  protected void mouseMove(final int x, final int y) {
    if(this.messageBox.state_0c != 3) {
      return;
    }

    // Yes/no
    if(this.messageBox.type_15 == 2) {
      final int selectionY = this.messageBox.y_1e + 7 + this.messageBox.text_00.length * 14 + 7;

      if(this.messageBox.menuIndex_18 != 0 && MathHelper.inBox(x, y, this.messageBox.x_1c + 4, selectionY, 112, 14)) {
        playSound(1);
        this.messageBox.menuIndex_18 = 0;

        if(this.messageBox.renderable_04 != null) {
          this.messageBox.renderable_04.y_44 = selectionY - 2;
        }
      } else if(this.messageBox.menuIndex_18 != 1 && MathHelper.inBox(x, y, this.messageBox.x_1c + 4, selectionY + 14, 112, 14)) {
        playSound(1);
        this.messageBox.menuIndex_18 = 1;

        if(this.messageBox.renderable_04 != null) {
          this.messageBox.renderable_04.y_44 = selectionY + 12;
        }
      }
    }
  }

  @Override
  protected void mouseClick(final int x, final int y, final int button, final int mods) {
    if(this.messageBox.state_0c != 3) {
      return;
    }

    if(this.messageBox.type_15 == 0) {
      playSound(2);
      this.result = MessageBoxResult.YES;
      this.messageBox.state_0c = 4;
    } else if(this.messageBox.type_15 == 2) {
      // Yes/no
      final int selectionY = this.messageBox.y_1e + 7 + this.messageBox.text_00.length * 14 + 7;

      if(MathHelper.inBox(x, y, this.messageBox.x_1c + 4, selectionY, 112, 14)) {
        playSound(2);
        this.messageBox.menuIndex_18 = 0;

        if(this.messageBox.renderable_04 != null) {
          this.messageBox.renderable_04.y_44 = selectionY - 2;
        }

        this.result = MessageBoxResult.YES;
        this.messageBox.state_0c = 4;
      } else if(MathHelper.inBox(x, y, this.messageBox.x_1c + 4, selectionY + 14, 112, 14)) {
        playSound(2);
        this.messageBox.menuIndex_18 = 1;

        if(this.messageBox.renderable_04 != null) {
          this.messageBox.renderable_04.y_44 = selectionY + 12;
        }

        this.result = MessageBoxResult.NO;
        this.messageBox.state_0c = 4;
      }
    }
  }

  @Override
  protected void keyPress(final int key, final int scancode, final int mods) {

    if(this.messageBox.type_15 == 0) {
      playSound(2);
      this.result = MessageBoxResult.YES;
      this.messageBox.state_0c = 4;
      return;
    }

    if(this.messageBox.state_0c != 3 || this.messageBox.type_15 != 2) {
      return;
    }

    final int selectionY = this.messageBox.y_1e + 7 + this.messageBox.text_00.length * 14 + 7;

    switch(key) {
      case GLFW_KEY_UP -> {
        playSound(1);

        this.messageBox.menuIndex_18 = 0;
        if(this.messageBox.renderable_04 != null) {
          this.messageBox.renderable_04.y_44 = selectionY - 2;
        }
      }

      case GLFW_KEY_DOWN -> {
        playSound(1);

        this.messageBox.menuIndex_18 = 1;
        if(this.messageBox.renderable_04 != null) {
          this.messageBox.renderable_04.y_44 = selectionY + 12;
        }
      }

      case GLFW_KEY_ENTER, GLFW_KEY_S -> {
        playSound(2);

        if(this.messageBox.menuIndex_18 == 0) {
          this.result = MessageBoxResult.YES;
        } else {
          this.result = MessageBoxResult.NO;
        }

        this.messageBox.state_0c = 4;
      }
    }
  }

  @Override
  protected boolean propagateRender() {
    return true;
  }
}
