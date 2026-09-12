package legend.game.unpacker.scripts;

import legend.game.unpacker.Unpacker;
import it.unimi.dsi.fastutil.ints.Int2IntMap;
import it.unimi.dsi.fastutil.ints.Int2IntOpenHashMap;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;
import legend.game.unpacker.Loader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.legendofdragoon.scripting.OpType;
import org.legendofdragoon.scripting.tokens.Op;
import org.legendofdragoon.scripting.tokens.Script;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static legend.core.GameEngine.SCRIPTS;
import static legend.game.unpacker.scripts.ScriptOpMatcherBasic.any;
import static legend.game.unpacker.scripts.ScriptOpMatcherBasic.imm;
import static legend.game.unpacker.scripts.ScriptOpMatcherBasic.var;

public class ScriptSearcher {
  private static final Logger LOGGER = LogManager.getFormatterLogger(ScriptSearcher.class);

  private Path getPatchConfig(final Path referencePath, final IntList branchList, final  Int2IntMap tableLengthList) {
    final ScriptPatcher patcher = new ScriptPatcher(Path.of("./patches"), Unpacker.ROOT, Unpacker.ROOT.resolve("patches/cache"), Unpacker.ROOT.resolve("patches/backups"));
    final ScriptPatchList patchList = patcher.getPatchList();

    final String relativePath = Loader.resolve("").relativize(referencePath).toString();
    final ScriptPatch patch = patchList.getPatchForScript(relativePath);
    final Path actualScriptPath;

    if(patch != null) {
      final Path configPath = patcher.resolvePatchConfigPath(patcher.patchesDir.resolve(patch.patchFile));
      patcher.getPatchConfigs(configPath, branchList, tableLengthList);

      final Path newPath = patcher.backupsDir.resolve(relativePath);
      if(Files.exists(newPath)) {
        actualScriptPath = newPath;
      } else {
        actualScriptPath = referencePath;
      }
    } else {
      actualScriptPath = referencePath;
    }

    return actualScriptPath;
  }

  public boolean compare(final ScriptOpMatcher[] referencePath, final Path comparisonPath) throws IOException {
    final IntList comparisonBranchList = new IntArrayList();
    final Int2IntMap comparisonTableLengthList = new Int2IntOpenHashMap();
    final Path actualComparisonPath = this.getPatchConfig(comparisonPath, comparisonBranchList, comparisonTableLengthList);

    final Script comparison = SCRIPTS.disassemble("Comparison", Files.readAllBytes(actualComparisonPath), comparisonBranchList, comparisonTableLengthList);

    int matches = 0;

    int comparisonStart = 0;
    while((comparisonStart = this.findStartingMatch(comparison, comparisonStart, referencePath[0])) != -1) {
      if(this.compare(referencePath, comparison, comparisonStart)) {
        matches++;
        LOGGER.info("Found match %d at address %#x", matches, comparisonStart * 4);
      }

      comparisonStart++;
    }

    if(matches == 0) {
      LOGGER.info("Found no matches");
      return false;
    }

    return true;
  }

  private int findStartingMatch(final Script comparison, final int start, final ScriptOpMatcher firstMatcher) {
    for(int i = start; i < comparison.entries.length; i++) {
      if(firstMatcher.matches(comparison, i)) {
        return i;
      }
    }

    return -1;
  }

  private boolean compare(final ScriptOpMatcher[] reference, final Script comparison, final int comparisonStart) {
    // Compare each op and if any don't match, continue the outer loop
    for(int referenceIndex = 0, comparisonIndex = comparisonStart; referenceIndex < reference.length; referenceIndex++) {
      if(comparisonIndex >= comparison.entries.length) {
        return false;
      }

      if(!reference[referenceIndex].matches(comparison, comparisonIndex)) {
        return false;
      }

      final Op comparisonOp = (Op)comparison.entries[comparisonIndex];
      comparisonIndex++;

      for(int i = 0; i < comparisonOp.params.length; i++) {
        comparisonIndex += comparisonOp.params[i].type.getWidth(comparisonOp.params[i]);
      }
    }

    return true;
  }

  // DRGN22/33/1
  public final ScriptOpMatcher[] pattern1 = {
    //LABEL_134
    new ScriptOpMatcherBasic(OpType.JMP_CMP, any(), any(), any()),
    new ScriptOpMatcherBasic(OpType.GOSUB, any()),
    //LABEL_135
    new ScriptOpMatcherBasic(OpType.MOV, any(), any()),
    new ScriptOpMatcherBasic(OpType.AND, any(), any()),
    new ScriptOpMatcherBasic(OpType.MOV, any(), var()),
    new ScriptOpMatcherBasic(OpType.OR, any(), var()),
    new ScriptOpMatcherBasic(OpType.INCR, any()),
    new ScriptOpMatcherBasic(OpType.INCR, any()),
    new ScriptOpMatcherBasic(OpType.JMP_CMP, any(), any(), any()),
    new ScriptOpMatcherZeroable(OpType.JMP_CMP, OpType.JMP_CMP_0, any(), any(), any()),
    new ScriptOpMatcherBasic(OpType.MOV, any(), var()),
    new ScriptOpMatcherBasic(OpType.MOV, any(), var()),
    new ScriptOpMatcherBasic(OpType.MOV, any(), var()),
    new ScriptOpMatcherBasic(OpType.GOSUB, any()),
    new ScriptOpMatcherZeroable(OpType.MOV, OpType.MOV_0, any(), any()),
    //LABEL_136
    new ScriptOpMatcherBasic(OpType.MOV, any(), any()),
    new ScriptOpMatcherBasic(OpType.AND, any(), any()),
    new ScriptOpMatcherZeroable(OpType.JMP_CMP, OpType.JMP_CMP_0, any(), any(), any()),
    new ScriptOpMatcherBasic(OpType.MOV, any(), any()),
    new ScriptOpMatcherBasic(OpType.MOD, any(), any()),
    new ScriptOpMatcherBasic(OpType.MOV, any(), var()),
    new ScriptOpMatcherBasic(OpType.INCR, any()),
    //LABEL_137
    new ScriptOpMatcherBasic(OpType.INCR, any()),
    new ScriptOpMatcherBasic(OpType.INCR, any()),
    new ScriptOpMatcherBasic(OpType.JMP_CMP, any(), any(), any()),
    new ScriptOpMatcherBasic(OpType.JMP, any()),
    //LABEL_138
    new ScriptOpMatcherBasic(OpType.GOSUB, any()),
    //LABEL_139
    new ScriptOpMatcherBasic(OpType.MOV, any(), any()),
    new ScriptOpMatcherBasic(OpType.AND, any(), any()),
    new ScriptOpMatcherZeroable(OpType.JMP_CMP, OpType.JMP_CMP_0, any(), any(), any()),
    new ScriptOpMatcherZeroable(OpType.MOV, OpType.MOV_0, any(), any()),
    new ScriptOpMatcherBasic(OpType.JMP_CMP, any(), var(), any()),
    new ScriptOpMatcherBasic(OpType.INCR, any()),
    new ScriptOpMatcherBasic(OpType.JMP_CMP, any(), var(), any()),
    new ScriptOpMatcherBasic(OpType.INCR, any()),
    new ScriptOpMatcherBasic(OpType.JMP_CMP, any(), var(), any()),
    new ScriptOpMatcherBasic(OpType.JMP, any()),
    //LABEL_140
    new ScriptOpMatcherBasic(OpType.MOV, any(), var()),
    //LABEL_141
    new ScriptOpMatcherBasic(OpType.INCR, any()),
    new ScriptOpMatcherBasic(OpType.INCR, any()),
    new ScriptOpMatcherBasic(OpType.JMP_CMP, any(), any(), any()),
    //LABEL_142
    new ScriptOpMatcherZeroable(OpType.MOV, OpType.MOV_0, any(), any()),
    //LABEL_143
    new ScriptOpMatcherBasic(OpType.JMP_CMP, any(), var(), any()),
    new ScriptOpMatcherBasic(OpType.GOSUB, any()),
    //LABEL_144
    new ScriptOpMatcherBasic(OpType.MOV, any(), any()),
    new ScriptOpMatcherBasic(OpType.AND, any(), any()),
    new ScriptOpMatcherZeroable(OpType.JMP_CMP, OpType.JMP_CMP_0, any(), any(), any()),
    new ScriptOpMatcherBasic(OpType.JMP_CMP, any(), var(), any()),
    new ScriptOpMatcherBasic(OpType.JMP_CMP, any(), var(), any()),
    new ScriptOpMatcherBasic(OpType.JMP_CMP, any(), var(), any()),
    new ScriptOpMatcherBasic(OpType.MOV, any(), var()),
    new ScriptOpMatcherBasic(OpType.JMP, any()),
    //LABEL_145
    new ScriptOpMatcherBasic(OpType.INCR, any()),
    new ScriptOpMatcherBasic(OpType.INCR, any()),
    new ScriptOpMatcherBasic(OpType.JMP_CMP, any(), any(), any()),
    new ScriptOpMatcherBasic(OpType.JMP, any()),
    //LABEL_146
    new ScriptOpMatcherBasic(OpType.INCR, any()),
    new ScriptOpMatcherBasic(OpType.JMP_CMP, any(), any(), any()),
    //LABEL_147
    new ScriptOpMatcherBasic(OpType.CALL, any(), any()),
    new ScriptOpMatcherZeroable(OpType.JMP_CMP, OpType.JMP_CMP_0, any(), any(), any()),
    new ScriptOpMatcherBasic(OpType.MOV, var(), any()),
    new ScriptOpMatcherBasic(OpType.AND, any(), any()),
    new ScriptOpMatcherZeroable(OpType.JMP_CMP, OpType.JMP_CMP_0, any(), any(), any()),
    //LABEL_148
    new ScriptOpMatcherBasic(OpType.JMP_CMP, any(), var(), any()),
    new ScriptOpMatcherBasic(OpType.JMP_CMP, any(), var(), any()),
    new ScriptOpMatcherBasic(OpType.MOV, var(), var()),
    new ScriptOpMatcherBasic(OpType.MOV, any(), any()),
    //LABEL_149
    new ScriptOpMatcherBasic(OpType.RETURN),
  };

  // DRGN22/681/1
  public final ScriptOpMatcher[] pattern2 = {
    //LABEL_30
    new ScriptOpMatcherBasic(OpType.MOV, var(), any()),
    new ScriptOpMatcherBasic(OpType.OR, imm(0x1), any()),
    new ScriptOpMatcherBasic(OpType.OR, imm(0x2), any()),
    new ScriptOpMatcherBasic(OpType.MOV, any(), var()),
    new ScriptOpMatcherZeroable(OpType.MOV, OpType.MOV_0, any(), any()),
    //LABEL_31
    new ScriptOpMatcherBasic(OpType.MOV, var(), any()),
    new ScriptOpMatcherBasic(OpType.JMP_CMP, imm(-1), any(), any()),
    new ScriptOpMatcherBasic(OpType.INCR, any()),
    new ScriptOpMatcherBasic(OpType.JMP_CMP, imm(3), any(), any()),
    new ScriptOpMatcherBasic(OpType.JMP, any()),
    //LABEL_32
    new ScriptOpMatcherBasic(OpType.MOV, any(), var()),
    //LABEL_33
    new ScriptOpMatcherBasic(OpType.RETURN),
  };

  // DRGN22/774/1
  public final ScriptOpMatcher[] pattern3 = {
    //LABEL_209
    new ScriptOpMatcherBasic(OpType.MEMCPY, imm(2), var(), var()),
    new ScriptOpMatcherBasic(OpType.MOV, imm(-1), var()),
    new ScriptOpMatcherBasic(OpType.MOV, imm(-1), var()),
    new ScriptOpMatcherBasic(OpType.ANDOR, imm(0x40), any(), var()),
    new ScriptOpMatcherBasic(OpType.ANDOR, imm(0x40), any(), var()),
    new ScriptOpMatcherBasic(OpType.ANDOR, imm(0x40), any(), var()),
    new ScriptOpMatcherBasic(OpType.ANDOR, imm(0x40), any(), var()),
    new ScriptOpMatcherBasic(OpType.ANDOR, imm(0x40), any(), var()),
    new ScriptOpMatcherBasic(OpType.ANDOR, imm(0x40), any(), var()),
    new ScriptOpMatcherBasic(OpType.ANDOR, imm(0x40), any(), var()),
    new ScriptOpMatcherBasic(OpType.ANDOR, imm(0x40), any(), var()),
    new ScriptOpMatcherBasic(OpType.ANDOR, imm(0x40), any(), var()),
    new ScriptOpMatcherBasic(OpType.RETURN),

    //LABEL_210
    new ScriptOpMatcherBasic(OpType.MEMCPY, imm(2), var(), var()),
    new ScriptOpMatcherBasic(OpType.ANDOR, imm(0x40), any(), var()),
    new ScriptOpMatcherBasic(OpType.ANDOR, imm(0x40), any(), var()),
    new ScriptOpMatcherBasic(OpType.ANDOR, imm(0x40), any(), var()),
    new ScriptOpMatcherBasic(OpType.ANDOR, imm(0x40), any(), var()),
    new ScriptOpMatcherBasic(OpType.ANDOR, imm(0x40), any(), var()),
    new ScriptOpMatcherBasic(OpType.ANDOR, imm(0x40), any(), var()),
    new ScriptOpMatcherBasic(OpType.ANDOR, imm(0x40), any(), var()),
    new ScriptOpMatcherBasic(OpType.ANDOR, imm(0x40), any(), var()),
    new ScriptOpMatcherBasic(OpType.ANDOR, imm(0x40), any(), var()),
    new ScriptOpMatcherBasic(OpType.RETURN),
  };

  // DRGN22/798/1
  public final ScriptOpMatcher[] pattern4 = {
    //LABEL_289
    new ScriptOpMatcherBasic(OpType.MOV, var(), any()),
    new ScriptOpMatcherBasic(OpType.MOV, var(), any()),
    new ScriptOpMatcherBasic(OpType.JMP_CMP, imm(4), any(), any()),
    new ScriptOpMatcherBasic(OpType.MOV, any(), var()),
    new ScriptOpMatcherBasic(OpType.MOV, any(), any()),
    new ScriptOpMatcherBasic(OpType.JMP, any()),
    //LABEL_290
    new ScriptOpMatcherBasic(OpType.JMP_CMP, imm(4), any(), any()),
    //LABEL_291
    new ScriptOpMatcherBasic(OpType.MOV, imm(6), any()),
    new ScriptOpMatcherBasic(OpType.JMP_CMP, any(), any(), any()),
    new ScriptOpMatcherBasic(OpType.MOV, imm(2), any()),
    //LABEL_292
    new ScriptOpMatcherBasic(OpType.MOV, any(), var()),
    //LABEL_293
    new ScriptOpMatcherBasic(OpType.ANDOR, imm(0x40), any(), var()),
    new ScriptOpMatcherBasic(OpType.ANDOR, imm(0x40), any(), var()),
    new ScriptOpMatcherBasic(OpType.ANDOR, imm(0x40), any(), var()),
    new ScriptOpMatcherBasic(OpType.ANDOR, imm(0x40), any(), var()),
    new ScriptOpMatcherBasic(OpType.ANDOR, imm(0x40), any(), var()),
    new ScriptOpMatcherBasic(OpType.ANDOR, imm(0x40), any(), var()),
    new ScriptOpMatcherBasic(OpType.ANDOR, imm(0x40), any(), var()),
    new ScriptOpMatcherBasic(OpType.ANDOR, imm(0x40), any(), var()),
    new ScriptOpMatcherBasic(OpType.ANDOR, imm(0x40), any(), var()),
    new ScriptOpMatcherBasic(OpType.RETURN),
  };

  // DRGN22/804/1
  public final ScriptOpMatcher[] pattern5 = {
    //LABEL_326
    new ScriptOpMatcherBasic(OpType.MOV, var(), var()),
    new ScriptOpMatcherBasic(OpType.MOV, any(), var()),
    new ScriptOpMatcherBasic(OpType.ANDOR, imm(0x40), any(), var()),
    new ScriptOpMatcherBasic(OpType.ANDOR, imm(0x40), any(), var()),
    new ScriptOpMatcherBasic(OpType.ANDOR, imm(0x40), any(), var()),
    new ScriptOpMatcherBasic(OpType.ANDOR, imm(0x40), any(), var()),
    new ScriptOpMatcherBasic(OpType.ANDOR, imm(0x40), any(), var()),
    new ScriptOpMatcherBasic(OpType.ANDOR, imm(0x40), any(), var()),
    new ScriptOpMatcherBasic(OpType.ANDOR, imm(0x40), any(), var()),
    new ScriptOpMatcherBasic(OpType.ANDOR, imm(0x40), any(), var()),
    new ScriptOpMatcherBasic(OpType.ANDOR, imm(0x40), any(), var()),
    new ScriptOpMatcherBasic(OpType.RETURN),
  };

  // DRGN22/807/1
  public final ScriptOpMatcher[] pattern6 = {
    //LABEL_272
    new ScriptOpMatcherBasic(OpType.MOV, imm(-1), var()),
    new ScriptOpMatcherBasic(OpType.MOV, imm(-1), var()),
    new ScriptOpMatcherBasic(OpType.ANDOR, imm(0x40), any(), var()),
    new ScriptOpMatcherBasic(OpType.ANDOR, imm(0x40), any(), var()),
    new ScriptOpMatcherBasic(OpType.ANDOR, imm(0x40), any(), var()),
    new ScriptOpMatcherBasic(OpType.ANDOR, imm(0x40), any(), var()),
    new ScriptOpMatcherBasic(OpType.ANDOR, imm(0x40), any(), var()),
    new ScriptOpMatcherBasic(OpType.ANDOR, imm(0x40), any(), var()),
    new ScriptOpMatcherBasic(OpType.ANDOR, imm(0x40), any(), var()),
    new ScriptOpMatcherBasic(OpType.ANDOR, imm(0x40), any(), var()),
    new ScriptOpMatcherBasic(OpType.RETURN),
  };

  // DRGN22/810/1
  public final ScriptOpMatcher[] pattern7 = {
    //LABEL_220
    new ScriptOpMatcherBasic(OpType.MOV, imm(1), any()),
    new ScriptOpMatcherBasic(OpType.MOV, var(), any()),
    new ScriptOpMatcherBasic(OpType.JMP_CMP, imm(4), any(), any()),
    new ScriptOpMatcherBasic(OpType.MOV, imm(2), any()),
    new ScriptOpMatcherBasic(OpType.MOV, var(), any()),
    new ScriptOpMatcherBasic(OpType.JMP_CMP, imm(4), any(), any()),
    //LABEL_221
    new ScriptOpMatcherBasic(OpType.MOV, imm(-1), any()),
    //LABEL_222
    new ScriptOpMatcherZeroable(OpType.MOV, OpType.MOV_0, any(), var()),
    new ScriptOpMatcherZeroable(OpType.MOV, OpType.MOV_0, any(), var()),
    new ScriptOpMatcherZeroable(OpType.MOV, OpType.MOV_0, any(), var()),
    new ScriptOpMatcherZeroable(OpType.MOV, OpType.MOV_0, any(), var()),
    new ScriptOpMatcherZeroable(OpType.MOV, OpType.MOV_0, any(), var()),
    new ScriptOpMatcherZeroable(OpType.MOV, OpType.MOV_0, any(), var()),
    new ScriptOpMatcherZeroable(OpType.MOV, OpType.MOV_0, any(), var()),
    new ScriptOpMatcherZeroable(OpType.MOV, OpType.MOV_0, any(), var()),
    new ScriptOpMatcherZeroable(OpType.MOV, OpType.MOV_0, any(), var()),
    new ScriptOpMatcherBasic(OpType.RETURN),
  };

  // DRGN22/834/1
  public final ScriptOpMatcher[] pattern8 = {
    //LABEL_71
    new ScriptOpMatcherBasic(OpType.JMP_CMP, imm(8), any(), any()),
    new ScriptOpMatcherBasic(OpType.MOV, any(), any()),
    new ScriptOpMatcherBasic(OpType.MOV, any(), var()),
    new ScriptOpMatcherBasic(OpType.ANDOR, imm(0x40), imm(0), var()),
    new ScriptOpMatcherBasic(OpType.ANDOR, imm(0x40), imm(0), var()),
    new ScriptOpMatcherBasic(OpType.ANDOR, imm(0x40), imm(0), var()),
    new ScriptOpMatcherBasic(OpType.ANDOR, imm(0x40), imm(0), var()),
    new ScriptOpMatcherBasic(OpType.ANDOR, imm(0x40), imm(0), var()),
    new ScriptOpMatcherBasic(OpType.ANDOR, imm(0x40), imm(0), var()),
    new ScriptOpMatcherBasic(OpType.ANDOR, imm(0x40), imm(0), var()),
    new ScriptOpMatcherBasic(OpType.ANDOR, imm(0x40), imm(0), var()),
    new ScriptOpMatcherBasic(OpType.ANDOR, imm(0x40), imm(0), var()),
    new ScriptOpMatcherBasic(OpType.SHL, imm(1), any()),
    new ScriptOpMatcherBasic(OpType.MOV, any(), var()),
    new ScriptOpMatcherBasic(OpType.GOSUB, any()),
    new ScriptOpMatcherBasic(OpType.INCR, any()),
    new ScriptOpMatcherBasic(OpType.MOV, any(), var()),
    new ScriptOpMatcherBasic(OpType.GOSUB, any()),
    new ScriptOpMatcherBasic(OpType.MOV, imm(-1), var()),
    new ScriptOpMatcherBasic(OpType.JMP, any()),
    //LABEL_72
    new ScriptOpMatcherBasic(OpType.JMP_CMP, imm(1), var(), any()),
    new ScriptOpMatcherBasic(OpType.JMP_CMP, imm(8), var(), any()),
    //LABEL_73
    new ScriptOpMatcherBasic(OpType.MOV, imm(8), var()),
    //LABEL_74
    new ScriptOpMatcherBasic(OpType.MOV, var(), any()),
    new ScriptOpMatcherBasic(OpType.CALL, any(), any()),
    new ScriptOpMatcherBasic(OpType.CALL, any(), any()),
    new ScriptOpMatcherBasic(OpType.RETURN),
  };

  // DRGN22/924/1
  public final ScriptOpMatcher[] pattern9 = {
    //LABEL_37
    new ScriptOpMatcherZeroable(OpType.MOV, OpType.MOV_0, any(), var()),
    new ScriptOpMatcherZeroable(OpType.MOV, OpType.MOV_0, any(), var()),
    new ScriptOpMatcherZeroable(OpType.MOV, OpType.MOV_0, any(), var()),
    new ScriptOpMatcherZeroable(OpType.MOV, OpType.MOV_0, any(), var()),
    new ScriptOpMatcherZeroable(OpType.MOV, OpType.MOV_0, any(), var()),
    new ScriptOpMatcherZeroable(OpType.MOV, OpType.MOV_0, any(), var()),
    new ScriptOpMatcherZeroable(OpType.MOV, OpType.MOV_0, any(), var()),
    new ScriptOpMatcherZeroable(OpType.MOV, OpType.MOV_0, any(), var()),
    new ScriptOpMatcherZeroable(OpType.MOV, OpType.MOV_0, any(), var()),
    new ScriptOpMatcherZeroable(OpType.MOV, OpType.MOV_0, any(), var()),
    new ScriptOpMatcherZeroable(OpType.MOV, OpType.MOV_0, any(), var()),
    new ScriptOpMatcherZeroable(OpType.MOV, OpType.MOV_0, any(), var()),
    new ScriptOpMatcherBasic(OpType.RETURN),
  };

  // DRGN22/969/1
  public final ScriptOpMatcher[] pattern10 = {
    //LABEL_42
    new ScriptOpMatcherZeroable(OpType.MOV, OpType.MOV_0, any(), var()),
    new ScriptOpMatcherZeroable(OpType.MOV, OpType.MOV_0, any(), var()),
    new ScriptOpMatcherZeroable(OpType.MOV, OpType.MOV_0, any(), var()),
    new ScriptOpMatcherBasic(OpType.ANDOR, imm(0x40), any(), var()),
    new ScriptOpMatcherBasic(OpType.ANDOR, imm(0x40), any(), var()),
    new ScriptOpMatcherBasic(OpType.ANDOR, imm(0x40), any(), var()),
    new ScriptOpMatcherBasic(OpType.ANDOR, imm(0x40), any(), var()),
    new ScriptOpMatcherBasic(OpType.ANDOR, imm(0x40), any(), var()),
    new ScriptOpMatcherBasic(OpType.ANDOR, imm(0x40), any(), var()),
    new ScriptOpMatcherBasic(OpType.ANDOR, imm(0x40), any(), var()),
    new ScriptOpMatcherBasic(OpType.ANDOR, imm(0x40), any(), var()),
    new ScriptOpMatcherBasic(OpType.ANDOR, imm(0x40), any(), var()),
    new ScriptOpMatcherBasic(OpType.RETURN),
  };
}
