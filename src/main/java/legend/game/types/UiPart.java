package legend.game.types;

import legend.game.unpacker.FileData;

import java.util.Arrays;

/**
 * @param _02 ubyte
 */
public record UiPart(RenderableMetrics14[] metrics_00, int _02) {
  public static UiPart fromFile(final FileData data, final FileData partData, final FileData entryIndicesData) {
    final int _02 = partData.readUByte(0x2);

    final int metricsIndicesIndex = partData.readUShort(0x0);
    final int metricsOffset = entryIndicesData.readInt(metricsIndicesIndex * 0x4);
    final int metricsCount = data.readInt(metricsOffset);
    final RenderableMetrics14[] metrics_00 = new RenderableMetrics14[metricsCount];
    Arrays.setAll(metrics_00, i -> RenderableMetrics14.fromFile(data.slice(metricsOffset + 0x4 + i * 0x14, 0x14)));

    return new UiPart(metrics_00, _02);
  }
}
