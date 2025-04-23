package jp.juggler.testCompose.utils

import android.os.Bundle

fun Bundle.string(key:String) = if(containsKey(key)) getString(key) else null
fun Bundle.boolean(key:String) = if(containsKey(key)) getBoolean(key) else null
fun Bundle.int(key:String) = if(containsKey(key)) getInt(key) else null
