package fycsb.gky.tb_autosign.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import fycsb.gky.tb_autosign.R;


/**
 * Created by codefu on 15-2-20.
 */
public class CustomListViewAdapter extends BaseAdapter {
    private List<String> data;
    private Context      mContext;

    public CustomListViewAdapter(Context context, List<String> data) {
        this.mContext = context;
        this.data = data;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.listview_item_layout,null);
            viewHolder = new ViewHolder();
            viewHolder.mTextView = (TextView) convertView.findViewById(R.id.sign_info);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.mTextView.setText(data.get(position));

        return convertView;
    }

    public static class ViewHolder{
        public TextView mTextView;

        public ViewHolder() {

        }
    }
}
