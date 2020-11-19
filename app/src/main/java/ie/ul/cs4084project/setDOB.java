package ie.ul.cs4084project;

import android.icu.util.LocaleData;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link setDOB#newInstance} factory method to
 * create an instance of this fragment.
 */
public class setDOB extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private String userType;
    private String dOB;
    private String age;

    public setDOB() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment setDOB.
     */
    // TODO: Rename and change types and number of parameters
    public static setDOB newInstance(String param1, String param2) {
        setDOB fragment = new setDOB();
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
        return inflater.inflate(R.layout.fragment_set_d_o_b, container, false);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        dOB = formatter.format(new Date());
        age = "0";
        final DatePicker datePicker = getActivity().findViewById(R.id.datePickerSetDOB);

        datePicker.setOnDateChangedListener(new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                dOB = dayOfMonth + "/" + monthOfYear + "/" + year;
                LocalDate dateOB = LocalDate.of(year, monthOfYear+1, dayOfMonth);
                LocalDate date = LocalDate.now();
                Period period = Period.between(dateOB, date);
                age = Integer.toString(period.getYears());

            }
        });

        Button button = getActivity().findViewById(R.id.buttonNextSetDOB);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = getArguments();
                bundle.putString(ProfileSetUp.DOB, dOB);
                bundle.putString(ProfileSetUp.AGE, age);
                Navigation.findNavController(v).navigate(R.id.action_setDOB_to_setBasicInfo3, bundle);
            }
        });
    }
}