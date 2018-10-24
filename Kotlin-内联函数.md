## Kotlin中lambda回调
Kotlin中对Java中的一些接口回调进行了优化，可以直接使用lambda表达式来实现。简化写一些不必要的嵌套，但是**在lambda表达式中，只支持单抽象方法模型，也就是说设计的接口里面只有一个抽象方法，
才符合lambda表达式的规则，多个回调方法不支持**

例如：
在Java中的一个onClick事件
```
tvJump.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    goHome();
                }
            });
```
在Kotlin中我们就可以简写为
不使用lambda表达式时

```
tv_normal_title.setOnClickListener { object : View.OnClickListener{
                override fun onClick(v: View?) {
                }
            } }
```

使用lambda表达式时
```
            tv_normal_title.setOnClickListener { view -> View.OnClickListener { } }

```
同时我们也可以省略 *View.OnClickListener { }* 借助Kotlin的智能类型推到 
```
            tv_normal_title.setOnClickListener { view -> }

```
当view参数没有使用的时候我们也可以直接去掉view
```
            tv_normal_title.setOnClickListener {  } 
```

## Kotlin内联函数--With
```

/**
 * Calls the specified function [block] with the given [receiver] as its receiver and returns its result.
 */
@kotlin.internal.InlineOnly
public inline fun <T, R> with(receiver: T, block: T.() -> R): R = receiver.block()
```
with函数是一个单独的函数，并不是Kotlin中的扩展函数，通过代码我们可以看到with函数有两个参数，第一个recevier是一个泛型类的变量，第二个参数是一个lambda函数。它是将某对象作为函数的参数，在函数块内可以通过this
指代该对象。返回值为函数块的最后一行或指定return表达式

#### 用处
这个函数允许做什么？ 使用with后，我们可以用一变量的代码块作为上下文，这样就不需要每次使用它重复的名字。它可以替代构建起，不需要为每个类创建特定构建器

#### with的适用场景
适用于调用同一个类的多个方法时，可以省去类名重复，直接调用类的方法即可，经常用于Android的RecycleView的onBindViewHolder中

#### 例子：
以下代码我们可以通过with(bean)直接在函数体内获取到bean的属性变量
```
override fun onBindViewHolder(bean: AccountBean, position: Int) {
            with(bean) {
                tvName.text = name
                tvNumber.text = if (number.length > 3) number.substring(0, 3) + "..." else number + "..."
                tvCate.text = "【 $cate 】"
                tvBZ.text = if (beizhu.isEmpty()) "暂无备注..." else beizhu
                tvTime.text = DateUtils.format(createAt)
               
            }
         
        }
```

## Kotlin内联扩展函数--let

> let扩展函数实际上是一个作用域函数，当你需要去定义一个变量在一个特定的作用域范围内可以使用let；同时let函数可以避免一些判断为null的操作

```
@kotlin.internal.InlineOnly
public inline fun <T, R> T.let(block: (T) -> R): R = block(this)
```
#### let适用场景
- 最常用的场景就是用let函数处理需要针对一个可null的对象做统一判空处理
- 需要去明确一个变量所处特定的作用域范围内可以使用
  

例如：
```
        context?.let { it.resources.getString(R.string.todo_update) }
```

## 内联扩展函数--run

```
@kotlin.internal.InlineOnly
public inline fun <T, R> T.run(block: T.() -> R): R = block()
```

