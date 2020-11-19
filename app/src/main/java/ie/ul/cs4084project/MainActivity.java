package ie.ul.cs4084project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.IdpResponse;
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
    private StorageReference storageReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        storageReference = FirebaseStorage.getInstance().getReference().child("profilesInfo");;
        setContentView(R.layout.activity_main);
    }

    public void signIn(View view){
        List<AuthUI.IdpConfig> providers= Arrays.asList(
                new AuthUI.IdpConfig.EmailBuilder().build()
        );

        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(providers)
                        .build(), RC_SIGN_IN
        );
    }

    public void signInTest(View view){

        Intent intent = new Intent(this, HomeActivity.class);
        intent.putExtra(ID, "1");
        intent.putExtra(EMAIL, "asdf");
        intent.putExtra(NAME, "Me");

        Toast.makeText(this, "Sign in successful!", Toast.LENGTH_SHORT).show();

        //storageReference.

        //if() {
            startActivity(intent);
        //}
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == RC_SIGN_IN){
            IdpResponse response = IdpResponse.fromResultIntent(data);

            if(resultCode == RESULT_OK){
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                System.out.println("Sign in successful! \n"+
                        "name = "+user.getDisplayName() +
                        "\nemail = " + user.getEmail() +
                        "\nid = " + user.getUid());
                Intent intent = new Intent(this, HomeActivity.class);
                intent.putExtra(ID, user.getUid());
                intent.putExtra(EMAIL, user.getEmail());
                intent.putExtra(NAME, user.getDisplayName());

                Toast.makeText(this, "Sign in successful!", Toast.LENGTH_SHORT).show();
                startActivity(intent);
            }else{
                if(response == null){
                    Toast.makeText(this, "Incorrect E-mail or Password!.", Toast.LENGTH_SHORT).show(); return;
                }
                if(response.getError().getErrorCode() == ErrorCodes.NO_NETWORK){

                    Toast.makeText(this, "Error! No network.", Toast.LENGTH_SHORT).show(); return;
                }
            }
        }
    }
}