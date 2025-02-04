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
import static org.lwjgl.glfw.GLFW.GLFW_KEY_F1;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_GRAVE_ACCENT;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_HOME;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_INSERT;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_LEFT;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_MINUS;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_PAGE_DOWN;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_PAGE_UP;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_RIGHT;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_RIGHT_BRACKET;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_SEMICOLON;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_SPACE;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_TAB;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_UP;
import static org.lwjgl.glfw.GLFW.GLFW_MOD_ALT;
import static org.lwjgl.glfw.GLFW.GLFW_MOD_CONTROL;
import static org.lwjgl.glfw.GLFW.GLFW_MOD_SHIFT;

public class KeybindsScreen extends VerticalLayoutScreen {
  private final Runnable unload;
  private final ConfigCollection config;

  private final Map<Integer, String> validKeys = new LinkedHashMap<>();
  private final Map<Integer, String> validMods = new LinkedHashMap<>();

  public KeybindsScreen(final ConfigCollection config, final Runnable unload) {
    this.addKey(GLFW_KEY_SPACE, "SPACE");
    this.addRegularKey(GLFW_KEY_APOSTROPHE);
    this.addRegularKeyRange(GLFW_KEY_COMMA, GLFW_KEY_9);
    this.addRegularKey(GLFW_KEY_SEMICOLON);
    this.addRegularKey(GLFW_KEY_EQUAL);
    this.addRegularKey(GLFW_KEY_MINUS);
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

    // Modifiers
    this.addMod(GLFW_MOD_ALT, "ALT");
    this.addMod(GLFW_MOD_CONTROL, "CTRL");
    this.addMod(GLFW_MOD_SHIFT, "SHFT");

    for(int i = 0; i < 12; i++) {
      this.addKey(GLFW_KEY_F1 + i, "F" + (i + 1));
    }

    deallocateRenderables(0xff);
    startFadeEffect(2, 10);

    this.config = config;
    this.unload = unload;

    this.addControl(new Background());

    final Label help = new Label(I18n.translate(CoreMod.MOD_ID + ".keybind.help"));
    final Label supportedMods = new Label(I18n.translate(CoreMod.MOD_ID + ".keybind.mods") + ' ' + String.join(", ", this.validMods.values()));

    supportedMods.setPos(32, 6);
    supportedMods.setWidth(this.getWidth() - 64);
    supportedMods.getFontOptions().horizontalAlign(HorizontalAlign.CENTRE);
    supportedMods.hide();
    this.addControl(supportedMods);

    help.setPos(32, 18);
    help.setWidth(this.getWidth() - 64);
    help.getFontOptions().horizontalAlign(HorizontalAlign.CENTRE);
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
          supportedMods.show();
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
            supportedMods.hide();
          } else {
            this.getStack().pushScreen(new MessageBoxScreen(I18n.translate("lod_core.keybind.duplicate_input"), 2, result -> {
              if(result == MessageBoxResult.YES) {
                for(final ControllerKeybindConfigEntry dupe : dupes) {
                  config.getConfig(dupe).removeAll(keycodes);
                  textboxes.get(dupe).setText(this.keysToString(config.getConfig(dupe)));
                }
              }

              config.setConfig(CoreMod.KEYBIND_CONFIGS.get(inputAction).get(), new IntOpenHashSet(keycodes));
              help.hide();
              supportedMods.hide();
            }));
          }
        });

        textbox.onKeyPress((key, scancode, mods) -> {
          if(key == GLFW_KEY_DELETE) {
            textbox.unfocus();
            return InputPropagation.HANDLED;
          }

          if(this.validKeys.containsKey(key)) {
            final int addedKey = key | (this.areModsValid(mods) ? mods << 9 : 0);
            keycodes.add(addedKey);
            textbox.setText(this.keysToString(keycodes));
          }

          return InputPropagation.HANDLED;
        });

        textbox.onCharPress(codepoint -> InputPropagation.HANDLED);

        this.addRow(I18n.translate(CoreMod.MOD_ID + ".keybind." + inputAction.name()), textbox);
      }
    }
  }

  private boolean areModsValid(final int mods) {
    if(mods == 0) {
      return false;
    }

    for(int i = 0; i < 32; i++) {
      final int bitMask = 1 << i;
      if((mods & bitMask) != 0) {
        if(!this.validMods.containsKey(bitMask)) {
          return false;
        }
      }
    }
    return true;
  }

  private String modsToText(final int mods) {
    final StringBuilder combo = new StringBuilder();

    for(final Map.Entry<Integer, String> entry : this.validMods.entrySet()) {
      if((mods & entry.getKey()) != 0) {
        if(!combo.isEmpty()) {
          combo.append('+');
        }
        combo.append(entry.getValue());
      }
    }
    if(!combo.isEmpty()) {
      combo.append('+');
    }
    return combo.toString();
  }

  private void addKey(final int keycode, final String name) {
    this.validKeys.put(keycode, name);
  }

  private void addMod(final int mod, final String name) {
    this.validMods.put(mod, name);
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
    final int mods = keycode >> 9;
    final int key = keycode & 0x1FF;
    return this.modsToText(mods) + this.validKeys.getOrDefault(key, "");
  }

  @Override
  public InputPropagation pressedThisFrame(final InputAction inputAction) {
    if(super.pressedThisFrame(inputAction) == InputPropagation.HANDLED) {
      return InputPropagation.HANDLED;
    }

    if(inputAction == InputAction.BUTTON_EAST) {
      for(final InputAction action : InputAction.values()) {
        if(CoreMod.KEYBIND_CONFIGS.containsKey(action)) {
          final ControllerKeybindConfigEntry keybind = CoreMod.KEYBIND_CONFIGS.get(action).get();
          if(keybind.required && this.config.getConfig(keybind).isEmpty()) {
            playMenuSound(4);
            this.getStack().pushScreen(new MessageBoxScreen(I18n.translate(CoreMod.MOD_ID + ".keybind.missing_input"), 0, result -> {}));
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
