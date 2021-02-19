package com.babblingbrookdev.azuriteplanner.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.babblingbrookdev.azuriteplanner.data.Repository
import com.babblingbrookdev.azuriteplanner.di.ViewModelKey
import com.babblingbrookdev.azuriteplanner.ui.chart.ChartFragment
import com.babblingbrookdev.azuriteplanner.ui.chart.ChartViewModel
import com.babblingbrookdev.azuriteplanner.ui.entry.EntryFragment
import com.babblingbrookdev.azuriteplanner.ui.entry.EntryViewModel
import com.babblingbrookdev.azuriteplanner.ui.list.ListFragment
import com.babblingbrookdev.azuriteplanner.ui.list.ListViewModel
import dagger.Module
import dagger.Provides
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap
import javax.inject.Provider

@Module(
    includes = [UiModule.ProvideViewModel::class,
        UiModule.ProvideViewModelFactory::class]
)
abstract class UiModule {

    @ContributesAndroidInjector(modules = [InjectIntoFragment::class])
    abstract fun bindChartFragment(): ChartFragment

    @ContributesAndroidInjector(modules = [InjectIntoFragment::class])
    abstract fun bindEntryFragment(): EntryFragment

    @ContributesAndroidInjector(modules = [InjectIntoFragment::class])
    abstract fun bindListFragment(): ListFragment

    @Module
    object ProvideViewModelFactory {
        @JvmStatic
        @Provides
        fun provideViewModelFactory(
            providers: Map<Class<out ViewModel>, @JvmSuppressWildcards Provider<ViewModel>>
        ): ViewModelProvider.Factory =
            ViewModelFactory(providers)
    }

    @Module
    object ProvideViewModel {

        @JvmStatic
        @Provides
        @IntoMap
        @ViewModelKey(ChartViewModel::class)
        fun provideChartViewModel(repository: Repository): ViewModel =
            ChartViewModel(repository)

        @JvmStatic
        @Provides
        @IntoMap
        @ViewModelKey(EntryViewModel::class)
        fun provideEntryViewModel(repository: Repository): ViewModel =
            EntryViewModel(repository)

        @JvmStatic
        @Provides
        @IntoMap
        @ViewModelKey(ListViewModel::class)
        fun provideListViewModel(repository: Repository): ViewModel =
            ListViewModel(repository)
    }

    @Module
    object InjectIntoFragment {

        @JvmStatic
        @Provides
        fun provideChartViewModel(
            factory: ViewModelProvider.Factory,
            target: ChartFragment
        ): ChartViewModel =
            ViewModelProvider(target, factory).get(ChartViewModel::class.java)

        @JvmStatic
        @Provides
        fun provideEntryViewModel(
            factory: ViewModelProvider.Factory,
            target: EntryFragment
        ): EntryViewModel =
            ViewModelProvider(target, factory).get(EntryViewModel::class.java)

        @JvmStatic
        @Provides
        fun provideListViewModel(
            factory: ViewModelProvider.Factory,
            target: ListFragment
        ): ListViewModel =
            ViewModelProvider(target, factory).get(ListViewModel::class.java)
    }
}