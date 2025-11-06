package legend.game;

import legend.core.RenderEngine;
import legend.game.scripting.FlowControl;
import legend.game.scripting.RunningScript;
import legend.game.scripting.ScriptFile;
import legend.game.types.BattleReportOverlayList10;

import java.util.Arrays;
import java.util.function.Function;

public final class Scus94491BpeSegment_8004 {
  private Scus94491BpeSegment_8004() { }

  public static int simpleRandSeed_8004dd44 = 3;
  public static final int[] _8004dd48 = {0, 1, 2, 1, 2, 1, 2};

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
    scriptSubFunctions_8004e29c[7] = Scus94491BpeSegment::scriptWaitForFilesToLoad;
    scriptSubFunctions_8004e29c[8] = Rumble::scriptStartRumbleMode;
    scriptSubFunctions_8004e29c[9] = Scus94491BpeSegment::scriptSetFlag;
    scriptSubFunctions_8004e29c[10] = Scus94491BpeSegment::scriptReadFlag;
    scriptSubFunctions_8004e29c[11] = Rumble::scriptStartRumble;

    scriptSubFunctions_8004e29c[16] = Rumble::scriptSetRumbleDampener;
    scriptSubFunctions_8004e29c[17] = Rumble::scriptResetRumbleDampener;

    scriptSubFunctions_8004e29c[192] = Textboxes::scriptGetFreeTextboxIndex;
    scriptSubFunctions_8004e29c[193] = Textboxes::scriptInitTextbox;
    scriptSubFunctions_8004e29c[194] = Textboxes::scriptSetTextboxContents;
    scriptSubFunctions_8004e29c[195] = Textboxes::scriptIsTextboxInitialized;
    scriptSubFunctions_8004e29c[196] = Textboxes::scriptGetTextboxState;
    scriptSubFunctions_8004e29c[197] = Textboxes::scriptGetTextboxTextState;

    scriptSubFunctions_8004e29c[199] = Textboxes::scriptSetTextboxVariable;
    scriptSubFunctions_8004e29c[200] = Textboxes::scriptAddTextbox;
    scriptSubFunctions_8004e29c[201] = Textboxes::scriptDeallocateTextbox;
    scriptSubFunctions_8004e29c[202] = Textboxes::scriptDeallocateAllTextboxes;
    scriptSubFunctions_8004e29c[203] = Textboxes::FUN_80029ecc;
    scriptSubFunctions_8004e29c[204] = Textboxes::FUN_80028ff8;
    scriptSubFunctions_8004e29c[205] = Textboxes::scriptGetTextboxSelectionIndex;
    scriptSubFunctions_8004e29c[206] = Textboxes::scriptGetTextboxElement;
    scriptSubFunctions_8004e29c[207] = Textboxes::scriptAddSelectionTextbox;

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

    scriptSubFunctions_8004e29c[864] = Scus94491BpeSegment_8002::scriptGiveChestContents;
    scriptSubFunctions_8004e29c[865] = Scus94491BpeSegment_8002::scriptTakeItem;
    scriptSubFunctions_8004e29c[866] = Scus94491BpeSegment_8002::scriptGiveGold;

    scriptSubFunctions_8004e29c[890] = Scus94491BpeSegment_8002::scriptReadRegistryEntryVar;

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

    scriptSubFunctions_8004e29c[960] = RenderEngine::scriptGetRenderAspectMultiplier;
  }
  // 8004f29c end of jump table

  public static final int[] additionOffsets_8004f5ac = {0, 8, -1, 14, 29, 8, 23, 19, -1, 0};
  public static final int[] additionCounts_8004f5c0 = {7, 5, 0, 4, 6, 5, 5, 3, 0, 0};

  public static final ScriptFile doNothingScript_8004f650 = new ScriptFile("Do nothing", new byte[] {0x4, 0x0, 0x0, 0x0, 0x1, 0x0, 0x0, 0x0});
  public static BattleReportOverlayList10 battleReportOverlayLists_8004f658;

  public static int _8004f6e4 = -1;

  public static int battleStartDelayTicks_8004f6ec;
}
