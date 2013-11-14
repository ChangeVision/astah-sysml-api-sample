import java.awt.geom.Point2D;
import java.io.IOException;

import com.change_vision.jude.api.inf.editor.InternalBlockDiagramEditor;
import com.change_vision.jude.api.inf.editor.ModelEditorFactory;
import com.change_vision.jude.api.inf.editor.SysmlModelEditor;
import com.change_vision.jude.api.inf.editor.TransactionManager;
import com.change_vision.jude.api.inf.exception.InvalidEditingException;
import com.change_vision.jude.api.inf.exception.InvalidUsingException;
import com.change_vision.jude.api.inf.exception.LicenseNotFoundException;
import com.change_vision.jude.api.inf.exception.ProjectLockedException;
import com.change_vision.jude.api.inf.exception.ProjectNotFoundException;
import com.change_vision.jude.api.inf.model.IAttribute;
import com.change_vision.jude.api.inf.model.IBlock;
import com.change_vision.jude.api.inf.model.IConnector;
import com.change_vision.jude.api.inf.model.IFlowProperty;
import com.change_vision.jude.api.inf.model.IInternalBlockDiagram;
import com.change_vision.jude.api.inf.model.IModel;
import com.change_vision.jude.api.inf.model.IPackage;
import com.change_vision.jude.api.inf.model.IPort;
import com.change_vision.jude.api.inf.model.IQuantityKind;
import com.change_vision.jude.api.inf.model.IUnit;
import com.change_vision.jude.api.inf.model.IValueType;
import com.change_vision.jude.api.inf.presentation.INodePresentation;
import com.change_vision.jude.api.inf.project.ProjectAccessor;

public class CreatingSampleSysmlInternalBlockDiagram {
	public static void main(String[] args) {
		CreatingSampleSysmlInternalBlockDiagram sample = new CreatingSampleSysmlInternalBlockDiagram();
		try {
			sample.create("SampleSysmlInternalBlockDiagram.asml");
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

			createInternalBlockModels(projectAccessor);

			createInternalBlockDiagrams(projectAccessor);

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

	private void createInternalBlockModels(ProjectAccessor projectAccessor)
			throws InvalidEditingException, ClassNotFoundException,
			ProjectNotFoundException, InvalidUsingException {
		IModel project = projectAccessor.getProject();

		SysmlModelEditor sme = ModelEditorFactory.getSysmlModelEditor();

		IPackage sysMLPackage = sme.createPackage(project, "SysML");
		IValueType integerValueType = sme.createValueType(sysMLPackage,
				"Integer");
		IValueType realValueType = sme.createValueType(sysMLPackage, "Real");
		IValueType booleanValueType = sme.createValueType(sysMLPackage,
				"Boolean");

		IPackage modelingDomainPackage = sme.createPackage(project,
				"ModelingDomain");
		IPackage valuePackage = sme.createPackage(modelingDomainPackage,
				"Automotive Value Types");
		valuePackage.addStereotype("modelLibrary");
		IPackage quantityKindPackage = sme.createPackage(valuePackage,
				"Automotive QuantityKind");
		quantityKindPackage.addStereotype("modelLibrary");
		IPackage unitsPackage = sme.createPackage(valuePackage,
				"Automotive Units");
		unitsPackage.addStereotype("modelLibrary");

		IQuantityKind accelerationQuantityKind = sme.createQuantityKind(
				quantityKindPackage, "Acceleration");
		IQuantityKind distanceQuantityKind = sme.createQuantityKind(
				quantityKindPackage, "Distance");
		IQuantityKind massQuantityKind = sme.createQuantityKind(
				quantityKindPackage, "Mass");
		IQuantityKind powerQuantityKind = sme.createQuantityKind(
				quantityKindPackage, "Power");
		IQuantityKind pressureQuantityKind = sme.createQuantityKind(
				quantityKindPackage, "Pressure");
		IQuantityKind temperatureQuantityKind = sme.createQuantityKind(
				quantityKindPackage, "Temperature");
		IQuantityKind timeQuantityKind = sme.createQuantityKind(
				quantityKindPackage, "Time");
		IQuantityKind velocityQuantityKind = sme.createQuantityKind(
				quantityKindPackage, "Velocity");
		IQuantityKind volumeQuantityKind = sme.createQuantityKind(
				quantityKindPackage, "Volume");

		IUnit fUnit = sme.createUnit(unitsPackage, "F");
		fUnit.setQuantityKind(temperatureQuantityKind);
		IUnit ftUnit = sme.createUnit(unitsPackage, "ft");
		ftUnit.setQuantityKind(distanceQuantityKind);
		IUnit ft3Unit = sme.createUnit(unitsPackage, "ft^3");
		ft3Unit.setQuantityKind(volumeQuantityKind);
		IUnit gUnit = sme.createUnit(unitsPackage, "g");
		gUnit.setQuantityKind(accelerationQuantityKind);
		IUnit hpUnit = sme.createUnit(unitsPackage, "hp");
		hpUnit.setQuantityKind(powerQuantityKind);
		IUnit lbUnit = sme.createUnit(unitsPackage, "lb");
		lbUnit.setQuantityKind(massQuantityKind);
		IUnit mphUnit = sme.createUnit(unitsPackage, "mph");
		mphUnit.setQuantityKind(velocityQuantityKind);
		IUnit psiUnit = sme.createUnit(unitsPackage, "psi");
		psiUnit.setQuantityKind(pressureQuantityKind);
		IUnit secUnit = sme.createUnit(unitsPackage, "sec");
		secUnit.setQuantityKind(timeQuantityKind);

		IValueType accelValueType = sme.createValueType(valuePackage, "Accel");
		sme.createGeneralization(accelValueType, realValueType, "");
		accelValueType.setUnit(gUnit);
		accelValueType.setQuantityKind(temperatureQuantityKind);
		IValueType distValueType = sme.createValueType(valuePackage, "Dist");
		sme.createGeneralization(distValueType, realValueType, "");
		distValueType.setUnit(ftUnit);
		IValueType horsepwrValueType = sme.createValueType(valuePackage,
				"Horsepwr");
		sme.createGeneralization(horsepwrValueType, realValueType, "");
		horsepwrValueType.setUnit(hpUnit);
		IValueType pressValueType = sme.createValueType(valuePackage, "Press");
		sme.createGeneralization(pressValueType, realValueType, "");
		pressValueType.setUnit(psiUnit);
		pressValueType.setQuantityKind(volumeQuantityKind);
		IValueType tempValueType = sme.createValueType(valuePackage, "Temp");
		sme.createGeneralization(tempValueType, realValueType, "");
		tempValueType.setUnit(fUnit);
		IValueType timeValueType = sme.createValueType(valuePackage, "Time");
		sme.createGeneralization(timeValueType, realValueType, "");
		timeValueType.setUnit(secUnit);
		IValueType velValueType = sme.createValueType(valuePackage, "Vel");
		sme.createGeneralization(velValueType, realValueType, "");
		velValueType.setUnit(mphUnit);
		IValueType volValueType = sme.createValueType(valuePackage, "Vol");
		sme.createGeneralization(volValueType, realValueType, "");
		volValueType.setUnit(ft3Unit);
		IValueType weightValueType = sme
				.createValueType(valuePackage, "Weight");
		sme.createGeneralization(weightValueType, realValueType, "");
		weightValueType.setUnit(lbUnit);

		IPackage hSUVStructurePackage = sme.createPackage(project,
				"HSUVStructure");
		// Block
		IBlock bodySubsystemBlock = sme.createBlock(hSUVStructurePackage,
				"BodySubsystem");
		IBlock brakeSubsystemBlock = sme.createBlock(hSUVStructurePackage,
				"BrakeSubsystem");
		IBlock cANBusBlock = sme.createBlock(hSUVStructurePackage, "CAN_Bus");
		IBlock chassisSubsystemBlock = sme.createBlock(hSUVStructurePackage,
				"ChassisSubsystem");
		IBlock electricalPowerControllerBlock = sme.createBlock(
				hSUVStructurePackage, "ElectricalPowerController");
		IBlock fS_EPCBlock = sme.createBlock(hSUVStructurePackage, "FS_EPC");
		IBlock fS_ICEBlock = sme.createBlock(hSUVStructurePackage, "FS_ICE");
		IBlock fS_TRSMBlock = sme.createBlock(hSUVStructurePackage, "FS_TRSM");
		IBlock fuelFlowBlock = sme
				.createBlock(hSUVStructurePackage, "FuelFlow");
		fuelFlowBlock.addStereotype("flowSpecification");
		IBlock hybridSUVBlock = sme.createBlock(hSUVStructurePackage,
				"HybridSUV");
		hybridSUVBlock.addStereotype("system");
		IBlock iCEDataBlock = sme.createBlock(hSUVStructurePackage, "ICEData");
		iCEDataBlock.addStereotype("signal");
		IBlock interiorSubsystemBlock = sme.createBlock(hSUVStructurePackage,
				"InteriorSubsystem");
		IBlock internalCombustionEngineBlock = sme.createBlock(
				hSUVStructurePackage, "InternalCombustionEngine");
		IBlock lightingSubsystemlock = sme.createBlock(hSUVStructurePackage,
				"LightingSubsystem");
		IBlock powerControlUnitBlock = sme.createBlock(hSUVStructurePackage,
				"PowerControlUnit");
		IBlock powerSubsystemBlock = sme.createBlock(hSUVStructurePackage,
				"PowerSubsystem");
		IBlock roadBlock = sme.createBlock(hSUVStructurePackage, "Road");
		roadBlock.addStereotype("external");
		IBlock transmissionBlock = sme.createBlock(hSUVStructurePackage,
				"Transmission");
		IBlock weatherBlock = sme.createBlock(hSUVStructurePackage, "Weather");
		weatherBlock.addStereotype("external");
		IBlock wheelHubAssemblyBlock = sme.createBlock(hSUVStructurePackage,
				"WheelHubAssembly");
		// IValueType
		IValueType fuelValueType = sme.createValueType(hSUVStructurePackage,
				"Fuel");
		// Port
		IPort fpPort = sme.createPort(electricalPowerControllerBlock, "fp");
		fpPort.setType(fS_EPCBlock);
		IPort fpPort2 = sme.createPort(transmissionBlock, "fp");
		fpPort2.setType(fS_TRSMBlock);
		IPort iCEFuelFittingPort = sme.createPort(
				internalCombustionEngineBlock, "ICEFuelFitting");
		iCEFuelFittingPort.setType(fuelFlowBlock);
		IPort epcPort = sme.createPort(powerControlUnitBlock, "epc");
		epcPort.setType(fS_EPCBlock);
		IPort etrsmPort = sme.createPort(powerControlUnitBlock, "etrsm");
		etrsmPort.setType(fS_TRSMBlock);
		IPort icePort = sme.createPort(powerControlUnitBlock, "ice");
		icePort.setType(fS_ICEBlock);
		// Part
		IAttribute wheelHubAssemblyBlockAsoEnd = sme.createPart(
				chassisSubsystemBlock, "", wheelHubAssemblyBlock);
		wheelHubAssemblyBlockAsoEnd.setMultiplicity(new int[][] { { 4 } });
		IAttribute powerSubsystemBlockAsoEnd = sme.createPart(hybridSUVBlock,
				"p", powerSubsystemBlock);
		IAttribute brakeSubsystemBlockAsoEnd = sme.createPart(hybridSUVBlock,
				"br", brakeSubsystemBlock);
		IAttribute bodySubsystemBlockAsoEnd = sme.createPart(hybridSUVBlock,
				"b", bodySubsystemBlock);
		IAttribute interiorSubsystemBlockAsoEnd = sme.createPart(
				hybridSUVBlock, "i", interiorSubsystemBlock);
		IAttribute lightingSubsystemlockAsoEnd = sme.createPart(hybridSUVBlock,
				"l", lightingSubsystemlock);
		IAttribute chassisSubsystemBlockAsoEnd = sme.createPart(hybridSUVBlock,
				"c", chassisSubsystemBlock);
		IAttribute wheelHubAssemblyBlockAsoEnd2 = sme.createPart(
				powerSubsystemBlock, "", wheelHubAssemblyBlock);
		wheelHubAssemblyBlockAsoEnd2.setMultiplicity(new int[][] { { 2 } });
		IAttribute internalCombustionEngineBlockAsoEnd = sme.createPart(
				powerSubsystemBlock, "ice", internalCombustionEngineBlock);
		IAttribute electricalPowerControllerBlockAsoEnd = sme.createPart(
				powerSubsystemBlock, "epc", electricalPowerControllerBlock);
		IAttribute transmissionBlockAsoEnd = sme.createPart(
				powerSubsystemBlock, "trsm", transmissionBlock);
		IAttribute cANBusBlockAsoEnd = sme.createPart(powerSubsystemBlock, "",
				cANBusBlock);
		IAttribute powerControlUnitBlockAsoEnd = sme.createPart(
				powerSubsystemBlock, "ecu", powerControlUnitBlock);
		sme.createConnector(electricalPowerControllerBlockAsoEnd, fpPort,
				cANBusBlockAsoEnd, null);
		sme.createConnector(transmissionBlockAsoEnd, fpPort2,
				cANBusBlockAsoEnd, null);
		sme.createConnector(internalCombustionEngineBlockAsoEnd,
				iCEFuelFittingPort, cANBusBlockAsoEnd, null);
		sme.createConnector(cANBusBlockAsoEnd, null,
				powerControlUnitBlockAsoEnd, epcPort);
		sme.createConnector(cANBusBlockAsoEnd, null,
				powerControlUnitBlockAsoEnd, etrsmPort);
		sme.createConnector(cANBusBlockAsoEnd, null,
				powerControlUnitBlockAsoEnd, icePort);
		IConnector ibd2Connector = sme.createConnector(
				bodySubsystemBlockAsoEnd, null, interiorSubsystemBlockAsoEnd,
				null);
		ibd2Connector.setName("b-i");
		IConnector ibd2Connector2 = sme.createConnector(
				bodySubsystemBlockAsoEnd, null, chassisSubsystemBlockAsoEnd,
				null);
		ibd2Connector2.setName("b-c");
		IConnector ibd2Connector3 = sme.createConnector(
				bodySubsystemBlockAsoEnd, null, lightingSubsystemlockAsoEnd,
				null);
		ibd2Connector3.setName("b-I");
		IConnector ibd2Connector4 = sme.createConnector(
				interiorSubsystemBlockAsoEnd, null,
				lightingSubsystemlockAsoEnd, null);
		ibd2Connector4.setName("i-l");
		IConnector ibd2Connector5 = sme.createConnector(
				chassisSubsystemBlockAsoEnd, null, brakeSubsystemBlockAsoEnd,
				null);
		ibd2Connector5.setName("c-bk");
		IConnector ibd2Connector6 = sme.createConnector(
				brakeSubsystemBlockAsoEnd, null, lightingSubsystemlockAsoEnd,
				null);
		ibd2Connector6.setName("bk-l");
		IConnector ibd2Connector7 = sme.createConnector(
				chassisSubsystemBlockAsoEnd, null, powerSubsystemBlockAsoEnd,
				null);
		ibd2Connector7.setName("p-c");
		IConnector ibd2Connector8 = sme.createConnector(
				powerSubsystemBlockAsoEnd, null, brakeSubsystemBlockAsoEnd,
				null);
		ibd2Connector8.setName("p-bk");
		// flowPropreties
		IFlowProperty engineData = sme.createFlowProperty(fS_ICEBlock,
				"engineData", iCEDataBlock);
		engineData.setDirectionOut();
		sme.createFlowProperty(fS_ICEBlock, "mixture", iCEDataBlock);
		sme.createFlowProperty(fS_ICEBlock, "throttlePosition", iCEDataBlock);
		IFlowProperty fuelSupply = sme.createFlowProperty(fuelFlowBlock,
				"fuelSupply", fuelValueType);
		fuelSupply.setDirectionOut();
		sme.createFlowProperty(fuelFlowBlock, "fuelReturn", fuelValueType);
		IFlowProperty fuelSupply3 = sme.createFlowProperty(
				internalCombustionEngineBlock, "fuelSupply", fuelValueType);
		fuelSupply3.setDirectionOut();
		sme.createFlowProperty(internalCombustionEngineBlock, "fuelReturn",
				fuelValueType);
		sme.createValueAttribute(iCEDataBlock, "rpm", integerValueType);
		sme.createValueAttribute(iCEDataBlock, "temperature", realValueType);
		sme.createValueAttribute(iCEDataBlock, "isKnocking", booleanValueType);
	}

	private void createInternalBlockDiagrams(ProjectAccessor projectAccessor)
			throws InvalidEditingException, ClassNotFoundException,
			ProjectNotFoundException, InvalidUsingException {
		// Diagram
		InternalBlockDiagramEditor ibdde = projectAccessor
				.getDiagramEditorFactory().getInternalBlockDiagramEditor();
		IInternalBlockDiagram ibd1 = ibdde.createInternalBlockDiagram(
				(IBlock) AstahUtil
						.findBlockByFullName("HSUVStructure::PowerSubsystem"),
				"CAN Bus description");
		// part,port,connector was auto create
		IInternalBlockDiagram ibd2 = ibdde.createInternalBlockDiagram(
				(IBlock) AstahUtil
						.findBlockByFullName("HSUVStructure::HybridSUV"),
				"HybridSUV Internal Structure");
		// part,port,connector was auto create

		// "CAN Bus description"
		ibdde.setDiagram(ibd1);
		INodePresentation noNeedPs = AstahUtil.getPartPresentationByLabel(ibd1,
				":WheelHubAssembly");
		ibdde.deletePresentation(noNeedPs);

		INodePresentation electricalPowerControllerBlockAsoEndsP = AstahUtil
				.getPartPresentationByLabel(ibd1,
						"epc:ElectricalPowerController");
		electricalPowerControllerBlockAsoEndsP.setLocation(new Point2D.Double(
				0, 0));
		INodePresentation fpPortP = AstahUtil.getPortPresentation(ibd1, "fp",
				"HSUVStructure::FS_EPC");
		fpPortP.setLocation(new Point2D.Double(
				electricalPowerControllerBlockAsoEndsP.getLocation().getX()
						+ electricalPowerControllerBlockAsoEndsP.getWidth()
						/ 2.0D, electricalPowerControllerBlockAsoEndsP
						.getLocation().getY()
						+ electricalPowerControllerBlockAsoEndsP.getHeight()
						- 7.0D));
		INodePresentation transmissionBlockAsoEndsP = AstahUtil
				.getPartPresentationByLabel(ibd1, "trsm:Transmission");
		transmissionBlockAsoEndsP.setLocation(new Point2D.Double(200, 0));
		INodePresentation fpPort2P = AstahUtil.getPortPresentation(ibd1, "fp",
				"HSUVStructure::FS_TRSM");
		fpPort2P.setLocation(new Point2D.Double(transmissionBlockAsoEndsP
				.getLocation().getX()
				+ transmissionBlockAsoEndsP.getWidth()
				/ 2.0D, transmissionBlockAsoEndsP.getLocation().getY()
				+ transmissionBlockAsoEndsP.getHeight() - 7.0D));
		INodePresentation internalCombustionEngineBlockAsoEndsP = AstahUtil
				.getPartPresentationByLabel(ibd1,
						"ice:InternalCombustionEngine");
		internalCombustionEngineBlockAsoEndsP.setLocation(new Point2D.Double(
				400, 0));
		INodePresentation iCEFuelFittingPortP = AstahUtil.getPortPresentation(
				ibd1, "ICEFuelFitting", "HSUVStructure::FuelFlow");
		iCEFuelFittingPortP.setLocation(new Point2D.Double(
				internalCombustionEngineBlockAsoEndsP.getLocation().getX()
						+ internalCombustionEngineBlockAsoEndsP.getWidth()
						/ 2.0D, internalCombustionEngineBlockAsoEndsP
						.getLocation().getY()
						+ internalCombustionEngineBlockAsoEndsP.getHeight()
						- 7.0D));
		INodePresentation cANBusBlockAsoEndsP = AstahUtil
				.getPartPresentationByLabel(ibd1, ":CAN_Bus");
		cANBusBlockAsoEndsP.setLocation(new Point2D.Double(100, 200));
		cANBusBlockAsoEndsP.setWidth(300.0D);
		INodePresentation powerControlUnitBlockAsoEndsP = AstahUtil
				.getPartPresentationByLabel(ibd1, "ecu:PowerControlUnit");
		powerControlUnitBlockAsoEndsP.setLocation(new Point2D.Double(0, 400));
		powerControlUnitBlockAsoEndsP.setWidth(500.0D);
		INodePresentation epcPortP = AstahUtil.getPortPresentation(ibd1, "epc",
				"HSUVStructure::FS_EPC");
		epcPortP.setLocation(new Point2D.Double(powerControlUnitBlockAsoEndsP
				.getLocation().getX() + 20.0D, powerControlUnitBlockAsoEndsP
				.getLocation().getY() - 7.0D));
		INodePresentation etrsmPortP = AstahUtil.getPortPresentation(ibd1,
				"etrsm", "HSUVStructure::FS_TRSM");
		etrsmPortP.setLocation(new Point2D.Double(powerControlUnitBlockAsoEndsP
				.getLocation().getX()
				+ powerControlUnitBlockAsoEndsP.getWidth() / 2.0D,
				powerControlUnitBlockAsoEndsP.getLocation().getY() - 7.0D));
		INodePresentation icePortP = AstahUtil.getPortPresentation(ibd1, "ice",
				"HSUVStructure::FS_ICE");
		icePortP.setLocation(new Point2D.Double(powerControlUnitBlockAsoEndsP
				.getLocation().getX()
				+ powerControlUnitBlockAsoEndsP.getWidth() - 30.0D,
				powerControlUnitBlockAsoEndsP.getLocation().getY() - 7.0D));

		// "HybridSUV Internal Structure"
		ibdde.setDiagram(ibd2);
		INodePresentation bodySubsystemBlockAsoEndsP = AstahUtil
				.getPartPresentationByLabel(ibd2, "b:BodySubsystem");
		bodySubsystemBlockAsoEndsP.setLocation(new Point2D.Double(200, 0));
		INodePresentation interiorSubsystemBlockAsoEndsP = AstahUtil
				.getPartPresentationByLabel(ibd2, "i:InteriorSubsystem");
		interiorSubsystemBlockAsoEndsP.setLocation(new Point2D.Double(400, 0));
		INodePresentation chassisSubsystemBlockAsoEndsP = AstahUtil
				.getPartPresentationByLabel(ibd2, "c:ChassisSubsystem");
		chassisSubsystemBlockAsoEndsP.setLocation(new Point2D.Double(0, 200));
		INodePresentation brakeSubsystemBlockAsoEndsP = AstahUtil
				.getPartPresentationByLabel(ibd2, "br:BrakeSubsystem");
		brakeSubsystemBlockAsoEndsP.setLocation(new Point2D.Double(200, 200));
		INodePresentation lightingSubsystemlockAsoEndsP = AstahUtil
				.getPartPresentationByLabel(ibd2, "l:LightingSubsystem");
		lightingSubsystemlockAsoEndsP.setLocation(new Point2D.Double(400, 200));
		INodePresentation powerSubsystemBlockAsoEndsP = AstahUtil
				.getPartPresentationByLabel(ibd2, "p:PowerSubsystem");
		powerSubsystemBlockAsoEndsP.setLocation(new Point2D.Double(0, 400));
	}

}
