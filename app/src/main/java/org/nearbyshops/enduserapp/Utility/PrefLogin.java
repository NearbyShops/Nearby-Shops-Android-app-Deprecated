package org.nearbyshops.enduserapp.Utility;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.content.ContextCompat;
import android.util.Base64;

import com.google.gson.Gson;

import org.nearbyshops.enduserapp.ModelRoles.EndUser;
import org.nearbyshops.enduserapp.ModelRoles.User;
import org.nearbyshops.enduserapp.MyApplication;
import org.nearbyshops.enduserapp.R;



import static android.content.Context.MODE_PRIVATE;

/**
 * Created by sumeet on 25/9/16.
 */

public class PrefLogin {


    public static final Integer ROLE_ADMIN = 1;
    public static final Integer ROLE_STAFF = 2;


    public static void saveCredentials(Context context, String username, String password)
    {
        // get a handle to shared Preference
        SharedPreferences sharedPref;

        sharedPref = context.getSharedPreferences(
                context.getString(R.string.preference_file_name),
                MODE_PRIVATE);

        // write to the shared preference
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("username", username);
        editor.putString("password",password);
        editor.apply();
    }



    public static void saveUsername(Context context, String username)
    {
        // get a handle to shared Preference
        SharedPreferences sharedPref;

        sharedPref = context.getSharedPreferences(
                context.getString(R.string.preference_file_name),
                MODE_PRIVATE);

        // write to the shared preference
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("username", username);
//        editor.putString("password",password);
        editor.apply();
    }


    public static void savePassword(Context context, String password)
    {
        // get a handle to shared Preference
        SharedPreferences sharedPref;

        sharedPref = context.getSharedPreferences(
                context.getString(R.string.preference_file_name),
                MODE_PRIVATE);

        // write to the shared preference
        SharedPreferences.Editor editor = sharedPref.edit();
//        editor.putString("username", username);
        editor.putString("password",password);
        editor.apply();
    }





    public static String getUsername(Context context)
    {
        context = MyApplication.getAppContext();

        SharedPreferences sharedPref = context.getSharedPreferences(context.getString(R.string.preference_file_name), MODE_PRIVATE);
        String service_url = sharedPref.getString("username", "");
        return service_url;
    }

    public static String getPassword(Context context)
    {
        context = MyApplication.getAppContext();

        SharedPreferences sharedPref = context.getSharedPreferences(context.getString(R.string.preference_file_name), MODE_PRIVATE);
        String service_url = sharedPref.getString("password", "");
        return service_url;
    }







    public static void setRoleID(Context context, int role_id)
    {
        // get a handle to shared Preference
        SharedPreferences sharedPref;

        sharedPref = context.getSharedPreferences(
                context.getString(R.string.preference_file_name),
                MODE_PRIVATE);

        // write to the shared preference
        SharedPreferences.Editor editor = sharedPref.edit();

        editor.putInt("role", role_id);

        editor.apply();
    }


    public static int getRoleID(Context context) {

        context = MyApplication.getAppContext();
        SharedPreferences sharedPref = context.getSharedPreferences(context.getString(R.string.preference_file_name), MODE_PRIVATE);
        int role_id = sharedPref.getInt("role", -1);
        return role_id;
    }



    public static String baseEncoding(String username,String password)
    {
        String credentials = username + ":" + password;
        // create Base64 encodet string
        String basic =
                "Basic " + Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);

        return basic;
    }



    public static String getAuthorizationHeaders(Context context)
    {
        return PrefLogin.baseEncoding(
                PrefLogin.getUsername(context),
                PrefLogin.getPassword(context));

    }



//    public static void saveEndUser(EndUser endUser, Context context)
//    {
//
//        if(context == null)
//        {
//            return;
//        }
//
//        //Creating a shared preference
//
//        SharedPreferences sharedPref = context.getSharedPreferences(context.getString(R.string.preference_file_name), MODE_PRIVATE);
//
//        SharedPreferences.Editor prefsEditor = sharedPref.edit();
//
//        if(endUser == null)
//        {
//            prefsEditor.putString("admin", "null");
//
//        }
//        else
//        {
//            Gson gson = new Gson();
//            String json = gson.toJson(endUser);
//            prefsEditor.putString("admin", json);
//        }
//
//        prefsEditor.apply();
//    }
//
//
//    public static EndUser getEndUser(Context context)
//    {
//        SharedPreferences sharedPref = context.getSharedPreferences(context.getString(R.string.preference_file_name), MODE_PRIVATE);
//
//        Gson gson = new Gson();
//        String json = sharedPref.getString("admin", "null");
//
//        if(json.equals("null"))
//        {
//
//            return null;
//
//        }else
//        {
//            return gson.fromJson(json, EndUser.class);
//        }
//
//    }
//







    public static void saveUserProfile(User user, Context context)
    {


        //Creating a shared preference

        SharedPreferences sharedPref = context.getSharedPreferences(context.getString(R.string.preference_file_name), MODE_PRIVATE);

        SharedPreferences.Editor prefsEditor = sharedPref.edit();

        Gson gson = UtilityFunctions.provideGson();
        String json = gson.toJson(user);
        prefsEditor.putString("user_profile", json);

        prefsEditor.apply();
    }


    public static User getUser(Context context)
    {
        SharedPreferences sharedPref = context.getSharedPreferences(context.getString(R.string.preference_file_name), MODE_PRIVATE);

        Gson gson = UtilityFunctions.provideGson();
        String json = sharedPref.getString("user_profile", null);

        return gson.fromJson(json, User.class);
    }




}
