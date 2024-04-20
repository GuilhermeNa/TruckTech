package br.com.apps.trucktech.ui.fragments.base_fragments

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
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
        loadData().run { bind() }
        initMenuClickListener()
    }

    abstract fun configureBaseFragment(configurator: BasePreviewConfigurator)

    abstract fun loadData()

    abstract fun bind()

    private fun initMenuClickListener() {
        toolbar?.apply {

            setNavigationOnClickListener {
                toolbar!!.menu.clear()
                it.popBackStack()
            }

            setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.menu_preview_edit -> {
                        toolbar!!.menu.clear()
                        onEditCLickDirection()
                        true
                    }

                    R.id.menu_preview_delete -> {
                        toolbar!!.menu.clear()
                        onDeleteClick()
                        true
                    }

                else -> false
            }
        }

    }
}

    abstract fun onDeleteClick()

    abstract fun onEditCLickDirection()

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

