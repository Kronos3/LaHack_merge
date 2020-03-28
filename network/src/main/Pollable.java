package main;

import java.util.ArrayList;
import java.util.function.Function;

public class Pollable<E> extends ArrayList<E> {
    private Function<Integer, Object> poll;

    /**
     * Create a new pollable with a poll function
     * @param poll_proto poll function
     */
    public Pollable(Function<Integer, Object> poll_proto) {
        super();
        this.poll = poll_proto;
    }

    /**
     * Poll n amounts from the
     * @param n number of times to poll
     * @return number of times items added
     */
    public int poll(int n) {
        int out = 0;

        for (int i = 0; i < n; i++) {
            E obj = (E)this.poll.apply(0);
            if (obj != null) {
                out++;
                this.add(obj);
            }
        }

        return out;
    }
}
