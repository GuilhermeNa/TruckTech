package br.com.apps.trucktech.ui.fragments.nav_travel.freight.freight_preview

import android.content.Context
import android.view.View.GONE
import android.view.View.VISIBLE
import br.com.apps.trucktech.R
import br.com.apps.trucktech.databinding.FragmentFreightPreviewBinding
import br.com.apps.trucktech.expressions.getColorById
import br.com.apps.trucktech.util.state.StateDarkLayerI
import br.com.apps.trucktech.util.state.StatePreviewI

class FreightPreviewState(
    private val context: Context,
    private val binding: FragmentFreightPreviewBinding
) : StatePreviewI, StateDarkLayerI {

    override fun showWriteOptions() {}

    override fun hideWriteOptions() {
        hasMenuItems(false)
        hasFabs(false)
    }

    override fun hasDarkLayer(hasLayer: Boolean) {
        binding.fragFreightPreviewDarkLayer.visibility =
            when (hasLayer) {
                true -> VISIBLE
                false -> GONE
            }
    }

    fun showAppBarCollapsed(hasWriteAuth: Boolean) {
        binding.apply {
            collapsingToolbar.setCollapsedTitleTextColor(context.getColorById(R.color.black_layer))
            toolbar.navigationIcon?.setTint(context.getColorById(R.color.black_layer))
        }
        if (hasWriteAuth) {
            hasFabs(false)
            hasMenuItems(true)
        }
    }

    fun showAppBarExpanded(hasWriteAuth: Boolean) {
        binding.apply {
            collapsingToolbar.setExpandedTitleColor(context.getColorById(R.color.white))
            toolbar.navigationIcon?.setTint(context.getColorById(R.color.white))
        }
        if (hasWriteAuth) {
            hasFabs(true)
            hasMenuItems(false)
        }
    }

    private fun hasMenuItems(hasItem: Boolean) {
        binding.apply {
            toolbar.menu?.findItem(R.id.menu_preview_delete)?.isEnabled = hasItem
            toolbar.menu?.findItem(R.id.menu_preview_delete)?.isVisible = hasItem
            toolbar.menu?.findItem(R.id.menu_preview_edit)?.isEnabled = hasItem
            toolbar.menu?.findItem(R.id.menu_preview_edit)?.isVisible = hasItem
        }
    }

    private fun hasFabs(hasFabs: Boolean) {
        when (hasFabs) {
            true -> {
                binding.apply {
                    fabEdit.visibility = VISIBLE
                    fabDelete.visibility = VISIBLE
                }
            }

            false -> {
                binding.apply {
                    fabDelete.visibility = GONE
                    fabEdit.visibility = GONE
                }
            }
        }
    }

}