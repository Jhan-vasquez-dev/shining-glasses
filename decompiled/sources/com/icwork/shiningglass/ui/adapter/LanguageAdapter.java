package com.icwork.shiningglass.ui.adapter;

import android.content.Context;
import android.widget.TextView;
import com.icwork.shiningglass.R;
import com.icwork.shiningglass.model.bean.Language;
import com.icwork.shiningglass.ui.adapter.baseadapter.CommonAdapter;
import com.icwork.shiningglass.ui.adapter.baseadapter.ViewHolder;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public class LanguageAdapter extends CommonAdapter<Language> {
    public LanguageAdapter(Context context, List<Language> list, int i) {
        super(context, list, i);
    }

    @Override // com.icwork.shiningglass.ui.adapter.baseadapter.CommonAdapter
    public void convert(ViewHolder viewHolder, Language language, int i) {
        ((TextView) viewHolder.getView(R.id.tv_language_title)).setText(language.getLanguageName());
    }
}
