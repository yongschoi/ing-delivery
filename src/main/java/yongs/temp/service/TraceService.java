package yongs.temp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import yongs.temp.dao.TraceRepository;
import yongs.temp.model.Trace;

@Slf4j
@Service
public class TraceService {
	@Autowired
    TraceRepository repo;
	
	public void setStatus(Trace trace) {
		log.debug("no: " + trace.getNo() + ", status: " + trace.getStatus());
		Trace savedTrace = repo.findByNo(trace.getNo());
		savedTrace.setStatus(trace.getStatus());
		repo.save(savedTrace);
	}
	
	public Trace findByNo(long no) {
		log.debug("no: " + no );
		return repo.findByNo(no);
	}
}
