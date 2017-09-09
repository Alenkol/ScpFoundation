package space.akvo.scpfoundation;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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

import java.util.ArrayList;

/**
 * Created by akvo on 2017/8/19.
 */

public class ShowFragment extends Fragment {

    final String main_scp_url ="http://scp-wiki-cn.wikidot.com";
    private RichText richText;
    private TextView scp_detail_tx;
    private String open_url;
    private String scp_detail;
    private ArrayList footer_list = new ArrayList();
    private String tz_scp_url = new String();
    public int state;
    public String toolbarText;
    public MainActivity ma;
    public void onStart(){
        ma = (MainActivity)getActivity();
        super.onStart();
        scp_detail_tx = getActivity().findViewById(R.id.scp_detail);
        ma.backState = 1;
        getScpDetail();
    }

    public void getScpDetail(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                GetScpDetails gsd = new GetScpDetails();
                gsd.execute(open_url);
                scp_detail = gsd.getSth();
                if (scp_detail!=null) {
                    footer_list = gsd.get_footer();
                    Message message = new Message();
                    message.what = 1;
                    handler.sendMessage(message);
                }else {
                    toolbarText = "无网络";
                    getActivity().getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.main_container, new NoInternetFragment(), null)
                            .addToBackStack(null)
                            .commit();
                    ma.setState(1);
                }
            }
        }).start();
        ma.changeToolbarText(toolbarText);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_scp_detail,container,false);
        Bundle bundle = getArguments();
        open_url = new String(bundle.getString("url"));
        return view;
    }


    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 1:
                    richText = RichText.from(scp_detail).
                            urlClick(new OnUrlClickListener() {
                                @Override
                                public boolean urlClicked(String url) {
                                    if (url.contains("footnoteref-")) {
                                        new AlertDialog.Builder(getContext())
                                                .setMessage(footer_list.get(Integer.parseInt(url.replace("footnoteref-", "")) - 1).toString()).show();
                                    }else if(url.matches("/.+?")){
                                        tz_scp_url = main_scp_url+url;
                                        open_url = tz_scp_url;
                                        show_new();
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

    private void add_back_url(String url){
        MainActivity activity = (MainActivity)getActivity();
        if (activity.back_url.size()==7){
            activity.back_url.remove(0);
            activity.back_url.add(url);
        }else {
            activity.back_url.add(url);
        }
    }

    public void show_new(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                GetScpDetails gsd = new GetScpDetails();
                add_back_url(tz_scp_url);
                gsd.execute(tz_scp_url);
                scp_detail = gsd.getSth();
                footer_list = gsd.get_footer();
                Message message = new Message();
                message.what = 1;
                handler.sendMessage(message);
            }
        }).start();
    }

    public void set_tz_scp_url(String url){
        tz_scp_url = url;
    }
}
