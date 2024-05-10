package br.com.apps.model.model.request.request

import br.com.apps.model.exceptions.InvalidTypeException
import br.com.apps.model.model.label.Label
import java.math.BigDecimal

data class RequestItem(
    val id: String? = null,
    var labelId: String? = null,
    val requestId: String? = null,

    val label: Label? = null,
    var kmMarking: Int? = null,
    var value: BigDecimal? = BigDecimal.ZERO,
    var type: RequestItemType? = null
) {

    fun getDescription(): String {
        return when (type) {
            RequestItemType.REFUEL -> "Abastecimento"
            RequestItemType.COST -> "Despesa"
            RequestItemType.WALLET -> "Ajuda de custo"
            else -> throw InvalidTypeException("Fun getDescription needs a valid type")
        }
    }

    fun getImage(): String {
        return when (type) {
            RequestItemType.REFUEL -> "https://blog.praxio.com.br/wp-content/uploads/2022/01/Imagem-Blog-Praxio-7.png"
            RequestItemType.COST -> "https://www.garbuio.com.br/wp-content/uploads/2022/08/manutencao-de-caminhao.jpg"
            RequestItemType.WALLET -> "https://wtdistribuidora.com.br/uploads/blog/97/83a2ff783cb23fb1db944ddd279411ff.jpg"
            else -> throw IllegalArgumentException("Invalid type for request item")
        }
    }

}

enum class RequestItemType(val description: String) {
    REFUEL("REFUEL"),
    COST("COST"),
    WALLET("WALLET");

    companion object {
        fun getType(type: String): RequestItemType {
            return when (type) {
                REFUEL.description -> REFUEL
                COST.description -> COST
                WALLET.description -> WALLET
                else -> throw InvalidTypeException("Fun getType needs a valid type")
            }
        }
    }

}
