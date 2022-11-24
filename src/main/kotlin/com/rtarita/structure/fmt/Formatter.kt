package com.rtarita.structure.fmt

interface Formatter<in T> {
    fun T.format(): String
}