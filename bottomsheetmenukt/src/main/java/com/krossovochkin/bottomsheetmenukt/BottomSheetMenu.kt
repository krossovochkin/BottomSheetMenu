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
import android.support.annotation.StringRes
import android.support.design.widget.BottomSheetBehavior
import android.support.design.widget.BottomSheetDialog
import android.support.v4.graphics.drawable.DrawableCompat
import android.text.SpannableString
import android.text.Spanned
import android.text.TextUtils
import android.text.style.ForegroundColorSpan
import android.view.*
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TextView
import kotlin.properties.Delegates

fun MenuItem.setTextColor(@ColorInt textColor: Int) {
    val s = SpannableString(title)
    s.setSpan(ForegroundColorSpan(textColor), 0, s.length, Spanned.SPAN_INCLUSIVE_INCLUSIVE)
    title = s
}

fun MenuItem.setIconTint(@ColorInt tintColor: Int) {
    val drawable = icon.mutate()
    DrawableCompat.setTint(drawable, tintColor)
    icon = drawable
}

class BottomSheetMenu(
        context: Context,
        private val bottomSheetMenuListener: BottomSheetMenuListener)
    : BottomSheetDialog(context) {

    private var iconSize : Int by Delegates.notNull()
    private var title : CharSequence? = null

    init {
        iconSize = context.resources.getDimensionPixelSize(R.dimen.bottom_sheet_menu_item_icon_size)

        setOnShowListener({ dialog ->
            val d = dialog as BottomSheetDialog
            val bottomSheet = d.findViewById(android.support.design.R.id.design_bottom_sheet) as FrameLayout
            BottomSheetBehavior.from(bottomSheet).setState(BottomSheetBehavior.STATE_EXPANDED)
        })
    }

    class Builder(private val context: Context, private val bottomSheetMenuListener: BottomSheetMenuListener) {

        private var title : CharSequence? = null

        fun setTitle(@StringRes titleId : Int) : Builder {
            title = context.getString(titleId)
            return this
        }

        fun setTitle(title : CharSequence?) : Builder {
            this.title = title
            return this
        }

        private fun create(): BottomSheetMenu {
            val menu = BottomSheetMenu(context, bottomSheetMenuListener)
            menu.setTitle(title)
            menu.build()
            return menu
        }

        fun show(): BottomSheetMenu {
            val menu = create()
            menu.show()
            return menu
        }
    }

    override fun setTitle(title: CharSequence?) {
        // no need to call super.setTitle, as title is blocked in BottomSheetDialog
        this.title = title
    }

    private fun build() {
        val layoutInflater = LayoutInflater.from(context)
        val menu = newMenuInstance(context) ?: return

        val menuInflater = MenuInflater(context)
        bottomSheetMenuListener.onCreateBottomSheetMenu(menuInflater, menu)

        val rootView = View.inflate(context, R.layout.view_bottom_sheet_menu, null) as LinearLayout
        rootView.addView(createHeaderView(layoutInflater, rootView, title))
        for (i in 0 until menu.size()) {
            rootView.addView(createMenuItemView(layoutInflater, rootView, menu.getItem(i)))
        }
        setContentView(rootView)
    }

    private fun createHeaderView(inflater: LayoutInflater, rootView: ViewGroup, title: CharSequence?): View {
        if (TextUtils.isEmpty(title)) {
            return inflater.inflate(R.layout.view_bottom_sheet_header_placeholder, rootView, false)
        } else {
            val titleView = inflater.inflate(R.layout.view_bottom_sheet_header, rootView, false) as TextView
            titleView.text = title
            return titleView
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
}