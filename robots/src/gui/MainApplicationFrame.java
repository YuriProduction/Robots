package gui;

import Localization.LanguageAdapter;
import Serialization.SaveableAndLoadable;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.prefs.BackingStoreException;
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

    LogWindow logWindow = createLogWindow("logWindow");
    GameWindow gameWindow = new GameWindow("GameWindow");
    addWindow(logWindow);
    addWindow(gameWindow);
    gameWindow.loadData();

    listOfInternalFrames.add(gameWindow);
    listOfInternalFrames.add(logWindow);
    setJMenuBar(generateMenuBar());
    setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);


  }


  protected LogWindow createLogWindow(String name) {
    LogWindow logWindow = new LogWindow(Logger.getDefaultLogSource(), name);
    logWindow.loadData();
    setMinimumSize(logWindow.getSize());
    logWindow.pack();
    Logger.debug("Протокол работает");
    return logWindow;
  }

  protected void addWindow(JInternalFrame frame) {
    desktopPane.add(frame);
    frame.setVisible(true);
  }

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
      for (JInternalFrame internalFrame : listOfInternalFrames) {
        if (internalFrame instanceof SaveableAndLoadable) {
          ((SaveableAndLoadable) internalFrame).saveData();
        }
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
    }
  }
}
