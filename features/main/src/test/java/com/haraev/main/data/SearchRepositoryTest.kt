package com.haraev.main.data

import com.haraev.main.data.api.MainService
import com.haraev.main.data.model.Movie
import com.haraev.main.data.model.response.SearchMoviesResponse
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import io.reactivex.Single
import org.assertj.core.api.Assertions
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature
import retrofit2.Response
import java.net.HttpURLConnection

object SearchRepositoryTest : Spek({

    Feature("search repository") {

        lateinit var searchRepositoryImpl: SearchRepositoryImpl

        Scenario("successful search") {

            //region Fields
            val query = "Your lie in april"

            val movies = listOf(
                Movie(
                    null,
                    "overview",
                    "2014-10-10",
                    1,
                    "Shigatsu wa Kimi no Uso",
                    "Your lie in april",
                    101731,
                    8.83
                )
            )

            val searchMoviesResponse = SearchMoviesResponse(
                page = 1,
                movies = movies,
                totalPages = 1,
                totalResults = 1
            )

            val getMoviesResponse: Response<SearchMoviesResponse> = Response.success(
                HttpURLConnection.HTTP_OK,
                searchMoviesResponse
            )

            val mainService = mock<MainService> {
                on { getMovies("ru", query, 1) } doReturn Single.just(getMoviesResponse)
            }

            lateinit var getSearchMoviesResponse: SearchMoviesResponse
            //endregion

            Given("search repository") {

                searchRepositoryImpl = SearchRepositoryImpl(
                    mainService
                )

            }

            When("getMovies") {
                getSearchMoviesResponse = searchRepositoryImpl.getMovies(query, 1).blockingGet()
            }

            Then("result should return movies list with movie") {
                val actualResult = getSearchMoviesResponse

                Assertions.assertThat(actualResult).isEqualTo(searchMoviesResponse)
            }

        }

        Scenario("successful search with no movies found result") {

            //region Fields
            val query = "qazwsx"

            val movies = emptyList<Movie>()

            val searchMoviesResponse = SearchMoviesResponse(
                page = 1,
                movies = movies,
                totalPages = 0,
                totalResults = 0
            )

            val getMoviesResponse: Response<SearchMoviesResponse> = Response.success(
                HttpURLConnection.HTTP_OK,
                searchMoviesResponse
            )

            val mainService = mock<MainService> {
                on { getMovies("ru", query, 1) } doReturn Single.just(getMoviesResponse)
            }

            lateinit var getSearchMoviesResponse: SearchMoviesResponse
            //endregion

            Given("search repository") {

                searchRepositoryImpl = SearchRepositoryImpl(
                    mainService
                )

            }

            When("getMovies") {
                getSearchMoviesResponse = searchRepositoryImpl.getMovies(query, 1).blockingGet()
            }

            Then("result should return empty movies list") {
                val actualResult = getSearchMoviesResponse

                Assertions.assertThat(actualResult).isEqualTo(searchMoviesResponse)
            }

        }
    }

})
