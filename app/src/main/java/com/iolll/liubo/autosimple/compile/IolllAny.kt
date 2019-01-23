package com.iolll.liubo.autosimple.compile

/**
 * Created by LiuBo on 2018/11/1.
 */
class IolllAny() : Any()

//    fun Any.withNotNull(function: () -> Unit) {
//    }
fun <T, R> T.run2(block: T.() -> R): R {
    contract {
        this.callsInPlace(block, InvocationKind.EXACTLY_ONCE)
    }
    return block()
}

fun <T> T.withNotNull(block: T.() -> Unit): T {
    contract {
        callsInPlace(block, InvocationKind.EXACTLY_ONCE)
    }
    if (this!=null)
        this.block()
    return this
}
fun <T> T.withNull(block: T.() -> Unit): T {
    contract {
        callsInPlace(block, InvocationKind.EXACTLY_ONCE)
    }
    if (null == this)
        block()
    return this
}
//
//fun <T> T.withNotNull(block: T.() -> Unit): T {
//    println(" in ")
//    if (null != this)
//        if (this !== Unit)
//            contract {
//                println(" in block")
//                returnsNotNull()
//            }
//    println(" out this" + this)
//    return this
//}

//fun <T, R> T.withNull(block: T.() -> R): R {
//    if (null == this) {
//        if (Unit === this)
//            contract {
//                callsInPlace(block, InvocationKind.EXACTLY_ONCE)
//            }
//    }
//    return block()
//}

inline fun contract(builder: ContractBuilder.() -> Unit) {}

interface ContractBuilder {
    @ContractsDsl
    fun returns(): Returns

    @ContractsDsl
    fun returns(value: Any?): Returns

    @ContractsDsl
    fun returnsNotNull(): ReturnsNotNull

    @ContractsDsl
    fun <R> callsInPlace(lambda: Function<R>, kind: InvocationKind = InvocationKind.UNKNOWN): CallsInPlace
}

annotation class ContractsDsl

@ContractsDsl
@SinceKotlin("1.2")
interface Effect

@ContractsDsl
@SinceKotlin("1.2")
interface ConditionalEffect : Effect

@ContractsDsl
@SinceKotlin("1.2")
interface SimpleEffect {
    @ContractsDsl
    infix fun implies(booleanExpression: Boolean): ConditionalEffect
}


@ContractsDsl
@SinceKotlin("1.2")
interface Returns : SimpleEffect

@ContractsDsl
@SinceKotlin("1.2")
interface ReturnsNotNull : SimpleEffect

@ContractsDsl
@SinceKotlin("1.2")
interface CallsInPlace : SimpleEffect

enum class InvocationKind {
    @ContractsDsl
    AT_MOST_ONCE,
    @ContractsDsl
    AT_LEAST_ONCE,
    @ContractsDsl
    EXACTLY_ONCE,
    @ContractsDsl
    UNKNOWN
}

