package com.jay30.retrofit2demo

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jay30.retrofit2demo.viewmodel.DataModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen() {
    val context = LocalContext.current

    val userName = remember { mutableStateOf(TextFieldValue()) }
    val job = remember { mutableStateOf(TextFieldValue()) }
    val response = remember { mutableStateOf("") }

    val topAppBarColors = TopAppBarColors(
        containerColor = Color.Green,
        scrolledContainerColor = Color.Gray,
        navigationIconContentColor = Color.Green,
        titleContentColor = Color.Green,
        actionIconContentColor = Color.Green,
    )

    Surface(
        modifier = Modifier
            .fillMaxSize(),
        color = MaterialTheme.colorScheme.primary,
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    colors = topAppBarColors,
                    title = {
                        Text(
                            text = "Retrofit POST Request in Android",
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Center,
                            color = Color.White,
                        )
                    }
                )
            },
            content = { innerPadding ->
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize() ,
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Retrofit POST Request in Android",
                            color = Color.Green,
                            fontSize = 20.sp,
                            fontFamily = FontFamily.Default,
                            fontWeight = FontWeight.Bold, textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(5.dp))
                        TextField(
                            value = userName.value,
                            onValueChange = { userName.value = it },
                            placeholder = { Text(text = "Enter your name") },
                            modifier = Modifier
                                .padding(16.dp)
                                .fillMaxWidth(),
                            textStyle = TextStyle(color = Color.Black, fontSize = 15.sp),
                            singleLine = true,
                        )
                        Spacer(modifier = Modifier.height(5.dp))
                        TextField(
                            value = job.value,
                            onValueChange = { job.value = it },
                            placeholder = { Text(text = "Enter your job") },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            textStyle = TextStyle(color = Color.Black, fontSize = 15.sp),
                            singleLine = true,
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        Button(
                            onClick = {
                                postDataUsingRetrofit(
                                    context, userName, job, response
                                )
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                        ) {
                            Text(text = "Post Data", modifier = Modifier.padding(8.dp))
                        }
                        Spacer(modifier = Modifier.height(20.dp))
                        Text(
                            text = response.value,
                            color = Color.Black,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold, modifier = Modifier
                                .padding(10.dp)
                                .fillMaxWidth(),
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        )
    }
}

fun postDataUsingRetrofit(
    context: Context,
    userName: MutableState<TextFieldValue>,
    job: MutableState<TextFieldValue>,
    result: MutableState<String>
) {
    val url = "https://reqres.in/api/"
    val retrofit = Retrofit.Builder()
        .baseUrl(url)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    val retrofitAPI = retrofit.create(RetrofitAPI::class.java)
    val dataModel = DataModel(userName.value.text, job.value.text)
    val call: Call<DataModel?>? = retrofitAPI.postData(dataModel)
    call!!.enqueue(object : Callback<DataModel?> {
        override fun onResponse(call: Call<DataModel?>?, response: Response<DataModel?>) {
            Toast.makeText(context, "Data posted to API", Toast.LENGTH_SHORT).show()
            val model: DataModel? = response.body()
            val response =
                "Response Code:${response.code()}\nUser Name:${model!!.name}\nJob:${model!!.job}\n"
            result.value = response
        }

        override fun onFailure(call: Call<DataModel?>?, t: Throwable) {
            result.value = "Error found is : " + t.message
        }
    })

}