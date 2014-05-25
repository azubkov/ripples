package com.artit.ripples.utils;

import org.apache.log4j.Logger;

public abstract class ExecutorTask implements Runnable {
    private static final Logger LOG = Logger.getLogger(ExecutorTask.class);

    public final void run() {
        try {
            perform();
        } catch (Throwable t) {
            LOG.error(t, t);
        }
    }

    protected abstract void perform();
}
