package com.marius.team28

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.marius.team28.data.database.AppDatabase
import com.marius.team28.data.database.Crag
import com.marius.team28.data.database.Route
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainViewModel(application: Application): AndroidViewModel(application)  {

    private val db = AppDatabase.getDatabase(application)

    private val crags: MutableLiveData<List<Crag>> by lazy {
        MutableLiveData<List<Crag>>().also {
            fetchCrag()
        }
    }

    private val routes = MutableLiveData<List<Route>>()

    private fun fetchCrag() {
        viewModelScope.launch(Dispatchers.IO) {
            db.cragDao().getCrags().also {
                crags.postValue(it)
            }
        }
    }

    fun getRoutes(): MutableLiveData<List<Route>> {
        return routes
    }

    fun fetchRoutes(crag: String){
        viewModelScope.launch(Dispatchers.IO){
            db.cragDao().getRoutesFromCrag(crag).also{
                routes.postValue(it)
            }
        }
    }

    fun getCrag(): LiveData<List<Crag>>{
        return crags
    }
}