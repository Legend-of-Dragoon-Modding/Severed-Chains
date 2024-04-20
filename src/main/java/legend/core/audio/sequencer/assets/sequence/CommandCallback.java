package legend.core.audio.sequencer.assets.sequence;

@FunctionalInterface
public interface CommandCallback<T extends Command> {
  void execute(final T command);
}
