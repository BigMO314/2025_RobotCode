package frc.robot.Subsystems;

import com.ctre.phoenix.motorcontrol.can.VictorSPX;

import edu.wpi.first.networktables.NetworkTable;
import frc.robot.Robot;

@SuppressWarnings("unused")
public class Manipulator {
    
    private static NetworkTable tblManipulator = Robot.tblSubsystems.getSubTable("Manipulator");

    private static VictorSPX mtrManipulator_L = new VictorSPX(6);
    private static VictorSPX mtrManipulator_R = new VictorSPX(7);

}
