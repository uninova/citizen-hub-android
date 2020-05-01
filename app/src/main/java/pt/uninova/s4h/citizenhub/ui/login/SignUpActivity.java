package pt.uninova.s4h.citizenhub.ui.login;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.concurrent.ExecutionException;

import pt.uninova.s4h.citizenhub.ui.Home;
import pt.uninova.s4h.citizenhub.ui.R;

public class SignUpActivity extends AppCompatActivity {

    EditText email;
    EditText password;
    CheckBox check;
    boolean accepted_terms = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        email = findViewById(R.id.text_email_login);
        password = findViewById(R.id.text_password_login);
        accepted_terms = false;
        check = findViewById(R.id.checkBox);
    }

    @Override
    public void onBackPressed() {
        finish(); //to return to login
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public void checkSignUp(View view) throws ExecutionException, InterruptedException, ClassNotFoundException, InstantiationException, IllegalAccessException {
        if (checkFields(true) == false)
            return;

        if (!haveNetworkConnection()) {
            Toast.makeText(this, "Please, check your internet connection!", Toast.LENGTH_LONG).show();
            return;
        }

        if (!check.isChecked()) {
            Toast.makeText(this, "Please accept terms and conditions.", Toast.LENGTH_LONG).show();
            return;
        }

        String current_email = email.getText().toString();
        String current_password = password.getText().toString();

        if (!current_email.contains("@") || !current_email.contains(".")) {
            Toast.makeText(this, "Please, enter a valid e-mail!", Toast.LENGTH_LONG).show();
            return;
        }
        String type = "signup";
        //TODO following three statements enable connection to server
        //LoginBackgroundWork loginBackgroundWorker = new LoginBackgroundWork(this);
        //loginBackgroundWorker.execute(type, current_email, current_password);
        //String result = loginBackgroundWorker.get();
        String result = "Successful Registration!"; //TODO remove this later, only to bypass connection to server
        if (result.contains("Successful Registration!")) {
            Toast.makeText(this, "Welcome!", Toast.LENGTH_LONG).show();
            Home.loggedIn = true;
            Home.loggedEmail = current_email;
            LoginActivity.loginActivity.finish();
            finish(); //to go back to previous activity
        } else {
            Toast.makeText(this, "Error, Please Try Again.", Toast.LENGTH_LONG).show();
        }
    }

    boolean checkFields(boolean fields) //true is both fields, false is only email
    {
        if (email.getText().toString().matches("") && fields == false) {
            Toast.makeText(this, "You did not enter an e-mail", Toast.LENGTH_SHORT).show();
            return false;
        } else if (email.getText().toString().matches("") && fields == true) {
            Toast.makeText(this, "You did not enter an e-mail", Toast.LENGTH_SHORT).show();
            return false;
        } else if (password.getText().toString().matches("") && fields == true) {
            Toast.makeText(this, "You did not enter a password", Toast.LENGTH_SHORT).show();
            return false;
        } else
            return true;
    }

    public void displayMessage(View view) {
        check.setChecked(false);
        final EditText taskEditText = new EditText(this);
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("")
                .setMessage("This mobile application will ask you for your e-mail contact in order to " +
                        "keep you updated with new versions of the application and to divulge information " +
                        "on the project outcomes. The data collected by this application is soly stored in " +
                        "your mobile application and will not be deposited by the application developers in " +
                        "any other device or infrastructure. e-mail will be archived at UNINOVA – FCT – NOVA " +
                        "University.At any time you can contact us to op-out from our database of users. ")
                .setPositiveButton("Accept", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        accepted_terms = true;
                        check.setChecked(true);
                        InputMethodManager inputManager = (InputMethodManager)
                                getSystemService(Context.INPUT_METHOD_SERVICE);

                        inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                                InputMethodManager.HIDE_NOT_ALWAYS);
                    }
                })
                .setNegativeButton("Reject", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        accepted_terms = false;
                        check.setChecked(false);
                    }
                })
                .create();
        dialog.show();
    }

    private boolean haveNetworkConnection() {
        boolean haveConnectedWifi = false;
        boolean haveConnectedMobile = false;

        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] netInfo = cm.getAllNetworkInfo();
        for (NetworkInfo ni : netInfo) {
            if (ni.getTypeName().equalsIgnoreCase("WIFI"))
                if (ni.isConnected())
                    haveConnectedWifi = true;
            if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
                if (ni.isConnected())
                    haveConnectedMobile = true;
        }
        return haveConnectedWifi || haveConnectedMobile;
    }
}


