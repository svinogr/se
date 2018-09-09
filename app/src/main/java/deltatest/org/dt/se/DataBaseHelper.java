package deltatest.org.dt.se;

import android.content.Context;

import net.sqlcipher.database.SQLiteDatabase;
import net.sqlcipher.database.SQLiteException;
import net.sqlcipher.database.SQLiteOpenHelper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;


public class DataBaseHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 2;
    public static final String DATABASE_NAME = "rw.db";
    public static final String TABLE_QUEST = "QUEST";
    public static final String TABLE_ANS = "ANS";
    private static String DB_PATH;
    private Context context;
    public static final String PASS = "*?~@4b4kSw8a";

    public static final String TABLE_KEY_ID = "_id";
    public static final String TABLE_KEY_TEXT = "text";
    public static final String TABLE_KEY_IMG = "img";
    public static final String TABLE_KEY_CATEGORY = "cat";
    public static final String TABLE_KEY_RIGHT = "pravil";
    public static final String TABLE_KEY_ID_QUESTION = "id_quest";

    private static DataBaseHelper instance;

    private static final String CREATE_QUEST_TABLE =
            "CREATE TABLE " + TABLE_QUEST +
                    "(" +
                    TABLE_KEY_ID + " integer NOT NULL primary key autoincrement, " +
                    TABLE_KEY_TEXT + " text, " +
                    TABLE_KEY_CATEGORY + " integer, " +
                    TABLE_KEY_IMG + " text)";


    private static final String CREATE_ANS_TABLE =
            "CREATE TABLE " + TABLE_ANS +
                    "(" +
                    TABLE_KEY_ID + " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, " +
                    TABLE_KEY_TEXT + " text, " +
                    TABLE_KEY_RIGHT + " INTEGER, " +
                    TABLE_KEY_ID_QUESTION + " INTEGER )";


    private DataBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
        //TODO здесь адрес базы нужно изменить
        System.out.println(context);
        DB_PATH = "/data/data/deltatest.org.dt.se/databases/" + DATABASE_NAME;
    }

    public static synchronized DataBaseHelper getHelper(Context context) {
        if (instance == null) {
            instance = new DataBaseHelper(context);
        }
        return instance;
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        if (!db.isReadOnly()) {
            db.execSQL("PRAGMA foreign_keys=ON;");
        }
    }

    public void create_db() {
        InputStream myInput = null;
        OutputStream myOutput = null;

        if (checkBD()) {
            return;
        }

        try {
            File file = new File(DB_PATH);
            if (!file.exists()) {
                this.getReadableDatabase(PASS);

                myInput = context.getAssets().open(DATABASE_NAME);
                String outFileName = DB_PATH;
                myOutput = new FileOutputStream(outFileName);
                byte[] buffer = new byte[1024];
                int length;

                while ((length = myInput.read(buffer)) > 0) {
                    myOutput.write(buffer, 0, length);
                }

                myOutput.flush();
                myOutput.close();
                myInput.close();
                close();
            }
            seVersionDB();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void deleteBD() {
        File file = new File(DB_PATH);
        if (file.exists()) {
            file.delete();
        }
    }

    private void seVersionDB() {
        System.out.println("DATABASE_VERSION" + DATABASE_VERSION);
        SQLiteDatabase sqLiteDatabase;
        try {
            sqLiteDatabase = getWritableDatabase(PASS);
            sqLiteDatabase.setVersion(DATABASE_VERSION);
            sqLiteDatabase.close();
        } catch (SQLiteException e) {

        }
    }

    private boolean checkBD() {
        SQLiteDatabase sqLiteDatabase;
        try {
            sqLiteDatabase = SQLiteDatabase.openDatabase(DB_PATH, PASS, null, SQLiteDatabase.OPEN_READONLY);
            int version = sqLiteDatabase.getVersion();
            sqLiteDatabase.close();
            if (version < DATABASE_VERSION) {
                deleteBD();
                return false;
            } else return true;
        } catch (SQLiteException e) {
            System.out.println(e);
            return false;
        }
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_QUEST_TABLE);
        db.execSQL(CREATE_ANS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
   /*     sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_QUEST);
        onCreate(sqLiteDatabase);*/

    }
}
