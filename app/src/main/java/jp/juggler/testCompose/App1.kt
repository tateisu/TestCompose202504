package jp.juggler.testCompose

import android.app.Application
import timber.log.Timber

class App1 : Application() {
    override fun onCreate() {
        super.onCreate()
        if( Timber.treeCount == 0 ) {
            Timber.plant(Timber.DebugTree())
        }
    }
}
