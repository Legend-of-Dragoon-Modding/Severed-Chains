package legend.core.audio.sequencer.assets;

import legend.core.audio.SampleRate;
import legend.game.unpacker.FileData;

import javax.annotation.Nullable;

public final class SoundFont {
  private final Instrument[] instruments;

  SoundFont(final FileData data, final SoundBank soundBank, final SampleRate sampleRate) {
    final int instrumentsUpperBound = data.readUShort(0);

    this.instruments = new Instrument[instrumentsUpperBound + 1];
    int lastOffset = data.size();
    for(int instrument = instrumentsUpperBound; instrument >= 0; instrument--) {
      final int offset = data.readShort(2 + instrument * 2);

      if(offset != -1) {
        this.instruments[instrument] = new Instrument(data.slice(offset, lastOffset - offset), soundBank, sampleRate);

        lastOffset = offset;
      }
    }
  }

  @Nullable
  Instrument getInstrument(final int index) {
    if(index >= this.instruments.length || index < 0) {
      return null;
    }

    return this.instruments[index];
  }

  void changeSampleRate(final SampleRate sampleRate) {
    for(final Instrument instrument : this.instruments) {
      instrument.changeSampleRate(sampleRate);
    }
  }
}
