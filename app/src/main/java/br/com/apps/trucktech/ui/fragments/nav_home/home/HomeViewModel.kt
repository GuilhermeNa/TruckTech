package br.com.apps.trucktech.ui.fragments.nav_home.home

import androidx.lifecycle.ViewModel
import br.com.apps.model.model.FleetFine
import java.time.LocalDateTime

class HomeViewModel: ViewModel() {

    /**
     * Calculates the number of fines that occurred in the current year.
     * @param fineData A list of fine data.
     * @return The number of fines that occurred in the current year.
     */
    fun getThisYearFines(fineData: List<FleetFine>): Int {
        val thisYear = LocalDateTime.now().year
        return fineData.filter { it.date?.year == thisYear }.size
    }

    fun getNewFinesText(): String {
        val year = LocalDateTime.now().year
        return "Multas $year"
    }
}





