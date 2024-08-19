package br.com.apps.repository.repository.user

class UserRepository(private val read: UserReadImpl): UserInterface {

    override suspend fun getUserRequestNumber(uid: String): Int = read.getUserRequestNumber(uid)

    override suspend fun updateRequestNumber(uid: String) = read.updateRequestNumber(uid)

    override suspend fun fetchById(uid: String, flow: Boolean) = read.fetchById(uid, flow)

}