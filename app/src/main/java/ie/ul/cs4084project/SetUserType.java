package ie.ul.cs4084project;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SetUserType#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SetUserType extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private Button button;
    private ArrayList<UserTypeItem> userTypeList;
    private UserTypeAdapter adapter;
    private String userType;

    public SetUserType() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SetUserType.
     */
    // TODO: Rename and change types and number of parameters
    public static SetUserType newInstance(String param1, String param2) {
        SetUserType fragment = new SetUserType();
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
        initList();
    }

    private void initList(){
        userTypeList = new ArrayList<UserTypeItem>();
        userTypeList.add(new UserTypeItem("Athlete", R.drawable.logo_green));
        userTypeList.add(new UserTypeItem("Gym", R.drawable.gym_green));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_set_user_type, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        Spinner spinner = getActivity().findViewById(R.id.spinnerSetUserType);
        adapter = new UserTypeAdapter(getContext(), userTypeList);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                UserTypeItem userTypeItem = (UserTypeItem) parent.getItemAtPosition(position);
                userType = userTypeItem.getUserType();
                Toast.makeText(getContext(), userType, Toast.LENGTH_SHORT);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        button = getActivity().findViewById(R.id.buttonNextSetUserType);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString(ProfileSetUp.USER_TYPE, userType);
                Navigation.findNavController(v).navigate(R.id.action_setUserType_to_setDOB3, bundle);
            }
        });
    }
    //https://www.youtube.com/watch?v=GeO5F0nnzAw
}