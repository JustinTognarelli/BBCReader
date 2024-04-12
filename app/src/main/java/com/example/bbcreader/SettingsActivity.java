package com.example.bbcreader;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.Switch;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.google.android.material.snackbar.Snackbar;

import java.util.Locale;

/**
 * Fragment for allowing users to delete all their favourites at once and to change the
 * information tabs text size
 */
public class SettingsActivity extends Fragment {

    /**
     * Called to create the view for the settings page
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.settings_layout, container, false);

        // Getting text size from shared preferences
        String textSzStr = Settings.loadData(requireContext().getApplicationContext(), "textSize");
        SeekBar textSizeSeekBar = view.findViewById(R.id.textSizeSeekBar);
        int textSz = 15;
        if (!textSzStr.equals("")) {
            textSz = Integer.parseInt(textSzStr);
        }

        // Setting title of the toolbar
        Toolbar toolbar = (Toolbar) requireActivity().findViewById(R.id.toolbar);
        toolbar.setTitle(getResources().getString(R.string.Settings));

        // Setting progress of seek bar based on text size
        textSizeSeekBar.setProgress(textSz);
        textSizeSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            /**
             * Called when the set text size seek bar has been changed
             */
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                // Updating text size based on the progress bar value
                Settings.saveData(requireContext().getApplicationContext(), "textSize", progress + "");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        // Button click listener for deleting all favorites
        Button favouritesButt = view.findViewById(R.id.deleteAllFavoritesOption);
        favouritesButt.setOnClickListener(new View.OnClickListener() {
            /**
             * Called when the delete all favourites button has been clicked
             */
            @Override
            public void onClick(View v) {
                // Removing all articles from favorites
                DatabaseService DBService = new DatabaseService(requireContext().getApplicationContext());
                for (Article art : DBService.getAllArticles()) {
                    DBService.removeArticle(art);
                }
            }
        });

        return view;
    }
}
