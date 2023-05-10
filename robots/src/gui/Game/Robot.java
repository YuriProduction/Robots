package gui.Game;

import gui.Game.Coordinates.RobotPosition;
import gui.Game.Coordinates.RobotTargetPosition;

public class Robot {

  public RobotPosition robotPosition = new RobotPosition();
  public RobotTargetPosition robotTargetPosition = new RobotTargetPosition();
  public volatile double m_robotDirection = 0;
  public final double maxVelocity = 0.1;
  public final double maxAngularVelocity = 0.001;

  public void setRobotTargetPosition(int x, int y) {
    this.robotTargetPosition.targetX = x;
    this.robotTargetPosition.targetY = y;
  }
}
