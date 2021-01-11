package com.harvest.core_base.preferences;

import android.content.Context;
import android.content.SharedPreferences;

import com.harvest.core_base.interfaces.IContext;
import com.harvest.core_base.service.ServiceFacade;

public class CommonPreferenceUtils {

    public static final String settingsSp = "settings";

    private static SharedPreferences getSharedPreference(String name) {
        return ServiceFacade.getInstance().get(IContext.class).getApplicationContext().getSharedPreferences(name, Context.MODE_PRIVATE);
    }
}
