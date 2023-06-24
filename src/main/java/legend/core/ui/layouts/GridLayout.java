package legend.core.ui.layouts;

import legend.core.ui.Control;

public class GridLayout implements Layout {
  private int rowCount = 1;
  private int columnCount = 1;
  private LayoutDirection layoutDirection = LayoutDirection.HORIZONTAL;

  public int getRowCount() {
    return this.rowCount;
  }

  public void setRowCount(final int rowCount) {
    this.rowCount = rowCount;
  }

  public int getColumnCount() {
    return this.columnCount;
  }

  public void setColumnCount(final int columnCount) {
    this.columnCount = columnCount;
  }

  public LayoutDirection getLayoutDirection() {
    return this.layoutDirection;
  }

  public void setLayoutDirection(final LayoutDirection layoutDirection) {
    this.layoutDirection = layoutDirection;
  }

  @Override
  public void updateLayout(final int parentWidth, final int parentHeight, final int index, final Control control) {
    final int cellWidth = parentWidth / this.columnCount;
    final int cellHeight = parentHeight / this.rowCount;

    control.setWidth(cellWidth);
    control.setHeight(cellHeight);
    control.setX(this.layoutDirection.calculateX(index, this.rowCount, this.columnCount));
    control.setY(this.layoutDirection.calculateY(index, this.rowCount, this.columnCount));
  }

  public enum LayoutDirection {
    HORIZONTAL((index, rowCount, columnCount) -> index % columnCount, (index, rowCount, columnCount) -> index / columnCount),
    VERTICAL((index, rowCount, columnCount) -> index % rowCount, (index, rowCount, columnCount) -> index / rowCount),
    ;

    private final PositionCalculation calculateX;
    private final PositionCalculation calculateY;

    LayoutDirection(final PositionCalculation calculateX, final PositionCalculation calculateY) {
      this.calculateX = calculateX;
      this.calculateY = calculateY;
    }

    public int calculateX(final int index, final int rowCount, final int columnCount) {
      return this.calculateX.calculate(index, rowCount, columnCount);
    }

    public int calculateY(final int index, final int rowCount, final int columnCount) {
      return this.calculateY.calculate(index, rowCount, columnCount);
    }

    @FunctionalInterface
    public interface PositionCalculation {
      int calculate(final int index, final int rowCount, final int columnCount);
    }
  }
}
