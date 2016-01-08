package com.bqclientevernote.afrasilv.casobqclienteevernote;

import android.app.Application;

import com.evernote.client.android.EvernoteSession;

import java.util.Locale;

/**
 * Created by alex on 7/01/16.
 */
public class EvernoteInit extends Application {

    private static final String CONSUMER_KEY = "aalex12-8143";
    private static final String CONSUMER_SECRET = "7f51f235db3bfe04";
    private static final EvernoteSession.EvernoteService EVERNOTE_SERVICE = EvernoteSession.EvernoteService.SANDBOX;

    EvernoteSession mEvernoteSession;

    @Override
    public void onCreate() {
        super.onCreate();

        mEvernoteSession = new EvernoteSession.Builder(this)
                .setEvernoteService(EVERNOTE_SERVICE)
                .setSupportAppLinkedNotebooks(true)
                .setLocale(Locale.ENGLISH)
                .build(CONSUMER_KEY, CONSUMER_SECRET)
                .asSingleton();

    }
}
