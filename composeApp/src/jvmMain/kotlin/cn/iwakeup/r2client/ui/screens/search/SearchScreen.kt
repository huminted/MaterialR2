package cn.iwakeup.r2client.ui.screens.search

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.clearText
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Link
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.semantics.isTraversalGroup
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.traversalIndex
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cn.iwakeup.r2client.APP_NAME
import cn.iwakeup.r2client.data.BucketBasicInfo
import cn.iwakeup.r2client.resource.Res
import cn.iwakeup.r2client.resource.icon
import cn.iwakeup.r2client.ui.components.EmptyStatus
import cn.iwakeup.r2client.ui.components.InitialLoadingIndicator
import cn.iwakeup.r2client.ui.theme.AppTheme
import org.jetbrains.compose.resources.painterResource
import software.amazon.awssdk.services.s3.model.S3Object


@Composable
fun SearchScreen(defaultSelectedBucket: BucketBasicInfo, buckets: List<BucketBasicInfo>) {

    val viewModel = remember { SearchViewModel(defaultSelectedBucket) }
    val uiState by viewModel.uiSate.collectAsStateWithLifecycle()
    val searchFieldState = rememberTextFieldState()

    SimpleSearchBar(
        modifier = Modifier.fillMaxSize(), searchFieldState,
        searchOption = {
            MinimalDropdownMenu(buckets) {
                viewModel.setSelectedBucket(it)
            }
        },
        onSearch = {
            viewModel.search(searchFieldState.text.toString())
        }, {
            viewModel.resetSearch()
        }, {
            when (uiState) {
                SearchUIState.Loading -> {
                    InitialLoadingIndicator(modifier = Modifier.fillMaxSize(), loadingHintText = "加载中")
                }

                is SearchUIState.Success -> {
                    val successState = uiState as SearchUIState.Success

                    if (successState.list.isNotEmpty()) {
                        SearchResultList(successState.enableCopyLink, successState.list) {
                            viewModel.copyObjectLink(it)
                        }
                    } else {
                        EmptyStatus(modifier = Modifier.fillMaxSize(), hintText = "No Object Found")
                    }

                }

                SearchUIState.Init -> {

                }
            }


        })

}

@Composable
fun SearchResultList(enableCopyLink: Boolean, searchResults: List<S3Object>, copyObjectLink: (String) -> Unit) {

    Column(Modifier.verticalScroll(rememberScrollState())) {
        searchResults.forEach { result ->
            ListItem(
                headlineContent = { Text(result.key()) },
                trailingContent = {
                    IconButton(
                        onClick = { copyObjectLink(result.key()) },
                        enabled = enableCopyLink
                    ) {
                        Icon(
                            imageVector = Icons.Default.Link,
                            contentDescription = "Link"
                        )
                    }


                },
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SimpleSearchBar(
    modifier: Modifier = Modifier,
    textFieldState: TextFieldState,
    searchOption: @Composable () -> Unit,
    onSearch: (String) -> Unit,
    onCloseResultList: () -> Unit,
    showSearchResult: @Composable () -> Unit,
) {
    var expanded by rememberSaveable { mutableStateOf(false) }

    Box(
        modifier.semantics { isTraversalGroup = true }
    ) {

        Image(
            modifier = Modifier.align(Alignment.BottomEnd).padding(80.dp).alpha(0.1f),
            painter = painterResource(Res.drawable.icon),
            contentDescription = ""
        )

        Text(
            modifier = Modifier.align(Alignment.Center).padding(bottom = 120.dp), text = APP_NAME,
            fontSize = 40.sp,
            fontWeight = FontWeight.SemiBold,
            color = AppTheme.colors.secondary
        )
        SearchBar(
            modifier = Modifier
                .align(Alignment.Center)
                .semantics { traversalIndex = 0f },
            inputField = {
                SearchBarDefaults.InputField(
                    query = textFieldState.text.toString(),
                    onQueryChange = {
                        textFieldState.edit { replace(0, length, it) }
                    },
                    onSearch = {
                        onSearch(textFieldState.text.toString())
                    },
                    expanded = expanded,
                    onExpandedChange = { expanded = it },
                    placeholder = { },
                    leadingIcon = {
                        Icon(Icons.Default.Search, "")
                    },
                    trailingIcon = {
                        if (expanded) {
                            IconButton(onClick = {
                                expanded = false
                                textFieldState.clearText()
                                onCloseResultList()
                            }, content = {
                                Icon(Icons.Default.Close, "")
                            })
                        } else {
                            searchOption()

                        }

                    }
                )
            },
            expanded = expanded,
            onExpandedChange = { expanded = it },
        ) {
            // Display search results in a scrollable column

            showSearchResult()

        }
    }
}

@Composable
fun MinimalDropdownMenu(buckets: List<BucketBasicInfo>, onSelect: (BucketBasicInfo) -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    Box(
        modifier = Modifier
    ) {
        IconButton(onClick = { expanded = !expanded }) {
            Icon(Icons.Default.MoreVert, contentDescription = "More options")
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {

            buckets.forEach { bucket ->
                DropdownMenuItem(
                    text = { Text(bucket.bucketName) },
                    onClick = {
                        onSelect(bucket)
                        expanded = false
                    }
                )
            }


        }
    }
}