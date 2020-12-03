package ru.haroncode.aquarius

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_main.*

class MainFragment : Fragment(R.layout.fragment_main) {

    interface Callback {

        fun moveToPagination()

        fun moveToList()
    }

    private val callback: Callback?
        get() = activity as? Callback

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        simpleList.setOnClickListener { callback?.moveToList() }
        pagination.setOnClickListener { callback?.moveToPagination() }
    }

}