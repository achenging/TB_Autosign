package fycsb.gky.tb_autosign.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import fycsb.gky.tb_autosign.R;

/**
 * Created by codefu.
 */
public class ChangeUserListViewAdapter extends BaseAdapter {
    private List<String>   data;
    private Context        mContext;
    private String name;
    public ChangeUserListViewAdapter(Context context, List<String> data, String name) {
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
        ViewHolder viewHolder;
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
