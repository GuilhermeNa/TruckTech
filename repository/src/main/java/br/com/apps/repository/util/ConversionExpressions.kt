package br.com.apps.repository.util

import br.com.apps.model.dto.DocumentDto
import br.com.apps.model.dto.FineDto
import br.com.apps.model.dto.LabelDto
import br.com.apps.model.dto.TruckDto
import br.com.apps.model.dto.payroll.AdvanceDto
import br.com.apps.model.dto.payroll.LoanDto
import br.com.apps.model.dto.request.request.PaymentRequestDto
import br.com.apps.model.dto.request.request.RequestItemDto
import br.com.apps.model.dto.travel.ExpendDto
import br.com.apps.model.dto.travel.FreightDto
import br.com.apps.model.dto.travel.RefuelDto
import br.com.apps.model.dto.travel.TravelDto
import br.com.apps.model.exceptions.ConversionException
import br.com.apps.model.mapper.toModel
import br.com.apps.model.model.Document
import br.com.apps.model.model.Fine
import br.com.apps.model.model.Truck
import br.com.apps.model.model.label.Label
import br.com.apps.model.model.payroll.Advance
import br.com.apps.model.model.payroll.Loan
import br.com.apps.model.model.request.request.PaymentRequest
import br.com.apps.model.model.request.request.RequestItem
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

fun DocumentSnapshot.toTravelObject(): Travel {
    return this.toObject(TravelDto::class.java)?.toModel()
        ?: throw ConversionException("ConversionExpression, toTravelObject: ($this)")
}

fun QuerySnapshot.toFreightList(): List<Freight> {
    return this.mapNotNull { freightDocument ->
        freightDocument.toFreightObject()
    }
}

fun DocumentSnapshot.toFreightObject(): Freight {
    return this.toObject(FreightDto::class.java)?.toModel()
        ?: throw ConversionException("ConversionExpression, toFreightObject: ($this)")
}

fun QuerySnapshot.toRefuelList(): List<Refuel> {
    return this.mapNotNull { refuelDocument ->
        refuelDocument.toRefuelObject()
    }
}

fun DocumentSnapshot.toRefuelObject(): Refuel {
    return this.toObject(RefuelDto::class.java)?.toModel()
        ?: throw ConversionException("ConversionExpression, toRefuelObject: ($this)")
}

fun QuerySnapshot.toExpendList(): List<Expend> {
    return this.mapNotNull { expendDocument ->
        expendDocument.toExpendObject()
    }
}

fun DocumentSnapshot.toExpendObject(): Expend {
    return this.toObject(ExpendDto::class.java)?.toModel()
        ?: throw ConversionException("ConversionExpression, toExpendObject: ($this)")
}

fun QuerySnapshot.toLabelList(): List<Label> {
    return this.mapNotNull { labelDocument ->
        labelDocument.toLabelObject()
    }
}

fun DocumentSnapshot.toLabelObject(): Label {
    return this.toObject(LabelDto::class.java)?.toModel()
        ?: throw ConversionException("ConversionExpression, toLabelObject: ($this)")
}

fun QuerySnapshot.toFineList(): List<Fine> {
    return this.mapNotNull { fineDocument ->
        fineDocument.toFineObject()
    }
}

fun DocumentSnapshot.toFineObject(): Fine {
    return this.toObject(FineDto::class.java)?.toModel()
        ?: throw ConversionException("ConversionExpression, toFineObject: ($this)")
}

fun QuerySnapshot.toAdvanceList(): List<Advance> {
    return this.mapNotNull { advanceDocument ->
        advanceDocument.toAdvanceObject()
    }
}

fun DocumentSnapshot.toAdvanceObject(): Advance {
    return this.toObject(AdvanceDto::class.java)?.toModel()
        ?: throw ConversionException("ConversionExpression, toAdvanceObject: ($this)")
}

fun QuerySnapshot.toLoanList(): List<Loan> {
    return this.mapNotNull { loanDocument ->
        loanDocument.toLoanObject()
    }
}

fun DocumentSnapshot.toLoanObject(): Loan {
    return this.toObject(LoanDto::class.java)?.toModel()
        ?: throw ConversionException("ConversionExpression, toLoanObject: ($this)")
}

fun QuerySnapshot.toRequestList(): List<PaymentRequest> {
    return this.mapNotNull { requestDocument ->
        requestDocument.toRequestObject()
    }
}

fun DocumentSnapshot.toRequestObject(): PaymentRequest {
    return this.toObject(PaymentRequestDto::class.java)?.toModel()
        ?: throw ConversionException("ConversionExpression, toRequestObject: ($this)")
}

fun QuerySnapshot.toRequestItemList(): List<RequestItem> {
    return this.mapNotNull { requestItemDocument ->
        requestItemDocument.toRequestItemObject()
    }
}

fun DocumentSnapshot.toRequestItemObject(): RequestItem {
    return this.toObject(RequestItemDto::class.java)?.toModel()
        ?: throw ConversionException("ConversionExpression, toRequestItemObject: ($this)")
}

fun QuerySnapshot.toDocumentList(): List<Document> {
    return this.mapNotNull { document ->
        document.toDocumentObject()
    }
}

fun DocumentSnapshot.toDocumentObject(): Document {
    return this.toObject(DocumentDto::class.java)?.toModel()
        ?: throw ConversionException("ConversionExpression, toDocumentObject: ($this)")
}

fun QuerySnapshot.toTruckList(): List<Truck> {
    return this.mapNotNull { document ->
        document.toTruckObject()
    }
}

fun DocumentSnapshot.toTruckObject(): Truck {
    return this.toObject(TruckDto::class.java)?.toModel()
        ?: throw ConversionException("ConversionExpression, toTruckObject: ($this)")
}
