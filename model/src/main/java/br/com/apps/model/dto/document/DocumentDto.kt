package br.com.apps.model.dto.document

import br.com.apps.model.interfaces.DtoObjectInterface
import br.com.apps.model.model.document.Document
import java.util.Date

abstract class DocumentDto(
    open val masterUid: String? = null,
    open val id: String? = null,
    open val labelId: String? = null,
    open val urlImage: String? = null,
    open val expeditionDate: Date? = null,
    open val type: String? = null,
): DtoObjectInterface<Document>{

    abstract override fun toModel(): Document

    abstract override fun validateDataForDbInsertion()

    abstract override fun validateDataIntegrity()

}