# OkGo 的 okRx2部分 抽取 方便使用

# 涉及到的依赖
    // okRx2
    compile 'com.lzy.net:okrx2:2.0.2'

    //OkRx2 作者二次封装 Converter类所需依赖
    compile 'com.readystatesoftware.chuck:library:1.0.4'

    compile 'com.google.code.gson:gson:2.8.0'
    compile 'io.reactivex.rxjava2:rxandroid:2.0.2'
#  Demo 下的 几个包 得拷贝  base  callback  model  utils  以及 App
   net 下是对于请求 进行二次封装 喜欢可以拷贝使用  不喜欢 直接 rx连点也可以
# 主要样例代码
    几种请求样式  文件上传下载  MainActivity

# copy了 OkGo 的样例代码中的OkRx2使用到的 二次封装代码 主要

# project 下 要加个jit pack  maven 库

