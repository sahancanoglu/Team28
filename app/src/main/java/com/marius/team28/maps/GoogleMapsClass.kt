package com.marius.team28.maps

class GoogleMapsClass {
    var routes = ArrayList<Routes>()
}

class Routes {
    var legs = ArrayList<Legs>()
}

class Legs {
    var steps = ArrayList<Steps>()
}

@Suppress("PropertyName")
class Steps {
    var end_location = Location()
    var start_location = Location()
}

class Location{
    var lat = ""
    var lng = ""
}