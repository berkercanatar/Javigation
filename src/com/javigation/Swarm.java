package com.javigation;

public class Swarm {

    DroneConnection drone;
    private int droneCount;
    private int velocity;
    private int position;

    public Swarm(DroneConnection drone, int droneCount, int velocity, int position){
        this.drone = drone;
        this.velocity = velocity;
        this.position = position;
    }


    private void updateVelocity (){}
    private void updatePosition(){}
    private void addDrone(){}
    private DroneConnection[] initializeDrones(){
        DroneConnection[] drones = new DroneConnection[droneCount];

        //implementing

        return drones;
    }
}
