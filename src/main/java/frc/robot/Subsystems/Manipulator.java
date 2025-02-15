package frc.robot.Subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;

import edu.wpi.first.networktables.NetworkTable;
import frc.robot.Robot;

@SuppressWarnings("unused")
public class Manipulator {
    
    private static NetworkTable tblManipulator = Robot.tblSubsystems.getSubTable("Manipulator");

    private static VictorSPX mtrManipulator_L = new VictorSPX(6);
    private static VictorSPX mtrManipulator_R = new VictorSPX(7);

    private static double mPower;

    private Manipulator(){}

    public static void init(){

        mtrManipulator_L.setInverted(true);
        mtrManipulator_R.setInverted(false);

    }

    public static void setManipulatorPower(double power){
    
        mPower = power;
    
    }

    public static void periodic(){

        mtrManipulator_L.set(ControlMode.PercentOutput, mPower);
        mtrManipulator_R.set(ControlMode.PercentOutput, mPower);

    }

}
