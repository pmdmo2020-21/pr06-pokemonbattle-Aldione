package es.iessaladillo.pedrojoya.intents.data.local

import es.iessaladillo.pedrojoya.intents.data.local.model.Pokemon
import es.iessaladillo.pedrojoya.intents.data.local.model.PokemonFactory
import kotlin.random.Random


object Database : DataSource{

    private val pokemons: List<Pokemon> = PokemonFactory.listPokemon()
    private val random: Random = Random.Default

    override fun getRandomPokemon(): Pokemon {
        val randomSelectPokemon: Int = random.nextInt(6)

        return pokemons[randomSelectPokemon]
    }

    override fun getAllPokemons(): List<Pokemon> {
        return pokemons
    }

    override fun getPokemonById(id: Long): Pokemon {
        val pokemon = pokemons.filter { pokemon -> pokemon.id == id }

        if(pokemon.isEmpty()) {
            throw IllegalArgumentException("Pokemon invalido")
        }

        return pokemon[0]
    }

}