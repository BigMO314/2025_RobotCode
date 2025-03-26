package frc.robot.Periods;

import com.ctre.phoenix6.signals.NeutralModeValue;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.wpilibj.Timer;
import frc.molib.Console;
import frc.molib.dashboard.DashboardOptionBase;
import frc.molib.dashboard.DashboardSelector;
import frc.robot.Robot;
import frc.robot.Subsystems.Chassis;
import frc.robot.Subsystems.Manipulator;

@SuppressWarnings("")

/**Controls the robot through pre programmed sequences during the Autonomous game period */
public class Autonomous {

    private enum StartingPosition implements DashboardOptionBase {
        LEFT("Left"),
        CENTER("Center"),
        RIGHT("Right");

        public static final StartingPosition DEFAULT = CENTER;

        private final String LABEL;

        private StartingPosition(String label) { LABEL = label; }

        public static String getTitle() { return "Starting Position"; }
        public String getLabel() { return LABEL; }
    }

    private enum StartingDelay implements DashboardOptionBase {
        NONE("-No Delay", 0.0),
        ONE_SECOND("1 Second", 1.0),
        TWO_SECONDS("2 seconds", 2.0),
        THREE_SECONDS("3 seconds", 3.0),
        FIVE_SECONDS("5 seconds", 5.0),
        TEN_SECONDS("10 seconds", 10.0);

        public static final StartingDelay DEFAULT = NONE;

        private final String LABEL;
        private final double DELAY;

        private StartingDelay(String label, double delay) {LABEL = label; DELAY = delay;}

        public static String getTitle() { return "Starting Delay"; }
        public String getLabel() { return LABEL; }
        public double getTime() { return DELAY; }
    }

    private enum Sequence implements DashboardOptionBase {

        DO_NOTHING("Do Nothing") {

        },
        PREPARE_FOR_MATCH("Prepare For Match") {
            @Override public void start() {
                switch (mStage) {
                    case 0:
                        tmrStage.reset();
                        mStage++;
                    case 1:
                        if(tmrStage.get() >= mSelectedStartingDelay.getTime()) mStage++;
                        break;
                    case 2:
                        Console.logMsg("Auton Finished!");
                        mStage++;
                    default:
                        Chassis.disable();
                }
            }
        },
        DRIVE_FOWARD("Drive Foward") {
            @Override public void periodic() {
                switch(mStage) {
                    case 0:
                        tmrStage.reset();
                        mStage++;
                    case 1:
                        if(tmrStage.get() >= mSelectedStartingDelay.getTime()) mStage++;
                        break;
                    case 2:
                        Console.logMsg("Driving forward...");
                        Chassis.disableAnglePID();
                        Chassis.resetDistance();
                        Chassis.goToDistance(60.0);
                        tmrStage.reset();
                        mStage++;
                    case 3:
                        if(tmrStage.get() >= 5.0 ||Chassis.isAtDistance()) mStage++;
                        break;
                    default:
                        Console.logMsg("Done Driving Foward!");
                        Robot.disableSubsystems();
                }
            }
        },
        CENTER_SCORE("Center Score") {
            @Override public void periodic() {
                switch(mStage) {
                    case 0: 
                        Console.logMsg("Starting Auton...");
                        tmrStage.reset();
                        mStage++;
                    case 1:
                        if(tmrStage.get() >= mSelectedStartingDelay.getTime()) mStage++;
                        break;
                    case 2:
                        Console.logMsg("Driving Forward...");
                        Chassis.disableAnglePID();
                        Chassis.resetDistance();
                        Chassis.goToDistance(-52.0);
                        tmrStage.reset();
                        mStage++;
                        break;
                    case 3:
                        if(tmrStage.get() >= 1.5 || Chassis.isAtDistance()) mStage++;
                        break;
                    case 4:
                        Console.logMsg("Enabling Manipulator...");
                        Chassis.disable();
                    	Manipulator.enableRoller();
                        tmrStage.reset();
                        mStage++;
                        break;
                    case 5:
                        if(tmrStage.get() >= 1.0) mStage++;
                        break;
                    case 6:
                        Console.logMsg("Auton Finished!");
                        Manipulator.disableRoller();
                        mStage++;
                        break;
                    default:
                        Robot.disableSubsystems();
                }
            }
        },
        SIDE_SCORE("Side Score") {
            @Override public void periodic() {
                switch (mStage) {
                    case 0:
                        Console.logMsg("Starting Auton...");
                        tmrStage.reset();
                        mStage++;
                    case 1:
                        if(tmrStage.get() >= mSelectedStartingDelay.getTime()) mStage++;
                        break;
                    case 2:
                        Console.logMsg("Driving Foward...");
                        Chassis.disableAnglePID();
                        Chassis.resetDistance();
                        Chassis.goToDistance(36.0);
                        tmrStage.reset();
                        mStage++;
                    case 3:
                        if(tmrStage.get() >= 3.0 || Chassis.isAtDistance()) mStage++;
                        break;
                    case 6:
                        Console.logMsg("Enabling Manipulator...");
                        Manipulator.enableRoller();
                        tmrStage.reset();
                        mStage++;
                    case 7:
                        if(tmrStage.get() >= 1.0) mStage++;
                        break;
                    case 8:
                        Console.logMsg("Auton Finished!");
                        mStage++;
                    default:
                        Robot.disableSubsystems();
                } 
            }
        },
        TIMED_DRIVE_FOWARD("Timed Drive Foward") {
            @Override public void periodic() {
                switch(mStage) {
                    case 0:
                        Console.logMsg("Starting Auton...");
                        tmrStage.reset();
                        mStage++;
                    case 1:
                        if(tmrStage.get() >= mSelectedStartingDelay.getTime()) mStage++;
                        break;
                    case 2:
                        Console.logMsg("Driving Foward...");
                        Chassis.setDrivePower(0.5, 0.5);
                        tmrStage.reset();
                        mStage++;
                    case 3:
                        if(tmrStage.get() >= 1.0) mStage++;
                        break;
                    case 4:
                        Console.logMsg("Auton Finished!");
                        mStage++;
                        default:
                        Robot.disableSubsystems();
                }
            }
        },
        TIMED_DRIVE_AND_SCORE("Timed Drive Backwards and Score") {
            @Override public void periodic() {
                switch(mStage) {
                    case 0:
                        Console.logMsg("Starting Auton...");
                        tmrStage.reset();
                        mStage++;
                        break;
                    case 1:
                        if(tmrStage.get() >= mSelectedStartingDelay.getTime()) mStage++;
                        break;
                    case 2:
                        Console.logMsg("Driving Foward...");
                        Chassis.setDrivePower(-0.3, -0.35);
                        tmrStage.reset();
                        mStage++;
                        break;
                    case 3:
                        if(tmrStage.get() >= 1.0) mStage++;
                        break;
                    case 4:
                        Chassis.setDrivePower(0.0, 0.0);
                        mStage++; 
                        break;
                    case 5:
                        Console.logMsg("Enabling Manipulator...");
                        Manipulator.enableRoller();
                        tmrStage.reset();
                        mStage++;
                        break;
                    case 6:
                        if(tmrStage.get() >= 2.0) mStage++;
                        break;
                    case 7:
                        Console.logMsg("Auton Finished!");
                        mStage++;
                    default:
                        Robot.disableSubsystems();
                }
            }
        };

        private static final Timer tmrStage = new Timer();
        public static final Sequence DEFAULT = PREPARE_FOR_MATCH;
    
        private final String LABEL;
        private static int mStage = 0;
    
        private Sequence(String label) {LABEL = label;}
    
        /**Returns the title of this sector */
        public static String getTitle() { return "Sequence"; }
        public String getLabel() { return LABEL; }

        /**Call once at the start of autonomous to prepare each sequence */
        public void start() {
            tmrStage.restart();
            mStage = 0;

            Chassis.resetDistance();
            Chassis.resetAngle();
        }


        public void periodic() {}

    }


    //Network Tables
    private static final NetworkTable tblAutonomous = Robot.tblPeriods.getSubTable("Autonomous");

    //Dashboard Objects - Selectors
    private static final DashboardSelector<StartingPosition> dshStartingPosition = new DashboardSelector<StartingPosition>(tblAutonomous, StartingPosition.getTitle(), StartingPosition.DEFAULT);
    private static final DashboardSelector<StartingDelay> dshStartingDelay = new DashboardSelector<StartingDelay>(tblAutonomous, StartingDelay.getTitle(), StartingDelay.DEFAULT);
    private static final DashboardSelector<Sequence> dshSequence = new DashboardSelector<Sequence>(tblAutonomous, Sequence.getTitle(), Sequence.DEFAULT);

    //Buffer Variables For Dashboard Selectors
    private static StartingPosition mSelectedStartingPosition = StartingPosition.DEFAULT;
    private static StartingDelay mSelectedStartingDelay = StartingDelay.DEFAULT;
    private static Sequence mSelectedSequence = Sequence.DEFAULT;

    /**unused constructor*/
    private Autonomous() {}

    /**Called at robot startup */
    public static void init() {
        
    }

    /**Called at start of Autonomous */
    public static void start() {
        Console.printHeader("Autonomous Enabled");


        //Pull selected options
        mSelectedStartingPosition = dshStartingPosition.get();
        mSelectedStartingDelay = dshStartingDelay.get(); 
        mSelectedSequence = dshSequence.get();

        Console.logMsg("Sequence: " + mSelectedSequence.getLabel());
        Console.logMsg("Delay: " + mSelectedStartingDelay.getLabel());
        Console.logMsg("Start Position: " + mSelectedStartingPosition.getLabel());

        Chassis.setNeutralMode(NeutralModeValue.Brake);

        //onEnable setup for selected sequence
        mSelectedSequence.start();

        Console.printSeparator();
    }


    /**Called to perform pre-programmed Sequences.*/
    public static void periodic() {
        mSelectedSequence.periodic();

        Chassis.periodic();
        Manipulator.periodic(); 
    }

}
