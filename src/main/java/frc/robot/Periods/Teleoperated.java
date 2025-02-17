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
import frc.robot.Subsystems.Elevator;
import frc.robot.Subsystems.Manipulator;

@SuppressWarnings("unused")
public class Teleoperated {
    
    public enum DriveStyle implements DashboardOptionBase {

        TANK_DRIVE("Tank"),
        ARCADE_DRIVE("Arcade"),
        CHEEZY_DRIVE("Cheesy");

        public static final String getKey() { return "Drive Style"; }
        public static final DriveStyle getDefaultOption() { return TANK_DRIVE; }

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

    private static final DriveExponent mSelectedDriveExponent = DriveExponent.NONE;

    private static final double DRIVE_POWER = 0.2;

    private static XboxController ctlDrive = new XboxController(0);
    private static XboxController ctlOperate = new XboxController(1);

    private static Button btnDrive_Brake = new Button() {
        @Override public boolean get() { return ctlDrive.getRightBumperButton(); }
    };

    private static Button btnOperate_Bottom = new Button() {
        @Override public boolean get() { return ctlOperate.getAButton(); }
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

    public static double processDriverInput(double value){

        if(mSelectedDriveExponent == DriveExponent.SQUARED){
            value = value*value*Math.signum(value);
        }else if(mSelectedDriveExponent == DriveExponent.CUBED){
            value = value*value*value;
        }

        return value*DRIVE_POWER;

    }

    private static void setTankDrive(double left, double right){

        Chassis.setDrivePower(left, right);

    }

    private static void setArcadeDrive(double throttle, double steering){

        Chassis.setDrivePower(
            MathUtil.clamp(throttle+steering, -DRIVE_POWER, DRIVE_POWER),
            MathUtil.clamp(throttle-steering, -DRIVE_POWER, DRIVE_POWER)
            );

    }

    public static void periodic(){
        if(mSelectedDriveStyle == DriveStyle.TANK_DRIVE){
            setTankDrive(processDriverInput(ctlDrive.getLeftY()), processDriverInput(ctlDrive.getRightY()));
        }else if (mSelectedDriveStyle == DriveStyle.ARCADE_DRIVE){
            setArcadeDrive(processDriverInput(ctlDrive.getLeftY()), processDriverInput(ctlDrive.getLeftX()));
        }else if(mSelectedDriveStyle == DriveStyle.CHEEZY_DRIVE){
            setArcadeDrive(processDriverInput(ctlDrive.getLeftY()), processDriverInput(ctlDrive.getRightX()));
        }

        if (btnDrive_Brake.getPressed()){
            Chassis.setNeutralMode(NeutralModeValue.Brake);
        }else if(btnDrive_Brake.getReleased()){
            Chassis.setNeutralMode(NeutralModeValue.Brake);
        }

        if (btnOperate_Bottom.getPressed()){
            Elevator.goToHeight(Elevator.Position.Bottom.getHeight());
        }
        
    
        Chassis.periodic();
        Elevator.periodic();
        Manipulator.periodic();
    
    }

}
