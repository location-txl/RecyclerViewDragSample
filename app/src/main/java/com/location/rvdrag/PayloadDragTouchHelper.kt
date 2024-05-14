package com.location.rvdrag

import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.TestItemTouchHelper

/**
 *
 * @author tianxiaolong
 * time：2024/5/10 18:51
 * description：
 */
class PayloadDragTouchHelper(
    private val adapter: TestAdapter,
    private val gridLayoutManager: GridLayoutManager
    ): TestItemTouchHelper.Callback() {
    companion object{
        private const val TAG = "DragTouchHelper"
    }
    override fun getMovementFlags(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder
    ): Int {
        if(viewHolder is TestAdapter.PayloadViewHolder){
            return makeMovementFlags(0, 0)
        }
        return makeMovementFlags(ItemTouchHelper.UP or ItemTouchHelper.DOWN or ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT, 0)
    }

    private var payloadHolder: TestAdapter.PayloadViewHolder? = null


    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        if(viewHolder.bindingAdapterPosition < 0 || target.bindingAdapterPosition < 0){
            return false
        }

        if(USE_PAYLOAD){
            payloadHolder = target as? TestAdapter.PayloadViewHolder
            if(target is TestAdapter.PayloadViewHolder){
                return true
            }
        }
//        (recyclerView.layoutManager as? TestGridLayoutManager)?.d = false

        return adapter.itemMove(viewHolder.bindingAdapterPosition, target.bindingAdapterPosition)
    }









    override fun isLongPressDragEnabled(): Boolean {
        return true
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {

    }


    override fun clearView(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder) {
        super.clearView(recyclerView, viewHolder)
        if(USE_PAYLOAD){
            payloadHolder?.let {
                adapter.addHeader(viewHolder.bindingAdapterPosition, recyclerView)
                payloadHolder = null
            }
        }



    }



    private var selectedViewHolder: RecyclerView.ViewHolder? = null

    override fun onSelectedChanged(viewHolder: RecyclerView.ViewHolder?, actionState: Int) {
        super.onSelectedChanged(viewHolder, actionState)
        Log.d(TAG, "onSelectedChanged: $viewHolder")
        if (actionState == ItemTouchHelper.ACTION_STATE_DRAG) {
            selectedViewHolder = viewHolder
        }
    }




    override fun applyTargetTranslate(
        recyclerView: RecyclerView,
        prevSelected: RecyclerView.ViewHolder,
        targetTranslateX: Float,
        targetTranslateY: Float
    ): FloatArray {
        if(USE_PAYLOAD && payloadHolder != null){
           val nextPayloadHolderPos =  adapter.findNextPayloadPos()
            if (nextPayloadHolderPos != null) {
                val payloadViewHolder = recyclerView.findViewHolderForAdapterPosition(nextPayloadHolderPos)
                if(payloadViewHolder?.itemView != null){
                    val top = payloadViewHolder.itemView.top.toFloat()
                    val left = payloadViewHolder.itemView.left.toFloat()

                    return floatArrayOf(left - prevSelected.itemView.left, top - prevSelected.itemView.top).also {
                        Log.d(TAG, "applyTargetTranslate: ${it.joinToString()}")
                    }
                }
            }
        }
        return super.applyTargetTranslate(recyclerView, prevSelected, targetTranslateX, targetTranslateY)
    }

    override fun chooseDropTarget(
        selected: RecyclerView.ViewHolder,
        dropTargets: MutableList<RecyclerView.ViewHolder>,
        curX: Int,
        curY: Int
    ): RecyclerView.ViewHolder? {
        return super.chooseDropTarget(selected, dropTargets, curX, curY)
    }
}