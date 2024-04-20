package br.com.apps.repository

import br.com.apps.model.dto.FineDto
import br.com.apps.model.dto.LabelDto
import br.com.apps.model.dto.payroll.AdvanceDto
import br.com.apps.model.dto.payroll.LoanDto
import br.com.apps.model.dto.travel.ExpendDto
import br.com.apps.model.dto.travel.FreightDto
import br.com.apps.model.dto.travel.RefuelDto
import br.com.apps.model.dto.travel.TravelDto
import br.com.apps.model.mapper.toModel
import br.com.apps.model.model.Fine
import br.com.apps.model.model.label.Label
import br.com.apps.model.model.payroll.Advance
import br.com.apps.model.model.payroll.Loan
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

fun QuerySnapshot.toLabelList(): List<Label> {
    return this.mapNotNull { labelDocument ->
        labelDocument.toLabelObject()
    }
}

fun DocumentSnapshot.toLabelObject(): Label? {
    return this.toObject(LabelDto::class.java)?.toModel()
}

fun QuerySnapshot.toFineList(): List<Fine> {
    return this.mapNotNull { fineDocument ->
        fineDocument.toFineObject()
    }
}

fun DocumentSnapshot.toFineObject(): Fine? {
    return this.toObject(FineDto::class.java)?.toModel()
}

fun QuerySnapshot.toAdvanceList(): List<Advance> {
    return this.mapNotNull { advanceDocument ->
        advanceDocument.toAdvanceObject()
    }
}

fun DocumentSnapshot.toAdvanceObject(): Advance? {
    return this.toObject(AdvanceDto::class.java)?.toModel()
}

fun QuerySnapshot.toLoanList(): List<Loan> {
    return this.mapNotNull { loanDocument ->
        loanDocument.toLoanObject()
    }
}

fun DocumentSnapshot.toLoanObject(): Loan? {
    return this.toObject(LoanDto::class.java)?.toModel()
}