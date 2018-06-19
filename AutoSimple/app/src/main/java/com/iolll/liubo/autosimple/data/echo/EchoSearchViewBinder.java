package com.iolll.liubo.autosimple.data.echo;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.iolll.liubo.autosimple.R;
import com.iolll.liubo.iolllmusic.data.model.echo.EchoSearch;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.drakeet.multitype.ItemViewBinder;

/**
 * Created by LiuBo on 2018/6/12.
 */
public class EchoSearchViewBinder extends ItemViewBinder<EchoSearch, EchoSearchViewBinder.ViewHolder> {



    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View root = inflater.inflate(R.layout.item_echo_search, parent, false);
        return new ViewHolder(root);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, @NonNull EchoSearch echoSearch) {
        holder.info.setText(echoSearch.getInfo());
        holder.name.setText(echoSearch.getName());
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.name)
        TextView name;
        @BindView(R.id.info)
        TextView info;
        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
