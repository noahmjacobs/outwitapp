package edu.byuh.cis.cs203.outwit203_oneplayermode.themes;

import android.graphics.Color;
import android.graphics.Paint;

import edu.byuh.cis.cs203.outwit203_oneplayermode.Team;

public class ClassicTheme extends Theme {

    public ClassicTheme() {
        lightCell = new Paint();
        lightCell.setColor(Color.rgb(217, 198, 149));
        lightCell.setStyle(Paint.Style.FILL);
        darkCell = new Paint(lightCell);
        darkCell.setColor(Color.rgb(133, 98, 6));
        neutralColor = new Paint(lightCell);
        neutralColor.setColor(Color.rgb(231,175,28));
        suggestedMoveColor = new Paint();
        suggestedMoveColor.setColor(Color.RED);
        suggestedMoveColor.setStyle(Paint.Style.FILL);
        goldLeaf = new Paint();
        goldLeaf.setColor(Color.rgb(202,192,6));
        goldLeaf.setStyle(Paint.Style.FILL);
        darkChip = new Paint(goldLeaf);
        lightChip = new Paint(goldLeaf);
        darkChip.setColor(Color.rgb(98,78, 26));
        lightChip.setColor(Color.rgb(250,233, 188));
    }

    @Override
    public String getTeamName(Team team) {
        if (team == Team.DARK) {
            return "Dark Brown";
        } else {
            return "Light Brown";
        }
    }

    @Override
    public int getBorderColor() {
        return Color.BLACK;
    }

    @Override
    public int getTextColor() {
        return Color.BLACK;
    }


}
