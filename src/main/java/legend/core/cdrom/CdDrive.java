package legend.core.cdrom;

import legend.core.InterruptType;
import legend.core.IoHelper;
import legend.core.MathHelper;
import legend.core.spu.XaAdpcm;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.file.Path;
import java.nio.file.Paths;

import static legend.core.Hardware.INTERRUPTS;
import static legend.core.Hardware.MEMORY;
import static legend.core.Hardware.SPU;

public class CdDrive {
  private static final Logger LOGGER = LogManager.getFormatterLogger(CdDrive.class);
  private static final Marker DRIVE_MARKER = MarkerManager.getMarker("CDROM_DRIVE");
  private static final Marker COMMAND_MARKER = MarkerManager.getMarker("CDROM_COMMAND").setParents(DRIVE_MARKER);
  private static final Marker DMA_MARKER = MarkerManager.getMarker("CDROM_DMA").setParents(DRIVE_MARKER);

  private legend.core.cdrom.IsoReader diskSync;
  private int diskIndex;

  /**
   * 0 = mute, 80 = normal, ff = double
   */
  private int audioCdLeftToSpuLeft = 0x80;
  /**
   * 0 = mute, 80 = normal, ff = double
   */
  private int audioCdLeftToSpuRight;
  /**
   * 0 = mute, 80 = normal, ff = double
   */
  private int audioCdRightToSpuRight = 0x80;
  /**
   * 0 = mute, 80 = normal, ff = double
   */
  private int audioCdRightToSpuLeft;

  public CdDrive() {
    this.loadDisk(1);
  }

  public void loadDisk(final int index) {
    LOGGER.info(DRIVE_MARKER, "Loading disk %d", index);

    final Path path = Paths.get("isos/%d.iso".formatted(index));

    try {
      this.diskSync = new legend.core.cdrom.IsoReader(path);
    } catch(final FileNotFoundException e) {
      throw new RuntimeException("Couldn't find ISO %d. Did you remember to put your ISOs in the /isos/ folder?".formatted(index));
    } catch(final IOException e) {
      throw new RuntimeException("Failed to load disk " + index, e);
    }

    this.diskIndex = index;
  }

  public void readFromDisk(final CdlLOC pos, final int sectorCount, final long dest) {
    LOGGER.info(DMA_MARKER, "[CDROM] Performing direct read from disk: %d sectors from %s to %08x", sectorCount, pos, dest);

    final CdlLOC loc = new CdlLOC().set(pos);

    for(int i = 0; i < sectorCount; i++) {
      final byte[] data = new byte[0x800];

      try {
        this.diskSync.seekSector(loc);
        this.diskSync.advance(0xc);
        this.diskSync.read(data);
      } catch(final IOException e) {
        throw new RuntimeException(e);
      }

      MEMORY.setBytes(dest + i * data.length, data);

      loc.advance(1);

      INTERRUPTS.set(InterruptType.CDROM);
    }
  }

  public void playXaAudio(final CdlLOC locIn, final int filterFile, final int filterChannel, final Runnable onCompletion) {
    final CdlLOC loc = new CdlLOC().set(locIn);

    final Runnable player = () -> {
      LOGGER.info("[ASYNC XA] Uploading XA file %d channel %d to SPU", filterFile, filterChannel);

      final legend.core.cdrom.IsoReader reader;
      try {
        reader = new IsoReader(Paths.get("isos/1.iso"));
        reader.seekSectorRaw(loc);
      } catch(final IOException e) {
        LOGGER.error("[ASYNC XA] Failed to open ISO");
        return;
      }

      final SectorSubHeader sectorSubHeader = new SectorSubHeader();
      while(true) {
        final byte[] rawSector = new byte[2352];
        try {
          reader.read(rawSector);
        } catch(final IOException e) {
          throw new RuntimeException(e);
        }

        loc.advance(1);

        sectorSubHeader.file = rawSector[16];
        sectorSubHeader.channel = rawSector[17];
        sectorSubHeader.subMode = rawSector[18];
        sectorSubHeader.codingInfo = rawSector[19];

        if(sectorSubHeader.isForm2()) {
          if(sectorSubHeader.isEndOfFile()) {
            break;
          }

          if(sectorSubHeader.isRealTime() && sectorSubHeader.isAudio()) {
            if(filterFile != sectorSubHeader.file || filterChannel != sectorSubHeader.channel) {
              continue;
            }

            LOGGER.info(DRIVE_MARKER, "[ASYNC XA] Sending sector %d to SPU", loc.pack());

            final byte[] decodedXaAdpcm = XaAdpcm.decode(rawSector, sectorSubHeader.codingInfo);
            this.applyVolume(decodedXaAdpcm);
            SPU.pushCdBufferSamples(decodedXaAdpcm);
          }
        }
      }

      LOGGER.info("[ASYNC XA] XA upload complete");

      onCompletion.run();
    };

    new Thread(player).start();
  }

  public void setAudioMix(final int cdLeftToSpuLeft, final int cdLeftToSpuRight, final int cdRightToSpuRight, final int cdRightToSpuLeft) {
    LOGGER.info(COMMAND_MARKER, "[CDROM] Committing CDROM audio volume settings {LL: %x, LR: %x, RL: %x, RR: %x}", this.audioCdLeftToSpuLeft, this.audioCdLeftToSpuRight, this.audioCdRightToSpuLeft, this.audioCdRightToSpuRight);
    this.audioCdLeftToSpuLeft = cdLeftToSpuLeft;
    this.audioCdLeftToSpuRight = cdLeftToSpuRight;
    this.audioCdRightToSpuLeft = cdRightToSpuLeft;
    this.audioCdRightToSpuRight = cdRightToSpuRight;
  }

  private void applyVolume(final byte[] rawSector) {
    for(int i = 0; i < rawSector.length; i += 4) {
      final short l = (short)((rawSector[i + 1] & 0xff) << 8 | rawSector[i    ] & 0xff);
      final short r = (short)((rawSector[i + 3] & 0xff) << 8 | rawSector[i + 2] & 0xff);

      final int volumeL = MathHelper.clamp((l * this.audioCdLeftToSpuLeft  >> 7) + (r * this.audioCdRightToSpuLeft  >> 7), -0x8000, 0x7FFF);
      final int volumeR = MathHelper.clamp((l * this.audioCdLeftToSpuRight >> 7) + (r * this.audioCdRightToSpuRight >> 7), -0x8000, 0x7FFF);

      rawSector[i    ] = (byte)(volumeL       & 0xff);
      rawSector[i + 1] = (byte)(volumeL >>> 8 & 0xff);
      rawSector[i + 2] = (byte)(volumeR       & 0xff);
      rawSector[i + 3] = (byte)(volumeR >>> 8 & 0xff);
    }
  }

  public void dump(final ByteBuffer stream) throws IOException {
    IoHelper.write(stream, (byte)this.diskIndex);
    IoHelper.write(stream, this.diskSync.getPos());
  }

  public void load(final ByteBuffer stream, final int version) throws IOException {
    if(version >= 1) {
      this.diskIndex = IoHelper.readByte(stream);
      this.loadDisk(this.diskIndex);
      this.diskSync.setPos(IoHelper.readLong(stream));
    }
  }

  private static class SectorSubHeader {
    public byte file;
    public byte channel;
    public byte subMode;
    public byte codingInfo;

    public boolean isEndOfRecord() {
      return (this.subMode & 0x1) != 0;
    }

    public boolean isVideo() {
      return (this.subMode & 0x2) != 0;
    }

    public boolean isAudio() {
      return (this.subMode & 0x4) != 0;
    }

    public boolean isData() {
      return (this.subMode & 0x8) != 0;
    }

    public boolean isTrigger() {
      return (this.subMode & 0x10) != 0;
    }

    public boolean isForm2() {
      return (this.subMode & 0x20) != 0;
    }

    public boolean isRealTime() {
      return (this.subMode & 0x40) != 0;
    }

    public boolean isEndOfFile() {
      return (this.subMode & 0x80) != 0;
    }
  }
}
