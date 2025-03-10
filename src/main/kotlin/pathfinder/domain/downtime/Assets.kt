package pathfinder.domain.downtime

import jakarta.persistence.Embeddable

@Embeddable
class Assets {
    var gold = 0.0
    var goods = 0
    var labor = 0
    var influence = 0
    var magic = 0

    fun getCapital(capital: Capital) = when(capital) {
        Capital.GOODS -> goods
        Capital.LABOR -> labor
        Capital.INFLUENCE -> influence
        Capital.MAGIC -> magic
    }

    fun addCapital(capital: Capital, value: Int) = when(capital) {
        Capital.GOODS -> gold += value
        Capital.LABOR -> labor += value
        Capital.INFLUENCE -> influence += value
        Capital.MAGIC -> magic += value
    }

    fun subtractCapital(capital: Capital, value: Int) = when(capital) {
        Capital.GOODS -> gold -= value
        Capital.LABOR -> labor -= value
        Capital.INFLUENCE -> influence -= value
        Capital.MAGIC -> magic -= value
    }
}