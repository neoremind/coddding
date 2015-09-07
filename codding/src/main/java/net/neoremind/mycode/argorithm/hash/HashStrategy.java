package net.neoremind.mycode.argorithm.hash;

/**
 * 哈希策略
 *
 * @author zhangxu
 */
public interface HashStrategy<K> {

    /**
     * 求Key的哈希值
     *
     * @param key 键
     *
     * @return 哈希值
     */
    int hash(K key);

    /**
     * 根据hash值找桶、或者叫做槽位（bucket、slot）
     *
     * @param hash   哈希值
     * @param length 散列表大小
     *
     * @return 桶索引index
     */
    int indexFor(int hash, int length);

}
