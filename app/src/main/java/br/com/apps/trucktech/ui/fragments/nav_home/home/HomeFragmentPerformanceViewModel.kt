package br.com.apps.trucktech.ui.fragments.nav_home.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import br.com.apps.trucktech.expressions.getKeyByValue
import br.com.apps.usecase.IncomeUseCase

private const val TRAVEL = "Viagem"
private const val MONTH = "Mês"
private const val QUARTER = "Trimestre"
private const val SEMESTER = "Semestre"
private const val YEAR = "Ano"
private const val ALL = "Tudo"

class HomeFragmentPerformanceViewModel() : ViewModel() {


    //---------------------------------------------------------------------------------------------//
    // HEADER
    //---------------------------------------------------------------------------------------------//

    val headerItemsMap = mapOf(
        Pair(0, TRAVEL),
        Pair(1, MONTH),
        Pair(2, QUARTER),
        Pair(3, SEMESTER),
        Pair(4, YEAR),
        Pair(5, ALL)
    )

    private var _headerPos = MutableLiveData(0)
    val headerPos get() = _headerPos

    fun newHeaderSelected(headerTitle: String) {
        _headerPos.value = headerItemsMap.getKeyByValue(headerTitle)

    }

    //---------------------------------------------------------------------------------------------//
    // PERIOD
    //---------------------------------------------------------------------------------------------//

    private var _periodPos = MutableLiveData(0)
    val periodPos get() = _periodPos

    fun newPeriodSelected(adapterPos: Int) {
        _periodPos.value = adapterPos
    }

}