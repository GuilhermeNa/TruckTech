package br.com.apps.model.model.payment_method

enum class PixType(val description: String) {
    PHONE("PHONE"),
    EMAIL("EMAIL"),
    ID("ID"),
    KEY("KEY"),
    CPF("CPF"),
    CNPJ("CNPJ");

    companion object {
        fun getType(type: String): PixType {
            return when (type) {
                "PHONE" -> PHONE
                "EMAIL" -> EMAIL
                "ID" -> ID
                "KEY" -> KEY
                "CPF" -> CPF
                "CNPJ" -> CNPJ
                else -> throw IllegalArgumentException("PixType, getType: Invalid type for string ($type)")
            }
        }

        fun getTypeInString(text: String): String {
            return when (text) {
                "Celular" -> "PHONE"
                "Email" -> "EMAIL"
                "Id" -> "ID"
                "Chave" -> "KEY"
                "Cpf" -> "CPF"
                "Cnpj" -> "CNPJ"
                else -> throw IllegalArgumentException()
            }
        }

        fun getMappedPixTypeAndDescription(): Map<PixType, String> {
            return hashMapOf(
                Pair(PHONE, "Celular"),
                Pair(EMAIL, "Email"),
                Pair(ID, "Id"),
                Pair(KEY, "Chave"),
                Pair(CPF, "Cpf"),
                Pair(CNPJ, "Cnpj"),
            )
        }

        fun isValidString(text: String): Boolean {
            val list = getMappedPixTypeAndDescription().entries.map { it.value }
            return list.contains(text)
        }

    }


}


