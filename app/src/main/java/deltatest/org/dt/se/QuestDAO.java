package deltatest.org.dt.se;

import android.content.Context;
import android.database.Cursor;

import net.sqlcipher.DatabaseUtils;

import java.util.ArrayList;
import java.util.List;


public class QuestDAO extends DBDAO {
    private Cursor cursor;

    public QuestDAO(Context context) {
        super(context);
    }

    public void set() {
        for (int i = 0; i < 15000; i++) {
            database.execSQL("insert into QUEST default values ");
        }
        for (int i = 0; i < 45000; i++) {
            database.execSQL("insert into ANS default values ");
        }
    }


    public Cursor getQuestCursor(String cat) {
        Cursor cursor = null;

        cursor = database.query(DataBaseHelper.TABLE_QUEST,
                new String[]{
                        DataBaseHelper.TABLE_KEY_ID,
                        DataBaseHelper.TABLE_KEY_TEXT,
                        DataBaseHelper.TABLE_KEY_CATEGORY,
                        DataBaseHelper.TABLE_KEY_IMG,
                },
                DataBaseHelper.TABLE_KEY_CATEGORY + " = ? ", new String[]{cat}, null, null, null, null
        );

        return cursor;
    }


    public List<Question> getQuestionsByCat(String cat) {
        Cursor cursor = null;
        List<Question> questions = new ArrayList<>();
        try {
            cursor = database.query(DataBaseHelper.TABLE_QUEST,
                    new String[]{
                            DataBaseHelper.TABLE_KEY_ID,
                            DataBaseHelper.TABLE_KEY_TEXT,
                            DataBaseHelper.TABLE_KEY_CATEGORY,
                            DataBaseHelper.TABLE_KEY_IMG,
                    },
                    DataBaseHelper.TABLE_KEY_CATEGORY + " = ? ", new String[]{cat}, null, null, null, null
            );

            if (cursor.moveToFirst()) {
                do {
                    Question question = new Question();
                    question.setId(cursor.getInt(0));
                    question.setBody(stringToUpperCase(cursor.getString(1)));
                    question.setCategory(stringToUpperCase(cursor.getString(2)));
                    question.setImg(cursor.getString(3));
                    questions.add(question);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        getAns(questions);

        return questions;
    }

    public List<Question> getQuestionsByCatPag(String cat, int limit, int offset) {
        Cursor cursor = null;
        List<Question> questions = new ArrayList<>();
        try {
            cursor = database.query(DataBaseHelper.TABLE_QUEST,
                    new String[]{
                            DataBaseHelper.TABLE_KEY_ID,
                            DataBaseHelper.TABLE_KEY_TEXT,
                            DataBaseHelper.TABLE_KEY_CATEGORY,
                            DataBaseHelper.TABLE_KEY_IMG,
                    },
                    DataBaseHelper.TABLE_KEY_CATEGORY + " = ? limit ? offset ? ", new String[]{cat, String.valueOf(limit), String.valueOf(offset)}, null, null, null, null
            );

            if (cursor.moveToFirst()) {
                do {
                    Question question = new Question();
                    question.setId(cursor.getInt(0));
                    question.setBody(stringToUpperCase(cursor.getString(1)));
                    question.setCategory(stringToUpperCase(cursor.getString(2)));
                    question.setImg(cursor.getString(3));
                    questions.add(question);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        getAns(questions);

        return questions;
    }

    private String stringToUpperCase(String s) {
        return s != null && s.length() != 0 ? s.substring(0, 1).toUpperCase() + s.substring(1) : null;
    }

    private void getAns(List<Question> questions) {
        for (Question q : questions) {
            List<Ans> ans = getAns(q.getId());
            q.getAns().addAll(ans);
        }
    }

    private List<Ans> getAns(int id) {
        Cursor cursor = null;
        List<Ans> ansList = new ArrayList<>();
        try {
            cursor = database.query(DataBaseHelper.TABLE_ANS,
                    new String[]{
                            DataBaseHelper.TABLE_KEY_ID,
                            DataBaseHelper.TABLE_KEY_TEXT,
                            DataBaseHelper.TABLE_KEY_RIGHT},
                    DataBaseHelper.TABLE_KEY_ID_QUESTION + " =? ", new String[]{String.valueOf(id)}, null, null, null, null);


            if (cursor.moveToFirst()) {
                do {
                    Ans ans = new Ans();
                    ans.setId(cursor.getInt(0));
                    ans.setBody((cursor.getString(1)));
                    ans.setRight(cursor.getInt(2));
                    ansList.add(ans);

                } while (cursor.moveToNext());
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return ansList;
    }

    public int getCount(String cat) {
        String databaseCompare;
        if (cat == null) {
            databaseCompare = "select count(*) from " + DataBaseHelper.TABLE_QUEST;
        } else
            databaseCompare = "select count(*) from " + DataBaseHelper.TABLE_QUEST + " where " + DataBaseHelper.TABLE_KEY_CATEGORY + " = " + cat;
        int l = (int) DatabaseUtils.longForQuery(database, databaseCompare, null);
        return l;
    }

    public int getCountSearch(String cat, String seacrhText) {
        String databaseCompare;
        if (cat == null) {
            databaseCompare = "select count(*) from " + DataBaseHelper.TABLE_QUEST;
        } else
            databaseCompare = "select count(*) from " + DataBaseHelper.TABLE_QUEST + " where " + DataBaseHelper.TABLE_KEY_CATEGORY + " = " + cat + " and text like '%" + seacrhText + "%'";
        int l = (int) DatabaseUtils.longForQuery(database, databaseCompare, null);
        return l;
    }

    public List<Question> search(String cat, int limit, int offset, String searchText) {
        Cursor cursor = null;
        List<Question> questions = new ArrayList<>();
        try {
            cursor = database.query(DataBaseHelper.TABLE_QUEST,
                    new String[]{
                            DataBaseHelper.TABLE_KEY_ID,
                            DataBaseHelper.TABLE_KEY_TEXT,
                            DataBaseHelper.TABLE_KEY_CATEGORY,
                            DataBaseHelper.TABLE_KEY_IMG,
                    },
                    DataBaseHelper.TABLE_KEY_CATEGORY + " = ? and text like ? limit ? offset ? ", new String[]{cat, "%" + searchText + "%", String.valueOf(limit), String.valueOf(offset)}, null, null, null, null
            );

            if (cursor.moveToFirst()) {
                do {
                    Question question = new Question();
                    question.setId(cursor.getInt(0));
                    question.setBody(stringToUpperCase(cursor.getString(1)));
                    question.setCategory(stringToUpperCase(cursor.getString(2)));
                    question.setImg(cursor.getString(3));
                    questions.add(question);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        getAns(questions);

        return questions;
    }

    public Cursor getAnsCursor(int id) {
        Cursor cursor = null;
        cursor = database.query(DataBaseHelper.TABLE_ANS,
                new String[]{
                        DataBaseHelper.TABLE_KEY_ID,
                        DataBaseHelper.TABLE_KEY_TEXT,
                        DataBaseHelper.TABLE_KEY_RIGHT},
                DataBaseHelper.TABLE_KEY_ID_QUESTION + " =? ", new String[]{String.valueOf(id)}, null, null, null, null);


        return cursor;
    }

    public Cursor getSearchCursor(String search, String cat) {
        System.out.println(search);
        Cursor cursor = null;
            cursor = database.query(DataBaseHelper.TABLE_QUEST,
                    new String[]{
                            DataBaseHelper.TABLE_KEY_ID,
                            DataBaseHelper.TABLE_KEY_TEXT,
                            DataBaseHelper.TABLE_KEY_CATEGORY,
                            DataBaseHelper.TABLE_KEY_IMG,
                    },
                    DataBaseHelper.TABLE_KEY_CATEGORY + " = ? and "+DataBaseHelper.TABLE_KEY_TEXT + " like ? ", new String[]{cat, "%"+String.valueOf(search)+"%"}, null, null, null, null
            );
            return cursor;
    }
}

