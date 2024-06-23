package soa.L6.pet_feeder.ui.home;

import android.app.TimePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import soa.L6.pet_feeder.Model.FeederState;
import soa.L6.pet_feeder.Utils.MQTTManager;
import soa.L6.pet_feeder.Activities.MainActivity;
import soa.L6.pet_feeder.Utils.PetFeederConstants;
import soa.L6.pet_feeder.R;
import soa.L6.pet_feeder.databinding.FragmentHomeBinding;
import android.widget.Button;
import android.widget.Toast;

public class HomeFragment extends Fragment {

    private static final String NO_DATA_TEXT = "-";
    private static final int NO_DATA_TIME = -1;
    private FragmentHomeBinding binding;

    private TextView time_label;
    private TextView amount_label;
    private TextView refillLabel;
    private TextView clearNeedLabel;
    private Button modify_schedule_btn;
    private EditText input_time;
    private EditText input_amount;

    private Button btnAddTime;
    private MQTTManager mqttManager;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);
        MainActivity mainActivity = (MainActivity) requireActivity();
        mqttManager = mainActivity.getMQTTManager();
        mainActivity.homeFragment = this;

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        RecyclerView recyclerView = root.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        input_time = root.findViewById(R.id.input_time);
        input_amount = root.findViewById(R.id.input_quantity);
        time_label = root.findViewById(R.id.next_feeding_time);
        amount_label = root.findViewById(R.id.food_quantity);
        refillLabel = root.findViewById(R.id.txt_aviso_recarga);
        clearNeedLabel = root.findViewById(R.id.txt_aviso_cambio);
        btnAddTime = root.findViewById(R.id.btnAddTime);
        modify_schedule_btn = root.findViewById(R.id.update_button);
        // Manejar el clic del botón para agregar horarios
        btnAddTime.setOnClickListener(v -> showTimePickerDialog());
        modify_schedule_btn.setOnClickListener(v -> saveNewAlimentacion());
        input_time.setFocusable(false); // Esto hace que el EditText no sea enfocable
        input_time.setCursorVisible(false); // Oculta el cursor para que no parezca editable
        input_time.setKeyListener(null); // Desactiva el teclado virtual
        setHomeData(mainActivity.feederState);
        /*
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            // Para que cargue primero el main activity y despues el fragment
            @Override
            public void run() {
                MainActivity mainActivity = (MainActivity) getActivity();
                if (mainActivity != null && mainActivity.feederState != null) {
                    setHomeData(mainActivity.feederState);
                    mainActivity.homeFragment = this;
                }
            }
        });*/

        return root;
    }
    private void showTimePickerDialog() {
        TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(), (view, hourOfDay, minute) -> {
            String time = String.format("%02d:%02d", hourOfDay, minute);
            input_time.setText(time);
        }, 0, 0, true);
        timePickerDialog.show();
    }
    private void saveNewAlimentacion() {
        String time = input_time.getText().toString();
        String amount = input_amount.getText().toString();
        input_time.setText("");
        input_amount.setText("");

        MainActivity mainActivity = (MainActivity) getActivity();
        if (time.isEmpty() || amount.isEmpty()) {
            // Mostrar toast indicando que algún campo está vacío
            Toast.makeText(getContext(), "Por favor completa ambos campos", Toast.LENGTH_SHORT).show();
        } else {
            String message = time + ";" + amount;
            assert mainActivity != null;
            mainActivity.feederState.setNextMealTime(time);
            mainActivity.feederState.setFoodAmount(Integer.parseInt(amount));
            setHomeData(mainActivity.feederState);
            mqttManager.publishMessage(PetFeederConstants.PUB_TOPIC_ALIMENTACION, message);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public void setHomeData(FeederState state){
        Log.d(HomeFragment.class.getName(), "Feeder State Updateado: " + state);
        time_label.setText(state.getNextMealTime());
        amount_label.setText(String.format("%.2f",state.getFoodAmount()));
        // Handle refillNeeded
        if (state.isRefillNeed()) {
            refillLabel.setBackgroundResource(R.drawable.tag_informe);
        } else {
            refillLabel.setBackgroundResource(R.drawable.tag_informe_desactivado); // This removes the background drawable
        }

        // Handle clearNeeded
        if (state.isClearNeed()) {
            clearNeedLabel.setBackgroundResource(R.drawable.tag_informe);
        } else {
            clearNeedLabel.setBackgroundResource(R.drawable.tag_informe_desactivado); // This removes the background drawable
        }
        // Debug logs to check the drawable change
        Log.d(HomeFragment.class.getName(), "Refill Label Drawable: " + (state.isRefillNeed() ? "tag_informe" : "tag_informe_desactivado"));
        Log.d(HomeFragment.class.getName(), "Clear Label Drawable: " + (state.isClearNeed() ? "tag_informe" : "tag_informe_desactivado"));
    }
}