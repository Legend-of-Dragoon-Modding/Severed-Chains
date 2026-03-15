package legend.game.inventory.screens;

import discord.DiscordRichPresence;
import legend.core.QueuedModelStandard;
import legend.core.gpu.Bpp;
import legend.core.gpu.VramTexture;
import legend.core.gpu.VramTextureSingle;
import legend.core.gte.MV;
import legend.core.opengl.Obj;
import legend.core.opengl.QuadBuilder;
import legend.core.opengl.Texture;
import legend.core.platform.input.InputAction;
import legend.core.platform.input.InputMod;
import legend.game.credits.Credits.CreditsType;
import legend.game.i18n.I18n;
import legend.game.tim.Tim;
import legend.game.types.Translucency;
import legend.game.unpacker.FileData;
import legend.game.unpacker.Loader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static legend.core.GameEngine.DEFAULT_FONT;
import static legend.core.GameEngine.RENDERER;
import static legend.core.gpu.VramTextureLoader.palettesFromTim;
import static legend.core.gpu.VramTextureLoader.stitchVertical;
import static legend.core.gpu.VramTextureLoader.textureFromTim;
import static legend.game.FullScreenEffects.startFadeEffect;
import static legend.game.Menus.deallocateRenderables;
import static legend.game.Text.renderText;
import static legend.game.modding.coremod.CoreMod.INPUT_ACTION_MENU_BACK;
import static legend.game.modding.coremod.CoreMod.INPUT_ACTION_MENU_DOWN;
import static legend.game.modding.coremod.CoreMod.INPUT_ACTION_MENU_END;
import static legend.game.modding.coremod.CoreMod.INPUT_ACTION_MENU_HOME;
import static legend.game.modding.coremod.CoreMod.INPUT_ACTION_MENU_PAGE_DOWN;
import static legend.game.modding.coremod.CoreMod.INPUT_ACTION_MENU_PAGE_UP;
import static legend.game.modding.coremod.CoreMod.INPUT_ACTION_MENU_UP;
import static legend.game.sound.Audio.playMenuSound;

public class CreditsScreen extends MenuScreen {
  public static class CreditEntry {
    public CreditsType type;
    public CreditFontProperties properties;
    public String text;
    public float y;
  }

  public static class CreditFontProperties {
    public FontOptions font;
    public float paddingTop;
    public float paddingBottom;

    public CreditFontProperties(final FontOptions font, final float paddingTop, final float paddingBottom) {
      this.font = font;
      this.paddingTop = paddingTop;
      this.paddingBottom = paddingBottom;
    }
  }

  private static final Logger LOGGER = LogManager.getFormatterLogger(DiscordRichPresence.class);

  private int loadingStage;
  private final Runnable unload;

  private final List<CreditEntry> credits;
  private final Map<CreditsType, CreditFontProperties> fonts;
  private final FontOptions font;
  private float scrollSpeed = 1;
  private float scrollValue;
  private float pauseTime;
  private float backgroundOpacity;
  private boolean scrolling;

  private VramTexture backgroundTexture;
  private VramTexture[] backgroundPalettes;
  private Texture backgroundTex;
  private Texture fadeTex;
  private Obj backgroundObj;
  private final MV transforms = new MV();

  public CreditsScreen(final Runnable unload) {
    this.unload = unload;
    this.fonts = new EnumMap<>(CreditsType.class);
    this.credits = new ArrayList<>();
    this.font = new FontOptions();
    this.scrolling = true;

    this.setFonts();
    this.loadCredits();
    this.setCredits();
  }

  private void setFonts() {
    this.fonts.clear();
    this.fonts.put(CreditsType.DIRECTOR_3, new CreditFontProperties(new FontOptions().colour(TextColour.GOLD).shadowColour(TextColour.DARK_GREY).size(1.4f).horizontalAlign(HorizontalAlign.CENTRE), 25, 8));
    this.fonts.put(CreditsType.MAJOR_HEADER_0, new CreditFontProperties(new FontOptions().colour(TextColour.RED).shadowColour(TextColour.DARK_GREY).size(1.2f).horizontalAlign(HorizontalAlign.CENTRE), 25, 8));
    this.fonts.put(CreditsType.MINOR_HEADER_1, new CreditFontProperties(new FontOptions().colour(TextColour.CYAN).shadowColour(TextColour.DARK_GREY).size(1.4f).horizontalAlign(HorizontalAlign.CENTRE), 25, 8));
    this.fonts.put(CreditsType.NAME_2, new CreditFontProperties(new FontOptions().colour(TextColour.WHITE).size(1).horizontalAlign(HorizontalAlign.CENTRE), 0, 2));
    this.fonts.put(CreditsType.LINK_5, new CreditFontProperties(new FontOptions().colour(TextColour.GREY).size(0.8f).horizontalAlign(HorizontalAlign.CENTRE), 0, 0));
  }

  private void loadCredits() {
    this.credits.clear();
    final Path path = Path.of("credits.txt");
    try(final BufferedReader br = new BufferedReader(new FileReader(String.valueOf(path)))) {
      for(String line; (line = br.readLine()) != null; ) {
        line = line.trim();
        this.addEntry(line);
      }
    } catch(final IOException ex) {
      LOGGER.error("Failed to load credits.txt", ex);
    }
  }

  private void addEntry(final String line) {
    final CreditEntry entry = new CreditEntry();
    if(line.startsWith("### ")) {
      entry.type = CreditsType.MINOR_HEADER_1;
      entry.text = line.substring(4);
    } else if(line.startsWith("## ")) {
      entry.type = CreditsType.MAJOR_HEADER_0;
      entry.text = line.substring(3);
    } else if(line.startsWith("# ")) {
      entry.type = CreditsType.DIRECTOR_3;
      entry.text = line.substring(2);
    } else if(line.startsWith("@ ")) {
      entry.type = CreditsType.LINK_5;
      entry.text = line.substring(2);
    } else {
      entry.type = CreditsType.NAME_2;
      entry.text = line;
    }
    entry.text = I18n.translate(entry.text);
    entry.properties = this.fonts.get(entry.type);
    this.credits.add(entry);
  }

  private void setCredits() {
    float y = 0;
    boolean wasLink = false;
    for(final CreditEntry entry : this.credits) {
      final float textHeight = DEFAULT_FONT.textHeight(entry.text) * entry.properties.font.getSize();
      final float extraHeight = wasLink && entry.type == CreditsType.NAME_2 ? 10 : 0;
      entry.y = y + entry.properties.paddingTop + extraHeight;
      y += textHeight + entry.properties.paddingBottom + entry.properties.paddingTop + extraHeight;
      wasLink = entry.type == CreditsType.LINK_5;
    }
  }

  private void renderCredits() {
    final float renderWidthCenter = RENDERER.getNativeWidth() * 0.5f;
    final float renderHeight = RENDERER.getNativeHeight();

    for(final CreditEntry entry : this.credits) {
      if(entry.y > this.scrollValue - 50 - renderHeight && entry.y < this.scrollValue + 50 + renderHeight) {
        final float y = entry.y - this.scrollValue + renderHeight;

        this.font.set(entry.properties.font).size(entry.properties.font.getSize());

        if(y < 80) {
          final float colourRatio = Math.min(1, (y + 20) / 80);
          this.font.colour(this.font.getRed() * colourRatio, this.font.getGreen() * colourRatio, this.font.getBlue() * colourRatio);
          if(this.font.hasShadow()) {
            this.font.shadowColour(this.font.getShadowRed() * colourRatio, this.font.getShadowGreen() * colourRatio, this.font.getShadowBlue() * colourRatio);
          }
        } else if(y > renderHeight - 100) {
          final float colourRatio = (renderHeight - y) / 100;
          this.font.colour(this.font.getRed() * colourRatio, this.font.getGreen() * colourRatio, this.font.getBlue() * colourRatio);
          if(this.font.hasShadow()) {
            this.font.shadowColour(this.font.getShadowRed() * colourRatio, this.font.getShadowGreen() * colourRatio, this.font.getShadowBlue() * colourRatio);
          }
        }

        renderText(entry.text, renderWidthCenter, y, this.font);
      }
    }

    if(this.scrolling && this.scrollValue > this.credits.getLast().y + 50 + renderHeight) {
      this.loadingStage = 100;
    }
  }

  private void renderBackground() {
    this.transforms.transfer.set(0.0f, 0.0f, 1001.0f);
    this.transforms.scaling(368.0f, 424.0f, 1.0f);

    RENDERER
      .queueOrthoModel(this.backgroundObj, this.transforms, QueuedModelStandard.class)
      .texture(this.backgroundTex)
    ;

    this.transforms.transfer.set(0.0f, 0.0f, 1000.0f);
    this.transforms.scaling(368.0f, 240.0f, 1.0f);

    RENDERER
      .queueOrthoModel(this.backgroundObj, this.transforms, QueuedModelStandard.class)
      .texture(this.fadeTex)
      .useTextureAlpha()
      .translucency(Translucency.HALF_B_PLUS_HALF_F)
    ;
  }

  @Override
  protected void render() {
    switch(this.loadingStage) {
      // Load TIMs
      case 0 -> {
        this.loadingStage++;
        Loader.loadFiles(this::menuTexturesMrgLoaded, "SECT/DRGN0.BIN/5718/0", "SECT/DRGN0.BIN/5718/1");
      }

      // State 1 is waiting for the textures to load

      case 2 -> {
        this.initBackground();
        this.renderBackground();
        startFadeEffect(2, 10);
        deallocateRenderables(0xff);
        this.loadingStage++;
      }

      case 3 -> {
        this.renderBackground();
        deallocateRenderables(0);
        this.loadingStage++;
      }

      case 4 -> {
        this.renderBackground();
        this.renderCredits();

        if(this.scrolling) {
          this.scrollValue = Math.max(0, this.scrollValue + this.scrollSpeed);
        } else {
          this.pauseTime++;
          if(this.pauseTime > 50) {
            this.pauseTime = 0;
            this.scrolling = true;
          }
        }

        if(this.backgroundOpacity < 0.3f) {
          this.backgroundOpacity += 0.0035f;
        }
      }

      // Fade out
      case 100 -> {
        this.renderBackground();
        this.renderCredits();

        this.backgroundObj.delete();
        this.backgroundTex.delete();
        this.fadeTex.delete();

        this.unload.run();
      }
    }
  }

  private void menuTexturesMrgLoaded(final List<FileData> files) {
    this.backgroundTexture = stitchVertical(
      textureFromTim(new Tim(files.get(0))),
      textureFromTim(new Tim(files.get(1)))
    );

    this.backgroundPalettes = palettesFromTim(new Tim(files.get(0)));

    this.loadingStage++;
  }

  private void initBackground() {
    this.backgroundTex = ((VramTextureSingle)this.backgroundTexture).createOpenglTexture((VramTextureSingle)this.backgroundPalettes[0]);
    this.fadeTex = Texture.png(Path.of("gfx/ui/credits_fade.png"));

    this.backgroundObj = new QuadBuilder("Title Screen Background")
      .pos(0.0f, 0.0f, 0.0f)
      .size(1.0f, 1.0f)
      .bpp(Bpp.BITS_24)
      .build()
    ;

    this.backgroundTexture = null;
    this.backgroundPalettes = null;
  }

  @Override
  protected InputPropagation mouseClick(final double x, final double y, final int button, final Set<InputMod> mods) {
    if(super.mouseClick(x, y, button, mods) == InputPropagation.HANDLED) {
      return InputPropagation.HANDLED;
    }

    return InputPropagation.PROPAGATE;
  }

  private void menuEscape() {
    playMenuSound(3);
    this.loadingStage = 100;
  }

  private void menuNavigateUp(final int amount) {
    if(amount < 1) {
      this.scrolling = true;
      this.scrollValue = 0;
    } else {
      this.scrolling = false;
      this.scrollValue -= amount;
    }
    this.scrollValue = Math.max(0, this.scrollValue);
  }

  private void menuNavigateDown(final int amount) {
    if(amount >= 9999999) {
      this.scrolling = true;
      this.scrollValue = this.credits.getLast().y + 50;
    } else {
      this.scrolling = false;
      this.scrollValue += amount;
    }
    this.scrollValue = Math.max(0, this.scrollValue);
  }

  @Override
  public InputPropagation inputActionPressed(final InputAction action, final boolean repeat) {
    if(super.inputActionPressed(action, repeat) == InputPropagation.HANDLED) {
      return InputPropagation.HANDLED;
    }

    if(this.loadingStage != 4) {
      return InputPropagation.PROPAGATE;
    }

    if(action == INPUT_ACTION_MENU_BACK.get() && !repeat) {
      this.menuEscape();
      return InputPropagation.HANDLED;
    }

    if(action == INPUT_ACTION_MENU_PAGE_UP.get()) {
      this.menuNavigateUp(50);
      return InputPropagation.HANDLED;
    }

    if(action == INPUT_ACTION_MENU_PAGE_DOWN.get()) {
      this.menuNavigateDown(50);
      return InputPropagation.HANDLED;
    }

    if(action == INPUT_ACTION_MENU_DOWN.get()) {
      this.scrollSpeed = -2;
      return InputPropagation.HANDLED;
    }

    if(action == INPUT_ACTION_MENU_UP.get()) {
      this.scrollSpeed = 2;
      return InputPropagation.HANDLED;
    }

    if(action == INPUT_ACTION_MENU_HOME.get()) {
      this.setFonts();
      this.loadCredits();
      this.setCredits();
      this.menuNavigateUp(0);
      return InputPropagation.HANDLED;
    }

    if(action == INPUT_ACTION_MENU_END.get()) {
      this.menuNavigateDown(9999999);
      return InputPropagation.HANDLED;
    }

    return InputPropagation.PROPAGATE;
  }

  @Override
  public InputPropagation inputActionReleased(final InputAction action) {
    if(super.inputActionReleased(action) == InputPropagation.HANDLED) {
      return InputPropagation.HANDLED;
    }

    if(action == INPUT_ACTION_MENU_DOWN.get() || action == INPUT_ACTION_MENU_UP.get()) {
      this.scrollSpeed = 1;
      return InputPropagation.HANDLED;
    }

    return InputPropagation.PROPAGATE;
  }
}
