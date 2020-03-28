package com.haraev.authentication.data

import com.haraev.authentication.domain.repository.LoginRepository
import com.haraev.core.data.api.LoginService
import com.haraev.core.data.exception.NetworkException
import com.haraev.test.okhttp.getTestRetrofit
import com.nhaarman.mockitokotlin2.mock
import io.reactivex.Completable
import okhttp3.mockwebserver.Dispatcher
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okhttp3.mockwebserver.RecordedRequest
import org.assertj.core.api.Assertions
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature
import java.net.HttpURLConnection

object LoginRepositoryImplTest : Spek({

    Feature("login") {

        //region Fields and functions
        lateinit var mockServer: MockWebServer

        lateinit var loginRepository: LoginRepository

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
                mock()
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
            lateinit var loginResult: Completable
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
                loginResult = loginRepository.login(login, password)
            }

            Then("result should be error with status and message") {

                val expectedResult = NetworkException(
                    30,
                    "Invalid username and/or password: You did not provide a valid login."
                )

                val actualResult = loginResult
                    .blockingGet()

                Assertions.assertThat(expectedResult).isEqualTo(actualResult)
            }

        }

        Scenario("enterButtonClicked with correct login data") {

            //region Fields
            val login = "bob123"
            val password = "bob123"
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
                 "session_id": "17d3a37a679d07ecf27ce31f6a1eab75fd638cf5"
               }
            """.trimIndent()

            lateinit var loginResult: Completable
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

            When("click enter button") {
                loginResult = loginRepository.login(login, password)
            }

            Then("ui state should show default state with enabled enter button") {
                val actualResult = loginResult.blockingGet()

                Assertions.assertThat(actualResult).isNull()
            }

        }

    }

})