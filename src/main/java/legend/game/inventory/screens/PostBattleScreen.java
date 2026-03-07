package legend.game.inventory.screens;

import it.unimi.dsi.fastutil.objects.Object2IntLinkedOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import legend.core.GameEngine;
import legend.core.MathHelper;
import legend.core.QueuedModelStandard;
import legend.core.gte.MV;
import legend.core.memory.Method;
import legend.core.memory.types.IntRef;
import legend.core.opengl.MeshObj;
import legend.core.opengl.QuadBuilder;
import legend.game.EngineState;
import legend.game.additions.Addition;
import legend.game.characters.CharacterData2c;
import legend.game.characters.LevelUpActions;
import legend.game.combat.types.EnemyDrop;
import legend.game.i18n.I18n;
import legend.game.inventory.SpellStats0c;
import legend.game.inventory.WhichMenu;
import legend.game.modding.coremod.CoreMod;
import legend.game.textures.TextureAtlasIcon;
import legend.game.types.Renderable58;
import legend.game.types.Translucency;
import legend.lodmod.characters.UnlockAdditionLevelUpAction;
import legend.lodmod.characters.UnlockAdditionLevelUpActionOptions;
import legend.lodmod.characters.UnlockSpellLevelUpAction;
import legend.lodmod.characters.UnlockSpellLevelUpActionOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static legend.core.GameEngine.CONFIG;
import static legend.core.GameEngine.PLATFORM;
import static legend.core.GameEngine.REGISTRIES;
import static legend.core.GameEngine.RENDERER;
import static legend.game.FullScreenEffects.fullScreenEffect_800bb140;
import static legend.game.FullScreenEffects.startFadeEffect;
import static legend.game.Graphics.renderMode;
import static legend.game.Graphics.resizeDisplay;
import static legend.game.Menus.allocateRenderable;
import static legend.game.Menus.clearRenderables;
import static legend.game.Menus.deallocateRenderables;
import static legend.game.Menus.uiFile_800bdc3c;
import static legend.game.Menus.uploadRenderables;
import static legend.game.Menus.whichMenu_800bdc38;
import static legend.game.SItem.cacheCharacterSlots;
import static legend.game.SItem.giveItems;
import static legend.game.SItem.menuStack;
import static legend.game.Scus94491BpeSegment_800b.gameState_800babc8;
import static legend.game.Scus94491BpeSegment_800b.goldGainedFromCombat_800bc920;
import static legend.game.Scus94491BpeSegment_800b.itemOverflow;
import static legend.game.Scus94491BpeSegment_800b.itemsDroppedByEnemies_800bc928;
import static legend.game.Scus94491BpeSegment_800b.livingChars_800bc968;
import static legend.game.Scus94491BpeSegment_800b.secondaryCharIds_800bdbf8;
import static legend.game.Scus94491BpeSegment_800b.spGained_800bc950;
import static legend.game.Scus94491BpeSegment_800b.totalXpFromCombat_800bc95c;
import static legend.game.Text.renderText;
import static legend.game.Text.textZ_800bdf00;
import static legend.game.combat.SBtld.FUN_80019470;
import static legend.game.combat.SBtld.addLevelUpOverlay;
import static legend.game.combat.SBtld.drawBattleReportOverlays;
import static legend.game.modding.coremod.CoreMod.INPUT_ACTION_MENU_BACK;
import static legend.game.modding.coremod.CoreMod.INPUT_ACTION_MENU_CONFIRM;
import static legend.game.sound.Audio.playMenuSound;

public class PostBattleScreen extends MenuScreen {
  private final FontOptions fontOptions = new FontOptions().colour(TextColour.WHITE);
  private final FontOptions nameFont = new FontOptions().colour(TextColour.WHITE).shadowColour(TextColour.BLACK).size(0.8f);

  private final Map<CharacterData2c, TextureAtlasIcon> portraits = new HashMap<>();
  private final MV transforms = new MV();

  private int unlockHeight_8011e178;
  private int soundTick_8011e17c;
  private final Object2IntMap<CharacterData2c> pendingXp_8011e180 = new Object2IntLinkedOpenHashMap<>();
  private final Object2IntMap<CharacterData2c> levelsGained_8011e1c8 = new Object2IntLinkedOpenHashMap<>();
  private final Object2IntMap<CharacterData2c> dragoonLevelsGained_8011e1d8 = new Object2IntLinkedOpenHashMap<>();
  private final Map<CharacterData2c, List<Addition>> additionsUnlocked_8011e1b8 = new LinkedHashMap<>();
  private final Map<CharacterData2c, List<SpellStats0c>> spellsUnlocked_8011e1a8 = new LinkedHashMap<>();

  private MenuState inventoryMenuState_800bdc28 = MenuState.INIT_0;
  private MenuState confirmDest_800bdc30;

  private final MeshObj[] resultsBackgroundObj = new MeshObj[6];
  private final MV resultsBackgroundTransforms = new MV();

  @Method(0x8010d614L)
  @Override
  protected void render() {
    switch(this.inventoryMenuState_800bdc28) {
      case INIT_0 -> {
        clearRenderables();
        resizeDisplay(320, 240);
        textZ_800bdf00 = 33;
        this.initResultBackgrounds();
        startFadeEffect(2, 10);
        this.inventoryMenuState_800bdc28 = MenuState.WAIT_FOR_FADE_IN_AND_INIT_CONTROLS_2;
      }

      case WAIT_FOR_FADE_IN_AND_INIT_CONTROLS_2 -> {
        if(fullScreenEffect_800bb140.currentColour_28 == 0) {
          deallocateRenderables(0xff);
          Renderable58 glyph = this.drawGlyph(0, 0, 165, 21, 720, 497);
          glyph.widthScale = 0;
          glyph.heightScale_38 = 0;
          glyph = this.drawGlyph(2, 2, 13, 21, 720, 497);
          glyph.widthScale = 0;
          glyph.heightScale_38 = 0;
          glyph = this.drawGlyph(1, 1, 13, 149, 720, 497);
          glyph.widthScale = 0;
          glyph.heightScale_38 = 0;

          this.drawGlyph(0x3e, 0x3e, 24, 28, 736, 497);
          this.drawGlyph(0x3d, 0x3d, 24, 40, 736, 497);
          this.drawGlyph(0x40, 0x40, 24, 52, 736, 497);

          cacheCharacterSlots();

          //LAB_8010d87c
          this.pendingXp_8011e180.clear();
          this.levelsGained_8011e1c8.clear();
          this.dragoonLevelsGained_8011e1d8.clear();
          this.additionsUnlocked_8011e1b8.clear();
          this.spellsUnlocked_8011e1a8.clear();

          this.portraits.clear();

          for(final CharacterData2c character : gameState_800babc8.charData_32c) {
            this.portraits.put(character, GameEngine.getTextureAtlas().getIcon(character.template.getRegistryId()));
          }

          int xpDivisor = 0;
          for(int charSlot = 0; charSlot < gameState_800babc8.charIds_88.size(); charSlot++) {
            if(livingChars_800bc968.contains(gameState_800babc8.getCharacterBySlot(charSlot))) {
              xpDivisor++;
            }
          }

          for(int charSlot = 0; charSlot < gameState_800babc8.charIds_88.size(); charSlot++) {
            if(livingChars_800bc968.contains(gameState_800babc8.getCharacterBySlot(charSlot))) {
              this.pendingXp_8011e180.put(gameState_800babc8.getCharacterBySlot(charSlot), totalXpFromCombat_800bc95c / xpDivisor);
            }
          }

          final float secondaryCharXpMultiplier = CONFIG.getConfig(CoreMod.SECONDARY_CHARACTER_XP_MULTIPLIER_CONFIG.get());

          //LAB_8010d9d4
          //LAB_8010d9f8
          for(int secondaryCharSlot = 0; secondaryCharSlot < secondaryCharIds_800bdbf8.size(); secondaryCharSlot++) {
            final int secondaryCharIndex = secondaryCharIds_800bdbf8.getInt(secondaryCharSlot);
            this.pendingXp_8011e180.put(gameState_800babc8.charData_32c.get(secondaryCharIndex), (int)(MathHelper.safeDiv(totalXpFromCombat_800bc95c, xpDivisor) * secondaryCharXpMultiplier));
          }

          this.inventoryMenuState_800bdc28 = MenuState.WAIT_FOR_FIRST_BUTTON_PRESS_3;
          this.drawGlyph(0x3f, 0x3f, 144, 28, 736, 497);
          this.drawGlyph(0x3f, 0x3f, 144, 156, 736, 497);
          this.drawReport();
        }
      }

      case WAIT_FOR_FIRST_BUTTON_PRESS_3 -> {
        if(PLATFORM.isActionPressed(INPUT_ACTION_MENU_CONFIRM.get())) {
          this.inventoryMenuState_800bdc28 = MenuState.CHECK_NEXT_THING_TO_GIVE;
        }

        this.drawReport();
      }

      case CHECK_NEXT_THING_TO_GIVE -> {
        this.checkForUnlocks();
        this.drawReport();

        this.additionsUnlocked_8011e1b8.values().removeIf(List::isEmpty);
        this.spellsUnlocked_8011e1a8.values().removeIf(List::isEmpty);

        if(goldGainedFromCombat_800bc920 != 0) {
          this.inventoryMenuState_800bdc28 = MenuState.TICK_GOLD_4;
          break;
        }

        if(!this.pendingXp_8011e180.isEmpty()) {
          this.inventoryMenuState_800bdc28 = MenuState.TICK_XP_5;
          break;
        }

        if(!this.levelsGained_8011e1c8.isEmpty()) {
          this.inventoryMenuState_800bdc28 = MenuState.SECONDARY_LEVEL_UP_8;
          break;
        }

        if(!spGained_800bc950.isEmpty()) {
          this.inventoryMenuState_800bdc28 = MenuState.DRAGOON_LEVEL_UPS_10;
          break;
        }

        if(!this.additionsUnlocked_8011e1b8.isEmpty()) {
          boolean foundPrimary = false;
          for(int i = 0; i < gameState_800babc8.charIds_88.size(); i++) {
            final CharacterData2c character = gameState_800babc8.getCharacterBySlot(i);

            if(this.additionsUnlocked_8011e1b8.containsKey(character)) {
              foundPrimary = true;
              break;
            }
          }

          if(foundPrimary) {
            this.inventoryMenuState_800bdc28 = MenuState.EMBIGGEN_UNLOCKED_ADDITIONS_6;
            this.unlockHeight_8011e178 = 0;
          } else {
            this.inventoryMenuState_800bdc28 = MenuState.SECONDARY_ADDITION;
            playMenuSound(9);
          }

          break;
        }

        if(!this.spellsUnlocked_8011e1a8.isEmpty()) {
          boolean foundPrimary = false;
          for(int i = 0; i < gameState_800babc8.charIds_88.size(); i++) {
            final CharacterData2c character = gameState_800babc8.getCharacterBySlot(i);

            if(this.spellsUnlocked_8011e1a8.containsKey(character)) {
              foundPrimary = true;
              break;
            }
          }

          if(foundPrimary) {
            this.inventoryMenuState_800bdc28 = MenuState.EMBIGGEN_SPELL_UNLOCK_12;
            this.unlockHeight_8011e178 = 0;
          } else {
            this.inventoryMenuState_800bdc28 = MenuState.SECONDARY_SPELL;
            playMenuSound(9);
          }

          break;
        }

        this.inventoryMenuState_800bdc28 = MenuState.WAIT_FOR_INPUT_14;
      }

      case TICK_GOLD_4 -> {
        final int goldTick;
        if(PLATFORM.isActionPressed(INPUT_ACTION_MENU_CONFIRM.get())) {
          goldTick = goldGainedFromCombat_800bc920;
        } else {
          //LAB_8010dab4
          goldTick = 10;
        }

        //LAB_8010dabc
        final int goldGained = goldGainedFromCombat_800bc920;

        if(goldTick >= goldGained) {
          this.soundTick_8011e17c = 0;
          goldGainedFromCombat_800bc920 = 0;
          this.inventoryMenuState_800bdc28 = MenuState.CHECK_NEXT_THING_TO_GIVE;
          gameState_800babc8.gold_94 += goldGained;
        } else {
          //LAB_8010db00
          goldGainedFromCombat_800bc920 -= goldTick;
          gameState_800babc8.gold_94 += goldTick;
        }

        //LAB_8010db18
        if(gameState_800babc8.gold_94 > 99999999) {
          gameState_800babc8.gold_94 = 99999999;
        }

        //LAB_8010db3c
        //LAB_8010db40
        this.soundTick_8011e17c++;

        if((this.soundTick_8011e17c & 0x1) != 0) {
          playMenuSound(1);
        }

        this.drawReport();
      }

      case TICK_XP_5 -> {
        final boolean moreXpToGive = this.givePendingXp();

        if(moreXpToGive) {
          this.soundTick_8011e17c++;

          if((this.soundTick_8011e17c & 0x1) != 0) {
            playMenuSound(1);
          }
        } else {
          totalXpFromCombat_800bc95c = 0;

          if(PLATFORM.isActionPressed(INPUT_ACTION_MENU_CONFIRM.get())) {
            playMenuSound(2);
            this.unlockHeight_8011e178 = 0;
            this.inventoryMenuState_800bdc28 = MenuState.CHECK_NEXT_THING_TO_GIVE;
          }
        }

        this.drawReport();
      }

      case SECONDARY_LEVEL_UP_8 -> {
        addLevelUpOverlay(-80, 44);
        playMenuSound(9);
        this.inventoryMenuState_800bdc28 = MenuState.CONFIRM_SECONDARY_LEVEL_UP_9;
        this.drawReport();
      }

      case CONFIRM_SECONDARY_LEVEL_UP_9 -> {
        final var it = this.levelsGained_8011e1c8.object2IntEntrySet().iterator();
        final var entry = it.next();
        final CharacterData2c character = entry.getKey();
        this.drawChar(24, 152, character);

        if(PLATFORM.isActionPressed(INPUT_ACTION_MENU_CONFIRM.get()) || PLATFORM.isActionPressed(INPUT_ACTION_MENU_BACK.get())) {
          playMenuSound(2);
          it.remove();
          this.inventoryMenuState_800bdc28 = MenuState.CHECK_NEXT_THING_TO_GIVE;
        }

        this.drawReport();
      }

      case DRAGOON_LEVEL_UPS_10 -> {
        final boolean moreXpToGive = this.givePendingDxp();

        if(moreXpToGive) {
          this.soundTick_8011e17c++;

          if((this.soundTick_8011e17c & 0x1) != 0) {
            playMenuSound(1);
          }
        } else if(PLATFORM.isActionPressed(INPUT_ACTION_MENU_CONFIRM.get())) {
          playMenuSound(2);
          this.unlockHeight_8011e178 = 0;
          this.inventoryMenuState_800bdc28 = MenuState.CHECK_NEXT_THING_TO_GIVE;
        }

        this.drawReport();
      }

      case EMBIGGEN_UNLOCKED_ADDITIONS_6 -> {
        if(this.unlockHeight_8011e178 < 20) {
          this.unlockHeight_8011e178 += 2;
        } else {
          //LAB_8010dcc8
          if(PLATFORM.isActionPressed(INPUT_ACTION_MENU_CONFIRM.get())) {
            playMenuSound(2);

            //LAB_8010dcf0
            this.inventoryMenuState_800bdc28 = MenuState.ENSMALLEN_UNLOCKED_ADDITIONS_7;
          }
        }

        //LAB_8010dcf4
        //LAB_8010dcf8
        this.renderAdditionsUnlocked(this.unlockHeight_8011e178);
        this.drawReport();
      }

      case ENSMALLEN_UNLOCKED_ADDITIONS_7 -> {
        this.renderAdditionsUnlocked(this.unlockHeight_8011e178);
        this.drawReport();

        if(this.unlockHeight_8011e178 > 0) {
          this.unlockHeight_8011e178 -= 2;
        } else {
          for(int i = 0; i < gameState_800babc8.charIds_88.size(); i++) {
            final CharacterData2c character = gameState_800babc8.getCharacterBySlot(i);
            final List<Addition> unlocks = this.additionsUnlocked_8011e1b8.get(character);

            if(unlocks != null) {
              unlocks.removeFirst();
            }
          }

          //LAB_8010dd28
          this.inventoryMenuState_800bdc28 = MenuState.CHECK_NEXT_THING_TO_GIVE;
        }
      }

      case SECONDARY_ADDITION -> {
        final var it = this.additionsUnlocked_8011e1b8.entrySet().iterator();
        final var entry = it.next();
        final CharacterData2c character = entry.getKey();
        final List<Addition> additions = entry.getValue();
        this.drawChar(24, 152, character);
        this.renderAdditionUnlocked(20, 169, additions.getFirst(), 20);

        if(PLATFORM.isActionPressed(INPUT_ACTION_MENU_CONFIRM.get()) || PLATFORM.isActionPressed(INPUT_ACTION_MENU_BACK.get())) {
          playMenuSound(2);
          additions.removeFirst();
          this.inventoryMenuState_800bdc28 = MenuState.CHECK_NEXT_THING_TO_GIVE;
        }

        this.drawReport();
      }

      case EMBIGGEN_SPELL_UNLOCK_12 -> {
        if(this.unlockHeight_8011e178 < 20) {
          this.unlockHeight_8011e178 += 2;
        } else {
          //LAB_8010def4
          if(PLATFORM.isActionPressed(INPUT_ACTION_MENU_CONFIRM.get())) {
            playMenuSound(2);

            //LAB_8010df1c
            this.inventoryMenuState_800bdc28 = MenuState.ENSMALLEN_SPELL_UNLOCK_13;
          }
        }

        //LAB_8010df20
        //LAB_8010df24
        this.renderSpellsUnlocked(this.unlockHeight_8011e178);
        this.drawReport();
      }

      case ENSMALLEN_SPELL_UNLOCK_13 -> {
        this.renderSpellsUnlocked(this.unlockHeight_8011e178);
        this.drawReport();

        if(this.unlockHeight_8011e178 > 0) {
          this.unlockHeight_8011e178 -= 2;
        } else {
          for(int i = 0; i < gameState_800babc8.charIds_88.size(); i++) {
            final CharacterData2c character = gameState_800babc8.getCharacterBySlot(i);
            final List<SpellStats0c> unlocks = this.spellsUnlocked_8011e1a8.get(character);

            if(unlocks != null) {
              unlocks.removeFirst();
            }
          }

          //LAB_8010df54
          //LAB_8010df1c
          this.inventoryMenuState_800bdc28 = MenuState.CHECK_NEXT_THING_TO_GIVE;
        }

        //LAB_8010df20
        //LAB_8010df24
      }

      case SECONDARY_SPELL -> {
        final var it = this.spellsUnlocked_8011e1a8.entrySet().iterator();
        final var entry = it.next();
        final CharacterData2c character = entry.getKey();
        final List<SpellStats0c> spells = entry.getValue();
        this.drawChar(24, 152, character);
        this.renderSpellUnlocked(20, 169, spells.getFirst(), 20);

        if(PLATFORM.isActionPressed(INPUT_ACTION_MENU_CONFIRM.get()) || PLATFORM.isActionPressed(INPUT_ACTION_MENU_BACK.get())) {
          playMenuSound(2);
          spells.removeFirst();
          this.inventoryMenuState_800bdc28 = MenuState.CHECK_NEXT_THING_TO_GIVE;
        }

        this.drawReport();
      }

      case WAIT_FOR_INPUT_14 -> {
        if(PLATFORM.isActionPressed(INPUT_ACTION_MENU_CONFIRM.get()) || PLATFORM.isActionPressed(INPUT_ACTION_MENU_BACK.get())) {
          playMenuSound(3);

          if((itemsDroppedByEnemies_800bc928.isEmpty() || giveItems(itemsDroppedByEnemies_800bc928) == 0) && itemOverflow.isEmpty()) {
            //LAB_8010dfac
            // No items remaining
            this.fadeToMenuState(MenuState.UNLOAD_18);
          } else {
            // Some items remaining
            resizeDisplay(384, 240);
            renderMode = EngineState.RenderMode.LEGACY;
            deallocateRenderables(0xff);
            menuStack.popScreen();
            menuStack.pushScreen(new TooManyItemsScreen());
          }
        }

        //LAB_8010dfb8
        //LAB_8010dfbc
        this.drawReport();
      }

      case FADE_OUT_16 -> {
        startFadeEffect(1, 10);
        this.inventoryMenuState_800bdc28 = MenuState.WAIT_FOR_FADE_OUT_17;
        this.drawReport();
      }

      case WAIT_FOR_FADE_OUT_17 -> {
        if(fullScreenEffect_800bb140.currentColour_28 >= 0xff) {
          this.inventoryMenuState_800bdc28 = this.confirmDest_800bdc30;
          FUN_80019470();
        }

        this.drawReport();
      }

      case UNLOAD_18 -> {
        whichMenu_800bdc38 = WhichMenu.UNLOAD_POST_COMBAT_REPORT_30;
        menuStack.popScreen();
        this.deleteResultsScreenObjects();
      }
    }

    //LAB_8010e09c
    //LAB_8010e0a0
    this.drawResultsBackground(166,  22, 136, 192, 1);
    this.drawResultsBackground( 14,  22, 144, 120, 1);
    this.drawResultsBackground( 14, 150, 144,  64, 1);
  }

  /**
   * @return True if there is remaining XP to give
   */
  @Method(0x8010cc24L)
  private boolean givePendingXp() {
    for(final var entry : this.pendingXp_8011e180.object2IntEntrySet()) {
      final CharacterData2c character = entry.getKey();
      final int pendingXp = entry.getIntValue();

      final int cappedPendingXp;
      if(PLATFORM.isActionPressed(INPUT_ACTION_MENU_CONFIRM.get()) || pendingXp < 10) {
        cappedPendingXp = pendingXp;
      } else {
        cappedPendingXp = 10;
      }

      //LAB_8010cc94
      //LAB_8010cc98
      //LAB_8010ccd4
      character.xp_00 += cappedPendingXp;
      this.pendingXp_8011e180.mergeInt(character, -cappedPendingXp, Integer::sum);

      if(this.pendingXp_8011e180.getInt(character) <= 0) {
        this.pendingXp_8011e180.removeInt(character);
      }

      //LAB_8010cd30
      final LevelUpActions levelUpActions = new LevelUpActions();
      while(character.xp_00 >= character.getXpToNextLevel()) {
        character.applyLevelUp(levelUpActions);
        this.levelsGained_8011e1c8.mergeInt(character, 1, Integer::sum);
      }

      this.applyLevelUpActions(character, levelUpActions);
    }

    //LAB_8010cdb0
    //LAB_8010cdcc
    return !this.pendingXp_8011e180.isEmpty();
  }

  private boolean givePendingDxp() {
    for(final var entry : spGained_800bc950.object2IntEntrySet()) {
      final CharacterData2c character = entry.getKey();
      final int pendingXp = entry.getIntValue();

      final int cappedPendingXp;
      if(PLATFORM.isActionPressed(INPUT_ACTION_MENU_CONFIRM.get()) || pendingXp < 10) {
        cappedPendingXp = pendingXp;
      } else {
        cappedPendingXp = 10;
      }

      //LAB_8010cc94
      //LAB_8010cc98
      //LAB_8010ccd4
      character.dlevelXp_0e += cappedPendingXp;
      spGained_800bc950.mergeInt(character, -cappedPendingXp, Integer::sum);

      if(spGained_800bc950.getInt(character) <= 0) {
        spGained_800bc950.removeInt(character);
      }

      //LAB_8010cd30
      final LevelUpActions levelUpActions = new LevelUpActions();
      while(character.dlevelXp_0e >= character.getDxpToNextLevel()) {
        character.applyDragoonLevelUp(levelUpActions);
        this.dragoonLevelsGained_8011e1d8.mergeInt(character, 1, Integer::sum);
      }

      this.applyLevelUpActions(character, levelUpActions);
    }

    //LAB_8010cdb0
    //LAB_8010cdcc
    return !spGained_800bc950.isEmpty();
  }

  private void checkForUnlocks() {
    for(int i = 0; i < gameState_800babc8.charData_32c.size(); i++) {
      final CharacterData2c character = gameState_800babc8.charData_32c.get(i);
      final LevelUpActions actions = new LevelUpActions();
      character.template.checkUnlocks(character, actions);
      this.applyLevelUpActions(character, actions);
    }
  }

  //TODO refactor this so it doesn't need to check instanceof
  private void applyLevelUpActions(final CharacterData2c character, final LevelUpActions levelUpActions) {
    final List<LevelUpActions.Entry<?>> results = levelUpActions.run(character);

    for(final LevelUpActions.Entry<?> entry : results) {
      if(entry.action instanceof final UnlockAdditionLevelUpAction unlockAddition) {
        final UnlockAdditionLevelUpActionOptions options = unlockAddition.cast(entry.options);
        this.additionsUnlocked_8011e1b8.computeIfAbsent(character, k -> new ArrayList<>()).add(REGISTRIES.additions.getEntry(options.additionId).get());
      }

      if(entry.action instanceof final UnlockSpellLevelUpAction unlockSpell) {
        final UnlockSpellLevelUpActionOptions options = unlockSpell.cast(entry.options);
        this.spellsUnlocked_8011e1a8.computeIfAbsent(character, k -> new ArrayList<>()).add(REGISTRIES.spells.getEntry(options.spellId).get());
      }
    }
  }

  @Method(0x8010cfa0L)
  private Renderable58 drawGlyph(final int startGlyph, final int endGlyph, final int x, final int y, final int u, final int v) {
    final Renderable58 renderable = allocateRenderable(uiFile_800bdc3c._d2d8(), null);
    renderable.glyph_04 = startGlyph;
    renderable.startGlyph_10 = startGlyph;

    if(startGlyph != endGlyph) {
      renderable.endGlyph_14 = endGlyph;
    } else {
      renderable.endGlyph_14 = endGlyph;
      renderable.flags_00 |= Renderable58.FLAG_NO_ANIMATION;
    }

    //LAB_8010d004
    renderable.x_40 = x;
    renderable.y_44 = y;
    renderable.clut_30 = v << 6 | (u & 0x3f0) >> 4;
    renderable.tpage_2c = 0x1b;
    renderable.useOriginalTpage = true;
    return renderable;
  }

  @Method(0x8010d050L)
  private void fadeToMenuState(final MenuState nextMenuState) {
    this.inventoryMenuState_800bdc28 = MenuState.FADE_OUT_16;
    this.confirmDest_800bdc30 = nextMenuState;
  }

  private void initResultBackgrounds() {
    this.resultsBackgroundObj[0] = new QuadBuilder("Results Screen Background")
      .size(1.0f, 1.0f)
      .translucency(Translucency.HALF_B_PLUS_HALF_F)
      .monochrome(0, 128.0f / 255.0f)
      .rgb(1, 0.0f, 20.0f / 255.0f, 80.0f / 255.0f)
      .rgb(2, 0.0f, 20.0f / 255.0f, 80.0f / 255.0f)
      .monochrome(3, 0.0f)
      .build();

    this.resultsBackgroundObj[1] = new QuadBuilder("Results Screen Portrait Shadow")
      .size(1.0f, 1.0f)
      .monochrome(0, 127.0f / 255.0f)
      .monochrome(2, 127.0f / 255.0f)
      .monochrome(1, 0.0f)
      .monochrome(3, 0.0f)
      .build();

    this.resultsBackgroundObj[2] = new QuadBuilder("Results Screen Addition Background")
      .size(1.0f, 1.0f)
      .rgb(0, 1.0f, 122.0f / 255.0f, 0.0f)
      .rgb(2, 1.0f, 122.0f / 255.0f, 0.0f)
      .rgb(1, 73.0f / 255.0f, 35.0f / 255.0f, 0.0f)
      .rgb(3, 73.0f / 255.0f, 35.0f / 255.0f, 0.0f)
      .build();

    this.resultsBackgroundObj[3] = new QuadBuilder("Results Screen Addition Border")
      .size(1.0f, 1.0f)
      .rgb(0, 1.0f, 122.0f / 255.0f, 0.0f)
      .rgb(1, 1.0f, 122.0f / 255.0f, 0.0f)
      .rgb(2, 1.0f, 122.0f / 255.0f, 0.0f)
      .rgb(3, 1.0f, 122.0f / 255.0f, 0.0f)
      .build();

    this.resultsBackgroundObj[4] = new QuadBuilder("Results Screen Spell Background")
      .size(1.0f, 1.0f)
      .rgb(0, 0.0f, 132.0f / 255.0f, 254.0f / 255.0f)
      .rgb(2, 0.0f, 132.0f / 255.0f, 254.0f / 255.0f)
      .rgb(1, 0.0f, 38.0f / 255.0f, 72.0f / 255.0f)
      .rgb(3, 0.0f, 38.0f / 255.0f, 72.0f / 255.0f)
      .build();

    this.resultsBackgroundObj[5] = new QuadBuilder("Results Screen Spell Border")
      .size(1.0f, 1.0f)
      .monochrome(0, 127.0f / 255.0f)
      .monochrome(2, 127.0f / 255.0f)
      .monochrome(1, 0.0f)
      .monochrome(3, 0.0f)
      .build();
  }

  @Method(0x8010d078L)
  private void drawResultsBackground(int x, final int y, final int w, final int h, final int type) {
    if(this.resultsBackgroundObj[type - 1] != null) {
      x -= 8;
      //y += 120;

      final int z;
      switch(type) {
        case 1 -> //Background gradient
          z = 38;

        case 2 -> //Character portrait shadow
          z = 37;

        case 3 -> //Addition background
          z = 34;

        case 4 -> //Addition border
          z = 35;

        case 5 -> //Spell background
          z = 34;

        case 6 -> //Spell border
          z = 35;

        default -> z = 0;
      }

      //LAB_8010d2c4
      this.resultsBackgroundTransforms.transfer.set(x, y, z * 4);
      this.resultsBackgroundTransforms.scaling(w, h, 1);

      RENDERER.queueOrthoModel(this.resultsBackgroundObj[type - 1], this.resultsBackgroundTransforms, QueuedModelStandard.class);

      //LAB_8010d318
    }
  }

  @Method(0x8010e114L)
  private void drawCharPortrait(final int x, final int y, final CharacterData2c character) {
    this.transforms.transfer.set(x - 7.0f, y - 1.0f, 144.0f);
    this.transforms.scaling(30.0f, 36.0f, 1.0f);
    this.portraits.get(character).render(this.transforms)
      .scissor(x - 6, y + 1, 24, 32);
  }

  @Method(0x8010e200L)
  private void drawDigit(final int x, final int y, int val, final IntRef a3) {
    val %= 10;
    if(val != 0 || a3.get() != 0) {
      //LAB_8010e254
      final Renderable58 renderable = this.drawGlyph(val + 3, val + 3, x, y, 736, 497);
      renderable.flags_00 |= Renderable58.FLAG_DELETE_AFTER_RENDER;
      a3.set(1);
    }

    //LAB_8010e290
  }

  @Method(0x8010e2a0L)
  private void drawTwoDigitNumber(final int x, final int y, final int dlevel) {
    final int s2 = Math.min(99, dlevel);
    final IntRef sp0x10 = new IntRef();
    this.drawDigit(x, y, s2 / 10, sp0x10.set(0));
    this.drawDigit(x + 6, y, s2, sp0x10.incr());
  }

  @Method(0x8010e340L)
  private void drawSixDigitNumber(final int x, final int y, final int val) {
    final int s2 = Math.min(999_999, val);
    final IntRef sp0x10 = new IntRef();
    this.drawDigit(x, y, s2 / 100_000, sp0x10);
    this.drawDigit(x +  6, y, s2 / 10_000, sp0x10);
    this.drawDigit(x + 12, y, s2 /  1_000, sp0x10);
    this.drawDigit(x + 18, y, s2 /    100, sp0x10);
    this.drawDigit(x + 24, y, s2 /     10, sp0x10);
    this.drawDigit(x + 30, y, s2, sp0x10.incr());
  }

  @Method(0x8010e490L)
  private void drawEightDigitNumber(final int x, final int y, final int val) {
    final int s2 = Math.min(99_999_999, val);
    final IntRef sp0x10 = new IntRef();
    this.drawDigit(x, y, s2 / 10_000_000, sp0x10);
    this.drawDigit(x +  6, y, s2 / 1_000_000, sp0x10);
    this.drawDigit(x + 12, y, s2 /   100_000, sp0x10);
    this.drawDigit(x + 18, y, s2 /    10_000, sp0x10);
    this.drawDigit(x + 24, y, s2 /     1_000, sp0x10);
    this.drawDigit(x + 30, y, s2 /       100, sp0x10);
    this.drawDigit(x + 36, y, s2 /        10, sp0x10);
    this.drawDigit(x + 42, y, s2, sp0x10.incr());
  }

  @Method(0x8010e630L)
  private void drawNextLevelXp(final int x, final int y, final int val) {
    if(val != 0) {
      this.drawSixDigitNumber(x, y, val);
    } else {
      //LAB_8010e660
      final Renderable58 renderable = this.drawGlyph(0x47, 0x47, x + 30, y, 736, 497);
      renderable.flags_00 |= Renderable58.FLAG_DELETE_AFTER_RENDER;
    }

    //LAB_8010e698
  }

  @Method(0x8010e6a8L)
  private int getXpWidth(final int xp) {
    if(xp > 99999) {
      return 36;
    }

    //LAB_8010e6c4
    if(xp > 9999) {
      return 30;
    }

    //LAB_8010e6d4
    if(xp > 999) {
      return 24;
    }

    //LAB_8010e6e4
    if(xp > 99) {
      //LAB_8010e6fc
      return 18;
    }

    if(xp > 9) {
      //LAB_8010e700
      return 12;
    }

    return 6;
  }

  @Method(0x8010e708L)
  private void drawChar(final int x, final int y, final CharacterData2c character) {
    this.drawResultsBackground(x + 1, y + 5, 24, 32, 2);
    this.drawCharPortrait(x - 1, y + 4, character);
    renderText(character.getName(), x + 22, y + 4, this.nameFont);
    this.drawGlyph(0x3b, 0x3b, x + 30, y + 16, 736, 497).flags_00 |= Renderable58.FLAG_DELETE_AFTER_RENDER;
    this.drawGlyph(0x3c, 0x3c, x + 30, y + 28, 736, 497).flags_00 |= Renderable58.FLAG_DELETE_AFTER_RENDER;
    this.drawGlyph(0x3d, 0x3d, x, y + 40, 736, 497).flags_00 |= Renderable58.FLAG_DELETE_AFTER_RENDER;

    final Renderable58 glyph = this.drawGlyph(0x3c, 0x3c, x, y + 52, 736, 497);
    glyph.flags_00 |= Renderable58.FLAG_DELETE_AFTER_RENDER;
    glyph.widthCut = 16;
    this.drawGlyph(0x3d, 0x3d, x + 10, y + 52, 736, 497).flags_00 |= Renderable58.FLAG_DELETE_AFTER_RENDER;

    this.drawTwoDigitNumber(x + 108, y + 16, character.level_12);

    final int dlevel;
    if(!character.hasDragoon()) {
      dlevel = 0;
    } else {
      dlevel = character.dlevel_13;
    }

    //LAB_8010e8e0
    this.drawTwoDigitNumber(x + 108, y + 28, dlevel);
    final int xp = character.getXpToNextLevel();
    this.drawSixDigitNumber(x + 76 - this.getXpWidth(xp), y + 40, character.xp_00);
    this.drawGlyph(0x22, 0x22, x - (this.getXpWidth(xp) - 114), y + 40, 736, 497).flags_00 |= Renderable58.FLAG_DELETE_AFTER_RENDER;
    this.drawNextLevelXp(x + 84, y + 40, xp);

    final int dxp = character.getDxpToNextLevel();
    this.drawSixDigitNumber(x + 76 - this.getXpWidth(dxp), y + 52, character.dlevelXp_0e);
    this.drawGlyph(0x22, 0x22, x - (this.getXpWidth(dxp) - 114), y + 52, 736, 497).flags_00 |= Renderable58.FLAG_DELETE_AFTER_RENDER;
    this.drawNextLevelXp(x + 84, y + 52, dxp);
  }

  @Method(0x8010e9a8L)
  private void drawReport() {
    int y1 = 24;
    int y2 = -82;
    int y3 = -70;

    //LAB_8010e9fc
    for(int i = 0; i < gameState_800babc8.charIds_88.size(); i++) {
      final CharacterData2c character = gameState_800babc8.getCharacterBySlot(i);

      this.drawChar(176, y1, character);

      if(this.levelsGained_8011e1c8.containsKey(character)) {
        this.levelsGained_8011e1c8.removeInt(character);
        addLevelUpOverlay(72, y2);
        playMenuSound(9);
      }

      //LAB_8010ea44
      if(this.dragoonLevelsGained_8011e1d8.containsKey(character)) {
        this.dragoonLevelsGained_8011e1d8.removeInt(character);
        addLevelUpOverlay(72, y3);
        playMenuSound(9);
      }

      //LAB_8010ea70
      y1 += 64;
      y2 += 64;
      y3 += 64;
    }

    this.drawEightDigitNumber( 96, 28, goldGainedFromCombat_800bc920);
    this.drawSixDigitNumber(108, 40, totalXpFromCombat_800bc95c);

    y1 = 63;
    y2 = 64;

    this.fontOptions.shadowColour(TextColour.BLACK);

    //LAB_8010eae0
    for(final EnemyDrop enemyDrop : itemsDroppedByEnemies_800bc928) {
      enemyDrop.item.renderIcon(18, y1, 0x8);
      renderText(I18n.translate(enemyDrop.item.getNameTranslationKey()), 28, y2, this.fontOptions);

      //LAB_8010eb38
      y2 += 16;
      y1 += 16;
    }

    //LAB_8010eb58
    this.drawEightDigitNumber(96, 156, gameState_800babc8.gold_94);

    //LAB_8010ebb0
    uploadRenderables();
    drawBattleReportOverlays();
  }

  @Method(0x8010ebecL)
  private void renderAdditionsUnlocked(final int height) {
    for(int i = 0; i < gameState_800babc8.charIds_88.size(); i++) {
      final CharacterData2c character = gameState_800babc8.getCharacterBySlot(i);

      if(this.additionsUnlocked_8011e1b8.containsKey(character)) {
        this.renderAdditionUnlocked(168, 40 + i * 64, this.additionsUnlocked_8011e1b8.get(character).getFirst(), height);
      }
    }
  }

  @Method(0x8010ec6cL)
  private void renderSpellsUnlocked(final int height) {
    //LAB_8010ec98
    for(int i = 0; i < gameState_800babc8.charIds_88.size(); i++) {
      final CharacterData2c character = gameState_800babc8.getCharacterBySlot(i);

      if(this.spellsUnlocked_8011e1a8.containsKey(character)) {
        this.renderSpellUnlocked(168, 40 + i * 64, this.spellsUnlocked_8011e1a8.get(character).getFirst(), height);
      }

      //LAB_8010ecc0
    }
  }

  @Method(0x8010d398L)
  private void renderAdditionUnlocked(final int x, final int y, final Addition addition, final int height) {
    this.drawResultsBackground(x, y + 20 - height, 134, (height + 1) * 2, 4);
    this.drawResultsBackground(x + 1, y + 20 - height + 1, 132, height * 2, 3);

    if(height >= 20) {
      this.fontOptions.noShadow();
      renderText(I18n.translate(addition), x - 4, y + 6, this.fontOptions);
      renderText(I18n.translate("lod_core.ui.post_battle.addition_unlocked"), x - 4, y + 20, this.fontOptions);
    }

    //LAB_8010d470
  }

  @Method(0x8010d498L)
  private void renderSpellUnlocked(final int x, final int y, final SpellStats0c spell, final int height) {
    this.drawResultsBackground(x, y + 20 - height, 134, (height + 1) * 2, 6); // New spell border
    this.drawResultsBackground(x + 1, y + 20 - height + 1, 132, height * 2, 5); // New spell background

    if(height >= 20) {
      renderText(I18n.translate(spell), x - 4, y + 6, this.fontOptions);
      renderText(I18n.translate("lod_core.ui.post_battle.spell_unlocked"), x - 4, y + 20, this.fontOptions);
    }

    //LAB_8010d470
  }

  private void deleteResultsScreenObjects() {
    for(int i = 0; i < this.resultsBackgroundObj.length; i++) {
      if(this.resultsBackgroundObj[i] != null) {
        this.resultsBackgroundObj[i].delete();
        this.resultsBackgroundObj[i] = null;
      }
    }
  }

  private enum MenuState {
    INIT_0,
    WAIT_FOR_UI_FILE_TO_LOAD_1,
    WAIT_FOR_FADE_IN_AND_INIT_CONTROLS_2,
    WAIT_FOR_FIRST_BUTTON_PRESS_3,
    CHECK_NEXT_THING_TO_GIVE,
    TICK_GOLD_4,
    TICK_XP_5,
    EMBIGGEN_UNLOCKED_ADDITIONS_6,
    ENSMALLEN_UNLOCKED_ADDITIONS_7,
    SECONDARY_ADDITION,
    SECONDARY_LEVEL_UP_8,
    CONFIRM_SECONDARY_LEVEL_UP_9,
    DRAGOON_LEVEL_UPS_10,
    EMBIGGEN_SPELL_UNLOCK_12,
    ENSMALLEN_SPELL_UNLOCK_13,
    SECONDARY_SPELL,
    WAIT_FOR_INPUT_14,
    _15,
    FADE_OUT_16,
    WAIT_FOR_FADE_OUT_17,
    UNLOAD_18,
  }
}
