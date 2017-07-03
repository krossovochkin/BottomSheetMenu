/*
 * Copyright (C) 2017 Vasya Drobushkov
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.krossovochkin.bottomsheetmenukt

import android.annotation.SuppressLint
import android.content.Context
import android.support.annotation.ColorInt
import android.support.design.widget.BottomSheetBehavior
import android.support.design.widget.BottomSheetDialog
import android.support.v4.graphics.drawable.DrawableCompat
import android.text.SpannableString
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.view.*
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TextView

class BottomSheetMenu(
        context: Context,
        private val bottomSheetMenuListener: BottomSheetMenuListener,
        private val iconSize: Int = context.resources.getDimensionPixelSize(R.dimen.bottom_sheet_menu_item_icon_size))
    : BottomSheetDialog(context) {

    init {
        setOnShowListener({ dialog ->
            val d = dialog as BottomSheetDialog
            val bottomSheet = d.findViewById(android.support.design.R.id.design_bottom_sheet) as FrameLayout
            BottomSheetBehavior.from(bottomSheet).setState(BottomSheetBehavior.STATE_EXPANDED)
        })
    }

    class Builder(private val context: Context, private val bottomSheetMenuListener: BottomSheetMenuListener) {

        private fun create(): BottomSheetMenu {
            val menu = BottomSheetMenu(context, bottomSheetMenuListener)
            menu.build()
            return menu
        }

        fun show(): BottomSheetMenu {
            val menu = create()
            menu.show()
            return menu
        }
    }

    private fun build() {
        val layoutInflater = LayoutInflater.from(context)
        val menu = newMenuInstance(context) ?: return

        val menuInflater = MenuInflater(context)
        bottomSheetMenuListener.onCreateBottomSheetMenu(menuInflater, menu)

        val rootView = View.inflate(context, R.layout.view_bottom_sheet_menu, null) as LinearLayout
        setContentView(rootView)
        for (i in 0 until menu.size()) {
            rootView.addView(createMenuItemView(layoutInflater, rootView, menu.getItem(i)))
        }
    }

    private fun createMenuItemView(inflater: LayoutInflater, rootView: ViewGroup, item: MenuItem): View {
        val menuItemView = inflater.inflate(R.layout.view_bottom_sheet_menu_item, rootView, false) as TextView

        menuItemView.text = item.title

        val drawable = item.icon.mutate()
        drawable.setBounds(0, 0, iconSize, iconSize)
        menuItemView.setCompoundDrawables(drawable, null, null, null)

        menuItemView.setOnClickListener {
            bottomSheetMenuListener.onBottomSheetMenuItemSelected(item)
            dismiss()
        }
        return menuItemView
    }

    interface BottomSheetMenuListener {
        fun onCreateBottomSheetMenu(inflater: MenuInflater, menu: Menu)

        fun onBottomSheetMenuItemSelected(item: MenuItem)
    }

    @SuppressLint("PrivateApi")
    private fun newMenuInstance(context: Context): Menu? {
        try {
            val menuBuilderClass = Class.forName("com.android.internal.view.menu.MenuBuilder")
            val constructor = menuBuilderClass.getDeclaredConstructor(Context::class.java)
            return constructor.newInstance(context) as Menu
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return null
    }

    companion object {
        fun setMenuItemTextColor(menuItem: MenuItem, @ColorInt textColor: Int) {
            val s = SpannableString(menuItem.title)
            s.setSpan(ForegroundColorSpan(textColor), 0, s.length, Spanned.SPAN_INCLUSIVE_INCLUSIVE)
            menuItem.title = s
        }

        fun setMenuItemIconTint(menuItem: MenuItem, @ColorInt tintColor: Int) {
            val drawable = menuItem.icon.mutate()
            DrawableCompat.setTint(drawable, tintColor)
            menuItem.icon = drawable
        }
    }
}