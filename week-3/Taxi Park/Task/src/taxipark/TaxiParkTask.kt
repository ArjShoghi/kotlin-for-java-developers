package taxipark

import kotlin.math.floor

/*
 * Task #1. Find all the drivers who performed no trips.
 */
fun TaxiPark.findFakeDrivers(): Set<Driver> =
    allDrivers.filter { driver ->
        driver !in trips.map { trip ->
            trip.driver
        }
    }.toSet()


/*
 * Task #2. Find all the clients who completed at least the given number of trips.
 */
fun TaxiPark.findFaithfulPassengers(minTrips: Int): Set<Passenger> =
    allPassengers.filter { passenger ->
        trips.filter { trip -> passenger in trip.passengers }
            .count() >= minTrips
    }.toSet()


/*
 * Task #3. Find all the passengers, who were taken by a given driver more than once.
 */
fun TaxiPark.findFrequentPassengers(driver: Driver): Set<Passenger> =
    allPassengers.filter { passenger ->
        trips.filter { trip -> passenger in trip.passengers && driver == trip.driver }
            .count() > 1
    }.toSet()

/*
 * Task #4. Find the passengers who had a discount for majority of their trips.
 */
fun TaxiPark.findSmartPassengers(): Set<Passenger> =
    allPassengers.filter { passenger ->
        trips.filter { trip -> passenger in trip.passengers && trip.discount != null }.count() >
                trips.filter { trip -> passenger in trip.passengers && trip.discount == null }.count()

    }.toSet()

/*
 * Task #5. Find the most frequent trip duration among minute periods 0..9, 10..19, 20..29, and so on.
 * Return any period if many are the most frequent, return `null` if there're no trips.
 */
fun TaxiPark.findTheMostFrequentTripDurationPeriod(): IntRange? {
    if (trips.isEmpty()) {
        return null
    }
    var maxDuration = trips.map { trip -> trip.duration }.max() ?: 0


    var map: HashMap<IntRange, Int> = HashMap()

    for (i in 0..maxDuration step 10) {
        map[IntRange(i, i + 9)] = 0
    }


    trips.stream().forEach { trip ->
        var lower = trip.duration - (trip.duration % 10)
        var upper = lower + 9
        var range = IntRange(lower, upper)
        map[range] = map.getOrDefault(range, 0) + 1
    }

    return map?.maxBy { it.value }?.key
}

/*
 * Task #6.
 * Check whether 20% of the drivers contribute 80% of the income.
 */
fun TaxiPark.checkParetoPrinciple(): Boolean {
    if (trips.isEmpty()) return false

    var totalCost = trips.map { trip -> trip.cost }.sum()
    var eightyCost = totalCost * 0.8

    var twentyDriver = floor(allDrivers.size * 0.2)

    var map: HashMap<Driver, Double> = HashMap()

    allDrivers.stream().forEach { driver ->
        map[driver] = trips.filter { trip ->
            trip.driver == driver
        }.map { trip ->
            trip.cost
        }.sum()
    }

    var sortedDriverList =
        map.entries.sortedByDescending { it.value }.map { it.key }.toList().subList(0, twentyDriver.toInt())
    var sum = map.entries.filter { it.key in sortedDriverList }.map { it.value }.sum()

    return sum >= eightyCost

}