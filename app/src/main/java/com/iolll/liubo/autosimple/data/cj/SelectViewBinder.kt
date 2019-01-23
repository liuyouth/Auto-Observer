package com.iolll.liubo.autosimple.data.cj

import android.graphics.Color
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.iolll.liubo.autosimple.R
import me.drakeet.multitype.ItemViewBinder
import me.drakeet.multitype.MultiTypeAdapter

/**
 * Created by LiuBo on 2018/6/12.
 */
 class SelectViewBinder : ItemViewBinder<SelectBean, SelectViewBinder.ViewHolder>() {


    override fun onCreateViewHolder(inflater: LayoutInflater, parent: ViewGroup): ViewHolder {
        val root = inflater.inflate(R.layout.item_echo_search, parent, false)
        return ViewHolder(root)
    }
    var lastPosition = 0
    override fun onBindViewHolder(holder: ViewHolder, echoSearch: SelectBean) {
        holder.name.text = echoSearch.name
        holder.info.setBackgroundColor(if (echoSearch.isSelect) {Color.parseColor("#ff6767")}else{Color.parseColor("#ffffff")})
        holder.bindData(adapter,echoSearch,lastPosition)
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name: TextView = itemView.findViewById(R.id.name)
        val info: TextView = itemView.findViewById(R.id.name)
        private lateinit var data:SelectBean
        private lateinit var adapter:MultiTypeAdapter
        private var lastPosition: Int = 0
        fun bindData(adapter: MultiTypeAdapter,data:SelectBean,lastPosition :Int){
            this.data = data
            this.adapter = adapter
            this.lastPosition = lastPosition
        }
        init {
            //            ButterKnife.bind(this, itemView)
            itemView.setOnClickListener{
                if (lastPosition!=0)
                    (adapter.items[lastPosition] as SelectBean).isSelect = false
                data.isSelect = true
                lastPosition = layoutPosition
                adapter.notifyDataSetChanged()
            }
        }
    }
}



