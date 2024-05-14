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
//            TestData(-99, DataType.Top),
        ),
        uiList = (20..100).map { TestData(it) }
    ) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.test1.setOnClickListener {
            testAdapter.appendHeader()
        }
        with(binding.rv){
            adapter = testAdapter
            layoutManager = TestGridLayoutManager(this@MainActivity, 3, testAdapter)
            addItemDecoration(TestItemDecoration(testAdapter))
            TestItemTouchHelper(
               if(USE_PAYLOAD){
                   PayloadDragTouchHelper(testAdapter, layoutManager as GridLayoutManager)
               }else{
                   DragTouchHelper(testAdapter, layoutManager as GridLayoutManager)
               }
            ).apply {
                attachToRecyclerView(this@with)
            }
        }
    }
}