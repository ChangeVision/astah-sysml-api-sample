import com.change_vision.jude.api.inf.AstahAPI;

import com.change_vision.jude.api.inf.model.IAssociation;
import com.change_vision.jude.api.inf.model.IAttribute;
import com.change_vision.jude.api.inf.model.IBindingConnector;
import com.change_vision.jude.api.inf.model.IBlock;
import com.change_vision.jude.api.inf.model.IClass;
import com.change_vision.jude.api.inf.model.IConnector;
import com.change_vision.jude.api.inf.model.IConstraintBlock;
import com.change_vision.jude.api.inf.model.IConstraintParameter;
import com.change_vision.jude.api.inf.model.IConstraintProperty;
import com.change_vision.jude.api.inf.model.IDependency;
import com.change_vision.jude.api.inf.model.IDiagram;
import com.change_vision.jude.api.inf.model.IElement;
import com.change_vision.jude.api.inf.model.IFlowProperty;
import com.change_vision.jude.api.inf.model.IInterfaceBlock;
import com.change_vision.jude.api.inf.model.INamedElement;
import com.change_vision.jude.api.inf.model.IPackage;
import com.change_vision.jude.api.inf.model.IPort;
import com.change_vision.jude.api.inf.model.IRequirement;
import com.change_vision.jude.api.inf.model.ITestCase;
import com.change_vision.jude.api.inf.model.IUnit;
import com.change_vision.jude.api.inf.model.IUseCase;
import com.change_vision.jude.api.inf.model.IValueAttribute;
import com.change_vision.jude.api.inf.model.IValueProperty;
import com.change_vision.jude.api.inf.model.IValueType;
import com.change_vision.jude.api.inf.model.IValueTypeProperty;
import com.change_vision.jude.api.inf.presentation.ILinkPresentation;
import com.change_vision.jude.api.inf.presentation.INodePresentation;
import com.change_vision.jude.api.inf.presentation.IPresentation;
import com.change_vision.jude.api.inf.project.ModelFinder;
import com.change_vision.jude.api.inf.project.ProjectAccessor;


public class AstahUtil {
	
	private static ProjectAccessor projectAccessor;
	
	public static ProjectAccessor getProjectAccessor() {
		if (projectAccessor != null) {
			return projectAccessor;
		}
		try {
			projectAccessor = AstahAPI.getAstahAPI().getProjectAccessor();
			return projectAccessor;
		} catch (Exception e) {
			throw new APIException(e);
		}
	}
	
	public static boolean isSpecifiedStereotype(INamedElement model, String stereotype) {
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
	
    public static boolean isPackage(INamedElement model) {
    	if (model instanceof IPackage) {
    		return true;
    	}
    	return false;
    }
    
    public static boolean isAssociation(INamedElement model) {
    	if (model instanceof IAssociation) {
    		return true;
    	}
    	return false;
    }
    
    public static boolean isConnector(INamedElement model) {
    	if (model instanceof IConnector) {
    		return true;
    	}
    	return false;
    }
    
    public static boolean isBindingConnector(INamedElement model) {
    	if (model instanceof IBindingConnector) {
    		return true;
    	}
    	return false;
    }
    
    public static boolean isDeriveReqtDependency(INamedElement model) {
    	if (model instanceof IDependency) {
    		return isSpecifiedStereotype(model, "deriveReqt");
    	}
    	return false;
    }
    
    public static boolean isVerifyDependency(INamedElement model) {
    	if (model instanceof IDependency) {
    		return isSpecifiedStereotype(model, "verify");
    	}
    	return false;
    }
    
    public static boolean isSatisfyDependency(INamedElement model) {
    	if (model instanceof IDependency) {
    		return isSpecifiedStereotype(model, "satisfy");
    	}
    	return false;
    }
    
    public static boolean isRefineDependency(INamedElement model) {
    	if (model instanceof IDependency) {
    		return isSpecifiedStereotype(model, "refine");
    	}
    	return false;
    }
    
    public static boolean isTraceDependency(INamedElement model) {
    	if (model instanceof IDependency) {
    		return isSpecifiedStereotype(model, "trace");
    	}
    	return false;
    }
    
    public static boolean isCopyDependency(INamedElement model) {
    	if (model instanceof IDependency) {
    		return isSpecifiedStereotype(model, "copy");
    	}
    	return false;
    }
    
    public static boolean isAllocateDependency(INamedElement model) {
    	if (model instanceof IDependency) {
    		return isSpecifiedStereotype(model, "allocate");
    	}
    	return false;
    }
    
    public static boolean isConstraintProperty(INamedElement model) {
    	if (model instanceof IConstraintProperty) {
    		return true;
    	}
    	return false;
    }
    
    public static boolean isConstraintParameter(INamedElement model) {
    	if (model instanceof IConstraintParameter) {
    		return true;
    	}
    	return false;
    }
    
    public static boolean isPart(INamedElement model) {
    	if (model instanceof IAttribute 
    			&& model instanceof IConstraintParameter == false 
    			&& model instanceof IConstraintProperty == false
    			&& model instanceof IFlowProperty == false
    			&& model instanceof IPort == false
    			&& model instanceof IValueAttribute == false
    			&& model instanceof IValueProperty == false
    			&& model instanceof IValueTypeProperty == false
    			&& ((IAttribute)model).getOwner() != null
    			) {
    		if (!isTypeUndefined(((IAttribute)model))) {
        		IAssociation association = ((IAttribute)model).getAssociation(); 
        		if (association != null)  {
                    IAttribute[] memberEnds = association.getMemberEnds();
                    for (int i = 0; i < memberEnds.length; i++) {
                        if (memberEnds[i] != null && !memberEnds[i].equals(((IAttribute)model)) ){
                            if (memberEnds[i].isComposite() && "".equals(memberEnds[i].getName())) {
                            	return true;
                            }
                        }
                    }
        		}
    		}
    	}
    	return false;
    }
    
	public static boolean isTypeUndefined(IAttribute iAttribute) {
		String taggedValue = iAttribute.getTaggedValue("jude.is_type_unknown");
		if (taggedValue != null && taggedValue.equals("true")) {
			return true;
		} else {
			return false;
		}
	}
    
    public static boolean isReference(INamedElement model) {
    	if (model instanceof IAttribute 
    			&& model instanceof IConstraintParameter == false 
    			&& model instanceof IConstraintProperty == false
    			&& model instanceof IFlowProperty == false
    			&& model instanceof IPort == false
    			&& model instanceof IValueAttribute == false
    			&& model instanceof IValueProperty == false
    			&& model instanceof IValueTypeProperty == false
    			&& ((IAttribute)model).getOwner() != null
    			) {
    		if (!isTypeUndefined(((IAttribute)model))) {
        		IAssociation association = ((IAttribute)model).getAssociation(); 
        		if (association != null)  {
                    IAttribute[] memberEnds = association.getMemberEnds();
                    for (int i = 0; i < memberEnds.length; i++) {
                        if (memberEnds[i] != null && !memberEnds[i].equals(((IAttribute)model)) ){
                            if ("".equals(memberEnds[i].getName())) {
                            	return true;
                            }
                        }
                    }
        		}
    		}
    	}
    	return false;
    }
    
    public static boolean isRequirement(INamedElement model) {
    	if (model instanceof IRequirement) {
    		return true;
    	}
    	return false;
    }
    
    public static boolean isTestCase(INamedElement model) {
    	if (model instanceof ITestCase) {
    		return true;
    	}
    	return false;
    }
    
    public static boolean isBlock(INamedElement model) {
    	if (model instanceof IBlock) {
    		return true;
    	}
    	return false;
    }
    
    public static boolean isInterfaceBlock(INamedElement model) {
    	if (model instanceof IInterfaceBlock) {
    		return true;
    	}
    	return false;
    }
    
    public static boolean isConstraintBlock(INamedElement model) {
    	if (model instanceof IConstraintBlock) {
    		return true;
    	}
    	return false;
    }
    
    public static boolean isValueType(INamedElement model) {
    	if (model instanceof IValueType) {
    		return true;
    	}
    	return false;
    }
    
    public static boolean isUnit(INamedElement model) {
    	if (model instanceof IUnit) {
    		return true;
    	}
    	return false;
    }
	
    public static boolean isInterface(INamedElement model) {
    	if (model instanceof IClass) {
    		return isSpecifiedStereotype(model, "interface");
    	}
    	return false;
    }
    
    public static boolean isActor(INamedElement model) {
    	if (model instanceof IClass) {
    		return isSpecifiedStereotype(model, "actor");
    	}
    	return false;
    }
    
    public static boolean isUseCase(INamedElement model) {
    	if (model instanceof IUseCase) {
    		return true;
    	}
    	return false;
    }
    
	public static INamedElement findPackageByFullName(final String fullName) {
		try {
			// There are three methods to search from ProjectAccessor.
            // public INamedElement[] findElements(Class elementKind, String name) throws ProjectNotFoundException;
			// public abstract INamedElement[] findElements(Class elementKind) throws ProjectNotFoundException;
			// public INamedElement[] findElements(ModelFinder picker) throws ProjectNotFoundException;
			INamedElement[] results = projectAccessor.findElements(new ModelFinder() {
				public boolean isTarget(INamedElement namedElement) {
					return isPackage(namedElement)
							&& fullName.equals(((INamedElement)namedElement).getFullName("::"));
				}
			});
			return results.length > 0 ? results[0] : null;
		} catch (Exception e) {
			throw new APIException(e);
		}
	}
	
	public static INamedElement findAssociation(
			final IClass class1, 
			final IClass class2, 
			final String name, 
			final String sourceEndRollName,
			final String targetEndRollName) {
		try {
			// There are three methods to search from ProjectAccessor.
            // public INamedElement[] findElements(Class elementKind, String name) throws ProjectNotFoundException;
			// public abstract INamedElement[] findElements(Class elementKind) throws ProjectNotFoundException;
			// public INamedElement[] findElements(ModelFinder picker) throws ProjectNotFoundException;
			INamedElement[] results = projectAccessor.findElements(new ModelFinder() {
				public boolean isTarget(INamedElement namedElement) {
					if (isAssociation(namedElement)) {
						IAssociation ass = (IAssociation) namedElement;
						if (name.equals(((INamedElement)namedElement).getName())) {
							IAttribute[] memberEnds = ass.getMemberEnds();
							if (memberEnds[0].getName().equals(sourceEndRollName)
									&& memberEnds[1].getName().equals(targetEndRollName)
									&& memberEnds[0].getType().equals(class1)
									&& memberEnds[1].getType().equals(class2)) {
								return true;
							}
						}
					}
					return false;
				}
			});
			return results.length > 0 ? results[0] : null;
		} catch (Exception e) {
			throw new APIException(e);
		}
	}
	
	public static INamedElement findDeriveReqtDependency(
			final IRequirement source,
			final IRequirement target, 
			final String name) {
		try {
			// There are three methods to search from ProjectAccessor.
            // public INamedElement[] findElements(Class elementKind, String name) throws ProjectNotFoundException;
			// public abstract INamedElement[] findElements(Class elementKind) throws ProjectNotFoundException;
			// public INamedElement[] findElements(ModelFinder picker) throws ProjectNotFoundException;
			INamedElement[] results = projectAccessor.findElements(new ModelFinder() {
				public boolean isTarget(INamedElement namedElement) {
					if (isDeriveReqtDependency(namedElement)) {
						IDependency dep = (IDependency) namedElement;
						if (name.equals(dep.getName())) {
							INamedElement supplier = dep.getSupplier();
							INamedElement client = dep.getClient();
							if (supplier.equals(source)
									&& client.equals(target)) {
								return true;
							}
						}
					}
					return false;
				}
			});
			return results.length > 0 ? results[0] : null;
		} catch (Exception e) {
			throw new APIException(e);
		}
	}
	
	public static INamedElement findVerifyDependency(
			final ITestCase source,
			final IRequirement target, 
			final String name) {
		try {
			// There are three methods to search from ProjectAccessor.
            // public INamedElement[] findElements(Class elementKind, String name) throws ProjectNotFoundException;
			// public abstract INamedElement[] findElements(Class elementKind) throws ProjectNotFoundException;
			// public INamedElement[] findElements(ModelFinder picker) throws ProjectNotFoundException;
			INamedElement[] results = projectAccessor.findElements(new ModelFinder() {
				public boolean isTarget(INamedElement namedElement) {
					if (isVerifyDependency(namedElement)) {
						IDependency dep = (IDependency) namedElement;
						if (name.equals(dep.getName())) {
							INamedElement supplier = dep.getSupplier();
							INamedElement client = dep.getClient();
							if (supplier.equals(target)
									&& client.equals(source)) {
								return true;
							}
						}
					}
					return false;
				}
			});
			return results.length > 0 ? results[0] : null;
		} catch (Exception e) {
			throw new APIException(e);
		}
	}
	
	public static INamedElement findSatisfyDependency(
			final INamedElement source,
			final IRequirement target, 
			final String name) {
		try {
			// There are three methods to search from ProjectAccessor.
            // public INamedElement[] findElements(Class elementKind, String name) throws ProjectNotFoundException;
			// public abstract INamedElement[] findElements(Class elementKind) throws ProjectNotFoundException;
			// public INamedElement[] findElements(ModelFinder picker) throws ProjectNotFoundException;
			INamedElement[] results = projectAccessor.findElements(new ModelFinder() {
				public boolean isTarget(INamedElement namedElement) {
					if (isSatisfyDependency(namedElement)) {
						IDependency dep = (IDependency) namedElement;
						if (name.equals(dep.getName())) {
							INamedElement supplier = dep.getSupplier();
							INamedElement client = dep.getClient();
							if (supplier.equals(target)
									&& client.equals(source)) {
								return true;
							}
						}
					}
					return false;
				}
			});
			return results.length > 0 ? results[0] : null;
		} catch (Exception e) {
			throw new APIException(e);
		}
	}
	
	public static INamedElement findRefineDependency(
			final INamedElement source,
			final IRequirement target, 
			final String name) {
		try {
			// There are three methods to search from ProjectAccessor.
            // public INamedElement[] findElements(Class elementKind, String name) throws ProjectNotFoundException;
			// public abstract INamedElement[] findElements(Class elementKind) throws ProjectNotFoundException;
			// public INamedElement[] findElements(ModelFinder picker) throws ProjectNotFoundException;
			INamedElement[] results = projectAccessor.findElements(new ModelFinder() {
				public boolean isTarget(INamedElement namedElement) {
					if (isRefineDependency(namedElement)) {
						IDependency dep = (IDependency) namedElement;
						if (name.equals(dep.getName())) {
							INamedElement supplier = dep.getSupplier();
							INamedElement client = dep.getClient();
							if (supplier.equals(target)
									&& client.equals(source)) {
								return true;
							}
						}
					}
					return false;
				}
			});
			return results.length > 0 ? results[0] : null;
		} catch (Exception e) {
			throw new APIException(e);
		}
	}
	
	public static INamedElement findTraceDependency(
			final IRequirement source,
			final IRequirement target, 
			final String name) {
		try {
			// There are three methods to search from ProjectAccessor.
            // public INamedElement[] findElements(Class elementKind, String name) throws ProjectNotFoundException;
			// public abstract INamedElement[] findElements(Class elementKind) throws ProjectNotFoundException;
			// public INamedElement[] findElements(ModelFinder picker) throws ProjectNotFoundException;
			INamedElement[] results = projectAccessor.findElements(new ModelFinder() {
				public boolean isTarget(INamedElement namedElement) {
					if (isTraceDependency(namedElement)) {
						IDependency dep = (IDependency) namedElement;
						if (name.equals(dep.getName())) {
							INamedElement supplier = dep.getSupplier();
							INamedElement client = dep.getClient();
							if (supplier.equals(source)
									&& client.equals(target)) {
								return true;
							}
						}
					}
					return false;
				}
			});
			return results.length > 0 ? results[0] : null;
		} catch (Exception e) {
			throw new APIException(e);
		}
	}
	
	public static INamedElement findCopyDependency(
			final IRequirement source,
			final IRequirement target, 
			final String name) {
		try {
			// There are three methods to search from ProjectAccessor.
            // public INamedElement[] findElements(Class elementKind, String name) throws ProjectNotFoundException;
			// public abstract INamedElement[] findElements(Class elementKind) throws ProjectNotFoundException;
			// public INamedElement[] findElements(ModelFinder picker) throws ProjectNotFoundException;
			INamedElement[] results = projectAccessor.findElements(new ModelFinder() {
				public boolean isTarget(INamedElement namedElement) {
					if (isCopyDependency(namedElement)) {
						IDependency dep = (IDependency) namedElement;
						if (name.equals(dep.getName())) {
							INamedElement supplier = dep.getSupplier();
							INamedElement client = dep.getClient();
							if (supplier.equals(source)
									&& client.equals(target)) {
								return true;
							}
						}
					}
					return false;
				}
			});
			return results.length > 0 ? results[0] : null;
		} catch (Exception e) {
			throw new APIException(e);
		}
	}
	
	public static INamedElement findAllocateDependency(
			final IRequirement source,
			final IRequirement target, 
			final String name) {
		try {
			// There are three methods to search from ProjectAccessor.
            // public INamedElement[] findElements(Class elementKind, String name) throws ProjectNotFoundException;
			// public abstract INamedElement[] findElements(Class elementKind) throws ProjectNotFoundException;
			// public INamedElement[] findElements(ModelFinder picker) throws ProjectNotFoundException;
			INamedElement[] results = projectAccessor.findElements(new ModelFinder() {
				public boolean isTarget(INamedElement namedElement) {
					if (isAllocateDependency(namedElement)) {
						IDependency dep = (IDependency) namedElement;
						if (name.equals(dep.getName())) {
							INamedElement supplier = dep.getSupplier();
							INamedElement client = dep.getClient();
							if (supplier.equals(source)
									&& client.equals(target)) {
								return true;
							}
						}
					}
					return false;
				}
			});
			return results.length > 0 ? results[0] : null;
		} catch (Exception e) {
			throw new APIException(e);
		}
	}
	
	public static INamedElement findConstraintParameter(
			final IClass parentBlock, 
			final String name,
			final IClass type) {
		try {
			// There are three methods to search from ProjectAccessor.
            // public INamedElement[] findElements(Class elementKind, String name) throws ProjectNotFoundException;
			// public abstract INamedElement[] findElements(Class elementKind) throws ProjectNotFoundException;
			// public INamedElement[] findElements(ModelFinder picker) throws ProjectNotFoundException;
			INamedElement[] results = projectAccessor.findElements(new ModelFinder() {
				public boolean isTarget(INamedElement namedElement) {
					if (isConstraintParameter(namedElement)) {
						IConstraintParameter cp = (IConstraintParameter) namedElement;
						if (name.equals(cp.getName())) {
							IElement owner = cp.getOwner();
							IClass type2 = cp.getType();
							if (parentBlock.equals(owner)
									&& type.equals(type2)) {
								return true;
							}
						}
					}
					return false;
				}
			});
			return results.length > 0 ? results[0] : null;
		} catch (Exception e) {
			throw new APIException(e);
		}
	}
	
	public static INamedElement findConstraintProperty(
			final IClass parentBlock, 
			final String name,
			final IClass type) {
		try {
			// There are three methods to search from ProjectAccessor.
            // public INamedElement[] findElements(Class elementKind, String name) throws ProjectNotFoundException;
			// public abstract INamedElement[] findElements(Class elementKind) throws ProjectNotFoundException;
			// public INamedElement[] findElements(ModelFinder picker) throws ProjectNotFoundException;
			INamedElement[] results = projectAccessor.findElements(new ModelFinder() {
				public boolean isTarget(INamedElement namedElement) {
					if (isConstraintProperty(namedElement)) {
						IConstraintProperty cp = (IConstraintProperty) namedElement;
						if (name.equals(cp.getName())) {
							IElement owner = cp.getOwner();
							IClass type2 = cp.getType();
							if (parentBlock.equals(owner)
									&& type.equals(type2)) {
								return true;
							}
						}
					}
					return false;
				}
			});
			return results.length > 0 ? results[0] : null;
		} catch (Exception e) {
			throw new APIException(e);
		}
	}
	
	public static INamedElement findPart(
			final IClass parentBlock, 
			final String name,
			final IBlock type) {
		try {
			// There are three methods to search from ProjectAccessor.
            // public INamedElement[] findElements(Class elementKind, String name) throws ProjectNotFoundException;
			// public abstract INamedElement[] findElements(Class elementKind) throws ProjectNotFoundException;
			// public INamedElement[] findElements(ModelFinder picker) throws ProjectNotFoundException;
			INamedElement[] results = projectAccessor.findElements(new ModelFinder() {
				public boolean isTarget(INamedElement namedElement) {
					if (isPart(namedElement)) {
						IAttribute cp = (IAttribute) namedElement;
						if (name.equals(cp.getName())) {
							IElement owner = cp.getOwner();
							IClass type2 = cp.getType();
							if (parentBlock.equals(owner)
									&& type.equals(type2)) {
								return true;
							}
						}
					}
					return false;
				}
			});
			return results.length > 0 ? results[0] : null;
		} catch (Exception e) {
			throw new APIException(e);
		}
	}
	
	public static INamedElement findReference(
			final IClass parentBlock, 
			final String name,
			final IBlock type) {
		try {
			// There are three methods to search from ProjectAccessor.
            // public INamedElement[] findElements(Class elementKind, String name) throws ProjectNotFoundException;
			// public abstract INamedElement[] findElements(Class elementKind) throws ProjectNotFoundException;
			// public INamedElement[] findElements(ModelFinder picker) throws ProjectNotFoundException;
			INamedElement[] results = projectAccessor.findElements(new ModelFinder() {
				public boolean isTarget(INamedElement namedElement) {
					if (isReference(namedElement)) {
						IAttribute cp = (IAttribute) namedElement;
						if (name.equals(cp.getName())) {
							IElement owner = cp.getOwner();
							IClass type2 = cp.getType();
							if (parentBlock.equals(owner)
									&& type.equals(type2)) {
								return true;
							}
						}
					}
					return false;
				}
			});
			return results.length > 0 ? results[0] : null;
		} catch (Exception e) {
			throw new APIException(e);
		}
	}
	
	public static INamedElement findRequirementByFullName(final String fullName) {
		try {
			// There are three methods to search from ProjectAccessor.
            // public INamedElement[] findElements(Class elementKind, String name) throws ProjectNotFoundException;
			// public abstract INamedElement[] findElements(Class elementKind) throws ProjectNotFoundException;
			// public INamedElement[] findElements(ModelFinder picker) throws ProjectNotFoundException;
			INamedElement[] results = projectAccessor.findElements(new ModelFinder() {
				public boolean isTarget(INamedElement namedElement) {
					return isRequirement(namedElement)
							&& fullName.equals(((INamedElement)namedElement).getFullName("::"));
				}
			});
			return results.length > 0 ? results[0] : null;
		} catch (Exception e) {
			throw new APIException(e);
		}
	}
	
	public static INamedElement findTestCaseByFullName(final String fullName) {
		try {
			// There are three methods to search from ProjectAccessor.
            // public INamedElement[] findElements(Class elementKind, String name) throws ProjectNotFoundException;
			// public abstract INamedElement[] findElements(Class elementKind) throws ProjectNotFoundException;
			// public INamedElement[] findElements(ModelFinder picker) throws ProjectNotFoundException;
			INamedElement[] results = projectAccessor.findElements(new ModelFinder() {
				public boolean isTarget(INamedElement namedElement) {
					return isTestCase(namedElement)
							&& fullName.equals(((INamedElement)namedElement).getFullName("::"));
				}
			});
			return results.length > 0 ? results[0] : null;
		} catch (Exception e) {
			throw new APIException(e);
		}
	}
	
	public static INamedElement findBlockByFullName(final String fullName) {
		try {
			// There are three methods to search from ProjectAccessor.
            // public INamedElement[] findElements(Class elementKind, String name) throws ProjectNotFoundException;
			// public abstract INamedElement[] findElements(Class elementKind) throws ProjectNotFoundException;
			// public INamedElement[] findElements(ModelFinder picker) throws ProjectNotFoundException;
			INamedElement[] results = projectAccessor.findElements(new ModelFinder() {
				public boolean isTarget(INamedElement namedElement) {
					return isBlock(namedElement)
							&& fullName.equals(((INamedElement)namedElement).getFullName("::"));
				}
			});
			return results.length > 0 ? results[0] : null;
		} catch (Exception e) {
			throw new APIException(e);
		}
	}
	
	public static INamedElement findInterfaceBlockByFullName(final String fullName) {
		try {
			// There are three methods to search from ProjectAccessor.
            // public INamedElement[] findElements(Class elementKind, String name) throws ProjectNotFoundException;
			// public abstract INamedElement[] findElements(Class elementKind) throws ProjectNotFoundException;
			// public INamedElement[] findElements(ModelFinder picker) throws ProjectNotFoundException;
			INamedElement[] results = projectAccessor.findElements(new ModelFinder() {
				public boolean isTarget(INamedElement namedElement) {
					return isInterfaceBlock(namedElement)
							&& fullName.equals(((INamedElement)namedElement).getFullName("::"));
				}
			});
			return results.length > 0 ? results[0] : null;
		} catch (Exception e) {
			throw new APIException(e);
		}
	}
	
	public static INamedElement findConstraintBlockByFullName(final String fullName) {
		try {
			// There are three methods to search from ProjectAccessor.
            // public INamedElement[] findElements(Class elementKind, String name) throws ProjectNotFoundException;
			// public abstract INamedElement[] findElements(Class elementKind) throws ProjectNotFoundException;
			// public INamedElement[] findElements(ModelFinder picker) throws ProjectNotFoundException;
			INamedElement[] results = projectAccessor.findElements(new ModelFinder() {
				public boolean isTarget(INamedElement namedElement) {
					return isConstraintBlock(namedElement)
							&& fullName.equals(((INamedElement)namedElement).getFullName("::"));
				}
			});
			return results.length > 0 ? results[0] : null;
		} catch (Exception e) {
			throw new APIException(e);
		}
	}
	
	public static INamedElement findValueTypeByFullName(final String fullName) {
		try {
			// There are three methods to search from ProjectAccessor.
            // public INamedElement[] findElements(Class elementKind, String name) throws ProjectNotFoundException;
			// public abstract INamedElement[] findElements(Class elementKind) throws ProjectNotFoundException;
			// public INamedElement[] findElements(ModelFinder picker) throws ProjectNotFoundException;
			INamedElement[] results = projectAccessor.findElements(new ModelFinder() {
				public boolean isTarget(INamedElement namedElement) {
					return isValueType(namedElement)
							&& fullName.equals(((INamedElement)namedElement).getFullName("::"));
				}
			});
			return results.length > 0 ? results[0] : null;
		} catch (Exception e) {
			throw new APIException(e);
		}
	}
	
	public static INamedElement findUnitByFullName(final String fullName) {
		try {
			// There are three methods to search from ProjectAccessor.
            // public INamedElement[] findElements(Class elementKind, String name) throws ProjectNotFoundException;
			// public abstract INamedElement[] findElements(Class elementKind) throws ProjectNotFoundException;
			// public INamedElement[] findElements(ModelFinder picker) throws ProjectNotFoundException;
			INamedElement[] results = projectAccessor.findElements(new ModelFinder() {
				public boolean isTarget(INamedElement namedElement) {
					return isUnit(namedElement)
							&& fullName.equals(((INamedElement)namedElement).getFullName("::"));
				}
			});
			return results.length > 0 ? results[0] : null;
		} catch (Exception e) {
			throw new APIException(e);
		}
	}
	
	public static INamedElement findActorByFullName(final String fullName) {
		try {
			// There are three methods to search from ProjectAccessor.
            // public INamedElement[] findElements(Class elementKind, String name) throws ProjectNotFoundException;
			// public abstract INamedElement[] findElements(Class elementKind) throws ProjectNotFoundException;
			// public INamedElement[] findElements(ModelFinder picker) throws ProjectNotFoundException;
			INamedElement[] results = projectAccessor.findElements(new ModelFinder() {
				public boolean isTarget(INamedElement namedElement) {
					return isActor(namedElement)
							&& fullName.equals(((INamedElement)namedElement).getFullName("::"));
				}
			});
			return results.length > 0 ? results[0] : null;
		} catch (Exception e) {
			throw new APIException(e);
		}
	}
	
	public static INamedElement findUseCaseByFullName(final String fullName) {
		try {
			// There are three methods to search from ProjectAccessor.
            // public INamedElement[] findElements(Class elementKind, String name) throws ProjectNotFoundException;
			// public abstract INamedElement[] findElements(Class elementKind) throws ProjectNotFoundException;
			// public INamedElement[] findElements(ModelFinder picker) throws ProjectNotFoundException;
			INamedElement[] results = projectAccessor.findElements(new ModelFinder() {
				public boolean isTarget(INamedElement namedElement) {
					return isUseCase(namedElement)
							&& fullName.equals(((INamedElement)namedElement).getFullName("::"));
				}
			});
			return results.length > 0 ? results[0] : null;
		} catch (Exception e) {
			throw new APIException(e);
		}
	}
	
	public static INodePresentation getPartPresentationByLabel(IDiagram dgm, String label) {
		try {
			for( IPresentation p : dgm.getPresentations()) {
				if (p instanceof INodePresentation && p.getType().equals("Part") 
						&& ((INodePresentation)p).getLabel().equals(label)) {
					return (INodePresentation)p;
				}
			}
			return null;
		} catch (Exception e) {
			throw new APIException(e);
		}
	}
	
	public static INodePresentation getPortPresentation(IDiagram dgm, String name, String typeFullName) {
		try {
			for( IPresentation p : dgm.getPresentations()) {
				if (p instanceof INodePresentation && p.getType().equals("Port") 
						&& ((INodePresentation)p).getLabel().equals(name)
						&& ((IPort)p.getModel()).getType().getFullName("::").equals(typeFullName)) {
					return (INodePresentation)p;
				}
			}
			return null;
		} catch (Exception e) {
			throw new APIException(e);
		}
	}
	
	public static ILinkPresentation getConnectorPresentationByName(IDiagram dgm, String name) {
		try {
			for( IPresentation p : dgm.getPresentations()) {
				if (p.getModel() instanceof IConnector 
						&& ((IConnector)p.getModel()).getName().equals(name)) {
					return (ILinkPresentation)p;
				}
			}
			return null;
		} catch (Exception e) {
			throw new APIException(e);
		}
	}
}
