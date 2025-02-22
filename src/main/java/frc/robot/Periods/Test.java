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

    private static DashboardValue<Double> dshElevatorHeight = new DashboardValue<Double>(tbl_Test, "Height");

    private Test() {}

    private static XboxController ctlTest = new XboxController(0);

    private static Button btnTest_forward12 = new Button(){

        @Override public boolean get() { return ctlTest.getLeftBumperButtonPressed(); }

    };

    private static Button btnTest_right90 = new Button(){

        @Override public boolean get() { return ctlTest.getRightBumperButtonPressed(); }

    };

    private static Button btnTest_Bottom = new Button(){

        @Override public boolean get() { return ctlTest.getPOV() == 180; }

    };

    private static Button btnTest_L1 = new Button(){

        @Override public boolean get() { return ctlTest.getAButtonPressed(); }

    };

    private static Button btnTest_L2 = new Button(){

        @Override public boolean get() { return ctlTest.getBButtonPressed(); }

    };

    private static Button btnTest_L3 = new Button() {

        @Override public boolean get() { return ctlTest.getXButtonPressed(); }

    };

    public static Button btnTest_L4 = new Button() {

        @Override public boolean get() { return ctlTest.getYButton(); }

    };

    public static Button btnTest_goToDashboard = new Button() {

        @Override public boolean get() { return ctlTest.getPOV() == 0.00; }

    };

    public static void init() {

        dshElevatorHeight.set(0.0);

    }

    public static void start() {}

    public static void syncDashboardValues(){

    }

    public static void periodic() {

        if (btnTest_forward12.getPressed()){ Chassis.goToDistance(12.00); }
        else if (btnTest_right90.getPressed()){ Chassis.goToAngle(90.00); }
        else if (btnTest_Bottom.getPressed()){ Manipulator.goToHeight(Manipulator.Position.Bottom.getHeight()); }
        else if (btnTest_L1.getPressed()){ Manipulator.goToHeight(Manipulator.Position.L1.getHeight()); }
        else if (btnTest_L2.getPressed()){ Manipulator.goToHeight(Manipulator.Position.L2.getHeight()); }
        else if (btnTest_L3.getPressed()){ Manipulator.goToHeight(Manipulator.Position.L3.getHeight()); }
        else if (btnTest_L4.getPressed()){ Manipulator.goToHeight(Manipulator.Position.L4.getHeight()); }
        else if (btnTest_goToDashboard.getPressed()){ Manipulator.goToHeight(dshElevatorHeight.get()); }

    }
    
}
