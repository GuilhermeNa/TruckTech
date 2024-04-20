package br.com.apps.usecase

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import br.com.apps.model.dto.LabelDto
import br.com.apps.model.model.Label
import br.com.apps.model.model.LabelType
import br.com.apps.repository.Resource
import br.com.apps.repository.repository.LabelRepository

class LabelUseCase(private val repository: LabelRepository) {

    /**
     *
     */
    fun getAll(uid: String): MutableLiveData<List<Label>> {
        return repository.getAll(uid)
    }

    suspend fun getAllByType(type: LabelType, uid: String): LiveData<List<Label>> {
        return repository.getAllByType(type.description, uid)
    }

    /**
     *
     */
    fun save(labelDto: LabelDto): MutableLiveData<Boolean> {
        val liveData = MutableLiveData<Boolean>()
        repository.save(labelDto)
        liveData.value = true
        return liveData
    }

    /**
     *
     */
    fun getById(id: String): LiveData<Label> {
        return repository.getById(id)
    }

    suspend fun getOperationalLabels(masterUid: String, type: LabelType): LiveData<Resource<List<Label>>> {
        return repository.getOperationalLabels(masterUid, type.description)
    }

    fun getListOfTitles(labels: List<Label>): List<String> {
        return labels.mapNotNull { it.name }
    }

}