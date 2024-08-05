package br.com.apps.model.enums

enum class TransactionType {
    RECEIVABLE {
        override fun teste(a: String) {
            TODO("Not yet implemented")
        }
    },
    PAYABLE{
        override fun teste(a: String) {
            TODO("Not yet implemented")
        }

    };

    abstract fun teste(a: String)


}