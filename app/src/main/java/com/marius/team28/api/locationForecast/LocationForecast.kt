package com.marius.team28.api.locationForecast

import com.google.gson.annotations.SerializedName

data class LocationForecast (

    @SerializedName("type"       ) var type       : String?     = null,
    @SerializedName("geometry"   ) var geometry   : Geometry?   = Geometry(),
    @SerializedName("properties" ) var properties : Properties? = Properties()

)

data class Geometry (

    @SerializedName("type"        ) var type        : String?        = null,
    @SerializedName("coordinates" ) var coordinates : ArrayList<Double> = arrayListOf()

)

data class Units (

    @SerializedName("air_pressure_at_sea_level" ) var airPressureAtSeaLevel : String? = null,
    @SerializedName("air_temperature"           ) var airTemperature        : String? = null,
    @SerializedName("cloud_area_fraction"       ) var cloudAreaFraction     : String? = null,
    @SerializedName("precipitation_amount"      ) var precipitationAmount   : String? = null,
    @SerializedName("relative_humidity"         ) var relativeHumidity      : String? = null,
    @SerializedName("wind_from_direction"       ) var windFromDirection     : String? = null,
    @SerializedName("wind_speed"                ) var windSpeed             : String? = null

)

data class Meta (

    @SerializedName("updated_at" ) var updatedAt : String? = null,
    @SerializedName("units"      ) var units     : Units?  = Units()

)

data class Details (

    @SerializedName("air_pressure_at_sea_level" ) var airPressureAtSeaLevel : Double? = null,
    @SerializedName("air_temperature"           ) var airTemperature        : Double? = null,
    @SerializedName("cloud_area_fraction"       ) var cloudAreaFraction     : Double?    = null,
    @SerializedName("relative_humidity"         ) var relativeHumidity      : Double? = null,
    @SerializedName("wind_from_direction"       ) var windFromDirection     : Double? = null,
    @SerializedName("wind_speed"                ) var windSpeed             : Double? = null

)

data class Instant (

    @SerializedName("details" ) var details : Details? = Details()

)

data class Summary (

    @SerializedName("symbol_code" ) var symbolCode : String? = null

)

data class Next12Hours (

    @SerializedName("summary" ) var summary : Summary? = Summary()

)

data class Details2 (

    @SerializedName("precipitation_amount" ) var precipitationAmount : Double? = null

)

data class Next6Hours (

    @SerializedName("summary" ) var summary : Summary? = Summary(),
    @SerializedName("details" ) var details : Details2? = Details2()

)

data class ForecastData (

    @SerializedName("instant"       ) var instant     : Instant?     = Instant(),
    @SerializedName("next_12_hours" ) var next12Hours : Next12Hours? = Next12Hours(),
    @SerializedName("next_1_hours"  ) var next1Hours  : Next1Hours?  = Next1Hours(),
    @SerializedName("next_6_hours"  ) var next6Hours  : Next6Hours?  = Next6Hours()

)

data class Timeseries (

    @SerializedName("time" ) var time : String? = null,
    @SerializedName("data" ) var data : ForecastData?   = ForecastData()

)

data class Properties (

    @SerializedName("meta"       ) var meta       : Meta?                 = Meta(),
    @SerializedName("timeseries" ) var timeseries : ArrayList<Timeseries> = arrayListOf()

)

data class Next1Hours (

    @SerializedName("summary" ) var summary : Summary? = Summary(),
    @SerializedName("details" ) var details : Details2? = Details2()

)
