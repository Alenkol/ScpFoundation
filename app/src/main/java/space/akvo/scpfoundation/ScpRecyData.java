package space.akvo.scpfoundation;

/**
 * Created by akvo on 2017/9/8.
 */

public class ScpRecyData {
    private String tit;
    private String sub;
    private String des;
    public ScpRecyData(int tit, int sub, int des){
//        this.tit = tit;
//        this.sub = sub;
//        this.des = des;
        this.tit = getApplication.getInstance().getString(tit);
        this.sub = getApplication.getInstance().getString(sub);
        this.des = getApplication.getInstance().getString(des);
    }

    public String getTit(){
        return this.tit;
    }

    public String getSub() {
        return this.sub;
    }

    public String getDes() {
        return this.des;
    }
}
