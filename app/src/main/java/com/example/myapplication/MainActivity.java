package com.example.myapplication;


import android.os.Bundle;
import android.util.Log;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.myapplication.databinding.ActivityMainBinding;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    private MQTTManager mqttManager;
    public PetRecorder petRecorder;
    public List<Pet> petList;

    public int next_meal_time;
    public float next_food_amount;
    public boolean refill_need = false;
    public boolean clear_need = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);

        mqttManager = new MQTTManager(this);
        mqttManager.connect();

        getSupportActionBar().hide();

        petList = PetRecorder.loadPetsFromFile(this);
        /*Pet newCat = new Pet("Juan");
        petList.add(newCat);
        Log.d("TEST",petList.toString());
        PetRecorder.savePetsToFile(this,petList);*/
    }

}