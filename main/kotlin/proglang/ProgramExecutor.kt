package proglang

import java.util.concurrent.locks.Lock
import java.util.concurrent.locks.ReentrantLock
import kotlin.concurrent.withLock

class ProgramExecutor(val threadBody: Stmt, val pauseValue: Long, val store:MutableMap<String, Int>, private val lock:Lock = ReentrantLock()):Runnable {

    override fun run() {
        var cur:Stmt? = threadBody.clone()
        while(cur != null){
            Thread.sleep(pauseValue)
            lock.withLock {
                println(Thread().id)
                println(store)
               cur = cur!!.step(store)
            }
        }
    }
}