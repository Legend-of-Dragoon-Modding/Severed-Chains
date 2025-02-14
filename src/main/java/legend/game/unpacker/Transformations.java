package legend.game.unpacker;

import java.util.Queue;

public class Transformations {
  private final PathNode root;
  private final Queue<PathNode> transformationQueue;
  private int remaining;

  public Transformations(final PathNode root, final Queue<PathNode> transformationQueue) {
    this.root = root;
    this.transformationQueue = transformationQueue;
    this.remaining = transformationQueue.size();
  }

  public PathNode poll() {
    synchronized(this.transformationQueue) {
      return this.transformationQueue.poll();
    }
  }

  public int getRemaining() {
    synchronized(this.transformationQueue) {
      return this.remaining;
    }
  }

  public void decrementRemaining() {
    synchronized(this.transformationQueue) {
      this.remaining--;
    }
  }

  public boolean isEmpty() {
    synchronized(this.transformationQueue) {
      return this.remaining == 0;
    }
  }

  public void addNode(final PathNode node) {
    this.addNode(node.fullPath, node.data);
  }

  public void addNode(final String fullPath, final FileData data) {
    synchronized(this.transformationQueue) {
      this.remaining++;

      final PathNode node = this.insert(fullPath, data);
      this.transformationQueue.add(node);
    }
  }

  public void replaceNode(final PathNode node, final FileData newData) {
    this.addNode(node.fullPath, newData);
  }

  public PathNode addChild(final PathNode parent, final String pathSegment, final FileData data) {
    synchronized(this.transformationQueue) {
      this.remaining++;

      final PathNode node = this.insert(parent.fullPath + '/' + pathSegment, data);
      this.transformationQueue.add(node);
      return node;
    }
  }

  private PathNode insert(String path, final FileData data) {
    path += '/'; // Add a final slash to make the below loop easier

    PathNode current = this.root;
    int slash;
    int previousSlash = 0;
    while((slash = path.indexOf('/', previousSlash + 1)) != -1) {
      final String pathSegment = path.substring(previousSlash == 0 ? 0 : previousSlash + 1, slash);
      final boolean finalSegment = slash == path.length() - 1; // Is this the final segment in the path?

      if(!current.children.containsKey(pathSegment) || finalSegment) {
        final String fullPath = current.fullPath.isEmpty() ? "" : current.fullPath + '/';
        current = current.addChild(new PathNode(fullPath + pathSegment, pathSegment, finalSegment ? data : null, current));
      } else {
        current = current.children.get(pathSegment);
      }

      previousSlash = slash;
    }

    return current;
  }
}
