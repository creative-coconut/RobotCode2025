// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.Filesystem;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import edu.wpi.first.wpilibj2.command.button.Trigger;
import edu.wpi.first.math.filter.Debouncer;
import edu.wpi.first.math.filter.Debouncer.DebounceType;
import edu.wpi.first.wpilibj.smartdashboard.*;
import edu.wpi.first.wpilibj2.command.button.POVButton;
import frc.robot.Constants.OperatorConstants;
import frc.robot.commands.Subsystems.RunElevator;
import frc.robot.subsystems.swervedrive.SwerveSubsystem;
import frc.robot.subsystems.Hopper;
import frc.robot.subsystems.Elevator;
import frc.robot.subsystems.Intake;
//import frc.robot.subsystems.PhotonVision;
import com.pathplanner.lib.auto.NamedCommands;
import edu.wpi.first.wpilibj.RobotBase;
import java.io.File;
import com.pathplanner.lib.auto.AutoBuilder;


/**
 * This class is where the bulk of the robot should be declared. Since Command-based is a "declarative" paradigm, very
 * little robot logic should actually be handled in the {@link Robot} periodic methods (other than the scheduler calls).
 * Instead, the structure of the robot (including subsystems, commands, and trigger mappings) should be declared here.
 */
public class RobotContainer
{
  private final Elevator elevator = new Elevator();
  private final Intake intake = new Intake();
  private final RunElevator system = new RunElevator(elevator, intake);
  private final Hopper hopper = new Hopper();

  //Create Auto Chooser
  private final SendableChooser<Command> autoChooser;

  // The robot's subsystems and commands are defined here...
  private final SwerveSubsystem drivebase = new SwerveSubsystem(new File(Filesystem.getDeployDirectory(),
                                                                         "swerve"));

  // CommandJoystick rotationController = new CommandJoystick(1);
  // Replace with CommandPS4Controller or CommandJoystick if needed
  public CommandXboxController manipXbox = new CommandXboxController(1);
  public XboxController otherManipXbox = new XboxController(2);

  // CommandJoystick driverController   = new CommandJoystick(3);//(OperatorConstants.DRIVER_CONTROLLER_PORT);
  public XboxController driverXbox = new XboxController(0);
  public CommandXboxController driverXboxCommand = new CommandXboxController(0);

  /**
   * The container for the robot. Contains subsystems, OI devices, and commands.
   */
  public RobotContainer()
  {
    //Register Named Commands for PathPlanner
    //NamedCommands.registerCommand("Run Intake", intake.runIntakeAuto());
    //NamedCommands.registerCommand("Run Hopper", hopper.runHopperAuto());

    
    //Build Auto-Chooser
    //Default auto does nothing, change later if you want a specific default auto
    autoChooser = AutoBuilder.buildAutoChooser("Do Nothing");
    SmartDashboard.putData("Auto Chooser", autoChooser);


    // Configure the trigger bindings
    configureBindings();

    // Applies deadbands and inverts controls because joysticks
    // are back-right positive while robot
    // controls are front-left positive
    // left stick controls translation
    // right stick controls the desired angle NOT angular rotation
    Command driveFieldOrientedDirectAngle = drivebase.driveCommand(
        () -> MathUtil.applyDeadband(driverXbox.getLeftY(), OperatorConstants.LEFT_Y_DEADBAND),
        () -> MathUtil.applyDeadband(driverXbox.getLeftX(), OperatorConstants.LEFT_X_DEADBAND),
        () -> driverXbox.getRightX(),
        () -> driverXbox.getRightY());

        drivebase.setDefaultCommand(driveFieldOrientedDirectAngle);
    // Applies deadbands and inverts controls because joysticks
    // are back-right positive while robot
    // controls are front-left positive
    // left stick controls translation
    // right stick controls the angular velocity of the robot
    /*
    Command driveFieldOrientedDirectAngleSim = drivebase.simDriveCommand(
        () -> -MathUtil.applyDeadband(driverXbox.getLeftY(), OperatorConstants.LEFT_Y_DEADBAND),
        () -> -MathUtil.applyDeadband(driverXbox.getLeftX(), OperatorConstants.LEFT_X_DEADBAND),
        () -> driverXbox.getRawAxis(2));

    drivebase.setDefaultCommand(
      !RobotBase.isSimulation() ? driveFieldOrientedDirectAngle : driveFieldOrientedDirectAngleSim);
*/
  }

  /**
   * Use this method to define your trigger->command mappings. Triggers can be created via the
   * {@link Trigger#Trigger(java.util.function.BooleanSupplier)} constructor with an arbitrary predicate, or via the
   * named factories in {@link edu.wpi.first.wpilibj2.command.button.CommandGenericHID}'s subclasses for
   * {@link CommandXboxController Xbox}/{@link edu.wpi.first.wpilibj2.command.button.CommandPS4Controller PS4}
   * controllers or {@link edu.wpi.first.wpilibj2.command.button.CommandJoystick Flight joysticks}.
   */
  private void configureBindings()
  {
    // Schedule `ExampleCommand` when `exampleCondition` changes to `true`

    new JoystickButton(driverXbox, 1).onTrue((new InstantCommand(drivebase::zeroGyro)));
    new JoystickButton(driverXbox, 3).onTrue(new InstantCommand(drivebase::addFakeVisionReading));
    new JoystickButton(driverXbox,
                       2).whileTrue(
        Commands.deferredProxy(() -> drivebase.driveToPose(
                                   new Pose2d(new Translation2d(4, 4), Rotation2d.fromDegrees(0)))
                              ));
    
    /**button commands**/
    //hopper in
    new JoystickButton(otherManipXbox, 5).onTrue(hopper.runLeft(hopper.hopperSpeed));
    new JoystickButton(otherManipXbox, 5).onTrue(hopper.runRight(hopper.hopperSpeed));
    new JoystickButton(otherManipXbox, 5).onFalse(hopper.runLeft(0));
    new JoystickButton(otherManipXbox, 5).onFalse(hopper.runRight(0));
    
    //hopper out
    new JoystickButton(otherManipXbox, 6).onTrue(hopper.reverseLeft(hopper.reverseHopperSpeed));
    new JoystickButton(otherManipXbox, 6).onTrue(hopper.reverseRight(hopper.reverseHopperSpeed));    
    new JoystickButton(otherManipXbox, 6).onFalse(hopper.reverseLeft(0));
    new JoystickButton(otherManipXbox, 6).onFalse(hopper.reverseRight(0));
    
    //intake controls
    new JoystickButton(otherManipXbox, 2).onTrue(intake.run(intake.intakeSpeed*-1));
    new JoystickButton(otherManipXbox, 2).onFalse(intake.rest());
    
    //elevator + arm + intake
    new JoystickButton(otherManipXbox, 3).onTrue(system.grab()); //X
    new JoystickButton(otherManipXbox, 4).onTrue(system.rest()); //Y
    new POVButton(otherManipXbox, 0).onTrue(system.setPosition(0)); //up
    new POVButton(otherManipXbox, 90).onTrue(system.setPosition(1)); //right
    new POVButton(otherManipXbox, 180).onTrue(system.setPosition(2)); //down
    new POVButton(otherManipXbox, 270).onTrue(system.setPosition(3)); //left
  }


  /**
   * Use this to pass the autonomous command to the main {@link Robot} class.
   *
   * @return the command to run in autonomous
   */ 
  public Command getAutonomousCommand()
  {
    // An example command will be run in autonomous
    //return drivebase.getAutonomousCommand(autoChooser.getSelected().toString(), true);
    return autoChooser.getSelected();
  }

  public void setDriveMode()
  {
   // drivebase.setDefaultCommand();
  }

  public void setMotorBrake(boolean brake)
  {
    drivebase.setMotorBrake(brake);
  }
}