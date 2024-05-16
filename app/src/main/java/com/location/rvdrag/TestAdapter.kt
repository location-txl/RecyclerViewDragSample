package com.location.rvdrag

import android.graphics.Color
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.location.rvdrag.databinding.ItemHomeBinding

/**
 *
 * @author tianxiaolong
 * time：2024/5/10 17:24
 * description：
 */
const val USE_PAYLOAD = false
class TestAdapter(
    header:List<TestData>,
    uiList:List<TestData>,
    private val longClickDrag:(holder:ViewHolder) -> Unit
    ): RecyclerView.Adapter<TestAdapter.TestViewHolder>() {

    companion object{
        const val COLUMNS = 3
    }
    private var tmpHeaderSize = -1

    private fun refreshHeaderSize(){
        if(tmpHeaderSize == -1){
            tmpHeaderSize = list.indexOfFirst { it.isHeader.not() }.let {
                if(it == -1){
                    if(list.lastOrNull()?.isHeader == true) list.size else 0
                }else{
                    it
                }
            }
        }
    }
    val headerSize:Int
        get() = refreshHeaderSize().let {
            tmpHeaderSize
        }

    private val list = mutableListOf<TestData>().apply {
        addAll(header)
        if(USE_PAYLOAD && header.size % COLUMNS != 0){
            repeat(COLUMNS - header.size % COLUMNS){
                add(TestData(-1, DataType.Payload))
            }
        }
        addAll(uiList)
    }

    abstract class TestViewHolder(
        val binding:ItemHomeBinding,
        private val longClickDrag:(holder:ViewHolder) -> Unit
        ): RecyclerView.ViewHolder(binding.root){
        init {
            binding.textView.setOnLongClickListener {
                longClickDrag(this)
                true
            }
        }
        open fun bind(data:TestData){
            binding.root.isVisible = true
            binding.textView.text = data.id.toString()
        }
    }


    open class ItemViewHolder(binding: ItemHomeBinding,longClickDrag:(holder:ViewHolder) -> Unit): TestViewHolder(binding, longClickDrag){

        var isHeader = false
            private set
        override fun bind(data: TestData) {
            super.bind(data)
            isHeader = data.isHeader
            Log.d("ItemViewHolder", "bind: ${data.id}")
        }

    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TestViewHolder {
        return ItemViewHolder(ItemHomeBinding.inflate(LayoutInflater.from(parent.context), parent, false), longClickDrag)


    }

    override fun getItemCount(): Int = list.size

    override fun getItemViewType(position: Int): Int {
//        if(USE_PAYLOAD.not()
//            && position == headerSize - 1
//            && headerSize % COLUMNS != 0
//            ){
//                return DataType.Item_Full.type
//            }
        return list[position].type.type
    }

    override fun onBindViewHolder(holder: TestViewHolder, position: Int) {
        holder.bind(list[position])
    }

    fun itemMove(srcPos: Int, destPos: Int, isAppendHeader:Boolean = false): Boolean {

        Log.d("txlA", " headerSize:$headerSize" )
        val lastHeaderSize = if(isAppendHeader) headerSize + 1 else headerSize
        val isHeader =  if(srcPos >= headerSize && destPos < lastHeaderSize){
             true
        }else if(srcPos < headerSize && destPos >= headerSize){
            false
        }else{
            null
        }
        list.add(destPos, list.removeAt(srcPos).apply {
            isHeader?.let {
                this.isHeader = it
            }
        })
        isHeader?.let {
            tmpHeaderSize = -1
        }
        Log.d("txlA", " itemMove: src:$srcPos dest:$destPos afterHeaderSize:$headerSize" )
        notifyItemMoved(srcPos, destPos)

        return true
    }

    fun addHeader(itemPos: Int, recyclerView: RecyclerView): Boolean {
        if(USE_PAYLOAD.not()){
            return false
        }
        val testData = list[itemPos]
        val payloadIndex = list.indexOfFirst { it.type == DataType.Payload }
        if(payloadIndex == -1){
            return false
        }
        list[payloadIndex] = testData.copy(type = DataType.Item)
//        notifyItemMoved(itemPos, payloadIndex)
        list.removeAt(itemPos)
//
//        notifyItemRemoved(payloadIndex)
//        notifyItemRemoved(itemPos)
//        notifyItemInserted(payloadIndex)
//        notifyItemMoved(itemPos, payloadIndex)
//        notifyItemRemoved(itemPos)
//        notifyItemChanged(payloadIndex)
//        notifyDataSetChanged()
        registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver(){


            override fun onItemRangeChanged(positionStart: Int, itemCount: Int) {
                super.onItemRangeChanged(positionStart, itemCount)
                Handler(Looper.getMainLooper()).postDelayed( {
                    Log.d("ItemViewHolder", "trigger")

//                    recyclerView.findViewHolderForAdapterPosition(itemPos)?.itemView?.isVisible = false
                    recyclerView.findViewHolderForAdapterPosition(itemPos)?.itemView?.let {
                        it.isVisible = false
                        it.translationX = 0f
                        it.translationY = 0f
                    }
                    notifyItemRemoved(itemPos)


                },250)


                unregisterAdapterDataObserver(this)
            }


        })
        Log.d("ItemViewHolder", "addHeader: $itemPos $payloadIndex")
        notifyItemChanged(payloadIndex)

        return true
    }

    private inline fun findIndex(predicate: (TestData) -> Boolean): Int{
        return list.indexOfFirst(predicate)
    }

    fun findNextPayloadPos():Int? = list.indexOfFirst { it.type == DataType.Payload }.let {
        if(it == -1) null else it
    }

    fun appendHeader() {
//        list.add( 0, TestData(-1, DataType.Top))
//        notifyDataSetChanged()
//        notifyItemRangeInserted(1, 1)
    }

    fun addSelectToHeader(selectHolder: RecyclerView.ViewHolder) {
        val selectPos = selectHolder.bindingAdapterPosition
        itemMove(selectPos, headerSize, isAppendHeader = true)
    }
}



sealed interface DataType{
    val type:Int

    data object Item : DataType{
        override val type: Int = 1
    }

    data object Payload: DataType{
        override val type: Int = 2
    }
    data object Item_Full: DataType{
        override val type: Int = 3
    }

}
data class TestData(val id:Int, val type:DataType = DataType.Item, var isHeader:Boolean = false)