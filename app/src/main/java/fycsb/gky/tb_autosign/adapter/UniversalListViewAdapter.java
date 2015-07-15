package fycsb.gky.tb_autosign.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import fycsb.gky.tb_autosign.R;


/**
 * Created by codefu.
 */
public class UniversalListViewAdapter<T> extends BaseAdapter {
    private List<T>   data;
    private Context   mContext;
    private Animation anim;


    private List<Long> itemAnims;

    public UniversalListViewAdapter(Context context, List<T> data) {
        this.mContext = context;
        this.data = data;
        anim = AnimationUtils.loadAnimation(mContext, R.anim.left_in);
        itemAnims = new ArrayList<>(50);
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.listview_sign_layout, null);
            viewHolder = new ViewHolder();
            viewHolder.mTextView = (TextView) convertView.findViewById(R.id.usernameItem);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        String item = data.get(position).toString();
        viewHolder.mTextView.setText(item);
        long id = getItemId(position);
        if (!itemAnims.contains(id)) {
            itemAnims.add(id);
            convertView.startAnimation(anim);
        }
        return convertView;
    }

    public static class ViewHolder {
        public TextView mTextView;

        public ViewHolder() {

        }
    }
}
