package com.example.splitit.repository;

public interface OnRepositoryActionListener {
    void actionSuccess();
    void actionFailed(String message);
}
