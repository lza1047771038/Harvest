package com.harvest.core_base.interfaces;

import android.app.Application;
import android.content.Context;

public interface IContext {
    Context getContext();

    Context getApplicationContext();

    Application getApplication();
}
