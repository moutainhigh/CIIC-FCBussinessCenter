package com.ciicsh.caldispatchjob.compute.util;

import com.ciicsh.caldispatchjob.compute.spliter.PartitioningSpliterator;

import java.util.List;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * Created by bill on 17/8/31.
 */
public class StreamOpt {
    public static <T> Stream<T> partition(Stream<T> stream, int partitionSize) {
        return StreamSupport.stream(new PartitioningSpliterator(stream.spliterator(), partitionSize),
                stream.isParallel());
                //.map(sp -> StreamSupport.stream(sp, stream.isParallel()));
    }

    public static <E> Stream<List<E>> doPartition(Stream<E> list, int size, int batchSize){
        Stream<List<E>> partitioned = PartitioningSpliterator.partition(list, size, batchSize);
        return partitioned;
    }

    public static <E> Stream<List<E>> doPartition(Stream<E> list, int size){
        Stream<List<E>> partitioned = PartitioningSpliterator.partition(list, size);
        return partitioned;
    }
}
