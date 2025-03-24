package com.github.linguExplorer

import com.badlogic.gdx.audio.Music
import com.github.linguExplorer.repositories.PhraseAssetRepository
import com.github.linguExplorer.repositories.PhraseRepository


val allPhrasesList = PhraseRepository().getAllPhrases()
val allPhraseAssets = PhraseAssetRepository().getAllPhraseAssets()
val phraseIndex = 0.75
lateinit var music: Music
