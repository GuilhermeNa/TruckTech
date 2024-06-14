package br.com.apps.usecase

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import br.com.apps.model.dto.travel.ExpendDto
import br.com.apps.model.model.label.Label
import br.com.apps.model.model.travel.Expend
import br.com.apps.model.model.travel.Travel
import br.com.apps.model.model.user.PermissionLevelType
import br.com.apps.repository.repository.expend.ExpendRepository
import br.com.apps.repository.repository.label.LabelRepository
import br.com.apps.repository.util.EMPTY_DATASET
import br.com.apps.repository.util.EMPTY_ID
import br.com.apps.repository.util.Response
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import java.security.InvalidParameterException

class ExpendUseCase(
    private val repository: ExpendRepository,
    private val labelRepository: LabelRepository
): CredentialsValidatorI<ExpendDto> {

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
     * Merges the provided list of expenditures into the corresponding travels in the travel list.
     * Each expenditure is associated with a travel based on the travel ID.
     *
     * @param travelList The list of travels into which expenditures will be merged.
     * @param expendListNullable The nullable list of expenditures to merge into the travels.
     * @throws InvalidParameterException if the provided expenditure list is null.
     * @throws InvalidParameterException if any travel in the travel list has a null ID.
     */
    fun mergeExpendList(travelList: List<Travel>, expendListNullable: List<Expend>?) {
        val expendList = expendListNullable ?: throw InvalidParameterException(EMPTY_DATASET)
        travelList.forEach { travel ->
            val travelId = travel.id ?: throw InvalidParameterException(EMPTY_ID)
            val expends = expendList.filter { it.travelId == travelId }
            travel.expendsList = expends
        }
    }

    suspend fun delete(permission: PermissionLevelType, dto: ExpendDto) {
        validatePermission(permission, dto)
        repository.delete(dto.id!!)
    }

    suspend fun save(permission: PermissionLevelType, dto: ExpendDto) {
        if (!dto.validateFields()) throw InvalidParameterException("Invalid Refuel for saving")
        validatePermission(permission, dto)
        repository.save(dto)
    }

    override fun validatePermission(permission: PermissionLevelType, dto: ExpendDto) {
        dto.isValid?.let {
            if (dto.isValid!! && permission != PermissionLevelType.MANAGER)
                throw InvalidParameterException("Invalid credentials for $permission")

        } ?: throw NullPointerException("Validation is null")
    }


}