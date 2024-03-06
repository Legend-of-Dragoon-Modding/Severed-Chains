package legend.game.debugger;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import legend.game.net.NetClient;

public class ClientUiController {
  @FXML
  private Button btnConnect;

  @FXML
  private TextField txtIp;

  @FXML
  private TextField txtPort;

  @FXML
  private TextArea txtLog;

  public void connectClicked(final ActionEvent event) {
    try {
      final String ip = this.txtIp.getText();
      final int port = Integer.parseInt(this.txtPort.getText());
      new Thread(() -> {
        try {
          this.addText("Connecting...");
          new NetClient().connect(ip, port, this::addText);
        } catch(final InterruptedException e) {
          throw new RuntimeException(e);
        }
      }).start();
    } catch(final Throwable e) {
      e.printStackTrace();
    }
  }

  private void addText(final String text) {
    this.txtLog.setText(this.txtLog.getText() + text + "\n");
  }
}
