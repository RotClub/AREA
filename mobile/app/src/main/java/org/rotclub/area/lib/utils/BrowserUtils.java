package org.rotclub.area.lib.utils;

import static org.rotclub.area.lib.FrispyGlobalKt.BASE_URL;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Browser;

public class BrowserUtils {
    public static void openUrl(Context context, String url, String token) {
        if (url == null) {
            return;
        }
        if (!url.startsWith("http://") && !url.startsWith("https://")) {
            url = "http://" + url;
        }

        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        Bundle bundle = new Bundle();
        bundle.putString("Authorization", "Bearer " + token);
        intent.putExtra(Browser.EXTRA_HEADERS, bundle);
        context.startActivity(intent);
    }

    public static String hrefToLink(String href) {
        if (href == null)
            return null;
        if (href.startsWith("/"))
            href = href.substring(1);
        return BASE_URL + href;
    }
}
