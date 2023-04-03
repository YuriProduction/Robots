package gui;

import static org.junit.jupiter.api.Assertions.assertNotEquals;

import Localization.Languages.Russifier;
import java.awt.AWTException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class MainApplicationFrameTest {

  MainApplicationFrame frame1;
  MainApplicationFrame frame2;

  @BeforeEach
  void prepareData() throws AWTException {
    frame1 = new MainApplicationFrame(new Russifier());
    frame1.setTitle("TestFrame1");
    frame2 = new MainApplicationFrame(new Russifier());
    frame2.setTitle("TestFrame2");
  }

  @Test
  public void testTitles() {
    assertNotEquals(frame1.getTitle(), frame2.getTitle());
  }

  @Test
  public void testEnables() {
    frame1.setEnabled(false);
    assertNotEquals(frame1.isEnabled(), frame2.isEnabled());
  }


}