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
package com.krossovochkin.bottomsheetmenu;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.lang.reflect.Constructor;

public class BottomSheetMenu extends BottomSheetDialog {

    private final BottomSheetMenuListener mBottomSheetMenuListener;
    private final int mIconSize;
    @Nullable
    private CharSequence mTitle;

    public static class Builder {

        private final Context mContext;
        private final BottomSheetMenuListener mBottomSheetMenuListener;
        private CharSequence mTitle;

        public Builder(@NonNull Context context, BottomSheetMenuListener listener) {
            mContext = context;
            mBottomSheetMenuListener = listener;
        }

        public BottomSheetMenu.Builder setTitle(@StringRes int titleId) {
            mTitle = mContext.getString(titleId);
            return this;
        }

        public BottomSheetMenu.Builder setTitle(CharSequence title) {
            mTitle = title;
            return this;
        }

        private BottomSheetMenu create() {
            final BottomSheetMenu menu = new BottomSheetMenu(mContext, mBottomSheetMenuListener);
            menu.setTitle(mTitle);
            menu.build();
            return menu;
        }

        @SuppressWarnings("UnusedReturnValue")
        public BottomSheetMenu show() {
            final BottomSheetMenu menu = create();
            menu.show();
            return menu;
        }
    }

    @SuppressLint("InflateParams")
    private BottomSheetMenu(@NonNull Context context, BottomSheetMenuListener bottomSheetMenuListener) {
        super(context);
        mIconSize = context.getResources().getDimensionPixelSize(R.dimen.bottom_sheet_menu_item_icon_size);
        mBottomSheetMenuListener = bottomSheetMenuListener;

        setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                BottomSheetDialog d = (BottomSheetDialog) dialog;
                FrameLayout bottomSheet = (FrameLayout) d.findViewById(android.support.design.R.id.design_bottom_sheet);

                if (bottomSheet != null) {
                    BottomSheetBehavior.from(bottomSheet).setState(BottomSheetBehavior.STATE_EXPANDED);
                }
            }
        });
    }

    @Override
    public void setTitle(CharSequence title) {
        // no need to call super.setTitle, as title is blocked in BottomSheetDialog
        mTitle = title;
    }

    private void build() {
        final LayoutInflater layoutInflater = LayoutInflater.from(getContext());
        final Menu menu = newMenuInstance(getContext());

        if (menu == null) {
            return;
        }

        final MenuInflater menuInflater = new MenuInflater(getContext());
        mBottomSheetMenuListener.onCreateBottomSheetMenu(menuInflater, menu);

        final LinearLayout rootView = (LinearLayout) View.inflate(getContext(), R.layout.view_bottom_sheet_menu, null);
        rootView.addView(createHeaderView(layoutInflater, rootView, mTitle));
        for (int i = 0; i < menu.size(); i++) {
            rootView.addView(createMenuItemView(layoutInflater, rootView, menu.getItem(i)));
        }
        setContentView(rootView);
    }

    private View createHeaderView(final LayoutInflater inflater, ViewGroup rootView, @Nullable CharSequence title) {
        if (TextUtils.isEmpty(title)) {
            return inflater.inflate(R.layout.view_bottom_sheet_header_placeholder, rootView, false);
        } else {
            final TextView titleView = (TextView) inflater.inflate(R.layout.view_bottom_sheet_header, rootView, false);
            titleView.setText(mTitle);
            return titleView;
        }
    }

    private View createMenuItemView(final LayoutInflater inflater, ViewGroup rootView, final MenuItem item) {
        final TextView menuItemView = (TextView) inflater.inflate(R.layout.view_bottom_sheet_menu_item, rootView, false);

        menuItemView.setText(item.getTitle());

        final Drawable drawable = item.getIcon().mutate();
        drawable.setBounds(0, 0, mIconSize, mIconSize);
        menuItemView.setCompoundDrawables(drawable, null, null, null);

        menuItemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mBottomSheetMenuListener.onBottomSheetMenuItemSelected(item);
                dismiss();
            }
        });
        return menuItemView;
    }

    public interface BottomSheetMenuListener {
        void onCreateBottomSheetMenu(MenuInflater inflater, Menu menu);

        void onBottomSheetMenuItemSelected(MenuItem item);
    }

    @SuppressLint("PrivateApi")
    private Menu newMenuInstance(Context context) {
        try {
            Class<?> menuBuilderClass = Class.forName("com.android.internal.view.menu.MenuBuilder");
            Constructor<?> constructor = menuBuilderClass.getDeclaredConstructor(Context.class);
            return (Menu) constructor.newInstance(context);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}