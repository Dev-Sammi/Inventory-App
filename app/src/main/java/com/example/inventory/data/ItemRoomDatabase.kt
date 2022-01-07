package com.example.inventory.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Database(entities = [Item::class], version = 1, exportSchema = false)
abstract class ItemRoomDatabase : RoomDatabase() {
    abstract fun itemDao(): ItemDao

    class ItemDatabaseCallback(private val applicationScope: CoroutineScope): RoomDatabase.Callback(){
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            INSTANCE?.let { dataBase ->
                applicationScope.launch {
                    dataBase.itemDao().apply {
                        insert(Item(itemName = "Nails", itemPrice = 5.25, quantityInStock = 5))
                        insert(Item(itemName = "Iron rod", itemPrice = 20.5, quantityInStock = 8))
                        insert(Item(itemName = "Cement", itemPrice = 25.25, quantityInStock = 7))
                        insert(Item(itemName = "Paint", itemPrice = 40.30, quantityInStock = 3))
                        insert(Item(itemName = "Chair", itemPrice = 1.8, quantityInStock = 17))
                        insert(Item(itemName = "Fan", itemPrice = 7.01, quantityInStock = 15))
                        insert(Item(itemName = "Dinning Tables", itemPrice = 8.21, quantityInStock = 7))
                        insert(Item(itemName = "Flower", itemPrice = 4.00, quantityInStock = 50))
                    }
                }
            }
        }

    }

    companion object {
        @Volatile
        private var INSTANCE: ItemRoomDatabase? = null
        fun getDatabase(context: Context, applicationScope: CoroutineScope): ItemRoomDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ItemRoomDatabase::class.java,
                    "item_database"
                )
                    .fallbackToDestructiveMigration()
                    .addCallback(ItemDatabaseCallback(applicationScope))
                    .build()
                INSTANCE = instance
                return instance
            }
        }
    }
}
