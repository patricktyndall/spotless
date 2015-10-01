package controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.wrapper.spotify.models.Artist;

import data.Pair;

public class SearchBoxController extends AbstractPlaylistController{

	public List<String> getSearchResults(String search) {
//		String[] ary = new String[]{"first", "second", "third", "fourth"};
//		int i = search.length();
//		ary[i] = "Deleted";
//		List<String> ret = Arrays.asList(ary);
//		return ret;
		
		List<Pair> resultPairs = library.searchForTrackTitle(search);
		List<String> ret = new ArrayList<String>();
		
		for(Pair p :  resultPairs)
			ret.add(makeStringFromArtists(p.artists));
		
		return ret;
	}
	
	private String makeStringFromArtists(List<String> artists){
		String ret = "";
		for (String s : artists)
			ret = ret + s + " ";
		
		return ret;
	}

}
