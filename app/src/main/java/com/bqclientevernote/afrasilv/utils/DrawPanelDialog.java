package com.bqclientevernote.afrasilv.utils;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.bqclientevernote.afrasilv.casobqclienteevernote.R;

public class DrawPanelDialog extends Dialog {

  protected DrawingView drawingView;
  private Callback callback;

  public DrawPanelDialog(Context context, Callback callback) {
    super(context);
    requestWindowFeature(Window.FEATURE_NO_TITLE);
    setContentView(R.layout.draw_text_view);

    drawingView = (DrawingView) findViewById(R.id.drawing_panel_view);

    TextView tvCancel = (TextView) findViewById(R.id.tv_cancel);
    tvCancel.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        cancel();
      }
    });

    TextView tvOk = (TextView) findViewById(R.id.tv_ok);
    tvOk.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        sendBitmap();
        cancel();
      }
    });


    this.callback = callback;
  }


  private void sendBitmap() {
    if (callback != null) {
      callback.onBitmapCreated(drawingView.getBitmap());
    }
  }

  public interface Callback {

    void onBitmapCreated(Bitmap bitmap);
  }
}