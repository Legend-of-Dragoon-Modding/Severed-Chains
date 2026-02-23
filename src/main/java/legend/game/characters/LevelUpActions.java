package legend.game.characters;

import legend.game.types.CharacterData2c;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class LevelUpActions {
  private final List<Entry<?>> actions = new ArrayList<>();

  public <T> void add(final LevelUpAction<T> action, @Nullable final T options) {
    this.actions.add(new Entry<>(action, options));
  }

  public List<Entry<?>> run(final CharacterData2c character) {
    for(final Entry entry : this.actions) {
      entry.action.apply(character, entry.options);
    }

    return this.actions;
  }

  public static class Entry<T> {
    public final LevelUpAction<T> action;
    public final T options;

    public Entry(final LevelUpAction<T> action, final T options) {
      this.action = action;
      this.options = options;
    }
  }
}
