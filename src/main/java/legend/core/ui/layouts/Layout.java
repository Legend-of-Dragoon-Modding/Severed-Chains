package legend.core.ui.layouts;

import legend.core.ui.Control;

@FunctionalInterface
public interface Layout {
  void updateLayout(final int parentWidth, final int parentHeight, final int index, final Control control);
}
