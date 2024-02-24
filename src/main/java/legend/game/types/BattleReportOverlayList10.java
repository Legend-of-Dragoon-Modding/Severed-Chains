package legend.game.types;

import java.util.Arrays;

public class BattleReportOverlayList10 {
//  public int _00;
//  public int _01;
//  public int _02;
  public int ticksRemaining_03;
  public final BattleReportOverlay0e[] overlays_04 = new BattleReportOverlay0e[8];
  public BattleReportOverlayList10 next_08;
  public BattleReportOverlayList10 prev_0c;

  public BattleReportOverlayList10() {
    Arrays.setAll(this.overlays_04, i -> new BattleReportOverlay0e());
  }
}
