package legend.core.gte;

public class GsOBJTABLE2 {
  public GsDOBJ2[] top;
  public int nobj;
  public int maxobj;

  public void set(final GsOBJTABLE2 other) {
    this.top = other.top;
    this.nobj = other.nobj;
    this.maxobj = other.maxobj;
  }
}
