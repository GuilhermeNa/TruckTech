package br.com.apps.model.mapper

import br.com.apps.model.dto.FineDto
import br.com.apps.model.model.Fine
import java.time.LocalDateTime
import java.util.Date

class FineMapper {

    companion object {


        fun toModel(fineDto: FineDto): Fine {
            return Fine(
                uid = fineDto.uid,
                id = fineDto.id,
                truckId = fineDto.truckId,
                driverId = fineDto.driverId,
                date = fineDto.date?.let { getDate(fineDto.date) },
                description = fineDto.description ?: "",
                code = fineDto.code ?: ""
            )

        }

        private fun getDate(date: Date): LocalDateTime {
            val instant = date.toInstant()
            return LocalDateTime.ofInstant(instant, java.time.ZoneId.systemDefault())
        }

    }






}