package frc.robot.Periods;

import com.ctre.phoenix6.signals.NeutralModeValue;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.networktables.NetworkTable;
import frc.molib.buttons.Button;
import frc.molib.dashboard.DashboardOptionBase;
import frc.molib.dashboard.DashboardSelector;
import frc.molib.hid.XboxController;
import frc.robot.Robot;
import frc.robot.Subsystems.Algae_Mech;
import frc.robot.Subsystems.Chassis;
import frc.robot.Subsystems.Manipulator;

@SuppressWarnings("unused")
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

    private static final double DRIVE_POWER = 0.75;

    private static XboxController ctlDrive = new XboxController(0);
    private static XboxController ctlOperate = new XboxController(1);

    private static Button btnDrive_Brake = new Button() {
        @Override public boolean get() { return ctlDrive.getRightBumperButton(); }
    };

    private static Button btnDrive_Boost = new Button() {
        @Override public boolean get() { return ctlDrive.getLeftBumperButton(); }
    };

    private static Button btnOperate_Bottom = new Button() {
        @Override public boolean get() { return ctlOperate.getLeftBumperButton(); }
    };

    private static Button btnOperate_Elevator_L1 = new Button() {
        @Override public boolean get() { return ctlOperate.getAButton(); }
    };

    private static Button btnOperate_Elevator_L2 = new Button() {
        @Override public boolean get() { return ctlOperate.getBButton(); }
    };

    private static Button btnOperate_Elevator_L3 = new Button() {
        @Override public boolean get() { return ctlOperate.getXButton(); }
    };

    private static Button btnOperate_Elevator_L4 = new Button() {
        @Override public boolean get() { return ctlOperate.getYButton(); }
    };

    private static Button btnOperate_Dispense = new Button() {
        @Override public boolean get() { return ctlOperate.getRightTrigger(); }
    };

    private static Button btnOperate_ReverseDispense = new Button() {
        @Override public boolean get() { return ctlOperate.getLeftTrigger(); }
    };

    private static Button btnOperate_Elevator_manualUp = new Button() {
        @Override public boolean get() { return ctlOperate.getPOV() == 0; }
    };

    private static Button btnOperate_Elevator_manualDown = new Button(){
        @Override public boolean get() { return ctlOperate.getPOV() == 180; }
    };

    private static Button btnOperate_Winch_Up = new Button(){
        @Override public boolean get() { return ctlOperate.getYButton(); }
    };

    private static Button btnOperate_Winch_Down = new Button(){
        @Override public boolean get() { return ctlOperate.getAButton(); }
    };

    private static Button btnOperate_Roller_On = new Button(){
        @Override public boolean get() { return ctlOperate.getLeftTrigger(); }
    };

    private static Button btnOperate_Roller_Reverse = new Button(){
        @Override public boolean get() { return ctlOperate.getRightTrigger(); }
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

        return value * (btnDrive_Boost.get() ? DRIVE_POWER : DRIVE_POWER * 0.66);

    }

    private static void setTankDrive(double left, double right){

        Chassis.setDrivePower(left, right);

    }

    //FIXME Power Scale
    private static void setArcadeDrive(double throttle, double steering){
        double scale = btnDrive_Boost.get() ? DRIVE_POWER : DRIVE_POWER * 0.66;

        Chassis.setDrivePower(
            MathUtil.clamp(throttle+steering, -scale, scale),
            MathUtil.clamp(throttle-steering, -scale, scale)
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
            Chassis.setNeutralMode(NeutralModeValue.Coast);
        }

        if (btnOperate_Roller_On.get()){
            Algae_Mech.enableRoller();
        }else if (btnOperate_Roller_Reverse.get()){
            Algae_Mech.reverseRoller();
        } else {
            Algae_Mech.disableRoller();
        }

        if (btnOperate_Winch_Up.get()){
            Algae_Mech.enableWhinch();
        }else if (btnOperate_Winch_Down.get()){
            Algae_Mech.reverseWinch();
        } else {
            Algae_Mech.disableWhinch();
        }

        /*
        if (btnOperate_Bottom.getPressed()){
            Manipulator.goToHeight(Manipulator.Position.Bottom.getHeight());
        }else if(btnOperate_L1.getPressed()){
            Manipulator.goToHeight(Manipulator.Position.L1.getHeight());
        }else if(btnOperate_L2.getPressed()){
            Manipulator.goToHeight(Manipulator.Position.L2.getHeight());
        }else if(btnOperate_L3.getPressed()){
            Manipulator.goToHeight(Manipulator.Position.L3.getHeight());
        }else if(btnOperate_L4.getPressed()){
            Manipulator.goToHeight(Manipulator.Position.L4.getHeight());
        }else if(btnOperate_manualUp.get()){
            Manipulator.disableElevatorPID();
            Manipulator.raiseElevator();
        }else if(btnOperate_manualDown.get()){
            Manipulator.disableElevatorPID();
            Manipulator.lowerElevator();
        }else if(btnOperate_Dispense.get()){
            Manipulator.enableWheels();
        }else if(btnOperate_ReverseDispense.get()){
            Manipulator.reverseWheels();
        }
        */
        

        Chassis.periodic();
        //Manipulator.periodic();
        Algae_Mech.periodic();
    
    }

}
