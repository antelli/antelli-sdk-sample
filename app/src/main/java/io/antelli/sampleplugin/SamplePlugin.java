package io.antelli.sampleplugin;

import android.content.Context;
import android.os.RemoteException;

import io.antelli.sdk.AntelliPlugin;
import io.antelli.sdk.callback.IAnswerCallback;
import io.antelli.sdk.model.Answer;
import io.antelli.sdk.model.AnswerItem;
import io.antelli.sdk.model.Command;
import io.antelli.sdk.model.Gallery;
import io.antelli.sdk.model.Question;
import io.antelli.sdk.model.Tip;

/**
 * Handcrafted by Štěpán Šonský on 31.10.2017.
 */

public class SamplePlugin extends AntelliPlugin {

    private static final int ACTION_CLICK = 1;
    private static final String PARAM_NAME = "NAME";

    @Override
    protected boolean canAnswer(Question question) throws RemoteException {
        Context langContext = getContext(question.getLanguage());
        return question.containsOne(langContext.getResources().getStringArray(R.array.keywords));
    }

    @Override
    protected void answer(Question question, IAnswerCallback callback) throws RemoteException {

        //Get localized context (you can get strings in question's language from it)
        Context langContext = getContext(question.getLanguage());

        //You can create Answer easily using builder pattern
        Answer answer = new Answer()
                //Add simple conversation item
                .addItem(new AnswerItem()
                        .setTitle(langContext.getString(R.string.item1_title))
                        .setSubtitle(langContext.getString(R.string.item1_subtitle))
                        .setText(langContext.getString(R.string.item1_text))
                        .setSpeech(langContext.getString(R.string.item1_text))
                        .setCommand(new Command(ACTION_CLICK).putString(PARAM_NAME, langContext.getString(R.string.item_click, 1)))
                )
                //Add card item
                .addItem(new AnswerItem()
                        .setTitle(langContext.getString(R.string.item2_title))
                        .setText(langContext.getString(R.string.item2_text))
                        .setImage("http://lorempixel.com/400/200")
                        .setType(AnswerItem.TYPE_CARD)
                        .setCommand(new Command(ACTION_CLICK).putString(PARAM_NAME, langContext.getString(R.string.item_click, 2))))
                //Add card image item
                .addItem(new AnswerItem()
                        .setTitle(langContext.getString(R.string.item3_title))
                        .setText(langContext.getString(R.string.item3_text))
                        .setImage("http://lorempixel.com/400/200")
                        .setCommand(new Command(ACTION_CLICK).putString(PARAM_NAME, langContext.getString(R.string.item_click, 3)))
                        .setType(AnswerItem.TYPE_CARD_IMAGE));

        //Create gallery item
        Gallery gallery = new Gallery();

        //Fill gallery item
        for (int i = 1; i < 6; i++) {
            gallery.addItem(new AnswerItem()
                    .setTitle(langContext.getString(R.string.item4_title, i))
                    .setSubtitle(langContext.getString(R.string.item4_subtitle))
                    .setImage("http://lorempixel.com/400/200")
                    .setCommand(new Command(ACTION_CLICK).putString(PARAM_NAME, langContext.getString(R.string.item_gallery_click, i))));
        }

        //Add gallery item to Answer
        answer.addItem(new AnswerItem()
                .setType(AnswerItem.TYPE_GALLERY)
                .setGallery(gallery));

        //Add tips for user (can be used as question, or can include command)
        String[] keywords = langContext.getResources().getStringArray(R.array.keywords);
        for (String keyword : keywords) {
            answer.addTip(new Tip(keyword));
        }
        answer.addTip(new Tip(langContext.getString(R.string.tip), new Command(ACTION_CLICK).putString(PARAM_NAME, langContext.getString(R.string.tip_click))));

        callback.publish(answer);
    }

    @Override
    protected void command(Command command, IAnswerCallback callback) throws RemoteException {
        //Handle clicks depending on action and params from Command
        switch (command.getAction()) {
            case ACTION_CLICK:
                callback.publish(new Answer(command.getString(PARAM_NAME)));
        }
    }

    @Override
    protected Class getSettingsActivity() {
        return SettingsActivity.class;
    }

    @Override
    protected void reset() {

    }
}
