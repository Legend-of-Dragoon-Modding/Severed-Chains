package legend.game.fmv;

public class SectorHeader {
  public static final int HEADER_SIZE = 24;

  private final byte[] data;

  public final Submode submode = new Submode();
  public final Coding coding = new Coding();

  public SectorHeader(final byte[] data) {
    this.data = data;
  }

  public byte getMode() {
    return this.data[15];
  }

  public byte getChannel() {
    return this.data[17];
  }

  public class Submode {
    public boolean isEof() {
      return (SectorHeader.this.data[18] & 0x80) != 0;
    }

    public boolean isRealtime() {
      return (SectorHeader.this.data[18] & 0x40) != 0;
    }

    public FORM getForm() {
      return FORM.of(SectorHeader.this.data[18] >>> 5 & 1);
    }

    public boolean isTrigger() {
      return (SectorHeader.this.data[18] & 0x10) != 0;
    }

    public TYPE getType() {
      return switch(SectorHeader.this.data[18] & 0xe) {
        case 2 -> TYPE.VIDEO;
        case 4 -> TYPE.AUDIO;
        case 8 -> TYPE.DATA;
        default -> throw new IllegalArgumentException("Invalid type");
      };
    }

    public boolean isEndAudio() {
      return (SectorHeader.this.data[18] & 0x1) != 0;
    }
  }

  public class Coding {
    public int getBitsPerSample() {
      return (SectorHeader.this.data[19] & 0x10) != 0 ? 8 : 4;
    }

    public int getSampleRate() {
      return (SectorHeader.this.data[19] & 0x4) != 0 ? 18900 : 37800;
    }

    public boolean isStereo() {
      return (SectorHeader.this.data[19] & 0x1) != 0;
    }
  }

  public enum FORM {
    FORM1,
    FORM2,
    ;

    public static FORM of(final int form) {
      return FORM.values()[form];
    }
  }

  public enum TYPE {
    VIDEO,
    AUDIO,
    DATA,
  }
}
