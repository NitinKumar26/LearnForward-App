package com.learnforward.CustomImageView;

public interface GestureImageViewListener {

    public void onTouch(float x, float y);

    public void onScale(float scale);

    public void onPosition(float x, float y);

}
