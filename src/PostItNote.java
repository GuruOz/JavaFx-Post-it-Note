import javafx.application.Application;
import javafx.stage.Stage;



/**
 * 
 * @author Guru
 *
 */
public class PostItNote extends Application {
	
	/**
	 * summary
	 */
	PostItNoteStage mainWindow;

	/**
	 * 
	 * @param args takes in an array of arguments
	 * 
	 */
	public static void main(String[] args) {
		System.out.println("Starting Post-It Note application...");
        System.out.println("Author: Guruparan Prakash");
       	launch(args);
        
    }
	


	@Override public void start(Stage arg0) throws Exception {
				
		 mainWindow = new PostItNoteStage(200, 200, 960, 400);
		 
	}

}
