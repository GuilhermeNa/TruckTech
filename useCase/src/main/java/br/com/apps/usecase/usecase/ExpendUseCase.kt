package br.com.apps.usecase.usecase

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import br.com.apps.model.dto.travel.OutlayDto
import br.com.apps.model.model.label.Label
import br.com.apps.model.model.travel.Outlay
import br.com.apps.model.model.travel.Travel
import br.com.apps.repository.repository.outlay.OutlayRepository
import br.com.apps.repository.repository.label.LabelRepository
import br.com.apps.repository.util.EMPTY_DATASET
import br.com.apps.repository.util.Response
import br.com.apps.repository.util.WriteRequest
import br.com.apps.repository.util.validateAndProcess
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

class ExpendUseCase(
    private val repository: OutlayRepository,
    private val labelRepository: LabelRepository
) {

    /**
     * Retrieves all expends related to a specific [Travel].
     *
     * This function asynchronously get an [Outlay] list, a [Label] list and merges them
     * into a single LiveData object.
     *
     * @param masterUid The master user ID used to retrieve label data.
     * @param travelId The travel ID used to retrieve expenditure data.
     * @return A LiveData object containing a response of expenditure data with associated labels.
     */
    suspend fun getExpendListWithLabelByTravelId(
        masterUid: String,
        travelId: String
    ): LiveData<Response<List<Outlay>>> {
        return coroutineScope {
            val mediator = MediatorLiveData<Response<List<Outlay>>>()

            CoroutineScope(Dispatchers.Main).launch {
                val labelResponse = loadLabels(masterUid, mediator)
                val labels = labelResponse.await()

                loadExpends(travelId, mediator) { expends ->

                    mergeLabelListToAnExpend(expends, labels)
                    mediator.value = Response.Success(data = expends)
                }
            }

            return@coroutineScope mediator
        }
    }

    private suspend fun loadLabels(
        masterUid: String,
        mediator: MediatorLiveData<Response<List<Outlay>>>
    ): CompletableDeferred<List<Label>> {
        val deferred = CompletableDeferred<List<Label>>()
        val liveData = labelRepository.getAllOperationalLabelListForDrivers(masterUid)

        mediator.addSource(liveData) { response ->
            when (response) {
                is Response.Error -> deferred.completeExceptionally(response.exception)
                is Response.Success -> {
                    response.data?.let { deferred.complete(it) }
                        ?: deferred.completeExceptionally(NullPointerException(EMPTY_DATASET))
                }
            }
            mediator.removeSource(liveData)
        }

        return deferred
    }

    private suspend fun loadExpends(
        travelId: String,
        mediator: MediatorLiveData<Response<List<Outlay>>>,
        complete: (outlayList: List<Outlay>) -> Unit
    ) {
        val liveData = repository.fetchOutlayListByTravelId(travelId, true)

        mediator.addSource(liveData) { response ->
            when (response) {
                is Response.Error -> throw response.exception
                is Response.Success -> {
                    response.data?.let { complete(it) }
                        ?: complete(emptyList())
                }
            }
        }
    }

    private fun mergeLabelListToAnExpend(dataSet: List<Outlay>, labelData: List<Label>) {
        dataSet.forEach { expend ->
            val label = labelData.firstOrNull { it.id == expend.labelId }
            expend.label = label
        }
    }

    /**
     * Deletes an OutlayDto entity from the repository after validating the permission.
     *
     * @param writeReq A WriteRequest object containing the data (OutlayDto)
     * to delete and the authorization level (authLevel).
     *
     * @throws InvalidAuthLevelException if the permission validation fails (Response.Error).
     */
    suspend fun delete(writeReq: WriteRequest<OutlayDto>) {
        val dto = writeReq.data
        val auth = writeReq.authLevel

        validateAndProcess(
            validatePermission = { dto.validateWriteAccess(auth) }
        ).let { response ->
            when (response) {
                is Response.Error -> throw response.exception
                is Response.Success -> repository.delete(dto.id)
            }
        }
    }

    /**
     * Saves an OutlayDto entity to the repository after validating permission and data.
     *
     * @param writeReq: A WriteRequest object containing the data (OutlayDto) to save and
     * the authorization level (authLevel).
     *
     * @throws InvalidAuthLevelException if the permission validation fails (Response.Error).
     * @throws NullPointerException if there is any null field.
     */
    suspend fun save(writeReq: WriteRequest<OutlayDto>) {
        val dto = writeReq.data
        val auth = writeReq.authLevel

        validateAndProcess(
            validatePermission = { dto.validateWriteAccess(auth) },
            validateData = { dto.validateDataForDbInsertion() }
        ).let { response ->
            when (response) {
                is Response.Error -> throw response.exception
                is Response.Success -> repository.save(dto)
            }
        }

    }

}