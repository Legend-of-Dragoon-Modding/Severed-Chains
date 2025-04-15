package legend.game.debugger;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import legend.game.modding.events.scripting.ScriptAllocatedEvent;
import legend.game.modding.events.scripting.ScriptDeallocatedEvent;
import legend.game.modding.events.scripting.ScriptTickEvent;
import legend.game.scripting.ScriptFile;
import legend.game.scripting.ScriptStackFrame;
import legend.game.scripting.ScriptState;
import org.legendofdragoon.modloader.events.EventListener;
import org.legendofdragoon.scripting.Disassembler;
import org.legendofdragoon.scripting.Translator;
import org.legendofdragoon.scripting.tokens.Param;
import org.legendofdragoon.scripting.tokens.Script;

import java.nio.file.Path;

import static legend.core.GameEngine.EVENTS;
import static legend.core.GameEngine.SCRIPTS;

public class ScriptLiveDebuggerController {
  private final Disassembler disassembler;
  private final Translator translator;
  private Script tokens;
  private int index;
  private boolean stepping;
  private boolean runningDebugCode;

  @FXML
  private AnchorPane pnlUi;

  @FXML
  private Button btnPause;
  @FXML
  private Button btnResume;
  @FXML
  private Button btnStep;

  @FXML
  private TextArea txtCode;

  @FXML
  private TextField txtRun;
  @FXML
  private Button btnRun;

  public ScriptLiveDebuggerController() {
    this.disassembler = new Disassembler(SCRIPTS.meta());
    this.translator = new Translator();
    this.translator.lineNumbers = true;
  }

  public void initialize() {
    EVENTS.register(this);
    this.clear();
  }

  public void setState(final int index) {
    this.index = index;
    this.decompile();
  }

  public void uninitialize() {
    final ScriptState<?> state = SCRIPTS.getState(this.index);

    if(state != null) {
      state.resume();
    }
  }

  private void decompile() {
    final ScriptState<?> state = SCRIPTS.getState(this.index);

    if(state != null) {
      this.pnlUi.setDisable(false);

      if(state.isPaused()) {
        this.btnPause.setDisable(true);
        this.btnResume.setDisable(false);
        this.btnStep.setDisable(false);
      } else {
        this.btnPause.setDisable(false);
        this.btnResume.setDisable(true);
        this.btnStep.setDisable(true);
      }

      this.tokens = this.disassembler.disassemble(state.frame().file.data);
    }
  }

  private void displayCode(final int offset) {
    int start = offset;
    int end = offset + 1;

    for(int backtrack = 0; start >= 0 && backtrack < 5; start--) {
      if(!(this.tokens.entries[start] instanceof Param)) {
        backtrack++;
      }
    }

    for(int lookahead = 0; end < this.tokens.entries.length && lookahead < 5; end++) {
      if(!(this.tokens.entries[end] instanceof Param)) {
        lookahead++;
      }
    }

    final Script script = new Script(end - start);
    System.arraycopy(this.tokens.entries, start, script.entries, 0, script.entries.length);

    final String[] lines = this.translator.translate(script, SCRIPTS.meta()).split("\n");
    final StringBuilder out = new StringBuilder();

    for(int i = 0; i < lines.length; i++) {
      out.append(lines[i]).append('\n');

      if(lines[i].startsWith(Integer.toHexString(offset * 4))) {
        out.append("~".repeat(lines[i].length())).append('\n');
      }
    }

    this.txtCode.setText(out.toString());
  }

  private void clear() {
    this.pnlUi.setDisable(true);
    this.btnPause.setDisable(true);
    this.btnResume.setDisable(true);
    this.btnStep.setDisable(true);
  }

  @EventListener
  public void onScriptAllocated(final ScriptAllocatedEvent event) {
    if(event.scriptIndex == this.index) {
      Platform.runLater(this::decompile);
    }
  }

  @EventListener
  public void onScriptDeallocated(final ScriptDeallocatedEvent event) {
    if(event.scriptIndex == this.index) {
      Platform.runLater(this::clear);
    }
  }

  @EventListener
  public void onScriptTick(final ScriptTickEvent event) {
    if(event.scriptIndex == this.index) {
      final ScriptState<?> state = SCRIPTS.getState(this.index);

      if(this.stepping) {
        state.pause();
        this.stepping = false;
      }

      if(this.runningDebugCode) {
        this.runningDebugCode = false;
        return;
      }

      final int offset = state.context.opOffset_08;
      Platform.runLater(() -> this.displayCode(offset));
    }
  }

  public void pauseScript(final ActionEvent event) throws Exception {
    SCRIPTS.getState(this.index).pause();
    this.btnPause.setDisable(true);
    this.btnResume.setDisable(false);
    this.btnStep.setDisable(false);
  }

  public void resumeScript(final ActionEvent event) throws Exception {
    SCRIPTS.getState(this.index).resume();
    this.btnPause.setDisable(false);
    this.btnResume.setDisable(true);
    this.btnStep.setDisable(true);
  }

  public void stepScript(final ActionEvent event) throws Exception {
    this.stepping = true;
    SCRIPTS.getState(this.index).resume();
  }

  public void txtCodeKeyPressed(final KeyEvent event) {
    if(event.getCode() == KeyCode.ENTER && !this.txtRun.getText().isBlank()) {
      this.runCode();
    }
  }

  public void runCode(final ActionEvent event) {
    this.runCode();
  }

  private void runCode() {
    this.runningDebugCode = true;

    final byte[] compiled = SCRIPTS.compile(Path.of("./patches/dummy"), this.txtRun.getText() + "\nreturn");
    final ScriptFile script = new ScriptFile("Injected code", compiled);

    final ScriptState<?> state = SCRIPTS.getState(this.index);
    state.pushFrame(new ScriptStackFrame(script, 0));

    this.stepping = true;
    state.resume();
  }
}
