package com.github.linguExplorer.database

import com.github.linguExplorer.repositories.PhraseAssetRepository
import com.github.linguExplorer.repositories.PhraseRepository

val allPhrasesList = PhraseRepository().getAllPhrases()
val allPhraseAssets = PhraseAssetRepository().getAllPhraseAssets()
val phraseIndex = 0.75
