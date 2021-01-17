[![jCenter](https://api.bintray.com/packages/haroncode/maven/aquarius/images/download.svg)](https://bintray.com/haroncode/maven/gemini-core/_latestVersion)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)
![Build](https://github.com/HaronCode/Aquarius/workflows/Build/badge.svg)

# Aquarius
Simple wrapper for recycler view to easy work with difficult lists.

## Installing
Available through bintray.com.

Add the maven repo to your root `build.gradle`

```groovy
allprojects {
    repositories {
        maven { url 'https://dl.bintray.com/haroncode/maven' }
    }
}
```

```groovy
implementation("com.haroncode.gemini:aquarius:${latest-version}")
```
## Benefits
- [x] Drag and Drop
- [x] DiffUtil 
- [x] Async adapter 
- [x] Dsl for ItemDecoration


## Getting started 

Create Item class like this:

```kotlin
sealed class Item : ComparableItem
```

Then create simple renderer:

```kotlin
class SimpleTextRenderer<Item> : ItemBaseRenderer<Item, RenderContract>() {

    interface RenderContract {
        val title: String
        val subtitle: String?
            get() = null
    }

    override val layoutRes: Int = R.layout.item_text

    override fun onBindView(viewHolder: BaseViewHolder, item: RenderContract) {
        viewHolder.itemView.title.text = item.title
        viewHolder.itemView.subtitle.text = item.subtitle
    }
}
```

Next step is implement `RenderContract` in particular child of ```Item```:

```kotlin
sealed class Item : ComparableItem {
   
    data class SimpleTextItem(
        override val title: String,
        override val subtitle: String? = null,
        val uuid: String = UUID.randomUUID().toString()
    ) : Item(), SimpleTextRenderer.RenderContract
}
```

after that we can create `RenderAdapter` and populate this list:

```kotlin
class ListFragment : Fragment(R.layout.fragment_list) {

    private val itemAdapter by lazy {
        RenderAdapterBuilder<Item>()
            .renderer(Item.SimpleTextItem::class, SimpleTextRenderer())
            .build()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val spaceDecoration = SpaceRuleItemDecoration.Builder<Item>()
            .addRule {
                paddingVertical(4.dp)
                paddingHorizontal(16.dp)
            }
            .create()

        val dividerDecoration = DividerRuleItemDecoration.Builder<Item>(requireContext())
            .addRule {
                gravity(Gravity.BOTTOM)
                with {
                    viewType(Item.SimpleTextItem::class)
                }
            }
            .create()

        with(recyclerView) {
            adapter = itemAdapter
            setHasFixedSize(true)

            addItemDecoration(spaceDecoration)
            addItemDecoration(dividerDecoration)
        }

        itemAdapter.differ.submitList(ItemFactory.staticItems(requireContext())) // ItemFactory create list of SimpleTextItem
    }
}
```
Also you can find more examples in sample project.

## License
```
MIT License

Copyright (c) 2020 Nikita Zaltsman (@HaronCode) and Kinayatov Dias (@kdk96)

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
```
