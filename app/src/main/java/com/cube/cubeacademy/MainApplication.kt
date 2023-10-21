package com.cube.cubeacademy

import android.app.Application
import com.cube.cubeacademy.lib.di.Repository
import com.cube.cubeacademy.lib.models.Nominee
import com.cube.cubeacademy.utils.ApiResult
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltAndroidApp
class MainApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        //Planting timber for logging, should only print logs in debug mode
        if (BuildConfig.DEBUG) Timber.plant(Timber.DebugTree())
    }

}