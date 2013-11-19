/*
 * Copyright 2013 Peng fei Pan
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package me.xiaopan.easy.network.android.examples;

import me.xiaopan.easy.network.android.R;
import me.xiaopan.easy.network.android.image.ImageLoader;
import me.xiaopan.easy.network.android.image.OptionsFactory;
import android.app.Application;

public class MyApplication extends Application {
	@Override
	public void onCreate() {
		super.onCreate();
		ImageLoader.getInstance().getConfiguration().getDefaultOptions().setLoadingImageResource(R.drawable.images_loading).setLoadFailureImageResource(R.drawable.images_load_failure);
		OptionsFactory.getListOptions().setLoadingImageResource(R.drawable.images_loading).setLoadFailureImageResource(R.drawable.images_load_failure);
	}
}