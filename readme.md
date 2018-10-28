# 一次草榴爬虫网站的搭建

## 主要工具
SpringBoot、Redis、ElasticSearch、Joup

## 一些值得注意的点
1. 由于阿里云的主机配置内存只有2g，因此es经常崩溃，目前的解决办法是，在爬虫玩之后将数据从redis同步到es。当然，如果现在只是爬取信息而不下载视频的话es应该是不会崩溃的，因为推测崩溃的原因是下载视频时java程序的内存占用过高
2. 在本机测试时，es是装在docker里的，配了9300的端口映射，但是transport还是一直连不上es
3. redis的分页目前还是比较粗糙，后面再看看
4. redis的资源释放：一定要在finally块中将jedis放回jedispool，不然会报NoSuchElementException: Timeout waiting for idle object
5. es远程连接的配置，以及不能以root用户进行操作
6. nginx403的排查
7. 爬虫的时候，尤其是文件下载的部分，要写得健壮
8. 控制文件下载的数量，使用redis实现一盒类似于分布式锁，应当在代码层面上对get和incr外面加一把锁，然后进行下载，如果下载报了异常，搞一个类似回滚的操作，decr，不用加锁
9. 熟悉linux命令
10. 线程池，阻塞队列

## 遗留的问题：
redis分页，定时爬虫以及如何终止，定时同步数据，如何更新视频，排名系统

# 第一次迭代需要解决的问题：
1. 前台直接跳转到视频源存在access denied问题
2. 很多的previewUrl没有爬出来
3. 很多视频会失效
4. 定时任务