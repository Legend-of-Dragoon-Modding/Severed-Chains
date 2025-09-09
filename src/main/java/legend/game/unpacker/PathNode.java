package legend.game.unpacker;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class PathNode {
  public final String fullPath;
  public final String pathSegment;
  public final FileData data;
  @Nullable
  public final PathNode parent;
  public final Map<String, PathNode> children = new ConcurrentHashMap<>();
  public final Set<String> flags = new HashSet<>();

  public PathNode(final String fullPath, final String pathSegment, final FileData data, @Nullable final PathNode parent) {
    this.fullPath = fullPath;
    this.pathSegment = pathSegment;
    this.data = data;
    this.parent = parent;
  }

  public PathNode addChild(final PathNode node) {
    this.children.put(node.pathSegment, node);
    return node;
  }

  public void flatten(final Collection<PathNode> out) {
    for(final PathNode child : this.children.values()) {
      if(child.data != null) {
        out.add(child);
      } else {
        child.flatten(out);
      }
    }
  }

  @Override
  public String toString() {
    return this.pathSegment;
  }
}
