package controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.wrapper.spotify.Api;
import com.wrapper.spotify.exceptions.WebApiException;
import com.wrapper.spotify.methods.AddTrackToPlaylistRequest;
import com.wrapper.spotify.methods.PlaylistCreationRequest;
import com.wrapper.spotify.methods.TrackSearchRequest;
import com.wrapper.spotify.models.Page;
import com.wrapper.spotify.models.Playlist;
import com.wrapper.spotify.models.SimpleArtist;
import com.wrapper.spotify.models.Track;

import data.Pair;

public class Library {

	private Api api;
	private String username;

	public Library(Api api){
		this.api = api;

		try {
			this.username = api.getMe().build().get().getId();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (WebApiException e) {
			e.printStackTrace();
		}
	}
	
	public void createPlaylist(String title, List<Pair> data){
		List<String> URIs = this.search(data);
		Playlist playlist = makeNewPlaylist(title);	
		this.addTracksToPlaylist(playlist, URIs);
	}
	
	public Playlist makeNewPlaylist(String title){
		Playlist playlist = null;

		final PlaylistCreationRequest request = api.createPlaylist(username, title)
				.publicAccess(true)
				.build();
		try {
			playlist = request.get();
		} catch (Exception e) {
			System.out.println("Something went wrong!" + e.getMessage());
		}

		return playlist;

	}

	public void addTracksToPlaylist(Playlist playlist, List<String >data){

		final AddTrackToPlaylistRequest request = api.addTracksToPlaylist(username, playlist.getId(), data)
				.position(0) // TODO check this index
				.build();

		try {
			request.get();
		} catch (Exception e) {
			System.out.println("Something went wrong!" + e.getMessage());
		}
	}

	public List<String> search(List<Pair> data){

		List<String> results = new ArrayList<String>(data.size());

		for(Pair p : data){

			TrackSearchRequest request = api.searchTracks(p.song).market("US").build();
			Page<Track> trackSearchResult = null;

			try {
				trackSearchResult = request.get();
			} catch (Exception e) {
				System.out.println("Something went wrong!" + e.getMessage());
			}

			outer:
				for(Track t : trackSearchResult.getItems()){
					for (SimpleArtist artist : t.getArtists())
						if(artist.getName().equalsIgnoreCase(p.artist)){
							results.add(t.getUri());	
							break outer;
						}
				}	
		}

		for (String s : results)
			System.out.println(s);
		return results;
	}
}
