package com.viet.yardsale.android_php.yardsale;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.TextView;

import com.viet.yardsale.ConfirmPWSentActivity;
import com.viet.yardsale.ForgetPasswordActivity;
import com.viet.yardsale.R;
import com.viet.yardsale.services.StaticComponents;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

/**
 * Created by Viet on 6/22/2015.
 */
public class GenerateUserTempPassword extends AsyncTask {
    private Activity activity;
    private String tempEmail;


    public GenerateUserTempPassword(Activity activity){
        this.activity = activity;
    }

    @Override
    protected Object doInBackground(Object[] params) {

        try {
            String data = "";
            String link = "";

            String username = (String) params[0];
            String regEmail = (String) params[1];
            tempEmail = regEmail;

            link = StaticComponents.dbAdress + "generateTempPassword.php";
            data += "&" + URLEncoder.encode("username", "UTF-8") + "=" + URLEncoder.encode(username, "UTF-8");
            data += "&" + URLEncoder.encode("regEmail", "UTF-8") + "=" + URLEncoder.encode(regEmail, "UTF-8");

            URL url = new URL(link);
            URLConnection conn = url.openConnection();

            conn.setDoOutput(true);
            OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());

            wr.write(data);
            wr.flush();

            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));

            StringBuilder sb = new StringBuilder();
            String line = null;

            // Read Server Response
            while ((line = reader.readLine()) != null) {
                sb.append(line);
                break;
            }

            return sb.toString();
        } catch (Exception e) {
            //return new String("Exception: " + e.getMessage());
            return "Please check internet connection";
        }
    }

    @Override
    protected void onPostExecute(Object result){
        StaticComponents.unfreezeActivity(activity);//release the activity after finish processing
        String tempResult = (String)result;
        if(tempResult.equals("done")){
            StaticComponents.signup_or_forget_password_user_email = tempEmail;
            Intent intent = new Intent(activity, ConfirmPWSentActivity.class);
            activity.startActivity(intent);
            activity.finish();
        }
        else {
            ((TextView)((ForgetPasswordActivity) activity).findViewById(R.id.errMessage)).setText((String) result);
        }
    }
}
