package legend.game;

import legend.core.gte.MV;
import legend.core.memory.Value;
import legend.core.memory.types.ArrayRef;
import legend.core.memory.types.IntRef;
import legend.core.memory.types.ShortRef;
import legend.core.memory.types.UnsignedByteRef;
import legend.core.memory.types.UnsignedIntRef;
import legend.game.combat.types.CombatantStruct1a8;
import legend.game.sound.ReverbConfigAndLocation;
import legend.game.types.AdditionData0e;
import legend.game.types.LodString;

import java.util.Arrays;

import static legend.core.GameEngine.MEMORY;

public final class Scus94491BpeSegment_8005 {
  private Scus94491BpeSegment_8005() { }

  public static final ArrayRef<ShortRef> submapMusic_80050068 = MEMORY.ref(2, 0x80050068L, ArrayRef.of(ShortRef.class, 64, 2, ShortRef::new));
  public static final int[] monsterSoundFileIndices_800500e8 = {4, 5, 6, 7};
  public static final int[] characterSoundFileIndices_800500f8 = {1, 2, 3};

  public static final int[] charSlotSpuOffsets_80050190 = {0x44250, 0x4b780, 0x52cb0};

  public static final ArrayRef<UnsignedByteRef> combatSoundEffectsTypes_8005019c = MEMORY.ref(1, 0x8005019cL, ArrayRef.of(UnsignedByteRef.class, 32, 1, UnsignedByteRef::new));
  public static final ArrayRef<UnsignedIntRef> combatMusicFileIndices_800501bc = MEMORY.ref(4, 0x800501bcL, ArrayRef.of(UnsignedIntRef.class, 32, 4, UnsignedIntRef::new));

  public static int _80050274 = -1;

  public static final Value _8005027c = MEMORY.ref(4, 0x8005027cL);

  public static final int[] shadowScale_8005039c = {0x1800, 0x1800, 0x1000, 0xe00, 0x1600, 0x1300, 0xe00, 0x2000, 0x1300, 0x1500};
  public static final ArrayRef<ShortRef> _800503b0 = MEMORY.ref(2, 0x800503b0L, ArrayRef.of(ShortRef.class, 18, 2, ShortRef::new));
  public static final ArrayRef<ShortRef> _800503d4 = MEMORY.ref(2, 0x800503d4L, ArrayRef.of(ShortRef.class, 18, 2, ShortRef::new));
  public static final ArrayRef<ShortRef> _800503f8 = MEMORY.ref(2, 0x800503f8L, ArrayRef.of(ShortRef.class, 22, 2, ShortRef::new));
  public static final ArrayRef<ShortRef> _80050424 = MEMORY.ref(2, 0x80050424L, ArrayRef.of(ShortRef.class, 22, 2, ShortRef::new));

  public static final LodString[] itemCombatDescriptions_80051758 = {
    new LodString(" "), new LodString("Detonates & attacks all."), new LodString("S attack(thunder, multi)."), new LodString("S attack(flame, multi)."), new LodString(" "),
    new LodString("S attack(earth, multi)."), new LodString("S attack(water, multi)."), new LodString("S attack(wind, multi)."), new LodString("Generates 1 attack item."), new LodString("S attack(light, multi)."),
    new LodString("S attack(dark, multi)."), new LodString("Recovers 1/2 of max HP."), new LodString("Dissolves petrification."), new LodString("Dissolves B, C, F, D."), new LodString("Dissolves P, S, A-block."),
    new LodString("A attack(thndr, multi)."), new LodString("A attack(earth, multi)."), new LodString("A attack(flame, multi)."), new LodString("A attack(light, multi)."), new LodString("Recovers 100SP in combat."),
    new LodString("Confuses minor enemy."), new LodString(" "), new LodString("A attack(water, multi)."), new LodString("Stuns minor enemy."), new LodString("A attack(dark, multi)."),
    new LodString("Poisons minor enemy."), new LodString("Frightens minor enemy."), new LodString(" "), new LodString("A attack (wind, multi)."), new LodString("Eliminates minor enemy."),
    new LodString("Revitalize & Recover 1/2HP."), new LodString("Avoids minor enemy 3x."), new LodString("Allow minor enemy attack 3x."), new LodString("Generates 1 recovery item."), new LodString(" "),
    new LodString("Nullify magic attack 3x."), new LodString("Nullify phys attack 3x."), new LodString("Completely recovers MP."), new LodString("Escape a minor enemy."), new LodString("Completely recovers HP."),
    new LodString("Blocks enemy move 3x (rep)."), new LodString("Recovers 100% HP for all."), new LodString("Recovers 100% MP for all."), new LodString("3 turns powerful (rep)."), new LodString("3 turns weak (rep)."),
    new LodString("Double agility 3x (rep)."), new LodString("Halves agility 3x(rep)."), new LodString(" "), new LodString("Gives subtle good aroma."), new LodString("A attack(non, multi)."),
    new LodString("Powerful A attack(Flame)."), new LodString("Powerful A attack(Water)."), new LodString("Powerful A attack(Wind)."), new LodString("Powerful A attack(Earth)."), new LodString("Powerful A attack(Light)."),
    new LodString("Powerful A attack(Dark)."), new LodString("Powerful A attack(Thndr)."), new LodString("Recovers 1/2 HP for all."), new LodString("A attack (non, multi, rep)."), new LodString(" "),
    new LodString(" "), new LodString(" "), new LodString(" "), new LodString(" "),
  };

  public static final LodString[] spellCombatDescriptions_80052018 = {
    new LodString("Fire STR 50% Single"), new LodString("Fire STR 25% All"), new LodString("Fire STR 75% Single"), new LodString("Fire STR 175% All"), new LodString("STR 100% Single"),
    new LodString("Wind STR 25% All"), new LodString("Wind STR 75% Single"), new LodString("Damage Resist 50% Dur 3"), new LodString("Wind STR 75% All"), new LodString("STR 50% All"),
    new LodString("Light STR 25% All"), new LodString("Ally Single 100% Rev & Rec"), new LodString("Ally All 100% Recover"), new LodString("Light STR 100% All HP"), new LodString("Wind STR 25% All"),
    new LodString("Dark STR 25% Single & HP"), new LodString("Dark STR 25% All & Fear"), new LodString("Wind STR 100% Single"), new LodString("Lethal attack for all"), new LodString("Darkn STR 100% Single"),
    new LodString("Thunder STR 50% Single"), new LodString("Thunder STR 65% Single"), new LodString("Thunder STR 75% Single"), new LodString("Thunder STR 100% Single"), new LodString("Water STR 50% Single"),
    new LodString("HP Recv & Cure - All"), new LodString("Damage Resist 50% Dur 3"), new LodString("Water STR 50% All"), new LodString("Water STR 100% Single"), new LodString("Earth STR 25% All"),
    new LodString("Earth STR 50% All"), new LodString("Earth STR 75% All"), new LodString(""), new LodString(""), new LodString(""),
    new LodString(""), new LodString(""), new LodString(""), new LodString(""), new LodString(""),
    new LodString(""), new LodString(""), new LodString(""), new LodString(""), new LodString(""),
    new LodString(""), new LodString(""), new LodString(""), new LodString(""), new LodString(""),
    new LodString(""), new LodString(""), new LodString(""), new LodString(""), new LodString(""),
    new LodString(""), new LodString(""), new LodString(""), new LodString(""), new LodString(""),
    new LodString(""), new LodString(""), new LodString(""), new LodString(""), new LodString(""),
    new LodString("Light STR 25% All"), new LodString("HP Recv & Cure-Single"), new LodString("HP Recv & Cure-All"), new LodString(""), new LodString(""),
    new LodString(""), new LodString(""), new LodString(""), new LodString(""), new LodString(""),
    new LodString(""), new LodString(""), new LodString(""), new LodString(""), new LodString(""),
    new LodString(""), new LodString(""), new LodString(""), new LodString(""),
  };

  public static final LodString[] spells_80052734 = {
    new LodString("Flameshot"), new LodString("Explosion"), new LodString("Final Burst"), new LodString("Red-Eyed Dragon"), new LodString("Divine DG Cannon"),
    new LodString("Wing Blaster"), new LodString("Gaspless"), new LodString("Blossom Storm"), new LodString("Jade Dragon"), new LodString("Divine DG Ball"),
    new LodString("Star Children"), new LodString("Moon Light"), new LodString("Gates of Heaven"), new LodString("W Silver Dragon"), new LodString("Wing Blaster"),
    new LodString("Astral Drain"), new LodString("Death Dimension"), new LodString("Gaspless"), new LodString("Demon's Gate"), new LodString("Dark Dragon"),
    new LodString("Atomic Mind"), new LodString("Thunder Kid"), new LodString("Thunder God"), new LodString("Violet Dragon"), new LodString("Freezing Ring"),
    new LodString("Rainbow Breath"), new LodString("Rose Storm"), new LodString("Diamond Dust"), new LodString("Blue Sea Dragon"), new LodString("Grand Stream"),
    new LodString("Meteor Strike"), new LodString("Golden Dragon"), new LodString(""), new LodString(""), new LodString(""),
    new LodString(""), new LodString(""), new LodString(""), new LodString(""), new LodString(""),
    new LodString(""), new LodString(""), new LodString(""), new LodString(""), new LodString(""),
    new LodString(""), new LodString(""), new LodString(""), new LodString(""), new LodString(""),
    new LodString(""), new LodString(""), new LodString(""), new LodString(""), new LodString(""),
    new LodString(""), new LodString(""), new LodString(""), new LodString(""), new LodString(""),
    new LodString(""), new LodString(""), new LodString(""), new LodString(""), new LodString(""),
    new LodString("Star Children"), new LodString("Moon Light"), new LodString("Gates of Heaven"), new LodString(""), new LodString(""),
    new LodString(""), new LodString(""), new LodString(""), new LodString(""), new LodString(""),
    new LodString(""), new LodString(""), new LodString(""), new LodString(""), new LodString(""),
    new LodString(""), new LodString(""), new LodString(""), new LodString(""),
  };
  public static final ArrayRef<AdditionData0e> additionData_80052884 = MEMORY.ref(1, 0x80052884L, ArrayRef.of(AdditionData0e.class, 43, 0xe, AdditionData0e::new));

  public static final LodString[] digits_80052b40 = { new LodString("0"), new LodString("1"), new LodString("2"), new LodString("3"), new LodString("4"), new LodString("5"), new LodString("6"), new LodString("7"), new LodString("8"), new LodString("9") };
  public static final boolean[] renderBorder_80052b68 = {false, true, false, false, false, false, false, false, false, false, false, false, false, false, false, false};
  public static final int[] textboxMode_80052b88 = {0, 1, 2, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3};
  public static final int[] textboxTextType_80052ba8 = {0, 1, 2, 3, 4, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2};

  public static int submapCut_80052c30 = 675;
  public static int submapScene_80052c34 = 4;
  /** TODO This seems like it's set to a lot of different things, hopefully they're actually related. */
  public static int index_80052c38;
  public static int submapCut_80052c3c = -1;
  /** Moved from SMAP since it's referenced unconditionally when saving the game */
  public static int submapCutForSave_800cb450;
  /** Something related to submap camera and map transitioning */
  public static boolean _80052c40;
  public static int submapEnvState_80052c44 = 2;

  public static boolean reinitializingWmap_80052c6c;

  public static final ArrayRef<IntRef> _80052d6c = MEMORY.ref(4, 0x80052d6cL, ArrayRef.of(IntRef.class, 4, 4, IntRef::new));
  public static final String[][] diskFmvs_80052d7c = {
    {"\\STR\\DEMOH.IKI", "\\STR\\DEMO2.IKI", "\\STR\\OPENH.IKI", "\\STR\\WAR1H.IKI"},
    {"\\STR\\TVRH.IKI", "\\STR\\GOAST.IKI", "\\STR\\ROZEH.IKI"},
    {"\\STR\\TREEH.IKI", "\\STR\\WAR2H.IKI", "\\STR\\BLACKH.IKI", "\\STR\\DRAGON1.IKI", "\\STR\\DENIN.IKI", "\\STR\\DENIN2.IKI", "\\STR\\DRAGON2.IKI", "\\STR\\DEIASH.IKI"},
    {"\\STR\\MOONH.IKI", "\\STR\\ENDING1H.IKI", "\\STR\\ENDING2H.IKI"}
  };

  public static final int vramWidth_800546c0 = 1024;
  public static final int vramHeight_800546c2 = 512;

  public static int matrixStackIndex_80054a08;
  public static final MV[] matrixStack_80054a0c = new MV[20];
  static {
    Arrays.setAll(matrixStack_80054a0c, i -> new MV());
  }

  /** Precomputed sin/cos table */
  public static final ArrayRef<ShortRef> sin_cos_80054d0c = MEMORY.ref(2, 0x80054d0cL, ArrayRef.of(ShortRef.class, 0x2000, 2, ShortRef::new));
  public static final ArrayRef<ShortRef> atanTable_80058d0c = MEMORY.ref(2, 0x80058d0cL, ArrayRef.of(ShortRef.class, 0x401, 2, ShortRef::new));

  /**
   * Start of a fairly large block of data - something to do with SPU reverb initialisation. Stride is 66 bytes. Unknown length.
   */
  public static final ArrayRef<ReverbConfigAndLocation> reverbConfigs_80059f7c = MEMORY.ref(2, 0x80059f7cL, ArrayRef.of(ReverbConfigAndLocation.class, 9, 0x42, ReverbConfigAndLocation::new));

  /** short */
  public static int sssqFadeCurrent_8005a1ce;

  public static boolean standingInSavePoint_8005a368;

  public static final CombatantStruct1a8[] combatants_8005e398 = new CombatantStruct1a8[10];
}
