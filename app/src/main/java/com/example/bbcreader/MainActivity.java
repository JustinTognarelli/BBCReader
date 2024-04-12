package com.example.bbcreader;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;

/**
 * class for displaying the main activity of the BBC Reader
 * This fragment displays a list of articles and provides a search bar
 */
public class MainActivity extends Fragment {
    private ArrayList<Article> feedItems;
    private ArrayList<Article> currentViewItems;
    private ListView listView;

    /**
     * Called to create the home page
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.activity_main, container, false);

        listView = rootView.findViewById(R.id.listView);

        Bundle bundle = getArguments();

        Toolbar toolbar = (Toolbar) requireActivity().findViewById(R.id.toolbar);
        toolbar.setTitle(getResources().getString(R.string.Home));


        if (bundle != null) {
            feedItems = bundle.getParcelableArrayList("articles");
            currentViewItems = feedItems;

            ArrayList<String> articleTitles = new ArrayList<>();
            for (Article article : feedItems){
                articleTitles.add(article.title);
            }

            ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_list_item_1, articleTitles);
            listView.setAdapter(adapter);


            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    Bundle bundle = new Bundle();
                    Article article = currentViewItems.get(position);
                    bundle.putString("title", article.title);
                    bundle.putString("description", article.description);
                    bundle.putString("link", article.link);
                    bundle.putString("guid", article.guid);
                    bundle.putString("pubDate", article.pubDate);
                    bundle.putString("thumbnailUrl", article.thumbnailUrl);
                    bundle.putBoolean("Add", true);

                    ArticleInfo art = new ArticleInfo();
                    art.setArguments(bundle);

                    requireActivity().getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragment, art)
                            .commit();
                }
            });

            EditText edit = rootView.findViewById(R.id.search);

            TextWatcher watcher = new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    searchArticles(s.toString());
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            };

            edit.addTextChangedListener(watcher);
        }


        return rootView;
    }

    /**
     * Performs a search on the list of articles based on the specified search
     */
    private void searchArticles(String search){
        ArrayList<String> articleTitles = new ArrayList<>();
        currentViewItems = new ArrayList<>();
        for (Article article : feedItems){
            if (article.title.toLowerCase().contains(search.toLowerCase())){
                articleTitles.add(article.title);
                currentViewItems.add(article);
            }
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_list_item_1, articleTitles);
        listView.setAdapter(adapter);

        if (currentViewItems.size() == 0){
            Toast.makeText(requireContext().getApplicationContext(), getResources().getString(R.string.Therearenoarticlesthatmatchyoursearch), Toast.LENGTH_SHORT).show();
        }
    }
}
