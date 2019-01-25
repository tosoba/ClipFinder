package com.example.there.domain.common.mvi

import com.badoo.mvicore.element.Actor
import com.badoo.mvicore.element.NewsPublisher
import com.badoo.mvicore.element.Reducer
import com.badoo.mvicore.feature.ActorReducerFeature
import io.reactivex.Completable
import io.reactivex.CompletableTransformer
import io.reactivex.Observable


open class SimpleStatelessAsyncFeature<Input : Any>(
        initialState: AsyncState<Unit> = AsyncState(false, Unit),
        actor: ActorImpl<Input>,
        reducer: ReducerImpl = ReducerImpl(),
        newsPublisher: NewsPublisherImpl<Input> = NewsPublisherImpl()
) : ActorReducerFeature<LoadAsyncWish<Input>, SimpleStatelessAsyncFeature.Effect, AsyncState<Unit>, SimpleStatelessAsyncFeature.News>(
        initialState = initialState,
        actor = actor,
        reducer = reducer,
        newsPublisher = newsPublisher
) {

    sealed class Effect {
        object StartedLoading : Effect()
        object Loaded : Effect()
        data class ErrorLoading(val throwable: Throwable) : Effect()
    }

    open class ReducerImpl : Reducer<AsyncState<Unit>, Effect> {

        override fun invoke(
                previousState: AsyncState<Unit>,
                effect: Effect
        ): AsyncState<Unit> = when (effect) {
            is Effect.StartedLoading -> previousState.copy(isLoading = true)
            else -> previousState.copy(isLoading = false)
        }
    }

    open class ActorImpl<Input : Any>(
            private val transformer: CompletableTransformer,
            private val mapToCompletable: Input.() -> Completable
    ) : Actor<AsyncState<Unit>, LoadAsyncWish<Input>, Effect> {

        override fun invoke(
                previousState: AsyncState<Unit>,
                wish: LoadAsyncWish<Input>
        ): Observable<out Effect> = wish.input.mapToCompletable()
                .compose(transformer)
                .toSingle<Effect> { Effect.Loaded }
                .toObservable()
                .startWith(Effect.StartedLoading)
                .onErrorReturn { Effect.ErrorLoading(it) }
    }

    sealed class News {
        class Error(val throwable: Throwable) : News()
        object Success : News()
    }

    open class NewsPublisherImpl<Input : Any> : NewsPublisher<LoadAsyncWish<Input>, Effect, AsyncState<Unit>, News> {

        override fun invoke(
                wish: LoadAsyncWish<Input>,
                effect: Effect,
                state: AsyncState<Unit>
        ): News? = when (effect) {
            is Effect.ErrorLoading -> News.Error(effect.throwable)
            is Effect.Loaded -> News.Success
            else -> null
        }
    }
}