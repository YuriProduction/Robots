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
        int x = (int) gameWindow.getM_visualizer().getM_robotPositionX();
        int y = (int) gameWindow.getM_visualizer().getM_robotPositionY();
        area.setText("X = " + x
            + " \tY = " + y);
//        if (x < 0) {
//          gameWindow.getM_visualizer().setM_robotPositionX(20);
//        }
//        if (y < 0) {
//          gameWindow.getM_visualizer().setM_robotPositionY(20);
//        }

      }
    }, 0, 5);
  }
}
