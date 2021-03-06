
/*
 *   Copyright 2018 Firas Abbas (Kairos Sin)
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.porphiros.booksquery;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatRatingBar;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.request.RequestOptions;

import java.util.List;

public class BooksAdapter extends RecyclerView.Adapter<BooksAdapter.BookHolder> {

    private static final String TAG = BooksAdapter.class.getSimpleName();

    private List<Book> mBooks;
    private Context mContext;

    public BooksAdapter(Context context, List<Book> books) {
        mBooks = books;
        mContext = context;
    }

    @NonNull
    @Override
    public BookHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_row, parent, false);


        return new BookHolder(view);
    }

    @SuppressLint("CheckResult")
    @Override
    public void onBindViewHolder(@NonNull BookHolder holder, int position) {

        Book book = mBooks.get(position);
        holder.mTitle_Tv.setText(book.getTitle());
        holder.mRatingBar.setRating((float) book.getRating());
        //set the image with Glide library

        //to setup placeholders
        RequestOptions requestOptions = new RequestOptions();
        //In case there is no image loaded from the web api
        requestOptions.placeholder(R.drawable.no_image);
        requestOptions.error(R.drawable.no_image);

        GlideApp.with(holder.mThumbnail.getContext())
                .setDefaultRequestOptions(requestOptions)
                .load(Uri.parse(book.getThumbnailSmall()))
                .into(holder.mThumbnail);


        holder.mSubtitle.setText(book.getSubtitle());

    }

    @Override
    public int getItemCount() {
        return mBooks.size();
    }

    /**
     * helper method to be called once {@link android.content.AsyncTaskLoader} is finished loading
     *
     * @param data the data received from the background loader
     */
    public void updateAdapterData(List<Book> data) {
        mBooks = data;
        notifyDataSetChanged();
    }

    public class BookHolder extends RecyclerView.ViewHolder /*implements click listener*/
            implements View.OnClickListener {

        private TextView mTitle_Tv;
        private AppCompatRatingBar mRatingBar;
        private ImageView mThumbnail;
        private TextView mSubtitle;

        public BookHolder(View itemView) {
            super(itemView);
            mTitle_Tv = itemView.findViewById(R.id.row_book_title);
            mRatingBar = itemView.findViewById(R.id.row_book_rating);
            mThumbnail = itemView.findViewById(R.id.row_book_img);
            mSubtitle = itemView.findViewById(R.id.row_book_subtitle);

            //attach the click listener to this holder view
            itemView.setOnClickListener(this);
        }

        //Open a details view activity
        @Override
        public void onClick(View v) {
            //Add book to recents
            Book book = mBooks.get(this.getAdapterPosition());
            BookRecentUtils.addBookToQueue(book);

            Log.i(TAG, "recent Queue Size:" + BookRecentUtils.getQueueSize());

            //start intent
            Intent intent = BookDetailActivity.newIntent(mContext, book);
            mContext.startActivity(intent);
        }
    }


}
