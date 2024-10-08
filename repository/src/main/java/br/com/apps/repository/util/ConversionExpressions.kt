package br.com.apps.repository.util

import br.com.apps.model.dto.CustomerDto
import br.com.apps.model.dto.FleetFineDto
import br.com.apps.model.dto.LabelDto
import br.com.apps.model.dto.UserDto
import br.com.apps.model.dto.bank.BankAccountDto
import br.com.apps.model.dto.bank.BankDto
import br.com.apps.model.dto.document.TruckDocumentDto
import br.com.apps.model.dto.employee_dto.DriverDto
import br.com.apps.model.dto.finance.TransactionDto
import br.com.apps.model.dto.finance.payable.PayableDto
import br.com.apps.model.dto.finance.receivable.ReceivableDto
import br.com.apps.model.dto.fleet.TrailerDto
import br.com.apps.model.dto.fleet.TruckDto
import br.com.apps.model.dto.payroll.AdvanceDto
import br.com.apps.model.dto.payroll.LoanDto
import br.com.apps.model.dto.request.ItemDto
import br.com.apps.model.dto.request.RequestDto
import br.com.apps.model.dto.travel.FreightDto
import br.com.apps.model.dto.travel.OutlayDto
import br.com.apps.model.dto.travel.RefuelDto
import br.com.apps.model.dto.travel.TravelAidDto
import br.com.apps.model.dto.travel.TravelDto
import br.com.apps.model.exceptions.ConversionException
import br.com.apps.model.model.Customer
import br.com.apps.model.model.FleetFine
import br.com.apps.model.model.User
import br.com.apps.model.model.bank.Bank
import br.com.apps.model.model.bank.BankAccount
import br.com.apps.model.model.document.TruckDocument
import br.com.apps.model.model.employee.Employee
import br.com.apps.model.model.finance.Transaction
import br.com.apps.model.model.finance.payable.Payable
import br.com.apps.model.model.finance.receivable.Receivable
import br.com.apps.model.model.fleet.Trailer
import br.com.apps.model.model.fleet.Truck
import br.com.apps.model.model.label.Label
import br.com.apps.model.model.payroll.Advance
import br.com.apps.model.model.payroll.Loan
import br.com.apps.model.model.request.Item
import br.com.apps.model.model.request.Request
import br.com.apps.model.model.travel.Freight
import br.com.apps.model.model.travel.Outlay
import br.com.apps.model.model.travel.Refuel
import br.com.apps.model.model.travel.Travel
import br.com.apps.model.model.travel.TravelAid
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

//------------------------------------------------------------------------------------------------//
// -
//------------------------------------------------------------------------------------------------//

fun QuerySnapshot.toTravelList(): List<Travel> {
    return this.mapNotNull { travelDocument ->
        travelDocument.toTravelObject()
    }
}

fun DocumentSnapshot.toTravelObject(): Travel {
    return this.toObject(TravelDto::class.java)?.toModel()
        ?: throw ConversionException("ConversionExpression, toTravelObject: ($this)")
}

//------------------------------------------------------------------------------------------------//
// -
//------------------------------------------------------------------------------------------------//

fun QuerySnapshot.toFreightList(): List<Freight> {
    return this.mapNotNull { freightDocument ->
        freightDocument.toFreightObject()
    }
}

fun DocumentSnapshot.toFreightObject(): Freight {
    return this.toObject(FreightDto::class.java)?.toModel()
        ?: throw ConversionException("ConversionExpression, toFreightObject: ($this)")
}

//------------------------------------------------------------------------------------------------//
// -
//------------------------------------------------------------------------------------------------//

fun QuerySnapshot.toRefuelList(): List<Refuel> {
    return this.mapNotNull { refuelDocument ->
        refuelDocument.toRefuelObject()
    }
}

fun DocumentSnapshot.toRefuelObject(): Refuel {
    return this.toObject(RefuelDto::class.java)?.toModel()
        ?: throw ConversionException("ConversionExpression, toRefuelObject: ($this)")
}

//------------------------------------------------------------------------------------------------//
// -
//------------------------------------------------------------------------------------------------//

fun QuerySnapshot.toOutlayList(): List<Outlay> {
    return this.mapNotNull { expendDocument ->
        expendDocument.toExpendObject()
    }
}

fun DocumentSnapshot.toExpendObject(): Outlay {
    return this.toObject(OutlayDto::class.java)?.toModel()
        ?: throw ConversionException("ConversionExpression, toExpendObject: ($this)")
}

//------------------------------------------------------------------------------------------------//
// -
//------------------------------------------------------------------------------------------------//

fun QuerySnapshot.toLabelList(): List<Label> {
    return this.mapNotNull { labelDocument ->
        labelDocument.toLabelObject()
    }
}

fun DocumentSnapshot.toLabelObject(): Label {
    return this.toObject(LabelDto::class.java)?.toModel()
        ?: throw ConversionException("ConversionExpression, toLabelObject: ($this)")
}

//------------------------------------------------------------------------------------------------//
// -
//------------------------------------------------------------------------------------------------//

fun QuerySnapshot.toFineList(): List<FleetFine> {
    return this.mapNotNull { fineDocument ->
        fineDocument.toFineObject()
    }
}

fun DocumentSnapshot.toFineObject(): FleetFine {
    return this.toObject(FleetFineDto::class.java)?.toModel()
        ?: throw ConversionException("ConversionExpression, toFineObject: ($this)")
}

//------------------------------------------------------------------------------------------------//
// -
//------------------------------------------------------------------------------------------------//

fun QuerySnapshot.toAdvanceList(): List<Advance> {
    return this.mapNotNull { advanceDocument ->
        advanceDocument.toAdvanceObject()
    }
}

fun DocumentSnapshot.toAdvanceObject(): Advance {
    return this.toObject(AdvanceDto::class.java)?.toModel()
        ?: throw ConversionException("ConversionExpression, toAdvanceObject: ($this)")
}

//------------------------------------------------------------------------------------------------//
// -
//------------------------------------------------------------------------------------------------//

fun QuerySnapshot.toLoanList(): List<Loan> {
    return this.mapNotNull { loanDocument ->
        loanDocument.toLoanObject()
    }
}

fun DocumentSnapshot.toLoanObject(): Loan {
    return this.toObject(LoanDto::class.java)?.toModel()
        ?: throw ConversionException("ConversionExpression, toLoanObject: ($this)")
}

//------------------------------------------------------------------------------------------------//
// -
//------------------------------------------------------------------------------------------------//

fun QuerySnapshot.toRequestList(): List<Request> {
    return this.mapNotNull { requestDocument ->
        requestDocument.toRequestObject()
    }
}

fun DocumentSnapshot.toRequestObject(): Request {
    return this.toObject(RequestDto::class.java)?.toModel()
        ?: throw ConversionException("ConversionExpression, toRequestObject: ($this)")
}

//------------------------------------------------------------------------------------------------//
// -
//------------------------------------------------------------------------------------------------//

fun QuerySnapshot.toDocumentList(): List<TruckDocument> {
    return this.mapNotNull { document ->
        document.toDocumentObject()
    }
}

fun DocumentSnapshot.toDocumentObject(): TruckDocument {
    return this.toObject(TruckDocumentDto::class.java)?.toModel()
        ?: throw ConversionException("ConversionExpression, toDocumentObject: ($this)")
}

//------------------------------------------------------------------------------------------------//
// -
//------------------------------------------------------------------------------------------------//

fun QuerySnapshot.toTruckList(): List<Truck> {
    return this.mapNotNull { document ->
        document.toTruckObject()
    }
}

fun DocumentSnapshot.toTruckObject(): Truck {
    return this.toObject(TruckDto::class.java)?.toModel()
        ?: throw ConversionException("ConversionExpression, toTruckObject: ($this)")
}

//------------------------------------------------------------------------------------------------//
// -
//------------------------------------------------------------------------------------------------//

fun QuerySnapshot.toBankList(): List<Bank> {
    return this.mapNotNull { document ->
        document.toBankObject()
    }
}

fun DocumentSnapshot.toBankObject(): Bank {
    return this.toObject(BankDto::class.java)?.toModel()
        ?: throw ConversionException("ConversionExpression, toBankObject: ($this)")
}

//------------------------------------------------------------------------------------------------//
// -
//------------------------------------------------------------------------------------------------//

fun QuerySnapshot.toBankAccountList(): List<BankAccount> {
    return this.mapNotNull { document ->
        document.toBankAccountObject()
    }
}

fun DocumentSnapshot.toBankAccountObject(): BankAccount {
    return this.toObject(BankAccountDto::class.java)?.toModel()
        ?: throw ConversionException("ConversionExpression, toBankAccountObject: ($this)")
}

//------------------------------------------------------------------------------------------------//
// -
//------------------------------------------------------------------------------------------------//

fun QuerySnapshot.toEmployeeList(): List<Employee> {
    return this.mapNotNull { document ->
        document.toEmployeeObject()
    }
}

fun DocumentSnapshot.toEmployeeObject(): Employee {
    //TODO( criar os possivels funcionarios aqui)
    return this.toObject(DriverDto::class.java)?.toModel()
        ?: throw ConversionException("ConversionExpression, toBankAccountObject: ($this)")
}

//------------------------------------------------------------------------------------------------//
// -
//------------------------------------------------------------------------------------------------//

fun QuerySnapshot.toCustomerList(): List<Customer> {
    return this.mapNotNull { document ->
        document.toCustomerObject()
    }
}

fun DocumentSnapshot.toCustomerObject(): Customer {
    return this.toObject(CustomerDto::class.java)?.toModel()
        ?: throw ConversionException("ConversionExpression, toCustomerObject: ($this)")
}

//------------------------------------------------------------------------------------------------//
// -
//------------------------------------------------------------------------------------------------//

fun DocumentSnapshot.toUserObject(): User {
    return this.toObject(UserDto::class.java)?.toModel()
        ?: throw ConversionException("ConversionExpression, toUserObject: ($this)")
}

//------------------------------------------------------------------------------------------------//
// -
//------------------------------------------------------------------------------------------------//

fun QuerySnapshot.toTravelAidList(): List<TravelAid> {
    return this.mapNotNull { document ->
        document.toTravelAidObject()
    }
}

fun DocumentSnapshot.toTravelAidObject(): TravelAid {
    return this.toObject(TravelAidDto::class.java)?.toModel()
        ?: throw ConversionException("ConversionExpression, toTravelAidObject: ($this)")
}

//------------------------------------------------------------------------------------------------//
// -
//------------------------------------------------------------------------------------------------//

fun QuerySnapshot.toTrailerList(): List<Trailer> {
    return this.mapNotNull { document ->
        document.toTrailerObject()
    }
}

fun DocumentSnapshot.toTrailerObject(): Trailer {
    return this.toObject(TrailerDto::class.java)?.toModel()
        ?: throw ConversionException("ConversionExpression, toTrailerObject: ($this)")
}

//------------------------------------------------------------------------------------------------//
// -
//------------------------------------------------------------------------------------------------//

fun QuerySnapshot.toReceivableList(): List<Receivable> {
    return this.mapNotNull { document ->
        document.toReceivableObject()
    }
}

fun DocumentSnapshot.toReceivableObject(): Receivable {
    return this.toObject(ReceivableDto::class.java)?.toModel()
        ?: throw ConversionException("ConversionExpression, toReceivableObject: ($this)")
}

//------------------------------------------------------------------------------------------------//
// -
//------------------------------------------------------------------------------------------------//

fun QuerySnapshot.toPayableList(): List<Payable> {
    return this.mapNotNull { document ->
        document.toPayableObject()
    }
}

fun DocumentSnapshot.toPayableObject(): Payable {
    return this.toObject(PayableDto::class.java)?.toModel()
        ?: throw ConversionException("ConversionExpression, toPayableObject: ($this)")
}

//------------------------------------------------------------------------------------------------//
// -
//------------------------------------------------------------------------------------------------//

fun QuerySnapshot.toTransactionList(): List<Transaction> {
    return this.mapNotNull { document ->
        document.toTransactionObject()
    }
}

fun DocumentSnapshot.toTransactionObject(): Transaction {
    return this.toObject(TransactionDto::class.java)?.toModel()
        ?: throw ConversionException("ConversionExpression, toTransactionObject: ($this)")
}

//------------------------------------------------------------------------------------------------//
// -
//------------------------------------------------------------------------------------------------//

fun QuerySnapshot.toItemList(): List<Item> {
    return this.mapNotNull { document ->
        document.toItemObject()
    }
}

fun DocumentSnapshot.toItemObject(): Item {
    return this.toObject(ItemDto::class.java)?.toModel()
        ?: throw ConversionException("ConversionExpression, toItemObject: ($this)")
}