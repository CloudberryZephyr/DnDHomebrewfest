package com.example.dndhomebrewfest

import android.app.Application

// Step 6: Override the app's onCreate() function
class HomebreweryApp : Application(){

    lateinit var container : AppContainer
    companion object {
        private var appInstance: HomebreweryApp? = null

        fun getApp(): HomebreweryApp {
            if (appInstance == null) {
                throw Exception("app is null!")
            }
            return appInstance!!
        }
    }

    override fun onCreate() {
        appInstance = this
        container = DefaultAppContainer(context = this)
        super.onCreate()
    }

}