package jp.techacademy.kosuke.miyazaki.taskapp2

import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import jp.techacademy.kosuke.miyazaki.taskapp2.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private lateinit var taskAdapter: TaskAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }

        taskAdapter = TaskAdapter(this)
        binding.listView.adapter = taskAdapter

        binding.listView.setOnItemClickListener { parent, view, position, id ->
        }

        binding.listView.setOnItemLongClickListener { parent, view, position, id ->
            true
        }

        reloadListView()
    }

    private fun reloadListView() {
        val tasks = mutableListOf("aaa", "bbb", "ccc")
        taskAdapter.updateTaskList(tasks)
    }
}