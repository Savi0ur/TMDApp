package com.haraev.tmdapp.ui

import com.haraev.core.data.LocalUserDataSource
import com.haraev.core.ui.Event
import com.haraev.test.aac.disableTestMode
import com.haraev.test.aac.enableTestMode
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature
import org.assertj.core.api.Assertions.*
import java.util.*

object MainViewModelTest : Spek({

    //region Fields and functions
    beforeGroup {
        enableTestMode()
    }

    afterGroup {
        disableTestMode()
    }
    //endregion

    Feature("mainViewModel") {

        Scenario("launch while session id is null") {

            //region Fields
            lateinit var mainViewModel: MainViewModel

            lateinit var localUserDataSource: LocalUserDataSource
            //endregion

            Given("session local data source with session id null") {
                localUserDataSource = mock {
                    on { sessionId } doReturn (null)
                }
            }

            When("init MainViewModel") {
                mainViewModel = MainViewModel(localUserDataSource)
            }

            Then("ui event should be OpenLoginScreen") {
                val uiCommand = mainViewModel.eventsQueue.value

                val expectedState = LinkedList<Event>().apply { add(MainEvents.OpenLoginScreen) }

                assertThat(uiCommand).isEqualTo(expectedState)
            }

        }

        Scenario("launch while session id is not null") {

            //region Fields
            lateinit var mainViewModel: MainViewModel

            lateinit var localUserDataSource: LocalUserDataSource
            //endregion

            Given("session local data source with session id not null") {
                val sId = "17d3a37a679d07ecf27ce31f6a1eab75fd638cf5"
                localUserDataSource = mock {
                    on { sessionId } doReturn (sId)
                }
            }

            When("init MainViewModel") {
                mainViewModel = MainViewModel(localUserDataSource)
            }

            Then("ui event should be OpenSearchScreen") {
                val uiCommand = mainViewModel.eventsQueue.value

                val expectedState = LinkedList<Event>().apply { add(MainEvents.OpenSearchScreen) }

                assertThat(uiCommand).isEqualTo(expectedState)
            }

        }

    }
})