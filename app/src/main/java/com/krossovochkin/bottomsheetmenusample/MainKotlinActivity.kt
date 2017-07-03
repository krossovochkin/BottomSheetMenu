package com.krossovochkin.bottomsheetmenusample

import android.graphics.Color
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.Toast
import com.krossovochkin.bottomsheetmenukt.BottomSheetMenu

class MainKotlinActivity : AppCompatActivity(), BottomSheetMenu.BottomSheetMenuListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById(R.id.btn_open_bottom_sheet).setOnClickListener {
            BottomSheetMenu.Builder(context = this@MainKotlinActivity, bottomSheetMenuListener = this@MainKotlinActivity)
                    .show()
        }
    }

    override fun onBottomSheetMenuItemSelected(item: MenuItem) {
        when (item.itemId) {
            R.id.action_create -> Toast.makeText(this, "Create clicked", Toast.LENGTH_SHORT).show()
            R.id.action_delete -> Toast.makeText(this, "Delete clicked", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreateBottomSheetMenu(inflater: MenuInflater, menu: Menu) {
        inflater.inflate(R.menu.menu_bottom_sheet, menu)

        val item = menu.findItem(R.id.action_delete)
        BottomSheetMenu.setMenuItemTextColor(item, Color.RED)
        BottomSheetMenu.setMenuItemIconTint(item, Color.RED)
    }
}