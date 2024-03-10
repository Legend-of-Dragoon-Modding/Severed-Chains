package legend.core.audio.opus;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public final class OpusFile {
  private final List<OggPage> pages = new ArrayList<>();
  private final int serialNumber;
  private final OpusTags opusTags = new OpusTags();

  public OpusFile(final byte channelCount, final short preSkip, final int originalSampleRate) {
    final Random random = new Random();
    this.serialNumber = random.nextInt();

    final OggPage headerPage = new OggPage((byte)0x02, 0, this.serialNumber, 0, 50);
    final OpusHead opusHead = new OpusHead(channelCount, preSkip, originalSampleRate, 0);
    headerPage.addDataSegment(opusHead.toBytes());

    this.pages.add(headerPage);

    final OggPage commentsPage = new OggPage((byte) 0x00, 0, this.serialNumber, 1, 50);

    this.pages.add(commentsPage);

    this.pages.add(new OggPage((byte)0x0, 0, this.serialNumber, 2, 50));
  }

  public void addOpusSegment(final byte[] data) {
    if(this.pages.get(this.pages.size() - 1).isFull()) {
      this.pages.add(new OggPage((byte)0x0, this.pages.get(this.pages.size() - 1).getGranulePosition(), this.serialNumber, this.pages.size(), 50));
    }

   this.pages.get(this.pages.size() - 1).addAudioSegment(data);
  }

  public void addComment(final String comment) {
    this.opusTags.addComment(comment);
  }

  public byte[] toBytes() {
    this.pages.get(1).addDataSegment(this.opusTags.toBytes());
    this.pages.get(this.pages.size() - 1).setEndOfStream();

    final ByteArrayOutputStream bos = new ByteArrayOutputStream();

    for(final OggPage page : this.pages) {
      bos.writeBytes(page.toBytes());
    }

    return bos.toByteArray();
  }
}
