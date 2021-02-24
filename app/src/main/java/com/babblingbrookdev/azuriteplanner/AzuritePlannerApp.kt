package com.babblingbrookdev.azuriteplanner

import android.app.Application
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase

import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class AzuritePlannerApp : Application() {

    private lateinit var firebaseAnalytics: FirebaseAnalytics

    override fun onCreate() {
        super.onCreate()
        firebaseAnalytics = Firebase.analytics
    }
}