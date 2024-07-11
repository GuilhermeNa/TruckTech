package br.com.apps.usecase.usecase

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import br.com.apps.model.dto.travel.ExpendDto
import br.com.apps.model.model.label.Label
import br.com.apps.model.model.travel.Expend
import br.com.apps.model.model.travel.Travel
import br.com.apps.repository.repository.expend.ExpendRepository
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
    private val repository: ExpendRepository,
    private val labelRepository: LabelRepository
) {

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
        mediator: MediatorLiveData<Response<List<Expend>>>
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
        mediator: MediatorLiveData<Response<List<Expend>>>,
        complete: (expendList: List<Expend>) -> Unit
    ) {
        val liveData = repository.getExpendListByTravelId(travelId, true)

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

    private fun mergeLabelListToAnExpend(dataSet: List<Expend>, labelData: List<Label>) {
        dataSet.forEach { expend ->
            val label = labelData.firstOrNull { it.id == expend.labelId }
            expend.label = label
        }
    }

    /**
     * Deletes an ExpendDto entity from the repository after validating the permission.
     *
     * @param writeReq A WriteRequest object containing the data (ExpendDto)
     * to delete and the authorization level (authLevel).
     *
     * @throws InvalidAuthLevelException if the permission validation fails (Response.Error).
     */
    suspend fun delete(writeReq: WriteRequest<ExpendDto>) {
        val dto = writeReq.data
        val auth = writeReq.authLevel

        validateAndProcess(
            permission = { dto.validatePermission(auth) }
        ).let { response ->
            when (response) {
                is Response.Error -> throw response.exception
                is Response.Success -> repository.delete(dto.id)
            }
        }
    }

    /**
     * Saves an ExpendDto entity to the repository after validating permission and data.
     *
     * @param writeReq: A WriteRequest object containing the data (ExpendDto) to save and
     * the authorization level (authLevel).
     *
     * @throws InvalidAuthLevelException if the permission validation fails (Response.Error).
     * @throws NullPointerException if there is any null field.
     */
    suspend fun save(writeReq: WriteRequest<ExpendDto>) {
        val dto = writeReq.data
        val auth = writeReq.authLevel

        validateAndProcess(
            permission = { dto.validatePermission(auth) },
            validator = { dto.validateDataForSaving() }
        ).let { response ->
            when (response) {
                is Response.Error -> throw response.exception
                is Response.Success -> repository.save(dto)
            }
        }

    }

}