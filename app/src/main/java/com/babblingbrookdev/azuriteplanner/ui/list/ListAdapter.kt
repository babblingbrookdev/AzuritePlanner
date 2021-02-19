package com.babblingbrookdev.azuriteplanner.ui.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.widget.PopupMenu
import androidx.recyclerview.widget.RecyclerView
import com.babblingbrookdev.azuriteplanner.R
import com.babblingbrookdev.azuriteplanner.databinding.EntryItemBinding
import com.babblingbrookdev.azuriteplanner.model.Entry
import java.text.SimpleDateFormat
import java.util.*

class ListAdapter(
    private var list: MutableList<Entry>,
    private val listener: OnClickListener
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    interface OnClickListener {
        fun onEditClicked(entry: Entry)
        fun onDeleteClicked(entry: Entry)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding = EntryItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return EntryViewHolder(binding, parent)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as EntryViewHolder).bind(list[position], listener)
    }

    override fun getItemCount(): Int = list.size

    fun replaceData(list: List<Entry>) {
        this.list = list.toMutableList()
        notifyDataSetChanged()
    }

    class EntryViewHolder(binding: EntryItemBinding, private val parent: ViewGroup) :
        RecyclerView.ViewHolder(binding.root) {

        private val binding = binding
        fun bind(entry: Entry, listener: OnClickListener) {
            val date = Date(entry.entryDate.timeInMillis)
            val formatter = SimpleDateFormat("MMMM dd, yyyy", Locale.getDefault())
            binding.entryDate.text = formatter.format(date)

            binding.entryTotalAzurite.text = String.format(
                parent.resources.getString(R.string.entry_total_azurite),
                entry.currentAzurite.toString()
            )
            binding.entryAzuriteGoal.text = String.format(
                parent.resources.getString(R.string.entry_azurite_goal),
                entry.azuriteGoal
            )
            binding.entryHourlyProduction.text = String.format(
                parent.resources.getString(R.string.entry_hourly_production),
                entry.baseProduction
            )
            binding.entryRoyalChallenge.text = String.format(
                parent.resources.getString(R.string.entry_royal_challenge),
                entry.rcAmount
            )
            binding.entryMenu.setOnClickListener {
                val popup = PopupMenu(parent.context, binding.entryMenu)
                popup.menuInflater.inflate(R.menu.menu_entry, popup.menu)
                popup.setOnMenuItemClickListener {
                    when (it.itemId) {
                        R.id.entry_edit -> listener.onEditClicked(entry)
                        R.id.entry_delete -> listener.onDeleteClicked(entry)
                    }
                    return@setOnMenuItemClickListener true
                }
                popup.show()
            }
        }
    }
}