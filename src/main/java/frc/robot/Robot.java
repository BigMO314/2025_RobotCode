// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.TimedRobot;
import frc.molib.Managers;
import frc.molib.dashboard.DashboardManager;
import frc.robot.Periods.Teleoperated;
import frc.robot.Subsystems.Chassis;

/**
 * The methods in this class are called automatically corresponding to each mode, as described in
 * the TimedRobot documentation. If you change the name of this class or the package after creating
 * this project, you must also update the Main.java file in the project.
 */
public class Robot extends TimedRobot {

    public static NetworkTable tblMain = NetworkTableInstance.getDefault().getTable("BigMO");
    public static NetworkTable tblPeriods = tblMain.getSubTable("Periods");
    public static NetworkTable tblSubsystems = tblMain.getSubTable("Subsystems");

    /**
     * This function is run when the robot is first started up and should be used for any
     * initialization code.
     */
    public Robot() {}

    @Override
    public void robotInit() {

        Chassis.init();
        Teleoperated.init();

        DashboardManager.initSelectors();

    }

    @Override
    public void robotPeriodic() {

        Managers.update();
    
        Chassis.syncDashboardValues();
        
    }

    @Override
    public void autonomousInit() {}

    @Override
    public void autonomousPeriodic() {}

    @Override
    public void teleopInit() {
        Teleoperated.start();
    }

    @Override
    public void teleopPeriodic() {
        Teleoperated.periodic();
    }

    @Override
    public void disabledInit() {}

    @Override
    public void disabledPeriodic() {}

    @Override
    public void testInit() {}

    @Override
    public void testPeriodic() {}

    @Override
    public void simulationInit() {}

    @Override
    public void simulationPeriodic() {}
}
