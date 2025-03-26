package frc.robot.Subsystems;

import com.ctre.phoenix6.configs.MotorOutputConfigs;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.InvertedValue;
import com.ctre.phoenix6.signals.NeutralModeValue;

import edu.wpi.first.networktables.NetworkTable;
import frc.robot.Robot;

@SuppressWarnings("unused")
public class Manipulator {
    
    //Network Tables
    private static NetworkTable tblManipulator = Robot.tblSubsystems.getSubTable("Manipulator");

    //Motor
    private static TalonFX mtrRoller = new TalonFX(5);

    //Power Buffers
    private static double mRoller;

    //called at robot startup
    public static void init(){

        mtrRoller.getConfigurator().apply(new MotorOutputConfigs()
            .withInverted(InvertedValue.Clockwise_Positive)
            .withNeutralMode(NeutralModeValue.Brake)
        );

    }

    public static void syncDashboardValues() {
        
    }

    public static void disable() {
        disableRoller();
    }

    public static void enableRoller() {
        setRollerPower(0.15);
    }

    public static void disableRoller() {
        setRollerPower(0.0);
    }

    public static void reverseRoller() {
        setRollerPower(-0.25);
    }

    public static void setRollerPower(double power) {
        mRoller = power;
    }

    public static void setNeutralMode() {
        mtrRoller.setNeutralMode(NeutralModeValue.Brake);
    }

    public static void periodic(){

        mtrRoller.set(mRoller);

    }

}
