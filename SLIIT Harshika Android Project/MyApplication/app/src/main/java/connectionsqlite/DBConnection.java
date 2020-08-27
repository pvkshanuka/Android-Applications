package connectionsqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBConnection extends SQLiteOpenHelper {

    public DBConnection(Context context) {
        super(context, "Orders", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query1, query2;
        query1 = "CREATE TABLE driver (\n" +
                "    dri_id   INTEGER      PRIMARY KEY AUTOINCREMENT\n" +
                "                          UNIQUE\n" +
                "                          NOT NULL,\n" +
                "    fname    VARCHAR (45),\n" +
                "    lname    VARCHAR (45),\n" +
                "    email    VARCHAR (45),\n" +
                "    password VARCHAR (45),\n" +
                "    nic      VARCHAR (45),\n" +
                "    age      INTEGER,\n" +
                "    status   INTEGER\n" +
                ");";

        query2 = "CREATE TABLE orders (\n" +
                "    or_id        INTEGER       PRIMARY KEY AUTOINCREMENT\n" +
                "                               NOT NULL,\n" +
                "    dri_id       INTEGER       NOT NULL,\n" +
                "    paddress     VARCHAR (300),\n" +
                "    daddress     VARCHAR (300),\n" +
                "    pdate        LONG,\n" +
                "    deliverytime LONG,\n" +
                "    note         VARCHAR (300),\n" +
                "    status       INTEGER\n" +
                ");";

        db.execSQL(query1);
        db.execSQL(query2);


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }


}