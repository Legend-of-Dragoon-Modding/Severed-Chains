package legend.core.opengl;

public class TmdObjLoaderMeshes {
  public TmdObjLoaderMesh opaque;
  public TmdObjLoaderMesh untexturedTranslucent;
  public TmdObjLoaderMesh[] translucent;

  public int meshCount() {
    int meshCount = this.translucent.length;

    if(this.opaque != null) {
      meshCount++;
    }

    if(this.untexturedTranslucent != null) {
      meshCount++;
    }

    return meshCount;
  }
}
