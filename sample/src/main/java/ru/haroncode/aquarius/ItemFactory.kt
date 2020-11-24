package ru.haroncode.aquarius

import android.content.Context
import ru.haroncode.aquarius.renderers.CarouselRenderer.ImageItem
import java.util.*

object ItemFactory {

    fun staticItems(context: Context): List<Item> {
        val resultList = mutableListOf<Item>()
        resultList += Item.Title(R.string.lorem_title)
        resultList += Item.Button(context.getString(R.string.update))
        resultList += generateSimpleTextItems(context)
        resultList += generateCardItems(context)
        resultList += Item.Title(R.string.carousel)
        resultList += carousel()

        resultList += Item.Button(context.getString(R.string.load_more), R.id.button_load_more)

        return resultList
    }

    private fun generateSimpleTextItems(context: Context, count: Int = 5): List<Item> {
        val simpleTextItem = Item.SimpleTextItem(
            context.getString(R.string.lorem_title),
            context.getString(R.string.lorem_subtitle)
        )
        return Collections.nCopies(count, simpleTextItem)
    }

    private fun generateCardItems(context: Context, count: Int = 5): List<Item> {
        val result = mutableListOf<Item>()
        for (index in 0 until count) {
            result += Item.CardItem(
                context.getString(R.string.lorem_title),
                context.getString(R.string.lorem_subtitle)
            )
        }
        return result
    }

    fun loadMore(): List<Item> {
        val resultList = mutableListOf<Item>()
        resultList += Item.Title(R.string.carousel)
        resultList += carousel()
        return resultList
    }

    private fun carousel(): Item.CarouselItem {
        return Item.CarouselItem(
            images = listOf(
                ImageItem("https://i.pinimg.com/originals/f4/d2/96/f4d2961b652880be432fb9580891ed62.png"),
                ImageItem("https://i.ytimg.com/vi/lkQ0LDx9jHs/maxresdefault.jpg"),
                ImageItem("https://rozetked.me/images/uploads/dwoilp3BVjlE.jpg"),
                ImageItem("https://s0.rbk.ru/v6_top_pics/media/img/7/65/755540270893657.jpg"),
                ImageItem("https://www.spletnik.ru/img/__post/68/68cd2b706c1fe59dc8df7e58a1655546_300.jpg"),
                ImageItem("https://gorod48.ru/upload/iblock/cbd/cbdddbed4705f37ccb30322e7ffb9da9.JPG"),
            )
        )
    }
}