package br.com.apps.usecase.usecase

import br.com.apps.model.dto.request.ItemDto
import br.com.apps.repository.repository.item.ItemRepository
import br.com.apps.repository.util.WriteRequest

class ItemUseCase(private val repository: ItemRepository) {

    fun save(writeReq: WriteRequest<ItemDto>) {

    }

    fun delete(writeReq: WriteRequest<ItemDto>) {

    }

}