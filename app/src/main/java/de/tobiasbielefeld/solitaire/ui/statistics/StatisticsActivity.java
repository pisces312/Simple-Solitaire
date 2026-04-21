/*
 * Copyright (C) 2016  Tobias Bielefeld
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 * If you want to contact me, send me an e-mail at tobias.bielefeld@gmail.com
 */

package de.tobiasbielefeld.solitaire.ui.statistics;

import android.os.Bundle;
import androidx.fragment.app.DialogFragment;
import androidx.appcompat.app.ActionBar;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import androidx.viewpager2.widget.ViewPager2;

import de.tobiasbielefeld.solitaire.R;
import de.tobiasbielefeld.solitaire.classes.CustomAppCompatActivity;
import de.tobiasbielefeld.solitaire.dialogs.DialogHighScoreDelete;

import static de.tobiasbielefeld.solitaire.SharedData.*;

public class StatisticsActivity extends CustomAppCompatActivity {

    private HideWinPercentage callback;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activty_statistics);

        ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setTabMode(TabLayout.MODE_FIXED);

        ViewPager2 pager = findViewById(R.id.pager);
        TabsPagerAdapter adapter = new TabsPagerAdapter(this);

        pager.setAdapter(adapter);
        new TabLayoutMediator(tabs, pager,
                (tab, position) -> tab.setText(adapter.getPageTitle(position))
        ).attach();
    }

    @Override
    public boolean onCreateOptionsMenu(android.view.Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_statistics, menu);
        menu.getItem(1).setChecked(prefs.getSavedStatisticsHideWinPercentage());

        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.item_delete) {
            DialogFragment deleteDialog = new DialogHighScoreDelete();
            deleteDialog.show(getSupportFragmentManager(), "high_score_delete");
        } else if (item.getItemId() == R.id.item_hide) {
            boolean checked = !prefs.getSavedStatisticsHideWinPercentage();

            prefs.saveStatisticsHideWinPercentage(checked);
            item.setChecked(checked);
            callback.sendNewState(checked);

        }

        return true;
    }

    public void setCallback(HideWinPercentage callback) {
        this.callback = callback;
    }

    public interface HideWinPercentage {
        void sendNewState(boolean state);
    }

    /**
     * deletes the data, reloads the activity
     */
    public void deleteHighScores() {
        scores.deleteScores();
        gameLogic.deleteStatistics();
        currentGame.deleteAdditionalStatisticsData();
        showToast(getString(R.string.statistics_button_deleted_all_entries), this);

        finish();
        startActivity(getIntent());
    }
}