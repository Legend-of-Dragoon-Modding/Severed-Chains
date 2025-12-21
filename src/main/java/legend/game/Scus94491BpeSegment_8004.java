package legend.game;

import legend.core.RenderEngine;
import legend.game.additions.Addition;
import legend.game.scripting.FlowControl;
import legend.game.scripting.RunningScript;
import legend.game.scripting.ScriptFile;
import legend.game.types.BattleReportOverlayList10;
import org.legendofdragoon.modloader.registries.RegistryDelegate;

import java.util.Arrays;
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

  public static final Function<RunningScript, FlowControl>[] scriptSubFunctions_8004e29c = new Function[1024];
  public static Function<RunningScript, FlowControl>[] engineStateFunctions_8004e29c = new Function[1024];
  static {
    Arrays.setAll(scriptSubFunctions_8004e29c, i -> Scus94491BpeSegment::scriptRewindAndPause2);

    scriptSubFunctions_8004e29c[0] = Scus94491BpeSegment::scriptSetIndicatorsDisabled;
    scriptSubFunctions_8004e29c[1] = Scus94491BpeSegment::scriptReadIndicatorsDisabled;
    scriptSubFunctions_8004e29c[2] = Scus94491BpeSegment::scriptSetGlobalFlag1;
    scriptSubFunctions_8004e29c[3] = Scus94491BpeSegment::scriptReadGlobalFlag1;
    scriptSubFunctions_8004e29c[4] = Scus94491BpeSegment::scriptSetGlobalFlag2;
    scriptSubFunctions_8004e29c[5] = Scus94491BpeSegment::scriptReadGlobalFlag2;
    scriptSubFunctions_8004e29c[6] = FullScreenEffects::scriptStartFadeEffect;
    scriptSubFunctions_8004e29c[7] = DrgnFiles::scriptWaitForFilesToLoad;
    scriptSubFunctions_8004e29c[8] = Rumble::scriptStartRumbleMode;
    scriptSubFunctions_8004e29c[9] = Scus94491BpeSegment::scriptSetFlag;
    scriptSubFunctions_8004e29c[10] = Scus94491BpeSegment::scriptReadFlag;
    scriptSubFunctions_8004e29c[11] = Rumble::scriptStartRumble;

    scriptSubFunctions_8004e29c[16] = Rumble::scriptSetRumbleDampener;
    scriptSubFunctions_8004e29c[17] = Rumble::scriptResetRumbleDampener;

    scriptSubFunctions_8004e29c[192] = Text::scriptGetFreeTextboxIndex;
    scriptSubFunctions_8004e29c[193] = Text::scriptInitTextbox;
    scriptSubFunctions_8004e29c[194] = Text::scriptSetTextboxContents;
    scriptSubFunctions_8004e29c[195] = Text::scriptIsTextboxInitialized;
    scriptSubFunctions_8004e29c[196] = Text::scriptGetTextboxState;
    scriptSubFunctions_8004e29c[197] = Text::scriptGetTextboxTextState;

    scriptSubFunctions_8004e29c[199] = Text::scriptSetTextboxVariable;
    scriptSubFunctions_8004e29c[200] = Text::scriptAddTextbox;
    scriptSubFunctions_8004e29c[201] = Text::scriptDeallocateTextbox;
    scriptSubFunctions_8004e29c[202] = Text::scriptDeallocateAllTextboxes;
    scriptSubFunctions_8004e29c[203] = Text::FUN_80029ecc;
    scriptSubFunctions_8004e29c[204] = Text::FUN_80028ff8;
    scriptSubFunctions_8004e29c[205] = Text::scriptGetTextboxSelectionIndex;
    scriptSubFunctions_8004e29c[206] = Text::scriptGetTextboxElement;
    scriptSubFunctions_8004e29c[207] = Text::scriptAddSelectionTextbox;

    scriptSubFunctions_8004e29c[224] = Audio::scriptLoadMenuSounds;
    scriptSubFunctions_8004e29c[225] = Audio::FUN_8001e918;
    scriptSubFunctions_8004e29c[226] = Audio::scriptLoadCharAttackSounds;
    scriptSubFunctions_8004e29c[227] = Audio::FUN_8001eb30;
    scriptSubFunctions_8004e29c[228] = Audio::scriptLoadBattleCutsceneSounds;
    scriptSubFunctions_8004e29c[229] = Audio::scriptLoadMonsterAttackSounds;
    scriptSubFunctions_8004e29c[230] = Audio::scriptLoadMusicPackage;
    scriptSubFunctions_8004e29c[231] = Audio::FUN_8001fe28;
    scriptSubFunctions_8004e29c[232] = Audio::scriptUnloadSoundFile;
    scriptSubFunctions_8004e29c[233] = Audio::scriptUnuseCharSoundFile;
    scriptSubFunctions_8004e29c[234] = Audio::scriptStopEncounterSoundEffects;
    scriptSubFunctions_8004e29c[235] = Audio::scriptFreeEncounterSoundEffects;
    scriptSubFunctions_8004e29c[236] = Audio::scriptPlaySound;
    scriptSubFunctions_8004e29c[237] = Audio::scriptStopSound;

    scriptSubFunctions_8004e29c[240] = Audio::scriptStopSoundsAndSequences;
    scriptSubFunctions_8004e29c[241] = Audio::scriptStartCurrentMusicSequence;
    scriptSubFunctions_8004e29c[242] = Audio::scriptToggleMusicSequencePause;
    scriptSubFunctions_8004e29c[243] = Audio::scriptToggleMusicSequencePause2;
    scriptSubFunctions_8004e29c[244] = Audio::scriptStopCurrentMusicSequence;
    scriptSubFunctions_8004e29c[245] = Audio::scriptStartEncounterSounds;
    scriptSubFunctions_8004e29c[246] = Audio::scriptStopEncounterSounds;
    scriptSubFunctions_8004e29c[247] = Audio::scriptStopEncounterSounds2;
    scriptSubFunctions_8004e29c[248] = Audio::FUN_8001b094;
    scriptSubFunctions_8004e29c[249] = Audio::FUN_8001b134;
    scriptSubFunctions_8004e29c[250] = Audio::FUN_8001b13c;
    scriptSubFunctions_8004e29c[251] = Audio::FUN_8001b144;
    scriptSubFunctions_8004e29c[252] = Audio::scriptSetMainVolume;
    scriptSubFunctions_8004e29c[253] = Audio::scriptSetSequenceVolume;
    scriptSubFunctions_8004e29c[254] = Audio::scriptSetAllSoundSequenceVolumes;
    scriptSubFunctions_8004e29c[255] = Audio::scriptSssqFadeIn;

    scriptSubFunctions_8004e29c[704] = Audio::scriptStartSequenceAndChangeVolumeOverTime;
    scriptSubFunctions_8004e29c[705] = Audio::scriptSssqFadeOut;
    scriptSubFunctions_8004e29c[706] = Audio::scriptChangeSequenceVolumeOverTime;
    scriptSubFunctions_8004e29c[707] = Audio::scriptGetSequenceFlags;
    scriptSubFunctions_8004e29c[708] = Audio::scriptGetSssqTempoScale;
    scriptSubFunctions_8004e29c[709] = Audio::scriptSetSssqTempoScale;
    scriptSubFunctions_8004e29c[710] = Audio::scriptGetLoadedSoundFiles;
    scriptSubFunctions_8004e29c[711] = Audio::scriptGetSequenceVolume;

    scriptSubFunctions_8004e29c[714] = Audio::scriptStopAndUnloadSequences;
    scriptSubFunctions_8004e29c[715] = Audio::scriptLoadCharacterAttackSounds;
    scriptSubFunctions_8004e29c[716] = Audio::scriptReplaceMonsterSounds;
    scriptSubFunctions_8004e29c[717] = Audio::scriptLoadCutsceneSounds;
    scriptSubFunctions_8004e29c[718] = Audio::scriptLoadFinalBattleSounds;

    scriptSubFunctions_8004e29c[864] = Scus94491BpeSegment::scriptGiveChestContents;
    scriptSubFunctions_8004e29c[865] = Scus94491BpeSegment::scriptTakeItem;
    scriptSubFunctions_8004e29c[866] = Scus94491BpeSegment::scriptGiveGold;

    scriptSubFunctions_8004e29c[890] = Scus94491BpeSegment::scriptReadRegistryEntryVar;

    scriptSubFunctions_8004e29c[900] = SItem::scriptGetMaxItemCount;
    scriptSubFunctions_8004e29c[901] = SItem::scriptGetMaxEquipmentCount;
    scriptSubFunctions_8004e29c[902] = SItem::scriptIsItemSlotUsed;
    scriptSubFunctions_8004e29c[903] = SItem::scriptIsEquipmentSlotUsed;
    scriptSubFunctions_8004e29c[904] = SItem::scriptGetItemSlot;
    scriptSubFunctions_8004e29c[905] = SItem::scriptGetEquipmentSlot;
    scriptSubFunctions_8004e29c[906] = SItem::scriptSetItemSlot;
    scriptSubFunctions_8004e29c[907] = SItem::scriptSetEquipmentSlot;
    scriptSubFunctions_8004e29c[908] = SItem::scriptGiveItem;
    scriptSubFunctions_8004e29c[909] = SItem::scriptGiveEquipment;
    scriptSubFunctions_8004e29c[910] = SItem::scriptTakeItem;
    scriptSubFunctions_8004e29c[911] = SItem::scriptTakeEquipment;
    scriptSubFunctions_8004e29c[912] = SItem::scriptGenerateAttackItem;
    scriptSubFunctions_8004e29c[913] = SItem::scriptGenerateRecoveryItem;
    scriptSubFunctions_8004e29c[914] = SItem::scriptHasGood;
    scriptSubFunctions_8004e29c[915] = SItem::scriptGiveGood;
    scriptSubFunctions_8004e29c[916] = SItem::scriptTakeGood;

    scriptSubFunctions_8004e29c[960] = RenderEngine::scriptGetRenderAspectMultiplier;
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
  public static BattleReportOverlayList10 battleReportOverlayLists_8004f658;
}
