var run = require("cordova/exec");

var AndroidWindowTools = {

	SYSTEM_UI_FLAG_FULLSCREEN: 4,
	SYSTEM_UI_FLAG_HIDE_NAVIGATION: 2,
	SYSTEM_UI_FLAG_IMMERSIVE: 2048,
	SYSTEM_UI_FLAG_IMMERSIVE_STICKY: 4096,
	SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN: 1024,
	SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION: 512,
	SYSTEM_UI_FLAG_LAYOUT_STABLE: 256,
	SYSTEM_UI_FLAG_LIGHT_STATUS_BAR: 8192,
	SYSTEM_UI_FLAG_LOW_PROFILE: 1,
	SYSTEM_UI_FLAG_VISIBLE: 0,
		
	FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS: -2147483648,
	FLAG_LAYOUT_ATTACHED_IN_DECOR: 1073741824,
	FLAG_LOCAL_FOCUS_MODE: 268435456,
	FLAG_TRANSLUCENT_NAVIGATION: 134217728, 
	FLAG_TRANSLUCENT_STATUS: 67108864,
	FLAG_LAYOUT_IN_OVERSCAN: 33554432,
	FLAG_HARDWARE_ACCELERATED: 16777216,
	FLAG_TURN_SCREEN_ON: 2097152,
	FLAG_SPLIT_TOUCH: 8388608,
	FLAG_DISMISS_KEYGUARD: 4194304,
	FLAG_SHOW_WALLPAPER: 1048576,
	FLAG_SHOW_WHEN_LOCKED: 524288,
	FLAG_ALT_FOCUSABLE_IM: 131072,
	FLAG_LAYOUT_INSET_DECOR: 65536,
	FLAG_IGNORE_CHEEK_PRESSES: 32768,
	FLAG_SCALED: 16384,
	FLAG_SECURE: 8192,
	FLAG_DITHER: 4096,
	FLAG_FORCE_NOT_FULLSCREEN: 2048,
	FLAG_FULLSCREEN: 1024,
	FLAG_LAYOUT_NO_LIMITS: 512,
	FLAG_LAYOUT_IN_SCREEN: 256,
	FLAG_KEEP_SCREEN_ON: 128,
	FLAG_TOUCHABLE_WHEN_WAKING: 64,
	FLAG_NOT_TOUCH_MODAL: 32,
	FLAG_NOT_TOUCHABLE: 16,
	FLAG_NOT_FOCUSABLE: 8,
	FLAG_BLUR_BEHIND: 4,
	FLAG_DIM_BEHIND: 2,
	FLAG_ALLOW_LOCK_WHILE_SCREEN_ON: 1,

	getDisplayCutout: function(success, error) {
        run(success, error, "AndroidWindowTools", "getDisplayCutout");
    },

	getMetrics: function(success, error)
	{
        run(success, error, "AndroidWindowTools", "getMetrics");
	},

	getRealMetrics: function(success, error)
	{
        run(success, error, "AndroidWindowTools", "getRealMetrics");
	},

	getRealSize: function(success, error)
	{
        run(success, error, "AndroidWindowTools", "getRealSize");
	},

    getSoftwareKeys: function(success, error) {
        run(success, error, "AndroidWindowTools", "getSoftwareKeys");
    },

	addWindowFlags: function(flags, success, error)
	{
		run(success, error, "AndroidWindowTools", "addWindowFlags", [flags || 0]);
	},

	clearWindowFlags: function(flags, success, error)
	{
		run(success, error, "AndroidWindowTools", "clearWindowFlags", [flags || 0]);
	},

	setSystemUiVisibility: function(visibility, success, error)
	{
		run(success, error, "AndroidWindowTools", "setSystemUiVisibility", [visibility || 0]);
	},

	setStatusBarColor: function(color, success, error)
	{
		run(success, error, "AndroidWindowTools", "setStatusBarColor", [color]);
	},

	setNavigationBarColor: function(color, success, error)
	{
		run(success, error, "AndroidWindowTools", "setNavigationBarColor", [color]);
	}
};


module.exports = AndroidWindowTools;