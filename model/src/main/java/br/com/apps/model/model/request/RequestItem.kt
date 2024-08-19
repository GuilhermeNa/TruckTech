package br.com.apps.model.model.request
/*

import br.com.apps.model.dto.request.request.RequestItemDto
import br.com.apps.model.enums.RequestItemType
import br.com.apps.model.interfaces.ModelObjectInterface
import br.com.apps.model.model.label.Label
import java.math.BigDecimal

data class RequestItem(
    val id: String? = null,
    var labelId: String? = null,
    val requestId: String,

    var docUrl: String? = null,
    val label: Label? = null,
    var kmMarking: Int? = null,
    var value: BigDecimal,
    var type: RequestItemType
) : ModelObjectInterface<RequestItemDto> {

    */
/**
     * Returns the description of the request item based on its type.
     *
     * @return A descriptive string based on the type of request item.
     *//*

    fun getDescription(): String {
        return when (type) {
            RequestItemType.REFUEL -> "Abastecimento"
            RequestItemType.COST -> "Despesa"
            RequestItemType.WALLET -> "Vale para viagem"
        }
    }

    */
/**
     * Returns the image URL associated with the request item based on its type.
     *
     * @return The URL of the image related to the type of request item.
     *//*

    fun getImage(): String {
        return when (type) {
            RequestItemType.REFUEL -> "https://blog.praxio.com.br/wp-content/uploads/2022/01/Imagem-Blog-Praxio-7.png"
            RequestItemType.COST -> "https://www.garbuio.com.br/wp-content/uploads/2022/08/manutencao-de-caminhao.jpg"
            RequestItemType.WALLET -> "https://wtdistribuidora.com.br/uploads/blog/97/83a2ff783cb23fb1db944ddd279411ff.jpg"
        }
    }

    override fun toDto() = RequestItemDto(
        id = id,
        labelId = labelId,
        requestId = requestId,
        docUrl = docUrl,
        kmMarking = kmMarking,
        value = value.toDouble(),
        type = type.description
    )

}

*/

