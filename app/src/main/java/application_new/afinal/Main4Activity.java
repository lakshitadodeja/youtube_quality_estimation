package application_new.afinal;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

public class Main4Activity extends YouTubeBaseActivity implements
        YouTubePlayer.OnInitializedListener{
    long load1= System.currentTimeMillis();
    long load2;
    long start=0;
    long stop =0;
    long fr=0;
    long tot=0;
    public static final String API_KEY = "AIzaSyAZ5ADpV2j0vtOTLcRS_0th2M0J1RwfgsQ";
    //public static final String VIDEO_ID = "Eq4f7GEj58s";

    private YouTubePlayer youTubePlayer;
    private YouTubePlayerView youTubePlayerView;
    private TextView textVideoLog;

    private static final int RQS_ErrorDialog = 1;

    private MyPlayerStateChangeListener myPlayerStateChangeListener;
    private MyPlaybackEventListener myPlaybackEventListener;

    String log = "";
    long st;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main3);

        youTubePlayerView = (YouTubePlayerView)findViewById(R.id.youtube_view);
        youTubePlayerView.initialize(API_KEY, this);

        textVideoLog = (TextView)findViewById(R.id.videolog);

        myPlayerStateChangeListener = new MyPlayerStateChangeListener();
        myPlaybackEventListener = new MyPlaybackEventListener();
    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider,
                                        YouTubeInitializationResult result) {

        if (result.isUserRecoverableError()) {
            result.getErrorDialog(this, RQS_ErrorDialog).show();
        } else {
            Toast.makeText(this,
                    "YouTubePlayer.onInitializationFailure(): " + result.toString(),
                    Toast.LENGTH_LONG).show();
        }
    }
    int flag=0;
    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer player,
                                        boolean wasRestored) {

        youTubePlayer = player;

        Toast.makeText(getApplicationContext(),
                "Let's Start",
                Toast.LENGTH_LONG).show();

        youTubePlayer.setPlayerStateChangeListener(myPlayerStateChangeListener);
        youTubePlayer.setPlaybackEventListener(myPlaybackEventListener);

        if (!wasRestored) {
            Intent i = getIntent();
            // Receiving the Data
            final String VideoId = i.getStringExtra("VideoId");
            //int l=VideoId.length();
            String V="";

            for (String re: VideoId.split("[=/]")){
                V=re;
            }

            player.loadVideo(V);
            
        }

    }

    private final class MyPlayerStateChangeListener implements YouTubePlayer.PlayerStateChangeListener {

        private void updateLog(String prompt){
            log += "\n-" +
                    prompt + "\n";
            textVideoLog.setText(log);
        };

        @Override
        public void onAdStarted() {
            //load2=System.currentTimeMillis();
            //updateLog("Load Time is "  + (load2-load1) + "milliseconds");
            //updateLog("AdStarted");

        }

        @Override
        public void onError(
                com.google.android.youtube.player.YouTubePlayer.ErrorReason arg0) {
            updateLog("Error " + arg0.toString());
            LayoutInflater layoutInflater
                    = (LayoutInflater)getBaseContext()
                    .getSystemService(LAYOUT_INFLATER_SERVICE);
            View popupView = layoutInflater.inflate(R.layout.popup, null);
            final PopupWindow popupWindow = new PopupWindow(
                    popupView,
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);

            Button btnDismiss = (Button)popupView.findViewById(R.id.dismiss);
            TextView tV = (TextView) findViewById(R.id.tV);
            tV.setText("Error " + arg0.toString());
            btnDismiss.setOnClickListener(new Button.OnClickListener(){

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    //popupWindow.dismiss();
                    Intent nextScreen = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(nextScreen);
                }});


        }

        @Override
        public void onLoaded(String arg0) {
            if(flag==0){
            updateLog("Loaded " + arg0);
            load2=System.currentTimeMillis();
            start=System.currentTimeMillis();
            updateLog("Load Time is "  + (load2-load1) + "milliseconds");
            flag=1;}
        }

        @Override
        public void onLoading() {
            updateLog("Loading");
            //start=0;

        }

        @Override
        public void onVideoEnded() {
            updateLog("Video Ended");
            updateLog("Load Time is \n"  + (load2-load1) + "milliseconds");
            updateLog("Frequency of Buffering is " + (fr) );
            updateLog("Total buffering time is " + (tot) + "milliseconds" );
            updateLog("Mean buffering time is " + (tot/fr) + "milliseconds");
            long l =youTubePlayer.getDurationMillis();
            updateLog("Video played for " + ((System.currentTimeMillis())-st) + "Milliseconds");
            updateLog("Duration of video " + (l) + "miliseconds");
            l=load2-load1;
            int Li,Lf,Lb,mean= (int) (tot/fr);
            if(l<=1000)
                Li=1;
            else if(l<=5000)
                Li=2;
            else
                Li=3;

            if(mean<=5000)
                Lb=1;
            else if(mean<=10000)
                Lb=2;
            else
                Lb=3;
            if(fr<=2)
                Lf=1;
            else if(fr<=15)
                Lf=2;
            else
                Lf=3;
            double mos=4.23-0.0672*Li-0.742*Lf-0.106*Lb;
            updateLog("The MOS(Mean Opinion Score) is " + mos);
            load1= System.currentTimeMillis();

        }

        @Override
        public void onVideoStarted() {
            updateLog("VideoStarted");

        }

    }

    private final class MyPlaybackEventListener implements YouTubePlayer.PlaybackEventListener {

        private void updateLog(String prompt){
            log += "\n-" + prompt + "\n";
            textVideoLog.setText(log);
        };

        @Override
        public void onBuffering(boolean arg0) {

            if(arg0)
            {
              //  updateLog("Buffering ");
                start = System.currentTimeMillis();
                fr++;
            }
            else
            {

                stop = System.currentTimeMillis();
                tot+= (stop-start);
                updateLog("Buffer Time is "  + (stop-start) + "milliseconds");

            }
        }

        @Override
        public void onPaused() {
            //updateLog("Paused");
        }

        @Override
        public void onPlaying() {
            //updateLog("Playing");
        }

        @Override
        public void onSeekTo(int arg0) {
            //updateLog("SeekTo " + String.valueOf(arg0));
        }

        @Override
        public void onStopped() {
            //updateLog("Stopped");

        }

    }

}

