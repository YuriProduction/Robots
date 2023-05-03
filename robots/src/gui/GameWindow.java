package gui;

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

public class GameWindow extends JInternalFrame implements Changeable {

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

  @Override
  public void load() throws PropertyVetoException {
    DataOfFrame dataOfThisFrame = new PreferencesDemo().readXML(this.getName());
    this.setLocation(dataOfThisFrame.X(), dataOfThisFrame.Y());
    this.setSize(dataOfThisFrame.width(), dataOfThisFrame.height());
    this.setIcon(dataOfThisFrame.Icon());
  }

  @Override
  public void save() throws IOException, BackingStoreException {
    Preferences userPrefs = Preferences.userNodeForPackage(PreferencesDemo.class);
    OutputStream osTree = new BufferedOutputStream(
        new FileOutputStream(
            System.getProperty("user.home") + "\\Preferencess\\" + this.getName()
                + ".xml"));
    userPrefs.putInt("X", this.getX());
    userPrefs.putInt("Y", this.getY());
    userPrefs.putInt("width", this.getWidth());
    userPrefs.putInt("height", this.getHeight());
    userPrefs.putBoolean("Icon", this.isIcon());
    userPrefs.put("name", this.getName());
    userPrefs.exportSubtree(osTree);
    osTree.close();
  }
}
