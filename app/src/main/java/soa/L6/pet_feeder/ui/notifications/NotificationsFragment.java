package soa.L6.pet_feeder.ui.notifications;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.view.ContextThemeWrapper;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import java.util.Comparator;
import java.util.stream.Collectors;

import soa.L6.pet_feeder.Activities.MainActivity;
import soa.L6.pet_feeder.Model.FeederRecorder;
import soa.L6.pet_feeder.Model.Food;
import soa.L6.pet_feeder.Model.PetRecorder;
import soa.L6.pet_feeder.R;
import soa.L6.pet_feeder.Utils.PetFeederConstants;
import soa.L6.pet_feeder.databinding.FragmentNotificationsBinding;

public class NotificationsFragment extends Fragment {

    private Button btn_sincro;
    private Button btn_delete;
    private MainActivity mainActivity;
    private AlertDialog dialog;


    private FragmentNotificationsBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        NotificationsViewModel notificationsViewModel =
                new ViewModelProvider(this).get(NotificationsViewModel.class);

        binding = FragmentNotificationsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        btn_sincro = root.findViewById(R.id.btn_sincro);
        btn_sincro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sinchronizeApp();
            }

        });

        btn_delete = root.findViewById(R.id.btn_delete);
        btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteAppData();
            }

        });

        mainActivity = (MainActivity) requireActivity();

        createFoodCards();

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public void createFoodCards()
    {
        LinearLayout containerLayout = binding.getRoot().findViewById(R.id.contenedor_linear);
        FeederRecorder feeder = mainActivity.feederState.getFeederRecorder();

        for (Food food : feeder.getFoodList().stream().sorted(Comparator.comparing(Food::getHour)).collect(Collectors.toList())) {
            addFoodToContainer(containerLayout, food);
        }
    }

    public void deleteAppData(){
        MainActivity mainActivity = (MainActivity) getActivity();
        LinearLayout containerLayout = binding.getRoot().findViewById(R.id.contenedor_linear);

        PetRecorder petRecorder = mainActivity.petRecorder;
        FeederRecorder feederRecorder = mainActivity.feederState.getFeederRecorder();

        petRecorder.clearPetList();
        petRecorder.savePetsToFile(getContext());

        mainActivity.feederState.clearFoodList();

        Toast.makeText(getContext(), "Datos Eliminados", Toast.LENGTH_SHORT).show();

    }

    public void sinchronizeApp(){
        Toast.makeText(getContext(), "App sincronizada", Toast.LENGTH_SHORT).show();
    }

    private static final int PADDING_LEFT = 20;
    private static final int PADDING_TOP = 10;
    private static final int PADDING_RIGHT = 0;
    private static final int PADDING_BOTTOM = 0;
    private static final int PADDING_PANEL = 16;

    private void addFoodToContainer(ViewGroup container, Food food) {
        // Crear un contenedor para el Pet
        LinearLayout foodContainer = new LinearLayout(getContext());

        foodContainer.setOnClickListener(v -> DeleteFoodDialog(food));

        foodContainer.setOrientation(LinearLayout.VERTICAL);
        foodContainer.setPadding(PADDING_PANEL, PADDING_PANEL, PADDING_PANEL, PADDING_PANEL);
        foodContainer.setBackgroundResource(R.drawable.panel_redondeado); // Asignar el drawable como fondo

        // Crear TextView para el nombre
        TextView nameTextView = new TextView(getContext());
        nameTextView.setText("Horario: " + food.getHour());
        nameTextView.setTextSize(24);
        nameTextView.setPadding(PADDING_LEFT, PADDING_TOP, PADDING_RIGHT, PADDING_BOTTOM);
        nameTextView.setTextColor(Color.BLACK);
        foodContainer.addView(nameTextView);

        // Crear TextView para feed_times
        TextView feedTimesTextView = new TextView(getContext());
        feedTimesTextView.setText("Cantidad de Comida: " + food.getFood_amount());
        feedTimesTextView.setTextSize(16);
        feedTimesTextView.setPadding(PADDING_LEFT, PADDING_TOP, PADDING_RIGHT, PADDING_BOTTOM);
        foodContainer.addView(feedTimesTextView);

        // Agregar el contenedor del Pet al contenedor principal
        container.addView(foodContainer);

        // Añadir un margen inferior al contenedor del Pet
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) foodContainer.getLayoutParams();
        params.setMargins(0, 0, 0, 16);
        foodContainer.setLayoutParams(params);
    }

    private void DeleteFoodDialog(Food food) {
        LayoutInflater inflater = getLayoutInflater();
        View popupView = inflater.inflate(R.layout.popup_notifications_layout, null);

        // Crear el AlertDialog
        AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(getContext(), R.style.CustomDialogTheme));
        builder.setView(popupView)
                .setTitle("¿Eliminar horario " + food.getHour() + " ?")
                .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Acción al hacer clic en "Aceptar"
                        //aca deberia estar el delete
                        mainActivity.feederState.getFeederRecorder().deleteFood(food);
                        mainActivity.feederState.getFeederRecorder().saveFoodToFile(getContext());

                    }
                })
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Acción al hacer clic en "Cancelar"
                        dialog.dismiss();
                    }
                });

        // Mostrar el AlertDialog
        dialog = builder.create();
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                // cambio color texto botones
                int textColor = android.graphics.Color.argb(255, 0, 0, 0);
                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(textColor);
                dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(textColor);
            }
        });
        dialog.show();

    }

    public void acceptDialog(){
        if(dialog != null){
            Button positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
            positiveButton.performClick();
        }

    }
}