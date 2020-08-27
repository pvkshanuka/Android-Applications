package DB;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SQLiteHelper extends SQLiteOpenHelper {


    public SQLiteHelper(Context context){
        super(context,"cusmanage",null,1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        System.out.println("onCreate");

        db.execSQL("CREATE TABLE customer (\n" +
                "    cid   INTEGER PRIMARY KEY AUTOINCREMENT\n" +
                "                    NOT NULL,\n" +
                "    fname    VARCHAR,\n" +
                "    lname     VARCHAR,\n" +
                "    cno     INTEGER,\n" +
                "    email    VARCHAR,\n" +
                "    pword  VARCHAR,\n" +
                "    status  INTEGER\n" +
                ");\n");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        System.out.println("onUpgrade");
    }

}
