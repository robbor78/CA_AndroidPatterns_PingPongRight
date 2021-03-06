package edu.vuum.mocca;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @class SimpleSemaphore
 * @brief This class provides a simple counting semaphore
 * implementation using Java a ReentrantLock and a
 * ConditionObject (which is accessed via a Condition). It must
 * implement both "Fair" and "NonFair" semaphore semantics,
 * just liked Java Semaphores.
 */
public class SimpleSemaphore {
    /**
     * Define a Lock to protect the critical section.
     */
    // TODO - you fill in here
    private ReentrantLock mLock;

    /**
     * Define a Condition that waits while the number of permits is 0.
     */
    // TODO - you fill in here
    private Condition mHasPermits;

    /**
     * Define a count of the number of available permits.
     */
    // TODO - you fill in here.  Make sure that this data member will
    // ensure its values aren't cached by multiple Threads..
    private SimpleAtomicLong mAvailablePermits;

    public SimpleSemaphore(int permits, boolean fair) {
        // TODO - you fill in here to initialize the SimpleSemaphore,
        // making sure to allow both fair and non-fair Semaphore
        // semantics.

        mLock = new ReentrantLock(fair);
        mHasPermits = mLock.newCondition();
        mAvailablePermits = new SimpleAtomicLong(permits);
    }

    /**
     * Acquire one permit from the semaphore in a manner that can be
     * interrupted.
     */
    public void acquire() throws InterruptedException {
        // TODO - you fill in here.
        mLock.lock();
        try {
            while (mAvailablePermits.get() == 0) {
                mHasPermits.awaitUninterruptibly();
            }
            mAvailablePermits.decrementAndGet();
        } finally {
            mLock.unlock();
        }
    }

    /**
     * Acquire one permit from the semaphore in a manner that cannot be
     * interrupted.
     */
    public void acquireUninterruptibly() {
        // TODO - you fill in here.
        mLock.lock();
        try {
            while (mAvailablePermits.get() == 0) {
                mHasPermits.await();
            }
            mAvailablePermits.decrementAndGet();
        } catch (InterruptedException e) {
        } finally {
            mLock.unlock();
        }
    }

    /**
     * Return one permit to the semaphore.
     */
    public void release() {
        // TODO - you fill in here.
        mLock.lock();
        try {
            mAvailablePermits.incrementAndGet();
            mHasPermits.signalAll();
        } finally {
            mLock.unlock();
        }
    }

    /**
     * Return the number of permits available.
     */
    public int availablePermits() {
        // TODO - you fill in here by changing null to the appropriate
        // return value.
        long ap = mAvailablePermits.get();
        return (int)ap;
    }
}
