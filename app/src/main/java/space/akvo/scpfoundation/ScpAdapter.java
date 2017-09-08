package space.akvo.scpfoundation;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by akvo on 2017/9/6.
 */

public class ScpAdapter extends ArrayAdapter<ScpData> {
    private int id;
    public ScpAdapter(Context context, int scpViewId, List<ScpData> objects){
        super(context,scpViewId,objects);
        id = scpViewId;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        ScpData sd = getItem(position);
        View view = LayoutInflater.from(getContext()).inflate(id,parent,false);
        TextView scp_ids = (TextView)view.findViewById(R.id.scp_id);
        TextView scp_name = (TextView)view.findViewById(R.id.scp_name);
        TextView scp_level = (TextView)view.findViewById(R.id.scp_level);
        scp_ids.setText(sd.getScp_id());
        scp_name.setText(sd.getScp_nam());
        scp_level.setText(sd.getScp_lev());
        return view;
    }
}
