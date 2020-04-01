package com.haraev.main.presentation.search

import com.haraev.core.aac.Event
import com.haraev.main.R
import com.haraev.main.data.model.Movie
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

            lateinit var movies : List<Movie>
            //endregion

            Given("mock searchUseCase.getMovies() with successful result") {

                movies = listOf(
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


                searchUseCase = mock {
                    on { getMovies(query, 1) } doReturn (Single.just(
                        SearchMoviesResponse(
                            page = 1,
                            movies = movies,
                            totalPages = 1,
                            totalResults = 1
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

            Then("view state should be list with movie") {

                val actualState = searchViewModel.uiState.value

                val expectedResult = SearchViewState(
                    movies
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
                    movies
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
                    null
                )

                assertThat(actualState).isEqualTo(expectedResult)
            }

            And("eventsQueue should show unknown error message") {

                val actualResult = searchViewModel.eventsQueue.value

                val expectedResult =
                    LinkedList<Event>().apply { add(SearchEvents.ErrorMessage(R.string.unknown_error_message)) }

                assertThat(actualResult).isEqualTo(expectedResult)
            }

        }

    }
})