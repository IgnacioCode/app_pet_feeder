package com.example.myapplication.ui.dashboard;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.myapplication.MainActivity;
import com.example.myapplication.Pet;
import com.example.myapplication.PetRecorder;
import com.example.myapplication.R;
import com.example.myapplication.databinding.FragmentDashboardBinding;

import java.util.List;
import java.util.Objects;

public class DashboardFragment extends Fragment {

    private FragmentDashboardBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        DashboardViewModel dashboardViewModel =
                new ViewModelProvider(this).get(DashboardViewModel.class);

        binding = FragmentDashboardBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        MainActivity mainActivity = (MainActivity) getActivity();
        LinearLayout containerLayout = root.findViewById(R.id.contenedor_linear);

        PetRecorder petRecorder = mainActivity.petRecorder;

        for (Pet pet : petRecorder.getPetList()) {
            addPetToContainer(containerLayout, pet);
        }
        return root;
    }

    private static final int PADDING_LEFT = 20;
    private static final int PADDING_TOP = 10;
    private static final int PADDING_RIGHT = 0;
    private static final int PADDING_BOTTOM = 0;
    private static final int PADDING_PANEL = 16;

    private void addPetToContainer(ViewGroup container, Pet pet) {
        // Crear un contenedor para el Pet
        LinearLayout petContainer = new LinearLayout(getContext());
        petContainer.setOrientation(LinearLayout.VERTICAL);
        petContainer.setPadding(PADDING_PANEL, PADDING_PANEL, PADDING_PANEL, PADDING_PANEL);
        petContainer.setBackgroundResource(R.drawable.panel_redondeado); // Asignar el drawable como fondo

        // Crear TextView para el nombre
        TextView nameTextView = new TextView(getContext());
        nameTextView.setText("Nombre: " + pet.getName());
        nameTextView.setTextSize(24);
        nameTextView.setPadding(PADDING_LEFT, PADDING_TOP, PADDING_RIGHT, PADDING_BOTTOM);
        nameTextView.setTextColor(Color.BLACK);
        petContainer.addView(nameTextView);

        // Crear TextView para feed_times
        TextView feedTimesTextView = new TextView(getContext());
        feedTimesTextView.setText("Cantidad de Comidas: " + pet.getFeed_times());
        feedTimesTextView.setTextSize(16);
        feedTimesTextView.setPadding(PADDING_LEFT, PADDING_TOP, PADDING_RIGHT, PADDING_BOTTOM);
        petContainer.addView(feedTimesTextView);

        // Crear TextView para food_amount
        TextView foodAmountTextView = new TextView(getContext());
        foodAmountTextView.setText("Cantidad de comida: " + pet.getFood_amount());
        foodAmountTextView.setTextSize(16);
        foodAmountTextView.setPadding(PADDING_LEFT, PADDING_TOP, PADDING_RIGHT, PADDING_BOTTOM);
        petContainer.addView(foodAmountTextView);

        // Crear TextView para eat_average
        TextView eatAverageTextView = new TextView(getContext());
        eatAverageTextView.setText("Promedio ingerido: " + String.format("%.2f",pet.getEat_average()));
        eatAverageTextView.setTextSize(16);
        eatAverageTextView.setPadding(PADDING_LEFT, PADDING_TOP, PADDING_RIGHT, 16);
        petContainer.addView(eatAverageTextView);

        // Agregar el contenedor del Pet al contenedor principal
        container.addView(petContainer);

        // Añadir un margen inferior al contenedor del Pet
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) petContainer.getLayoutParams();
        params.setMargins(0, 0, 0, 16);
        petContainer.setLayoutParams(params);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}