package team.codemonsters.ddd.toolkit.domain.common

import reactor.util.function.Tuple3
import reactor.util.function.Tuple4
import reactor.util.function.Tuples

inline fun <R, T> Result<T>.flatMap(transform: (value: T) -> Result<R>): Result<R> {
    return when {
        isSuccess -> transform(this.getOrNull()!! as T)
        else -> Result.failure(this.exceptionOrNull()!!)
    }
}

inline fun <T> Result<T>.ensure(
    predicate: (value: T) -> Boolean,
    errorMessage: String
): Result<T> {
    if (this.isFailure)
        return this
    if (!predicate(this.getOrThrow()))
        return Result.failure(RuntimeException(errorMessage))
    return this
}

inline fun <T> Result<T>.ensure(
    predicate: (value: T) -> Boolean,
    error: Throwable
): Result<T> {
    if (this.isFailure)
        return this
    if (!predicate(this.getOrThrow()))
        return Result.failure(error)
    return this
}

/**
 * Объединяет заданные Result<T> в новый Result
 * который будет успешен, когда все заданные Result на вход успешны,
 * объединив их значения в Tuple4.
 * Ошибка любого результата на вход
 * приведет к Ошибке конечного результата,
 * который будет содержать сообщение об ошибке первого элемента с ошибкой на входе.
 * @param a Первый результат {@link Result}.
 * @param b Второй результат {@link Result}.
 * @param c Третий результат {@link Result}.
 * @param d Четвертый результат {@link Result}.
 * @param <A> тип значения a
 * @param <B> тип значения b
 * @param <C> тип значения c
 * @param <D> тип значения d
 *
 * @return a {@link Mono}.
 */
fun <A : Any, B : Any, C : Any, D : Any> Result.Companion.zip(a: Result<A>, b: Result<B>, c: Result<C>, d: Result<D>)
        : Result<Tuple4<A, B, C, D>> =
    if (sequenceOf(a, b, c, d).none { it.isFailure })
        success(Tuples.of(a.getOrThrow(), b.getOrThrow(), c.getOrThrow(), d.getOrThrow()))
    else
        failure(sequenceOf(a, b, c, d).first { it.isFailure }.exceptionOrNull()!!)

fun <A : Any, B : Any, C : Any> Result.Companion.zip(a: Result<A>, b: Result<B>, c: Result<C>)
        : Result<Tuple3<A, B, C>> =
    if (sequenceOf(a, b, c).none { it.isFailure })
        success(Tuples.of(a.getOrThrow(), b.getOrThrow(), c.getOrThrow()))
    else
        failure(sequenceOf(a, b, c).first { it.isFailure }.exceptionOrNull()!!)