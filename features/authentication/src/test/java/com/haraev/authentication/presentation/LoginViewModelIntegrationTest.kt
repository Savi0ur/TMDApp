package com.haraev.authentication.presentation

import com.haraev.authentication.R
import com.haraev.authentication.data.LoginRepositoryImpl
import com.haraev.authentication.domain.usecase.LoginUseCase
import com.haraev.core.data.api.LoginService
import com.haraev.test.aac.disableTestMode
import com.haraev.test.aac.enableTestMode
import com.haraev.test.retofit.getTestRetrofit
import com.haraev.test.rxjava.TestThreadScheduler
import com.nhaarman.mockitokotlin2.mock
import okhttp3.mockwebserver.Dispatcher
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okhttp3.mockwebserver.RecordedRequest
import org.assertj.core.api.Assertions.*
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature
import java.net.HttpURLConnection

object LoginViewModelIntegrationTest : Spek({

    //region Fields and functions
    beforeGroup {
        enableTestMode()
    }

    afterGroup {
        disableTestMode()
    }
    //endregion

    Feature("loginViewModel") {

        //region Fields and functions
        lateinit var mockServer: MockWebServer

        lateinit var loginViewModel: LoginViewModel

        beforeEachScenario {

            mockServer = MockWebServer()
                .apply {
                    start()
                }

            val baseUrl = mockServer.url("/")

            val loginService = getTestRetrofit(baseUrl)
                .create(LoginService::class.java)

            val loginRepository = LoginRepositoryImpl(
                loginService,
                mock()
            )

            val loginUseCase = LoginUseCase(loginRepository)

            loginViewModel = LoginViewModel(
                loginUseCase,
                TestThreadScheduler()
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

            When("login data changed") {
                loginViewModel.loginDataChanged(login, password)
            }

            And("enter button clicked") {
                loginViewModel.enterButtonClicked(login, password)
            }

            Then("state should show error with wrong login data message") {

                val expectedResult = LoginViewState(
                    progressBarVisibility = false,
                    enterButtonEnable = true,
                    loginAndPasswordFieldsEnable = true,
                    errorMessage = R.string.wrong_login_or_password
                )

                val actualResult = loginViewModel.uiState.value

                assertThat(expectedResult).isEqualTo(actualResult)
            }

            And("command should be null") {

                val actualResult = loginViewModel.uiCommand.value

                assertThat(actualResult).isNull()
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

            When("login data changed") {
                loginViewModel.loginDataChanged(login, password)
            }

            And("enter button clicked") {
                loginViewModel.enterButtonClicked(login, password)
            }

            Then("state should not show error message") {

                val expectedResult = LoginViewState(
                    progressBarVisibility = false,
                    enterButtonEnable = true,
                    loginAndPasswordFieldsEnable = true,
                    errorMessage = null
                )

                val actualResult = loginViewModel.uiState.value

                assertThat(expectedResult).isEqualTo(actualResult)

            }

            And("command should be NavigateToNextScreen") {
                val actualResult = loginViewModel.uiCommand.value

                val expectedResult = LoginEvents.NavigateToNextScreen

                assertThat(actualResult).isEqualTo(expectedResult)
            }

        }

    }
})