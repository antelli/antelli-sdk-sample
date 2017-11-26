package io.antelli.sampleplugin;

import android.content.Context;
import android.os.RemoteException;

import java.util.ArrayList;
import java.util.List;

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

    private static final String ACTION_CLICK = "click";
    private static final String PARAM_NAME = "NAME";

    Context langContext;

    @Override
    protected boolean canAnswer(Question question) throws RemoteException {
        Context langContext = getContext(question.getLanguage());
        return question.containsOne(langContext.getResources().getStringArray(R.array.keywords));
    }

    @Override
    protected void answer(Question question, IAnswerCallback callback) throws RemoteException {

        //Get localized context for multilanguage plugins (you can get strings in question's language from it)
        langContext = getContext(question.getLanguage());

        //You can create Answer easily using builder pattern
        Answer answer = new Answer()
                //Add simple conversation item
                .addItem(createSampleConversationItem())
                //Add card item
                .addItem(createSampleCardItem())
                //Add card image item
                .addItem(createSampleCardImageItem())
                //Add gallery item
                .addItem(createGalleryItem());

        //Add tips to Answer
        answer.setTips(createSampleTips());

        //Publish Answer to Antelli
        callback.publish(answer);
    }

    private AnswerItem createSampleConversationItem(){
        return new AnswerItem()
                .setTitle(langContext.getString(R.string.item1_title))
                .setSubtitle(langContext.getString(R.string.item1_subtitle))
                .setText(langContext.getString(R.string.item1_text))
                .setSpeech(langContext.getString(R.string.item1_text))
                .setCommand(new Command(ACTION_CLICK).putString(PARAM_NAME, langContext.getString(R.string.item_click, 1)));
    }

    private AnswerItem createSampleCardItem(){
        return new AnswerItem()
                .setTitle(langContext.getString(R.string.item2_title))
                .setText(langContext.getString(R.string.item2_text))
                .setImage("http://lorempixel.com/400/200")
                .setType(AnswerItem.TYPE_CARD)
                .setCommand(new Command(ACTION_CLICK).putString(PARAM_NAME, langContext.getString(R.string.item_click, 2)));
    }

    private AnswerItem createSampleCardImageItem(){
        return new AnswerItem()
                .setTitle(langContext.getString(R.string.item3_title))
                .setText(langContext.getString(R.string.item3_text))
                .setImage("http://lorempixel.com/400/200")
                .setCommand(new Command(ACTION_CLICK).putString(PARAM_NAME, langContext.getString(R.string.item_click, 3)))
                .setType(AnswerItem.TYPE_CARD_IMAGE);
    }

    private AnswerItem createGalleryItem(){
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

        return new AnswerItem()
                .setType(AnswerItem.TYPE_GALLERY)
                .setGallery(gallery);
    }

    private List<Tip> createSampleTips(){
        List<Tip> tips = new ArrayList<>();

        //Add Tips with String only - after click will be used as question
        String[] keywords = langContext.getResources().getStringArray(R.array.keywords);
        for (String keyword : keywords) {
            tips.add(new Tip(keyword));
        }

        //Add Tip with Command - after click will be command() method executed
        tips.add(new Tip(langContext.getString(R.string.tip), new Command(ACTION_CLICK).putString(PARAM_NAME, langContext.getString(R.string.tip_click))));
        return tips;
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
