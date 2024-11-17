package hu.ait.todocompose.ui.screen.shopping

import androidx.annotation.DrawableRes
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import hu.ait.todocompose.R
import hu.ait.todocompose.data.TodoDAO
import hu.ait.todocompose.data.TodoItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TodoViewModel @Inject constructor(
    val todoDAO: TodoDAO
) : ViewModel() {


    fun getAllToDoList(): Flow<List<TodoItem>> {
        return todoDAO.getAllTodos()
    }

    fun addTodoList(todoItem: TodoItem) {
        viewModelScope.launch(Dispatchers.IO) {
            todoDAO.insert(todoItem)
        }
    }

    fun removeTodoItem(todoItem: TodoItem) {
        //launch will run the code in the courotine meaning htat it will not block the main thread
        //separate thread than the composition
        //viewModelScope will be destroyed when the viewmodel is destroyed
        ///Dispatchers.IO means that I want to run this on the background threa and not the UI thread
        viewModelScope.launch(Dispatchers.IO){
            todoDAO.delete(todoItem)
        }
    }

    fun editTodoItem(editedTodo: TodoItem) {
        viewModelScope.launch(Dispatchers.IO){
            todoDAO.update(editedTodo)
        }
    }

    fun changeTodoState(todoItem: TodoItem, value: Boolean) {
        //state update
        //will leave the checkbox checked even after leaving the screen
        val updatedTodo=todoItem.copy()
        updatedTodo.isDone=value
        viewModelScope.launch(Dispatchers.IO){
            todoDAO.update(updatedTodo)
        }
    }

    fun clearAllTodos() {
        viewModelScope.launch(Dispatchers.IO){
            todoDAO.deleteAllTodos()
        }
    }

    suspend fun getAllTodoNum(): Int {
        return todoDAO.getTodosNum()
    }

    fun getShoppingByCategory(category: TodoType): Flow<List<TodoItem>> {
        return todoDAO.getShoppingByCategory(category)
    }

    suspend fun getShoppingCountByCategory(category: TodoType): Int {
        return todoDAO.getShoppingNumByCategory(category)
    }

    suspend fun getShoppingPriceByCategory(category: TodoType): Int {
        return todoDAO.getShoppingPriceByCategory(category)
    }

}

enum class TodoType(val typeName:String, @DrawableRes val icon: Int){
    FOOD("Food",R.drawable.food),
    ELECTRONIC("Electronic",R.drawable.electronics),
    BOOK("Book",R.drawable.book),
    OTHER("Other",R.drawable.others)
}