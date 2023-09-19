package com.marius.team28.api.frost

import com.google.gson.annotations.SerializedName

data class WeatherStation (

    @SerializedName("@context"         ) var context         : String?         = null,
    @SerializedName("@type"            ) var type            : String?         = null,
    @SerializedName("apiVersion"       ) var apiVersion       : String?         = null,
    @SerializedName("license"          ) var license          : String?         = null,
    @SerializedName("createdAt"        ) var createdAt        : String?         = null,
    @SerializedName("queryTime"        ) var queryTime        : Double?         = null,
    @SerializedName("currentItemCount" ) var currentItemCount : Int?            = null,
    @SerializedName("itemsPerPage"     ) var itemsPerPage     : Int?            = null,
    @SerializedName("offset"           ) var offset           : Int?            = null,
    @SerializedName("totalItemCount"   ) var totalItemCount   : Int?            = null,
    @SerializedName("currentLink"      ) var currentLink      : String?         = null,
    @SerializedName("data"             ) var data             : ArrayList<Data> = arrayListOf()

)

data class FrostGeometry (

    @SerializedName("@type"       ) var type       : String?           = null,
    @SerializedName("coordinates" ) var coordinates : ArrayList<Double> = arrayListOf(),
    @SerializedName("nearest"     ) var nearest     : Boolean?          = null

)

data class Data (

    @SerializedName("@type"          ) var type          : String?           = null,
    @SerializedName("id"             ) var id             : String?           = null,
    @SerializedName("name"           ) var name           : String?           = null,
    @SerializedName("shortName"      ) var shortName      : String?           = null,
    @SerializedName("country"        ) var country        : String?           = null,
    @SerializedName("countryCode"    ) var countryCode    : String?           = null,
    @SerializedName("geometry"       ) var geometry       : FrostGeometry?         = FrostGeometry(),
    @SerializedName("distance"       ) var distance       : Double?           = null,
    @SerializedName("masl"           ) var masl           : Int?              = null,
    @SerializedName("validFrom"      ) var validFrom      : String?           = null,
    @SerializedName("county"         ) var county         : String?           = null,
    @SerializedName("countyId"       ) var countyId       : Int?              = null,
    @SerializedName("municipality"   ) var municipality   : String?           = null,
    @SerializedName("municipalityId" ) var municipalityId : Int?              = null,
    @SerializedName("stationHolders" ) var stationHolders : ArrayList<String> = arrayListOf(),
    @SerializedName("externalIds"    ) var externalIds    : ArrayList<String> = arrayListOf(),
    @SerializedName("wigosId"        ) var wigosId        : String?           = null

)
