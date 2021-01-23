package com.harvest.core_network.impl;

import androidx.annotation.MainThread;

public interface ErrorNotifier {
    @MainThread
    void notifyError(Throwable throwable);
}
