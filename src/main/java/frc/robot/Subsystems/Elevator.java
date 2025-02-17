package frc.robot.Subsystems;

import com.ctre.phoenix6.configs.MotorOutputConfigs;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.InvertedValue;
import com.ctre.phoenix6.signals.NeutralModeValue;

import edu.wpi.first.networktables.NetworkTable;
import frc.molib.PIDController;
import frc.molib.dashboard.DashboardValue;
import frc.robot.Robot;

@SuppressWarnings("unused")
public class Elevator {

    public enum Position{
        Bottom(0.0),
        L1(0.0),
        L2(0.0),
        L3(0.0),
        L4(0.0);

        private final double HEIGHT;
        private Position(double height) { HEIGHT = height; }
        public double getHeight() { return HEIGHT; }

    }
    
    private static NetworkTable tblElevator = Robot.tblSubsystems.getSubTable("Elevator");

    private static NetworkTable tblElevatorHeightPID = tblElevator.getSubTable("Elevator Height PID");

    public static DashboardValue<Double> dshElevatorHeight = new DashboardValue<Double>(tblElevator, "Height");

    private static DashboardValue<Double> dshElevator_Height_P = new DashboardValue<Double>(tblElevatorHeightPID, "P Value");
    private static DashboardValue<Double> dshElevator_Height_I = new DashboardValue<Double>(tblElevatorHeightPID, "I Value");
    private static DashboardValue<Double> dshElevator_Height_D = new DashboardValue<Double>(tblElevatorHeightPID, "D Value");

    private static DashboardValue<Boolean> dshElevator_Height_OnTarget = new DashboardValue<Boolean>(tblElevatorHeightPID, "On Target");

    private static TalonFX mtrElevator = new TalonFX(5);

    private static PIDController pidElevatorHeight = new PIDController(0.0, 0.0, 0.0);

    private static final double GEAR_RATIO = 1.0;
    private static final double SPROCKET_DIAMETER = 1.0;

    private static double mElevatorPower;

    private Elevator(){}
    
    public static void init(){

        mtrElevator.getConfigurator().apply(new MotorOutputConfigs()
            .withInverted(InvertedValue.Clockwise_Positive)
            .withNeutralMode(NeutralModeValue.Brake)
            );

            dshElevator_Height_P.set(pidElevatorHeight.getP());
            dshElevator_Height_I.set(pidElevatorHeight.getI());
            dshElevator_Height_D.set(pidElevatorHeight.getD());

    }

    public static void syncDashboardValues(){

        pidElevatorHeight.setP(dshElevator_Height_P.get());
        pidElevatorHeight.setI(dshElevator_Height_I.get());
        pidElevatorHeight.setD(dshElevator_Height_D.get());

    }

    public static void disable(){
        
        setElevatorPower(0.0);
        disableDistancePID();
    
    }

    public static void disableDistancePID(){

        pidElevatorHeight.disable();

    }

    public static Double getHeight(){
        
        return mtrElevator.getPosition().getValueAsDouble() * GEAR_RATIO * SPROCKET_DIAMETER * Math.PI;

    }

    public static void goToHeight(double height){
        
        pidElevatorHeight.enable();
        pidElevatorHeight.setSetpoint(height);

    }

    public static boolean isAtHeight(){

        return pidElevatorHeight.atSetpoint();
    
    }

    public static void setElevatorPower(double power){

        mElevatorPower = power;
    
    }

    public static void periodic(){

        if(pidElevatorHeight.isEnabled()){
            double power = pidElevatorHeight.calculate(getHeight());
            setElevatorPower(power);
        }

        mtrElevator.set(mElevatorPower);

    }

}
