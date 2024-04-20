package br.com.apps.model.model.employee

abstract class Employee(

    open val masterUid: String? = null,
    open val id: String? = null,

    open val name: String? = "",
    open val type: EmployeeType? = null

)

enum class EmployeeType(val description: String) {
    DRIVER("DRIVER"),
    ADMIN("ADMIN");

    companion object {
        fun getType(type: String): EmployeeType {
            return when (type) {
                "DRIVER" -> DRIVER
                "ADMIN" -> ADMIN
                else -> throw IllegalArgumentException()
            }
        }
    }

}
