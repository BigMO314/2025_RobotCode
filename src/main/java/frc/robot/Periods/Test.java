package frc.robot.Periods;

import edu.wpi.first.networktables.NetworkTable;
import frc.molib.buttons.Button;
import frc.molib.dashboard.DashboardValue;
import frc.molib.hid.XboxController;
import frc.robot.Robot;
import frc.robot.Subsystems.Chassis;
import frc.robot.Subsystems.Manipulator;

@SuppressWarnings("unused")
public class Test {

    private static NetworkTable tbl_Test = Robot.tblPeriods.getSubTable("Test");

    private Test() {}

    private static XboxController ctlTest = new XboxController(0);

    private static Button btnTest_ResetDistance = new Button() {
        @Override public boolean get() { return ctlTest.getStartButton(); }
    };

    private static Button btnTest_ResetAngle = new Button() {
        @Override public boolean get() { return ctlTest.getBackButton(); }
    };

    private static Button btnTest_forward12 = new Button(){

        @Override public boolean get() { return ctlTest.getAButton(); }

    };

    private static Button btnTest_forward48 = new Button(){

        @Override public boolean get() { return ctlTest.getBButton(); }

    };

    private static Button btnTest_reverse48 = new Button(){

        @Override public boolean get() { return ctlTest.getYButton(); }

    };

    private static Button btnTest_right = new Button(){

        @Override public boolean get() { return ctlTest.getRightBumperButton(); }

    };

    private static Button btnTest_left = new Button(){

        @Override public boolean get() { return ctlTest.getLeftBumperButton(); }

    };

    private static Button btnTest_DisablePIDs = new Button() {
        @Override public boolean get() { return ctlTest.getXButton(); }
    };

    public static void init() {

    }

    public static void start() {}

    public static void syncDashboardValues(){

    }

    public static void periodic() {

        if (btnTest_forward12.getPressed()){
            Chassis.disableAnglePID(); 
            Chassis.resetDistance();
            Chassis.goToDistance(12.00); 
        } else if (btnTest_forward48.getPressed()){ 
            Chassis.disableAnglePID(); 
            Chassis.resetDistance();
            Chassis.goToDistance(48.00); 
        } else if (btnTest_reverse48.getPressed()){ 
            Chassis.disableAnglePID(); 
            Chassis.resetDistance();
            Chassis.goToDistance(-48.00); 
        } else if (btnTest_right.getPressed()){
            Chassis.disableDistancePID(); 
            Chassis.resetAngle();
            Chassis.goToAngle(90.00); 
        } else if (btnTest_left.getPressed()){ 
            Chassis.disableDistancePID(); 
            Chassis.resetAngle();
            Chassis.goToAngle(-180.00);
        }

        if(btnTest_ResetAngle.getPressed()) Chassis.resetAngle();
        if(btnTest_ResetDistance.getPressed()) Chassis.resetDistance();

        if(btnTest_DisablePIDs.getPressed()) Chassis.disablePIDs();

        Chassis.periodic();
        Manipulator.periodic();

    }
    
}
