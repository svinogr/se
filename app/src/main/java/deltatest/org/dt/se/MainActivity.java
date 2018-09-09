package deltatest.org.dt.se;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import net.sqlcipher.database.SQLiteDatabase;

public class MainActivity extends AppCompatActivity {
    private static String[] thems = new String[]{
            "Старший электромеханик"

    };
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SQLiteDatabase.loadLibs(this);

        DataBaseHelper helper = DataBaseHelper.getHelper(this);
        helper.create_db();

        recyclerView = findViewById(R.id.main_rec);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        ThemAdapter themAdapter = new ThemAdapter(thems);
        recyclerView.setAdapter(themAdapter);

    }
}
