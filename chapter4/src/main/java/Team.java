import com.alibaba.fastjson.JSON;
import lombok.Data;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Data
public class Team {

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

    /**
     * If a player was replaced by a substitute, or get a red card, he cannot
     * go to field again, and cannot be used as a new substitute also
     *
     * Since there can be up to 3 substitutes to replace main players and there
     * shall be at least 7 players or game will be over directly, thus there will
     * be up to 7 players out of field
     */
    private Player[] outOfFieldPlayers = new Player[7];

    public static Team fromJSON(String resource) {
        try {
            String json = IOUtils.resourceToString("/" + resource, StandardCharsets.UTF_8);
            return JSON.parseObject(json, Team.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        System.out.println(Team.fromJSON("real-madrid.json"));
        System.out.println(Team.fromJSON("barcelona.json"));
    }

}
