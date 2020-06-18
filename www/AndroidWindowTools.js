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
		
	getRealSize: function(success, error)
	{
        run(success, error, "AndroidWindowTools", "getRealSize");
	},

    getSoftwareKeys: function(success, error) {
        run(success, error, "AndroidWindowTools", "getSoftwareKeys");
    },

    getDisplayCutout: function(success, error) {
        run(success, error, "AndroidWindowTools", "getDisplayCutout");
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