package legend.core.audio;

public interface MidiInstrumentLayer {
  int getRootKey();
  double getLockedVolume();
  double getVolume();
  int getPitchBendMultiplier();
  int getPan();
  int getCents();
  boolean isLockedPanAndVolume();
  boolean isPitchBendMultiplierFromInstrument();
  boolean canUseModulation();
  int getFlags();
  int get_0e();

  MidiSoundFontEntry getSoundFontEntry();
  AdsrEnvelope getAdsrEnvelope();
}
