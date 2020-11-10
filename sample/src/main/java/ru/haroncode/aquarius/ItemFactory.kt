package ru.haroncode.aquarius

import ru.haroncode.aquarius.renderers.CarouselRenderer.ImageItem

object ItemFactory {

    fun staticItems(): List<Item> {
        val resultList = mutableListOf<Item>()
        resultList += Item.Header
        resultList += Item.Button("Update")
        resultList += listOf(
            Item.SimpleTextItem("title1", "subtitle1"),
            Item.SimpleTextItem("title2", "subtitle2"),
            Item.SimpleTextItem("title3", "subtitle3"),
        )

        resultList += Item.SimpleTextItem("Carousel")
        resultList += carousel()
        resultList += Item.Button("LoadMore", R.id.button_load_more)
        return resultList
    }


    fun loadMore(): List<Item> {
        val resultList = mutableListOf<Item>()
        resultList += listOf(
            Item.SimpleTextItem("title1", "subtitle1"),
            Item.SimpleTextItem("title2", "subtitle2"),
            Item.SimpleTextItem("title3", "subtitle3"),
            Item.SimpleTextItem("title3", "subtitle3"),
            Item.SimpleTextItem("title3", "subtitle3"),

        )

        resultList += Item.SimpleTextItem("Carousel")
        resultList += carousel()
        resultList += carousel()
        resultList += carousel()
        resultList += carousel()
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