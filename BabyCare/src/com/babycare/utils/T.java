package com.babycare.utils;


import android.content.Context;
import android.view.Gravity;
import android.widget.Toast;

/**
 * 
 * @author way
 * 
 */
public class T {
	// Toast
	private static Toast toast;

	/**
	 * 
	 * 
	 * @param context
	 * @param message
	 */
	public static void showShort(Context context, CharSequence message) {
		if (null == toast) {
			toast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
		} else {
			toast.setText(message);
		}
		toast.show();
	}


	/**
	 * 
	 * 
	 * @param context
	 * @param message
	 */
	public static void showLong(Context context, CharSequence message) {
		if (null == toast) {
			toast = Toast.makeText(context, message, Toast.LENGTH_LONG);
			 toast.setGravity(Gravity.CENTER, 0, 0);
		} else {
			toast.setText(message);
		}
		toast.show();
	}



//	/**
//	 * 
//	 * 
//	 * @param context
//	 * @param message
//	 * @param duration
//	 */
//	private static void show(Context context, CharSequence message, int duration) {
//		if (null == toast) {
//			toast = Toast.makeText(context, message, duration);
//			// toast.setGravity(Gravity.CENTER, 0, 0);
//		} else {
//			toast.setText(message);
//		}
//		toast.show();
//	}

//	/**
//	 * 
//	 * 
//	 * @param context
//	 * @param message
//	 * @param duration
//	 */
//	public static void show(Context context, int message, int duration) {
//		if (null == toast) {
//			toast = Toast.makeText(context, message, duration);
//			// toast.setGravity(Gravity.CENTER, 0, 0);
//		} else {
//			toast.setText(message);
//		}
//		toast.show();
//	}
//
//	/** Hide the toast, if any. */
//	public static void hideToast() {
//		if (null != toast) {
//			toast.cancel();
//		}
//	}
}
