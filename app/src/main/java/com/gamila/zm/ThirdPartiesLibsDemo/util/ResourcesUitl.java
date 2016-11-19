package com.gamila.zm.ThirdPartiesLibsDemo.util;

import android.content.Context;

/**
 * Created by zeinab on 4/19/2016.
 */
public class ResourcesUitl {

    private static ResourcesUitl mResourcesUitl;
    private Context context;

    private void ResourcesUitl() {

    }

    public static ResourcesUitl getInstance() {

        if (mResourcesUitl == null) {

            mResourcesUitl = new ResourcesUitl();

        }

        return mResourcesUitl;
    }

    public void init(Context context) {
        this.context = context;
    }


    public String getString(int id  ) {

        return context.getString(id);
    }

    public String getString(int id ,Object... formatArgs ) {

        return context.getString(id,formatArgs);
    }
}
