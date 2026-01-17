package data.repository

import domain.repository.AuthRepository
import domain.repository.AuthStatus
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.gotrue.SessionStatus
import io.github.jan.supabase.gotrue.auth
import io.github.jan.supabase.gotrue.providers.builtin.Email
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put

class AuthRepositoryImpl(
    private val supabase: SupabaseClient
) : AuthRepository {

    override val authStatus: Flow<AuthStatus> = supabase.auth.sessionStatus.map { status ->
        when (status) {
            is SessionStatus.Authenticated -> AuthStatus.AUTHENTICATED
            is SessionStatus.NotAuthenticated -> AuthStatus.UNAUTHENTICATED
            else -> AuthStatus.LOADING
        }
    }

    override suspend fun signUp(email: String, password: String, name: String): Result<Unit> {
        return try {
            supabase.auth.signUpWith(Email) {
                this.email = email
                this.password = password
                this.data = buildJsonObject {
                    put("full_name", name)
                }
            }
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun signIn(email: String, password: String): Result<Unit> {
        return try {
            supabase.auth.signInWith(Email) {
                this.email = email
                this.password = password
            }
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun signOut() {
        supabase.auth.signOut()
    }

    override suspend fun getCurrentUserEmail(): String? {
        return supabase.auth.currentUserOrNull()?.email
    }

    override suspend fun getCurrentUserName(): String? {
        val user = supabase.auth.currentUserOrNull()
        // read JSON metadata saved earlier
        return user?.userMetadata?.get("full_name")?.toString()?.replace("\"", "")
    }
}