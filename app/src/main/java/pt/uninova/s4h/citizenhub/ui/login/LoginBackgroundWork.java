package pt.uninova.s4h.citizenhub.ui.login;

import android.app.AlertDialog;
import android.content.Context;
import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

public class LoginBackgroundWork extends AsyncTask<String, Void, String> {

    Context context;
    AlertDialog alertDialog;
    String result;
    String type;
    LoginBackgroundWork (Context ctx) {
        context = ctx;
    }

    @Override
    protected String doInBackground(String... params) {
        type = params[0];
        String login_url = "https://auth.smart4health.gris.uninova.pt/login.php";
        String signup_url = "https://auth.smart4health.gris.uninova.pt/signup.php";
        if(type.equals("login")) {
            try {
                String mail = params[1];
                String password = params[2];
                URL url = new URL(login_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String post_data = URLEncoder.encode("mail","UTF-8")+"="+URLEncoder.encode(mail,"UTF-8")+"&"
                        +URLEncoder.encode("password","UTF-8")+"="+URLEncoder.encode(password,"UTF-8");
                bufferedWriter.write(post_data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream,"iso-8859-1"));
                String result="";
                String line="";
                while((line = bufferedReader.readLine())!= null) {
                    result += line;
                }
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                return result;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else if(type.equals("signup")) {
            try {
                String mail = params[1];
                String password = params[2];
                URL url = new URL(signup_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String post_data = URLEncoder.encode("mail","UTF-8")+"="+URLEncoder.encode(mail,"UTF-8")+"&"
                        + URLEncoder.encode("password","UTF-8")+"="+URLEncoder.encode(password,"UTF-8");
                bufferedWriter.write(post_data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream,"iso-8859-1"));
                String result="";
                String line="";
                while((line = bufferedReader.readLine())!= null) {
                    result += line;
                }
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                return result;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    protected void onPreExecute() {
        alertDialog = new AlertDialog.Builder(context).create();
        alertDialog.setTitle("Login status");
    }

    @Override
    protected void onPostExecute(String result) {
        if(type.equals("signup"))
            alertDialog.setTitle("Sign Up status");
        else
            alertDialog.setTitle("Login status");

        if(result.contains("Error"))
        {
            if(type.equals("signup"))
                alertDialog.setMessage("Error, please try again. This e-mail may already exist.");
            else if(type.equals("login"))
                alertDialog.setMessage("Error, please try again. This password may be wrong.");
        }
        else if (result.contains("Successful Registration"))
        {
            if(type.equals("signup"))
                alertDialog.setMessage("Successful Registration!");
            else if(type.equals("login"))
                alertDialog.setMessage("Sucessful Login!");
        }
        else
            alertDialog.setMessage(result);
        if(!(result.contains("Successful Registration") && type.equals("login")))
            alertDialog.show();
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }

}
