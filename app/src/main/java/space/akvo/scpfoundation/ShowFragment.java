package space.akvo.scpfoundation;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

//import space.akvo.scpfoundation.GestureListener;

import com.zzhoujay.richtext.RichText;
import com.zzhoujay.richtext.callback.OnUrlClickListener;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by akvo on 2017/8/19.
 */

public class ShowFragment extends Fragment {

    final String main_scp_url = "http://scp-wiki-cn.wikidot.com";
    private RichText richText;
    private TextView scp_detail_tx;
    private ProgressBar progressBar;
    private ScrollView scrollView;
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
    private SwipeRefreshLayout swipeRefreshLayout;
    public ScrollViewX svx;
    private String scps;
    private String scpt;
    private boolean canNext = true;
    private List<String> listItems;

    public void onStart() {
        super.onStart();
        ma = (MainActivity) getActivity();
        swipeRefreshLayout.setRefreshing(true);
        scp_detail_tx = getActivity().findViewById(R.id.scp_detail);
        scp_detail_tx.setText("");
        ma.changeToolbarText(toolbarText);
        getScpDetail();

        if (listItems!=null){
            if (ma.global==null){
                ma.global = new Global();
            }
            ma.global.setListItems(listItems);
            listItems =null;
        }

        ma.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                slideLeft();
            }
        });
        ma.fab.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                slideRight();
                return true;
            }
        });
        svx = getActivity().findViewById(R.id.scroll);
        svx.setOnScrollListener(new ScrollViewX.OnScrollListener() {
            @Override
            public void onScrollChanged(int x, int y, int oldX, int oldY) {
            }

            @Override
            public void onScrollStopped() {
                if (svx.isAtTop()) {
                    swipeRefreshLayout.setEnabled(true);
                } else if (svx.isAtBottom()) {
                    snackBar = Snackbar.make(ma.toolbar, "别扯了，到底了~", Snackbar.LENGTH_INDEFINITE);
                    ma.showSnackBar(snackBar);
                    System.out.println("bottom");
                }
                else {
                    swipeRefreshLayout.setEnabled(false);
                }
            }

            @Override
            public void onScrolling() {
            }
        });

        ma.setState(1);
        ma.sbs = new StoreBacks();

        ma.sbs.addBack(ma.toolbar.getTitle().toString(), open_url);
    }

    public void getScpDetail() {
        if (canNext) {
            scps = open_url.replace(main_scp_url + "/", "");
            scpt = ma.toolbar.getTitle().toString();
        }
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
                    }else {
                        ma.setState(1);
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

        swipeRefreshLayout = (SwipeRefreshLayout)view.findViewById(R.id.swipeRefresh);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(true);
                scp_detail_tx.setText("");
                getScpDetail();
            }
        });

        return view;
    }


    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            swipeRefreshLayout.setRefreshing(false);
            swipeRefreshLayout.setEnabled(true);
            switch (msg.what) {
                case 0:
                    scp_detail_tx.setText("");
                    progressBar.setVisibility(View.GONE);
                    snackBar = Snackbar.make(ma.toolbar, "网络连接失败！", Snackbar.LENGTH_LONG).setAction("刷新", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            getScpDetail();
                        }
                    });
                    ma.showSnackBar(snackBar);
                    break;
                case 1:
                    svx.scrollTo(0,0);
                    canAdd = true;
                    svx.setVisibility(View.VISIBLE);
                    richText = RichText.from(scp_detail).
                            urlClick(new OnUrlClickListener() {
                                @Override
                                public boolean urlClicked(String url) {
                                    canNext = false;
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
                                        progressBar.setVisibility(View.VISIBLE);
                                        svx.setVisibility(View.GONE);
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

    public void slideLeft(){
        canNext = true;
        List<String> list = ma.global.getListItems();
        int a = list.indexOf(scps+"!@"+scpt);
        if (a+1==list.size()){
            snackBar = Snackbar.make(ma.toolbar, "已经到达最后一项！", Snackbar.LENGTH_LONG);
            ma.showSnackBar(snackBar);
        }else {
            open_url = main_scp_url + "/" + (list.get(a + 1).split("!@")[0]);
            ma.changeToolbarText((list.get(a + 1).split("!@")[1]));
            getScpDetail();
        }
    }

    public void slideRight(){
        canNext = true;
        List<String> list = ma.global.getListItems();
        int a = list.indexOf(scps+"!@"+scpt);
        if (a==0){
            snackBar = Snackbar.make(ma.toolbar, "这是第一项哦~", Snackbar.LENGTH_LONG);
            ma.showSnackBar(snackBar);
        }else {
            open_url = main_scp_url + "/" + (list.get(a - 1).split("!@")[0]);
            ma.changeToolbarText((list.get(a - 1).split("!@")[1]));
            getScpDetail();
        }
    }

    public void onPause(){
        listItems = ma.global.copyList();
        toolbarText = ma.toolbar.getTitle().toString();
        super.onPause();
    }

}

