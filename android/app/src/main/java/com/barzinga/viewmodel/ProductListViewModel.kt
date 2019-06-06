package com.barzinga.viewmodel

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.graphics.Bitmap
import com.barzinga.model.Product
import com.barzinga.restClient.RepositoryProvider
import com.barzinga.util.forPrediction

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers


/**
 * Created by diego.santos on 03/10/17.
 */
class ProductListViewModel(application: Application) : AndroidViewModel(application) {

    var products = mutableListOf<Product>()

    fun listProducts(listener: ProductsListener) {

        val compositeDisposable: CompositeDisposable = CompositeDisposable()
        val repository = RepositoryProvider.provideProductsRepository()

        compositeDisposable.add(
                repository.listProducts()
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .subscribe ({
                            result ->
                                products = result
                                listener.onProductsListGotten(result)
//                                updateUi(result)
                        }, { error ->
                            error.printStackTrace()
                                listener.onProductsListError()
//                                updateUi(null)
                        })
        )

    }

    fun predict(bitmap: Bitmap, listener: ProductsListener) {
        val compositeDisposable: CompositeDisposable = CompositeDisposable()
        val repository = RepositoryProvider.provideProductsRepository()


        val imageString = bitmap.forPrediction()

        compositeDisposable.add(
            repository.predict(imageString)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe ({
                        result ->
                    val id = result.firstOrNull() ?: ""
                    listener.autoFillSearchWithPrediction(productName(id))
                    print(result)
//                                updateUi(result)
                }, { error ->
                    // TODO: Show error couldn't find product
//                    listener.autoFillSearchWithPrediction("Paço")
                    error.printStackTrace()
//                                updateUi(null)
                })
        )

    }

    fun productName(id: String): String {
        return products.filter { id == it.id }.first().description ?: ""
    }

    interface ProductsListener{
        fun onProductsListGotten(products: ArrayList<Product>)
        fun onProductsListError()
        fun onProductsQuantityChanged()
        fun autoFillSearchWithPrediction(predictionQuery: String)
    }
}