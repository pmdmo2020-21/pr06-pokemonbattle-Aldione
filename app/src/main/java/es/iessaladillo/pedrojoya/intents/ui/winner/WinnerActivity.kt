package es.iessaladillo.pedrojoya.intents.ui.winner

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import es.iessaladillo.pedrojoya.intents.data.local.Database
import es.iessaladillo.pedrojoya.intents.data.local.model.Pokemon
import es.iessaladillo.pedrojoya.intents.databinding.WinnerActivityBinding

class WinnerActivity : AppCompatActivity() {

    companion object {
        const val extra: String = "EXTRA ID"

        fun newIntent(context: Context, pokemonId: Long): Intent {
            return Intent(context, WinnerActivity::class.java)
                .putExtra(extra, pokemonId)
        }
    }

    private lateinit var binding: WinnerActivityBinding
    private lateinit var pokemon: Pokemon

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = WinnerActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        dataFromBattle()
        setupViews()
    }

    private fun dataFromBattle() {
        checkData()
        pokemon = Database.getPokemonById(intent.getLongExtra(extra, 0))
    }

    private fun checkData() {
        if(intent == null || !intent.hasExtra(extra)) {
            throw RuntimeException("Error!")
        }
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun setupViews() {
        binding.pokemonWin.setImageDrawable(getDrawable(pokemon.drawable))
        binding.nameWinner.text = getString(pokemon.name)
    }

}