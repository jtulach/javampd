package org.bff.javampd.server;

import org.bff.javampd.BaseTest;
import org.bff.javampd.MPDItem;
import org.bff.javampd.artist.MPDArtist;
import org.bff.javampd.integrationdata.TestAlbums;
import org.bff.javampd.integrationdata.TestArtists;
import org.bff.javampd.integrationdata.TestSongs;
import org.bff.javampd.player.Player;
import org.bff.javampd.playlist.Playlist;
import org.bff.javampd.song.MPDSong;
import org.bff.javampd.statistics.ServerStatistics;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class MPDServerStatisticsIT extends BaseTest {

    private Player player;
    private Playlist playlist;
    private ServerStatistics serverStatistics;
    private static long DELAY = 1000;

    @Before
    public void setUp() {
        this.player = getMpd().getPlayer();
        this.playlist = getMpd().getPlaylist();
        this.serverStatistics = getMpd().getServerStatistics();
    }

    @Test
    public void testGetPlaytime() {
        player.stop();
        List<MPDSong> songs = new ArrayList<>(TestSongs.getSongs());
        playlist.addSong(songs.get(0));
        player.play();
        delay(DELAY);

        player.stop();

        assertTrue(serverStatistics.getPlaytime() > 0);
    }

    @Test
    public void testGetUptime() {
        assertTrue(serverStatistics.getUptime() > 0);
    }

    @Test
    public void testAlbumCount() {
        Set<String> albumSet = TestAlbums.getAlbums().stream().filter(album ->
                !TestAlbums.NULL_ALBUM.equals(album.getName())).map(MPDItem::getName).collect(Collectors.toSet());
        //stats does not report blank albums

        assertEquals(albumSet.size(), serverStatistics.getAlbumCount());
    }

    @Test
    public void testArtistCount() {
        Set<MPDArtist> artistSet = TestArtists.getArtists().stream().filter(artist ->
                !TestArtists.NULL_ARTIST.equals(artist.getName())).collect(Collectors.toSet());
        //stats does not report blank albums

        assertEquals(artistSet.size(), serverStatistics.getArtistCount());
    }

    @Test
    public void testSongCount() {
        assertEquals(TestSongs.getSongs().size(), serverStatistics.getSongCount());
    }

    @Test
    public void testGetDatabasePlaytime() {
        assertTrue(serverStatistics.getDatabasePlaytime() > 0);
    }

    @Test
    public void testGetDatabaseUpdateTime() {
        assertTrue(serverStatistics.getLastUpdateTime() > 0);
    }
}
