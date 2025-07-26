package legend.game.unpacker;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Queue;

public class Transformations {
  private static final Logger LOGGER = LogManager.getFormatterLogger(Transformations.class);

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
      final String fullPath = current.fullPath.isEmpty() ? "" : current.fullPath + '/';

      // Java complains about non-final current in lambda
      final PathNode currentRef = current;
      current = current.children.compute(pathSegment, (k, existing) -> {
        if(existing != null && !finalSegment) {
          return existing;
        }

//        LOGGER.info("Inserting %s", fullPath + pathSegment);
        return new PathNode(fullPath + pathSegment, pathSegment, finalSegment ? data : null, currentRef);
      });

      previousSlash = slash;
    }

    return current;
  }
}
