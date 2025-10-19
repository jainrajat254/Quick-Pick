package org.rajat.quickpick.utils.tokens

expect object PlatformScheduler {
    fun scheduleRefreshAt(triggerAtMillis: Long)
    fun cancelScheduledRefresh()
}
