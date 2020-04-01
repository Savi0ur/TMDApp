package com.haraev.main.data

import com.haraev.main.data.api.MainService
import com.haraev.main.data.model.response.AccountDetailsResponse
import com.haraev.test.retofit.getTestRetrofit
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import io.reactivex.Completable
import io.reactivex.Single
import okhttp3.mockwebserver.Dispatcher
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okhttp3.mockwebserver.RecordedRequest
import org.assertj.core.api.Assertions.assertThat
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature
import java.net.HttpURLConnection

object ProfileRepositoryImplTest : Spek({

    Feature("profile repository") {

        //region Fields and functions
        lateinit var mockServer: MockWebServer

        lateinit var profileRepositoryImpl: ProfileRepositoryImpl

        val sessionIdResult = "17d3a37a679d07ecf27ce31f6a1eab75fd638cf5"

        beforeEachScenario {

            mockServer = MockWebServer()
                .apply {
                    start()
                }

            val baseUrl = mockServer.url("/")

            val mainService = getTestRetrofit(baseUrl)
                .create(MainService::class.java)

            profileRepositoryImpl = ProfileRepositoryImpl(
                mainService,
                mock {
                    on { sessionId } doReturn (sessionIdResult)
                    on { userLogin } doReturn ("login")
                    on { userPassword } doReturn ("password")
                }
            )
        }

        afterEachScenario {
            mockServer.shutdown()
        }
        //endregion

        Scenario("getAccountDetails with 200 response") {

            //region Fields
            val name = "Иванов Иван"
            val username = "vano123"

            val accountDetailUrlPath = "/account?session_id=$sessionIdResult"
            val accountDetailsResponseCode = HttpURLConnection.HTTP_OK
            val accountDetailsResponseBody = """
                {
                  "avatar": {
                    "gravatar": {
                      "hash": "95c249bb7fa142429dfc484ec01fc697"
                    }
                  },
                  "id": 9184864,
                  "iso_639_1": "en",
                  "iso_3166_1": "US",
                  "name": "$name",
                  "include_adult": false,
                  "username": "$username"
                }
            """.trimIndent()

            lateinit var getAccountDetailsResult: Single<AccountDetailsResponse>
            //endregion

            Given("dispatcher with 200 response") {

                //region Dispatcher
                mockServer.dispatcher = object : Dispatcher() {
                    override fun dispatch(request: RecordedRequest): MockResponse {
                        when (request.path) {
                            accountDetailUrlPath -> return MockResponse()
                                .setResponseCode(accountDetailsResponseCode)
                                .setBody(accountDetailsResponseBody)
                        }

                        return MockResponse().setResponseCode(HttpURLConnection.HTTP_NOT_FOUND)
                    }
                }
                //endregion

            }

            When("getAccountDetails") {
                getAccountDetailsResult = profileRepositoryImpl.getAccountDetails()
            }

            Then("result should return AccountDetailsResponse with name and username") {
                val actualResult = getAccountDetailsResult.blockingGet()

                val expectedResult = AccountDetailsResponse(name, username)

                assertThat(actualResult).isEqualTo(expectedResult)
            }

        }

        Scenario("logout with 200 response") {

            //region Fields

            val logoutUrlPath = "/authentication/session"
            val logoutResponseCode = HttpURLConnection.HTTP_OK
            val logoutResponseBody = """
                {
                  "success": true
                }
            """.trimIndent()

            lateinit var logoutResult: Completable
            //endregion

            Given("dispatcher with 200 response") {

                //region Dispatcher
                mockServer.dispatcher = object : Dispatcher() {
                    override fun dispatch(request: RecordedRequest): MockResponse {
                        when (request.path) {
                            logoutUrlPath -> return MockResponse()
                                .setResponseCode(logoutResponseCode)
                                .setBody(logoutResponseBody)
                        }

                        return MockResponse().setResponseCode(HttpURLConnection.HTTP_NOT_FOUND)
                    }
                }
                //endregion

            }

            When("logout") {
                logoutResult = profileRepositoryImpl.logout()
            }

            Then("result should be null") {
                val actualResult = logoutResult.blockingGet()

                assertThat(actualResult).isNull()
            }

        }

    }

})