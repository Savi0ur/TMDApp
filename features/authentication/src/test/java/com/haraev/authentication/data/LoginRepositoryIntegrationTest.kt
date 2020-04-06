package com.haraev.authentication.data

import com.haraev.authentication.domain.repository.LoginRepository
import com.haraev.core.data.SessionLocalDataSource
import com.haraev.core.data.api.LoginService
import com.haraev.core.data.exception.NetworkException
import com.haraev.core.data.exception.NetworkExceptionType
import com.haraev.test.retofit.getTestRetrofit
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.verifyZeroInteractions
import okhttp3.mockwebserver.Dispatcher
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okhttp3.mockwebserver.RecordedRequest
import org.assertj.core.api.Assertions.*
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature
import java.net.HttpURLConnection

object LoginRepositoryIntegrationTest : Spek({

    Feature("login") {

        //region Fields and functions
        lateinit var mockServer: MockWebServer

        lateinit var loginRepository: LoginRepository

        val sessionLocalDataSource = mock<SessionLocalDataSource> {
            on { sessionId } doReturn null
            on { userLogin } doReturn null
            on { userPassword } doReturn null
        }

        beforeEachScenario {

            mockServer = MockWebServer()
                .apply {
                    start()
                }

            val baseUrl = mockServer.url("/")

            val loginService = getTestRetrofit(baseUrl)
                .create(LoginService::class.java)

            loginRepository = LoginRepositoryImpl(
                loginService,
                sessionLocalDataSource
            )
        }

        afterEachScenario {
            mockServer.shutdown()
        }
        //endregion

        Scenario("login with wrong login data") {

            //region Fields
            val login = "bob123"
            val password = "bob321"
            val tokenUrlPath = "/authentication/token/new"
            val tokenResponseCode = HttpURLConnection.HTTP_OK
            val tokenResponseBody = """
            {
              "success": true,
              "expires_at": "2020-03-24 19:26:41 UTC",
              "request_token": "24fc31271238369c10c5b416e2f033f49072955b"
            }
            """.trimIndent()
            val validateLoginUrlPath = "/authentication/token/validate_with_login"
            val validateLoginResponseCode = HttpURLConnection.HTTP_UNAUTHORIZED
            val validateLoginResponseBody = """
           {
             "status_code": 30,
             "status_message": "Invalid username and/or password: You did not provide a valid login."
           }
            """.trimIndent()
            var loginResult: Throwable? = null
            //endregion


            Given("wrong login and password, 401 response") {

                //region Dispatcher
                mockServer.dispatcher = object : Dispatcher() {
                    override fun dispatch(request: RecordedRequest): MockResponse {
                        when (request.path) {
                            tokenUrlPath -> return MockResponse()
                                .setResponseCode(tokenResponseCode)
                                .setBody(tokenResponseBody)
                            validateLoginUrlPath -> return MockResponse()
                                .setResponseCode(validateLoginResponseCode)
                                .setBody(validateLoginResponseBody)
                        }

                        return MockResponse().setResponseCode(HttpURLConnection.HTTP_NOT_FOUND)
                    }
                }
                //endregion

            }

            When("complete login process") {
                loginResult = loginRepository.login(login, password).blockingGet()
            }

            Then("result should be error with status and message") {

                val expectedResult = NetworkException(
                    NetworkExceptionType.INVALID_LOGIN_CREDENTIALS.code,
                    "Invalid username and/or password: You did not provide a valid login."
                )

                assertThat(loginResult).isEqualTo(expectedResult)
            }

            And("should be zero interactions with sessionLocalDataSource") {
                verifyZeroInteractions(sessionLocalDataSource)
            }

        }

        Scenario("login with correct login data") {

            //region Fields
            val login = "bob123"
            val password = "bob123"
            val newSessionId = "17d3a37a679d07ecf27ce31f6a1eab75fd638cf5"

            val tokenUrlPath = "/authentication/token/new"
            val tokenResponseCode = HttpURLConnection.HTTP_OK
            val tokenResponseBody = """
                {
                  "success": true,
                  "expires_at": "2020-03-25 11:34:30 UTC",
                  "request_token": "5c20e16fe5f9b9416f07419b08445d5a990ca2a4"
                }
            """.trimIndent()
            val validateLoginUrlPath = "/authentication/token/validate_with_login"
            val validateLoginResponseCode = HttpURLConnection.HTTP_OK
            val validateLoginResponseBody = """
                {
                  "success": true,
                  "expires_at": "2020-03-25 11:34:30 UTC",
                  "request_token": "5c20e16fe5f9b9416f07419b08445d5a990ca2a4"
                }
            """.trimIndent()
            val newSessionUrlPath = "/authentication/session/new"
            val newSessionResponseCode = HttpURLConnection.HTTP_OK
            val newSessionResponseBody = """
               {
                 "success": true,
                 "session_id": "$newSessionId"
               }
            """.trimIndent()

            var loginResult: Throwable? = null
            //endregion

            Given("correct login and password, 200 response") {

                //region Dispatcher
                mockServer.dispatcher = object : Dispatcher() {
                    override fun dispatch(request: RecordedRequest): MockResponse {
                        when (request.path) {
                            tokenUrlPath -> return MockResponse()
                                .setResponseCode(tokenResponseCode)
                                .setBody(tokenResponseBody)
                            validateLoginUrlPath -> return MockResponse()
                                .setResponseCode(validateLoginResponseCode)
                                .setBody(validateLoginResponseBody)
                            newSessionUrlPath -> return MockResponse()
                                .setResponseCode(newSessionResponseCode)
                                .setBody(newSessionResponseBody)
                        }

                        return MockResponse().setResponseCode(HttpURLConnection.HTTP_NOT_FOUND)
                    }
                }
                //endregion

            }

            When("complete login process") {
                loginResult = loginRepository.login(login, password).blockingGet()
            }

            Then("result shoult be null") {

                assertThat(loginResult).isNull()
            }

            And("sessionId should save in sessionLocalDataSource") {
                verify(sessionLocalDataSource).sessionId = newSessionId
            }

            And("userLogin should save in sessionLocalDataSource") {
                verify(sessionLocalDataSource).userLogin = login
            }

            And("userPassword should save in sessionLocalDataSource") {
                verify(sessionLocalDataSource).userPassword = password
            }

        }

    }

})