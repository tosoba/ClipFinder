package com.example.there.findclips.fragment.user

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.Toolbar
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import com.example.there.findclips.R
import com.example.there.findclips.base.fragment.HasMainToolbar
import com.example.there.findclips.util.ext.mainActivity
import kotlinx.android.synthetic.main.fragment_user.*


class UserFragment : Fragment(), HasMainToolbar {

    override val toolbar: Toolbar
        get() = account_toolbar

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_user, container, false)
        mainActivity?.setSupportActionBar(view.findViewById(R.id.account_toolbar))
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        login_btn?.setOnClickListener {
            if (mainActivity?.loggedIn != true) {
                mainActivity?.openLoginWindow()
            } else {
                mainActivity?.logOutPlayer()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean = false
}
