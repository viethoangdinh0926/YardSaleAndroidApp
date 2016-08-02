package com.viet.yardsale.services;

import android.app.Activity;
import android.app.ProgressDialog;

/**
 * Created by Viet on 6/24/2015.
 */
public class StaticComponents {
    public static String currentUser = null;

    public static boolean alreadyCheckAppVersion = false;

    //Hanging a activity for background processing
    private static ProgressDialog progress;
    public static void freezeActivity(Activity activity){
        progress = ProgressDialog.show(activity, "", "Processing...", true);
    }
    public static void unfreezeActivity(Activity activity){
        progress.dismiss();
    }

    //hold the email of the user who just sign up, then mention it in the activity of confirmation
    public static String signup_or_forget_password_user_email;

    //keep tracking number of photos user has posted for the yard sale
    public static int uploadedImageCount = 0;

    //coordination of the current user
    public static double latitude;
    public static double longtitude;

    //The formatted string with all information of all yard sales in range that the current user are searching
    public static String returned_yard_sales_information;

    //the yard sale owner whom the current user is picking. And the number of photos that the owner has posted for the yard sale
    public static String owner_of_yard_sale;
    public static String number_of_yard_sale_photos;
    public static String looking_at_yard_sale_latitude;
    public static String looking_at_yard_sale_longtitude;

    //tell the app that user is changing email or password. This information will be used in the confirmation activity to notice the user.
    public static boolean password_false_email_true;

    //validate email address
    public final static boolean isValidEmail(String target) {
        if (target == null) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }

    //show or hide ads
    public static boolean showAds;

    //database address
    public static String dbAdress = "http://massbaybook.hostei.com/yardsale/";
}
