import java.awt.geom.Point2D;
import java.io.IOException;

import com.change_vision.jude.api.inf.AstahAPI;
import com.change_vision.jude.api.inf.editor.ActivityDiagramEditor;
import com.change_vision.jude.api.inf.editor.ModelEditorFactory;
import com.change_vision.jude.api.inf.editor.SysmlModelEditor;
import com.change_vision.jude.api.inf.editor.TransactionManager;
import com.change_vision.jude.api.inf.exception.InvalidEditingException;
import com.change_vision.jude.api.inf.exception.InvalidUsingException;
import com.change_vision.jude.api.inf.exception.LicenseNotFoundException;
import com.change_vision.jude.api.inf.exception.ProjectLockedException;
import com.change_vision.jude.api.inf.exception.ProjectNotFoundException;
import com.change_vision.jude.api.inf.model.IActivityDiagram;
import com.change_vision.jude.api.inf.model.IFlow;
import com.change_vision.jude.api.inf.model.IModel;
import com.change_vision.jude.api.inf.model.IPackage;
import com.change_vision.jude.api.inf.presentation.ILinkPresentation;
import com.change_vision.jude.api.inf.presentation.INodePresentation;
import com.change_vision.jude.api.inf.project.ProjectAccessor;

/**
 * Sample source codes for creating an activity diagram and all models of activity diagram.
 */
public class CreatingSampleSysmlActivityDiagram {
	public static void main(String[] args) {
		CreatingSampleSysmlActivityDiagram sample = new CreatingSampleSysmlActivityDiagram();
		try {
			sample.create("SampleSysmlActivityDiagram.asml");
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
		ProjectAccessor projectAccessor = AstahAPI.getAstahAPI()
				.getProjectAccessor();
		try {
			// Create a project
	        // Please don't forget to save and close the project.
			projectAccessor.create(name);
			
			// Begin transaction when creating or editing models
			// Please don't forget to end the transaction
			TransactionManager.beginTransaction();

			createActivityAndDiagram(projectAccessor);

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

	private void createActivityAndDiagram(ProjectAccessor projectAccessor)
			throws InvalidEditingException, ClassNotFoundException,
			ProjectNotFoundException, InvalidUsingException {
		
		// Get a root model of the project
        // Astah SysML model is tree structure. The project model is the root.
		IModel project = projectAccessor.getProject();

		// Most of SysML models like block, constraint parameter can be created by SysMLModelEditor.
		SysmlModelEditor sme = ModelEditorFactory.getSysmlModelEditor();
		IPackage hSUVActivityPackage = sme.createPackage(project,
				"HSUVActivity");

		// Diagrams are created by diagramEditor.
		ActivityDiagramEditor actde = projectAccessor.getDiagramEditorFactory()
				.getActivityDiagramEditor();
		IActivityDiagram dgm = actde.createActivityDiagram(hSUVActivityPackage,
				"Enable on Brake Pressure > 0");
		
		// There are only two kinds of presentation APIs in Astah.
		// INodePresentation is for those presentations whose shape are like rectangle like block and port.
		// ILinkPresentation is for those presentations whose shape are like line like connector and association.
		INodePresentation brakePressureP = actde.createActivityParameterNode(
				"Brake Pressure", null, new Point2D.Double(0, 200));
		INodePresentation decisionP = actde.createDecisionMergeNode(null,
				new Point2D.Double(150, 200));
		INodePresentation enableP = actde.createAction("enable",
				new Point2D.Double(250, 100));
		enableP.getModel().addStereotype("ValueSpecificationAction");
		INodePresentation disableP = actde.createAction("disable",
				new Point2D.Double(250, 300));
		disableP.getModel().addStereotype("ValueSpecificationAction");
		INodePresentation mergeP = actde.createDecisionMergeNode(null,
				new Point2D.Double(500, 200));
		INodePresentation controlValueP = actde.createActivityParameterNode(
				"ControlValue", null, new Point2D.Double(650, 200));

		ILinkPresentation flowP = actde.createFlow(brakePressureP, decisionP);
		
		// The flow between brakePressure and decision is a right angle.
		// Properties such as color and shape can be set by setProperty.
		flowP.setProperty("line.shape", "line_right_angle");
		ILinkPresentation flow2P = actde.createFlow(decisionP, enableP);
		((IFlow) flow2P.getModel()).setGuard("[Brake Pressure > 0]");
		flow2P.setProperty("line.shape", "line_right_angle");
		Point2D decisionPlocation = decisionP.getLocation();
		Point2D enablePlocation = enableP.getLocation();
		Point2D disablePlocation = disableP.getLocation();
		Point2D mergePlocation = mergeP.getLocation();
		Point2D.Double[] pnts2 = {
				new Point2D.Double(decisionPlocation.getX()
						+ decisionP.getWidth() / 2.0D, decisionPlocation.getY()),
				new Point2D.Double(decisionPlocation.getX()
						+ decisionP.getWidth() / 2.0D, enablePlocation.getY()
						+ enableP.getHeight() / 2.0D),
				new Point2D.Double(enablePlocation.getX(),
						enablePlocation.getY() + enableP.getHeight() / 2.0D) };
		flow2P.setAllPoints(pnts2);
		ILinkPresentation flow3P = actde.createFlow(decisionP, disableP);
		((IFlow) flow3P.getModel()).setGuard("else");
		
		// The flow between brakePressure and decision is a right angle.
		flow3P.setProperty("line.shape", "line_right_angle");
		Point2D.Double[] pnts3 = {
				new Point2D.Double(decisionPlocation.getX()
						+ decisionP.getWidth() / 2.0D, decisionPlocation.getY()
						+ decisionP.getHeight()),
				new Point2D.Double(decisionPlocation.getX()
						+ decisionP.getWidth() / 2.0D, disablePlocation.getY()
						+ disableP.getHeight() / 2.0D),
				new Point2D.Double(disablePlocation.getX(),
						disablePlocation.getY() + disableP.getHeight() / 2.0D) };
		flow3P.setAllPoints(pnts3);
		ILinkPresentation flow4P = actde.createFlow(enableP, mergeP);
		
		// The flow between brakePressure and decision is a right angle.
		flow4P.setProperty("line.shape", "line_right_angle");
		Point2D.Double[] pnts4 = {
				new Point2D.Double(enablePlocation.getX() + enableP.getWidth(),
						enablePlocation.getY() + enableP.getHeight() / 2.0D),
				new Point2D.Double(mergePlocation.getX() + mergeP.getWidth()
						/ 2.0D, enablePlocation.getY() + enableP.getHeight()
						/ 2.0D),
				new Point2D.Double(mergePlocation.getX() + mergeP.getWidth()
						/ 2.0D, mergePlocation.getY()) };
		flow4P.setAllPoints(pnts4);
		ILinkPresentation flow5P = actde.createFlow(disableP, mergeP);
		
		// The flow between brakePressure and decision is a right angle.
		flow5P.setProperty("line.shape", "line_right_angle");
		Point2D.Double[] pnts5 = {
				new Point2D.Double(disablePlocation.getX()
						+ disableP.getWidth(), disablePlocation.getY()
						+ disableP.getHeight() / 2.0D),
				new Point2D.Double(mergePlocation.getX() + mergeP.getWidth()
						/ 2.0D, disablePlocation.getY() + disableP.getHeight()
						/ 2.0D),
				new Point2D.Double(mergePlocation.getX() + mergeP.getWidth()
						/ 2.0D, mergePlocation.getY() + mergeP.getHeight()) };
		flow5P.setAllPoints(pnts5);
		ILinkPresentation flow6P = actde.createFlow(mergeP, controlValueP);
		
		// The flow between brakePressure and decision is a right angle.
		flow6P.setProperty("line.shape", "line_right_angle");
	}
}
