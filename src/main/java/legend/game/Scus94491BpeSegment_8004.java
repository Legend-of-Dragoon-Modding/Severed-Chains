package legend.game;

import legend.core.renderer.RenderEngine;
import legend.game.additions.Addition;
import legend.game.scripting.FlowControl;
import legend.game.scripting.RunningScript;
import legend.game.scripting.ScriptFile;
import legend.game.sound.Audio;
import legend.game.types.BattleReportOverlayList10;
import org.legendofdragoon.modloader.registries.RegistryDelegate;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Function;

import static legend.lodmod.LodAdditions.ALBERT_FLOWER_STORM;
import static legend.lodmod.LodAdditions.ALBERT_GUST_OF_WIND_DANCE;
import static legend.lodmod.LodAdditions.ALBERT_HARPOON;
import static legend.lodmod.LodAdditions.ALBERT_ROD_TYPHOON;
import static legend.lodmod.LodAdditions.ALBERT_SPINNING_CANE;
import static legend.lodmod.LodAdditions.BLAZING_DYNAMO;
import static legend.lodmod.LodAdditions.BONE_CRUSH;
import static legend.lodmod.LodAdditions.BURNING_RUSH;
import static legend.lodmod.LodAdditions.CATS_CRADLE;
import static legend.lodmod.LodAdditions.COOL_BOOGIE;
import static legend.lodmod.LodAdditions.CRUSH_DANCE;
import static legend.lodmod.LodAdditions.DEMONS_DANCE;
import static legend.lodmod.LodAdditions.DOUBLE_PUNCH;
import static legend.lodmod.LodAdditions.DOUBLE_SLASH;
import static legend.lodmod.LodAdditions.DOUBLE_SMACK;
import static legend.lodmod.LodAdditions.FERRY_OF_STYX;
import static legend.lodmod.LodAdditions.FIVE_RING_SHATTERING;
import static legend.lodmod.LodAdditions.FLOWER_STORM;
import static legend.lodmod.LodAdditions.GUST_OF_WIND_DANCE;
import static legend.lodmod.LodAdditions.HAMMER_SPIN;
import static legend.lodmod.LodAdditions.HARD_BLADE;
import static legend.lodmod.LodAdditions.HARPOON;
import static legend.lodmod.LodAdditions.HEX_HAMMER;
import static legend.lodmod.LodAdditions.INFERNO;
import static legend.lodmod.LodAdditions.MADNESS_HERO;
import static legend.lodmod.LodAdditions.MOON_STRIKE;
import static legend.lodmod.LodAdditions.MORE_MORE;
import static legend.lodmod.LodAdditions.OMNI_SWEEP;
import static legend.lodmod.LodAdditions.PERKY_STEP;
import static legend.lodmod.LodAdditions.PURSUIT;
import static legend.lodmod.LodAdditions.ROD_TYPHOON;
import static legend.lodmod.LodAdditions.SPINNING_CANE;
import static legend.lodmod.LodAdditions.SUMMON_4_GODS;
import static legend.lodmod.LodAdditions.VOLCANO;
import static legend.lodmod.LodAdditions.WHIP_SMACK;

public final class Scus94491BpeSegment_8004 {
  private Scus94491BpeSegment_8004() { }

  public static int simpleRandSeed_8004dd44 = 3;

  private static final List<Function<RunningScript, FlowControl>> scriptSubFunctions_8004e29c;
  private static List<Function<RunningScript, FlowControl>> engineStateFunctions_8004e29c;
  static {
    final Function<RunningScript, FlowControl>[] functions = new Function[1024];
    functions[0] = Scus94491BpeSegment::scriptSetIndicatorsDisabled;
    functions[1] = Scus94491BpeSegment::scriptReadIndicatorsDisabled;
    functions[2] = Scus94491BpeSegment::scriptSetGlobalFlag1;
    functions[3] = Scus94491BpeSegment::scriptReadGlobalFlag1;
    functions[4] = Scus94491BpeSegment::scriptSetGlobalFlag2;
    functions[5] = Scus94491BpeSegment::scriptReadGlobalFlag2;
    functions[6] = FullScreenEffects::scriptStartFadeEffect;
    functions[7] = DrgnFiles::scriptWaitForFilesToLoad;
    functions[8] = Rumble::scriptStartRumbleMode;
    functions[9] = Scus94491BpeSegment::scriptSetFlag;
    functions[10] = Scus94491BpeSegment::scriptReadFlag;
    functions[11] = Rumble::scriptStartRumble;

    functions[16] = Rumble::scriptSetRumbleDampener;
    functions[17] = Rumble::scriptResetRumbleDampener;

    functions[192] = Text::scriptGetFreeTextboxIndex;
    functions[193] = Text::scriptInitTextbox;
    functions[194] = Text::scriptSetTextboxContents;
    functions[195] = Text::scriptIsTextboxInitialized;
    functions[196] = Text::scriptGetTextboxState;
    functions[197] = Text::scriptGetTextboxTextState;

    functions[199] = Text::scriptSetTextboxVariable;
    functions[200] = Text::scriptAddTextbox;
    functions[201] = Text::scriptDeallocateTextbox;
    functions[202] = Text::scriptDeallocateAllTextboxes;
    functions[203] = Text::FUN_80029ecc;
    functions[204] = Text::FUN_80028ff8;
    functions[205] = Text::scriptGetTextboxSelectionIndex;
    functions[206] = Text::scriptGetTextboxElement;
    functions[207] = Text::scriptAddSelectionTextbox;

    functions[224] = Audio::scriptLoadMenuSounds;
    functions[225] = Audio::FUN_8001e918;

    functions[227] = Audio::FUN_8001eb30;

    functions[230] = Audio::scriptLoadMusicPackage;
    functions[231] = Audio::FUN_8001fe28;
    functions[232] = Audio::scriptUnloadSoundFile;
    functions[233] = Audio::scriptUnuseCharSoundFile;
    functions[234] = Audio::scriptStopEncounterSoundEffects;
    functions[235] = Audio::scriptFreeEncounterSoundEffects;
    functions[236] = Audio::scriptPlaySound;
    functions[237] = Audio::scriptStopSound;

    functions[240] = Audio::scriptStopSoundsAndSequences;
    functions[241] = Audio::scriptStartCurrentMusicSequence;
    functions[242] = Audio::scriptToggleMusicSequencePause;
    functions[243] = Audio::scriptToggleMusicSequencePause2;
    functions[244] = Audio::scriptStopCurrentMusicSequence;

    functions[248] = Audio::FUN_8001b094;
    functions[249] = Audio::FUN_8001b134;
    functions[250] = Audio::FUN_8001b13c;
    functions[251] = Audio::FUN_8001b144;
    functions[252] = Audio::scriptSetMainVolume;
    functions[253] = Audio::scriptSetSequenceVolume;
    functions[254] = Audio::scriptSetAllSoundSequenceVolumes;
    functions[255] = Audio::scriptSssqFadeIn;

    functions[704] = Audio::scriptStartSequenceAndChangeVolumeOverTime;
    functions[705] = Audio::scriptSssqFadeOut;
    functions[706] = Audio::scriptChangeSequenceVolumeOverTime;
    functions[707] = Audio::scriptGetSequenceFlags;
    functions[708] = Audio::scriptGetSssqTempoScale;
    functions[709] = Audio::scriptSetSssqTempoScale;
    functions[710] = Audio::scriptGetLoadedSoundFiles;
    functions[711] = Audio::scriptGetSequenceVolume;

    functions[714] = Audio::scriptStopAndUnloadSequences;

    functions[864] = Scus94491BpeSegment::scriptGiveChestContents;
    functions[865] = Scus94491BpeSegment::scriptTakeItem;
    functions[866] = Scus94491BpeSegment::scriptGiveGold;

    functions[890] = Scus94491BpeSegment::scriptReadRegistryEntryVar;
    functions[891] = SItem::scriptInputActionPressed;
    functions[892] = SItem::scriptInputActionHeld;

    functions[900] = SItem::scriptGetMaxItemCount;
    functions[901] = SItem::scriptGetMaxEquipmentCount;
    functions[902] = SItem::scriptIsItemSlotUsed;
    functions[903] = SItem::scriptIsEquipmentSlotUsed;
    functions[904] = SItem::scriptGetItemSlot;
    functions[905] = SItem::scriptGetEquipmentSlot;
    functions[906] = SItem::scriptSetItemSlot;
    functions[907] = SItem::scriptSetEquipmentSlot;
    functions[908] = SItem::scriptGiveItem;
    functions[909] = SItem::scriptGiveEquipment;
    functions[910] = SItem::scriptTakeItem;
    functions[911] = SItem::scriptTakeEquipment;
    functions[912] = SItem::scriptGenerateAttackItem;
    functions[913] = SItem::scriptGenerateRecoveryItem;
    functions[914] = SItem::scriptHasGood;
    functions[915] = SItem::scriptGiveGood;
    functions[916] = SItem::scriptTakeGood;

    functions[960] = RenderEngine::scriptGetRenderAspectMultiplier;

    //noinspection Java9CollectionFactory List.of rejects nulls
    scriptSubFunctions_8004e29c = Collections.unmodifiableList(Arrays.asList(functions));
  }
  // 8004f29c end of jump table

  // Dart, Lavitz, Shana, Rose, Haschel, Albert, Meru, Kongol, Miranda, DD
  public static final int[] additionOffsets_8004f5ac = {0, 8, -1, 14, 29, 8, 23, 19, -1, 0};

  @SuppressWarnings("unchecked")
  public static final RegistryDelegate<Addition>[][] CHARACTER_ADDITIONS = new RegistryDelegate[][] {
    {DOUBLE_SLASH, VOLCANO, BURNING_RUSH, CRUSH_DANCE, MADNESS_HERO, MOON_STRIKE, BLAZING_DYNAMO},
    {HARPOON, SPINNING_CANE, ROD_TYPHOON, GUST_OF_WIND_DANCE, FLOWER_STORM},
    {},
    {WHIP_SMACK, MORE_MORE, HARD_BLADE, DEMONS_DANCE},
    {DOUBLE_PUNCH, FERRY_OF_STYX, SUMMON_4_GODS, FIVE_RING_SHATTERING, HEX_HAMMER, OMNI_SWEEP},
    {ALBERT_HARPOON, ALBERT_SPINNING_CANE, ALBERT_ROD_TYPHOON, ALBERT_GUST_OF_WIND_DANCE, ALBERT_FLOWER_STORM},
    {DOUBLE_SMACK, HAMMER_SPIN, COOL_BOOGIE, CATS_CRADLE, PERKY_STEP},
    {PURSUIT, INFERNO, BONE_CRUSH},
    {},
  };

  public static final ScriptFile doNothingScript_8004f650 = new ScriptFile("Do nothing", new byte[] {0x4, 0x0, 0x0, 0x0, 0x1, 0x0, 0x0, 0x0});
  public static final List<BattleReportOverlayList10> battleReportOverlayLists_8004f658 = new LinkedList<>();

  @Nullable
  public static Function<RunningScript, FlowControl> getScriptFunction(final int index) {
    Function<RunningScript, FlowControl> function = scriptSubFunctions_8004e29c.get(index);

    if(function == null && engineStateFunctions_8004e29c != null) {
      function = engineStateFunctions_8004e29c.get(index);
    }

    return function;
  }

  public static void loadEngineStateFunctions(final EngineState<?> engineState) {
    final Function<RunningScript, FlowControl>[] functions = engineState.getScriptFunctions();

    if(functions == null) {
      engineStateFunctions_8004e29c = null;
      return;
    }

    //noinspection Java9CollectionFactory List.of rejects nulls
    engineStateFunctions_8004e29c = Collections.unmodifiableList(Arrays.asList(functions));
  }
}
