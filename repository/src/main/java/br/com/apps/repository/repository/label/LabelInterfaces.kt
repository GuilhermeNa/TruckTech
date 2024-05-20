package br.com.apps.repository.repository.label

import androidx.lifecycle.LiveData
import br.com.apps.model.dto.LabelDto
import br.com.apps.model.model.label.Label
import br.com.apps.repository.util.Response

interface LabelRepositoryI : LabelWriteI, LabelReadI

interface LabelWriteI {

    suspend fun delete(id: String)

    suspend fun save(dto: LabelDto)

}

interface LabelReadI {

    suspend fun getLabelListByMasterUid(masterUid: String, flow: Boolean = false)
            : LiveData<Response<List<Label>>>

    suspend fun getLabelListByMasterUidAndType(
        type: String,
        masterUid: String,
        flow: Boolean = false
    ): LiveData<Response<List<Label>>>

    suspend fun getLabelById(labelId: String, flow: Boolean = false): LiveData<Response<Label>>

    suspend fun getLabelListByMasterUidAndTypeAndOperational(
        masterUid: String,
        type: String,
        isOperational: Boolean,
        flow: Boolean = false
    ): LiveData<Response<List<Label>>>

    suspend fun getDefaultLabelList(
        type: String,
        isOperational: Boolean,
        flow: Boolean = false
    ): LiveData<Response<List<Label>>>

    suspend fun getDefaultExpendLabelList(isOperational: Boolean? = false, flow: Boolean = false)
            : LiveData<Response<List<Label>>>

    suspend fun getAllOperationalLabelListForDrivers(masterUid: String): LiveData<Response<List<Label>>>

}