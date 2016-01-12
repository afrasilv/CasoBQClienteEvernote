package com.bqclientevernote.afrasilv.casobqclienteevernote;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.GravityEnum;
import com.afollestad.materialdialogs.MaterialDialog;
import com.bqclientevernote.afrasilv.asyntask.AddNoteAsyntask;
import com.bqclientevernote.afrasilv.asyntask.GetNoteMetada;
import com.bqclientevernote.afrasilv.fragments.NoteFragment;
import com.bqclientevernote.afrasilv.utils.Constants;
import com.bqclientevernote.afrasilv.drawclass.DrawPanelDialog;
import com.bqclientevernote.afrasilv.utils.NoteDateComparator;
import com.bqclientevernote.afrasilv.utils.NoteTitleComparator;
import com.evernote.client.android.EvernoteSession;
import com.evernote.client.android.asyncclient.EvernoteCallback;
import com.evernote.client.android.asyncclient.EvernoteNoteStoreClient;
import com.evernote.edam.notestore.NoteFilter;
import com.evernote.edam.notestore.NoteList;
import com.evernote.edam.type.Note;
import com.evernote.edam.type.Notebook;
import com.googlecode.tesseract.android.TessBaseAPI;

import net.i2p.android.ext.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    EvernoteSession mEvernoteSession;
    NoteFragment noteFragment;
    private static final String CONTENT_TEXT_MASK =
            "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<!DOCTYPE en-note SYSTEM \"http://xml.evernote.com/pub/enml2.dtd\">\n<en-note><div>%s<br clear=\"none\"/></div></en-note>";


    private static final EvernoteSession.EvernoteService EVERNOTE_SERVICE = EvernoteSession.EvernoteService.SANDBOX;
    private static final boolean SUPPORT_APP_LINKED_NOTEBOOKS = false;

    private ArrayList<Note> noteList = new ArrayList<>();
    private int listNotesSize = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        final View addStringNote = findViewById(R.id.add_string_note);


        final View addOCRNote = findViewById(R.id.add_drawing_note);

        FloatingActionButton actionC = new FloatingActionButton(getBaseContext());
        actionC.setTitle("Hide/Show Action above");
        actionC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addStringNote.setVisibility(addStringNote.getVisibility() == View.GONE ? View.VISIBLE : View.GONE);
                addOCRNote.setVisibility(addOCRNote.getVisibility() == View.GONE ? View.VISIBLE : View.GONE);
            }
        });

        addStringNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new MaterialDialog.Builder(MainActivity.this)
                        .title(R.string.new_note)
                        .titleGravity(GravityEnum.CENTER)
                        .customView(R.layout.edit_note_layout, true)
                        .positiveText(R.string.save_changes)
                        .negativeText(R.string.close)
                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                EditText newTitle = (EditText) dialog.findViewById(R.id.edit_title_note);
                                EditText newContent = (EditText) dialog.findViewById(R.id.edit_content_note);

                                Note note = new Note();
                                note.setTitle(newTitle.getText().toString());
                                note.setContent(String.format(CONTENT_TEXT_MASK, newContent.getText().toString()));

                                AddNoteAsyntask addNoteAsyntask = new AddNoteAsyntask(note);
                                addNoteAsyntask.execute();

                                addSize(1);
                                noteList.add(note);
                                noteFragment.updateNotes(noteList);

                                dialog.dismiss();
                            }
                        })
                        .onNegative(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                dialog.dismiss();
                            }
                        })
                        .show();
            }
        });

        addOCRNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DrawPanelDialog(MainActivity.this, new DrawPanelDialog.Callback() {
                    @Override
                    public void onBitmapCreated(final Bitmap bitmap) {
                        String path = provideTesseractLangPath();


                        TessBaseAPI tessBaseAPI = provideTessBaseAPI(path);
                        String recognizedText = "";

                        if (tessBaseAPI != null) {
                            tessBaseAPI.setImage(bitmap);
                            recognizedText = tessBaseAPI.getUTF8Text();

                            tessBaseAPI.end();
                        }


                        MaterialDialog editNote = new MaterialDialog.Builder(MainActivity.this)
                                .title(R.string.new_note)
                                .titleGravity(GravityEnum.CENTER)
                                .customView(R.layout.edit_note_layout, true)
                                .positiveText(R.string.save_changes)
                                .negativeText(R.string.close)
                                .onPositive(new MaterialDialog.SingleButtonCallback() {
                                    @Override
                                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                        EditText newTitle = (EditText) dialog.findViewById(R.id.edit_title_note);
                                        EditText newContent = (EditText) dialog.findViewById(R.id.edit_content_note);

                                        Note note = new Note();
                                        note.setTitle(newTitle.getText().toString());
                                        note.setContent(String.format(CONTENT_TEXT_MASK, newContent.getText().toString()));

                                        AddNoteAsyntask addNoteAsyntask = new AddNoteAsyntask(note);
                                        addNoteAsyntask.execute();

                                        addSize(1);
                                        noteList.add(note);
                                        noteFragment.updateNotes(noteList);

                                        dialog.dismiss();
                                    }
                                })
                                .onNegative(new MaterialDialog.SingleButtonCallback() {
                                    @Override
                                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                        dialog.dismiss();
                                    }
                                })
                                .build();

                        EditText contentText = (EditText) editNote.findViewById(R.id.edit_content_note);

                        contentText.setText(recognizedText);

                        editNote.show();
                    }
                }).show();
            }
        });

        loginToEvernote();
        if (! EvernoteSession.getInstance().isLoggedIn()) {
            EvernoteSession.getInstance().authenticate(this);
            return;
        }

        loadNotes();
    }

    private void loginToEvernote() {
        String consumerKey;
        if ("Your consumer key".equals(Constants.CONSUMER_KEY)) {
            consumerKey = BuildConfig.EVERNOTE_CONSUMER_KEY;
        } else {
            // isn't the default value anymore
            consumerKey = Constants.CONSUMER_KEY;
        }

        String consumerSecret;
        if ("Your consumer secret".equals(Constants.CONSUMER_SECRET)) {
            consumerSecret = BuildConfig.EVERNOTE_CONSUMER_SECRET;
        } else {
            // isn't the default value anymore
            consumerSecret = Constants.CONSUMER_SECRET;
        }

        mEvernoteSession = new EvernoteSession.Builder(this)
                .setEvernoteService(EVERNOTE_SERVICE)
                .setSupportAppLinkedNotebooks(SUPPORT_APP_LINKED_NOTEBOOKS)
                .build(consumerKey, consumerSecret)
                .asSingleton();
    }

    private void loadNotes(){
        EvernoteNoteStoreClient noteStoreClient = mEvernoteSession.getEvernoteClientFactory().getNoteStoreClient();

        final MainActivity mainActivity = this;

        noteStoreClient.listNotebooksAsync(new EvernoteCallback<List<Notebook>>() {
            @Override
            public void onSuccess(List<Notebook> result) {
                for (Notebook notebook : result) {
                    NoteFilter filter = new NoteFilter();
                    filter.setNotebookGuid(notebook.getGuid());

                    final EvernoteNoteStoreClient noteStoreClient = EvernoteSession.getInstance().getEvernoteClientFactory().getNoteStoreClient();

                    noteStoreClient.findNotesAsync(filter, 0, 100, new EvernoteCallback<NoteList>() {
                        @Override
                        public void onSuccess(NoteList noteList) {
                            List<Note> notes = noteList.getNotes();

                            addSize(notes.size());

                            for (Note note : notes) {
                                GetNoteMetada getNoteMetada = new GetNoteMetada(note.getGuid(), mainActivity);
                                getNoteMetada.execute();
                            }

                        }

                        @Override
                        public void onException(Exception exception) {
                            Log.e("ERROR", "Error retrieving notebooks", exception);
                        }

                    });

                }
            }

            @Override
            public void onException(Exception exception) {
                Log.e("ERROR", "Error retrieving notebooks", exception);
            }
        });

    }


    @Override
    public void onActivityResult(int requestCode, final int resultCode, Intent data) {
        switch (requestCode) {
            case EvernoteSession.REQUEST_CODE_LOGIN:
                if (resultCode == Activity.RESULT_OK) {
                    Toast.makeText(MainActivity.this, "Successfully login in", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainActivity.this, "Login Failure", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                super.onActivityResult(requestCode, resultCode, data);
                    break;
            }

        // isLoggedIn() don't change immidiatelly. 1 sec delay is enough to wait it change
        final Handler handler = new Handler();

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if ((resultCode == Activity.RESULT_OK) && ((mEvernoteSession.isLoggedIn()))) {
                    Toast.makeText(MainActivity.this, "Successfully login in", Toast.LENGTH_SHORT).show();
                    loadNotes();
                }
            }
        }, 1500);
    }

    public void addNote(Note note){
        this.noteList.add(note);
        if(this.listNotesSize == this.noteList.size()){
            FragmentManager fmgr = getFragmentManager();
            noteFragment = (NoteFragment) fmgr.findFragmentById(R.id.note_fragment);
            noteFragment.setNotes(noteList);
        }
    }

    public void addSize(int size){
        this.listNotesSize += size;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_notes, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.menu_item_sort_note_title:
                sortNotesByTitle();
                return true;
            case R.id.menu_item_sort_note_date:
                sortNotesByDate();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void sortNotesByTitle() {
        Collections.sort(noteList, new NoteTitleComparator());
        renderNotes();
    }

    public void sortNotesByDate() {
        Collections.sort(noteList, new NoteDateComparator());
        renderNotes();
    }

    public void renderNotes() {
        noteFragment.updateNotes(noteList);
    }


    // ------------------ OCR METHODS ------------------ //
    private String provideTesseractLangPath() {

        String basePath =
                Environment.getExternalStorageDirectory().getAbsolutePath();
        String tessDataFolder = "/tessdata/";
        String languageExtension = ".traineddata";
        String externalLangPath = basePath + tessDataFolder + "spa" + languageExtension;
        File dir = new File(basePath + tessDataFolder);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        if (!(new File(externalLangPath)).exists()) {
            String assetLangPath = tessDataFolder + "spa" + languageExtension;
            try {
                copyFileFromAssetsToExternal(this, assetLangPath, externalLangPath);
            } catch (IOException e) {
                Log.i("ProvideTesseractLang", "Error copying files");
                return "";
            }
        }
        return basePath;
    }


    private TessBaseAPI provideTessBaseAPI(String tesseractLangPath) {
        TessBaseAPI tessBaseAPI = new TessBaseAPI();
        if (!"".equals(tesseractLangPath)) {
            tessBaseAPI.init(tesseractLangPath, "spa");
            return tessBaseAPI;
        } else {
            return null;
        }
    }

    private void copyFileFromAssetsToExternal(Context context, String assetPath, String externalPath)
            throws IOException {
        AssetManager assetManager = context.getAssets();
        InputStream in = assetManager.open(assetPath);
        OutputStream out = new FileOutputStream(externalPath);

        // Transfer bytes from in to out
        byte[] buf = new byte[1024];
        int len;
        while ((len = in.read(buf)) > 0) {
            out.write(buf, 0, len);
        }
        in.close();
        out.close();
    }

}
