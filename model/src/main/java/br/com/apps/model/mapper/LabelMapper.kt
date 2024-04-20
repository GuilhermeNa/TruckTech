package br.com.apps.model.mapper

import br.com.apps.model.dto.LabelDto
import br.com.apps.model.model.label.Label
import br.com.apps.model.model.label.LabelType

fun LabelDto.toModel(): Label {
    return Label(
        masterUid = this.uid,
        id = this.id,
        name = this.name,
        urlIcon = this.urlIcon,
        color = this.color,
        type = LabelType.getType(this.type),
        isDefaultLabel = this.isDefaultLabel,
        isOperational = this.isOperational
    )
}