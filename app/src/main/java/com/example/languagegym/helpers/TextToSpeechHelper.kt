package com.example.languagegym.helpers

import android.content.Context
import android.speech.tts.TextToSpeech
import java.util.Locale

class TextToSpeechHelper(private val context: Context) : TextToSpeech.OnInitListener {

    private var textToSpeech: TextToSpeech? = null

    init {
        textToSpeech = TextToSpeech(context, this)
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            // Language setting (may be user setting)
            val result = textToSpeech?.setLanguage(Locale.getDefault())

            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                // Language not supported, handle this situation as you wish
            }
        } else {
            // TTS initialization failed, handle this situation as you wish
        }
    }

    fun speak(word: String) {
        if (textToSpeech != null && !textToSpeech!!.isSpeaking) {
            val params = HashMap<String, String>()
            params[TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID] = "wordUtteranceId"
            textToSpeech!!.speak(word, TextToSpeech.QUEUE_FLUSH, params)
        }
    }

    fun release() {
        textToSpeech?.stop()
        textToSpeech?.shutdown()
    }
}