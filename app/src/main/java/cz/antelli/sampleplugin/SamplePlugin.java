package cz.antelli.sampleplugin;

import android.os.RemoteException;

import io.antelli.sdk.AntelliPlugin;
import io.antelli.sdk.callback.IAnswerCallback;
import io.antelli.sdk.model.Answer;
import io.antelli.sdk.model.AnswerItem;
import io.antelli.sdk.model.Command;
import io.antelli.sdk.model.Gallery;
import io.antelli.sdk.model.Question;

/**
 * Handcrafted by Štěpán Šonský on 31.10.2017.
 */

public class SamplePlugin extends AntelliPlugin {

    private static final int ACTION_CLICK = 1;
    private static final String PARAM_NAME = "NAME";

    @Override
    protected boolean canAnswer(Question question) throws RemoteException {
        return question.equals("ahoj světe") || question.equals("android demo");
    }

    @Override
    protected void answer(Question question, IAnswerCallback callback) throws RemoteException {

        //You can create Answer easily using builder pattern
        Answer answer = new Answer()
                .addItem(new AnswerItem()
                        .setTitle("Ahoj světe!")
                        .setSubtitle("Výborně")
                        .setText("Zdá se, že to funguje :)")
                        .setCommand(new Command(ACTION_CLICK).putString(PARAM_NAME, "první"))
                )
                .addItem(new AnswerItem()
                        .setTitle("Karta")
                        .setText("Toto je ukázka položky typu CARD")
                        .setImage("http://lorempixel.com/400/200")
                        .setType(AnswerItem.TYPE_CARD)
                        .setCommand(new Command(ACTION_CLICK).putString(PARAM_NAME, "druhou")))
                .addItem(new AnswerItem()
                        .setTitle("Obrázek")
                        .setText("I takto může vypadat vaše odpověď")
                        .setImage("http://lorempixel.com/400/200")
                        .setCommand(new Command(ACTION_CLICK).putString(PARAM_NAME, "třetí"))
                        .setType(AnswerItem.TYPE_CARD_IMAGE));

        //Create of gallery item
        Gallery gallery = new Gallery();

        for (int i = 1; i < 6; i++) {
            gallery.addItem(new AnswerItem()
                    .setTitle("Item " + i)
                    .setSubtitle("Galerie")
                    .setImage("http://lorempixel.com/400/200")
                    .setCommand(new Command(ACTION_CLICK).putString(PARAM_NAME, i + ". položku v galerii")));
        }

        //Add gallery item to Answer
        answer.addItem(new AnswerItem()
                .setType(AnswerItem.TYPE_GALLERY)
                .setGallery(gallery));

        callback.publish(answer);
    }

    @Override
    protected void command(Command command, IAnswerCallback callback) throws RemoteException {
        switch (command.getAction()) {
            case ACTION_CLICK:
                callback.publish(new Answer("Kliknul jsi na " + command.getString(PARAM_NAME)));
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
