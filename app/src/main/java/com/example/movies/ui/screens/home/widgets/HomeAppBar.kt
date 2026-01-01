package com.example.movies.ui.screens.home.widgets

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandIn
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation3.ui.LocalNavAnimatedContentScope
import com.example.movies.R
import com.example.movies.ui.LocalSharedElementScope
import kotlinx.coroutines.delay

@Composable
@OptIn(ExperimentalMaterial3Api::class)
@Suppress("AssignedValueIsNeverRead")
fun HomeAppBar(
    query: State<String>,
    onQueryChange: (String) -> Unit,
    onSearchSettingsClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var showSearchBar by remember { mutableStateOf(false) }

    with(LocalSharedElementScope.current) {
        with(LocalNavAnimatedContentScope.current) {
            TopAppBar(
                title = {
                    if (!showSearchBar) {
                        Text(stringResource(R.string.app_name))
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                ),
                actions = {
                    if (!showSearchBar) {
                        Actions(
                            { showSearchBar = true },
                            onSearchSettingsClick
                        )
                    } else {
                        SearchBar(
                            query,
                            onQueryChange,
                            { showSearchBar = false },
                        )
                    }
                },
                modifier = modifier
                    .renderInSharedTransitionScopeOverlay(1f)
                    .animateEnterExit(
                        enter = fadeIn() + slideInVertically { it },
                        exit = fadeOut() + slideOutVertically { it }
                    )
            )
        }
    }
}

@Composable
private fun Actions(
    onSearchClick: () -> Unit,
    onSearchSettingsClick: () -> Unit
) {
    IconButton(onClick = onSearchClick) {
        Icon(
            painterResource(R.drawable.baseline_search_24),
            contentDescription = stringResource(R.string.search_icon),
        )
    }
    Menu(onSearchSettingsClick)
}

@Composable
private fun Menu(
    onSearchSettingsClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var isExpanded by remember { mutableStateOf(false) }

    Box(modifier = modifier) {
        IconButton(
            onClick = { isExpanded = !isExpanded },
        ) {
            Icon(
                painterResource(R.drawable.baseline_more_vert_24),
                contentDescription = stringResource(R.string.search_menu),
            )
        }
        DropdownMenu(
            expanded = isExpanded,
            onDismissRequest = { isExpanded = false }
        ) {
            DropdownMenuItem(
                text = { Text(stringResource(R.string.search_settings)) },
                onClick = {
                    isExpanded = false
                    onSearchSettingsClick()
                }
            )
        }
    }
}

@Composable
private fun SearchBar(
    query: State<String>,
    onQueryChange: (String) -> Unit,
    onClose: () -> Unit,
    modifier: Modifier = Modifier
) {
    val showCrossIcon by remember { derivedStateOf { query.value.isNotEmpty() } }
    val focusRequester = remember { FocusRequester() }
    LaunchedEffect(Unit) {
        delay(100)
        focusRequester.requestFocus()
    }

    Row(
        modifier = modifier.padding(start = 4.dp, end = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = onClose) {
            Icon(
                painterResource(R.drawable.outline_arrow_back_24),
                contentDescription = stringResource(R.string.close_search),
            )
        }
        TextField(
            value = query.value,
            onValueChange = onQueryChange,
            modifier = Modifier
                .fillMaxWidth()
                .focusRequester(focusRequester),
            colors = TextFieldDefaults.colors().copy(
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent
            ),
            trailingIcon = {
                AnimatedVisibility(
                    showCrossIcon,
                    enter = fadeIn() + expandIn { it },
                    exit = fadeOut() + shrinkOut { it },
                ) {
                    IconButton(onClick = {
                        onQueryChange("")
                        onClose()
                    }) {
                        Icon(
                            painterResource(R.drawable.baseline_close_24),
                            contentDescription = stringResource(R.string.reset_search),
                        )
                    }
                }
            }
        )
    }
}