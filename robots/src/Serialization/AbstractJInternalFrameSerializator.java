package Serialization;

import java.beans.PropertyVetoException;
import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;
import javax.swing.JInternalFrame;

public abstract class AbstractJInternalFrameSerializator extends JInternalFrame implements
    Changeable {

  public AbstractJInternalFrameSerializator(String title, boolean resizeble, boolean closable,
      boolean maximizable, boolean iconifiable) {
    super(title, resizeble, closable, maximizable, iconifiable);
  }

  @Override
  public void load() throws PropertyVetoException {
    final DataOfFrame dataOfThisFrame = new PreferencesDemo().readXML(this.getName());
    this.setLocation(dataOfThisFrame.X(), dataOfThisFrame.Y());
    this.setSize(dataOfThisFrame.width(), dataOfThisFrame.height());
    this.setIcon(dataOfThisFrame.Icon());
  }

  @Override
  public void save() throws IOException, BackingStoreException {
    Preferences userPrefs = Preferences.userNodeForPackage(PreferencesDemo.class);
    OutputStream osTree = new BufferedOutputStream(new FileOutputStream(
        System.getProperty("user.home") + "\\Preferencess\\" + this.getName() + ".xml"));
    System.out.println(this.getX() + " " + this.getY());
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
