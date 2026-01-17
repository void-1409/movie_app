package domain.repository

import kotlinx.coroutines.flow.Flow

enum class AuthStatus {
    LOADING,
    AUTHENTICATED,
    UNAUTHENTICATED
}

interface AuthRepository {
    val authStatus: Flow<AuthStatus>

    suspend fun signUp(email: String, password: String, name: String): Result<Unit>
    suspend fun signIn(email: String, password: String): Result<Unit>
    suspend fun signOut()
    suspend fun getCurrentUserEmail(): String?  // helper to show in profile
    suspend fun getCurrentUserName(): String?   // helper to show in profile

    // add getting profile, bookingHistory, etc later
}