package space.akvo.scpfoundation;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.zzhoujay.richtext.RichText;
import com.zzhoujay.richtext.callback.OnUrlClickListener;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;

/**
 * Created by akvo on 2017/8/19.
 */

public class ShowFragment extends Fragment {

    final String main_scp_url = "http://scp-wiki-cn.wikidot.com";
    private RichText richText;
    private TextView scp_detail_tx;
    private String open_url;
    private String scp_detail;
    private ArrayList footer_list = new ArrayList();
    private String tz_scp_url = new String();
    public int state;
    public String toolbarText;
    public MainActivity ma;
    private Document doc;
    public Message message;
    public Snackbar snackBar;
    public boolean canAdd;

    public void onStart() {
        super.onStart();
        ma = (MainActivity) getActivity();
        scp_detail_tx = getActivity().findViewById(R.id.scp_detail);

        ma.changeToolbarText(toolbarText);
        ma.sbs = new StoreBacks();
        getScpDetail();

        ma.sbs.addBack(ma.toolbar.getTitle().toString(), open_url);
        System.out.println(ma.sbs.getAllUrls()+"\n"+ma.sbs.getAllTitles());
//        sbs.addBack(ma.toolbar.getTitle().toString(), open_url);
//        System.out.println(sbs.getBack()[0]+"55555"+sbs.getBack()[1]);
    }

    public void getScpDetail() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                GetScpDetails gsd = new GetScpDetails();
                gsd.execute(open_url);
                scp_detail = gsd.getSth();
                if (scp_detail != null) {
                    doc = Jsoup.parse(scp_detail);
                    footer_list = gsd.get_footer();
                    message = new Message();
                    message.what = 1;
                    if (ma.sbs.getSize()>1){
                        ma.setState(2);
                    }
                } else {
                    ma.setState(0);
                    message = new Message();
                    message.what = 0;
                }
                handler.sendMessage(message);
            }
        }).start();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_scp_detail, container, false);
        Bundle bundle = getArguments();
        open_url = new String(bundle.getString("url"));
        return view;
    }


    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    richText = RichText.from("").into(scp_detail_tx);
                    snackBar = Snackbar.make(ma.toolbar, "网络连接失败！", Snackbar.LENGTH_INDEFINITE).setAction("刷新", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            getScpDetail();
                        }
                    });
                    ma.showSnackBar(snackBar);
                    ma.swipeRefresh.setEnabled(false);
                    break;
                case 1:
                    canAdd = true;
                    richText = RichText.from(scp_detail).
                            urlClick(new OnUrlClickListener() {
                                @Override
                                public boolean urlClicked(String url) {
                                    //ma.setState(2);


//                                    if (ma.sbs.getBackSize() == 1){
//                                        ma.setState(1);
//                                    }else {
//                                        System.out.println(ma.sbs.getBack()[0] + "55555" + ma.sbs.getBack()[1]);
//                                    }

                                    if (url.contains("footnoteref-")) {
                                        new AlertDialog.Builder(getContext())
                                                .setMessage(footer_list.get(Integer.parseInt(url.replace("footnoteref-", "")) - 1).toString()).show();
                                    } else if (url.matches("/.+?")) {
                                        tz_scp_url = main_scp_url + url;
                                        open_url = tz_scp_url;
                                        getTitle(url);
                                        if (canAdd) {
                                            ma.sbs.addBack(ma.toolbar.getTitle().toString(), open_url);
                                        }
                                        canAdd = false;
                                        getScpDetail();
                                    }
                                    return false;
                                }
                            })
                            .into(scp_detail_tx);
                    break;
                default:
                    break;
            }
        }
    };

    public void getTitle(String s) {
        ma.changeToolbarText(doc.select("a[href=" + s + "]").text());
    }

    public void setOpenUrl(String a) {
        open_url = a;
    }
}
