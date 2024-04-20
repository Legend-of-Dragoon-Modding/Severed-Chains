package legend.core.audio.sequencer;

enum VoicePriority {
  LOW,
  MEDIUM,
  HIGH;

  static VoicePriority getPriority(final boolean layerPriority, final int channelPriority) {
    if(!layerPriority) {
      return LOW;
    }

    if(channelPriority != 0x7f) {
      return MEDIUM;
    }

    return HIGH;
  }

  static VoicePriority decreasePriority(final VoicePriority priority) {
    if(priority == HIGH) {
      return MEDIUM;
    }

    return LOW;
  }
}
