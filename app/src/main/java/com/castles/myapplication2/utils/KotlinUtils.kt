package com.castles.myapplication2.utils

open class View {
    open fun onClick(){
        println("View onClicked");
    }
}
fun View.showoff() = println("it is a View")

class Button : View(){
    override fun onClick() {
        super.onClick()
        println("Button Clicked")
    }
}
fun Button.showoff() = println("it is a button")

val list = listOf(1,2,3,4,8,5,7)

fun <T> listOf(vararg values:T){
    println(values);
}

fun View.textVararg(args : Array<String>){
    listOf("args",args)
}

infix fun Any.to(other :Any) = Pair(this,other)