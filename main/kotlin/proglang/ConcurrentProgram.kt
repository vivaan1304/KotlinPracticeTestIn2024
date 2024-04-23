package proglang

import java.util.concurrent.locks.Lock
import java.util.concurrent.locks.ReentrantLock

class ConcurrentProgram(val threadBodies: List<Stmt>,
                        val pauseValues:  List<Long>) {
    init {
        if(threadBodies.size != pauseValues.size) throw IllegalArgumentException("get gud")
    }
    private val lock:Lock = ReentrantLock()
    fun execute(initalStore: Map<String, Int>) :MutableMap<String, Int>{
        val workingStore = mutableMapOf<String,Int>()
        initalStore.entries.forEach { (k, v) -> workingStore.put(k, v)}
        val N = threadBodies.size
        val threads = (0..<N).map { i -> Thread(ProgramExecutor(threadBodies[i], pauseValues[i], store = workingStore, lock)) }
        try {
            threads.forEach{it.start()}
        }catch (_: UndefinedBehaviourException) {
            throw UndefinedBehaviourException("")
        }finally {
            threads.forEach{it.join()}
        }
        return workingStore
    }
}