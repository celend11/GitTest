@file:JvmName("testActivity")
package com.castles.myapplication2
//import com.castles.myapplication.utils.Button
import android.Manifest
import android.content.ContentResolver
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.Button
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.castles.myapplication2.R
import com.castles.myapplication2.java.JavaUtils
import com.castles.myapplication2.java.JavaUtils.*
import com.castles.myapplication2.java.Policy
import com.castles.myapplication2.utils.View
import com.castles.myapplication2.utils.showoff
import java.beans.PropertyChangeListener
import java.beans.PropertyChangeSupport
import java.time.LocalDate
import java.util.*
import kotlin.collections.HashMap
import kotlin.experimental.and
import kotlin.properties.Delegates
import kotlin.reflect.KProperty

class MainActivity : AppCompatActivity(){
    private lateinit var bnt:Button
    val runable = Runnable { println(788888888) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val person = Person("frodo",30)
        bnt = findViewById<Button>(R.id.button);
        println("XXXXXXXXXXXXXXXXXXXXXxx ${person.age} ${person.name}")
        val persons = listOf(Person("json",60),Person("tom",40))
        var person2 = persons.maxBy { it.age?:0 }
        println(person)
        println(person2)
        val arrays = arrayListOf<String>("java")
        arrays.add("c#")
        val color = Color.RED
        val javautils = JavaUtils()
        javautils.setSleepTime(this,50000)
        println(color.getRGB(color))

        javautils.postBBB()
        postAAA(1000){runable}
        val resolver: ContentResolver = this.getContentResolver()
        saveBrightness(resolver,80)   //必须系统app
//        javautils.testDate()
        try {
            if(checkSelfPermission(Manifest.permission.SET_TIME) != PackageManager.PERMISSION_GRANTED){
                requestPermissions(arrayOf(Manifest.permission.SET_TIME),1)
            }else{
                javautils.CloseAutoTime(this,"false")
                setDate(2018,8,11)     //设置系统时间
            }
        }catch (e:Exception){
            println(e)
        }
        StartOrStopWifi(this,true)   //控制wifi开关
//        javautils.toggleGps(this);   //控制GPS开关
        javautils.changeGpsStatus(false,this)
//        javautils.setGpsLocationMode(2,this)
        javautils.changeMobileNetworkStatus(this,true)
        val locale = Locale("en", "US")
        javautils.updateLanguage(locale)
//        setDefaultApp("com.liangfu.test")
//        val policyAdmin = Policy(this)
//        javautils.changeLockScreenPassword(this,"123456","1234567890")
        javautils.changeLockScreenStatus(this,false,"123456")
//        javautils.setLauncherApp(this,"com.castles.aidltest/.MainActivity")
//        policyAdmin.requestLockAdmins()
//        policyAdmin.setApplicationHidden("com.castles.aidltest",false)
//        javautils.setTimeZone(this,"Asia/Shanghai")
//        setDefaultApp("com.example.casw2_d_link.ctms_app")
        setDefaultApp2("com.castles.aidltest")
//        javautils.ChangeDebugStatus(this)





        //            println(fuzzandbuzz(i))
//            println("\n")
//        }
        for (i in 100 downTo  50){
            println(fuzzandbuzz(i))
            println("\n")
        }

        for (i in 1 .. 50){
            println(fuzzandbuzz(i))
            println("\n")
        }
        println(testSHA256());
        val propertyChangeAware = PropertyChangeAware("fro",30)
        propertyChangeAware.addPropertyChangeListener(PropertyChangeListener { evt -> println("${evt.propertyName} changed from ${evt.oldValue} to ${evt.newValue}")  })
        propertyChangeAware.age = 28
        propertyChangeAware.name = "liang"
        val char = 'c';
        println(char in 'a'..'z');
        val hashMap = hashMapOf(1 to "one",2 to "second",3 to "three");
        val sets =  sortedSetOf(1,6,5,7,9,0,3,4)

        sets += 10
//        val iterator = sets.iterator()
//        while (iterator.hasNext()){
//
//        }
        println("${sets.last()} == ${sets.max()} ")
//        var sets = hashMap.toMap();
        for ((index,element) in hashMap){
            println("$index =XXX=$element");
        }

        val view = View()
        view.onClick()
        view.showoff()
//        val button = Button()
//        button.onClick()
//        button.showoff()
        println("12.345-6.A".split(".","-"))
        val path = "/users/liangfu/demo/lg.png"
        val regex = """(.+)/(.+)\.(.+)""".toRegex()   //三重引号  正则表达式
        val m = regex.matchEntire(path);
        if(m!=null){
            val (d,f,e) = m.destructured
            println("path=$d filename=$f type=$e")
        }
        println("""$99.9 {'$'}""")
        val p1 = Person("test",30)
        val p2 = Person("test",30)
        println("has data =${p1==p2}")
        val p3 = PersonT("test",30)
        val p4 = PersonT("test",30)
        println("has no data =${p3==p4}")
        val bob = Client("bob",123)
        println(bob.copy(postalCode=456))
        MPerson.A()
        MPerson.B()
        run{
            println(42)
            for (i in 1..100000){
                when (i){
                    10000 -> println("10000")
                    100000 -> println("100000")
                }

            }
        }
        println("99999999")
        val list = listOf(book("ABC",50, listOf("frodo","liang","fu")),book("ABD",30, listOf("frodo","xu","dan")))
        val flatmap = list.flatMap { it.authors }.toSet()  //去重
        println(flatmap)
        var flatmap2 = list.flatMap { it.authors }     //不去重
        println(flatmap2)
        //惰性集合操作
        list.asSequence()
            .map{it.name}
            .filter { it.startsWith("B")}
            .toList()
        val naturalNumbers = generateSequence(0) {it +1 }
        val num100 = naturalNumbers.takeWhile { it<=100 }
        bnt.setOnClickListener{view-> when(view.id) {
                R.id.button -> println("button clicked")
            }
        }
        val pp1 = Point(10,50)
        val pp2 = Point(20,15)
        println("----------------------------------")
        println(pp1+pp2)
        val foo = Foo()
        val oldvalue = foo.p
        println("oldvalue=$oldvalue")
        foo.p = "yyy"
        var sum : (Int, Int) -> Int = {x,y -> x*2+y}
        println("sum="+sum(5,6))

        var cannull : (Int,Int) -> Int? = { x,y-> null }
        var cannull2 : ((Int,Int) -> Int)? = null
        cannull(3,3)
//        val i = Intent("android.intent.action.REBOOT")
//        // 立即重启：1
//        // 立即重启：1
//        i.putExtra("nowait", 1)
//        // 重启次数：1
//        // 重启次数：1
//        i.putExtra("interval", 1)
//        // 不出现弹窗：0
//        // 不出现弹窗：0
//        i.putExtra("window", 0)
//        startActivity(i)
//        val flaten = list.flatten{it.authors}

//        view.textVararg(arrayOf("A","B","C"))
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when(requestCode){
            1 ->  setDate(2018,8,11)     //设置系统时间
        }
    }


}


class testLambda(val i : Int){
    val clicks : Int
        get() = i

}
@RequiresApi(Build.VERSION_CODES.O)
operator fun ClosedRange<LocalDate>.iterator():Iterator<LocalDate> =
    object:Iterator<LocalDate>{
        var current = start
        override fun hasNext(): Boolean {
            return current<=endInclusive
        }
        override fun next() = current.apply {
            current = this.plusDays(1)
        }
    }

fun f3(){
    val s : Collection<Int> = arrayListOf(1,2,3,4,6,5)
    val s2 : MutableCollection<Int> = arrayListOf(2,8)
    for (item in s2){

    }
}

data class Point(val x : Int,val y : Int){
    operator fun plus(other: Point) : Point{
        return Point(x + other.x,y + other.y)
    }
}

class book(val name:String,val price : Int,val authors : List<String>)



class DelCollection<T>(val intext :Collection<T>):Collection<T> by intext{

}

fun f() : Unit{

}

fun f2():Nothing{
    throw java.lang.Exception("")
}

fun max(a:Int,b:Int) = if (a>b) a else b;

data class Person(val name : String,val age : Int?)

class PersonT(val name:String,val age:Int?)

fun renderPersonList(person: Collection<Person>){

}
class Client(val name:String,val postalCode:Int){
    fun copy(name:String = this.name,postalCode:Int = this.postalCode) = Client(name,postalCode)

}

class MPerson(val w : Int,val h : Int){
    val isOk : Boolean
        get() = w == h

    companion object{
        fun A(){

            println("AAAAAAAAA")
        }
        fun B(){
            println("BBBBBBBB")
        }
    }

}
enum class Color{
    RED,ORANGE,BLUE;
    fun getRGB(color: Color) : Color{
        when(color){
            BLUE -> "蓝色"
            ORANGE ->"橘黄"
            RED ->"红色"
        }
        return color;
    }
}

interface Test{
    fun test1()
    fun test2()
}
class Num():Test{
    override fun test1() {
        TODO("Not yet implemented")
    }

    override fun test2() {
        TODO("Not yet implemented")
    }
}
class Sum():Test{
    override fun test1() {
        TODO("Not yet implemented")
    }

    override fun test2() {
        TODO("Not yet implemented")
    }

    override fun toString(): String {
        return super.toString()
    }

    override fun equals(other: Any?): Boolean {
        return super.equals(other)
    }

    override fun hashCode(): Int {
        return super.hashCode()
    }
}

fun compareTest(test: Test){
    if(test is Sum){
        val n = test as Sum;
        return n.test1();
    }else if(test is Num){

    }
}

fun fuzzandbuzz(i :Int) =
    when{
        i % 15 == 0 -> "fuzzbuzz"
        i % 3 == 0 -> "fuzz"
        i % 5 == 0 -> "buzz"
        else -> "$i"
    }

fun testMap(){
    val map = HashMap<Char,String>()
    for (i in 'A'..'F'){
        map[i] =Integer.toBinaryString(i.toInt());
    }
}

fun testSHA256() : StringBuffer{
    val str = "5080B6EE794E0F0CC0C15D77440643D6F93C9224B1A3618B443BE202B4B82AD8";
    val bytes = str.toByteArray();
    val stringBuffer = StringBuffer();
    var temp:String;
    for (i in 0..bytes.size-1){
        var by = bytes[i] and 0xFF.toByte();
        temp = Integer.toHexString(by.toInt());
        if (temp.length==1){
            //1得到一位的进行补0操作
            stringBuffer.append("0");
        }
        stringBuffer.append(temp);
    }
    return stringBuffer;
}

fun <T> toJsonString(collection: Collection<T>) : String{
    for ((index,element) in collection.withIndex()){

    }

    return ""
}
val String.lastChar : Char
    get() = get(length -1)

class PropertyChangeAware(name :String, age :Int){
    protected val changeSupport = PropertyChangeSupport(this)

    fun addPropertyChangeListener(listener : PropertyChangeListener){
        changeSupport.addPropertyChangeListener(listener)
    }

    fun removePropertyChangeListener(listener : PropertyChangeListener){
        changeSupport.removePropertyChangeListener(listener)
    }

    var age:Int = age
    set(newValue){
        val oldValue = field
        field = newValue
        changeSupport.firePropertyChange("age",oldValue,newValue)
    }
    var name:String = name
        set(newValue){
            val oldValue = field
            field = newValue
            changeSupport.firePropertyChange("name",oldValue,newValue)
        }

//    var height : Int by Delegates.observable(age,"")
}

class Foo {

    var p : String by Delegate()

}
class Delegate{

    operator fun getValue(foo: Foo, property: KProperty<*>): String {
        println("Delegate:getValue")
        return property.name
    }

    operator fun setValue(foo: Foo, property: KProperty<*>, s: String) {
        println("Delegate:setValue property= ${property.name} value=$s")
//        foo.p = s
    }
}



