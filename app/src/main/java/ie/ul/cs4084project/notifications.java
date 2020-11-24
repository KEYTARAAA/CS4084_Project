package ie.ul.cs4084project;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link notifications#newInstance} factory method to
 * create an instance of this fragment.
 */
public class notifications extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static String id;


    // TODO: Rename and change types of parameters

    private FirebaseFirestore db;
    private LinearLayout linearLayout;
    private int count;
    private boolean check;

    public notifications(final String id) {

        db = FirebaseFirestore.getInstance();
        this.id = id;
        check = false;
        count=0;
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment notifications.
     */
    // TODO: Rename and change types and number of parameters
    public static notifications newInstance(String param1, String param2) {
        notifications fragment = new notifications(id);
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_notifications, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        linearLayout = getActivity().findViewById(R.id.scrollNotifications);
        check = true;
        reload();

    }

    public void reload(){
        linearLayout.removeAllViews();
        db.collection("Profiles").document(id).collection("Notifications")
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                List<DocumentSnapshot> notifications = task.getResult().getDocuments();
                for (DocumentSnapshot ds: notifications) {
                    if(ds.get("Type").toString().compareTo("Friend Request") == 0){
                        friendRequest(ds);
                    }else if(ds.get("Type").toString().compareTo("Friend Request Accepted") == 0){
                        friendRequestAccepted(ds);
                    }
                }
            }
        });
    }

    private void friendRequestAccepted(final DocumentSnapshot ds){
        CardView cardView = new CardView(getContext());
        ViewGroup.MarginLayoutParams lp = new ViewGroup.MarginLayoutParams(ViewGroup.MarginLayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        lp.bottomMargin = 20;
        cardView.setLayoutParams(lp);
        cardView.setCardElevation(10);
        cardView.setElevation(10);
        LinearLayout layout = new LinearLayout(getContext());
        layout.setOrientation(LinearLayout.HORIZONTAL);
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        ImageView imageView = new ImageView(getContext());
        imageView.setLayoutParams(layoutParams);
        imageView.getLayoutParams().width = 200;
        imageView.getLayoutParams().height = 200;
        imageView.requestLayout();
        new DownloadImageTask(imageView)
                .execute(ds.get("Profile Picture").toString());
        TextView textView = new TextView(getContext());
        textView.setText("@" + ds.get("Display Name").toString() + "\n" + ds.get("Name").toString());
        final TextView text = new TextView(getContext());
        text.setText("You and "+ds.get("Name")+" are now friends!");
        textView.setPadding(50,25,50,0);
        layout.addView(imageView);
        layout.addView(textView);
        layout.addView(text);
        cardView.addView(layout);
        cardView.setRadius(80);
        cardView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                getProfile(ds.get("ID").toString());
            }
        });
        linearLayout.addView(cardView);
    }

    private void friendRequest(final DocumentSnapshot ds){
        CardView cardView = new CardView(getContext());
        LinearLayout layout = new LinearLayout(getContext());
        layout.setOrientation(LinearLayout.HORIZONTAL);
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        ImageView imageView = new ImageView(getContext());
        imageView.setLayoutParams(layoutParams);
        imageView.getLayoutParams().width = 200;
        imageView.getLayoutParams().height = 200;
        imageView.requestLayout();
        new DownloadImageTask(imageView)
                .execute(ds.get("Profile Picture").toString());
        TextView textView = new TextView(getContext());
        textView.setText("@" + ds.get("Display Name").toString() + "\n" + ds.get("Name").toString());
        final Button button = new Button(getContext());
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(button.getText().toString().compareTo("Confirm") == 0) {
                    addFriend(ds.get("ID").toString(), ds.get("Name").toString(), ds.get("Display Name").toString(), ds.get("Profile Picture").toString(), button);
                }
            }
        });
        button.setText("Confirm");
        button.setBackgroundColor(Color.rgb(0,225,0));
        layout.addView(imageView);
        layout.addView(textView);
        layout.addView(button);
        cardView.addView(layout);
        cardView.setRadius(80);
        cardView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                getProfile(ds.get("ID").toString());
            }
        });
        linearLayout.addView(cardView);
    }

    private void addFriend(final String viewProfileId, String name, String displayName, String profilePic, Button button){
        button.setText("Confirmed");
        Map<String, String> data = new HashMap();
        data.put("Name", name);
        data.put("Display Name", displayName);
        data.put("ID", viewProfileId);
        data.put("Profile Picture", profilePic);
        db.collection("Profiles").document(id).collection("Friends")
                .document(viewProfileId).set(data);

        changeNotification(viewProfileId);

        db.collection("Profiles").document(id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                DocumentSnapshot ds = task.getResult();
                String myProfilePicture = ds.get("Profile Picture").toString();
                String myName = ds.get("Name").toString();
                String myId = ds.get("ID").toString();
                String myDisplayName = ds.get("Display Name").toString();

                Map<String, String> myData = new HashMap<>();
                myData.put("Name", myName);
                myData.put("Display Name", myDisplayName);
                myData.put("ID", myId);
                myData.put("Profile Picture", myProfilePicture);
                db.collection("Profiles").document(viewProfileId).collection("Friends")
                        .document(id).set(myData);
            }
        });
    }



    private void changeNotification(final String viewProfileId){
        final String newKey = id +"-" + System.currentTimeMillis();
        db.collection("Profiles").document(id).collection("Unanswered Friend Requests")
                .document(viewProfileId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                DocumentSnapshot ds = task.getResult();
                String key = ds.get("Key").toString();

                Map<String, String> data = new HashMap<>();
                data.put("Key", newKey);
                data.put("Type", "Friend Request Accepted");
                data.put("Seen", "false");
                data.put("Name", ds.get("Name").toString());
                data.put("Display Name", ds.get("Display Name").toString());
                data.put("ID", ds.get("ID").toString());
                data.put("Profile Picture", ds.get("Profile Picture").toString());
                db.collection("Profiles").document(id).collection("Notifications")
                        .document(newKey).set(data);
                db.collection("Profiles").document(id).collection("Notifications").document(key).delete();//maybe too fast
                db.collection("Profiles").document(id).collection("Unanswered Friend Requests").document(viewProfileId).delete();
                db.collection("Profiles").document(viewProfileId).collection("Pending Friend Requests").document(id).delete();


            }
        });//set notification in theirs aswel

        db.collection("Profiles").document(id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                DocumentSnapshot ds = task.getResult();
                Map<String, String> data = new HashMap<>();
                data.put("Key", newKey);
                data.put("Type", "Friend Request Accepted");
                data.put("Seen", "false");
                data.put("Name", ds.get("Name").toString());
                data.put("Display Name", ds.get("Display Name").toString());
                data.put("ID", ds.get("ID").toString());
                data.put("Profile Picture", ds.get("Profile Picture").toString());
                db.collection("Profiles").document(viewProfileId).collection("Notifications")
                        .document(newKey).set(data);


            }
        });
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

    private void getProfile(String id) {
        Intent intent = new Intent(getActivity(), ViewProfile.class);
        intent.putExtra(MainActivity.ID, getActivity().getIntent().getStringExtra(MainActivity.ID));
        intent.putExtra(MainActivity.EMAIL, getActivity().getIntent().getStringExtra(MainActivity.EMAIL));
        intent.putExtra(MainActivity.NAME, getActivity().getIntent().getStringExtra(MainActivity.NAME));
        intent.putExtra(Search.VIEW_PROFILE_ID, id);
        startActivity(intent);
    }

    public int getCount() {

        db.collection("Profiles").document(id).collection("Notifications")
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                List<DocumentSnapshot> notifications = task.getResult().getDocuments();
                for (DocumentSnapshot ds : notifications) {
                    if (ds.get("Seen").toString().compareTo("false") == 0) {
                        count++;
                        db.collection("Profiles").document(id).collection("Notifications").document(ds.get("ID").toString())
                                .update("Seen", "true");
                    }
                }

            }
        });

        return count;
    }

    public boolean getCheck(){
        return check;
    }
}