package com.location.rvdrag

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.whenStarted
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.TestItemTouchHelper
import com.location.rvdrag.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {
    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private val testAdapter by lazy { TestAdapter(
        header = listOf(
            TestData(-100, DataType.Item, true),
            TestData(-99, DataType.Item, true),
            TestData(-98, DataType.Item, true),
            TestData(-97, DataType.Item, true),
            TestData(-96, DataType.Item, true),
            TestData(-95, DataType.Item, true),
            TestData(-94, DataType.Item, true),
//            TestData(-99, DataType.Top),
        ),
        uiList = (20..100).map { TestData(it) }
    ) }
    private val itemDecoration by lazy {             TestItemDecoration(testAdapter, this)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.test1.setOnClickListener {

        }
        with(binding.rv){
            adapter = testAdapter
            addItemDecoration(itemDecoration)
            layoutManager = TestGridLayoutManager(this@MainActivity, 3, testAdapter)
            TestItemTouchHelper(
               if(USE_PAYLOAD){
                   PayloadDragTouchHelper(testAdapter, layoutManager as GridLayoutManager)
               }else{
                   DragTouchHelper(testAdapter, layoutManager as GridLayoutManager, itemDecoration, this)
               }
            ).apply {
                attachToRecyclerView(this@with)
            }


        }
    }
}