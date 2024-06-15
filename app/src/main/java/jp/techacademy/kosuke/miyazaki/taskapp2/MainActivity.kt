package jp.techacademy.kosuke.miyazaki.taskapp2

import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration
import io.realm.kotlin.query.Sort
import jp.techacademy.kosuke.miyazaki.taskapp2.databinding.ActivityMainBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import io.realm.kotlin.ext.query
import io.realm.kotlin.notifications.InitialResults
import io.realm.kotlin.notifications.UpdatedResults
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import io.realm.kotlin.delete

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var taskAdapter: TaskAdapter
    private lateinit var realm: Realm
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

        val config = RealmConfiguration.create(schema = setOf(Task::class))
        realm = Realm.open(config)

        val tasks = realm.query<Task>().sort("date", Sort.DESCENDING).find()

        //Realmが起動、または更新（追加・変更・削除）時にreloadLsitViewを実行する
        CoroutineScope(Dispatchers.Default).launch {
            tasks.asFlow().collect {
                when (it) {
                    //更新時
                    is UpdatedResults -> reloadListView(it.list)
                    //起動時
                    is InitialResults -> reloadListView(it.list)
                    else -> {}
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        //Realmデータベースとの接続を閉じる
        realm.close()
    }

    private fun addTaskForTest() {
        val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.JAPANESE)

        realm.writeBlocking {
            delete<Task>()

            copyToRealm(Task().apply {
                id = 0
                title = "作業"
                contents = "プログラムを書いてpushする"
                date = simpleDateFormat.format(Date())
            })
        }
    }

    private fun reloadListView(tasks: List<Task>) {
        taskAdapter.updateTaskList(tasks)
    }
}
