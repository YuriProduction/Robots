package gui;

import gui.Protocolling.RobotCoordinatesViewer;
import java.awt.BorderLayout;

import javax.swing.JInternalFrame;
import javax.swing.JPanel;

public class GameWindow extends JInternalFrame {

  public GameVisualizer getM_visualizer() {
    return m_visualizer;
  }

  private final GameVisualizer m_visualizer;

  public GameWindow() {
    super("Игровое поле", true, true, true, true);
    m_visualizer = new GameVisualizer();
    JPanel panel = new JPanel(new BorderLayout());
    panel.add(m_visualizer, BorderLayout.CENTER);
    //добавляем окно в панель контента
    getContentPane().add(panel);
    pack();
  }
}
