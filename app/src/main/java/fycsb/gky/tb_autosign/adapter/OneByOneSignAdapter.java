package fycsb.gky.tb_autosign.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import fycsb.gky.tb_autosign.R;


/**
 * Created by codefu.
 */
public class OneByOneSignAdapter extends BaseAdapter {
    private List<Map<String, String>> mData;
    private Context                   mContext;
    private Animation                 mAnim;


    public List<Long> getItemAnims() {
        return itemAnims;
    }

    private List<Long> itemAnims;

    public OneByOneSignAdapter(Context context, List<Map<String, String>> data) {
        this.mContext = context;
        this.mData = data;
        mAnim = AnimationUtils.loadAnimation(mContext, R.anim.left_in);
        itemAnims = new ArrayList<>(50);
    }


    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Object getItem(int position) {
        return mData.get(position);
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
            viewHolder.textView = (TextView) convertView.findViewById(R.id.usernameItem);
            viewHolder.imageView = (ImageView) convertView.findViewById(R.id.currentFlag);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        Map<String, String> map = mData.get(position);
        String item = map.get(mContext.getString(R.string.sign_detail_key));
        String flag = map.get(mContext.getString(R.string.sign_issuccess_key));
        boolean is_success = Boolean.valueOf(flag);
        viewHolder.textView.setText(item);
        if (is_success) {
            viewHolder.imageView.setImageResource(R.drawable.ic_check);
        } else {
            viewHolder.imageView.setImageResource(R.drawable.ic_close);

        }
        long id = getItemId(position);
        if (!itemAnims.contains(id)) {
            itemAnims.add(id);
            convertView.startAnimation(mAnim);
        }
        return convertView;
    }

    static class ViewHolder {
        public TextView  textView;
        public ImageView imageView;

        public ViewHolder() {

        }
    }

}
