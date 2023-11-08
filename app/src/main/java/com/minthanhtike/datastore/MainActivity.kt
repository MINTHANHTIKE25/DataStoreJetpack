package com.minthanhtike.datastore

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.datastore.preferences.core.emptyPreferences
import com.minthanhtike.datastore.ui.theme.DataStoreTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.launch
import java.util.concurrent.Flow

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DataStoreTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Greeting()
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Greeting(modifier: Modifier = Modifier) {
    
    val dataStoring = DataStoring(context = LocalContext.current)
    
    var email by remember { mutableStateOf("") }
    var phnumber by remember { mutableStateOf(0) }
    var isDark by remember { mutableStateOf(false) }
    var name by remember { mutableStateOf("") }

    var nameDS by remember {
        mutableStateOf("")
    }
    var emailDS by remember {
        mutableStateOf("")
    }
    LaunchedEffect(key1 = Unit){
        val userData = dataStoring.getFromDataStore().collect{
            preference-> nameDS = preference.name
            emailDS = preference.emailAddress
        }
    }
    Column(modifier = modifier.fillMaxSize()) {
        OutlinedTextField(
            value = email,
            onValueChange = { email = it })

        OutlinedTextField(
            value = phnumber.toString(),
            onValueChange = {
                phnumber = it.toInt()
            })
        Button(onClick = {
            CoroutineScope(Dispatchers.IO).launch {
                dataStoring.saveToDataStore(
                    userData = UserData(
                        emailAddress = email,
                        mobileNumber = phnumber,
                        isDark = false,
                        name = "Min Than Htike"
                    )
                )
            }
        }) {
            Text(text = "Saving")
        }

        Spacer(modifier = modifier.height(40.dp))
        Text(text = emailDS)
        Text(text = nameDS)

    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    DataStoreTheme {
        Greeting()
    }
}