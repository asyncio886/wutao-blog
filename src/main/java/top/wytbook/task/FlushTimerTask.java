package top.wytbook.task;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * 刷库的定时任务
 */
@Component
public class FlushTimerTask {

    @Value("#{updateCacheToDb}")
    UpdateCacheToDb updateCacheToDb;

    @Async("timerTaskThreadPool")
    @Scheduled(fixedDelay = 30, timeUnit = TimeUnit.MINUTES)
    public void flushLikeRecord() {
        updateCacheToDb.flushLikeRecordToDb();
    }

    @Async("timerTaskThreadPool")
    @Scheduled(fixedDelay = 40, timeUnit = TimeUnit.MINUTES)
    public void flushLikeCount() {
        updateCacheToDb.flushLikeCountToDb();
    }

    @Async("timerTaskThreadPool")
    @Scheduled(fixedDelay = 20, timeUnit = TimeUnit.MINUTES)
    public void flushWatchCount() {
        updateCacheToDb.flushWatchCountToDb();
    }
}
