package jp.juggler.testCompose.utils

import android.app.Activity
import android.app.Application
import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import timber.log.Timber

object LoggingActivityLifecycleCallbacks : Application.ActivityLifecycleCallbacks {

    override fun onActivityPreCreated(
        activity: Activity,
        savedInstanceState: Bundle?
    ) = Timber.i("onActivityPreCreated $activity savedInstanceState=$savedInstanceState")

    override fun onActivityCreated(
        activity: Activity,
        savedInstanceState: Bundle?
    ) {
        Timber.i("onActivityCreated $activity savedInstanceState=$savedInstanceState")
        if (activity is FragmentActivity) {
            activity.supportFragmentManager.registerFragmentLifecycleCallbacks(
                LoggingFragmentLifecycleCallbacks,
                true
            )
        }
    }

    override fun onActivityPostCreated(
        activity: Activity,
        savedInstanceState: Bundle?
    ) = Timber.i("onActivityPostCreated $activity savedInstanceState=$savedInstanceState")

    override fun onActivityPreDestroyed(activity: Activity) =
        Timber.i("onActivityPreDestroyed $activity")

    override fun onActivityDestroyed(activity: Activity) =
        Timber.i("onActivityDestroyed $activity")

    override fun onActivityPostDestroyed(activity: Activity) =
        Timber.i("onActivityPostDestroyed $activity")

    override fun onActivityPreSaveInstanceState(
        activity: Activity,
        outState: Bundle
    ) = Timber.i("onActivityPreSaveInstanceState $activity")

    override fun onActivitySaveInstanceState(
        activity: Activity,
        outState: Bundle
    ) = Timber.i("onActivitySaveInstanceState $activity")

    override fun onActivityPostSaveInstanceState(
        activity: Activity,
        outState: Bundle
    ) = Timber.i("onActivityPostSaveInstanceState $activity")

    override fun onActivityPreStarted(activity: Activity) =
        Timber.i("onActivityPreStarted $activity")

    override fun onActivityStarted(activity: Activity) =
        Timber.i("onActivityStarted $activity")

    override fun onActivityPostStarted(activity: Activity) =
        Timber.i("onActivityPostStarted $activity")

    override fun onActivityPreStopped(activity: Activity) =
        Timber.i("onActivityPreStopped $activity")

    override fun onActivityStopped(activity: Activity) =
        Timber.i("onActivityStopped $activity")

    override fun onActivityPostStopped(activity: Activity) =
        Timber.i("onActivityPostStopped $activity")

    override fun onActivityPreResumed(activity: Activity) =
        Timber.i("onActivityPreResumed $activity")

    override fun onActivityResumed(activity: Activity) =
        Timber.i("onActivityResumed $activity")

    override fun onActivityPostResumed(activity: Activity) =
        Timber.i("onActivityPostResumed $activity")

    override fun onActivityPrePaused(activity: Activity) =
        Timber.i("onActivityPrePaused $activity")

    override fun onActivityPaused(activity: Activity) =
        Timber.i("onActivityPaused $activity")

    override fun onActivityPostPaused(activity: Activity) =
        Timber.i("onActivityPostPaused $activity")

}