package com.haraev.authentication.presentation

import com.haraev.authentication.R
import com.haraev.authentication.TestThreadScheduler
import com.haraev.authentication.data.LoginRepositoryImpl
import com.haraev.authentication.disableTestMode
import com.haraev.authentication.domain.repository.LoginRepository
import com.haraev.authentication.domain.usecase.LoginUseCase
import com.haraev.authentication.enableTestMode
import com.haraev.core.data.api.ErrorHandlingInterceptor
import com.haraev.core.data.api.LoginService
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import com.squareup.moshi.Moshi
import io.reactivex.Completable
import io.reactivex.schedulers.TestScheduler
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.Dispatcher
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okhttp3.mockwebserver.RecordedRequest
import org.assertj.core.api.Assertions.assertThat
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
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

        val loginViewModel = LoginViewModel(
            loginUseCase,
            TestThreadScheduler(testScheduler)
        )

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

                val exceptedState = LoginViewState(enterButtonEnable = true)

                assertThat(uiState).isEqualTo(exceptedState)
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

                val exceptedState = LoginViewState(enterButtonEnable = false)

                assertThat(uiState).isEqualTo(exceptedState)
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

                val exceptedState = LoginViewState(enterButtonEnable = false)

                assertThat(uiState).isEqualTo(exceptedState)
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

                val exceptedState = LoginViewState(
                    progressBarVisibility = true,
                    enterButtonEnable = false,
                    loginAndPasswordFieldsEnable = false
                )

                assertThat(uiState).isEqualTo(exceptedState)
            }

        }

    }

    Feature("enterButtonClicked") {

        //region Fields and functions
        lateinit var mockServer: MockWebServer

        lateinit var loginService: LoginService

        lateinit var loginRepository: LoginRepository

        lateinit var loginViewModel: LoginViewModel

        beforeEachScenario {
            enableTestMode()

            mockServer = MockWebServer()
                .apply {
                    start()
                }

            val moshi = Moshi.Builder().build()

            val errorInterceptor = ErrorHandlingInterceptor(moshi)

            val client = OkHttpClient.Builder()
                .addInterceptor(errorInterceptor)
                .build()

            loginService = Retrofit.Builder()
                .baseUrl(mockServer.url("/"))
                .client(client)
                .addConverterFactory(MoshiConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
                .create(LoginService::class.java)

            loginRepository = LoginRepositoryImpl(
                loginService,
                mock()
            )

            loginViewModel = LoginViewModel(
                loginUseCase = LoginUseCase(
                    loginRepository
                ),
                scheduler = TestThreadScheduler()
            )
        }

        afterEachScenario {
            disableTestMode()

            mockServer.shutdown()
        }
        //endregion

        Scenario("enterButtonClicked with wrong login data") {

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

            When("click enter button") {
                loginViewModel.enterButtonClicked(login, password)
            }

            Then("ui state should show wrong_login_or_password error message") {
                val uiState = loginViewModel.uiState.value

                val exceptedState = LoginViewState(
                    progressBarVisibility = false,
                    enterButtonEnable = true,
                    loginAndPasswordFieldsEnable = true,
                    errorMessage = R.string.wrong_login_or_password
                )

                assertThat(uiState).isEqualTo(exceptedState)
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
                loginViewModel.enterButtonClicked(login, password)
            }

            Then("ui state should show default state with enabled enter button") {
                val uiState = loginViewModel.uiState.value

                val exceptedState = LoginViewState(enterButtonEnable = true)

                assertThat(uiState).isEqualTo(exceptedState)
            }

            And("ui command should be NavigateToNextScreen") {
                val uiCommand = loginViewModel.uiCommand.value

                val exceptedCommand = LoginViewCommand.NavigateToNextScreen

                assertThat(uiCommand).isEqualTo(exceptedCommand)
            }

        }

    }
})