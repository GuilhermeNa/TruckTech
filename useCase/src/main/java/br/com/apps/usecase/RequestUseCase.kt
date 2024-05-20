package br.com.apps.usecase

import br.com.apps.repository.repository.request.RequestRepository

/**
 * Use case class responsible for handling business logic related to payment requests and items.
 *
 * @param repository The repository instance to interact with Firestore.
 */
class RequestUseCase(private val repository: RequestRepository) {

}