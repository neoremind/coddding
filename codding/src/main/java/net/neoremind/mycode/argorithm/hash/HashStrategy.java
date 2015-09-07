package net.neoremind.mycode.argorithm.hash;

/**
 * @author zhangxu
 */
public interface HashStrategy<K> {

    int hash(K key);

    int indexFor(int hash, int length);

}
