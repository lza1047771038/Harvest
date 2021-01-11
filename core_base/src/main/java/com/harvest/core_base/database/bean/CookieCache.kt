package com.harvest.core_base.database.bean

import androidx.room.ColumnInfo
import androidx.room.Entity
import okhttp3.Cookie
import okhttp3.HttpUrl

@Entity(tableName = "cookies")
class CookieCache {
    @ColumnInfo
    var targetUrl: String? = null

    @ColumnInfo
    var name: String? = null

    @ColumnInfo
    var value: String? = null

    @ColumnInfo
    var domain: String? = null

    @ColumnInfo
    var expiresAt: Long = 0

    @ColumnInfo
    var path: String? = null

    @ColumnInfo
    var secure = false

    @ColumnInfo
    var httpOnly = false

    @ColumnInfo
    var persistent = false

    @ColumnInfo
    var hostOnly = false

    companion object {
        @JvmStatic
        fun toCookieCache(url: HttpUrl, cookie: Cookie): CookieCache {
            return CookieCache().apply {
                targetUrl = url.toString()
                name = cookie.name()
                value = cookie.value()
                domain = cookie.domain()
                expiresAt = cookie.expiresAt()
                path = cookie.path()
                secure = cookie.secure()
                httpOnly = cookie.httpOnly()
                persistent = cookie.persistent()
                hostOnly = cookie.hostOnly()
            }
        }

        @JvmStatic
        fun toCookieCaches(url: HttpUrl, cookies: List<Cookie>): Array<CookieCache> {
            return Array<CookieCache>(cookies.size) {
                toCookieCache(url, cookies[it])
            }
        }

        @JvmStatic
        fun toCookie(cookieCache: CookieCache): Cookie {
            return Cookie.Builder().name(cookieCache.name)
                .value(cookieCache.value)
                .domain(cookieCache.domain)
                .expiresAt(cookieCache.expiresAt)
                .path(cookieCache.path)
                .build()
        }

        @JvmStatic
        fun toCookies(cookieCaches: List<CookieCache>): List<Cookie> {
            val cookieList = ArrayList<Cookie>()
            for (cookieCache in cookieCaches) {
                cookieList.add(toCookie(cookieCache))
            }
            return cookieList
        }
    }
}