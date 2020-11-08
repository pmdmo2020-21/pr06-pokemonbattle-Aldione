package es.iessaladillo.pedrojoya.intents.ui.selection

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.RadioButton
import androidx.appcompat.app.AppCompatActivity
import es.iessaladillo.pedrojoya.intents.data.local.Database
import es.iessaladillo.pedrojoya.intents.data.local.model.Pokemon
import es.iessaladillo.pedrojoya.intents.databinding.SelectionActivityBinding
import es.iessaladillo.pedrojoya.intents.ui.battle.BattleActivity

class SelectionActivity : AppCompatActivity() {

    companion object {
        const val extra: String = "EXTRA ID"

        fun newIntent(context: Context, pokemonId: Long): Intent {
            return Intent(context, SelectionActivity::class.java)
                .putExtra(extra, pokemonId)
        }
    }

    private lateinit var binding: SelectionActivityBinding
    private lateinit var radioButtons: Array<RadioButton>
    private lateinit var imageViews: Array<ImageView>
    private lateinit var pokemon: Pokemon


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = SelectionActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        dataFromBattle()
        setupFields()
        setupViews()
        setupInitState()
    }

    override fun onBackPressed() {
        resultBattle()
        super.onBackPressed()
    }

    private fun resultBattle() {
        val intent: Intent = BattleActivity.newIntent(this, pokemon.id)
        setResult(RESULT_OK, intent)
    }

    private fun dataFromBattle() {
        checkIntentData()
        val pokemon: Pokemon = getPokemonId(intent.getLongExtra(extra, 0))

        this.pokemon = pokemon
    }

    private fun setupFields() {
        radioButtons = arrayOf(
            binding.selectedFirstPokemonTop,
            binding.selectedSecondPokemonTop,
            binding.selectedFirstPokemonMid,
            binding.selectedSecondPokemonMid,
            binding.selectedFirstPokemonBottom,
            binding.selectedSecondPokemonBottom,
        )

        imageViews = arrayOf(
            binding.imagenFirstPokemonTop,
            binding.imagenSecondPokemonTop,
            binding.imagenFirstPokemonMid,
            binding.imagenSecondPokemonMid,
            binding.imagenFirstPokemonBottom,
            binding.imagenSecondPokemonBottom,
        )
    }

    private fun setupViews() {
        setupTagsOfRadioButtons()
        setupTagsForImageViews()
        setupsRadioButtonsListener()
        setupImageViewsListener()
    }

    private fun setupInitState() {
        var pokemon: Pokemon

        for (radioButton in radioButtons) {
            pokemon = radioButton.tag as Pokemon

            if (this.pokemon == pokemon) {
                radioButton.isChecked = true
                return
            }
        }
    }

    private fun checkIntentData() {
        if (intent == null || !intent.hasExtra(extra)) {
            throw RuntimeException("Error!")
        }
    }

    private fun getPokemonId(pokemonId: Long): Pokemon {

        return Database.getPokemonById(pokemonId)
    }

    private fun setupTagsOfRadioButtons() {
        for (i in radioButtons.indices) {
            radioButtons[i].tag = Database.getPokemonById(i.toLong() + 1)
        }
    }

    private fun setupTagsForImageViews() {
        for (i in imageViews.indices) {
            imageViews[i].tag = radioButtons[i]
        }
    }

    private fun setupsRadioButtonsListener() {
        for (i in radioButtons.indices) {
            radioButtons[i].setOnClickListener { view: View ->
                checkRadioButton(view)
            }
        }
    }

    private fun setupImageViewsListener() {
        for (i in imageViews.indices) {
            imageViews[i].setOnClickListener { view: View ->
                listenOnClickimage(view)
            }
        }
    }

    private fun listenOnClickimage(view: View) {
        val radioButton: RadioButton = view.tag as RadioButton
        radioButton.isChecked = true
        checkRadioButton(radioButton)
    }

    private fun checkRadioButton(view: View) {
        val selectedRadioButton: RadioButton = view as RadioButton

        for (radioButton in radioButtons) {
            if (selectedRadioButton != radioButton) {
                radioButton.isChecked = false
            }
        }
        selectCurrentPokemonFromView(view)
    }

    private fun selectCurrentPokemonFromView(view: View) {
        val pokemon: Pokemon = view.tag as Pokemon

        this.pokemon = pokemon
    }

}