package legend.core.audio.sequencer.assets.sequence;

@FunctionalInterface
public interface Command {
  int getDeltaTime();
}
