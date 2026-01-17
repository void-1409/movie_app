package domain.repository

import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    // returns true if a user is currently logged in
    val isUserLoggedIn: Flow<Boolean>

    suspend fun signUp(email: String, password: String, name: String): Result<Unit>
    suspend fun signIn(email: String, password: String): Result<Unit>
    suspend fun signOut()
    suspend fun getCurrentUserEmail(): String?  // helper to show in profile
    suspend fun getCurrentUserName(): String?   // helper to show in profile

    // add getting profile, bookingHistory, etc later
}