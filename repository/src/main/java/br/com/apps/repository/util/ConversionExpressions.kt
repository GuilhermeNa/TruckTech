package br.com.apps.repository.util

import br.com.apps.model.dto.CustomerDto
import br.com.apps.model.dto.FleetFineDto
import br.com.apps.model.dto.LabelDto
import br.com.apps.model.dto.TruckDocumentDto
import br.com.apps.model.dto.bank.BankDto
import br.com.apps.model.dto.employee_dto.BankAccountDto
import br.com.apps.model.dto.employee_dto.DriverEmployeeDto
import br.com.apps.model.dto.fleet.TrailerDto
import br.com.apps.model.dto.fleet.TruckDto
import br.com.apps.model.dto.payroll.AdvanceDto
import br.com.apps.model.dto.payroll.LoanDto
import br.com.apps.model.dto.payroll.TravelAidDto
import br.com.apps.model.dto.request.request.RequestItemDto
import br.com.apps.model.dto.request.request.TravelRequestDto
import br.com.apps.model.dto.travel.ExpendDto
import br.com.apps.model.dto.travel.FreightDto
import br.com.apps.model.dto.travel.RefuelDto
import br.com.apps.model.dto.travel.TravelDto
import br.com.apps.model.dto.user_dto.CommonUserDto
import br.com.apps.model.exceptions.ConversionException
import br.com.apps.model.mapper.EmployeeMapper.Companion.toModel
import br.com.apps.model.mapper.toModel
import br.com.apps.model.model.Customer
import br.com.apps.model.model.FleetFine
import br.com.apps.model.model.TruckDocument
import br.com.apps.model.model.bank.Bank
import br.com.apps.model.model.bank.BankAccount
import br.com.apps.model.model.employee.Employee
import br.com.apps.model.model.fleet.Trailer
import br.com.apps.model.model.fleet.Truck
import br.com.apps.model.model.label.Label
import br.com.apps.model.model.payroll.Advance
import br.com.apps.model.model.payroll.Loan
import br.com.apps.model.model.request.travel_requests.PaymentRequest
import br.com.apps.model.model.request.travel_requests.RequestItem
import br.com.apps.model.model.travel.Expend
import br.com.apps.model.model.travel.Freight
import br.com.apps.model.model.travel.Refuel
import br.com.apps.model.model.travel.Travel
import br.com.apps.model.model.travel.TravelAid
import br.com.apps.model.model.user.CommonUser
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.QuerySnapshot

// Utility extension functions for Firebase Firestore snapshots to convert various document types
// to corresponding model objects.

// These extension functions provide convenient conversion methods:

// Sample:
//   - toTravelList(): Converts a Firestore `QuerySnapshot` containing documents
//     into a list of travel objects.
//   - toTravelObject(): Converts a Firestore `DocumentSnapshot` representing a travel document
//     into a single `Travel` object.


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

fun QuerySnapshot.toFineList(): List<FleetFine> {
    return this.mapNotNull { fineDocument ->
        fineDocument.toFineObject()
    }
}

fun DocumentSnapshot.toFineObject(): FleetFine {
    return this.toObject(FleetFineDto::class.java)?.toModel()
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
    this.isEmpty
    return this.mapNotNull { requestDocument ->
        requestDocument.toRequestObject()
    }
}

fun DocumentSnapshot.toRequestObject(): PaymentRequest {
    return this.toObject(TravelRequestDto::class.java)?.toModel()
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

fun QuerySnapshot.toDocumentList(): List<TruckDocument> {
    return this.mapNotNull { document ->
        document.toDocumentObject()
    }
}

fun DocumentSnapshot.toDocumentObject(): TruckDocument {
    return this.toObject(TruckDocumentDto::class.java)?.toModel()
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

fun QuerySnapshot.toBankList(): List<Bank> {
    return this.mapNotNull { document ->
        document.toBankObject()
    }
}

fun DocumentSnapshot.toBankObject(): Bank {
    return this.toObject(BankDto::class.java)?.toModel()
        ?: throw ConversionException("ConversionExpression, toBankObject: ($this)")
}

fun QuerySnapshot.toBankAccountList(): List<BankAccount> {
    return this.mapNotNull { document ->
        document.toBankAccountObject()
    }
}

fun DocumentSnapshot.toBankAccountObject(): BankAccount {
    return this.toObject(BankAccountDto::class.java)?.toModel()
        ?: throw ConversionException("ConversionExpression, toBankAccountObject: ($this)")
}

fun QuerySnapshot.toEmployeeList(): List<Employee> {
    return this.mapNotNull { document ->
        document.toEmployeeObject()
    }
}

fun DocumentSnapshot.toEmployeeObject(): Employee {
    return this.toObject(DriverEmployeeDto::class.java)?.toModel()
        ?: throw ConversionException("ConversionExpression, toBankAccountObject: ($this)")
}

fun QuerySnapshot.toCustomerList(): List<Customer> {
    return this.mapNotNull { document ->
        document.toCustomerObject()
    }
}

fun DocumentSnapshot.toCustomerObject(): Customer {
    return this.toObject(CustomerDto::class.java)?.toModel()
        ?: throw ConversionException("ConversionExpression, toCustomerObject: ($this)")
}

fun DocumentSnapshot.toCommonUserObject(): CommonUser {
    return this.toObject(CommonUserDto::class.java)?.toModel() as CommonUser
}

fun QuerySnapshot.toTravelAidList(): List<TravelAid> {
    return this.mapNotNull { document ->
        document.toTravelAidObject()
    }
}

fun DocumentSnapshot.toTravelAidObject(): TravelAid {
    return this.toObject(TravelAidDto::class.java)?.toModel()
        ?: throw ConversionException("ConversionExpression, toTravelAidObject: ($this)")
}

fun QuerySnapshot.toTrailerList(): List<Trailer> {
    return this.mapNotNull { document ->
        document.toTrailerObject()
    }
}

fun DocumentSnapshot.toTrailerObject(): Trailer {
    return this.toObject(TrailerDto::class.java)?.toModel()
        ?: throw ConversionException("ConversionExpression, toTrailerObject: ($this)")
}