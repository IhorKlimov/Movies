package com.example.movies.ui.widgets

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import com.example.movies.R
import com.example.movies.ui.theme.MoviesTheme

@Composable
fun DropdownSelector(
    label: String,
    selectedItemIndex: Int,
    options: List<String>,
    onValueChange: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    var isExpanded by remember { mutableStateOf(false) }

    Box(modifier = modifier.fillMaxWidth()) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .clickable { isExpanded = !isExpanded }
                .padding(16.dp)
        ) {
            Column {
                Text(
                    label,
                    style = MaterialTheme.typography.labelMedium.copy(
                        MaterialTheme.colorScheme.onSurfaceVariant
                    ),
                )
                Text(options[selectedItemIndex])
            }
            Icon(
                painterResource(R.drawable.baseline_arrow_drop_down_24),
                contentDescription = stringResource(R.string.dropdown_icon)
            )
        }
        DropdownMenu(
            isExpanded,
            onDismissRequest = {
                isExpanded = false
            },
            offset = DpOffset(16.dp, 0.dp),
            modifier = Modifier.align(Alignment.BottomEnd)
        ) {
            options.forEachIndexed { index, name ->
                DropdownMenuItem(
                    text = { Text(name) },
                    onClick = {
                        isExpanded = false
                        onValueChange(index)
                    }
                )
            }
        }
    }
}

@Suppress("AssignedValueIsNeverRead")
@Preview(showBackground = true)
@Composable
fun DropdownSelectorPreview() {
    var selectedIndex by remember { mutableIntStateOf(0) }

    MoviesTheme {
        DropdownSelector(
            label = stringResource(R.string.sort_by),
            selectedItemIndex = selectedIndex,
            options = listOf(
                "Option 1",
                "Option 2",
                "Option 3",
                "Option 4"
            ),
            onValueChange = { selectedIndex = it }
        )
    }
}