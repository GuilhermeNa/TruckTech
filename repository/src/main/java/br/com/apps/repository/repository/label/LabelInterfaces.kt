package br.com.apps.repository.repository.label

import androidx.lifecycle.LiveData
import br.com.apps.model.dto.LabelDto
import br.com.apps.model.model.label.Label
import br.com.apps.repository.util.Response

interface LabelRepositoryInterface : LabelWriteInterface, LabelReadInterface

interface LabelWriteInterface {

    /**
     * Delete a label.
     * @param id The id of the label.
     * @return LiveData with the result of operation.
     */
    suspend fun delete(id: String)

    /**
     * Add a new user or edit if already exists.
     * @param dto The label sent by the user.
     * @return The ID of the saved label.
     */
    suspend fun save(dto: LabelDto)

}

interface LabelReadInterface {

    /**
     * Retrieves a list of [Label]'s associated with a master UID.
     *
     * @param masterUid The master UID to identify which labels to retrieve.
     * @param flow If true, indicates to use continuous data flow updates.
     * @return A LiveData object containing the response of the operation,
     *         either a Success with a list of Label objects or an Error with an exception.
     */
    suspend fun getLabelListByMasterUid(masterUid: String, flow: Boolean = false)
            : LiveData<Response<List<Label>>>

    /**
     * Retrieves a list of [Label]'s associated with a master UID and specific type.
     *
     * @param type The type of labels to retrieve.
     * @param masterUid The master UID to identify which labels to retrieve.
     * @param flow If true, indicates to use continuous data flow updates.
     * @return A LiveData object containing the response of the operation,
     *         either a Success with a list of Label objects or an Error with an exception.
     */
    suspend fun getLabelListByMasterUidAndType(
        type: String,
        masterUid: String,
        flow: Boolean = false
    ): LiveData<Response<List<Label>>>

    /**
     * Retrieves a [Label] by its ID.
     *
     * @param labelId The ID of the label to retrieve.
     * @param flow If true, indicates to use continuous data flow updates.
     * @return A LiveData object containing the response of the operation,
     *         either a Success with the Label object or an Error with an exception.
     */
    suspend fun getLabelById(labelId: String, flow: Boolean = false): LiveData<Response<Label>>

    /**
     * Retrieves a list of [Label]'s associated with a master UID, specific type, and operational status.
     *
     * @param masterUid The master UID to identify which labels to retrieve.
     * @param type The type of labels to retrieve.
     * @param isOperational If true, filters labels that are operational.
     * @param flow If true, indicates to use continuous data flow updates.
     * @return A LiveData object containing the response of the operation,
     *         either a Success with a list of Label objects or an Error with an exception.
     */
    suspend fun getLabelListByMasterUidAndTypeAndOperational(
        masterUid: String,
        type: String,
        isOperational: Boolean,
        flow: Boolean = false
    ): LiveData<Response<List<Label>>>

    /**
     * Retrieves a list of default [Label]'s based on type and operational status.
     *
     * @param type The type of labels to retrieve.
     * @param isOperational If true, filters labels that are operational.
     * @param flow If true, indicates to use continuous data flow updates.
     * @return A LiveData object containing the response of the operation,
     *         either a Success with a list of Label objects or an Error with an exception.
     */
    suspend fun getDefaultLabelList(
        type: String,
        isOperational: Boolean,
        flow: Boolean = false
    ): LiveData<Response<List<Label>>>

    /**
     * Retrieves a list of default expenditure [Label]'s based on operational status.
     *
     * @param isOperational If true, filters labels that are operational.
     * @param flow If true, indicates to use continuous data flow updates.
     * @return A LiveData object containing the response of the operation,
     *         either a Success with a list of Label objects or an Error with an exception.
     */
    suspend fun getDefaultExpendLabelList(isOperational: Boolean? = false, flow: Boolean = false)
            : LiveData<Response<List<Label>>>

    /**
     * Retrieves a list of operational [Label]'s for drivers associated with a master UID.
     *
     * @param masterUid The master UID to identify which labels to retrieve.
     * @return A LiveData object containing the response of the operation,
     *         either a Success with a list of Label objects or an Error with an exception.
     */
    suspend fun getAllOperationalLabelListForDrivers(masterUid: String): LiveData<Response<List<Label>>>

}