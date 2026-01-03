package com.example.movies.ui.screens.home.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.movies.R
import com.example.movies.ui.theme.MoviesTheme
import com.example.movies.ui.widgets.DropdownSelector

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchSettingsScreen(
    onBackPressed: (DiscoverSettings) -> Unit,
    viewmodel: SearchSettingsViewModel = viewModel()
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.search_settings)) },
                colors = TopAppBarDefaults.topAppBarColors().copy(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                ),
                navigationIcon = {
                    IconButton(onClick = {
                        onBackPressed(viewmodel.searchSettingsResult)
                    }) {
                        Icon(
                            painterResource(R.drawable.outline_arrow_back_24),
                            contentDescription = stringResource(R.string.go_back)
                        )
                    }
                }
            )
        }
    ) { padding ->
        Content(
            modifier = Modifier.padding(padding),
            selectedSortByIndex = viewmodel.selectedSortByIndex,
            sortByOptions = viewmodel.sortOptions.map { it.getReadableName(LocalContext.current) },
            onSortByChanged = viewmodel::setSortBy
        )
    }
}

@Composable
private fun Content(
    selectedSortByIndex: Int,
    sortByOptions: List<String>,
    onSortByChanged: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.padding(vertical = 16.dp)) {
        DropdownSelector(
            stringResource(R.string.sort_by),
            selectedSortByIndex,
            sortByOptions,
            onSortByChanged
        )
    }
}


@Preview(showBackground = true)
@Composable
fun ContentPreview() {
    MoviesTheme {
        Content(
            selectedSortByIndex = 0,
            sortByOptions = listOf(
                "Option 1",
                "Option 2",
                "Option 3",
                "Option 4"
            ),
            onSortByChanged = { },
        )
    }
}