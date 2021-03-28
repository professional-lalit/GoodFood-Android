package com.goodfood.app.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.goodfood.app.models.domain.User
import com.goodfood.app.networking.NetworkResponse
import com.goodfood.app.repositories.UserRepository
import com.goodfood.app.ui.common.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


/**
 * Created by Lalit N. Hajare (Android Developer) on 25/03/21.
 *
 * This project is for demonstration purposes. It is highly likely that you must
 * be seeing this code by clicking the link in my resume.
 * This code is free and can be reused by anyone,
 * also if any suggestions they are welcomed at: `lalit.appsmail@gmail.com`
 * (please keep the subject as 'GoodFood Android Code Suggestion')
 */

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val userRepository: UserRepository
) : BaseViewModel() {

    private val _user = MutableLiveData<User>()
    val user: LiveData<User> = _user

    fun getMeDetails() {
        viewModelScope.launch {
            val networkResponse = userRepository.fetchMeDetails()
            if (networkResponse is NetworkResponse.NetworkSuccess) {
                val user = networkResponse.data as User
                _user.postValue(user)
            } else if (networkResponse is NetworkResponse.NetworkError) {
                _errorData.postValue(networkResponse.error)
            }
        }
    }

    fun logout(){
        userRepository.logout()
        _screenToNav.postValue(Navigables.LOGIN)
    }

}