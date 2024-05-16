package com.location.rvdrag

import android.graphics.Rect
import android.util.Log
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import androidx.recyclerview.widget.TestItemTouchHelper
import kotlin.math.abs

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

//        if(viewHolder is TestAdapter.PayloadViewHolder){
//            return makeMovementFlags(0, 0)
//        }
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
        return false
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {

    }






    private var selectedViewHolder: RecyclerView.ViewHolder? = null

    override fun onSelectedChanged(viewHolder: RecyclerView.ViewHolder?, actionState: Int) {
        super.onSelectedChanged(viewHolder, actionState)
        Log.d(TAG, "onSelectedChanged: $viewHolder")
        if (actionState == ItemTouchHelper.ACTION_STATE_DRAG) {
            selectedViewHolder = viewHolder
            addSelectToHeader = false
            viewHolder?.let {
                it.itemView.scaleX = 1.2f
                it.itemView.scaleY = 1.2f
            }
        }else if(selectedViewHolder != null && actionState == ItemTouchHelper.ACTION_STATE_IDLE){
            selectedViewHolder?.let {
                if(addSelectToHeader){
                    adapter.addSelectToHeader(it)
                }else if(itemDecoration.showTopContainer){
                    adapter.addSelectToHeader(it)
                }
            }

            selectedViewHolder = null
        }
    }




    private var addSelectToHeader = false
//    private val addHeadCheckDiff = 50

    override fun onSelectMove(select: ViewHolder, curX: Int, curY: Int) {
        val showTopContainerRange = itemDecoration.showTopContainerRange
        val ifTopContainerIsShowRange = itemDecoration.ifTopContainerIsShowRange
        Log.d("txlkl", "curY: $curY  ifTopContainerIsShowRange:$ifTopContainerIsShowRange")
        if(showTopContainerRange != null
            &&
            ifTopContainerIsShowRange != null
            &&
            select.bindingAdapterPosition >= adapter.headerSize
            && adapter.headerSize % TestAdapter.COLUMNS == 0){
            val isShowContainer = if(!itemDecoration.showTopContainer){
                showTopContainerRange.contains(curY)
            }else{
                ifTopContainerIsShowRange.contains(curY)
            }
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
    }

    override fun chooseDropTarget(
        selected: RecyclerView.ViewHolder,
        dropTargets: MutableList<RecyclerView.ViewHolder>,
        curX: Int,
        curY: Int
    ): RecyclerView.ViewHolder? {


        val chooseDropTarget = super.chooseDropTarget(selected, dropTargets, curX, curY)
        addSelectToHeader = false
        Log.d("txlnv", "chooseDropTarget: $chooseDropTarget")
        val headerSize = adapter.headerSize
        val headLastHolder:TestAdapter.ItemViewHolder? = if(chooseDropTarget != null || headerSize % TestAdapter.COLUMNS == 0){
            null
        }else{
            dropTargets.find {
                it.bindingAdapterPosition ==  headerSize - 1 && it is TestAdapter.ItemViewHolder
            } as? TestAdapter.ItemViewHolder
        }
        if(headLastHolder != null){
            val right = curX + selected.itemView.width
            val bottom = curY + selected.itemView.height
            val left = curX
            val top = curY
            val triggerHeight = (bottom - top) * 0.7
            val triggerWidth = (right - left) * 0.8
            val container = headLastHolder.binding.textView
            val headLastLeft = headLastHolder.itemView.left + container.left + container.width
            val headLastTop = headLastHolder.itemView.top + container.top
            val headLastRight = headLastLeft + (headLastHolder.itemView.width - container.width)
            val headLastBottom = headLastHolder.itemView.top + container.bottom
//            Log.d("txlddd", " select left:$left headLastLeft:$headLastLeft selectRight:$right headLastRight:$headLastRight")
            if(
//                left >= headLastLeft && right <= headLastRight
                (
                        (left >= headLastLeft && abs(headLastRight - left) >= triggerWidth)
                        ||
                                (right <= headLastRight && abs(headLastRight - left) >= triggerWidth)
                        )
                && (
                        (top >= headLastTop && abs(top - headLastBottom) >= triggerHeight)
                        ||
                                (bottom <= headLastBottom && abs(headLastTop - bottom) >= triggerHeight)
                        )
            ){
                addSelectToHeader = true
            }




        }
        return chooseDropTarget
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
        viewHolder.itemView.scaleX = 1.0f
        viewHolder.itemView.scaleY = 1.0f
    }

    override fun applyTargetTranslate(
        recyclerView: RecyclerView,
        prevSelected: ViewHolder,
        targetTranslateX: Float,
        targetTranslateY: Float
    ): FloatArray {


        return super.applyTargetTranslate(
            recyclerView,
            prevSelected,
            targetTranslateX,
            targetTranslateY
        )
    }

    override fun getAnimationDuration(
        recyclerView: RecyclerView,
        animationType: Int,
        animateDx: Float,
        animateDy: Float
    ): Long {
        if((addSelectToHeader
                    || itemDecoration.showTopContainer
                ) && animationType == ItemTouchHelper.ANIMATION_TYPE_DRAG){
            return 0
        }
        return super.getAnimationDuration(recyclerView, animationType, animateDx, animateDy)
    }









}