package br.com.apps.model.model.travel

import br.com.apps.model.exceptions.NullCustomerException
import br.com.apps.model.expressions.toPercentValue
import br.com.apps.model.model.Customer
import br.com.apps.model.model.employee.Employee
import br.com.apps.model.model.fleet.Truck
import java.math.BigDecimal
import java.time.LocalDateTime

/**
 * This class encapsulates details of a cargo logistical shipment.
 *
 *  Observe:
 *  * Freights are transported by a [Truck].
 *  * Each freight has a [Customer] who ordered the transport.
 *  * Each freight is associated with a specific [Travel] and is part of a list of freights.
 *  * Must generate a commission payment for the (driver) [Employee] who performed the transport.
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
 * must only be set by a Admin user.
 * @property isCommissionPaid Indicates whether the commission for this freight has been paid (true) or not (false).
 * @property customer Optional [Customer] object containing additional details about the customer associated with this freight.
 */
data class Freight(
    val masterUid: String,
    var id: String,
    val truckId: String,
    val travelId: String,
    val employeeId: String,
    var customerId: String,
    var cargo: String,
    var origin: String,
    var destiny: String,
    var value: BigDecimal,
    var weight: BigDecimal,
    var loadingDate: LocalDateTime,
    var commissionPercentual: BigDecimal,
    @field:JvmField
    var isValid: Boolean,
    @field:JvmField
    var isCommissionPaid: Boolean,
    var customer: Customer? = null,
) {

    /**
     * Calculates and returns the commission value based on the freight value
     * and commission percentage.
     *
     * @return The calculated commission value as a BigDecimal.
     */
    fun getCommissionValue(): BigDecimal {
        val x = value.multiply(commissionPercentual)
        return x.toPercentValue()
    }

    /**
     * Sets the [customer] property of this document based on the provided list of [Customer]'s.
     * @param customers A list of customers objects to search for the customer with the matching ID.
     * @throws NullCustomerException If no customer in the list has an ID that matches the [customerId]
     * of this document.
     */
    fun setCustomerById(customers: List<Customer>) {
        customer = customers.firstOrNull { it.id == customerId }
            ?: throw NullCustomerException("Customer not found")
    }

    /**
     * @return The name of the [Customer] or the string "Error" if the customer is not registered.
     */
    fun getCustomerName(): String {
        return try {
            customer!!.name
        } catch (e: Exception) {
            e.printStackTrace()
            "Erro"
        }
    }

    /**
     * @return The cnpj of the [Customer] or the string "Error" if the customer is not registered.
     */
    fun getCustomerCnpj(): String {
        return try {
            customer!!.name
        } catch (e: Exception) {
            e.printStackTrace()
            "Erro"
        }
    }

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

}
