package yongs.temp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;
import yongs.temp.model.Trace;
import yongs.temp.service.TraceService;

@Slf4j
@RestController
@RequestMapping("/trace")
public class TraceController {
	@Autowired
	TraceService traceService;
	
	@GetMapping("/no/{no}")
	public Trace findByNo(@PathVariable("no") long no) throws Exception {
		log.debug("TraceController.findByNo()");
		return traceService.findByNo(no);
	}
}
