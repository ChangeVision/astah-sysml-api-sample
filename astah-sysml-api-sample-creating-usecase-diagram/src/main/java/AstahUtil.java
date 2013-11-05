import com.change_vision.jude.api.inf.AstahAPI;
import com.change_vision.jude.api.inf.model.IAssociation;
import com.change_vision.jude.api.inf.model.IAttribute;
import com.change_vision.jude.api.inf.model.IClass;
import com.change_vision.jude.api.inf.model.INamedElement;
import com.change_vision.jude.api.inf.model.IPackage;
import com.change_vision.jude.api.inf.model.IUseCase;
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
	
	public static INamedElement findActorByFullName(final String fullName) {
		try {
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
}
