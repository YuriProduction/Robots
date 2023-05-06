package gui;

import gui.Game.GameView;
import java.awt.BorderLayout;
import javax.swing.JInternalFrame;
import javax.swing.JPanel;

public class GameWindow extends JInternalFrame {

  //private final GameVisualizer m_visualizer;
  private final GameView gameView;

  public GameWindow() {
    super("Игровое поле", true, true, true, true);
    gameView = new GameView();
    JPanel panel = new JPanel(new BorderLayout());
    panel.add(gameView, BorderLayout.CENTER);
    //добавляем окно в панель контента
    getContentPane().add(panel);
    pack();
  }
}
