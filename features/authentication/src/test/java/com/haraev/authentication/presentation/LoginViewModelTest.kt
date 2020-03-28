package com.haraev.authentication.presentation

import com.haraev.test.rxjava.TestThreadScheduler
import com.haraev.authentication.domain.usecase.LoginUseCase
import com.haraev.test.aac.disableTestMode
import com.haraev.test.aac.enableTestMode
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import io.reactivex.Completable
import io.reactivex.schedulers.TestScheduler
import org.assertj.core.api.Assertions.assertThat
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature

object LoginViewModelTest : Spek({

    //region Fields and functions
    beforeEachGroup {
        enableTestMode()
    }

    afterEachGroup {
        disableTestMode()
    }
    //endregion

    Feature("loginViewModel") {

        //region Fields and functions
        val loginUseCase = mock<LoginUseCase> {
            on { login(any(), any()) }.doReturn(Completable.complete())
        }

        val testScheduler = TestScheduler()

        val loginViewModel by memoized {
            LoginViewModel(
                loginUseCase,
                TestThreadScheduler(testScheduler)
            )
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
})