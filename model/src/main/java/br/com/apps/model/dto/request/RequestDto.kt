package br.com.apps.model.dto.request

import br.com.apps.model.enums.AccessLevel
import br.com.apps.model.enums.PaymentRequestStatusType
import br.com.apps.model.exceptions.AccessLevelException
import br.com.apps.model.exceptions.CorruptedFileException
import br.com.apps.model.exceptions.invalid.InvalidForSavingException
import br.com.apps.model.interfaces.AccessPermissionInterface
import br.com.apps.model.interfaces.DtoObjectInterface
import br.com.apps.model.model.request.Request
import br.com.apps.model.util.ACCESS_DENIED
import br.com.apps.model.util.toLocalDateTime
import java.util.Date

data class RequestDto(
    val masterUid: String? = null,
    var id: String? = null,
    val uid: String? = null,
    val urlImage: String? = null,
    val requestNumber: Long? = null,
    val date: Date? = null,
    val status: String? = null,
) : DtoObjectInterface<Request>, AccessPermissionInterface {

    override fun validateDataIntegrity() {
        if (masterUid == null ||
            id == null ||
            uid == null ||
            requestNumber == null ||
            date == null ||
            status == null
        ) throw CorruptedFileException("RequestDto data is corrupted: ($this)")
    }

    override fun validateDataForDbInsertion() {
        if (masterUid == null ||
            uid == null ||
            requestNumber == null ||
            date == null ||
            status == null
        ) throw InvalidForSavingException("RequestDto data is invalid: ($this)")
    }

    override fun toModel(): Request {
        validateDataIntegrity()
        return Request(
            masterUid = masterUid!!,
            id = id!!,
            uid = uid!!,
            urlImage = urlImage,
            requestNumber = requestNumber!!,
            date = date!!.toLocalDateTime(),
            status = PaymentRequestStatusType.valueOf(status!!)
        )
    }

    override fun validateWriteAccess(access: AccessLevel?) {
        if (access == null) throw NullPointerException()
        if (status != PaymentRequestStatusType.SENT.name &&
            access != AccessLevel.MANAGER
        ) throw AccessLevelException(ACCESS_DENIED)
    }

    override fun validateReadAccess() {
        TODO("Not yet implemented")
    }

}