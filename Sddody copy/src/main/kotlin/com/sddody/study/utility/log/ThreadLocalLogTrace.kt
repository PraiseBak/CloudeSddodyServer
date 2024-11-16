package com.sddody.study.utility.log;

class ThreadLocalLogTrace : LogTrace {
    private val traceIdHolder: ThreadLocal<TraceId> = ThreadLocal<TraceId>()
    override fun begin(message: String): TraceStatus {
        syncTraceId()
        val traceId: TraceId = traceIdHolder.get()
        val startTimeMs = System.currentTimeMillis()
        logger().info("[{}] {}{}", traceId.getId(), addSpace(START_PREFIX, traceId.getLevel()), message)
        return TraceStatus(traceId, startTimeMs, message)
    }

    override fun end(status: TraceStatus) {
        complete(status, null)
    }

    override fun exception(status: TraceStatus, e: Exception) {
        complete(status, e)
    }

    private fun complete(status: TraceStatus, e: Exception?) {
        val stopTimeMs = System.currentTimeMillis()
        val resultTimeMs: Long = stopTimeMs - status.startTimeMs
        val traceId: TraceId = status.traceId
        if (e == null) {
            logger().info(
                "[{}] {}{} time={}ms",
                traceId.getId(),
                addSpace(COMPLETE_PREFIX, traceId.getLevel()),
                status.message,
                resultTimeMs
            )
        } else {
            logger().info(
                "[{}] {}{} time={}ms ex={}",
                traceId.getId(),
                addSpace(EX_PREFIX, traceId.getLevel()),
                status.message,
                resultTimeMs,
                e.toString()
            )
        }
        releaseTraceId()
    }

    private fun syncTraceId() {
        val traceId: TraceId? = traceIdHolder.get()
        if (traceId == null) {
            traceIdHolder.set(TraceId())
        } else {
            traceIdHolder.set(traceId.createNextId())
        }
    }

    private fun releaseTraceId() {
        val traceId: TraceId = traceIdHolder.get()
        if (traceId.isFirstLevel()) {
            traceIdHolder.remove() //destroy
        } else {
            traceIdHolder.set(traceId.createPreviousId())
        }
    }

    companion object {
        private const val START_PREFIX = "-->"
        private const val COMPLETE_PREFIX = "<--"
        private const val EX_PREFIX = "<X-"
        private fun addSpace(prefix: String, level: Int): String {
            val sb = StringBuilder()
            for (i in 0 until level) {
                sb.append(if (i == level - 1) "|$prefix" else "|   ")
            }
            return sb.toString()
        }
    }
}


