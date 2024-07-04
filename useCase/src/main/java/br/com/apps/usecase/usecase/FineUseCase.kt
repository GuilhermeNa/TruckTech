package br.com.apps.usecase.usecase

import br.com.apps.model.model.Fine
import br.com.apps.repository.repository.fine.FineRepository
import java.time.LocalDateTime

class FineUseCase(private val repository: FineRepository) {

    /**
     * Calculates the number of fines that occurred in the current year.
     * @param fineData A list of fine data.
     * @return The number of fines that occurred in the current year.
     */
    fun getThisYearFines(fineData: List<Fine>): Int {
        val thisYear = LocalDateTime.now().year
        return fineData.filter { it.date?.year == thisYear }.size
    }

}