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
import br.com.apps.trucktech.util.state.StateDeleteI
import br.com.apps.trucktech.util.state.StateI
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class RequestsListState(
    private val binding: FragmentRequestsListBinding,
    private val lifecycleScope: LifecycleCoroutineScope
) : StateI, StateDeleteI {

    override fun showLoading() {
        binding.apply {
            boxGif.loadingGif.loadGif(R.drawable.gif_request, binding.root.context)
            fragRequestsBoxError.layout.visibility = GONE
            fragmentRequestsListToolbar.toolbar.visibility = GONE
            fragmentRequestsListHeaderRecycler.visibility = GONE
            fragmentRequestsListRecycler.visibility = GONE
            fragmentRequestsListFab.visibility = GONE
        }
    }

    override fun showLoaded() {
        binding.fragRequestsBoxError.apply {
            if (layout.visibility == VISIBLE) {
                layout.visibility = GONE
                empty.visibility = GONE
                error.visibility = GONE
            }
        }

        lifecycleScope.launch {
            delay(250)

            binding.fragmentRequestsListHeaderRecycler.apply {
                if (visibility == GONE) {
                    visibility = VISIBLE
                    animation =
                        AnimationUtils.loadAnimation(binding.root.context, R.anim.slide_in_from_top)
                }
            }

            delay(250)

            binding.fragmentRequestsListRecycler.apply {
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

    override fun showEmpty() {
        binding.apply {
            if (fragmentRequestsListHeaderRecycler.visibility == VISIBLE) {
                fragmentRequestsListHeaderRecycler.visibility = GONE
            }

            lifecycleScope.launch {
                fragRequestsBoxError.apply {
                    if (layout.visibility == GONE) {
                        delay(250)
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
    }

    override fun showUpdating() {}

    override fun showError(e: Exception) {
        binding.apply {
            if (fragmentRequestsListHeaderRecycler.visibility == VISIBLE) {
                fragmentRequestsListHeaderRecycler.visibility = GONE
            }

            lifecycleScope.launch {
                fragRequestsBoxError.apply {
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
        binding.boxLoading.apply {
            layout.visibility = GONE
        }
    }

    fun showAfterLoading() {
        binding.apply {
            boxGif.layout.apply {
                if (visibility == VISIBLE) {
                    visibility = GONE
                    animation =
                        AnimationUtils.loadAnimation(
                            binding.root.context,
                            R.anim.fade_out_and_shrink
                        )
                }
            }

            lifecycleScope.launch {
                delay(250)

                fragmentRequestsListFab.apply {
                    if (visibility == GONE) {
                        visibility = VISIBLE
                        animation =
                            AnimationUtils.loadAnimation(
                                binding.root.context,
                                R.anim.slide_in_from_bottom
                            )
                    }
                }

                fragmentRequestsListToolbar.toolbar.apply {
                    if (visibility == GONE) {
                        visibility = VISIBLE
                        animation = AnimationUtils.loadAnimation(
                            binding.root.context,
                            R.anim.slide_in_from_top
                        )
                    }
                }

            }

        }
    }

}