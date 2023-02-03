package legend.game.sound;

import legend.core.opengl.Gui;
import legend.core.opengl.GuiManager;
import org.lwjgl.BufferUtils;
import org.lwjgl.system.MemoryStack;

import java.nio.IntBuffer;

import static legend.game.Scus94491BpeSegment_800c._800c3a40;
import static legend.game.Scus94491BpeSegment_800c.sequenceData_800c4ac8;

public class MusicDebuggerGui extends Gui {
  private final IntBuffer sequenceIndex = BufferUtils.createIntBuffer(1);

  @Override
  protected void draw(final GuiManager manager, final MemoryStack stack) {
    this.simpleWindow(manager, stack, "Music Debugger", 0, 0, 728, 600, () -> {
      this.row(manager, 30.0f, 2, row -> {
        row.nextColumn(0.25f);
        this.property(manager, "Sequence", 0, 23, 1, 1, this.sequenceIndex);
      });

      final SequenceData124 sequenceData = sequenceData_800c4ac8[this.sequenceIndex.get(0)];

      if(sequenceData._028 == 1 || sequenceData._02a == 1) {
        this.row(manager, 24.0f, 5, row -> {
          row.nextColumn(0.1f);
          this.label(manager, "Offset:");
          row.nextColumn(0.1f);
          this.label(manager, String.valueOf(sequenceData.sssqReader_010.offset()), TextAlign.RIGHT);

          row.nextColumn(0.1f);
          this.label(manager, "");

          row.nextColumn(0.1f);
          this.label(manager, "Delta:");
          row.nextColumn(0.1f);
          this.label(manager, String.valueOf(sequenceData.deltaTime_118), TextAlign.RIGHT);
        });

        this.row(manager, 24.0f, 2, row -> {
          row.nextColumn(0.15f);
          this.label(manager, "Command:");
          row.nextColumn(0.85f);
          this.label(manager, "%02x %02x %02x %02x".formatted(sequenceData.command_000, sequenceData.param0_002, sequenceData.param1_003, sequenceData.param2_005));
        });

        for(int sequenceChannel = 0; sequenceChannel < 16; sequenceChannel++) {
          for(int playingNoteIndex = 0; playingNoteIndex < 24; playingNoteIndex++) {
            final SpuStruct66 playingNote = _800c3a40[playingNoteIndex];

            if(playingNote.sequenceData_06 == sequenceData && playingNote.commandChannel_04 == sequenceChannel) {
              final int finalSequenceChannel = sequenceChannel;
              this.row(manager, 24.0f, 2, row -> {
                row.nextColumn(0.1f);
                this.label(manager, finalSequenceChannel + ": ");

                row.nextColumn(0.1f);
                this.label(manager, this.getNoteName(playingNote.noteNumber_02));

                row.nextColumn(0.25f);
                this.label(manager, "Root: %s\t(%d-%d)".formatted(this.getNoteName(playingNote.rootKey_40), playingNote.minKeyRange_20, playingNote.maxKeyRange_1e));
              });
            }
          }
        }
      }
    });
  }

  private String getNoteName(final int midiNote) {
    final int note = (midiNote - 21) % 12;
    final int octave = (midiNote - 21) / 12;

    final String noteStr = switch(note) {
      case 0 -> "A";
      case 1 -> "A#/Bb";
      case 2 -> "B";
      case 3 -> "C";
      case 4 -> "C#/Db";
      case 5 -> "D";
      case 6 -> "D#/Eb";
      case 7 -> "E";
      case 8 -> "F";
      case 9 -> "F#/Gb";
      case 10 -> "G";
      case 11 -> "G#/Ab";
      default -> "Negative note";
    };

    return noteStr + octave;
  }
}
