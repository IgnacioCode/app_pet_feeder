package soa.L6.pet_feeder.ui.notifications;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import soa.L6.pet_feeder.Activities.MainActivity;
import soa.L6.pet_feeder.Model.PetRecorder;
import soa.L6.pet_feeder.R;
import soa.L6.pet_feeder.databinding.FragmentNotificationsBinding;

public class NotificationsFragment extends Fragment {

    private Button btn_sincro;
    private Button btn_delete;

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
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public void deleteAppData(){
        MainActivity mainActivity = (MainActivity) getActivity();
        LinearLayout containerLayout = binding.getRoot().findViewById(R.id.contenedor_linear);

        PetRecorder petRecorder = mainActivity.petRecorder;

        petRecorder.clearPetList();

        petRecorder.savePetsToFile(getContext());

        Toast.makeText(getContext(), "Datos Eliminados", Toast.LENGTH_SHORT).show();

    }

    public void sinchronizeApp(){
        Toast.makeText(getContext(), "App sincronizada", Toast.LENGTH_SHORT).show();
    }
}