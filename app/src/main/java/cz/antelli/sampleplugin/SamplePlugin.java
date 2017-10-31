package cz.antelli.sampleplugin;

import android.os.RemoteException;

import cz.antelli.sdk.AntelliPlugin;
import cz.antelli.sdk.model.Answer;
import cz.antelli.sdk.model.AnswerItem;
import cz.antelli.sdk.model.Question;

/**
 * Handcrafted by Štěpán Šonský on 31.10.2017.
 */

public class SamplePlugin extends AntelliPlugin {

    private static final String COMMAND_CLICK = "COMMAND_CLICK";

    @Override
    protected boolean canAnswer(Question question) throws RemoteException {
        return question.equals("ahoj světe") || question.containsAllWords("hello", "world");
    }

    @Override
    protected Answer answer(Question question) throws RemoteException {
        return new Answer().addItem(new AnswerItem(
                "Ahoj světe!",
                "Výborně" ,
                "Zdá se, že to funguje :)",
                "http://lorempixel.com/400/200",
                "Zdá se, že to funguje",
                COMMAND_CLICK));
    }

    @Override
    protected Answer command(String command) throws RemoteException {
        switch (command){
            case COMMAND_CLICK:
                return new Answer("Klik");
        }
        return null;
    }

    @Override
    protected Class getSettingsActivity() {
        return SettingsActivity.class;
    }

    @Override
    protected void reset() {

    }
}
