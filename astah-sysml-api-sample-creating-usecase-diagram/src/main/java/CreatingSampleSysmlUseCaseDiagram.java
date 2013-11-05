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
		ProjectAccessor projectAccessor = AstahUtil.getProjectAccessor();
		try {
			projectAccessor.create(name);
			TransactionManager.beginTransaction();

			createUseCaseModel(projectAccessor);

			createUseCaseAndDiagram(projectAccessor);

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

	private void createUseCaseModel(ProjectAccessor projectAccessor)
			throws InvalidEditingException, ClassNotFoundException,
			ProjectNotFoundException, InvalidUsingException {
		IModel project = projectAccessor.getProject();

		SysmlModelEditor sme = ModelEditorFactory.getSysmlModelEditor();
		IPackage hSUVUseCasesPackage = sme.createPackage(project,
				"HSUVUseCases");

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
		UseCaseDiagramEditor ucde = projectAccessor.getDiagramEditorFactory()
				.getUseCaseDiagramEditor();

		IUseCaseDiagram uc = ucde.createUseCaseDiagram(
				AstahUtil.findPackageByFullName("HSUVUseCases"), "HybridSUV");
		ucde.setDiagram(uc);

		ucde.createRect(new Point2D.Double(150, -30), 250.0D, 600.D);
		ucde.createText("HybridSUV", new Point2D.Double(250, -30));
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
