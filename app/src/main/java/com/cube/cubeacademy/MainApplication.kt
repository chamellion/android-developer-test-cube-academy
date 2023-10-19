package com.cube.cubeacademy

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class MainApplication : Application(){

    override fun onCreate() {
        super.onCreate()
        //Planting timber for logging, should only print logs in debug mode
        if (BuildConfig.DEBUG) Timber.plant(Timber.DebugTree())
    }
}