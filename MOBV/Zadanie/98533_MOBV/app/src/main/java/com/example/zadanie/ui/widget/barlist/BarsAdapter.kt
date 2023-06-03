package com.example.zadanie.ui.widget.barlist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.zadanie.R
import com.example.zadanie.data.db.model.BarItem
import com.example.zadanie.helpers.autoNotify
import com.google.android.material.chip.Chip
import kotlin.properties.Delegates

class BarsAdapter(val events: BarsEvents? = null) :
    RecyclerView.Adapter<BarsAdapter.BarItemViewHolder>() {
    var items: List<BarItem> by Delegates.observable(emptyList()) { _, old, new ->
        autoNotify(old, new) { o, n -> o.id.compareTo(n.id) == 0 }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BarItemViewHolder {
        return BarItemViewHolder(parent)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: BarItemViewHolder, position: Int) {
        holder.bind(items[position], events)
    }

    class BarItemViewHolder(
        private val parent: ViewGroup, itemView: View = LayoutInflater.from(parent.context).inflate(
            R.layout.bar_item, parent, false
        )
    ) : RecyclerView.ViewHolder(itemView) {

        fun bind(item: BarItem, events: BarsEvents?) {
            itemView.findViewById<TextView>(R.id.name).text = item.name
            itemView.findViewById<TextView>(R.id.count).text = item.users.toString()
            itemView.findViewById<Chip>(R.id.type).text = item.type
            when (item.type) {
                "fast_food" -> itemView.findViewById<ImageView>(R.id.type_img)
                    .setImageResource(R.drawable.ic_baseline_fastfood_24)
                "cafe" -> itemView.findViewById<ImageView>(R.id.type_img)
                    .setImageResource(R.drawable.ic_baseline_coffee_24)
                "Cafe" -> itemView.findViewById<ImageView>(R.id.type_img)
                    .setImageResource(R.drawable.ic_baseline_coffee_24)
                "pub" -> itemView.findViewById<ImageView>(R.id.type_img)
                    .setImageResource(R.drawable.ic_baseline_sports_bar_24)
                "restaurant" -> itemView.findViewById<ImageView>(R.id.type_img)
                    .setImageResource(R.drawable.ic_baseline_table_restaurant_24)
                "bar" -> itemView.findViewById<ImageView>(R.id.type_img)
                    .setImageResource(R.drawable.ic_baseline_wine_bar_24)
                else -> {
                    itemView.findViewById<ImageView>(R.id.type_img)
                        .setImageResource(R.drawable.ic_baseline_restaurant_24)
                }
            }
            itemView.setOnClickListener { events?.onBarClick(item) }
        }
    }
}