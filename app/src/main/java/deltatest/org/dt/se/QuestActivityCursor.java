package deltatest.org.dt.se;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

public class QuestActivityCursor extends AppCompatActivity implements IMark {
    private static final String CAT = "cat";
    private static final String MARK = "mark";
    private String cat;
    private RecyclerView recyclerView;
    private CursorAdapter questAdapter;
    private LinearLayoutManager linearLayoutManager;
    private SearchView searchView;
    private QuestDAO questDAO;
    private String search;
    private Cursor cursorQ;

    private boolean searchTime = false;

    private final Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 100) {
                try {

                    int number = Integer.parseInt((String) msg.obj);

                } catch (NumberFormatException e) {
                    search = (String) msg.obj;
                    if (!search.trim().equals("")) {
                        searchTime = true;
                        Cursor searchCursor = questDAO.getSearchCursor(search, cat);
                        questAdapter.changeCursor(searchCursor);
                    } else {
                        searchTime = false;
                        cursorQ = questDAO.getQuestCursor(cat);
                        questAdapter.changeCursor(cursorQ);}


                } catch (IndexOutOfBoundsException e) {
                    recyclerView.stopScroll();
                }
            }
        }
    };

    private final Handler handlerMark = new Handler() {
        @Override
        public void handleMessage(Message msg) {
                int number = (int) msg.obj;
                questAdapter.notifyDataSetChanged();
                linearLayoutManager.scrollToPositionWithOffset(number, 0);
            }

    };


    public static Intent getIntent(Context context, String cat) {
        Intent intent = new Intent(context, QuestActivityCursor.class);
        intent.putExtra(CAT, cat);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quest);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        setTitle("Квалифицированный матрос");
        setTitle("");
        cat = getIntent().getStringExtra(CAT);
        recyclerView = findViewById(R.id.quest_activity_rec);

        questDAO = new QuestDAO(this);
        cursorQ = questDAO.getQuestCursor(cat);
        questAdapter = new CursorAdapter(cursorQ, this);

        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(questAdapter);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.search, menu);
        MenuItem menuItem = menu.findItem(R.id.search);
        searchView = (SearchView) menuItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                handler.removeMessages(100);
                handler.sendMessageDelayed(handler.obtainMessage(100, newText), 250);

                return true;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                finish();
                break;
            case R.id.to_mark:
                    goToMark();

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void setMark(int numberMark) {
        if (searchTime){
            return;
        }
        SharedPreferences.Editor editor = getSharedPreferences(MARK, Context.MODE_PRIVATE).edit();
        editor.putInt(MARK, numberMark);
        editor.apply();
        Toast.makeText(this, "Закладка установлена", Toast.LENGTH_LONG).show();
    }

    @Override
    public void goToMark() {
        SharedPreferences sharedPreferences = getSharedPreferences(MARK, Context.MODE_PRIVATE);
        if (sharedPreferences.contains(MARK)) {
            // Получаем число из настроек
            final int mark = sharedPreferences.getInt(MARK, 1);
            handler.removeMessages(105);
            handlerMark.sendMessageDelayed(handler.obtainMessage(105, mark), 0);

        } else
            Toast.makeText(this, "Для создания закладки прикоснитесь и подержите нужный вопрос", Toast.LENGTH_LONG).show();
    }
}
