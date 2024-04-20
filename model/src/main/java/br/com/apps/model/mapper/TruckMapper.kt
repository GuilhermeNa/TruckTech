package br.com.apps.model.mapper

import br.com.apps.model.dto.TruckDto
import br.com.apps.model.model.Truck

class TruckMapper {

    companion object {

     /*   fun toDto(truck: Truck): TruckDto {

            return TruckDto()
        }*/

        fun toModel(truckDto: TruckDto, id: String? = null): Truck {
            return Truck(
                id = id,
                driverId = truckDto.driverId,
                masterUid = truckDto.masterUid,
                plate = truckDto.plate,
                color = truckDto.color
            )
        }

    }


}