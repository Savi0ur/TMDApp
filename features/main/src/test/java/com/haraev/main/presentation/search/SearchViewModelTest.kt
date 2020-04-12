package com.haraev.main.presentation.search

import com.haraev.core.ui.Event
import com.haraev.core.ui.ShowErrorMessage
import com.haraev.main.R
import com.haraev.main.data.model.Genre
import com.haraev.main.data.model.Movie
import com.haraev.main.data.model.response.MovieDetailsResponse
import com.haraev.main.data.model.response.SearchMoviesResponse
import com.haraev.main.domain.usecase.SearchUseCase
import com.haraev.test.aac.disableTestMode
import com.haraev.test.aac.enableTestMode
import com.haraev.test.rxjava.TestThreadScheduler
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import io.reactivex.Observable
import io.reactivex.Single
import org.assertj.core.api.Assertions.*
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature
import java.util.*

object SearchViewModelTest : Spek({

    beforeGroup {
        enableTestMode()
    }

    afterGroup {
        disableTestMode()
    }

    Feature("search view model") {

        Scenario("successful search") {

            //region Fields
            lateinit var searchViewModel: SearchViewModel

            lateinit var searchUseCase: SearchUseCase

            val query = "Your lie in april"

            lateinit var movieDetails: MovieDetailsResponse
            //endregion

            Given("mock searchUseCase.getMovies() with successful result") {

                val posterPath = null
                val overview = "overview"
                val releaseDate = "2014-10-10"
                val serverId = 1
                val originalTitle = "Shigatsu wa Kimi no Uso"
                val title = "Your lie in april"
                val voteCount = 101731
                val voteAverage = 8.83
                val genres = listOf(Genre("драма"))
                val duration = 122

                val movies = listOf(
                    Movie(
                        posterPath = posterPath,
                        overview = overview,
                        releaseDate = releaseDate,
                        serverId = serverId,
                        originalTitle = originalTitle,
                        title = title,
                        voteCount = voteCount,
                        voteAverage = voteAverage
                    )
                )

                movieDetails = MovieDetailsResponse(
                    posterPath = posterPath,
                    overview = overview,
                    releaseDate = releaseDate,
                    serverId = serverId,
                    originalTitle = originalTitle,
                    title = title,
                    voteCount = voteCount,
                    voteAverage = voteAverage,
                    genres = genres,
                    duration = duration
                )


                searchUseCase = mock {
                    on { getMovies(query, 1) } doReturn (Single.just(
                        SearchMoviesResponse(
                            page = 1,
                            movies = movies,
                            totalPages = 1,
                            totalResults = 1
                        )
                    ))
                    on { getMovieDetails(movies[0].serverId) } doReturn (Single.just(
                        movieDetails
                    ))
                }

                searchViewModel = SearchViewModel(
                    searchUseCase,
                    TestThreadScheduler()
                )

            }

            When("search edit text changed") {
                searchViewModel.onSearchEditTextTextChanged(
                    Observable.just(query)
                )
            }

            Then("view state should be list with movie") {

                val actualState = searchViewModel.uiState.value

                val expectedResult = SearchViewState(
                    movies = listOf(movieDetails)
                )

                assertThat(actualState).isEqualTo(expectedResult)
            }

        }

        Scenario("successful search with no movies found result") {

            //region Fields
            lateinit var searchViewModel: SearchViewModel

            lateinit var searchUseCase: SearchUseCase

            val query = "qazwsx"

            val movies = emptyList<Movie>()
            //endregion

            Given("mock searchUseCase.getMovies() with no movies found result") {

                searchUseCase = mock {
                    on { getMovies(query, 1) } doReturn (Single.just(
                        SearchMoviesResponse(
                            page = 1,
                            movies = movies,
                            totalPages = 0,
                            totalResults = 0
                        )
                    ))
                }

                searchViewModel = SearchViewModel(
                    searchUseCase,
                    TestThreadScheduler()
                )

            }

            When("search edit text changed") {
                searchViewModel.onSearchEditTextTextChanged(
                    Observable.just(query)
                )
            }

            Then("view state should show empty movies list") {

                val actualState = searchViewModel.uiState.value

                val expectedResult = SearchViewState(
                    movies = emptyList()
                )

                assertThat(actualState).isEqualTo(expectedResult)
            }

        }

        Scenario("failed search") {

            //region Fields
            lateinit var searchViewModel: SearchViewModel

            lateinit var searchUseCase: SearchUseCase

            val query = "qazwsx"

            //endregion

            Given("mock searchUseCase.getMovies() with server error result") {

                searchUseCase = mock {
                    on { getMovies(query, 1) } doReturn (Single.error(
                        IllegalStateException("server error")
                    ))
                }

                searchViewModel = SearchViewModel(
                    searchUseCase,
                    TestThreadScheduler()
                )

            }

            When("search edit text changed") {
                searchViewModel.onSearchEditTextTextChanged(
                    Observable.just(query)
                )
            }

            Then("view state should show empty movies list") {

                val actualState = searchViewModel.uiState.value

                val expectedResult = SearchViewState(
                    movies = null
                )

                assertThat(actualState).isEqualTo(expectedResult)
            }

            And("eventsQueue should show unknown error message") {

                val actualResult = searchViewModel.eventsQueue.value

                val expectedResult =
                    LinkedList<Event>().apply { add(ShowErrorMessage(R.string.unknown_error_message)) }

                assertThat(actualResult).isEqualTo(expectedResult)
            }

        }

    }
})