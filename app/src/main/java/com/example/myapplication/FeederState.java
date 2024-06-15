package com.example.myapplication;

public class FeederState {

    private int nextMealTime;
    private double foodAmount;
    private boolean refillNeed;
    private boolean clearNeed;

    public FeederState(){
        nextMealTime = -1;
        foodAmount = 0;
        refillNeed = false;
        clearNeed = false;
    }
    public FeederState(int nextMealTime, double foodAmount, boolean refillNeed, boolean clearNeed) {
        this.nextMealTime = nextMealTime;
        this.foodAmount = foodAmount;
        this.refillNeed = refillNeed;
        this.clearNeed = clearNeed;
    }
    public int getNextMealTime() {
        return nextMealTime;
    }
    public void setNextMealTime(int nextMealTime) {
        this.nextMealTime = nextMealTime;
    }
    public double getFoodAmount() {
        return foodAmount;
    }
    public void setFoodAmount(double foodAmount) {
        this.foodAmount = foodAmount;
    }
    public boolean isRefillNeed() {
        return refillNeed;
    }
    public void setRefillNeed(boolean refillNeed) {
        this.refillNeed = refillNeed;
    }
    public boolean isClearNeed() {
        return clearNeed;
    }
    public void setClearNeed(boolean clearNeed) {
        this.clearNeed = clearNeed;
    }
}
