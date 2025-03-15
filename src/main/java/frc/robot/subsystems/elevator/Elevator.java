// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems.elevator;

import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.SparkMax;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Elevator extends SubsystemBase {
  public SparkMax elevatorA = new SparkMax(11, MotorType.kBrushless);
  public SparkMax elevatorB = new SparkMax(12, MotorType.kBrushless);

  public double elevatorSpeed = 0.4; //untested!
  public double[] heights = {24.000, 31.875, 47.625, 72.000, 20.000}; //height in inches
  //                         L1      L2      L3      L4     grab
  public double currentPosition = 0.0;


  public Elevator() {}

  public double getPosition(){
    return currentPosition;
  }

  public Command setPosition(double s, double p){
    return runOnce(() -> set(s, p));
  }

  public void set(double speed, double position){
    currentPosition = position;
    elevatorA.set(speed);
    elevatorB.set(-speed);
    elevatorA.set(0);
    elevatorB.set(0);
  }
}