package legend.game.sound;

/** A note from a sequence that is currently playing */
public class PlayingNote66 {
  public boolean used_00;
  public int noteNumber_02;
  public int sequenceChannel_04;
  public SequenceData124 sequenceData_06;
  public boolean finished_08;
  public int _0a;
  public int _0c;
  public int instrumentIndex_0e;
  public int breathControlListIndex_10;
  public int _12;
  public boolean modulationEnabled_14;
  public int modulation_16;
  public int _18;

  public int _1c;
  public int maxKeyRange_1e;
  public int minKeyRange_20;
  public PlayableSound0c playableSound_22;
  public int patchIndex_24;
  public int sequenceIndex_26;
  public int channelVolume_28;
  public int instrumentVolume_2a;
  public int velocityVolume_2c;
  public int instrumentLayerVolume_2e;
  public int[] volumeLeftRight_30 = new int[2];
  public int volume_34;
  public int cents_36;
  /** 0x40 is normal */
  public int pitchBend_38;
  public int pitchBendMultiplier_3a;
  public int breath_3c;
  public int nextCommand_3e;
  public int rootKey_40;
  public boolean pitchShifted_42;
  public boolean portamentoChanging_44;
  public boolean volumeChanging_46;
  public boolean panChanging_48;
  public int _4a;
  public int pan_4c;
  /** Portamento Note, Default value is 120 */
  public int portamentoNote_4e;
  public int newVolume_50;
  public int previousVolume_52;
  public int remainingVolumeChangeTime_54;
  public int totalVolumeChangeTime_56;
  public int newPan_58;
  /** Pan value before last pan command */
  public int previousPan_5a;
  public int remainingPanTime_5c;
  public int totalPanTime_5e;
  public int newPortamento_60;
  public int portamentoTimeRemaining_62;
  public int portamentoTimeTotal_64;

  public void clear() {
    this.used_00 = false;
    this.noteNumber_02 = 0;
    this.sequenceChannel_04 = 0;
    this.sequenceData_06 = null;
    this.finished_08 = false;
    this._0a = 0;
    this._0c = 0;
    this.instrumentIndex_0e = 0;
    this.breathControlListIndex_10 = 0;
    this._12 = 0;
    this.modulationEnabled_14 = false;
    this.modulation_16 = 0;
    this._18 = 0;
    this._1c = 0;
    this.maxKeyRange_1e = 0;
    this.minKeyRange_20 = 0;
    this.playableSound_22 = null;
    this.patchIndex_24 = 0;
    this.sequenceIndex_26 = 0;
    this.channelVolume_28 = 0;
    this.instrumentVolume_2a = 0;
    this.velocityVolume_2c = 0;
    this.instrumentLayerVolume_2e = 0;
    this.volumeLeftRight_30[0] = 0;
    this.volumeLeftRight_30[1] = 0;
    this.volume_34 = 0;
    this.cents_36 = 0;
    this.pitchBend_38 = 0;
    this.pitchBendMultiplier_3a = 0;
    this.breath_3c = 0;
    this.nextCommand_3e = 0;
    this.rootKey_40 = 0;
    this.pitchShifted_42 = false;
    this.portamentoChanging_44 = false;
    this.volumeChanging_46 = false;
    this.panChanging_48 = false;
    this._4a = 0;
    this.pan_4c = 0;
    this.portamentoNote_4e = 0;
    this.newVolume_50 = 0;
    this.previousVolume_52 = 0;
    this.remainingVolumeChangeTime_54 = 0;
    this.totalVolumeChangeTime_56 = 0;
    this.newPan_58 = 0;
    this.previousPan_5a = 0;
    this.remainingPanTime_5c = 0;
    this.totalPanTime_5e = 0;
    this.newPortamento_60 = 0;
    this.portamentoTimeRemaining_62 = 0;
    this.portamentoTimeTotal_64 = 0;
  }
}
