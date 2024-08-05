package br.com.apps.model.model.employee

import br.com.apps.model.enums.WorkRole
import br.com.apps.model.interfaces.ModelObjectInterface

abstract class Employee(
    open val masterUid: String? = null,
    open val id: String? = null,
    open val name: String? = "",
    open val type: WorkRole? = null
): ModelObjectInterface<Employee>


