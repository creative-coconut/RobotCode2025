// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.SparkMax;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;


public class Intake extends SubsystemBase {
    public SparkMax intake = new SparkMax(22, MotorType.kBrushless);
  
    public double intakeSpeed = 0.4; //from 2024 code, untested
    public double outtakeSpeed = -.5; //from 2024 code, untested
    public boolean isRunning = false;

    public Intake() {}

    
    public void runIntake(double speed){
      intake.set(speed);
      isRunning = true;
    }

    public void stopIntake(){
      intake.set(0);
      isRunning = false;
    }

    public Command run(double speed){
      return runOnce(() -> intake.set(speed));
    }

    public Command checkIfRunning(){
      if(!isRunning){
        return runOnce(() -> stopIntake());
      }
      return runOnce(() -> System.out.println());
    }

    //Commands for use when constructing Autos
    public Command runIntakeAuto(){
      return runOnce(() -> intake.set(intakeSpeed));
    }
}