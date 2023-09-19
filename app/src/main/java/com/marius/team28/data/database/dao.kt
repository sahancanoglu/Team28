package com.marius.team28.data.database

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction


@Dao
interface AppDao{

    @Transaction
    @Query("SELECT * FROM crags WHERE crag_name = :cragName ")
    fun getCragWithRoutes(cragName: String): List<CragWithRoutes>

    @Query("SELECT * FROM routes WHERE crag_name = :cragName ")
    fun getRoutesFromCrag(cragName: String): List<Route>

    @Query("SELECT * FROM routes WHERE route_name = :routeName")
    fun getRoute(routeName: String): Route

    @Query("SELECT * FROM crags")
    suspend fun getCrags(): List<Crag>

}