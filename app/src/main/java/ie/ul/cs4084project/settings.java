package ie.ul.cs4084project;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link settings#newInstance} factory method to
 * create an instance of this fragment.
 */
public class settings extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static String name, id, email;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public settings(String name, String id, String email) {
        this.name = name;
        this.id = id;
        this.email = email;
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment settings.
     */
    // TODO: Rename and change types and number of parameters
    public static settings newInstance(String param1, String param2) {
        settings fragment = new settings(name, id, email);
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_settings, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);




        Button securityUpdateButton = getActivity().findViewById(R.id.securityUpdate);
        securityUpdateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), securityUpdate.class);
                intent.putExtra(MainActivity.ID, id);
                intent.putExtra(MainActivity.EMAIL, email);
                intent.putExtra(MainActivity.NAME, name);
                startActivity(intent);
            }
        });

        Button profileUpdateButton = getActivity().findViewById(R.id.profileUpdate);
        profileUpdateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), profileUpdate.class);
                intent.putExtra(MainActivity.ID, id);
                intent.putExtra(MainActivity.EMAIL, email);
                intent.putExtra(MainActivity.NAME, name);
                startActivity(intent);
            }
        });


        Button run = getActivity().findViewById(R.id.buttonSettingsRun);
        Button intervals = getActivity().findViewById(R.id.buttonSettingsIntervalTraining);
        run.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getActivity(), Run.class);
                intent.putExtra(MainActivity.ID, id);
                intent.putExtra(MainActivity.EMAIL, email);
                intent.putExtra(MainActivity.NAME, name);

                startActivity(intent);
            }
        });

        intervals.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getActivity(), TrainingIntervals.class);
                intent.putExtra(MainActivity.ID, id);
                intent.putExtra(MainActivity.EMAIL, email);
                intent.putExtra(MainActivity.NAME, name);

                startActivity(intent);
            }
        });
    }
}