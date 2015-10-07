package gui;

import controller.AbstractPlaylistController;
import javafx.scene.layout.Region;

public abstract class AbstractEditorPane extends Region{
	
	public abstract void setListener(PlaylistEditor e);
	public abstract void setController(AbstractPlaylistController c);
	

}
