package gui;

import Serialization.AbstractJInternalFrameSerializator;
import Serialization.DataOfFrame;
import Serialization.PreferencesDemo;
import Serialization.Changeable;
import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.TextArea;

import java.beans.PropertyVetoException;
import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;
import javax.swing.JInternalFrame;
import javax.swing.JPanel;

import log.LogChangeListener;
import log.LogEntry;
import log.LogWindowSource;

public class LogWindow extends AbstractJInternalFrameSerializator implements LogChangeListener {

  private LogWindowSource m_logSource;
  private TextArea m_logContent;

  public LogWindow(LogWindowSource logSource, String name) {
    super("Протокол работы", true, true, true, true);
    this.setName(name);
    m_logSource = logSource;
    m_logSource.registerListener(this);
    m_logContent = new TextArea("");
    m_logContent.setSize(200, 500);

    JPanel panel = new JPanel(new BorderLayout());
    panel.add(m_logContent, BorderLayout.CENTER);
    getContentPane().add(panel);
    pack();
    updateLogContent();
  }

  private void updateLogContent() {
    StringBuilder content = new StringBuilder();
    for (LogEntry entry : m_logSource.all()) {
      content.append(entry.getMessage()).append("\n");
    }
    m_logContent.setText(content.toString());
    m_logContent.invalidate();
  }

  @Override
  public void onLogChanged() {
    EventQueue.invokeLater(this::updateLogContent);
  }
}
