package com.example.core.ext

import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.functions.BiFunction
import java.util.concurrent.TimeUnit

sealed class RetryStrategy(val attempts: Long)
class Times(attempts: Long = 1) : RetryStrategy(attempts)
class WithDelay(val delay: Long, val unit: TimeUnit, attempts: Long = 1) : RetryStrategy(attempts)
class WithVariableDelay(attempts: Long, val unit: TimeUnit, val getDelay: (Int) -> Long) : RetryStrategy(attempts)

fun <T> Single<T>.retry(strategy: RetryStrategy): Single<T> = when (strategy) {
    is Times -> retry(strategy.attempts)
    is WithDelay -> retryWhen { errors ->
        errors.zipWith(Flowable.range(1, strategy.attempts.toInt()), BiFunction<Throwable, Int, Int> { _, attempt -> attempt })
            .flatMap { Flowable.timer(strategy.delay, strategy.unit) }
    }
    is WithVariableDelay -> retryWhen { errors ->
        errors.zipWith(Flowable.range(1, strategy.attempts.toInt()), BiFunction<Throwable, Int, Int> { _, attempt -> attempt })
            .flatMap { attempt -> Flowable.timer(strategy.getDelay(attempt), strategy.unit) }
    }
}

fun <T> Flowable<T>.retry(strategy: RetryStrategy): Flowable<T> = when (strategy) {
    is Times -> retry(strategy.attempts)
    is WithDelay -> retryWhen { errors ->
        errors.zipWith(Flowable.range(1, strategy.attempts.toInt()), BiFunction<Throwable, Int, Int> { _, attempt -> attempt })
            .flatMap { Flowable.timer(strategy.delay, strategy.unit) }
    }
    is WithVariableDelay -> retryWhen { errors ->
        errors.zipWith(Flowable.range(1, strategy.attempts.toInt()), BiFunction<Throwable, Int, Int> { _, attempt -> attempt })
            .flatMap { attempt -> Flowable.timer(strategy.getDelay(attempt), strategy.unit) }
    }
}

fun <T> Observable<T>.retry(strategy: RetryStrategy): Observable<T> = when (strategy) {
    is Times -> retry(strategy.attempts)
    is WithDelay -> retryWhen { errors ->
        errors.zipWith(Observable.range(1, strategy.attempts.toInt()), BiFunction<Throwable, Int, Int> { _, attempt -> attempt })
            .flatMap { Observable.timer(strategy.delay, strategy.unit) }
    }
    is WithVariableDelay -> retryWhen { errors ->
        errors.zipWith(Observable.range(1, strategy.attempts.toInt()), BiFunction<Throwable, Int, Int> { _, attempt -> attempt })
            .flatMap { attempt -> Observable.timer(strategy.getDelay(attempt), strategy.unit) }
    }
}

fun Completable.retry(strategy: RetryStrategy): Completable = when (strategy) {
    is Times -> retry(strategy.attempts)
    is WithDelay -> retryWhen { errors ->
        errors.zipWith(Flowable.range(1, strategy.attempts.toInt()), BiFunction<Throwable, Int, Int> { _, attempt -> attempt })
            .flatMap { Flowable.timer(strategy.delay, strategy.unit) }
    }
    is WithVariableDelay -> retryWhen { errors ->
        errors.zipWith(Flowable.range(1, strategy.attempts.toInt()), BiFunction<Throwable, Int, Int> { _, attempt -> attempt })
            .flatMap { attempt -> Flowable.timer(strategy.getDelay(attempt), strategy.unit) }
    }
}
