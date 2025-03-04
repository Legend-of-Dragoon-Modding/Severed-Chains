package legend.game.inventory.screens;

import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import it.unimi.dsi.fastutil.ints.IntSet;
import legend.core.platform.input.InputAction;
import legend.core.platform.input.InputKey;
import legend.game.i18n.I18n;
import legend.game.input.InputActionOld;
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
import static legend.game.modding.coremod.CoreMod.INPUT_ACTION_MENU_BACK;
import static org.lwjgl.sdl.SDLKeycode.SDLK_BACKSPACE;
import static org.lwjgl.sdl.SDLKeycode.SDLK_DOWN;
import static org.lwjgl.sdl.SDLKeycode.SDLK_END;
import static org.lwjgl.sdl.SDLKeycode.SDLK_ESCAPE;
import static org.lwjgl.sdl.SDLKeycode.SDLK_EXCLAIM;
import static org.lwjgl.sdl.SDLKeycode.SDLK_F1;
import static org.lwjgl.sdl.SDLKeycode.SDLK_HOME;
import static org.lwjgl.sdl.SDLKeycode.SDLK_INSERT;
import static org.lwjgl.sdl.SDLKeycode.SDLK_LEFT;
import static org.lwjgl.sdl.SDLKeycode.SDLK_PAGEDOWN;
import static org.lwjgl.sdl.SDLKeycode.SDLK_PAGEUP;
import static org.lwjgl.sdl.SDLKeycode.SDLK_RETURN;
import static org.lwjgl.sdl.SDLKeycode.SDLK_RIGHT;
import static org.lwjgl.sdl.SDLKeycode.SDLK_SPACE;
import static org.lwjgl.sdl.SDLKeycode.SDLK_TAB;
import static org.lwjgl.sdl.SDLKeycode.SDLK_TILDE;
import static org.lwjgl.sdl.SDLKeycode.SDLK_UP;
import static org.lwjgl.sdl.SDLKeycode.SDL_KMOD_ALT;
import static org.lwjgl.sdl.SDLKeycode.SDL_KMOD_CTRL;
import static org.lwjgl.sdl.SDLKeycode.SDL_KMOD_SHIFT;

public class KeybindsScreen extends VerticalLayoutScreen {
  private final Runnable unload;
  private final ConfigCollection config;

  private final Map<Integer, String> validKeys = new LinkedHashMap<>();
  private final Map<Integer, String> validMods = new LinkedHashMap<>();

  public KeybindsScreen(final ConfigCollection config, final Runnable unload) {
    this.addKey(SDLK_SPACE, "SPACE");
    this.addRegularKeyRange(SDLK_EXCLAIM, SDLK_TILDE);
    this.addKey(SDLK_ESCAPE, "ESC");
    this.addKey(SDLK_RETURN, "ENTER");
    this.addKey(SDLK_TAB, "TAB");
    this.addKey(SDLK_BACKSPACE, "BACKSPACE");
    this.addKey(SDLK_INSERT, "INSERT");
    this.addKey(SDLK_RIGHT, "RIGHT");
    this.addKey(SDLK_LEFT, "LEFT");
    this.addKey(SDLK_DOWN, "DOWN");
    this.addKey(SDLK_UP, "UP");
    this.addKey(SDLK_PAGEUP, "PGUP");
    this.addKey(SDLK_PAGEDOWN, "PGDN");
    this.addKey(SDLK_HOME, "HOME");
    this.addKey(SDLK_END, "END");

    // Modifiers
    this.addMod(SDL_KMOD_ALT, "ALT");
    this.addMod(SDL_KMOD_CTRL, "CTRL");
    this.addMod(SDL_KMOD_SHIFT, "SHFT");

    for(int i = 0; i < 12; i++) {
      this.addKey(SDLK_F1 + i, "F" + (i + 1));
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

    for(final InputActionOld inputAction : InputActionOld.values()) {
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

          for(final InputActionOld action : InputActionOld.values()) {
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

        textbox.onKeyPress((key, scancode, mods, repeat) -> {
          if(key == InputKey.DELETE) {
            textbox.unfocus();
            return InputPropagation.HANDLED;
          }

          if(this.validKeys.containsKey(key)) {
/*TODO
            final int addedKey = key | (this.areModsValid(mods) ? mods << 9 : 0);
            keycodes.add(addedKey);
            textbox.setText(this.keysToString(keycodes));
*/
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
  protected InputPropagation inputActionPressed(final InputAction action, final boolean repeat) {
    if(super.inputActionPressed(action, repeat) == InputPropagation.HANDLED) {
      return InputPropagation.HANDLED;
    }

    if(action == INPUT_ACTION_MENU_BACK.get()) {
      for(final InputActionOld actionOld : InputActionOld.values()) {
        if(CoreMod.KEYBIND_CONFIGS.containsKey(actionOld)) {
          final ControllerKeybindConfigEntry keybind = CoreMod.KEYBIND_CONFIGS.get(actionOld).get();
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
