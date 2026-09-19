package legend.lodmod.spells;

import legend.game.characters.Element;
import legend.game.combat.spells.SpellEffectPlan;
import org.legendofdragoon.modloader.registries.RegistryDelegate;

import java.util.List;

public class DragonSpell extends RetailSpell {
  private final int battleStage;

  public DragonSpell(final int targetType, final int flags, final int specialEffect, final int damage, final int multi, final int accuracy, final int mp, final int statusChance, final RegistryDelegate<Element> element, final int statusType, final int buffType, final int _0b, final int index, final int battleStage) {
    super(targetType, flags, specialEffect, damage, multi, accuracy, mp, statusChance, element, statusType, buffType, _0b, index);
    this.battleStage = battleStage;
  }

    /**
     * Creates a dragon spell with one effect plan.
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
     * @param battleStage battle-stage override returned while the spell is active
     * @param effectPlan effect plan attached to the spell
     */
    public DragonSpell(final int targetType, final int flags, final int specialEffect, final int damage, final int multi, final int accuracy, final int mp, final int statusChance, final RegistryDelegate<Element> element, final int statusType, final int buffType, final int _0b, final int index, final int battleStage, final SpellEffectPlan effectPlan) {
    this(targetType, flags, specialEffect, damage, multi, accuracy, mp, statusChance, element, statusType, buffType, _0b, index, battleStage, List.of(effectPlan));
  }

    /**
     * Creates a dragon spell with one or more effect plans.
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
     * @param battleStage battle-stage override returned while the spell is active
     * @param effectPlans non-null, non-empty effect plans defensively copied onto the spell
     */
    public DragonSpell(final int targetType, final int flags, final int specialEffect, final int damage, final int multi, final int accuracy, final int mp, final int statusChance, final RegistryDelegate<Element> element, final int statusType, final int buffType, final int _0b, final int index, final int battleStage, final List<SpellEffectPlan> effectPlans) {
    super(targetType, flags, specialEffect, damage, multi, accuracy, mp, statusChance, element, statusType, buffType, _0b, index, effectPlans);
    this.battleStage = battleStage;
  }

  @Override
  public int getBattleStage() {
    return this.battleStage;
  }
}
