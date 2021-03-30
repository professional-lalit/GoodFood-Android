package com.goodfood.app.events

import androidx.annotation.IdRes
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

fun sendClickEvent(@IdRes viewId: Int, payload: Any?) {
    EventBus.getDefault().post(ClickEventMessage(viewId, payload))
}

fun sendEvent(payload: Any?) {
    EventBus.getDefault().post(Message(payload))
}