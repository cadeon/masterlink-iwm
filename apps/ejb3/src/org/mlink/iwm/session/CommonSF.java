/*-----------------------------------------------------------------------------------
	File: ImplementationSFRemote.java
	Package: org.mlink.iwm.session
---------------------------------------------------------------------------------------*/

package org.mlink.iwm.session;
import java.util.Collection;
import java.util.Map;

import org.mlink.iwm.entity3.BaseEntity;

public interface CommonSF {
	public String echo(String str);
	public long create(BaseEntity object);
	public <K> K get(Class<K> clazz, long id);
	public void remove(BaseEntity object);
	public void update(BaseEntity object);
	
	public <K> Collection<K> getData(Class clazz, String fieldName, Object fieldValue);
	public <K> Collection<K> getData(Class clazz, String addnwhereClause, String fieldName, Object fieldValue);
	
	public <K> Collection<K> getData(Class clazz, String addnwhereClause, Map<String, Object> fieldNameValueMap);
	public <K> K getUniqueData(Collection<K> lst) throws Exception;

	public <K> Collection<K> findAll(Class<K> clazz);
}