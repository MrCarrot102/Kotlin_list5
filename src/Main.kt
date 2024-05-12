import java.time.LocalDate
import java.time.Month
import kotlin.random.*
enum class CostType(val costType: String){
    REFUELING("Tankowanie"),
    SERVICE("Serwis"),
    PARKING("Parking"),
    INSURANCE("Ubezpieczenie"),
    TICKET("Mandat")
}
data class Cost(
    val type: CostType,
    val date: LocalDate,
    val amount: Int
)

object DataProvider {
    private fun generalCosts(size:Int) = List(size) {
        Cost(
            CostType
                .values()[Random.nextInt(CostType.values().size)],
            LocalDate.of(
                2022,
                Random.nextInt(1,13),
                Random.nextInt(1,28)),
            Random.nextInt(5000)
            )
    }
    val cars= listOf(
        Car("Domowy", "Skoda","Fabia",2002,generalCosts(100)),
        Car("Służbowy", "BMW", "Coupe", 2015, generalCosts(50)),
        Car("Kolekcjonerski","Fiat", "125p", 1985, generalCosts(10))

    )
}
data class Car(
    val name: String,
    val brand: String,
    val model: String,
    val yearOfProduction: Int,
    val costs:List<Cost>
)

// zadanie 1 segregowanie po miesiacu
fun groupedCostMap(costs:List<Cost>):Map<Month,List<Cost>> {
    return costs.groupBy { it.date.month }
        .toSortedMap()
}
// zadanie 2 sortowanie po miesiacu i wedlug dat
fun printCostsByMonth(costs:List<Cost>){
    val groupedCosts=costs.groupBy {it.date.month}
        .toSortedMap()
    for((month, costsInMonth) in groupedCosts){
        println(month)
        costsInMonth.sortedBy {it.date}.forEach{cost->
            println("${cost.date.dayOfMonth} ${cost.type.costType} ${cost.amount} zł")
        }
    }
}
// zadanie 3
fun getCarCosts(carName:String):List<Pair<CostType, Int >> {
    val car=DataProvider.cars.find{it.name==carName}
        ?:throw IllegalArgumentException("Nie znaleziono samochodu o podanej nazwie")

    val costs=car.costs
    val totalCosts=costs.groupBy { it.type }
        .mapValues{(_, costs)->costs.sumBy{it.amount}}
        .toList()
        .sortedByDescending { it.second }
    return totalCosts
}
fun printCarCosts(costs:List<Pair<CostType, Int>>){
    costs.forEachIndexed{index, (costType, amount) ->
        val comma = if (index<costs.size-1)"," else ""
        println("${costType.costType} $amount zł$comma")
    }
}

// zadanie 4
fun getFullCosts(cars:List<Car>): Map<CostType, Int> {
    val allCosts=cars.flatMap{it.costs}
    return allCosts.groupBy{it.type}
        .mapValues{(_,costs)->costs.sumBy{it.amount}}
}
fun printFullCost(costs:Map<CostType,Int>) {
    costs.forEach{(costType, amount)->
        println("${costType.costType} $amount")
    }
}
fun main() {

    // zadania 1, 2 nie działają po zmianach wprowadzonych w ćwiczeniu 3 do klasy DataProvider :(

    //val costs = DataProvider.DataProvider
    // zadanie 1
    //val groupedCosts=groupedCostMap(costs)
    //println(groupedCosts)
    // zadanie 2
    //printCostsByMonth(costs)
    // zadanie 3
    printCarCosts(getCarCosts("Domowy"))
    // zadanie 4
    printFullCost(getFullCosts(DataProvider.cars))
}