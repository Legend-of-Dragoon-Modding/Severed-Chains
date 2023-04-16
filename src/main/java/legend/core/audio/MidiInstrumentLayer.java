package legend.core.audio;

public interface MidiInstrumentLayer {
  int getRootKey();
  double getVolume();
  int getPitchBendMultiplier();
  int getPan();
  int getCents();
  MidiSoundFontEntry getSoundFontEntry();
  AdsrEnvelope getAdsrEnvelope();
}
