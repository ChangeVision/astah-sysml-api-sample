import java.awt.geom.Point2D;
import java.io.IOException;

import com.change_vision.jude.api.inf.editor.ModelEditorFactory;
import com.change_vision.jude.api.inf.editor.RequirementDiagramEditor;
import com.change_vision.jude.api.inf.editor.SysmlModelEditor;
import com.change_vision.jude.api.inf.editor.TransactionManager;
import com.change_vision.jude.api.inf.editor.UseCaseModelEditor;
import com.change_vision.jude.api.inf.exception.InvalidEditingException;
import com.change_vision.jude.api.inf.exception.InvalidUsingException;
import com.change_vision.jude.api.inf.exception.LicenseNotFoundException;
import com.change_vision.jude.api.inf.exception.ProjectLockedException;
import com.change_vision.jude.api.inf.exception.ProjectNotFoundException;
import com.change_vision.jude.api.inf.model.IBlock;
import com.change_vision.jude.api.inf.model.IDependency;
import com.change_vision.jude.api.inf.model.IModel;
import com.change_vision.jude.api.inf.model.IPackage;
import com.change_vision.jude.api.inf.model.IRequirement;
import com.change_vision.jude.api.inf.model.IRequirementDiagram;
import com.change_vision.jude.api.inf.model.ITestCase;
import com.change_vision.jude.api.inf.model.IUseCase;
import com.change_vision.jude.api.inf.presentation.ILinkPresentation;
import com.change_vision.jude.api.inf.presentation.INodePresentation;
import com.change_vision.jude.api.inf.project.ProjectAccessor;

/**
 * Sample source codes for creating an requirement diagram and all models of requirement diagram.
 */
public class CreatingSampleSysmlRequirementDiagram {
	public static void main(String[] args) {
		CreatingSampleSysmlRequirementDiagram sample = new CreatingSampleSysmlRequirementDiagram();
		try {
			sample.create("SampleSysmlRequirementDiagram.asml");
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

			createRequirementModels(projectAccessor);

			createRequirementdDiagrams(projectAccessor);

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

	private void createRequirementModels(ProjectAccessor projectAccessor)
			throws InvalidEditingException, ClassNotFoundException,
			ProjectNotFoundException, InvalidUsingException {
		// Get a root model of the project
        // Astah SysML model is tree structure. The project model is the root.
		IModel project = projectAccessor.getProject();

		// Most of SysML models like block, constraint parameter can be created by SysMLModelEditor.
		SysmlModelEditor sme = ModelEditorFactory.getSysmlModelEditor();
		
		// Models on usecase diagram can be created by UseCaseModelEditor.
		UseCaseModelEditor ucd = ModelEditorFactory.getUseCaseModelEditor();

		IPackage hSUVRequirementsPackage = sme.createPackage(project,
				"HSUVRequirements");
		IPackage hSUVSpecificationPackage = sme.createPackage(
				hSUVRequirementsPackage, "HSUVSpecification");
		IRequirement ecoFriendlinessRequirement = sme.createRequirement(
				hSUVSpecificationPackage, "Eco-Friendliness");
		IRequirement emissionsRequirement = sme.createRequirement(
				ecoFriendlinessRequirement, "Emissions");
		emissionsRequirement
				.setRequirementText("The vehicle shall meet Ultra-Low Emissions Vehicle standards.");
		IRequirement ergonomicsRequirement = sme.createRequirement(
				hSUVSpecificationPackage, "Ergonomics");
		IRequirement performanceRequirement = sme.createRequirement(
				hSUVSpecificationPackage, "Performance");
		IRequirement performanceAccelerationRequirement = sme
				.createRequirement(performanceRequirement, "Acceleration");
		IRequirement brakingRequirement = sme.createRequirement(
				performanceRequirement, "Braking");
		IRequirement fuelEconomyRequirement = sme.createRequirement(
				performanceRequirement, "FuelEconomy");
		IRequirement offRoadCapabilityRequirement = sme.createRequirement(
				performanceRequirement, "OffRoadCapability");
		IRequirement qualificationRequirement = sme.createRequirement(
				hSUVSpecificationPackage, "Qualification");
		IRequirement safetyTestRequirement = sme.createRequirement(
				qualificationRequirement, "SafetyTest");
		IRequirement accelerationRequirement = sme.createRequirement(
				hSUVRequirementsPackage, "Acceleration");
		IRequirement capacityRequirement = sme.createRequirement(
				hSUVRequirementsPackage, "Capacity");
		IRequirement cargoCapacityRequirement = sme.createRequirement(
				capacityRequirement, "CargoCapacity");
		IRequirement fuelCapacityRequirement = sme.createRequirement(
				capacityRequirement, "FuelCapacity");
		IRequirement passengerCapacityRequirement = sme.createRequirement(
				capacityRequirement, "PassengerCapacity");
		IRequirement powerRequirement = sme.createRequirement(
				hSUVRequirementsPackage, "Power");
		IDependency powerDeriveReqt = sme.createDeriveReqtDependency(
				accelerationRequirement, powerRequirement, "");
		ITestCase maxAccelerationTestCase = sme.createTestCase(
				hSUVRequirementsPackage, "Max Acceleration");
		IDependency maxAccelerationVerify = sme.createVerifyDependency(
				maxAccelerationTestCase, accelerationRequirement, "");

		IPackage hSUVStructurePackage = sme.createPackage(project,
				"HSUVStructure");
		IBlock powerSubsystemBlock = sme.createBlock(hSUVStructurePackage,
				"PowerSubsystem");
		IDependency PowerSubsystemDeriveReqt = sme.createSatisfyDependency(
				powerSubsystemBlock, powerRequirement, "");

		IPackage hSUVUseCasesPackage = sme.createPackage(project,
				"HSUVUseCases");
		IUseCase accelerateUseCase = ucd.createUseCase(hSUVUseCasesPackage,
				"Accelerate");
		IDependency accelerateUseCaseRefine = sme.createRefineDependency(
				accelerateUseCase, accelerationRequirement, "");
	}

	private void createRequirementdDiagrams(ProjectAccessor projectAccessor)
			throws InvalidEditingException, ClassNotFoundException,
			ProjectNotFoundException, InvalidUsingException {
		
		// Diagrams are created by diagramEditor.
		RequirementDiagramEditor rde = projectAccessor
				.getDiagramEditorFactory().getRequirementDiagramEditor();
		IRequirementDiagram reqDgm = rde.createRequirementDiagram(
				AstahUtil.findPackageByFullName("HSUVRequirements"),
				"Acceleration Requirement Refinement and Verification");
		IRequirementDiagram reqDgm2 = rde.createRequirementDiagram(
				AstahUtil.findPackageByFullName("HSUVRequirements"),
				"HSUV Specification");

		// "Acceleration Requirement Refinement and Verification"
		// RequirementDiagramEditor can be used to create presentations on a requirement diagram.
		// Target diagram must be set to diagramEditor.
		rde.setDiagram(reqDgm);
		INodePresentation accelerationRequirementqP = rde
				.createNodePresentation(
						AstahUtil
								.findRequirementByFullName("HSUVRequirements::Acceleration"),
						new Point2D.Double(300, 0));
		// Detail compartment is visible.
		// Properties such as color and shape can be set by setProperty.
		accelerationRequirementqP.setProperty("detail_compartment_visibility",
				"true");
		// Id compartment is visible.
		// Properties such as color and shape can be set by setProperty.
		accelerationRequirementqP.setProperty("id_visibility", "true");
		// Text compartment is visible.
		// Properties such as color and shape can be set by setProperty.
		accelerationRequirementqP.setProperty("text_visibility", "true");
		INodePresentation accelerateUseCaseP = rde.createNodePresentation(
				AstahUtil.findUseCaseByFullName("HSUVUseCases::Accelerate"),
				new Point2D.Double(0, 300));
		ILinkPresentation accelerateUseCaseRefineP = rde
				.createLinkPresentation(
						AstahUtil.findRefineDependency(
								(IUseCase) AstahUtil
										.findUseCaseByFullName("HSUVUseCases::Accelerate"),
								(IRequirement) AstahUtil
										.findRequirementByFullName("HSUVRequirements::Acceleration"),
								""), accelerationRequirementqP,
						accelerateUseCaseP);
		INodePresentation powerRequirementP = rde.createNodePresentation(
				AstahUtil.findRequirementByFullName("HSUVRequirements::Power"),
				new Point2D.Double(300, 300));
		// Detail compartment is visible.
		// Properties such as color and shape can be set by setProperty.
		powerRequirementP.setProperty("detail_compartment_visibility", "true");
		// Id is visible.
		// Properties such as color and shape can be set by setProperty.
		powerRequirementP.setProperty("id_visibility", "true");
		// Text is visible.
		// Properties such as color and shape can be set by setProperty.
		powerRequirementP.setProperty("text_visibility", "true");
		ILinkPresentation powerDeriveReqtDependencyP = rde
				.createLinkPresentation(
						AstahUtil.findDeriveReqtDependency(
								(IRequirement) AstahUtil
										.findRequirementByFullName("HSUVRequirements::Acceleration"),
								(IRequirement) AstahUtil
										.findRequirementByFullName("HSUVRequirements::Power"),
								""), accelerationRequirementqP,
						powerRequirementP);
		INodePresentation maxAccelerationTestCaseP = rde
				.createNodePresentation(
						AstahUtil
								.findTestCaseByFullName("HSUVRequirements::Max Acceleration"),
						new Point2D.Double(600, 300));
		maxAccelerationTestCaseP.setProperty("detail_compartment_visibility",
				"true");
		ILinkPresentation maxAccelerationVerifyP = rde
				.createLinkPresentation(
						AstahUtil.findVerifyDependency(
								(ITestCase) AstahUtil
										.findTestCaseByFullName("HSUVRequirements::Max Acceleration"),
								(IRequirement) AstahUtil
										.findRequirementByFullName("HSUVRequirements::Acceleration"),
								""), accelerationRequirementqP,
						maxAccelerationTestCaseP);
		INodePresentation powerSubsystemBlockP = rde.createNodePresentation(
				AstahUtil.findBlockByFullName("HSUVStructure::PowerSubsystem"),
				new Point2D.Double(300, 600));
		ILinkPresentation powerSubsystemDeriveReqtP = rde
				.createLinkPresentation(
						AstahUtil.findSatisfyDependency(
								(IBlock) AstahUtil
										.findBlockByFullName("HSUVStructure::PowerSubsystem"),
								(IRequirement) AstahUtil
										.findRequirementByFullName("HSUVRequirements::Power"),
								""), powerRequirementP, powerSubsystemBlockP);

		// "HSUV Specification"
		rde.setDiagram(reqDgm2);
		INodePresentation hSUVSpecificationPackageP = rde
				.createNodePresentation(
						AstahUtil
								.findPackageByFullName("HSUVRequirements::HSUVSpecification"),
						new Point2D.Double(500, 0));
		INodePresentation ecoFriendlinessRequirementP = rde
				.createNodePresentation(
						AstahUtil
								.findRequirementByFullName("HSUVRequirements::HSUVSpecification::Eco-Friendliness"),
						new Point2D.Double(0, 200));
		// Detail compartment is visible.
		// Properties such as color and shape can be set by setProperty.
		ecoFriendlinessRequirementP.setProperty(
				"detail_compartment_visibility", "true");
		// Id is visible.
		// Properties such as color and shape can be set by setProperty.
		ecoFriendlinessRequirementP.setProperty("id_visibility", "true");
		// Text is visible.
		// Properties such as color and shape can be set by setProperty.
		ecoFriendlinessRequirementP.setProperty("text_visibility", "true");
		rde.createContainmentLinkPresentation(hSUVSpecificationPackageP,
				ecoFriendlinessRequirementP);
		INodePresentation performanceRequirementP = rde
				.createNodePresentation(
						AstahUtil
								.findRequirementByFullName("HSUVRequirements::HSUVSpecification::Performance"),
						new Point2D.Double(250, 200));
		// Detail compartment is visible.
		// Properties such as color and shape can be set by setProperty.
		performanceRequirementP.setProperty("detail_compartment_visibility",
				"true");
		// Id is visible.
		// Properties such as color and shape can be set by setProperty.
		performanceRequirementP.setProperty("id_visibility", "true");
		// Text is visible.
		// Properties such as color and shape can be set by setProperty.
		performanceRequirementP.setProperty("text_visibility", "true");
		rde.createContainmentLinkPresentation(hSUVSpecificationPackageP,
				performanceRequirementP);
		INodePresentation ergonomicsRequirementP = rde
				.createNodePresentation(
						AstahUtil
								.findRequirementByFullName("HSUVRequirements::HSUVSpecification::Ergonomics"),
						new Point2D.Double(600, 200));
		// Detail compartment is visible.
		// Properties such as color and shape can be set by setProperty.
		ergonomicsRequirementP.setProperty("detail_compartment_visibility",
				"true");
		// Id is visible.
		// Properties such as color and shape can be set by setProperty.
		ergonomicsRequirementP.setProperty("id_visibility", "true");
		// Text is visible.
		// Properties such as color and shape can be set by setProperty.
		ergonomicsRequirementP.setProperty("text_visibility", "true");
		rde.createContainmentLinkPresentation(hSUVSpecificationPackageP,
				ergonomicsRequirementP);
		INodePresentation qualificationRequirementP = rde
				.createNodePresentation(
						AstahUtil
								.findRequirementByFullName("HSUVRequirements::HSUVSpecification::Qualification"),
						new Point2D.Double(800, 200));
		// Detail compartment is visible.
		// Properties such as color and shape can be set by setProperty.
		qualificationRequirementP.setProperty("detail_compartment_visibility",
				"true");
		// Id is visible.
		// Properties such as color and shape can be set by setProperty.
		qualificationRequirementP.setProperty("id_visibility", "true");
		// Text is visible.
		// Properties such as color and shape can be set by setProperty.
		qualificationRequirementP.setProperty("text_visibility", "true");
		rde.createContainmentLinkPresentation(hSUVSpecificationPackageP,
				qualificationRequirementP);
		INodePresentation capacityRequirementP = rde
				.createNodePresentation(
						AstahUtil
								.findRequirementByFullName("HSUVRequirements::Capacity"),
						new Point2D.Double(1000, 200));
		// Detail compartment is visible.
		// Properties such as color and shape can be set by setProperty.
		capacityRequirementP.setProperty("detail_compartment_visibility",
				"true");
		// Id is visible.
		// Properties such as color and shape can be set by setProperty.
		capacityRequirementP.setProperty("id_visibility", "true");
		// Text is visible.
		// Properties such as color and shape can be set by setProperty.
		capacityRequirementP.setProperty("text_visibility", "true");
		rde.createContainmentLinkPresentation(hSUVSpecificationPackageP,
				capacityRequirementP);
		INodePresentation brakingRequirementP = rde
				.createNodePresentation(
						AstahUtil
								.findRequirementByFullName("HSUVRequirements::HSUVSpecification::Performance::Braking"),
						new Point2D.Double(150, 400));
		// Detail compartment is visible.
		// Properties such as color and shape can be set by setProperty.
		brakingRequirementP.setProperty("detail_compartment_visibility", "true");
		// Id is visible.
		// Properties such as color and shape can be set by setProperty.
		brakingRequirementP.setProperty("id_visibility", "true");
		// Text is visible.
		// Properties such as color and shape can be set by setProperty.
		brakingRequirementP.setProperty("text_visibility", "true");
		rde.createContainmentLinkPresentation(performanceRequirementP,
				brakingRequirementP);
		INodePresentation fuelEconomyRequirementP = rde
				.createNodePresentation(
						AstahUtil
								.findRequirementByFullName("HSUVRequirements::HSUVSpecification::Performance::FuelEconomy"),
						new Point2D.Double(270, 400));
		// Detail compartment is visible.
		// Properties such as color and shape can be set by setProperty.
		fuelEconomyRequirementP.setProperty("detail_compartment_visibility",
				"true");
		// Id is visible.
		// Properties such as color and shape can be set by setProperty.
		fuelEconomyRequirementP.setProperty("id_visibility", "true");
		// Text is visible.
		// Properties such as color and shape can be set by setProperty.
		fuelEconomyRequirementP.setProperty("text_visibility", "true");
		rde.createContainmentLinkPresentation(performanceRequirementP,
				fuelEconomyRequirementP);
		INodePresentation offRoadCapabilityRequirementP = rde
				.createNodePresentation(
						AstahUtil
								.findRequirementByFullName("HSUVRequirements::HSUVSpecification::Performance::OffRoadCapability"),
						new Point2D.Double(390, 400));
		// Detail compartment is visible.
		// Properties such as color and shape can be set by setProperty.
		offRoadCapabilityRequirementP.setProperty(
				"detail_compartment_visibility", "true");
		// Id is visible.
		// Properties such as color and shape can be set by setProperty.
		offRoadCapabilityRequirementP.setProperty("id_visibility", "true");
		// Text is visible.
		// Properties such as color and shape can be set by setProperty.
		offRoadCapabilityRequirementP.setProperty("text_visibility", "true");
		rde.createContainmentLinkPresentation(performanceRequirementP,
				offRoadCapabilityRequirementP);
		INodePresentation performanceAccelerationRequirementP = rde
				.createNodePresentation(
						AstahUtil
								.findRequirementByFullName("HSUVRequirements::HSUVSpecification::Performance::Acceleration"),
						new Point2D.Double(530, 400));
		performanceAccelerationRequirementP.setProperty(
				"detail_compartment_visibility", "true");
		performanceAccelerationRequirementP
				.setProperty("id_visibility", "true");
		performanceAccelerationRequirementP.setProperty("text_visibility",
				"true");
		rde.createContainmentLinkPresentation(performanceRequirementP,
				performanceAccelerationRequirementP);
		INodePresentation safetyTestRequirementP = rde
				.createNodePresentation(
						AstahUtil
								.findRequirementByFullName("HSUVRequirements::HSUVSpecification::Qualification::SafetyTest"),
						new Point2D.Double(800, 400));
		safetyTestRequirementP.setProperty("detail_compartment_visibility",
				"true");
		safetyTestRequirementP.setProperty("id_visibility", "true");
		safetyTestRequirementP.setProperty("text_visibility", "true");
		rde.createContainmentLinkPresentation(qualificationRequirementP,
				safetyTestRequirementP);
		INodePresentation cargoCapacityRequirementP = rde
				.createNodePresentation(
						AstahUtil
								.findRequirementByFullName("HSUVRequirements::HSUVSpecification::Capacity::CargoCapacity"),
						new Point2D.Double(950, 400));
		cargoCapacityRequirementP.setProperty("detail_compartment_visibility",
				"true");
		cargoCapacityRequirementP.setProperty("id_visibility", "true");
		cargoCapacityRequirementP.setProperty("text_visibility", "true");
		rde.createContainmentLinkPresentation(capacityRequirementP,
				cargoCapacityRequirementP);
		INodePresentation fuelCapacityRequirementP = rde
				.createNodePresentation(
						AstahUtil
								.findRequirementByFullName("HSUVRequirements::HSUVSpecification::Capacity::FuelCapacity"),
						new Point2D.Double(1070, 400));
		fuelCapacityRequirementP.setProperty("detail_compartment_visibility",
				"true");
		fuelCapacityRequirementP.setProperty("id_visibility", "true");
		fuelCapacityRequirementP.setProperty("text_visibility", "true");
		rde.createContainmentLinkPresentation(capacityRequirementP,
				fuelCapacityRequirementP);
		INodePresentation passengerCapacityRequirementP = rde
				.createNodePresentation(
						AstahUtil
								.findRequirementByFullName("HSUVRequirements::HSUVSpecification::Capacity::PassengerCapacity"),
						new Point2D.Double(1190, 400));
		passengerCapacityRequirementP.setProperty(
				"detail_compartment_visibility", "true");
		passengerCapacityRequirementP.setProperty("id_visibility", "true");
		passengerCapacityRequirementP.setProperty("text_visibility", "true");
		rde.createContainmentLinkPresentation(capacityRequirementP,
				passengerCapacityRequirementP);
		INodePresentation emissionsRequirementP = rde
				.createNodePresentation(
						AstahUtil
								.findRequirementByFullName("HSUVRequirements::HSUVSpecification::Eco-Friendliness::Emissions"),
						new Point2D.Double(0, 400));
		emissionsRequirementP.setProperty("detail_compartment_visibility",
				"true");
		emissionsRequirementP.setProperty("id_visibility", "true");
		emissionsRequirementP.setProperty("text_visibility", "true");
		rde.createContainmentLinkPresentation(ecoFriendlinessRequirementP,
				emissionsRequirementP);
	}

}
