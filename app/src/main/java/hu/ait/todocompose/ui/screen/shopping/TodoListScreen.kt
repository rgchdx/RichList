package hu.ait.todocompose.ui.screen.shopping

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.outlined.ArrowDropDown
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import hu.ait.todocompose.R
import hu.ait.todocompose.data.TodoItem
import hu.ait.todocompose.data.TodoPriority
import kotlinx.coroutines.launch
import java.util.Date

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TodoListScreen(
    modifier: Modifier = Modifier, viewModel: TodoViewModel = hiltViewModel(),
    onNavigateToSummary: (Int,Int,Int,Int,Int,Int,Int,Int,Int,Int)->Unit
) {
    val todoList by viewModel.getAllToDoList().collectAsState(emptyList())

    var showAddDialog by remember { mutableStateOf(false) }
    var todoToEdit: TodoItem? by rememberSaveable{
        mutableStateOf(null)
    }
    val coroutinesScope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        stringResource(R.string.richlist),
                        color = Color(0xFFDAE812)
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFFF27B20),
                    titleContentColor = Color(0xFFF27B20),
                    navigationIconContentColor = MaterialTheme.colorScheme.onSecondaryContainer,
                    actionIconContentColor = MaterialTheme.colorScheme.onSecondaryContainer
                ),
                actions = {
                    IconButton(
                        onClick = {
                            showAddDialog = true
                        }
                    ) {
                        Icon(
                            Icons.Filled.AddCircle, contentDescription = stringResource(R.string.add)
                        )
                    }
                    IconButton(
                        onClick = {
                            viewModel.clearAllTodos()
                        }
                    ) {
                        Icon(
                            Icons.Filled.Delete, contentDescription = stringResource(R.string.delete_all)
                        )
                    }
                    IconButton(
                        onClick = {
                            coroutinesScope.launch{
                                val todoNum = viewModel.getAllTodoNum()
                                val todoPrice = viewModel.getAllTodoNum()
                                val foodNum = viewModel.getShoppingCountByCategory(TodoType.FOOD)
                                val foodPrice = viewModel.getShoppingPriceByCategory(TodoType.FOOD)
                                val electronicsNum = viewModel.getShoppingCountByCategory(TodoType.ELECTRONIC)
                                val electronicsPrice = viewModel.getShoppingPriceByCategory(TodoType.ELECTRONIC)
                                val bookNum = viewModel.getShoppingCountByCategory(TodoType.BOOK)
                                val bookPrice = viewModel.getShoppingPriceByCategory(TodoType.BOOK)
                                val otherNum = viewModel.getShoppingCountByCategory(TodoType.OTHER)
                                val otherPrice = viewModel.getShoppingPriceByCategory(TodoType.OTHER)

                                onNavigateToSummary(todoNum,todoPrice,foodNum,foodPrice,electronicsNum,electronicsPrice,bookNum,bookPrice,otherNum,otherPrice)
                            }
                        }
                    ) {
                        Icon(
                            Icons.Filled.Info, contentDescription = stringResource(R.string.info)
                        )
                    }
                }
            )
        }
    ) { innerpadding ->
        Column(modifier = modifier
            .fillMaxSize()
            .padding(innerpadding)) {
            if (todoList.isEmpty()) {
                Text(
                    stringResource(R.string.empty_list), modifier = Modifier
                        .fillMaxSize()
                        .wrapContentSize(Alignment.Center)
                )
            } else {
                LazyColumn {
                    items(todoList) { todoItem ->
                        TodoCard(todoItem,
                            onTodoDelete = {
                                    item -> viewModel.removeTodoItem(item)
                            },
                            onTodoChecked = {item, checked -> viewModel.changeTodoState(item, checked)
                            },
                            onTodoEdit = {
                                    item ->
                                todoToEdit = item
                                showAddDialog = true
                            }
                        )
                    }
                }
            }
        }
    }

    if (showAddDialog) {
        TodoDialog(viewModel,
            todoToEdit = todoToEdit,
            onCancel = {
                showAddDialog = false
                todoToEdit = null
            }
        )
    }
}

@Composable
fun TodoDialog(
    viewModel: TodoViewModel,
    onCancel: () -> Unit,
    todoToEdit: TodoItem? = null
) {
    var todoTitle by remember { mutableStateOf(
        todoToEdit?.title ?: ""
    ) }
    var todoDesc by remember { mutableStateOf(
        todoToEdit?.description ?: ""
    ) }
    var important by remember { mutableStateOf(
        if (todoToEdit != null) {
            todoToEdit.priority == TodoPriority.HIGH
        } else {
            false
        }
    ) }
    var nameOfCategory by remember { mutableStateOf(TodoType.FOOD)}
    var price by remember { mutableStateOf(0)}
    var selectedCurrency by remember {mutableStateOf("USD")}
    val currencyList by remember { mutableStateOf(listOf("USD", "EUR", "YEN", "HUF")) }
    var todoTitleError by remember { mutableStateOf(false) }
    var todoDescError by remember { mutableStateOf(false) }
    var todoPriceError by remember {mutableStateOf(false)}

    Dialog(onDismissRequest = {
        onCancel()
    }) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            shape = RoundedCornerShape(size = 8.dp)
        ) {
            Column(
                modifier = Modifier.padding(15.dp)
            ) {
                Text(if (todoToEdit == null) stringResource(R.string.add_item) else stringResource(R.string.edit_item),
                    style = MaterialTheme.typography.titleMedium)

                OutlinedTextField(modifier = Modifier.fillMaxWidth(),
                    label = { Text(stringResource(R.string.item_name)) },
                    value = "$todoTitle",
                    onValueChange = { todoTitle = it },
                    isError = todoTitleError)
                if(todoTitleError){
                    Text(text = stringResource(R.string.title_cannot_be_empty),
                        color = Color.Red,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(start = 16.dp))
                }
                OutlinedTextField(modifier = Modifier.fillMaxWidth(),
                    label = { Text(stringResource(R.string.item_description)) },
                    value = "$todoDesc",
                    onValueChange = { todoDesc = it },
                    isError = todoDescError)
                if(todoDescError){
                    Text(text = stringResource(R.string.description_cannot_be_empty),
                        color = Color.Red,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(start = 16.dp))
                }
                Row(modifier = Modifier.fillMaxWidth()) {
                    OutlinedTextField(
                        modifier = Modifier.weight(1f),
                        label = { Text(stringResource(R.string.price)) },
                        value = price.toString(),
                        onValueChange = { price = it.toIntOrNull() ?: 0 },
                        isError = todoPriceError
                    )
                    SpinnerSample(
                        modifier = Modifier
                            .weight(1f)
                            .padding(start = 8.dp),
                        list = currencyList,
                        preselected = selectedCurrency,
                        onSelectionChanged = { selectedCurrency = it },
                        //modifier = Modifier.padding(start = 8.dp)
                    )
                }
                if(todoPriceError){
                    Text(text = stringResource(R.string.enter_a_valid_price_for_the_item),
                        color = Color.Red,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(start = 16.dp))
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    /*
                    Checkbox(checked = important,
                        onCheckedChange = {important = it})
                    Text("Important")
                     */
                    SpinnerSample(
                        list= TodoType.entries.map { it.typeName },
                        preselected = nameOfCategory.typeName,
                        onSelectionChanged = {
                            nameOfCategory = TodoType.valueOf(it.uppercase())
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 10.dp)
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = {
                        val isTitleValid = todoTitle.isNotEmpty()
                        val isDescValid = todoDesc.isNotEmpty()

                        if (isTitleValid && isDescValid) {
                            if (todoToEdit == null) {
                                viewModel.addTodoList(
                                    TodoItem(
                                        title = todoTitle,
                                        description = todoDesc,
                                        createDate = Date(System.currentTimeMillis()).toString(),
                                        priority = if (important) TodoPriority.HIGH else TodoPriority.NORMAL,
                                        isDone = false,
                                        category = nameOfCategory,
                                        price = price.toInt(),
                                        currency = selectedCurrency
                                    )
                                )
                            } else {
                                val editedTodo = todoToEdit.copy(
                                    title = todoTitle,
                                    description = todoDesc,
                                    priority = if (important) TodoPriority.HIGH else TodoPriority.NORMAL,
                                    category = nameOfCategory,
                                    price = price.toInt(),
                                    currency = selectedCurrency
                                )
                                viewModel.editTodoItem(editedTodo)
                            }

                            onCancel()
                        } else {
                            todoTitleError = !isTitleValid
                            todoDescError = !isDescValid
                            todoPriceError = !isTitleValid
                        }
                    }) {
                        Text(stringResource(R.string.save))
                    }
                }
            }
        }
    }
}


@Composable
fun TodoCard(todoItem: TodoItem,
             onTodoDelete: (TodoItem) -> Unit,
             onTodoChecked: (TodoItem, checked: Boolean) -> Unit,
             onTodoEdit: (TodoItem) -> Unit
) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
        ), shape = RoundedCornerShape(20.dp), elevation = CardDefaults.cardElevation(
            defaultElevation = 10.dp
        ), modifier = Modifier
            .padding(5.dp)
            .fillMaxWidth()
    ) {
        var expanded by remember { mutableStateOf(false) }
        var todoChecked by remember { mutableStateOf(todoItem.isDone) }
        val iconResource = todoItem.category.icon


        Column(
            modifier = Modifier
                .padding(20.dp)
                .animateContentSize()
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                if(iconResource!=0) {
                    Image(
                        painter = painterResource(id = iconResource),
                        contentDescription = todoItem.category.typeName,
                        modifier = Modifier
                            .size(40.dp)
                            .padding(end = 10.dp)
                    )
                }
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = todoItem.title, textDecoration = if (todoItem.isDone) {
                            TextDecoration.LineThrough
                        } else {
                            TextDecoration.None
                        }
                    )
                    Text(
                        text = todoItem.price.toString()+todoItem.currency, textDecoration = if (todoItem.isDone) {
                            TextDecoration.LineThrough
                        } else {
                            TextDecoration.None
                        }
                    )
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
                        checked = todoChecked,
                        onCheckedChange = {
                            todoChecked = it
                            onTodoChecked(todoItem, todoChecked)
                        },
                    )
                    Icon(
                        imageVector = Icons.Filled.Delete,
                        contentDescription = stringResource(R.string.delete),
                        modifier = Modifier.clickable {
                            onTodoDelete(todoItem)
                        },
                        tint = Color.Red
                    )
                    Icon( //adding the icon for the edit button
                        imageVector = Icons.Filled.Edit,
                        contentDescription = stringResource(R.string.delete),
                        modifier = Modifier.clickable {
                            onTodoEdit(todoItem)
                        },
                        tint = Color.Gray
                    )
                    IconButton(onClick = { expanded = !expanded }) {
                        Icon(
                            imageVector = if (expanded) Icons.Filled.KeyboardArrowUp
                            else Icons.Filled.KeyboardArrowDown,
                            contentDescription = if (expanded) {
                                stringResource(R.string.less)
                            } else {
                                stringResource(R.string.more)
                            }
                        )
                    }
                }
            }

            if (expanded) {
                Text(
                    text = todoItem.description,
                    style = TextStyle(
                        fontSize = 12.sp,
                    )
                )
                Text(
                    text = todoItem.createDate,
                    style = TextStyle(
                        fontSize = 12.sp,
                    )
                )
            }

        }
    }
}

@Composable
fun SpinnerSample(
    list: List<String>,
    preselected: String,
    onSelectionChanged: (myData: String) -> Unit,
    modifier: Modifier = Modifier
) {
    var selected by remember { mutableStateOf(preselected) }
    var expanded by remember { mutableStateOf(false) }
    OutlinedCard(
        modifier = modifier.clickable {
            expanded = !expanded
        }
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Top,
        ) {
            Text(
                text = selected,
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            )
            Icon(Icons.Outlined.ArrowDropDown, null, modifier =
            Modifier.padding(8.dp))
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier.fillMaxWidth()
            ) {
                list.forEach { listEntry ->
                    DropdownMenuItem(
                        onClick = {
                            selected = listEntry
                            expanded = false
                            onSelectionChanged(selected)
                        },
                        text = {
                            Text(
                                text = listEntry,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .align(Alignment.Start)
                            )
                        },
                    )
                }
            }
        }
    }
}