package es.iessaladillo.pedrojoya.intents.ui.battle

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import es.iessaladillo.pedrojoya.intents.data.local.Database
import es.iessaladillo.pedrojoya.intents.data.local.model.Pokemon
import es.iessaladillo.pedrojoya.intents.databinding.BattleActivityBinding
import es.iessaladillo.pedrojoya.intents.ui.selection.SelectionActivity
import es.iessaladillo.pedrojoya.intents.ui.winner.WinnerActivity

class BattleActivity : AppCompatActivity() {

    companion object {
        const val extra: String = "EXTRA ID"

        fun newIntent(context: Context, pokemonId: Long): Intent {
            return Intent(context, BattleActivity::class.java)
                .putExtra(extra, pokemonId)
        }
    }

    private lateinit var binding: BattleActivityBinding
    private val pokemonIds: LongArray = longArrayOf(0, 0)
    private lateinit var selectFisrtPokemonCall: ActivityResultLauncher<Intent>
    private lateinit var selectSecondPokemonCall: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = BattleActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupFields()
        setupViews()
    }

    private fun setupFields() {
        selectFisrtPokemonCall =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (checkActivityResult(result.resultCode, result.data)) {
                    sendPokemonForSet(
                        result.data, binding.pokemonFirst,
                        binding.nameFirstPokemon, 0
                    )
                }
            }

        selectSecondPokemonCall =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (checkActivityResult(result.resultCode, result.data)) {
                    sendPokemonForSet(
                        result.data, binding.pokemonSecond,
                        binding.nameSecondPokemon, 1
                    )
                }
            }
    }

    private fun sendPokemonForSet(
        data: Intent?,
        ivBattleFirstPokemon: ImageView,
        txtBattleFirstPokemonName: TextView,
        indexOfArray: Int
    ) {
        if (data == null) {
            throw IllegalArgumentException("Error! Cant be null")
        }

        pokemonIds[indexOfArray] = data.getLongExtra(extra, 0)
        selectPokemon(
            ivBattleFirstPokemon,
            txtBattleFirstPokemonName,
            pokemonIds[indexOfArray]
        )
    }

    private fun checkActivityResult(resultCode: Int, data: Intent?): Boolean {
        return resultCode == RESULT_OK && data != null

    }

    private fun setupViews() {
        selectPokemonRandom()
        binding.selectTop.setOnClickListener {
            goToSelecction(pokemonIds[0], selectFisrtPokemonCall)
        }
        binding.selectMiddle.setOnClickListener {
            goToSelecction(pokemonIds[1], selectSecondPokemonCall)
        }
        binding.btnBattle.setOnClickListener {
            val winnerPokemon: Long = pokemonWin()
            goToFight(winnerPokemon)
        }
    }

    private fun pokemonWin(): Long {
        val pokemon1: Pokemon = Database.getPokemonById(pokemonIds[0])
        val pokemon2: Pokemon = Database.getPokemonById(pokemonIds[1])
        val result: Long

        result = if (pokemon1.attack > pokemon2.attack) {
            pokemon1.id
        } else {
            pokemon2.id
        }

        return result
    }

    private fun goToFight(winnerPokemon: Long) {
        val intentForWinnerActivity = WinnerActivity.newIntent(this, winnerPokemon)
        startActivity(intentForWinnerActivity)
    }

    private fun goToSelecction(
        pokemonId: Long,
        activityResultLauncher: ActivityResultLauncher<Intent>
    ) {
        val intentforSelectionActivity: Intent = SelectionActivity.newIntent(this, pokemonId)
        activityResultLauncher.launch(intentforSelectionActivity)
    }

    private fun selectPokemonRandom() {
        choosePokemonRandom(
            binding.pokemonFirst, binding.nameFirstPokemon,
            0
        )
        choosePokemonRandom(
            binding.pokemonSecond, binding.nameSecondPokemon,
            1
        )
    }

    private fun selectPokemon(
        ivBattlePokemon: ImageView,
        txtBattlePokemonName: TextView, pokemonId: Long
    ) {
        val pokemon: Pokemon = Database.getPokemonById(pokemonId)

        imagePokemonBattle(ivBattlePokemon, pokemon.drawable)
        namePokemonBattle(txtBattlePokemonName, pokemon.name)
    }

    private fun choosePokemonRandom(
        imageOfPokemon: ImageView,
        nameOfPokemon: TextView,
        positionInArray: Int
    ) {
        val choosedPokemon: Pokemon = Database.getRandomPokemon()

        imagePokemonBattle(imageOfPokemon, choosedPokemon.drawable)
        namePokemonBattle(nameOfPokemon, choosedPokemon.name)
        pokemonIds[positionInArray] = choosedPokemon.id
    }


    @SuppressLint("UseCompatLoadingForDrawables")
    private fun imagePokemonBattle(imageView: ImageView, drawable: Int) {
        imageView.setImageDrawable(getDrawable(drawable))
    }

    private fun namePokemonBattle(textView: TextView, name: Int) {
        textView.text = getText(name)
    }

}