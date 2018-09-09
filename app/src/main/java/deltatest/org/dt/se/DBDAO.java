package deltatest.org.dt.se;

import android.content.Context;

import net.sqlcipher.database.SQLiteDatabase;

import static deltatest.org.dt.se.DataBaseHelper.PASS;


public class DBDAO {
    protected SQLiteDatabase database;
    private DataBaseHelper dataBaseHelper;
    protected Context context;

    public DBDAO(Context context) {
        this.context = context;
        dataBaseHelper = DataBaseHelper.getHelper(context);
        open();
    }

    public void open() {
        if (dataBaseHelper == null)
            dataBaseHelper = DataBaseHelper.getHelper(context);
        database = dataBaseHelper.getWritableDatabase(PASS);

    }

    public void close() {
        dataBaseHelper.close();
        database = null;
    }
}
