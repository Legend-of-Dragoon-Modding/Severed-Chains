package legend.core.opengl;

import legend.game.types.Translucency;

import static org.lwjgl.opengl.GL11C.GL_BLEND;
import static org.lwjgl.opengl.GL11C.GL_CULL_FACE;
import static org.lwjgl.opengl.GL11C.GL_DEPTH_TEST;
import static org.lwjgl.opengl.GL11C.GL_ONE;
import static org.lwjgl.opengl.GL11C.GL_ONE_MINUS_SRC_ALPHA;
import static org.lwjgl.opengl.GL11C.GL_SRC_ALPHA;
import static org.lwjgl.opengl.GL11C.glBlendFunc;
import static org.lwjgl.opengl.GL11C.glDisable;
import static org.lwjgl.opengl.GL11C.glEnable;

public class Obj {
  private final Shader shader;
  private final Mesh[] meshes;
  private final Translucency[] translucencies;

  public Obj(final Shader shader, final Mesh[] meshes, final Translucency[] translucencies) {
    this.shader = shader;
    this.meshes = meshes;
    this.translucencies = translucencies;
  }

  public void render() {
    this.shader.use();

    for(int i = 0; i < this.meshes.length; i++) {
      final Translucency translucency = this.translucencies[i];

      if(translucency == null) {
        glEnable(GL_DEPTH_TEST);
        glEnable(GL_CULL_FACE);
        glDisable(GL_BLEND);
      } else {
        glDisable(GL_DEPTH_TEST);
        glDisable(GL_CULL_FACE);
        glEnable(GL_BLEND);

//        glBlendEquation(GL_FUNC_SUBTRACT);

        switch(translucency) {
          case HALF_B_PLUS_HALF_F ->
            glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

          case B_PLUS_F ->
            glBlendFunc(GL_ONE, GL_ONE);
        }
      }

      this.meshes[i].draw();
    }
  }

  public void delete() {
    for(final Mesh mesh : this.meshes) {
      mesh.delete();
    }
  }
}
