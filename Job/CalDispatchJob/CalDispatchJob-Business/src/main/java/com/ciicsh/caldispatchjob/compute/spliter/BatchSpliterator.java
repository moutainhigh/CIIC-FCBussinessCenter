package com.ciicsh.caldispatchjob.compute.spliter;

import com.mongodb.DBObject;

import java.util.List;
import java.util.Spliterator;
import java.util.function.Consumer;

/**
 * Created by bill on 18/3/29.
 */

// Example implementation of a custom spliterator.
// In order to get a parallel stream of this spliterator, call
// StreamSupport.stream(new BatchSpliterator(batchs), true)
public class BatchSpliterator implements Spliterator<DBObject> {

    private final List<DBObject> batchs;
    private int from;
    private int to;

    public BatchSpliterator(List<DBObject> batchs) {
        this(batchs, 0, batchs.size());
    }

    private BatchSpliterator(List<DBObject> batchs, int from, int to) {
        this.batchs = batchs;
        this.from = from;
        this.to = to;
    }

    @Override
    public boolean tryAdvance(Consumer<? super DBObject> action) {
        if (size() > 0) {
            action.accept(batchs.get(from));
            from++;
            return true;
        }
        return false;
    }

    private int size() {
        return to - from;
    }

    @Override
    public Spliterator<DBObject> trySplit() {
        if (size() == 1) {
            System.out.printf("split -> [%d-%d] not splitting because size is 1\n", from, to);
            return null;
        }
        System.out.printf("split -> [%d-%d] -> [%d-%d], [%d-%d]\n", from, to, from, to-size()/2, from + size()/2, to);
        Spliterator<DBObject> other = new BatchSpliterator(batchs, from + size()/2, to);
        this.to -= size()/2;
        return other;
    }

    @Override
    public long estimateSize() {
        return Long.MAX_VALUE;
    }

    @Override
    public int characteristics() {
        return 0;
    }
}