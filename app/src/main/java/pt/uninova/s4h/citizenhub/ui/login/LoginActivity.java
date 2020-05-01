package pt.uninova.s4h.citizenhub.ui.login;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.concurrent.ExecutionException;

import pt.uninova.s4h.citizenhub.ui.Home;
import pt.uninova.s4h.citizenhub.ui.R;

public class LoginActivity extends AppCompatActivity {

    static Activity loginActivity;
    EditText email;
    EditText password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        loginActivity = this;
        if (Home.bypassForTesting) {
            Home.loggedIn = true;
            //set something dummy here if needed
            finish(); //close this activity
        }
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        email = findViewById(R.id.text_email_login);
        password = findViewById(R.id.text_password_login);
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true); //to not return to home page
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public void checkLogin(View view) throws InterruptedException, ExecutionException {
        if (checkFields(true) == false)
            return;

        if (!haveNetworkConnection()) {
            Toast.makeText(this, "Please, check your internet connection!", Toast.LENGTH_LONG).show();
            return;
        }

        String current_email = email.getText().toString();
        String current_password = password.getText().toString();
        String type = "login";
        //TODO following three statements enable connection to server
        //LoginBackgroundWork loginBackgroundWorker = new LoginBackgroundWork(this);
        //loginBackgroundWorker.execute(type, current_email, current_password);
        //String result = loginBackgroundWorker.get();
        String result = "Successful Login!"; //TODO remove this later, only to bypass connection to server
        if (result.matches("Successful Login!")) {
            Toast.makeText(this, result, Toast.LENGTH_LONG).show();
            Home.loggedIn = true;
            Home.loggedEmail = current_email;
            finish(); //to go back to previous activity (Home)
        }
    }

    boolean checkFields(boolean fields) //true is both fields, false is only email
    {
        if (email.getText().toString().matches("") && fields == false) {
            Toast.makeText(this, "You did not enter an e-mail", Toast.LENGTH_SHORT).show();
            return false;
        } else if (email.getText().toString().matches("") && fields == true) {
            Toast.makeText(this, "You did not enter an e-mail", Toast.LENGTH_SHORT).show();
            return false; //check for valid e-mail format is done in SignUp, check here unnecessary
        } else if (password.getText().toString().matches("") && fields == true) {
            Toast.makeText(this, "You did not enter a password", Toast.LENGTH_SHORT).show();
            return false;
        } else
            return true;
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

    public void checkSignUp(View view) throws ExecutionException, InterruptedException {
        if (!haveNetworkConnection()) {
            Toast.makeText(this, "Please, check your internet connection!", Toast.LENGTH_LONG).show();
            return;
        }

        Intent startIntent;
        startIntent = new Intent(this, SignUpActivity.class);
        this.startActivity(startIntent);
    }

    public void RecoverPassword(View view) {
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setType("text/plain");
        String[] recipients = {"uninova.development.team@gmail.com"};
        emailIntent.putExtra(Intent.EXTRA_EMAIL, recipients);
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "[Recover/Change Password] Want to Retrieve/Change my Password");
        emailIntent.putExtra(Intent.EXTRA_TEXT, "Please mention if you want to retrieve or change your password" +
                " and the e-mail address associated with your account: \n");

        startActivity(Intent.createChooser(emailIntent, "Send mail (choose Gmail, etc)"));
    }
}
