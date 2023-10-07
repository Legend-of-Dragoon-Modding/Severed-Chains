package legend.core.opengl;

public class Obj {
  private final Shader shader;
  private final Mesh[] meshes;
  private final Runnable[] shaderSetup;

  public Obj(final Shader shader, final Mesh[] meshes, final Runnable[] shaderSetup) {
    this.shader = shader;
    this.meshes = meshes;
    this.shaderSetup = shaderSetup;
  }

  public void render() {
    this.shader.use();

    for(int i = 0; i < this.meshes.length; i++) {
      this.shaderSetup[i].run();
      this.meshes[i].draw();
    }
  }

  public void delete() {
    for(final Mesh mesh : this.meshes) {
      mesh.delete();
    }
  }
}
