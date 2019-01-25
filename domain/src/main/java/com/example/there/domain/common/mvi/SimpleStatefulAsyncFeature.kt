package com.example.there.domain.common.mvi

import com.badoo.mvicore.element.Actor
import com.badoo.mvicore.element.NewsPublisher
import com.badoo.mvicore.element.Reducer
import com.badoo.mvicore.feature.ActorReducerFeature
import io.reactivex.Observable
import io.reactivex.ObservableTransformer


open class SimpleStatefulAsyncFeature<Input : Any, Value : Any, Result : Any>(
        initialState: AsyncState<Value>,
        actor: ActorImpl<Input, Value>,
        reducer: ReducerImpl<Value, Result>,
        newsPublisher: NewsPublisherImpl<Input, Value> = NewsPublisherImpl()
) : ActorReducerFeature<LoadAsyncWish<Input>, SimpleStatefulAsyncFeature.Effect, AsyncState<Value>, ErrorNews>(
        initialState = initialState,
        actor = actor,
        reducer = reducer,
        newsPublisher = newsPublisher
) {

    sealed class Effect {
        object StartedLoading : Effect()
        data class Loaded<Result : Any>(val result: Result) : Effect()
        data class ErrorLoading(val throwable: Throwable) : Effect()
    }

    open class ReducerImpl<Value : Any, Result : Any>(
            private val mapToStateValue: Result.() -> Value
    ) : Reducer<AsyncState<Value>, Effect> {

        @Suppress("UNCHECKED_CAST")
        override fun invoke(
                previousState: AsyncState<Value>,
                effect: Effect
        ): AsyncState<Value> = when (effect) {
            is Effect.StartedLoading -> previousState.copy(isLoading = true)
            is Effect.Loaded<*> -> previousState.copy(
                    isLoading = false,
                    value = (effect.result as Result).mapToStateValue()
            )
            is Effect.ErrorLoading -> previousState.copy(isLoading = false)
        }
    }

    open class ActorImpl<Input : Any, Value : Any>(
            private val transformer: ObservableTransformer<Effect, Effect>,
            private val mapInputToEffect: Input.() -> Observable<out Effect>
    ) : Actor<AsyncState<Value>, LoadAsyncWish<Input>, Effect> {

        override fun invoke(
                previousState: AsyncState<Value>,
                wish: LoadAsyncWish<Input>
        ): Observable<out Effect> = wish.input.mapInputToEffect()
                .compose(transformer)
                .startWith(Effect.StartedLoading)
                .onErrorReturn { Effect.ErrorLoading(it) }
    }

    open class NewsPublisherImpl<Input : Any, Value : Any> : NewsPublisher<LoadAsyncWish<Input>, Effect, AsyncState<Value>, ErrorNews> {

        override fun invoke(
                wish: LoadAsyncWish<Input>,
                effect: Effect,
                state: AsyncState<Value>
        ): ErrorNews? = when (effect) {
            is Effect.ErrorLoading -> ErrorNews(effect.throwable)
            else -> null
        }
    }
}