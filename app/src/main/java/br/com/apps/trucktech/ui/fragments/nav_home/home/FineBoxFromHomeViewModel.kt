package br.com.apps.trucktech.ui.fragments.nav_home.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import br.com.apps.model.model.Fine
import br.com.apps.repository.util.Response
import br.com.apps.repository.repository.FineRepository
import br.com.apps.usecase.FineUseCase
import kotlinx.coroutines.launch
import java.time.LocalDateTime

class FineBoxFromHomeViewModel(
    private val useCase: FineUseCase,
    private val repository: FineRepository
) : ViewModel() {

    /**
     * LiveData holding the response data of type [Response] with a [Fine]
     * to be displayed on screen.
     */
    private val _fineData = MutableLiveData<Response<List<Fine>>>()
    val fineData get() = _fineData

    //---------------------------------------------------------------------------------------------//
    // -
    //---------------------------------------------------------------------------------------------//

    fun loadData(driverId: String) {
        viewModelScope.launch {
            repository.getFineListByDriverId(driverId, withFlow = false).asFlow().collect {
                _fineData.value = it
            }
        }
    }

    /**
     * Retrieves the number of fines that occurred in the current year.
     * This function delegates the calculation to the corresponding use case.
     *
     * @param fineData A list of fine data.
     * @return The number of fines that occurred in the current year.
     */
    fun getThisYearFines(fineData: List<Fine>): String {
        return useCase.getThisYearFines(fineData).toString()
    }

    fun getNewFinesText(): String {
        val year = LocalDateTime.now().year
        return "Multas $year"
    }

}