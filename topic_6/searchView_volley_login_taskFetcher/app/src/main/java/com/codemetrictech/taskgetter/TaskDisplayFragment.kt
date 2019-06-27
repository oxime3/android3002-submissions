package com.codemetrictech.taskgetter

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.card_task.view.*
import kotlinx.android.synthetic.main.fragment_task_display.*

class TaskDisplayFragment(val hostActivity: MainActivity) : Fragment(){
    val TAG = "TaskDisplayFragment"
    private val groupAdapter = GroupAdapter<ViewHolder>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_task_display, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val taskRecyclerView = recyclerView_tasks
        taskRecyclerView.apply {
            adapter = groupAdapter
            layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater?.inflate(R.menu.actions_menu, menu)

        val searchItem = menu?.findItem(R.id.menu_item_search)
        val searchView = searchItem?.actionView as SearchView

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                Log.d(TAG, "onQueryTextSubmit")

                QueryPreferences.setStoredQuery(context!!, query!!)
                hostActivity.pullTasks(query)
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                Log.d(TAG, "onQueryTextChange")
                if(newText == "") {
                   hostActivity.pullTasks("")
                }
                return false
            }

        })

        searchView.setOnSearchClickListener {
            val query = QueryPreferences.getStoredQuery(context!!)
            searchView.setQuery(query, false)
        }

    }

    fun addTaskCard(task: Task) {
        groupAdapter.add(TaskCard(context!!, task ))
    }

    fun removeTaskCards(){
        groupAdapter.clear()
    }

    class TaskCard(private val context: Context, private val task: Task) : Item<ViewHolder>() {
        val TAG = "TaskCard"
        override fun getLayout(): Int = R.layout.card_task

        override fun notifyChanged() {
            super.notifyChanged()
            Log.d(TAG, "notified Changed.")
        }

        override fun bind(viewHolder: ViewHolder, position: Int) {
            val itemView = viewHolder.itemView

            itemView.textView_id.text = task.id.toString()
            itemView.textView_title.text = task.title
            itemView.textView_description.text = task.description

            if (task.done == true){
                itemView.textView_status.text = context.getString(R.string.TASK_COMPLETE)
                itemView.textView_status.setTextColor(ContextCompat.getColor(context, R.color.green))
            } else {
                itemView.textView_status.text = context.getString(R.string.TASK_INCOMPLETE)
                itemView.textView_status.setTextColor(ContextCompat.getColor(context, R.color.red))
            }
        }
    }
}