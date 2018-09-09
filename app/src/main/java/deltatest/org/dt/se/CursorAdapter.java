package deltatest.org.dt.se;

import android.content.Context;
import android.database.Cursor;
import android.database.DataSetObserver;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class CursorAdapter extends RecyclerView.Adapter<QH> {
    private Cursor cursor;
    private boolean mDataValid;
    private int mRowIdColumn;
    private Cursor cursorA;
    private static QuestDAO questDAO;

    public CursorAdapter(Cursor cursor, Context context) {
        questDAO = new QuestDAO(context);
        this.cursor = cursor;
        mDataValid = cursor != null;
        mRowIdColumn = mDataValid ? cursor.getColumnIndex("_id") : -1;
        mDataSetObserver = new NotifyingDataSetObserver();
        if (cursor != null) {
            cursor.registerDataSetObserver(mDataSetObserver);
        }
    }

    private DataSetObserver mDataSetObserver;

    @NonNull
    @Override

    public QH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_question_layot, parent, false);
        return new QH(inflate);

    }

    @Override
    public void onBindViewHolder(@NonNull QH holder, int position) {
        if (!mDataValid) {
            throw new IllegalStateException("this should only be called when the cursor is valid");
        }
        if (!cursor.moveToPosition(position)) {
            throw new IllegalStateException("couldn't move cursor to position " + position);
        }
        Question question = fromCursor(cursor, position);
        holder.bind(question);

    }

    private Question fromCursor(Cursor cursor, int position) {
        Question question = new Question();
        cursor.moveToPosition(position);
        int id = cursor.getInt(0);
        question.setId(id);

        question.setBody(stringToUpperCase(cursor.getString(1)));
        question.setCategory(stringToUpperCase(cursor.getString(2)));
        question.setImg(cursor.getString(3));

        cursorA = questDAO.getAnsCursor(id);
        try {
            if (cursorA.moveToFirst()) {
                do {
                    Ans ans = new Ans();
                    ans.setId(cursorA.getInt(0));
                    ans.setBody((cursorA.getString(1)));
                    ans.setRight(cursorA.getInt(2));
                    question.getAns().add(ans);
                } while (cursorA.moveToNext());
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursorA != null) {
                cursorA.close();
            }
        }

        return question;
    }

    private String stringToUpperCase(String s) {
        return s != null && s.length() != 0 ? s.substring(0, 1).toUpperCase() + s.substring(1) : null;
    }

    @Override
    public int getItemCount() {
        if (mDataValid && cursor != null) {
            return cursor.getCount();
        }
        return 0;
    }

    public Cursor getCursor() {
        return cursor;
    }

    @Override
    public long getItemId(int position) {
        if (mDataValid && cursor != null && cursor.moveToPosition(position)) {
            return cursor.getLong(mRowIdColumn);
        }
        return 0;
    }

    @Override
    public void setHasStableIds(boolean hasStableIds) {
        super.setHasStableIds(true);
    }

    public void changeCursor(Cursor cursor) {
        Cursor old = swapCursor(cursor);
        if (old != null) {
            old.close();
        }
    }

    public Cursor swapCursor(Cursor newCursor) {
        if (newCursor == cursor) {
            return null;
        }
        final Cursor oldCursor = cursor;
        if (oldCursor != null && mDataSetObserver != null) {
            oldCursor.unregisterDataSetObserver(mDataSetObserver);
        }
        cursor = newCursor;
        if (cursor != null) {
            if (mDataSetObserver != null) {
                cursor.registerDataSetObserver(mDataSetObserver);
            }
            mRowIdColumn = newCursor.getColumnIndexOrThrow("_id");
            mDataValid = true;
            notifyDataSetChanged();
        } else {
            mRowIdColumn = -1;
            mDataValid = false;
            notifyDataSetChanged();
            //There is no notifyDataSetInvalidated() method in RecyclerView.Adapter
        }
        return oldCursor;
    }

    private class NotifyingDataSetObserver extends DataSetObserver {
        @Override
        public void onChanged() {
            super.onChanged();
            mDataValid = true;
            notifyDataSetChanged();
        }

        @Override
        public void onInvalidated() {
            super.onInvalidated();
            mDataValid = false;
            notifyDataSetChanged();
            //There is no notifyDataSetInvalidated() method in RecyclerView.Adapter
        }
    }

}
