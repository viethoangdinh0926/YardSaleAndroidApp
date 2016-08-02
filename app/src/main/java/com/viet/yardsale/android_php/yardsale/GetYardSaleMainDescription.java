package com.viet.yardsale.android_php.yardsale;

import android.app.Activity;
import android.os.AsyncTask;
import android.widget.TextView;

import com.viet.yardsale.R;
import com.viet.yardsale.search_yardsale_operations.YardSalePreviewActivity;
import com.viet.yardsale.services.StaticComponents;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

/**
 * Created by Viet on 6/23/2015.
 */
public class GetYardSaleMainDescription extends AsyncTask {
    private Activity activity;

    public GetYardSaleMainDescription(Activity activity) {
        this.activity = activity;
    }

    @Override
    protected Object doInBackground(Object[] params) {

        try {
            String data = "";
            String link = "";

            String username = (String) params[0];

            link = StaticComponents.dbAdress + "getMainDescription.php";
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
            activity.finish();//close the activity when encountering online transfer errors
            return "Please check internet connection.";
        }
    }

    @Override
    protected void onPostExecute(Object result) {
        ((TextView)((YardSalePreviewActivity)activity).findViewById(R.id.mainDescription)).setText((String)result);
    }
}
