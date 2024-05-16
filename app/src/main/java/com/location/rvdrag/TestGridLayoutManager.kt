package com.location.rvdrag

import android.content.Context
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager

/**
 *
 * @author tianxiaolong
 * time：2024/5/13 15:47
 * description：
 */
class TestGridLayoutManager(context: Context?, spanCount: Int, private val adapter: TestAdapter) :
    GridLayoutManager(context, spanCount) {

    init {
        if(USE_PAYLOAD.not()){
            spanSizeLookup = object : SpanSizeLookup() {
                override fun getSpanSize(position: Int): Int {
                    return if (position == adapter.headerSize - 1
                        && adapter.headerSize % spanCount != 0) {
                        spanCount - (adapter.headerSize % spanCount) + 1
                    } else {
                        1
                    }
                }
            }
        }

    }

    override fun measureChild(child: View, widthUsed: Int, heightUsed: Int) {
        super.measureChild(child, widthUsed, heightUsed)
    }
}