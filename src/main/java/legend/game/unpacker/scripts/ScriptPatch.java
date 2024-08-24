package legend.game.unpacker.scripts;

public class ScriptPatch {
  public final PatchType type;
  public final String sourceFile;
  public final String patchFile;

  public ScriptPatch(final PatchType type, final String sourceFile, final String patchFile) {
    this.type = type;
    this.sourceFile = sourceFile;
    this.patchFile = patchFile;
  }
}
