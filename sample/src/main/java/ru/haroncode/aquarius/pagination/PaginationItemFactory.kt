package ru.haroncode.aquarius.pagination

import ru.haroncode.aquarius.pagination.PaginationItem.SimpleItemWithImage

object PaginationItemFactory {

    fun startItems(page: Int = 0): List<PaginationItem> {
        return listOf(
            SimpleItemWithImage(
                "https://i.pinimg.com/originals/f4/d2/96/f4d2961b652880be432fb9580891ed62.png",
                "${page * 10 + 1} test",
                "test"
            ),
            SimpleItemWithImage(
                "https://i.ytimg.com/vi/lkQ0LDx9jHs/maxresdefault.jpg",
                "${page * 10 + 2} test",
                "test"
            ),
            SimpleItemWithImage(
                "https://rozetked.me/images/uploads/dwoilp3BVjlE.jpg",
                "${page * 10 + 3} test",
                "test"
            ),
            SimpleItemWithImage(
                "https://s0.rbk.ru/v6_top_pics/media/img/7/65/755540270893657.jpg",
                "${page * 10 + 4} test",
                "test"
            ),
            SimpleItemWithImage(
                "https://i.pinimg.com/originals/f4/d2/96/f4d2961b652880be432fb9580891ed62.png",
                "${page * 10 + 5} test",
                "test"
            ),
            SimpleItemWithImage(
                "https://i.ytimg.com/vi/lkQ0LDx9jHs/maxresdefault.jpg",
                "${page * 10 + 6} test",
                "test"
            ),
            SimpleItemWithImage(
                "https://rozetked.me/images/uploads/dwoilp3BVjlE.jpg",
                "${page * 10 + 7} test",
                "test"
            ),
            SimpleItemWithImage(
                "https://s0.rbk.ru/v6_top_pics/media/img/7/65/755540270893657.jpg",
                "${page * 10 + 8} test",
                "test"
            ),
            PaginationItem.LoadingItem
        )
    }
}