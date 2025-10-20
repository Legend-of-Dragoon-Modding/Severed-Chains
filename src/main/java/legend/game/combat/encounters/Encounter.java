package legend.game.combat.encounters;

import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import it.unimi.dsi.fastutil.ints.IntSet;
import it.unimi.dsi.fastutil.ints.IntSets;
import legend.core.memory.Method;
import org.joml.Vector3f;
import org.legendofdragoon.modloader.registries.RegistryEntry;

import java.util.List;

import static legend.game.Scus94491BpeSegment.loadEncounterSounds;

public class Encounter extends RegistryEntry {
  public final List<Monster> monsters;
  public final IntSet uniqueIds;

  public final int musicIndex;
  public final int escapeChance;
  public final int playerOpeningCamera;
  public final int monsterOpeningCamera;
  public final int[] cameraPosIndices = new int[4];
  public final int postCombatSubmapCut;
  public final int postCombatSubmapScene;

  public final int introWaitTicks;

  public Encounter(final int musicIndex, final int escapeChance, final int playerOpeningCamera, final int monsterOpeningCamera, final int cameraPosIndex0, final int cameraPosIndex1, final int cameraPosIndex2, final int cameraPosIndex3, final int postCombatSubmapCut, final int postCombatSubmapScene, final Monster... monsters) {
    this(musicIndex, escapeChance, playerOpeningCamera, monsterOpeningCamera, cameraPosIndex0, cameraPosIndex1, cameraPosIndex2, cameraPosIndex3, postCombatSubmapCut, postCombatSubmapScene, 0, monsters);
  }

  public Encounter(final int musicIndex, final int escapeChance, final int playerOpeningCamera, final int monsterOpeningCamera, final int cameraPosIndex0, final int cameraPosIndex1, final int cameraPosIndex2, final int cameraPosIndex3, final int postCombatSubmapCut, final int postCombatSubmapScene, final int introWaitTicks, final Monster... monsters) {
    if(monsters.length == 0) {
      throw new IllegalArgumentException("There must be at least one monster");
    }

    this.musicIndex = musicIndex;
    this.escapeChance = escapeChance;
    this.playerOpeningCamera = playerOpeningCamera;
    this.monsterOpeningCamera = monsterOpeningCamera;
    this.cameraPosIndices[0] = cameraPosIndex0;
    this.cameraPosIndices[1] = cameraPosIndex1;
    this.cameraPosIndices[2] = cameraPosIndex2;
    this.cameraPosIndices[3] = cameraPosIndex3;
    this.postCombatSubmapCut = postCombatSubmapCut;
    this.postCombatSubmapScene = postCombatSubmapScene;
    this.introWaitTicks = introWaitTicks;
    this.monsters = List.of(monsters);

    final IntSet uniqueIds = new IntOpenHashSet();

    for(final Monster monster : monsters) {
      uniqueIds.add(monster.id);
    }

    if(uniqueIds.size() > 3) {
      throw new IllegalArgumentException("There cannot be more than 3 unique monsters in an encounter");
    }

    this.uniqueIds = IntSets.unmodifiable(uniqueIds);
  }

  @Method(0x8001d1c4L)
  public void loadSounds(final int phase) {
    loadEncounterSounds(this);
  }

  public int get(final int index) {
    return switch(index) {
      case 1 -> this.musicIndex;
      case 2 -> this.escapeChance;
      case 3 -> this.postCombatSubmapScene;
      case 4 -> this.playerOpeningCamera;
      case 5 -> this.monsterOpeningCamera;
      case 6 -> this.cameraPosIndices[0];
      case 7 -> this.cameraPosIndices[1];
      case 8 -> this.cameraPosIndices[2];
      case 9 -> this.cameraPosIndices[3];
      case 10 -> this.postCombatSubmapCut;
      default -> throw new IllegalArgumentException("Unknown stage data index " + index);
    };
  }

  public void set(final int index, final int value) {
    switch(index) {
      case 6 -> this.cameraPosIndices[0] = value;
      case 7 -> this.cameraPosIndices[1] = value;
      case 8 -> this.cameraPosIndices[2] = value;
      case 9 -> this.cameraPosIndices[3] = value;
      default -> throw new IllegalArgumentException("Unknown stage data index " + index);
    }
  }

  public static class Monster {
    public final int id;
    public final Vector3f pos;

    public Monster(final int id, final Vector3f pos) {
      this.id = id;
      this.pos = pos;
    }
  }
}
