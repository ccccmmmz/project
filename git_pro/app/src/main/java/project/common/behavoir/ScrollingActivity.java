package project.common.behavoir;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.test.commonporject.R;
import com.test.commonporject.test.Adapter;

public class ScrollingActivity extends AppCompatActivity {

    RecyclerView mRv;
    TextView mTvTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrolling);

        mRv = findViewById(R.id.rv);
        mTvTitle = findViewById(R.id.title);

        mRv.setLayoutManager(new LinearLayoutManager(this));
        mRv.setAdapter(new Adapter());
    }
}