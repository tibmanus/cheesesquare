/*
 * Copyright (C) 2015 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.support.android.designlibdemo;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;


public class CheeseListFragment extends Fragment {

    CheeseViewModel viewModel;
    private RecyclerView rv;
    private ProgressBar pb;
    private TextView et;

    enum STATE {
        RecyclerView, ProgressBar, ErrorText
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        LinearLayout view = (LinearLayout) inflater.inflate(
                R.layout.fragment_cheese_list, container, false);

        rv = view.findViewById(R.id.recyclerview);
        pb = view.findViewById(R.id.progressBar);
        et = view.findViewById(R.id.errorText);
        viewModel = ViewModelProviders.of(this).get(CheeseViewModel.class);

        et.setOnClickListener(v -> viewModel.refreshCheeses());
        viewModel.getCheeses().observe(this, cheeses -> {
            if (cheeses == null) return;

            if (cheeses.getStatus() == Status.SUCCESS) {
                show(STATE.RecyclerView);
                rv.setAdapter(new SimpleStringRecyclerViewAdapter(getActivity(), cheeses.getData()));
            }  else if (cheeses.getStatus() == Status.LOADING) {
                show(STATE.ProgressBar);
                rv.setAdapter(new SimpleStringRecyclerViewAdapter(getActivity(), null));
            } else if (cheeses.getStatus() == Status.ERROR) {
                show(STATE.ErrorText);
                rv.setAdapter(new SimpleStringRecyclerViewAdapter(getActivity(), null));
            }
        });

        setupRecyclerView(rv);
        return view;
    }

    private void show(STATE state) {
        switch (state) {
            case RecyclerView: {
                rv.setVisibility(View.VISIBLE);
                pb.setVisibility(View.GONE);
                et.setVisibility(View.GONE);
                break;
            }
            case ProgressBar: {
                rv.setVisibility(View.GONE);
                pb.setVisibility(View.VISIBLE);
                et.setVisibility(View.GONE);
                break;
            }
            case ErrorText: {
                rv.setVisibility(View.GONE);
                pb.setVisibility(View.GONE);
                et.setVisibility(View.VISIBLE);
                break;
            }
        }
    }

    private void setupRecyclerView(RecyclerView recyclerView) {
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));
        recyclerView.setAdapter(new SimpleStringRecyclerViewAdapter(getActivity(), null));
    }

    public static class SimpleStringRecyclerViewAdapter
            extends RecyclerView.Adapter<SimpleStringRecyclerViewAdapter.ViewHolder> {

        private final TypedValue mTypedValue = new TypedValue();
        private int mBackground;
        private List<Cheese> mValues;

        public static class ViewHolder extends RecyclerView.ViewHolder {
            public Cheese mBoundItem;

            public final View mView;
            public final ImageView mImageView;
            public final TextView mTextView;

            public ViewHolder(View view) {
                super(view);
                mView = view;
                mImageView = (ImageView) view.findViewById(R.id.avatar);
                mTextView = (TextView) view.findViewById(android.R.id.text1);
            }

            @Override
            public String toString() {
                return super.toString() + " '" + mTextView.getText();
            }
        }

        public Cheese getValueAt(int position) {
            return mValues.get(position);
        }

        public SimpleStringRecyclerViewAdapter(Context context, List<Cheese> items) {
            context.getTheme().resolveAttribute(R.attr.selectableItemBackground, mTypedValue, true);
            mBackground = mTypedValue.resourceId;
            mValues = items;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.list_item, parent, false);
            view.setBackgroundResource(mBackground);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            holder.mBoundItem = mValues.get(position);
            holder.mTextView.setText(holder.mBoundItem.getName());

            holder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Context context = v.getContext();
                    Intent intent = new Intent(context, CheeseDetailActivity.class);
                    intent.putExtra(CheeseDetailActivity.EXTRA_CHEESE, holder.mBoundItem);

                    context.startActivity(intent);
                }
            });

            Glide.with(holder.mImageView.getContext())
                    .load(holder.mBoundItem.getDrawableResId())
                    .fitCenter()
                    .into(holder.mImageView);
        }

        @Override
        public int getItemCount() {
            return mValues == null ? 0 : mValues.size();
        }
    }
}
