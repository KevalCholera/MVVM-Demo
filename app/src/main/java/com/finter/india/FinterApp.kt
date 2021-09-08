package com.finter.india

import android.app.*
import android.os.Build
import android.provider.Settings
import android.util.Log
import com.finter.india.utils.Constants
import com.finter.india.utils.SharedPreferenceUtil
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.messaging.FirebaseMessaging
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class FinterApp : Application() {

    @Inject
    lateinit var sharedPreferenceUtil: SharedPreferenceUtil
}