package gui;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JPanel;

public class GameVisualizer extends JPanel {

  private final Timer m_timer = initTimer();

  private static Timer initTimer() {
    Timer timer = new Timer("events generator", true);
    return timer;
  }

  public double getM_robotPositionX() {
    return m_robotPositionX;
  }

  public double getM_robotPositionY() {
    return m_robotPositionY;
  }

  public void setM_robotPositionX(double m_robotPositionX) {
    this.m_robotPositionX = m_robotPositionX;
  }

  public void setM_robotPositionY(double m_robotPositionY) {
    this.m_robotPositionY = m_robotPositionY;
  }

  private volatile double m_robotPositionX = 100;
  private volatile double m_robotPositionY = 100;
  private volatile double m_robotDirection = 0;

  private volatile int m_targetPositionX = 150;
  private volatile int m_targetPositionY = 100;

  private static final double maxVelocity = 0.1;
  private static final double maxAngularVelocity = 0.001;

  public GameVisualizer() {

    //по таймеру перерисовываем
    m_timer.schedule(new TimerTask() {
      @Override
      public void run() {
        onRedrawEvent();
      }
    }, 0, 50);
    m_timer.schedule(new TimerTask() {
      @Override
      public void run() {
        onModelUpdateEvent();
      }
    }, 0, 10);
    addMouseListener(new MouseAdapter() {
      @Override
      public void mouseClicked(MouseEvent e) {
        setTargetPosition(e.getPoint());
        //обновляем координаты
        repaint();
      }
    });
    setDoubleBuffered(true);
  }

  protected void setTargetPosition(Point p) {
    m_targetPositionX = p.x;
    m_targetPositionY = p.y;
  }

  protected void onRedrawEvent() {
    EventQueue.invokeLater(this::repaint);
  }

  private static double distance(double x1, double y1, double x2, double y2) {
    double diffX = x1 - x2;
    double diffY = y1 - y2;
    return Math.sqrt(diffX * diffX + diffY * diffY);
  }

  private static double angleTo(double fromX, double fromY, double toX, double toY) {
    double diffX = toX - fromX;
    double diffY = toY - fromY;

    return asNormalizedRadians(Math.atan2(diffY, diffX));
  }

  protected void onModelUpdateEvent() {

    //по теореме Пифагора расстояние от
    // нашей точки до робота
    double distance = distance(m_targetPositionX, m_targetPositionY, m_robotPositionX,
        m_robotPositionY);
    if (distance < 0.5) {
      return;
    }
    //это просто скорость
    double velocity = maxVelocity;
    //угол, на который надо развернуться, в полярных координатах
    double angleToTarget = angleTo(m_robotPositionX, m_robotPositionY, m_targetPositionX,
        m_targetPositionY);
    //это угловая скорость
    double angularVelocity = 0;
    if (angleToTarget - m_robotDirection > Math.PI) {
      angularVelocity = -maxAngularVelocity;
    }
    if (angleToTarget - m_robotDirection < -Math.PI) {
      angularVelocity = maxAngularVelocity;
    }
    if (angleToTarget - m_robotDirection < Math.PI && angleToTarget - m_robotDirection >= 0) {
      angularVelocity = maxAngularVelocity;
    }
    if (angleToTarget - m_robotDirection < 0 && angleToTarget - m_robotDirection >= -Math.PI) {
      angularVelocity = -maxAngularVelocity;
    }
    if (unreachable()) {
      angularVelocity = 0;
    }

    moveRobot(velocity, angularVelocity, 10);
  }

  private static double applyLimits(double value, double min, double max) {
    if (value < min) {
      return min;
    }
    if (value > max) {
      return max;
    }
    return value;
  }

  private void moveRobot(double velocity, double angularVelocity, double duration) {
    velocity = applyLimits(velocity, 0, maxVelocity);
    angularVelocity = applyLimits(angularVelocity, -maxAngularVelocity, maxAngularVelocity);
    double newX = m_robotPositionX + velocity / angularVelocity * (
        Math.sin(m_robotDirection + angularVelocity * duration) - Math.sin(m_robotDirection));
    if (!Double.isFinite(newX)) {
      newX = m_robotPositionX + velocity * duration * Math.cos(m_robotDirection);
    }
    double newY = m_robotPositionY - velocity / angularVelocity * (
        Math.cos(m_robotDirection + angularVelocity * duration) - Math.cos(m_robotDirection));
    if (!Double.isFinite(newY)) {
      newY = m_robotPositionY + velocity * duration * Math.sin(m_robotDirection);
    }
    m_robotPositionX = newX;
    m_robotPositionY = newY;
    double newDirection = asNormalizedRadians(m_robotDirection + angularVelocity * duration);
    m_robotDirection = newDirection;
  }

  private static double asNormalizedRadians(double angle) {
    while (angle < 0) {
      angle += 2 * Math.PI;
    }
    while (angle >= 2 * Math.PI) {
      angle -= 2 * Math.PI;
    }
    return angle;
  }

  private static int round(double value) {
    return (int) (value + 0.5);
  }

  @Override
  public void paint(Graphics g) {
    super.paint(g);
    Graphics2D g2d = (Graphics2D) g;
    drawRobot(g2d, round(m_robotPositionX), round(m_robotPositionY), m_robotDirection);
    drawTarget(g2d, m_targetPositionX, m_targetPositionY);
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

  private boolean unreachable() {
    double dx = m_targetPositionX - m_robotPositionX;
    double dy = m_targetPositionY - m_robotPositionY;

    double new_dx = Math.cos(m_robotDirection) * dx + Math.sin(m_robotDirection) * dy;
    double new_dy = Math.cos(m_robotDirection) * dy - Math.sin(m_robotDirection) * dx;

    double y_center = maxVelocity / maxAngularVelocity;
    double dist1 = (Math.sqrt(Math.pow((new_dx), 2) + Math.pow(new_dy - y_center, 2)));
    double dist2 = (Math.sqrt(Math.pow((new_dx), 2) + Math.pow(new_dy + y_center, 2)));

    if (dist1 > maxVelocity / maxAngularVelocity && dist2 > maxVelocity / maxAngularVelocity) {
      return false;
    }
    return true;
  }
}
