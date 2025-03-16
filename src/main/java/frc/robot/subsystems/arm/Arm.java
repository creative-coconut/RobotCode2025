// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems.arm;

import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.RelativeEncoder;
//import com.revrobotics.spark.SparkRelativeEncoder;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Arm extends SubsystemBase {
  private double[] PID = {4.25, 0, 0.6};

  public SparkMax arm = new SparkMax(13, MotorType.kBrushless);
  private RelativeEncoder encoder = arm.getAlternateEncoder();

  public double armSpeed = 0.1;
  public double[] angles = {0, 35, 35, 90, 180}; //angles in degrees from vertical
//                          L1 L2  L3  L4  grab
  public double currentPosition = 0.0;


  public Arm() {}

  public double getPosition(){
    return currentPosition;
  }

  public void setPosition(double p){
    currentPosition = p;
    encoder.setPosition(p/72); //5 motor rotations = 1 arm rotation
  }
}
