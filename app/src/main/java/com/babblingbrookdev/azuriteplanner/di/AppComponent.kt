package com.babblingbrookdev.azuriteplanner.di

import com.babblingbrookdev.azuriteplanner.AzuritePlannerApp
import com.babblingbrookdev.azuriteplanner.data.DataModule
import com.babblingbrookdev.azuriteplanner.ui.UiModule
import dagger.BindsInstance
import dagger.Component
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Component(
    modules = [
        AndroidSupportInjectionModule::class,
        DataModule::class,
        UiModule::class
    ]
)
@Singleton
interface AppComponent {
    fun inject(app: AzuritePlannerApp)

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(app: AzuritePlannerApp): Builder

        fun build(): AppComponent
    }
}