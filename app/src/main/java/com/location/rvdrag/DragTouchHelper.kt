package com.location.rvdrag

import android.graphics.Rect
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
class DragTouchHelper(
    private val adapter: TestAdapter,
    private val gridLayoutManager: GridLayoutManager,
    private val itemDecoration: TestItemDecoration,
    private val recyclerView: RecyclerView,
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



    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
//        if(true){
//            return false
//        }
        if(viewHolder.bindingAdapterPosition < 0 || target.bindingAdapterPosition < 0){
            return false
        }
        if(gridLayoutManager.spanSizeLookup.getSpanSize(target.bindingAdapterPosition) != 1){
//            if(target is TestAdapter.ItemViewHolder){
//                val selectRect = Rect()
//                viewHolder.itemView.getDrawingRect(selectRect)
//                val targetRect = Rect()
//                target.binding.textView.getDrawingRect(targetRect)
//                if(target){
//
//                    return false
//                }
//            }
//            return false

        }



//        (recyclerView.layoutManager as? TestGridLayoutManager)?.d = false

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
    }



    override fun isLongPressDragEnabled(): Boolean {
        return true
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {

    }






    private var selectedViewHolder: RecyclerView.ViewHolder? = null

    override fun onSelectedChanged(viewHolder: RecyclerView.ViewHolder?, actionState: Int) {
        super.onSelectedChanged(viewHolder, actionState)
        Log.d(TAG, "onSelectedChanged: $viewHolder")
        if (actionState == ItemTouchHelper.ACTION_STATE_DRAG) {
            selectedViewHolder = viewHolder
        }
    }

    override fun chooseDropTarget(
        selected: RecyclerView.ViewHolder,
        dropTargets: MutableList<RecyclerView.ViewHolder>,
        curX: Int,
        curY: Int
    ): RecyclerView.ViewHolder? {
//        for (targetViewHolder in dropTargets) {
//            if(targetViewHolder is TestAdapter.ItemViewHolder){
//               if(
//                   targetViewHolder.isHeader
//                   && targetViewHolder.itemView.height /2f >= targetViewHolder.itemView.bottom - curY
//                   ){
//                   Log.i("abc", " is check done")
//                   if(itemDecoration.showTopContainer.not()){
////                       itemDecoration.showTopContainer = true
////                       recyclerView.invalidateItemDecorations()
//                   }
//                   break
//               }
//            }
//        }
        if(selected.bindingAdapterPosition >= adapter.headerSize
            && adapter.headerSize % TestAdapter.COLUMNS == 0){
            val isShowContainer =  itemDecoration.showTopContainerRange?.contains(curY) == true
            if(isShowContainer && itemDecoration.showTopContainer.not()) {
                itemDecoration.showTopContainer = true
                recyclerView.invalidateItemDecorations()
            }else if(isShowContainer.not()
                && itemDecoration.showTopContainer
               ){
                itemDecoration.showTopContainer = false
                recyclerView.invalidateItemDecorations()
            }
        }

        return super.chooseDropTarget(selected, dropTargets, curX, curY)
    }


    override fun getTargetRect(viewHolder: RecyclerView.ViewHolder, outRect: Rect):Boolean {
        if(
            viewHolder is TestAdapter.ItemViewHolder
            &&
            gridLayoutManager.spanSizeLookup.getSpanSize(viewHolder.bindingAdapterPosition) != 1){
            //有占位
            val container = viewHolder.binding.textView
            outRect.set(
                viewHolder.itemView.left + container.left,
                   viewHolder.itemView.top + container.top,
                viewHolder.itemView.left + container.right,
                viewHolder.itemView.top + container.bottom
            )
            return true
        }else{
           return super.getTargetRect(viewHolder, outRect)
        }
    }

    override fun clearView(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder) {
        super.clearView(recyclerView, viewHolder)
        if(itemDecoration.showTopContainer){
            itemDecoration.showTopContainer = false
            recyclerView.invalidateItemDecorations()
        }
    }







}