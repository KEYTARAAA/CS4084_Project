package ie.ul.cs4084project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final int RC_SIGN_IN = 123;
    protected static final String ID = "ID";
    protected static final String EMAIL = "EMAIL";
    protected static final String NAME = "NAME";
    protected static final String PROFILE_PICTURE = "PROFILE_PICTURE";
    protected static final String DISPLAY_NAME = "DISPLAY_NAME";
    private String name, id, email;
    private StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        storageReference = FirebaseStorage.getInstance().getReference().child("profilesInfo");
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) !=
                PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION }
        ,PackageManager.PERMISSION_GRANTED);
        }
        setContentView(R.layout.activity_main);
    }

    public void signIn(View view) {
        List<AuthUI.IdpConfig> providers = Arrays.asList(
                new AuthUI.IdpConfig.EmailBuilder().build()
        );

        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(providers)
                        .build(), RC_SIGN_IN
        );
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);

            if (resultCode == RESULT_OK) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                name = user.getDisplayName();
                email = user.getEmail();
                id = user.getUid();

                storageReference.child(id + ".txt").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        done();
                    }


                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        setUp();
                    }
                });
            } else {
                if (response == null) {
                    Toast.makeText(this, "Incorrect E-mail or Password!.", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (response.getError().getErrorCode() == ErrorCodes.NO_NETWORK) {

                    Toast.makeText(this, "Error! No network.", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        }
    }

    private void done() {

        Intent intent = new Intent(this, HomeActivity.class);
        intent.putExtra(ID, id);
        intent.putExtra(EMAIL, email);
        intent.putExtra(NAME, name);

        Toast.makeText(this, "Sign in successful!", Toast.LENGTH_SHORT).show();
        startActivity(intent);
    }

    private void setUp() {

        Intent intent = new Intent(this, ProfileSetUp.class);
        intent.putExtra(ID, id);
        intent.putExtra(EMAIL, email);
        intent.putExtra(NAME, name);

        Toast.makeText(this, "Sign in successful!", Toast.LENGTH_SHORT).show();
        startActivity(intent);
    }
}