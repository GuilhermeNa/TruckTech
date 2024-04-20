package br.com.apps.model.mapper

import br.com.apps.model.dto.LabelDto
import br.com.apps.model.model.Label
import br.com.apps.model.model.LabelType

class LabelMapper {

    companion object {

        fun toModel(labelDto: LabelDto, id: String? = null): Label {
            return Label(
                uid = labelDto.uid,
                id = id,
                name = labelDto.name,
                icon = labelDto.icon,
                color = labelDto.color,
                type = LabelType.getType(labelDto.type),
                isDefaultLabel = labelDto.isDefaultLabel
            )
        }
    }


}