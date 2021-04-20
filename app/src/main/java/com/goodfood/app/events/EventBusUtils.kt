package com.goodfood.app.events

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.annotation.IdRes
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import org.greenrobot.eventbus.EventBus


/**
 * Created by Lalit N. Hajare (Android Developer) on 31/03/21.
 *
 * This project is for demonstration purposes. It is highly likely that you must
 * be seeing this code by clicking the link in my resume.
 * This code is free and can be reused by anyone,
 * also if any suggestions they are welcomed at: `lalit.appsmail@gmail.com`
 * (please keep the subject as 'GoodFood Android Code Suggestion')
 */

fun sendClickEvent(@IdRes viewId: Int, payload: Any? = null) {
    EventBus.getDefault().post(ClickEventMessage(viewId = viewId, payload = payload))
}

fun sendEvent(eventId: Int, payload: Any? = null) {
    EventBus.getDefault().post(Message(eventId = eventId, payload = payload))
}

fun sendSticky(eventId: Int, payload: Any? = null) {
    EventBus.getDefault().post(Message(eventId = eventId, payload = payload))
}

fun removeSticky(event: Message) {
    EventBus.getDefault().removeStickyEvent(event)
}

fun Context.sendBroadcast(intent: Intent) {
    LocalBroadcastManager.getInstance(this).sendBroadcast(intent)
}

fun Context.registerReceiver(receiver: BroadcastReceiver, filter: IntentFilter) {
    registerReceiver(receiver, filter)
}

fun Context.unregisterReceiver(receiver: BroadcastReceiver) {
    unregisterReceiver(receiver)
}