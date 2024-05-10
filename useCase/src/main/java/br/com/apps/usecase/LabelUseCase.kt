package br.com.apps.usecase

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import br.com.apps.model.dto.LabelDto
import br.com.apps.model.model.label.Label
import br.com.apps.model.model.label.LabelType
import br.com.apps.repository.Response
import br.com.apps.repository.repository.LabelRepository
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

class LabelUseCase(private val repository: LabelRepository) {

    /**
     * getAll
     */
    suspend fun getAll(uid: String): LiveData<Response<List<Label>>> {
        return repository.getAll(uid)
    }

    suspend fun getAllByType(type: LabelType, uid: String): LiveData<Response<List<Label>>> {
        return repository.getAllByType(type.description, uid)
    }

    /**
     * save
     */
    fun save(labelDto: LabelDto): MutableLiveData<Boolean> {
        val liveData = MutableLiveData<Boolean>()
        repository.save(labelDto)
        liveData.value = true
        return liveData
    }

    /**
     * get by Id
     */
    suspend fun getLabelById(id: String): LiveData<Response<Label>> {
        return repository.getLabelById(id)
    }

    /**
     * Get operational labels, default and by user
     */
    suspend fun getOperationalLabels(
        masterUid: String,
        type: LabelType,
        isOperational: Boolean
    ): LiveData<Response<List<Label>>> {
        return coroutineScope {
            val mediator = MediatorLiveData<Response<List<Label>>>()
            val dataSet = mutableListOf<Label>()

            CoroutineScope(Dispatchers.Main).launch {
                val deferredA = CompletableDeferred<Unit>()
                val deferredB = CompletableDeferred<Unit>()

                val liveDataA =
                    repository.getUserLabelList(masterUid, type.description, isOperational)
                val liveDataB = repository.getDefaultLabelList(type.description, isOperational)

                mediator.addSource(liveDataA) { responseA ->
                    when (responseA) {
                        is Response.Error -> mediator.value = Response.Error(responseA.exception)
                        is Response.Success -> {
                            val userLabelList = responseA.data ?: emptyList()
                            dataSet.addAll(userLabelList)
                        }
                    }
                    deferredA.complete(Unit)
                }
                mediator.addSource(liveDataB) { responseB ->
                    when (responseB) {
                        is Response.Error -> mediator.value = Response.Error(responseB.exception)
                        is Response.Success -> {
                            val defaultLabelList = responseB.data ?: emptyList()
                            dataSet.addAll(defaultLabelList)
                        }
                    }
                    deferredB.complete(Unit)
                }

                awaitAll(deferredA, deferredB)
                mediator.value = Response.Success(data = dataSet)
            }

            return@coroutineScope mediator
        }
    }

/*    *//**
     * Merge label to freightList
     *//*
    fun mergeFreightToLabel(freightList: List<Freight>, labelList: List<Label>) {
        freightList.forEach { freight ->
            labelList
                .firstOrNull { it.id == freight.labelId }
                ?.let { label ->
                    freight.label = label
                }
        }
    }*/

}