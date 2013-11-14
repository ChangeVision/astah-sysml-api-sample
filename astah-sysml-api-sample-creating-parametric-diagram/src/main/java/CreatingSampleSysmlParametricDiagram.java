import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.io.IOException;

import com.change_vision.jude.api.inf.editor.ModelEditorFactory;
import com.change_vision.jude.api.inf.editor.ParametricDiagramEditor;
import com.change_vision.jude.api.inf.editor.SysmlModelEditor;
import com.change_vision.jude.api.inf.editor.TransactionManager;
import com.change_vision.jude.api.inf.exception.InvalidUsingException;
import com.change_vision.jude.api.inf.exception.LicenseNotFoundException;
import com.change_vision.jude.api.inf.exception.ProjectLockedException;
import com.change_vision.jude.api.inf.exception.ProjectNotFoundException;
import com.change_vision.jude.api.inf.model.IBindingConnector;
import com.change_vision.jude.api.inf.model.IBlock;
import com.change_vision.jude.api.inf.model.IComment;
import com.change_vision.jude.api.inf.model.IConstraintBlock;
import com.change_vision.jude.api.inf.model.IConstraintParameter;
import com.change_vision.jude.api.inf.model.IConstraintProperty;
import com.change_vision.jude.api.inf.model.IModel;
import com.change_vision.jude.api.inf.model.IPackage;
import com.change_vision.jude.api.inf.model.IParametricDiagram;
import com.change_vision.jude.api.inf.model.IQuantityKind;
import com.change_vision.jude.api.inf.model.IUnit;
import com.change_vision.jude.api.inf.model.IValueProperty;
import com.change_vision.jude.api.inf.model.IValueType;
import com.change_vision.jude.api.inf.presentation.ILinkPresentation;
import com.change_vision.jude.api.inf.presentation.INodePresentation;
import com.change_vision.jude.api.inf.presentation.IPresentation;
import com.change_vision.jude.api.inf.project.ProjectAccessor;

public class CreatingSampleSysmlParametricDiagram {
	public static void main(String[] args) {
		CreatingSampleSysmlParametricDiagram sample = new CreatingSampleSysmlParametricDiagram();
		try {
			sample.create("SampleSysmlParametricDiagram.asml");
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

			createParametricModelandDiagram(projectAccessor);

			TransactionManager.endTransaction();
			projectAccessor.save();

			System.out.println("Create " + name + " Project done.");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			TransactionManager.abortTransaction();
			projectAccessor.close();
		}
	}

	private void createParametricModelandDiagram(ProjectAccessor projectAccessor)
			throws Exception {
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
		IConstraintParameter twaccellerationEquation = sme
				.createConstraintParameter(accellerationEquation, "tw",
						weightValueType);
		IConstraintParameter deltaaccellerationEquation = sme
				.createConstraintParameter(accellerationEquation, "delta-t",
						timeValueType);
		IConstraintParameter tpaccellerationEquation = sme
				.createConstraintParameter(accellerationEquation, "tp",
						horsepwrValueType);
		IConstraintParameter aaccellerationEquation = sme
				.createConstraintParameter(accellerationEquation, "a",
						accelValueType);
		IConstraintBlock capacityEquation = sme.createConstraintBlock(
				hSUVMOEsPackage, "CapacityEquation");
		IConstraintParameter vc = sme.createConstraintParameter(
				capacityEquation, "vc", realValueType);
		IConstraintBlock economyEquation = sme.createConstraintBlock(
				hSUVMOEsPackage, "EconomyEquation");
		IConstraintParameter feconomyEquation = sme.createConstraintParameter(
				economyEquation, "f", realValueType);
		IConstraintBlock maxAccelerationAnalysis = sme.createConstraintBlock(
				hSUVMOEsPackage, "MaxAccelerationAnalysis");
		IConstraintParameter qmaxAccelerationAnalysis = sme
				.createConstraintParameter(maxAccelerationAnalysis, "q",
						realValueType);
		IConstraintParameter zmaxAccelerationAnalysis = sme
				.createConstraintParameter(maxAccelerationAnalysis, "z",
						realValueType);
		IConstraintBlock myObjectiveFunction = sme.createConstraintBlock(
				hSUVMOEsPackage, "MyObjectiveFunction");
		sme.createConstraint(myObjectiveFunction, "CE = Sum(Wi*Pi)");
		IConstraintParameter p1myObjectiveFunction = sme
				.createConstraintParameter(myObjectiveFunction, "p1",
						realValueType);
		IConstraintParameter p2myObjectiveFunction = sme
				.createConstraintParameter(myObjectiveFunction, "p2",
						realValueType);
		IConstraintParameter p3myObjectiveFunction = sme
				.createConstraintParameter(myObjectiveFunction, "p3",
						realValueType);
		IConstraintParameter cemyObjectiveFunction = sme
				.createConstraintParameter(myObjectiveFunction, "CE",
						realValueType);
		IConstraintParameter p4myObjectiveFunction = sme
				.createConstraintParameter(myObjectiveFunction, "p4",
						realValueType);
		IConstraintParameter p5myObjectiveFunction = sme
				.createConstraintParameter(myObjectiveFunction, "p5",
						realValueType);
		IConstraintBlock postionEquation = sme.createConstraintBlock(
				hSUVMOEsPackage, "PostionEquation");
		sme.createConstraint(postionEquation, "x(n+1)=x(n)+v*5280/3600*dt");
		IConstraintParameter deltapostionEquation = sme
				.createConstraintParameter(postionEquation, "delta-t",
						timeValueType);
		IConstraintParameter vpostionEquation = sme.createConstraintParameter(
				postionEquation, "v", realValueType);
		IConstraintParameter xpostionEquation = sme.createConstraintParameter(
				postionEquation, "x", realValueType);
		IConstraintBlock powerEquation = sme.createConstraintBlock(
				hSUVMOEsPackage, "PowerEquation");
		sme.createConstraint(powerEquation, "tp = whlpowr - (Cd*v) - (Cf*tw*v)");
		IConstraintParameter whlpowrpowerEquation = sme
				.createConstraintParameter(powerEquation, "whlpowr",
						horsepwrValueType);
		IConstraintParameter cdpowerEquation = sme.createConstraintParameter(
				powerEquation, "Cd", realValueType);
		IConstraintParameter cfpowerEquation = sme.createConstraintParameter(
				powerEquation, "Cf", realValueType);
		IConstraintParameter twpowerEquation = sme.createConstraintParameter(
				powerEquation, "tw", weightValueType);
		IConstraintParameter tppowerEquation = sme.createConstraintParameter(
				powerEquation, "tp", horsepwrValueType);
		IConstraintParameter vpowerEquation = sme.createConstraintParameter(
				powerEquation, "v", velValueType);
		IConstraintParameter ipowerEquation = sme.createConstraintParameter(
				powerEquation, "i", realValueType);
		IConstraintBlock straightLineVehicleDynamics = sme
				.createConstraintBlock(hSUVMOEsPackage,
						"StraightLineVehicleDynamics");
		IConstraintParameter whlpowrstraightLineVehicleDynamics = sme
				.createConstraintParameter(straightLineVehicleDynamics,
						"whlpowr", horsepwrValueType);
		IConstraintParameter cdstraightLineVehicleDynamics = sme
				.createConstraintParameter(straightLineVehicleDynamics, "Cd",
						realValueType);
		IConstraintParameter cfstraightLineVehicleDynamics = sme
				.createConstraintParameter(straightLineVehicleDynamics, "Cf",
						realValueType);
		IConstraintParameter twstraightLineVehicleDynamics = sme
				.createConstraintParameter(straightLineVehicleDynamics, "tw",
						weightValueType);
		IConstraintParameter accstraightLineVehicleDynamics = sme
				.createConstraintParameter(straightLineVehicleDynamics, "acc",
						accelValueType);
		IConstraintParameter velstraightLineVehicleDynamics = sme
				.createConstraintParameter(straightLineVehicleDynamics, "vel",
						velValueType);
		IConstraintParameter inclinestraightLineVehicleDynamics = sme
				.createConstraintParameter(straightLineVehicleDynamics,
						"incline", realValueType);
		IConstraintParameter xstraightLineVehicleDynamics = sme
				.createConstraintParameter(straightLineVehicleDynamics, "x",
						realValueType);
		IConstraintParameter dtstraightLineVehicleDynamics = sme
				.createConstraintParameter(straightLineVehicleDynamics, "dt",
						timeValueType);
		IConstraintBlock unitCostEquation = sme.createConstraintBlock(
				hSUVMOEsPackage, "UnitCostEquation");
		IConstraintParameter ucunitCostEquation = sme
				.createConstraintParameter(unitCostEquation, "uc",
						realValueType);
		IConstraintBlock velocityEquation = sme.createConstraintBlock(
				hSUVMOEsPackage, "VelocityEquation");
		sme.createConstraint(velocityEquation,
				"v(n+1) = v(n)+a*32*3600/5280*dt");
		IConstraintParameter deltavelocityEquation = sme
				.createConstraintParameter(velocityEquation, "delta-t",
						timeValueType);
		IConstraintParameter vvelocityEquation = sme.createConstraintParameter(
				velocityEquation, "v", velValueType);
		IConstraintParameter avelocityEquation = sme.createConstraintParameter(
				velocityEquation, "a", accelValueType);
		// Block
		IBlock masuresOfEffectivenessBlock = sme.createBlock(hSUVMOEsPackage,
				"MeasuresOfEffectiveness");
		// ConstraintProperty
		IConstraintProperty powerEquationAsoEnd = sme.createConstraintProperty(
				straightLineVehicleDynamics, "pwr", powerEquation);
		IConstraintProperty postionEquationAsoEnd = sme
				.createConstraintProperty(straightLineVehicleDynamics, "pos",
						postionEquation);
		IConstraintProperty velocityEquationAsoEnd = sme
				.createConstraintProperty(straightLineVehicleDynamics, "vel",
						velocityEquation);
		IConstraintProperty accellerationEquationAsoEnd = sme
				.createConstraintProperty(straightLineVehicleDynamics, "acc",
						accellerationEquation);
		IConstraintProperty economyEquationProp = sme.createConstraintProperty(
				masuresOfEffectivenessBlock, "", economyEquation);
		IConstraintProperty maxAccelerationAnalysisProp = sme
				.createConstraintProperty(masuresOfEffectivenessBlock, "",
						maxAccelerationAnalysis);
		IConstraintProperty capacityEquationProp = sme
				.createConstraintProperty(masuresOfEffectivenessBlock, "",
						capacityEquation);
		IConstraintProperty unitCostEquationProp = sme
				.createConstraintProperty(masuresOfEffectivenessBlock, "",
						unitCostEquation);
		IConstraintProperty myObjectiveFunctionProp = sme
				.createConstraintProperty(masuresOfEffectivenessBlock, "",
						myObjectiveFunction);
		// Connector
		IBindingConnector twFrameConnector = sme.createBindingConnector(null,
				twstraightLineVehicleDynamics, powerEquationAsoEnd,
				twpowerEquation);
		IBindingConnector tw2FrameConnector = sme.createBindingConnector(null,
				twstraightLineVehicleDynamics, accellerationEquationAsoEnd,
				twaccellerationEquation);
		IBindingConnector cfFrameConnector = sme.createBindingConnector(null,
				cfstraightLineVehicleDynamics, powerEquationAsoEnd,
				cfpowerEquation);
		IBindingConnector cdFrameConnector = sme.createBindingConnector(null,
				cdstraightLineVehicleDynamics, powerEquationAsoEnd,
				cdpowerEquation);
		IBindingConnector whlpowrFrameConnector = sme.createBindingConnector(
				null, whlpowrstraightLineVehicleDynamics, powerEquationAsoEnd,
				whlpowrpowerEquation);
		IBindingConnector inclineFrameConnector = sme.createBindingConnector(
				null, inclinestraightLineVehicleDynamics, powerEquationAsoEnd,
				ipowerEquation);
		IBindingConnector acclineFrameConnector = sme.createBindingConnector(
				null, accstraightLineVehicleDynamics,
				accellerationEquationAsoEnd, aaccellerationEquation);
		IBindingConnector dtFrameConnector = sme.createBindingConnector(null,
				dtstraightLineVehicleDynamics, velocityEquationAsoEnd,
				deltavelocityEquation);
		IBindingConnector velFrameConnector = sme.createBindingConnector(null,
				velstraightLineVehicleDynamics, velocityEquationAsoEnd,
				vvelocityEquation);
		IBindingConnector xFrameConnector = sme.createBindingConnector(null,
				xstraightLineVehicleDynamics, postionEquationAsoEnd,
				xpostionEquation);

		IBindingConnector v_v_Connector = sme.createBindingConnector(
				powerEquationAsoEnd, vpowerEquation, velocityEquationAsoEnd,
				vvelocityEquation);
		IBindingConnector tp_tp_Connector = sme.createBindingConnector(
				powerEquationAsoEnd, tppowerEquation,
				accellerationEquationAsoEnd, tpaccellerationEquation);
		IBindingConnector delta_delta_Connector = sme.createBindingConnector(
				accellerationEquationAsoEnd, deltaaccellerationEquation,
				postionEquationAsoEnd, deltapostionEquation);

		IBindingConnector a_a_Connector = sme.createBindingConnector(
				accellerationEquationAsoEnd, aaccellerationEquation,
				velocityEquationAsoEnd, avelocityEquation);
		IBindingConnector v_v_Connector2 = sme.createBindingConnector(
				velocityEquationAsoEnd, vvelocityEquation,
				postionEquationAsoEnd, vpostionEquation);

		// Diagram
		ParametricDiagramEditor parde = projectAccessor
				.getDiagramEditorFactory().getParametricDiagramEditor();
		IParametricDiagram parDgm1 = parde.createParametricDiagram(
				masuresOfEffectivenessBlock, "HSUV MOEs");
		IParametricDiagram parDgm2 = parde.createParametricDiagram(
				straightLineVehicleDynamics, "StraightLineVehicleDynamics");

		// "HSUV MOEs"
		parde.setDiagram(parDgm1);
		// parametic diagram's ConstraintProperty presentation created by
		// diagram;
		INodePresentation economyEquationPropP = ((INodePresentation) economyEquationProp
				.getPresentations()[0]);
		economyEquationPropP.setLocation(new Point2D.Double(0, 0));
		INodePresentation maxAccelerationAnalysisPropP = ((INodePresentation) maxAccelerationAnalysisProp
				.getPresentations()[0]);
		maxAccelerationAnalysisPropP.setLocation(new Point2D.Double(0, 150));
		INodePresentation capacityEquationPropP = ((INodePresentation) capacityEquationProp
				.getPresentations()[0]);
		capacityEquationPropP.setLocation(new Point2D.Double(0, 300));
		INodePresentation unitCostEquationPropP = ((INodePresentation) unitCostEquationProp
				.getPresentations()[0]);
		unitCostEquationPropP.setLocation(new Point2D.Double(0, 400));
		INodePresentation myObjectiveFunctionPropP = ((INodePresentation) myObjectiveFunctionProp
				.getPresentations()[0]);
		myObjectiveFunctionPropP.setLocation(new Point2D.Double(600, 0));
		myObjectiveFunctionPropP.setWidth(220);
		myObjectiveFunctionPropP.setHeight(220);

		INodePresentation fuelEconomyP = parde.createValuePropertyPresentation(
				"HSUValt1.FuelEconomy", new Point2D.Double(300, 0));
		((IValueProperty) fuelEconomyP.getModel()).addStereotype("moe");
		INodePresentation quarterMileTimeP = parde
				.createValuePropertyPresentation("HSUValt1.QuarterMileTime",
						new Point2D.Double(300, 100));
		((IValueProperty) quarterMileTimeP.getModel()).addStereotype("moe");
		INodePresentation zero60TimeP = parde.createValuePropertyPresentation(
				"HSUValt1.Zero60Time", new Point2D.Double(300, 200));
		((IValueProperty) zero60TimeP.getModel()).addStereotype("moe");
		INodePresentation cargoCapacityP = parde
				.createValuePropertyPresentation("HSUValt1.CargoCapacity",
						new Point2D.Double(300, 300));
		((IValueProperty) cargoCapacityP.getModel()).addStereotype("moe");
		INodePresentation unitCostP = parde.createValuePropertyPresentation(
				"HSUValt1.UnitCost", new Point2D.Double(300, 400));
		((IValueProperty) unitCostP.getModel()).addStereotype("moe");
		INodePresentation costEffectivenessP = parde
				.createValuePropertyPresentation("HSUValt1.CostEffectiveness",
						new Point2D.Double(600, -100));
		((IValueProperty) costEffectivenessP.getModel()).addStereotype("moe");

		INodePresentation fPresentation = (INodePresentation) feconomyEquation
				.getPresentations()[0];
		fPresentation.setLocation(new Point2D.Double(economyEquationPropP
				.getLocation().getX() + economyEquationPropP.getWidth(),
				economyEquationPropP.getLocation().getY()
						+ economyEquationPropP.getHeight() / 2.0));
		INodePresentation qPresentation = (INodePresentation) qmaxAccelerationAnalysis
				.getPresentations()[0];
		qPresentation.setLocation(new Point2D.Double(
				maxAccelerationAnalysisPropP.getLocation().getX()
						+ maxAccelerationAnalysisPropP.getWidth(),
				maxAccelerationAnalysisPropP.getLocation().getY() + 20));
		INodePresentation zPresentation = (INodePresentation) zmaxAccelerationAnalysis
				.getPresentations()[0];
		zPresentation.setLocation(new Point2D.Double(
				maxAccelerationAnalysisPropP.getLocation().getX()
						+ maxAccelerationAnalysisPropP.getWidth(),
				maxAccelerationAnalysisPropP.getLocation().getY() + 50));
		INodePresentation vcPresentation = (INodePresentation) vc
				.getPresentations()[0];
		vcPresentation.setLocation(new Point2D.Double(capacityEquationPropP
				.getLocation().getX() + capacityEquationPropP.getWidth(),
				capacityEquationPropP.getLocation().getY()
						+ capacityEquationPropP.getHeight() / 2.0));
		INodePresentation ucPresentation = (INodePresentation) ucunitCostEquation
				.getPresentations()[0];
		ucPresentation.setLocation(new Point2D.Double(unitCostEquationPropP
				.getLocation().getX() + unitCostEquationPropP.getWidth(),
				unitCostEquationPropP.getLocation().getY()
						+ unitCostEquationPropP.getHeight() / 2.0));

		INodePresentation p1myObjectiveFunctionP = ((INodePresentation) p1myObjectiveFunction
				.getPresentations()[0]);
		p1myObjectiveFunctionP.setLocation(new Point2D.Double(
				myObjectiveFunctionPropP.getLocation().getX(),
				myObjectiveFunctionPropP.getLocation().getY() + 30));
		INodePresentation p2myObjectiveFunctionP = ((INodePresentation) p2myObjectiveFunction
				.getPresentations()[0]);
		p2myObjectiveFunctionP.setLocation(new Point2D.Double(
				myObjectiveFunctionPropP.getLocation().getX(),
				myObjectiveFunctionPropP.getLocation().getY() + 60));
		INodePresentation p3myObjectiveFunctionP = ((INodePresentation) p3myObjectiveFunction
				.getPresentations()[0]);
		p3myObjectiveFunctionP.setLocation(new Point2D.Double(
				myObjectiveFunctionPropP.getLocation().getX(),
				myObjectiveFunctionPropP.getLocation().getY() + 90));
		INodePresentation cemyObjectiveFunctionP = ((INodePresentation) cemyObjectiveFunction
				.getPresentations()[0]);
		cemyObjectiveFunctionP.setLocation(new Point2D.Double(
				myObjectiveFunctionPropP.getLocation().getX()
						+ myObjectiveFunctionPropP.getWidth() / 2.0D,
				myObjectiveFunctionPropP.getLocation().getY()));
		INodePresentation p4myObjectiveFunctionP = ((INodePresentation) p4myObjectiveFunction
				.getPresentations()[0]);
		p4myObjectiveFunctionP.setLocation(new Point2D.Double(
				myObjectiveFunctionPropP.getLocation().getX() + 20,
				myObjectiveFunctionPropP.getLocation().getY()
						+ myObjectiveFunctionPropP.getHeight()));
		INodePresentation p5myObjectiveFunctionP = ((INodePresentation) p5myObjectiveFunction
				.getPresentations()[0]);
		p5myObjectiveFunctionP.setLocation(new Point2D.Double(
				myObjectiveFunctionPropP.getLocation().getX() + 100,
				myObjectiveFunctionPropP.getLocation().getY()
						+ myObjectiveFunctionPropP.getHeight()));

		IBindingConnector fbindingConnector = sme.createBindingConnector(
				economyEquationProp, feconomyEquation,
				(IValueProperty) fuelEconomyP.getModel(), null);
		ILinkPresentation fbindingConnectorP = parde.createLinkPresentation(
				fbindingConnector, fPresentation, fuelEconomyP);
		fbindingConnectorP.setProperty("line.shape", "line_right_angle");
		// Point2D.Double[] fbindingConnectorpnts = {
		// new Point2D.Double(fPresentation.getLocation().getX() +
		// fPresentation.getWidth(),
		// fPresentation.getLocation().getY() + fPresentation.getHeight() /
		// 2.0D),
		// new Point2D.Double(fuelEconomyP.getLocation().getX(),
		// fPresentation.getLocation().getY() + fPresentation.getHeight() /
		// 2.0D) };
		// fbindingConnectorP.setAllPoints(fbindingConnectorpnts);
		IBindingConnector qbindingConnector = sme.createBindingConnector(
				maxAccelerationAnalysisProp, qmaxAccelerationAnalysis,
				(IValueProperty) quarterMileTimeP.getModel(), null);
		ILinkPresentation qbindingConnectorP = parde.createLinkPresentation(
				qbindingConnector, qPresentation, quarterMileTimeP);
		qbindingConnectorP.setProperty("line.shape", "line_right_angle");
		Point2D.Double[] qbindingConnectorpnts = {
				new Point2D.Double(qPresentation.getLocation().getX()
						+ qPresentation.getWidth(), qPresentation.getLocation()
						.getY() + qPresentation.getHeight() / 2.0D),
				new Point2D.Double(qPresentation.getLocation().getX()
						+ qPresentation.getWidth() + 50.0D, qPresentation
						.getLocation().getY()
						+ qPresentation.getHeight()
						/ 2.0D),
				new Point2D.Double(qPresentation.getLocation().getX()
						+ qPresentation.getWidth() + 50.0D, quarterMileTimeP
						.getLocation().getY()
						+ quarterMileTimeP.getHeight()
						/ 2.0D),
				new Point2D.Double(quarterMileTimeP.getLocation().getX(),
						quarterMileTimeP.getLocation().getY()
								+ quarterMileTimeP.getHeight() / 2.0D) };
		qbindingConnectorP.setAllPoints(qbindingConnectorpnts);
		IBindingConnector zbindingConnector = sme.createBindingConnector(
				maxAccelerationAnalysisProp, zmaxAccelerationAnalysis,
				(IValueProperty) zero60TimeP.getModel(), null);
		ILinkPresentation zbindingConnectorP = parde.createLinkPresentation(
				zbindingConnector, zPresentation, zero60TimeP);
		zbindingConnectorP.setProperty("line.shape", "line_right_angle");
		Point2D.Double[] zbindingConnectorpnts = {
				new Point2D.Double(zPresentation.getLocation().getX()
						+ zPresentation.getWidth(), zPresentation.getLocation()
						.getY() + zPresentation.getHeight() / 2.0D),
				new Point2D.Double(zPresentation.getLocation().getX()
						+ zPresentation.getWidth() + 50.0D, zPresentation
						.getLocation().getY()
						+ zPresentation.getHeight()
						/ 2.0D),
				new Point2D.Double(zPresentation.getLocation().getX()
						+ zPresentation.getWidth() + 50.0D, zero60TimeP
						.getLocation().getY() + zero60TimeP.getHeight() / 2.0D),
				new Point2D.Double(zero60TimeP.getLocation().getX(),
						zero60TimeP.getLocation().getY()
								+ zero60TimeP.getHeight() / 2.0D) };
		zbindingConnectorP.setAllPoints(zbindingConnectorpnts);
		IBindingConnector vcbindingConnector = sme.createBindingConnector(
				capacityEquationProp, vc,
				(IValueProperty) cargoCapacityP.getModel(), null);
		ILinkPresentation vcbindingConnectorP = parde.createLinkPresentation(
				vcbindingConnector, vcPresentation, cargoCapacityP);
		vcbindingConnectorP.setProperty("line.shape", "line_right_angle");
		// Point2D.Double[] vcbindingConnectorpnts = {
		// new Point2D.Double(vcPresentation.getLocation().getX() +
		// vcPresentation.getWidth(),
		// vcPresentation.getLocation().getY() + vcPresentation.getHeight() /
		// 2.0D),
		// new Point2D.Double(cargoCapacityP.getLocation().getX(),
		// vcPresentation.getLocation().getY() + vcPresentation.getHeight() /
		// 2.0D) };
		// vcbindingConnectorP.setAllPoints(vcbindingConnectorpnts);
		IBindingConnector ucbindingConnector = sme.createBindingConnector(
				unitCostEquationProp, ucunitCostEquation,
				(IValueProperty) unitCostP.getModel(), null);
		ILinkPresentation ucbindingConnectorP = parde.createLinkPresentation(
				ucbindingConnector, ucPresentation, unitCostP);
		ucbindingConnectorP.setProperty("line.shape", "line_right_angle");
		// Point2D.Double[] ucbindingConnectorpnts = {
		// new Point2D.Double(ucPresentation.getLocation().getX() +
		// ucPresentation.getWidth(),
		// ucPresentation.getLocation().getY() + ucPresentation.getHeight() /
		// 2.0D),
		// new Point2D.Double(unitCostP.getLocation().getX(),
		// ucPresentation.getLocation().getY() + ucPresentation.getHeight() /
		// 2.0D) };
		// ucbindingConnectorP.setAllPoints(ucbindingConnectorpnts);
		IBindingConnector p1bindingConnector = sme.createBindingConnector(
				(IValueProperty) fuelEconomyP.getModel(), null,
				myObjectiveFunctionProp, p1myObjectiveFunction);
		ILinkPresentation p1bindingConnectorP = parde.createLinkPresentation(
				p1bindingConnector, fuelEconomyP, p1myObjectiveFunctionP);
		p1bindingConnectorP.setProperty("line.shape", "line_right_angle");
		// Point2D.Double[] p1bindingConnectorpnts = {
		// new Point2D.Double(fuelEconomyP.getLocation().getX() +
		// fuelEconomyP.getWidth(),
		// p1myObjectiveFunctionP.getLocation().getY() +
		// p1myObjectiveFunctionP.getHeight() / 2.0D),
		// new Point2D.Double(p1myObjectiveFunctionP.getLocation().getX(),
		// p1myObjectiveFunctionP.getLocation().getY() +
		// p1myObjectiveFunctionP.getHeight() / 2.0D) };
		// p1bindingConnectorP.setAllPoints(p1bindingConnectorpnts);
		IBindingConnector p2bindingConnector = sme.createBindingConnector(
				(IValueProperty) quarterMileTimeP.getModel(), null,
				myObjectiveFunctionProp, p2myObjectiveFunction);
		ILinkPresentation p2bindingConnectorP = parde.createLinkPresentation(
				p2bindingConnector, quarterMileTimeP, p2myObjectiveFunctionP);
		p2bindingConnectorP.setProperty("line.shape", "line_right_angle");
		Point2D.Double[] p2bindingConnectorpnts = {
				new Point2D.Double(quarterMileTimeP.getLocation().getX()
						+ quarterMileTimeP.getWidth(), quarterMileTimeP
						.getLocation().getY()
						+ quarterMileTimeP.getHeight()
						/ 2.0D),
				new Point2D.Double(quarterMileTimeP.getLocation().getX()
						+ quarterMileTimeP.getWidth() + 50.0D, quarterMileTimeP
						.getLocation().getY()
						+ quarterMileTimeP.getHeight()
						/ 2.0D),
				new Point2D.Double(quarterMileTimeP.getLocation().getX()
						+ quarterMileTimeP.getWidth() + 50.0D,
						p2myObjectiveFunctionP.getLocation().getY()
								+ p2myObjectiveFunctionP.getHeight() / 2.0D),
				new Point2D.Double(p2myObjectiveFunctionP.getLocation().getX(),
						p2myObjectiveFunctionP.getLocation().getY()
								+ p2myObjectiveFunctionP.getHeight() / 2.0D) };
		p2bindingConnectorP.setAllPoints(p2bindingConnectorpnts);
		IBindingConnector p3bindingConnector = sme.createBindingConnector(
				(IValueProperty) zero60TimeP.getModel(), null,
				myObjectiveFunctionProp, p3myObjectiveFunction);
		ILinkPresentation p3bindingConnectorP = parde.createLinkPresentation(
				p3bindingConnector, zero60TimeP, p3myObjectiveFunctionP);
		p3bindingConnectorP.setProperty("line.shape", "line_right_angle");
		Point2D.Double[] p3bindingConnectorpnts = {
				new Point2D.Double(zero60TimeP.getLocation().getX()
						+ zero60TimeP.getWidth(), zero60TimeP.getLocation()
						.getY() + zero60TimeP.getHeight() / 2.0D),
				new Point2D.Double(zero60TimeP.getLocation().getX()
						+ zero60TimeP.getWidth() + 80.0D, zero60TimeP
						.getLocation().getY() + zero60TimeP.getHeight() / 2.0D),
				new Point2D.Double(zero60TimeP.getLocation().getX()
						+ zero60TimeP.getWidth() + 80.0D,
						p3myObjectiveFunctionP.getLocation().getY()
								+ p3myObjectiveFunctionP.getHeight() / 2.0D),
				new Point2D.Double(p3myObjectiveFunctionP.getLocation().getX(),
						p3myObjectiveFunctionP.getLocation().getY()
								+ p3myObjectiveFunctionP.getHeight() / 2.0D) };
		p3bindingConnectorP.setAllPoints(p3bindingConnectorpnts);
		IBindingConnector p4bindingConnector = sme.createBindingConnector(
				(IValueProperty) cargoCapacityP.getModel(), null,
				myObjectiveFunctionProp, p4myObjectiveFunction);
		ILinkPresentation p4bindingConnectorP = parde.createLinkPresentation(
				p4bindingConnector, cargoCapacityP, p4myObjectiveFunctionP);
		p4bindingConnectorP.setProperty("line.shape", "line_right_angle");
		Point2D.Double[] p4bindingConnectorpnts = {
				new Point2D.Double(cargoCapacityP.getLocation().getX()
						+ cargoCapacityP.getWidth(), cargoCapacityP
						.getLocation().getY()
						+ cargoCapacityP.getHeight()
						/ 2.0D),
				new Point2D.Double(p4myObjectiveFunctionP.getLocation().getX()
						+ p4myObjectiveFunctionP.getWidth() / 2.0D,
						cargoCapacityP.getLocation().getY()
								+ cargoCapacityP.getHeight() / 2.0D),
				new Point2D.Double(p4myObjectiveFunctionP.getLocation().getX()
						+ p4myObjectiveFunctionP.getWidth() / 2.0D,
						p4myObjectiveFunctionP.getLocation().getY()
								+ p4myObjectiveFunctionP.getHeight() / 2.0D) };
		p4bindingConnectorP.setAllPoints(p4bindingConnectorpnts);
		IBindingConnector p5bindingConnector = sme.createBindingConnector(
				(IValueProperty) unitCostP.getModel(), null,
				myObjectiveFunctionProp, p5myObjectiveFunction);
		ILinkPresentation p5bindingConnectorP = parde.createLinkPresentation(
				p5bindingConnector, unitCostP, p5myObjectiveFunctionP);
		p5bindingConnectorP.setProperty("line.shape", "line_right_angle");
		Point2D.Double[] p5bindingConnectorpnts = {
				new Point2D.Double(unitCostP.getLocation().getX()
						+ unitCostP.getWidth(), unitCostP.getLocation().getY()
						+ unitCostP.getHeight() / 2.0D),
				new Point2D.Double(p5myObjectiveFunctionP.getLocation().getX()
						+ p5myObjectiveFunctionP.getWidth() / 2.0D, unitCostP
						.getLocation().getY() + unitCostP.getHeight() / 2.0D),
				new Point2D.Double(p5myObjectiveFunctionP.getLocation().getX()
						+ p5myObjectiveFunctionP.getWidth() / 2.0D,
						p5myObjectiveFunctionP.getLocation().getY()
								+ p5myObjectiveFunctionP.getHeight() / 2.0D) };
		p5bindingConnectorP.setAllPoints(p5bindingConnectorpnts);
		IBindingConnector cebindingConnector = sme.createBindingConnector(
				(IValueProperty) costEffectivenessP.getModel(), null,
				myObjectiveFunctionProp, cemyObjectiveFunction);
		ILinkPresentation cebindingConnectorP = parde.createLinkPresentation(
				cebindingConnector, costEffectivenessP, cemyObjectiveFunctionP);
		cebindingConnectorP.setProperty("line.shape", "line_right_angle");

		// "StraightLineVehicleDynamics"
		parde.setDiagram(parDgm2);
		// parametic diagram's ConstraintProperty presentation created by
		// diagram;
		INodePresentation powerEquationAsoEndP = ((INodePresentation) powerEquationAsoEnd
				.getPresentations()[0]);
		powerEquationAsoEndP.setLocation(new Point2D.Double(100, 100));
		powerEquationAsoEndP.setWidth(250);
		INodePresentation accellerationEquationAsoEndP = ((INodePresentation) accellerationEquationAsoEnd
				.getPresentations()[0]);
		accellerationEquationAsoEndP.setLocation(new Point2D.Double(400, 100));
		accellerationEquationAsoEndP.setWidth(250);
		INodePresentation velocityEquationAsoEndP = ((INodePresentation) velocityEquationAsoEnd
				.getPresentations()[0]);
		velocityEquationAsoEndP.setLocation(new Point2D.Double(400, 300));
		velocityEquationAsoEndP.setWidth(250);
		INodePresentation postionEquationAsoEndP = ((INodePresentation) postionEquationAsoEnd
				.getPresentations()[0]);
		postionEquationAsoEndP.setLocation(new Point2D.Double(400, 500));
		postionEquationAsoEndP.setWidth(250);

		INodePresentation tpNoteP = parde.createNote(
				"tp (hp) = wheel power - drag - friction", new Point2D.Double(
						100, 240));
		((IComment) tpNoteP.getModel()).addStereotype("rationale");
		parde.createNoteAnchor(tpNoteP, powerEquationAsoEndP);
		INodePresentation vNoteP = parde.createNote(
				"v(n+1) (mph) = v(n) + delta-v = v(n) + a*delta-t",
				new Point2D.Double(100, 340));
		((IComment) vNoteP.getModel()).addStereotype("rationale");
		parde.createNoteAnchor(vNoteP, velocityEquationAsoEndP);
		INodePresentation xNoteP = parde.createNote(
				"x(n+1) (ft) = x(n) + delta-x = x(n) + v*delta-t",
				new Point2D.Double(100, 440));
		((IComment) xNoteP.getModel()).addStereotype("rationale");
		parde.createNoteAnchor(xNoteP, postionEquationAsoEndP);
		INodePresentation aNoteP = parde.createNote("a(g) = F/m = P*t/m",
				new Point2D.Double(500, 0));
		((IComment) aNoteP.getModel()).addStereotype("rationale");
		parde.createNoteAnchor(aNoteP, accellerationEquationAsoEndP);

		INodePresentation twstraightLineVehicleDynamicsP = (INodePresentation) twstraightLineVehicleDynamics
				.getPresentations()[0];
		twstraightLineVehicleDynamicsP.setLocation(new Point2D.Double(0, 20));
		INodePresentation cfstraightLineVehicleDynamicsP = (INodePresentation) cfstraightLineVehicleDynamics
				.getPresentations()[0];
		cfstraightLineVehicleDynamicsP.setLocation(new Point2D.Double(0, 50));
		INodePresentation cdstraightLineVehicleDynamicsP = (INodePresentation) cdstraightLineVehicleDynamics
				.getPresentations()[0];
		cdstraightLineVehicleDynamicsP.setLocation(new Point2D.Double(0, 80));
		INodePresentation whlpowrstraightLineVehicleDynamicsP = (INodePresentation) whlpowrstraightLineVehicleDynamics
				.getPresentations()[0];
		whlpowrstraightLineVehicleDynamicsP.setLocation(new Point2D.Double(0,
				110));
		INodePresentation inclinestraightLineVehicleDynamicsP = (INodePresentation) inclinestraightLineVehicleDynamics
				.getPresentations()[0];
		inclinestraightLineVehicleDynamicsP.setLocation(new Point2D.Double(0,
				160));

		Rectangle2D frameRect = parDgm2.getBoundRect();
		INodePresentation accstraightLineVehicleDynamicsP = (INodePresentation) accstraightLineVehicleDynamics
				.getPresentations()[0];
		accstraightLineVehicleDynamicsP.setLocation(new Point2D.Double(
				frameRect.getX() + frameRect.getWidth(), 200));
		INodePresentation dtstraightLineVehicleDynamicsP = (INodePresentation) dtstraightLineVehicleDynamics
				.getPresentations()[0];
		dtstraightLineVehicleDynamicsP.setLocation(new Point2D.Double(frameRect
				.getX() + frameRect.getWidth(), 370));
		INodePresentation velstraightLineVehicleDynamicsP = (INodePresentation) velstraightLineVehicleDynamics
				.getPresentations()[0];
		velstraightLineVehicleDynamicsP.setLocation(new Point2D.Double(
				frameRect.getX() + frameRect.getWidth(), 420));
		INodePresentation xstraightLineVehicleDynamicsP = (INodePresentation) xstraightLineVehicleDynamics
				.getPresentations()[0];
		xstraightLineVehicleDynamicsP.setLocation(new Point2D.Double(frameRect
				.getX() + frameRect.getWidth(), 600));

		INodePresentation ipowerEquationP = (INodePresentation) ipowerEquation
				.getPresentations()[0];
		ipowerEquationP.setLocation(new Point2D.Double(powerEquationAsoEndP
				.getLocation().getX(), powerEquationAsoEndP.getLocation()
				.getY() + powerEquationAsoEndP.getHeight() / 2.0));
		INodePresentation whlpowrpowerEquationP = (INodePresentation) whlpowrpowerEquation
				.getPresentations()[0];
		whlpowrpowerEquationP.setLocation(new Point2D.Double(
				powerEquationAsoEndP.getLocation().getX() + 15.0,
				powerEquationAsoEndP.getLocation().getY() - 7.0D));
		INodePresentation cdpowerEquationP = (INodePresentation) cdpowerEquation
				.getPresentations()[0];
		cdpowerEquationP.setLocation(new Point2D.Double(powerEquationAsoEndP
				.getLocation().getX() + 100.0, powerEquationAsoEndP
				.getLocation().getY() - 7.0D));
		INodePresentation cfpowerEquationP = (INodePresentation) cfpowerEquation
				.getPresentations()[0];
		cfpowerEquationP.setLocation(new Point2D.Double(powerEquationAsoEndP
				.getLocation().getX() + 160.0, powerEquationAsoEndP
				.getLocation().getY() - 7.0D));
		INodePresentation twpowerEquationP = (INodePresentation) twpowerEquation
				.getPresentations()[0];
		twpowerEquationP.setLocation(new Point2D.Double(powerEquationAsoEndP
				.getLocation().getX() + 220.0, powerEquationAsoEndP
				.getLocation().getY() - 7.0D));
		INodePresentation tppowerEquationP = (INodePresentation) tppowerEquation
				.getPresentations()[0];
		tppowerEquationP.setLocation(new Point2D.Double(powerEquationAsoEndP
				.getLocation().getX() + powerEquationAsoEndP.getWidth(),
				powerEquationAsoEndP.getLocation().getY()
						+ powerEquationAsoEndP.getHeight() / 2.0));
		INodePresentation vpowerEquationP = (INodePresentation) vpowerEquation
				.getPresentations()[0];
		vpowerEquationP.setLocation(new Point2D.Double(powerEquationAsoEndP
				.getLocation().getX() + 130.0D, powerEquationAsoEndP
				.getLocation().getY() + powerEquationAsoEndP.getHeight()));

		INodePresentation tpaccellerationEquationP = (INodePresentation) tpaccellerationEquation
				.getPresentations()[0];
		tpaccellerationEquationP.setLocation(new Point2D.Double(
				accellerationEquationAsoEndP.getLocation().getX(),
				accellerationEquationAsoEndP.getLocation().getY()
						+ accellerationEquationAsoEndP.getHeight() / 2.0));
		INodePresentation twaccellerationEquationP = (INodePresentation) twaccellerationEquation
				.getPresentations()[0];
		twaccellerationEquationP.setLocation(new Point2D.Double(
				accellerationEquationAsoEndP.getLocation().getX()
						+ accellerationEquationAsoEndP.getWidth() / 2,
				accellerationEquationAsoEndP.getLocation().getY()));
		INodePresentation deltaaccellerationEquationP = (INodePresentation) deltaaccellerationEquation
				.getPresentations()[0];
		deltaaccellerationEquationP.setLocation(new Point2D.Double(
				accellerationEquationAsoEndP.getLocation().getX()
						+ accellerationEquationAsoEndP.getWidth(),
				accellerationEquationAsoEndP.getLocation().getY()
						+ accellerationEquationAsoEndP.getHeight() / 2.0));
		INodePresentation aaccellerationEquationP = (INodePresentation) aaccellerationEquation
				.getPresentations()[0];
		aaccellerationEquationP.setLocation(new Point2D.Double(
				accellerationEquationAsoEndP.getLocation().getX()
						+ accellerationEquationAsoEndP.getWidth() / 2,
				accellerationEquationAsoEndP.getLocation().getY()
						+ accellerationEquationAsoEndP.getHeight()));

		INodePresentation avelocityEquationP = (INodePresentation) avelocityEquation
				.getPresentations()[0];
		avelocityEquationP.setLocation(new Point2D.Double(
				velocityEquationAsoEndP.getLocation().getX()
						+ velocityEquationAsoEndP.getWidth() / 2.0D,
				velocityEquationAsoEndP.getLocation().getY()));
		INodePresentation deltavelocityEquationP = (INodePresentation) deltavelocityEquation
				.getPresentations()[0];
		deltavelocityEquationP.setLocation(new Point2D.Double(
				velocityEquationAsoEndP.getLocation().getX()
						+ velocityEquationAsoEndP.getWidth(),
				velocityEquationAsoEndP.getLocation().getY()
						+ velocityEquationAsoEndP.getHeight() / 2.0));
		INodePresentation vvelocityEquationP = (INodePresentation) vvelocityEquation
				.getPresentations()[0];
		vvelocityEquationP.setLocation(new Point2D.Double(
				velocityEquationAsoEndP.getLocation().getX()
						+ velocityEquationAsoEndP.getWidth() / 2,
				velocityEquationAsoEndP.getLocation().getY()
						+ velocityEquationAsoEndP.getHeight()));

		INodePresentation vpostionEquationP = (INodePresentation) vpostionEquation
				.getPresentations()[0];
		vpostionEquationP.setLocation(new Point2D.Double(postionEquationAsoEndP
				.getLocation().getX()
				+ postionEquationAsoEndP.getWidth()
				/ 2.0D, postionEquationAsoEndP.getLocation().getY()));
		INodePresentation deltapostionEquationP = (INodePresentation) deltapostionEquation
				.getPresentations()[0];
		deltapostionEquationP.setLocation(new Point2D.Double(
				postionEquationAsoEndP.getLocation().getX()
						+ postionEquationAsoEndP.getWidth(),
				postionEquationAsoEndP.getLocation().getY()
						+ postionEquationAsoEndP.getHeight() / 2.0));
		INodePresentation xpostionEquationP = (INodePresentation) xpostionEquation
				.getPresentations()[0];
		xpostionEquationP.setLocation(new Point2D.Double(postionEquationAsoEndP
				.getLocation().getX() + postionEquationAsoEndP.getWidth() / 2,
				postionEquationAsoEndP.getLocation().getY()
						+ postionEquationAsoEndP.getHeight()));

		ILinkPresentation twFrameConnectorP = parde.createLinkPresentation(
				twFrameConnector, twstraightLineVehicleDynamicsP,
				twpowerEquationP);
		twFrameConnectorP.setProperty("line.shape", "line_right_angle");
		Point2D.Double[] twFrameConnectorpnts = {
				new Point2D.Double(twstraightLineVehicleDynamicsP.getLocation()
						.getX() + twstraightLineVehicleDynamicsP.getWidth(),
						twstraightLineVehicleDynamicsP.getLocation().getY()
								+ twstraightLineVehicleDynamicsP.getHeight()),
				new Point2D.Double(twpowerEquationP.getLocation().getX()
						+ twpowerEquationP.getWidth() / 2.0D,
						twstraightLineVehicleDynamicsP.getLocation().getY()
								+ twstraightLineVehicleDynamicsP.getHeight()),
				new Point2D.Double(twpowerEquationP.getLocation().getX()
						+ twpowerEquationP.getWidth() / 2.0D, twpowerEquationP
						.getLocation().getY()) };
		twFrameConnectorP.setAllPoints(twFrameConnectorpnts);
		ILinkPresentation cfFrameConnectorP = parde.createLinkPresentation(
				cfFrameConnector, cfstraightLineVehicleDynamicsP,
				cfpowerEquationP);
		cfFrameConnectorP.setProperty("line.shape", "line_right_angle");
		Point2D.Double[] cfFrameConnectorpnts = {
				new Point2D.Double(cfstraightLineVehicleDynamicsP.getLocation()
						.getX() + cfstraightLineVehicleDynamicsP.getWidth(),
						cfstraightLineVehicleDynamicsP.getLocation().getY()
								+ cfstraightLineVehicleDynamicsP.getHeight()
								/ 2.0D),
				new Point2D.Double(cfpowerEquationP.getLocation().getX()
						+ cfpowerEquationP.getWidth() / 2.0D,
						cfstraightLineVehicleDynamicsP.getLocation().getY()
								+ cfstraightLineVehicleDynamicsP.getHeight()
								/ 2.0D),
				new Point2D.Double(cfpowerEquationP.getLocation().getX()
						+ cfpowerEquationP.getWidth() / 2.0D, cfpowerEquationP
						.getLocation().getY()) };
		cfFrameConnectorP.setAllPoints(cfFrameConnectorpnts);
		ILinkPresentation cdFrameConnectorP = parde.createLinkPresentation(
				cdFrameConnector, cdstraightLineVehicleDynamicsP,
				cdpowerEquationP);
		cdFrameConnectorP.setProperty("line.shape", "line_right_angle");
		Point2D.Double[] cdFrameConnectorpnts = {
				new Point2D.Double(cdstraightLineVehicleDynamicsP.getLocation()
						.getX() + cdstraightLineVehicleDynamicsP.getWidth(),
						cdstraightLineVehicleDynamicsP.getLocation().getY()
								+ cdstraightLineVehicleDynamicsP.getHeight()
								/ 2.0D),
				new Point2D.Double(cdpowerEquationP.getLocation().getX()
						+ cdpowerEquationP.getWidth() / 2.0D,
						cdstraightLineVehicleDynamicsP.getLocation().getY()
								+ cdstraightLineVehicleDynamicsP.getHeight()
								/ 2.0D),
				new Point2D.Double(cdpowerEquationP.getLocation().getX()
						+ cdpowerEquationP.getWidth() / 2.0D, cdpowerEquationP
						.getLocation().getY()) };
		cdFrameConnectorP.setAllPoints(cdFrameConnectorpnts);
		ILinkPresentation whlpowrFrameConnectorP = parde
				.createLinkPresentation(whlpowrFrameConnector,
						whlpowrstraightLineVehicleDynamicsP,
						whlpowrpowerEquationP);
		whlpowrFrameConnectorP.setProperty("line.shape", "line_right_angle");
		Point2D.Double[] whlpowrFrameConnectorpnts = {
				new Point2D.Double(whlpowrstraightLineVehicleDynamicsP
						.getLocation().getX()
						+ whlpowrstraightLineVehicleDynamicsP.getWidth(),
						whlpowrstraightLineVehicleDynamicsP.getLocation()
								.getY()
								+ whlpowrstraightLineVehicleDynamicsP
										.getHeight() / 2.0D),
				new Point2D.Double(whlpowrstraightLineVehicleDynamicsP
						.getLocation().getX()
						+ whlpowrstraightLineVehicleDynamicsP.getWidth()
						+ 50.0D, whlpowrstraightLineVehicleDynamicsP
						.getLocation().getY()
						+ whlpowrstraightLineVehicleDynamicsP.getHeight()
						/ 2.0D),
				new Point2D.Double(whlpowrstraightLineVehicleDynamicsP
						.getLocation().getX()
						+ whlpowrstraightLineVehicleDynamicsP.getWidth()
						+ 50.0D,
						whlpowrpowerEquationP.getLocation().getY() - 3.0D),
				new Point2D.Double(whlpowrpowerEquationP.getLocation().getX()
						+ whlpowrpowerEquationP.getWidth() / 2.0D,
						whlpowrpowerEquationP.getLocation().getY() - 3.0D),
				new Point2D.Double(whlpowrpowerEquationP.getLocation().getX()
						+ whlpowrpowerEquationP.getWidth() / 2.0D,
						whlpowrpowerEquationP.getLocation().getY()) };
		whlpowrFrameConnectorP.setAllPoints(whlpowrFrameConnectorpnts);
		ILinkPresentation inclineFrameConnectorP = parde
				.createLinkPresentation(inclineFrameConnector,
						inclinestraightLineVehicleDynamicsP, ipowerEquationP);
		inclineFrameConnectorP.setProperty("line.shape", "line_right_angle");
		ILinkPresentation acclineFrameConnectorP = parde
				.createLinkPresentation(acclineFrameConnector,
						accstraightLineVehicleDynamicsP,
						aaccellerationEquationP);
		acclineFrameConnectorP.setProperty("line.shape", "line_right_angle");
		Point2D.Double[] acclineFrameConnectorpnts = {
				new Point2D.Double(accstraightLineVehicleDynamicsP
						.getLocation().getX(), accstraightLineVehicleDynamicsP
						.getLocation().getY()
						+ accstraightLineVehicleDynamicsP.getHeight() / 2.0D),
				new Point2D.Double(aaccellerationEquationP.getLocation().getX()
						+ aaccellerationEquationP.getWidth(),
						accstraightLineVehicleDynamicsP.getLocation().getY()
								+ accstraightLineVehicleDynamicsP.getHeight()
								/ 2.0D),
				new Point2D.Double(aaccellerationEquationP.getLocation().getX()
						+ aaccellerationEquationP.getWidth(),
						aaccellerationEquationP.getLocation().getY()
								+ aaccellerationEquationP.getHeight()) };
		acclineFrameConnectorP.setAllPoints(acclineFrameConnectorpnts);
		ILinkPresentation dtFrameConnectorP = parde.createLinkPresentation(
				dtFrameConnector, dtstraightLineVehicleDynamicsP,
				deltavelocityEquationP);
		dtFrameConnectorP.setProperty("line.shape", "line_right_angle");
		ILinkPresentation velFrameConnectorP = parde.createLinkPresentation(
				velFrameConnector, velstraightLineVehicleDynamicsP,
				vvelocityEquationP);
		velFrameConnectorP.setProperty("line.shape", "line_right_angle");
		Point2D.Double[] velFrameConnectorpnts = {
				new Point2D.Double(velstraightLineVehicleDynamicsP
						.getLocation().getX()
						+ velstraightLineVehicleDynamicsP.getWidth() / 2.0D,
						velstraightLineVehicleDynamicsP.getLocation().getY()
								+ velstraightLineVehicleDynamicsP.getHeight()),
				new Point2D.Double(velstraightLineVehicleDynamicsP
						.getLocation().getX()
						+ velstraightLineVehicleDynamicsP.getWidth() / 2.0D,
						velstraightLineVehicleDynamicsP.getLocation().getY()
								+ velstraightLineVehicleDynamicsP.getHeight()
								+ 20.0D),
				new Point2D.Double(vvelocityEquationP.getLocation().getX()
						+ vvelocityEquationP.getWidth(),
						velstraightLineVehicleDynamicsP.getLocation().getY()
								+ velstraightLineVehicleDynamicsP.getHeight()
								+ 20.0D),
				new Point2D.Double(vvelocityEquationP.getLocation().getX()
						+ vvelocityEquationP.getWidth(), vvelocityEquationP
						.getLocation().getY() + vvelocityEquationP.getHeight()) };
		velFrameConnectorP.setAllPoints(velFrameConnectorpnts);
		ILinkPresentation xFrameConnectorP = parde.createLinkPresentation(
				xFrameConnector, xstraightLineVehicleDynamicsP,
				xpostionEquationP);
		xFrameConnectorP.setProperty("line.shape", "line_right_angle");
		Point2D.Double[] xFrameConnectorpnts = {
				new Point2D.Double(xstraightLineVehicleDynamicsP.getLocation()
						.getX()
						+ xstraightLineVehicleDynamicsP.getWidth()
						/ 2.0D, xstraightLineVehicleDynamicsP.getLocation()
						.getY() + xstraightLineVehicleDynamicsP.getHeight()),
				new Point2D.Double(xstraightLineVehicleDynamicsP.getLocation()
						.getX()
						+ xstraightLineVehicleDynamicsP.getWidth()
						/ 2.0D, xstraightLineVehicleDynamicsP.getLocation()
						.getY()
						+ xstraightLineVehicleDynamicsP.getHeight()
						+ 12.0D),
				new Point2D.Double(xpostionEquationP.getLocation().getX()
						+ xpostionEquationP.getWidth() / 2.0D,
						xstraightLineVehicleDynamicsP.getLocation().getY()
								+ xstraightLineVehicleDynamicsP.getHeight()
								+ 12.0D),
				new Point2D.Double(xpostionEquationP.getLocation().getX()
						+ xpostionEquationP.getWidth() / 2.0D,
						xpostionEquationP.getLocation().getY()
								+ xpostionEquationP.getHeight()) };
		xFrameConnectorP.setAllPoints(xFrameConnectorpnts);

		ILinkPresentation v_v_ConnectorP = parde.createLinkPresentation(
				v_v_Connector, vpowerEquationP, vvelocityEquationP);
		v_v_ConnectorP.setProperty("line.shape", "line_right_angle");
		Point2D.Double[] v_v_Connectorpnts = {
				new Point2D.Double(vpowerEquationP.getLocation().getX()
						+ vpowerEquationP.getWidth() / 2.0D, vpowerEquationP
						.getLocation().getY() + vpowerEquationP.getHeight()),
				new Point2D.Double(vpowerEquationP.getLocation().getX()
						+ vpowerEquationP.getWidth() / 2.0D, vpowerEquationP
						.getLocation().getY()
						+ vpowerEquationP.getHeight()
						+ 10.0D),
				new Point2D.Double(vpowerEquationP.getLocation().getX()
						+ vpowerEquationP.getWidth() / 2.0D + 100.0D,
						vpowerEquationP.getLocation().getY()
								+ vpowerEquationP.getHeight() + 10.0D),
				new Point2D.Double(vpowerEquationP.getLocation().getX()
						+ vpowerEquationP.getWidth() / 2.0D + 100.0D,
						vvelocityEquationP.getLocation().getY()
								+ vvelocityEquationP.getHeight() + 10.0D),
				new Point2D.Double(vvelocityEquationP.getLocation().getX(),
						vvelocityEquationP.getLocation().getY()
								+ vvelocityEquationP.getHeight() + 10.0D),
				new Point2D.Double(vvelocityEquationP.getLocation().getX(),
						vvelocityEquationP.getLocation().getY()
								+ vvelocityEquationP.getHeight()) };
		v_v_ConnectorP.setAllPoints(v_v_Connectorpnts);

		ILinkPresentation tp_tp_ConnectorP = parde.createLinkPresentation(
				tp_tp_Connector, tppowerEquationP, tpaccellerationEquationP);
		tp_tp_ConnectorP.setProperty("line.shape", "line_right_angle");
		ILinkPresentation tw2FrameConnectorP = parde.createLinkPresentation(
				tw2FrameConnector, twstraightLineVehicleDynamicsP,
				twaccellerationEquationP);
		tw2FrameConnectorP.setProperty("line.shape", "line_right_angle");
		Point2D.Double[] tw2FrameConnectorpnts = {
				new Point2D.Double(twstraightLineVehicleDynamicsP.getLocation()
						.getX() + twstraightLineVehicleDynamicsP.getWidth(),
						twstraightLineVehicleDynamicsP.getLocation().getY()
								+ twstraightLineVehicleDynamicsP.getHeight()
								/ 2.0D),
				new Point2D.Double(twstraightLineVehicleDynamicsP.getLocation()
						.getX()
						+ twstraightLineVehicleDynamicsP.getWidth()
						+ 350.0D, twstraightLineVehicleDynamicsP.getLocation()
						.getY()
						+ twstraightLineVehicleDynamicsP.getHeight()
						/ 2.0D),
				new Point2D.Double(twstraightLineVehicleDynamicsP.getLocation()
						.getX()
						+ twstraightLineVehicleDynamicsP.getWidth()
						+ 350.0D,
						twaccellerationEquationP.getLocation().getY() - 10.0D),
				new Point2D.Double(twaccellerationEquationP.getLocation()
						.getX() + twaccellerationEquationP.getWidth() / 2.0D,
						twaccellerationEquationP.getLocation().getY() - 10.0D),
				new Point2D.Double(twaccellerationEquationP.getLocation()
						.getX() + twaccellerationEquationP.getWidth() / 2.0D,
						twaccellerationEquationP.getLocation().getY()) };
		tw2FrameConnectorP.setAllPoints(tw2FrameConnectorpnts);
		ILinkPresentation delta_delta_ConnectorP = parde
				.createLinkPresentation(delta_delta_Connector,
						deltaaccellerationEquationP, deltapostionEquationP);
		delta_delta_ConnectorP.setProperty("line.shape", "line_right_angle");
		Point2D.Double[] delta_delta_Connectorpnts = {
				new Point2D.Double(deltaaccellerationEquationP.getLocation()
						.getX() + deltaaccellerationEquationP.getWidth(),
						deltaaccellerationEquationP.getLocation().getY()
								+ deltaaccellerationEquationP.getHeight()
								/ 2.0D),
				new Point2D.Double(deltaaccellerationEquationP.getLocation()
						.getX()
						+ deltaaccellerationEquationP.getWidth()
						+ 50.0D, deltaaccellerationEquationP.getLocation()
						.getY()
						+ deltaaccellerationEquationP.getHeight()
						/ 2.0D),
				new Point2D.Double(deltaaccellerationEquationP.getLocation()
						.getX()
						+ deltaaccellerationEquationP.getWidth()
						+ 50.0D, deltapostionEquationP.getLocation().getY()
						+ deltapostionEquationP.getHeight() / 2.0D),
				new Point2D.Double(deltapostionEquationP.getLocation().getX()
						+ deltapostionEquationP.getWidth(),
						deltapostionEquationP.getLocation().getY()
								+ deltapostionEquationP.getHeight() / 2.0D) };
		delta_delta_ConnectorP.setAllPoints(delta_delta_Connectorpnts);

		ILinkPresentation a_a_ConnectorP = parde.createLinkPresentation(
				a_a_Connector, aaccellerationEquationP, avelocityEquationP);
		a_a_ConnectorP.setProperty("line.shape", "line_right_angle");
		ILinkPresentation v_v_Connector2P = parde.createLinkPresentation(
				v_v_Connector2, vvelocityEquationP, vpostionEquationP);
		v_v_Connector2P.setProperty("line.shape", "line_right_angle");

		INodePresentation framePresentation = getFramePresentation(parDgm2);
		framePresentation.setWidth(750);

	}

	private INodePresentation getFramePresentation(IParametricDiagram dgm)
			throws InvalidUsingException {
		for (IPresentation p : dgm.getPresentations()) {
			if (p.getType().equals("ParentConstraintBlock")) {
				return (INodePresentation) p;
			}
		}
		return null;
	}
}
