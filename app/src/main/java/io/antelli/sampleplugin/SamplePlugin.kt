package io.antelli.sampleplugin

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.RemoteException

import java.util.ArrayList

import io.antelli.sdk.AntelliPlugin
import io.antelli.sdk.callback.IAnswerCallback
import io.antelli.sdk.callback.ICanAnswerCallback
import io.antelli.sdk.model.Answer
import io.antelli.sdk.model.AnswerItem
import io.antelli.sdk.model.Command
import io.antelli.sdk.model.Hint
import io.antelli.sdk.model.Question

/**
 * Handcrafted by Štěpán Šonský on 31.10.2017.
 */

class SamplePlugin : AntelliPlugin() {

    companion object {
        private val ACTION_CLICK = "click"
        private val PARAM_TEXT = "TEXT"
    }

    @Throws(RemoteException::class)
    override fun canAnswer(question: Question, callback: ICanAnswerCallback) {
        callback.canAnswer(question.containsOne(*resources.getStringArray(R.array.keywords)))
    }

    @Throws(RemoteException::class)
    override fun answer(question: Question, callback: IAnswerCallback) {

        //You can create Answer easily using builder pattern
        val answer = Answer()
                //Add simple conversation item
                .addItem(createSampleConversationItem())
                //Add card item
                .addItem(createSampleCardItem())
                //Add card image item
                .addItem(createSampleCardImageItem())
                //Add carousel item
                .addItem(createCarouselItem())

        //Add Hints to the last AnswerItem
        answer.items[answer.items.size - 1].hints = createSampleHints()

        //Publish Answer to Antelli
        callback.answer(answer)
    }

    private fun createSampleConversationItem(): AnswerItem {
        return AnswerItem()
                .setTitle(getString(R.string.item1_title))
                .setSubtitle(getString(R.string.item1_subtitle))
                .setText(getString(R.string.item1_text))
                .setSpeech(getString(R.string.item1_text))
                .setCommand(Command(ACTION_CLICK).putString(PARAM_TEXT, getString(R.string.item_click, 1)))
    }

    private fun createSampleCardItem(): AnswerItem {
        return AnswerItem()
                .setTitle(getString(R.string.item2_title))
                .setText(getString(R.string.item2_text))
                .setImage("https://picsum.photos/1280/720?random")
                .setType(AnswerItem.TYPE_CARD)
                .setCommand(Command(ACTION_CLICK).putString(PARAM_TEXT, getString(R.string.item_click, 2)))
    }

    private fun createSampleCardImageItem(): AnswerItem {
        return AnswerItem()
                .setTitle(getString(R.string.item3_title))
                .setText(getString(R.string.item3_text))
                .setImage("https://picsum.photos/1280/720?random")
                .setCommand(Command(ACTION_CLICK).putString(PARAM_TEXT, getString(R.string.item_click, 3)))
                .setType(AnswerItem.TYPE_IMAGE)
    }

    private fun createCarouselItem(): AnswerItem {
        //Create carousel item
        val subItems = ArrayList<AnswerItem>()

        //Fill carousel item
        for (i in 1..5) {
            subItems.add(AnswerItem()
                    .setTitle(getString(R.string.item4_subitem_title, i))
                    .setSubtitle(getString(R.string.item4_subitem_subtitle))
                    .setImage("https://picsum.photos/1280/720?random")
                    .setCommand(Command(ACTION_CLICK).putString(PARAM_TEXT, getString(R.string.item_carousel_click, i))))
        }

        return AnswerItem()
                .setTitle(getString(R.string.item4_title))
                .setText(getString(R.string.item4_text))
                .setType(AnswerItem.TYPE_CAROUSEL_SMALL)
                .setItems(subItems)
    }

    private fun createSampleHints(): List<Hint> {
        val Hints = ArrayList<Hint>()

        //Add Hint with String only - after click will be used as user's question
        Hints.add(Hint("Hello World"))

        //Add Hint with Command - after click command() method will be executed
        Hints.add(Hint(getString(R.string.command_hint), Command(ACTION_CLICK).putString(PARAM_TEXT, getString(R.string.hint_click))))

        //Add Hint with Intent - after click Intent will be executed (only ACTION_VIEW is supported)
        Hints.add(Hint(getString(R.string.intent_hint), Command(Intent(Intent.ACTION_VIEW, Uri.parse("http://www.antelli.io")))))

        //Add Hint with Intent - after click Google Maps will be opened
        Hints.add(Hint(getString(R.string.app_hint), Command(packageManager.getLaunchIntentForPackage("com.google.android.apps.maps"))))
        return Hints
    }


    @Throws(RemoteException::class)
    override fun command(command: Command, callback: IAnswerCallback) {
        //Handle clicks depending on action and params from the Command
        when (command.action) {
            ACTION_CLICK -> callback.answer(Answer(command.getString(PARAM_TEXT)))
        }
    }

    override fun getSettingsActivity(): Class<out Activity>? {
        return SettingsActivity::class.java
    }

    override fun reset() {
        //Reset here your state variables (if you have any) to default values
    }
}
