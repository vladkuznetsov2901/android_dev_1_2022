package com.example.myapplication

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.CompletableJob
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeout
import kotlinx.coroutines.yield
import java.math.BigInteger

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}

suspend fun main() {
    val scope = CoroutineScope(Job() + Dispatchers.Default)
    val job = scope.launch {
        println(Fibonacci.take(5))
        println(Fibonacci.take(10))
        println(Fibonacci.take(15))

        println(Fibonacci.take(15))
        println(Fibonacci.take(10))
        println(Fibonacci.take(5))

        println(Fibonacci.take(55))
        println(Fibonacci.take(100))
        println(Fibonacci.take(555))


    }

    scope.launch {
        withTimeout(3000) {
            println("cancel job")
            job.cancel()
        }

    }

    scope.launch {
        while (job.isActive) {
            print(".")
        }
    }

    (scope.coroutineContext.job as? CompletableJob)?.let {
        it.complete()
        it.join()
    } ?: throw IllegalStateException()
}


object Fibonacci {

    suspend fun take(index: Int): BigInteger {
        if (index.toBigInteger() == BigInteger.ZERO) {
            return BigInteger.ZERO
        }
        if (index.toBigInteger() < BigInteger.ZERO) {
            throw IndexOutOfBoundsException(index.toString())
        }
        var n0 = BigInteger.ZERO
        var n1 = BigInteger.ONE
        var i = 0
        while (i.toBigInteger() <= index.toBigInteger()) {
            yield()
            val n2 = n0 + n1
            n0 = n1
            n1 = n2
            i++
        }
        return n1
    }

}