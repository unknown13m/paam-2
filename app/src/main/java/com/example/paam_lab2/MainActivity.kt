package com.example.paam_lab2

import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

class MainActivity : AppCompatActivity() {

    private val chiuitText = mutableStateOf("")

    private val resultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                extractText(result.data)
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        chiuitText.value = getText(R.string.chiuit_example).toString()
        setContent { HomeScreen() }
    }

    @Composable
    private fun HomeScreen() {
        val text by remember { chiuitText }

        Surface(color = Color.White) {
            Box(modifier = Modifier.fillMaxSize()) {

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                        .padding(vertical = 100.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            modifier = Modifier
                                .weight(0.8f)
                                .padding(8.dp),
                            text = text
                        )
                        Button(
                            modifier = Modifier
                                .weight(0.2f)
                                .padding(8.dp),
                            onClick = { shareChiuit(text) }
                        ) {
                            Icon(
                                Icons.Filled.Send,
                                stringResource(R.string.send_action_icon_content_description)
                            )
                        }
                    }
                }

                FloatingActionButton(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(16.dp),
                    onClick = { composeChiuit(text) }
                ) {
                    Icon(
                        Icons.Filled.Edit,
                        stringResource(R.string.edit_action_icon_content_description)
                    )
                }
            }
        }
    }

    private fun shareChiuit(text: String) {
        val sendIntent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, text)
            type = "text/plain"
        }
        startActivity(Intent.createChooser(sendIntent, ""))
    }

    private fun composeChiuit(text: String) {
        val intent = Intent(this, ComposeActivity::class.java).apply {
            putExtra(ComposeActivity.EXTRA_TEXT, text)
        }
        resultLauncher.launch(intent)
    }

    private fun extractText(data: Intent?) {
        data?.getStringExtra(ComposeActivity.EXTRA_TEXT)?.let {
            if (it.isNotBlank()) {
                chiuitText.value = it
            }
        }
    }

    @Preview(showBackground = true)
    @Composable
    private fun DefaultPreview() {
        HomeScreen()
    }
}
