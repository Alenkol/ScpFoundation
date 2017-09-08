package space.akvo.scpfoundation;

import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by akvo on 2017/8/18.
 */

public class GetScpDetails {
    private Document doc;
    private Element content;
    private Elements ets;
    private ArrayList titles = new ArrayList();
    private ArrayList whats = new ArrayList();
    private StringBuilder tmp = new StringBuilder();
    private Elements pic_eles;
    private ArrayList pic_src = new ArrayList();
    private ArrayList pic_text = new ArrayList();
    private Elements foot_text;

    private void takeDoc(String url){
        try {
            doc = Jsoup.connect(url).get();
            content = doc.getElementById("page-content");
            do_foot();
            content.select("div.heritage-emblem").remove();
            content.select("div#page-rate-widget-box").remove();
            pic_eles = content.getElementsByClass("scp-image-block");
            content.select("div.scp-image-block").remove();
            foot_text = content.getElementsByClass("footnote-footer");
        } catch (IOException e) {
            doc = null;
        }
    }

    public void execute(String url){
        this.takeDoc(url);
    }

    private void do_foot(){
        Elements ets = content.select("sup.footnoteref > a");
        for (Element et:ets){
            content.select("a#"+et.attr("id").toString()).attr("href",et.attr("id").toString());
        }
    }

    public String getSth(){
        if (doc!=null) {
            for (Element pic_ele : pic_eles) {
                pic_src.add(pic_ele.select("img").attr("src"));
                pic_text.add(pic_ele.getElementsByTag("p").text());
            }
            if(pic_eles.hasText()) {
                content.append("<p><strong>图册：</strong></p> " + pic_eles.toString());
            }
            //content.append(pic_eles.html());
            String returns = content.html().toString().replaceAll("<p>«.+»</p>", "");
            returns = returns.replaceAll("</?blockquote>|<br>", "");
            returns = returns.replace("作者信息</span>", "作者信息： </span>");

            //returns = returns.replace("<br />","");
            //System.out.println(returns);
            return returns;
        }else{
            return null;
        }
    }

    private ArrayList take_foot(){
        ArrayList al = new ArrayList();
        for (Element et:foot_text){
            String s = et.text().toString();
            String t = s.substring(s.indexOf("译注：")+3);
            al.add(t);
        }
        return al;
    }

    public ArrayList get_footer(){
        return take_foot();
    }

//    public String getSth(String url){
//        this.takeDoc(url);
//        for (Element pic_ele:pic_eles){
//            pic_src.add(pic_ele.select("img").attr("src"));
//            pic_text.add(pic_ele.getElementsByTag("p").text());
//        }
//        String returns= content.html().toString().replaceAll("<p>«.+»</p>","");
//        returns = "> 编号：SCP-173\n\n333\n\n444";
//        return returns;
//    }
}
