// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.Subsystems;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.elevator.Elevator;
import frc.robot.subsystems.arm.Arm;
import frc.robot.subsystems.intake.Intake;


public class RunElevator extends Command {
  private final Elevator elevatorSubsystem;
  private final Arm armSubsystem = new Arm();
  private final Intake intakeSubsystem = new Intake();

  public RunElevator(Elevator eSubsystem) {
    elevatorSubsystem = eSubsystem;
    addRequirements(eSubsystem);
  }

  public Command grab(){
    return elevatorSubsystem.runOnce(() -> elevatorSubsystem.setPosition(elevatorSubsystem.elevatorSpeed, elevatorSubsystem.heights[1]))
                            .andThen(() -> armSubsystem.setPosition(armSubsystem.angles[4]))
                            .andThen(() -> intakeSubsystem.runIntake(intakeSubsystem.intakeSpeed))
                            .andThen(() -> elevatorSubsystem.setPosition(elevatorSubsystem.elevatorSpeed, elevatorSubsystem.heights[4]))
                            .andThen(rest());
  }

  public Command rest(){
    return elevatorSubsystem.runOnce(() -> intakeSubsystem.stopIntake())
                            .andThen(() -> armSubsystem.setPosition(0))
                            .andThen(() -> elevatorSubsystem.setPosition(elevatorSubsystem.elevatorSpeed, 0));
  }

  public Command setPosition(int p){
    return elevatorSubsystem.runOnce(() -> elevatorSubsystem.setPosition(elevatorSubsystem.elevatorSpeed, elevatorSubsystem.heights[p]))
                            .andThen(() -> armSubsystem.setPosition(armSubsystem.angles[p]))
                            .andThen(() -> intakeSubsystem.runIntake(intakeSubsystem.intakeSpeed));
  }


  // Called when the command is initially scheduled.
  @Override
  public void initialize() {}

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {}

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {}

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}
