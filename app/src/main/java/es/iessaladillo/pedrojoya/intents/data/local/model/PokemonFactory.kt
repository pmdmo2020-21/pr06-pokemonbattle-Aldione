package es.iessaladillo.pedrojoya.intents.data.local.model

import es.iessaladillo.pedrojoya.intents.R

class PokemonFactory private constructor() {

    companion object {
        fun listPokemon() : List<Pokemon> {
            return listOf(
                Pokemon(1, R.drawable.bulbasur, R.string.pokemonBulbasur, 10),
                Pokemon(2, R.drawable.giratina, R.string.pokemonGiratina, 120),
                Pokemon(3, R.drawable.cubone, R.string.pokemonCubone, 15),
                Pokemon(4, R.drawable.gyarados, R.string.pokemonGyarados, 60),
                Pokemon(5, R.drawable.feebas, R.string.pokemonFeebas, 30),
                Pokemon(6, R.drawable.pikachu, R.string.pokemonPikachu, 200),
            )
        }
    }

}