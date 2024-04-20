package br.com.apps.trucktech.ui.fragments.base_fragments

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.appcompat.widget.Toolbar
import br.com.apps.trucktech.R
import br.com.apps.trucktech.expressions.getColorById
import br.com.apps.trucktech.expressions.loadImageThroughUrl
import br.com.apps.trucktech.expressions.popBackStack
import com.google.android.material.appbar.CollapsingToolbarLayout

abstract class BasePreviewFragment : BaseFragmentForMainAct() {

    private var toolbar: Toolbar? = null

    private val configurator = object : BasePreviewConfigurator {
        override fun collapsingToolbar(
            collapsingToolbar: CollapsingToolbarLayout,
            toolbar: Toolbar,
            backgroundImage: ImageView,
            urlImage: String,
            title: String,
            titleExpandedColor: Int,
            titleCollapsedColor: Int
        ) {
            toolbar.inflateMenu(R.menu.menu_preview)
            this@BasePreviewFragment.toolbar = toolbar
            collapsingToolbar.title = title
            collapsingToolbar.setCollapsedTitleTextColor(
                requireContext().getColorById(
                    titleCollapsedColor
                )
            )
            collapsingToolbar.setExpandedTitleColor(requireContext().getColorById(titleExpandedColor))
            backgroundImage.loadImageThroughUrl(urlImage, requireContext())
        }
    }

    //---------------------------------------------------------------------------------------------//
    // ON VIEW CREATED
    //---------------------------------------------------------------------------------------------//

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        configureBaseFragment(configurator)
        initMenuClickListener()
    }

    abstract fun configureBaseFragment(configurator: BasePreviewConfigurator)

    private fun initMenuClickListener() {
        toolbar?.apply {

            setNavigationOnClickListener {
                toolbar!!.menu.clear()
                it.popBackStack()
            }

            setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.menu_preview_edit -> {
                        clearMenu()
                        onEditMenuCLick()
                        true
                    }

                    R.id.menu_preview_delete -> {
                        onDeleteMenuClick()
                        true
                    }

                else -> false
            }
        }

    }
}

    fun clearMenu() {
        toolbar!!.menu.clear()
    }

    abstract fun onDeleteMenuClick()

    abstract fun onEditMenuCLick()

//---------------------------------------------------------------------------------------------//
// ON VIEW CREATED
//---------------------------------------------------------------------------------------------//

override fun onDestroyView() {
    toolbar = null
    super.onDestroyView()
}

//---------------------------------------------------------------------------------------------//
// INTERFACE
//---------------------------------------------------------------------------------------------//

interface BasePreviewConfigurator {
    fun collapsingToolbar(
        collapsingToolbar: CollapsingToolbarLayout,
        toolbar: Toolbar,
        backgroundImage: ImageView,
        urlImage: String,
        title: String,
        titleExpandedColor: Int,
        titleCollapsedColor: Int
    )
}

}

