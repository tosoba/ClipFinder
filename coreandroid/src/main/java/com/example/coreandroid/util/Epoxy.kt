package com.example.coreandroid.util

//import android.os.Handler
//import androidx.fragment.app.Fragment
//import androidx.lifecycle.LiveData
//import androidx.lifecycle.Observer
//import com.airbnb.epoxy.*
//import com.airbnb.epoxy.EpoxyController
//import com.airbnb.epoxy.TypedEpoxyController
//
//open class EpoxyController(
//        val buildModelsCallback: EpoxyController.() -> Unit = {}
//) : AsyncEpoxyController() {
//    override fun buildModels() = buildModelsCallback()
//}
//
//open class TypedEpoxyController<State>(
//        val buildModelsCallback: EpoxyController.(state: State) -> Unit = {}
//) : TypedEpoxyController<State>() {
//    override fun buildModels(data: State) = buildModelsCallback(data)
//}
//
//open class AsyncTypedEpoxyController<State>(
//        diffingHandler: Handler,
//        modelBuildingHandler: Handler,
//        val buildModelsCallback: EpoxyController.(state: State) -> Unit = {}
//) : TypedEpoxyController<State>(modelBuildingHandler, diffingHandler) {
//
//    override fun buildModels(data: State) {
//        buildModelsCallback(data)
//    }
//}
//
//fun <State> Fragment.typedEpoxyController(
//        liveState: LiveData<State>,
//        buildModels: EpoxyController.(State) -> Unit
//) = TypedEpoxyController<State> {
//    if (view == null || isRemoving) return@TypedEpoxyController
//    liveState.observe(this@typedEpoxyController, Observer { buildModels(it) })
//}
//
//fun Fragment.simpleController(
//        buildModels: EpoxyController.() -> Unit
//) = EpoxyController {
//    // Models are built asynchronously, so it is possible that this is called after the fragment
//    // is detached under certain race conditions.
//    if (view == null || isRemoving) return@EpoxyController
//    buildModels()
//}
//
//fun <State> Fragment.asyncTypedEpoxyController(
//        modelBuildingHandler: Handler,
//        diffingHandler: Handler,
//        liveState: LiveData<State>,
//        buildModels: EpoxyController.(state: State) -> Unit
//) = AsyncTypedEpoxyController<State>(modelBuildingHandler, diffingHandler) {
//    if (view == null || isRemoving) return@AsyncTypedEpoxyController
//    liveState.observe(this@asyncTypedEpoxyController, Observer { buildModels(it) })
//}
//
//inline fun EpoxyController.carousel(modelInitializer: CarouselModelBuilder.() -> Unit) {
//    CarouselModel_().apply { modelInitializer() }.addTo(this)
//}
//
//inline fun <T> CarouselModelBuilder.withModelsFrom(
//        items: List<T>,
//        modelBuilder: (T) -> EpoxyModel<*>
//) {
//    models(items.map { modelBuilder(it) })
//}
//
//inline fun <T, R> CarouselModelBuilder.withModelsFrom(
//        items: Map<T, R>,
//        modelBuilder: (T, R) -> EpoxyModel<*>
//) {
//    models(items.map { (key, value) -> modelBuilder(key, value) })
//}