package legend.core.audio.sequencer.assets.sequence;

import legend.core.audio.sequencer.assets.sequence.Command;

@FunctionalInterface
public interface CommandCallback<T extends Command> {
  void execute(final T command);
}
