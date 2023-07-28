package legend.core.memory.types;

@FunctionalInterface
public interface ComponentFunction {
  float apply(int component, int scriptIndex, float x, float y, float z);
}
