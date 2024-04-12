package com.example.bbcreader;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;

/**
 * Page for managing the articles categorized as favorites
 */
public class FavouritesActivity extends Fragment {
    private ArrayList<Article> feedItems;
    private ListView listView;

    /**
     * Called to create the page for viewing the favourites
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.favourites_layout, container, false);

        listView = rootView.findViewById(R.id.listView);

        DatabaseService DBService = new DatabaseService(requireContext().getApplicationContext());
        feedItems = DBService.getAllArticles();

        Toolbar toolbar = (Toolbar) requireActivity().findViewById(R.id.toolbar);
        toolbar.setTitle(getResources().getString(R.string.Favourites));

        ArrayList<String> articleTitles = new ArrayList<>();
        for (Article article : feedItems){
            articleTitles.add(article.title);
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_list_item_1, articleTitles);
        listView.setAdapter(adapter);


        /**
         * Click listener for list view elements to send user to more information page
         */
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Bundle bundle = new Bundle();
                bundle.putString("title", feedItems.get(position).title);
                bundle.putString("description", feedItems.get(position).description);
                bundle.putString("link", feedItems.get(position).link);
                bundle.putString("guid", feedItems.get(position).guid);
                bundle.putString("pubDate", feedItems.get(position).pubDate);
                bundle.putString("thumbnailUrl", feedItems.get(position).thumbnailUrl);
                bundle.putBoolean("Add", false);

                ArticleInfo art = new ArticleInfo();
                art.setArguments(bundle);

                requireActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment, art)
                        .commit();
            }
        });


        return rootView;
    }
}
