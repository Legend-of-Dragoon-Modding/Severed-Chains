package legend.lodmod.characters;

import legend.game.additions.UnlockState;
import legend.game.characters.CharacterData2c;
import legend.game.characters.CharacterSpellInfo;
import legend.game.characters.CharacterTemplate;
import legend.game.characters.StatCollection;
import legend.game.types.GameState52c;
import org.legendofdragoon.modloader.registries.RegistryId;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static legend.lodmod.LodGoods.DIVINE_DRAGOON_SPIRIT;
import static legend.lodmod.LodSpells.DIVINE_DG_BALL;
import static legend.lodmod.LodSpells.DIVINE_DG_CANNON;

public class DartCharacterData extends CharacterData2c {
  private final Map<RegistryId, CharacterSpellInfo> divineSpells = new HashMap<>();

  public DartCharacterData(final GameState52c gameState, final CharacterTemplate template, final StatCollection stats) {
    super(gameState, template, stats);

    this.addDivineSpell(DIVINE_DG_BALL.getId(), new CharacterSpellInfo(List.of(), UnlockState.UNLOCKED, 0));
    this.addDivineSpell(DIVINE_DG_CANNON.getId(), new CharacterSpellInfo(List.of(), UnlockState.UNLOCKED, 0));
  }

  private boolean hasDivineSpirit() {
    return this.gameState.goods_19c.has(DIVINE_DRAGOON_SPIRIT);
  }

  @Override
  public int getStatusAndFlags() {
    int flags = super.getStatusAndFlags();

    if(this.hasDivineSpirit()) {
      flags |= 0x4000;
    }

    return flags;
  }

  @Override
  public boolean hasTransformed() {
    return !this.hasDivineSpirit() && super.hasTransformed();
  }

  @Override
  public List<RegistryId> getUnlockedSpells() {
    if(this.hasDivineSpirit()) {
      return this.getUnlockedDivineSpells();
    }

    return this.getUnlockedRedEyeSpells();
  }

  @Override
  public Collection<RegistryId> getAllSpells() {
    if(this.hasDivineSpirit()) {
      return this.getDivineSpells();
    }

    return this.getRedEyeSpells();
  }

  @Override
  public CharacterSpellInfo getSpellInfo(final RegistryId id) {
    if(this.hasDivineSpirit()) {
      final CharacterSpellInfo spellInfo = this.getDivineSpellInfo(id);

      if(spellInfo != null) {
        return spellInfo;
      }
    }

    return this.getRedEyeSpellInfo(id);
  }

  public CharacterSpellInfo addRedEyeSpell(final RegistryId id, final CharacterSpellInfo info) {
    super.addSpell(id, info);
    return info;
  }

  public void removeRedEyeSpell(final RegistryId id) {
    super.removeSpell(id);
  }

  public List<RegistryId> getUnlockedRedEyeSpells() {
    return super.getUnlockedSpells();
  }

  public Collection<RegistryId> getRedEyeSpells() {
    return super.getAllSpells();
  }

  public CharacterSpellInfo getRedEyeSpellInfo(final RegistryId id) {
    return super.getSpellInfo(id);
  }

  public CharacterSpellInfo addDivineSpell(final RegistryId id, final CharacterSpellInfo info) {
    this.divineSpells.put(id, info);
    return info;
  }

  public void removeDivineSpell(final RegistryId id) {
    this.divineSpells.remove(id);
  }

  public List<RegistryId> getUnlockedDivineSpells() {
    return new ArrayList<>(this.divineSpells.keySet());
  }

  public Collection<RegistryId> getDivineSpells() {
    return new ArrayList<>(this.divineSpells.keySet());
  }

  public CharacterSpellInfo getDivineSpellInfo(final RegistryId id) {
    return this.divineSpells.get(id);
  }
}
