package frc.robot.Subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;
import com.ctre.phoenix6.configs.MotorOutputConfigs;
import com.ctre.phoenix6.configs.SoftwareLimitSwitchConfigs;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.InvertedValue;
import com.ctre.phoenix6.signals.NeutralModeValue;

import edu.wpi.first.networktables.NetworkTable;
import frc.robot.Robot;

public class Algae_Mech {
    
    //Network Tables
    private static NetworkTable tblAlgae_Mech = Robot.tblSubsystems.getSubTable("Algae_Mech");

    //Motors
    private static VictorSPX mtrRoller = new VictorSPX(8);
    private static TalonFX mtrWinch = new TalonFX(9);

    //Power Buffers
    private static double mRoller;
    private static double mWinch;

    //Called at robot startup
    public static void init() {

        mtrRoller.setInverted(false);

        mtrWinch.getConfigurator().apply(new MotorOutputConfigs()
        .withInverted(InvertedValue.CounterClockwise_Positive)
        .withNeutralMode(NeutralModeValue.Brake)
        );

        mtrWinch.getConfigurator().apply( new SoftwareLimitSwitchConfigs()
        .withForwardSoftLimitThreshold(0.0)
        .withForwardSoftLimitEnable(true));

        mtrWinch.setPosition(0.0);

    }

    /**Determines if wheels are in brake mode or not */
    public static void setNeutralMode() {
        mtrWinch.setNeutralMode(NeutralModeValue.Brake);
        mtrRoller.setNeutralMode(NeutralMode.Brake);
    }

    /**Disables the whole subsystem */
    public static void disable() {
        disableRoller();
        disableWhinch();
    }

    /**Sets the Roller Power */
    public static void setRollerPower(double power) {
        mRoller = power;
    }

    /**Enables the Roller motor */
    public static void enableRoller() {
        setRollerPower(0.25);
    }

    /**Disables the Roller motor */
    public static void disableRoller() {
        setRollerPower(0.0);
    }

    /**Reverses the Roller motor */
    public static void reverseRoller() {
        setRollerPower(-0.5);
    }

    /**Sets the Winch power */
    public static void setWinchPower(double power) {
        mWinch = power;
    }

    /**Enables the Winch motor */
    public static void enableWhinch() {
        setWinchPower(0.2);
    }

    /**Disables the Winch motor */
    public static void disableWhinch() {
        setWinchPower(0.0);
    }

    /**Reverses the Winch motor */
    public static void reverseWinch() {
        setWinchPower(-0.5);
    }

    /**Called periodically tp power the motors*/
    public static void periodic() {

        //Apply power to motors
        mtrRoller.set(ControlMode.PercentOutput, mRoller);
        mtrWinch.set(mWinch);

    }

}
