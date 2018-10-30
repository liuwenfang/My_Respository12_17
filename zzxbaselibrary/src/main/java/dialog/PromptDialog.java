package dialog;

import android.app.Dialog;
import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import zzx.zzxbaselibrary.R;


/**
 * Created by Administrator on 2016/12/1.
 */
public class PromptDialog extends Dialog {
    public PromptDialog(Context context, String content) {
        super(context, R.style.Theme_Light_FullScreenDialogAct);
        setContentView(R.layout.dialog_prompt);
        Window window = this.getWindow();
//        window.setWindowAnimations(R.style.myDialogAnim);
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setGravity(Gravity.CENTER);
        window.setAttributes(lp);
        mContext = context;
        tvCancle = findViewById(R.id.tvCancel);
        tvConfirm = findViewById(R.id.tvConfirm);
        tvPrompt = findViewById(R.id.tvPrompt);
        tvPrompt.setText(content == null ? "" : content);
        tvTitle = findViewById(R.id.tvTitle);
        viewXian = findViewById(R.id.viewXian);
        initClick();
    }

    private Context mContext;
    public TextView tvCancle, tvConfirm, tvPrompt, tvTitle;
    public View viewXian;

    private void initClick() {
        tvCancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClick != null) {
                    onItemClick.OnCancelClick();
                } else {
                    dismiss();
                }
            }
        });
        tvConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClick != null) {
                    onItemClick.OnConfirmClick();
                }
            }
        });
    }

    public void setConfirmTextColor(int color) {
        tvConfirm.setTextColor(ContextCompat.getColor(mContext, color));
    }

    private OnItemClick onItemClick;

    public void setOnItemClick(OnItemClick onItemClick) {
        this.onItemClick = onItemClick;
    }

    public interface OnItemClick {
        void OnCancelClick();

        void OnConfirmClick();
    }

}
