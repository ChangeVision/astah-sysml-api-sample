import java.awt.geom.Point2D;
import java.io.IOException;

import com.change_vision.jude.api.inf.editor.BlockDefinitionDiagramEditor;
import com.change_vision.jude.api.inf.editor.ModelEditorFactory;
import com.change_vision.jude.api.inf.editor.SysmlModelEditor;
import com.change_vision.jude.api.inf.editor.TransactionManager;
import com.change_vision.jude.api.inf.editor.UseCaseModelEditor;
import com.change_vision.jude.api.inf.exception.InvalidEditingException;
import com.change_vision.jude.api.inf.exception.InvalidUsingException;
import com.change_vision.jude.api.inf.exception.LicenseNotFoundException;
import com.change_vision.jude.api.inf.exception.ProjectLockedException;
import com.change_vision.jude.api.inf.exception.ProjectNotFoundException;
import com.change_vision.jude.api.inf.model.IAssociation;
import com.change_vision.jude.api.inf.model.IAttribute;
import com.change_vision.jude.api.inf.model.IBlock;
import com.change_vision.jude.api.inf.model.IBlockDefinitionDiagram;
import com.change_vision.jude.api.inf.model.IClass;
import com.change_vision.jude.api.inf.model.IConnector;
import com.change_vision.jude.api.inf.model.IConstraintBlock;
import com.change_vision.jude.api.inf.model.IConstraintProperty;
import com.change_vision.jude.api.inf.model.IFlowProperty;
import com.change_vision.jude.api.inf.model.IModel;
import com.change_vision.jude.api.inf.model.IMultiplicityRange;
import com.change_vision.jude.api.inf.model.IPackage;
import com.change_vision.jude.api.inf.model.IPort;
import com.change_vision.jude.api.inf.model.IQuantityKind;
import com.change_vision.jude.api.inf.model.IUnit;
import com.change_vision.jude.api.inf.model.IValueType;
import com.change_vision.jude.api.inf.presentation.INodePresentation;
import com.change_vision.jude.api.inf.project.ProjectAccessor;

public class CreatingSampleSysmlBlockDefnitionDiagram {
	public static void main(String[] args) {
		CreatingSampleSysmlBlockDefnitionDiagram sample = new CreatingSampleSysmlBlockDefnitionDiagram();
		try {
			sample.create("SampleSysmlBlockDefnitionDiagram.asml");
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

			createBlockModels(projectAccessor);

			createBlockDefnitionDiagram(projectAccessor);

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

	private void createBlockModels(ProjectAccessor projectAccessor)
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

		IPackage hSUVMOEsPackage = sme.createPackage(project, "HSUV MOEs");
		// IConstraintBlock
		IConstraintBlock accellerationEquation = sme.createConstraintBlock(
				hSUVMOEsPackage, "AccellerationEquation");
		sme.createConstraint(accellerationEquation, "a=(550/32)*tp(hp)*dt*tw");
		sme.createConstraintParameter(accellerationEquation, "tw",
				weightValueType);
		sme.createConstraintParameter(accellerationEquation, "delta-t",
				timeValueType);
		sme.createConstraintParameter(accellerationEquation, "tp",
				horsepwrValueType);
		sme.createConstraintParameter(accellerationEquation, "a",
				accelValueType);
		IConstraintBlock capacityEquation = sme.createConstraintBlock(
				hSUVMOEsPackage, "CapacityEquation");
		sme.createConstraintParameter(capacityEquation, "vc", realValueType);
		IConstraintBlock economyEquation = sme.createConstraintBlock(
				hSUVMOEsPackage, "EconomyEquation");
		sme.createConstraintParameter(economyEquation, "f", realValueType);
		IConstraintBlock maxAccelerationAnalysis = sme.createConstraintBlock(
				hSUVMOEsPackage, "MaxAccelerationAnalysis");
		sme.createConstraintParameter(maxAccelerationAnalysis, "q",
				realValueType);
		sme.createConstraintParameter(maxAccelerationAnalysis, "z",
				realValueType);
		IConstraintBlock myObjectiveFunction = sme.createConstraintBlock(
				hSUVMOEsPackage, "MyObjectiveFunction");
		sme.createConstraint(myObjectiveFunction, "CE = Sum(Wi*Pi)");
		sme.createConstraintParameter(myObjectiveFunction, "p1", realValueType);
		sme.createConstraintParameter(myObjectiveFunction, "p2", realValueType);
		sme.createConstraintParameter(myObjectiveFunction, "p3", realValueType);
		sme.createConstraintParameter(myObjectiveFunction, "CE", realValueType);
		sme.createConstraintParameter(myObjectiveFunction, "p4", realValueType);
		sme.createConstraintParameter(myObjectiveFunction, "p5", realValueType);
		IConstraintBlock postionEquation = sme.createConstraintBlock(
				hSUVMOEsPackage, "PostionEquation");
		sme.createConstraint(postionEquation, "x(n+1)=x(n)+v*5280/3600*dt");
		sme.createConstraintParameter(postionEquation, "delta-t", timeValueType);
		sme.createConstraintParameter(postionEquation, "v", realValueType);
		sme.createConstraintParameter(postionEquation, "x", realValueType);
		IConstraintBlock powerEquation = sme.createConstraintBlock(
				hSUVMOEsPackage, "PowerEquation");
		sme.createConstraint(powerEquation, "tp = whlpowr - (Cd*v) - (Cf*tw*v)");
		sme.createConstraintParameter(powerEquation, "whlpowr",
				horsepwrValueType);
		sme.createConstraintParameter(powerEquation, "Cd", realValueType);
		sme.createConstraintParameter(powerEquation, "Cf", realValueType);
		sme.createConstraintParameter(powerEquation, "tw", weightValueType);
		sme.createConstraintParameter(powerEquation, "tp", horsepwrValueType);
		sme.createConstraintParameter(powerEquation, "v", velValueType);
		sme.createConstraintParameter(powerEquation, "i", realValueType);
		IConstraintBlock straightLineVehicleDynamics = sme
				.createConstraintBlock(hSUVMOEsPackage,
						"StraightLineVehicleDynamics");
		sme.createConstraintParameter(straightLineVehicleDynamics, "whlpowr",
				horsepwrValueType);
		sme.createConstraintParameter(straightLineVehicleDynamics, "Cd",
				realValueType);
		sme.createConstraintParameter(straightLineVehicleDynamics, "Cf",
				realValueType);
		sme.createConstraintParameter(straightLineVehicleDynamics, "tw",
				weightValueType);
		sme.createConstraintParameter(straightLineVehicleDynamics, "acc",
				accelValueType);
		sme.createConstraintParameter(straightLineVehicleDynamics, "vel",
				velValueType);
		sme.createConstraintParameter(straightLineVehicleDynamics, "incline",
				realValueType);
		sme.createConstraintParameter(straightLineVehicleDynamics, "x",
				realValueType);
		sme.createConstraintParameter(straightLineVehicleDynamics, "dt",
				timeValueType);
		IConstraintBlock unitCostEquation = sme.createConstraintBlock(
				hSUVMOEsPackage, "UnitCostEquation");
		sme.createConstraintParameter(unitCostEquation, "uc", realValueType);
		IConstraintBlock velocityEquation = sme.createConstraintBlock(
				hSUVMOEsPackage, "VelocityEquation");
		sme.createConstraint(velocityEquation,
				"v(n+1) = v(n)+a*32*3600/5280*dt");
		sme.createConstraintParameter(velocityEquation, "delta-t",
				timeValueType);
		sme.createConstraintParameter(velocityEquation, "v", velValueType);
		sme.createConstraintParameter(velocityEquation, "a", accelValueType);
		// Block
		IBlock masuresOfEffectivenessBlock = sme.createBlock(hSUVMOEsPackage,
				"MeasuresOfEffectiveness");
		sme.createConstraintProperty(straightLineVehicleDynamics, "pwr",
				powerEquation);
		sme.createConstraintProperty(straightLineVehicleDynamics, "pos",
				postionEquation);
		sme.createConstraintProperty(straightLineVehicleDynamics, "vel",
				velocityEquation);
		sme.createConstraintProperty(straightLineVehicleDynamics, "acc",
				accellerationEquation);
		sme.createConstraintProperty(masuresOfEffectivenessBlock, "",
				economyEquation);
		sme.createConstraintProperty(masuresOfEffectivenessBlock, "",
				maxAccelerationAnalysis);
		sme.createConstraintProperty(masuresOfEffectivenessBlock, "",
				capacityEquation);
		sme.createConstraintProperty(masuresOfEffectivenessBlock, "",
				unitCostEquation);
		sme.createConstraintProperty(masuresOfEffectivenessBlock, "",
				myObjectiveFunction);

		IPackage hSUVStructurePackage = sme.createPackage(project,
				"HSUVStructure");
		// Actor
		UseCaseModelEditor ucme = ModelEditorFactory.getUseCaseModelEditor();
		IClass driverActor = ucme.createActor(hSUVStructurePackage, "Driver");
		IClass maintainerActor = ucme.createActor(hSUVStructurePackage,
				"Maintainer");
		IClass passengerActor = ucme.createActor(hSUVStructurePackage,
				"Passenger");
		// Block
		IBlock automotiveDomainBlock = sme.createBlock(hSUVStructurePackage,
				"AutomotiveDomain");
		automotiveDomainBlock.addStereotype("domain");
		IBlock baggageBlock = sme.createBlock(hSUVStructurePackage, "Baggage");
		baggageBlock.addStereotype("external");
		IBlock bodySubsystemBlock = sme.createBlock(hSUVStructurePackage,
				"BodySubsystem");
		IBlock brakePedalBlock = sme.createBlock(hSUVStructurePackage,
				"BrakePedal");
		IBlock brakeSubsystemBlock = sme.createBlock(hSUVStructurePackage,
				"BrakeSubsystem");
		IBlock cANBusBlock = sme.createBlock(hSUVStructurePackage, "CAN_Bus");
		IBlock chassisSubsystemBlock = sme.createBlock(hSUVStructurePackage,
				"ChassisSubsystem");
		IBlock electricalPowerControllerBlock = sme.createBlock(
				hSUVStructurePackage, "ElectricalPowerController");
		IBlock enviromentBlock = sme.createBlock(hSUVStructurePackage,
				"Enviroment");
		enviromentBlock.addStereotype("external");
		IBlock fS_EPCBlock = sme.createBlock(hSUVStructurePackage, "FS_EPC");
		IBlock fS_ICEBlock = sme.createBlock(hSUVStructurePackage, "FS_ICE");
		IBlock fS_TRSMBlock = sme.createBlock(hSUVStructurePackage, "FS_TRSM");
		IBlock fuelFlowBlock = sme
				.createBlock(hSUVStructurePackage, "FuelFlow");
		fuelFlowBlock.addStereotype("flowSpecification");
		IBlock fuelTankAssemblyBlock = sme.createBlock(hSUVStructurePackage,
				"FuelTankAssembly");
		sme.createBlock(hSUVStructurePackage, "HSUV");
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
		IAssociation driverActorAso = sme.createAssociation(
				automotiveDomainBlock, driverActor, "", "", "");
		IAttribute[] driverActorAsoEnds = driverActorAso.getMemberEnds();
		driverActorAsoEnds[0].setComposite();
		IAssociation maintainerActorAso = sme.createAssociation(
				automotiveDomainBlock, maintainerActor, "", "", "");
		IAttribute[] maintainerActorAsoEnds = maintainerActorAso
				.getMemberEnds();
		maintainerActorAsoEnds[0].setComposite();
		IAssociation passengerActorAso = sme.createAssociation(
				automotiveDomainBlock, passengerActor, "", "", "");
		IAttribute[] passengerActorAsoEnds = passengerActorAso.getMemberEnds();
		passengerActorAsoEnds[0].setComposite();
		sme.createPart(automotiveDomainBlock, "HSUV", hybridSUVBlock);
		sme.createPart(automotiveDomainBlock, "VehicleCargo", baggageBlock);
		sme.createPart(automotiveDomainBlock, "drivingConditions",
				enviromentBlock);
		sme.createPart(brakeSubsystemBlock, "", brakePedalBlock);
		IAttribute wheelHubAssemblyBlockAsoEnd = sme.createPart(
				chassisSubsystemBlock, "", wheelHubAssemblyBlock);
		wheelHubAssemblyBlockAsoEnd.setMultiplicity(new int[][] { { 4 } });
		IAttribute roadBlockAsoEnd = sme.createPart(enviromentBlock, "road",
				roadBlock);
		roadBlockAsoEnd.setMultiplicity(new int[][] { { 1,
				IMultiplicityRange.UNLIMITED } });
		sme.createPart(enviromentBlock, "weather", weatherBlock);
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
		IAttribute brakePedalBlockAsoEnd3 = sme.createReference(
				powerSubsystemBlock, "bkp", brakePedalBlock);
		IAssociation brakePedalBlockAso3 = brakePedalBlockAsoEnd3
				.getAssociation();
		IAttribute[] brakePedalBlockAsoEnd3s = brakePedalBlockAso3
				.getMemberEnds();
		brakePedalBlockAsoEnd3s[0].setAggregation();
		IAttribute wheelHubAssemblyBlockAsoEnd2 = sme.createReference(
				powerSubsystemBlock, "", wheelHubAssemblyBlock);
		wheelHubAssemblyBlockAsoEnd2.setMultiplicity(new int[][] { { 2 } });
		sme.createPart(powerSubsystemBlock, "ft", fuelTankAssemblyBlock);
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
		IFlowProperty fuelSupply2 = sme.createFlowProperty(
				fuelTankAssemblyBlock, "fuelSupply", fuelValueType);
		fuelSupply2.setDirectionOut();
		sme.createFlowProperty(fuelTankAssemblyBlock, "fuelReturn",
				fuelValueType);
		IFlowProperty fuelSupply3 = sme.createFlowProperty(
				internalCombustionEngineBlock, "fuelSupply", fuelValueType);
		fuelSupply3.setDirectionOut();
		sme.createFlowProperty(internalCombustionEngineBlock, "fuelReturn",
				fuelValueType);
		sme.createValueAttribute(iCEDataBlock, "rpm", integerValueType);
		sme.createValueAttribute(iCEDataBlock, "temperature", realValueType);
		sme.createValueAttribute(iCEDataBlock, "isKnocking", booleanValueType);
	}

	private void createBlockDefnitionDiagram(ProjectAccessor projectAccessor)
			throws InvalidEditingException, ClassNotFoundException,
			ProjectNotFoundException, InvalidUsingException {
		// Diagram
		BlockDefinitionDiagramEditor bdde = projectAccessor
				.getDiagramEditorFactory().getBlockDefinitionDiagramEditor();
		IBlockDefinitionDiagram bdddod = bdde.createBlockDefinitionDiagram(
				AstahUtil.findPackageByFullName("HSUV MOEs"),
				"Definition of Dynamics");
		IBlockDefinitionDiagram bdd = bdde.createBlockDefinitionDiagram(
				AstahUtil.findPackageByFullName("HSUVStructure"),
				"Automotive Domain BreakDown");
		IBlockDefinitionDiagram bdd2 = bdde.createBlockDefinitionDiagram(
				AstahUtil.findPackageByFullName("HSUVStructure"),
				"CAN Bus Flow Properties");
		IBlockDefinitionDiagram bdd3 = bdde.createBlockDefinitionDiagram(
				AstahUtil.findBlockByFullName("HSUVStructure::HSUV"),
				"PowerSubsystem Fuel Flow Definition");
		IBlockDefinitionDiagram bdd4 = bdde.createBlockDefinitionDiagram(
				AstahUtil.findBlockByFullName("HSUVStructure::HybridSUV"),
				"HybridSUV BreakDown");

		// "Definition of Dynamics"
		bdde.setDiagram(bdddod);
		INodePresentation straightLineVehicleDynamicsP = bdde
				.createNodePresentation(
						AstahUtil
								.findConstraintBlockByFullName("HSUV MOEs::StraightLineVehicleDynamics"),
						new Point2D.Double(300, 0));
		straightLineVehicleDynamicsP.setProperty("constraint_visibility",
				"true");
		straightLineVehicleDynamicsP.setProperty(
				"parameter_compartment_visibility", "true");
		INodePresentation powerEquationP = bdde
				.createNodePresentation(
						AstahUtil
								.findConstraintBlockByFullName("HSUV MOEs::PowerEquation"),
						new Point2D.Double(0, 400));
		powerEquationP.setProperty("constraint_visibility", "true");
		powerEquationP.setProperty("parameter_compartment_visibility", "true");
		bdde.createLinkPresentation(
				((IConstraintProperty) AstahUtil
						.findConstraintProperty(
								(IClass) AstahUtil
										.findConstraintBlockByFullName("HSUV MOEs::StraightLineVehicleDynamics"),
								"pwr",
								(IConstraintBlock) AstahUtil
										.findConstraintBlockByFullName("HSUV MOEs::PowerEquation")))
						.getAssociation(), straightLineVehicleDynamicsP,
				powerEquationP);
		INodePresentation postionEquationP = bdde
				.createNodePresentation(
						AstahUtil
								.findConstraintBlockByFullName("HSUV MOEs::PostionEquation"),
						new Point2D.Double(200, 400));
		postionEquationP.setProperty("constraint_visibility", "true");
		postionEquationP
				.setProperty("parameter_compartment_visibility", "true");
		bdde.createLinkPresentation(
				((IConstraintProperty) AstahUtil
						.findConstraintProperty(
								(IClass) AstahUtil
										.findConstraintBlockByFullName("HSUV MOEs::StraightLineVehicleDynamics"),
								"pos",
								(IConstraintBlock) AstahUtil
										.findConstraintBlockByFullName("HSUV MOEs::PostionEquation")))
						.getAssociation(), straightLineVehicleDynamicsP,
				postionEquationP);
		INodePresentation velocityEquationP = bdde
				.createNodePresentation(
						AstahUtil
								.findConstraintBlockByFullName("HSUV MOEs::VelocityEquation"),
						new Point2D.Double(400, 400));
		velocityEquationP.setProperty("constraint_visibility", "true");
		velocityEquationP.setProperty("parameter_compartment_visibility",
				"true");
		bdde.createLinkPresentation(
				((IConstraintProperty) AstahUtil
						.findConstraintProperty(
								(IClass) AstahUtil
										.findConstraintBlockByFullName("HSUV MOEs::StraightLineVehicleDynamics"),
								"vel",
								(IConstraintBlock) AstahUtil
										.findConstraintBlockByFullName("HSUV MOEs::VelocityEquation")))
						.getAssociation(), straightLineVehicleDynamicsP,
				velocityEquationP);
		INodePresentation accellerationEquationP = bdde
				.createNodePresentation(
						AstahUtil
								.findConstraintBlockByFullName("HSUV MOEs::AccellerationEquation"),
						new Point2D.Double(600, 400));
		accellerationEquationP.setProperty("constraint_visibility", "true");
		accellerationEquationP.setProperty("parameter_compartment_visibility",
				"true");
		bdde.createLinkPresentation(
				((IConstraintProperty) AstahUtil
						.findConstraintProperty(
								(IClass) AstahUtil
										.findConstraintBlockByFullName("HSUV MOEs::StraightLineVehicleDynamics"),
								"acc",
								(IConstraintBlock) AstahUtil
										.findConstraintBlockByFullName("HSUV MOEs::AccellerationEquation")))
						.getAssociation(), straightLineVehicleDynamicsP,
				accellerationEquationP);

		// "Automotive Domain BreakDown"
		bdde.setDiagram(bdd);
		INodePresentation automotiveDomainBlockP = bdde
				.createNodePresentation(
						AstahUtil
								.findBlockByFullName("HSUVStructure::AutomotiveDomain"),
						new Point2D.Double(400, 0));
		automotiveDomainBlockP.setProperty("stereotype_visibility", "true");
		INodePresentation driverActorP = bdde.createNodePresentation(
				AstahUtil.findActorByFullName("HSUVStructure::Driver"),
				new Point2D.Double(0, 200));
		bdde.createLinkPresentation(
				AstahUtil.findAssociation(
						(IClass) AstahUtil
								.findBlockByFullName("HSUVStructure::AutomotiveDomain"),
						(IClass) AstahUtil
								.findActorByFullName("HSUVStructure::Driver"),
						"", "", ""), automotiveDomainBlockP, driverActorP);
		INodePresentation maintainerActorP = bdde.createNodePresentation(
				AstahUtil.findActorByFullName("HSUVStructure::Maintainer"),
				new Point2D.Double(200, 200));
		bdde.createLinkPresentation(
				AstahUtil.findAssociation(
						(IClass) AstahUtil
								.findBlockByFullName("HSUVStructure::AutomotiveDomain"),
						(IClass) AstahUtil
								.findActorByFullName("HSUVStructure::Maintainer"),
						"", "", ""), automotiveDomainBlockP, maintainerActorP);
		INodePresentation passengerActorP = bdde.createNodePresentation(
				AstahUtil.findActorByFullName("HSUVStructure::Passenger"),
				new Point2D.Double(400, 200));
		bdde.createLinkPresentation(
				AstahUtil.findAssociation(
						(IClass) AstahUtil
								.findBlockByFullName("HSUVStructure::AutomotiveDomain"),
						(IClass) AstahUtil
								.findActorByFullName("HSUVStructure::Passenger"),
						"", "", ""), automotiveDomainBlockP, passengerActorP);
		INodePresentation hybridSUVBlockP = bdde.createNodePresentation(
				AstahUtil.findBlockByFullName("HSUVStructure::HybridSUV"),
				new Point2D.Double(600, 200));
		hybridSUVBlockP.setProperty("stereotype_visibility", "true");
		bdde.createLinkPresentation(
				((IAttribute) AstahUtil
						.findPart(
								(IClass) AstahUtil
										.findBlockByFullName("HSUVStructure::AutomotiveDomain"),
								"HSUV",
								(IBlock) AstahUtil
										.findBlockByFullName("HSUVStructure::HybridSUV")))
						.getAssociation(), automotiveDomainBlockP,
				hybridSUVBlockP);
		INodePresentation baggageBlockP = bdde.createNodePresentation(
				AstahUtil.findBlockByFullName("HSUVStructure::Baggage"),
				new Point2D.Double(800, 200));
		baggageBlockP.setProperty("stereotype_visibility", "true");
		bdde.createLinkPresentation(
				((IAttribute) AstahUtil
						.findPart(
								(IClass) AstahUtil
										.findBlockByFullName("HSUVStructure::AutomotiveDomain"),
								"VehicleCargo",
								(IBlock) AstahUtil
										.findBlockByFullName("HSUVStructure::Baggage")))
						.getAssociation(), automotiveDomainBlockP,
				baggageBlockP);
		INodePresentation enviromentBlockP = bdde.createNodePresentation(
				AstahUtil.findBlockByFullName("HSUVStructure::Enviroment"),
				new Point2D.Double(1000, 200));
		enviromentBlockP.setProperty("stereotype_visibility", "true");
		bdde.createLinkPresentation(
				((IAttribute) AstahUtil
						.findPart(
								(IClass) AstahUtil
										.findBlockByFullName("HSUVStructure::AutomotiveDomain"),
								"drivingConditions",
								(IBlock) AstahUtil
										.findBlockByFullName("HSUVStructure::Enviroment")))
						.getAssociation(), automotiveDomainBlockP,
				enviromentBlockP);
		INodePresentation weatherBlockP = bdde.createNodePresentation(
				AstahUtil.findBlockByFullName("HSUVStructure::Weather"),
				new Point2D.Double(900, 400));
		weatherBlockP.setProperty("stereotype_visibility", "true");
		bdde.createLinkPresentation(((IAttribute) AstahUtil.findPart(
				(IClass) AstahUtil
						.findBlockByFullName("HSUVStructure::Enviroment"),
				"weather", (IBlock) AstahUtil
						.findBlockByFullName("HSUVStructure::Weather")))
				.getAssociation(), enviromentBlockP, weatherBlockP);
		INodePresentation roadBlockP = bdde.createNodePresentation(
				AstahUtil.findBlockByFullName("HSUVStructure::Road"),
				new Point2D.Double(1100, 400));
		roadBlockP.setProperty("stereotype_visibility", "true");
		bdde.createLinkPresentation(((IAttribute) AstahUtil.findPart(
				(IClass) AstahUtil
						.findBlockByFullName("HSUVStructure::Enviroment"),
				"road", (IBlock) AstahUtil
						.findBlockByFullName("HSUVStructure::Road")))
				.getAssociation(), enviromentBlockP, roadBlockP);

		// "CAN Bus Flow Properties"
		bdde.setDiagram(bdd2);
		INodePresentation fS_ICEBlockP = bdde.createNodePresentation(
				AstahUtil.findBlockByFullName("HSUVStructure::FS_ICE"),
				new Point2D.Double(0, 0));
		fS_ICEBlockP.setProperty("flow_compartment_visibility", "true");
		INodePresentation iCEDataBlockP = bdde.createNodePresentation(
				AstahUtil.findBlockByFullName("HSUVStructure::ICEData"),
				new Point2D.Double(300, 0));
		iCEDataBlockP.setProperty("value_compartment_visibility", "true");
		iCEDataBlockP.setProperty("stereotype_visibility", "true");
		INodePresentation fS_TRSMBlockP = bdde.createNodePresentation(
				AstahUtil.findBlockByFullName("HSUVStructure::FS_TRSM"),
				new Point2D.Double(0, 200));
		fS_TRSMBlockP.setProperty("flow_compartment_visibility", "true");
		INodePresentation note = bdde
				.createNote(
						"To be specified – What is being exchanged over the bus to/from the transmission.",
						new Point2D.Double(300, 200));
		bdde.createNoteAnchor(note, fS_TRSMBlockP);
		INodePresentation fS_EPCBlockP = bdde.createNodePresentation(
				AstahUtil.findBlockByFullName("HSUVStructure::FS_EPC"),
				new Point2D.Double(0, 400));
		fS_EPCBlockP.setProperty("flow_compartment_visibility", "true");
		INodePresentation note2 = bdde
				.createNote(
						"To be specified – What is being exchanged over the bus to/from the electronic power controller.",
						new Point2D.Double(300, 400));
		bdde.createNoteAnchor(note2, fS_EPCBlockP);

		// "PowerSubsystem Fuel Flow Definition"
		bdde.setDiagram(bdd3);
		INodePresentation powerSubsystemBlockP = bdde.createNodePresentation(
				AstahUtil.findBlockByFullName("HSUVStructure::PowerSubsystem"),
				new Point2D.Double(100, 0));
		INodePresentation fuelValueTypeP = bdde.createNodePresentation(
				AstahUtil.findValueTypeByFullName("HSUVStructure::Fuel"),
				new Point2D.Double(300, 0));
		fuelValueTypeP.setProperty("property_compartment_visibility", "true");
		fuelValueTypeP.setProperty("stereotype_visibility", "true");
		INodePresentation fuelTankAssemblyBlockP = bdde
				.createNodePresentation(
						AstahUtil
								.findBlockByFullName("HSUVStructure::FuelTankAssembly"),
						new Point2D.Double(0, 200));
		fuelTankAssemblyBlockP.setProperty("flow_compartment_visibility",
				"true");
		bdde.createLinkPresentation(
				((IAttribute) AstahUtil
						.findPart(
								(IClass) AstahUtil
										.findBlockByFullName("HSUVStructure::PowerSubsystem"),
								"ft",
								(IBlock) AstahUtil
										.findBlockByFullName("HSUVStructure::FuelTankAssembly")))
						.getAssociation(), powerSubsystemBlockP,
				fuelTankAssemblyBlockP);
		INodePresentation internalCombustionEngineBlockP = bdde
				.createNodePresentation(
						AstahUtil
								.findBlockByFullName("HSUVStructure::InternalCombustionEngine"),
						new Point2D.Double(300, 200));
		internalCombustionEngineBlockP.setProperty(
				"flow_compartment_visibility", "true");
		bdde.createLinkPresentation(
				((IAttribute) AstahUtil
						.findPart(
								(IClass) AstahUtil
										.findBlockByFullName("HSUVStructure::PowerSubsystem"),
								"ice",
								(IBlock) AstahUtil
										.findBlockByFullName("HSUVStructure::InternalCombustionEngine")))
						.getAssociation(), powerSubsystemBlockP,
				internalCombustionEngineBlockP);
		INodePresentation fuelFlowBlockP = bdde.createNodePresentation(
				AstahUtil.findBlockByFullName("HSUVStructure::FuelFlow"),
				new Point2D.Double(100, 400));
		fuelFlowBlockP.setProperty("flow_compartment_visibility", "true");
		fuelFlowBlockP.setProperty("stereotype_visibility", "true");

		// HybridSUV BreakDown
		bdde.setDiagram(bdd4);
		INodePresentation hybridSUVBlockP2 = bdde.createNodePresentation(
				AstahUtil.findBlockByFullName("HSUVStructure::HybridSUV"),
				new Point2D.Double(600, 0));
		hybridSUVBlockP2.setProperty("stereotype_visibility", "true");
		INodePresentation powerSubsystemBlockP2 = bdde.createNodePresentation(
				AstahUtil.findBlockByFullName("HSUVStructure::PowerSubsystem"),
				new Point2D.Double(0, 200));
		bdde.createLinkPresentation(((IAttribute) AstahUtil.findPart(
				(IClass) AstahUtil
						.findBlockByFullName("HSUVStructure::HybridSUV"), "p",
				(IBlock) AstahUtil
						.findBlockByFullName("HSUVStructure::PowerSubsystem")))
				.getAssociation(), hybridSUVBlockP2, powerSubsystemBlockP2);
		INodePresentation brakeSubsystemBlockP = bdde.createNodePresentation(
				AstahUtil.findBlockByFullName("HSUVStructure::BrakeSubsystem"),
				new Point2D.Double(200, 200));
		bdde.createLinkPresentation(((IAttribute) AstahUtil.findPart(
				(IClass) AstahUtil
						.findBlockByFullName("HSUVStructure::HybridSUV"), "br",
				(IBlock) AstahUtil
						.findBlockByFullName("HSUVStructure::BrakeSubsystem")))
				.getAssociation(), hybridSUVBlockP2, brakeSubsystemBlockP);
		INodePresentation bodySubsystemBlockP = bdde.createNodePresentation(
				AstahUtil.findBlockByFullName("HSUVStructure::BodySubsystem"),
				new Point2D.Double(400, 200));
		bdde.createLinkPresentation(((IAttribute) AstahUtil.findPart(
				(IClass) AstahUtil
						.findBlockByFullName("HSUVStructure::HybridSUV"), "b",
				(IBlock) AstahUtil
						.findBlockByFullName("HSUVStructure::BodySubsystem")))
				.getAssociation(), hybridSUVBlockP2, bodySubsystemBlockP);
		INodePresentation interiorSubsystemBlockP = bdde
				.createNodePresentation(
						AstahUtil
								.findBlockByFullName("HSUVStructure::InteriorSubsystem"),
						new Point2D.Double(600, 200));
		bdde.createLinkPresentation(
				((IAttribute) AstahUtil
						.findPart(
								(IClass) AstahUtil
										.findBlockByFullName("HSUVStructure::HybridSUV"),
								"i",
								(IBlock) AstahUtil
										.findBlockByFullName("HSUVStructure::InteriorSubsystem")))
						.getAssociation(), hybridSUVBlockP2,
				interiorSubsystemBlockP);
		INodePresentation lightingSubsystemlockP = bdde
				.createNodePresentation(
						AstahUtil
								.findBlockByFullName("HSUVStructure::LightingSubsystem"),
						new Point2D.Double(800, 200));
		bdde.createLinkPresentation(
				((IAttribute) AstahUtil
						.findPart(
								(IClass) AstahUtil
										.findBlockByFullName("HSUVStructure::HybridSUV"),
								"l",
								(IBlock) AstahUtil
										.findBlockByFullName("HSUVStructure::LightingSubsystem")))
						.getAssociation(), hybridSUVBlockP2,
				lightingSubsystemlockP);
		INodePresentation chassisSubsystemBlockP = bdde
				.createNodePresentation(
						AstahUtil
								.findBlockByFullName("HSUVStructure::ChassisSubsystem"),
						new Point2D.Double(1000, 200));
		bdde.createLinkPresentation(
				((IAttribute) AstahUtil
						.findPart(
								(IClass) AstahUtil
										.findBlockByFullName("HSUVStructure::HybridSUV"),
								"c",
								(IBlock) AstahUtil
										.findBlockByFullName("HSUVStructure::ChassisSubsystem")))
						.getAssociation(), hybridSUVBlockP2,
				chassisSubsystemBlockP);
		INodePresentation brakePedalBlockP = bdde.createNodePresentation(
				AstahUtil.findBlockByFullName("HSUVStructure::BrakePedal"),
				new Point2D.Double(0, 400));
		bdde.createLinkPresentation(((IAttribute) AstahUtil.findReference(
				(IClass) AstahUtil
						.findBlockByFullName("HSUVStructure::PowerSubsystem"),
				"bkp", (IBlock) AstahUtil
						.findBlockByFullName("HSUVStructure::BrakePedal")))
				.getAssociation(), powerSubsystemBlockP2, brakePedalBlockP);
		bdde.createLinkPresentation(((IAttribute) AstahUtil.findPart(
				(IClass) AstahUtil
						.findBlockByFullName("HSUVStructure::BrakeSubsystem"),
				"", (IBlock) AstahUtil
						.findBlockByFullName("HSUVStructure::BrakePedal")))
				.getAssociation(), brakeSubsystemBlockP, brakePedalBlockP);
		INodePresentation wheelHubAssemblyBlockP = bdde
				.createNodePresentation(
						AstahUtil
								.findBlockByFullName("HSUVStructure::WheelHubAssembly"),
						new Point2D.Double(1000, 400));
		bdde.createLinkPresentation(
				((IAttribute) AstahUtil
						.findPart(
								(IClass) AstahUtil
										.findBlockByFullName("HSUVStructure::ChassisSubsystem"),
								"",
								(IBlock) AstahUtil
										.findBlockByFullName("HSUVStructure::WheelHubAssembly")))
						.getAssociation(), chassisSubsystemBlockP,
				wheelHubAssemblyBlockP);
		bdde.createLinkPresentation(
				((IAttribute) AstahUtil
						.findReference(
								(IClass) AstahUtil
										.findBlockByFullName("HSUVStructure::PowerSubsystem"),
								"",
								(IBlock) AstahUtil
										.findBlockByFullName("HSUVStructure::WheelHubAssembly")))
						.getAssociation(), powerSubsystemBlockP2,
				wheelHubAssemblyBlockP);
	}
}
