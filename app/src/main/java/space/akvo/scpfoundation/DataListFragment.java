package space.akvo.scpfoundation;


import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by akvo on 2017/8/19.
 */

public class DataListFragment extends Fragment {

    private List<ScpData> scpList = new ArrayList<>();
    private ScpAdapter sa;
    private ListView lv;
    private readDatabase rd;
    private int listNum = 0;
    public String s;
    public String t;
    private ArrayList getDataArr;
    private Handler handler;
    public String toolbarText;
    public MainActivity ma;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_data_list,container,false);
        lv = view.findViewById(R.id.scp_list);
        //setList("select * from Y limit 264,948");
        ma = (MainActivity)getActivity();
        //listNum = ma.listNum;
        ma.backState = 0;
        setList();
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                ScpData scpa = scpList.get(i);
                ShowFragment sf = ma.getShow_fragment();
                ma.show_fragment = sf;
                sf.toolbarText = scpa.getScp_nam();
                ma.open_url = "http://scp-wiki-cn.wikidot.com/"+scpa.getScp_id();
                ma.putMessage(sf,"url","http://scp-wiki-cn.wikidot.com/"+scpa.getScp_id());
                ma.changeFragment(sf);
            }
        });

        handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what){
                    case 1:
                        for (Object a : getDataArr){
                            String[] b = a.toString().split("!@");
                            if (b.length==3) {
                                scpList.add(new ScpData(b[0], b[1], "项目等级："+b[2]));
                            }else{
                                scpList.add(new ScpData(b[0], b[1],""));
                            }
                        }
                        sa = new ScpAdapter(getContext(),R.layout.list_view,scpList);
                        lv.setAdapter(sa);
                        break;
                    default:
                        break;
                }
            }
        };
        return view;
    }

    public void onStart(){
        super.onStart();
        ma.changeToolbarText(toolbarText);
    }

    public void setList(){

        switch(listNum){
            case 0:
                this.s = "select * from Y limit 264,948";
                break;
            case 1:
                this.s = "select * from Y limit 1212,824";
                break;
            case 2:
                this.s = "select * from Y limit 2036,433";
                break;
            case 3:
                this.s = "select * from Y limit 2469,92";
                break;
            case 4:
                this.s = "select * from Y limit 0,264";
                break;
            default:
                break;
        }
        scpList.clear();
        rd = new readDatabase();
        new Thread(new Runnable() {
            @Override
            public void run() {

                if (ma.global == null){
                    ma.global = new Global();
                }

                getDataArr = rd.reDa(s)[0];
                ma.global.setListItems(rd.reDa(s)[1]);

                Message message = new Message();
                message.what = 1;
                handler.sendMessage(message);
            }
        }).start();

    }

    public void setListNum(int num){
        this.listNum = num;
    }

    public void reSetList(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (ma.global == null){
                    ma.global = new Global();
                }
                ma.global.setListItems(rd.reDa(s)[1]);
            }
        }).start();
    }
}
