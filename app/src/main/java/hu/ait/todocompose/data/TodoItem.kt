package hu.ait.todocompose.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import hu.ait.todocompose.R
import hu.ait.todocompose.ui.screen.shopping.TodoType
import java.io.Serializable


@Entity(tableName = "todotable")
data class TodoItem(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "title") val title:String,
    @ColumnInfo(name = "description") val description:String,
    @ColumnInfo(name = "createDate") val createDate:String,
    @ColumnInfo(name = "priority") var priority: TodoPriority,
    @ColumnInfo(name = "isDone") var isDone: Boolean,
    @ColumnInfo(name = "category") var category: TodoType,
    @ColumnInfo(name = "price") var price: Int,
    @ColumnInfo(name = "currency") var currency: String
) : Serializable

enum class TodoPriority {
    NORMAL, HIGH;

    /*
    fun getIcon(): Int {
        return if (this == NORMAL) R.drawable.others else R.drawable.important
    }

     */
}

/*
enum class TodoType{
    FOOD,CLOTHING,ELECTRONICS,OTHERS
}
 */