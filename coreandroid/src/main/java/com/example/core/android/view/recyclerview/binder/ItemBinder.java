package com.example.core.android.view.recyclerview.binder;

public interface ItemBinder<T> {
    int getLayoutRes(T model);
    int getBindingVariable(T model);
}
