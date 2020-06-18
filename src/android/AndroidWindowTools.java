package de.pcjv.AndroidWindowTools;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaArgs;
import org.apache.cordova.CordovaInterface;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CordovaWebView;
import org.apache.cordova.LOG;
import org.apache.cordova.PluginResult;
import org.json.JSONArray;
import org.json.JSONException;
import java.util.Arrays;

import android.annotation.TargetApi;
import android.app.Activity;
import android.graphics.Point;
import android.graphics.Color;
import android.os.Build;
import android.os.Handler;
import android.view.Display;
import android.view.DisplayCutout;
import android.view.View;
import android.view.Window;
import android.view.WindowInsets;
import android.view.WindowManager;

public class AndroidWindowTools extends CordovaPlugin
{
    private static final String TAG = "AndroidWindowTools";

	public static final String ACTION_SET_NAVIGATION_BAR_COLOR = "setNavigationBarColor";
	public static final String ACTION_SET_STATUS_BAR_COLOR = "setStatusBarColor";
	public static final String ACTION_SET_SYSTEM_UI_VISIBILITY = "setSystemUiVisibility";
	public static final String ACTION_GET_SOFTWARE_KEYS = "getSoftwareKeys";
	public static final String ACTION_GET_DISPLAY_CUTOUT = "getDisplayCutout";
	public static final String ACTION_GET_REAL_SIZE = "getRealSize";

	private CallbackContext context;
	private Activity activity;
	private Window window;
	private View decorView;
	
	/**
     * Sets the context of the Command. This can then be used to do things like
     * get file paths associated with the Activity.
     *
     * @param cordova The context of the main Activity.
     * @param webView The CordovaWebView Cordova is running in.
     */
    @Override
    public void initialize(final CordovaInterface cordova, final CordovaWebView webView) {
        LOG.v(TAG, "AndroidWindowTools: initialization");
        super.initialize(cordova, webView);

        this.cordova.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // Clear flag FLAG_FORCE_NOT_FULLSCREEN which is set initially
                // by the Cordova.
                final Window window = cordova.getActivity().getWindow();
				window.clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
			}
		});
	}

	@Override
	public boolean execute(final String action, final JSONArray args, final CallbackContext callbackContext)
			throws JSONException {
		LOG.v(TAG, "Executing action: " + action);

		context = callbackContext;
		activity = this.cordova.getActivity();
		window = activity.getWindow();
		decorView = window.getDecorView();

		if (ACTION_SET_NAVIGATION_BAR_COLOR.equals(action))
			return setNavigationBarColor(args.getInt(0));
		else if (ACTION_SET_STATUS_BAR_COLOR.equals(action))
			return setStatusBarColor(args.getInt(0));
		else if (ACTION_SET_SYSTEM_UI_VISIBILITY.equals(action))
			return setSystemUiVisibility(args.getInt(0));
		else if (ACTION_GET_SOFTWARE_KEYS.equals(action))
			return getSoftwareKeys();
		else if (ACTION_GET_DISPLAY_CUTOUT.equals(action))
			return getDisplayCutout();
		else if (ACTION_GET_REAL_SIZE.equals(action))
			return getRealSize();

		return false;
	}

	/**
	 * Get the display cutout information
	 */

	private boolean getDisplayCutout()
	{
        if(Build.VERSION.SDK_INT < 28) {
            // DisplayCutout is not available on api < 28
            callbackContext.sendPluginResult(new PluginResult(PluginResult.Status.OK, 0));
            return true;
        }

		activity.runOnUiThread(new Runnable()
		{
			@Override
			public void run() 
			{
				try
				{
                    final WindowInsets insets = getInsets();
                    final DisplayCutout cutout = insets.getDisplayCutout();
            
                    float dens = 1 / activity.getResources().getDisplayMetrics().density;
                    float bottom = cutout != null ? (cutout.getSafeInsetBottom() * dens) : 0; 
                    float left = cutout != null ? (cutout.getSafeInsetLeft() * dens) : 0; 
                    float right = cutout != null ? (cutout.getSafeInsetRight() * dens) : 0; 
                    float top = cutout != null ? (cutout.getSafeInsetTop() * dens) : 0; 
            
            		callbackContext.sendPluginResult(new PluginResult(PluginResult.Status.OK, left, top, right, bottom));
				}
				catch (Exception e)
				{
					context.error(e.getMessage());
				}
			}
		});
		
		return true;
	}

	private boolean getSoftwareKeys()
	{
        if(Build.VERSION.SDK_INT < 21) {
            callbackContext.sendPluginResult(new PluginResult(PluginResult.Status.OK, 0));
            return true;
        }

		boolean hasSoftwareKeys = true;
	
		activity.runOnUiThread(new Runnable()
		{
			@Override
			public void run() 
			{
				try
				{

					Display d = c.getWindowManager().getDefaultDisplay();
	
					DisplayMetrics realDisplayMetrics = new DisplayMetrics();
					d.getRealMetrics(realDisplayMetrics);
	
					int realHeight = realDisplayMetrics.heightPixels;
					int realWidth = realDisplayMetrics.widthPixels;
	
					DisplayMetrics displayMetrics = new DisplayMetrics();
					d.getMetrics(displayMetrics);
	
					int displayHeight = displayMetrics.heightPixels;
					int displayWidth = displayMetrics.widthPixels;
	
					hasSoftwareKeys =  (realWidth - displayWidth) > 0 ||
									   (realHeight - displayHeight) > 0;

					callbackContext.sendPluginResult(new PluginResult(PluginResult.Status.OK, hasSoftwareKeys));
				} 
				catch (Exception e)
				{
					context.error(e.getMessage());
				}
			}
		});
		
		return true;
	}

	/**
	 * Get the displays real width
	 */
	private boolean getRealSize() {
		activity.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				try {
					final Point outSize = new Point();

					decorView.getDisplay().getRealSize(outSize);

					final PluginResult res = new PluginResult(PluginResult.Status.OK, outSize.x, outSize.y);
					context.sendPluginResult(res);
				} catch (final Exception e) {
					context.error(e.getMessage());
				}
			}
		});

		return true;
	}

	private boolean setSystemUiVisibility(final int visibility) {
		activity.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				try {
					decorView.setOnFocusChangeListener(null);
					decorView.setOnSystemUiVisibilityChangeListener(null);
					window.clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
					decorView.setSystemUiVisibility(visibility);
					context.success();
				} catch (final Exception e) {
					context.error(e.getMessage());
				}
			}
		});

		return true;
	}

	private void setStatusBarBackgroundColor(final String colorPref) {
		if (Build.VERSION.SDK_INT >= 21) {
			if (colorPref != null && !colorPref.isEmpty()) {
				window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
				window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
				try {
					window.setStatusBarColor(Color.parseColor(colorPref));
				} catch (final IllegalArgumentException ignore) {
					LOG.e(TAG, "Invalid hexString argument, use f.i. '#999999'");
				} catch (final Exception ignore) {
					LOG.w(TAG, "Method window.setStatusBarColor not found for SDK level " + Build.VERSION.SDK_INT);
				}
			}
		}
	}

	private void setNavigationBarBackgroundColor(final String colorPref) {
		if (Build.VERSION.SDK_INT >= 21) {
			if (colorPref != null && !colorPref.isEmpty()) {
				window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
				window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
				try {
					window.setNavigationBarColor(Color.parseColor(colorPref));
				} catch (final IllegalArgumentException ignore) {
					LOG.e(TAG, "Invalid hexString argument, use f.i. '#999999'");
				} catch (final Exception ignore) {
                    LOG.w(TAG, "Method window.setStatusBarColor not found for SDK level " + Build.VERSION.SDK_INT);
                }
            }
        }
    }

    @TargetApi(23)
    private WindowInsets getInsets() {
        return this.webView.getView().getRootWindowInsets();
    }
}