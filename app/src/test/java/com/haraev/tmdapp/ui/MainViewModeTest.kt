package com.haraev.tmdapp.ui

import com.haraev.tmdapp.disableTestMode
import com.haraev.tmdapp.enableTestMode
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature
import org.assertj.core.api.Assertions.*

object MainViewModeTest : Spek({
    Feature("navigation commands") {

        //region Fields and functions
        beforeEachScenario {
            enableTestMode()
        }

        afterEachScenario {
            disableTestMode()
        }
        //endregion

        Scenario("launch while session id is null") {

            lateinit var mainViewModel : MainViewModel

            Given("session local data source with session id null") {
                mainViewModel = MainViewModel(
                    sessionLocalDataSource =  mock {
                        on { sessionId } doReturn(null)
                    }
                )
            }

            When("get navigation state") {
                mainViewModel.getNavigationState()
            }

            Then("ui command should be OpenLoginScreen") {
                val uiCommand = mainViewModel.uiCommand.value

                val exceptedState = MainViewCommand.OpenLoginScreen

                assertThat(uiCommand).isEqualTo(exceptedState)
            }

        }

        Scenario("launch while session id is not null") {

            lateinit var mainViewModel : MainViewModel

            Given("session local data source with session id not null") {
                val sId = "17d3a37a679d07ecf27ce31f6a1eab75fd638cf5"
                mainViewModel = MainViewModel(
                    sessionLocalDataSource =  mock {
                        on { sessionId } doReturn(sId)
                    }
                )
            }

            When("get navigation state") {
                mainViewModel.getNavigationState()
            }

            Then("ui command should be OpenSearchScreen") {
                val uiCommand = mainViewModel.uiCommand.value

                val exceptedState = MainViewCommand.OpenSearchScreen

                assertThat(uiCommand).isEqualTo(exceptedState)
            }

        }

    }
})