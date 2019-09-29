package createuser

import domain.entities.UserToCreate
import repository.UserRepositoryCrud
import javax.validation.Validation

class UseCase(private val userRepo: UserRepositoryCrud, private val passwordEncoder: PasswordEncoder) {

    private val validator = Validation.buildDefaultValidatorFactory().validator

    fun createUser(user: UserToCreate) {
        validator.validate(user).apply {
            if (isNotEmpty()) throw InvalidUserException(this)
        }

        userRepo.create(user.copy(password = passwordEncoder.encode(user.password)))
    }
}
