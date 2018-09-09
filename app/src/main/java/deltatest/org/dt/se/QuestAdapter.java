package deltatest.org.dt.se;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

public class QuestAdapter extends RecyclerView.Adapter<QH> {
    private List<Question> questions;

    public QuestAdapter(List<Question> questions) {
        this.questions = questions;
    }
    @NonNull
    @Override
    public QH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_question_layot, parent, false);
        return new QH(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull QH holder, int position) {
        holder.bind(questions.get(position));
    }

    @Override
    public int getItemCount() {
        return questions.size();
    }

    public void setQuestions(List<Question> questions) {
        this.questions = questions;
    }
}
