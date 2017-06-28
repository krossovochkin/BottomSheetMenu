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

import android.graphics.drawable.Drawable;
import android.support.annotation.ColorInt;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.MenuItem;

public class BottomSheetUtils {

    public static void setMenuItemTextColor(MenuItem menuItem, @ColorInt int textColor) {
        SpannableString s = new SpannableString(menuItem.getTitle());
        s.setSpan(new ForegroundColorSpan(textColor), 0, s.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        menuItem.setTitle(s);
    }

    public static void setMenuItemIconTint(MenuItem menuItem, @ColorInt int tintColor) {
        Drawable drawable = menuItem.getIcon().mutate();
        DrawableCompat.setTint(drawable, tintColor);
        menuItem.setIcon(drawable);
    }
}
