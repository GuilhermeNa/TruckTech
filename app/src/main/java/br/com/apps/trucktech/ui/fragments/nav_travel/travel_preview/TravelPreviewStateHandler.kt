package br.com.apps.trucktech.ui.fragments.nav_travel.travel_preview

import android.content.res.Resources
import android.view.View.GONE
import android.view.View.VISIBLE
import br.com.apps.trucktech.R
import br.com.apps.trucktech.databinding.FragmentTravelPreviewBinding


class TravelPreviewStateHandler(private val binding: FragmentTravelPreviewBinding) {

    fun showLoading() {
        binding.apply {
            toolbar.visibility = GONE
            boxHeader.layout.visibility = GONE
            boxDate.layout.visibility = GONE
            horizontalView.visibility = GONE
            boxOdometer.layout.visibility = GONE
            boxFreight.layout.visibility = GONE
            boxRefuel.layout.visibility = GONE
            boxExpend.layout.visibility = GONE
            boxAid.layout.visibility = GONE
            boxLoading.layout.visibility = VISIBLE
        }
    }

    fun showEmpty() {
        binding.apply {
            toolbar.visibility = VISIBLE
            boxDate.layout.visibility = GONE
            horizontalView.visibility = GONE
            boxOdometer.layout.visibility = GONE
            boxHeader.layout.visibility = GONE
            boxAid.layout.visibility = GONE
            boxFreight.layout.visibility = GONE
            boxRefuel.layout.visibility = GONE
            boxExpend.layout.visibility = GONE
            boxError.apply {
                layout.visibility = VISIBLE
                empty.visibility = VISIBLE
            }
            boxLoading.layout.visibility = GONE
        }
    }

    fun showError(e: Exception) {
        e.printStackTrace()
        binding.apply {
            toolbar.visibility = VISIBLE
            boxHeader.layout.visibility = GONE
            horizontalView.visibility = GONE
            boxOdometer.layout.visibility = GONE
            boxAid.layout.visibility = GONE
            boxFreight.layout.visibility = GONE
            boxRefuel.layout.visibility = GONE
            boxExpend.layout.visibility = GONE
            boxError.apply {
                layout.visibility = VISIBLE
                error.visibility = VISIBLE
            }
            toolbar.menu.apply {
                findItem(R.id.menu_travel_preview_options).run {
                    isEnabled = false
                    isVisible = false
                }

            }
            boxLoading.layout.visibility = GONE
        }
    }

    fun showLoaded() {
        binding.apply {
            toolbar.visibility = VISIBLE
            horizontalView.visibility = VISIBLE
            boxOdometer.layout.visibility = VISIBLE
            boxAid.layout.visibility = VISIBLE
            boxFreight.layout.visibility = VISIBLE
            boxRefuel.layout.visibility = VISIBLE
            boxExpend.layout.visibility = VISIBLE
            boxError.apply {
                layout.visibility = GONE
                error.visibility = GONE
                empty.visibility = GONE
            }
            boxLoading.layout.visibility = GONE
        }
    }

    fun showWhenFinished() {
        binding.apply {
            boxHeader.apply {
                fab.visibility = GONE
                layoutValidationIncomplete.visibility = GONE
                layoutIsFinished.visibility = VISIBLE
                bindClosedContainerImage()
            }
            boxDate.apply {
                layout.visibility = VISIBLE
                boxTravelPreviewInitialDate.visibility = VISIBLE
                boxTravelPreviewFinalDate.visibility = VISIBLE
            }
            boxOdometer.boxTravelPreviewFinalMeasure.visibility = VISIBLE
        }
    }

    private fun bindClosedContainerImage() {
        binding.boxHeader.image.run {
            layoutParams = layoutParams.apply {
                height = (160 * Resources.getSystem().displayMetrics.density).toInt()
                width = (160 * Resources.getSystem().displayMetrics.density).toInt()
            }
            setImageResource(R.drawable.image_container_closed)
        }
    }

    fun showWhenAlreadyAuthenticated() {
        binding.apply {
            boxHeader.apply {
                layoutValidationIncomplete.visibility = GONE
                layoutIsFinished.visibility = GONE
                fab.visibility = VISIBLE
                image.run {
                    setImageResource(R.drawable.image_container)
                }

                boxDate.apply {
                    layout.visibility = VISIBLE
                    boxTravelPreviewInitialDate.visibility = VISIBLE
                    boxTravelPreviewFinalDate.visibility = GONE
                }

                boxOdometer.boxTravelPreviewFinalMeasure.visibility = GONE
            }
        }
    }

    fun showWhenAwaitingAuthentication() {
        binding.apply {
            boxHeader.apply {
                fab.visibility = GONE
                layoutIsFinished.visibility = GONE
                layoutValidationIncomplete.visibility = VISIBLE
                image.run {
                    setImageResource(R.drawable.image_container)
                }
            }
            boxDate.apply {
                layout.visibility = VISIBLE
                boxTravelPreviewInitialDate.visibility = VISIBLE
                boxTravelPreviewFinalDate.visibility = GONE
            }
            boxOdometer.boxTravelPreviewFinalMeasure.visibility = GONE
        }
    }

    fun hideHeader() {
        binding.boxHeader.layout.visibility = GONE
    }

    fun showHeader() {
        binding.boxHeader.layout.visibility = VISIBLE
    }

    fun showFinishing() {
        binding.apply {
            boxLoading.layout.visibility = VISIBLE
        }
    }

}

