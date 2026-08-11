package legend.game.modding.events.characters;

import legend.game.characters.CharacterData2c;
import legend.game.characters.Element;
import legend.game.combat.bent.PlayerBattleEntity;
import org.legendofdragoon.modloader.events.Event;

import javax.annotation.Nullable;

/**
 * Fired when an element is resolved for a player character.
 *
 * <p>When {@link #bent} is {@code null}, the element is being resolved through the character
 * flow. When it is present, the element is being resolved for that player battle entity.</p>
 *
 * <p>The base element is the result supplied by the character template. Listeners may replace
 * {@link #element} with a resolved override.</p>
 */
public class ResolveCharacterElementEvent extends Event {
  public final CharacterData2c character;
  @Nullable
  public final PlayerBattleEntity bent;
  public final Element baseElement;
  public Element element;

  public ResolveCharacterElementEvent(final CharacterData2c character, @Nullable final PlayerBattleEntity bent, final Element baseElement) {
    this.character = character;
    this.bent = bent;
    this.baseElement = baseElement;
    this.element = baseElement;
  }
}
