package com.example.coreandroid.view.epoxy

import com.airbnb.epoxy.EpoxyModel
import com.airbnb.epoxy.EpoxyModelGroup
import com.example.coreandroid.R

class Column(
        models: Collection<EpoxyModel<*>>
) : EpoxyModelGroup(R.layout.column, models) {
    constructor(vararg models: EpoxyModel<*>) : this(models.toList())
}