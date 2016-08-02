package com.viet.yardsale.android_php_yardsale;

import android.app.Activity;
import android.os.AsyncTask;
import android.widget.TextView;

import com.viet.yardsale.EditAccountActivity;
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
public class GetCurrentUserEmail extends AsyncTask {
    private Activity activity;

    public GetCurrentUserEmail(Activity activity){
        this.activity = activity;
    }

    @Override
    protected Object doInBackground(Object[] params) {

        try {
            String data = "";
            String link = "";

            String username = (String) params[0];

            link = StaticComponents.dbAdress + "getCurrentUserEmail.php";
            data += "&" + URLEncoder.encode("username", "UTF-8") + "=" + URLEncoder.encode(username, "UTF-8");

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
            return "Please check internet connection.";
        }
    }

    @Override
    protected void onPostExecute(Object result){
        StaticComponents.unfreezeActivity(activity);//release the activity after finish processing
        String[] temps = ((String)result).split("\\s+");
        if(temps[0].equals("done")){
            ((TextView)((EditAccountActivity) activity).findViewById(R.id.currentEmail)).setText(temps[1]);
        }
        else {
            ((TextView)((EditAccountActivity) activity).findViewById(R.id.errMessage)).setText((String) result);
        }
    }
}