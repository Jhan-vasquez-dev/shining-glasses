package com.cdbwsoft.library.widget;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import com.cdbwsoft.library.AppConfig;
import com.cdbwsoft.library.R;
import java.lang.ref.WeakReference;

/* JADX INFO: loaded from: classes.dex */
public class AlertDialog extends Dialog {
    private boolean initialized;
    View.OnClickListener mButtonHandler;
    private Button mButtonNegative;
    private Message mButtonNegativeMessage;
    private CharSequence mButtonNegativeText;
    private Button mButtonNeutral;
    private Message mButtonNeutralMessage;
    private CharSequence mButtonNeutralText;
    private Button mButtonPositive;
    private Message mButtonPositiveMessage;
    private CharSequence mButtonPositiveText;
    public View mCustomTitleView;
    private ButtonHandler mHandler;
    private Drawable mIcon;
    private int mIconId;
    private ImageView mIconView;
    private ListView mListView;
    private CharSequence mMessage;
    private TextView mMessageView;
    private DialogInterface.OnClickListener mNegativeButtonListener;
    private DialogInterface.OnClickListener mNeutralButtonListener;
    private DialogInterface.OnClickListener mPositiveButtonListener;
    private boolean mReversal;
    private ScrollView mScrollView;
    private CharSequence mTitle;
    private TextView mTitleView;
    private View mView;
    private int mViewLayoutResId;

    public AlertDialog(Context context) {
        super(context);
        this.mReversal = AppConfig.DIALOG_BUTTON_REVERSAL;
        this.mButtonHandler = new View.OnClickListener() { // from class: com.cdbwsoft.library.widget.AlertDialog.1
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                Message messageObtain = (view != AlertDialog.this.mButtonPositive || AlertDialog.this.mButtonPositiveMessage == null) ? (view != AlertDialog.this.mButtonNegative || AlertDialog.this.mButtonNegativeMessage == null) ? (view != AlertDialog.this.mButtonNeutral || AlertDialog.this.mButtonNeutralMessage == null) ? null : Message.obtain(AlertDialog.this.mButtonNeutralMessage) : Message.obtain(AlertDialog.this.mButtonNegativeMessage) : Message.obtain(AlertDialog.this.mButtonPositiveMessage);
                if (messageObtain != null) {
                    messageObtain.sendToTarget();
                }
                AlertDialog.this.mHandler.obtainMessage(1, AlertDialog.this).sendToTarget();
            }
        };
    }

    public AlertDialog(Context context, int i) {
        super(context, i);
        this.mReversal = AppConfig.DIALOG_BUTTON_REVERSAL;
        this.mButtonHandler = new View.OnClickListener() { // from class: com.cdbwsoft.library.widget.AlertDialog.1
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                Message messageObtain = (view != AlertDialog.this.mButtonPositive || AlertDialog.this.mButtonPositiveMessage == null) ? (view != AlertDialog.this.mButtonNegative || AlertDialog.this.mButtonNegativeMessage == null) ? (view != AlertDialog.this.mButtonNeutral || AlertDialog.this.mButtonNeutralMessage == null) ? null : Message.obtain(AlertDialog.this.mButtonNeutralMessage) : Message.obtain(AlertDialog.this.mButtonNegativeMessage) : Message.obtain(AlertDialog.this.mButtonPositiveMessage);
                if (messageObtain != null) {
                    messageObtain.sendToTarget();
                }
                AlertDialog.this.mHandler.obtainMessage(1, AlertDialog.this).sendToTarget();
            }
        };
    }

    protected AlertDialog(Context context, boolean z, DialogInterface.OnCancelListener onCancelListener) {
        super(context, z, onCancelListener);
        this.mReversal = AppConfig.DIALOG_BUTTON_REVERSAL;
        this.mButtonHandler = new View.OnClickListener() { // from class: com.cdbwsoft.library.widget.AlertDialog.1
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                Message messageObtain = (view != AlertDialog.this.mButtonPositive || AlertDialog.this.mButtonPositiveMessage == null) ? (view != AlertDialog.this.mButtonNegative || AlertDialog.this.mButtonNegativeMessage == null) ? (view != AlertDialog.this.mButtonNeutral || AlertDialog.this.mButtonNeutralMessage == null) ? null : Message.obtain(AlertDialog.this.mButtonNeutralMessage) : Message.obtain(AlertDialog.this.mButtonNegativeMessage) : Message.obtain(AlertDialog.this.mButtonPositiveMessage);
                if (messageObtain != null) {
                    messageObtain.sendToTarget();
                }
                AlertDialog.this.mHandler.obtainMessage(1, AlertDialog.this).sendToTarget();
            }
        };
    }

    public void setReversal(boolean z) {
        if (this.mReversal != z) {
            this.mReversal = z;
            setupButtons();
        }
    }

    static boolean canTextInput(View view) {
        if (view.onCheckIsTextEditor()) {
            return true;
        }
        if (!(view instanceof ViewGroup)) {
            return false;
        }
        ViewGroup viewGroup = (ViewGroup) view;
        int childCount = viewGroup.getChildCount();
        while (childCount > 0) {
            childCount--;
            if (canTextInput(viewGroup.getChildAt(childCount))) {
                return true;
            }
        }
        return false;
    }

    @Override // android.app.Dialog
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        this.initialized = true;
        this.mHandler = new ButtonHandler(this);
        requestWindowFeature(1);
        setContentView(R.layout.dialog_layout);
        setupContent((LinearLayout) findViewById(R.id.contentPanel));
        boolean z = setupButtons();
        boolean z2 = setupTitle((LinearLayout) findViewById(R.id.topPanel));
        View viewFindViewById = findViewById(R.id.buttonPanel);
        if (!z) {
            viewFindViewById.setVisibility(8);
            setCanceledOnTouchOutside(true);
        }
        FrameLayout frameLayout = (FrameLayout) findViewById(R.id.customPanel);
        View viewInflate = this.mView;
        if (viewInflate == null) {
            viewInflate = this.mViewLayoutResId != 0 ? getLayoutInflater().inflate(this.mViewLayoutResId, (ViewGroup) frameLayout, false) : null;
        }
        boolean z3 = viewInflate != null;
        if (!z3 || !canTextInput(viewInflate)) {
            getWindow().setFlags(131072, 131072);
        }
        if (z3) {
            ((FrameLayout) findViewById(R.id.custom)).addView(viewInflate, new ViewGroup.LayoutParams(-1, -1));
            if (this.mListView != null) {
                ((LinearLayout.LayoutParams) frameLayout.getLayoutParams()).weight = 0.0f;
            }
        } else {
            findViewById(R.id.customPanel).setVisibility(8);
        }
        if (z2) {
            View viewFindViewById2 = (this.mMessage == null && this.mView == null && this.mListView == null) ? null : findViewById(R.id.titleDivider);
            if (viewFindViewById2 != null) {
                viewFindViewById2.setVisibility(0);
            }
        }
    }

    @Override // android.app.Dialog
    public void setTitle(int i) {
        this.mTitle = getContext().getText(i);
    }

    @Override // android.app.Dialog
    public void setTitle(CharSequence charSequence) {
        this.mTitle = charSequence;
    }

    public void setMessage(CharSequence charSequence) {
        this.mMessage = charSequence;
    }

    public void setMessage(int i) {
        setMessage(getContext().getString(i));
    }

    public void setPositiveButton(CharSequence charSequence, DialogInterface.OnClickListener onClickListener) {
        this.mButtonPositiveText = charSequence;
        this.mPositiveButtonListener = onClickListener;
        setupButtons();
    }

    public void setPositiveButton(int i, DialogInterface.OnClickListener onClickListener) {
        setPositiveButton(getContext().getString(i), onClickListener);
    }

    public void setNegativeButton(CharSequence charSequence, DialogInterface.OnClickListener onClickListener) {
        this.mButtonNegativeText = charSequence;
        this.mNegativeButtonListener = onClickListener;
        setupButtons();
    }

    public void setNegativeButton(int i, DialogInterface.OnClickListener onClickListener) {
        setNegativeButton(getContext().getString(i), onClickListener);
    }

    public void setNeutralButton(CharSequence charSequence, DialogInterface.OnClickListener onClickListener) {
        this.mButtonNeutralText = charSequence;
        this.mNeutralButtonListener = onClickListener;
        setupButtons();
    }

    public void setNeutralButton(int i, DialogInterface.OnClickListener onClickListener) {
        setNeutralButton(getContext().getString(i), onClickListener);
    }

    public void setView(int i) {
        this.mView = null;
        this.mViewLayoutResId = i;
    }

    public void setView(View view) {
        this.mView = view;
        this.mViewLayoutResId = 0;
    }

    public void setIcon(int i) {
        this.mIconId = i;
    }

    public void setIcon(Drawable drawable) {
        this.mIcon = drawable;
    }

    private void setupContent(LinearLayout linearLayout) {
        ScrollView scrollView = (ScrollView) findViewById(R.id.scrollView);
        this.mScrollView = scrollView;
        scrollView.setFocusable(false);
        TextView textView = (TextView) findViewById(R.id.message);
        this.mMessageView = textView;
        if (textView == null) {
            return;
        }
        CharSequence charSequence = this.mMessage;
        if (charSequence != null) {
            textView.setText(charSequence);
            return;
        }
        textView.setVisibility(8);
        this.mScrollView.removeView(this.mMessageView);
        if (this.mListView != null) {
            linearLayout.removeView(findViewById(R.id.scrollView));
            linearLayout.addView(this.mListView, new LinearLayout.LayoutParams(-1, -1));
            linearLayout.setLayoutParams(new LinearLayout.LayoutParams(-1, 0, 1.0f));
            return;
        }
        linearLayout.setVisibility(8);
    }

    private boolean setupButtons() {
        int i;
        if (!this.initialized) {
            return false;
        }
        if (!this.mReversal) {
            this.mButtonPositive = (Button) findViewById(R.id.button2);
            this.mButtonNegative = (Button) findViewById(R.id.button1);
        } else {
            this.mButtonPositive = (Button) findViewById(R.id.button2);
            this.mButtonNegative = (Button) findViewById(R.id.button1);
        }
        this.mButtonPositive.setOnClickListener(this.mButtonHandler);
        if (TextUtils.isEmpty(this.mButtonPositiveText)) {
            this.mButtonPositive.setVisibility(8);
            i = 0;
        } else {
            this.mButtonPositive.setText(this.mButtonPositiveText);
            this.mButtonPositive.setVisibility(0);
            i = 1;
        }
        this.mButtonNegative.setOnClickListener(this.mButtonHandler);
        if (TextUtils.isEmpty(this.mButtonNegativeText)) {
            this.mButtonNegative.setVisibility(8);
        } else {
            this.mButtonNegative.setText(this.mButtonNegativeText);
            this.mButtonNegative.setVisibility(0);
            i |= 2;
        }
        Button button = (Button) findViewById(R.id.button3);
        this.mButtonNeutral = button;
        button.setOnClickListener(this.mButtonHandler);
        if (TextUtils.isEmpty(this.mButtonNeutralText)) {
            this.mButtonNeutral.setVisibility(8);
        } else {
            this.mButtonNeutral.setText(this.mButtonNeutralText);
            this.mButtonNeutral.setVisibility(0);
            i |= 4;
        }
        if (AppConfig.CENTER_SINGLE_BUTTON) {
            if (i == 1) {
                centerButton(this.mButtonPositive);
            } else if (i == 2) {
                centerButton(this.mButtonNegative);
            } else if (i == 4) {
                centerButton(this.mButtonNeutral);
            }
        }
        applyButtons();
        return i != 0;
    }

    private void centerButton(Button button) {
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) button.getLayoutParams();
        layoutParams.gravity = 1;
        layoutParams.weight = 0.5f;
        button.setLayoutParams(layoutParams);
        View viewFindViewById = findViewById(R.id.leftSpacer);
        if (viewFindViewById != null) {
            viewFindViewById.setVisibility(0);
        }
        View viewFindViewById2 = findViewById(R.id.rightSpacer);
        if (viewFindViewById2 != null) {
            viewFindViewById2.setVisibility(0);
        }
    }

    public void setButton(int i, CharSequence charSequence, DialogInterface.OnClickListener onClickListener, Message message) {
        if (message == null && onClickListener != null) {
            message = this.mHandler.obtainMessage(i, onClickListener);
        }
        if (i == -3) {
            this.mButtonNeutralText = charSequence;
            this.mButtonNeutralMessage = message;
        } else if (i == -2) {
            this.mButtonNegativeText = charSequence;
            this.mButtonNegativeMessage = message;
        } else {
            if (i == -1) {
                this.mButtonPositiveText = charSequence;
                this.mButtonPositiveMessage = message;
                return;
            }
            throw new IllegalArgumentException("Button does not exist");
        }
    }

    private static final class ButtonHandler extends Handler {
        private static final int MSG_DISMISS_DIALOG = 1;
        private WeakReference<DialogInterface> mDialog;

        public ButtonHandler(DialogInterface dialogInterface) {
            this.mDialog = new WeakReference<>(dialogInterface);
        }

        @Override // android.os.Handler
        public void handleMessage(Message message) {
            int i = message.what;
            if (i == -3 || i == -2 || i == -1) {
                ((DialogInterface.OnClickListener) message.obj).onClick(this.mDialog.get(), message.what);
            } else {
                if (i != 1) {
                    return;
                }
                ((DialogInterface) message.obj).dismiss();
            }
        }
    }

    private boolean setupTitle(LinearLayout linearLayout) {
        if (this.mCustomTitleView != null) {
            linearLayout.addView(this.mCustomTitleView, 0, new LinearLayout.LayoutParams(-1, -2));
            findViewById(R.id.title_template).setVisibility(8);
            return true;
        }
        boolean zIsEmpty = TextUtils.isEmpty(this.mTitle);
        this.mIconView = (ImageView) findViewById(R.id.icon);
        if (!zIsEmpty) {
            TextView textView = (TextView) findViewById(R.id.alertTitle);
            this.mTitleView = textView;
            textView.setText(this.mTitle);
            int i = this.mIconId;
            if (i > 0) {
                this.mIconView.setImageResource(i);
                return true;
            }
            Drawable drawable = this.mIcon;
            if (drawable != null) {
                this.mIconView.setImageDrawable(drawable);
                return true;
            }
            if (i == 0) {
                this.mTitleView.setPadding(this.mIconView.getPaddingLeft(), this.mIconView.getPaddingTop(), this.mIconView.getPaddingRight(), this.mIconView.getPaddingBottom());
                this.mIconView.setVisibility(8);
            }
            return true;
        }
        findViewById(R.id.title_template).setVisibility(8);
        this.mIconView.setVisibility(8);
        linearLayout.setVisibility(8);
        return false;
    }

    public void applyButtons() {
        CharSequence charSequence = this.mButtonPositiveText;
        if (charSequence != null) {
            setButton(-1, charSequence, this.mPositiveButtonListener, null);
        }
        CharSequence charSequence2 = this.mButtonNegativeText;
        if (charSequence2 != null) {
            setButton(-2, charSequence2, this.mNegativeButtonListener, null);
        }
        CharSequence charSequence3 = this.mButtonNeutralText;
        if (charSequence3 != null) {
            setButton(-3, charSequence3, this.mNeutralButtonListener, null);
        }
    }
}
