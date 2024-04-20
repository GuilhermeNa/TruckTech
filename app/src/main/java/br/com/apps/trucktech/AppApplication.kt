package br.com.apps.trucktech

import android.app.Application
import br.com.apps.trucktech.di.appModules
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class AppApplication: Application() {

    override fun onCreate() {
        super.onCreate()
        initKoin()
    }

    private fun initKoin() {
        startKoin {
            androidContext(this@AppApplication)
            modules(appModules)
        }
    }

}