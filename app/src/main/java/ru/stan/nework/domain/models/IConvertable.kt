package ru.stan.nework.domain.models

import kotlin.reflect.KClass

interface IConvertable<T> {
    fun <I: Any> convertAs(clazz: KClass<I>): I?
}

@Suppress("UNCHECKED_CAST")
interface IConvertableTo<out T>: IConvertable<Any?> {
    fun convertTo(): T?

    override fun <I: Any> convertAs(clazz: KClass<I>): I? = convertTo() as? I
}