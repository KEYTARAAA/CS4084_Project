package ie.ul.cs4084project;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileAbout#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileAbout extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private Bundle bundle;

    public ProfileAbout(Bundle bundle) {
        this.bundle = bundle;
    }
    // TODO: Rename and change types and number of parameters
    public static ProfileAbout newInstance(Bundle b) {
        ProfileAbout fragment = new ProfileAbout(b);
        Bundle args = new Bundle();
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
        return inflater.inflate(R.layout.fragment_profile_about, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TextView textView = getView().findViewById(R.id.textViewDOB);
        textView.setText("Date Of Birth: " + bundle.getString(ProfileSetUp.DOB));

        textView = getView().findViewById(R.id.textViewAge);
        textView.setText("Age: " + bundle.getString(ProfileSetUp.AGE));

        textView = getView().findViewById(R.id.textViewGoal);
        textView.setText(bundle.getString(ProfileSetUp.GOAL));

        textView = getView().findViewById(R.id.textViewGender);
        textView.setText(bundle.getString(ProfileSetUp.GENDER));

        ImageView imageView1 = getView().findViewById(R.id.imageViewGender);
        imageView1.setImageResource(bundle.getInt(ProfileSetUp.GENDER_IMAGE));

        imageView1 = getView().findViewById(R.id.imageViewGoal);
        imageView1.setImageResource(bundle.getInt(ProfileSetUp.GOAL_IMAGE));
    }
}