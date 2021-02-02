package com.clipfinder.core.android.view.recyclerview.binder;

public class ItemBinderBase<T> implements ItemBinder<T> {
    private final int bindingVariable;
    private final int layoutId;

    public ItemBinderBase(int bindingVariable, int layoutId) {
        this.bindingVariable = bindingVariable;
        this.layoutId = layoutId;
    }

    public int getLayoutRes(T model) {
        return layoutId;
    }

    public int getBindingVariable(T model) {
        return bindingVariable;
    }
}
