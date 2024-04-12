package com.example.bbcreader;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.util.TypedValue;

import android.widget.TextView;

import android.content.Context;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.google.android.material.snackbar.Snackbar;
import com.squareup.picasso.Picasso;

/**
 * A page for displaying more detailed information about a specific article
 */
public class ArticleInfo extends Fragment {

    /**
     * Creates the view for the article info page
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.article_info_layout, container, false);

        Toolbar toolbar = (Toolbar) requireActivity().findViewById(R.id.toolbar);
        toolbar.setTitle(getResources().getString(R.string.ArticleInfo));

        Bundle bundle = getArguments();
        if (bundle != null) {
            String title = bundle.getString("title");
            String description = bundle.getString("description");
            String link = bundle.getString("link");
            String guid = bundle.getString("guid");
            String pubDate = bundle.getString("pubDate");
            String thumbnailUrl = bundle.getString("thumbnailUrl");
            Boolean Add = bundle.getBoolean("Add");

            if (!Add){
                Button button = view.findViewById(R.id.Addfav);
                button.setText(getResources().getString(R.string.RemovefromFavourites));
            }

            TextView titleTextView = view.findViewById(R.id.titleTextView);
            TextView descriptionTextView = view.findViewById(R.id.descriptionTextView);
            ImageView thumbnailImageView = view.findViewById(R.id.thumbnailImageView);
            TextView linkTextView = view.findViewById(R.id.linkTextView);
            TextView pubDateTextView = view.findViewById(R.id.pubDateTextView);

            String textSzStr = Settings.loadData(requireContext().getApplicationContext(), "textSize");
            float textSz = 15;
            if (!textSzStr.equals("")){
                textSz = Float.parseFloat(textSzStr);
            }


            titleTextView.setText(title);
            titleTextView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, textSz);
            descriptionTextView.setText(description);
            descriptionTextView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, textSz);
            linkTextView.setText(link);
            linkTextView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, textSz);
            pubDateTextView.setText(pubDate);
            pubDateTextView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, textSz);

            Picasso.get().load(thumbnailUrl).into(thumbnailImageView);

            /**
             * A button for adding an article to the favourites page
             */

            Button button = view.findViewById(R.id.Addfav);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DatabaseService DBService = new DatabaseService(requireContext().getApplicationContext());
                    DBService.isInDB(guid);

                    String message;
                    if (Add){
                        DBService.addArticle(new Article(title, description, link, guid, pubDate, thumbnailUrl));
                        message = getResources().getString(R.string.AddedtoFavourites);

                    } else {
                        DBService.removeArticle(new Article(title, description, link, guid, pubDate, thumbnailUrl));
                        message = getResources().getString(R.string.RemovedfromFavourites);
                    }
                    Snackbar snackbar = Snackbar.make(requireView(), message, Snackbar.LENGTH_SHORT);
                    snackbar.show();

                    requireActivity().getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragment, new FavouritesActivity())
                            .commit();
                }
            });
        }

        return view;
    }
}
