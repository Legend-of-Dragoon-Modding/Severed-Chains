package legend.game.types;

import legend.game.unpacker.FileData;

public class AnmSpriteGroup {
  public final int n_sprite_00;
  public final AnmSpriteMetrics14[] metrics_04;

  public AnmSpriteGroup(final FileData data) {
    this.n_sprite_00 = data.readInt(0x0);
    this.metrics_04 = new AnmSpriteMetrics14[this.n_sprite_00];

    for(int i = 0; i < this.n_sprite_00; i++) {
      this.metrics_04[i] = new AnmSpriteMetrics14(data.slice(0x4 + i * 0x14));
    }
  }
}
