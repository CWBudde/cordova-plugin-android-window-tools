package de.pcjv.awt;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaArgs;
import org.apache.cordova.CordovaInterface;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CordovaWebView;
import org.apache.cordova.LOG;
import org.apache.cordova.PluginResult;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
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
import android.util.DisplayMetrics; 

public class AndroidWindowTools extends CordovaPlugin
{
    private static final String TAG = "AndroidWindowTools";

	public static final String ACTION_SET_NAVIGATION_BAR_COLOR = "setNavigationBarColor";
	public static final String ACTION_SET_STATUS_BAR_COLOR = "setStatusBarColor";
	public static final String ACTION_SET_SYSTEM_UI_VISIBILITY = "setSystemUiVisibility";
	public static final String ACTION_ADD_WINDOW_FLAGS = "addWindowFlags";
	public static final String ACTION_CLEAR_WINDOW_FLAGS = "clearWindowFlags";
	public static final String ACTION_GET_SOFTWARE_KEYS = "getSoftwareKeys";
	public static final String ACTION_GET_DISPLAY_CUTOUT = "getDisplayCutout";
	public static final String ACTION_GET_METRICS = "getMetrics";
	public static final String ACTION_GET_REAL_SIZE = "getRealSize";
	public static final String ACTION_GET_REAL_METRICS = "getRealMetrics";

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
			return setNavigationBarBackgroundColor(args.getString(0));
		else if (ACTION_SET_STATUS_BAR_COLOR.equals(action))
			return setStatusBarBackgroundColor(args.getString(0));
		else if (ACTION_SET_SYSTEM_UI_VISIBILITY.equals(action))
			return setSystemUiVisibility(args.getInt(0));
		else if (ACTION_ADD_WINDOW_FLAGS.equals(action))
			return addWindowFlags(args.getInt(0));
		else if (ACTION_CLEAR_WINDOW_FLAGS.equals(action))
			return clearWindowFlags(args.getInt(0));
		else if (ACTION_GET_SOFTWARE_KEYS.equals(action))
			return getSoftwareKeys();
		else if (ACTION_GET_DISPLAY_CUTOUT.equals(action))
			return getDisplayCutout();
		else if (ACTION_GET_METRICS.equals(action))
			return getMetrics();
		else if (ACTION_GET_REAL_METRICS.equals(action))
			return getRealMetrics();
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
            context.sendPluginResult(new PluginResult(PluginResult.Status.OK, 0));
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
            
					JSONObject json = new JSONObject();
            		json.put("left", left);
            		json.put("top", top);
            		json.put("right", right);
            		json.put("bottom", bottom);

					context.sendPluginResult(new PluginResult(PluginResult.Status.OK, json));
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
            context.sendPluginResult(new PluginResult(PluginResult.Status.OK, 0));
            return true;
        }

		activity.runOnUiThread(new Runnable()
		{
			@Override
			public void run() 
			{
				try
				{
					Display d = decorView.getDisplay();
	
					DisplayMetrics realDisplayMetrics = new DisplayMetrics();
					d.getRealMetrics(realDisplayMetrics);
	
					int realHeight = realDisplayMetrics.heightPixels;
					int realWidth = realDisplayMetrics.widthPixels;
	
					DisplayMetrics displayMetrics = new DisplayMetrics();
					d.getMetrics(displayMetrics);
	
					int displayHeight = displayMetrics.heightPixels;
					int displayWidth = displayMetrics.widthPixels;
	
					context.sendPluginResult(new PluginResult(PluginResult.Status.OK, 
                      (realWidth - displayWidth) > 0 || (realHeight - displayHeight) > 0));
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

					JSONObject json = new JSONObject();
            		json.put("width", outSize.x);
            		json.put("height", outSize.y);

					context.sendPluginResult(new PluginResult(PluginResult.Status.OK, json));
				} catch (final Exception e) {
					context.error(e.getMessage());
				}
			}
		});

		return true;
	}

	private boolean getRealMetrics() {
        if(Build.VERSION.SDK_INT < 21) {
            context.sendPluginResult(new PluginResult(PluginResult.Status.OK, 0));
            return true;
        }

		activity.runOnUiThread(new Runnable()
		{
			@Override
			public void run() 
			{
				try
				{
					Display d = decorView.getDisplay();
	
					DisplayMetrics realDisplayMetrics = new DisplayMetrics();
					d.getRealMetrics(realDisplayMetrics);
	
					int realHeight = realDisplayMetrics.heightPixels;
					int realWidth = realDisplayMetrics.widthPixels;
	
					JSONObject json = new JSONObject();
            		json.put("width", realWidth);
            		json.put("height", realHeight);
	
					context.sendPluginResult(new PluginResult(PluginResult.Status.OK, json));
				} 
				catch (Exception e)
				{
					context.error(e.getMessage());
				}
			}
		});

		return true;
	}

	private boolean getMetrics() {
        if(Build.VERSION.SDK_INT < 21) {
            context.sendPluginResult(new PluginResult(PluginResult.Status.OK, 0));
            return true;
        }

		activity.runOnUiThread(new Runnable()
		{
			@Override
			public void run() 
			{
				try
				{
					Display d = decorView.getDisplay();
	
					DisplayMetrics realDisplayMetrics = new DisplayMetrics();
					d.getMetrics(realDisplayMetrics);
	
					int realHeight = realDisplayMetrics.heightPixels;
					int realWidth = realDisplayMetrics.widthPixels;
	
					JSONObject json = new JSONObject();
            		json.put("width", realWidth);
            		json.put("height", realHeight);
	
					context.sendPluginResult(new PluginResult(PluginResult.Status.OK, json));
				} 
				catch (Exception e)
				{
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
					LOG.e(TAG, "Error setting system UI visibility");
					context.error(e.getMessage());
				}
			}
		});

		return true;
	}
    
	private boolean addWindowFlags(final int flags) {
		activity.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				try {
					window.addFlags(flags);
					context.success();
				} catch (final Exception e) {
					LOG.e(TAG, "Error addng window flags");
					context.error(e.getMessage());
				}
			}
		});

		return true;
	}

	private boolean clearWindowFlags(final int flags) {
		activity.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				try {
					window.clearFlags(flags);
					context.success();
				} catch (final Exception e) {
					LOG.e(TAG, "Error clearing window flags");
					context.error(e.getMessage());
				}
			}
		});

		return true;
	}

	private boolean setStatusBarBackgroundColor(final String colorPref) {
		if (Build.VERSION.SDK_INT >= 21) {
			if (colorPref != null && !colorPref.isEmpty()) {
				this.cordova.getActivity().runOnUiThread(new Runnable() {
					@Override
					public void run() {
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
				});
			}
            return true;
        }
        else
        {
            return false;
        }
	}

	private boolean setNavigationBarBackgroundColor(final String colorPref) {
		if (Build.VERSION.SDK_INT >= 21) {
			if (colorPref != null && !colorPref.isEmpty()) {
				this.cordova.getActivity().runOnUiThread(new Runnable() {
					@Override
					public void run() {
						window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
						window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
						try {
							window.setNavigationBarColor(Color.parseColor(colorPref));
						} catch (final IllegalArgumentException ignore) {
							LOG.e(TAG, "Invalid hexString argument, use f.i. '#999999'");
						} catch (final Exception ignore) {
        		            LOG.w(TAG, "Method window.setNavigationBarColor not found for SDK level " + Build.VERSION.SDK_INT);
						}
					}
				});
            }
            return true;
        }
        else
        {
            return false;
        }
    }

    @TargetApi(23)
    private WindowInsets getInsets() {
        return this.webView.getView().getRootWindowInsets();
    }
}