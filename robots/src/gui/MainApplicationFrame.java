package gui;

import Localization.LanguageAdapter;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
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

  //окно, которое внутри хранит другие окна
  private final JDesktopPane desktopPane = new JDesktopPane();

  public MainApplicationFrame(LanguageAdapter adapter) {
    //Make the big window be indented 50 pixels from each edge
    //of the screen.
    adapter.setLocalLanguage();
    setExitEventOnMainFrame();
    int inset = 50;
    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    setBounds(inset, inset, screenSize.width - inset * 2, screenSize.height - inset * 2);

    setContentPane(desktopPane);

    //так как расширяет JInternalFrame,
    // значит можно его помещать внутрь основного окна
    //добавляем окно
    addWindow(createLogWindow());
    addWindow(new GameWindow(), 400, 400);

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

  protected void addWindow(JInternalFrame frame, int w, int h) {
    frame.setSize(w, h);
    addWindow(frame);
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
//                KeyEvent.VK_Q, ActionEvent.ALT_MASK));
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
        showConfirmMessage();
      }
    });
  }

  private JMenu generateOptionMenu() {
    JMenu menu = new JMenu("Опции");
    menu.setMnemonic(KeyEvent.VK_UNDEFINED);
    menu.getAccessibleContext().setAccessibleDescription("Команда для закрытия");
    menu.add(generateMenuItem("Выход", KeyEvent.VK_L, e -> showConfirmMessage()));

    return menu;
  }

  private JMenu generateTestMenu() {
    JMenu menu = new JMenu("Тесты");
    menu.setMnemonic(KeyEvent.VK_T);
    menu.getAccessibleContext().setAccessibleDescription("Тестовые команды");
    menu.add(generateMenuItem("Сообщение в лог", KeyEvent.VK_S, e -> Logger.debug("Новая строка")));
    return menu;
  }

  private JMenu generateMappingMenu() {
    JMenu menu = new JMenu("Режим отображения");
    menu.setMnemonic(KeyEvent.VK_V);
    menu.getAccessibleContext()
        .setAccessibleDescription("Управление режимом отображения приложения");

    menu.add(generateMenuItem("Системная схема", KeyEvent.VK_S, e -> {
      setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
      invalidate();
    }));
    menu.add(generateMenuItem("Универсальная схема", KeyEvent.VK_S, e -> {
      setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
      invalidate();
    }));

    return menu;
  }

  private JMenuItem generateMenuItem(String name, int keyEvent,
      final ActionListener actionListener) {
    JMenuItem item = new JMenuItem(name, keyEvent);
    item.addActionListener(actionListener);
    return item;
  }

  private void showConfirmMessage() {
    int choice = JOptionPane.showConfirmDialog(this, "Вы уверены, что хотите выйти?", "Выход",
        JOptionPane.YES_NO_OPTION);
    if (choice == JOptionPane.YES_OPTION) {
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
