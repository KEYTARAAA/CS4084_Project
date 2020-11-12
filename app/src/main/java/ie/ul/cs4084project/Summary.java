package ie.ul.cs4084project;

import android.graphics.Bitmap;
import android.graphics.Picture;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Summary#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Summary extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters

    private List<Fragment> fragments;
    private Bitmap bitmap;

    public Summary() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Summary.
     */
    // TODO: Rename and change types and number of parameters
    public static Summary newInstance(String param1, String param2) {
        Summary fragment = new Summary();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        fragments = new ArrayList<Fragment>();

        fragments.add(new ProfilePosts());
        fragments.add(new ProfilePicsAndVids());
        fragments.add(new ProfileAbout(getArguments()));
        fragments.add(new ProfileFriends());
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_summary, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        ViewPagerAdapter adapter = new ViewPagerAdapter(getActivity().getSupportFragmentManager(),  fragments);
        ViewPager viewPager = getActivity().findViewById(R.id.viewPagerSummary);
        viewPager.setAdapter(adapter);

        TabLayout tabLayout = getActivity().findViewById(R.id.tabLayoutSummary);

        tabLayout.setupWithViewPager(viewPager);
        tabLayout.getTabAt(0).setText("Posts");
        tabLayout.getTabAt(0).setTag("Posts");
        tabLayout.getTabAt(1).setText("Pics");
        tabLayout.getTabAt(1).setTag("Pics");
        tabLayout.getTabAt(2).setText("About");
        tabLayout.getTabAt(2).setTag("About Feed");
        tabLayout.getTabAt(3).setText("Friends");
        tabLayout.getTabAt(3).setTag("Friends");


        ImageView imageView = getActivity().findViewById(R.id.imageViewSummary);

        imageView.setImageURI(Uri.parse(getArguments().getString(ProfileSetUp.PROFILE_PIC)));
        DisplayMetrics metrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);
        imageView.getLayoutParams().width = (int)(metrics.widthPixels * 0.297619);
        imageView.getLayoutParams().height = (int)(metrics.widthPixels * 0.297619);
        imageView.requestLayout();

        TextView textView = getActivity().findViewById(R.id.textViewName);
        textView.setText("Claire Molloy");//getActivity().getIntent().getStringExtra(MainActivity.NAME));

        textView = getActivity().findViewById(R.id.textViewDisplayName);
        textView.setText("@" + getArguments().getString(ProfileSetUp.DISPLAY_NAME));

        textView = getActivity().findViewById(R.id.textViewSummaryBio);
        textView.setText(getArguments().getString(ProfileSetUp.BIO));


    }
}