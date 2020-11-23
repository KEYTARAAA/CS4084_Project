package ie.ul.cs4084project;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.InputStream;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfilePosts#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfilePosts extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private FirebaseFirestore db;
    private StorageReference storageReference;
    private LinearLayout scrollView;
    private static String viewProfileId;

    public ProfilePosts(String viewProfileId) {
        this.viewProfileId = viewProfileId;
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfilePosts.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfilePosts newInstance(String param1, String param2) {
        ProfilePosts fragment = new ProfilePosts(viewProfileId);
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        db = FirebaseFirestore.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile_posts, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        scrollView = getActivity().findViewById(R.id.scrollProfilePosts);
        load();
    }

    private void load(){
        db.collection("Profiles").document(viewProfileId).
                collection("Posts").get().addOnCompleteListener(
                new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            List<DocumentSnapshot> posts = task.getResult().getDocuments();
                            for (int i = (posts.size() - 1); i > -1; i--) {
                                DocumentSnapshot document = posts.get(i);
                                if (!document.get("image").toString().equals("")) {

                                    System.out.println("POOOOOOOOOOOOOOOOOOOOOOOOOOOOP "+ i + "->" + document.get("image").toString());
                                    ImageView imageView = new ImageView(getContext());
                                    new DownloadImageTask(imageView)
                                            .execute(document.get("image").toString());
                                    TextView textView = new TextView(getContext());
                                    textView.setText(document.get("post").toString() + '\n' + '~' + document.get("name") + "\n\n");


                                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                                    layoutParams.gravity = Gravity.CENTER_HORIZONTAL;
                                    scrollView.addView(imageView, layoutParams);
                                    scrollView.addView(textView);

                                } else {
                                    TextView p = new TextView(getContext());
                                    p.setText(document.get("post").toString() + '\n' + '~' + document.get("name") + "\n\n");
                                    scrollView.addView(p);
                                }
                            }
                        }
                        System.out.println("WEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE " + scrollView.getChildCount());
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
}