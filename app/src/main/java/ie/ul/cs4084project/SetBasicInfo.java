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
import android.widget.CalendarView;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SetBasicInfo#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SetBasicInfo extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private String userType;
    private ArrayList<GenderItem> genderList;
    private ArrayList<GoalItem> goalList;
    private GoalAdapter goalAdapter;
    private GenderAdapter genderAdapter;
    private String gender, goal;
    private  int genderImage, goalImage;

    public SetBasicInfo() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SetBasicInfo.
     */
    // TODO: Rename and change types and number of parameters
    public static SetBasicInfo newInstance(String param1, String param2) {
        SetBasicInfo fragment = new SetBasicInfo();
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
        genderList = new ArrayList<GenderItem>();
        genderList.add(new GenderItem("Male", R.drawable.male_blue));
        genderList.add(new GenderItem("Female", R.drawable.female_pink));
        genderList.add(new GenderItem("Other", R.drawable.other_gender_rainbow));

        goalList = new ArrayList<GoalItem>();
        goalList.add(new GoalItem("Gain Muscle", R.drawable.muscle_green));
        goalList.add(new GoalItem("Lose Weight", R.drawable.weight_green));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_set_basic_info, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        userType = getArguments().getString(ProfileSetUp.USER_TYPE);
        Spinner spinnerGender = getActivity().findViewById(R.id.spinnerGenderSetBasicInfo);
        Spinner spinnerGoal = getActivity().findViewById(R.id.spinnerGoalSetBasicInfo);
        genderAdapter = new GenderAdapter(getContext(), genderList);
        goalAdapter = new GoalAdapter(getContext(), goalList);
        spinnerGender.setAdapter(genderAdapter);
        spinnerGoal.setAdapter(goalAdapter);

        spinnerGender.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                GenderItem genderItem = (GenderItem) parent.getItemAtPosition(position);
                gender = genderItem.getGender();
                genderImage = genderItem.getGenderImage();
                Toast.makeText(getContext(), gender, Toast.LENGTH_SHORT);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinnerGoal.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                GoalItem goalItem = (GoalItem) parent.getItemAtPosition(position);
                goal = goalItem.getGoal();
                goalImage = goalItem.getGoalImage();
                Toast.makeText(getContext(), gender, Toast.LENGTH_SHORT);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        Button button = getActivity().findViewById(R.id.buttonNextSetBasicInfo);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = getArguments();
                bundle.putString(ProfileSetUp.GENDER, gender);
                bundle.putString(ProfileSetUp.GOAL, goal);
                bundle.putInt(ProfileSetUp.GENDER_IMAGE, genderImage);
                bundle.putInt(ProfileSetUp.GOAL_IMAGE, goalImage);
                Navigation.findNavController(v).navigate(R.id.action_setBasicInfo_to_setBio, bundle);
            }
        });
    }
}