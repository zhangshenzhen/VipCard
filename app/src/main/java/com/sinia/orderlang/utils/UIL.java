package com.sinia.orderlang.utils;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.LoadedFrom;
import com.nostra13.universalimageloader.core.display.BitmapDisplayer;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.imageaware.ImageAware;

import android.graphics.Bitmap;

public class UIL {

	public static DisplayImageOptions getOptions(int id) {
		DisplayImageOptions options = new DisplayImageOptions.Builder()
				.showImageOnLoading(id).cacheInMemory(true).cacheOnDisk(true)
				.bitmapConfig(Bitmap.Config.RGB_565)
				.displayer(new BitmapDisplayer() {
					@Override
					public void display(Bitmap arg0, ImageAware arg1,
							LoadedFrom arg2) {
						arg1.setImageBitmap(arg0);
						String imgFrom = arg2.toString();
						if (imgFrom.equals("NETWORK")) {
							FadeInBitmapDisplayer.animate(
									arg1.getWrappedView(), 500);
						}
					}
				}).build();

		return options;
	}
}
