package com.example.authentication.model

import android.content.Context
import com.example.authentication.androidWrapper.DeviceInfo

class ModelMainActivity(private val context: Context) {

    fun getAndroidId() = DeviceInfo.getAndroidId(context)

}