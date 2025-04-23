package jp.juggler.testCompose

import android.app.Application
import jp.juggler.testCompose.utils.LoggingActivityLifecycleCallbacks
import timber.log.Timber

class App1 : Application() {
    companion object {
        private var isRegisteredLoggingActivityLifecycleCallbacks = false
    }

    override fun onCreate() {
        super.onCreate()
        if (Timber.treeCount == 0) {
            Timber.plant(Timber.DebugTree())
        }
        if (!isRegisteredLoggingActivityLifecycleCallbacks) {
            isRegisteredLoggingActivityLifecycleCallbacks = true
            registerActivityLifecycleCallbacks(LoggingActivityLifecycleCallbacks)
            Timber.i("registerActivityLifecycleCallbacks")
        }
    }
}
