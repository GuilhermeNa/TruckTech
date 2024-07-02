package br.com.apps.trucktech.ui.fragments.nav_home.home.box_view_managers

import android.content.Context
import br.com.apps.model.model.Fine
import br.com.apps.trucktech.databinding.PanelHomeFragmentMyFinesBinding
import br.com.apps.trucktech.expressions.loadImageThroughUrl
import br.com.apps.trucktech.expressions.navigateTo
import br.com.apps.trucktech.ui.fragments.nav_home.home.HomeFragmentDirections
import java.time.LocalDateTime

class BoxFinesViewManager(
    private val binding: PanelHomeFragmentMyFinesBinding,
    private val context: Context
) {

    fun initialize(fines: List<Fine>?) {
        binding.apply {
            panelFinesImage.loadImageThroughUrl(
                "https://gringo.com.vc/wp-content/uploads/2022/06/Multa_18032016_1738_1280_960-1024x768.jpg"
            )

            panelFinesNewText.text = getNewFinesText()
            panelFinesNew.text = fines?.let { getThisYearFines(it).toString() }
            panelFinesAccumulated.text = fines?.size.toString()

            panelFinesCard.setOnClickListener {
                it.navigateTo(HomeFragmentDirections.actionHomeFragmentToFinesFragment())
            }
        }
    }

    /**
     * Calculates the number of fines that occurred in the current year.
     * @param fineData A list of fine data.
     * @return The number of fines that occurred in the current year.
     */
    private fun getThisYearFines(fineData: List<Fine>): Int {
        val thisYear = LocalDateTime.now().year
        return fineData.filter { it.date?.year == thisYear }.size
    }

    private fun getNewFinesText(): String {
        val year = LocalDateTime.now().year
        return "Multas $year"
    }

}