package com.location.rvdrag

import android.content.Context
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlin.math.abs
import kotlin.math.max

/**
 *
 * @author tianxiaolong
 * time：2024/5/10 19:05
 * description：
 */
class TestItemDecoration(private val adapter: TestAdapter, private val context: Context): RecyclerView.ItemDecoration() {
    private val offset = 10
    private val payloadOffset = 200

    var showTopContainer = false
    var showTopContainerRange:IntRange? = null
    val bg by lazy { BitmapFactory.decodeResource(context.resources, R.drawable.a) }


    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)
        Log.d("tddsa", "invoke getItemOffsets")
        outRect.set(offset, offset, offset, offset)
        val headerPos = if(showTopContainer) adapter.headerPos(parent) else null
        val viewPos = parent.getChildViewHolder(view).bindingAdapterPosition
        if(headerPos != null
            && viewPos < adapter.headerSize
            && viewPos >= headerPos.endStartPos
            && headerPos.endStartPos < adapter.headerSize){
            outRect.bottom = offset + payloadOffset

            Log.d("tddsa", "pos:${parent.getChildViewHolder(view).bindingAdapterPosition} getItemOffsets: $outRect")
        }

    }


    private val paint = Paint().apply {
        color = Color.RED
    }
    override fun onDrawOver(canvas: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        super.onDrawOver(canvas, parent, state)
    }
    private val headerBgRect = Rect()

    data class HeaderPos(
        val startPos: Int,
        val rightPos: Int,
        val endStartPos: Int,
    )

    fun TestAdapter.headerPos(rv: RecyclerView): HeaderPos? {

//        val childCount = rv.childCount
//        if (childCount == 0) {
//            return null
//        }
//        val child = rv.getChildAt(0)
//        val firstUiPos = rv.getChildViewHolder(child).bindingAdapterPosition
//        if(firstUiPos == RecyclerView.NO_POSITION){
//            return null
//        }
        val firstUiPos = 0
        val headerSize = headerSize - firstUiPos
        if(headerSize <= 0){
            return null
        }
//        val rightPos =
//            firstUiPos + if (headerSize < TestAdapter.COLUMNS) headerSize - 1 else TestAdapter.COLUMNS - 1
        val rightPos = headerSize - 1

        val bottomStartPos =
            firstUiPos + if (headerSize % TestAdapter.COLUMNS == 0) (headerSize / TestAdapter.COLUMNS - 1) * TestAdapter.COLUMNS else (headerSize / TestAdapter.COLUMNS) * TestAdapter.COLUMNS
        return HeaderPos(firstUiPos, rightPos, bottomStartPos).also {
            println("headerPos: $it")
        }
    }

    private val bitmapSrc = Rect()
//    private var
    override fun onDraw(canvas: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        super.onDraw(canvas, parent, state)
//        if(showTopContainer.not()){
        showTopContainerRange = null
//        }
    bitmapSrc.set(0, 0, bg.width, bg.height)

    val headerPos = adapter.headerPos(parent) ?: return
        headerBgRect.setEmpty()
        val childCount = parent.childCount
        for (index in 0 until childCount) {
            val child = parent.getChildAt(index)
            val childViewHolder = parent.getChildViewHolder(child)
            if (index == 0 && childViewHolder.bindingAdapterPosition < adapter.headerSize) {
                headerBgRect.left = child.left - offset

                if(childViewHolder.bindingAdapterPosition != 0 ){
//                    bitmapSrc.top = parent.computeVerticalScrollOffset()
//                    headerBgRect.top = -(childViewHolder.itemView.height + offset  + abs(child.top) )
                    val a = (childViewHolder.bindingAdapterPosition / TestAdapter.COLUMNS)
                    headerBgRect.top = -( (childViewHolder.itemView.height + offset * 2) * a +
                            if (child.top > 0) offset - child.top else offset + abs(child.top)
                            )


                        Log.d("fgdwq", " headerBgRect.top:${headerBgRect.top}  rvoffse:${parent.computeVerticalScrollOffset()}"  )


                }else{
                    headerBgRect.top = child.top - offset
                }
//
            }
            if (childViewHolder.bindingAdapterPosition == headerPos.rightPos) {
                headerBgRect.right = child.right + offset
            }
            if (childViewHolder.bindingAdapterPosition == headerPos.endStartPos) {
                headerBgRect.bottom = child.bottom + offset + if(showTopContainer) payloadOffset else 0

//                if(showTopContainer.not()){
                showTopContainerRange = IntRange(child.top, child.bottom - child.height / 2)
//                }
                Log.d("tddsa", "bottom:" + child.bottom)
            }
            if (headerBgRect.bottom != 0 && headerBgRect.right != 0) {
                break
            }
        }


        if (headerBgRect.isEmpty) {
            return
        }
//        Log.d("tddsa", "showTopContainerY:$showTopContainerY")

//        canvas.drawRect(headerBgRect, paint)
//        headerBgRect.top = 0
//        Log.d("abcdsa", "headerBgRect:$headerBgRect")
//    headerBgRect.top = 0
//        bitmapSrc.set(0, -parent.computeVerticalScrollOffset(), bg.width, headerBgRect.height())
        canvas.drawBitmap(bg, bitmapSrc, headerBgRect, paint)
    }
}