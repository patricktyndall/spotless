package controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.sun.javafx.collections.MappingChange.Map;
import com.wrapper.spotify.Api;
import com.wrapper.spotify.exceptions.WebApiException;
import com.wrapper.spotify.methods.AddTrackToPlaylistRequest;
import com.wrapper.spotify.methods.PlaylistCreationRequest;
import com.wrapper.spotify.methods.PlaylistRequest;
import com.wrapper.spotify.methods.PlaylistRequest.Builder;
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

		playlists = new HashMap<String, SimplePlaylist>();
		makePlaylistMap();
	}
	
	private void makePlaylistMap(){
		final UserPlaylistsRequest request = api.getPlaylistsForUser(username).build();
		try {
			final Page<SimplePlaylist> playlistsPage = request.get();
			for (SimplePlaylist p : playlistsPage.getItems()) {
				playlists.put(p.getName(), p);
			}
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
	
	public List<Pair> searchForTrackTitle(String s){
		TrackSearchRequest request = api.searchTracks(s).market("US").build();
		Page<Track> trackSearchResult = null;
		List<Pair> ret = new ArrayList<Pair>();
		try {
		   trackSearchResult = request.get();
		   System.out.println("I got " + trackSearchResult.getTotal() + " results!");
		} catch (Exception e) {
		   System.out.println("Something went wrong!" + e.getMessage());
		}
		int i = 0; // TODO better way to get top 10
		for(Track t : trackSearchResult.getItems()){
			ret.add(new Pair(t));
			i++;
			if(i==10)
				break;
		}
		return ret;
		
	}

	public List<Pair> getPlaylistTrackData(){
		PlaylistTracksRequest request = api.getPlaylistTracks(username, currentPlaylist.getId()).build();
		List<Pair> ret = new ArrayList<Pair>();

		try {
			final Page<PlaylistTrack> page = request.get();

			final List<PlaylistTrack> playlistTracks = page.getItems();

			for (PlaylistTrack playlistTrack : playlistTracks) {
				ret.add(new Pair(playlistTrack.getTrack()));
			}

		} catch (Exception e) {
			System.out.println("Something went wrong!!!!" + e.getMessage());
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

	public List<String> getPlaylistNames(){
		List<String> ret = new ArrayList<String>();
		UserPlaylistsRequest.Builder builder = api.getPlaylistsForUser(username);
		UserPlaylistsRequest request = builder.limit(40).offset(0).build();

		// System.out.println("Request Data: " +

		try {
			Page<SimplePlaylist> playlistsPage = request.get();

			for (SimplePlaylist playlist : playlistsPage.getItems()) {
				ret.add(playlist.getName());
				// System.out.println((playlist.getName()));
			}
			
			System.out.println("Playlist data :" +playlistsPage.getLimit()+ "  " + playlistsPage.getNext() + "   "+ 
			playlistsPage.getPrevious()+ "   " + playlistsPage.getTotal());

		} catch (Exception e) {
			System.out.println("Something went wrong!" + e.getMessage());
		}

		return ret;

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

	public boolean playlistSet(){
		return (!(currentPlaylist == null));
	}
}
