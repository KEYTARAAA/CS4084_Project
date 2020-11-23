package ie.ul.cs4084project;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;

import java.io.InputStream;
import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link profile#newInstance} factory method to
 * create an instance of this fragment.
 */
public class profile extends Fragment {
private boolean profileComplete = false;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private static String string;
    private String userType, name, dateOB, age, gender, goal, bio, displayName, profilePic;
    private ArrayList<Fragment> fragments;

    public profile(String s) {
         String[] array = s.split("\n");
        userType = array[0].substring(12);
        name = array[1].substring(7);
        dateOB = array[2].substring(16);
        age = array[3].substring(6);
        gender = array[4].substring(9);
        goal = array[5].substring(7);
        bio = array[6].substring(6);
        displayName = array[7].substring(15);
        profilePic = array[8].substring(18);

        System.out.println(userType);
        System.out.println(name);
        System.out.println(dateOB);
        System.out.println(age);
        System.out.println(gender);
        System.out.println(goal);
        System.out.println(bio);
        System.out.println(displayName);
        System.out.println(profilePic);

        Bundle bundle = new Bundle();
        bundle.putString(ProfileSetUp.USER_TYPE, userType);
        bundle.putString(ProfileSetUp.DOB, dateOB);
        bundle.putString(ProfileSetUp.AGE, age);
        bundle.putString(ProfileSetUp.GENDER, gender);
        bundle.putString(ProfileSetUp.GOAL, goal);
        if(gender.compareToIgnoreCase("male") == 0){
            bundle.putInt(ProfileSetUp.GENDER_IMAGE, R.drawable.male_blue);
        }else if(gender.compareToIgnoreCase("female") == 0){
            bundle.putInt(ProfileSetUp.GENDER_IMAGE, R.drawable.female_pink);
        }else if(gender.compareToIgnoreCase("other") == 0){
            bundle.putInt(ProfileSetUp.GENDER_IMAGE, R.drawable.other_gender_rainbow);
        }

        if (goal.compareToIgnoreCase("lose weight")==0){
            bundle.putInt(ProfileSetUp.GOAL_IMAGE, R.drawable.weight_green);
        }else if (goal.compareToIgnoreCase("gain muscle")==0){
            bundle.putInt(ProfileSetUp.GOAL_IMAGE, R.drawable.muscle_green);
        }

        fragments = new ArrayList<Fragment>();

        fragments.add(new ProfilePosts());
        fragments.add(new ProfilePicsAndVids());
        fragments.add(new ProfileAbout(bundle));
        fragments.add(new ProfileFriends());
        /*String poopPeep= "POOOOOOOOOOOOOOOOOOOOOOP " + key +" PEEEEEEEEEEEEEEEEEEEP";
        String myRegexp = "\\b" + key + "\\b";
        System.out.println(myRegexp);
        String[] pp = poopPeep.split(myRegexp);

        System.out.println("KEEEEEEEEEEEEYYYYYYYYYY"+ key+"YYYYY"+ pp[1]);
        ArrayList<String[]> prof =new ArrayList<String[]>();
        for (String str: array) {
            String[] spl = str.split(key);
            for (String ss: spl) {
                System.out.println("CRAAAAAAAAAAAAAAAAAAAACK   " + ss);
            }
            prof.add(str.split(key));
        }

        for(int i=0; i<array.length; i++){
            String str = array[i];
            String[] spl = str.split(key);
            //for (int j=0; j<spl.length; j++) {

                System.out.println("CRAAAAAAAAAAAAAAAAAAAACK   " + array[i]);
            //}
            prof.add(spl);
        }

         userType = prof.get(1)[1];
         name = prof.get(2)[1];
         dateOB = prof.get(3)[1];
         age = prof.get(4)[1];
         gender = prof.get(5)[1];
         goal = prof.get(6)[1];
         bio = prof.get(7)[1];
         displayName = prof.get(8)[1];
         profilePic = prof.get(9)[1];


        */
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment profile.
     */
    // TODO: Rename and change types and number of parameters
    public static profile newInstance(String param1, String param2) {
        profile fragment = new profile(string);
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
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        ImageView imageView = getActivity().findViewById(R.id.imageViewProfile);
        new DownloadImageTask(imageView)
                .execute(profilePic);

        TextView textView = getActivity().findViewById(R.id.textViewProfileDisplayName);
        textView.setText("@"+displayName);
        textView = getActivity().findViewById(R.id.textViewProfileName);
        textView.setText(name);
        textView = getActivity().findViewById(R.id.textViewProfileBio);
        textView.setText(bio);
        ViewPagerAdapter adapter = new ViewPagerAdapter(getActivity().getSupportFragmentManager(), fragments);
        ViewPager viewPager = getActivity().findViewById(R.id.viewPagerProfile);
        viewPager.setAdapter(adapter);

        TabLayout tabLayout = getActivity().findViewById(R.id.tabLayoutProfile);

        tabLayout.setupWithViewPager(viewPager);
        tabLayout.getTabAt(0).setText("Posts");
        tabLayout.getTabAt(0).setTag("Posts");
        tabLayout.getTabAt(1).setText("Pics");
        tabLayout.getTabAt(1).setTag("Pics");
        tabLayout.getTabAt(2).setText("About");
        tabLayout.getTabAt(2).setTag("About Feed");
        tabLayout.getTabAt(3).setText("Friends");
        tabLayout.getTabAt(3).setTag("Friends");
    }



    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }
}