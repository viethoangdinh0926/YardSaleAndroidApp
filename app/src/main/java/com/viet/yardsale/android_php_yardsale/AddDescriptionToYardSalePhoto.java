package com.viet.yardsale.android_php_yardsale;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.viet.yardsale.R;
import com.viet.yardsale.post_yardsale_operations.AddingMoreYardSaleDetailActivity;
import com.viet.yardsale.services.StaticComponents;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

/**
 * Created by Viet on 6/12/2015.
 */
public class AddDescriptionToYardSalePhoto extends AsyncTask {
    private Activity activity;
    private boolean nextAction;

    public AddDescriptionToYardSalePhoto(Activity activity, boolean nextAction) {
        this.activity = activity;
        this.nextAction = nextAction;
    }

    @Override
    protected Object doInBackground(Object[] params) {

        try {
            String data = "";
            String link = "";

            String image = (String) params[0];
            String description = (String) params[1];

            link = StaticComponents.dbAdress + "uploadProductDetails.php";
            data += "&" + URLEncoder.encode("file_path", "UTF-8") + "=" + URLEncoder.encode(image, "UTF-8");
            data += "&" + URLEncoder.encode("description", "UTF-8") + "=" + URLEncoder.encode(description, "UTF-8");

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
    protected void onPostExecute(Object result) {
        StaticComponents.unfreezeActivity(activity);

        AddingMoreYardSaleDetailActivity pa = (AddingMoreYardSaleDetailActivity)activity;
        if(((String)result).equals("ok")){
            if(nextAction){
                pa.finish();
            }
            else {
                //hide keyboard
                InputMethodManager imm = (InputMethodManager)pa.getSystemService(
                        Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(((EditText)pa.findViewById(R.id.productDescription)).getWindowToken(), 0);

                //clear text
                ((EditText)pa.findViewById(R.id.productDescription)).setText("");

                pa.findViewById(R.id.cameraFrame).setVisibility(View.VISIBLE);
                pa.findViewById(R.id.bttLayout).setVisibility(View.VISIBLE);
                pa.findViewById(R.id.imgPreviewLayout).setVisibility(View.INVISIBLE);
                pa.findViewById(R.id.descripeProductLayout).setVisibility(View.INVISIBLE);
            }
        }
        else {
            ((TextView) pa.findViewById(R.id.errProduct)).setText("Upload product information fail! please try again");
        }
    }
}