package com.iamdilipkumar.movies.movies.utilities;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.ShareCompat;

import com.iamdilipkumar.movies.movies.R;

import butterknife.BindString;

/**
 * Created on 18/04/17.
 *
 * @author dilipkumar4813
 * @version 1.0
 */

public class ShareUtils {

    @BindString(R.string.mime_type)
    String mMimeType;

    @BindString(R.string.share_title)
    String mShareTitle;

    @BindString(R.string.share_content)
    String mShareContent;

    /**
     * Method to build the builder for sharing
     *
     * @param activity - The activity in which the share has to be triggered
     */
    public void shareApp(Activity activity){
        ShareCompat.IntentBuilder
                .from(activity)
                .setType(mMimeType)
                .setChooserTitle(mShareTitle)
                .setText(mShareContent)
                .startChooser();
    }
}
