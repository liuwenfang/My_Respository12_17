package dialog;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import java.util.List;

import adapter.ZRecyclerViewAdapter;
import adapter.ZViewHolder;
import adapter.decoration.DividerDecoration;
import utils.DensityUtils;
import zzx.zzxbaselibrary.R;

/**
 * Created by zzx on 2017/12/30 0030.
 */

public class SelectDialog extends Dialog {
    private Context mContext;
    private List<String> items;
    private SelAdapter selAdapter;
    private RecyclerView mRecyclerView;

    public SelectDialog(@NonNull Context context, List<String> items) {
        super(context, R.style.Theme_Light_FullScreenDialogAct);
        setContentView(R.layout.dialog_select);
        Window window = this.getWindow();
//        window.setWindowAnimations(R.style.myDialogAnim);
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setGravity(Gravity.BOTTOM);
        window.setAttributes(lp);
        mContext = context;
        this.items = items;
        initView();
    }

    private void initView() {
        mRecyclerView = findViewById(R.id.mRecyclerView);
        findViewById(R.id.tvSelCancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        selAdapter = new SelAdapter();
        selAdapter.setOnItemClickListener(new ZRecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                if (onSelItem != null) {
                    onSelItem.OnSelItem(position);
                }
            }
        });
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mRecyclerView.addItemDecoration(new DividerDecoration(ContextCompat.getColor(mContext, R.color.colorGrayLine)
                , DensityUtils.dip2px(mContext, 0.5f)));
        mRecyclerView.setAdapter(selAdapter);
        selAdapter.notifyDataSetChanged();
    }

    private OnSelItem onSelItem;

    public void setOnSelItem(OnSelItem onSelItem) {
        this.onSelItem = onSelItem;
    }

    public interface OnSelItem {
        void OnSelItem(int position);
    }


    class SelAdapter extends ZRecyclerViewAdapter<String> {

        public SelAdapter() {
            super(mRecyclerView, items, R.layout.adapter_sel_item);
        }

        @Override
        protected void bindData(ZViewHolder holder, String data, int position) {
            holder.setText(R.id.tvSelItem, data);
        }
    }

}
