package com.example.there.findclips.util

import android.app.Activity
import com.example.there.findclips.FindClipsApp

val Activity.app: FindClipsApp
    get() = this.application as FindClipsApp