package deltatest.org.dt.se;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class ThemAdapter extends RecyclerView.Adapter<TV> {
    private String[] thems;

    public ThemAdapter(String[] thems) {
        this.thems = thems;
    }

    @NonNull
    @Override
    public TV onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_them_layout, parent, false);
        return new TV(inflate) ;
    }

    @Override
    public void onBindViewHolder(@NonNull TV holder, int position) {
     holder.bind(thems[position]);
    }

    @Override
    public int getItemCount() {
        return thems.length;
    }
}
