package br.com.apps.model.mapper

import br.com.apps.model.dto.LabelDto
import br.com.apps.model.model.Label
import br.com.apps.model.model.LabelType

fun LabelDto.toModel(): Label {
    return Label(
        uid = this.uid,
        id = this.id,
        name = this.name,
        icon = this.icon,
        color = this.color,
        type = LabelType.getType(this.type),
        isDefaultLabel = this.isDefaultLabel
    )
}
