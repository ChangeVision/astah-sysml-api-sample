
import java.awt.geom.Point2D;
import java.io.IOException;

import com.change_vision.jude.api.inf.AstahAPI;
import com.change_vision.jude.api.inf.editor.BlockDefinitionDiagramEditor;
import com.change_vision.jude.api.inf.editor.ModelEditorFactory;
import com.change_vision.jude.api.inf.editor.SysmlModelEditor;
import com.change_vision.jude.api.inf.editor.TransactionManager;
import com.change_vision.jude.api.inf.exception.InvalidEditingException;
import com.change_vision.jude.api.inf.exception.LicenseNotFoundException;
import com.change_vision.jude.api.inf.exception.ProjectLockedException;
import com.change_vision.jude.api.inf.exception.ProjectNotFoundException;
import com.change_vision.jude.api.inf.model.IAssociation;
import com.change_vision.jude.api.inf.model.IBlock;
import com.change_vision.jude.api.inf.model.IGeneralization;
import com.change_vision.jude.api.inf.model.IModel;
import com.change_vision.jude.api.inf.model.IPackage;
import com.change_vision.jude.api.inf.presentation.INodePresentation;
import com.change_vision.jude.api.inf.project.ProjectAccessor;


/**
 * Sample source codes for creating a block definition Diagram and block presentations by Astah SysML API.
 * Create models of a package, blocks and relations, and then create a block definition diagram
 * and other presentations. 
 */
public class APIForCreatingPresentationsSample {
		
	public static void main(String[] args) {
		
		try {
            if(args.length != 1){
                System.err.println("Usage: astah-mvn -q exec:exec -DmodelPath=YOUR_MODEL_PATH. YOUR_MODEL_PATH should be added.");
                System.exit(1);
            }
            String projectPath = args[0];
            
			System.out.println("Creating new project...");
			
            // Create a project and get a root model
			ProjectAccessor prjAccessor = AstahAPI.getAstahAPI().getProjectAccessor();
			prjAccessor.create(projectPath);
			
			// Get a project model. 
			// Please don't forget to close the project
            // Astah SysML model is tree structure. The project model is root.
			IModel project = prjAccessor.getProject();
			
			System.out.println("Creating new elements in the project...");
			
			// Begin transaction when creating or editing models
			TransactionManager.beginTransaction();
			
			// -----<<< Create Model >>>-----
			SysmlModelEditor sysmlModelEditor = ModelEditorFactory.getSysmlModelEditor();
			
			// Create a package
			IPackage packageA = sysmlModelEditor.createPackage(project, "PackageA");
			
			// Create a block in the specified package
			IBlock iBlockA = sysmlModelEditor.createBlock(packageA, "BlockA");
			// Add an operation to the block
			sysmlModelEditor.createOperation(iBlockA, "operation0", "void");
			
			// Create blocks and an interface in the specified package
			IBlock iBlockB = sysmlModelEditor.createBlock(packageA, "BlockB");
			IBlock iBlockC = sysmlModelEditor.createBlock(packageA, "BlockC");
			
			// Add an association between blocks
			IAssociation association 
				= sysmlModelEditor.createAssociation(iBlockA, iBlockB, "association name",
					"blockA end", "blockB end");
			// Add a generalization between blocks
			IGeneralization generalization 
				= sysmlModelEditor.createGeneralization(iBlockC, iBlockA, "generalization name");
			
			// -----<<< Create Block Definition Diagram >>>-----
			BlockDefinitionDiagramEditor blockDefinitionDiagramEditor 
				= AstahAPI.getAstahAPI().getProjectAccessor().getDiagramEditorFactory().getBlockDefinitionDiagramEditor();

			// Create a block definition diagram in the specified package
			blockDefinitionDiagramEditor.createBlockDefinitionDiagram(packageA, "Block Definition DiagramA");

			// Create block presentations in the block definition diagram
			Point2D locationA = new Point2D.Double(10.0d, 10.0d);
			INodePresentation blockAPs = blockDefinitionDiagramEditor.createNodePresentation(iBlockA, locationA);
			
			Point2D locationB = new Point2D.Double(300.0d, 25.0d);
			INodePresentation blockBPs = blockDefinitionDiagramEditor.createNodePresentation(iBlockB, locationB);
			
			Point2D locationC = new Point2D.Double(45.0d, 200.0d);
			INodePresentation blockCPs = blockDefinitionDiagramEditor.createNodePresentation(iBlockC, locationC);
						
			// Add an association presentation in the block definition diagram
			blockDefinitionDiagramEditor.createLinkPresentation(association, blockAPs, blockBPs);
			// Add a generalization presentation in the block definition diagram
			blockDefinitionDiagramEditor.createLinkPresentation(generalization, blockAPs, blockCPs);
			
			// Add color to a block presentation
			blockAPs.setProperty("fill.color", "#FF0000");
			
			// End transaction
			TransactionManager.endTransaction();
			
			// Save project
			prjAccessor.save();
			
			// Close project
			prjAccessor.close();
			
			System.out.println("Finished");
			
		} catch (LicenseNotFoundException e) {
			e.printStackTrace();
		} catch (ProjectNotFoundException e) {
			e.printStackTrace();
		} catch (ProjectLockedException e) {
        	// If the target project is being used, ProjectLoctedException will be thrown
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (InvalidEditingException e) {
			// Abort transaction
			TransactionManager.abortTransaction();
			// Get an exception message
			System.err.print(e.getMessage());
			e.printStackTrace();
		} catch (IOException e ) {
			e.printStackTrace();
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}
}