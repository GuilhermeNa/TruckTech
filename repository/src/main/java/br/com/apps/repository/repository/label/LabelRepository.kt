package br.com.apps.repository.repository.label

import br.com.apps.model.dto.LabelDto

class LabelRepository(
    private val write: LabelWriteImpl,
    private val read: LabelReadImpl
): LabelRepositoryInterface {

    //---------------------------------------------------------------------------------------------//
    // WRITE
    //---------------------------------------------------------------------------------------------//

    override suspend fun delete(id: String) = write.delete(id)

    override suspend fun save(dto: LabelDto) = write.save(dto)

    //---------------------------------------------------------------------------------------------//
    // READ
    //---------------------------------------------------------------------------------------------//

    override suspend fun fetchLabelListByMasterUid(masterUid: String, flow: Boolean) =
        read.fetchLabelListByMasterUid(masterUid, flow)

    override suspend fun fetchLabelListByMasterUidAndType(
        type: String,
        masterUid: String,
        flow: Boolean
    ) = read.fetchLabelListByMasterUidAndType(type, masterUid, flow)

    override suspend fun fetchLabelById(labelId: String, flow: Boolean) =
        read.fetchLabelById(labelId, flow)

    override suspend fun fetchLabelListByMasterUidAndTypeAndOperational(
        masterUid: String,
        type: String,
        isOperational: Boolean,
        flow: Boolean
    ) = read.fetchLabelListByMasterUidAndTypeAndOperational(masterUid, type, isOperational, flow)

    override suspend fun fetchDefaultLabelList(type: String, isOperational: Boolean, flow: Boolean) =
        read.fetchDefaultLabelList(type, isOperational, flow)

    override suspend fun fetchDefaultExpendLabelList(isOperational: Boolean?, flow: Boolean) =
        read.fetchDefaultExpendLabelList(isOperational, flow)

    override suspend fun getAllOperationalLabelListForDrivers(masterUid: String) =
        read.getAllOperationalLabelListForDrivers(masterUid)

}