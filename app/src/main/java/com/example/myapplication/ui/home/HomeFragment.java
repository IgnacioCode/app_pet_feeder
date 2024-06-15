package com.example.myapplication.ui.home;

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

import com.example.myapplication.FeederState;
import com.example.myapplication.MQTTManager;
import com.example.myapplication.MainActivity;
import com.example.myapplication.R;
import com.example.myapplication.databinding.FragmentHomeBinding;
import android.widget.Button;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;

    private TextView time_label;
    private TextView amount_label;
    private TextView refillLabel;
    private TextView clearNeedLabel;
    private Button modify_schedule_btn;
    private EditText input_time;
    private EditText input_amount;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        time_label = root.findViewById(R.id.next_feeding_time);
        amount_label = root.findViewById(R.id.food_quantity);
        refillLabel = root.findViewById(R.id.txt_aviso_recarga);
        clearNeedLabel = root.findViewById(R.id.txt_aviso_cambio);

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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public void setHomeData(FeederState state){
        time_label.setText(state.getNextMealTime()+" ");
        amount_label.setText(String.format("%.2f",state.getFoodAmount()));
        if(state.isRefillNeed()){
            refillLabel.setBackgroundResource(R.drawable.tag_informe);
        }
        if(state.isClearNeed()){
            clearNeedLabel.setBackgroundResource(R.drawable.tag_informe);
        }
    }
}