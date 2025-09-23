package com.starter.app.utils

fun main() {
    (0..500).forEach {
        println("<dimen name=\"sp_$it\">${it}sp</dimen>")
    }
}