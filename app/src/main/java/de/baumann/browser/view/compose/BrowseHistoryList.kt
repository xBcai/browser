package de.baumann.browser.view.compose

import android.graphics.Bitmap
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import de.baumann.browser.database.Record
import androidx.compose.material.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.accompanist.appcompattheme.AppCompatTheme
import de.baumann.browser.Ninja.R
import de.baumann.browser.database.BookmarkManager
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun BrowseHistoryList(
    records: List<Record>,
    shouldReverse: Boolean,
    isWideLayout: Boolean,
    bookmarkManager: BookmarkManager? = null,
    onClick: (Int)->Unit, onLongClick: (Int)->Unit
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(if (isWideLayout) 2 else 1),
        reverseLayout = shouldReverse
    ){
        itemsIndexed(records) { index, record ->
            RecordItem(
                record = record,
                bitmap =  bookmarkManager?.findFaviconBy(record.url)?.getBitmap(),
                modifier = Modifier.combinedClickable (
                    onClick = { onClick(index) },
                    onLongClick = { onLongClick(index) }
                )
            )
        }
    }
}

@Composable
fun RecordItem(
    modifier: Modifier,
    bitmap: Bitmap? = null,
    record: Record
) {
    val timeString = SimpleDateFormat("MMM dd", Locale.getDefault()).format(record.time)
    Row(
        modifier = modifier
            .height(54.dp)
            .padding(5.dp),
        horizontalArrangement = Arrangement.Center
    ) {
        if (bitmap!= null) {
            Image(
                modifier = Modifier.align(Alignment.CenterVertically).size(30.dp).padding(end = 5.dp),
                bitmap = bitmap.asImageBitmap(),
                contentDescription = null,
            )
        } else {
            Icon(
                modifier = Modifier.align(Alignment.CenterVertically).size(30.dp).padding(end = 5.dp),
                painter = painterResource(id = R.drawable.ic_history),
                contentDescription = null,
                tint = MaterialTheme.colors.onBackground
            )
        }
        Text(
            modifier = Modifier
                .weight(1F)
                .align(Alignment.CenterVertically),
            text = record.title ?: "Unknown",
            fontSize = 18.sp,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
        Text(
            modifier = Modifier
                .wrapContentSize()
                .align(Alignment.CenterVertically),
            text = timeString
        )
    }
}

@Preview
@Composable
private fun previewItem() {
    AppCompatTheme {
        RecordItem(
            modifier = Modifier,
            record = Record(title = "Hello", url = "123", time = System.currentTimeMillis())
        )
    }
}


@Preview
@Composable
private fun previewHistoryList() {
    val list = listOf(
        Record(title = "Hello aaa aaa aaa aa aa aaa aa a aa a a a aa a a a a a a a a a aa a a ", url = "123", time = System.currentTimeMillis()),
        Record(title = "Hello 2", url = "123", time = System.currentTimeMillis()),
        Record(title = "Hello 3", url = "123", time = System.currentTimeMillis()),
    )
    AppCompatTheme {
        BrowseHistoryList(records = list, shouldReverse = true, isWideLayout = true, onClick = {}, onLongClick = {})
    }
}