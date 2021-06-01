package com.dbp.theater

import android.app.Application

class GlobalApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        prefs = MySharedPreference(applicationContext)
    }

    companion object {
        lateinit var prefs: MySharedPreference
    }
}