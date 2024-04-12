package com.example.bbcreader;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.fragment.app.Fragment;

import java.io.IOException;
import java.util.ArrayList;

import org.xmlpull.v1.XmlPullParserException;

/**
 * Class for displaying the loading screen and also for loading the articles in the background
 */
public class Loading extends Fragment {
    private ListView listView;

    /**
     * Called to create the view hierarchy associated with the fragment.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.loading_layout, container, false);

        new FetchNewsTask().execute();

        return rootView;
    }

    /**
     * AsyncTask class to fetch news articles in the background.
     */
    private class FetchNewsTask extends AsyncTask<Void, Void, ArrayList<Article>> {
        /**
         * Background task to fetch news articles.
         */
        @Override
        protected ArrayList<Article> doInBackground(Void... voids) {
            try {
                return BBCNewsFeed.ParseNewsXML();
            } catch (IOException | XmlPullParserException e) {
                e.printStackTrace();
                return null;
            }
        }

        /**
         * Creates the bundle with the list of articles and passes it on to the next fragment
         */
        @Override
        protected void onPostExecute(ArrayList<Article> articles) {
            // Create a bundle to pass articles to the MainActivity
            Bundle bundle = new Bundle();
            bundle.putParcelableArrayList("articles", articles);

            // Create a new instance of MainActivity and set arguments
            MainActivity newMain = new MainActivity();
            newMain.setArguments(bundle);

            // Replace the current fragment with MainActivity
            requireActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment, newMain)
                    .commit();
        }
    }
}
