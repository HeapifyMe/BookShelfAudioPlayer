package pam.bookshelf;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static android.content.Context.MODE_PRIVATE;
import static pam.bookshelf.BookDetailsFragment.eTime;
import static pam.bookshelf.BookDetailsFragment.mTextViewStartTime;
import static pam.bookshelf.BookDetailsFragment.mTextViewStopTime;
import static pam.bookshelf.BookDetailsFragment.sTime;
import static pam.bookshelf.BookDetailsFragment.seekBar;
import static pam.bookshelf.BookDetailsFragment.txtAuthor;
import static pam.bookshelf.BookDetailsFragment.txtAuthorTitleName;
import static pam.bookshelf.BookListFragment.Author;
import static pam.bookshelf.BookListFragment.frame_details;
import static pam.bookshelf.BookListFragment.list;
import static pam.bookshelf.MainActivity.diagonalInches;

public class MyListAdapter extends BaseAdapter {
    private final Activity context;
//    List<HashMap> book=new ArrayList<HashMap>();
    List<Book> book=new ArrayList<Book>();
//    List<Book> book=new ArrayList<Book>();
    String book_id,AuthorName,AuthorBookName,cover_url;
static int Position;

// public MyListAdapter(Activity context, List<HashMap> book) {
//        this.context = context;
//        this.book = book;
//    }

 public MyListAdapter(Activity context, List<Book> book) {
        this.context = context;
        this.book = book;
    }

    @Override
    public int getCount() {
        return book.size();
    }

    @Override
    public Object getItem(int position) {
        return book.size();
    }

    @Override
    public long getItemId(int position) {
        return book.size();
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater layoutInflater=context.getLayoutInflater();
        View view=layoutInflater.inflate(R.layout.list_layout,null,true);

//        final ImageView imgBook = (ImageView) view.findViewById(R.id.imgBook);
        final TextView titleText = (TextView) view.findViewById(R.id.title);
        TextView subtitleText = (TextView) view.findViewById(R.id.subtitle);
        LinearLayout lin = (LinearLayout) view.findViewById(R.id.lin);

//        final HashMap<Integer ,String > AuthorName= book.get(0);
//        final HashMap<Integer ,String > AuthorBookName= book.get(1);
//
        Book books=book.get(position);
        AuthorName=books.getAuthor();
        AuthorBookName=books.getTitle();
        cover_url=books.getCover_url();

        titleText.setText(book.get(position).getAuthor());
        subtitleText.setText(book.get(position).getTitle());

//        Picasso.get()
//                .load(book.get(position).getCover_url())
//                .into(imgBook);
                if (position==0){
        if (diagonalInches>=6.5 || context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE ){
            // 6.5inch device or bigger
//            BookListFragment.Title=AuthorBookName.get(position);
//            Author=AuthorName.get(position);
            BookListFragment.Title=book.get(0).getTitle();
            BookListFragment.cover_url=book.get(0).getCover_url();
            BookListFragment.duration=book.get(0).getDuration();
            BookListFragment.book_id=book.get(0).getBook_id();
            Author=book.get(0).getAuthor();
            Toast.makeText(context, Author,Toast.LENGTH_LONG).show();
            try{
                FragmentTransaction transaction =((AppCompatActivity) context).getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.frame_details, new BookDetailsFragment());
           transaction.commit();
            }catch (Exception e){}
        }




                }
        lin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (diagonalInches>=6.5){
                    // 6.5inch device or bigger
//                    BookListFragment.Title=AuthorBookName.get(position);
//                    Author=AuthorName.get(position);
                    BookListFragment.Title=book.get(position).getTitle();
                    BookListFragment.book_id=book.get(position).getBook_id();
                    BookListFragment.duration=book.get(position).getDuration();
                    Author=book.get(position).getAuthor();
                    Position=position;

                    BookListFragment.cover_url=book.get(position).getCover_url();
                    txtAuthor.setText(BookListFragment.Author);
                    txtAuthorTitleName.setText(BookListFragment.Title);
                    Picasso.get()
                            .load(BookListFragment.cover_url)
                            .into(BookDetailsFragment.image_book);


                    eTime = Integer.valueOf(BookListFragment.duration)*1000;
                    Toast.makeText(context, ""+BookListFragment.book_id, Toast.LENGTH_SHORT).show();


                    seekBar.setMax(eTime);
                    seekBar.setProgress(sTime);

                    mTextViewStartTime.setText(String.format("%d min, %d sec", TimeUnit.MILLISECONDS.toMinutes(eTime),
                            TimeUnit.MILLISECONDS.toSeconds(eTime) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS. toMinutes(eTime))) );
                    mTextViewStopTime.setText(String.format("%d min, %d sec", TimeUnit.MILLISECONDS.toMinutes(sTime),
                            TimeUnit.MILLISECONDS.toSeconds(sTime) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS. toMinutes(sTime))) );

                }else{
                    // smaller device
                    if(context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
                        //Do some stuff
//                        BookListFragment.Title=AuthorBookName.get(position);
//                        Author=AuthorName.get(position);
                        BookListFragment.Title=book.get(position).getTitle();
                        BookListFragment.cover_url=book.get(position).getCover_url();
                        BookListFragment.book_id=book.get(position).getBook_id();
                        BookListFragment.duration=book.get(position).getDuration();

                        Author=book.get(position).getAuthor();
                        Position=position;

                        txtAuthor.setText(BookListFragment.Author);
                        txtAuthorTitleName.setText(BookListFragment.Title);
                        Picasso.get()
                                .load(BookListFragment.cover_url)
                                .into(BookDetailsFragment.image_book);

                        eTime = Integer.valueOf(BookListFragment.duration)*1000;
                        Toast.makeText(context, ""+BookListFragment.book_id, Toast.LENGTH_SHORT).show();


                        seekBar.setMax(eTime);
                        seekBar.setProgress(sTime);

                        mTextViewStartTime.setText(String.format("%d min, %d sec", TimeUnit.MILLISECONDS.toMinutes(eTime),
                                TimeUnit.MILLISECONDS.toSeconds(eTime) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS. toMinutes(eTime))) );
                        mTextViewStopTime.setText(String.format("%d min, %d sec", TimeUnit.MILLISECONDS.toMinutes(sTime),
                                TimeUnit.MILLISECONDS.toSeconds(sTime) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS. toMinutes(sTime))) );


                    }else {
                        MainActivity.frame_details.setVisibility(View.VISIBLE);
//                        BookListFragment.Title = AuthorBookName.get(position);
//                        Author = AuthorName.get(position);
                        BookListFragment.Title=book.get(position).getTitle();
                        BookListFragment.book_id=book.get(position).getBook_id();
                        BookListFragment.duration=book.get(position).getDuration();

                        BookListFragment.cover_url=book.get(position).getCover_url();
                        Author=book.get(position).getAuthor();
                        Position=position;

                        FragmentTransaction transaction = ((AppCompatActivity) context).getSupportFragmentManager().beginTransaction();
                        transaction.replace(R.id.frame_details, new BookDetailsFragment());
                        transaction.commit();


//                        eTime = Integer.valueOf(BookListFragment.duration)*1000;
//                        Toast.makeText(context, ""+BookListFragment.book_id, Toast.LENGTH_SHORT).show();
//
//
//                        seekBar.setMax(eTime);
//                        seekBar.setProgress(sTime);
//
//                        mTextViewStartTime.setText(String.format("%d min, %d sec", TimeUnit.MILLISECONDS.toMinutes(eTime),
//                                TimeUnit.MILLISECONDS.toSeconds(eTime) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS. toMinutes(eTime))) );
//                        mTextViewStopTime.setText(String.format("%d min, %d sec", TimeUnit.MILLISECONDS.toMinutes(sTime),
//                                TimeUnit.MILLISECONDS.toSeconds(sTime) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS. toMinutes(sTime))) );


                    }
                }


            }
        });

        return view;
    }

//    @Override
//    public int getViewTypeCount() {
//
//        return getCount();
//    }

    @Override
    public int getItemViewType(int position) {

        return position;
    }
}

