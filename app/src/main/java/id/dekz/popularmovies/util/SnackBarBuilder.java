package id.dekz.popularmovies.util;

import android.support.design.widget.Snackbar;
import android.view.View;

/**
 * Created by DEKZ on 6/16/2017.
 */

public class SnackBarBuilder {

    public static void showMessage(View view, String msg){
        Snackbar snackbarWarning = Snackbar.make(view, msg, Snackbar.LENGTH_LONG);
        snackbarWarning.show();
    }
}
