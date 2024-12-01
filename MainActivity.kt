/*
/* Pseudocode for Contact Book App */

/* 1. Initialize Main Activity */
START MainActivity
SET app theme and layout
NAVIGATE to ContactBookApp() composable

/* 2. Define Main Composable: ContactBookApp */
DEFINE ContactBookApp:
INITIALIZE list of contacts (mutable state list)
SET current screen (Home or AddContact) to "Home"
IF current screen is "Home":
DISPLAY HomeScreen composable with contacts and callbacks for adding and deleting contacts
ELSE IF current screen is "AddContact":
DISPLAY AddContactScreen composable with callbacks for saving or canceling

/* 3. Define Home Screen */
DEFINE HomeScreen(contacts, onAddContactClick, onDeleteContact):
DISPLAY TopAppBar with title "Contact Book"
ADD Floating Action Button for "Add Contact" action
DISPLAY list of contacts using LazyColumn:
FOR each contact in contacts:
DISPLAY ContactItem composable with contact details and delete button

/* 4. Define Add Contact Screen */
DEFINE AddContactScreen(onAddContact, onNavigateBack):
INITIALIZE name and phone input fields
DISPLAY input fields for "Name" and "Phone Number"
DISPLAY "Save" button:
ON click:
VALIDATE inputs are not blank
CALL onAddContact with name and phone
NAVIGATE back to HomeScreen

/* 5. Define ContactItem Composable */
DEFINE ContactItem(contact, onDeleteContact):
DISPLAY name and phone of contact
ADD delete button:
ON click:
CALL onDeleteContact with the contact

/* 6. Handle Data Persistence */
DEFINE Data Persistence Using SharedPreferences:
SAVE contacts:
CONVERT contact list to JSON string using Gson
STORE JSON string in SharedPreferences
LOAD contacts:
RETRIEVE JSON string from SharedPreferences
CONVERT JSON string back to contact list using Gson

/* 7. Integrate Persistence in ContactBookApp */
ON app start:
LOAD contacts from SharedPreferences
ON app close or update:
SAVE contacts to SharedPreferences
END

 */
package com.example.contactbook2

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.example.contactbook2.ui.theme.ContactBook2Theme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

data class Contact(val name: String, val phone: String)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ContactBook2Theme {
                ContactBookApp(applicationContext)
            }
        }
    }
}

@Composable
fun ContactBookApp(context: Context) {
    val sharedPreferences = context.getSharedPreferences("ContactBook", Context.MODE_PRIVATE)
    val contacts = remember { mutableStateListOf<Contact>() }

    // Load saved contacts on app start
    LaunchedEffect(Unit) {
        val savedContactsJson = sharedPreferences.getString("contacts", "[]")
        val savedContacts: List<Contact> = Gson().fromJson(savedContactsJson, object : TypeToken<List<Contact>>() {}.type)
        contacts.addAll(savedContacts)
    }

    // Save contacts whenever they are updated
    fun saveContacts() {
        val editor = sharedPreferences.edit()
        val contactsJson = Gson().toJson(contacts)
        editor.putString("contacts", contactsJson)
        editor.apply()
    }

    var currentScreen by remember { mutableStateOf("Home") }

    when (currentScreen) {
        "Home" -> HomeScreen(
            contacts = contacts,
            onAddContactClick = { currentScreen = "AddContact" },
            onDeleteContact = { contact ->
                contacts.remove(contact)
                saveContacts()
            }
        )
        "AddContact" -> AddContactScreen(
            onAddContact = { name, phone ->
                contacts.add(Contact(name, phone))
                saveContacts()
                currentScreen = "Home"
            },
            onNavigateBack = { currentScreen = "Home" }
        )
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    contacts: List<Contact>,
    onAddContactClick: () -> Unit,
    onDeleteContact: (Contact) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Contact Book") }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onAddContactClick) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Add Contact")
            }
        }
    ) { padding ->
        LazyColumn(modifier = Modifier.padding(padding)) {
            items(contacts) { contact ->
                ContactItem(contact = contact, onDeleteContact = onDeleteContact)
            }
        }
    }
}

@Composable
fun ContactItem(contact: Contact, onDeleteContact: (Contact) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            Text(text = "Name: ${contact.name}")
            Text(text = "Phone: ${contact.phone}")
        }
        IconButton(onClick = { onDeleteContact(contact) }) {
            Icon(
                imageVector = Icons.Default.Delete,
                contentDescription = "Delete Contact"
            )
        }
    }
}

@Composable
fun AddContactScreen(
    onAddContact: (String, String) -> Unit,
    onNavigateBack: () -> Unit
) {
    var name by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Add New Contact",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Name") },
            placeholder = { Text("Enter contact name") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        )

        OutlinedTextField(
            value = phone,
            onValueChange = { phone = it },
            label = { Text("Phone Number") },
            placeholder = { Text("Enter phone number") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        )

        Button(
            onClick = {
                if (name.isNotBlank() && phone.isNotBlank()) {
                    onAddContact(name, phone)
                }
            },
            modifier = Modifier.align(Alignment.End)
        ) {
            Text("Save Contact")
        }
    }
}