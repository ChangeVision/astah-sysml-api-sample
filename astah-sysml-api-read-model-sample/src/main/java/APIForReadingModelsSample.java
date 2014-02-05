import java.io.IOException;
import java.io.InputStream;

import com.change_vision.jude.api.inf.AstahAPI;
import com.change_vision.jude.api.inf.exception.LicenseNotFoundException;
import com.change_vision.jude.api.inf.exception.NonCompatibleException;
import com.change_vision.jude.api.inf.exception.ProjectLockedException;
import com.change_vision.jude.api.inf.exception.ProjectNotFoundException;
import com.change_vision.jude.api.inf.model.IBlock;
import com.change_vision.jude.api.inf.model.IModel;
import com.change_vision.jude.api.inf.model.INamedElement;
import com.change_vision.jude.api.inf.model.IOperation;
import com.change_vision.jude.api.inf.model.IPackage;
import com.change_vision.jude.api.inf.project.ProjectAccessor;

/**
 * Sample source codes for obtaining package and block information of Astah SysML models. 
 * Output package, blocks and operations in console.
 */
public class APIForReadingModelsSample {
	
	private static final String PROJECT_PATH = "SampleModel.asml";

    public static void main(String[] args) {
        try {
            System.out.println("Opening project...");

            ProjectAccessor prjAccessor = AstahAPI.getAstahAPI().getProjectAccessor();
    		InputStream astahFileStream = APIForReadingModelsSample.class.getResourceAsStream(PROJECT_PATH);    

            // Open a project
            // Please don't forget to close the project
            prjAccessor.open(astahFileStream);
            System.out.println("Printing the elements...");

            // Get a project model. 
            // Astah SysML model is tree structure. The project model is the root.
            IModel project = prjAccessor.getProject();

            // Get all of packages and blocks in the project
            printPackageInfo(project);

            // Close the project
            prjAccessor.close();

            System.out.println("Finished");

        } catch (LicenseNotFoundException e) {
            e.printStackTrace();
        } catch (ProjectNotFoundException e) {
            e.printStackTrace();
        } catch (ProjectLockedException e) {
        	// If the target project is being used, ProjectLoctedException will be thrown
            e.printStackTrace();
        } catch (NonCompatibleException e) {
        	// If Astah SysML model is read by Astah Professional, NonCompatibleException will be thrown
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }
	
    private static void printPackageInfo(IPackage iPackage) {
        // Display a package name
        System.out.println("Package name: " + iPackage.getName());
        // Diaplay a package definition
        System.out.println("Package definition: " + iPackage.getDefinition());

        // Display packages and blocks
        INamedElement[] iNamedElements = iPackage.getOwnedElements();
        for (int i = 0; i < iNamedElements.length; i++) {
            if (iNamedElements[i] instanceof IPackage) {
                IPackage iChildPackage = (IPackage) iNamedElements[i];
                // Display a package
                printPackageInfo(iChildPackage);
            } else if (iNamedElements[i] instanceof IBlock) {
            	IBlock iBlock = (IBlock) iNamedElements[i];
                // Display a iBlock
                printBlockInfo(iBlock);
            }
        }
    }

    private static void printBlockInfo(IBlock iBlock) {
        // Display a block name
        System.out.println("block name: " + iBlock.getName());
        // Display a block definition
        System.out.println("block definition: " + iBlock.getDefinition());

        // Display all operations
        IOperation[] iOperations = iBlock.getOperations();
        for (int i = 0; i < iOperations.length; i++) {
            printOperationInfo(iOperations[i]);
        }
    }

    private static void printOperationInfo(IOperation iOperation) {
        // Display an operation name
        System.out.println("Operation name: " + iOperation.getName());
        // Display a return type of operation
        System.out.println("Operation returnType: " + iOperation.getReturnTypeExpression());
        // Display an operation definition
        System.out.println("Operation definition: " + iOperation.getDefinition());
    }
}

