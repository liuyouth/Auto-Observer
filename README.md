# Auto-Observer

基于RxJava 2.0 

# 本Simple使用的第三方库
`SmartRefreshLayout``LoadSir``MultiType``butterknife``retrofit2``Rxjava2`

内置对 SmartRefreshLayout ,LoadSir 的适配器。
刷新加载框架使用 SmartRefreshLayout 
异常状态页面使用的 LoadSir
仅仅是simple使用的，如果你有更好的，或者想自己实现也可接入。

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
