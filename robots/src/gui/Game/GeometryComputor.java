package gui.Game;


import gui.Game.Coordinates.RobotPosition;
import gui.Game.Coordinates.RobotTargetPosition;

/**
 * Класс отвечает за необходимые для игры вычисления
 */
public class GeometryComputor {

  protected static double distance(RobotTargetPosition targetPosition, RobotPosition position) {
    double diffX = targetPosition.targetX - position.positionX;
    double diffY = targetPosition.targetY - position.positionY;
    return Math.sqrt(diffX * diffX + diffY * diffY);
  }

  protected static double angleTo(RobotPosition position, RobotTargetPosition targetPosition) {
    double diffX = targetPosition.targetX - position.positionX;
    double diffY = targetPosition.targetY - position.positionY;
    return asNormalizedRadians(Math.atan2(diffY, diffX));
  }

  protected double handleAngularVelocity(double angleToTarget, final Robot robot) {
    double angularVelocity = 0;
    if (angleToTarget - robot.m_robotDirection > Math.PI) {
      angularVelocity = -robot.maxAngularVelocity;
    }
    if (angleToTarget - robot.m_robotDirection < -Math.PI) {
      angularVelocity = robot.maxAngularVelocity;
    }
    if (angleToTarget - robot.m_robotDirection < Math.PI
        && angleToTarget - robot.m_robotDirection >= 0) {
      angularVelocity = robot.maxAngularVelocity;
    }
    if (angleToTarget - robot.m_robotDirection < 0
        && angleToTarget - robot.m_robotDirection >= -Math.PI) {
      angularVelocity = -robot.maxAngularVelocity;
    }
    if (unreachable(robot)) {
      angularVelocity = 0;
    }
    return angularVelocity;
  }


  protected static double applyLimits(double value, double min, double max) {
    if (value < min) {
      return min;
    }
    return Math.min(value, max);
  }


  public static int round(double value) {
    return (int) (value + 0.5);
  }

  protected boolean unreachable(final Robot robot) {
    double dx = robot.robotTargetPosition.targetX - robot.robotPosition.positionX;
    double dy = robot.robotTargetPosition.targetY - robot.robotPosition.positionY;

    double new_dx = Math.cos(robot.m_robotDirection) * dx + Math.sin(robot.m_robotDirection) * dy;
    double new_dy = Math.cos(robot.m_robotDirection) * dy - Math.sin(robot.m_robotDirection) * dx;

    double y_center = robot.maxVelocity / robot.maxAngularVelocity;
    double dist1 = (Math.sqrt(Math.pow((new_dx), 2) + Math.pow(new_dy - y_center, 2)));
    double dist2 = (Math.sqrt(Math.pow((new_dx), 2) + Math.pow(new_dy + y_center, 2)));

    if (dist1 > robot.maxVelocity / robot.maxAngularVelocity
        && dist2 > robot.maxVelocity / robot.maxAngularVelocity) {
      return false;
    }
    return true;
  }

  protected static double asNormalizedRadians(double angle) {
    while (angle < 0) {
      angle += 2 * Math.PI;
    }
    while (angle >= 2 * Math.PI) {
      angle -= 2 * Math.PI;
    }
    return angle;
  }

}
