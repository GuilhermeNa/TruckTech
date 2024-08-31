package br.com.apps.repository.repository.user

import androidx.lifecycle.LiveData
import br.com.apps.model.model.User
import br.com.apps.repository.util.Response


interface UserInterface: UserReadInterface

interface UserReadInterface {

    suspend fun fetchById(uid: String, flow: Boolean = false): LiveData<Response<User>>

    suspend fun updateRequestNumber(uid: String)

    suspend fun getUserRequestNumber(uid: String): Long

}

