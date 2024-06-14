package br.com.apps.repository.repository.request

import androidx.lifecycle.LiveData
import br.com.apps.model.dto.request.request.TravelRequestDto
import br.com.apps.model.dto.request.request.RequestItemDto
import br.com.apps.model.model.request.travel_requests.PaymentRequest
import br.com.apps.model.model.request.travel_requests.RequestItem
import br.com.apps.repository.util.Response

interface RequestRepositoryI : RequestWriteI, RequestReadI

interface RequestWriteI {

    suspend fun saveItem(itemDto: RequestItemDto)

    suspend fun updateEncodedImage(requestId: String, encodedImage: String)

    suspend fun save(dto: TravelRequestDto): String

    suspend fun delete(requestId: String, itemIdList: List<String>? = null)

    suspend fun deleteItem(requestId: String, itemId: String)

}

interface RequestReadI {

    suspend fun getRequestListByDriverId(driverId: String, flow: Boolean = false)
            : LiveData<Response<List<PaymentRequest>>>

    suspend fun getItemListByRequests(idList: List<String>, flow: Boolean = false)
            : LiveData<Response<List<RequestItem>>>

    suspend fun getRequestById(
        requestId: String,
        flow: Boolean = false
    ): LiveData<Response<PaymentRequest>>

    suspend fun getItemListByRequestId(requestId: String, flow: Boolean = false)
            : LiveData<Response<List<RequestItem>>>

    suspend fun getItemById(
        requestId: String,
        itemId: String,
        flow: Boolean = false
    ): LiveData<Response<RequestItem>>

}