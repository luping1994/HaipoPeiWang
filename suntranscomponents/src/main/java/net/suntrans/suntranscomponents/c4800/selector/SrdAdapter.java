package net.suntrans.suntranscomponents.c4800.selector;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

import net.suntrans.looney.utils.UiUtils;
import net.suntrans.suntranscomponents.R;
import net.suntrans.suntranscomponents.bean.RoomSrd;

import java.util.List;

/**
 * Created by Administrator on 2017/8/16.
 */

public class SrdAdapter extends BaseExpandableListAdapter {

    private List<RoomSrd> datas;
    private Context mContext;

    public SrdAdapter(List<RoomSrd> datas, Context mContext) {
        this.datas = datas;
        this.mContext = mContext;
    }

    @Override
    public int getGroupCount() {

        return datas.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return datas.get(groupPosition).list.size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return datas.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return datas.get(groupPosition).list.get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        View view = null;
        GroupHolder groupHolder = null;
        if (convertView != null) {
            view = convertView;
            groupHolder = (GroupHolder) view.getTag();
        } else {
            view = LayoutInflater.from(mContext).inflate(R.layout.item_group_c, parent, false);
            groupHolder = new GroupHolder(view);
            view.setTag(groupHolder);
        }
        groupHolder.setData(groupPosition);
        return view;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        View view = null;
        ChildHolder holder = null;
        if (convertView != null) {
            view = convertView;
            holder = (ChildHolder) view.getTag();
        } else {
            view = LayoutInflater.from(mContext).inflate(R.layout.item_children_c, parent, false);
            holder = new ChildHolder(view);
            view.setTag(holder);
        }
        holder.setData(groupPosition, childPosition);
        return view;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }


    public class GroupHolder {
        TextView mName;
        CheckBox state;
        TextView count;

        public GroupHolder(View view) {
            mName = (TextView) view.findViewById(R.id.name);
            state = (CheckBox) view.findViewById(R.id.checkbox);
            count = (TextView) view.findViewById(R.id.count);

        }

        public void setData(final int groupPosition) {
            mName.setText(datas.get(groupPosition).name == null ? "null" : datas.get(groupPosition).name);
            state.setChecked(datas.get(groupPosition).isChecked);
            state.setVisibility(View.INVISIBLE);
            state.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (state.isChecked()) {
                        boolean hasPerssion = true;
                        for (int i = 0; i < datas.get(groupPosition).list.size(); i++) {
                            if (datas.get(groupPosition).list.get(i).permission == 0) {
                                hasPerssion = false;
                                continue;
                            }
                            datas.get(groupPosition).list.get(i).setChecked(true);
                        }
                        if (!hasPerssion) {
                            UiUtils.showToast("该区域下部分设备无管理权限无法全选");
                            datas.get(groupPosition).setChecked(false);
                        } else {

                            datas.get(groupPosition).setChecked(true);
                        }
                    } else {
                        boolean hasPerssion = true;
                        for (int i = 0; i < datas.get(groupPosition).list.size(); i++) {
                            if (datas.get(groupPosition).list.get(i).permission == 0) {
                                hasPerssion = false;
                                continue;
                            }
                            datas.get(groupPosition).list.get(i).setChecked(false);
                        }
                        if (!hasPerssion) {
                            UiUtils.showToast("该区域下部分设备无管理权限无法全选");
                            datas.get(groupPosition).setChecked(true);
                        } else {
                            datas.get(groupPosition).setChecked(false);
                        }
                    }
                    notifyDataSetChanged();
                }
            });
            count.setText(datas.get(groupPosition).list.size() + "");
        }
    }

    public class ChildHolder {
        TextView mText;
        CheckBox state;
        RelativeLayout root;

        public ChildHolder(View view) {
            mText = (TextView) view.findViewById(R.id.name);
            state = (CheckBox) view.findViewById(R.id.checkbox);
            root = (RelativeLayout) view.findViewById(R.id.root);

        }

        public void setData(final int groupPosition, final int childPosition) {
            mText.setText(datas.get(groupPosition).list.get(childPosition).title == null ? "null" : datas.get(groupPosition).list.get(childPosition).title);
            state.setChecked(datas.get(groupPosition).list.get(childPosition).isChecked());

            root.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    if (datas.get(groupPosition).list.get(childPosition).permission == 0) {
//                        UiUtils.showToast("您没有该设备的管理权限");
//                        datas.get(groupPosition).list.get(childPosition).setChecked(state.isChecked());
//                    } else {
//                        datas.get(groupPosition).list.get(childPosition).setChecked(!state.isChecked());
//                    }
                    datas.get(groupPosition).list.get(childPosition).setChecked(!state.isChecked());
                    int checkedCount = 0;
                    for (int i = 0; i < datas.size(); i++) {
                       for(int j=0;j<datas.get(i).list.size();j++){
                           if (i!=groupPosition||j!=childPosition){
                               datas.get(i).list.get(j).setChecked(false);
                           }
                       }
                    }
                    if (checkedCount == datas.get(groupPosition).list.size()) {
                        datas.get(groupPosition).setChecked(true);
                    } else {
                        datas.get(groupPosition).setChecked(false);
                    }
                    notifyDataSetChanged();
                }
            });
        }
    }
}
