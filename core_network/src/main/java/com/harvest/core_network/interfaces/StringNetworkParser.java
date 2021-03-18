package com.harvest.core_network.interfaces;

public interface StringNetworkParser<T> {
    T parseString(String string) throws Exception;
}
