package legend.core.audio.sequencer;

enum VoicePriority {
  Low,
  Medium,
  High;

  static VoicePriority getPriority(final boolean layerPriority, final int channelPriority) {
    if(!layerPriority) {
      return Low;
    }

    if(channelPriority != 0x7f) {
      return Medium;
    }

    return High;
  }

  static VoicePriority decreasePriority(final VoicePriority priority) {
    if(priority == High) {
      return Medium;
    }

    return Low;
  }
}
