package br.com.apps.usecase

import br.com.apps.model.dto.travel.RefuelDto
import br.com.apps.model.model.travel.Refuel
import br.com.apps.model.model.travel.Travel
import br.com.apps.model.model.user.PermissionLevelType
import br.com.apps.repository.repository.refuel.RefuelRepository
import br.com.apps.repository.util.EMPTY_DATASET
import br.com.apps.repository.util.EMPTY_ID
import java.security.InvalidParameterException

class RefuelUseCase(private val repository: RefuelRepository)
    : CredentialsValidatorI<RefuelDto> {

    /**
     * Merges the provided list of refuels into the corresponding travels in the travel list.
     * Each refuel is associated with a travel based on the travel ID.
     *
     * @param travelList The list of travels into which refuels will be merged.
     * @param refuelListNullable The nullable list of refuels to merge into the travels.
     * @throws InvalidParameterException if the provided refuel list is null.
     * @throws InvalidParameterException if any travel in the travel list has a null ID.
     */
    fun mergeRefuelList(travelList: List<Travel>, refuelListNullable: List<Refuel>?) {
        val refuelList = refuelListNullable ?: throw InvalidParameterException(EMPTY_DATASET)
        travelList.forEach { travel ->
            val travelId = travel.id ?: throw InvalidParameterException(EMPTY_ID)
            val refuels = refuelList.filter { it.travelId == travelId }
            travel.refuelsList = refuels
        }
    }

    suspend fun delete(permission: PermissionLevelType, dto: RefuelDto) {
        validatePermission(permission, dto)
        repository.delete(dto.id!!)
    }

    suspend fun save(permission: PermissionLevelType, dto: RefuelDto) {
        if (!dto.validateFields()) throw InvalidParameterException("Invalid Refuel for saving")
        validatePermission(permission, dto)
        repository.save(dto)
    }

    override fun validatePermission(permission: PermissionLevelType, dto: RefuelDto) {
        dto.isValid?.let {
            if (dto.isValid!! && permission != PermissionLevelType.MANAGER)
                throw InvalidParameterException("Invalid credentials for $permission")

        } ?: throw NullPointerException("Validation is null")
    }

}