package com.paycraft.ticketbooker.utils;

import android.content.Context;

import java.io.IOException;
import java.io.InputStream;

import ir.mirrajabi.okhttpjsonmock.providers.InputStreamProvider;

public class AssetInputStreamProvider implements InputStreamProvider {


    private final Context mContext;

    public AssetInputStreamProvider(Context context){
        this.mContext = context;
    }

    @Override
    public InputStream provide(String path) {
        try {
            return mContext.getAssets().open(path);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
