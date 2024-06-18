package legend.game.inventory.screens;

import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import it.unimi.dsi.fastutil.ints.IntSet;
import legend.game.i18n.I18n;
import legend.game.input.InputAction;
import legend.game.inventory.screens.controls.Background;
import legend.game.inventory.screens.controls.Label;
import legend.game.inventory.screens.controls.Textbox;
import legend.game.modding.coremod.CoreMod;
import legend.game.modding.coremod.config.ControllerKeybindConfigEntry;
import legend.game.saves.ConfigCollection;
import legend.game.types.MessageBoxResult;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static legend.game.Scus94491BpeSegment.startFadeEffect;
import static legend.game.Scus94491BpeSegment_8002.deallocateRenderables;
import static legend.game.Scus94491BpeSegment_8002.playMenuSound;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_9;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_A;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_APOSTROPHE;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_BACKSPACE;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_COMMA;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_DELETE;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_DOWN;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_END;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_ENTER;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_EQUAL;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_ESCAPE;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_GRAVE_ACCENT;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_HOME;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_INSERT;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_LEFT;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_PAGE_DOWN;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_PAGE_UP;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_RIGHT;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_RIGHT_BRACKET;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_SEMICOLON;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_SPACE;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_TAB;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_UP;

public class KeybindsScreen extends VerticalLayoutScreen {
  private final Runnable unload;
  private final ConfigCollection config;

  private final Map<Integer, String> validKeys = new LinkedHashMap<>();

  public KeybindsScreen(final ConfigCollection config, final Runnable unload) {
    this.addKey(GLFW_KEY_SPACE, "SPACE");
    this.addRegularKey(GLFW_KEY_APOSTROPHE);
    this.addRegularKeyRange(GLFW_KEY_COMMA, GLFW_KEY_9);
    this.addRegularKey(GLFW_KEY_SEMICOLON);
    this.addRegularKey(GLFW_KEY_EQUAL);
    this.addRegularKeyRange(GLFW_KEY_A, GLFW_KEY_RIGHT_BRACKET);
    this.addRegularKey(GLFW_KEY_GRAVE_ACCENT);
    this.addKey(GLFW_KEY_ESCAPE, "ESC");
    this.addKey(GLFW_KEY_ENTER, "ENTER");
    this.addKey(GLFW_KEY_TAB, "TAB");
    this.addKey(GLFW_KEY_BACKSPACE, "BACKSPACE");
    this.addKey(GLFW_KEY_INSERT, "INSERT");
    this.addKey(GLFW_KEY_RIGHT, "RIGHT");
    this.addKey(GLFW_KEY_LEFT, "LEFT");
    this.addKey(GLFW_KEY_DOWN, "DOWN");
    this.addKey(GLFW_KEY_UP, "UP");
    this.addKey(GLFW_KEY_PAGE_UP, "PGUP");
    this.addKey(GLFW_KEY_PAGE_DOWN, "PGDN");
    this.addKey(GLFW_KEY_HOME, "HOME");
    this.addKey(GLFW_KEY_END, "END");

    deallocateRenderables(0xff);
    startFadeEffect(2, 10);

    this.config = config;
    this.unload = unload;

    this.addControl(new Background());

    final Label help = new Label(I18n.translate(CoreMod.MOD_ID + ".keybind.help"));
    help.setPos(32, 12);
    help.setWidth(this.getWidth() - 64);
    help.setHorizontalAlign(Label.HorizontalAlign.CENTRE);
    help.hide();
    this.addControl(help);

    final Map<ControllerKeybindConfigEntry, Textbox> textboxes = new HashMap<>();

    for(final InputAction inputAction : InputAction.values()) {
      if(CoreMod.KEYBIND_CONFIGS.containsKey(inputAction)) {
        final Textbox textbox = new Textbox();
        textboxes.put(CoreMod.KEYBIND_CONFIGS.get(inputAction).get(), textbox);
        textbox.setText(this.keysToString(config.getConfig(CoreMod.KEYBIND_CONFIGS.get(inputAction).get())));

        final IntSet keycodes = new IntOpenHashSet();

        textbox.onGotFocus(() -> {
          keycodes.clear();
          textbox.setText("");
          help.show();
        });

        textbox.onLostFocus(() -> {
          final List<ControllerKeybindConfigEntry> dupes = new ArrayList<>();

          for(final InputAction action : InputAction.values()) {
            if(action != inputAction && CoreMod.KEYBIND_CONFIGS.containsKey(action) && config.getConfig(CoreMod.KEYBIND_CONFIGS.get(action).get()).intStream().anyMatch(keycodes::contains)) {
              dupes.add(CoreMod.KEYBIND_CONFIGS.get(action).get());
            }
          }

          if(dupes.isEmpty()) {
            config.setConfig(CoreMod.KEYBIND_CONFIGS.get(inputAction).get(), new IntOpenHashSet(keycodes));
            help.hide();
          } else {
            this.getStack().pushScreen(new MessageBoxScreen(I18n.translate("lod-core.keybind.duplicate_input"), 2, result -> {
              if(result == MessageBoxResult.YES) {
                for(final ControllerKeybindConfigEntry dupe : dupes) {
                  config.getConfig(dupe).removeAll(keycodes);
                  textboxes.get(dupe).setText(this.keysToString(config.getConfig(dupe)));
                }
              }

              config.setConfig(CoreMod.KEYBIND_CONFIGS.get(inputAction).get(), new IntOpenHashSet(keycodes));
              help.hide();
            }));
          }
        });

        textbox.onKeyPress((key, scancode, mods) -> {
          if(key == GLFW_KEY_DELETE) {
            textbox.unfocus();
            return InputPropagation.HANDLED;
          }

          if(this.validKeys.containsKey(key)) {
            keycodes.add(key);
            textbox.setText(this.keysToString(keycodes));
          }

          return InputPropagation.HANDLED;
        });

        textbox.onCharPress(codepoint -> InputPropagation.HANDLED);

        this.addRow(I18n.translate(CoreMod.MOD_ID + ".keybind." + inputAction.name()), textbox);
      }
    }

    this.addRow(I18n.translate(CoreMod.MOD_ID + ".keybind.pause"), new Label("F11"));
    this.addRow(I18n.translate(CoreMod.MOD_ID + ".keybind.debugger"), new Label("F12"));
    this.addRow(I18n.translate(CoreMod.MOD_ID + ".keybind.kill_sound"), new Label("DEL"));
  }

  private void addKey(final int keycode, final String name) {
    this.validKeys.put(keycode, name);
  }

  private void addRegularKey(final int keycode) {
    this.addKey(keycode, String.valueOf((char)keycode));
  }

  private void addRegularKeyRange(final int keycodeStart, final int keycodeEnd) {
    for(int i = keycodeStart; i <= keycodeEnd; i++) {
      this.addRegularKey(i);
    }
  }

  private String keysToString(final IntSet keycodes) {
    return keycodes.intStream().sorted().mapToObj(this::keyToString).collect(Collectors.joining(", "));
  }

  private String keyToString(final int keycode) {
    return this.validKeys.getOrDefault(keycode, "");
  }

  @Override
  public InputPropagation pressedThisFrame(final InputAction inputAction) {
    if(super.pressedThisFrame(inputAction) == InputPropagation.HANDLED) {
      return InputPropagation.HANDLED;
    }

    if(inputAction == InputAction.BUTTON_EAST) {
      for(final InputAction action : InputAction.values()) {
        if(CoreMod.KEYBIND_CONFIGS.containsKey(action)) {
          if(this.config.getConfig(CoreMod.KEYBIND_CONFIGS.get(action).get()).isEmpty()) {
            playMenuSound(4);
            this.getStack().pushScreen(new MessageBoxScreen(I18n.translate(CoreMod.MOD_ID + ".keybind.missing_input"), 0, result -> { }));
            return InputPropagation.HANDLED;
          }
        }
      }

      playMenuSound(3);
      this.unload.run();
      return InputPropagation.HANDLED;
    }

    return InputPropagation.PROPAGATE;
  }
}
