package com.mobile.medialibraryapp.component

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.window.Dialog
import androidx.constraintlayout.compose.ConstraintLayout
import com.mobile.medialibraryapp.R
import com.mobile.medialibraryapp.dataclass.MediaEntity
import com.mobile.medialibraryapp.ui.theme.Dp12
import com.mobile.medialibraryapp.ui.theme.Dp16
import com.mobile.medialibraryapp.ui.theme.Dp24
import com.mobile.medialibraryapp.ui.theme.Dp6
import com.mobile.medialibraryapp.ui.theme.PageDimension
import com.mobile.medialibraryapp.ui.theme.Sp16
import com.mobile.medialibraryapp.util.toFormattedDate
import com.mobile.medialibraryapp.util.toReadableSize

@Composable
fun DialogComponent(showDialog: (Boolean) -> Unit, data: MediaEntity?) {

    val interactionSource = remember { MutableInteractionSource() }

    Dialog(onDismissRequest = { showDialog(false) }) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(Dp24),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.background )
        ) {
            ConstraintLayout(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(PageDimension),
            ) {
                val (close, content) = createRefs()

                IconButton(onClick = { showDialog(false) },
                    modifier = Modifier.constrainAs(close) {
                        top.linkTo(parent.top)
                        end.linkTo(parent.end)
                    }) {
                    Icon(
                        painterResource(id = R.drawable.ic_close), contentDescription = null
                    )
                }
                Column(
                    modifier = Modifier
                        .padding(Dp16)
                        .fillMaxWidth()
                        .verticalScroll(rememberScrollState())
                        .constrainAs(content) {
                            top.linkTo(parent.top)
                            bottom.linkTo(parent.bottom)
                        },
                ) {

                    DataComponent("File Name:",data?.name ?: "")
                    DataComponent("Media Type:",data?.mediaType ?: "")
                    DataComponent("File size:", data?.size?.toReadableSize() ?:"" )
                    DataComponent("Created at:",data?.uploadDate?.toFormattedDate() ?: "")

                }
            }
        }
    }
}

@Composable
private fun DataComponent(title : String, detail: String) {
    Column {
        Text(
            text = title,
            modifier = Modifier.padding(bottom = Dp6),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.displayLarge.copy(
                fontSize = Sp16
            )
        )
        Text(
            text =detail,
            modifier = Modifier.padding(bottom = Dp12),
            style = MaterialTheme.typography.bodyMedium.copy()
        )
    }
}