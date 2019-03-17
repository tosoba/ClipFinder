package com.example.coreandroid.view.list.binder;

public interface ItemBinder<T> {
    int getLayoutRes(T model);

    int getBindingVariable(T model);
}
