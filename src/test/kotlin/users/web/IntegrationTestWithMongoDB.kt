package users.web

import de.flapdoodle.embed.mongo.MongodExecutable
import de.flapdoodle.embed.mongo.MongodProcess
import de.flapdoodle.embed.mongo.MongodStarter
import de.flapdoodle.embed.mongo.config.MongodConfigBuilder
import de.flapdoodle.embed.mongo.config.Net
import de.flapdoodle.embed.mongo.distribution.Version
import de.flapdoodle.embed.process.runtime.Network
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import users.domain.UserRepository
import users.persistence.MongoDBUserRepository

object IntegrationTestWithMongoDB {

    private lateinit var webApp: WebApp
    private lateinit var mongodExe: MongodExecutable
    private lateinit var mongod: MongodProcess
    private lateinit var userRepository: UserRepository

    @BeforeAll
    @JvmStatic
    fun setup() {
        mongodExe = MongodStarter.getDefaultInstance().prepare(
            MongodConfigBuilder()
                .version(Version.Main.PRODUCTION)
                .net(Net("localhost", 12345, Network.localhostIsIPv6()))
                .build()
        )
        mongod = mongodExe.start()
        userRepository = MongoDBUserRepository("localhost", 12345, "db123").apply { createSchema() }
        webApp = WebApp(userRepository, 8081).apply { start() }
    }

    @BeforeEach
    fun beforeEach() {
        (userRepository as MongoDBUserRepository).deleteAll()
    }

    @Test
    fun `GIVEN a user's json, WHEN posting it, THEN it creates a user`() {
        IntegrationTest.`GIVEN a user's json, WHEN posting it, THEN it creates a user`()
    }

    @Test
    fun `GIVEN an existing user's json, WHEN posting it, THEN it creates only the first`() {
        IntegrationTest.`GIVEN an existing user's json, WHEN posting it, THEN it creates only the first`()
    }

    @AfterAll
    @JvmStatic
    fun afterAll() {
        webApp.close()
        mongod.stop()
        mongodExe.stop()
    }
}