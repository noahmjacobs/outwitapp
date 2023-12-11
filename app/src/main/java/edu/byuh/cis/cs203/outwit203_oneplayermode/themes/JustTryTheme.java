package edu.byuh.cis.cs203.outwit203_oneplayermode.themes;

import android.graphics.Color;
import android.graphics.Paint;

import edu.byuh.cis.cs203.outwit203_oneplayermode.Team;

public class JustTryTheme extends Theme {

    public JustTryTheme() {
        lightCell = new Paint();
        lightCell.setColor(Color.rgb(255,127,0));
        lightCell.setStyle(Paint.Style.FILL);
        darkCell = new Paint(lightCell);
        darkCell.setColor(Color.rgb(148,0,211));
        neutralColor = new Paint(lightCell);
        neutralColor.setColor(Color.rgb(0,225,0));
        suggestedMoveColor = new Paint();
        suggestedMoveColor.setColor(Color.rgb(255,0,0));
        suggestedMoveColor.setStyle(Paint.Style.FILL);
        goldLeaf = new Paint();
        goldLeaf.setColor(Color.rgb(0,255,0));
        goldLeaf.setStyle(Paint.Style.FILL);
        darkChip = new Paint(goldLeaf);
        lightChip = new Paint(goldLeaf);
        darkChip.setColor(Color.rgb(75,0,130));
        lightChip.setColor(Color.rgb(255,255,0));
    }

    @Override
    public String getTeamName(Team team) {
        if (team == Team.DARK) {
            return "Purple";
        } else {
            return "Yellow";
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
