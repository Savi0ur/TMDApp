package com.haraev.authentication.presentation

import com.haraev.test.rxjava.TestThreadScheduler
import com.haraev.authentication.data.LoginRepositoryImpl
import com.haraev.authentication.domain.repository.LoginRepository
import com.haraev.authentication.domain.usecase.LoginUseCase
import com.haraev.core.data.api.LoginService
import com.haraev.core.data.exception.NetworkException
import com.haraev.test.aac.disableTestMode
import com.haraev.test.aac.enableTestMode
import com.haraev.test.okhttp.getTestRetrofit
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import io.reactivex.Completable
import io.reactivex.schedulers.TestScheduler
import okhttp3.mockwebserver.Dispatcher
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okhttp3.mockwebserver.RecordedRequest
import org.assertj.core.api.Assertions.assertThat
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature
import java.net.HttpURLConnection

object LoginViewModelTest : Spek({

    Feature("onLoginDataChanged") {

        //region Fields and functions
        val loginUseCase = LoginUseCase(
            loginRepository = mock {
                on { login(any(), any()) }.doReturn(Completable.complete())
            }
        )

        val testScheduler = TestScheduler()

        val loginViewModel by memoized {
            LoginViewModel(
                loginUseCase,
                TestThreadScheduler(testScheduler)
            )
        }

        beforeEachScenario {
            enableTestMode()
        }

        afterEachScenario {
            disableTestMode()
        }
        //endregion

        Scenario("onLoginDataChanged while login and password not blank") {

            // region Fields
            var login = ""
            var password = ""
            // endregion

            Given("login and password not blank") {
                login = "bob123"
                password = "bob321"
            }

            When("enter valid credentials") {
                loginViewModel.loginDataChanged(login, password)
            }

            Then("ui state enter button should be true") {
                val uiState = loginViewModel.uiState.value

                val expectedState = LoginViewState(enterButtonEnable = true)

                assertThat(uiState).isEqualTo(expectedState)
            }
        }

        Scenario("onLoginDataChanged while login not blank and password blank") {

            //region Fields
            var login = ""
            var password = ""
            //endregion

            Given("login not blank and password blank") {
                login = "bob123"
                password = "   "
            }

            When("enter not valid credentials") {
                loginViewModel.loginDataChanged(login, password)
            }

            Then("ui state enter button should be false") {
                val uiState = loginViewModel.uiState.value

                val expectedState = LoginViewState(enterButtonEnable = false)

                assertThat(uiState).isEqualTo(expectedState)
            }
        }

        Scenario("onLoginDataChanged while login blank and password not blank") {

            //region Fields
            var login = ""
            var password = ""
            //endregion

            Given("login not blank and password blank") {
                login = "   "
                password = "bob321"
            }

            When("enter not valid credentials") {
                loginViewModel.loginDataChanged(login, password)
            }

            Then("ui state enter button should be false") {
                val uiState = loginViewModel.uiState.value

                val expectedState = LoginViewState(enterButtonEnable = false)

                assertThat(uiState).isEqualTo(expectedState)
            }
        }

        Scenario("onLoginDataChanged and enterButtonClicked while login and password not blank") {

            //region Fields
            var login = ""
            var password = ""
            //endregion

            Given("login and password not blank") {
                login = "bob123"
                password = "bob321"
            }

            When("enter valid credentials") {
                loginViewModel.loginDataChanged(login, password)
            }

            And("click enter button") {
                loginViewModel.enterButtonClicked(login, password)
            }

            Then("show progress bar, disable enter button, disable login and password fields") {
                val uiState = loginViewModel.uiState.value

                val expectedState = LoginViewState(
                    progressBarVisibility = true,
                    enterButtonEnable = false,
                    loginAndPasswordFieldsEnable = false
                )

                assertThat(uiState).isEqualTo(expectedState)
            }

        }

    }

    Feature("enterButtonClicked") {

        //region Fields and functions
        lateinit var mockServer: MockWebServer

        lateinit var loginRepository: LoginRepository

        beforeEachScenario {
            enableTestMode()

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
            disableTestMode()

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

                assertThat(expectedResult).isEqualTo(actualResult)
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

                assertThat(actualResult).isNull()
            }

        }
    }
})