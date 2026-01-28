package com.santhosh.sharedpreferencesexample

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.SearchView
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ModifierLocalBeyondBoundsLayout
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.edit
import com.santhosh.sharedpreferencesexample.ui.theme.SharedPreferencesExampleTheme
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch

const val PREFERENCES_KEY = "com.santhosh.sharedpreferencesexample.PREFERENCES"
class MainActivity : ComponentActivity() {
    val snackbarHostState = SnackbarHostState()

    private val sharedPreferencesListener = SharedPreferences.OnSharedPreferenceChangeListener { _, key ->
        if (key == "recently_searched_items"){
            val sharedPreferences = getSharedPreferences(PREFERENCES_KEY, MODE_PRIVATE)

            lifecycleScope.launch {
                snackbarHostState.showSnackbar(
                    "${sharedPreferences.getStringSet(key, setOf())?.size} searches"
                )
            }
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val sharedPreferences = getSharedPreferences(PREFERENCES_KEY, MODE_PRIVATE)

        sharedPreferences.registerOnSharedPreferenceChangeListener(sharedPreferencesListener)

        setContent {
            SharedPreferencesExampleTheme {
                PreferencesApp(snackbarHostState)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedBoxWithConstraintsScope")
@Composable
fun PreferencesApp(snackbarHostState: SnackbarHostState) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Shared Preferences") }
                )
            },
            snackbarHost = {
                SnackbarHost(snackbarHostState)
            }
        ) { contentPadding ->

            Surface(
                modifier = Modifier.fillMaxSize().padding(contentPadding),
                color = MaterialTheme.colorScheme.background
            ) {
                searchView()
            }

        }
    }
}

@Composable
fun searchView(){
    val sharedPreferences = LocalContext.current.getSharedPreferences(PREFERENCES_KEY,Context.MODE_PRIVATE)

    val searchQuery = remember { mutableStateOf("") }

    val recentlySearchedItems = remember {
        mutableStateListOf<String>()
    }

    val searchedItems = sharedPreferences.getStringSet(
        "recently_searched_items",setOf()
    )

    searchedItems?.toList().let {
        recentlySearchedItems.clear()
        recentlySearchedItems.addAll(it?.sorted() ?: emptyList() )
    }

    Column(
        modifier = Modifier.padding(16.dp)
    ) {

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            OutlinedTextField(
                value = searchQuery.value,
                onValueChange = { searchQuery.value = it },
                trailingIcon = {
                    IconButton(
                        onClick = {
                            searchQuery.value.let { recentlySearchedItems.add(it)}
                            sharedPreferences.edit{
                                putStringSet(
                                    "recently_searched_items",
                                    recentlySearchedItems.toSet()).apply()
                            }
                            searchQuery.value =""
                        }
                    )
                    {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Search"
                        )
                    }
                }
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Recently Searched Items",
            style = MaterialTheme.typography.titleMedium
        )

        Spacer(modifier = Modifier.height(8.dp))

        for (item in recentlySearchedItems)
        {
            Text(item, modifier = Modifier.padding(2.dp))
        }
    }

}

