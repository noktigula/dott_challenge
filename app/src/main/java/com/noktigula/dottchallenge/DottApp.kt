package com.noktigula.dottchallenge

import android.app.Application
import com.facebook.stetho.Stetho

class DottApp : Application() {
    override fun onCreate() {
        super.onCreate()
        Stetho.initializeWithDefaults(this)
    }
}