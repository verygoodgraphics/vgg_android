package com.example.verygoodgraphics.android.demo.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.example.verygoodgraphics.android.demo.VggDemos;
import com.example.verygoodgraphics.android.demo.databinding.ActivityVggDemoSelectBinding;

import java.util.ArrayList;

public class VggDemoSelectActivity extends Activity {

    ArrayList<VggDemos.DemoInfo> demos = VggDemos.DEMO_INFOS;
    private ActivityVggDemoSelectBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityVggDemoSelectBinding.inflate(getLayoutInflater());
        View root = binding.getRoot();
        ListView listView = binding.listView;
        ListAdapter adapter = new VggDemoSelectAdapter(this, demos);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener((parent, view, position, id) -> startActivity(VggDemoViewActivity.createIntent(this, position)));
        binding.xmlButton.setOnClickListener(v -> startActivity(new Intent(VggDemoSelectActivity.this, VggXmlDemoActivity.class)));
        setContentView(root);
    }

    private static class VggDemoSelectAdapter extends BaseAdapter implements ListAdapter {

        private final ArrayList<VggDemos.DemoInfo> demos;
        private final Activity activity;

        public VggDemoSelectAdapter(Activity activity, ArrayList<VggDemos.DemoInfo> demos) {
            this.activity = activity;
            this.demos = demos;
        }

        @Override
        public int getCount() {
            return demos.size();
        }

        @Override
        public Object getItem(int position) {
            return demos.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(activity).inflate(android.R.layout.simple_list_item_1, parent, false);
            }
            VggDemos.DemoInfo demo = demos.get(position);
            ((TextView) convertView).setText(demo.name);
            return convertView;
        }
    }

}
