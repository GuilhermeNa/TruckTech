package br.com.apps.usecase.usecase

import androidx.lifecycle.LiveData
import br.com.apps.model.dto.user_dto.CommonUserDto
import br.com.apps.model.dto.user_dto.MasterUserDto
import br.com.apps.model.dto.user_dto.UserDto
import br.com.apps.model.model.employee.EmployeeType
import br.com.apps.model.model.user.User
import br.com.apps.repository.repository.UserRepository
import br.com.apps.repository.util.Resource

class UserUseCase(private val repository: UserRepository) {

    /**
    * Try create a new user
    *
    * This method will send the data to the repository for create a new User
    *
    * @param userDto
    *
    * @return liveData with Resource from repository
    */
    fun createNewUser(userDto: UserDto): LiveData<Resource<Boolean>> {
        return when(userDto) {
            is CommonUserDto -> repository.createANewCommonUser(userDto)
            is MasterUserDto -> repository.createANewMasterUser(userDto)
            else -> throw IllegalArgumentException("Not valid DTO")
        }
    }

    /**
     * Retrieve the user by its Id
     */
    fun getById(uid: String, type: EmployeeType): LiveData<User> {
        return when(type) {
            EmployeeType.ADMIN,
            EmployeeType.DRIVER -> repository.getCommonUser(uid)
            else -> throw IllegalArgumentException("Not type for Employee")
        }
    }

}