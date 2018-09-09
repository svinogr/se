package deltatest.org.dt.se;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckedTextView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;

public class QH extends RecyclerView.ViewHolder {
    private Question question;
    private TextView quest;
    private TextView number;
    private LinearLayout linearLayout;
    private ImageView imageView;
    private Context context;

    public QH(View itemView) {
        super(itemView);
        this.context = itemView.getContext();
        quest = itemView.findViewById(R.id.question);
        number = itemView.findViewById(R.id.number);
        linearLayout = itemView.findViewById(R.id.list_answer);
        imageView = itemView.findViewById(R.id.img);
    }


    public  void  bind(Question question ){
        this.question = question;
        String numberStr = String.valueOf(getAdapterPosition() + 1);
        number.setText(numberStr);

        quest.setText(question.getBody());
        addAnswers();
        setImg();
        itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                ((IMark)context).setMark(getAdapterPosition());
                return true;
            }
        });
    }
    private void addAnswers() {
        linearLayout.removeAllViews();
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(0,2, 0, 2);

        for (Ans ans : question.getAns()) {
            TextView text = new CheckedTextView(context);
            switch (ans.getRight()) {
                case 1:
                    text.setLayoutParams(layoutParams);
                    text.setTypeface(null, Typeface.BOLD_ITALIC);
                    text.setBackground(context.getResources().getDrawable(R.drawable.border));
                    text.setTextColor(context.getResources().getColor(R.color.text));
                    break;
                case 0:
                    text.setTypeface(null, Typeface.ITALIC);
                    text.setTextColor(context.getResources().getColor(R.color.colorBack));
                    break;
                case -1:
                    break;
            }


            text.setText(ans.getBody());
            linearLayout.addView(text);
        }
    }
    private void setImg() {
        RequestOptions options = new RequestOptions()
                .transforms(new RoundedCorners(50))
                .fitCenter()
                .error(context.getResources().getDrawable(R.drawable.ic_launcher_background))
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .priority(Priority.HIGH);

        final String imgStr = question.getImg();
        if(imgStr != null){
            imageView.setVisibility(View.VISIBLE);
            int ident = context.getResources().getIdentifier(imgStr, "drawable", context.getPackageName());
            Glide.with(context).load(ident).apply(options).into(imageView);
        } else imageView.setVisibility(View.GONE);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = ImageActivity.createIntent(context, imgStr);
                context.startActivity(intent);
            }
        });


    }
}
