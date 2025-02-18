package frc.robot.Periods;

import com.ctre.phoenix6.signals.NeutralModeValue;

import frc.robot.Robot;
import frc.robot.Subsystems.Chassis;

public class Disabled {
    
        private Disabled() {}

    public static void init() {}

    public static void onEnable() {
        Robot.disableSubsystems();
        Chassis.setNeutralMode(NeutralModeValue.Coast);
    }
    
    public static void periodic() {}

}
