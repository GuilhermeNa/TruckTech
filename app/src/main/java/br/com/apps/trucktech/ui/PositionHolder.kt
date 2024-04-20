package br.com.apps.trucktech.ui

class PositionHolder<T>(initialPos: T) {

    private var newPos: T = initialPos

    private var lastPos: T? = null

    fun newPositionHaveBeenSelected(
        newPos: T,
        positions: (lastPos: T?, newPos: T) -> Unit = { _, _ -> }
    ) {
        this.newPos = newPos
        if(lastPos != newPos) {
            positions(lastPos, newPos)
            lastPos = newPos
        }
    }

    fun updateLastPos(position: T) {
        lastPos = position
    }


}