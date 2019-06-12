import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.Data;
import org.apache.commons.io.IOUtils;

import java.awt.*;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * <pre>
 * A simplified modeling of a soccer club team
 *
 * Sample data:
 * Real Madrid: https://en.wikipedia.org/wiki/2002%E2%80%9303_Real_Madrid_CF_season
 * Barcelona: https://en.wikipedia.org/wiki/2002%E2%80%9303_FC_Barcelona_season
 * </pre>
 */
@Data
class Team implements Drawable {
    private String clubName;

    private String colorOfJersey;

    private Coach headCoach;

    private Coach[] assistantCoaches;

    /**
     * Main players
     */
    private Player[] mainPlayers;

    /**
     * Substitutes
     */
    private Player[] benchPlayers;

    @Override
    public void draw(Graphics g) {
        g.setColor(colorOfJersey.equals("White") ? Color.WHITE : Color.BLUE);
        for (Player player : mainPlayers)
            player.draw(g);

        for (Player player : benchPlayers)
            player.draw(g);

        g.setColor(Color.BLACK);
        headCoach.draw(g);

        for (Coach ac : assistantCoaches) {
            g.setColor(Color.GREEN);
            ac.draw(g);
        }
    }

    void initLocations(int width, int height, boolean leftSide) {
        Player.initLocation(width, height, leftSide, mainPlayers, benchPlayers);

        this.headCoach.setLocation(leftSide ? new Location(width / 2 - 80, height) :
            new Location(width / 2 + 40, height));

        for (int i = 1; i <= assistantCoaches.length; i++) {
            int x = this.headCoach.getLocation().x + (leftSide ? -i * 60 : i * 60);
            assistantCoaches[i - 1].setLocation(new Location(x, height));
        }
    }

    void play(Ball ball) {
        int selected = Tools.nextInt(mainPlayers.length);
        boolean kick = Tools.nextInt(13) == 8;
        for (int i = 0; i < mainPlayers.length; i++) {
            if (i == selected && kick) {
                if (Tools.nextBoolean()) {
                    mainPlayers[i].shoot(ball);
                } else {
                    mainPlayers[i].pass(ball);
                }
            } else {
                mainPlayers[i].run();
            }
        }

        if (!kick)
            ball.getLocation().move(-40 + Tools.nextInt(81), -25 + Tools.nextInt(56), false);
    }

    static Team fromJSON(String resource) {
        try {
            String json = IOUtils.resourceToString("/" + resource, StandardCharsets.UTF_8);
            // JSON.parseObject(json, Team.class) requires too much access permission for this chapter
            // Choose more verbose approach here temporarily
            JSONObject jo = JSON.parseObject(json);
            Team team = new Team();

            team.clubName = jo.getString("clubName");
            team.colorOfJersey = jo.getString("colorOfJersey");
            team.headCoach = parseCoach(jo.getJSONObject("headCoach"));

            JSONArray jac = jo.getJSONArray("assistantCoaches");
            team.assistantCoaches = new Coach[jac.size()];
            for (int i = 0; i < jac.size(); i++) {
                team.assistantCoaches[i] = parseCoach(jac.getJSONObject(i));
            }

            team.mainPlayers = parsePlayers(jo.getJSONArray("mainPlayers"));
            team.benchPlayers = parsePlayers(jo.getJSONArray("benchPlayers"));

            return team;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static Coach parseCoach(JSONObject joc) {
        JSONObject name = joc.getJSONObject("name");
        return new Coach(new Name(name.getString("first"), name.getString("last")),
            joc.getInteger("age"), joc.getInteger("champions"));
    }

    private static Player[] parsePlayers(JSONArray ja) {
        Player[] players = new Player[ja.size()];
        for (int i = 0; i < ja.size(); i++) {
            JSONObject jo = ja.getJSONObject(i);
            JSONObject name = jo.getJSONObject("name");
            players[i] = new Player(new Name(name.getString("first"), name.getString("last")),
                jo.getInteger("number"), jo.getString("role"), jo.getInteger("age"), jo.getInteger("height"));
        }
        return players;
    }
}
