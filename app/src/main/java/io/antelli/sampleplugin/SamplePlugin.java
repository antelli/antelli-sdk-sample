package io.antelli.sampleplugin;

import android.content.Intent;
import android.net.Uri;
import android.os.RemoteException;

import java.util.ArrayList;
import java.util.List;

import io.antelli.sdk.AntelliPlugin;
import io.antelli.sdk.callback.IAnswerCallback;
import io.antelli.sdk.callback.ICanAnswerCallback;
import io.antelli.sdk.model.Answer;
import io.antelli.sdk.model.AnswerItem;
import io.antelli.sdk.model.Command;
import io.antelli.sdk.model.Hint;
import io.antelli.sdk.model.Question;

/**
 * Handcrafted by Štěpán Šonský on 31.10.2017.
 */

public class SamplePlugin extends AntelliPlugin {

    private static final String ACTION_CLICK = "click";
    private static final String PARAM_TEXT = "TEXT";

    @Override
    protected void canAnswer(Question question, ICanAnswerCallback callback) throws RemoteException {
        callback.canAnswer(question.containsOne(getResources().getStringArray(R.array.keywords)));
    }

    @Override
    protected void answer(Question question, IAnswerCallback callback) throws RemoteException {

        //You can create Answer easily using builder pattern
        Answer answer = new Answer()
                //Add simple conversation item
                .addItem(createSampleConversationItem())
                //Add card item
                .addItem(createSampleCardItem())
                //Add card image item
                .addItem(createSampleCardImageItem())
                //Add carousel item
                .addItem(createCarouselItem());

        //Add Hints to Answer
        answer.setHints(createSampleHints());

        //Publish Answer to Antelli
        callback.answer(answer);
    }

    private AnswerItem createSampleConversationItem() {
        return new AnswerItem()
                .setTitle(getString(R.string.item1_title))
                .setSubtitle(getString(R.string.item1_subtitle))
                .setText(getString(R.string.item1_text))
                .setSpeech(getString(R.string.item1_text))
                .setCommand(new Command(ACTION_CLICK).putString(PARAM_TEXT, getString(R.string.item_click, 1)));
    }

    private AnswerItem createSampleCardItem() {
        return new AnswerItem()
                .setTitle(getString(R.string.item2_title))
                .setText(getString(R.string.item2_text))
                .setImage("https://picsum.photos/1280/720?random")
                .setType(AnswerItem.TYPE_CARD)
                .setCommand(new Command(ACTION_CLICK).putString(PARAM_TEXT, getString(R.string.item_click, 2)));
    }

    private AnswerItem createSampleCardImageItem() {
        return new AnswerItem()
                .setTitle(getString(R.string.item3_title))
                .setText(getString(R.string.item3_text))
                .setImage("https://picsum.photos/1280/720?random")
                .setCommand(new Command(ACTION_CLICK).putString(PARAM_TEXT, getString(R.string.item_click, 3)))
                .setType(AnswerItem.TYPE_IMAGE);
    }

    private AnswerItem createCarouselItem() {
        //Create carousel item
        List<AnswerItem> subItems = new ArrayList<>();

        //Fill carousel item
        for (int i = 1; i < 6; i++) {
            subItems.add(new AnswerItem()
                    .setTitle(getString(R.string.item4_subitem_title, i))
                    .setSubtitle(getString(R.string.item4_subitem_subtitle))
                    .setImage("https://picsum.photos/1280/720?random")
                    .setCommand(new Command(ACTION_CLICK).putString(PARAM_TEXT, getString(R.string.item_carousel_click, i))));
        }

        return new AnswerItem()
                .setTitle(getString(R.string.item4_title))
                .setText(getString(R.string.item4_text))
                .setType(AnswerItem.TYPE_CAROUSEL_SMALL)
                .setItems(subItems);
    }

    private List<Hint> createSampleHints() {
        List<Hint> Hints = new ArrayList<>();

        //Add Hint with String only - after click will be used as user's question
        Hints.add(new Hint("Hello World"));

        //Add Hint with Command - after click command() method will be executed
        Hints.add(new Hint(getString(R.string.command_hint), new Command(ACTION_CLICK).putString(PARAM_TEXT, getString(R.string.hint_click))));

        //Add Hint with Intent - after click Intent will be executed (only ACTION_VIEW is supported)
        Hints.add(new Hint(getString(R.string.intent_hint), new Command(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.antelli.io")))));

        //Add Hint with Intent - after click Google Maps will be opened
        Hints.add(new Hint(getString(R.string.app_hint), new Command(getPackageManager().getLaunchIntentForPackage("com.google.android.apps.maps"))));
        return Hints;
    }


    @Override
    protected void command(Command command, IAnswerCallback callback) throws RemoteException {
        //Handle clicks depending on action and params from the Command
        switch (command.getAction()) {
            case ACTION_CLICK:
                callback.answer(new Answer(command.getString(PARAM_TEXT)));
        }
    }

    @Override
    protected Class getSettingsActivity() {
        //If your plugin has settings, return activity class here
        return SettingsActivity.class;
    }

    @Override
    protected void reset() {
        //Reset here your state variables (if you have any) to default values
    }
}
