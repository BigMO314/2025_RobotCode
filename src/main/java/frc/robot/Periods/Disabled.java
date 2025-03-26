package frc.robot.Periods;

import com.ctre.phoenix6.signals.NeutralModeValue;

import frc.molib.buttons.ButtonManager;
import frc.robot.Robot;
import frc.robot.Subsystems.Chassis;

public class Disabled {
    
        private Disabled() {}

    public static void init() {}

    public static void start() {
        Robot.disableSubsystems();
        Chassis.setNeutralMode(NeutralModeValue.Coast);
    }
    
    public static void periodic() {}

    public static void end() {
        ButtonManager.clearFlags();
    }

}
