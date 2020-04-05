package com.haraev.authentication.data

import com.haraev.core.data.SessionLocalDataSource
import com.haraev.core.data.api.LoginService
import com.haraev.core.data.exception.NetworkExceptionType
import com.haraev.core.data.exception.ResponseException
import com.haraev.core.data.model.response.SessionResponse
import com.haraev.core.data.model.response.TokenResponse
import com.nhaarman.mockitokotlin2.*
import io.reactivex.Single
import okhttp3.ResponseBody.Companion.toResponseBody
import org.assertj.core.api.Assertions.assertThat
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature
import retrofit2.Response
import java.net.HttpURLConnection

object LoginRepositoryTest : Spek({

    Feature("login") {

        lateinit var loginRepositoryImpl: LoginRepositoryImpl

        Scenario("successful login") {

            //region Fields
            val login = "login"
            val password = "password"
            val newSessionId = "17d3a37a679d07ecf27ce31f6a1eab75fd638cf5"

            val getNewTokenResponse: Response<TokenResponse> = Response.success(
                HttpURLConnection.HTTP_OK,
                TokenResponse(
                    isSuccess = true,
                    requestToken = "5c20e16fe5f9b9416f07419b08445d5a990ca2a4"
                )
            )

            val validateWithLoginResponse: Response<TokenResponse> =
                Response.success(
                    HttpURLConnection.HTTP_OK,
                    TokenResponse(
                        isSuccess = true,
                        requestToken = "5c20e16fe5f9b9416f07419b08445d5a990ca2a4"
                    )
                )

            val getNewSessionResponse: Response<SessionResponse> =
                Response.success(
                    HttpURLConnection.HTTP_OK,
                    SessionResponse(
                        isSuccess = true,
                        sessionId = newSessionId
                    )
                )

            val loginService = mock<LoginService> {
                on { getNewToken() } doReturn Single.just(getNewTokenResponse)
                on { validateWithLogin(any()) } doReturn Single.just(validateWithLoginResponse)
                on { getNewSession(any()) } doReturn Single.just(getNewSessionResponse)
            }

            val sessionLocalDataSource = mock<SessionLocalDataSource> {
                on { sessionId } doReturn null
                on { userLogin } doReturn null
                on { userPassword } doReturn null
            }

            var loginResult : Throwable? = null
            //endregion

            Given("login repository") {
                loginRepositoryImpl = LoginRepositoryImpl(
                    loginService,
                    sessionLocalDataSource
                )
            }

            When("login") {
               loginResult = loginRepositoryImpl.login(login, password).blockingGet()
            }

            Then("result should be null") {

                assertThat(loginResult).isNull()
            }

            And("sessionId should save in sessionLocalDataSource") {
                verify(sessionLocalDataSource).sessionId = newSessionId
            }

            And("userLogin should save in sessionLocalDataSource") {
                verify(sessionLocalDataSource).userLogin = login
            }

            And("userPassword should save in sessionLocalDataSource") {
                verify(sessionLocalDataSource).userPassword = password
            }

        }

        Scenario("login with wrong login data") {

            //region Fields
            val login = "wrong_login"
            val password = "wrong_password"

            val networkExceptionCode = NetworkExceptionType.INVALID_LOGIN_CREDENTIALS.code
            val networkExceptionMessage = "Invalid username and/or password: You did not provide a valid login."

            val getNewTokenResponse: Response<TokenResponse> = Response.success(
                HttpURLConnection.HTTP_OK,
                TokenResponse(
                    isSuccess = true,
                    requestToken = "5c20e16fe5f9b9416f07419b08445d5a990ca2a4"
                )
            )

            val validateWithLoginResponse: Response<TokenResponse> =
                Response.error(
                    HttpURLConnection.HTTP_UNAUTHORIZED,
                    """
                    {
                        "status_code": $networkExceptionCode,
                        "status_message": "$networkExceptionMessage"
                    }
                """.toResponseBody()
                )

            val loginService = mock<LoginService> {
                on { getNewToken() } doReturn Single.just(getNewTokenResponse)
                on { validateWithLogin(any()) } doReturn Single.just(validateWithLoginResponse)
            }

            val sessionLocalDataSource = mock<SessionLocalDataSource>()

            var loginResult : Throwable? = null
            //endregion

            Given("login repository") {
                loginRepositoryImpl = LoginRepositoryImpl(
                    loginService,
                    sessionLocalDataSource
                )
            }

            When("login") {
               loginResult = loginRepositoryImpl.login(login, password).blockingGet()
            }

            Then("result should be ResponseException") {

                val expectedResult = ResponseException()

                assertThat(loginResult).isEqualTo(expectedResult)
            }

            And("should be zero interactions with sessionLocalDataSource") {
                verifyZeroInteractions(sessionLocalDataSource)
            }

        }

    }

})