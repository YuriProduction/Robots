package gui;

import Localization.LanguageAdapter;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import java.awt.event.WindowAdapter;
import javax.swing.AbstractButton;
import javax.swing.InputMap;
import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;
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
    setBounds(inset, inset,
        screenSize.width - inset * 2,
        screenSize.height - inset * 2);

    setContentPane(desktopPane);

    //так как расширяет JInternalFrame,
    // значит можно его помещать внутрь основного окна
    LogWindow logWindow = createLogWindow();
    //добавляем окно
    addWindow(logWindow);

    GameWindow gameWindow = new GameWindow();
    gameWindow.setSize(400, 400);
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
//                KeyEvent.VK_Q, ActionEvent.ALT_MASK));
//        menuItem.setActionCommand("quit");
////        menuItem.addActionListener(this);
//        menu.add(menuItem);
// 
//        return menuBar;
//    }

  private JMenuBar generateMenuBar() {
    //упростить метод, чтобы меню с айтемами
    // генерировала одна функция
    JMenuBar menuBar = new JMenuBar();

    setMenuItemWithSubtitles(menuBar, new JMenu("Режим отображения"),
        KeyEvent.VK_V, "Управление режимом отображения приложения",
        new JMenuItem[]{new JMenuItem("Системная схема", KeyEvent.VK_S),
            new JMenuItem("Универсальная схема", KeyEvent.VK_S)},
        new ActionListener[]{(event) -> {
          setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
          this.invalidate();
        },
            (event) -> {
              setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
              this.invalidate();
            }}
    );

    setMenuItemWithSubtitles(menuBar, new JMenu("Тесты"),
        KeyEvent.VK_T, "Тестовые команды",
        new JMenuItem[]{new JMenuItem("Сообщение в лог", KeyEvent.VK_S)},
        new ActionListener[]{(event) -> {
          Logger.debug("Новая строка");
        }}
    );
    setMenuItemWithSubtitles(menuBar, new JMenu("Опции"),
        KeyEvent.VK_UNDEFINED, "Команда для закрытия",
        new JMenuItem[]{new JMenuItem("Выход", KeyEvent.VK_L)},
        new ActionListener[]{(event) -> {
          showConfirmMessage(this);
        }});
    return menuBar;
  }

  private void setMenuItemWithSubtitles(final JMenuBar menuBar, final JMenu menu, int mnemonicEvent
      , String description, final JMenuItem[] subItems, final ActionListener[] listeners) {
    menu.setMnemonic(mnemonicEvent);
    menu.getAccessibleContext().setAccessibleDescription(description);
    for (int i = 0; i < subItems.length; i++) {
      menu.add(subItems[i]);
      subItems[i].addActionListener(listeners[i]);
    }
    menuBar.add(menu);
  }


  private void setExitEventOnMainFrame() {
    final JFrame curFrame = this;
    this.addWindowListener(new WindowAdapter() {
      @Override
      public void windowClosing(java.awt.event.WindowEvent windowEvent) {
        showConfirmMessage(curFrame);
      }
    });
  }

  private void showConfirmMessage(final JFrame Frame) {
    int choice = JOptionPane.showConfirmDialog(
        Frame,
        "Вы уверены, что хотите выйти?",
        "Выход",
        JOptionPane.YES_NO_OPTION);
    if (choice == JOptionPane.YES_OPTION) {
      Frame.dispose();
    }
  }


  private void setLookAndFeel(String className) {
    try {
      UIManager.setLookAndFeel(className);
      SwingUtilities.updateComponentTreeUI(this);
    } catch (ClassNotFoundException | InstantiationException
             | IllegalAccessException | UnsupportedLookAndFeelException e) {
      // just ignore
    }
  }
}
