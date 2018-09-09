package deltatest.org.dt.se;

import java.util.ArrayList;
import java.util.List;


public class Question {
    int id;
    int nunber;
    String body;
    String img;
    List<Ans> ans = new ArrayList();
    String category;

    public Question() {
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBody() {
        return this.body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getImg() {
        return this.img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public List<Ans> getAns() {
        return this.ans;
    }

    public void setAns(List<Ans> ans) {
        this.ans = ans;
    }


    public int getNunber() {
        return this.nunber;
    }

    public void setNunber(int nunber) {
        this.nunber = nunber;
    }

    public String getCategory() {
        return this.category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

}
