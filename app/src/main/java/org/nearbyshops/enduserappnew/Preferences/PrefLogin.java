package org.nearbyshops.enduserappnew.Preferences;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Base64;

import com.google.gson.Gson;

import org.nearbyshops.enduserappnew.ModelRoles.User;
import org.nearbyshops.enduserappnew.MyApplication;
import org.nearbyshops.enduserappnew.R;



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
        return sharedPref.getString("username", "");
    }

    public static String getPassword(Context context)
    {
        context = MyApplication.getAppContext();

        SharedPreferences sharedPref = context.getSharedPreferences(context.getString(R.string.preference_file_name), MODE_PRIVATE);
        return sharedPref.getString("password", "");
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





}
