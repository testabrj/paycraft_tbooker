package com.paycraft.ticketbooker.utils

import android.content.Context

import java.io.IOException
import java.io.InputStream

import ir.mirrajabi.okhttpjsonmock.providers.InputStreamProvider

class AssetInputStreamProvider(private val mContext: Context) : InputStreamProvider {

    override fun provide(path: String): InputStream? {
        try {
            return mContext.assets.open(path)
        } catch (e: IOException) {
            e.printStackTrace()
        }

        return null
    }
}
