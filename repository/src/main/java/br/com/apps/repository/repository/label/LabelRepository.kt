package br.com.apps.repository.repository.label

import br.com.apps.model.dto.LabelDto

class LabelRepository(
    private val write: LabelWrite,
    private val read: LabelRead
): LabelRepositoryI {

    //---------------------------------------------------------------------------------------------//
    // WRITE
    //---------------------------------------------------------------------------------------------//

    override suspend fun delete(id: String) = write.delete(id)

    override suspend fun save(dto: LabelDto) = write.save(dto)

    //---------------------------------------------------------------------------------------------//
    // READ
    //---------------------------------------------------------------------------------------------//

    override suspend fun getLabelListByMasterUid(masterUid: String, flow: Boolean) =
        read.getLabelListByMasterUid(masterUid, flow)

    override suspend fun getLabelListByMasterUidAndType(
        type: String,
        masterUid: String,
        flow: Boolean
    ) =
        read.getLabelListByMasterUidAndType(type, masterUid, flow)

    override suspend fun getLabelById(labelId: String, flow: Boolean) =
        read.getLabelById(labelId, flow)

    override suspend fun getLabelListByMasterUidAndTypeAndOperational(
        masterUid: String,
        type: String,
        isOperational: Boolean,
        flow: Boolean
    ) = read.getLabelListByMasterUidAndTypeAndOperational(masterUid, type, isOperational, flow)

    override suspend fun getDefaultLabelList(type: String, isOperational: Boolean, flow: Boolean) =
        read.getDefaultLabelList(type, isOperational, flow)

    override suspend fun getDefaultExpendLabelList(isOperational: Boolean?, flow: Boolean) =
        read.getDefaultExpendLabelList(isOperational, flow)

    override suspend fun getAllOperationalLabelListForDrivers(masterUid: String) =
        read.getAllOperationalLabelListForDrivers(masterUid)

}