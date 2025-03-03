package com.example.demo
import android.app.Activity
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.selection.selectable
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Slider
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.demo.ui.theme.DemoTheme
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material3.Button
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.demo.models.ClosedBody
import com.example.demo.models.OpenBody
import com.example.demo.models.StackItem

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DemoTheme {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(24.dp)
                ) {
                    Greetings()

                }
            }
        }
    }
}

@Composable
private fun Greetings(
    viewModel: StackViewModel = viewModel(),
    modifier: Modifier = Modifier,
) {
    val items = viewModel.displayedItems.value
    val activity = LocalContext.current as? Activity

    //    Handle back button press
    BackHandler {
        if (items.size>1) {
            viewModel.expandAndRemoveAfter(items.size-2)
        }
        else {
            activity?.finish()
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        // Show progress indicator when loading is true
        val loading = viewModel.loading
        if (loading.value == true) {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center)
            )
        } else {
            Column(modifier = modifier) {
                LazyColumn(modifier = Modifier
                    .weight(1f)
                    .padding(vertical = 4.dp)) {
                    items(items.size) { index ->
                        val item = items[index]
                        val isLastCard = index == items.size - 1
                        val isExpanded = isLastCard

                        Greeting(
                            item = item,
                            expanded = isExpanded,
                            lastCard = index == items.size - 1,
                            index = index,
                            viewModel = viewModel,
                        ) {
                            if (!isLastCard) {
                                viewModel.expandAndRemoveAfter(index) // Expand the clicked card and remove those after it
                            }
                        }
                    }
                }

                // Button to add the next card in the list
                Button(
                    onClick = { viewModel.addNextItem() },
                    enabled = items.size < 3 ,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                ) {
                    Text(text = items.lastOrNull()?.cta_text ?: "Add Next Card")
                }
            }
        }
    }
}

@Composable
fun Greeting(
    item: StackItem,
    expanded: Boolean,
    lastCard: Boolean,
    index: Int,
    viewModel: StackViewModel,
    onExpandChange: (Boolean) -> Unit
) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primary,
        ),
        modifier = Modifier
            .padding(vertical = 4.dp, horizontal = 8.dp)
            .fillMaxWidth()
            .animateContentSize(
                animationSpec = tween(
                    durationMillis = 500,
                    easing = LinearOutSlowInEasing
                )
            )
            .clickable { onExpandChange(!expanded) }
    ) {
        if (expanded) {
            ExpandedCardContent(index, item.open_state.body, viewModel, onOptionSelected = { itemIndex, option ->
                viewModel.selectOption(itemIndex, option)
            })
        } else {
            CollapsedCardContent(index, item.open_state.body, item.closed_state.body, lastCard, viewModel){
                onExpandChange(!expanded)
            }
        }
    }

}

@Composable
private fun ExpandedCardContent(index: Int, body: OpenBody,
                                viewModel: StackViewModel,
                                onOptionSelected: (Int, String) -> Unit ) {
    Column(modifier = Modifier.padding(12.dp)) {
        Text(text = body.title, style = MaterialTheme.typography.headlineSmall)
        Text(text = body.subtitle)
        body.card?.let {
            Text(text = it.header)
            Text(text = it.description)
            DialInput(
                initialValue = viewModel.getInputValue()?: 150000,
                onValueChange = { newValue ->
                    viewModel.updateInputValue(newValue)  // Update the input value when it changes
                },
                min = it.min_range,
                max = it.max_range
            )
        }
        body.items?.forEach { planItem ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .selectable(
                        selected = (planItem.title == viewModel.selectedOptions.value[index]),
                        onClick = { onOptionSelected(index, planItem.title) }
                    )
                    .padding(8.dp)
            ) {

                RadioButton(
                    selected = (planItem.title == viewModel.selectedOptions.value[index]),
                    onClick = { onOptionSelected(index, planItem.title) },
                    colors = RadioButtonDefaults.colors(selectedColor = Color.Red, unselectedColor = Color.Gray, disabledSelectedColor = Color.LightGray)
                )
                Column(
                    modifier = Modifier
                        .padding(start = 8.dp)
                        .weight(1f)
                ) {
                    Text(
                        text = planItem.title,
                        style = MaterialTheme.typography.bodyLarge,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = planItem.subtitle,
                        style = MaterialTheme.typography.bodySmall,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        color = Color.Gray
                    )
                }
            }

        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = body.footer)

    }

}

@Composable
private fun CollapsedCardContent(
    index: Int,
    openBody: OpenBody,
    body: ClosedBody,
    lastCard: Boolean,
    viewModel: StackViewModel,
    onExpandChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .padding(12.dp)
            .animateContentSize()
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            body.key1?.let { key1 ->
                Row {
                    Text(text = "$key1:")
                    val selectedPlanItem = openBody.items?.find { it.title == viewModel.selectedOptions.value[index] }
                    var value1 = selectedPlanItem?.toMap()?.get(key1)
                    if(index == 0){
                        value1 = viewModel.getInputValue()
                    }
                    value1?.let { Text(text = " $it") }
                }
            }

            body.key2?.let { key2 ->
                val selectedPlanItem = openBody.items?.find { it.title == viewModel.selectedOptions.value[index] }
                val value2 = selectedPlanItem?.toMap()?.get(key2)

                if (value2 != null) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Row {
                        Text(text = "$key2:")
                        Text(text = " $value2")
                    }
                }
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        if (!lastCard) {
            IconButton(onClick = { onExpandChange(false) }) {
                Icon(
                    imageVector = Icons.Filled.ExpandMore,
                    contentDescription = "show more"
                )
            }
        }
    }
}

@Composable
fun DialInput(
    modifier: Modifier = Modifier,
    initialValue: Int,
    min: Int,
    max: Int,
    onValueChange: (Int) -> Unit
) {
    // Calculating the normalized slider position based on the initial value
    var sliderPosition = remember { mutableFloatStateOf((initialValue - min) / (max - min).toFloat()) }

    // Calculating the current value based on slider position
    val currentValue = (sliderPosition.floatValue * (max - min) + min).toInt()

    Column(
        modifier = modifier.padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "$currentValue",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        Slider(
            value = sliderPosition.floatValue,
            onValueChange = { position ->
                    sliderPosition.floatValue = position
                    onValueChange(currentValue)
            },
            valueRange = 0f..1f,
            steps = 2000,
            modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp)
        )
    }
}




