package com.icwork.shiningglass.ui.roteview;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import com.icwork.shiningglass.R;
import com.icwork.shiningglass.ui.roteview.ArcMenu;

/* JADX INFO: loaded from: classes.dex */
public class ArcMenuActivity extends Activity {
    private ArcMenu arcMenu;

    @Override // android.app.Activity
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_arcmenu);
        ArcMenu arcMenu = (ArcMenu) findViewById(R.id.id_arcmenu);
        this.arcMenu = arcMenu;
        arcMenu.setOnMenuItemClickListener(new ArcMenu.OnMenuItemClickListener() { // from class: com.icwork.shiningglass.ui.roteview.ArcMenuActivity.1
            @Override // com.icwork.shiningglass.ui.roteview.ArcMenu.OnMenuItemClickListener
            public void onClick(View view, int i) {
                Toast.makeText(ArcMenuActivity.this, ((TextView) view).getText().toString(), 0).show();
            }
        });
    }
}
