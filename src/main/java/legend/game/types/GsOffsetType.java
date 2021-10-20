package legend.game.types;

public enum GsOffsetType {
  GsOFSGTE(0),
  GsOFSGPU(4),
  ;

  public final int value;

  GsOffsetType(final int value) {
    this.value = value;
  }

  public int getValue() {
    return this.value;
  }

  public static GsOffsetType fromValue(final int value) {
    for(final GsOffsetType type : GsOffsetType.values()) {
      if(type.value == value) {
        return type;
      }
    }

    throw new IllegalArgumentException("There is no type with value " + value);
  }
}
