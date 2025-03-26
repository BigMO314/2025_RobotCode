package frc.robot.Periods;

import com.ctre.phoenix6.signals.NeutralModeValue;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.networktables.NetworkTable;
import frc.molib.buttons.Button;
import frc.molib.dashboard.DashboardOptionBase;
import frc.molib.dashboard.DashboardSelector;
import frc.molib.hid.XboxController;
import frc.robot.Robot;
import frc.robot.Subsystems.Chassis;
import frc.robot.Subsystems.Manipulator;

public class Teleoperated {
    
    public enum DriveStyle implements DashboardOptionBase {

        TANK_DRIVE("Tank"),
        ARCADE_DRIVE("Arcade"),
        CHEEZY_DRIVE("Cheesy");

        public static final String getKey() { return "Drive Style"; }
        public static final DriveStyle getDefaultOption() { return CHEEZY_DRIVE; }

        private final String label;

        private DriveStyle(String label) { this.label = label; }

        public String getLabel() { return label; }
    }

    public enum DriveExponent {

        NONE,
        SQUARED,
        CUBED;

    }

    private static NetworkTable tblTeleoperated = Robot.tblPeriods.getSubTable("Teleoperated");

    private static DashboardSelector<DriveStyle> dshDriveStyle = new DashboardSelector<DriveStyle>(tblTeleoperated, DriveStyle.getKey(), DriveStyle.getDefaultOption());

    private static DriveStyle mSelectedDriveStyle = DriveStyle.getDefaultOption();

    private static final DriveExponent mSelectedDriveExponent = DriveExponent.CUBED;

    private static final double STANDARD_POWER = 0.75;

    private static final double PRECISION_POWER = 0.2;
    private static final double BOOST_POWER = 0.9;

    private static XboxController ctlDrive = new XboxController(0);

    //Driver Buttons
    private static Button btnDrive_Brake = new Button() {
        @Override public boolean get() { return ctlDrive.getRightBumperButton(); }
    };

    private static Button btnDrive_Precision = new Button() { 
        @Override public boolean get() { return ctlDrive.getLeftTrigger(); }
    };

    private static Button btnDrive_Boost = new Button() {
        @Override public boolean get() { return ctlDrive.getLeftBumperButton(); }
    };

    //Manipulator Buttons
    private static Button btnOperate_Dispense = new Button() {
        @Override public boolean get() { return ctlDrive.getBButton(); }
    };

    private static Button btnOperate_ReverseDispense = new Button() {
        @Override public boolean get() { return ctlDrive.getXButton(); }
    };

    public static void start(){

        ctlDrive.configYAxisInverted(true);

        mSelectedDriveStyle = dshDriveStyle.get();

        Chassis.setNeutralMode(NeutralModeValue.Coast);

    }

    public static void init(){

    }

    public static void syncDashboardValues(){

    }

    public static double processDriverInput(double value, double scale){

        if(mSelectedDriveExponent == DriveExponent.SQUARED){
            value = value * value * Math.signum(value);
        }else if(mSelectedDriveExponent == DriveExponent.CUBED){
            value = value * value*value;
        }

        return value * scale;

    }

    private static void setTankDrive(double left, double right, double scale){

        Chassis.setDrivePower(left, right);

    }

    private static void setArcadeDrive(double throttle, double steering, double scale){
        //double newScale = btnDrive_Boost.get() ? DRIVE_POWER : DRIVE_POWER * 0.66;

        Chassis.setDrivePower(
            MathUtil.clamp(throttle+steering, -scale, scale),
            MathUtil.clamp(throttle-steering, -scale, scale)
        );

    }

    public static void periodic(){
        double drivePower;
        if(btnDrive_Precision.get()) drivePower = PRECISION_POWER;
        else if(btnDrive_Boost.get()) drivePower = BOOST_POWER;
        else drivePower = STANDARD_POWER;

        if(mSelectedDriveStyle == DriveStyle.TANK_DRIVE){
            setTankDrive(processDriverInput(ctlDrive.getLeftY(), drivePower), processDriverInput(ctlDrive.getRightY(), drivePower), drivePower);
        }else if (mSelectedDriveStyle == DriveStyle.ARCADE_DRIVE){
            setArcadeDrive(processDriverInput(ctlDrive.getLeftY(), drivePower), processDriverInput(ctlDrive.getLeftX(), drivePower), drivePower);
        }else if(mSelectedDriveStyle == DriveStyle.CHEEZY_DRIVE){
            setArcadeDrive(processDriverInput(ctlDrive.getLeftY(), drivePower), processDriverInput(ctlDrive.getRightX(), drivePower), drivePower);
        }

        if (btnDrive_Brake.getPressed()){
            Chassis.setNeutralMode(NeutralModeValue.Brake);
        }else if(btnDrive_Brake.getReleased()){
            Chassis.setNeutralMode(NeutralModeValue.Coast);
        }

        //Manipulator Controls
        if(btnOperate_Dispense.get()){
            Manipulator.enableRoller();
        }else if (btnOperate_ReverseDispense.get()){
            Manipulator.reverseRoller();
        }else {
            Manipulator.disableRoller();
        }

        Chassis.periodic();
        Manipulator.periodic();
    
    }

}
