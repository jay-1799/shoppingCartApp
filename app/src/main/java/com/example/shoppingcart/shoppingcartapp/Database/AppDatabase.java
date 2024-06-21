package com.example.shoppingcart.shoppingcartapp.Database;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.VisibleForTesting;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.shoppingcart.shoppingcartapp.Model.Cart;
import com.example.shoppingcart.shoppingcartapp.Model.Product;
import com.example.shoppingcart.shoppingcartapp.Model.User;

@Database(entities = {User.class, Product.class, Cart.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {

    private static AppDatabase sInstance;

    private boolean mIsDatabaseCreated = false;

    @VisibleForTesting
    private static final String DATABASE_NAME = "shopping-cart-db";

    public abstract ProductDao productDao();

    public abstract UserDao userDao();

    public abstract CartDao cartDao();

    public static AppDatabase getInstance(final Context context) {
        if (sInstance == null) {
            synchronized (AppDatabase.class) {
                if (sInstance == null) {
                    sInstance = buildDatabase(context.getApplicationContext());
                    sInstance.updateDatabaseCreated(context.getApplicationContext());
                }
            }
        }
        return sInstance;
    }

    /**
     * Build the database. {@link Builder#build()} only sets up the database configuration and
     * creates a new instance of the database.
     * The SQLite database is only created when it's accessed for the first time.
     */
    private static AppDatabase buildDatabase(final Context appContext) {
        return Room.databaseBuilder(appContext, AppDatabase.class, DATABASE_NAME)
                .addCallback(new Callback() {
                    @Override
                    public void onCreate(@NonNull SupportSQLiteDatabase db) {
                        super.onCreate(db);
                        //handle callback here, if necessary
                    }
                })
                .allowMainThreadQueries()
                .build();
    }

    /**
     * Check whether the database already exists and expose it via {@link #getDatabaseCreated()}
     */
    private void updateDatabaseCreated(final Context context) {
        if (context.getDatabasePath(DATABASE_NAME).exists()) {
            setDatabaseCreated();
        }
    }

    private void setDatabaseCreated(){
        mIsDatabaseCreated = true;
    }

    public boolean getDatabaseCreated() {
        return mIsDatabaseCreated;
    }
}
