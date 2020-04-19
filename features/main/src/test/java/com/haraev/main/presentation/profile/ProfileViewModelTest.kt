package com.haraev.main.presentation.profile

import com.haraev.core.ui.Event
import com.haraev.core.ui.ShowErrorMessage
import com.haraev.main.R
import com.haraev.main.data.model.response.AccountDetailsResponse
import com.haraev.main.domain.usecase.ProfileUseCase
import com.haraev.test.aac.disableTestMode
import com.haraev.test.aac.enableTestMode
import com.haraev.test.rxjava.TestThreadScheduler
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import io.reactivex.Completable
import io.reactivex.Single
import org.assertj.core.api.Assertions.assertThat
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature
import java.util.*

object ProfileViewModelTest : Spek({

    beforeGroup {
        enableTestMode()
    }

    afterGroup {
        disableTestMode()
    }

    Feature("profile view model") {

        Scenario("init profile view model") {

            //region Fields
            lateinit var profileViewModel: ProfileViewModel

            lateinit var profileUseCase: ProfileUseCase

            val name = "Иванов Иван"
            val username = "vano123"
            //endregion

            Given("mock profileUseCase.getAccountDetails with successful result") {

                profileUseCase = mock {
                    on { getAccountDetails() } doReturn (Single.just(
                        AccountDetailsResponse(
                            name,
                            username
                        )
                    ))
                }

            }

            When("init profileViewModel") {

                profileViewModel = ProfileViewModel(
                    profileUseCase,
                    TestThreadScheduler()
                )

            }

            Then("uiState should show name and username") {

                val actualState = profileViewModel.uiState.value

                val expectedResult = ProfileViewState(
                    progressBarVisibility = false,
                    name = name,
                    userName = username
                )

                assertThat(actualState).isEqualTo(expectedResult)
            }

        }

        Scenario("init profile view model with server error") {

            //region Fields
            lateinit var profileViewModel: ProfileViewModel

            lateinit var profileUseCase: ProfileUseCase
            //endregion

            Given("mock profileUseCase.getAccountDetails with error result") {

                profileUseCase = mock {
                    on { getAccountDetails() } doReturn (Single.error(IllegalStateException("server error")))
                }

            }

            When("init profileViewModel") {

                profileViewModel = ProfileViewModel(
                    profileUseCase,
                    TestThreadScheduler()
                )

            }

            Then("eventsQueue should show snackbar with unknown error message") {

                val actualResult = profileViewModel.eventsQueue.value

                val expectedResult =
                    LinkedList<Event>().apply { add(ShowErrorMessage(R.string.unknown_error_message)) }

                assertThat(actualResult).isEqualTo(expectedResult)
            }

        }

        Scenario("exit button clicked") {

            //region Fields
            lateinit var profileViewModel: ProfileViewModel

            lateinit var profileUseCase: ProfileUseCase
            //endregion

            Given("mock profileUseCase.logout with complete result") {

                profileUseCase = mock {
                    on { logout() } doReturn (Completable.complete())
                    on { getAccountDetails() } doReturn (Single.just(
                        AccountDetailsResponse(
                            name = "",
                            username = ""
                        )
                    ))
                }

                profileViewModel = ProfileViewModel(profileUseCase, TestThreadScheduler())

            }

            When("exit button clicked") {

                profileViewModel.exitButtonClicked()

            }

            Then("eventsQueue should be ProfileEvents.Logout") {

                val actualResult = profileViewModel.eventsQueue.value

                val expectedResult =
                    LinkedList<Event>().apply { add(ProfileEvents.Logout) }

                assertThat(actualResult).isEqualTo(expectedResult)

            }

        }

        Scenario("exit button clicked with server error") {

            //region Fields
            lateinit var profileViewModel: ProfileViewModel

            lateinit var profileUseCase: ProfileUseCase
            //endregion

            Given("mock profileUseCase.logout with error result") {

                profileUseCase = mock {
                    on { logout() } doReturn (Completable.error(IllegalStateException("server error")))
                    on { getAccountDetails() } doReturn (Single.just(
                        AccountDetailsResponse(
                            name = "",
                            username = ""
                        )
                    ))
                }

                profileViewModel = ProfileViewModel(profileUseCase, TestThreadScheduler())

            }

            When("exit button clicked") {

                profileViewModel.exitButtonClicked()

            }

            Then("eventsQueue should show unknown error message") {

                val actualResult = profileViewModel.eventsQueue.value

                val expectedResult =
                    LinkedList<Event>().apply { add(ShowErrorMessage(R.string.unknown_error_message)) }

                assertThat(actualResult).isEqualTo(expectedResult)

            }

        }

    }

})