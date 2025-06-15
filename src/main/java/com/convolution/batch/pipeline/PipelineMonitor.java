package com.convolution.batch.pipeline;

import java.util.concurrent.BlockingQueue;

public class PipelineMonitor implements Runnable {
    private final PipelineStats stats;
    private final BlockingQueue<?> readQueue, writeQueue;

    public PipelineMonitor(PipelineStats stats, BlockingQueue<?> readQueue, BlockingQueue<?> writeQueue) {
        this.stats = stats;
        this.readQueue = readQueue;
        this.writeQueue = writeQueue;
    }

    @Override
    public void run() {
        try {
            while (!Thread.currentThread().isInterrupted() && !stats.finished) {
                String msg = String.format(
                        "\rRead: %d/%d | Processed: %d/%d | Written: %d/%d | ReadQ: %d | WriteQ: %d",
                        stats.filesRead.get(), stats.totalFiles,
                        stats.filesProcessed.get(), stats.totalFiles,
                        stats.filesWritten.get(), stats.totalFiles,
                        readQueue.size(),
                        writeQueue.size()
                );
                System.out.print(msg);
                System.out.flush();
                Thread.sleep(100); // Обновлять чаще, если нужно
            }
            System.out.printf(
                    "\rRead: %d/%d | Processed: %d/%d | Written: %d/%d | ReadQ: %d | WriteQ: %d\n",
                    stats.filesRead.get(), stats.totalFiles,
                    stats.filesProcessed.get(), stats.totalFiles,
                    stats.filesWritten.get(), stats.totalFiles,
                    readQueue.size(), writeQueue.size()
            );
        } catch (InterruptedException ignored) {}
        // Для чистоты — перевести курсор на новую строку после завершения
        System.out.println();
    }
}

