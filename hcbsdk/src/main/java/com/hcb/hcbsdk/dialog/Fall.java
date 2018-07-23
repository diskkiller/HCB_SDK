package com.hcb.hcbsdk.dialog;

import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;


/*
 * Copyright 2014 litao
 * https://github.com/sd6352051/NiftyDialogEffects
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
public class Fall extends BaseEffects {

    @Override
    protected void setupAnimation(View view) {
        Animation alpha = new AlphaAnimation( 0, 1);
        alpha.setDuration(mDuration * 3 / 2);
        Animation scale = new ScaleAnimation(2, 1, 2,1,Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        scale.setDuration(mDuration);

        getAnimationSet().addAnimation(alpha);
        getAnimationSet().addAnimation(scale);

        getAnimationSet().setFillAfter(true);

    }
}
