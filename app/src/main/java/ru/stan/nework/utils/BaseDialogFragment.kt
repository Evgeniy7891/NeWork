package ru.stan.nework.utils

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.viewbinding.ViewBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import ru.stan.nework.R

abstract class BaseDialogFragment<V : ViewBinding> : BottomSheetDialogFragment() {

    private var _binding: V? = null
    protected val binding: V get() = _binding ?: throw IllegalStateException("Cannot access view")
    abstract fun viewBindingInflate(): V

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NORMAL, R.style.BottomSheetDialog)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = viewBindingInflate()
        return binding.root
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return super.onCreateDialog(savedInstanceState).also { dialog ->
            dialog.setOnShowListener {
                BottomSheetBehavior.from<View>(dialog.findViewById(com.google.android.material.R.id.design_bottom_sheet))
                    .apply {
                        state = BottomSheetBehavior.STATE_EXPANDED
                        isHideable = true
                        skipCollapsed = true
                    }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}