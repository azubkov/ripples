package com.artit.ripples.imposition.threaded;

import com.artit.ripples.utils.ExecutorTask;
import org.apache.log4j.Logger;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by IntelliJ IDEA.
 * User: art
 * Date: 07.05.2011
 * Time: 23:13:51
 * To change this template use File | Settings | File Templates.
 */
class StepLoadTracker extends ExecutorTask {
    private static final Logger LOG = Logger.getLogger(StepLoadTracker.class);
    private final long waitTimeout;
    private final int steps;
    private final ExecutorTask finishedCallback;
    private final AtomicBoolean isProcessed = new AtomicBoolean(false);
    private final AtomicInteger counter = new AtomicInteger();

    StepLoadTracker( int steps, ExecutorTask finishedCallback, long waitTimeout) {
        this.waitTimeout = waitTimeout;
        this.steps = steps;
        this.finishedCallback = finishedCallback;
    }
    /**without backup timer*/
    StepLoadTracker(int steps, ExecutorTask finishedCallback) {
        this( steps, finishedCallback, -1);
    }

    final void waitLoading(){
        LOG.debug("waiting for loading ... "+this);
        if(waitTimeout>0){
/*do smg*/
        }
    }

    final void stepLoaded(){
        LOG.debug("stepLoaded : "+counter.get());
        if(counter.incrementAndGet()>=steps){
            if(isProcessed.compareAndSet(false,true)){
                LOG.debug("Loading completed normally : "+this);
                finishedCallback.run();
            }
        }
    }

    @Override
    protected void perform() {
        if (isProcessed.compareAndSet(false, true)) {
            LOG.debug("Loading skipped by backup timer : "+this);
            finishedCallback.run();
        }
    }

    public int getSteps() {
        return steps;
    }

    public int getCounter() {
        return counter.get();
    }

    @Override
    public String toString() {
        return super.toString()+"{" +
                "steps=" + steps +
                ", waitTimeout=" + waitTimeout +
                ", counter=" + counter +
                '}';
    }
}
