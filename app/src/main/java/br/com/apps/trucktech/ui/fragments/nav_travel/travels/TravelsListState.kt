package br.com.apps.trucktech.ui.fragments.nav_travel.travels

import android.util.Log
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.animation.AnimationUtils
import androidx.lifecycle.LifecycleCoroutineScope
import br.com.apps.repository.util.TAG_DEBUG
import br.com.apps.trucktech.R
import br.com.apps.trucktech.databinding.FragmentTravelsBinding
import br.com.apps.trucktech.expressions.loadGif
import br.com.apps.trucktech.util.state.StateDeleteI
import br.com.apps.trucktech.util.state.StateI
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class TravelsListState(
    private val binding: FragmentTravelsBinding,
    private val lifecycleScope: LifecycleCoroutineScope
) : StateI, StateDeleteI {

    override fun showLoading() {
        binding.apply {
            boxGif.loadingGif.loadGif(R.drawable.gif_travel, binding.root.context)
            fragTravelsBoxError.layout.visibility = GONE
            travelsFragmentRecycler.visibility = GONE
            fragTravelFab.visibility = GONE
            fragmentTravelsToolbar.toolbar.visibility = GONE
        }
    }

    override fun showLoaded() {
        binding.travelsFragmentRecycler.apply {
            if (visibility == GONE) {
                val layoutAnim = AnimationUtils.loadLayoutAnimation(
                    binding.root.context,
                    R.anim.layout_controller_animation_slide_in_left
                )
                lifecycleScope.launch {
                    delay(500)
                    visibility = VISIBLE
                    layoutAnimation = layoutAnim
                }
            }
        }

        binding.fragTravelsBoxError.apply {
            if (layout.visibility == VISIBLE) {
                layout.visibility = GONE
                error.visibility = GONE
                empty.visibility = GONE
            }
        }

    }

    override fun showEmpty() {
        binding.travelsFragmentRecycler.visibility = GONE

        lifecycleScope.launch {
            binding.fragTravelsBoxError.apply {
                if (layout.visibility == GONE) {
                    delay(250)
                    error.visibility = GONE
                    empty.visibility = VISIBLE
                    layout.apply {
                        visibility = VISIBLE
                        animation = AnimationUtils.loadAnimation(
                            binding.root.context,
                            R.anim.fade_in_and_shrink
                        )
                    }
                } else {
                    error.visibility = GONE
                    empty.visibility = VISIBLE
                }
            }
        }
    }

    override fun showUpdating() {}

    override fun showError(e: Exception) {
        binding.travelsFragmentRecycler.visibility = GONE

        lifecycleScope.launch {
            binding.fragTravelsBoxError.apply {
                if (layout.visibility == GONE) {
                    delay(250)
                    error.visibility = VISIBLE
                    empty.visibility = GONE
                    layout.apply {
                        visibility = VISIBLE
                        animation = AnimationUtils.loadAnimation(
                            binding.root.context,
                            R.anim.fade_in_and_shrink
                        )
                    }
                } else {
                    error.visibility = VISIBLE
                    empty.visibility = GONE
                }
            }
        }
        Log.e(TAG_DEBUG, e.message.toString())
        e.printStackTrace()
    }

    override fun showDeleting() {
        binding.boxLoading.apply {
            layout.visibility = VISIBLE
        }
    }

    override fun showDeleted() {
        binding.boxGif.layout.visibility = GONE
        binding.boxLoading.apply {
            layout.visibility = GONE
        }
    }

}