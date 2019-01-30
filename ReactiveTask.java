import java.util.Queue;
import java.util.concurrent.atomic.AtomicBoolean;

public abstract class ReactiveTask {

    static Queue<ReactiveTask> taskQueue;

    AtomicBoolean runShouldEnqueueTask;
    AtomicBoolean runTask;

    public static void setTaskQueue(Queue<ReactiveTask> taskQueue) {
        ReactiveTask.taskQueue = taskQueue;
    }

    void startCheckEnqueueTaskConditionThread() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                runShouldEnqueueTask = new AtomicBoolean(true);
                while(runShouldEnqueueTask.get()) {
                    if(taskQueue.contains(this)) {
                        try {
                            Thread.sleep(500);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        continue;
                    }

                    if(checkEnqueueTaskCondition()) {
                        taskQueue.add(ReactiveTask.this);
                    }

                }
            }
        }).start();
    }

    void stopCheckEnqueueTaskConditionThread() {
        if(runShouldEnqueueTask != null)
            runShouldEnqueueTask.set(false);
    }

    void runTask() {
        runTask = new AtomicBoolean(true);
        new Thread(new Runnable() {
            @Override
            public void run() {
                task();
            }
        }).start();
    }

    void stopTask() {
        if(runTask != null)
            runTask.set(false);
    }

    abstract void task();
    abstract boolean checkEnqueueTaskCondition();
}
