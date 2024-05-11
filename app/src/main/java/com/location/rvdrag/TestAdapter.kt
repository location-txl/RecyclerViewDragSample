package com.location.rvdrag

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.location.rvdrag.databinding.ItemHomeBinding

/**
 *
 * @author tianxiaolong
 * time：2024/5/10 17:24
 * description：
 */
class TestAdapter(header:List<TestData>, uiList:List<TestData>): RecyclerView.Adapter<TestAdapter.TestViewHolder>() {

    companion object{
        const val COLUMNS = 3
    }
    var headerSize = 0
        private set

    private val list = mutableListOf<TestData>().apply {
        addAll(header)
        if(header.size % COLUMNS != 0){
            repeat(COLUMNS - header.size % COLUMNS){
                add(TestData(-1, DataType.Payload))
            }
        }
        headerSize = size
        addAll(uiList)
    }

    abstract class TestViewHolder(protected val binding:ItemHomeBinding): RecyclerView.ViewHolder(binding.root){
        open fun bind(data:TestData){
            binding.textView.text = data.id.toString()
        }
    }

    class TopViewHolder(binding: ItemHomeBinding): TestViewHolder(binding){
        override fun bind(data: TestData) {
            super.bind(data)
            binding.textView.setBackgroundColor(Color.GRAY)
        }
    }
    class ItemViewHolder(binding: ItemHomeBinding): TestViewHolder(binding)

    class PayloadViewHolder(binding: ItemHomeBinding): TestViewHolder(binding){
        override fun bind(data: TestData) {
            binding.textView.isVisible = false
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TestViewHolder {
        return when(viewType){
            DataType.Top.type -> TopViewHolder(ItemHomeBinding.inflate(LayoutInflater.from(parent.context), parent, false))
            DataType.Payload.type -> PayloadViewHolder(ItemHomeBinding.inflate(LayoutInflater.from(parent.context), parent, false))
            else -> ItemViewHolder(ItemHomeBinding.inflate(LayoutInflater.from(parent.context), parent, false))
        }


    }

    override fun getItemCount(): Int = list.size

    override fun getItemViewType(position: Int): Int {
        return list[position].type.type
    }

    override fun onBindViewHolder(holder: TestViewHolder, position: Int) {
        holder.bind(list[position])
    }

    fun itemMove(srcPos: Int, destPos: Int): Boolean {
        list.add(destPos, list.removeAt(srcPos))
        notifyItemMoved(srcPos, destPos)
        return true
    }

    fun addHeader(itemPos: Int): Boolean {
        val testData = list[itemPos]
        val payloadIndex = list.indexOfFirst { it.type == DataType.Payload }
        if(payloadIndex == -1){
            return false
        }
        list[payloadIndex] = testData.copy(type = DataType.Top)
        headerSize++
//        notifyItemMoved(itemPos, payloadIndex)
        list.removeAt(itemPos)
//        notifyItemChanged(payloadIndex)
//        notifyItemRemoved(itemPos)
//        notifyItemRemoved(payloadIndex)
//        notifyItemRemoved(itemPos)
//        notifyItemInserted(payloadIndex)
//        notifyItemMoved(itemPos, payloadIndex)
//        notifyItemRemoved(itemPos)
//        notifyItemChanged(payloadIndex)
        notifyDataSetChanged()
        return true
    }

    private inline fun findIndex(predicate: (TestData) -> Boolean): Int{
        return list.indexOfFirst(predicate)
    }
}



sealed interface DataType{
    val type:Int
    data object Top : DataType{
        override val type: Int = 0
    }
    data object Item : DataType{
        override val type: Int = 1
    }

    data object Payload: DataType{
        override val type: Int = 2
    }

}
data class TestData(val id:Int, val type:DataType = DataType.Item)