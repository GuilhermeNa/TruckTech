package br.com.apps.trucktech.util

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import java.lang.ref.WeakReference
import java.math.BigDecimal
import java.math.RoundingMode
import java.text.DecimalFormat
import java.text.NumberFormat
import java.util.Locale
import java.util.Objects

class MonetaryMaskUtil(editText: EditText) : TextWatcher {

    private var editTextWeakRef: WeakReference<EditText>

    private val locale = Locale.getDefault()

    init {
        editTextWeakRef = WeakReference(editText)
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

    override fun afterTextChanged(s: Editable?) {
        if(s!!.isEmpty()) return
        val editText = editTextWeakRef.get() ?: return
        editText.removeTextChangedListener(this)

        val parsed = parseToBigDecimal(s.toString())
        val formatted = NumberFormat.getCurrencyInstance(locale).format(parsed)
        //Remove o símbolo da moeda e espaçamento pra evitar bug
        val replaceable = String.format("[%s\\s]", getCurrencySymbol())
        val cleanString = formatted.replace(replaceable.toRegex(), "")

        editText.setText(cleanString)
        editText.setSelection(cleanString.length)
        editText.addTextChangedListener(this)
    }

    private fun parseToBigDecimal(value: String): BigDecimal? {
        val replaceable = String.format("[%s,.\\s]", getCurrencySymbol())
        val cleanString = value.replace(replaceable.toRegex(), "")
        return try {
            BigDecimal(cleanString).setScale(
                2, RoundingMode.FLOOR
            ).divide(BigDecimal(100), RoundingMode.FLOOR)
        } catch (e: NumberFormatException) {
            //ao apagar todos valores de uma só vez dava erro
            //Com a exception o valor retornado é 0.00
            BigDecimal(0)
        }
    }

    companion object {

        /**
         * Format a price string for saving into a database.
         * - Example: price = $ 10,100.55
         * - Return: 10100.55 for database storage
         */
        @JvmStatic
        fun String.formatPriceSave(): String {
            val replaceable = String.format("[%s,.\\s]", getCurrencySymbol())
            val cleanString = this.replace(replaceable.toRegex(), "")
            val stringBuilder = StringBuilder(cleanString.replace(" ".toRegex(), ""))
            return stringBuilder.insert(cleanString.length - 2, '.').toString()
        }

        private fun getCurrencySymbol(): String? {
            return Objects.requireNonNull(NumberFormat.getCurrencyInstance(Locale.getDefault()).currency).symbol
        }

        /**
         * Format a BigDecimal to show with two decimal places.
         * - Example: price = 200.8
         * - Return: 200.80
         */
        @JvmStatic
        fun BigDecimal.formatPriceShow(): String {
            return this.setScale(2, RoundingMode.HALF_EVEN).toPlainString()
        }

        /**
         * Format a price string to show with two decimal places.
         * - Example: price = 2222
         * - Return: 2222.00
         */
        @JvmStatic
        fun formatPrice(price: String?): String? {
            val df = DecimalFormat("0.00")
            return df.format(java.lang.Double.valueOf(price)).toString()
        }

        /**
         * Format an integer to show with two decimal places.
         * - Example: price = 5390
         * - Return: 5390.00
         */
        @JvmStatic
        fun Int.formatIntShow(): String {
            val df = DecimalFormat("0.00")
            return df.format(Integer.valueOf(this)).toString()
        }

        /**
         * Unmask a text formatted as a price to an integer format.
         *
         * Example: price = 5390.00
         * Return: 5390
         */
        @JvmStatic
        fun String.formatIntSave(): String {
            return this.substringBefore(".").replace(",", "")
        }

    }

}