package com.example.myapplication;

import java.io.Serializable;
import java.util.Objects;

public class Pet implements Serializable {
    private static final long serialVersionUID = 1L; // Versión del serializable

    private String name;
    private int feed_times;
    private float food_amount;
    private float eat_average;

    public Pet(String name) {
        this.name = name;
        this.feed_times = 0;
        this.food_amount = 0;
        this.eat_average = 0;
    }

    public void record_meal(float ate_amount) {
        feed_times++;
        food_amount = ate_amount;
        eat_average = food_amount / feed_times;
    }

    @Override
    public String toString() {
        return "Pet{" +
                "name='" + name + '\'' +
                ", feed_times=" + feed_times +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Pet pet = (Pet) o;
        return feed_times == pet.feed_times &&
                Float.compare(pet.food_amount, food_amount) == 0 &&
                Float.compare(pet.eat_average, eat_average) == 0 &&
                Objects.equals(name, pet.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, feed_times, food_amount, eat_average);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getFeed_times() {
        return feed_times;
    }

    public void setFeed_times(int feed_times) {
        this.feed_times = feed_times;
    }

    public float getFood_amount() {
        return food_amount;
    }

    public void setFood_amount(float food_amount) {
        this.food_amount = food_amount;
    }

    public float getEat_average() {
        return eat_average;
    }

    public void setEat_average(float eat_average) {
        this.eat_average = eat_average;
    }
}
