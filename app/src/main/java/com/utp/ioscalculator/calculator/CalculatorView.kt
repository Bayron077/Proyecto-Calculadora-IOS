package com.utp.ioscalculator.calculator

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController


@Composable
fun ShowCalculator(
    viewModel: CalculatorViewModel = viewModel(),
    navController: NavController
) {

    val result = viewModel.result.value
    val scrollState = rememberScrollState()

    LaunchedEffect(result) {
        scrollState.scrollTo(scrollState.maxValue)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .safeDrawingPadding()
    ) {

        Button(
            onClick = { navController.navigate("history") },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Transparent,
                contentColor = Color(0xFFFFA70E)
            ),
            modifier = Modifier
                .padding(start = 10.dp, top = 10.dp)
                .size(60.dp)
        ) {
            Text(
                text = "≡",
                fontSize = 35.sp
            )
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(10.dp),
            verticalArrangement = Arrangement.Bottom,
        ) {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(end = 5.dp, start = 5.dp),
                text = "",
                fontSize = 40.sp,
                color = Color.Gray,
                textAlign = TextAlign.End,
                softWrap = false,
                maxLines = 1
            )
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(end = 5.dp, start = 5.dp)
                    .horizontalScroll(scrollState),
                text = result,
                fontSize = 75.sp,
                color = Color.White,
                textAlign = TextAlign.End,
                softWrap = false,
                maxLines = 1
            )
            Column(
                Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(7.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(7.dp)
                ) {
                    ShowButton("AC", Color(0xFF5E5D59), viewModel)
                    ShowButton("⁺/₋", Color(0xFF5E5D59), viewModel)
                    ShowButton("%", Color(0xFF5E5D59), viewModel)
                    ShowButton("÷", Color(0xFFFFA70E), viewModel)
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(7.dp)
                ) {
                    ShowButton("7", Color(0xFF2A2928), viewModel)
                    ShowButton("8", Color(0xFF2A2928), viewModel)
                    ShowButton("9", Color(0xFF2A2928), viewModel)
                    ShowButton("x", Color(0xFFFFA70E), viewModel)
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(7.dp)
                ) {
                    ShowButton("4", Color(0xFF2A2928), viewModel)
                    ShowButton("5", Color(0xFF2A2928), viewModel)
                    ShowButton("6", Color(0xFF2A2928), viewModel)
                    ShowButton("-", Color(0xFFFFA70E), viewModel)
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(7.dp)
                ) {
                    ShowButton("1", Color(0xFF2A2928), viewModel)
                    ShowButton("2", Color(0xFF2A2928), viewModel)
                    ShowButton("3", Color(0xFF2A2928), viewModel)
                    ShowButton("+", Color(0xFFFFA70E), viewModel)
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(7.dp)
                ) {
                    ShowButton("", Color(0xFF2A2928), viewModel)
                    ShowButton("0", Color(0xFF2A2928), viewModel)
                    ShowButton(",", Color(0xFF2A2928), viewModel)
                    ShowButton("=", Color(0xFFFFA70E), viewModel)
                }
            }
        }
    }
}

@Composable
fun ShowButton(text: String, color: Color, viewModel: CalculatorViewModel) {
    Button(
        onClick = { viewModel.manageClickButtons(text) },
        shape = CircleShape,
        colors = ButtonDefaults.buttonColors(color),
        modifier = Modifier
            .size(88.dp)
    ) {
        Text(
            text = text,
            fontSize = 28.sp
        )
    }
}

@Preview
@Composable
fun TestView() {
    val navController = rememberNavController()
    ShowCalculator(
        navController = navController
    )
}
