package frc.robot.Subsystems;

import com.ctre.phoenix6.configs.MotorOutputConfigs;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.InvertedValue;
import com.ctre.phoenix6.signals.NeutralModeValue;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import frc.molib.PIDController;
import frc.molib.dashboard.DashboardValue;
import frc.robot.Robot;

@SuppressWarnings("unused")
public class Chassis {

    private static NetworkTable tblChassis = Robot.tblSubsystems.getSubTable("Chassis");

    private static NetworkTable tblDriveDistancePID = tblChassis.getSubTable("Drive Distance PID");
    private static NetworkTable tblDriveAnglePID = tblChassis.getSubTable("Drive Angle PID");

    private static DashboardValue<Double> dshDrive_Distance = new DashboardValue<Double>(tblChassis, "Distance");   
    private static DashboardValue<Double> dshDrive_Angle = new DashboardValue<Double>(tblChassis, "Angle");

    private static DashboardValue<Double> dshDrive_Distance_P = new DashboardValue<Double>(tblDriveDistancePID, "P Value");
    private static DashboardValue<Double> dshDrive_Distance_I = new DashboardValue<Double>(tblDriveDistancePID, "I Value");
    private static DashboardValue<Double> dshDrive_Distance_D = new DashboardValue<Double>(tblDriveDistancePID, "D Value");

    private static DashboardValue<Boolean> dshDrive_Distance_OnTarget = new DashboardValue<Boolean>(tblDriveDistancePID, "On Target");

    private static DashboardValue<Double> dshDrive_Angle_P = new DashboardValue<Double>(tblDriveAnglePID, "P Value");
    private static DashboardValue<Double> dshDrive_Angle_I = new DashboardValue<Double>(tblDriveAnglePID, "I Value");
    private static DashboardValue<Double> dshDrive_Angle_D = new DashboardValue<Double>(tblDriveAnglePID, "D Value");

    private static DashboardValue<Boolean> dshDrive_Angle_OnTarget = new DashboardValue<Boolean>(tblDriveAnglePID, "On Target");

    private static TalonFX mtrDrive_L1 = new TalonFX(1);
    private static TalonFX mtrDrive_L2 = new TalonFX(2);

    private static TalonFX mtrDrive_R1 = new TalonFX(3);
    private static TalonFX mtrDrive_R2 = new TalonFX(4);

    private static ADXRS450_Gyro gyrDrive = new ADXRS450_Gyro();

    private static PIDController pidDriveDistance = new PIDController(0, 0, 0);
    private static PIDController pidDriveAngle = new PIDController(0, 0, 0);

    private static final double WHEEL_DIAMETER = 4.0;
    private static final double GEAR_RATIO = 1/6;

    private static double mLeftPower;
    private static double mRightPower;

    private Chassis(){}

    public static void init(){
        
        mtrDrive_L1.getConfigurator().apply(new MotorOutputConfigs()
            .withInverted(InvertedValue.Clockwise_Positive)
            .withNeutralMode(NeutralModeValue.Coast));
        mtrDrive_L2.getConfigurator().apply(new MotorOutputConfigs()
            .withInverted(InvertedValue.Clockwise_Positive)
            .withNeutralMode(NeutralModeValue.Coast));
        mtrDrive_R1.getConfigurator().apply(new MotorOutputConfigs()
            .withInverted(InvertedValue.CounterClockwise_Positive)
            .withNeutralMode(NeutralModeValue.Coast));
        mtrDrive_R2.getConfigurator().apply(new MotorOutputConfigs()
            .withInverted(InvertedValue.CounterClockwise_Positive)
            .withNeutralMode(NeutralModeValue.Coast));

        dshDrive_Distance_P.set(pidDriveDistance.getP());
        dshDrive_Distance_I.set(pidDriveDistance.getI());
        dshDrive_Distance_D.set(pidDriveDistance.getD());

        dshDrive_Angle_P.set(pidDriveAngle.getP());
        dshDrive_Angle_I.set(pidDriveAngle.getI());
        dshDrive_Angle_D.set(pidDriveAngle.getD());
    

    }

    public static void syncDashboardValues(){

        pidDriveDistance.setP(dshDrive_Distance_P.get());
        pidDriveDistance.setI(dshDrive_Distance_I.get());
        pidDriveDistance.setD(dshDrive_Distance_D.get());

        pidDriveAngle.setP(dshDrive_Angle_P.get());
        pidDriveAngle.setI(dshDrive_Angle_I.get());
        pidDriveAngle.setD(dshDrive_Angle_D.get());

    }

    public static void setNeutralMode(NeutralModeValue mode){

        mtrDrive_L1.setNeutralMode(mode);
        mtrDrive_L2.setNeutralMode(mode);
        mtrDrive_R1.setNeutralMode(mode);
        mtrDrive_R2.setNeutralMode(mode);

    }

    public static void disable(){
        setDrivePower(0.0, 0.0);
        disablePIDs();
    }

    public static void disablePIDs(){
        disableDistancePID();
        disableAnglePID();
    }

    public static void disableDistancePID(){
        pidDriveDistance.disable();
    }

    public static void disableAnglePID(){
        pidDriveAngle.disable();
    }

    public static Double getDistance(){

        return mtrDrive_L1.getPosition().getValueAsDouble() * WHEEL_DIAMETER * GEAR_RATIO * Math.PI;
        
    }

    public static Double getAngle(){
        return gyrDrive.getAngle();
    }

    public static void goToDistance(double distance){
        pidDriveDistance.enable();
        pidDriveDistance.setSetpoint(distance);
    }

    public static void goToAngle(double angle){
        pidDriveAngle.enable();
        pidDriveAngle.setSetpoint(angle);
    }

    public static boolean isAtDistance(){
        return pidDriveDistance.atSetpoint();
    }

    public static boolean isAtAngle(){
        return pidDriveAngle.atSetpoint();
    }

    public static void setDrivePower(double leftPower, double rightPower){
        mLeftPower = leftPower;
        mRightPower = rightPower;
    }

    public static void periodic(){

        if(pidDriveDistance.isEnabled()){
            double power = pidDriveDistance.calculate(getDistance());
            setDrivePower(power, power);
        }
        else
        if(pidDriveAngle.isEnabled()){
            double power = pidDriveAngle.calculate(getAngle());
            setDrivePower(power, -power);
        }

        mtrDrive_L1.set(mLeftPower);
        mtrDrive_L2.set(mLeftPower);

        mtrDrive_R1.set(mRightPower);
        mtrDrive_R2.set(mRightPower);


    }


}
