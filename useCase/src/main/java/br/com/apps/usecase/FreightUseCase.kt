package br.com.apps.usecase

import br.com.apps.model.dto.travel.FreightDto
import br.com.apps.model.model.travel.Freight
import br.com.apps.model.model.travel.Travel
import br.com.apps.model.model.user.PermissionLevelType
import br.com.apps.repository.repository.freight.FreightRepository
import br.com.apps.repository.util.EMPTY_DATASET
import br.com.apps.repository.util.EMPTY_ID
import java.security.InvalidParameterException

class FreightUseCase(
    private val fRepository: FreightRepository,
    private val cUseCase: CustomerUseCase
) : CredentialsValidatorI<FreightDto> {

    /**
     * Merges the provided list of freights into the corresponding travels in the travel list.
     * Each freight is associated with a travel based on the travel ID.
     *
     * @param travelList The list of travels into which freights will be merged.
     * @param freightListNullable The nullable list of freights to merge into the travels.
     * @throws InvalidParameterException if the provided freight list is null.
     * @throws InvalidParameterException if any travel in the travel list has a null ID.
     */
    fun mergeFreightList(travelList: List<Travel>, freightListNullable: List<Freight>?) {
        val freightList = freightListNullable ?: throw InvalidParameterException(EMPTY_DATASET)
        travelList.forEach { travel ->
            val travelId = travel.id ?: throw InvalidParameterException(EMPTY_ID)
            val freights = freightList.filter { it.travelId == travelId }
            travel.freightsList = freights
        }
    }

    suspend fun delete(permission: PermissionLevelType, dto: FreightDto) {
        validatePermission(permission, dto)
        fRepository.delete(dto.id!!)
    }

    suspend fun save(permission: PermissionLevelType, dto: FreightDto) {
        if (!dto.validateFields()) throw InvalidParameterException("Invalid Freight for saving")
        validatePermission(permission, dto)
        fRepository.save(dto)
    }

    override fun validatePermission(permission: PermissionLevelType, dto: FreightDto) {
        dto.isValid?.let {
            if (dto.isValid!! && permission != PermissionLevelType.MANAGER)
                throw InvalidParameterException("Invalid credentials for $permission")

        } ?: throw NullPointerException("Validation is null")

    }

}
