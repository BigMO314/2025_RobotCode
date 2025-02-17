package frc.robot.Periods;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.wpilibj.Timer;
import frc.molib.Console;
import frc.molib.dashboard.DashboardOptionBase;
import frc.molib.dashboard.DashboardSelector;
import frc.robot.Robot;
import frc.robot.Subsystems.Chassis;
import frc.robot.Subsystems.Elevator;
import frc.robot.Subsystems.Manipulator;

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
            @Override public void onEnable() {

            }
        },
        DRIVE_FOWARD("Drive Foward") {
            @Override public void periodic() {
                switch(mStage) {
                    case 0:
                        Console.logMsg("Zeroing Elevator. Delaying other actions...");
                        
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


        public void onEnable() {
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

    //Buffer Variables For Dashboard Selectors
    private static StartingPosition mSelectedStartingPosition = StartingPosition.DEFAULT;
    private static StartingDelay mSelectedStartingDelay = StartingDelay.DEFAULT;
    private static Sequence mSelectedSequence = Sequence.DEFAULT;

    //unused constructer
    private Autonomous() {}

    public static void init() {
        Console.printHeader("Autonomous Initialization");

        Console.logMsg("Initializing Dashboard Selectors");
        dshStartingPosition.init();
        dshStartingDelay.init();
        dshSequence.init();


        Console.logMsg("Autonomous Initialization Complete");
    }

    public static void onEnable() {
        Console.printHeader("Autonomous Enabled");


        //Pull selected options
        mSelectedStartingPosition = dshStartingPosition.get();
        mSelectedStartingDelay = dshStartingDelay.get(); 
        mSelectedSequence = dshSequence.get();


        //onEnable setup for selected sequence
        mSelectedSequence.onEnable();

        Console.printSeparator();
    }



    public static void periodic() {
        mSelectedSequence.periodic();

        Chassis.periodic();
        Elevator.periodic();
        Manipulator.periodic(); 
    }

}
