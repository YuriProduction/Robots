package gui.Game;

import java.awt.Point;
import java.util.Timer;
import java.util.TimerTask;

public class GameModel {

  private final gui.Game.Robot robot = new Robot();

  private final GeometryComputor geometryComputor = new GeometryComputor();

  public Robot getRobot() {
    return robot;
  }


  private static Timer initTimer() {
    return new Timer("events generator", true);
  }

  public GameModel() {
    Timer m_timer = initTimer();
    m_timer.schedule(new TimerTask() {
      @Override
      public void run() {
        onModelUpdateEvent();
      }
    }, 0, 10);
  }

  protected void setTargetPosition(Point p) {
    robot.robotTargetPosition.targetX = p.x;
    robot.robotTargetPosition.targetY = p.y;

  }


  protected void onModelUpdateEvent() {

    //по теореме Пифагора расстояние от
    // нашей точки до робота
    double distance = GeometryComputor.distance(robot.robotTargetPosition, robot.robotPosition);
    if (distance < 0.5) {
      return;
    }
    //это просто скорость
    double velocity = robot.maxVelocity;
    //угол, на который надо развернуться, в полярных координатах
    double angleToTarget = GeometryComputor.angleTo(robot.robotPosition, robot.robotTargetPosition);
    //это угловая скорост
    moveRobot(velocity, geometryComputor.handleAngularVelocity(angleToTarget, robot));
  }

  protected void moveRobot(double velocity, double angularVelocity) {
    velocity = GeometryComputor.applyLimits(velocity, 0, robot.maxVelocity);
    angularVelocity = GeometryComputor.applyLimits(angularVelocity, -robot.maxAngularVelocity,
        robot.maxAngularVelocity);
    double newX = robot.robotPosition.positionX + velocity / angularVelocity * (
        Math.sin(robot.m_robotDirection + angularVelocity * (double) 10) - Math.sin(
            robot.m_robotDirection));
    if (!Double.isFinite(newX)) {
      newX =
          robot.robotPosition.positionX + velocity * (double) 10 * Math.cos(robot.m_robotDirection);
    }
    double newY = robot.robotPosition.positionY - velocity / angularVelocity * (
        Math.cos(robot.m_robotDirection + angularVelocity * (double) 10) - Math.cos(
            robot.m_robotDirection));
    if (!Double.isFinite(newY)) {
      newY =
          robot.robotPosition.positionY + velocity * (double) 10 * Math.sin(robot.m_robotDirection);
    }
    robot.robotPosition.positionX = newX;
    robot.robotPosition.positionY = newY;
    robot.m_robotDirection = GeometryComputor.asNormalizedRadians(
        robot.m_robotDirection + angularVelocity * (double) 10);
  }


}
