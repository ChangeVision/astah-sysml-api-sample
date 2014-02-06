import java.awt.geom.Point2D;
import java.io.IOException;

import com.change_vision.jude.api.inf.AstahAPI;
import com.change_vision.jude.api.inf.editor.ModelEditorFactory;
import com.change_vision.jude.api.inf.editor.SequenceDiagramEditor;
import com.change_vision.jude.api.inf.editor.SysmlModelEditor;
import com.change_vision.jude.api.inf.editor.TransactionManager;
import com.change_vision.jude.api.inf.editor.UseCaseModelEditor;
import com.change_vision.jude.api.inf.exception.InvalidEditingException;
import com.change_vision.jude.api.inf.exception.InvalidUsingException;
import com.change_vision.jude.api.inf.exception.LicenseNotFoundException;
import com.change_vision.jude.api.inf.exception.ProjectLockedException;
import com.change_vision.jude.api.inf.exception.ProjectNotFoundException;
import com.change_vision.jude.api.inf.model.IBlock;
import com.change_vision.jude.api.inf.model.IClass;
import com.change_vision.jude.api.inf.model.ICombinedFragment;
import com.change_vision.jude.api.inf.model.ILifeline;
import com.change_vision.jude.api.inf.model.IModel;
import com.change_vision.jude.api.inf.model.IPackage;
import com.change_vision.jude.api.inf.model.ISequenceDiagram;
import com.change_vision.jude.api.inf.presentation.INodePresentation;
import com.change_vision.jude.api.inf.project.ProjectAccessor;

/**
 * Sample source codes for creating an sequence diagram and all models of sequence diagram.
 */
public class CreatingSampleSysmlSequenceDiagram {
	public static void main(String[] args) {
		CreatingSampleSysmlSequenceDiagram sample = new CreatingSampleSysmlSequenceDiagram();
		try {
			sample.create("SampleSysmlSequenceDiagram.asml");
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

			createSequenceAndDiagram(projectAccessor);

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
			projectAccessor.close();
		}
	}

	private void createSequenceAndDiagram(ProjectAccessor projectAccessor)
			throws InvalidEditingException, ClassNotFoundException,
			ProjectNotFoundException, InvalidUsingException {
		// Get a root model of the project
        // Astah SysML model is tree structure. The project model is the root.
		IModel project = projectAccessor.getProject();

		// Most of SysML models like block, constraint parameter can be created by SysMLModelEditor.
		SysmlModelEditor sme = ModelEditorFactory.getSysmlModelEditor();
		IPackage hSUVStructurePackage = sme.createPackage(project,
				"HSUVStructure");
		
		// Models on usecase diagram can be created by UseCaseModelEditor.
		UseCaseModelEditor ucme = ModelEditorFactory.getUseCaseModelEditor();
		IClass driverActor = ucme.createActor(hSUVStructurePackage, "Driver");
		IBlock hybridSUVBlock = sme.createBlock(hSUVStructurePackage,
				"HybridSUV");
		hybridSUVBlock.addStereotype("system");

		// Diagrams are created by diagramEditor.
		SequenceDiagramEditor seqde = projectAccessor.getDiagramEditorFactory()
				.getSequenceDiagramEditor();
		ISequenceDiagram seq = seqde.createSequenceDiagram(
				hSUVStructurePackage, "DriveBlackBox");
		// SequenceDiagramEditor can be used to create presentations on a sequence diagram.
		// Target diagram must be set to diagramEditor.
		seqde.setDiagram(seq);

		// There are only two kinds of presentation APIs in Astah.
		// INodePresentation is for those presentations whose shape are like rectangle like block and port.
		// ILinkPresentation is for those presentations whose shape are like line like connector and association.
		INodePresentation driverP = seqde.createLifeline("driver", 0.0D);
		driverP.setProperty("stereotype_visibility", "false");
		((ILifeline) driverP.getModel()).setBase(driverActor);
		INodePresentation vehiclelnContextP = seqde.createLifeline(
				"vehiclelnContext", 500.0D);
		vehiclelnContextP.setProperty("stereotype_visibility", "false");
		((ILifeline) vehiclelnContextP.getModel()).setBase(hybridSUVBlock);

		INodePresentation startVehicleBlackBoxP = seqde.createInteractionUse(
				"StartVehicleBlackBox", "", null, new Point2D.Double(0, 150),
				650.0D, 80.0D);
		INodePresentation parP = seqde.createCombinedFragment("", "par",
				new Point2D.Double(-50, 300), 750.0D, 520.0D);
		ICombinedFragment par = (ICombinedFragment) parP.getModel();
		par.addInteractionOperand("", "self.oclInState(accelerating/cruising)");
		// Set length of the first operand is set as 370
		parP.setProperty("operand.1.length", "370");
		INodePresentation controlSpeedP = seqde.createCombinedFragment(
				"controlSpeed", "alt", new Point2D.Double(0, 350), 650.0D,
				300.0D);
		ICombinedFragment controlSpeed = (ICombinedFragment) controlSpeedP
				.getModel();
		controlSpeed.getInteractionOperands()[0]
				.setGuard("self.oclInState(idle)");
		controlSpeed.addInteractionOperand("",
				"self.oclInState(accelerating/cruising)");
		controlSpeed.addInteractionOperand("", "self.oclInState(braking)");
		controlSpeedP.setProperty("operand.1.length", "100");
		controlSpeedP.setProperty("operand.2.length", "100");
		INodePresentation idleP = seqde.createInteractionUse("Idle", "", null,
				new Point2D.Double(50, 370), 550.0D, 50.0D);
		INodePresentation accelerateP = seqde.createInteractionUse(
				"Accelerate/Cruise", "", null, new Point2D.Double(50, 470),
				550.0D, 50.0D);
		INodePresentation brakeP = seqde.createInteractionUse("Brake", "",
				null, new Point2D.Double(50, 570), 550.0D, 50.0D);
		INodePresentation steerP = seqde.createInteractionUse("Steer", "",
				null, new Point2D.Double(0, 700), 650.0D, 80.0D);
		INodePresentation parkP = seqde.createInteractionUse(
				"Park/ShutdownVehicle", "", null, new Point2D.Double(-50, 830),
				700.0D, 80.0D);
	}
}
