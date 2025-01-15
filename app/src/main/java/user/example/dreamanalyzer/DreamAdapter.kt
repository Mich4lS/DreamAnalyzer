package user.example.dreamanalyzer

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

data class Dream(val date: String, val details: String)

class DreamAdapter(
    private val dreams: List<Dream>,
    private val onClick: (Dream, Int) -> Unit
) : RecyclerView.Adapter<DreamAdapter.DreamViewHolder>() {

    inner class DreamViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val dateText: TextView = view.findViewById(R.id.dreamDate)
        val summaryText: TextView = view.findViewById(R.id.dreamSummary)

        fun bind(dream: Dream, position: Int) {
            dateText.text = dream.date
            summaryText.text = dream.details.take(30) + "..."
            itemView.setOnClickListener { onClick(dream, position) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DreamViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_dream, parent, false)
        return DreamViewHolder(view)
    }

    override fun onBindViewHolder(holder: DreamViewHolder, position: Int) {
        holder.bind(dreams[position], position)
    }

    override fun getItemCount() = dreams.size
}
