package br.com.apps.model.model.payroll

import br.com.apps.model.dto.payroll.AdvanceDto
import br.com.apps.model.enums.AdvanceType
import br.com.apps.model.interfaces.ModelObjectInterface
import br.com.apps.model.util.toDate
import java.math.BigDecimal
import java.time.LocalDateTime

data class Advance(
    val masterUid: String,
    val id: String? = null,
    val travelId: String? = null,
    val employeeId: String,
    val date: LocalDateTime,
    val value: BigDecimal,
    @field:JvmField
    val isPaid: Boolean,
    @field:JvmField
    val isApproved: Boolean,
    val type: AdvanceType
): ModelObjectInterface<AdvanceDto> {

    override fun toDto() = AdvanceDto(
            masterUid = masterUid,
            id = id,
            travelId = travelId,
            employeeId = employeeId,
            date = date.toDate(),
            value = value.toDouble(),
            isPaid = isPaid,
            isApproved = isApproved,
            type = type.toString()
        )

}


