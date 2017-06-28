### Bottom Sheet Menu
Library to create [Bottom Sheet Menu](https://material.io/guidelines/components/bottom-sheets.html) in a familiar `<menu>` style.

Basically this is implementation of **Modal bottom sheets** (list only) from [Material Design Guidelines](https://material.io/guidelines/components/bottom-sheets.html#bottom-sheets-modal-bottom-sheets)

![Image](/img/example.png)

### How to use
1. Create `menu.xml` file as for any other menu, providing id, title and icon for each item
```
<?xml version="1.0" encoding="utf-8"?>
<menu xmlns:android="http://schemas.android.com/apk/res/android">
    <item
        android:id="@+id/action_create"
        android:title="@string/action_create"
        android:icon="@drawable/ic_create_black_24dp"/>

    <item
        android:id="@+id/action_delete"
        android:title="@string/action_delete"
        android:icon="@drawable/ic_delete_black_24dp"/>
</menu>
```
2. Create instance of `BottomSheetMenu` using its `Builder` and provide callback for instantiating and handling menu item selection:
```
new BottomSheetMenu.Builder(getContext(), new BottomSheetMenuListener() {...} ).show();
```
3. In `BottomSheetMenuListener` initialize menu using previously created `menu.xml`
```
@Override
public void onCreateBottomSheetMenu(MenuInflater inflater, Menu menu) {
    inflater.inflate(R.menu.menu, menu);
    // add custom logic here, see below for more info
}
```
4. In `BottomSheetMenuListener` add handling selection menu items e.g. by id:
```
@Override
public void onBottomSheetMenuItemSelected(MenuItem item) {
    final int itemId = item.getItemId();
    switch (itemId) {
        case R.id.action_save:
            // do something
            break;
    }
}
```

So, code is similar to creating menu in Toolbar (where `onCreateOptionsMenu` and `onOptionsItemSelected` methods are used).<br/>
This library provides similar callbacks to make it more familiar to android components.

### Additional
Additionally `BottomSheetsUtils` class is added with few customization methods.
1. `setMenuItemTextColor` - sets `MenuItem` title to `Spannable` with `ForegroundColorSpan` with required color set.<br/>
This allows to set custom text color on menu item.<br/>
2. `setMenuItemIconTint` - similar to changing text color, uses `DrawableCompat#setTint` to set tint for icon.

**NOTE:** Though in material design guidelines there is no single word about possible customizations (and they mostly should be avoided to not ruin UX), this still may be a good idea to highlight e.g. some dangerous items in menu.

One can use these methods inside `onCreateBottomSheetMenu`:
```
@Override
public void onCreateBottomSheetMenu(MenuInflater inflater, Menu menu) {
    inflater.inflate(R.menu.menu_bottom_sheet, menu);

    MenuItem item = menu.findItem(R.id.action_delete);
    BottomSheetUtils.setMenuItemTextColor(item, Color.RED)
    BottomSheetUtils.setMenuItemIconTint(item, Color.RED);
}
```

### License
```
Copyright (C) 2017 Vasya Drobushkov

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

      http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
 ```