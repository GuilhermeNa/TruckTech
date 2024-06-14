package br.com.apps.trucktech.ui.fragments.base_fragments

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import br.com.apps.trucktech.R
import br.com.apps.trucktech.expressions.getColorById
import br.com.apps.trucktech.expressions.popBackStack
import br.com.apps.trucktech.ui.activities.main.VisualComponents

abstract class BaseFragmentWithToolbar : BaseFragmentForMainAct() {

    private var toolbar: Toolbar? = null

    private val configurator = object : BaseFragmentConfigurator {
        override fun toolbar(
            hasToolbar: Boolean,
            toolbar: Toolbar?,
            hasNavigation: Boolean,
            menuId: Int?,
            toolbarTextView: TextView?,
            title: String?
        ) {
            if (hasToolbar) {
                toolbar?.run {
                    menuId?.let {
                        inflateMenu(it)
                    }
                    toolbarTextView?.let {
                        it.text = title ?: "Undefined"
                    }
                    this@BaseFragmentWithToolbar.toolbar = this
                    if (hasNavigation) {
                        setNavigationIcon(R.drawable.icon_back)
                        navigationIcon?.setTint(context.getColorById(R.color.black_layer))
                    }
                }
            }
        }

        override fun bottomNavigation(hasBottomNavigation: Boolean) {
            mainActVM.setComponents(VisualComponents(hasBottomNavigation = hasBottomNavigation))
        }
    }

    //---------------------------------------------------------------------------------------------//
    // ON VIEW CREATED
    //---------------------------------------------------------------------------------------------//

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        configureBaseFragment(configurator)
        toolbar?.let { initMenuClickListeners(it) }
    }

    abstract fun configureBaseFragment(configurator: BaseFragmentConfigurator)

    open fun initMenuClickListeners(toolbar: Toolbar) {
        toolbar.setNavigationOnClickListener { view ->
            view.popBackStack()
        }
    }

    //---------------------------------------------------------------------------------------------//
    // ON DESTROY VIEW
    //---------------------------------------------------------------------------------------------//

    override fun onDestroyView() {
        super.onDestroyView()
        toolbar = null
    }

    //---------------------------------------------------------------------------------------------//
    // INTERFACE
    //---------------------------------------------------------------------------------------------//

    interface BaseFragmentConfigurator {

        fun toolbar(
            hasToolbar: Boolean,
            toolbar: Toolbar? = null,
            hasNavigation: Boolean = false,
            menuId: Int? = null,
            toolbarTextView: TextView? = null,
            title: String? = null
        )

        fun bottomNavigation(hasBottomNavigation: Boolean)

    }

}


