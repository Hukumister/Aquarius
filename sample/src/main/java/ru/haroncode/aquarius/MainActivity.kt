package ru.haroncode.aquarius

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import ru.haroncode.aquarius.list.ListFragment
import ru.haroncode.aquarius.pagination.PaginationFragment

class MainActivity : AppCompatActivity(), MainFragment.Callback {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (supportFragmentManager.fragments.isEmpty()) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, MainFragment())
                .commit()
        }
    }

    override fun moveToPagination() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.container, PaginationFragment())
            .addToBackStack(null)
            .commit()
    }

    override fun moveToList() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.container, ListFragment())
            .addToBackStack(null)
            .commit()
    }
}