# Auto-Observer 0.2  !["#"](https://img.shields.io/badge/Version-0.2-green.svg) !["#"](https://img.shields.io/badge/language- java | kotlin -orange.svg)
[![Github Releases (by Release)](https://img.shields.io/github/downloads/atom/atom/v0.190.0/total.svg)](https://raw.githubusercontent.com/liuyouth/Auto-Observer/master/AutoObserver.jar)



基于RxJava 2.0 
Auto-Observer 的作用，让开发者更专注于业务代码，减少冗余代码，  
使工程更加健壮可通过AOP的方式进行全局管控  

# 本Simple使用的第三方库
`SmartRefreshLayout`   `LoadSir`   `MultiType`   `butterknife`   `retrofit2`   `Rxjava2`

内置对 SmartRefreshLayout ,LoadSir 的适配器。  
刷新加载框架使用 SmartRefreshLayout   
异常状态页面使用的 LoadSir  
仅仅是simple使用的，如果你有更好的，或者想自己实现也可接入。  

# 目前仅有针对于列表页面定制的观察者
我们来看一下业务场景，我们的列表页面一般都有上拉加载，下拉刷新的功能，这个功能并不复杂。  
我们拿到了数据之后判断是否请求成功，是否有数据，然后进行展示并关闭刷新 加载状态。  
做完这些之后看起来我们的列表已经可以正常工作了，是的没错。但是我们忽略了一些细节，比如是否可以进行上拉加载？  
这个字段一般后台都会返回给我们，如果没有返回那么他肯定给你返回了总页数，我们也可以根据总页数与当前页码进行比对拿到是否可以进行上拉加载。  
如果也没有，请不要介意怼一怼后台，毕竟怼怼更健康嘛~  
当我们完成了是否可以上拉加载以后。你觉得做完了是吗？  
当后台按照条件查询，可匹配的数据为0时；  
我们的页面展示了什么？ 空白页面，对就是空白页面，这样给用户的体验是很直观的！ 就是空的！ 对！   
当然你这么想也对，如果访问网络速度过快用户恰好也没看到加载过程，用户可能会以为哦？我刷新了没刷新。再刷一次看看吧。  
当然此时我们加上一个单独页面 ，经过UImm设计的没有数据的页面（图片，Gif）效果是不是比我们真正空白的空白页体验好多了  
举一反三 在第一次刷新的时候 如果发生了网络异常，或者是其他的错误，我们也这么去提示岂不是更好了。  

我们在发起网络请求之前判断下我们的设备是否进行了联网，如果没有就丢给用户一个没有网络的状态页。  
如果进行了联网则去完成观察者与被观察者的绑定进行网络请求，  
在请求回来的时候我们进行统一的code码的辨别，如果是有些公用的code可以统一一起做处理  
如果没有异常我们会正常的走到OnNext  
然后由列表观察者自身携带 `isRefresh` 标识进行分发是需要将数据交由 `onRefresh` 处理还是 `onLoadMore`  
并且我们内置了一些常规操作例如页码的+- 重置。  

同时如果您有特殊的操作可以在外部去操作已经默认实现的东西。  



调用代码
```java
    private void initView() {
        adapter.register(EchoSearch.class, new EchoSearchViewBinder());
        UIUtil.INSTANCE.initRecyclerview(list, adapter, items, new LinearLayoutManager(context));
        smartRefreshLayout.setOnRefreshListener(refreshLayout -> getData(true))
                .setOnLoadMoreListener(refreshLayout -> getData(false));
        echoSearchByNameObserver = new AutoListListener.Builder<ApiResult<ArrayList<EchoSearch>>>()
                .setAutoSwitchStatusPageObserver(LoadSir.getDefault().register(smartRefreshLayout, v -> getData()))
                .setAutoRefreshObserver(smartRefreshLayout)
                .setItems(items)
                .onRefresh(arrayListApiResult -> items.clear())
                .onComplete(arrayListApiResult -> {
                    items.addAll(arrayListApiResult.getData());
                    adapter.notifyDataSetChanged();
                })
                .build();
    }

    private void getData() {
        getData(null);
    }

    private void getData(Boolean isRefresh) {
        DataManager.echoSearchByName(keyword, echoSearchByNameObserver.getPage())
//                .compose(provider.bindToLifecycle())
                .subscribe(echoSearchByNameObserver.setRefresh(isRefresh));
    }
```

版本变更
# 0.2
1. 添加了OnSubscribe 接口 将绑定事件暴露出来
2. 添加AutoPageData 接口 由列表返回的实体实现，用户判断是否含有下一页
3. AutoRefreshListener 添加 是否含有下一页参数，方便用户一键设置。
4. AutoDialogListener 弹框监听，下个版本可用。
# 0.1
1. 新建工程不过多描述
2. AutoListObserver 自动列表观察者 在列表页面使用该Observer 绑定网络请求
3. AutoSwitchStatusPageListener 切换页面监听，需要用户自己实现。自己的页面切换是用什么方式实现
4. AutoRefreshListener 刷新加载监听，同上面向接口编程，不指定刷新加载怎么实现，用户可以自己定义

# 交流
联系方式：faithll@163.com
QQ：38637714

# License
```license
Copyright 2017 iolll.com<faithll@163.com>

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0
   
Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```
