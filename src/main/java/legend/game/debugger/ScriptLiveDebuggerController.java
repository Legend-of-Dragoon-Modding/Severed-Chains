package legend.game.debugger;

import com.opencsv.exceptions.CsvException;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;
import legend.game.modding.events.scripting.ScriptAllocatedEvent;
import legend.game.modding.events.scripting.ScriptDeallocatedEvent;
import legend.game.modding.events.scripting.ScriptTickEvent;
import legend.game.scripting.ScriptState;
import org.legendofdragoon.modloader.events.EventListener;
import org.legendofdragoon.scripting.Disassembler;
import org.legendofdragoon.scripting.Translator;
import org.legendofdragoon.scripting.meta.Meta;
import org.legendofdragoon.scripting.meta.MetaManager;
import org.legendofdragoon.scripting.meta.NoSuchVersionException;
import org.legendofdragoon.scripting.tokens.Script;

import java.io.IOException;
import java.nio.file.Path;

import static legend.core.GameEngine.EVENTS;
import static legend.core.GameEngine.SCRIPTS;

public class ScriptLiveDebuggerController {
  private final Meta meta;
  private final Disassembler disassembler;
  private final Translator translator;
  private Script tokens;
  private int index;
  private boolean stepping;

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

  public ScriptLiveDebuggerController() throws NoSuchVersionException, IOException, CsvException {
    this.meta = new MetaManager(null, Path.of("./patches")).loadMeta("meta");
    this.disassembler = new Disassembler(this.meta);
    this.translator = new Translator();
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
    final Script line = new Script(1);
    line.entries[0] = this.tokens.entries[offset];
    this.txtCode.setText("0x" + Integer.toHexString(offset * 0x4) + ": " + this.translator.translate(line, this.meta));
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
}
