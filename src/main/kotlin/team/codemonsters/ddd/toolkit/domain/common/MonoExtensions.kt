package team.codemonsters.ddd.toolkit.domain.common

import reactor.core.publisher.Mono

fun <T, R> Mono<Result<T>>.pipe(function: (T) -> Mono<Result<R>>)
        : Mono<Result<R>> =
    this.flatMap { result ->
        result.fold(
            { value -> function(value) },
            { exception -> Result.failure<R>(exception) }
        ) as Mono<out Result<R>>?
    }

fun <T, R> Mono<Result<T>>.mapResult(function: (T) -> Result<R>)
        : Mono<Result<R>> =
    this.map { result ->
        result.fold(
            { value -> function(value) },
            { exception -> Result.failure(exception) }
        )
    }
