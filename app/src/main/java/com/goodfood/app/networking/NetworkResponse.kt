package com.goodfood.app.networking

import com.goodfood.app.models.domain.Error


/**
 * Created by Lalit N. Hajare (Android Developer) on 21/03/21.
 *
 * This project is for demonstration purposes. It is highly likely that you must
 * be seeing this code by clicking the link in my resume.
 * This code is free and can be reused by anyone,
 * also if any suggestions they are welcomed at: `lalit.appsmail@gmail.com`
 * (please keep the subject as 'GoodFood Android Code Suggestion')
 */
sealed class NetworkResponse {

    data class NetworkSuccess(val data: Any?): NetworkResponse()

    data class NetworkError(val error: Error): NetworkResponse()

}