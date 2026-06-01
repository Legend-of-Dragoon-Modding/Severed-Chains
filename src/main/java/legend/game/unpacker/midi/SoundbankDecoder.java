package legend.game.unpacker.midi;

import legend.core.DebugHelper;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class SoundbankDecoder {
  public static void main(final String[] args) throws IOException {
    new SoundbankDecoder(Files.readAllBytes(Path.of("./files/SECT/DRGN0.BIN/5820/3"))).decode();
  }

  private static final int[] positiveXaAdpcmTable = {0, 60, 115, 98, 122};
  private static final int[] negativeXaAdpcmTable = {0, 0, -52, -55, -60};

  private final byte[] data;
  private int index;

  public SoundbankDecoder(final byte[] data) {
    this.data = data;
  }

  public void decode() {
    final byte[] decodedSamples = new byte[56];
    int old = 0;
    int older = 0;

    this.advance();

    final SourceDataLine sound;
    try {
      sound = AudioSystem.getSourceDataLine(new AudioFormat(18900, 16, 1, true, false));
      sound.open();
      sound.start();
    } catch(final LineUnavailableException | IllegalArgumentException e) {
      throw new RuntimeException(e);
    }

    while(!this.isStartOfSample()) {
      System.out.printf("Shift %d\tFilter %d\tEnd %b\tRepeat %b\tStart %b%n", this.getShift(), this.getFilter(), this.isLoopEnd(), this.isLoopRepeat(), this.isLoopStart());

      final int filter = this.getFilter();
      final int f0 = positiveXaAdpcmTable[filter];
      final int f1 = negativeXaAdpcmTable[filter];

      int position = 2; //skip shift and flags
      int nibble = 1;
      for(int i = 0; i < 28; i++) {
        nibble = nibble + 1 & 0x1;

        final int t = this.signed4bit(this.data[this.index + position] >> nibble * 4 & 0x0f);
        final int s = (t << this.getShift()) + (old * f0 + older * f1 + 32) / 64;
        final short sample = (short)Math.clamp(s, -0x8000, 0x7fff);

        decodedSamples[i * 2] = (byte)(sample & 0xff);
        decodedSamples[i * 2 + 1] = (byte)(sample >> 8 & 0xff);

        System.out.printf("%04x", sample);

        older = old;
        old = sample;

        position += nibble;
      }

      System.out.println();
      sound.write(decodedSamples, 0, decodedSamples.length);
//      DebugHelper.sleep(1);
      this.advance();
    }

    DebugHelper.sleep(5000);
    sound.close();
  }

  public void advance() {
    this.index += 0x10;
  }

  public boolean isStartOfSample() {
    for(int i = this.index; i < this.index + 0x10; i++) {
      if(this.data[i] != 0) {
        return false;
      }
    }

    return true;
  }

  public int getShift() {
    return SoundbankDecoder.this.data[this.index] & 0xf;
  }

  public int getFilter() {
    return SoundbankDecoder.this.data[this.index] >> 4 & 0x3;
  }

  public boolean isLoopEnd() {
    return (SoundbankDecoder.this.data[this.index + 1] & 0x1) != 0;
  }

  public boolean isLoopRepeat() {
    return (SoundbankDecoder.this.data[this.index + 1] & 0x2) != 0;
  }

  public boolean isLoopStart() {
    return (SoundbankDecoder.this.data[this.index + 1] & 0x4) != 0;
  }

  private int signed4bit(final int value) {
    return value << 28 >> 28;
  }
}
