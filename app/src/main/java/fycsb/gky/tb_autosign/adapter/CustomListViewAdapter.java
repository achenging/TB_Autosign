package fycsb.gky.tb_autosign.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;
import java.util.Map;

import fycsb.gky.tb_autosign.R;
import fycsb.gky.tb_autosign.ui.AutoSignActivity;


/**
 * Created by codefu on 15-2-20.
 */
public class CustomListViewAdapter extends BaseAdapter {
    private List<String>   data;
    private Context        mContext;
    private String name;
    public CustomListViewAdapter(Context context, List<String> data, String name) {
        this.mContext = context;
        this.data = data;
        this.name = name;
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.listview_already_login_user_layout, null);
            viewHolder = new ViewHolder();
            viewHolder.mTextView = (TextView) convertView.findViewById(R.id.usernameItem);
            viewHolder.mImageView = (ImageView) convertView.findViewById(R.id.currentFlag);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        String item = data.get(position);
        viewHolder.mImageView.setVisibility(View.GONE);
        if (item.equals(name)){
            viewHolder.mImageView.setVisibility(View.VISIBLE);
        }
        viewHolder.mTextView.setText(item);
        return convertView;
    }

    public static class ViewHolder {
        public TextView  mTextView;
        public ImageView mImageView;

        public ViewHolder() {

        }
    }
}
