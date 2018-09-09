package deltatest.org.dt.se;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class TV extends RecyclerView.ViewHolder implements View.OnClickListener {
    private TextView textView;
    private Context context;
    private ImageView imageView;
    public TV(View itemView) {
        super(itemView);
        this.context = itemView.getContext();
        textView = itemView.findViewById(R.id.card_them_text);
        imageView = itemView.findViewById(R.id.card_item_image);

        imageView.setOnClickListener(this);
        textView.setOnClickListener(this);
    }
    public void bind(String nameThem){
        textView.setText(nameThem);
    }

    @Override
    public void onClick(View view) {
        Intent intent = QuestActivityCursor.getIntent(context, String.valueOf(getAdapterPosition()+1));
        context.startActivity(intent);
    }
}
