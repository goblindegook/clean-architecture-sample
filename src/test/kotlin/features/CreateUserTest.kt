package features

import domain.EmailAddress
import domain.UserEntity
import domain.UserRepository
import io.mockk.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

@DisplayName("Create user use case")
object CreateUserTest {

    @Test
    fun `GIVEN a valid user, WHEN running the use case, THEN it calls the repo`() {
        val user = UserEntity(email = EmailAddress("lsoares@gmail.com"), name = "Luís Soares", password = "toEncode")
        val repository = mockk<UserRepository> {
            every { save(user.copy(hashedPassword = "encoded")) } just Runs
        }

        CreateUser(repository).execute(user)

        verify(exactly = 1) { repository.save(user.copy(hashedPassword = "encoded")) }
    }
}