package gui;

import Localization.Languages.Russifier;
import java.awt.Frame;

import java.beans.PropertyVetoException;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class RobotsProgram {

  public static void main(String[] args) {
    try {
      System.out.println("I have managed to start project!");
      //устанавливаем тему
      UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
//        UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
//        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
//        UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
    } catch (Exception e) {
      e.printStackTrace();
    }
    SwingUtilities.invokeLater(() -> {
      MainApplicationFrame frame = null;
      try {
        frame = new MainApplicationFrame(new Russifier());
      } catch (PropertyVetoException e) {
        throw new RuntimeException(e);
      }
      frame.pack();
      frame.setVisible(true);
      //разворачивает окно на весь экран
      frame.setExtendedState(Frame.MAXIMIZED_BOTH);
    });
  }
}
