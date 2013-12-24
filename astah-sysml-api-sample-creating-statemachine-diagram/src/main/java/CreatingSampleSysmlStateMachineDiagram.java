import java.awt.geom.Point2D;
import java.io.IOException;

import com.change_vision.jude.api.inf.AstahAPI;
import com.change_vision.jude.api.inf.editor.ModelEditorFactory;
import com.change_vision.jude.api.inf.editor.StateMachineDiagramEditor;
import com.change_vision.jude.api.inf.editor.SysmlModelEditor;
import com.change_vision.jude.api.inf.editor.TransactionManager;
import com.change_vision.jude.api.inf.exception.InvalidEditingException;
import com.change_vision.jude.api.inf.exception.InvalidUsingException;
import com.change_vision.jude.api.inf.exception.LicenseNotFoundException;
import com.change_vision.jude.api.inf.exception.ProjectLockedException;
import com.change_vision.jude.api.inf.exception.ProjectNotFoundException;
import com.change_vision.jude.api.inf.model.IModel;
import com.change_vision.jude.api.inf.model.IPackage;
import com.change_vision.jude.api.inf.model.IStateMachineDiagram;
import com.change_vision.jude.api.inf.model.ITransition;
import com.change_vision.jude.api.inf.presentation.ILinkPresentation;
import com.change_vision.jude.api.inf.presentation.INodePresentation;
import com.change_vision.jude.api.inf.project.ProjectAccessor;

/**
 * Sample source codes for creating an state machine diagram and all models of state machine diagram.
 */
public class CreatingSampleSysmlStateMachineDiagram {
	public static void main(String[] args) {
		CreatingSampleSysmlStateMachineDiagram sample = new CreatingSampleSysmlStateMachineDiagram();
		try {
			sample.create("SampleSysmlStateMachineDiagram.asml");
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

			createStateMachineAndDiagram(projectAccessor);

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

	private void createStateMachineAndDiagram(ProjectAccessor projectAccessor) 
			throws InvalidEditingException, ClassNotFoundException, ProjectNotFoundException, InvalidUsingException {
		// Get a root model of the project
        // Astah SysML model is tree structure. The project model is the root.
		IModel project = projectAccessor.getProject();
		
		// Most of SysML models like block, constraint parameter can be created by SysMLModelEditor.
		SysmlModelEditor sme = ModelEditorFactory.getSysmlModelEditor();
		IPackage hSUVOperationalStatesPackage = sme.createPackage(project, "HSUVOperationalStates");
		
		// Diagrams are created by diagramEditor
		StateMachineDiagramEditor stmde = projectAccessor.getDiagramEditorFactory().getStateMachineDiagramEditor();
		IStateMachineDiagram stm = stmde.createStatemachineDiagram(hSUVOperationalStatesPackage, "HSUVOperationalStates");
		stmde.setDiagram(stm);

		// There are only two kinds of presentation APIs in Astah.
		// INodePresentation is for those presentations whose shape are like rectangle like block and port.
		// ILinkPresentation is for those presentations whose shape are like line like connector and association.
		INodePresentation initialPseudostate = stmde.createInitialPseudostate(null, new Point2D.Double(0, 0));
		INodePresentation off = stmde.createState("Off", null, new Point2D.Double(200, 0));
		// Action of a state isn't visible
		off.setProperty("action_visibility", "false");
		off.setWidth(100);
		off.setHeight(100);
		INodePresentation finalState = stmde.createFinalState(null, new Point2D.Double(400, 0));
		
		INodePresentation operate = stmde.createState("Operate", null, new Point2D.Double(0, 200));
		// Action of a state isn't visible
		operate.setProperty("action_visibility", "false");
		operate.setWidth(500);
		operate.setHeight(400);
		INodePresentation initialPseudostateInOperate = stmde.createInitialPseudostate(operate, new Point2D.Double(120, 270));
		INodePresentation idle = stmde.createState("Idle", operate, new Point2D.Double(200, 250));
		// Action of a state isn't visible
		idle.setProperty("action_visibility", "false");
		idle.setWidth(100);
		idle.setHeight(50);
		INodePresentation accellerating = stmde.createState("Accellerating/Cruising", operate, new Point2D.Double(20, 400));
		// Action of a state isn't visible
		accellerating.setProperty("action_visibility", "false");
		accellerating.setWidth(100);
		accellerating.setHeight(50);
		INodePresentation braking = stmde.createState("Braking", operate, new Point2D.Double(350, 400));
		// Action of a state isn't visible
		braking.setProperty("action_visibility", "false");
		braking.setWidth(100);
		braking.setHeight(50);
		
		ILinkPresentation transition = stmde.createTransition(initialPseudostate, off);
		// The transition is a right angle.
		// Properties such as color and shape can be set by setProperty.
		transition.setProperty("line.shape", "line_right_angle");
		ILinkPresentation transition2 = stmde.createTransition(off, finalState);
		// The transition is a right angle.
		// Properties such as color and shape can be set by setProperty.
		transition2.setProperty("line.shape", "line_right_angle");
		Point2D.Double[] pnts2 = {
				new Point2D.Double(off.getLocation().getX() + off.getWidth(), finalState.getLocation().getY() + 5.0D), 
				new Point2D.Double(finalState.getLocation().getX(), finalState.getLocation().getY() + 5.0D)};
		transition2.setAllPoints(pnts2);
		((ITransition)transition2.getModel()).setEvent("KeyOff");
		ILinkPresentation transition3 = stmde.createTransition(off, operate);
		// The transition is a right angle.
		// Properties such as color and shape can be set by setProperty.
		transition3.setProperty("line.shape", "line_right_angle");
		((ITransition)transition3.getModel()).setEvent("start");
		Point2D.Double[] pnts3 = {new Point2D.Double(200, 50), 
				new Point2D.Double(100, 50), 
				new Point2D.Double(100, 200)};
		transition3.setAllPoints(pnts3);
		ILinkPresentation transition4 = stmde.createTransition(operate, off);
		// The transition is a right angle.
		// Properties such as color and shape can be set by setProperty.
		transition4.setProperty("line.shape", "line_right_angle");
		((ITransition)transition4.getModel()).setEvent("shutOff");
		Point2D.Double[] pnts4 = {new Point2D.Double(400, 400), 
				new Point2D.Double(400, 50), 
				new Point2D.Double(300, 50)};
		transition4.setAllPoints(pnts4);
		ILinkPresentation transition5 = stmde.createTransition(initialPseudostateInOperate, idle);
		// The transition is a right angle.
		// Properties such as color and shape can be set by setProperty.
		transition5.setProperty("line.shape", "line_right_angle");
		ILinkPresentation transition6 = stmde.createTransition(idle, accellerating);
		// The transition is a right angle.
		// Properties such as color and shape can be set by setProperty.
		transition6.setProperty("line.shape", "line_right_angle");
		((ITransition)transition6.getModel()).setEvent("accelerate");
		Point2D.Double[] pnts6 = {new Point2D.Double(250, 300), 
				new Point2D.Double(250, 350), 
				new Point2D.Double(70, 350), 
				new Point2D.Double(70, 450)};
		transition6.setAllPoints(pnts6);
		ILinkPresentation transition7 = stmde.createTransition(accellerating, braking);
		// The transition is a right angle.
		// Properties such as color and shape can be set by setProperty.
		transition7.setProperty("line.shape", "line_right_angle");
		((ITransition)transition7.getModel()).setEvent("engageBrake");
		ILinkPresentation transition8 = stmde.createTransition(braking, accellerating);
		// The transition is a right angle.
		// Properties such as color and shape can be set by setProperty.
		transition8.setProperty("line.shape", "line_right_angle");
		((ITransition)transition8.getModel()).setEvent("releaseBrake");
		Point2D.Double[] pnts8 = {new Point2D.Double(400, 450), 
				new Point2D.Double(400, 500), 
				new Point2D.Double(70, 500), 
				new Point2D.Double(70, 450)};
		transition8.setAllPoints(pnts8);
		ILinkPresentation transition9 = stmde.createTransition(braking, idle);
		// The transition is a right angle.
		// Properties such as color and shape can be set by setProperty.
		transition9.setProperty("line.shape", "line_right_angle");
		((ITransition)transition9.getModel()).setEvent("stopped");
	}
}
