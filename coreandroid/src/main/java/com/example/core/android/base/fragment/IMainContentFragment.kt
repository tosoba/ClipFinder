package com.example.core.android.base.fragment

import androidx.fragment.app.Fragment
import com.google.android.material.floatingactionbutton.FloatingActionButton

interface IMainContentFragment {
    val currentNavHostFragment: BaseNavHostFragment?
    val currentFragment: Fragment?
    val playButton: FloatingActionButton

    fun disablePlayButton() {
        if (playButton.isOrWillBeShown) playButton.hide()
    }

    fun enablePlayButton(btnClicked: () -> Unit) {
        if (playButton.isOrWillBeHidden) playButton.show()
        playButton.setOnClickListener { btnClicked() }
    }
}
