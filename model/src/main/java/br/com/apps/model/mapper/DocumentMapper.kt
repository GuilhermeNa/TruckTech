package br.com.apps.model.mapper

import br.com.apps.model.dto.DocumentDto
import br.com.apps.model.model.Document

class DocumentMapper {

    companion object {

        fun toModel(documentDto: DocumentDto): Document {
            return Document(
                id = documentDto.id,
                truckId = documentDto.truckId,
                expenseId = documentDto.expenseId,
                labelId = documentDto.labelId,
            )
        }
    }

}