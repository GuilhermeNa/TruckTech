package br.com.apps.model.model.travel

import br.com.apps.model.dto.travel.FreightDto
import br.com.apps.model.exceptions.EmptyDataException
import br.com.apps.model.exceptions.null_objects.NullCustomerException
import br.com.apps.model.expressions.toPercentValue
import br.com.apps.model.interfaces.ModelObjectInterface
import br.com.apps.model.model.Customer
import br.com.apps.model.model.employee.Employee
import br.com.apps.model.model.fleet.Truck
import br.com.apps.model.util.ERROR_STRING
import br.com.apps.model.util.toDate
import java.math.BigDecimal
import java.time.LocalDateTime

/**
 * This class encapsulates details of a cargo logistical shipment.
 *
 *  Notes:
 *  * Freights are transported by a [Truck].
 *  * Each freight has a [Customer] who ordered the transport.
 *  * Each freight is associated with a specific [Travel] and is part of its list of freights.
 *  * Must generate a commission payment for the (driver) [Employee] who performed the transport.
 *  * This object needs to be validated([isValid]) by an administrator to verify its accuracy.
 *    After validation, it cannot be modified by users without appropriate permissions.
 *
 * @property masterUid Unique identifier for the master record associated with this freight.
 * @property id Unique identifier for the [Freight].
 * @property truckId Identifier for the [Truck] used for transporting the cargo.
 * @property travelId Identifier for the [Travel] associated with this freight.
 * @property employeeId Identifier for the driver) [Employee] responsible for the transport.
 * @property customerId Identifier for the [Customer] for whom the freight is being transported.
 * @property cargo Description of the cargo being transported.
 * @property origin Location from where the cargo is being shipped.
 * @property destiny Destination location where the cargo is being delivered.
 * @property value Monetary value of the cargo shipment.
 * @property weight Weight of the cargo.
 * @property loadingDate Date and time when the cargo was loaded for transport.
 * @property commissionPercentual Percentage of commission to be paid to the driver based on the [value].
 * @property isValid Indicates whether the freight record is valid (true) or not (false). This property
 * must only be set by a Assistant user.
 * @property isCommissionPaid Indicates whether the commission for this freight has been paid (true) or not (false).
 * @property _customer Optional [Customer] object containing additional details about the customer associated with this freight.
 */
data class Freight(
    val masterUid: String,
    var id: String,
    val truckId: String,
    val travelId: String,
    val employeeId: String,
    val customerId: String,
    val cargo: String,
    val origin: String,
    val destiny: String,
    val value: BigDecimal,
    val weight: BigDecimal,
    val loadingDate: LocalDateTime,
    val commissionPercentual: BigDecimal,
    val isValid: Boolean,
    private var _customer: Customer? = null
) : ModelObjectInterface<FreightDto> {

    val customer: Customer?
        get() = _customer

    companion object {

        /**
         * Extension function for list of [Freight]'s to merge with a list of [Customer]'s.
         *
         * Each freight in the list will have its customer updated with
         * the corresponding from the customers list.
         *
         * @param customers A list of customers objects.
         *
         * @return A [List] of [Freight] with valid [Customer]'s.
         */
        fun List<Freight>.merge(customers: List<Customer>) {
            this.forEach { it.setCustomerById(customers) }
        }

    }

    /**
     * Calculates and returns the commission value based on the freight value
     * and [commissionPercentual].
     *
     * @return The calculated commission value as a BigDecimal.
     */
    fun getCommissionValue(): BigDecimal {
        val x = value.multiply(commissionPercentual)
        return x.toPercentValue()
    }

    /**
     * Sets the [_customer] property of this document based on the provided list of [Customer]'s.
     * @param customers A list of customers objects to search for the customer with the matching ID.
     * @throws NullCustomerException If no customer in the list has an ID that matches the [customerId]
     * of this document.
     */
    fun setCustomerById(customers: List<Customer>) {
        if (customers.isEmpty()) throw EmptyDataException("Customer list cannot be empty")

        _customer = customers.firstOrNull { it.id == customerId }
            ?: throw NullCustomerException("Customer not found")
    }

    fun setCustomer(customer: Customer) {
        _customer = customer
    }

    /**
     * @return The name of the [Customer] or the string "Error" if the customer is not registered.
     */
    fun getCustomerName(): String {
        return try {
            _customer!!.name
        } catch (e: Exception) {
            e.printStackTrace()
            ERROR_STRING
        }
    }

    /**
     * @return The cnpj of the [Customer] or the string "Error" if the customer is not registered.
     */
    fun getCustomerCnpj(): String {
        return try {
            _customer!!.cnpj
        } catch (e: Exception) {
            e.printStackTrace()
            ERROR_STRING
        }
    }

    override fun toDto() = FreightDto(
        masterUid = masterUid,
        id = id,
        truckId = truckId,
        travelId = travelId,
        employeeId = employeeId,
        customerId = customerId,
        origin = origin,
        destiny = destiny,
        cargo = cargo,
        weight = weight.toDouble(),
        value = value.toDouble(),
        loadingDate = loadingDate.toDate(),
        commissionPercentual = commissionPercentual.toDouble(),
        isValid = isValid
    )

}
