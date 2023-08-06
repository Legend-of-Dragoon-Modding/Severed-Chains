package legend.core.memory.types;

import org.joml.Vector3f;

@FunctionalInterface
public interface ComponentFunction {
  float apply(int component, int scriptIndex, Vector3f point);
}
