import java.awt.geom.Point2D;
import java.io.IOException;

import com.change_vision.jude.api.inf.editor.ModelEditorFactory;
import com.change_vision.jude.api.inf.editor.SysmlModelEditor;
import com.change_vision.jude.api.inf.editor.TransactionManager;
import com.change_vision.jude.api.inf.editor.UseCaseDiagramEditor;
import com.change_vision.jude.api.inf.editor.UseCaseModelEditor;
import com.change_vision.jude.api.inf.exception.InvalidEditingException;
import com.change_vision.jude.api.inf.exception.InvalidUsingException;
import com.change_vision.jude.api.inf.exception.LicenseNotFoundException;
import com.change_vision.jude.api.inf.exception.ProjectLockedException;
import com.change_vision.jude.api.inf.exception.ProjectNotFoundException;
import com.change_vision.jude.api.inf.model.IClass;
import com.change_vision.jude.api.inf.model.IModel;
import com.change_vision.jude.api.inf.model.IPackage;
import com.change_vision.jude.api.inf.model.IUseCase;
import com.change_vision.jude.api.inf.model.IUseCaseDiagram;
import com.change_vision.jude.api.inf.presentation.INodePresentation;
import com.change_vision.jude.api.inf.project.ProjectAccessor;

/**
 * Sample source codes for creating an usecase diagram and all models of usecase diagram.
 */
public class CreatingSampleSysmlUseCaseDiagram {
	public static void main(String[] args) {
		CreatingSampleSysmlUseCaseDiagram sample = new CreatingSampleSysmlUseCaseDiagram();
		try {
			sample.create("SampleSysmlUseCaseDiagram.asml");
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
		ProjectAccessor projectAccessor = AstahUtil.getProjectAccessor();
		try {
			// Create a project
	        // Please don't forget to save and close the project.
			projectAccessor.create(name);
			
			// Begin transaction when creating or editing models
			// Please don't forget to end the transaction
			TransactionManager.beginTransaction();

			createUseCaseModel(projectAccessor);

			createUseCaseAndDiagram(projectAccessor);

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

	private void createUseCaseModel(ProjectAccessor projectAccessor)
			throws InvalidEditingException, ClassNotFoundException,
			ProjectNotFoundException, InvalidUsingException {
		// Get a root model of the project
        // Astah SysML model is tree structure. The project model is the root.
		IModel project = projectAccessor.getProject();
		
		// Most of SysML models like block, constraint parameter can be created by SysMLModelEditor.
		SysmlModelEditor sme = ModelEditorFactory.getSysmlModelEditor();
		IPackage hSUVUseCasesPackage = sme.createPackage(project,
				"HSUVUseCases");
		// Models on usecase diagram can be created by UseCaseModelEditor.
		UseCaseModelEditor ucme = ModelEditorFactory.getUseCaseModelEditor();
		IClass departmentActor = ucme.createActor(hSUVUseCasesPackage,
				"Department Of Motor Vehicles");
		IClass driverActor = ucme.createActor(hSUVUseCasesPackage, "Driver");
		IClass insuranceCompanyActor = ucme.createActor(hSUVUseCasesPackage,
				"InsuranceCompany");
		IClass maintainerActor = ucme.createActor(hSUVUseCasesPackage,
				"Maintainer");
		IClass registeredActor = ucme.createActor(hSUVUseCasesPackage,
				"Registered Owner");
		IUseCase accelerateUseCase = ucme.createUseCase(hSUVUseCasesPackage,
				"Accelerate");
		IUseCase insureUseCase = ucme.createUseCase(hSUVUseCasesPackage,
				"Insure the vehicle");
		IUseCase maintainUseCase = ucme.createUseCase(hSUVUseCasesPackage,
				"Maintain the vehicle");
		IUseCase operateUseCase = ucme.createUseCase(hSUVUseCasesPackage,
				"Operate the vehicle");
		IUseCase registerUseCase = ucme.createUseCase(hSUVUseCasesPackage,
				"Register the vehicle");

		sme.createAssociation(driverActor, operateUseCase, "", "", "");
		sme.createAssociation(registeredActor, insureUseCase, "", "", "");
		sme.createAssociation(registeredActor, registerUseCase, "", "", "");
		sme.createAssociation(maintainerActor, maintainUseCase, "", "", "");
		sme.createAssociation(insuranceCompanyActor, insureUseCase, "", "", "");
		sme.createAssociation(departmentActor, registerUseCase, "", "", "");
	}

	private void createUseCaseAndDiagram(ProjectAccessor projectAccessor)
			throws InvalidEditingException, ClassNotFoundException,
			ProjectNotFoundException, InvalidUsingException {
		// Diagrams are created by diagramEditor
		UseCaseDiagramEditor ucde = projectAccessor.getDiagramEditorFactory()
				.getUseCaseDiagramEditor();

		IUseCaseDiagram uc = ucde.createUseCaseDiagram(
				AstahUtil.findPackageByFullName("HSUVUseCases"), "HybridSUV");
		// Target diagram must be set to diagramEditor.
		ucde.setDiagram(uc);

		// Create rectangle presentation on the usecase diagram
		ucde.createRect(new Point2D.Double(150, -30), 250.0D, 600.D);
		// Create "HybridSUV" text on the usecase diagram
		ucde.createText("HybridSUV", new Point2D.Double(250, -30));
		
		// There are only two kinds of presentation APIs in Astah.
		// INodePresentation is for those presentations whose shape are like rectangle like block and port.
		// ILinkPresentation is for those presentations whose shape are like line like connector and association.
		INodePresentation driveActorP = ucde.createNodePresentation(
				AstahUtil.findActorByFullName("HSUVUseCases::Driver"),
				new Point2D.Double(0, 0));
		INodePresentation operateUseCaseP = ucde
				.createNodePresentation(
						AstahUtil
								.findUseCaseByFullName("HSUVUseCases::Operate the vehicle"),
						new Point2D.Double(200, 0));
		operateUseCaseP.setWidth(180.0D);
		INodePresentation registeredActorP = ucde
				.createNodePresentation(AstahUtil
						.findActorByFullName("HSUVUseCases::Registered Owner"),
						new Point2D.Double(0, 225));
		INodePresentation insureUseCaseP = ucde
				.createNodePresentation(
						AstahUtil
								.findUseCaseByFullName("HSUVUseCases::Insure the vehicle"),
						new Point2D.Double(200, 150));
		insureUseCaseP.setWidth(180.0D);
		INodePresentation insuranceCompanyActorP = ucde
				.createNodePresentation(AstahUtil
						.findActorByFullName("HSUVUseCases::InsuranceCompany"),
						new Point2D.Double(500, 150));
		INodePresentation registerUseCaseP = ucde
				.createNodePresentation(
						AstahUtil
								.findUseCaseByFullName("HSUVUseCases::Register the vehicle"),
						new Point2D.Double(200, 300));
		registerUseCaseP.setWidth(180.0D);
		INodePresentation maintainerActorP = ucde.createNodePresentation(
				AstahUtil.findActorByFullName("HSUVUseCases::Maintainer"),
				new Point2D.Double(0, 450));
		INodePresentation departmentActorP = ucde
				.createNodePresentation(
						AstahUtil
								.findActorByFullName("HSUVUseCases::Department Of Motor Vehicles"),
						new Point2D.Double(500, 300));
		INodePresentation maintainUseCaseP = ucde
				.createNodePresentation(
						AstahUtil
								.findUseCaseByFullName("HSUVUseCases::Maintain the vehicle"),
						new Point2D.Double(200, 450));
		maintainUseCaseP.setWidth(180.0D);

		ucde.createLinkPresentation(
				AstahUtil.findAssociation(
						(IClass) AstahUtil
								.findActorByFullName("HSUVUseCases::Driver"),
						(IUseCase) AstahUtil
								.findUseCaseByFullName("HSUVUseCases::Operate the vehicle"),
						"", "", ""), driveActorP, operateUseCaseP);
		ucde.createLinkPresentation(
				AstahUtil.findAssociation(
						(IClass) AstahUtil
								.findActorByFullName("HSUVUseCases::Registered Owner"),
						(IUseCase) AstahUtil
								.findUseCaseByFullName("HSUVUseCases::Insure the vehicle"),
						"", "", ""), registeredActorP, insureUseCaseP);
		ucde.createLinkPresentation(
				AstahUtil.findAssociation(
						(IClass) AstahUtil
								.findActorByFullName("HSUVUseCases::Registered Owner"),
						(IUseCase) AstahUtil
								.findUseCaseByFullName("HSUVUseCases::Register the vehicle"),
						"", "", ""), registeredActorP, registerUseCaseP);
		ucde.createLinkPresentation(
				AstahUtil.findAssociation(
						(IClass) AstahUtil
								.findActorByFullName("HSUVUseCases::Maintainer"),
						(IUseCase) AstahUtil
								.findUseCaseByFullName("HSUVUseCases::Maintain the vehicle"),
						"", "", ""), maintainerActorP, maintainUseCaseP);
		ucde.createLinkPresentation(
				AstahUtil.findAssociation(
						(IClass) AstahUtil
								.findActorByFullName("HSUVUseCases::InsuranceCompany"),
						(IUseCase) AstahUtil
								.findUseCaseByFullName("HSUVUseCases::Insure the vehicle"),
						"", "", ""), insuranceCompanyActorP, insureUseCaseP);
		ucde.createLinkPresentation(
				AstahUtil.findAssociation(
						(IClass) AstahUtil
								.findActorByFullName("HSUVUseCases::Department Of Motor Vehicles"),
						(IUseCase) AstahUtil
								.findUseCaseByFullName("HSUVUseCases::Register the vehicle"),
						"", "", ""), departmentActorP, registerUseCaseP);
	}
}
