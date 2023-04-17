package gui;

import Localization.LanguageAdapter;
import Serialization.PreferencesDemo;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.io.BufferedOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;
import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import log.Logger;

/**
 * Что требуется сделать: 1. Метод создания меню перегружен функционалом и трудно читается. Следует
 * разделить его на серию более простых методов (или вообще выделить отдельный класс).
 */
public class MainApplicationFrame extends JFrame {

  // окно, которое внутри хранит другие окна
  private final JDesktopPane desktopPane = new JDesktopPane();
  private final List<JInternalFrame> listOfInternalFrames = new ArrayList<>();

  public MainApplicationFrame(LanguageAdapter adapter) {
    //Make the big window be indented 50 pixels from each edge
    //of the screen.
    adapter.setLocalLanguage();
    setExitEventOnMainFrame();
    int inset = 50;
    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    setBounds(inset, inset, screenSize.width - inset * 2, screenSize.height - inset * 2);

    setContentPane(desktopPane);

//    new PreferencesDemo().readXML("logWindow");
//    new PreferencesDemo().readXML("GameWindow");

    //так как расширяет JInternalFrame,
    // значит можно его помещать внутрь основного окна
    //добавляем окно
    LogWindow logWindow = createLogWindow();
    logWindow.setName("logWindow");
    listOfInternalFrames.add(logWindow);
    addWindow(logWindow);

//    GameWindow gameWindow = new GameWindow();
//    gameWindow.setSize(400, 400);
    GameWindow gameWindow = new GameWindow();
    gameWindow.setSize(400, 400);
    gameWindow.setName("GameWindow");
    listOfInternalFrames.add(gameWindow);
    addWindow(gameWindow);

    setJMenuBar(generateMenuBar());
    setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);


  }


  protected LogWindow createLogWindow() {
    LogWindow logWindow = new LogWindow(Logger.getDefaultLogSource());
    logWindow.setLocation(10, 10);
    logWindow.setSize(300, 800);
    setMinimumSize(logWindow.getSize());
    logWindow.pack();
    Logger.debug("Протокол работает");
    return logWindow;
  }

  protected void addWindow(JInternalFrame frame) {
    desktopPane.add(frame);
    frame.setVisible(true);
  }

//    protected JMenuBar createMenuBar() {
//        JMenuBar menuBar = new JMenuBar();
// 
//        //Set up the lone menu.
//        JMenu menu = new JMenu("Document");
//        menu.setMnemonic(KeyEvent.VK_D);
//        menuBar.add(menu);
// 
//        //Set up the first menu item.
//        JMenuItem menuItem = new JMenuItem("New");
//        menuItem.setMnemonic(KeyEvent.VK_N);
//        menuItem.setAccelerator(KeyStroke.getKeyStroke(
//                KeyEvent.VK_N, ActionEvent.ALT_MASK));
//        menuItem.setActionCommand("new");
////        menuItem.addActionListener(this);
//        menu.add(menuItem);
// 
//        //Set up the second menu item.
//        menuItem = new JMenuItem("Quit");
//        menuItem.setMnemonic(KeyEvent.VK_Q);
//        menuItem.setAccelerator(KeyStroke.getKeyStroke(
//        KeyEvent.VK_Q, ActionEvent.ALT_MASK));
//        menuItem.setActionCommand("quit");
////        menuItem.addActionListener(this);
//        menu.add(menuItem);
// 
//        return menuBar;
//    }

  private JMenuBar generateMenuBar() {
    //упростить метод
    JMenuBar menuBar = new JMenuBar();

    menuBar.add(generateMappingMenu());
    menuBar.add(generateTestMenu());
    menuBar.add(generateOptionMenu());
    return menuBar;
  }

  private void setExitEventOnMainFrame() {
    this.addWindowListener(new WindowAdapter() {
      @Override
      public void windowClosing(java.awt.event.WindowEvent windowEvent) {
        try {
          showConfirmMessage();
        } catch (IOException | BackingStoreException e) {
          throw new RuntimeException(e);
        }
      }
    });
  }

  private JMenu generateOptionMenu() {
    JMenu menu = new JMenu("Опции");
    menu.setMnemonic(KeyEvent.VK_UNDEFINED);
    menu.getAccessibleContext().setAccessibleDescription("Команда для закрытия");
    JMenuItem item = new JMenuItem("Выход", KeyEvent.VK_L);

    item.addActionListener(e -> {
      try {
        showConfirmMessage();
      } catch (IOException | BackingStoreException ex) {
        throw new RuntimeException(ex);
      }
    });

    menu.add(item);

    return menu;
  }

  private JMenu generateTestMenu() {
    JMenu menu = new JMenu("Тесты");
    menu.setMnemonic(KeyEvent.VK_T);
    menu.getAccessibleContext().setAccessibleDescription("Тестовые команды");
    JMenuItem item = new JMenuItem("Сообщение в лог", KeyEvent.VK_S);

    item.addActionListener(e -> Logger.debug("Новая строка"));
    menu.add(item);

    return menu;
  }

  private JMenu generateMappingMenu() {
    JMenu menu = new JMenu("Режим отображения");
    menu.setMnemonic(KeyEvent.VK_V);
    menu.getAccessibleContext()
        .setAccessibleDescription("Управление режимом отображения приложения");
    JMenuItem item1 = new JMenuItem("Системная схема", KeyEvent.VK_S);
    JMenuItem item2 = new JMenuItem("Универсальная схема", KeyEvent.VK_S);

    item1.addActionListener(e -> {
      setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
      invalidate();
    });

    item2.addActionListener(e -> {
      setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
      invalidate();
    });

    menu.add(item1);
    menu.add(item2);

    return menu;
  }

  private void showConfirmMessage() throws IOException, BackingStoreException {
    int choice = JOptionPane.showConfirmDialog(this, "Вы уверены, что хотите выйти?", "Выход",
        JOptionPane.YES_NO_OPTION);
    if (choice == JOptionPane.YES_OPTION) {
      Preferences userPrefs = Preferences.userNodeForPackage(PreferencesDemo.class);

      for (JInternalFrame internalFrame : listOfInternalFrames) {
        OutputStream osTree = new BufferedOutputStream(
            new FileOutputStream(
                System.getProperty("user.home") + "\\Preferencess\\" + internalFrame.getName()
                    + ".xml"));
        userPrefs.putInt("X", internalFrame.getX());
        userPrefs.putInt("Y", internalFrame.getY());
        userPrefs.putInt("width", internalFrame.getWidth());
        userPrefs.putInt("height", internalFrame.getHeight());
        userPrefs.put("name", internalFrame.getName());
        userPrefs.exportSubtree(osTree);
        osTree.close();
      }

      dispose();
    }
  }

  private void setLookAndFeel(String className) {
    try {
      UIManager.setLookAndFeel(className);
      SwingUtilities.updateComponentTreeUI(this);
    } catch (ClassNotFoundException | InstantiationException | IllegalAccessException |
             UnsupportedLookAndFeelException e) {
      // just ignore
    }
  }
}
