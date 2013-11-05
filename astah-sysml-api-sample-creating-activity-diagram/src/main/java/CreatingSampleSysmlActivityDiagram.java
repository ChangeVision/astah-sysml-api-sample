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
		ProjectAccessor projectAccessor = AstahAPI.getAstahAPI()
				.getProjectAccessor();
		try {
			projectAccessor.create(name);
			TransactionManager.beginTransaction();

			createActivityAndDiagram(projectAccessor);

			TransactionManager.endTransaction();
			projectAccessor.save();

			System.out.println("Create " + name + " Project done.");
		} catch (InvalidEditingException e) {
			e.printStackTrace();
		} catch (InvalidUsingException e) {
			e.printStackTrace();
		} finally {
			TransactionManager.abortTransaction();
			projectAccessor.close();
		}
	}

	private void createActivityAndDiagram(ProjectAccessor projectAccessor)
			throws InvalidEditingException, ClassNotFoundException,
			ProjectNotFoundException, InvalidUsingException {
		IModel project = projectAccessor.getProject();

		SysmlModelEditor sme = ModelEditorFactory.getSysmlModelEditor();
		IPackage hSUVActivityPackage = sme.createPackage(project,
				"HSUVActivity");

		ActivityDiagramEditor actde = projectAccessor.getDiagramEditorFactory()
				.getActivityDiagramEditor();
		IActivityDiagram dgm = actde.createActivityDiagram(hSUVActivityPackage,
				"Enable on Brake Pressure > 0");
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
		flow6P.setProperty("line.shape", "line_right_angle");
	}
}
