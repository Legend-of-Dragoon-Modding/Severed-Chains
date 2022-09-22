package legend.core.memory;

import javax.annotation.Nullable;
import java.lang.reflect.Method;

public record MethodBinding(Method method, @Nullable Object instance, boolean ignoreExtraParams) {
}
