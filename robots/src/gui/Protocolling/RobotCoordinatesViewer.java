package gui.Protocolling;

import gui.GameVisualizer;
import gui.GameWindow;
import java.util.TimerTask;
import javax.swing.JInternalFrame;
import java.util.Timer;
import javax.swing.JTextArea;

public class RobotCoordinatesViewer extends JInternalFrame {

  public RobotCoordinatesViewer(GameWindow gameWindow) {
    super.setSize(400, 400);
    JTextArea area = new JTextArea();
    add(area);
    Timer timer = new Timer("Shower coordinates", true);
    timer.schedule(new TimerTask() {
      @Override
      public void run() {
        area.setText("X = " + (int) gameWindow.getM_visualizer().getM_robotPositionX()
            + " \tY = " + (int) gameWindow.getM_visualizer().getM_robotPositionY());

      }
    }, 0, 5);
  }
}
