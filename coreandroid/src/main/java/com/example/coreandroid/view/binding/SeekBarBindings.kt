package com.example.coreandroid.view.binding

import android.databinding.BindingAdapter
import android.widget.SeekBar

@BindingAdapter("progressChangeListener")
fun bindProgressChangeListener(seekBar: SeekBar, listener: SeekBar.OnSeekBarChangeListener) {
    seekBar.setOnSeekBarChangeListener(listener)
}