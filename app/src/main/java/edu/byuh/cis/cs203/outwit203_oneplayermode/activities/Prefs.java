package edu.byuh.cis.cs203.outwit203_oneplayermode.activities;

import android.content.Context;
import android.os.Bundle;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.ListPreference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;
import androidx.preference.SwitchPreference;

import edu.byuh.cis.cs203.outwit203_oneplayermode.R;
import edu.byuh.cis.cs203.outwit203_oneplayermode.Team;
import edu.byuh.cis.cs203.outwit203_oneplayermode.themes.Awesome80sTheme;
import edu.byuh.cis.cs203.outwit203_oneplayermode.themes.ClassicTheme;
import edu.byuh.cis.cs203.outwit203_oneplayermode.themes.JustTryTheme;
import edu.byuh.cis.cs203.outwit203_oneplayermode.themes.Theme;

public class Prefs extends AppCompatActivity {

    public static final String MUSIC_PREFS_KEY = "MUSIC_PREFS_KEY";
    //public static final String ANIMATION_PREFS_KEY = "ANIMATION_PREFS_KEY";
    public static final String WHO_GOES_FIRST_PREFS_KEY = "WHO_GOES_FIRST_PREFS_KEY";
    public static final String ANIMATION_SPEED_PREFS_KEY = "ANIMATION_SPEED_PREFS_KEY";
    public static final String CHIPSET_PREFS_KEY = "CHIPSET_PREFS_KEY";
    public static final String CHIP_LAYOUT_PREFS_KEY = "CHIP_LAYOUT_PREFS_KEY";
    public static final String THEME_PREFS_KEY = "THEME_PREFS_KEY";
    public static final String WHO_IS_PLAYING_KEY = "WHO_IS_PLAYING_KEY";

    public static final String CLASSIC_THEME = "CLASSIC_THEME";
    public static final String C64_THEME = "C64_THEME";
    public static final String RAINBOW_THEME = "RAINBOW_THEME";

    public static final int DARK_FIRST = 1;
    public static final int LIGHT_FIRST = 2;
    public static final int RANDOM_FIRST = 3;

    public static final int STANDARD_CHIPSET = 1;
    public static final int ALL_POWER = 2;
    public static final int ALL_REGULAR = 3;

    public static final int STANDARD_LAYOUT = 1;
    public static final int CENTRALIZED_LAYOUT = 2;
    public static final int RANDOMIZED_LAYOUT = 3;

    public static final int TWO_HUMANS = 1;
    public static final int HUMAN_LIGHT_AI_DARK = 2;
    public static final int HUMAN_DARK_AI_LIGHT = 3;




    /**
     * Auto-generated code. I take no responsibility for it.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);
        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.settings, new SettingsFragment())
                    .commit();
        }
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    /**
     * Helper "façade" method to simplify access to Android's obtuse Preferences API
     * @param c a context object, typically the main activity
     * @return true if music is desired; false otherwise
     */
    public static boolean getMusicPrefs(Context c) {
        return PreferenceManager.getDefaultSharedPreferences(c).getBoolean(MUSIC_PREFS_KEY, false);
    }

    public static Theme getTheme(Context c) {
        String th = PreferenceManager.getDefaultSharedPreferences(c).getString(THEME_PREFS_KEY, CLASSIC_THEME);
        return switch (th) {
            case RAINBOW_THEME -> new JustTryTheme();
            case C64_THEME -> new Awesome80sTheme();
            default -> new ClassicTheme();
        };
    }

//    /**
//     * Helper "façade" method to simplify access to Android's obtuse Preferences API
//     * @param c a context object, typically the main activity
//     * @return true if animation is desired; false otherwise
//     */
//    public static boolean getAnimationPrefs(Context c) {
//        return PreferenceManager.getDefaultSharedPreferences(c).getBoolean(ANIMATION_PREFS_KEY, true);
//    }

    /**
     * Helper "façade" method to simplify access to Android's obtuse Preferences API
     * @param c a context object, typically the main activity
     * @return the first player
     */
    public static Team whoGoesFirst(Context c) {
        String tmp = PreferenceManager.getDefaultSharedPreferences(c).getString(WHO_GOES_FIRST_PREFS_KEY, ""+RANDOM_FIRST);
        int firstly = Integer.parseInt(tmp);
        if (firstly == Prefs.DARK_FIRST) {
            return Team.DARK;
        } else if (firstly == Prefs.LIGHT_FIRST) {
            return Team.LIGHT;
        } else {
            if (Math.random() < 0.5) {
                return Team.DARK;
            } else {
                return Team.LIGHT;
            }
        }
    }

    /**
     * Helper "façade" method to simplify access to Android's obtuse Preferences API
     * @param c a context object, typically the main activity
     * @return number of milliseconds between animation frames: 10 (fast), 50 (medium), or 100 (slow)
     */
    public static int animationSpeed(Context c) {
        String tmp = PreferenceManager.getDefaultSharedPreferences(c).getString(ANIMATION_SPEED_PREFS_KEY, "10");
        return Integer.parseInt(tmp);
    }

    /**
     * Helper "façade" method to simplify access to Android's obtuse Preferences API
     * @param c a context object, typically the main activity
     * @return STANDARD_CHIPSET, ALL_POWER, or ALL_REGULAR
     */
    public static int chipset(Context c) {
        String tmp = PreferenceManager.getDefaultSharedPreferences(c).getString(CHIPSET_PREFS_KEY, ""+STANDARD_CHIPSET);
        return Integer.parseInt(tmp);
    }

    /**
     * Helper "façade" method to simplify access to Android's obtuse Preferences API
     * @param c a context object, typically the main activity
     * @return STANDARD_LAYOUT, CENTRALIZED_LAYOUT, or RANDOMIZED_LAYOUT
     */
    public static int chipLayout(Context c) {
        String tmp = PreferenceManager.getDefaultSharedPreferences(c).getString(CHIP_LAYOUT_PREFS_KEY, "1");
        return Integer.parseInt(tmp);
    }

    public static Team getWhosePlayingPreference(Context c) {
        String tmp = android.preference.PreferenceManager.getDefaultSharedPreferences(c).getString(WHO_IS_PLAYING_KEY, ""+TWO_HUMANS);
        int number = Integer.parseInt(tmp);
        return switch (number) {
            case Prefs.HUMAN_DARK_AI_LIGHT -> Team.LIGHT;
            case Prefs.HUMAN_LIGHT_AI_DARK -> Team.DARK;
            default -> Team.NEUTRAL;
        };
    }

//    public static int getWhosePlayingPreference(Context c) {
//        String tmp = android.preference.PreferenceManager.getDefaultSharedPreferences(c).getString(WHO_IS_PLAYING_KEY, ""+TWO_HUMANS);
//        return Integer.parseInt(tmp);
//    }

    /**
     * Auto-generated static nested class.
     * Our job is to fill out the onCreatePreferences method.
     */
    public static class SettingsFragment extends PreferenceFragmentCompat {
        @Override
        public void onCreatePreferences(Bundle b, String s) {
            var context = getPreferenceManager().getContext();
            var screen = getPreferenceManager().createPreferenceScreen(context);

            var whoIsPlaying = new ListPreference(context);
            whoIsPlaying.setTitle("Who is playing?");
            whoIsPlaying.setSummary("Two or one players?");
            whoIsPlaying.setKey(WHO_IS_PLAYING_KEY);
            String[] playerOptions = {"Two human players", "Human (dark) vs AI (light)", "Human (light) vs AI (dark)"};
            String[] playerOptionValues = {""+TWO_HUMANS, ""+HUMAN_DARK_AI_LIGHT, ""+HUMAN_LIGHT_AI_DARK};
            whoIsPlaying.setEntries(playerOptions);
            whoIsPlaying.setEntryValues(playerOptionValues);
            whoIsPlaying.setDefaultValue(""+TWO_HUMANS);
            screen.addPreference(whoIsPlaying);

            //add preference widgets here
            var music = new SwitchPreference(context);
            music.setTitle("Background Music");
            music.setSummaryOn("Music will play");
            music.setSummaryOff("Music will not play");
            music.setDefaultValue(false);
            music.setKey(MUSIC_PREFS_KEY);
            screen.addPreference(music);

            var themePref = new ListPreference(context);
            themePref.setTitle("Choose theme");
            themePref.setSummary("Select a color scheme");
            themePref.setDefaultValue(CLASSIC_THEME);
            themePref.setKey(THEME_PREFS_KEY);
            String[] themeIDs = {CLASSIC_THEME, C64_THEME, RAINBOW_THEME};
            String[] themeNames = {"Classic", "Bright '80s", "Just Try"};
            themePref.setEntries(themeNames);
            themePref.setEntryValues(themeIDs);
            themePref.setDefaultValue(CLASSIC_THEME);
            screen.addPreference(themePref);

            //GOT RID OF ANIMATION PREFERENCE. IT WAS TOO MUCH HASSLE
            //FOR TOO LITTLE BENEFIT.
//            var animation = new SwitchPreference(context);
//            animation.setTitle("Animated Chip Movements");
//            animation.setSummaryOn("Chips will slide smoothly from one square to another");
//            animation.setSummaryOff("Chips will teleport from one square to another");
//            animation.setKey(ANIMATION_PREFS_KEY);
//            animation.setDefaultValue(true);
//            screen.addPreference(animation);

            var whosOnFirst = new ListPreference(context);
            whosOnFirst.setTitle("Starting player");
            whosOnFirst.setSummary("Which player goes first?");
            String[] entries = {"Dark always goes first", "Light always goes first", "Random"};
            String[] values = {""+DARK_FIRST, ""+LIGHT_FIRST, ""+RANDOM_FIRST};
            whosOnFirst.setEntries(entries);
            whosOnFirst.setEntryValues(values);
            whosOnFirst.setDefaultValue(""+RANDOM_FIRST);
            whosOnFirst.setKey(WHO_GOES_FIRST_PREFS_KEY);
            screen.addPreference(whosOnFirst);

            var speed = new ListPreference(context);
            speed.setTitle("Animation Speed");
            speed.setSummary("How fast will the chips move?");
            entries = new String[]{"Fast", "Medium", "Slow"};
            values = new String[]{"10", "50", "100"};
            speed.setEntries(entries);
            speed.setEntryValues(values);
            speed.setDefaultValue("10");
            speed.setKey(ANIMATION_SPEED_PREFS_KEY);
            screen.addPreference(speed);

            var chipset = new ListPreference(context);
            chipset.setTitle("Chip Set");
            chipset.setSummary("What kinds of chips to use?");
            entries = new String[]{"Standard set", "All power chips", "All regular chips"};
            values = new String[]{""+STANDARD_CHIPSET, ""+ALL_POWER, ""+ALL_REGULAR};
            chipset.setEntries(entries);
            chipset.setEntryValues(values);
            chipset.setDefaultValue(""+STANDARD_CHIPSET);
            chipset.setKey(CHIPSET_PREFS_KEY);
            screen.addPreference(chipset);

            var chiplayout = new ListPreference(context);
            chiplayout.setTitle("Chip Layout");
            chiplayout.setSummary("How are chips arranged on the board?");
            entries = new String[]{"Classic", "Centralized", "Randomized"};
            values = new String[]{""+STANDARD_LAYOUT, ""+CENTRALIZED_LAYOUT, ""+RANDOMIZED_LAYOUT};
            chiplayout.setEntries(entries);
            chiplayout.setEntryValues(values);
            chiplayout.setDefaultValue(""+STANDARD_LAYOUT);
            chiplayout.setKey(CHIP_LAYOUT_PREFS_KEY);
            screen.addPreference(chiplayout);

            setPreferenceScreen(screen);
        }


    }
}