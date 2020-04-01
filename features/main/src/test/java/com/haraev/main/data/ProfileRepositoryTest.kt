package com.haraev.main.data

import com.haraev.core.data.SessionLocalDataSource
import com.haraev.main.data.api.MainService
import com.haraev.main.data.model.request.DeleteSessionBody
import com.haraev.main.data.model.response.AccountDetailsResponse
import com.haraev.main.data.model.response.DeleteSessionResponse
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import io.reactivex.Single
import okhttp3.ResponseBody.Companion.toResponseBody
import org.assertj.core.api.Assertions.*
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature
import retrofit2.Response
import java.net.HttpURLConnection

object ProfileRepositoryTest : Spek({

    Feature("profile repository") {

        lateinit var profileRepositoryImpl: ProfileRepositoryImpl

        Scenario("getAccountDetails with successful result") {

            //region Fields
            val name = "Иванов Иван"
            val username = "vano123"
            val thisSessionId = "17d3a37a679d07ecf27ce31f6a1eab75fd638cf5"

            val getAccountDetailsResponse: Response<AccountDetailsResponse> = Response.success(
                HttpURLConnection.HTTP_OK,
                AccountDetailsResponse(
                    name = name,
                    username = username
                )
            )

            val mainService = mock<MainService> {
                on { getAccountDetails(thisSessionId) } doReturn Single.just(getAccountDetailsResponse)
            }

            val sessionLocalDataSource = mock<SessionLocalDataSource> {
                on { sessionId } doReturn thisSessionId
                on { userLogin } doReturn "login"
                on { userPassword } doReturn "password"
                on { requireSessionId() } doReturn thisSessionId
            }

            lateinit var getAccountDetailsResult: AccountDetailsResponse
            //endregion

            Given("profile repository") {

                profileRepositoryImpl = ProfileRepositoryImpl(
                    mainService,
                    sessionLocalDataSource
                )

            }

            When("getAccountDetails") {
                getAccountDetailsResult = profileRepositoryImpl.getAccountDetails().blockingGet()
            }

            Then("result should return AccountDetailsResponse with name and username") {
                val actualResult = getAccountDetailsResult

                val expectedResult = AccountDetailsResponse(name, username)

                assertThat(actualResult).isEqualTo(expectedResult)
            }

        }

        Scenario("getAccountDetails with server error result") {

            //region Fields
            val thisSessionId = "17d3a37a679d07ecf27ce31f6a1eab75fd638cf5"

            val networkExceptionCode = 7
            val networkExceptionMessage = "Invalid API key: You must be granted a valid key."

            val getAccountDetailsResponse: Response<AccountDetailsResponse> = Response.error(
                HttpURLConnection.HTTP_UNAUTHORIZED,
                """
                    {
                        "status_code": $networkExceptionCode,
                        "status_message": "$networkExceptionMessage"
                    }
                """.toResponseBody()
            )

            val mainService = mock<MainService> {
                on { getAccountDetails(thisSessionId) } doReturn Single.just(getAccountDetailsResponse)
            }

            val sessionLocalDataSource = mock<SessionLocalDataSource> {
                on { sessionId } doReturn thisSessionId
                on { userLogin } doReturn "login"
                on { userPassword } doReturn "password"
                on { requireSessionId() } doReturn thisSessionId
            }

            lateinit var getAccountDetailsResult: Single<AccountDetailsResponse>
            //endregion

            Given("profile repository") {

                profileRepositoryImpl = ProfileRepositoryImpl(
                    mainService,
                    sessionLocalDataSource
                )

            }

            When("getAccountDetails") {
                getAccountDetailsResult = profileRepositoryImpl.getAccountDetails()
            }

            Then("result should throw IllegalStateException with message") {

                assertThatThrownBy { getAccountDetailsResult.blockingGet() }
                    .isInstanceOf(IllegalStateException::class.java)
                    .hasMessage("Пустое тело ответа")
            }

        }

        Scenario("logout with successful result") {

            //region Fields
            val thisSessionId = "17d3a37a679d07ecf27ce31f6a1eab75fd638cf5"

            val logoutResponse: Response<DeleteSessionResponse> = Response.success(
                HttpURLConnection.HTTP_OK,
                DeleteSessionResponse(
                    success = true
                )
            )

            val mainService = mock<MainService> {
                on { deleteSession(DeleteSessionBody(thisSessionId)) } doReturn Single.just(logoutResponse)
            }

            val sessionLocalDataSource = mock<SessionLocalDataSource> {
                on { sessionId } doReturn thisSessionId
                on { userLogin } doReturn "login"
                on { userPassword } doReturn "password"
                on { requireSessionId() } doReturn thisSessionId
            }

            var logoutResult: Throwable? = null
            //endregion

            Given("profile repository") {

                profileRepositoryImpl = ProfileRepositoryImpl(
                    mainService,
                    sessionLocalDataSource
                )
            }

            When("logout") {
                logoutResult = profileRepositoryImpl.logout().blockingGet()
            }

            Then("result should be null") {
                val actualResult = logoutResult

                assertThat(actualResult).isNull()
            }

        }

        Scenario("logout with server error result") {

            //region Fields
            val thisSessionId = "17d3a37a679d07ecf27ce31f6a1eab75fd638cf5"

            val networkExceptionCode = 7
            val networkExceptionMessage = "Invalid API key: You must be granted a valid key."

            val logoutResponse: Response<DeleteSessionResponse> = Response.error(
                HttpURLConnection.HTTP_UNAUTHORIZED,
                """
                    {
                        "status_code": $networkExceptionCode,
                        "status_message": "$networkExceptionMessage"
                    }
                """.toResponseBody()
            )

            val mainService = mock<MainService> {
                on { deleteSession(DeleteSessionBody(thisSessionId)) } doReturn Single.just(logoutResponse)
            }

            val sessionLocalDataSource = mock<SessionLocalDataSource> {
                on { sessionId } doReturn thisSessionId
                on { userLogin } doReturn "login"
                on { userPassword } doReturn "password"
                on { requireSessionId() } doReturn thisSessionId
            }

            var logoutResult: Throwable? = null
            //endregion

            Given("profile repository") {

                profileRepositoryImpl = ProfileRepositoryImpl(
                    mainService,
                    sessionLocalDataSource
                )
            }

            When("logout") {
                logoutResult = profileRepositoryImpl.logout().blockingGet()
            }

            Then("result should be exception") {
                val actualResult = logoutResult

                assertThat(actualResult).isInstanceOf(IllegalStateException::class.java)
            }

        }

    }
})