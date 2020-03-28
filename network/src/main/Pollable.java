package main;

import java.util.ArrayList;
import java.util.function.Function;

public class Pollable<E, K> extends ArrayList<K> {
    private Function<E, K> poll;

    /**
     * Create a new pollable with a poll function
     * @param poll_proto poll function
     */
    public Pollable(Function<E, K> poll_proto) {
        super();
        this.poll = poll_proto;
    }

    /**
     * Poll n amounts from the
     * @param n number of times to poll
     * @return number of times items added
     */
    public int poll(E arg, int n) {
        int out = 0;

        for (int i = 0; i < n; i++) {
            K obj = this.poll.apply(arg);
            if (obj != null) {
                out++;
                this.add(obj);
            }
        }

        return out;
    }
}
