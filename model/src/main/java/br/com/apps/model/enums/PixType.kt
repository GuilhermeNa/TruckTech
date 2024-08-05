package br.com.apps.model.enums

enum class PixType(val description: String) {
    PHONE("Celular"),
    EMAIL("Email"),
    CPF("Cpf"),
    CNPJ("Cnpj");

    companion object {

        /**
         * @throws IllegalArgumentException
         */
        fun String.toPixType() = PixType.valueOf(this)

        fun String.toPixTypeString() = PixType.valueOf("this")

        fun String.descriptionToPixType() = PixType.values().associateBy { it.description }[this]

        fun getPixTypeList() = PixType.values().toList()

        fun getPixTypeDescriptions() = PixType.values().map { it.description }

        fun getMappedPixTypeAndDescription(): Map<PixType, String> {
            return hashMapOf(
                Pair(PHONE, "Celular"),
                Pair(EMAIL, "Email"),
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


