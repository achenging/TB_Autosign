package fycsb.gky.tb_autosign.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

import fycsb.gky.tb_autosign.R;

/**
 * Created by codefu.
 */
public class AutoCompleteTextAdapter extends BaseAdapter {
    private List<String> users;
    private Context mContext;
    public AutoCompleteTextAdapter(Context mContext, List<String> users) {
        this.users = users;
        this.mContext = mContext;
    }
    @Override
    public int getCount() {
        return users.size();
    }

    @Override
    public Object getItem(int position) {
        return users.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.dropdown_list,null);
            viewHolder = new ViewHolder();
            viewHolder.mTextView = (TextView) convertView.findViewById(R.id.already_login_user);
//            viewHolder.mButton = (Button) convertView.findViewById(R.id.del_already_login_user_btn);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.mTextView.setText(users.get(position));
        viewHolder.mButton.setOnClickListener(new DelBtnClickListener(position));

        return convertView;
    }

    private class DelBtnClickListener  implements View.OnClickListener{
        private int pos;
        public DelBtnClickListener(int pos){
            this.pos = pos;
        }
        @Override
        public void onClick(View v) {
            users.remove(pos);
            AutoCompleteTextAdapter.this.notifyDataSetChanged();
        }
    }

    public static class ViewHolder {
        public TextView mTextView;
        public Button   mButton;
    }
}
