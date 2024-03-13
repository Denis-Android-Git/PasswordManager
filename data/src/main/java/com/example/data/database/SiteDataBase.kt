package com.example.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.domain.model.Item
import com.example.domain.repository.DBRepository
import net.sqlcipher.database.SupportFactory

@Database(
    entities = [Item::class],
    version = 2
)
abstract class SiteDataBase : RoomDatabase() {
    abstract fun itemDao(): DBRepository

    companion object {
        @Volatile
        private var INSTANCE: SiteDataBase? = null
        fun getDatabase(
            context: Context, passphrase: ByteArray
        ): SiteDataBase {

            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    SiteDataBase::class.java,
                    "SiteDataBase"
                )
                    .openHelperFactory(SupportFactory(passphrase))
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}