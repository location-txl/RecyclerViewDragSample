package com.location.rvdrag

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.whenStarted
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import com.location.rvdrag.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {
    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private val testAdapter by lazy { TestAdapter(
        header = listOf(
            TestData(-100, DataType.Top),
//            TestData(-99, DataType.Top),
        ),
        uiList = (20..100).map { TestData(it) }
    ) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.test1.setOnClickListener {
//            testAdapter.itemMove(0, 2)
//            testAdapter.notifyItemMoved(4, 1)
//            testAdapter.notifyItemChanged(0)
            testAdapter.notifyItemRemoved(0)
        }
        with(binding.rv){
            layoutManager = GridLayoutManager(this@MainActivity, 3)
            adapter = testAdapter
            addItemDecoration(TestItemDecoration(testAdapter))
            ItemTouchHelper(DragTouchHelper(testAdapter)).apply {
                attachToRecyclerView(this@with)
            }
        }
    }
}