package gui;

import java.util.List;

import com.wrapper.spotify.models.SimpleArtist;
import com.wrapper.spotify.models.Track;

import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.ImageViewBuilder;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

public class SearchResultNode extends HBox{

	public SearchResultNode(Track t){
		
		// TODO make these hyperlinks
		
		String trackName = t.getName();
		String artists = "";
		
		for(SimpleArtist s : t.getArtists())
			artists  = artists + s.getName(); // format this better, also make it a bunch of hyperlinks
		
		List<com.wrapper.spotify.models.Image> URLS = t.getAlbum().getImages();
		String imageSource = URLS.get(URLS.size()-1).getUrl();
        
        ImageView albumArt = ImageViewBuilder.create()
                .image(new Image(imageSource))
                .build();

        VBox vbox = new VBox();
        vbox.getChildren().add(new Label(trackName));
        vbox.getChildren().add(new Label(artists));
        
		this.getChildren().add(albumArt);
		this.getChildren().add(vbox);
		
	
		
		
		
	}
	
	

}
