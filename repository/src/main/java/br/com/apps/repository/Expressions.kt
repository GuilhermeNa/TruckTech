package br.com.apps.repository

import br.com.apps.model.dto.travel.ExpendDto
import br.com.apps.model.dto.travel.FreightDto
import br.com.apps.model.dto.travel.RefuelDto
import br.com.apps.model.dto.travel.TravelDto
import br.com.apps.model.mapper.toModel
import br.com.apps.model.model.travel.Expend
import br.com.apps.model.model.travel.Freight
import br.com.apps.model.model.travel.Refuel
import br.com.apps.model.model.travel.Travel
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.QuerySnapshot

fun QuerySnapshot.toTravelList(): List<Travel> {
    return this.mapNotNull { travelDocument ->
        travelDocument.toTravelObject()
    }
}

fun DocumentSnapshot.toTravelObject(): Travel? {
    return this.toObject(TravelDto::class.java)?.toModel()
}

fun QuerySnapshot.toFreightList(): List<Freight> {
    return this.mapNotNull { freightDocument ->
        freightDocument.toFreightObject()
    }
}

fun DocumentSnapshot.toFreightObject(): Freight? {
    return this.toObject(FreightDto::class.java)?.toModel()
}

fun QuerySnapshot.toRefuelList(): List<Refuel> {
    return this.mapNotNull { refuelDocument ->
        refuelDocument.toRefuelObject()
    }
}

fun DocumentSnapshot.toRefuelObject(): Refuel? {
    return this.toObject(RefuelDto::class.java)?.toModel()
}

fun QuerySnapshot.toExpendList(): List<Expend> {
    return this.mapNotNull { expendDocument ->
        expendDocument.toExpendObject()
    }
}

fun DocumentSnapshot.toExpendObject(): Expend? {
    return this.toObject(ExpendDto::class.java)?.toModel()
}