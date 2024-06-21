package com.example.myapplication.ui.home;

import android.app.TimePickerDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
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

import com.example.myapplication.FeederState;
import com.example.myapplication.MQTTManager;
import com.example.myapplication.MainActivity;
import com.example.myapplication.R;
import com.example.myapplication.databinding.FragmentHomeBinding;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

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
        MainActivity mainActivity = (MainActivity) getActivity();
        if (mainActivity != null) {
            mqttManager = mainActivity.getMQTTManager();
        }
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

        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                MainActivity mainActivity = (MainActivity) getActivity();
                if (mainActivity != null && mainActivity.feederState != null) {
                    setHomeData(mainActivity.feederState);
                }
            }
        });

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
            time_label.setText(time);
            amount_label.setText(amount);
            mqttManager.publishMessage(mqttManager.publishAlimentacion, message);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public void setHomeData(FeederState state){

        time_label.setText(state.getNextMealTime());
        amount_label.setText(String.format("%.2f",state.getFoodAmount()));
        if(state.isRefillNeed()){
            refillLabel.setBackgroundResource(R.drawable.tag_informe);
        }
        if(state.isClearNeed()){
            clearNeedLabel.setBackgroundResource(R.drawable.tag_informe);
        }
    }
}