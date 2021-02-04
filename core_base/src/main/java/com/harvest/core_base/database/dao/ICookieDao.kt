package com.harvest.core_base.database.dao

import androidx.room.*
import com.harvest.core_base.database.bean.CookieCache

@Dao
interface ICookieDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveCookies(vararg cookies: CookieCache)

    @Query("select * from cookies where targetUrl = :httpUrl")
    fun getCookies(httpUrl: String): List<CookieCache>

    @Query("delete from cookies where targetUrl = :httpUrl")
    fun deleteCookies(httpUrl: String)
}