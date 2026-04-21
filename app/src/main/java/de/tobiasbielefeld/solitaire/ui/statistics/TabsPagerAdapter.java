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

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import de.tobiasbielefeld.solitaire.R;

/**
 * Adapter for the tabs (ViewPager2 version)
 */

public class TabsPagerAdapter extends FragmentStateAdapter {

    private final String[] TITLES;

    TabsPagerAdapter(FragmentActivity activity) {
        super(activity);
        TITLES = new String[]{activity.getString(R.string.settings_other),
                activity.getString(R.string.statistics_high_scores), activity.getString(R.string.statistics_recent_scores)};
    }

    public CharSequence getPageTitle(int position) {
        return TITLES[position];
    }

    @Override
    public int getItemCount() {
        return TITLES.length;
    }

    @NonNull
    @Override
    public Fragment createFragment(int index) {
        switch (index) {
            case 0:
                return new StatisticsFragment();
            case 1:
                return new HighScoresFragment();
            case 2:
                return new RecentScoresFragment();
        }
        return new StatisticsFragment();
    }
}