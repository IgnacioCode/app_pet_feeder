package com.example.myapplication;


import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.myapplication.databinding.ActivityMainBinding;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String FILE_NAME = "pets.dat";

    private ActivityMainBinding binding;
    public MQTTManager mqttManager;
    public PetRecorder petRecorder;
    public List<Pet> petList;

    public FeederState feederState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mqttManager = new MQTTManager(this);
        mqttManager.connect();
        feederState = mqttManager.getNewFeederState(this);

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

        getSupportActionBar().hide();

        petRecorder = new PetRecorder(FILE_NAME);
        petRecorder.loadPetsFromFile(this);

        Pet newCat = new Pet("Pepe","CA");
        newCat.record_meal(60.1);
        newCat.record_meal(12.36);
        newCat.record_meal(2.36);
        newCat.record_meal(8.49);
        newCat.record_meal(24.94);
        petRecorder.addPetToList(newCat);

        Log.d("TEST",petRecorder.getPetList().toString());
        petRecorder.savePetsToFile(this);
    }
    public MQTTManager getMQTTManager() {
        return mqttManager;
    }

}