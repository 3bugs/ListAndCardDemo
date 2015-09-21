package com.example.listandcarddemo;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new MyAdapter(this);
        mRecyclerView.setAdapter(mAdapter);
    }

    private static class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {

        private Context mContext;
        private List<String> mDataset = new ArrayList<>();

        public static class ViewHolder extends RecyclerView.ViewHolder {
            public CardView mCardView;
            public ImageView mImageView;
            public TextView mTextView;

            public ViewHolder(CardView v) {
                super(v);
                mCardView = v;
                mImageView = (ImageView) v.findViewById(R.id.card_image);
                mTextView = (TextView) v.findViewById(R.id.card_text);
            }
        }

        // คอนสตรัคเตอร์
        public MyAdapter(Context context) {
            this.mContext = context;

            AssetManager am = context.getAssets();
            try {
                String[] filenames = am.list("");
                for (String f : filenames) {
                    if (f.length() > 7 && "animals".equals(f.substring(0, 7))) {
                        mDataset.add(f);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public MyAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                       int viewType) {
            CardView v = (CardView) LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.list_item, parent, false);
            ViewHolder holder = new ViewHolder(v);
            return holder;
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            AssetManager am = mContext.getAssets();

            String filename = mDataset.get(position);
            try {
                InputStream stream = am.open(filename);
                Drawable drawable = Drawable.createFromStream(stream, filename);
                holder.mImageView.setImageDrawable(drawable);
            } catch (IOException e) {
                e.printStackTrace();
            }

            String animalName = filename
                    .replace(".png", "")
                    .substring(filename.indexOf('-') + 1);
            holder.mTextView.setText(animalName);
        }

        @Override
        public int getItemCount() {
            return mDataset.size();
        }
    }
}
