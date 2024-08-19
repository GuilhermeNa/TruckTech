package br.com.apps.usecase.usecase

import br.com.apps.repository.repository.employee.EmployeeRepository

class EmployeeUseCase(
    private val authUseCase: AuthenticationUseCase,
    private val repository: EmployeeRepository
) {




}