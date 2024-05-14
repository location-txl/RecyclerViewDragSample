package com.location.rvdrag

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.view.View
import androidx.core.view.forEach
import androidx.recyclerview.widget.RecyclerView

/**
 *
 * @author tianxiaolong
 * time：2024/5/10 19:05
 * description：
 */
class TestItemDecoration(private val adapter: TestAdapter): RecyclerView.ItemDecoration() {
    private val offset = 10
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)
        outRect.set(offset, offset, offset, offset)
    }


    private val paint = Paint().apply {
        color = Color.RED
    }
    override fun onDrawOver(canvas: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        super.onDrawOver(canvas, parent, state)
    }
    private val headerBgRect = Rect()

    override fun onDraw(canvas: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        super.onDraw(canvas, parent, state)
//        val layoutManager = parent.layoutManager ?: return
//        val headerSize = adapter.headerSize
//        val lastRowFirst = (headerSize / TestAdapter.COLUMNS - 1) * TestAdapter.COLUMNS
//        if(headerSize > 0){
//            headerBgRect.setEmpty()
//            parent.forEach {
////            layoutManager.viewh
//                val childViewHolder = parent.getChildViewHolder(it)
//                if(childViewHolder.bindingAdapterPosition == 0){
//                    headerBgRect.left = it.left - offset
//                    headerBgRect.top = it.top - offset
//                }
//                if(childViewHolder.bindingAdapterPosition == TestAdapter.COLUMNS - 1){
//                    headerBgRect.right = it.right + offset
//                }
//                if(lastRowFirst == childViewHolder.bindingAdapterPosition){
//                    headerBgRect.bottom = it.bottom + offset
//                }
//            }
//
//            if(headerBgRect.isEmpty){
//                return
//            }
//            canvas.drawRect(headerBgRect, paint)

//        }


    }
}