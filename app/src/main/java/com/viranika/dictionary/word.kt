package com.viranika.dictionary

class word {

    var englishTranslationId: String
        private set

    var persianTranslationId: String
        private set

    var imageResourceId: Int

    var audioResourceId: Int
        private set


    constructor(englishTranslationId: String, persianTranslationId: String, imageResourceId: Int, audioResourceId: Int) {
        this.englishTranslationId = englishTranslationId
        this.persianTranslationId = persianTranslationId
        this.imageResourceId = imageResourceId
        this.audioResourceId = audioResourceId
    }
}