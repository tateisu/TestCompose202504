package jp.juggler.testCompose.bottomNavigation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import jp.juggler.testCompose.databinding.ScreenFragmentBinding
import jp.juggler.testCompose.utils.string

class ScreenFragment : Fragment() {
    companion object {
        const val ARG_NAME = "name"

        fun createScreenFragment(name: String) = ScreenFragment().apply {
            arguments = Bundle().apply {
                putString(ARG_NAME, name)
            }
        }
    }

    private val name by lazy {
        arguments?.string(ARG_NAME) ?: "??"
    }

    private var views: ScreenFragmentBinding? = null

    override fun onDestroyView() {
        super.onDestroyView()
        views = null
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = ScreenFragmentBinding.inflate(inflater, container, false)
        .also { views = it }.root

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        views?.apply {
            tvName.text = name
        }
    }

    override fun toString() = "${this.javaClass.simpleName} $name"
}