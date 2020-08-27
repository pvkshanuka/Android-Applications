package Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SQLiteHelper extends SQLiteOpenHelper {

    public SQLiteHelper(Context context){
        super(context,"hotelrefresh",null,1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        System.out.println("onCreate");

        db.execSQL("CREATE TABLE venue (\n" +
                "    ve_id   INTEGER PRIMARY KEY AUTOINCREMENT\n" +
                "                    NOT NULL,\n" +
                "    name    VARCHAR,\n" +
                "    occ     INTEGER,\n" +
                "    area    DOUBLE,\n" +
                "    height  DOUBLE,\n" +
                "    img,\n" +
                "    details VARCHAR,\n" +
                "    details2 VARCHAR,\n" +
                "    status  INTEGER\n" +
                ");\n");

        db.execSQL("CREATE TABLE customer (\n" +
                "    cus_id INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
                "    name   VARCHAR,\n" +
                "    uname  VARCHAR,\n" +
                "    pword  VARCHAR,\n" +
                "    status INTEGER\n" +
                ");\n");

        db.execSQL("CREATE TABLE packagereservation (\n" +
                "    pr_id   INTEGER  PRIMARY KEY AUTOINCREMENT,\n" +
                "    cus_id  INTEGER  NOT NULL,\n" +
                "    pa_id   INTEGER  NOT NULL,\n" +
                "    ptime   DATETIME,\n" +
                "    pdate   DATETIME,\n" +
                "    pmethod VARCHAR,\n" +
                "    status  INTEGER\n" +
                ");\n");

        db.execSQL("CREATE TABLE package (\n" +
                "    pa_id  INTEGER PRIMARY KEY AUTOINCREMENT\n" +
                "                   NOT NULL,\n" +
                "    ve_id  INTEGER NOT NULL,\n" +
                "    hours  INTEGER,\n" +
                "    price  DOUBLE,\n" +
                "    offer  DOUBLE,\n" +
                "    status INTEGER\n" +
                ");\n");

        System.out.println("Inserting Data..!");


        db.execSQL("INSERT INTO venue(name,occ,area,height,details,details2,status) VALUES ('Hall 1','200',2500,2.5,'Upholding its reputation to the name,the grandest venues at NAME is located on the 3rd floor, with a panomaric view of the lake and beyond.the unique column free feature ensures the largest seating capacity for aminimum of xxx guests.','Details2',1)");
        db.execSQL("INSERT INTO venue(name,occ,area,height,details,details2,status) VALUES ('Hall 2','300',3000,2.8,'Upholding its reputation to the name,the grandest venues at NAME is located on the 3rd floor, with a panomaric view of the lake and beyond.the unique column free feature ensures the largest seating capacity for aminimum of xxx guests.','Details2',1)");
        db.execSQL("INSERT INTO venue(name,occ,area,height,details,details2,status) VALUES ('Hall 3','400',4000,3,'Upholding its reputation to the name,the grandest venues at NAME is located on the 3rd floor, with a panomaric view of the lake and beyond.the unique column free feature ensures the largest seating capacity for aminimum of xxx guests.','Details2',1)");
        db.execSQL("INSERT INTO venue(name,occ,area,height,details,details2,status) VALUES ('Hall 4','500',5200,3,'Upholding its reputation to the name,the grandest venues at NAME is located on the 3rd floor, with a panomaric view of the lake and beyond.the unique column free feature ensures the largest seating capacity for aminimum of xxx guests.','Details2',1)");
        db.execSQL("INSERT INTO venue(name,occ,area,height,details,details2,status) VALUES ('Hall 5','600',5800,3.2,'Upholding its reputation to the name,the grandest venues at NAME is located on the 3rd floor, with a panomaric view of the lake and beyond.the unique column free feature ensures the largest seating capacity for aminimum of xxx guests.','Details2',1)");
        db.execSQL("INSERT INTO venue(name,occ,area,height,details,details2,status) VALUES ('Hall 6','700',6200,4,'Upholding its reputation to the name,the grandest venues at NAME is located on the 3rd floor, with a panomaric view of the lake and beyond.the unique column free feature ensures the largest seating capacity for aminimum of xxx guests.','Details2',1)");
        db.execSQL("INSERT INTO venue(name,occ,area,height,details,details2,status) VALUES ('Hall 7','800',7000,4,'Upholding its reputation to the name,the grandest venues at NAME is located on the 3rd floor, with a panomaric view of the lake and beyond.the unique column free feature ensures the largest seating capacity for aminimum of xxx guests.','Details2',1)");

        db.execSQL("INSERT INTO customer(name,uname,pword,status) VALUES ('Asel Kalmith','kalmith1212@gmail.com','1212',1)");
        db.execSQL("INSERT INTO customer(name,uname,pword,status) VALUES ('Chammika Lakshan','chammilak@gmail.com','1234',1)");


        System.out.println("Inserted");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        System.out.println("onUpgrade");
    }
}
