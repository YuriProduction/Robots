package gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.UIManager;
import log.Logger;

public class MenuHandler {

  public JMenu generateMappingMenu(MainApplicationFrame frame, int mnemonicEvent,
      String description) {
    JMenu menu = new JMenu("Режим отображения");
    menu.setMnemonic(mnemonicEvent);
    menu.getAccessibleContext().setAccessibleDescription(description);
    JMenuItem item1 = new JMenuItem("Системная схема", KeyEvent.VK_S);
    JMenuItem item2 = new JMenuItem("Универсальная схема", KeyEvent.VK_S);

    item1.addActionListener(e -> {
      frame.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
      frame.invalidate();
    });

    item2.addActionListener(e -> {
      frame.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
      frame.invalidate();
    });

    menu.add(item1);
    menu.add(item2);

    return menu;
  }

  public JMenu generateTestMenu(MainApplicationFrame frame, int mnemonicEvent,
      String description) {
    JMenu menu = new JMenu("Тесты");
    menu.setMnemonic(mnemonicEvent);
    menu.getAccessibleContext().setAccessibleDescription(description);
    JMenuItem item1 = new JMenuItem("Сообщение в лог", KeyEvent.VK_S);

    item1.addActionListener(e -> Logger.debug("Новая строка"));

    menu.add(item1);

    return menu;
  }

  public JMenu generateOptionMenu(MainApplicationFrame frame, int mnemonicEvent,
      String description) {
    JMenu menu = new JMenu("Опции");
    menu.setMnemonic(mnemonicEvent);
    menu.getAccessibleContext().setAccessibleDescription(description);
    JMenuItem item1 = new JMenuItem("Выход", KeyEvent.VK_L);

    item1.addActionListener(e -> frame.showConfirmMessage(frame));

    menu.add(item1);

    return menu;
  }

}
