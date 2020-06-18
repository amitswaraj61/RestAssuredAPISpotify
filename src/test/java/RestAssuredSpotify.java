import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.json.simple.JSONObject;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class RestAssuredSpotify {
    String userId = "";
    String pId1 = "";
    String pId2 = "";
    String tokenValue = "";
    public String[] trackId;

    @BeforeMethod
    public void setUp() {
        tokenValue = "Bearer BQAf7zUVL4V54XyFSQZ3mhO255F3C7xeTQ_ltGbIA2sO0AcNWuChECtRKzCeBZYxCAen3wYEnJssmuCGrcfo7FavcyE8kcBViuIoxyZvSJNkNfVFO_ZaC8bzbsj14O_KlIwJv5O5rOHzgZzOS4b6cNtYt2yRfNaIXzSbJPdo4TZSvQmMI5i2kVV14Tl1TghFfudKw2kWJLyNwdhKCiXnTDMdZB9u15NFh5TXU8qr2kVmBdWdxiju2BMHtA6fPfgkZ6ICb_AvOn4UFIz20Bmh1b4CEBglGVxQ";
    }

    // ----------------Here user will get the user id-------------------------------
    @Test
    public void getUserIdFromSpotifyApp() {
        Response response = RestAssured.given()
                .accept("application/json")
                .contentType("application/json")
                .header("Authorization", tokenValue)
                .when()
                .get("https://api.spotify.com/v1/me");
        userId = response.path("id");
        System.out.println("userId-------------->" + userId);

        //------------------Here user will get the profile-----------------------------------
        Response response1 = RestAssured.given()
                .accept("application/json")
                .contentType("application/json")
                .header("Authorization", tokenValue)
                .when()
                .get("https://api.spotify.com/v1/me");
        String body = response1.getBody().asString();
        System.out.println(body);

        //-------------------------Here user will create the playlist--------------------------

       /* JSONObject playList = new JSONObject();
        playList.put("name", "Amit Swa");
        playList.put("description", "Party song ");
        playList.put("public",false);

        Response response3 = RestAssured.given()
                .accept("application/json")
                .contentType("application/json")
                .header("Authorization",tokenValue)
                .body(playList.toJSONString())
                .pathParam("user_id",userId)
                .when()
                .post("https://api.spotify.com/v1/users/{user_id}/playlists");
*/
        ///////////////Here user will get the all playlist and get playlist id-------------------------

        Response response4 = RestAssured.given()
                .accept("application/json")
                .contentType("application/json")
                .header("Authorization", tokenValue)
                .pathParam("user_id", userId)
                .when()
                .get("https://api.spotify.com/v1/users/{user_id}/playlists");
        int total = response4.path("total");
        System.out.println("total playlist---------->" + total);
        pId1 = response4.path("items[0].id");
        pId2 = response4.path("items[1].id");
        System.out.println("Playlist id 1 is ----------> " + pId1);
        System.out.println("Playlist id 2 is ----------> " + pId2);


        ///////////////////Here user will get all playlist items------------------


        Response response5 = RestAssured.given()
                .accept("application/json")
                .contentType("application/json")
                .header("Authorization", tokenValue)
                .pathParam("playlist_id", pId1)
                .when()
                .get("https://api.spotify.com/v1/playlists/{playlist_id}/tracks");
        int total2 = response5.path("total");
        trackId = new String[total2];
        for (int i = 0; i < trackId.length; i++) {
            trackId[i] = response5.path("items[" + i + "].track.uri"); // get the tarck of uri
        }
        for (String track : trackId) {
            System.out.println("Track uri---------->" + track);
        }
        //--------------user will delete  playlist-----------------------------

        Response response6 = RestAssured.given()
                .accept("application/json")
                .contentType("application/json")
                .header("Authorization", tokenValue)
                .pathParam("playlist_id", pId1)
                .body("{\"uris\": [\"" + trackId[1] + "\"]}")
                .when()
                .delete("https://api.spotify.com/v1/playlists/{playlist_id}/tracks");
        response6.prettyPrint();

        //------------------Add playlist items-------------------------


        Response response7 = RestAssured.given()
                .accept("application/json")
                .contentType("application/json")
                .header("Authorization", tokenValue)
                .pathParam("playlist_id", pId1)
                .body("{\"uris\": [\"" + trackId[1] + "\"]}")
                .when()
                .post("https://api.spotify.com/v1/playlists/{playlist_id}/tracks");
        response7.prettyPrint();

        //----------------------change  a playlist details----------------------------

        Response response8 = RestAssured.given()
                .accept("application/json")
                .contentType("application/json")
                .header("Authorization", tokenValue)
                .pathParam("playlist_id", pId1)
                .body("{\"name\": \"Amit raja \",\"description\": \"New playlist description\",\"public\": true}")
                .when()
                .put("https://api.spotify.com/v1/playlists/{playlist_id}");
        response.then().assertThat().statusCode(200);

    }
}







