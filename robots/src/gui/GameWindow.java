package gui;

import Serialization.AbstractJInternalFrameSerializator;
import Serialization.DataOfFrame;
import Serialization.PreferencesDemo;
import Serialization.Changeable;
import java.awt.BorderLayout;

import java.beans.PropertyVetoException;
import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;
import javax.swing.JInternalFrame;
import javax.swing.JPanel;

public class GameWindow extends AbstractJInternalFrameSerializator {

  private final GameVisualizer m_visualizer;

  public GameWindow(String name) {
    super("Игровое поле", true, true, true, true);
    this.setName(name);
    m_visualizer = new GameVisualizer();
    JPanel panel = new JPanel(new BorderLayout());
    panel.add(m_visualizer, BorderLayout.CENTER);
    //добавляем окно в панель контента
    getContentPane().add(panel);
    pack();
  }
}
