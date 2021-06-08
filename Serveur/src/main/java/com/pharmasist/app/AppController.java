package com.pharmasist.app;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class AppController {

	@RequestMapping(path="/CheckConnection")
	public @ResponseBody String checkConnection() {
		return "{\"response\":0}";
	}
}
