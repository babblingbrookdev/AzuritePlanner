package com.babblingbrookdev.azuriteplanner

import android.app.Application
import com.babblingbrookdev.azuriteplanner.di.AppComponent
import com.babblingbrookdev.azuriteplanner.di.DaggerAppComponent
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import javax.inject.Inject

class AzuritePlannerApp : Application(), HasAndroidInjector {

    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Any>

    lateinit var appComponent: AppComponent

    private lateinit var firebaseAnalytics: FirebaseAnalytics

    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerAppComponent.builder()
            .application(this)
            .build()
        appComponent.inject(this)

        firebaseAnalytics = Firebase.analytics
    }

    override fun androidInjector(): AndroidInjector<Any> = dispatchingAndroidInjector
}