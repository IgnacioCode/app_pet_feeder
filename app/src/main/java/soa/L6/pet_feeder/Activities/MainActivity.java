package soa.L6.pet_feeder.Activities;


import android.os.Bundle;
import android.util.Log;

import soa.L6.pet_feeder.Model.FeederState;
import soa.L6.pet_feeder.Model.Pet;
import soa.L6.pet_feeder.Model.PetRecorder;
import soa.L6.pet_feeder.R;
import soa.L6.pet_feeder.Utils.MQTTManager;
import soa.L6.pet_feeder.Utils.PetFeederConstants;
import soa.L6.pet_feeder.ui.home.HomeFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import soa.L6.pet_feeder.databinding.ActivityMainBinding;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.util.List;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    public HomeFragment homeFragment;
    public MQTTManager mqttManager;
    public PetRecorder petRecorder;
    public List<Pet> petList;

    public FeederState feederState;
    private final MqttCallback callback = new MqttCallback() {
        @Override
        public void connectionLost(Throwable cause) {
            Log.d(MainActivity.class.getName(), "Conexión perdida");
        }

        @Override
        public void messageArrived(String topic, MqttMessage message) {
            String messageContent = new String(message.getPayload());
            Log.d(MainActivity.class.getName(), "Mensaje recibido: " + messageContent);

            runOnUiThread(() -> {
                if (Objects.equals(topic, PetFeederConstants.SUB_TOPIC_ESTADOS)) {
                    feederState.UpdateEstado(messageContent);
                    callSetHomeDataInFragment();
                }
            });


        }

        @Override
        public void deliveryComplete(IMqttDeliveryToken token) {
            Log.d(MainActivity.class.getName(), "Entrega completada");
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        mqttManager = new MQTTManager(this,callback);
        mqttManager.connect();
        feederState = new FeederState();

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

        petRecorder = new PetRecorder(PetFeederConstants.FILE_NAME_PETS);
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
    private void callSetHomeDataInFragment() {
        if (homeFragment != null && homeFragment.isAdded()) {
            homeFragment.setHomeData(feederState);
        } else {
            Log.d(MainActivity.class.getName(), "homeFragment no está listo, reintentando...");
            getSupportFragmentManager().executePendingTransactions();
            new android.os.Handler().postDelayed(this::callSetHomeDataInFragment, 1000); // Reintentar después de 1 segundo
        }
    }

}