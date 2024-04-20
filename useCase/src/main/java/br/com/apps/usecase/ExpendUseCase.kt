package br.com.apps.usecase

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import br.com.apps.model.model.label.Label
import br.com.apps.model.model.travel.Expend
import br.com.apps.model.model.travel.Travel
import br.com.apps.repository.EMPTY_DATASET
import br.com.apps.repository.EMPTY_ID
import br.com.apps.repository.Response
import br.com.apps.repository.repository.ExpendRepository
import br.com.apps.repository.repository.LabelRepository
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import java.security.InvalidParameterException

class ExpendUseCase(
    private val repository: ExpendRepository,
    private val labelRepository: LabelRepository
) {

    fun mergeExpendList(travelList: List<Travel>, expendListNullable: List<Expend>?) {
        val expendList = expendListNullable ?: throw InvalidParameterException(EMPTY_DATASET)
        travelList.forEach { travel ->
            val travelId = travel.id ?: throw InvalidParameterException(EMPTY_ID)
            val expends = expendList.filter { it.travelId == travelId }
            travel.expendsList = expends
        }
    }

    suspend fun getExpendListForThisTravel(idList: List<String>): LiveData<Response<List<Expend>>> {
        return repository.getExpendListForThisTravelListId(idList)
    }

    suspend fun deleteExpendForThisTravel(travelId: String, id: String) {
        repository.deleteExpendForThisTravel(travelId, id)
    }

    /**
     * Retrieves all expends related to a specific [Travel].
     *
     * This function asynchronously get an [Expend] list, a [Label] list and merges them
     * into a single LiveData object.
     *
     * @param masterUid The master user ID used to retrieve label data.
     * @param travelId The travel ID used to retrieve expenditure data.
     * @return A LiveData object containing a response of expenditure data with associated labels.
     */
    suspend fun getExpendListWithLabelByTravelId(
        masterUid: String,
        travelId: String
    ): LiveData<Response<List<Expend>>> {
        return coroutineScope {
            val mediator = MediatorLiveData<Response<List<Expend>>>()
            val dataSet = mutableListOf<Expend>()
            val labelData = mutableListOf<Label>()

            CoroutineScope(Dispatchers.Main).launch {
                val deferredA = CompletableDeferred<Unit>()
                val deferredB = CompletableDeferred<Unit>()

                val liveDataA = repository.getExpendListByTravelId(travelId, true)
                val liveDataB = labelRepository.getAllOperationalLabelListForDrivers(masterUid)

                var isFirstLoading = true
                mediator.addSource(liveDataA) { responseA ->
                    when (responseA) {
                        is Response.Error -> throw responseA.exception
                        is Response.Success -> {
                            val expendList = responseA.data ?: emptyList()
                            dataSet.clear()
                            dataSet.addAll(expendList)
                        }
                    }
                    if (isFirstLoading) {
                        deferredA.complete(Unit)
                        isFirstLoading = false
                    } else {
                        mergeLabelListToAnExpend(dataSet, labelData)
                    }
                }
                mediator.addSource(liveDataB) { responseB ->
                    when (responseB) {
                        is Response.Error -> throw responseB.exception
                        is Response.Success -> {
                            val labelList = responseB.data ?: emptyList()
                            labelData.addAll(labelList)
                        }
                    }
                    deferredB.complete(Unit)
                }

                awaitAll(deferredA, deferredB)
                mediator.removeSource(liveDataB)
                mergeLabelListToAnExpend(dataSet, labelData)
                mediator.value = Response.Success(data = dataSet)
            }

            return@coroutineScope mediator
        }
    }

    private fun mergeLabelListToAnExpend(dataSet: List<Expend>, labelData: List<Label>) {
        dataSet.forEach { expend ->
            val label = labelData.firstOrNull { it.id == expend.labelId }
            expend.label = label
        }
    }


}