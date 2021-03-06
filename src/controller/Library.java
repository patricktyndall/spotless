package controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import com.wrapper.spotify.Api;
import com.wrapper.spotify.exceptions.WebApiException;
import com.wrapper.spotify.methods.AddTrackToPlaylistRequest;
import com.wrapper.spotify.methods.PlaylistCreationRequest;
import com.wrapper.spotify.methods.PlaylistTracksRequest;
import com.wrapper.spotify.methods.TrackSearchRequest;
import com.wrapper.spotify.methods.UserPlaylistsRequest;
import com.wrapper.spotify.models.Page;
import com.wrapper.spotify.models.Playlist;
import com.wrapper.spotify.models.PlaylistTrack;
import com.wrapper.spotify.models.SimpleArtist;
import com.wrapper.spotify.models.SimplePlaylist;
import com.wrapper.spotify.models.Track;

import data.Pair;

public class Library {

	private Api api;
	private String username;
	private HashMap<String, SimplePlaylist> playlists;
	private SimplePlaylist currentPlaylist;

	public Library(Api api){
		this.api = api;

		try {
			this.username = api.getMe().build().get().getId();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (WebApiException e) {
			e.printStackTrace();
		}

		playlists = new LinkedHashMap<String, SimplePlaylist>();
		makePlaylistMap();
	}

	private void makePlaylistMap(){
		final UserPlaylistsRequest request = api.getPlaylistsForUser(username).limit(50).build();
		try {
			// while(true){
				Page<SimplePlaylist> playlistsPage = request.get();
				for (SimplePlaylist p : playlistsPage.getItems()) {
					playlists.put(p.getName(), p);
				}
				 /*String next;
				if((next = playlistsPage.getNext()) == null)
					break;
				playlistsPage = api.getPlaylistsForUser(username).offset(20).build().get();
				System.out.println(next);*/ 
				// TODO figure out how to get next page of items from this URL
			
			// }
		} catch (Exception e) {
			System.out.println("Something went wrong!" + e.getMessage());
		}
	}

	private void updatePlaylistMap(){
		final UserPlaylistsRequest request = api.getPlaylistsForUser(username).build();
		try {
			final Page<SimplePlaylist> playlistsPage = request.get();
			for (SimplePlaylist p : playlistsPage.getItems()) {
				if(!playlists.keySet().contains(p.getName()))
					playlists.put(p.getName(), p);
			}
		} catch (Exception e) {
			System.out.println("Something went wrong!" + e.getMessage());
		}


	}

	public void createPlaylist(String title, List<Pair> data){
		List<String> URIs = this.search(data);
		SimplePlaylist playlist = makeNewPlaylist(title);	
		this.addTracksToPlaylist(playlist, URIs);
		currentPlaylist = playlist;
	}

	public List<Track> searchForTrackTitle(String s){
		TrackSearchRequest.Builder builder = api.searchTracks(s).market("US");
		TrackSearchRequest request = builder.limit(5).build(); // TODO revisit, see if you can make results smaller to improve performance
		Page<Track> trackSearchResult = null;
		List<Track> ret = new ArrayList<Track>();
		try {
			trackSearchResult = request.get();
			System.out.println("I got " + trackSearchResult.getTotal() + " results!");
		} catch (Exception e) {
			System.out.println("Something went wrong!" + e.getMessage());
		}

		for(Track t : trackSearchResult.getItems()){
			ret.add(t);

		}
		return ret;

	}



	public List<Track> getPlaylistTrackData(){
		PlaylistTracksRequest request = api.getPlaylistTracks(username, currentPlaylist.getId()).build();
		List<Track> ret = new ArrayList<Track>();

		try {
			final Page<PlaylistTrack> page = request.get();
			final List<PlaylistTrack> playlistTracks = page.getItems();
			for (PlaylistTrack playlistTrack : playlistTracks) {
				ret.add((playlistTrack.getTrack()));
			}

		} catch (Exception e) {
			System.out.println("Something went wrong!" + e.getMessage());
			return null;
		}

		return ret;
	}

	public SimplePlaylist makeNewPlaylist(String title){
		Playlist playlist = null;

		final PlaylistCreationRequest request = api.createPlaylist(username, title)
				.publicAccess(true)
				.build();
		try {
			playlist = request.get();
		} catch (Exception e) {
			System.out.println("Something went wrong!" + e.getMessage());
		}

		updatePlaylistMap();
		currentPlaylist = playlists.get(playlist.getName());
		return playlists.get(playlist.getName());
	}

	public void setCurrentPlaylist(String title){
		currentPlaylist = playlists.get(title);
	}
	
	public boolean playlistSet(){
		return (!(currentPlaylist == null));
	}

	public void addTracksToPlaylist(SimplePlaylist playlist, List<String >data){

		final AddTrackToPlaylistRequest request = api.addTracksToPlaylist(username, playlist.getId(), data)
				.position(0) // TODO check this index
				.build();

		try {
			request.get();
		} catch (Exception e) {
			System.out.println("Something went wrong!" + e.getMessage());
		}
	}

	public void addTrackToCurrentPlaylist(final Track t){
		// TODO is this safe?
		new Thread(){
			public void run(){
			List<String> data = new ArrayList<String>();
			data.add(t.getUri());
			final AddTrackToPlaylistRequest request = api.addTracksToPlaylist(username, currentPlaylist.getId(), data)
					.position(0) // TODO check this index
					.build();

			try {
				request.get();
			} catch (Exception e) {
				System.out.println("Something went wrong!" + e.getMessage());
			}}
		}.start();

	}
	
	public List<String> getPlaylistNames(){
		return new ArrayList<String>(playlists.keySet());
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
						if(p.artists.contains(artist.getName())){
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
