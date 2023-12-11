package edu.byuh.cis.cs203.outwit203_oneplayermode.themes;

import android.graphics.Color;
import android.graphics.Paint;

import edu.byuh.cis.cs203.outwit203_oneplayermode.Team;

public class Awesome80sTheme extends Theme {

    public Awesome80sTheme() {
        lightCell = new Paint();
        lightCell.setColor(Color.rgb(25,137,74));
        lightCell.setStyle(Paint.Style.FILL);
        darkCell = new Paint(lightCell);
        darkCell.setColor(Color.rgb(61,115,255));
        neutralColor = new Paint(lightCell);
        neutralColor.setColor(Color.rgb(235,255,78));
        suggestedMoveColor = new Paint();
        suggestedMoveColor.setColor(Color.rgb(222,128,238));
        suggestedMoveColor.setStyle(Paint.Style.FILL);
        goldLeaf = new Paint();
        goldLeaf.setColor(Color.rgb(255,105,180));
        goldLeaf.setStyle(Paint.Style.FILL);
        darkChip = new Paint(goldLeaf);
        lightChip = new Paint(goldLeaf);
        darkChip.setColor(Color.rgb(128,191,255));
        lightChip.setColor(Color.rgb(0,255,96));
    }

    @Override
    public String getTeamName(Team team) {
        if (team == Team.DARK) {
            return "Blue";
        } else {
            return "Green";
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
