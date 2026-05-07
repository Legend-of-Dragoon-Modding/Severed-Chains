package legend.lodmod.characters;

import legend.game.additions.UnlockState;
import legend.game.characters.CharacterData2c;
import legend.game.characters.CharacterSpellInfo;
import legend.game.characters.CharacterTemplate;
import legend.game.characters.StatCollection;
import legend.game.types.GameState52c;
import org.legendofdragoon.modloader.registries.RegistryId;

import java.util.Collection;
import java.util.List;

import static legend.lodmod.LodGoods.DIVINE_DRAGOON_SPIRIT;
import static legend.lodmod.LodSpells.DIVINE_DG_BALL;
import static legend.lodmod.LodSpells.DIVINE_DG_CANNON;

public class DartCharacterData extends CharacterData2c {
  private final List<RegistryId> divineSpells = List.of(DIVINE_DG_BALL.getId(), DIVINE_DG_CANNON.getId());
  private final CharacterSpellInfo spellInfo = new CharacterSpellInfo(List.of(), UnlockState.UNLOCKED, 0);

  public DartCharacterData(final GameState52c gameState, final CharacterTemplate template, final StatCollection stats) {
    super(gameState, template, stats);
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
      return this.divineSpells;
    }

    return super.getUnlockedSpells();
  }

  @Override
  public Collection<RegistryId> getAllSpells() {
    if(this.hasDivineSpirit()) {
      return this.divineSpells;
    }

    return super.getAllSpells();
  }

  @Override
  public CharacterSpellInfo getSpellInfo(final RegistryId id) {
    if(this.hasDivineSpirit()) {
      return this.spellInfo;
    }

    return super.getSpellInfo(id);
  }
}
