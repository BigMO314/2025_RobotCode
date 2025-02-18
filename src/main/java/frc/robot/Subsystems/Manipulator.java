package frc.robot.Subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;
import com.ctre.phoenix6.configs.MotorOutputConfigs;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.InvertedValue;
import com.ctre.phoenix6.signals.NeutralModeValue;

import edu.wpi.first.networktables.NetworkTable;
import frc.molib.Console;
import frc.molib.PIDController;
import frc.molib.dashboard.DashboardValue;
import frc.robot.Robot;

@SuppressWarnings("unused")
public class Manipulator {
    
    /**Positions for elevator to score*/
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

    //Network Tables
    private static NetworkTable tblManipulator = Robot.tblSubsystems.getSubTable("Manipulator");
    private static NetworkTable tblElevatorHeightPID = tblManipulator.getSubTable("Elevator Height PID");
    public static DashboardValue<Double> dshElevatorHeight = new DashboardValue<Double>(tblManipulator, "Height");

    //Elevator height PID Values
    private static DashboardValue<Double> dshElevator_Height_P = new DashboardValue<Double>(tblElevatorHeightPID, "P Value");
    private static DashboardValue<Double> dshElevator_Height_I = new DashboardValue<Double>(tblElevatorHeightPID, "I Value");
    private static DashboardValue<Double> dshElevator_Height_D = new DashboardValue<Double>(tblElevatorHeightPID, "D Value");

    private static DashboardValue<Boolean> dshElevator_Height_OnTarget = new DashboardValue<Boolean>(tblElevatorHeightPID, "On Target");

    //Motors
    private static TalonFX mtrElevator = new TalonFX(5);
    private static VictorSPX mtrManipulator_L = new VictorSPX(6);
    private static VictorSPX mtrManipulator_R = new VictorSPX(7);

    //PID Controller
    private static PIDController pidElevatorHeight = new PIDController(0.0, 0.0, 0.0);

    //Constants 
    private static final double GEAR_RATIO = 1.0;
    private static final double SPROCKET_DIAMETER = 1.0;

    //Power Buffers 
    private static double mElevatorPower;
    private static double mPower;

    /**Unused Constructor */
    private Manipulator(){}

    /** Called at robot startup */
    public static void init(){
        Console.printHeader("Initializing Manipulator");

        Console.logMsg("Configurating Motors");
        mtrElevator.getConfigurator().apply(new MotorOutputConfigs()
            .withInverted(InvertedValue.Clockwise_Positive)
            .withNeutralMode(NeutralModeValue.Brake)
            );

        mtrManipulator_L.setInverted(true);
        mtrManipulator_R.setInverted(false);

        Console.logMsg("Initializing Dashboard Values");
        dshElevator_Height_P.set(pidElevatorHeight.getP());
        dshElevator_Height_I.set(pidElevatorHeight.getI());
        dshElevator_Height_D.set(pidElevatorHeight.getD());


        Console.logMsg("Manipulator Initualization Complete!");
    }

    /**
     * Synchronizes values between dashboard and robot
     */
    public static void syncDashboardValues(){

        //Update PID values
        pidElevatorHeight.setP(dshElevator_Height_P.get());
        pidElevatorHeight.setI(dshElevator_Height_I.get());
        pidElevatorHeight.setD(dshElevator_Height_D.get());

    }

    /**Disables whole subsystem*/
    public static void disable(){

        setElevatorPower(0.0);
        setManipulatorPower(0.0);
        disableElevatorPID();

    }

    /**Disables Elevator power and Elevator PID control*/
    public static void disableElevator(){
        
        setElevatorPower(0.0);
        disableElevatorPID();
    
    }

    /**Disables Manipulator power*/
    public static void disableManipulator(){

        setManipulatorPower(0.0);

    }

    /**Disables Elevator PID control*/
    public static void disableElevatorPID(){

        pidElevatorHeight.disable();

    }

    /**Finds current height of Elevator */
    public static double getHeight(){
        
        return mtrElevator.getPosition().getValueAsDouble() * GEAR_RATIO * SPROCKET_DIAMETER * Math.PI;

    }

    /**
     * Enables Elevator PID
     * @param height Height the PID will attempt to go to
     */
    public static void goToHeight(double height){
        
        pidElevatorHeight.enable();
        pidElevatorHeight.setSetpoint(height);

    }

    /**Returns Elevator height to PID */
    public static boolean isAtHeight(){

        return pidElevatorHeight.atSetpoint();
    
    }

    /**Sets Elevator Power */
    public static void setElevatorPower(double power){

        mElevatorPower = power;
    
    }

    public static void lowerElevator(){

        setElevatorPower(-0.1);

    }

    public static void raiseElevator(){

        setElevatorPower(0.5);

    }

    /**Sets Manipulator Power */
    public static void setManipulatorPower(double power){
    
        mPower = power;
    
    }

    public static void enableManipulator() {

        setManipulatorPower(1.0);

    }

    public static void reverseManipulator(){

        setManipulatorPower(-0.5);

    }
    
    public static void periodic(){

        if(pidElevatorHeight.isEnabled()){
            setElevatorPower(pidElevatorHeight.calculate(getHeight()));
        }

        mtrElevator.set(mElevatorPower);
        mtrManipulator_L.set(ControlMode.PercentOutput, mPower);
        mtrManipulator_R.set(ControlMode.PercentOutput, mPower);


    }

}
