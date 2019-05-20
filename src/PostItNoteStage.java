

import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.input.DataFormat;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * This class implements the stage and handles all mouse and text input
 * @author Guruparan Prakash
 * @version 1.0
 *
 */
public class PostItNoteStage {
	
	
	//Borderpane is for the textarea
	BorderPane content;
	//Borderpane for the buttons on the top
	BorderPane buttonArea;
	//Borderpane for the bottom. This will contain the resize button
	BorderPane bottom;
	
	//Button to open a new post it
	Button newPostItNote;
	//Button to close the current post it
	Button deletePostItNote;
	//To set the font for the new or delete button3 
	Font buttonFont;
	//Button to click, hold and drag around to change the window size
	Button resize;
	
	
	//Text area for content pane
	TextArea textArea;
	
	//stores the horizontal position of the event relative to the origin of the mouse event
	double x;
	//stores the vertical position of the event relative to the origin of the mouse event
	double y;
	
	//Context menu for the right click
	ContextMenu rightClickMenu;
	//Menu items
	MenuItem cut;
	MenuItem copy;
	MenuItem paste;
	MenuItem about;
	MenuItem exit;
	
	//To access the OS clipboard
	Clipboard clipboard;
	ClipboardContent textContent;
	
	//These store the mouse coordiantes from an event
	double mousedragx;
	double mousedragy;
	double dragX;
	double dragY;
	double width;
	double height;
	
	
	//stores the number of times the "new postitnote button" has been clicked on that instance
	int iteration = 0;
	
	
	/**
	 * This constructs a stage with the specified height, width and screen position
	 * @param sizeX The width of the application
	 * @param sizeY The height of the application
	 * @param positionX The starting X position on the screen
	 * @param positionY The starting Y position on the screen
	 */	
	public PostItNoteStage(double sizeX, double sizeY, double positionX, double positionY) {
		
		//Initialising the variables
		content = new BorderPane();
		buttonArea = new BorderPane();
		bottom = new BorderPane();
		newPostItNote = new Button("+");
		deletePostItNote = new Button("x");
		resize = new Button();
		textArea = new TextArea();			
		
		//making the resize button smaller
		resize.setPadding(Insets.EMPTY);
		resize.setMaxHeight(2);
		resize.setMaxWidth(2);
		
		//setting button font
		buttonFont = Font.font("Arial", FontWeight.BOLD,20);	
		
		//adding buttons to the button area
		buttonArea.setLeft(newPostItNote);
		buttonArea.setRight(deletePostItNote);
		
		//creating a new stage
		Stage stage = new Stage();		
		
		//taking out the default control buttons that come standard
		stage.initStyle(StageStyle.UNDECORATED);
		
		//taking the parameters given to set the position of the window on the screen
		stage.setX(positionX);
		stage.setY(positionY);
		
		//Creating a new scene and using a borderpane as the root node
		Scene scene = new Scene(content, sizeY, sizeX);
		
		//Setting the colors and adding some flair
		content.setStyle("-fx-background-color: rgb(248, 253, 201)");
		buttonArea.setStyle("-fx-background-color: rgb(248, 247, 182)");
		newPostItNote.setStyle("-fx-background-color: rgb(248, 247, 182)");
		deletePostItNote.setStyle("-fx-background-color: rgb(248, 247, 182)");	
		textArea.setStyle("-fx-background-color: rgb(248, 253, 201)");
		resize.setStyle("-fx-background-color: rgb(248, 253, 201)");
		newPostItNote.setFont(buttonFont);
		newPostItNote.setTextFill(Color.GREY);
		deletePostItNote.setFont(buttonFont);
		deletePostItNote.setTextFill(Color.GREY);
		content.setTop(buttonArea);
		content.setCenter(textArea);
		
		//Setting the scene we just creating into the stage.
		stage.setScene(scene);
		stage.show();
		
		
		//Making the window resizable
		buttonArea.setOnMousePressed(e->{
			mousedragx=e.getSceneX();
			mousedragy=e.getSceneY();				
		});
		
		buttonArea.setOnMouseDragged(e->{		
						
			/**
			 * finds the difference between the coordinates when the mouse was press and when the mouse is dragged.
			 * That allows us to find the 
			 */
			stage.setX(e.getScreenX()-mousedragx);
			stage.setY(e.getScreenY()-mousedragy);		
			
		});
		
		//Setting up the resize button...
		content.setBottom(resize);
		BorderPane.setAlignment(resize, Pos.BOTTOM_RIGHT);
		
		//Adding an arrow image to the button
		Image resizeImage = new Image(getClass().getResourceAsStream("arrow.png"));
		ImageView resizeImageView = new ImageView(resizeImage);		
		resize.setGraphic(resizeImageView);
					
		/**
		 * Saves the current stage width and height, and saves the event's mouse coordinates.
		 */
		resize.setOnMousePressed(e->{
			dragX=e.getSceneX();
			dragY=e.getSceneY();
			height = stage.getHeight();
			width = stage.getWidth();
			
		});
		
		//add functionality to the resize button
		resize.setOnMouseDragged(e->{	
			
			/**
			 * Adds the current difference in the cursor start point and end point, adds that to the current height and width,
			 * and the resultant number is used to set the height and width.
			 */
			stage.setHeight(height+(e.getSceneY()-dragY));
			stage.setWidth(width+(e.getSceneX()-dragX));
			//set the min height and width
			if (stage.getHeight() < sizeY || stage.getWidth() < sizeX) {
				
				stage.setHeight(sizeY);
				stage.setWidth(sizeX);
			}
			});
		
		//Changing the cursor when on the resize button to indicate its interactable
		resize.setOnMouseEntered(e->{
			scene.setCursor(Cursor.SE_RESIZE);
		});
		resize.setOnMouseExited(e->{
			scene.setCursor(Cursor.DEFAULT);
		});
		
		
		Region region = (Region) textArea.lookup(".content");
		region.setStyle("-fx-background-color: rgb(253, 253, 201)");
		
			//Adds an event for the 'new' button
			newPostItNote.setOnAction(e -> {
								
				 x = stage.getX();
				 y = stage.getY();
				 
				 //
				 /**
				  *int iteration saves the number of times the button has been pressed in this instance. 
				  *we are then multiplying the width with that number and adding to the Y coordinates 
				  *so the new windows do no overlap
				  */
				 double newX = x+stage.getWidth()+10+(stage.getWidth()*iteration);
				 double newY = y;
				 Rectangle2D screen = Screen.getPrimary().getVisualBounds();
				 if(stage.getX() + stage.getWidth()*2 > screen.getWidth()){
				 newX = 0+(stage.getWidth()*iteration);
				 newY = stage.getY() + stage.getHeight();
				 }
			     new PostItNoteStage(sizeX,sizeY,newX,newY);
				
			     //increments the iteration by 1
				iteration++;
				
			});
			
			//very self explanatory
			deletePostItNote.setOnAction(e -> {
				stage.close();
			});
			
			/**
			 * Creating new context menu and menu items to put inside
			 */
			rightClickMenu = new ContextMenu();
			cut = new MenuItem("Cut");
			copy = new MenuItem("Copy");
			paste = new MenuItem("Paste");
			about = new MenuItem("About");
			exit = new MenuItem("Exit");
			//adding the menuitems to the contextmenu
			rightClickMenu.getItems().addAll(cut,copy,paste,about,exit);
					
			
			//Filter to get rid of the default context menu
			textArea.addEventFilter(ContextMenuEvent.CONTEXT_MENU_REQUESTED, Event::consume);			
			
			
			//Adding our own context menu
			textArea.setOnMouseClicked(e-> {
				
				/**
				 * If the user right clicks on the textarea, out custom context menu pops up
				 */
				if(e.getButton() == MouseButton.SECONDARY)  {
					
					rightClickMenu.show(content, e.getScreenX(), e.getScreenY());			
							 
				}
				
				/**
				 * If the user wants to get rid of the context menu, they simply need to left click anywhere on the textarea.
				 */
				else if (e.getButton()== MouseButton.PRIMARY) {
					//checks if the context menu is visable
					if (rightClickMenu.isShowing()) {
						
						rightClickMenu.hide();						
					}					
				}
			});
			
			cut.setOnAction(e ->{
				
				Clipboard clipboard = Clipboard.getSystemClipboard();
				ClipboardContent textContent = new ClipboardContent();
				textContent.putString(textArea.getSelectedText());
		        clipboard.setContent(textContent);
		        textArea.deleteText(textArea.getSelection());
							
			});
			paste.setOnAction(e ->{
							
				textArea.appendText(Clipboard.getSystemClipboard().getString());
			});
			copy.setOnAction(e->{
				
				Clipboard clipboard = Clipboard.getSystemClipboard();
				ClipboardContent textContent = new ClipboardContent();
				textContent.putString(textArea.getSelectedText());
		        clipboard.setContent(textContent);
		        
			});
			
			//rightclick event listener for about
			about.setOnAction(e->{
				
				Alert alert = new Alert(AlertType.INFORMATION);	
				GridPane grid = new GridPane();
				Label label1 = new Label("Digital Post-It Note Using JavaFX");	
				Label label2 = new Label("Version 1.1");
				Label label3 = new Label("Author: GuruOz");
				Label label4 = new Label("Copyright (c) 2019");
				Image image = new Image(getClass().getResource("pic.jpg").toExternalForm());
				ImageView imageView = new ImageView(image);
				imageView.setFitHeight(100);
				imageView.setFitWidth(100);				
				
				//place the image to the left and span across 5 rows
				grid.add(imageView, 0, 0, 1, 5);
				
				grid.add(label1,1,0);
				grid.add(label2, 1, 1);
				grid.add(label3, 1, 2);
				grid.add(label4, 1, 3);
				alert.getDialogPane().setContent(grid);
				alert.showAndWait();
				
			});
			
			//rightclick event listener for exit
			exit.setOnAction(e->{
				stage.close();
			});
	}
		
}
