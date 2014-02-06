import java.io.InputStream;

import com.change_vision.jude.api.inf.AstahAPI;
import com.change_vision.jude.api.inf.exception.InvalidUsingException;
import com.change_vision.jude.api.inf.model.IActivityNode;
import com.change_vision.jude.api.inf.model.IAssociation;
import com.change_vision.jude.api.inf.model.IAttribute;
import com.change_vision.jude.api.inf.model.IBlock;
import com.change_vision.jude.api.inf.model.IClass;
import com.change_vision.jude.api.inf.model.ICombinedFragment;
import com.change_vision.jude.api.inf.model.IConstraint;
import com.change_vision.jude.api.inf.model.IConstraintBlock;
import com.change_vision.jude.api.inf.model.IConstraintParameter;
import com.change_vision.jude.api.inf.model.IDependency;
import com.change_vision.jude.api.inf.model.IDiagram;
import com.change_vision.jude.api.inf.model.IExtend;
import com.change_vision.jude.api.inf.model.IFlow;
import com.change_vision.jude.api.inf.model.IFlowProperty;
import com.change_vision.jude.api.inf.model.IGate;
import com.change_vision.jude.api.inf.model.IInclude;
import com.change_vision.jude.api.inf.model.IInteraction;
import com.change_vision.jude.api.inf.model.IInteractionFragment;
import com.change_vision.jude.api.inf.model.IInteractionOperand;
import com.change_vision.jude.api.inf.model.IInteractionUse;
import com.change_vision.jude.api.inf.model.IInterfaceBlock;
import com.change_vision.jude.api.inf.model.ILifeline;
import com.change_vision.jude.api.inf.model.ILifelineLink;
import com.change_vision.jude.api.inf.model.IMessage;
import com.change_vision.jude.api.inf.model.INamedElement;
import com.change_vision.jude.api.inf.model.IOperation;
import com.change_vision.jude.api.inf.model.IQuantityKind;
import com.change_vision.jude.api.inf.model.IRequirement;
import com.change_vision.jude.api.inf.model.IStateInvariant;
import com.change_vision.jude.api.inf.model.ITestCase;
import com.change_vision.jude.api.inf.model.ITransition;
import com.change_vision.jude.api.inf.model.IUnit;
import com.change_vision.jude.api.inf.model.IUseCase;
import com.change_vision.jude.api.inf.model.IValueAttribute;
import com.change_vision.jude.api.inf.model.IValueType;
import com.change_vision.jude.api.inf.model.IValueTypeProperty;
import com.change_vision.jude.api.inf.model.IVertex;
import com.change_vision.jude.api.inf.presentation.ILinkPresentation;
import com.change_vision.jude.api.inf.presentation.INodePresentation;
import com.change_vision.jude.api.inf.presentation.IPresentation;
import com.change_vision.jude.api.inf.project.ModelFinder;
import com.change_vision.jude.api.inf.project.ProjectAccessor;

/**
 * Sample source codes for getting all kinds of models in Astah Sysml.
 * How to find models by find method is shown.
 */
public class ReadingSampleSysmlModels {

	private static ProjectAccessor projectAccessor;

	public static void main(String[] args) throws Exception {
		AstahAPI api = AstahAPI.getAstahAPI();
		projectAccessor = api.getProjectAccessor();
		try {
			showInConsle(0, "[Start ReadingSampleSysmlModels]");

			openSampleModel("Sample.asml");

			showModels();

			showDiagrams();

		} catch (Exception e) {
			throw e;
		} finally {
			//Close project
			projectAccessor.close();

			showInConsle(0, "[End ReadingSampleSysmlModels]");
		}
	}

	private static void showInConsle(int indexSize, String context) {
		StringBuilder str = new StringBuilder();
		for (int i = 0; i < indexSize; i++) {
			str.append("\t");
		}
		str.append(context);
		System.out.println(str.toString());
	}

	private static void showModels() throws Exception, InvalidUsingException {

		showInConsle(1, "[1.Start to show models.]");
        
		// Show how to get model informations of requirement diagram such as requirement and testcase 
		showRequirementModels();

		// Show how to get model informations of block definition diagram such as block and value 
		showBlockModels();

		// Show how to get model informations of usecase diagram such as usecase and actor 
		showUseCaseModels();

		// Show how to get model informations of activity diagram such as initial state and activity 
		showActivityModels();

		// Show how to get model informations of state machine diagram such as state and transition 
		showStateMachineModels();

		// Show how to get model informations of sequence diagram such as lifeline and message 
		showSequenceModels();

		showInConsle(1, "[1.End to show models.]");
	}

	private static void showRequirementModels() throws InvalidUsingException,
			Exception {
		INamedElement[] reqs = findRequirements();
		showInConsle(2, "[Start to show Requirement(Name, ID, Text)]");
		for (INamedElement element : reqs) {
			printRequirement((IRequirement) element);
		}
		showInConsle(2, "[End to show Requirement(Name, ID, Text)]");

		INamedElement[] tests = findTestCases();
		showInConsle(2, "[Start to show TestCase(Namet)]");
		for (INamedElement element : tests) {
			printTestCase((ITestCase) element);
		}
		showInConsle(2, "[End to show TestCase(Name)]");

		INamedElement[] deriveReqtDependencies = findDeriveReqtDependencies();
		showInConsle(2, "[Start to show deriveReqtDependency]");
		for (INamedElement element : deriveReqtDependencies) {
			printDeriveReqtDependency((IDependency) element);
		}
		showInConsle(2, "[End to show deriveReqtDependency]");

		INamedElement[] copyDependencies = findCopyDependencies();
		showInConsle(2, "[Start to show copyDependency]");
		for (INamedElement element : copyDependencies) {
			printCopyDependency((IDependency) element);
		}
		showInConsle(2, "[End to show copyDependency]");

		INamedElement[] satisfyDependencies = findSatisfyDependencies();
		showInConsle(2, "[Start to show satisfyDependency]");
		for (INamedElement element : satisfyDependencies) {
			printSatisfyDependency((IDependency) element);
		}
		showInConsle(2, "[End to show satisfyDependency]");

		INamedElement[] verifyDependencies = findVerifyDependencies();
		showInConsle(2, "[Start to show verifyDependency]");
		for (INamedElement element : verifyDependencies) {
			printVerifyDependency((IDependency) element);
		}
		showInConsle(2, "[End to show verifyDependency]");

		INamedElement[] refineDependencies = findRefineDependencies();
		showInConsle(2, "[Start to show refineDependency]");
		for (INamedElement element : refineDependencies) {
			printRefineDependency((IDependency) element);
		}
		showInConsle(2, "[End to show refineDependency]");

		INamedElement[] traceDependencies = findTraceDependencies();
		showInConsle(2, "[Start to show traceDependency]");
		for (INamedElement element : traceDependencies) {
			printTraceDependency((IDependency) element);
		}
		showInConsle(2, "[End to show copyDependency]");

		INamedElement[] allocateDependencies = findAllocateDependencies();
		showInConsle(2, "[Start to show allocateDependency]");
		for (INamedElement element : allocateDependencies) {
			printAllocateDependency((IDependency) element);
		}
		showInConsle(2, "[End to show allocateDependency]");
	}

	private static void showBlockModels() throws Exception,
			InvalidUsingException {
		INamedElement[] blocks = findBlocks();
		showInConsle(2, "[Start to show Block(Name, Child)]");
		for (INamedElement element : blocks) {
			printBlock((IBlock) element);
		}
		showInConsle(2, "[End to show Block(Name, Child)]");

		INamedElement[] interfaceblocks = findInterfaceBlocks();
		showInConsle(2, "[Start to show InterfaceBlock(Name, Child)]");
		for (INamedElement element : interfaceblocks) {
			printInterfaceBlock((IInterfaceBlock) element);
		}
		showInConsle(2, "[End to show InterfaceBlock(Name, Child)]");

		INamedElement[] constraintBlocks = findConstraintBlocks();
		showInConsle(2, "[Start to show ConstraintBlock(Name, Child)]");
		for (INamedElement element : constraintBlocks) {
			printConstraintBlock((IConstraintBlock) element);
		}
		showInConsle(2, "[End to show ConstraintBlock(Name, Child)]");

		INamedElement[] valueTypes = findValueTypes();
		showInConsle(2, "[Start to show ValueType(Name, Child)]");
		for (INamedElement element : valueTypes) {
			printValueType((IValueType) element);
		}
		showInConsle(2, "[End to show ValueType(Name, Child)]");

		INamedElement[] units = findUnits();
		showInConsle(2, "[Start to show Unit(Name, Symbol, QuantityKind)]");
		for (INamedElement element : units) {
			printUnit((IUnit) element);
		}
		showInConsle(2, "[End to show Unit(Name, Symbol, QuantityKind)]");

		INamedElement[] quantityKinds = findQuantityKinds();
		showInConsle(2, "[Start to show QuantityKind(Name)]");
		for (INamedElement element : quantityKinds) {
			printQuantityKind((IQuantityKind) element);
		}
		showInConsle(2, "[End to show QuantityKind(Name)]");

		INamedElement[] interfaces = findInterfaces();
		showInConsle(2, "[Start to show Interface(Name, Child)]");
		for (INamedElement element : interfaces) {
			printInterface((IClass) element);
		}
		showInConsle(2, "[End to show Interface(Name, Child)]");

		INamedElement[] associations = findAssociations();
		showInConsle(2, "[Start to show Association]");
		for (INamedElement element : associations) {
			printAssociation((IAssociation) element);
		}
		showInConsle(2, "[End to show Association]");

		INamedElement[] dependencies = findDependencies();
		showInConsle(2, "[Start to show Dependency]");
		for (INamedElement element : dependencies) {
			printDependency((IDependency) element);
		}
		showInConsle(2, "[End to show Dependency]");

		INamedElement[] flowDependencies = findFlowDependencies();
		showInConsle(2, "[Start to show flowDependency]");
		for (INamedElement element : flowDependencies) {
			printFlowDependency((IDependency) element);
		}
		showInConsle(2, "[End to show flowDependency]");
	}

	private static void showUseCaseModels() throws Exception,
			InvalidUsingException {
		INamedElement[] actors = findActors();
		showInConsle(2, "[Start to show Actor(Name)]");
		for (INamedElement element : actors) {
			printActor((IClass) element);
		}
		showInConsle(2, "[End to show Actor(Name)]");

		INamedElement[] useCases = findUseCases();
		showInConsle(2, "[Start to show UseCase(Name)]");
		for (INamedElement element : useCases) {
			printUseCase((IUseCase) element);
		}
		showInConsle(2, "[End to show UseCase(Name)]");

		INamedElement[] includes = findIncludes();
		showInConsle(2, "[Start to show Include(Name)]");
		for (INamedElement element : includes) {
			printInclude((IInclude) element);
		}
		showInConsle(2, "[End to show Include(Name)]");

		INamedElement[] extnds = findExtends();
		showInConsle(2, "[Start to show Extend(Name)]");
		for (INamedElement element : extnds) {
			printExtend((IExtend) element);
		}
		showInConsle(2, "[End to show Extend(Name)]");
	}

	private static void showActivityModels() throws Exception,
			InvalidUsingException {
		INamedElement[] activityNodes = findActivityNodes();
		showInConsle(2, "[Start to show ActivityNode(Name)]");
		for (INamedElement element : activityNodes) {
			printActivityNode((IActivityNode) element);
		}
		showInConsle(2, "[End to show ActivityNode(Name)]");
	}

	private static void showStateMachineModels() throws Exception,
			InvalidUsingException {
		INamedElement[] vertexs = findVertexs();
		showInConsle(2, "[Start to show Vertex(Name)]");
		for (INamedElement element : vertexs) {
			printVertex((IVertex) element);
		}
		showInConsle(2, "[End to show Vertex(Name)]");
	}

	private static void showSequenceModels() throws Exception,
			InvalidUsingException {
		INamedElement[] lifelines = findLifelines();
		showInConsle(2, "[Start to show Lifeline(Name)]");
		for (INamedElement element : lifelines) {
			printLifeline((ILifeline) element);
		}
		showInConsle(2, "[End to show Lifeline(Name)]");

		INamedElement[] messages = findMessages();
		showInConsle(2, "[Start to show Message(Name)]");
		for (INamedElement element : messages) {
			printMessage((IMessage) element);
		}
		showInConsle(2, "[End to show Message(Name)]");

		INamedElement[] interactionFragments = findInteractionFragments();
		showInConsle(2, "[Start to show InteractionFragment(Name)]");
		for (INamedElement element : interactionFragments) {
			printInteractionFragment((IInteractionFragment) element);
		}
		showInConsle(2, "[End to show InteractionFragment(Name)]");

		INamedElement[] interactions = findInteractions();
		showInConsle(2, "[Start to show Interaction(Name)]");
		for (INamedElement element : interactions) {
			printInteraction((IInteraction) element);
		}
		showInConsle(2, "[End to show Interaction(Name)]");
	}

	private static void showDiagrams() throws Exception, InvalidUsingException {

		showInConsle(1, "[2.Start to show diagrams.]");

		INamedElement[] sgms = findDiagrams();
		for (INamedElement element : sgms) {
			showInConsle(2, "[Start to show "
					+ element.getClass().getInterfaces()[0].getSimpleName()
					+ "(" + ((IDiagram) element).getFullName("::") + ")]");
			printDiagramElements((IDiagram) element);
			showInConsle(2, "[End to show "
					+ element.getClass().getInterfaces()[0].getSimpleName()
					+ "(" + ((IDiagram) element).getFullName("::") + ")]");
		}

		showInConsle(1, "[2.End to show diagrams.]");
	}

	/**
	 * open sample model
	 * 
	 * @throws Exception
	 */
	private static void openSampleModel(String path) throws Exception {
		InputStream astahFileStream = ReadingSampleSysmlModels.class
				.getResourceAsStream(path);           
		// Open a project.
        // Please don't forget to save and close the project
		projectAccessor.open(astahFileStream);
	}

	/**
	 * get Diagrams
	 * 
	 * @return IDiagram[]
	 * @throws Exception
	 */
	private static INamedElement[] findDiagrams() throws Exception {
		// There are three methods to search from ProjectAccessor.
        // public INamedElement[] findElements(Class elementKind, String name) throws ProjectNotFoundException;
		// public abstract INamedElement[] findElements(Class elementKind) throws ProjectNotFoundException;
		// public INamedElement[] findElements(ModelFinder picker) throws ProjectNotFoundException;
		INamedElement[] foundElements = projectAccessor
				.findElements(new ModelFinder() {
					public boolean isTarget(INamedElement namedElement) {
						return namedElement instanceof IDiagram;
					}
				});
		return foundElements;
	}

	/**
	 * get Requirements
	 * 
	 * @return IRequirement[]
	 * @throws Exception
	 */
	private static INamedElement[] findRequirements() throws Exception {
		// There are three methods to search from ProjectAccessor.
        // public INamedElement[] findElements(Class elementKind, String name) throws ProjectNotFoundException;
		// public abstract INamedElement[] findElements(Class elementKind) throws ProjectNotFoundException;
		// public INamedElement[] findElements(ModelFinder picker) throws ProjectNotFoundException;
		INamedElement[] foundElements = projectAccessor
				.findElements(new ModelFinder() {
					public boolean isTarget(INamedElement namedElement) {
						return namedElement instanceof IRequirement;
					}
				});
		return foundElements;
	}

	/**
	 * get TestCases
	 * 
	 * @return ITestCase[]
	 * @throws Exception
	 */
	private static INamedElement[] findTestCases() throws Exception {
		// There are three methods to search from ProjectAccessor.
        // public INamedElement[] findElements(Class elementKind, String name) throws ProjectNotFoundException;
		// public abstract INamedElement[] findElements(Class elementKind) throws ProjectNotFoundException;
		// public INamedElement[] findElements(ModelFinder picker) throws ProjectNotFoundException;
		INamedElement[] foundElements = projectAccessor
				.findElements(new ModelFinder() {
					public boolean isTarget(INamedElement namedElement) {
						return namedElement instanceof ITestCase;
					}
				});
		return foundElements;
	}

	/**
	 * get DeriveReqtDependencies
	 * 
	 * @return IDependency[]
	 * @throws Exception
	 */
	private static INamedElement[] findDeriveReqtDependencies()
			throws Exception {
		// There are three methods to search from ProjectAccessor.
        // public INamedElement[] findElements(Class elementKind, String name) throws ProjectNotFoundException;
		// public abstract INamedElement[] findElements(Class elementKind) throws ProjectNotFoundException;
		// public INamedElement[] findElements(ModelFinder picker) throws ProjectNotFoundException;
		INamedElement[] foundElements = projectAccessor
				.findElements(new ModelFinder() {
					public boolean isTarget(INamedElement namedElement) {
						return namedElement instanceof IDependency
								&& isSpecifiedStereotype(namedElement,
										"deriveReqt");
					}
				});
		return foundElements;
	}

	/**
	 * get CopyDependencies
	 * 
	 * @return IDependency[]
	 * @throws Exception
	 */
	private static INamedElement[] findCopyDependencies() throws Exception {
		// There are three methods to search from ProjectAccessor.
        // public INamedElement[] findElements(Class elementKind, String name) throws ProjectNotFoundException;
		// public abstract INamedElement[] findElements(Class elementKind) throws ProjectNotFoundException;
		// public INamedElement[] findElements(ModelFinder picker) throws ProjectNotFoundException;
		INamedElement[] foundElements = projectAccessor
				.findElements(new ModelFinder() {
					public boolean isTarget(INamedElement namedElement) {
						return namedElement instanceof IDependency
								&& isSpecifiedStereotype(namedElement, "copy");
					}
				});
		return foundElements;
	}

	/**
	 * get SatisfyDependencies
	 * 
	 * @return IDependency[]
	 * @throws Exception
	 */
	private static INamedElement[] findSatisfyDependencies() throws Exception {
		// There are three methods to search from ProjectAccessor.
        // public INamedElement[] findElements(Class elementKind, String name) throws ProjectNotFoundException;
		// public abstract INamedElement[] findElements(Class elementKind) throws ProjectNotFoundException;
		// public INamedElement[] findElements(ModelFinder picker) throws ProjectNotFoundException;
		INamedElement[] foundElements = projectAccessor
				.findElements(new ModelFinder() {
					public boolean isTarget(INamedElement namedElement) {
						return namedElement instanceof IDependency
								&& isSpecifiedStereotype(namedElement,
										"satisfy");
					}
				});
		return foundElements;
	}

	/**
	 * get VerifyDependencies
	 * 
	 * @return IDependency[]
	 * @throws Exception
	 */
	private static INamedElement[] findVerifyDependencies() throws Exception {
		// There are three methods to search from ProjectAccessor.
        // public INamedElement[] findElements(Class elementKind, String name) throws ProjectNotFoundException;
		// public abstract INamedElement[] findElements(Class elementKind) throws ProjectNotFoundException;
		// public INamedElement[] findElements(ModelFinder picker) throws ProjectNotFoundException;
		INamedElement[] foundElements = projectAccessor
				.findElements(new ModelFinder() {
					public boolean isTarget(INamedElement namedElement) {
						return namedElement instanceof IDependency
								&& isSpecifiedStereotype(namedElement, "verify");
					}
				});
		return foundElements;
	}

	/**
	 * get RefineDependencies
	 * 
	 * @return IDependency[]
	 * @throws Exception
	 */
	private static INamedElement[] findRefineDependencies() throws Exception {
		// There are three methods to search from ProjectAccessor.
        // public INamedElement[] findElements(Class elementKind, String name) throws ProjectNotFoundException;
		// public abstract INamedElement[] findElements(Class elementKind) throws ProjectNotFoundException;
		// public INamedElement[] findElements(ModelFinder picker) throws ProjectNotFoundException;
		INamedElement[] foundElements = projectAccessor
				.findElements(new ModelFinder() {
					public boolean isTarget(INamedElement namedElement) {
						return namedElement instanceof IDependency
								&& isSpecifiedStereotype(namedElement, "refine");
					}
				});
		return foundElements;
	}

	/**
	 * get TraceDependencies
	 * 
	 * @return IDependency[]
	 * @throws Exception
	 */
	private static INamedElement[] findTraceDependencies() throws Exception {
		// There are three methods to search from ProjectAccessor.
        // public INamedElement[] findElements(Class elementKind, String name) throws ProjectNotFoundException;
		// public abstract INamedElement[] findElements(Class elementKind) throws ProjectNotFoundException;
		// public INamedElement[] findElements(ModelFinder picker) throws ProjectNotFoundException;
		INamedElement[] foundElements = projectAccessor
				.findElements(new ModelFinder() {
					public boolean isTarget(INamedElement namedElement) {
						return namedElement instanceof IDependency
								&& isSpecifiedStereotype(namedElement, "trace");
					}
				});
		return foundElements;
	}

	/**
	 * get AllocateDependencies
	 * 
	 * @return IDependency[]
	 * @throws Exception
	 */
	private static INamedElement[] findAllocateDependencies() throws Exception {
		// There are three methods to search from ProjectAccessor.
        // public INamedElement[] findElements(Class elementKind, String name) throws ProjectNotFoundException;
		// public abstract INamedElement[] findElements(Class elementKind) throws ProjectNotFoundException;
		// public INamedElement[] findElements(ModelFinder picker) throws ProjectNotFoundException;
		INamedElement[] foundElements = projectAccessor
				.findElements(new ModelFinder() {
					public boolean isTarget(INamedElement namedElement) {
						return namedElement instanceof IDependency
								&& isSpecifiedStereotype(namedElement,
										"allocate");
					}
				});
		return foundElements;
	}

	/**
	 * get Blocks
	 * 
	 * @return IBlock[]
	 * @throws Exception
	 */
	private static INamedElement[] findBlocks() throws Exception {
		// There are three methods to search from ProjectAccessor.
        // public INamedElement[] findElements(Class elementKind, String name) throws ProjectNotFoundException;
		// public abstract INamedElement[] findElements(Class elementKind) throws ProjectNotFoundException;
		// public INamedElement[] findElements(ModelFinder picker) throws ProjectNotFoundException;
		INamedElement[] foundElements = projectAccessor
				.findElements(new ModelFinder() {
					public boolean isTarget(INamedElement namedElement) {
						return namedElement instanceof IBlock;
					}
				});
		return foundElements;
	}

	/**
	 * get InterfaceBlocks
	 * 
	 * @return IInterfaceBlock[]
	 * @throws Exception
	 */
	private static INamedElement[] findInterfaceBlocks() throws Exception {
		// There are three methods to search from ProjectAccessor.
        // public INamedElement[] findElements(Class elementKind, String name) throws ProjectNotFoundException;
		// public abstract INamedElement[] findElements(Class elementKind) throws ProjectNotFoundException;
		// public INamedElement[] findElements(ModelFinder picker) throws ProjectNotFoundException;
		INamedElement[] foundElements = projectAccessor
				.findElements(new ModelFinder() {
					public boolean isTarget(INamedElement namedElement) {
						return namedElement instanceof IInterfaceBlock;
					}
				});
		return foundElements;
	}

	/**
	 * get ConstraintBlocks
	 * 
	 * @return IConstraintBlock[]
	 * @throws Exception
	 */
	private static INamedElement[] findConstraintBlocks() throws Exception {
		// There are three methods to search from ProjectAccessor.
        // public INamedElement[] findElements(Class elementKind, String name) throws ProjectNotFoundException;
		// public abstract INamedElement[] findElements(Class elementKind) throws ProjectNotFoundException;
		// public INamedElement[] findElements(ModelFinder picker) throws ProjectNotFoundException;
		INamedElement[] foundElements = projectAccessor
				.findElements(new ModelFinder() {
					public boolean isTarget(INamedElement namedElement) {
						return namedElement instanceof IConstraintBlock;
					}
				});
		return foundElements;
	}

	/**
	 * get ValueTypes
	 * 
	 * @return IValueType[]
	 * @throws Exception
	 */
	private static INamedElement[] findValueTypes() throws Exception {
		// There are three methods to search from ProjectAccessor.
        // public INamedElement[] findElements(Class elementKind, String name) throws ProjectNotFoundException;
		// public abstract INamedElement[] findElements(Class elementKind) throws ProjectNotFoundException;
		// public INamedElement[] findElements(ModelFinder picker) throws ProjectNotFoundException;
		INamedElement[] foundElements = projectAccessor
				.findElements(new ModelFinder() {
					public boolean isTarget(INamedElement namedElement) {
						return namedElement instanceof IValueType;
					}
				});
		return foundElements;
	}

	/**
	 * get Units
	 * 
	 * @return IUnit[]
	 * @throws Exception
	 */
	private static INamedElement[] findUnits() throws Exception {
		// There are three methods to search from ProjectAccessor.
        // public INamedElement[] findElements(Class elementKind, String name) throws ProjectNotFoundException;
		// public abstract INamedElement[] findElements(Class elementKind) throws ProjectNotFoundException;
		// public INamedElement[] findElements(ModelFinder picker) throws ProjectNotFoundException;
		INamedElement[] foundElements = projectAccessor
				.findElements(new ModelFinder() {
					public boolean isTarget(INamedElement namedElement) {
						return namedElement instanceof IUnit;
					}
				});
		return foundElements;
	}

	/**
	 * get QuantityKinds
	 * 
	 * @return IQuantityKind[]
	 * @throws Exception
	 */
	private static INamedElement[] findQuantityKinds() throws Exception {
		// There are three methods to search from ProjectAccessor.
        // public INamedElement[] findElements(Class elementKind, String name) throws ProjectNotFoundException;
		// public abstract INamedElement[] findElements(Class elementKind) throws ProjectNotFoundException;
		// public INamedElement[] findElements(ModelFinder picker) throws ProjectNotFoundException;
		INamedElement[] foundElements = projectAccessor
				.findElements(new ModelFinder() {
					public boolean isTarget(INamedElement namedElement) {
						return namedElement instanceof IQuantityKind;
					}
				});
		return foundElements;
	}

	/**
	 * get Interfaces
	 * 
	 * @return IClass[]
	 * @throws Exception
	 */
	private static INamedElement[] findInterfaces() throws Exception {
		// There are three methods to search from ProjectAccessor.
        // public INamedElement[] findElements(Class elementKind, String name) throws ProjectNotFoundException;
		// public abstract INamedElement[] findElements(Class elementKind) throws ProjectNotFoundException;
		// public INamedElement[] findElements(ModelFinder picker) throws ProjectNotFoundException;
		INamedElement[] foundElements = projectAccessor
				.findElements(new ModelFinder() {
					public boolean isTarget(INamedElement namedElement) {
						return namedElement instanceof IClass
								&& isSpecifiedStereotype(namedElement,
										"interface");
					}
				});
		return foundElements;
	}

	/**
	 * get Associations
	 * 
	 * @return IAssociation[]
	 * @throws Exception
	 */
	private static INamedElement[] findAssociations() throws Exception {
		// There are three methods to search from ProjectAccessor.
        // public INamedElement[] findElements(Class elementKind, String name) throws ProjectNotFoundException;
		// public abstract INamedElement[] findElements(Class elementKind) throws ProjectNotFoundException;
		// public INamedElement[] findElements(ModelFinder picker) throws ProjectNotFoundException;
		INamedElement[] foundElements = projectAccessor
				.findElements(new ModelFinder() {
					public boolean isTarget(INamedElement namedElement) {
						return namedElement instanceof IAssociation;
					}
				});
		return foundElements;
	}

	/**
	 * get Dependencies
	 * 
	 * @return IDependency[]
	 * @throws Exception
	 */
	private static INamedElement[] findDependencies() throws Exception {
		// There are three methods to search from ProjectAccessor.
        // public INamedElement[] findElements(Class elementKind, String name) throws ProjectNotFoundException;
		// public abstract INamedElement[] findElements(Class elementKind) throws ProjectNotFoundException;
		// public INamedElement[] findElements(ModelFinder picker) throws ProjectNotFoundException;
		INamedElement[] foundElements = projectAccessor
				.findElements(new ModelFinder() {
					public boolean isTarget(INamedElement namedElement) {
						return namedElement instanceof IDependency
								&& !isSpecifiedStereotype(namedElement,
										"deriveReqt")
								&& !isSpecifiedStereotype(namedElement, "copy")
								&& !isSpecifiedStereotype(namedElement,
										"satisfy")
								&& !isSpecifiedStereotype(namedElement,
										"verify")
								&& !isSpecifiedStereotype(namedElement,
										"refine")
								&& !isSpecifiedStereotype(namedElement, "trace")
								&& !isSpecifiedStereotype(namedElement,
										"allocate")
								&& !isSpecifiedStereotype(namedElement, "flow");
					}
				});
		return foundElements;
	}

	/**
	 * get FlowDependencies
	 * 
	 * @return IDependency[]
	 * @throws Exception
	 */
	private static INamedElement[] findFlowDependencies() throws Exception {
		// There are three methods to search from ProjectAccessor.
        // public INamedElement[] findElements(Class elementKind, String name) throws ProjectNotFoundException;
		// public abstract INamedElement[] findElements(Class elementKind) throws ProjectNotFoundException;
		// public INamedElement[] findElements(ModelFinder picker) throws ProjectNotFoundException;
		INamedElement[] foundElements = projectAccessor
				.findElements(new ModelFinder() {
					public boolean isTarget(INamedElement namedElement) {
						return namedElement instanceof IDependency
								&& isSpecifiedStereotype(namedElement, "flow");
					}
				});
		return foundElements;
	}

	/**
	 * get Actors
	 * 
	 * @return IClass[]
	 * @throws Exception
	 */
	private static INamedElement[] findActors() throws Exception {
		// There are three methods to search from ProjectAccessor.
        // public INamedElement[] findElements(Class elementKind, String name) throws ProjectNotFoundException;
		// public abstract INamedElement[] findElements(Class elementKind) throws ProjectNotFoundException;
		// public INamedElement[] findElements(ModelFinder picker) throws ProjectNotFoundException;
		INamedElement[] foundElements = projectAccessor
				.findElements(new ModelFinder() {
					public boolean isTarget(INamedElement namedElement) {
						return namedElement instanceof IClass
								&& isSpecifiedStereotype(namedElement, "actor");
					}
				});
		return foundElements;
	}

	/**
	 * get UseCases
	 * 
	 * @return IUseCase[]
	 * @throws Exception
	 */
	private static INamedElement[] findUseCases() throws Exception {
		// There are three methods to search from ProjectAccessor.
        // public INamedElement[] findElements(Class elementKind, String name) throws ProjectNotFoundException;
		// public abstract INamedElement[] findElements(Class elementKind) throws ProjectNotFoundException;
		// public INamedElement[] findElements(ModelFinder picker) throws ProjectNotFoundException;
		INamedElement[] foundElements = projectAccessor
				.findElements(new ModelFinder() {
					public boolean isTarget(INamedElement namedElement) {
						return namedElement instanceof IUseCase;
					}
				});
		return foundElements;
	}

	/**
	 * get Includes
	 * 
	 * @return IInclude[]
	 * @throws Exception
	 */
	private static INamedElement[] findIncludes() throws Exception {
		// There are three methods to search from ProjectAccessor.
        // public INamedElement[] findElements(Class elementKind, String name) throws ProjectNotFoundException;
		// public abstract INamedElement[] findElements(Class elementKind) throws ProjectNotFoundException;
		// public INamedElement[] findElements(ModelFinder picker) throws ProjectNotFoundException;
		INamedElement[] foundElements = projectAccessor
				.findElements(new ModelFinder() {
					public boolean isTarget(INamedElement namedElement) {
						return namedElement instanceof IInclude;
					}
				});
		return foundElements;
	}

	/**
	 * get Extends
	 * 
	 * @return IExtend[]
	 * @throws Exception
	 */
	private static INamedElement[] findExtends() throws Exception {
		// There are three methods to search from ProjectAccessor.
        // public INamedElement[] findElements(Class elementKind, String name) throws ProjectNotFoundException;
		// public abstract INamedElement[] findElements(Class elementKind) throws ProjectNotFoundException;
		// public INamedElement[] findElements(ModelFinder picker) throws ProjectNotFoundException;
		INamedElement[] foundElements = projectAccessor
				.findElements(new ModelFinder() {
					public boolean isTarget(INamedElement namedElement) {
						return namedElement instanceof IExtend;
					}
				});
		return foundElements;
	}

	/**
	 * get ActivityNodes
	 * 
	 * @return IActivityNode[]
	 * @throws Exception
	 */
	private static INamedElement[] findActivityNodes() throws Exception {
		// There are three methods to search from ProjectAccessor.
        // public INamedElement[] findElements(Class elementKind, String name) throws ProjectNotFoundException;
		// public abstract INamedElement[] findElements(Class elementKind) throws ProjectNotFoundException;
		// public INamedElement[] findElements(ModelFinder picker) throws ProjectNotFoundException;
		INamedElement[] foundElements = projectAccessor
				.findElements(new ModelFinder() {
					public boolean isTarget(INamedElement namedElement) {
						return namedElement instanceof IActivityNode;
					}
				});
		return foundElements;
	}

	/**
	 * get Vertexs
	 * 
	 * @return IVertex[]
	 * @throws Exception
	 */
	private static INamedElement[] findVertexs() throws Exception {
		// There are three methods to search from ProjectAccessor.
        // public INamedElement[] findElements(Class elementKind, String name) throws ProjectNotFoundException;
		// public abstract INamedElement[] findElements(Class elementKind) throws ProjectNotFoundException;
		// public INamedElement[] findElements(ModelFinder picker) throws ProjectNotFoundException;
		INamedElement[] foundElements = projectAccessor
				.findElements(new ModelFinder() {
					public boolean isTarget(INamedElement namedElement) {
						return namedElement instanceof IVertex;
					}
				});
		return foundElements;
	}

	/**
	 * get Lifelines
	 * 
	 * @return ILifeline[]
	 * @throws Exception
	 */
	private static INamedElement[] findLifelines() throws Exception {
		// There are three methods to search from ProjectAccessor.
        // public INamedElement[] findElements(Class elementKind, String name) throws ProjectNotFoundException;
		// public abstract INamedElement[] findElements(Class elementKind) throws ProjectNotFoundException;
		// public INamedElement[] findElements(ModelFinder picker) throws ProjectNotFoundException;
		INamedElement[] foundElements = projectAccessor
				.findElements(new ModelFinder() {
					public boolean isTarget(INamedElement namedElement) {
						return namedElement instanceof ILifeline;
					}
				});
		return foundElements;
	}

	/**
	 * get Messages
	 * 
	 * @return ILifeline[]
	 * @throws ExceIMessageption
	 */
	private static INamedElement[] findMessages() throws Exception {
		// There are three methods to search from ProjectAccessor.
        // public INamedElement[] findElements(Class elementKind, String name) throws ProjectNotFoundException;
		// public abstract INamedElement[] findElements(Class elementKind) throws ProjectNotFoundException;
		// public INamedElement[] findElements(ModelFinder picker) throws ProjectNotFoundException;
		INamedElement[] foundElements = projectAccessor
				.findElements(new ModelFinder() {
					public boolean isTarget(INamedElement namedElement) {
						return namedElement instanceof IMessage;
					}
				});
		return foundElements;
	}

	/**
	 * get InteractionFragments
	 * 
	 * @return IInteractionFragment[]
	 * @throws ExceIMessageption
	 */
	private static INamedElement[] findInteractionFragments() throws Exception {
		// There are three methods to search from ProjectAccessor.
        // public INamedElement[] findElements(Class elementKind, String name) throws ProjectNotFoundException;
		// public abstract INamedElement[] findElements(Class elementKind) throws ProjectNotFoundException;
		// public INamedElement[] findElements(ModelFinder picker) throws ProjectNotFoundException;
		INamedElement[] foundElements = projectAccessor
				.findElements(new ModelFinder() {
					public boolean isTarget(INamedElement namedElement) {
						return namedElement instanceof IInteractionFragment;
					}
				});
		return foundElements;
	}

	/**
	 * get Interactions
	 * 
	 * @return IInteraction[]
	 * @throws ExceIMessageption
	 */
	private static INamedElement[] findInteractions() throws Exception {
		// There are three methods to search from ProjectAccessor.
        // public INamedElement[] findElements(Class elementKind, String name) throws ProjectNotFoundException;
		// public abstract INamedElement[] findElements(Class elementKind) throws ProjectNotFoundException;
		// public INamedElement[] findElements(ModelFinder picker) throws ProjectNotFoundException;
		INamedElement[] foundElements = projectAccessor
				.findElements(new ModelFinder() {
					public boolean isTarget(INamedElement namedElement) {
						return namedElement instanceof IInteraction;
					}
				});
		return foundElements;
	}

	public static boolean isSpecifiedStereotype(INamedElement model,
			String stereotype) {
		if (model != null && (stereotype != null && !"".equals(stereotype))) {
			String[] stereotypes = model.getStereotypes();
			if (stereotypes == null)
				return false;
			for (String stereotypeName : stereotypes) {
				if (stereotype.equals(stereotypeName)) {
					return true;
				}
			}
		}
		return false;
	}

	private static void printRequirement(IRequirement req)
			throws InvalidUsingException {
		showInConsle(
				3,
				"[RequirementName]=" + req.getFullName("::") + " [Id]="
						+ req.getRequirementID() + " [Text]="
						+ req.getRequirementText());
	}

	private static void printTestCase(ITestCase tc)
			throws InvalidUsingException {
		showInConsle(3, "[TestCaseName]=" + tc.getFullName("::"));
	}

	private static void printDeriveReqtDependency(IDependency idep)
			throws InvalidUsingException {
		showInConsle(3, "[DeriveReqtDependencyName]=" + idep.getName()
				+ " [Supplier]=" + idep.getSupplier().getFullName("::")
				+ " [Client]=" + idep.getClient().getFullName("::"));
	}

	private static void printCopyDependency(IDependency idep)
			throws InvalidUsingException {
		showInConsle(3, "[CopyDependencyName]=" + idep.getName()
				+ " [Supplier]=" + idep.getSupplier().getFullName("::")
				+ " [Client]=" + idep.getClient().getFullName("::"));
	}

	private static void printSatisfyDependency(IDependency idep)
			throws InvalidUsingException {
		showInConsle(3, "[SatisfyDependencyName]=" + idep.getName()
				+ " [Supplier]=" + idep.getSupplier().getFullName("::")
				+ " [Client]=" + idep.getClient().getFullName("::"));
	}

	private static void printVerifyDependency(IDependency idep)
			throws InvalidUsingException {
		showInConsle(3, "[VerifyDependencyName]=" + idep.getName()
				+ " [Supplier]=" + idep.getSupplier().getFullName("::")
				+ " [Client]=" + idep.getClient().getFullName("::"));
	}

	private static void printRefineDependency(IDependency idep)
			throws InvalidUsingException {
		showInConsle(3, "[RefineDependencyName]=" + idep.getName()
				+ " [Supplier]=" + idep.getSupplier().getFullName("::")
				+ " [Client]=" + idep.getClient().getFullName("::"));
	}

	private static void printTraceDependency(IDependency idep)
			throws InvalidUsingException {
		showInConsle(3, "[TraceDependencyName]=" + idep.getName()
				+ " [Supplier]=" + idep.getSupplier().getFullName("::")
				+ " [Client]=" + idep.getClient().getFullName("::"));
	}

	private static void printAllocateDependency(IDependency idep)
			throws InvalidUsingException {
		showInConsle(3, "[AllocateDependencyName]=" + idep.getName()
				+ " [Supplier]=" + idep.getSupplier().getFullName("::")
				+ " [Client]=" + idep.getClient().getFullName("::"));
	}

	private static void printBlock(IBlock block) throws InvalidUsingException {
		showInConsle(3, "[BlockName]=" + block.getFullName("::"));
		for (IConstraint co : block.getConstraints()) {
			showInConsle(4, "[ConstraintName]=" + co.getName());
		}
		for (IOperation op : block.getOperations()) {
			showInConsle(4, "[OperationName]=" + op.getName()
					+ " [ReturnType]=" + op.getReturnType());
		}
		for (IFlowProperty fp : block.getFlowProperties()) {
			showInConsle(4, "[FlowPropertyName]=" + fp.getName() + " [Type]="
					+ fp.getType().getFullName("::"));
		}
		for (IAttribute part : block.getParts()) {
			showInConsle(4, "[PartName]="
					+ part.getName()
					+ " [Target]="
					+ part.getAssociation().getMemberEnds()[1].getType()
							.getFullName("::"));
		}
		for (IAttribute ref : block.getReferences()) {
			showInConsle(4, "[ReferenceName]="
					+ ref.getName()
					+ " [Target]="
					+ ref.getAssociation().getMemberEnds()[1].getType()
							.getFullName("::"));
		}
		for (IValueAttribute va : block.getValueAttributes()) {
			showInConsle(4, "[ValueName]=" + va.getName() + " [Type]="
					+ va.getType().getFullName("::"));
		}
	}

	private static void printInterfaceBlock(IInterfaceBlock block)
			throws InvalidUsingException {
		showInConsle(3, "[InterfaceBlockName]=" + block.getFullName("::"));
		for (IConstraint co : block.getConstraints()) {
			showInConsle(4, "[ConstraintName]=" + co.getName());
		}
		for (IOperation op : block.getOperations()) {
			showInConsle(4, "[OperationName]=" + op.getName()
					+ " [ReturnType]=" + op.getReturnType().getFullName("::"));
		}
		for (IFlowProperty fp : block.getFlowProperties()) {
			showInConsle(4, "[FlowPropertyName]=" + fp.getName() + " [Type]="
					+ fp.getType().getFullName("::"));
		}
		for (IAttribute ref : block.getReferences()) {
			showInConsle(4, "[ReferenceName]="
					+ ref.getName()
					+ " [Target]="
					+ ref.getAssociation().getMemberEnds()[1].getType()
							.getFullName("::"));
		}
	}

	private static void printConstraintBlock(IConstraintBlock block)
			throws InvalidUsingException {
		showInConsle(3, "[ConstraintBlockName]=" + block.getFullName("::"));
		for (IConstraint co : block.getConstraints()) {
			showInConsle(4, "[ConstraintName]=" + co.getName());
		}
		for (IConstraintParameter cp : block.getConstraintParameters()) {
			showInConsle(4, "[ParameterName]=" + cp.getName() + " [Type]="
					+ cp.getType().getFullName("::"));
		}
	}

	private static void printValueType(IValueType vt)
			throws InvalidUsingException {
		showInConsle(3, "[ValueTypeName]=" + vt.getFullName("::"));
		for (IOperation op : vt.getOperations()) {
			showInConsle(4, "[OperationName]=" + op.getName()
					+ " [ReturnType]=" + op.getReturnType().getFullName("::"));
		}
		for (IValueTypeProperty vp : vt.getProperties()) {
			showInConsle(4, "[PropertyName]=" + vp.getName() + " [Type]="
					+ vp.getType().getFullName("::"));
		}
	}

	private static void printUnit(IUnit ut) throws InvalidUsingException {
		showInConsle(3, "[UnitName]=" + ut.getFullName("::") + " [Symbol]="
				+ ut.getSymbol() + " [QuantityKind]="
				+ ut.getQuantityKind().getFullName("::"));
	}

	private static void printQuantityKind(IQuantityKind qk)
			throws InvalidUsingException {
		showInConsle(3, "[QuantityKindName]=" + qk.getFullName("::"));
	}

	private static void printInterface(IClass itf) throws InvalidUsingException {
		showInConsle(3, "[InterfaceName]=" + itf.getFullName("::"));
		for (IOperation op : itf.getOperations()) {
			showInConsle(4, "[OperationName]=" + op.getName()
					+ " [ReturnType]=" + op.getReturnType().getFullName("::"));
		}
		for (IAttribute at : itf.getAttributes()) {
			showInConsle(
					4,
					"[AttributName]=" + at.getName() + " [Type]="
							+ at.getType());
		}
	}

	private static void printAssociation(IAssociation ias)
			throws InvalidUsingException {
		showInConsle(3, "[AssociationName]=" + ias.getName());
		for (IAttribute ae : ias.getMemberEnds()) {
			showInConsle(4, "[AssociationEndName]=" + ae.getName()
					+ " [AssociationEndType]=" + ae.getType().getFullName("::"));
		}
	}

	private static void printDependency(IDependency idep)
			throws InvalidUsingException {
		showInConsle(3, "[DependencyName]=" + idep.getName() + " [Supplier]="
				+ idep.getSupplier().getFullName("::") + " [Client]="
				+ idep.getClient().getFullName("::"));
	}

	private static void printFlowDependency(IDependency idep)
			throws InvalidUsingException {
		showInConsle(3, "[FlowDependencyName]=" + idep.getName()
				+ " [Supplier]=" + idep.getSupplier().getFullName("::")
				+ " [Client]=" + idep.getClient().getFullName("::"));
	}

	private static void printActor(IClass ac) throws InvalidUsingException {
		showInConsle(3, "[ActorName]=" + ac.getFullName("::"));
	}

	private static void printUseCase(IUseCase uc) throws InvalidUsingException {
		showInConsle(3, "[UseCaseName]=" + uc.getFullName("::"));
	}

	private static void printInclude(IInclude in) throws InvalidUsingException {
		showInConsle(
				3,
				"[IncludeName]=" + in.getName() + " [Addition]="
						+ in.getAddition() + " [IncludingCase]="
						+ in.getIncludingCase());
	}

	private static void printExtend(IExtend ex) throws InvalidUsingException {
		showInConsle(
				3,
				"[ExtendName]=" + ex.getName() + " [Extension]="
						+ ex.getExtension() + " [ExtendedCase]="
						+ ex.getExtendedCase());
	}

	private static void printActivityNode(IActivityNode an)
			throws InvalidUsingException {
		showInConsle(3, "[ActivityNodeName]=" + an.getName());
		for (IFlow flow : an.getIncomings()) {
			showInConsle(4, "[Incoming FlowName]=" + flow.getName());
		}
		for (IFlow flow : an.getOutgoings()) {
			showInConsle(4, "[Outgoing FlowName]=" + flow.getName());
		}
	}

	private static void printVertex(IVertex vt) throws InvalidUsingException {
		showInConsle(3, "[VertexName]=" + vt.getName());
		for (ITransition tr : vt.getIncomings()) {
			showInConsle(4, "[Incoming TransitionName]=" + tr.getName());
		}
		for (ITransition tr : vt.getOutgoings()) {
			showInConsle(4, "[Outgoing TransitionName]=" + tr.getName());
		}
	}

	private static void printLifeline(ILifeline ll)
			throws InvalidUsingException {
		showInConsle(3, "[LifelineName]=" + ll.getName() + "[Base]="
				+ ll.getBase().getFullName("::"));
	}

	private static void printMessage(IMessage ms) throws InvalidUsingException {
		showInConsle(
				3,
				"[LifelineName]=" + ms.getName() + " [Source]="
						+ ms.getSource() + " [Target]=" + ms.getTarget()
						+ " [Operation]=" + ms.getOperation().getFullName("::")
						+ " [ReturnValueVariable]="
						+ ms.getReturnValueVariable() + " [Guard]="
						+ ms.getGuard() + " [Index]=" + ms.getIndex()
						+ " [Activator]=" + ms.getActivator()
						+ " [Predecessor]=" + ms.getPredecessor()
						+ " [Successor]=" + ms.getSuccessor());
	}

	private static void printInteractionFragment(IInteractionFragment itf)
			throws InvalidUsingException {
		if (itf instanceof IInteractionUse) {
			showInConsle(
					3,
					"[InteractionUseName]=" + itf.getName()
							+ " [SequenceDiagram]="
							+ ((IInteractionUse) itf).getSequenceDiagram()
							+ " [Argument]="
							+ ((IInteractionUse) itf).getArgument());
			for (IGate gt : ((IInteractionUse) itf).getGates()) {
				showInConsle(4, "[Gate Message]=" + gt.getMessage());
			}
			for (ILifeline lf : ((IInteractionUse) itf).getLifelines()) {
				showInConsle(4, "[LifelineName]=" + lf.getName() + " [Base]="
						+ lf.getBase());
				for (INamedElement ll : lf.getFragments()) {
					showInConsle(5, "[InteractionFragmentName]=" + ll.getName());
				}
				for (ILifelineLink ll : lf.getLifelineLinks()) {
					showInConsle(
							5,
							"[LifelineLinkName]=" + ll.getName() + " [Source]="
									+ ll.getSource() + " [Target]="
									+ ll.getTarget());
				}
			}
			for (ILifeline lf : ((IInteractionUse) itf).getContainers()) {
				showInConsle(4, "[LifelineName]=" + lf.getName() + " [Base]="
						+ lf.getBase());
				for (INamedElement ll : lf.getFragments()) {
					showInConsle(5, "[InteractionFragmentName]=" + ll.getName());
				}
				for (ILifelineLink ll : lf.getLifelineLinks()) {
					showInConsle(
							5,
							"[LifelineLinkName]=" + ll.getName() + " [Source]="
									+ ll.getSource() + " [Target]="
									+ ll.getTarget());
				}
			}
		} else if (itf instanceof ICombinedFragment) {
			showInConsle(3, "[CombinedFragmentName]=" + itf.getName());
			for (IInteractionOperand io : ((ICombinedFragment) itf)
					.getInteractionOperands()) {
				showInConsle(4, "[InteractionOperandName]=" + io.getName()
						+ " [Guard]=" + io.getGuard());
				for (ILifeline lf : io.getLifelines()) {
					showInConsle(5, "[LifelineName]=" + lf.getName()
							+ " [Base]=" + lf.getBase());
					for (INamedElement ll : lf.getFragments()) {
						showInConsle(6,
								"[InteractionFragmentName]=" + ll.getName());
					}
					for (ILifelineLink ll : lf.getLifelineLinks()) {
						showInConsle(6, "[LifelineLinkName]=" + ll.getName()
								+ " [Source]=" + ll.getSource() + " [Target]="
								+ ll.getTarget());
					}
				}
			}
		} else {
			showInConsle(3,
					"[StateInvariantName]=" + ((IStateInvariant) itf).getName());
			for (ILifeline lf : ((IStateInvariant) itf).getContainers()) {
				showInConsle(4, "[LifelineName]=" + lf.getName() + " [Base]="
						+ lf.getBase());
				for (INamedElement ll : lf.getFragments()) {
					showInConsle(5, "[InteractionFragmentName]=" + ll.getName());
				}
				for (ILifelineLink ll : lf.getLifelineLinks()) {
					showInConsle(
							5,
							"[LifelineLinkName]=" + ll.getName() + " [Source]="
									+ ll.getSource() + " [Target]="
									+ ll.getTarget());
				}
			}
		}
	}

	private static void printInteraction(IInteraction it)
			throws InvalidUsingException {
		showInConsle(3, "[Interaction Argument]=" + it.getArgument());
		for (ILifeline lf : it.getLifelines()) {
			showInConsle(4, "[LifelineName]=" + lf.getName() + " [Base]="
					+ lf.getBase().getFullName("::"));
			for (INamedElement ll : lf.getFragments()) {
				showInConsle(5, "[InteractionFragmentName]=" + ll.getName());
			}
			for (ILifelineLink ll : lf.getLifelineLinks()) {
				showInConsle(
						5,
						"[LifelineLinkName]=" + ll.getName() + " [Source]="
								+ ll.getSource() + " [Target]="
								+ ll.getTarget());
			}
		}
		for (IGate gt : it.getGates()) {
			showInConsle(4, "[Gate Message]=" + gt.getMessage());
		}
		for (IMessage ms : it.getMessages()) {
			showInConsle(4, "[LifelineName]=" + ms.getName() + " [Source]="
					+ ms.getSource() + " [Target]=" + ms.getTarget()
					+ " [Operation]=" + ms.getOperation()
					+ " [ReturnValueVariable]=" + ms.getReturnValueVariable()
					+ " [Guard]=" + ms.getGuard() + " [Index]=" + ms.getIndex()
					+ " [Activator]=" + ms.getActivator() + " [Predecessor]="
					+ ms.getPredecessor() + " [Successor]=" + ms.getSuccessor());
		}
	}

	private static void printDiagramElements(IDiagram dgm)
			throws InvalidUsingException {
		printDiagramPresentations(dgm);
	}

	private static void printDiagramPresentations(IDiagram dgm)
			throws InvalidUsingException {
		IPresentation[] presentations = dgm.getPresentations();
		for (IPresentation p : presentations) {
			StringBuilder str = new StringBuilder();
			if (p instanceof INodePresentation) {
				str.append("[INodePresentation]=" + p.getLabel() + " [Type]="
						+ p.getType());
			} else if (p instanceof ILinkPresentation) {
				str.append("[ILinkPresentation]=" + p.getLabel() + " [Type]="
						+ p.getType() + " [Source]="
						+ ((ILinkPresentation) p).getSource().getLabel()
						+ " [Target]"
						+ ((ILinkPresentation) p).getTarget().getLabel());
			}
			if (p.getModel() != null) {
				str.append(" [Model("
						+ ((INamedElement) p.getModel()).getClass()
								.getInterfaces()[0].getSimpleName() + ")]="
						+ ((INamedElement) p.getModel()).getFullName("::"));
			} else {
				str.append(" [Model]=null");
			}
			showInConsle(3, str.toString());
		}
	}
}
