package br.com.apps.trucktech.ui.fragments.nav_requests.requests_list

import android.util.Log
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.animation.AnimationUtils
import androidx.lifecycle.LifecycleCoroutineScope
import br.com.apps.repository.util.TAG_DEBUG
import br.com.apps.trucktech.R
import br.com.apps.trucktech.databinding.FragmentRequestsListBinding
import br.com.apps.trucktech.expressions.loadGif
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class RequestsListState(
    private val binding: FragmentRequestsListBinding,
    private val scope: LifecycleCoroutineScope
) {

    fun showLoading() {
        binding.apply {
            fragRlBoxGif.loadingGif.loadGif(R.drawable.gif_request, binding.root.context)
            fragRlBoxError.layout.visibility = GONE
            fragRlToolbar.toolbar.visibility = GONE
            fragRlRecyclerHeader.visibility = GONE
            fragRlRecyclerBoddy.visibility = GONE
            fragRlFab.visibility = GONE
        }
    }

    fun showLoaded() = scope.launch {
        binding.run {

            fragRlBoxError.run {
                if (layout.visibility == VISIBLE) {
                    layout.visibility = GONE
                    empty.visibility = GONE
                    error.visibility = GONE
                }
            }

            delay(250)

            fragRlRecyclerBoddy.run {
                if (visibility == GONE) {
                    val layoutAnim = AnimationUtils.loadLayoutAnimation(
                        binding.root.context,
                        R.anim.layout_controller_animation_slide_in_left
                    )
                    visibility = VISIBLE
                    layoutAnimation = layoutAnim
                }
            }

        }
    }

    fun showEmpty() = scope.launch {
        binding.apply {

            fragRlRecyclerBoddy.run {
                if(visibility == VISIBLE) {
                    visibility = GONE
                }
            }

            delay(250)

            fragRlBoxError.run {
                if (layout.visibility == GONE) {

                    empty.visibility = VISIBLE
                    error.visibility = GONE
                    layout.apply {
                        visibility = VISIBLE
                        animation = AnimationUtils.loadAnimation(
                            binding.root.context,
                            R.anim.fade_in_and_shrink
                        )
                    }
                } else {
                    empty.visibility = VISIBLE
                    error.visibility = GONE
                }
            }
        }
    }

    fun showError(e: Exception) = scope.launch {
        binding.fragRlBoxError.run {
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
        Log.e(TAG_DEBUG, e.message.toString())
        e.printStackTrace()
    }

    fun showDeleting() {
        binding.fragRlBoxLoading.run {
            layout.visibility = VISIBLE
        }
    }

    fun showDeleted() {
        binding.fragRlBoxLoading.apply {
            layout.visibility = GONE
        }
    }

    fun showAfterLoading() = scope.launch {
        binding.run {

            fragRlBoxGif.layout.run {
                if (visibility == VISIBLE) {
                    visibility = GONE
                    animation =
                        AnimationUtils.loadAnimation(
                            binding.root.context,
                            R.anim.fade_out_and_shrink
                        )
                }
            }

            delay(450)

            fragRlToolbar.toolbar.apply {
                if (visibility == GONE) {
                    visibility = VISIBLE
                    animation = AnimationUtils.loadAnimation(
                        binding.root.context,
                        R.anim.slide_in_from_top
                    )
                }
            }

            delay(100)

            fragRlRecyclerHeader.run {
                if (visibility == GONE) {
                    visibility = VISIBLE
                    animation =
                        AnimationUtils.loadAnimation(
                            binding.root.context,
                            R.anim.slide_in_from_top
                        )
                }
            }

            fragRlFab.apply {
                if (visibility == GONE) {
                    visibility = VISIBLE
                    animation =
                        AnimationUtils.loadAnimation(
                            binding.root.context,
                            R.anim.slide_in_from_bottom
                        )
                }
            }

        }
    }

}