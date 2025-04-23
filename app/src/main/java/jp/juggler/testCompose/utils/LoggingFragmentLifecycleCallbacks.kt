package jp.juggler.testCompose.utils

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import timber.log.Timber

object LoggingFragmentLifecycleCallbacks : FragmentManager.FragmentLifecycleCallbacks() {
    @Deprecated("Deprecated in Java")
    override fun onFragmentActivityCreated(
        fm: FragmentManager,
        f: Fragment,
        savedInstanceState: Bundle?
    ) = Timber.i("onFragmentActivityCreated $f, savedInstanceState=$savedInstanceState")

    override fun onFragmentPreCreated(
        fm: FragmentManager,
        f: Fragment,
        savedInstanceState: Bundle?
    ) = Timber.i("onFragmentPreCreated $f, savedInstanceState=$savedInstanceState")

    override fun onFragmentCreated(
        fm: FragmentManager,
        f: Fragment,
        savedInstanceState: Bundle?
    ) = Timber.i("onFragmentCreated $f, savedInstanceState=$savedInstanceState")

    override fun onFragmentDestroyed(fm: FragmentManager, f: Fragment) =
        Timber.i("onFragmentDestroyed $f")

    override fun onFragmentSaveInstanceState(
        fm: FragmentManager,
        f: Fragment,
        outState: Bundle
    ) = Timber.i("onFragmentSaveInstanceState $f, outState=$outState")

    override fun onFragmentPreAttached(
        fm: FragmentManager,
        f: Fragment,
        context: Context
    ) = Timber.i("onFragmentPreAttached $f, context=$context")

    override fun onFragmentAttached(fm: FragmentManager, f: Fragment, context: Context) =
        Timber.i("onFragmentAttached $f, context=$context")

    override fun onFragmentDetached(fm: FragmentManager, f: Fragment) =
        Timber.i("onFragmentDetached $f")

    override fun onFragmentViewCreated(
        fm: FragmentManager,
        f: Fragment,
        v: View,
        savedInstanceState: Bundle?
    ) = Timber.i("onFragmentViewCreated $f, savedInstanceState=$savedInstanceState")

    override fun onFragmentViewDestroyed(fm: FragmentManager, f: Fragment) =
        Timber.i("onFragmentViewDestroyed $f")

    override fun onFragmentStarted(fm: FragmentManager, f: Fragment) =
        Timber.i("onFragmentStarted $f")

    override fun onFragmentStopped(fm: FragmentManager, f: Fragment) =
        Timber.i("onFragmentStopped $f")

    override fun onFragmentResumed(fm: FragmentManager,f: Fragment) =
        Timber.i("onFragmentResumed $f")

    override fun onFragmentPaused(fm: FragmentManager, f: Fragment) =
        Timber.i("onFragmentPaused $f")
}
