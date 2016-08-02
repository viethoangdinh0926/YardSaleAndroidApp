package com.viet.yardsale.android_php_yardsale;

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
public class GetPictureDescription extends AsyncTask {
    private Activity activity;

    public GetPictureDescription(Activity activity) {
        this.activity = activity;
    }

    @Override
    protected Object doInBackground(Object[] params) {

        try {
            String data = "";
            String link = "";

            String username = (String) params[0];
            String picture_id = (String) params[1];

            link = StaticComponents.dbAdress + "getPictureDescription.php";
            data += "&" + URLEncoder.encode("username", "UTF-8") + "=" + URLEncoder.encode(username, "UTF-8");
            data += "&" + URLEncoder.encode("picture_id", "UTF-8") + "=" + URLEncoder.encode(picture_id, "UTF-8");

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

            return picture_id + sb.toString();
        } catch (Exception e) {
            activity.finish();
            return "Please check internet connection.";
        }
    }

    @Override
    protected void onPostExecute(Object result) {
        String pic_id = ((String)result).substring(0,1);
        String result1 = ((String)result).substring(1,((String)result).length());
        if (!(result1.equals("N/A"))) {
            if(pic_id.equals("0")){
                ((TextView) ((YardSalePreviewActivity) activity).findViewById(R.id.pic1Description)).setText(result1);
            }
            else if(pic_id.equals("1")){
                ((TextView) ((YardSalePreviewActivity) activity).findViewById(R.id.pic2Description)).setText(result1);
            }
            else if(pic_id.equals("2")){
                ((TextView) ((YardSalePreviewActivity) activity).findViewById(R.id.pic3Description)).setText(result1);
            }
        }
    }
}
