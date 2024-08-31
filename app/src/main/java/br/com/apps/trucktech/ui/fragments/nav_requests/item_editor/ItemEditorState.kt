package br.com.apps.trucktech.ui.fragments.nav_requests.item_editor

import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.animation.AnimationUtils
import androidx.lifecycle.LifecycleCoroutineScope
import br.com.apps.trucktech.R
import br.com.apps.trucktech.databinding.FragmentItemEditorBinding
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class ItemEditorState(
    private val binding: FragmentItemEditorBinding,
    private val lifecycle: LifecycleCoroutineScope
) {

    fun showLoading() {
        binding.run {
            fragIeLayoutDescription.visibility = GONE
            fragIeLayoutValue.visibility = GONE
            fragIeFab.visibility = GONE
            fragIeLayoutImg.visibility = GONE
            fragIeBadgeMenu.visibility = GONE
        }
    }

    fun showLoaded() {
        binding.run {
            lifecycle.launch {
                delay(100)
                fragIeLayoutDescription.run {
                    visibility = VISIBLE
                    animation = AnimationUtils.loadAnimation(
                        binding.root.context,
                        R.anim.slide_in_from_left
                    )
                }

                delay(100)
                fragIeLayoutValue.run {
                    visibility = VISIBLE
                    animation = AnimationUtils.loadAnimation(
                        binding.root.context,
                        R.anim.slide_in_from_left
                    )
                }

                delay(250)
                fragIeFab.run {
                    visibility = VISIBLE
                    animation = AnimationUtils.loadAnimation(
                        binding.root.context,
                        R.anim.slide_in_from_bottom
                    )
                }

            }
        }
    }

    fun showImage() {
        fun showImageView() {
            binding.fragIeLayoutImg.run {
                visibility = VISIBLE
                animation = AnimationUtils.loadAnimation(
                    binding.root.context,
                    R.anim.slide_in_from_left
                )
            }
        }

        fun showBadgeLayout() {
            binding.fragIeBadgeMenu.run {
                visibility = VISIBLE
                animation = AnimationUtils.loadAnimation(
                    binding.root.context,
                    R.anim.slide_in_from_right
                )
            }
        }

        if (binding.fragIeLayoutImg.visibility != VISIBLE) {
            lifecycle.launch {
                delay(450)
                showImageView()
                delay(200)
                showBadgeLayout()
            }
        }
    }

    fun hideImage() {
        binding.run {
            if (fragIeLayoutImg.visibility != GONE) {
                lifecycle.launch {
                    delay(200)
                    fragIeBadgeMenu.run {
                        visibility = GONE
                        animation = AnimationUtils.loadAnimation(
                            binding.root.context,
                            R.anim.slide_out_to_right
                        )
                    }
                    fragIeLayoutImg.run {
                        delay(300)
                        visibility = GONE
                        animation = AnimationUtils.loadAnimation(
                            binding.root.context,
                            R.anim.slide_out_to_left
                        )
                    }
                }
            }
        }
    }

    fun showFab() {
        binding.fragIeFab.run {
            if (this.visibility != VISIBLE) {
                lifecycle.launch {
                    delay(200)
                    visibility = VISIBLE
                }

            }
        }
    }

    fun hideFab() {
        binding.fragIeFab.run {
            if (this.visibility != GONE) {
                visibility = GONE
            }
        }
    }

    fun setFabIconPhoto() {
        binding.fragIeFab.setImageResource(R.drawable.icon_camera)
    }

    fun setFabIconRefresh() {
        binding.fragIeFab.setImageResource(R.drawable.icon_refresh)
    }

}