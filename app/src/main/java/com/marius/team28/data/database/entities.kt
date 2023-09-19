package com.marius.team28.data.database

import androidx.room.*

@Entity(tableName = "crags")
data class Crag(
    @PrimaryKey @ColumnInfo(name = "crag_name") val name: String,
    @ColumnInfo(name = "description") val description: String,
    @ColumnInfo(name = "responsible") val responsible: String,
    @ColumnInfo(name = "latitude") val latitude: Double,
    @ColumnInfo(name = "longitude") val longitude: Double
)

@Entity(tableName = "routes")
data class Route(
    @PrimaryKey @ColumnInfo(name = "route_name") val name: String,
    val grade: String?,
    @ColumnInfo(name = "crag_name") val cragName: String
)

@Entity
data class CragWithRoutes(
    @Embedded val parentCrag: Crag,
    @Relation(
        parentColumn = "crag_name",
        entityColumn = "crag_name"
    )
    val routes: List<Route>
)