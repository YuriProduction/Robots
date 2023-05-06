package gui.Game;

import javax.swing.JPanel;

public class GameController implements interactiveWithModel {

  private JPanel currentWorkPanel;

  public JPanel getCurrentWorkPanel() {
    return currentWorkPanel;
  }

  public GameController(final JPanel panel) {
    this.currentWorkPanel = panel;
  }


}
