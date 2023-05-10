package gui.Game;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.JPanel;

public class GameView extends JPanel implements interactiveWithModel {


  private final Timer m_timer = initTimer();

  private static Timer initTimer() {
    Timer timer = new Timer("events generator", true);
    return timer;
  }

  //опять же передаю this, не знаю как лучше сделать по-другому чтоб проверять
  // , что с моделью можно взаимодействовать
  @Override
  public void paint(Graphics g) {
    super.paint(g);
    Graphics2D g2d = (Graphics2D) g;
    drawRobot(g2d, GeometryComputor.round(model.getRobot().robotPosition.positionX),
        GeometryComputor.round(model.getRobot().robotPosition.positionY), model.getRobot().m_robotDirection);
    drawTarget(g2d, model.getRobot().robotTargetPosition.targetX, model.getRobot().robotTargetPosition.targetY);
  }

  private final GameModel model = new GameModel();
  private final GameController controller = new GameController(this);
  //Пытаюсь сделать так, чтобы контроллер отслеживал события с того же окна, что и
  //View, вы говорили плохая практика передавать this, но нет идей как по-другому это
  //релизовать

  public GameView() {

    //по таймеру перерисовываем
    m_timer.schedule(new TimerTask() {
      @Override
      public void run() {
        onRedrawEvent();
      }
    }, 0, 50);
    //контроллер отслеживает действия пользователя
    controller.getCurrentWorkPanel().addMouseListener(new MouseAdapter() {
      @Override
      public void mouseClicked(MouseEvent e) {
        Point point = e.getPoint();
        //чтобы точка ставилась где надо
        double scale = Toolkit.getDefaultToolkit().getScreenResolution() / 150.0;
        point.x = (int) (point.x / scale);
        point.y = (int) (point.y / scale);
        //передает данные модели
        model.setTargetPosition(point);
        repaint();
      }
    });
    setDoubleBuffered(true);
  }

  protected void onRedrawEvent() {
    EventQueue.invokeLater(this::repaint);
  }

  private static void fillOval(Graphics g, int centerX, int centerY, int diam1, int diam2) {
    g.fillOval(centerX - diam1 / 2, centerY - diam2 / 2, diam1, diam2);
  }

  private static void drawOval(Graphics g, int centerX, int centerY, int diam1, int diam2) {
    g.drawOval(centerX - diam1 / 2, centerY - diam2 / 2, diam1, diam2);
  }

  private void drawRobot(Graphics2D g, int x, int y, double direction) {

    if (x < 0) {
      x = 0;
    }
    if (y < 0) {
      y = 0;
    }
    if (y > getHeight()) {
      y = getHeight();
    }
    if (x > getWidth()) {
      x = getWidth();
    }

    int robotCenterX = x;
    int robotCenterY = y;
    AffineTransform t = AffineTransform.getRotateInstance(direction, robotCenterX, robotCenterY);
    g.setTransform(t);
    g.setColor(Color.MAGENTA);
    fillOval(g, robotCenterX, robotCenterY, 30, 10);
    g.setColor(Color.BLACK);
    drawOval(g, robotCenterX, robotCenterY, 30, 10);
    g.setColor(Color.WHITE);
    fillOval(g, robotCenterX + 10, robotCenterY, 5, 5);
    g.setColor(Color.BLACK);
    drawOval(g, robotCenterX + 10, robotCenterY, 5, 5);
  }

  private void drawTarget(Graphics2D g, int x, int y) {
    AffineTransform t = AffineTransform.getRotateInstance(0, 0, 0);
    g.setTransform(t);
    g.setColor(Color.GREEN);
    fillOval(g, x, y, 5, 5);
    g.setColor(Color.BLACK);
    drawOval(g, x, y, 5, 5);
  }
}
