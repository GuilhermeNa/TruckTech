package br.com.apps.trucktech.ui

class PositionHolderTeste<T>(
    var initialPos: T
) {

    private var _actualPos: T = initialPos
    val actualPos get() = _actualPos

    private var _lastPos: T? = null
    val lastPos get() = _lastPos

    fun newPosSelected(
        newPos: T,
        positions: (actual: T?) -> Unit
    ) {
        if (newPos != _actualPos) {
            positions(_actualPos)
            _lastPos = _actualPos
            _actualPos = newPos
        }

    }

}