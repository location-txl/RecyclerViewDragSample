package com.location.rvdrag

import android.util.Log
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView

/**
 *
 * @author tianxiaolong
 * time：2024/5/10 18:51
 * description：
 */
class DragTouchHelper(private val adapter: TestAdapter): ItemTouchHelper.Callback() {
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
//        Log.d(TAG, " target is ${target.javaClass.simpleName}")
//        Log.d(TAG, " ${viewHolder.bindingAdapterPosition} ${target.bindingAdapterPosition}")
        if(viewHolder.bindingAdapterPosition < 0 || target.bindingAdapterPosition < 0){
            return false
        }
        payloadHolder = target as? TestAdapter.PayloadViewHolder
        if(target is TestAdapter.PayloadViewHolder){
//            return adapter.addHeader(viewHolder.bindingAdapterPosition)
            return true
        }
        return adapter.itemMove(viewHolder.bindingAdapterPosition, target.bindingAdapterPosition)
    }

    override fun onMoved(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        fromPos: Int,
        target: RecyclerView.ViewHolder,
        toPos: Int,
        x: Int,
        y: Int
    ) {
        super.onMoved(recyclerView, viewHolder, fromPos, target, toPos, x, y)
//      Log.d(TAG, "onMoved: $fromPos $toPos")
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {

    }






    override fun isLongPressDragEnabled(): Boolean {
        return true
    }




    override fun clearView(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder) {
        super.clearView(recyclerView, viewHolder)
//        payloadHolder?.let {
//            adapter.addHeader(viewHolder.bindingAdapterPosition)
//            payloadHolder = null
//        }

    }

    override fun getAnimationDuration(
        recyclerView: RecyclerView,
        animationType: Int,
        animateDx: Float,
        animateDy: Float
    ): Long {
        if(payloadHolder != null){
            return 0
        }
        return super.getAnimationDuration(recyclerView, animationType, animateDx, animateDy)
    }

    private var selectedViewHolder: RecyclerView.ViewHolder? = null

    override fun onSelectedChanged(viewHolder: RecyclerView.ViewHolder?, actionState: Int) {
        super.onSelectedChanged(viewHolder, actionState)
        Log.d(TAG, "onSelectedChanged: $viewHolder")
        if (actionState == ItemTouchHelper.ACTION_STATE_DRAG) {
            selectedViewHolder = viewHolder
        }else if(viewHolder == null && actionState == ItemTouchHelper.ACTION_STATE_IDLE && payloadHolder != null){
            selectedViewHolder?.let {
                adapter.addHeader(it.bindingAdapterPosition)
                selectedViewHolder = null
            }
            payloadHolder = null
        }
    }



}