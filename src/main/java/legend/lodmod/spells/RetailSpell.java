package legend.lodmod.spells;

import legend.game.characters.Element;
import legend.game.combat.Battle;
import legend.game.combat.effects.ScriptDeffEffect;
import legend.game.combat.spells.SpellEffectPlan;
import legend.game.combat.types.BattleObject;
import legend.game.inventory.SpellStats0c;
import legend.game.scripting.ScriptState;
import legend.game.unpacker.Loader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;
import org.legendofdragoon.modloader.registries.RegistryDelegate;

import java.util.List;

import static legend.game.DrgnFiles.loadDrgnDir;
import static legend.game.combat.Battle.deffManager_800c693c;
import static legend.game.combat.Battle.dragoonDeffFlags_800fafec;
import static legend.game.combat.Battle.dragoonDeffsWithExtraTims_800fb040;

public class RetailSpell extends SpellStats0c {
  private static final Logger LOGGER = LogManager.getFormatterLogger(RetailSpell.class);
  private static final Marker DEFF = MarkerManager.getMarker("DEFF");

  private final int index;

  public RetailSpell(final int targetType, final int flags, final int specialEffect, final int damage, final int multi, final int accuracy, final int mp, final int statusChance, final RegistryDelegate<Element> element, final int statusType, final int buffType, final int _0b, final int index) {
    super(targetType, flags, specialEffect, damage, multi, accuracy, mp, statusChance, element, statusType, buffType, _0b);
    this.index = index;
  }

    /**
     * Creates a retail spell with one effect plan.
     *
     * @param targetType retail target flags stored in {@link #targetType_00}
     * @param flags retail spell flags stored in {@link #flags_01}
     * @param specialEffect retail special-effect value stored in {@link #specialEffect_02}
     * @param damage retail damage multiplier stored in {@link #damageMultiplier_03}
     * @param multi retail multi-purpose value stored in {@link #multi_04}
     * @param accuracy spell accuracy stored in {@link #accuracy_05}
     * @param mp MP cost stored in {@link #mp_06}
     * @param statusChance retail status chance stored in {@link #statusChance_07}
     * @param element spell element
     * @param statusType retail status mask stored in {@link #statusType_09}
     * @param buffType retail buff mask stored in {@link #buffType_0a}
     * @param _0b retail spell metadata stored in {@link #_0b}
     * @param index Dragoon spell effect index used to load the spell's visual effect
     * @param effectPlan effect plan attached to the spell
     */
    public RetailSpell(final int targetType, final int flags, final int specialEffect, final int damage, final int multi, final int accuracy, final int mp, final int statusChance, final RegistryDelegate<Element> element, final int statusType, final int buffType, final int _0b, final int index, final SpellEffectPlan effectPlan) {
    this(targetType, flags, specialEffect, damage, multi, accuracy, mp, statusChance, element, statusType, buffType, _0b, index, List.of(effectPlan));
  }

    /**
     * Creates a retail spell with one or more effect plans.
     *
     * @param targetType retail target flags stored in {@link #targetType_00}
     * @param flags retail spell flags stored in {@link #flags_01}
     * @param specialEffect retail special-effect value stored in {@link #specialEffect_02}
     * @param damage retail damage multiplier stored in {@link #damageMultiplier_03}
     * @param multi retail multi-purpose value stored in {@link #multi_04}
     * @param accuracy spell accuracy stored in {@link #accuracy_05}
     * @param mp MP cost stored in {@link #mp_06}
     * @param statusChance retail status chance stored in {@link #statusChance_07}
     * @param element spell element
     * @param statusType retail status mask stored in {@link #statusType_09}
     * @param buffType retail buff mask stored in {@link #buffType_0a}
     * @param _0b retail spell metadata stored in {@link #_0b}
     * @param index Dragoon spell effect index used to load the spell's visual effect
     * @param effectPlans non-null, non-empty effect plans defensively copied onto the spell
     */
    public RetailSpell(final int targetType, final int flags, final int specialEffect, final int damage, final int multi, final int accuracy, final int mp, final int statusChance, final RegistryDelegate<Element> element, final int statusType, final int buffType, final int _0b, final int index, final List<SpellEffectPlan> effectPlans) {
    this(targetType, flags, specialEffect, damage, multi, accuracy, mp, statusChance, element, statusType, buffType, _0b, index);
    this.setEffectPlans(effectPlans);
  }

  @Override
  public void loadDeff(final Battle battle, final ScriptState<? extends BattleObject> parent, final ScriptDeffEffect effect, final int flags, final int bentIndex, final int deffParam, final int entrypoint) {
    LOGGER.info(DEFF, "Loading dragoon DEFF (ID: %d, flags: %x)", this.index, flags);

    deffManager_800c693c.flags_20 |= dragoonDeffFlags_800fafec[this.index] << 16;
    battle.allocateDeffEffectManager(parent, flags, bentIndex, deffParam, entrypoint, effect);

    if((deffManager_800c693c.flags_20 & 0x4_0000) != 0) {
      battle.loadDeffSounds(battle.loadedDeff_800c6938.bentState_04, this.index != 46 || entrypoint != 0 ? 0 : 2);
    }

    for(int i = 0; i < dragoonDeffsWithExtraTims_800fb040.length; i++) {
      if(dragoonDeffsWithExtraTims_800fb040[i] == this.index) {
        if(Loader.isDirectory("SECT/DRGN0.BIN/%d".formatted(4115 + i))) {
          loadDrgnDir(0, 4115 + i).thenAccept(battle::uploadTims);
        }
      }
    }

    battle.loadDeff(
      Loader.resolve("SECT/DRGN0.BIN/" + (4139 + this.index * 2)),
      Loader.resolve("SECT/DRGN0.BIN/" + (4140 + this.index * 2))
    );
  }
}
