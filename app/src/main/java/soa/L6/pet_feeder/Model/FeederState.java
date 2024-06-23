package soa.L6.pet_feeder.Model;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import soa.L6.pet_feeder.Utils.PetFeederConstants;

public class FeederState {

    private String nextMealTime;
    private double foodAmount;
    private boolean refillNeed;
    private boolean clearNeed;
    private EstadoEmbebido estado;
    private List<Alimentacion> alimentaciones = new ArrayList<>();

    public FeederState(){
        nextMealTime = "";
        foodAmount = 0;
        refillNeed = false;
        clearNeed = false;
    }
    public FeederState(String nextMealTime, double foodAmount, boolean refillNeed, boolean clearNeed) {
        this.nextMealTime = nextMealTime;
        this.foodAmount = foodAmount;
        this.refillNeed = refillNeed;
        this.clearNeed = clearNeed;
    }
    @Override
    public String toString() {
        return "FeederState{" +
                "nextMealTime='" + nextMealTime + '\'' +
                ", foodAmount=" + foodAmount +
                ", refillNeed=" + refillNeed +
                ", clearNeed=" + clearNeed +
                ", estado=" + estado +
                ", alimentaciones=" + alimentaciones +
                '}';
    }
    public String getNextMealTime() {
        return nextMealTime;
    }
    public void setNextMealTime(String nextMealTime) {
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
    public void AddAlimentacion(String horario, double cantComida) {
        alimentaciones.add(new Alimentacion(horario,cantComida));
    }
    public void UpdateEstado(String message) {
        estado = EstadoEmbebido.fromString(message);
        if (Objects.equals(estado.getEstadoActual(), PetFeederConstants.ESTADO_RENOVAR_COMIDA))
            clearNeed = true;
        if (Objects.equals(estado.getEstadoActual(), PetFeederConstants.ESTADO_PEDIR_RECARGA))
            refillNeed = true;
        if(Objects.equals(estado.getEstadoActual(), PetFeederConstants.ESTADO_ESPERA)) {
            refillNeed = false;
            clearNeed = false;
        }
        Log.d(FeederState.class.getName(), "Estado Updateado " + estado);
    }
}
