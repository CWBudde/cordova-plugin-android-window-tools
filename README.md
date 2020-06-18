Android Window Tools for Cordova
================================

Plugin for Cordova 8.0+ to perform some basic window operations like setting certain flags or changing the statusbar color. Also getting various sizes and insets.

Released under MIT license; see LICENSE for details.

How does it work?
-----------------

A loose collection of various tools that perform simple tasks on the Android side.

Installation
------------

**Cordova**

`cordova plugin add cordova-plugin-android-window-tools`

Code example
------------

Here are some examples on how you can use this plugin

```js
AndroidWindowTools.getSoftwareKeys(successSoftKeysFunction, errorFunction);

AndroidWindowTools.getDisplayCutout(successFunction, errorFunction);

AndroidWindowTools.getRealSize(successRealSizeFunction, errorFunction);

// See https://developer.android.com/reference/android/view/Window#setNavigationBarColor(int)
AndroidWindowTools.setNavigationBarColor('#f00', successFunction, errorFunction);

// See https://developer.android.com/reference/android/view/Window#setStatusBarColor(int)
AndroidWindowTools.setStatusBarColor('#0f0', successFunction, errorFunction);

// See https://developer.android.com/reference/android/view/View.html#setSystemUiVisibility(int)
AndroidWindowTools.setSystemUiVisibility(AndroidWindowTools.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | AndroidWindowTools.SYSTEM_UI_FLAG_LAYOUT_STABLE | AndroidWindowTools.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION, successFunction, errorFunction);

```