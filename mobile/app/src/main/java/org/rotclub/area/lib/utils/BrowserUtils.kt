package org.rotclub.area.lib.utils

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Browser
import org.rotclub.area.lib.baseUrl


object BrowserUtils {
    fun openUrl(context: Context, url: String, token: String) {
        var thisUrl = url
        if (!url.startsWith("http://") && !url.startsWith("https://")) {
            thisUrl = "http://$url"
        }

        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(thisUrl))
        val bundle = Bundle()
        bundle.putString("Authorization", "Bearer $token")
        intent.putExtra(Browser.EXTRA_HEADERS, bundle)
        context.startActivity(intent)
    }

    fun hrefToLink(href: String): String {
        var thisHref = href
        if (href.startsWith("/"))
            thisHref = href.substring(1)
        return baseUrl.value + thisHref
    }
}
