// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems.arm;

import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.spark.SparkClosedLoopController;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Arm extends SubsystemBase {
  public SparkMax arm = new SparkMax(20, MotorType.kBrushless);
  public RelativeEncoder encoder = arm.getAlternateEncoder(/*8192*/);
  public SparkClosedLoopController controller = arm.getClosedLoopController();

  public double armSpeed = 0.1;
  public double[] angles = {0, 35, 35, 90, 180}; //angles in degrees from vertical
  //                        L1 L2  L3  L4  grab
  public double[] PIDs = {4.25, 0, 0.6};

  public Arm() {
    //controller.setP(PIDs[0]);
    //controller.setI(PIDs[1]);
    //controller.setD(PIDs[2]);
  }

  public double getPosition(){
    return encoder.getPosition();
  }

  public void setPosition(double p){
    controller.setReference(p, SparkMax.ControlType.kPosition);
  }
}
