package frc.robot.Periods;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.wpilibj.Timer;
import frc.molib.Console;
import frc.molib.dashboard.DashboardOptionBase;
import frc.molib.dashboard.DashboardSelector;
import frc.robot.Robot;
import frc.robot.Subsystems.Chassis;
import frc.robot.Subsystems.Manipulator;

@SuppressWarnings("unused")
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

    private enum ScoringPosition implements DashboardOptionBase {
        L1("L1"),
        L2("L2"),
        L3("L3"),
        L4("L4");

        public static final ScoringPosition DEFAULT = L1;
        
        private final String LABEL;

        private ScoringPosition(String label) { LABEL = label; }

        public static String getTitle() { return "Scoring Position"; }
        public String getLabel() { return LABEL; }

    }

    private enum Sequence implements DashboardOptionBase {

        DO_NOTHING("Do Nothing") {

        },
        PREPARE_FOR_MATCH("Prepare For Match") {
            @Override public void start() {
                switch (mStage) {
                    case 0:
                        Console.logMsg("Lowering Manipulator");
                        Manipulator.lowerElevator();
                        tmrStage.reset();
                        mStage++;
                    default:
                        if(tmrStage.get() >= 3.0)mStage++;
                        break;
                }
            }
        },
        DRIVE_FOWARD("Drive Foward") {
            @Override public void periodic() {
                switch(mStage) {
                    case 0:
                        Console.logMsg("Zeroing Manipulator. Delaying other actions...");
                        Manipulator.lowerElevator();
                        tmrStage.reset();
                        mStage++;
                    case 1:
                        if(tmrStage.get() >= mSelectedStartingDelay.getTime())mStage++;
                        break;
                    case 2:
                        Console.logMsg("Driving forward...");
                        Chassis.disableAnglePID();
                        Chassis.resetDistance();
                        Chassis.goToDistance(60.0);
                        tmrStage.reset();
                        mStage++;
                    case 3:
                        if(tmrStage.get() >= 5.0 ||Chassis.isAtDistance())mStage++;
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
                        Console.logMsg("Zeroing Elevator. Delaying other actions...");
                        Manipulator.lowerElevator();
                        tmrStage.reset();
                        mStage++;
                    case 1:
                        if(tmrStage.get() >= mSelectedStartingDelay.getTime()) mStage++;
                        break;
                    case 2:
                        Console.logMsg("Driving forward...");
                        Chassis.disableAnglePID();
                        Chassis.resetDistance();
                        Chassis.goToDistance(24.0);
                        tmrStage.reset();
                        mStage++;
                    case 3:
                        if(tmrStage.get() >= 3.0 || Chassis.isAtDistance()) mStage++;
                        break;
                    case 4:
                        Manipulator.goToHeight(12.0);
                        tmrStage.reset();
                        mStage++;
                    case 5:
                        if(tmrStage.get() >= 3.0 || Manipulator.isAtHeight())mStage++;
                        break;
                    case 6:
                        Manipulator.enableManipulator();
                        tmrStage.reset();
                        mStage++;
                    case 7:
                    if(tmrStage.get() >= 1.0)mStage++;
                    break;
                    default:
                        Robot.disableSubsystems();
                }
            }
        },
        RIGHT_SCORE("Right Score") {
            @Override public void periodic() {
                switch (mStage) {
                    case 0:
                        Manipulator.lowerElevator();
                        tmrStage.reset();
                        mStage++;
                    case 1:
                        if(tmrStage.get() >= 3.0)mStage++;
                        break;
                    case 2:
                        Chassis.disableAnglePID();
                        Chassis.resetDistance();
                        Chassis.goToDistance(60.0);
                        tmrStage.reset();
                        mStage++;
                    case 3:
                        if(tmrStage.get() >= 3.0 || Chassis.isAtDistance())mStage++;
                        break;
                    case 4:
                        Manipulator.goToHeight(12.0);
                        tmrStage.reset();
                        mStage++;
                    case 5:
                        if(tmrStage.get() >= 4.0 || Manipulator.isAtHeight())mStage++;
                        break;
                    case 6:
                        Manipulator.enableManipulator();
                        tmrStage.reset();
                        mStage++;
                    case 7:
                        if(tmrStage.get() >= 1.0)mStage++;
                        break;
                    default:
                        Robot.disableSubsystems();
                } 
            }
        },
        LEFT_SCORE("Left Score") {
            @Override public void periodic() {
                switch (mStage) {
                    case 0:
                        Manipulator.lowerElevator();
                        tmrStage.reset();
                        mStage++;
                    case 1:
                        if(tmrStage.get() >= 3.0)mStage++;
                        break;
                    case 2:
                        Chassis.disableAnglePID();
                        Chassis.resetDistance();
                        Chassis.goToDistance(60.0);
                        tmrStage.reset();
                        mStage++;
                    case 3:
                        if(tmrStage.get() >= 4.0 || Chassis.isAtDistance())mStage++;
                        break;
                    case 4:
                        Manipulator.goToHeight(12.0);
                        tmrStage.reset();
                        mStage++;
                    case 5:
                        if(tmrStage.get() >= 4.0 || Manipulator.isAtHeight())mStage++;
                        break;
                    case 6:
                        Manipulator.enableManipulator();
                        tmrStage.reset();
                        mStage++;
                    case 7:
                        if(tmrStage.get() >= 1.0)mStage++;
                        break;
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
    
        public static String getTitle() { return "Starting Position"; }
        public String getLabel() { return LABEL; }


        public void start() {
            tmrStage.restart();
            mStage = 0;
        }


        public void periodic() {}

    }

    
   

    //Network Tables
    private static final NetworkTable tblAutonomous = Robot.tblPeriods.getSubTable("Autonomous");

    //Dashboard Objects - Selectors
    private static final DashboardSelector<StartingPosition> dshStartingPosition = new DashboardSelector<StartingPosition>(tblAutonomous, StartingPosition.getTitle(), StartingPosition.DEFAULT);
    private static final DashboardSelector<StartingDelay> dshStartingDelay = new DashboardSelector<StartingDelay>(tblAutonomous, StartingDelay.getTitle(), StartingDelay.DEFAULT);
    private static final DashboardSelector<Sequence> dshSequence = new DashboardSelector<Sequence>(tblAutonomous, Sequence.getTitle(), Sequence.DEFAULT);
    private static final DashboardSelector<ScoringPosition> dshScoringPosition = new DashboardSelector<ScoringPosition>(tblAutonomous, ScoringPosition.getTitle(), ScoringPosition.DEFAULT);

    //Buffer Variables For Dashboard Selectors
    private static StartingPosition mSelectedStartingPosition = StartingPosition.DEFAULT;
    private static StartingDelay mSelectedStartingDelay = StartingDelay.DEFAULT;
    private static Sequence mSelectedSequence = Sequence.DEFAULT;
    private static ScoringPosition mSelectedScoringPosition = ScoringPosition.DEFAULT;

    /**unused constructor*/
    private Autonomous() {}

    /**Called at robot startup */
    public static void init() {
        
    }

    /**Called at start of Autonomous */
    public static void onEnable() {
        Console.printHeader("Autonomous Enabled");


        //Pull selected options
        mSelectedStartingPosition = dshStartingPosition.get();
        mSelectedStartingDelay = dshStartingDelay.get(); 
        mSelectedSequence = dshSequence.get();
        mSelectedScoringPosition = dshScoringPosition.get();


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
