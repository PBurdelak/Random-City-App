package com.pburdelak.randomcityapp.screen.list

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.pburdelak.randomcityapp.databinding.ItemListElementBinding
import com.pburdelak.randomcityapp.model.CityColorCombination
import java.text.SimpleDateFormat
import java.util.Locale

class ListRVAdapter(
    private val onClick: (position: Int) -> Unit
) : RecyclerView.Adapter<ListRVAdapter.ViewHolder>() {

    private var items: List<CityColorCombination> = emptyList()

    fun replaceData(list: List<CityColorCombination>) {
        val newList = mutableListOf<CityColorCombination>()
        newList.addAll(list)
        val diffCallback = DiffCallback(items, newList)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        items = newList
        diffResult.dispatchUpdatesTo(this)
    }
    override fun getItemCount(): Int = items.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemListElementBinding.inflate(inflater, parent, false)
        return ViewHolder(binding).apply {
            itemView.setOnClickListener { onClick(adapterPosition) }
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) =
        holder.bind(items[position])

    class ViewHolder(private val binding: ItemListElementBinding) :
        RecyclerView.ViewHolder(binding.root) {

        companion object {
            private val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", Locale.ENGLISH)
        }

        fun bind(item: CityColorCombination) {
            val color = Color.parseColor(item.color.toLowerCase(Locale.ROOT))
            binding.textName.text = item.city
            binding.textName.setTextColor(color)
            binding.textDate.text = formatter.format(item.creationDate)
        }
    }

    private class DiffCallback(
        private val oldList: List<CityColorCombination>,
        private val newList: List<CityColorCombination>
    ) : DiffUtil.Callback() {

        override fun getOldListSize(): Int = oldList.size

        override fun getNewListSize(): Int = newList.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
            oldList[oldItemPosition].creationDate == newList[newItemPosition].creationDate

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
            oldList[oldItemPosition] == newList[newItemPosition]
    }
}