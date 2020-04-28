package pam.bookshelf;

import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.os.Handler;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.protocol.HTTP;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import static pam.bookshelf.MainActivity.adapter;
import static pam.bookshelf.MainActivity.booksArray;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link BookDetailsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link BookDetailsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BookDetailsFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
Book book;
    private String mParam1;
    private String mParam2;
//    ArrayList<String> arrayListImageUrL;
//    ArrayList<String> arrayListId;
//    ArrayList<String> arrayListAuthorName;
//    ArrayList<String> arrayListDuration;
//    ArrayList<String> arrayListBookTitle;
    ProgressBar progressBar;
ImageView btnPlay,btnBack,btnNext,btnPause,btnStop;
    private Handler hdlr = new Handler();

int Position;
    int count=0;
    public static    TextView mTextViewStartTime,mTextViewStopTime;
    String i="1";
    private OnFragmentInteractionListener mListener;
    private static final String TAG = MainActivity.class.getSimpleName();
    // Views
    Button btn;
    public static   SeekBar seekBar;
    List<Book> arrayList;
    LinearLayout linearLayout1,linearLayout2;
AudiobookService.BookProgress bookProgress=new AudiobookService.BookProgress();

    MediaPlayer mPlayer;
    public static int oTime =0, sTime =0, eTime =0, fTime = 5000, bTime = 1000;
    AudiobookService.MediaControlBinder   audioServiceBinder;
    private Button mNowPlayingButton;
    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            // Cast and assign background service's onBind method returned iBander object.
            audioServiceBinder = (AudiobookService.MediaControlBinder) iBinder;
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {

        }
    };

    public BookDetailsFragment() {
    }

    public static BookDetailsFragment newInstance(String param1, String param2) {
        BookDetailsFragment fragment = new BookDetailsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }
public static TextView txtAuthorTitleName,txtAuthor;
    public static  ImageView image_book;
    Context context;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_book_details, container, false);
        txtAuthor=view.findViewById(R.id.txtAuthor);
        image_book=view.findViewById(R.id.image_book);
        txtAuthorTitleName=view.findViewById(R.id.txtAuthorTitleName);
        seekBar=view.findViewById(R.id.seekbar);
        progressBar=view.findViewById(R.id.simpleProgressBar);
        linearLayout1=view.findViewById(R.id.linearLayout);
        linearLayout2=view.findViewById(R.id.linearLayout2);
        Position=Integer.valueOf(BookListFragment.book_id)-1;
        mTextViewStartTime=view.findViewById(R.id.txtStartTime);
        mTextViewStopTime=view.findViewById(R.id.txtSongTime);
        btnPlay=view.findViewById(R.id.imgplay) ;
        btnBack=view.findViewById(R.id.imgback);
        btnPause=view.findViewById(R.id.imgpause);
        btnStop=view.findViewById(R.id.imgStop);
        context=getActivity();
        mPlayer = new MediaPlayer();
        btnNext=view.findViewById(R.id.imgprevious);
        bindAudioService();
linearLayout1.setVisibility(View.INVISIBLE);
linearLayout2.setVisibility(View.INVISIBLE);
        arrayList=new ArrayList<>();
//        arrayListAuthorName=new ArrayList<>();
//        arrayListBookTitle=new ArrayList<>();
//        arrayListImageUrL=new ArrayList<>();
//        arrayListId=new ArrayList<>();
//        arrayListDuration=new ArrayList<>();
//        new SendRequest().execute();
        playMusic();

        btnStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "Stop", Toast.LENGTH_SHORT).show();
                audioServiceBinder.stop();
                btnPlay.setBackgroundColor(Color.GRAY);
            }
        });
        btnPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "Pause", Toast.LENGTH_SHORT).show();
                if(audioServiceBinder.isPlaying()==true){
                    btnPause.setBackgroundColor(Color.GREEN);
                    audioServiceBinder.pause();}
                else {
                    audioServiceBinder.pause();
                    btnPause.setBackgroundColor(Color.GRAY);
                }
            }
        });


        btnPlay.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {

//                 eTime = Integer.valueOf(arrayListDuration.get(Position))*1000;
                 eTime = Integer.valueOf(BookListFragment.duration)*1000;
            Toast.makeText(context, "Play", Toast.LENGTH_SHORT).show();


              seekBar.setMax(eTime);
            seekBar.setProgress(sTime);

            audioServiceBinder.play(Integer.valueOf(BookListFragment.book_id));
            mTextViewStartTime.setText(String.format("%d min, %d sec", TimeUnit.MILLISECONDS.toMinutes(eTime),
                    TimeUnit.MILLISECONDS.toSeconds(eTime) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS. toMinutes(eTime))) );
            mTextViewStopTime.setText(String.format("%d min, %d sec", TimeUnit.MILLISECONDS.toMinutes(sTime),
                    TimeUnit.MILLISECONDS.toSeconds(sTime) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS. toMinutes(sTime))) );
          //  seekBar.setMax(sTime);
         // seekBar.setProgress(eTime);

           // audioServiceBinder.seekTo(10);
          //  audioServiceBinder.setProgressHandler(hdlr);

            hdlr.postDelayed(UpdateSongTime, 500);
            btnPlay.setBackgroundColor(Color.GREEN);

            audioServiceBinder.setProgressHandler(hdlr);
     }
        });
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                sTime=seekBar.getProgress();
//                audioServiceBinder.mediaPlayer(sTime);
                Toast.makeText(context, "sTime "+sTime, Toast.LENGTH_SHORT).show();
//
//                if(audioServiceBinder.isPlaying()){
//                    progressBar.setVisibility(View.VISIBLE);
//                }
//                else {
//                    progressBar.setVisibility(View.GONE);
//                }



                eTime = Integer.valueOf(BookListFragment.duration)*1000;
                Toast.makeText(context, "Play", Toast.LENGTH_SHORT).show();


                seekBar.setMax(eTime);
                sTime=12000;
                seekBar.setProgress(sTime);
                audioServiceBinder.play(Integer.valueOf(BookListFragment.book_id));
                mTextViewStartTime.setText(String.format("%d min, %d sec", TimeUnit.MILLISECONDS.toMinutes(eTime),
                        TimeUnit.MILLISECONDS.toSeconds(eTime) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS. toMinutes(eTime))) );
                mTextViewStopTime.setText(String.format("%d min, %d sec", TimeUnit.MILLISECONDS.toMinutes(sTime),
                        TimeUnit.MILLISECONDS.toSeconds(sTime) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS. toMinutes(sTime))) );
                //  seekBar.setMax(sTime);
                // seekBar.setProgress(eTime);

                // audioServiceBinder.seekTo(10);
                //  audioServiceBinder.setProgressHandler(hdlr);

                hdlr.postDelayed(UpdateSongTime, 500);
                btnPlay.setBackgroundColor(Color.GREEN);

                audioServiceBinder.setProgressHandler(hdlr);



            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            //    audioServiceBinder.seekTo(seekBar.getProgress());


            }
        });


         count=Position;
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sTime=audioServiceBinder.getCurrentAudioPosition();
                if((sTime + fTime) <= eTime)
                {
                    sTime = sTime - 5000;
                    seekBar.setProgress(sTime);
                    audioServiceBinder.mediaPlayer(sTime);
                }
                else
                {
                // Toast.makeText(getApplicationContext(), "Cannot jump forward 5 seconds", Toast.LENGTH_SHORT).show();
                }
                if(!btnPlay.isEnabled()){
                    btnPlay.setEnabled(true);
                }

            }
        });
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
sTime=audioServiceBinder.getCurrentAudioPosition();
                if((sTime + fTime) <= eTime)
                {

                    sTime = sTime + 5000;
                    seekBar.setProgress(sTime);
                    audioServiceBinder.mediaPlayer(sTime);

                }
                else
                {
                   // Toast.makeText(getApplicationContext(), "Cannot jump forward 5 seconds", Toast.LENGTH_SHORT).show();
                }
                if(!btnPlay.isEnabled()){
                    btnPlay.setEnabled(true);
                }


            }
        });




        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
    private void bindAudioService()
    {
        if(audioServiceBinder == null) {
            Intent intent = new Intent(context, AudiobookService.class);

            // Below code will invoke serviceConnection's onServiceConnected method.
           getActivity(). bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
        }
    }
    private void unBoundAudioService()
    {
        if(audioServiceBinder != null) {
           context. unbindService(serviceConnection);
        }
    }

    @Override
    public void onDestroy() {

        unBoundAudioService();
        super.onDestroy();
    }
    public class SendRequest extends AsyncTask<String, Void, String> {


        ProgressDialog progressDialog=new ProgressDialog(context);


        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
        protected void onPreExecute() {

        }protected String doInBackground(String... arg0) {
            try {
                HttpClient client = new DefaultHttpClient();
                HttpConnectionParams.setConnectionTimeout(client.getParams(), 20000); //Timeout Limit
                HttpResponse response;
                try {


//                    HttpGet post = new HttpGet("https://script.google.com/macros/s/AKfycbx-7xFa0tpDYiY9aNM4B2q09XJrbRQk-4J8GuFGDFg8gLp3EVpC/dev");
                    HttpGet post;


                    post = new HttpGet("https://kamorris.com/lab/abp/booksearch.php");



                    post.setHeader(HTTP.CONTENT_TYPE, "application/json");

                    response = client.execute(post);
                    int responseCodeDeal=response.getStatusLine().getStatusCode();
                    Log.e("responseCodeDeal", String.valueOf(responseCodeDeal));

//                        Config.Toast(context,"responseCodeMerchantPin :"+responseCodeDeal);
                    /*Checking response */
                    InputStream inpts=null;
                    if(response!=null){
                        inpts = response.getEntity().getContent(); //Get the data in the entity
                    }

                    BufferedReader br = new BufferedReader(new InputStreamReader(inpts));
                    StringBuilder sb = new StringBuilder();

                    String line;
                    while ((line = br.readLine()) != null) {
                        sb.append(line + "\n");
                    }
                    inpts.close();
                    String  result = sb.toString();
                    Log.e("json ",result);


                    if (responseCodeDeal==200) {
                        JSONArray json = new JSONArray(result);
// ...

                        for (int i = 0; i < json.length(); i++) {
                            JSONObject e = json.getJSONObject(i);


//                      arrayListAuthorName.add(      e.getString("author"));
//                      arrayListId.add(e.getString("book_id"));
//                      arrayListBookTitle.add(e.getString("title"));
//                            arrayListImageUrL.add(e.getString("cover_url"));
//                            arrayListDuration.add(e.getString("duration"));
                        }
                    }

                } catch(JSONException e) {
                    e.printStackTrace();
                }

            }catch (Exception e){
              Log.e("message",e.toString());
            }

            return null;
        }

        @Override
        protected void onPostExecute(String result) {
//            Log.e("Arraylist ",arrayListBookTitle.toString());
//            Toast.makeText(getActivity(), ""+BookListFragment.book_id, Toast.LENGTH_SHORT).show();
//            txtAuthorTitleName.setText(arrayListBookTitle.get(Position));
//            txtAuthor.setText(arrayListAuthorName.get(Position));
//            Picasso.get()
//                    .load(arrayListImageUrL.get(Position))
//                  .into(image_book);
            linearLayout1.setVisibility(View.VISIBLE);
            linearLayout2.setVisibility(View.VISIBLE);
        }
    }
    private Runnable UpdateSongTime = new Runnable() {
        @Override
        public void run() {
            try {
//                sTime = audioServiceBinder.getCurrentAudioPosition();
                if (sTime == 0) {
                    progressBar.setVisibility(View.VISIBLE);
                } else {
                    progressBar.setVisibility(View.GONE);
                }
                seekBar.setProgress(sTime);

//            Toast.makeText(getActivity(),String.valueOf(audioServiceBinder.getCurrentAudioPosition()),Toast.LENGTH_LONG).show();
                mTextViewStopTime.setText(String.format("%d min, %d sec", TimeUnit.MILLISECONDS.toMinutes(sTime),
                        TimeUnit.MILLISECONDS.toSeconds(sTime) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(sTime))));
                hdlr.postDelayed(this, 500);
            } catch (Exception e) {
            }
        }
    };




    public  void playMusic(){
        Toast.makeText(context, ""+BookListFragment.book_id, Toast.LENGTH_SHORT).show();
//        txtAuthorTitleName.setText(arrayListBookTitle.get(Position));
//        txtAuthor.setText(arrayListAuthorName.get(Position));
//        Picasso.get()
//                .load(arrayListImageUrL.get(Position))
//                .into(image_book);

        txtAuthor.setText(BookListFragment.Author);
        txtAuthorTitleName.setText(BookListFragment.Title);
        Picasso.get()
                .load(BookListFragment.cover_url)
                .into(BookDetailsFragment.image_book);


        eTime = Integer.valueOf(BookListFragment.duration)*1000;


        seekBar.setMax(eTime);
        seekBar.setProgress(sTime);

        mTextViewStartTime.setText(String.format("%d min, %d sec", TimeUnit.MILLISECONDS.toMinutes(eTime),
                TimeUnit.MILLISECONDS.toSeconds(eTime) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS. toMinutes(eTime))) );
        mTextViewStopTime.setText(String.format("%d min, %d sec", TimeUnit.MILLISECONDS.toMinutes(sTime),
                TimeUnit.MILLISECONDS.toSeconds(sTime) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS. toMinutes(sTime))) );

        linearLayout1.setVisibility(View.VISIBLE);
        linearLayout2.setVisibility(View.VISIBLE);
//        audioServiceBinder.play(Integer.valueOf(BookListFragment.book_id));
    }

}
