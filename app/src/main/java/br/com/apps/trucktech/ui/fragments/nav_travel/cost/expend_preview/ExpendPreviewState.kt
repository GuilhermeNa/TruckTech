package br.com.apps.trucktech.ui.fragments.nav_travel.cost.expend_preview

import android.content.Context
import android.view.View
import br.com.apps.trucktech.R
import br.com.apps.trucktech.databinding.FragmentExpendPreviewBinding
import br.com.apps.trucktech.expressions.getColorById
import br.com.apps.trucktech.util.state.StateDarkLayerI
import br.com.apps.trucktech.util.state.StatePreviewI

class ExpendPreviewState(
    private val binding: FragmentExpendPreviewBinding,
    private val context: Context
) : StatePreviewI, StateDarkLayerI {

    override fun showWriteOptions() {
        binding.apply {
            toolbar.apply {
                menu.findItem(R.id.menu_preview_delete).isEnabled = true
                menu.findItem(R.id.menu_preview_delete).isVisible = true
                menu.findItem(R.id.menu_preview_edit).isEnabled = true
                menu.findItem(R.id.menu_preview_edit).isVisible = true
            }
        }
    }

    override fun hideWriteOptions() {
        binding.apply {
            toolbar.apply {
                menu.findItem(R.id.menu_preview_delete).isEnabled = false
                menu.findItem(R.id.menu_preview_delete).isVisible = false
                menu.findItem(R.id.menu_preview_edit).isEnabled = false
                menu.findItem(R.id.menu_preview_edit).isVisible = false
            }
        }
    }

    override fun hasDarkLayer(hasLayer: Boolean) {
        binding.fragExpendPreviewDarkLayer.visibility =
            when (hasLayer) {
                true -> View.VISIBLE
                false -> View.GONE
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
                    fabEdit.visibility = View.VISIBLE
                    fabDelete.visibility = View.VISIBLE
                }
            }

            false -> {
                binding.apply {
                    fabDelete.visibility = View.GONE
                    fabEdit.visibility = View.GONE
                }
            }
        }
    }

}