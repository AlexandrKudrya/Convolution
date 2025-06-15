package com.convolution.batch.pipeline;

import com.convolution.core.BaseConvolution;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class PipelineController {

    public static void runPipeline(
            Path inputRoot, Path outputRoot,
            BaseConvolution convolution, float[][] kernel,
            int readerThreads, int workerThreads, int writerThreads, int queueSize
    ) throws InterruptedException, IOException {
        // Рекурсивно собираем все файлы
        List<Path> allFiles = Files.walk(inputRoot)
                .filter(Files::isRegularFile)
                .filter(path -> {
                    String n = path.getFileName().toString().toLowerCase();
                    return n.endsWith(".jpg") || n.endsWith(".png") || n.endsWith(".bmp");
                })
                .toList();

        PipelineStats stats = new PipelineStats(allFiles.size());

        BlockingQueue<ImageTask> readQueue = new ArrayBlockingQueue<>(queueSize);
        BlockingQueue<ResultTask> writeQueue = new ArrayBlockingQueue<>(queueSize);

        List<List<Path>> readerParts = splitList(allFiles, readerThreads);
        List<Thread> readers = new ArrayList<>();
        for (int i = 0; i < readerThreads; i++) {
            readers.add(new Thread(
                    new ImageReader(readerParts.get(i), inputRoot, outputRoot, readQueue, stats),
                    "Reader-" + i
            ));
        }

        ExecutorService workers = Executors.newFixedThreadPool(workerThreads);
        for (int i = 0; i < workerThreads; i++) {
            workers.submit(new ConvolutionWorker(readQueue, writeQueue, convolution, kernel, stats));
        }

        List<Thread> writers = new ArrayList<>();
        for (int i = 0; i < writerThreads; i++) {
            writers.add(new Thread(
                    new ImageWriter(writeQueue, stats),
                    "Writer-" + i
            ));
        }

        // Thread monitor = new Thread(new PipelineMonitor(stats, readQueue, writeQueue));
        // monitor.start();

        readers.forEach(Thread::start);
        writers.forEach(Thread::start);

        for (Thread t : readers) t.join();

        for (int i = 0; i < workerThreads; i++) readQueue.put(ImageTask.POISON);

        workers.shutdown();
        workers.awaitTermination(1, TimeUnit.HOURS);

        for (int i = 0; i < writerThreads; i++) writeQueue.put(ResultTask.POISON);

        for (Thread t : writers) t.join();

        stats.finished = true;
        // monitor.join();
    }

    static <T> List<List<T>> splitList(List<T> list, int parts) {
        List<List<T>> result = new ArrayList<>();
        int n = list.size() / parts, rem = list.size() % parts, pos = 0;
        for (int i = 0; i < parts; i++) {
            int len = n + (i < rem ? 1 : 0);
            result.add(list.subList(pos, pos + len));
            pos += len;
        }
        return result;
    }
}