package br.com.apps.usecase

import br.com.apps.model.model.request.request.PaymentRequest
import br.com.apps.model.model.request.request.RequestItem
import br.com.apps.repository.repository.request.RequestRepository
import br.com.apps.repository.util.EMPTY_ID
import java.security.InvalidParameterException

/**
 * Use case class responsible for handling business logic related to payment requests and items.
 *
 * @param repository The repository instance to interact with Firestore.
 */
class RequestUseCase(private val repository: RequestRepository) {

    fun mergeRequestData(
        requestList: List<PaymentRequest>,
        itemList: List<RequestItem>
    ) {
        requestList.forEach { request ->
            val requestId = request.id ?: throw InvalidParameterException(EMPTY_ID)
            val items = itemList.filter { it.requestId == requestId }
            request.itemsList?.clear()
            request.itemsList?.addAll(items)
        }
    }


}