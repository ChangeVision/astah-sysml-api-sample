import java.awt.geom.Point2D;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

import com.change_vision.jude.api.inf.AstahAPI;
import com.change_vision.jude.api.inf.editor.MindmapEditor;
import com.change_vision.jude.api.inf.editor.TransactionManager;
import com.change_vision.jude.api.inf.exception.InvalidEditingException;
import com.change_vision.jude.api.inf.exception.InvalidUsingException;
import com.change_vision.jude.api.inf.exception.LicenseNotFoundException;
import com.change_vision.jude.api.inf.exception.ProjectLockedException;
import com.change_vision.jude.api.inf.exception.ProjectNotFoundException;
import com.change_vision.jude.api.inf.model.IMindMapDiagram;
import com.change_vision.jude.api.inf.model.IModel;
import com.change_vision.jude.api.inf.presentation.ILinkPresentation;
import com.change_vision.jude.api.inf.presentation.INodePresentation;
import com.change_vision.jude.api.inf.project.ProjectAccessor;

/**
 * Sample source codes for creating an mindmap diagram and all models of mindmap diagram.
 */
public class CreatingSampleSysmlMindmapDiagram {
	public static void main(String[] args) {
		CreatingSampleSysmlMindmapDiagram sample = new CreatingSampleSysmlMindmapDiagram();
		try {
			sample.create("SampleSysmlMindmapDiagram.asml");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (LicenseNotFoundException e) {
			e.printStackTrace();
		} catch (ProjectNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ProjectLockedException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void create(String name) throws ClassNotFoundException,
			LicenseNotFoundException, ProjectNotFoundException, IOException,
			ProjectLockedException {
		
		// ProjectAccessor is an interface to Operate Astah project like creating project and getting project root.
		ProjectAccessor projectAccessor = AstahAPI.getAstahAPI().getProjectAccessor();
		try {
			// Create a project
	        // Please don't forget to save and close the project.
			projectAccessor.create(name);
			
			// Begin transaction when creating or editing models
			// Please don't forget to end the transaction
			TransactionManager.beginTransaction();

			createMindmapDiagram(projectAccessor);

			// End transaction
			TransactionManager.endTransaction();
			
			// Save the project
			projectAccessor.save();

			System.out.println("Create " + name + " Project done.");
		} catch (InvalidEditingException e) {
			e.printStackTrace();
			TransactionManager.abortTransaction();
		} catch (InvalidUsingException e) {
			e.printStackTrace();
			TransactionManager.abortTransaction();
		} finally {
			// Close the project
			projectAccessor.close();
		}
	}

	private void createMindmapDiagram(ProjectAccessor projectAccessor) 
			throws ClassNotFoundException, InvalidUsingException, InvalidEditingException, ProjectNotFoundException, IOException {
		// Get a root model of the project
        // Astah SysML model is tree structure. The project model is the root.
		IModel project = projectAccessor.getProject();
		
		// Diagrams are created by diagramEditor.
		MindmapEditor mme = projectAccessor.getDiagramEditorFactory().getMindmapEditor();
		
		IMindMapDiagram mmDgm = mme.createMindmapDiagram(project, "Mindmap");
		
		// Get root topic of the mindmap
		INodePresentation rootTopicP = mmDgm.getRoot();
		INodePresentation childTopicP = mme.createTopic(rootTopicP, "Child Topic");
		mme.setBoundaryVisibility(childTopicP, true);
		
		INodePresentation floatingTopic1P = mme.createFloatingTopic("FloatingTopic1", new Point2D.Double(500, 150));
		// Set background color for the topic.
		floatingTopic1P.setProperty("fill.color", "#0000FF");
		
		ILinkPresentation mmLinkP = mme.createMMLinkPresentation(floatingTopic1P, childTopicP);
		// Set line color for the link.
		mmLinkP.setProperty("line.color", "#FFFF00");
		
		INodePresentation floatingTopic2P = mme.createFloatingTopic("FloatingTopic2", new Point2D.Double(300, 280));
		// Set an image to a topic.
		mme.setImage(floatingTopic2P, ImageIO.read(getImageInputStream("astah_version_sysml.png")));
		
		INodePresentation textP = mme.createText("Mindmap Sample", new Point2D.Double(250, 150));
		// Set font color for the text.
		textP.setProperty("font.color", "#FF0000");
		
		INodePresentation rectP = mme.createRect(new Point2D.Double(150, 100), 720.0D, 280.0D);
		// Set color for the frame of a topic.
		rectP.setProperty("line.color", "#00FF00");
	}
	
	private static InputStream getImageInputStream(String path) {
		return CreatingSampleSysmlMindmapDiagram.class.getResourceAsStream(path);
	}
	
}
