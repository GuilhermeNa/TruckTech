package br.com.apps.repository.repository.request

import androidx.lifecycle.LiveData
import br.com.apps.model.dto.request.request.PaymentRequestDto
import br.com.apps.model.dto.request.request.RequestItemDto
import br.com.apps.model.model.request.request.PaymentRequest
import br.com.apps.model.model.request.request.RequestItem
import br.com.apps.repository.util.Response

interface RequestRepositoryI: RequestWriteI, RequestReadI

interface RequestWriteI {

    suspend fun saveItem(itemDto: RequestItemDto)

    suspend fun updateEncodedImage(requestId: String, encodedImage: String)

    suspend fun save(dto: PaymentRequestDto): String

    suspend fun delete(requestId: String, itemIdList: List<String>? = null)

    suspend fun deleteItem(requestId: String, itemId: String)

}

interface RequestReadI {

    suspend fun getCompleteRequestListByDriverId(driverId: String, flow: Boolean)
            : LiveData<Response<List<PaymentRequest>>>

    suspend fun getCompleteRequestById(requestId: String, flow: Boolean): LiveData<Response<PaymentRequest>>

    suspend fun getItemById(
        requestId: String,
        itemId: String,
        flow: Boolean = false
    ): LiveData<Response<RequestItem>>

}