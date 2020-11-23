package ie.ul.cs4084project;

import android.graphics.Color;
import android.nfc.tech.NfcA;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SetDisplayName#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SetDisplayName extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private FirebaseFirestore db;
    private boolean go;

    public SetDisplayName() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SetDisplayName.
     */
    // TODO: Rename and change types and number of parameters
    public static SetDisplayName newInstance(String param1, String param2) {
        SetDisplayName fragment = new SetDisplayName();
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
        go = true;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_set_display_name, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final EditText editText = getActivity().findViewById(R.id.editTextSetDisplayName);
        Button button = getActivity().findViewById(R.id.buttonNextSetDisplayName);

        db = FirebaseFirestore.getInstance();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                go = true;
                db.collection("Display Names").get().addOnCompleteListener(
                        new OnCompleteListener<QuerySnapshot>() {

                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    List<DocumentSnapshot> posts = task.getResult().getDocuments();
                                    for (int i = (posts.size() - 1); i > -1; i--) {
                                        DocumentSnapshot document = posts.get(i);
                                        if (document.get("Display Name").toString().compareToIgnoreCase(editText.getText().toString())==0) {
                                            go = false;
                                        }
                                    }
                                }
                                if (go) {
                                    Bundle bundle = getArguments();
                                    bundle.putString(ProfileSetUp.DISPLAY_NAME, editText.getText().toString());
                                    Navigation.findNavController(v).navigate(R.id.action_setDisplayName_to_summary, bundle);
                                }else {
                                    editText.getText().clear();
                                    editText.setHint("Display Name Taken");
                                    editText.setHintTextColor(Color.RED);
                                    Toast.makeText(getContext(), "Display Name Taken", Toast.LENGTH_LONG);
                                }
                            }
                        });
            }
        });
    }
}