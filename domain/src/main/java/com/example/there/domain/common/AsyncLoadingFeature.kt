package com.example.there.domain.common

import com.badoo.mvicore.element.Actor
import com.badoo.mvicore.element.NewsPublisher
import com.badoo.mvicore.element.Reducer
import com.badoo.mvicore.feature.ActorReducerFeature
import io.reactivex.Observable
import io.reactivex.ObservableTransformer


open class AsyncLoadingFeature<Input : Any, Value : Any, Result : Any>(
        initialState: State<Value>,
        actor: ActorImpl<Input, Value>,
        reducer: ReducerImpl<Value, Result>,
        newsPublisher: NewsPublisherImpl<Input, Value> = NewsPublisherImpl()
) : ActorReducerFeature<AsyncLoadingFeature.LoadWish<Input>, AsyncLoadingFeature.Effect, AsyncLoadingFeature.State<Value>, AsyncLoadingFeature.ErrorNews>(
        initialState = initialState,
        actor = actor,
        reducer = reducer,
        newsPublisher = newsPublisher
) {
    class LoadWish<Input>(val input: Input)

    data class State<Value : Any>(val isLoading: Boolean, val value: Value)

    open class ErrorNews(val throwable: Throwable)

    sealed class Effect {
        object StartedLoading : Effect()
        data class Loaded<Result : Any>(val result: Result) : Effect()
        data class ErrorLoading(val throwable: Throwable) : Effect()
    }

    open class ReducerImpl<Value : Any, Result : Any>(
            private val mapToStateValue: Result.() -> Value
    ) : Reducer<State<Value>, Effect> {

        @Suppress("UNCHECKED_CAST")
        override fun invoke(
                state: State<Value>,
                effect: Effect
        ): State<Value> = when (effect) {
            is Effect.StartedLoading -> state.copy(isLoading = true)
            is Effect.Loaded<*> -> state.copy(
                    isLoading = false,
                    value = (effect.result as Result).mapToStateValue()
            )
            is Effect.ErrorLoading -> state.copy(isLoading = false)
        }
    }

    open class ActorImpl<Input : Any, Value : Any>(
            private val transformer: ObservableTransformer<Effect, Effect>,
            private val mapInputToEffect: Input.() -> Observable<out Effect>
    ) : Actor<State<Value>, LoadWish<Input>, Effect> {

        override fun invoke(
                state: State<Value>,
                wish: LoadWish<Input>
        ): Observable<out Effect> = wish.input.mapInputToEffect()
                .compose(transformer)
                .startWith(Effect.StartedLoading)
                .onErrorReturn { Effect.ErrorLoading(it) }
    }

    open class NewsPublisherImpl<Input : Any, Value : Any> : NewsPublisher<LoadWish<Input>, Effect, State<Value>, ErrorNews> {

        override fun invoke(
                wish: LoadWish<Input>,
                effect: Effect,
                state: State<Value>
        ): ErrorNews? = when (effect) {
            is Effect.ErrorLoading -> ErrorNews(effect.throwable)
            else -> null
        }
    }
}