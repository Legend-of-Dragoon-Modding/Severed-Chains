package legend.game.unpacker.scripts;

public class ScriptPatch {
  public final String sourceFile;
  public final String patchFile;

  public ScriptPatch(final String sourceFile, final String patchFile) {
    this.sourceFile = sourceFile;
    this.patchFile = patchFile;
  }
}
