package net.neoremind.mycode.guava.cache;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

import java.util.concurrent.ExecutionException;

public class CacheTest2 {

  private static final Cache<String, String> CACHE =
      CacheBuilder.newBuilder()
          .softValues()
          .maximumSize(100)
          .build();

  private static final LoadingCache<String, String> CACHE2 =
      CacheBuilder.newBuilder()
          .maximumSize(100)
          .softValues()
          //build方法中可以指定CacheLoader，在缓存不存在时通过CacheLoader的实现自动加载缓存
          .build(CacheLoader.from(CacheTest2::createValue));

  private static String createValue(String key) {
    return key + "456";
  }

  public static void main(String[] args) throws ExecutionException, InterruptedException {
    String res = CACHE.get("abc", () -> "456");
    System.out.println(res);

    String res2 = CACHE2.get("abc");
    System.out.println(res2);
  }
}
