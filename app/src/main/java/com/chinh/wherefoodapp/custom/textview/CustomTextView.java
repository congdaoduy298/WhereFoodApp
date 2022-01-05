package com.chinh.wherefoodapp.custom.textview;

import android.content.Context;
import android.graphics.Typeface;

public class CustomTextView {
    private static Typeface RockStar;
    private static Typeface AmpleSoft;
    private static Typeface Glowist;

    public static Typeface getGlowist(Context context) {
        if(Glowist == null){
            Glowist= Typeface.createFromAsset(context.getAssets(),"fonts/Glowist.otf");
        }
        return Glowist;
    }

    public static Typeface getAmpleSoft(Context context) {
        if(AmpleSoft == null){
            AmpleSoft= Typeface.createFromAsset(context.getAssets(),"fonts/#9Slide03 AMPLESOFT MEDIUM_1.ttf");
        }
        return AmpleSoft;
    }

    public static Typeface getRockStar(Context context) {
        if(RockStar == null){
            RockStar= Typeface.createFromAsset(context.getAssets(),"fonts/RockstarDisplay-Regular.otf");
        }
        return RockStar;
    }

}
