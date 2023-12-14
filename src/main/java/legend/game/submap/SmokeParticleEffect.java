package legend.game.submap;

import legend.core.gte.MV;
import legend.core.opengl.MeshObj;

public class SmokeParticleEffect {
  public SmokeParticleInstance3c[] particles;
  public int firstEmptyIndex;

  public MeshObj particle;
  public final MV transforms = new MV();

  public void deallocate() {
    this.particles = null;
    this.firstEmptyIndex = 0;
    this.transforms.identity();

    if(this.particle != null) {
      this.particle.delete();
      this.particle = null;
    }
  }
}
