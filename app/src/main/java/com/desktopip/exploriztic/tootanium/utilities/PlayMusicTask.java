package com.desktopip.exploriztic.tootanium.utilities;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.desktopip.exploriztic.tootanium.R;
import com.desktopip.exploriztic.tootanium.interfaces.IFileExplore;
import com.desktopip.exploriztic.tootanium.volley.FileExplorerServices;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

//import dyanamitechetan.vusikview.VusikView;

/**
 * Created by Jayus on 06/08/2018.
 */

public class PlayMusicTask{

    private Context context;
    private String urlDownload;
    private String fileName;
    private String uid;
    private String userName;
    private String password;
    private MediaPlayer mediaPlayer = null;

    private int mediaFileLength;
    private int seekForwardTime = 5000; // 5000 milliseconds
    private int seekBackwardTime = 5000; // 5000 milliseconds
    private MediaPlayerUtils utils;

    private TextView title_music, txtCurrentTime, txtAudioLength, songCurrentDurationLabel, songTotalDurationLabel;
    private SeekBar seekbar;
    private Button btn_play_close, btn_play_download;
    private ImageButton btn_play_pause, btn_mute, btn_backward, btn_forward;
    //private VusikView musikView;

    // Session manager
    SessionManager session;
    // Download manager
    DownloadTask downloadTask;
    // Alert manager
    CustomAlertDialogManager alert;

    Dialog customDialog;

    final Handler handler = new Handler();

    public  PlayMusicTask(final Context context){}

    public PlayMusicTask(final Context context, final String urlDownload, final String fileName, final String path, String titleName) {
        this.context = context;
        this.urlDownload = urlDownload;
        this.fileName = fileName;

        utils = new MediaPlayerUtils();

        mediaPlayer = new MediaPlayer();
        mediaPlayer.setOnBufferingUpdateListener(new MediaPlayer.OnBufferingUpdateListener() {
            @Override
            public void onBufferingUpdate(MediaPlayer mediaPlayer, int percent) {
                seekbar.setSecondaryProgress(percent);
            }
        });
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                btn_play_pause.setImageResource(R.drawable.ic_play);
                //musikView.stopNotesFall();
            }
        });

        session = new SessionManager(context);

        HashMap<String, String> user = session.getUserDetails();
        uid = user.get(SessionManager.KEY_UID);
        userName = user.get(SessionManager.KEY_USERNAME);
        password = user.get(SessionManager.KEY_PASSWORD);

        customDialog = new Dialog(context);
        customDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        customDialog.setContentView(R.layout.dip_media_player);

        btn_play_close = customDialog.findViewById(R.id.btn_play_close);
        btn_play_download = customDialog.findViewById(R.id.btn_play_download);
        btn_play_pause = customDialog.findViewById(R.id.btn_play_pause);
        btn_backward = customDialog.findViewById(R.id.btn_backward);
        btn_forward = customDialog.findViewById(R.id.btn_forward);
        //btn_mute = customDialog.findViewById(R.id.btn_mute);
        title_music = customDialog.findViewById(R.id.title_music);
        songCurrentDurationLabel = customDialog.findViewById(R.id.songCurrentDurationLabel);
        songTotalDurationLabel = customDialog.findViewById(R.id.songTotalDurationLabel);
        //musikView = customDialog.findViewById(R.id.musicView);
        seekbar = customDialog.findViewById(R.id.seekbar);

        customDialog.setCancelable(false);
        customDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        customDialog.show();

        seekbar.setMax(99);
        seekbar.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (mediaPlayer.isPlaying()) {
                    SeekBar seekbar = (SeekBar) view;
                    int playPosition = (mediaFileLength / 100) * seekbar.getProgress();
                    mediaPlayer.seekTo(playPosition);
                }
                return false;
            }
        });

//        btn_mute.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                boolean mute;
//                if (!mediaPlayer.isPlaying()) {
//                    mute = false;
//                    if(!mute){
//                        btn_mute.setImageResource(R.drawable.ic_volume_up);
//                        mediaPlayer.setVolume(1,1);
//                    }
//                    else {
//                        btn_mute.setImageResource(R.drawable.ic_volume_off);
//                        mediaPlayer.setVolume(0,0);
//                    }
//                }
//            }
//        });

        btn_play_pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new PlayingMusic().execute(urlDownload + fileName);
            }
        });

        btn_forward.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // get current song position
                int currentPosition = mediaPlayer.getCurrentPosition();
                // check if seekForward time is lesser than song duration
                if(currentPosition + seekForwardTime <= mediaPlayer.getDuration()){
                    // forward song
                    mediaPlayer.seekTo(currentPosition + seekForwardTime);
                }else{
                    // forward to end position
                    mediaPlayer.seekTo(mediaPlayer.getDuration());
                }
            }
        });

        btn_backward.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // get current song position
                int currentPosition = mediaPlayer.getCurrentPosition();
                // check if seekBackward time is greater than 0 sec
                if(currentPosition - seekBackwardTime >= 0){
                    // forward song
                    mediaPlayer.seekTo(currentPosition - seekBackwardTime);
                }else{
                    // backward to starting position
                    mediaPlayer.seekTo(0);
                }

            }
        });

        btn_play_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FileExplorerServices.clearTempMusicPlayer(new IFileExplore() {
                    @Override
                    public void onSuccess(JSONObject result) {
                        //Log.d("clear", result.toString());
                    }

                    @Override
                    public void onError(VolleyError error) {

                    }
                }, context, "clearTempAudioPlay", userName, fileName);

                mediaPlayer.stop();
                customDialog.cancel();
            }
        });

        btn_play_download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FileExplorerServices.download(new IFileExplore() {
                    @Override
                    public void onSuccess(JSONObject result) {
                        try {
                            String file = result.getString("message");
                            downloadTask = new DownloadTask(context, urlDownload + file);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(VolleyError error) {
                        if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                            alert.showAlertDialog(context, "Network Error", "Request Time-Out", "Close");
                        } else if (error instanceof AuthFailureError) {
                            alert.showAlertDialog(context, "Authentication Error", "Authentication Failed" + error.toString(), "Close");
                        } else if (error instanceof ServerError) {
                            alert.showAlertDialog(context, "Server Error", "Internal Server Error", "Close");
                        } else if (error instanceof NetworkError) {
                            alert.showAlertDialog(context, "Network Error", "Unable To Access The Network" + error.toString(), "Close");
                        } else if (error instanceof ParseError) {
                            alert.showAlertDialog(context, "Parse Error", "There was a problem parsing the package" + error.toString(), "Close");
                        }
                    }
                }, context, "downloadFile", userName, password, path);
            }
        });
        title_music.setText(titleName);
        //musikView.start();
        new PlayingMusic().execute(urlDownload + fileName);
    }

    private class PlayingMusic extends AsyncTask<String, String, String>{

        @Override
        protected String doInBackground(String... params) {
            try {
                mediaPlayer.setDataSource(params[0]);
                mediaPlayer.prepare();
            } catch (Exception e) {

            }
            return "";
        }

        @Override
        protected void onPostExecute(String result) {
            mediaFileLength = mediaPlayer.getDuration();
            //realTimeLength = mediaFileLength;

            if (!mediaPlayer.isPlaying()) {
                mediaPlayer.start();
                btn_play_pause.setImageResource(R.drawable.ic_pause);
            } else {
                mediaPlayer.pause();
                btn_play_pause.setImageResource(R.drawable.ic_play);
            }
            updateSeekBar();
            super.onPostExecute(result);
        }

        private void updateSeekBar() {
            seekbar.setProgress((int) (((float) mediaPlayer.getCurrentPosition() / mediaFileLength) * 100));
            if (mediaPlayer.isPlaying()) {
                Runnable updater = new Runnable() {
                    @Override
                    public void run() {
                        long totalDuration = mediaPlayer.getDuration();
                        long currentDuration = mediaPlayer.getCurrentPosition();
                        updateSeekBar();
                        //realTimeLength-=1000; // 1 second

                        //txtCurrentTime.setText(""+utils.milliSecondsToTimer(currentDuration));
                        //txtAudioLength.setText(" / "+ utils.milliSecondsToTimer(totalDuration));
                        songCurrentDurationLabel.setText(""+utils.milliSecondsToTimer(currentDuration));
                        songTotalDurationLabel.setText(""+ utils.milliSecondsToTimer(totalDuration));

//                        txtTime.setText(String.format("%d:%d", TimeUnit.MILLISECONDS.toMinutes(realTimeLength)
//                                , TimeUnit.MILLISECONDS.toSeconds(realTimeLength) -
//                                        TimeUnit.MILLISECONDS.toSeconds(TimeUnit.MILLISECONDS.toMinutes(realTimeLength))));

                    }
                };
                handler.postDelayed(updater, 1000); // 1 second
            }
        }
    }

}
