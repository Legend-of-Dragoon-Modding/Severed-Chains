package legend.core.audio.assets;

import it.unimi.dsi.fastutil.ints.Int2ObjectArrayMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import legend.game.unpacker.FileData;
import legend.game.unpacker.Unpacker;
import legend.game.unpacker.UnpackerException;

import java.util.List;

public final class SoundFactory {
  public static SequencedAudio backgroundMusic(final int fileIndex) {
    //TODO use Scus94491BpeSegment#loadDrgnDir
    final List<FileData> files = Unpacker.loadDirectory("SECT/DRGN0.BIN/" + fileIndex);

    final SshdParser sshdParser;
    final Sssq sssq;
    //TODO SSsq;
    switch(files.size()) {
      case 4 -> {
        sshdParser = new SshdParser(files.get(2));

        sssq = new Sssq(files.get(1), sshdParser.createSoundFont(files.get(3)));
      }
      case 5 -> {
        final int soundBankCount = files.get(1).readUShort(0);
        final FileData[] soundBanks = new FileData[soundBankCount];

        soundBanks[0] = files.get(4);

        int totalSoundBankSize = soundBanks[0].size();
        for(int i = 1; i < soundBankCount; i++) {
          soundBanks[i] = Unpacker.loadFile("SECT/DRGN0.BIN/" + (fileIndex + i));
          totalSoundBankSize += soundBanks[i].size();
        }

        final byte[] allSoundBanks = new byte[totalSoundBankSize];
        int offset = 0;

        for(final FileData soundBank : soundBanks) {
          soundBank.copyFrom(0, allSoundBanks, offset, soundBank.size());
          offset += soundBank.size();
        }

        sshdParser = new SshdParser(files.get(3));

        sssq = new Sssq(files.get(2), sshdParser.createSoundFont(new FileData(allSoundBanks)));
      }
      default -> throw new UnpackerException("Uknwonw Sequenced Audio type. File count: " + files.size());
    }

    return new SequencedAudio(sssq, sshdParser.getBreathControls(), sshdParser.getVelocityRamp());
  }


  private static class SshdParser {
    /**
     * <ul>
     *   <li>Index 0 - Instruments</li>
     *   <li>Index 1 - Velocity Ramp</li>
     *   <li>Index 2 - Breath Control wave</li>
     *   <li>Index 3 - Multi-Sequence</li>
     *   <li>Index 4 - Channels + Instruments (SFX)</li>
     * </ul>
     */
    private final Int2ObjectMap<FileData> subfiles = new Int2ObjectArrayMap<>();

    SshdParser(final FileData sshdData) {
      if(sshdData.readInt(12) != 0x64685353) {
        throw new UnpackerException("Not a SShd file!");
      }

      int lastOffset = sshdData.size();
      for(int i = 23;  i >= 0; i--) {
        final int offset = sshdData.readInt(16 + i * 4);

        if(offset != - 1) {
          this.subfiles.put(i, sshdData.slice(offset, lastOffset - offset));
          lastOffset = offset;
        }
      }
    }

    private SoundFont createSoundFont(final FileData data) {
      if(this.subfiles.containsKey(0)) {
        return new SoundFont(this.subfiles.get(0), new SoundBank(data));
      }

      return new SoundFont(this.subfiles.get(4).slice(0x190), new SoundBank(data));
    }

    private byte[][] getBreathControls() {
      if(!this.subfiles.containsKey(2)) {
        return new byte[0][];
      }

      final FileData data = this.subfiles.get(2);

      final int upperBound = data.readUShort(0);
      final byte[][] breathControls = new byte[upperBound + 1][];

      int nextOffset = data.size();
      for(int i = upperBound; i >= 0; i--) {
        final int startOffset = data.readUShort(2 + i * 2);
        breathControls[i] = data.slice(startOffset, nextOffset - startOffset).getBytes();

        nextOffset = startOffset;
      }

      return breathControls;
    }

    private byte[] getVelocityRamp() {
      final FileData data = this.subfiles.get(1);
      return data.slice(2, data.size() - 2).getBytes();
    }
  }
}
