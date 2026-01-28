📒 SharedPreferences Search History (Jetpack Compose)

This is a simple Android app built with Jetpack Compose that demonstrates how to:

Save data using SharedPreferences

Read data after app restart

Listen for preference changes

Show a Snackbar when data changes

Maintain UI state using Compose

🚀 What does this app do?

User types a search term

Clicks the Search icon

The term is saved in SharedPreferences

Recently searched items are displayed in a list

A Snackbar shows how many searches exist

Data persists even after app restart

🧠 Concepts Used

SharedPreferences

OnSharedPreferenceChangeListener

SnackbarHostState

lifecycleScope.launch

remember, mutableStateOf, mutableStateListOf

LocalContext (for accessing Context in Composables)

🗂️ Preference Key Used
const val PREFERENCES_KEY =
    "com.santhosh.sharedpreferencesexample.PREFERENCES"

🏗️ Architecture Overview
MainActivity
 ├── Registers SharedPreferences listener
 ├── Shows Snackbar when data changes
 └── Hosts Compose UI

PreferencesApp (Composable)
 └── Scaffold + SnackbarHost

searchView (Composable)
 ├── Reads SharedPreferences
 ├── Manages search text (Compose state)
 └── Displays recent searches

✍️ How data is saved

When the search icon is clicked:

sharedPreferences.edit {
    putStringSet(
        "recently_searched_items",
        recentlySearchedItems.toSet()
    )
}


Converts list → Set<String>

Saves it persistently

Automatically triggers the listener

👂 Preference Change Listener
private val sharedPreferencesListener =
    SharedPreferences.OnSharedPreferenceChangeListener { _, key ->
        if (key == "recently_searched_items") {
            lifecycleScope.launch {
                snackbarHostState.showSnackbar(
                    "Searches updated"
                )
            }
        }
    }


✔ Reacts whenever search history changes
✔ Shows Snackbar safely using lifecycleScope

🧩 Why LocalContext is needed?
val sharedPreferences =
    LocalContext.current.getSharedPreferences(...)


Composables do not have direct access to Context

LocalContext provides the current Activity context

🧼 UI State Handling
val searchQuery = remember { mutableStateOf("") }


Compose controls the UI

SharedPreferences only handles persistence

Clean separation of concerns

⚠️ Notes / Limitations

StringSet does not guarantee order

Duplicate searches may be added

Not suitable for large or complex data

For production apps → DataStore recommended

🎯 Learning Outcome

After this project, you’ll understand:

When to use SharedPreferences

How Compose state works

How listeners react to preference updates

How persistence survives app restarts

📌 Best Use Case for SharedPreferences

App settings

Simple user preferences

Small key-value data

❌ Not for large data or structured objects

🧑‍💻 Author

Santhosh Sekar
