package com.loongcheer.core.common.utils.task;

public interface WorkerListener {
    void onWorkStart(Worker worker);

    void onWorkFinished(Worker worker);
}
