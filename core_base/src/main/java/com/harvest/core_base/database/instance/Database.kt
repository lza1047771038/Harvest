package com.harvest.core_base.database.instance

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.harvest.core_base.database.bean.CookieCache
import com.harvest.core_base.database.dao.ICookieDao
import com.harvest.core_base.interfaces.IContext
import com.harvest.core_base.service.ServiceFacade

class DBInstance {
    companion object {

        @JvmStatic
        @Synchronized
        fun <T> getInstance(clazz: Class<T>, dbName: String): T where T : RoomDatabase {
            var dbService = ServiceFacade.getInstance().get(clazz)
            if (dbService == null) {
                val context =
                    ServiceFacade.getInstance().get(IContext::class.java).applicationContext
                dbService = Room.databaseBuilder(context, clazz, dbName)
                    .build()
                ServiceFacade.getInstance().put(clazz, dbService)
            }
            return dbService
        }

        @JvmStatic
        fun getAppDatabase(): CommonDatabase {
            return getInstance(CommonDatabase::class.java, CommonDatabase.dbName)
        }
    }
}

@Database(entities = [CookieCache::class], version = 1, exportSchema = false)
abstract class CommonDatabase : RoomDatabase() {
    companion object {
        const val dbName: String = "CommonDataBase"
    }

    abstract val cookieDao: ICookieDao
}