package space.akvo.scpfoundation;


import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
    private String s;
    private ArrayList getDataArr;
    private Handler handler;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_data_list,container,false);
        lv = (ListView)view.findViewById(R.id.scp_list);
        setList("select * from Y limit 264,948");
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                ScpData scpa = scpList.get(i);
                MainActivity ma = (MainActivity)getActivity();
                ma.toolbarText.add(scpa.getScp_nam());
                ma.changeToolbarText(scpa.getScp_nam());
                ShowFragment sf = ma.getShow_fragment();
                ma.show_fragment = sf;
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

    public void setList(final String s){
        this.s = s;
        scpList.clear();
        rd = new readDatabase();
        new Thread(new Runnable() {
            @Override
            public void run() {
                getDataArr = rd.reDa(s);
                Message message = new Message();
                message.what = 1;
                handler.sendMessage(message);
            }
        }).start();

    }



}
