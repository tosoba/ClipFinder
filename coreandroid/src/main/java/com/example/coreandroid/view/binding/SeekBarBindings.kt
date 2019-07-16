package com.example.coreandroid.view.binding

import android.widget.SeekBar
import androidx.databinding.BindingAdapter

@BindingAdapter("progressChangeListener")
fun bindProgressChangeListener(seekBar: SeekBar, listener: SeekBar.OnSeekBarChangeListener) {
    seekBar.setOnSeekBarChangeListener(listener)
}