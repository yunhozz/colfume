package colfume.common.util;

import java.time.Instant;
import java.util.concurrent.atomic.AtomicInteger;

public class RandomIdUtils {

    private static final int counterMax = 256 * 256;
    private static final AtomicInteger intVal = new AtomicInteger(0);

    private String generate() {
        final long uid = Instant.now().toEpochMilli() * counterMax + intVal.accumulateAndGet(1, (index, inc) -> (index + inc) % counterMax);
        return Long.toHexString(uid);
    }

    public String next() {
        return generate();
    }
}