package team.codemonsters.ddd.toolkit.domain.common

inline fun <T> Result<T>.ensure(predicate: (value: T) -> Boolean, errorMessage: String): Result<T> {
    if (this.isFailure)
        return this
    if (!predicate(this.getOrThrow()))
        return Result.failure(RuntimeException(errorMessage))
    return this
}