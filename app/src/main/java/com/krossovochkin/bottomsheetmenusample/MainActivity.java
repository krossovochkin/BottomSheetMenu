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
package com.krossovochkin.bottomsheetmenusample;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.krossovochkin.bottomsheetmenu.BottomSheetMenu;
import com.krossovochkin.bottomsheetmenu.BottomSheetUtils;

public class MainActivity extends AppCompatActivity implements BottomSheetMenu.BottomSheetMenuListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.btn_open_bottom_sheet).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new BottomSheetMenu.Builder(MainActivity.this, MainActivity.this)
                        .setTitle("Title")
                        .show();
            }
        });
    }

    @Override
    public void onBottomSheetMenuItemSelected(MenuItem item) {
        final int itemId = item.getItemId();
        switch (itemId) {
            case R.id.action_create:
                Toast.makeText(this, "Create clicked", Toast.LENGTH_SHORT).show();
                break;
            case R.id.action_delete:
                Toast.makeText(this, "Delete clicked", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    @Override
    public void onCreateBottomSheetMenu(MenuInflater inflater, Menu menu) {
        inflater.inflate(R.menu.menu_bottom_sheet, menu);

        MenuItem item = menu.findItem(R.id.action_delete);
        BottomSheetUtils.setMenuItemTextColor(item, Color.RED);
        BottomSheetUtils.setMenuItemIconTint(item, Color.RED);
    }
}
