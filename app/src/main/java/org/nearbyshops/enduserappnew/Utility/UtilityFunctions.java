package org.nearbyshops.enduserappnew.Utility;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Created by sumeet on 10/7/17.
 */

public class UtilityFunctions {


    /* Utility Functions */

    public static Gson provideGson() {

        GsonBuilder gsonBuilder = new GsonBuilder();
        //gsonBuilder.setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES);
        return gsonBuilder
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
                .create();

//        .setDateFormat("yyyy-MM-dd hh:mm:ss.S")
    }

}
