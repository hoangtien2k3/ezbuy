package com.ezbuy.framework.annotations.logging;

import brave.Span;
import com.ezbuy.framework.model.logging.LoggerDTO;
import lombok.extern.slf4j.Slf4j;
import reactor.util.context.Context;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.atomic.AtomicReference;

@Slf4j
public class LoggerQueue {
    private static LoggerQueue mMe = null;
    private ArrayBlockingQueue<LoggerDTO> myQueue = null;
    private static Object myLock = new Object();
    private int countFalse = 0;
    private int countSuccess = 0;


    public static LoggerQueue getInstance() {
        if (mMe == null) {
            mMe = new LoggerQueue();
        }
        return mMe;
    }

    private LoggerQueue() {
        myQueue = new ArrayBlockingQueue<>(100000) {};
    }

    public void clearQueue() {
        myQueue.clear();
    }

    public LoggerDTO getQueue() {
        return myQueue.poll();
    }

    public boolean addQueue(LoggerDTO task) {
        if (myQueue.add(task)) {
            countSuccess++;
            return true;
        }
        countFalse++;
        return false;
    }


    public boolean addQueue(AtomicReference<Context> contextRef, Span newSpan, String service, Long startTime, Long endTime, String result, Object obj, String logType, String actionType, Object[] args, String title) {
        try {
            if (myQueue.add(
                    new LoggerDTO(contextRef, newSpan, service, startTime, endTime,
                            result, obj, logType, actionType, args, title)
            )) {
                countSuccess++;
                return true;
            }
        } catch (Exception ex) {
        }
        countFalse++;
        return false;
    }

    public List<LoggerDTO> getRecords() {
        List<LoggerDTO> records = new ArrayList<>();
        if (myQueue != null) {
            myQueue.drainTo(records, 100000);
        }
        return records;
    }

    public int getQueueSize() {
        return myQueue.size();
    }

    public int getCountFalse() {
        return countFalse;
    }

    public int getCountSuccess() {
        return countSuccess;
    }

    public void resetCount() {
        countSuccess = 0;
        countFalse = 0;
    }
}
