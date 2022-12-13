package legend.core.cdrom;

public class FileLoadingInfo {
  public final CdlLOC pos = new CdlLOC();
  public int size;
  public String name;
  /**
   * 0 - transfer to transferDest
   * 1 - compressed/allocate on linked list head
   * 2 - allocate on linked list tail
   * 3 - allocate on linked list tail (does something weird)
   * 4 - allocate on linked list head
   * 5 - allocate on linked list head (does something weird)
   */
  public int type;
}
