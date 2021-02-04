package com.harvest.core_network.interfaces;

import androidx.annotation.MainThread;

public interface ErrorNotifier {
    @MainThread
    void notifyError(Throwable throwable);
}
