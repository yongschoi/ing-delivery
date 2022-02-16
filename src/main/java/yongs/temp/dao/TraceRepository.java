package yongs.temp.dao;

import org.springframework.data.mongodb.repository.MongoRepository;

import yongs.temp.model.Trace;

public interface TraceRepository extends MongoRepository<Trace, String> {
	public Trace findByNo(final long no);
	public Trace deleteByNo(final long no);
}
