package jp.juggler.testCompose.utils

import androidx.fragment.app.FragmentContainerView
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction

val FragmentContainerView.fragmentManager
    get() = FragmentManager.findFragmentManager(this)

fun FragmentContainerView.inTransaction(func: FragmentTransaction.(id: Int) -> Unit) {
    val id = this.id
    fragmentManager.beginTransaction().apply { func(id) }.commit()
}
