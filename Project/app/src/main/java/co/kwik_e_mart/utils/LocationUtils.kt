package co.kwik_e_mart.utils

import kotlin.math.*
import kotlin.random.Random

fun generateRandomLocation(latitude: Double, longitude: Double, distanceKm: Double): Pair<Double, Double> {
    val radiusEarthKm = 6371.0

    // Convert distance to radians
    val distanceRad = distanceKm / radiusEarthKm

    // Random bearing (direction) in radians
    val bearing = Random.nextDouble(0.0, 2 * Math.PI)

    // Convert latitude and longitude to radians
    val latRad = Math.toRadians(latitude)
    val lonRad = Math.toRadians(longitude)

    // Calculate the new latitude
    val newLatRad = asin(sin(latRad) * cos(distanceRad) + cos(latRad) * sin(distanceRad) * cos(bearing))

    // Calculate the new longitude
    val newLonRad = lonRad + atan2(sin(bearing) * sin(distanceRad) * cos(latRad), cos(distanceRad) - sin(latRad) * sin(newLatRad))

    // Convert back to degrees
    val newLatitude = Math.toDegrees(newLatRad)
    val newLongitude = Math.toDegrees(newLonRad)

    return Pair(newLatitude, newLongitude)
}
