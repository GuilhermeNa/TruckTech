package br.com.apps.trucktech.util

import androidx.lifecycle.MutableLiveData
import br.com.apps.repository.util.Response
import br.com.apps.trucktech.util.state.State

fun <T> Response<T>.buildUiResponse(state: MutableLiveData<State>, data: MutableLiveData<T>) {
    when (this) {
        is Response.Error -> state.value = State.Error(this.exception)

        is Response.Success ->
            state.value =
                this.data?.let { receivedData ->
                    if (receivedData is List<*> && receivedData.isEmpty()) {
                        State.Empty
                    } else {
                        data.value = receivedData
                        State.Loaded
                    }
                } ?: State.Error(NullPointerException())
    }
}