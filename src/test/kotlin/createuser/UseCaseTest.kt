package createuser

import domain.UserEntity
import domain.UserRepositoryCrud
import io.mockk.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

@DisplayName("Create user use case")
object UseCaseTest {

    @Test
    fun `GIVEN a valid user, WHEN running the use case, THEN it calls the repo`() {
        val user = UserEntity(email = "lsoares@gmail.com", name = "Luís Soares", password = "toEncode")
        val repository = mockk<UserRepositoryCrud> {
            every { save(user.copy(password = "encoded")) } just Runs
        }
        val passwordEncoder = mockk<PasswordEncoder> {
            every { encode("toEncode") } returns "encoded"
        }

        UseCase(repository, passwordEncoder).createUser(user)

        verify(exactly = 1) { passwordEncoder.encode("toEncode") }
        verify(exactly = 1) { repository.save(user.copy(password = "encoded")) }
    }

    @Test
    fun `GIVEN an invalid user, WHEN running the use case, THEN it throws exception and does not call the repo`() {
        val repository = mockk<UserRepositoryCrud>()

        assertThrows<InvalidUserException> {
            UseCase(repository, PasswordEncoder())
                .createUser(UserEntity(email = "lsoares", name = "L", password = "1"))
        }

        verify { repository wasNot Called }
    }
}