package Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SQLiteHelper extends SQLiteOpenHelper {

    public SQLiteHelper(Context context){
        super(context,"usersdb",null,1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        System.out.println("OnCreate");
        db.execSQL("CREATE TABLE user (id INTEGER PRIMARY KEY AUTOINCREMENT,name VARCHAR(20),mobile VARCHAR(20),password VARCHAR(20))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        System.out.println("OnUpgrade");
    }
}
