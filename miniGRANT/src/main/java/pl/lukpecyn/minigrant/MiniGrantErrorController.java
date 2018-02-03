package pl.lukpecyn.minigrant;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.web.ErrorAttributes;
import org.springframework.boot.autoconfigure.web.ErrorController;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.ServletRequestAttributes;

public class MiniGrantErrorController implements ErrorController {

	@Value("${app.version}")
	public String appVersion;
	@Value("${app.name}")
	public String appName;

	private static final Logger logger = LoggerFactory.getLogger(ErrorController.class);
	
	@Autowired
    private ErrorAttributes errorAttributes;

    private static final String PATH = "/error";

    @RequestMapping(value = PATH)
    public String error(Model model, HttpServletRequest request, HttpServletResponse response) {    	
    	
    	String e = Integer.toString(response.getStatus());
    	Map<String, Object> errorAttributes = getErrorAttributes(request,true);
    	
    	model.addAllAttributes(errorAttributes);
		model.addAttribute("appVersion", appVersion);
		model.addAttribute("appName", appName);
    	System.out.println(appName + " - " + appVersion);
    	
    	logger.error(getErrorAttributes(request,true).toString());
    	
        //return "Error handling: " + "<p>" + errorAttributes.get("path") + "</p><p>" + errorAttributes.get("status") + "</p><p>" + errorAttributes.get("error") + "</p><p>" + errorAttributes.get("exception") + "</p><p><code>" + errorAttributes.get("message") + "</code></p><p><code>" + errorAttributes.get("trace") + "</code></p>";
    	return "error";
    }

    @Override
    public String getErrorPath() {
        System.out.println(PATH);
    	return PATH;
    }
    
    private Map<String, Object> getErrorAttributes(HttpServletRequest request, boolean includeStackTrace) {
        RequestAttributes requestAttributes = new ServletRequestAttributes(request);
        return errorAttributes.getErrorAttributes(requestAttributes, includeStackTrace);
    }
}
